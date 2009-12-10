/*
 * Copyright 2007:
 * Per Fredriksson <perfr775@student.liu.se>
 * David Karlslätt <davka417@student.liu.se>
 * Tor Knutsson	<torkn754@student.liu.se>
 * Daniel Källming <danka053@student.liu.se>
 * Ted Palmgren <tedpa175@student.liu.se>
 * Freddie Pintar <frepi150@student.liu.se>
 * Mårten Thurén <marth852@student.liu.se> 
 * 
 * This file is part of reMIND.
 *
 * reMIND is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * reMIND is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with reMIND; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package mind.io;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import mind.model.FunctionControl;
import mind.model.ID;
import mind.model.Model;
import mind.model.Node;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * EventHandler for the SAX parsing of Excel 2003 XML files
 * added Pum5 2007
 * 
 * @author Per Fredriksson
 * @author David Karlslätt
 * @version 2007-11-28
 */
public class ExmlSAXHandler extends DefaultHandler {

	// Variables for temporary storage

	private HashMap hash;
	private String currentNodeLabel;
	public String charStr, oldStr;
	private Boolean inFunction;
	public FunctionControl functionController;
	public String currentFunction;
	private String currentFunctionLabel;
	public Model model;
	public Node currentNode;
	public ID currentNodeID;
	public LinkedList<Object> list;
	public int timeStepCounter, timeStepRoof, costFuncRoof;
	private String currentFlowType;
	public boolean parsingFunctionEditor;
	private String currentFlow;
	private int FErowCount;

	
	/**
	 * Constructor.
	 * Added by PUM5 2007-11-28
	 * @param m Provides the parser with a model to save to.
	 * @exception SAXException If the model has 0 nodes. 
	 * */
	public ExmlSAXHandler(Model m) throws SAXException 
	{
		model = m;
		if (!m.getAllNodes().elements().hasMoreElements())
			throw new SAXException("Unable to import to model without nodes");
	}

	/**
	 * Sets initial variable values.
	 * Added by PUM5 2007-11-28
	 * @exception SAXException Unknown possible cause. FIXME
	 * */
	public void startDocument() throws SAXException 
	{
		hash = new HashMap();
		inFunction = false;
		currentFunction = null;
		currentFunctionLabel = null;
		currentFlowType = "InFlows";
		currentNodeID = null;
		parsingFunctionEditor = false;
	}


	/**
	 * Updates the functioncontrollers of all nodes if no errors occured.
	 * Added by PUM5 2007-11-28
	 * @exception SAXException Unknown possible cause. FIXME
	 * */
	public void endDocument() throws SAXException 
	{

		for (int i = 0; i < hash.size(); i++) {
			((Node) model.getNode((ID) hash.keySet().toArray()[i]))
					.setFunctionController((FunctionControl) hash
							.get(((ID) hash.keySet().toArray()[i])));
		}
	}

	/**
	 * Runs at the start of each element.
	 * Added by PUM5 2007-11-28
	 * @param namespaceURI
	 * @param lName The local name.
	 * @param qName The qualified name.
	 * @param attrs Attributes of the element.
	 * @exception SAXException If it can't find correct ID from the label.
	 * */
	public void startElement(String namespaceURI, String lName, 
			String qName,
			Attributes attrs) throws SAXException {
		// if reached a new function which isn't the first in a nod pars and add
		// it
		if (oldStr != null) {
			if (oldStr.equals("Function Type") && currentFunction != null) {
				parseAndAddToFunctioncollection();
			}
		}
		charStr = null;
		// when reacing a new worksheet ie a new node a new functioncontroller
		// is needed
		if (qName == "Worksheet" || qName == "ss:Worksheet") {

			currentFunction = null;
			inFunction = false;

			// String name = attrs.getLocalName(0);
			String value = attrs.getValue(0);
			//System.out.println(value);
			// FIXME borde läsa nodLabel från sidan istället.
			currentNodeLabel = value;
			functionController = new FunctionControl();

			try {
				currentNodeID = getCorrectNodeID(currentNodeLabel);
			} catch (FileInteractionException e) {
				throw new SAXException(e);
			}
			currentNode = (Node) model.getNode(currentNodeID);
		}

	}

	/*
	 * 
	 * endElement is executed at every end tag its is here that we can read the
	 * information between the tags with the oldStr and charStr variables oldStr
	 * keeps the value of the first Cell on every Row, charStr gets the value of
	 * every cell.
	 * 
	 * FIXME borde skrivas om så den blir lite mer överskådlig och förstålig
	 */

	/**
	 * Executes at every end tag it is here that we can read the
	 * information between the tags with the oldStr and charStr variables oldStr
	 * keeps the value of the first Cell on every Row, charStr gets the value of
	 * every cell.
	 * FIXME borde skrivas om så den blir lite mer överskådlig och förstålig
	 * Added by PUM5 2007-11-28
	 * @param namespaceURI
	 * @param lName The local name.
	 * @param qName The qualified name.
	 * @exception SAXException Various possible parse related causes.
	 * */
	public void endElement(String namespaceURI, String sName, String qName)
			throws SAXException {
		if (parsingFunctionEditor)
			parseTable(qName);
		// If a end of a node(worksheet)is reached
		else if (qName == "Worksheet" || qName == "ss:Worksheet") {

			if (currentFunction != null)
				parseAndAddToFunctioncollection();
			if (currentNodeID != null)
				hash.put(currentNodeID, functionController);
		}

		if (qName.equals("Row")) {
			oldStr = null;
			charStr = null;
			timeStepCounter = 1;
		}

		if (charStr != null) {
			if (!inFunction) {

				if (oldStr != null)

					if (isFunction(oldStr)) {
						list = new LinkedList<Object>();
						currentFunction = charStr;
						inFunction = true;
						timeStepCounter = 1;
						timeStepRoof = 0;
						costFuncRoof = 0;
						currentFlowType = "InFlows";
					}
				oldStr = null;
			}

			else if (charStr.toLowerCase().equals("outflows"))
				currentFlowType = "OutFlows";
			else if (oldStr == null)
				;
			else if (oldStr.contains("\n"))
				;
			else if (oldStr.equals("Label")) {

				Node tempNode;
				tempNode = (Node) model.getNode(currentNodeID);
				if (!tempNode.getAllFunctionLabels().contains(charStr))
					throw new SAXException("The node " + currentNode.getLabel()
							+ " does not contain function: " + currentFunction
							+ " with label " + charStr);
				list.addLast("label");
				list.addLast(charStr);
				currentFunctionLabel = charStr;
			} else {
				if (currentFunction.equals("Boundary")) {
					boundary();
				} else if (currentFunction.equals("BoundaryTOP")) {
					boundaryTOP();
				}
                                //Added by Nawzad Mardan 2008-06-01
                                else if(currentFunction.equals("StartStopEquation")) {
					startStopEquation();
                                }
                                else if (currentFunction.equals("FlowDependency")) {
					flowDependency();
				} else if (currentFunction.equals("FlowEquation")) {
					flowEquation();
				} else if (currentFunction.equals("BatchEquation")) {
					batchEquation();
				} else if (currentFunction.equals("FunctionEditor")) {
					if (!parsingFunctionEditor)
						functionEditor();
				} else if (currentFunction.equals("LogicalEquation")) {
					logicalEquation();
				} else if (currentFunction.equals("Destination")) {
					destination();
				} else if (currentFunction.equals("StorageEquation")) {
					storageEquation();
				} else if (currentFunction.equals("Source")) {
					source();
				} else if (currentFunction.equals("FlowRelation")) {
					flowRelation();
				} else if (currentFunction.equals("InvestmentCost")) {
					investmentCost();
				}

			}
			if (oldStr == null)
				oldStr = charStr;
			/*
			 * if (oldStr.contains("\n")) oldStr=oldStr.replace("\n", "");
			 * oldStr=oldStr.replace(" ", "");
			 */
			charStr = null;
		}
	}

	/**
	 * Handles boundary function elements.
	 * Added by PUM5 2007-11-28
	 * @exception SAXException. Various causes.
	 * */
	public void boundary() throws SAXException 
	{
            //Added by Nawzad Mardan 080910
            /*
             *xml += XML.indent(indent+1) + "<inflows>"  + (c_radin ? "true" : "false") + "</inflows>"  + XML.nl();
        // Added by Nawzad Mardan 080910
        xml += XML.indent(indent+1) + "<outflows>"  + (c_radout ? "true" : "false") + "</outflows>"  + XML.nl();
        sheet.addRow(sheet.addLockedCell("Inflows")+sheet.addCell((new Boolean(c_radin))));		
	sheet.addRow(sheet.addLockedCell("Outflows")+sheet.addCell((new Boolean(c_radout))));
             */
               if (oldStr.equals("Inflows")) 
                 {
                    list.add("inflows");
                    list.add("true");
			//addVariable("inflows");
                 } 
               //Added by Nawzad Mardan 080910
               else if (oldStr.equals("Outflows")) 
                 {
                    list.add("outflows");
                    list.add("true");
			//addVariable("outflows");
		} 
               else if (oldStr.equals("Time Step")) {
			addTimeStep();
		} else if (oldStr.equals("Min")) {
			try {
				addTimeStepVariable("min", !(0 < Float.valueOf(charStr)
						.floatValue()));
			} catch (Exception ex) {
				throw new SAXException("Unknown value" + charStr);
			}
		} else if (oldStr.equals("Max")) {
			try {
				addTimeStepVariable("max", !(0 < Float.valueOf(charStr)
						.floatValue()));
			} catch (Exception ex) {
				throw new SAXException(createErrorMessage("Unknown value" + charStr));
			}

		} else if (oldStr.equals("Resource")) {
			addVariable("resource.type");
		} else if (oldStr.contains("\n"))
			;
		else
			throw new SAXException(createErrorMessage("Unknow attribute: \""
					+ oldStr + "\""));

	}

	/**
	 * Handles boundaryTOP function elements.
	 * Added by PUM5 2007-11-28
	 * @exception SAXException. Various causes.
	 * */
	public void boundaryTOP() throws SAXException 
	{
            
               //Added by Nawzad Mardan 080910
               if (oldStr.equals("Inflows")) 
                 {
                    list.add("inflow");
                    list.add("true");
			//addVariable("inflows");
                 } 
               //Added by Nawzad Mardan 080910
               else if (oldStr.equals("Outflows")) 
                 {
                    list.add("outflow");
                    list.add("true");
			//addVariable("outflows");
		} 
               else if (oldStr.equals("Resource")) {
			addVariable("resource.type");
		} else if (oldStr.equals("Min")) {
			checkBoundaryTOP();
			addVariable("MinLim");
		} else if (oldStr.equals("Max")) {
			checkBoundaryTOP();
			addVariable("MaxLim");
		} else if (oldStr.contains("\n"))
			;
		else
			throw new SAXException(createErrorMessage("Unknow attribute: \""
					+ oldStr + "\""));

	}
        
        /**
	 * Handles StartStop function elements.
	 * Added by Nawzad Mardan 2008-06-01
	 * @exception SAXException. Various causes.
         * '
         */
        
        public void startStopEquation() throws SAXException
        {
            if (oldStr.equals("Resource")) {
			addVariable("resource.type");
            }
            else if(oldStr.equals("Start up"))
            {
                //System.out.println("The model have a start cost for the first time step"); 
                list.add("startAltOne");
                list.add("true");
            }
            else if(oldStr.equals("Shut down"))
            {
                //System.out.println("The model have a start cost for the first time step"); 
                list.add("stopAltOne");
                list.add("true");
            }
            else if(oldStr.equals("Start up within the analysis period"))
            {
                //System.out.println("The model have a start cost for the first time step"); 
                list.add("startFirstTimestepAltOne");
                list.add("true");
            }
            else if(oldStr.equals("Shut down within the analysis period"))
            {
                //System.out.println("The model have a start cost for the first time step"); 
                list.add("stopLastTimestepAltOne");
                list.add("true");
            } 
            else if (oldStr.equals("Percentage of previous flow")) {
			addVariable("percentageOfPreviousFlow");
		} 
            else if (oldStr.equals("Threshold value")) {
			checkstartStopEquation();
			addVariable("thresholdValue");
		} 
            
            else if(oldStr.equals("Start cost for the first time step:"))
            {
                //System.out.println("The model have a start cost for the first time step"); 
                list.add("firstStartCostChoice");
                list.add("true");
            }
            else if(oldStr.equals("Start cost value")) {
                //System.out.println("The model have a startCost cost for the first time step"); 
			checkstartStopEquation();
			addVariable("startCost");
		} 
             else if(oldStr.equals("Stop cost for the last time step:"))
            {
                //System.out.println("The model have a stop cost for the first time step"); 
                 list.add("firstStopCostChoice");
                 list.add("true");
            }
            else if(oldStr.equals("Stop cost value")) {
			checkstartStopEquation();
			addVariable("stopCost");
		} 
            else if(oldStr.equals("Operate value")) {
			checkstartStopEquation();
			addVariable("operateCost");
		}
            else if(oldStr.equals("Minimum flow")) {
			addVariable("miniFlowAltTwo");
		}
            
            else if(oldStr.equals("Start"))
            {
                list.add("startAltThree");
                list.add("true");
            }
            else if(oldStr.equals("Stop"))
            {
                list.add("stopAltThree");
                list.add("true");
            }
            else if(oldStr.equals("First time step is included"))
            {
                list.add("startFirstTimestepAltThree");
                list.add("true");
            }
            else if(oldStr.equals("Last time step is included"))
            {
                list.add("stopLastTimestepAltThree");
                list.add("true");
            }
            
            else if(oldStr.equals("Operate Cost Value")) 
                {
			checkstartStopEquation();
			addVariable("operateCost3");
		}
            else if(oldStr.equals("Minimum Flow Value")) {
			//checkstartStopEquation();
			addVariable("minimumFlow");
		}
            
            else if(oldStr.equals("Operator")) {
			//checkstartStopEquation();
			addVariable("operator3");
		}
            
            else if(oldStr.equals("Min")) {
			addVariable("lim1");
		}
            else if(oldStr.equals("Max")) {
			addVariable("lim2");
		}
            else if(oldStr.equals("R <")) {
			addVariable("lim2");
		}
            else if(oldStr.equals("R >")) {
			addVariable("lim2");
		}
            else if(oldStr.equals("R =")) {
			addVariable("lim2");}
                     
            else if(oldStr.equals("Start west"))
                { 
                 list.add("startWasteChoice");
                 list.add("true");
                }
           else if(oldStr.equals("Stop west"))
                { 
                 list.add("stopWasteChoice");
                 list.add("true");
                }
           else if(oldStr.equals("Start west for the first time step"))
                { 
                 list.add("startWasteOfFirstTimestepChoice");
                 list.add("true");
                }
           else if(oldStr.equals("Stop west for the last time step"))
                { 
                 list.add("stopWasteOfLastTimestepChoice");
                 list.add("true");
                }
           else if(oldStr.equals("Percentage value of the waste")) 
                {
		checkstartStopEquation();
		addVariable("percentageWasteValue");
                }
           else if(oldStr.equals("Fixed value of the waste")) 
                {
		checkstartStopEquation();
		addVariable("fixedWasteValue");
                }
            //operateCostAltFour, miniFlowAltFour, Operate cost, Minimum flow value;
           else if(oldStr.equals("Operate cost")) 
                {
		addVariable("operateCostAltFour");
                }
           else if(oldStr.equals("Minimum flow value")) 
                {
		addVariable("miniFlowAltFour");
                }
           else if(oldStr.equals("Out flow")) 
                {
		addVariable("outFlowValue");
                }
            else
                throw new SAXException(createErrorMessage("Unknow attribute: \""+ oldStr + "\""));
            
        }
	/**
	 * Handles flowDependency function elements.
	 * Added by PUM5 2007-11-28
	 * @exception SAXException. Various causes.
	 * */
	public void flowDependency() throws SAXException 
	{
		if (oldStr.equals("ResX")) {
			addVariable("resourceX.type");
		} else if (oldStr.equals("ResY")) {
			addVariable("resourceY.type");
		} else if (oldStr.equals("Xin")) {
			addVariable("x_in");
		} else if (oldStr.equals("Yin")) {
			addVariable("y_in");
		} else if (oldStr.equals("Time Step")) {
			addTimeStep();
		} else if (oldStr.equals("Start")) {
			try {
				Float.valueOf(charStr);
			}	
			catch (NumberFormatException e) {
				throw new SAXException(createErrorMessage("Non-float value: \""
						+ oldStr + "\""));
			}
			addTimeStepVariable("start");
		} else if (oldStr.equals("End")) {
			addTimeStepVariable("end");
		} else if (oldStr.equals("Slope")) {
			addTimeStepVariable("slope");
		} else if (oldStr.equals("Offset")) {
			addTimeStepVariable("offset");
		} else if (oldStr.contains("\n"))
			;

		else
			throw new SAXException(createErrorMessage("Unknow attribute: \""
					+ oldStr + "\""));

	}

	/**
	 * Handles flowEquation function elements.
	 * Added by PUM5 2007-11-28
	 * @exception SAXException. Various causes.
	 * */
	public void flowEquation() throws SAXException 
	{
		if (oldStr.equals("Time Step")) {
			addTimeStep();
		} else if (oldStr.equals("RHSValue")) {
			addTimeStepVariable("equationvalue");
		} else if (oldStr.equals("RHSConstraint")) {
			addTimeStepVariable("constrainttype");
		} else if (oldStr.equals("Flow")) {
			if (currentFlowType.equals("InFlows"))
				addTimeStepVariable("flowid_in");
			else
				addTimeStepVariable("flowid_out");

		} else if (oldStr.equals("Coeff")) {
			if (currentFlowType.equals("InFlows"))
				addTimeStepVariable("coeff_in");
			else
				addTimeStepVariable("coeff_out");
		} else if (oldStr.contains("\n"))
			;
		else
			throw new SAXException(createErrorMessage("Unknow attribute: \""
					+ oldStr + "\""));
	}

	/**
	 * Handles batchEquation function elements.
	 * Added by PUM5 2007-11-28
	 * @exception SAXException. Various causes.
	 * */
	public void batchEquation() throws SAXException 
	{
		if (oldStr.equals("Min")) {
			addVariable("c_dblMinBatch");

		} else if (oldStr.equals("Max")) {
			addVariable("c_dblMaxBatch");
		} else if (oldStr.equals("BatchTime")) {
			addVariable("c_intBatchTime");
		} else if (oldStr.equals("Adjusting Time")) {
			addVariable("c_intAdjustingTime");

		} else if (oldStr.equals("Predetermined Interval")) {
			addVariable("c_intPredetIntervals");

		} else if (oldStr.equals("Time Step")) {
			addTimeStep();
			for (int i = 0; i < timeStepRoof - 1; i++) {

				list.addLast("T");
				list.addLast(list.get(getIndex("c_dblMinBatch", 1)));
				list.addLast(list.get(getIndex("c_dblMinBatch", 1) + 1));
				list.addLast(list.get(getIndex("c_dblMaxBatch", 1)));
				list.addLast(list.get(getIndex("c_dblMaxBatch", 1) + 1));
				list.addLast(list.get(getIndex("c_intBatchTime", 1)));
				list.addLast(list.get(getIndex("c_intBatchTime", 1) + 1));
				list.addLast(list.get(getIndex("c_intAdjustingTime", 1)));
				list.addLast(list.get(getIndex("c_intAdjustingTime", 1) + 1));
				list.addLast(list.get(getIndex("c_intPredetIntervals", 1)));
				list.addLast(list.get(getIndex("c_intPredetIntervals", 1) + 1));
			}
		} else if (oldStr.equals("Flow")) {
			if (currentFlowType.equals("InFlows"))
				addTimeStepVariable("flowid_in");
			else
				addTimeStepVariable("flowid_out");
		} else if (oldStr.contains("\n"))
			;
		else
			throw new SAXException(createErrorMessage("Unknow attribute: \""
					+ oldStr + "\""));
	}

	/**
	 * Handles functionEditor function elements.
	 * Added by PUM5 2007-11-28
	 * @exception SAXException. Various causes.
	 * */
	public void functionEditor() throws SAXException 
	{
		if (oldStr.equals("IntVar")) {
			list.addLast("intVar");
			list.addLast("[" + charStr + "]");
		} else if (oldStr.equals("FloatVar")) {
			list.addLast("floatVar");
			list.addLast("[" + charStr + "]");
		} else if (oldStr.equals("MaxIntVar")) {
			addVariable("maxIntVar");
		} else if (oldStr.equals("MaxFloatVar")) {
			addVariable("maxFloatVar");
		} else if (oldStr.equals("Time Step")) {
			list.addLast("timestep");

		} else if (oldStr.equals("Rows")) {
			addVariable("rows");
			
			try {
				FErowCount = Integer.valueOf(charStr).intValue();
			} catch (NumberFormatException e) {
				throw new SAXException(createErrorMessage("Unknown value " +charStr + " in Row" ));
			
			}
			
			
		} else if (oldStr.equals("Columns")) {
			addVariable("columns");
			parsingFunctionEditor = true;
		} else if (oldStr.contains("\n"))
			;
		else
			throw new SAXException(createErrorMessage("Unknow attribute: \""
					+ oldStr + "\""));
	}

	/**
	 * Handles logicalEquation function elements.
	 * Added by PUM5 2007-11-28
	 * @exception SAXException. Various causes.
	 * */
	public void logicalEquation() throws SAXException 
	{
		if (oldStr.equals("Time Step")) {
			addTimeStep();
		} else if (oldStr.equals("RHSValue")) {
			addTimeStepVariable("c_RHSValue");
		} else if (oldStr.equals("RHSConstraint")) {
			addTimeStepVariable("c_RHSConstraint");
		} else if (oldStr.equals("Flow")) {
			addTimeStepVariable("flowid");
		} else if (oldStr.equals("Min")) {
			addTimeStepVariable("c_min");
		} else if (oldStr.equals("Max")) {
			addTimeStepVariable("c_max");
		} else if (oldStr.equals("Coefficient")) {
			addTimeStepVariable("c_coeff");
		} else if (oldStr.contains("\n"))
			;
		else
			throw new SAXException(createErrorMessage("Unknow attribute: \""
					+ oldStr + "\""));

	}

	/**
	 * Handles destination function elements.
	 * Added by PUM5 2007-11-28
	 * @exception SAXException. Various causes.
	 * */
	public void destination() throws SAXException 
	{
		if (oldStr.equals("Resource")) {
			addVariable("resource.type");
		} else if (oldStr.contains("\n"))
			;
		else
			throw new SAXException(createErrorMessage("Unknow attribute: \""
					+ oldStr + "\""));

	}

	/**
	 * Handles storageEquation function elements.
	 * Added by PUM5 2007-11-28
	 * @exception SAXException. Various causes.
	 * */
	public void storageEquation() throws SAXException 
	{
		if (oldStr.equals("Time Step")) {
			if (timeStepRoof == 0) {
				list.add(getIndex("label",1)+2,"T");
				timeStepRoof = model.getTimesteplevel(currentNodeID)
				.getMaxTimesteps();
			}
			else{
				list.addLast("T");
				for (int i = 0; i < timeStepRoof - 1; i++) {

					list.addLast("T");
					list.addLast(list.get(getIndex("c_dblInitialStorage", 1)));
					list.addLast(list.get(getIndex("c_dblInitialStorage", 1) + 1));
					list.addLast(list.get(getIndex("c_intMaxStorageTime", 1)));
					list.addLast(list.get(getIndex("c_intMaxStorageTime", 1) + 1));
					list.addLast(list.get(getIndex("c_dblFinalStorage", 1)));
					list.addLast(list.get(getIndex("c_dblFinalStorage", 1) + 1));
				}
			}	
		} else if (oldStr.equals("Flow")) {
			if (currentFlowType.equals("InFlows"))
				addTimeStepVariable("flowid_in");
			else
				addTimeStepVariable("flowid_out");
		} else if (oldStr.equals("Max")) {
			if (currentFlowType.equals("InFlows"))
				addTimeStepVariable("coeff_in_max");
			else
				addTimeStepVariable("coeff_out_max");
		} else if (oldStr.equals("Min")) {
			if (currentFlowType.equals("InFlows"))
				addTimeStepVariable("coeff_in_min");
			else
				addTimeStepVariable("coeff_out_min");
		} else if (oldStr.equals("InEfficiency")) {
			addTimeStepVariable("c_dblInEfficiency");
		} else if (oldStr.equals("OutEfficiency")) {
			addTimeStepVariable("c_dblOutEfficiency");
		} else if (oldStr.equals("TotalInFlowMin")) {
			addTimeStepVariable("c_dblTotalInFlowMin");
		} else if (oldStr.equals("TotalInFlowMax")) {
			addTimeStepVariable("c_dblTotalInFlowMax");
		} else if (oldStr.equals("TotalOutFlowMin")) {
			addTimeStepVariable("c_dblTotalOutFlowMin");
		} else if (oldStr.equals("TotalOutFlowMax")) {
			addTimeStepVariable("c_dblTotalOutFlowMax");
		} else if (oldStr.equals("InitialStorage")) {
			addVariable("c_dblInitialStorage");
		} else if (oldStr.equals("FinalStorage")) {
			addVariable("c_dblFinalStorage");
		} else if (oldStr.equals("StorageEfficiency")) {
			addTimeStepVariable("c_dblStorageEfficiency");
		} else if (oldStr.equals("MinStorage")) {
			addTimeStepVariable("c_dblMinStorage");
		} else if (oldStr.equals("MaxStorage")) {
			addTimeStepVariable("c_dblMaxStorage");
		} else if (oldStr.equals("MaxStorageTime")) {
			addVariable("c_intMaxStorageTime");
		} else if (oldStr.contains("\n"))
			;
		else
			throw new SAXException(createErrorMessage("Unknow attribute: \""
					+ oldStr + "\""));

	}
	
	/**
	 * Handles source function elements.
	 * Added by PUM5 2007-11-28
	 * @exception SAXException. Various causes.
	 * */
	public void source() throws SAXException 
	{
		//System.out.println(model.getObjectFunctionLabels() + " " + oldStr);
		if (oldStr.equals("Resource")) {
			addVariable("resource.type");
		} else if (oldStr.equals("Time Step")) {
			addTimeStep();
			
		} else if (model.getObjectFunctionLabels().contains(oldStr)) {
			if (!charStr.equals("-")) {
				int timeStepIndex = getIndex("T", timeStepCounter + 1);
				if (timeStepCounter < timeStepRoof&&(!(timeStepIndex==-1))) {
					list.add(timeStepIndex, charStr);
					list.add(timeStepIndex, "cost");
					list.add(timeStepIndex, oldStr);
					list.add(timeStepIndex, "label");
				} else if (timeStepCounter == timeStepRoof||(timeStepIndex==-1)) {
					list.addLast("label");
					list.addLast(oldStr);
					list.addLast("cost");
					list.addLast(charStr);
				} else
					throw new SAXException("More Data then TimeSteps");
			}
			timeStepCounter++;
		} else if (oldStr.contains("\n"))
			;
		else
			throw new SAXException(createErrorMessage("Unknow attribute: \""
					+ oldStr + "\""));

	}

	/**
	 * Handles flowRelation function elements.
	 * Added by PUM5 2007-11-28
	 * @exception SAXException. Various causes.
	 * */
	public void flowRelation() throws SAXException 
	{
		if (oldStr.equals("Direction")) {
			addVariable("direction");
		} else if (oldStr.equals("Resource")) {
			addVariable("resource.type");
		} else if (oldStr.equals("Time Step")) {
			addTimeStep();
		} else if (oldStr.equals("Flow")) {
			currentFlow = charStr;
		} else if (oldStr.equals("Operator")) {
			if (charStr.equals("Less") ||charStr.equals("Greater")||charStr.equals("Equal")|| charStr.equals("Less-Greater"))
			{
				if (timeStepCounter < timeStepRoof) {
					int timeStepIndex = getIndex("T", timeStepCounter + 1);
					list.add(timeStepIndex, charStr);
					list.add(timeStepIndex, "operator");
					list.add(timeStepIndex, currentFlow);
					list.add(timeStepIndex, "flow");
					list.add(timeStepIndex, "flowData");
				} else if (timeStepCounter == timeStepRoof) {
					list.addLast("flowData");
					list.addLast("flow");
					list.addLast(currentFlow);
					list.addLast("operator");
					list.addLast(charStr);
				} else
					throw new SAXException("More Data then TimeSteps");
			}
			timeStepCounter++;
		} else if (oldStr.equals("Equal") || (oldStr.equals("Max"))
				|| oldStr.equals("Min")) {
			addTimeStepVariable("limit");
		} else if (oldStr.contains("\n"))
			;
		else
			throw new SAXException(createErrorMessage("Unknow attribute: \""
					+ oldStr + "\""));
	}

	/**
	 * Handles investmentCost function elements.
	 * Added by PUM5 2007-11-28
	 * @exception SAXException. Various causes.
	 * */
	public void investmentCost() throws SAXException 
	{

		if (oldStr.equals("CostWhenNoInvest")) {
			list.addLast("c");
			list.addLast(charStr);
                }
                // Added by Nawzad Mardan 2008-08-14
        else if (oldStr.equals("Technical lifespan")) {
			addVariable("technicallifespan");
		        
		} 
        else if (oldStr.equals("Economic lifespan")) {
			addVariable("economiclifespan");
		        
		}
        //annualRateValue
        else if (oldStr.equals("Annual rate value")) {
			addVariable("annualRateValue");

		}
        else if (oldStr.equals("Percentage value of the scrap")) {
			addVariable("percentagescrapvalue");
		        
		} 
       else if (oldStr.equals("Fixed value of the scrap")) {
			addVariable("fastscrapvalue");
		        
		} 
                else if (oldStr.equals("CostFunction Number")) {
			list.addLast("costfunlist");
			costFuncRoof++;
		} else if (oldStr.equals("Time Step")) {
			list.add(getIndex("costfunlist",1),"T");
			if (timeStepRoof == 0)
				timeStepRoof = model.getTimesteplevel(currentNodeID)
						.getMaxTimesteps();

		}
		// works just like timesteps so we can use the timeStepCounter variable
		else if (oldStr.equals("Start")) {
			if (!charStr.equals("-")) {
				if (timeStepCounter < costFuncRoof) {
					int timeStepIndex = getIndex("costfunlist",
							timeStepCounter + 1);
					list.add(timeStepIndex, charStr);
					list.add(timeStepIndex, "start");
				} else if (timeStepCounter == costFuncRoof) {
					list.addLast("start");
					list.addLast(charStr);
				} else
					throw new SAXException("More Data then TimeSteps");
			}
			timeStepCounter++;
		}
		// works just like timesteps so we can use the timeStepCounter variable
		else if (oldStr.equals("End")) {
			if (!charStr.equals("-")) {
				if (timeStepCounter < costFuncRoof) {
					int timeStepIndex = getIndex("costfunlist",
							timeStepCounter + 1);
					list.add(timeStepIndex, charStr);
					list.add(timeStepIndex, "end");
				} else if (timeStepCounter == costFuncRoof) {
					list.addLast("end");
					list.addLast(charStr);
				} else
					throw new SAXException("More Data then TimeSteps");
			}
			timeStepCounter++;
		}
		// works just like timesteps so we can use the timeStepCounter variable
		else if (oldStr.equals("Slope")) {
			if (!charStr.equals("-")) {
				if (timeStepCounter < costFuncRoof) {
					int timeStepIndex = getIndex("costfunlist",
							timeStepCounter + 1);
					list.add(timeStepIndex, charStr);
					list.add(timeStepIndex, "slope");
				} else if (timeStepCounter == costFuncRoof) {
					list.addLast("slope");
					list.addLast(charStr);
				} else
					throw new SAXException("More Data then TimeSteps");

			}
			timeStepCounter++;
		} else if (oldStr.equals("Offset")) {
			if (!charStr.equals("-")) {
				if (timeStepCounter < costFuncRoof) {
					int timeStepIndex = getIndex("costfunlist",
							timeStepCounter + 1);
					list.add(timeStepIndex, charStr);
					list.add(timeStepIndex, "offset");
				} else if (timeStepCounter == costFuncRoof) {
					list.addLast("offset");
					list.addLast(charStr);
				} else
					throw new SAXException("More Data then TimeSteps");

			}
			timeStepCounter++;
		} else if (oldStr.equals("InFlows")) {
			if (!charStr.equals("-")) {
				if (timeStepCounter < timeStepRoof) {
					int timeStepIndex = getIndex("T", timeStepCounter + 1);
					list.add(timeStepIndex, charStr);
					list.add(timeStepIndex, "flowid_in");
				} else if (timeStepCounter == timeStepRoof) {
					list.add(getIndex("costfunlist", 1), "flowid_in");
					list.add(getIndex("costfunlist", 1), charStr);
				} else
					throw new SAXException("More Data then TimeSteps");

			}
			timeStepCounter++;
		} else if (oldStr.equals("OutFlows")) {
			if (!charStr.equals("-")) {
				if (timeStepCounter < timeStepRoof) {
					int timeStepIndex = getIndex("T", timeStepCounter + 1);
					list.add(timeStepIndex, charStr);
					list.add(timeStepIndex, "flowid_out");
				} else if (timeStepCounter == timeStepRoof) {
					list.add(getIndex("costfunlist", 1), "flowid_out");
					list.add(getIndex("costfunlist", 1), charStr);
				} else
					throw new SAXException("More Data then TimeSteps");

			}
			timeStepCounter++;
		} else
			throw new SAXException(createErrorMessage("Unknow attribute: \""
					+ oldStr + "\""));

	}

	/**
	 * Parses the Excel table.
	 * Added by PUM5 2007-11-28
	 * @param tagname Name of the tag.
	 * @exception SAXException. Various causes.
	 * */
	public void parseTable(String tagname) throws SAXException 
	{
		// We don't want to do anything if it is a Cell tag
		if (tagname.equals("Cell"))
			return;
		// if it is a data tag we want to look if it was some data in it or if
		// it was empty
		// and add the data to the list.
		else if (tagname.equals("Data")) {
			if (charStr != null) {
				list.addLast("cell");
				list.addLast(charStr);
			}
			// empty cell:
			else {
				list.addLast("cell");
				list.addLast("");
			}
		} else if (tagname.equals("Row")) {
			if (FErowCount-- > 0)
				list.add("row");
			else
				parsingFunctionEditor = false;

		}
		// If we
		else
			throw new SAXException(createErrorMessage("Unknown xml tag: "
					+ tagname));

	}

	/**
	 * Gets the index corresponding to the string value in the list.
	 * Added by PUM5 2007-11-28
	 * @param value 
	 * @param timeStepC
	 * @return Returns index if found, else -1.
	 * */
	public int getIndex(String value, int timeStepC) 
	{
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(value))
				timeStepC--;
			if (timeStepC == 0)
				return i;
		}
		return -1;
	}

	/**
	 * Finds nodeID corresponding to nodeLabel
	 * Added by PUM5 2007-11-28
	 * @param nodeLabel
	 * @return The nodeID looked for.
	 * @exception FileInteractionException. If the nodeID isn't found.
	 * */
	public ID getCorrectNodeID(String nodeLabel)
			throws FileInteractionException {
		functionController = new FunctionControl();
		Set nodeCollection = model.getAllNodes().keySet();
		for (int i = 0; i < nodeCollection.size(); i++) {
			if (nodeCollection.toArray()[i].toString().equals(
					nodeLabel.substring(0, nodeLabel.indexOf('-')))) {
				return (ID) nodeCollection.toArray()[i];
			}
		}

		throw new FileInteractionException("NodeID for node" + nodeLabel
				+ " not found");
	}

	/**
	 * Checks if the given String is "Function Type".
	 * Added by PUM5 2007-11-28
	 * @param str 
	 * @return Boolean value.
	 * */
	public boolean isFunction(String str) 
	{
		return (str.equals("Function Type"));
	}

	/**
	 * Creates Strings of buffers.
	 * Added by PUM5 2007-11-28
	 * @param buf[] Character array.
	 * @param offset The offset for the buffert.
	 * @param len The length.
	 * @exception SAXException. Various causes.
	 * */
	public void characters(char buf[], int offset, int len) throws SAXException 
	{
		if (charStr == null) {
			charStr = new String();
		}

		charStr = charStr + new String(buf, offset, len);
	}

	/**
	 * Parses and adds functions to Functioncollection.
	 * Added by PUM5 2007-11-28
	 * @exception SAXException. Various causes.
	 * */
	public void parseAndAddToFunctioncollection() throws SAXException 
	{
		list.addLast(model.getTimesteplevel(currentNodeID));
		try {
			functionController.parseAndAdd(currentFunction.substring(0, 1)
					.toLowerCase()
					+ currentFunction.substring(1), list, model
					.getResourceController(), false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new SAXException(createErrorMessage(e.getMessage()));
		}
		currentFunction = null;
		inFunction = false;
	}

	/**
	 * Adds varaible to list.
	 * Added by PUM5 2007-11-28
	 * @param Attribute
	 * */
	public void addVariable(String Attribute) 
	{
		list.addLast(Attribute);
		list.addLast(charStr);
	}

	/**
	 * Adds time step.
	 * Added by PUM5 2007-11-28
	 * */
	public void addTimeStep() 
	{
		list.addLast("T");
		if (timeStepRoof == 0)
			timeStepRoof = model.getTimesteplevel(currentNodeID)
					.getMaxTimesteps();
	}

	/**
	 * Adds time step variable.
	 * Added by PUM5 2007-11-28
	 * @attribute Att
	 * @exception SAXException. Various causes.
	 * */
	public void addTimeStepVariable(String Att) throws SAXException 
	{
		addTimeStepVariable(Att, charStr.equals("-"));
	}

	/**
	 * Adds time step variable.
	 * Added by PUM5 2007-11-28
	 * @param Attribute 
	 * @param avoid
	 * @exception SAXException. Various causes.
	 * */
	public void addTimeStepVariable(String Attribute, Boolean avoid)
			throws SAXException {
		if (!avoid) {
			if (timeStepCounter < timeStepRoof) {
				int timeStepIndex = getIndex("T", timeStepCounter + 1);
				list.add(timeStepIndex, charStr);
				list.add(timeStepIndex, Attribute);
			} else if (timeStepCounter == timeStepRoof) {
				list.addLast(Attribute);
				list.addLast(charStr);
			} else
				throw new SAXException(
						createErrorMessage(createErrorMessage("More Data then TimeSteps")));
		}
		timeStepCounter++;
	}

	/**
	 * Creates error message for the currentNode.
	 * Added by PUM5 2007-11-28
	 * @param message The message to be sent.
	 * */
	public String createErrorMessage(String message) 
	{

		return "Error in node: \"" + currentNode.getID().toString() + "-"
				+ currentNode.getLabel() + "\" in function \""
				+ currentFunctionLabel + "\": \n" + message;
	}

	/**
	 * Creates warning by handling exception.
	 * Added by PUM5 2007-11-28
	 * @param err A SAXParseException
	 * @exception SAXException. Various causes.
	 * */
	public void warning(SAXParseException err) throws SAXException 
	{
		throw new SAXException("SAX Warning: " + err.getMessage());
	}

	/**
	 * Creates error by handling exception.
	 * Added by PUM5 2007-11-28
	 * @param err A SAXParseException
	 * @exception SAXException. Various causes.
	 * */
	public void error(SAXParseException err) throws SAXException 
	{
		throw new SAXException(); // "SAX Error: " + err.getMessage());
	}

	/**
	 * Creates fatal error by handling exception.
	 * Added by PUM5 2007-11-28
	 * @param err A SAXParseException
	 * @exception SAXException. Various causes.
	 * */
	public void fatalError(SAXParseException err) throws SAXException 
	{
		throw new SAXException("SAX Fatal: " + err.getMessage());
	}

	/**
	 * Checks if the value in boundaryTOP is > 0.
	 * 
	 * Added by PUM5 2007-11-28
	 * @param err A SAXParseException
	 * @exception SAXException. Various causes.
	 * */
	private void checkBoundaryTOP() throws SAXException
	{
		try {
			float f =Float.valueOf(charStr);

			if (f>0){
				list.add("is" + oldStr);
				list.add("true"); //this one will be ignored when parsing
		}
			else{
				list.add("is" + oldStr);
				list.add("false"); //this one will be ignored when parsing
		}
			
		}
		catch (NumberFormatException e) {

			throw new SAXException(createErrorMessage(charStr + " Is not a float value "));
		}
	} 
	
        /**
	 * Checks if the value in startStopEquatio is > 0.
	 * 
	 * Added by Nawzad Mardan 2008-06-01
	 * @param err A SAXParseException
	 * @exception SAXException. Various causes.
	 * */
	private void checkstartStopEquation() throws SAXException
	{
		try {
			float f =Float.valueOf(charStr);
                        if (oldStr.equals("Threshold value"))
                            oldStr = "choiceOne";
                        if (oldStr.equals("Start cost value"))
                            oldStr = "startCostChoice";
                        if(oldStr.equals("Stop cost value"))
                            oldStr = "stopCostChoice";
                        if(oldStr.equals("Operate value"))
                            oldStr = "choiceTwo";
                        if(oldStr.equals("Operate Cost Value"))
                            oldStr = "choiceThree";    
                        if(oldStr.equals("Percentage value of the waste") || oldStr.equals("Fixed value of the waste"))
                            oldStr = "choiceFour";    

			if (f>0){
				//list.add("choiceOne");    
                                list.add(oldStr);
				list.add("true"); //this one will be ignored when parsing
		}
			else{
				//list.add("choiceOne");
                                list.add(oldStr);
				list.add("false"); //this one will be ignored when parsing
		}
			
		}
		catch (NumberFormatException e) {

			throw new SAXException(createErrorMessage(charStr + " Is not a float value "));
		}
	} 
}