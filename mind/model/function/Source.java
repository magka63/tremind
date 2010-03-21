/*
 * Copyright 2001:
 * Peter Andersson <petan117@student.liu.se>
 * Martin Hagman <marha189@student.liu.se>
 * Henrik Norin <henno776@student.liu.se>
 * Anna Stjerneby <annst566@student.liu.se>
 * Tim Terlegård <timte878@student.liu.se>
 * Johan Trygg <johtr599@student.liu.se>
 * Peter Åstrand <petas096@student.liu.se>
 * 
 * Copyright 2007:
 * Per Fredriksson <perfr775@student.liu.se>
 * David Karlslätt <davka417@student.liu.se>
 * Tor Knutsson	<torkn754@student.liu.se>
 * Daniel Källming <danka053@student.liu.se>
 * Ted Palmgren <tedpa175@student.liu.se>
 * Freddie Pintar <frepi150@student.liu.se>
 * Mårten Thurén <marth852@student.liu.se>
 *
 * Copyright 2010
 * Nawzad Mardan <nawzad.mardan@liu.se>
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
package mind.model.function;

import java.util.Vector;
import java.util.LinkedList;

import mind.model.*;
import mind.io.*;

/**
 * The function Source creates an outlet for resources in the model
 * Updated 2002-06-11 by Urban Liljedahl. Changing the handling of cost
 * value to make it possible to use both positive and negative values
 * for the source cost.
 *
 * This version of Source is modifyed to handle multiple object functions.
 * and is not compatible with earlier versions of reMind.
 *
 * @author Peter Andersson
 * @author Johan Trygg
 * @author Urban Liljedahl
 * @author Freddie Pintar
 * @author Tor Knutsson
 * @version 2007-12-12
 */

public class Source extends NodeFunction implements Cloneable {
	private Vector c_cost;

	private ID c_resource;
    // Added by Nawzad Mardan 20100209 at 23.50
    private boolean c_largeCostSize;

	/**
	 * Creates an empty function
	 */
	public Source() {
		super(new ID(ID.FUNCTION), "Source", null); //Timesteplevel must be set later
		c_cost = new Vector();
	}

	/**
	 * Creates a sourcefunction with cost and resource
	 * @param cost A vector with the price for each unit each timestep
	 * @param resource The resource for the source
	 */
	public Source(Vector cost, ID resource) {
		super(new ID(ID.FUNCTION), "Source", null); //Timesteplevel must be set later
		c_cost = cost;
		c_resource = resource;
	}

	/*
	 * Inner class defining a timestep (a vector of CostTuples)
	 */
	private class TimestepInfo extends Vector {
		public TimestepInfo() {
			super();
		}

		public TimestepInfo(Vector v) {
			super(v);
		}
	}

	/**
	 * Creates a new copy of the function
	 * @return A complete copy
	 */
	public Object clone() throws CloneNotSupportedException {
		Source clone = (Source) super.clone();
		c_cost = new Vector(clone.c_cost);
		return clone;
	}

	/**
	 * Gets the costs for every object function and unit
	 * @return The costs of the resource this timestep
	 */
	public Vector getCost() {
		if (c_cost.size() > 0)
            {
            try
              {
              Vector v = (Vector) c_cost.get(getTimestep() - 1);
              return v;//((Vector) c_cost.get(timestep - 1));
              }
            catch(Exception e)
              {
              return null;
              }
            }
			//return (Vector) c_cost.get(getTimestep() - 1);

		return null;
	}
// Added by Nawzad Mardan 20100209
    /**
	 * Gets the size of cost vector as a boolean
	 * @return The size of cost vector as a boolean
	 */
	public boolean getCostSize() {
         if(c_cost.size() > 1)
                c_largeCostSize = true;

         return c_largeCostSize;

    }

	/**
	 * Gets the cost for a specific timestep
	 * @return the cost of the resource the defined timestep
	 */
	public Vector getCost(int timestep) {
		if (c_cost.size() > 0)
            {
            try
              {
              Vector v = (Vector) c_cost.get(timestep - 1);
              return v;//((Vector) c_cost.get(timestep - 1));
              }
            catch(Exception e)
              {
              return null;
              }
            }
		else
			return null;
	}

	/**
	 * Sets the cost for a unit of the resource
	 * @param cost The cost for every unit of the resource this timestep
	 */
	/*
	 public void setCost(float cost)
	 {
	 c_cost.setElementAt(new Float(cost), getTimestep()-1);
	 }*/

	/**
	 * Sets the cost for a unit of the resource
	 * @param cost The cost for every unit of the resource this timestep
	 */
	public void setCost(Vector cost) {
		c_cost.setElementAt(new TimestepInfo(cost), getTimestep() - 1);
	}

	/**
	 * Gets the resource associated with the function
	 * @return Returns the resource
	 */
	public ID getResource() {
		return c_resource;
	}

	/**
	 * Sets the resource associated with the function
	 * @param resource The resource to be set
	 */
	public void setResource(ID resource) {
		c_resource = resource;
	}

	/**
	 * parseData parses a linked list with data and initializes the class
	 * with the values found in the linked list.
	 * The function is used when loading a model or node from disk.
	 *
	 * @param data A linked list with data.
	 * @param rc A control with all available resources.
	 * @param createMissingResources If this is true then
	 * if data contains resources not found in rc, these resources
	 * should be created and added to rc.
	 */
	public void parseData(LinkedList data, ResourceControl rc,
			boolean createMissigResource) throws RmdParseException {
		//debug
		/*System.out.println("LINKED LIST ****************************");
		for (int j = 0; j < data.size(); j++)
			System.out.println(data.get(j));
		System.out.println("****************************");*/
		//Assumes the data to come in this order:
		//label tag, label,
		//resource tag, resource, cost tag timestep 1, cost timestep 1,
		//cost tag timestep 2, cost timestep 2, ... ,
		//cost tag timestep n, cost timestep n,
		//timesteplevel.
		//There must be at least one timestep. Label and resource are optional.
		//Every timestep must contain a cost
		//for( int testa = 0; testa < data.size(); testa++)
		//    System.out.println(data.get(testa));

		c_timesteplevel = (Timesteplevel) data.removeLast();

		String tag;

		if (((String) data.getFirst()).equals("label")) {
			data.removeFirst(); //Throw away the tag
			setLabel((String) data.removeFirst());
		}

		String resource = "";
		if (((String) data.getFirst()).equals("resource.type")) {
			data.removeFirst(); //Throw away the tag
			resource = (String) data.removeFirst();
			c_resource = rc.getResourceID(resource);
			//Check if the resource exists
			if (c_resource == null) {
				if (createMissigResource) {
					c_resource = rc.addResource(resource, "", ""); //we do not know
					//the unit or prefix
				} else
					throw new RmdParseException("The resource '" + resource
							+ "' is not defined.");
			}
		} else {
			c_resource = null;
		}

		//empty the default c_cost
		c_cost.clear();
		// iterate through all the timesteps
		for (int i = 0; data.size() > 0
				&& ((String) data.getFirst()).equals("T"); i++) {
			data.removeFirst(); // remove the "T"
			//System.out.println("timestep " + i);
			TimestepInfo info = new TimestepInfo();

			//set the tuple
			for (int j = 0; data.size() > 0
					&& ((String) data.getFirst()).equals("label"); j++) {
				CostTuple tuple = new CostTuple();
				data.removeFirst();//throw away "label"
				tuple.setLabel((String) data.removeFirst());
				try {
					if (((String) data.getFirst()).equals("cost")) {
						data.removeFirst();
						tuple.setValue(Float.parseFloat((String) data
								.removeFirst()));
				
					}
					else throw new RmdParseException("Unknown attribute" +(String)data.getFirst());
				} catch (NumberFormatException e) {
					throw new RmdParseException("The source data "
							+ "is incorrect. <timestep.source>, "
							+ "<label>, or <cost> "
							+ "tag contains invalid data.");
				}
				//System.out.println("Label: " + tuple.getLabel());
				//System.out.println("Value: " + tuple.getValue());
				info.add(tuple);
			}
			c_cost.addElement(info);
			//else throw new RmdParseException("Unknown value: " + data.getFirst());
		}
		//DEBUG
		/*
		 for(int i=0; i<c_cost.size(); i++){
		 TimestepInfo info = (TimestepInfo)c_cost.get(i);
		 for(int j=0; j<info.size(); j++){
		 CostTuple tuple = (CostTuple)info.get(j);
		 System.out.println("Label: " + tuple.getLabel());
		 System.out.println("Value: " + tuple.getValue());
		 }
		 }
		 */
	}

	/*******************************************************
	 * Inherited functions overridden
	 ******************************************************/

	/**
	 * Returns optimizationinformation from source
	 * @param maxTimesteps The maximum number of timesteps in the model
	 * @param node The ID for the node that generates the equations
	 * @return Some equations that model the source's behaviour
	 * @throws ModelException when resource does not exist
	 */
	public EquationControl getEquationControl(int maxTimesteps, ID node,
			Vector toFlows, Vector fromFlows) throws ModelException {
		EquationControl control = new EquationControl();
		control.setProducer(getResource());

		Equation obj = new Equation(node, getID(), 1, 1, Equation.GOALORFREE);
		Vector foundFlows = new Vector(0);

		if (c_resource == null)
			throw new ModelException("Resource for Source function in Node "
					+ node + " not specified.\n\n" + "Can not optimize.");

		// Check what outgoing flows has the same resource as
		// this source functios
		ID resource;
		for (int i = 0; i < fromFlows.size(); i++) {
			resource = ((Flow) fromFlows.get(i)).getResource();
			if (resource != null && resource.equals(getResource()))
				foundFlows.addElement(fromFlows.get(i));
		}
		if (foundFlows.size() <= 0)
			throw new ModelException("No flow going out from node " + node
					+ " has the same resource as "
					+ "the source function inside it.\n" + "Can not optimize.");

		Vector lengthsVector = getTimesteplevel().getBottomLevel()
				.getLengthsVector();
		float length = .0f;

		for (int i = 0; i < maxTimesteps; i++) {
			// for every timestep, we need to generate a variable

			// First we find out the index of the cost
			// These two lines makes us use the same value in the vector
			// many times if we don't have enough information for all
			// timesteps in the model
			int vectorsize = c_cost.size();
			int index = ((i * vectorsize) / maxTimesteps) % vectorsize;

			// Get the cost from the index and multiply with timestep length
			length = ((Float) lengthsVector.get(i)).floatValue();
			//previous version:
			//float cost = ((Float)c_cost.get(index)).floatValue()*length;

			//new version added 2004-01-08
			//check that number of object functions match number om tuples
			//in the timestepinfo.
			ObjectFunction objFun = ObjectFunction.getInstance();
			int n = objFun.numberOfFunctions();
			if (!validFunction(n, index))
				throw new ModelException("The cost values in node " + node
						+ " doesn't match the object functions");

			//calculate the expression:
			//k1.0*k2.0*v.0 + k1.1*k2.1*v.1 + ... + k1.(n-1)*k2.(n-1)*v.(n-1)
			//where n is the number of object functions
			//(and thus number of terms)
			float koeff = 0.0f;
			for (int j = 0; j < n; j++) {
				koeff = koeff
						+ objFun.getK1(j)
						* objFun.getK2(j)
						* ((CostTuple) ((TimestepInfo) c_cost.get(index))
								.get(j)).getValue();
			}
			float cost = koeff * length;

			for (int j = 0; j < foundFlows.size(); j++) {
				Variable var = new Variable(((Flow) foundFlows.get(j)).getID(),
						i + 1, cost);
				// Add our generated variable to the equation.

				obj.addVariable(var);
				//create and add a variable to the objFun variable collection
				//cost for current timestep (i) and
				//current object function cost value (k)
				int p = ((TimestepInfo) c_cost.get(index)).size();
				for (int k = 0; k < p; k++) {
					CostTuple costTuple = (CostTuple) ((TimestepInfo) c_cost
							.get(index)).get(k);
					Variable var2 = new Variable(((Flow) foundFlows.get(j))
							.getID(), i + 1, costTuple.getValue());

					// Scale the variable depending on the number of timesteps
					var2.multiply(length);
					objFun.addVariable(var2, costTuple.getLabel(), i + 1);// i+1 == timestep
				}
			}
		}
		control.add(obj);

		return control;
	}

        /**
	 * Returns optimizationinformation from source
	 * @param data The timestep numbers for each year
         * @param annualRate The annually interest
         * @param timeStepValues The length of timesteps 
	 * @param node The ID for the node that generates the equations
	 * @return Some equations that model the source's behaviour
	 * @throws ModelException when resource does not exist
	 * @author Added by Nawzad Mardan 070801 used by Discountedsystemcost Function
         * Returns optimizationinformation from the function.
          */
        
        /*
         * This method added in order to generate new type of equations, this is not the same equations 
         * when the Discountedsystemcost function is not used. These equations have 
         * a different timestep numbers and a different coefficient depending on the
         * length of the analyses period. This method calls when the user trigger 
         * the Discountedsystemcost function.

         */
       public EquationControl getEquationControl2(Object [][] data,float rate,Vector timeStepValues,
					      ID node, Vector toFlows,Vector fromFlows)throws ModelException
       {
		EquationControl control = new EquationControl();
		control.setProducer(getResource());

		Equation obj = new Equation(node, getID(), 1, 1, Equation.GOALORFREE);
		Vector foundFlows = new Vector(0);
        Vector annualRate = null;
        int analyperiod = data.length;
        annualRate = calculateAnnualRate(analyperiod, rate);

		if (c_resource == null)
			throw new ModelException("Resource for Source function in Node "
					+ node + " not specified.\n\n" + "Can not optimize.");

		// Check what outgoing flows has the same resource as
		// this source functios
		ID resource;
		for (int i = 0; i < fromFlows.size(); i++) {
			resource = ((Flow) fromFlows.get(i)).getResource();
			if (resource != null && resource.equals(getResource()))
				foundFlows.addElement(fromFlows.get(i));
		}
		if (foundFlows.size() <= 0)
			throw new ModelException("No flow going out from node " + node
					+ " has the same resource as "
					+ "the source function inside it.\n" + "Can not optimize.");
		float rateCoff = .0f;
                int timestepNum =0;
                float timestepLength =.0f;
                TimestepInfo tsi;
                CostTuple ct ;
                float cost=.0f;
                
                // for each timestep, we need to generate a variable
                // Each year have own rate and a number of time steps
                // Generate a variable for each timesteps and for each year
		for (int i = 0; i < data.length; i++) 
                {
                   // Get anuualy rate from the annual vector
                   rateCoff = ((Float)annualRate.get(i)).floatValue();
                   int timePeriodLength = data[i].length;
                   // Each year have his own time steps
                   for(int j= 1; j< timePeriodLength; j++)
                   {
                       // Check if the array is not empty
                       if(!data[i][j].equals(""))
                       {
                       // Get timestep number from the array
                       String value = (String)data[i][j];
                       timestepNum =Integer.parseInt(value);
                       
                       // Check if the correct timestep insert it
                       if(timestepNum <= 0)
                           throw new ModelException("Please insert correct Timestep number in the Discountsystem's table.\n" + 
                                        " Timestep number should be largar than zero.\n Can not optimize.");
                           
                       
                       // Get the length of the timestep
                       timestepLength = ((Float)timeStepValues.get(timestepNum-1)).floatValue();
                              
                       // Get the cost from the index  for the timestep number
                      // Check if the cost is the same for all timesteps
                       if(timestepNum > c_cost.size())
                            {
                            tsi = (TimestepInfo)c_cost.get(0);
                            }
                       else
                            {
                           // Different timesteps have a different cost
                                tsi = (TimestepInfo)c_cost.get(timestepNum-1);
                            }
                       ct = (CostTuple)tsi.get(0);
                       cost = ct.getValue();
                       //((CostTuple) ((TimestepInfo) c_cost.get(index)).get(j)).getValue();
                       //CostTuple ct = (CostTuple)tsi.get(0);
                       //float cost = ct.getValue();
                       //float cost = ((Float)c_cost.get(timestepNum)).floatValue();
                       
                      // multiply the cost with timestep length and the annualy rate
                       float coeff = cost*timestepLength*rateCoff;
                       // Generat new variable for each time step and each flow
                       for(int k = 0; k < foundFlows.size(); k++)
                            {
                            Variable var = new Variable(((Flow) foundFlows.get(k)).getID(),timestepNum, coeff);
                            // Add our generated variable to the equation.
                            obj.addVariable(var);
                            }// END FOR  Generate variable
                        }// END IF checking
                    }// END FOR for each year
                }// END FOR for all timesteps and all years
              
                control.add(obj);
           return control;
	}



// Validate the function with respect to the object functions
	private boolean validFunction(int n, int index) {
		return n == ((TimestepInfo) c_cost.get(index)).size();
	}

	/**
	 * Returns a short overview of the current equations of the function
	 * @return A short overview of the equation returned by the function
	 */
	public String getOverview() {
		return c_resource + " at a cost of " + (Float) c_cost.get(0)
				+ " per unit.";
	}

	public int getTimesteps() {
		return c_cost.size();
	}

	public String toXML(ResourceControl resource, int indent) {
		String xml = XML.indent(indent) + "<source>" + XML.nl();

		if (getLabel() != null)
			xml = xml + XML.indent(indent + 1) + "<label>"
					+ XML.toXML(getLabel()) + "</label>" + XML.nl();

		if (c_resource != null)
			xml = xml + XML.indent(indent + 1) + "<resource.type>"
					+ XML.toXML(resource.getLabel(c_resource))
					+ "</resource.type>" + XML.nl();

		for (int i = 0; i < c_cost.size(); i++) {
			xml = xml + XML.indent(indent + 1) + "<timestep.source nr=\""
					+ (i + 1) + "\">" + XML.nl();
			TimestepInfo info = (TimestepInfo) c_cost.get(i);
			for (int j = 0; j < info.size(); j++) {
				CostTuple tuple = (CostTuple) info.get(j);
				xml = xml + XML.indent(indent + 2) + "<label>"
						+ tuple.getLabel() + "</label>" + XML.nl();
				xml = xml + XML.indent(indent + 2) + "<cost>"
						+ tuple.getValue() + "</cost>" + XML.nl();
			}
			xml = xml + XML.indent(indent + 1) + "</timestep.source>"
					+ XML.nl();
		}
		xml = xml + XML.indent(indent) + "</source>" + XML.nl();

		return xml;
	}

	/**
	 * Puts EXML data in the given ExmlSheet
	 * PUM5 Added 2007-12-12
	 * @param resources A ResourceControl
	 * @param sheet The ExmlSheet to be changed 
	 */
	public void toEXML(ResourceControl resources,ExmlSheet sheet) {

    	//Find Label
    	String label = ((this.getLabel()==null)?"":this.getLabel());

		//Add function header
		sheet.addFunctionHeader("Source", label);
    	
    	//Add Label
    	String resource = ((c_resource==null)?"":resources.getLabel(c_resource));
    	sheet.addRow(sheet.addLockedCell("Resource")+sheet.addCell(XML.toXML(resource)));

    	// Add timestep nrs in one Row.
    	int numberOfTimesteps = getTimesteps();
    	sheet.addTimeStepRow(numberOfTimesteps);
    	TimestepInfo info = (TimestepInfo) c_cost.get(0);
    			
		
    	Vector labelsv = ObjectFunction.getInstance().getObjectFunctionLabels();
    	int nrOfLabels = labelsv.size();
    	sheet.initTable(nrOfLabels, numberOfTimesteps);
		for (int j = 0; j < nrOfLabels; j++) {
    		sheet.addLockedTableValue(XML.toXML((String)labelsv.get(j)).toString());    		
		}
    	
		/*
		 * Labels which are collected from ObjectFunctions static label list are
		 * compared with the ones from info, since an object with a zero value does not apear in the 
		 * object list while iterating over costs. Hence, we can detect if an object is missing and print a dash.
		 * Labels in Objects from ObjectFunctions are unique and are always coming in ordered formation.
		 * */
		
    	for(int i = 0; i < numberOfTimesteps; i++){
    		int success = 0;
    		info = (TimestepInfo) c_cost.get(i);
    		for (int j = 0; j < info.size(); j++) {    			    			
				CostTuple tuple = (CostTuple) info.get(success);
				if (labelsv.get(j).toString().compareTo(tuple.getLabel()) == 0) {
					sheet.addTableValue(tuple.getValue());
					success++;
				} else {
					sheet.addTableValue("0");
				}
				
			} 
    	    if (info.size() < nrOfLabels)
    	    	sheet.fillTableToNextCol("0",false);
    	}
    	sheet.endTable();
    	sheet.addRow(sheet.addCell(""));
    	
	}

	/*
	 * All these protected methods below are used for timesteps.
	 * These methods are abstract in NodeFunction and are called
	 * from there.
	 */

	protected void timestepInsertAt(int index) {
		c_cost.insertElementAt(new TimestepInfo(), index);
	}

	protected void timestepRemoveAt(int index) {
		c_cost.removeElementAt(index);
	}

	protected void timestepSetMoreDetailed(int factor) {
		int oldsize = c_cost.size();
		int newsize = oldsize * factor;

		//Copy old cost values to new cost array
		Vector newCost = new Vector(newsize, 1);
		TimestepInfo info;
		for (int i = 0; i < oldsize; i++) {
			info = (TimestepInfo) c_cost.get(i);
			for (int k = 0; k < factor; k++) {
				newCost.add(info.clone());
			}
		}
		c_cost = newCost;
	}

// Added by Nawzad Mardan 20100321
  /* To solve the bug in the Source function. If the user add a new Source function in a node
    which have several levels of time steps and user enter only the values for the first time steps instead for alls
    time steps and save the model. If the user try to open the model an errer  occur and the model can not be opened
   */
   public void setDetailedDataToRemainedTimesteps(int factor)
    {
	//int newsize = oldsize * factor;

	//Copy values from the first time step to new array
	Vector newTimestepInfo = new Vector(factor,1);
	TimestepInfo info = (TimestepInfo) c_cost.get(0);
	for(int i = 0; i < factor; i++)
        {
		newTimestepInfo.add(info.clone());
        }
	c_cost = newTimestepInfo;
    }

   // Added by Nawzad Mardan 20100209
	public void updateCost(int factor) {
		int oldsize = c_cost.size();
		int newsize = oldsize * factor;

		//Copy old cost values to new cost array
		Vector newCost = new Vector(newsize, 1);
		TimestepInfo info;
		for (int i = 0; i < oldsize; i++) {
			info = (TimestepInfo) c_cost.get(i);
			for (int k = 0; k < factor; k++) {
				newCost.add(info.clone());
			}
		}
		c_cost = newCost;
	}
	protected void timestepSetLessDetailed(int newSize, int factor) {
		Vector newCost = new Vector(newSize);
		int i, oldindex;

		//Copy old cost values to new cost array
		for (i = 0, oldindex = 0; i < newSize; i++, oldindex += factor)
			newCost.add(c_cost.get(oldindex));

		c_cost = newCost;
	}

	protected void timestepResetData(int size) {
		c_cost = new Vector();
		TimestepInfo info = new TimestepInfo();
		c_cost.add(info);
	}

  public boolean isRelatedToFlow(ID flow) {
    return false;
  }
  // Added by Nawzad Mardan 090416
   /**
     * Calculating the annually rate
     *
     */

    private Vector calculateAnnualRate(int analysPeriod, float annualratevalue)
    {
        double annualRate;
        Vector annualRateVector = new Vector();

        // Chang rate from integer to foat
         float rate;
        // Change it to percent
        rate = annualratevalue/100;
        for(int i = 1; i<= analysPeriod;i++)
        {
            // annualrate = (1+rate)power(- number of year)
            annualRate = Math.pow((1+rate),-i);
            annualRateVector.add(new Float(annualRate));

        }
        return annualRateVector;
    }
}