/*
 * Copyright 2004:
 * Marcus Bergendorff <amaebe-1@student.luth.se>
 * Jan Sk�llermark <jansok-1@student.luth.se>
 * Nils-Oskar Spett <nilspe-1@student.luth.se>
 * Richard Harju <richar-1@student.luth.se>
 *
 * Copyright 2007:
 * Per Fredriksson <perfr775@student.liu.se>
 * David Karlsl�tt <davka417@student.liu.se>
 * Tor Knutsson	<torkn754@student.liu.se>
 * Daniel K�llming <danka053@student.liu.se>
 * Ted Palmgren <tedpa175@student.liu.se>
 * Freddie Pintar <frepi150@student.liu.se>
 * M�rten Thur�n <marth852@student.liu.se>
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

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import mind.io.ExmlSheet;
import mind.io.RmdParseException;
import mind.io.XML;
import mind.model.Equation;
import mind.model.EquationControl;
import mind.model.Flow;
import mind.model.ID;
import mind.model.ModelException;
import mind.model.NodeFunction;
import mind.model.ResourceControl;
import mind.model.Timesteplevel;
import mind.model.Variable;
import mind.model.function.parser.SFLexer;
import mind.model.function.parser.SFParser;

/**
 * FunctionEditor
 * @version 2007-12-12
 * @author Marcus Bergendorff and Jan Sk�llermark
 * @author Freddie Pintar
 * @author Tor Knutsson
 */

public class FunctionEditor extends NodeFunction {

	private static final int DEFAULT_COLS = 7;

	private static final int DEFAULT_ROWS = 10;

	// "float variables"
	private static int maxFloatVar = 1;

	// "integer variables"
	private static int maxIntVar = 1;

	private int c_currentTS = 1; // Current Time Step

	//contains the parsed expressions from the spreadsheet
	private EquationControl c_expressions = new EquationControl();

	private Vector c_floatVars;

	private ID c_funcID;

	private Flow[] c_inFlows;

	private Vector c_intVars;
	private Flow[] c_outFlows;

	//private DefaultTableModel c_model;
	private Vector c_timeStepInfo;

	//variables for keeping track of last parsed cell
	private int lpCol = 0, lpRow = 0, tserr = 1;

	/**
	 * Constructor for FunctionEditor
	 */
	public FunctionEditor() {
		super(new ID(ID.FUNCTION), "FunctionEditor", null);
		c_funcID = getID();
		c_outFlows = null;
		c_inFlows = null;

		c_timeStepInfo = new Vector(1);
		c_timeStepInfo.add(new DefaultTableModel(DEFAULT_ROWS, DEFAULT_COLS)); //  TOP timestep
		c_intVars = new Vector(1);
		c_floatVars = new Vector(1);
	}


	/**
	 * Look for every unique integer variable and give it a positive infinite bound.
	 *
	 * @param equationControl
	 *
	 * FIXME - experimental
	 */
	private void fixIntegers(EquationControl equationControl) {
		Vector equations = equationControl.getAllEquations();
		Vector boundedVars = new Vector();

		for (Iterator iter = equations.iterator(); iter.hasNext();) {
			Equation element = (Equation) iter.next();
			Vector variables = element.getAllVariables();

			for (Iterator iterator2 = variables.iterator(); iterator2.hasNext();) {
				Variable theVar = (Variable) iterator2.next();

				if (theVar.isInteger() && (!isVarInVector(theVar, boundedVars))) {
					Equation eq = new Equation(new ID("N"), theVar.toString(),
							0, Equation.PLUSINFINITY);
					eq.addVariable(theVar);
					equationControl.add(eq);
					boundedVars.add(theVar);
				}
			}
		}
	}

	/**
	 * Returns optimizationinformation from FunctionEditor
	 * @param maxTimesteps The maximum number of timesteps in the model
	 * @param node The ID for the node that generates the equations
	 * @return Some equations that model the flowEquation's behaviour
	 * @throws ModelException if it cannot optimize
	 */
	public EquationControl getEquationControl(int maxTimesteps, ID node,
			Vector toFlows, Vector fromFlows) throws ModelException {

		//The best solution should be to check for changes to the model.
		//If no change has occured and the function is allready
		//parsed then the parser doesn't have to be called.
		//As a check for changes to the model does not exist,
		//the parser is allways called when exporting to MPS-format.

		c_inFlows = new Flow[toFlows.size()];
		c_outFlows = new Flow[fromFlows.size()];

		for (int i = 0; i < toFlows.size(); i++) {
			c_inFlows[i] = (Flow) toFlows.elementAt(i);
		}

		for (int i = 0; i < fromFlows.size(); i++) {
			c_outFlows[i] = (Flow) fromFlows.elementAt(i);
		}
		try {
			parseTimesteps(maxTimesteps, node, true);
		} catch (Exception e) {
			//e.printStackTrace();
			throw new ModelException(e.getMessage() + "\nNode: " + node);
		}

		//validate resources
		for (int i = 0; i < toFlows.size(); i++) {
			Flow f = (Flow) toFlows.elementAt(i);
			ID res = f.getResource();
			c_expressions.setConsumers(res);
		}
		for (int i = 0; i < fromFlows.size(); i++) {
			Flow f = (Flow) fromFlows.elementAt(i);
			ID res = f.getResource();
			c_expressions.setProducers(res);
		}

		return c_expressions;
	}

	public Vector getFloatVars() {
		return c_floatVars;
	}

	public Flow[] getInFlows() {
		return c_inFlows;
	}

	public Vector getIntVars() {
		return c_intVars;
	}

	public DefaultTableModel getModel() {
		return (DefaultTableModel) c_timeStepInfo.elementAt(c_currentTS - 1);
	}

	public int getNextFloatVar() {
		int tmp = maxFloatVar;
		maxFloatVar++;
		return tmp;
	}

	public int getNextIntVar() {
		int tmp = maxIntVar;
		maxIntVar++;
		return tmp;
	}

	public Flow[] getOutFlows() {
		return c_outFlows;
	}

	/**
	 * Returns the number of timesteps
	 * @return number of timesteps
	 */
	protected int getTimesteps() {
		return c_timeStepInfo.size();
	}

	/**
	 * @param element a Variable
	 * @param vector the Vector
	 * @return true if the variable is in the vector otherwise false
	 *
	 */
	private boolean isVarInVector(Variable element, Vector vector) {
		for (Iterator iter = vector.iterator(); iter.hasNext();) {
			Variable vElement = (Variable) iter.next();
			if (vElement.equals(element))
				return true;
		}
		return false;
	}

	/**
	 * Makes an independent copy of the original.
	 * @param dtm The Original DefaultTableModel to Copy
	 */
	private DefaultTableModel modelCopy(DefaultTableModel dtm) {
		Vector original = dtm.getDataVector();
		Vector copy = new Vector();
		Vector headerCopy = new Vector();

		for (int i = 0; i < original.size(); i++) {
			Vector v = (Vector) original.elementAt(i);
			copy.addElement(v.clone());
		}

		for (int j = 0; j < dtm.getColumnCount(); j++) {
			headerCopy.addElement((Object) dtm.getColumnName(j));
		}
		return new DefaultTableModel(copy, headerCopy);
	}

	/**
	 * Parses the table for this FunctionEditor
	 *
	 * Modified to work with the generated Jacc and JFlex parser
	 *
	 */
	public void parse(ID node) throws FunctionEditorException {
		try {
			parseTimesteps(getTimesteps(), node, false);
		} catch (ModelException e) {
			throw new FunctionEditorException(e.getMessage(), lpRow, lpCol,
					tserr);
		}
	}

	public void parseData(LinkedList data, ResourceControl rc,
			boolean createMissingResources) throws RmdParseException {

		setTimesteplevel((Timesteplevel) data.removeLast());

		c_timeStepInfo.clear();

		if (((String) data.getFirst()).equals("label")) {
			data.removeFirst();
			setLabel((String) data.removeFirst());
		}

		//intVar
		if (((String) data.getFirst()).equals("intVar")) {
			data.removeFirst();
			String vars = (String) data.removeFirst();
			vars = vars.substring(1, vars.length() - 1); //remove '[' and ']'
			StringTokenizer tok = new StringTokenizer(vars, ",");
			while (tok.hasMoreTokens()) {
				c_intVars.add(tok.nextToken());
			}
		}

		//floatVar
		if (((String) data.getFirst()).equals("floatVar")) {
			data.removeFirst();
			String vars = (String) data.removeFirst();
			vars = vars.substring(1, vars.length() - 1); //remove '[' and ']'
			StringTokenizer tok = new StringTokenizer(vars, ",");
			while (tok.hasMoreTokens()) {
				c_floatVars.add(tok.nextToken());
			}
		}

		//maxIntVar
		if (((String) data.getFirst()).equals("maxIntVar")) {
			data.removeFirst();
			maxIntVar = Integer.parseInt((String) data.removeFirst());
		}

		//maxFloatVar
		if (((String) data.getFirst()).equals("maxFloatVar")) {
			data.removeFirst();
			maxFloatVar = Integer.parseInt((String) data.removeFirst());
		}

		//parse TS info
		while ((!data.isEmpty())
				&& data.getFirst().toString().equals("timestep")) {
			data.removeFirst();
			DefaultTableModel df = new DefaultTableModel();
			c_timeStepInfo.add(df);

			if (((String) data.getFirst()).equals("rows")) {
				data.removeFirst();
				String str = (String) data.removeFirst();
				df.setRowCount(Integer.parseInt(str));
			}
			if (((String) data.getFirst()).equals("columns")) {
				data.removeFirst();
				String str = (String) data.removeFirst();
				df.setColumnCount(Integer.parseInt(str));
			}
			//parse celleri
			int x = 0;
			int y = 0;
			while ((!data.isEmpty())
					&& data.getFirst().toString().equals("row")) {
				data.removeFirst(); //remove "row"
				while ((!data.isEmpty())
						&& data.getFirst().toString().equals("cell")) {
					data.removeFirst(); //remove "cell"
					String str = (String) data.removeFirst();
					if (str.equals("null"))
						str = "";
					df.setValueAt(str, y, x);
					x++;
				}
				y++;
				x = 0;
			}
		}

	}

	private void parseTimesteps(int maxTimesteps, ID node, boolean exporting)
			throws ModelException {
		SFLexer lexer = null;
		SFParser p = null;

		int timestep_count = 1;
		DefaultTableModel dtm = null;
		Vector tmpVector = new Vector(1);
		try {
			int numOfTimesteps = getTimesteps();
			int factor = (maxTimesteps / numOfTimesteps);
			int equationNr = 1;

			c_expressions.clear();

			for (int i = 0; i < numOfTimesteps; i++) {
				for (int j = 0; j < factor; j++) {

					dtm = (DefaultTableModel) c_timeStepInfo.elementAt(i);

					// parse the table
					int xMax = dtm.getColumnCount();
					int yMax = dtm.getRowCount();
					for (int y = 0; y < yMax; y++) {
						lpRow = y;
						for (int x = 0; x < xMax; x++) {
							lpCol = x;
							String str = (String) dtm.getValueAt(y, x);

							if ((!(str == null || str.equals("")))
									&& str.matches(".*[<>=].*")
									&& !str.startsWith("%")) {
								System.out.println("STR:" + str);
								lexer = new SFLexer(new java.io.StringReader(
										str), dtm, x, y);
								p = new SFParser(lexer, c_inFlows, c_outFlows,
										timestep_count,
										numOfTimesteps * factor, c_intVars,
										c_floatVars);
								p.lex();
								p.parse();
								Equation equation = p.getEquation();
								equation.setID(node, c_funcID, timestep_count,
										equationNr);
								tmpVector.add(equation);
								//c_expressions.add(equation);
								equationNr++;
							}
						}
					}
					if (exporting && tmpVector.isEmpty()) {
						throw new ModelException(
								"No Relation Between In and Out Flows in: \nTimestep: "
										+ timestep_count);
					} else {
						c_expressions.addAll(tmpVector);
						tmpVector.clear();
					}
					timestep_count++;
				}
			}
		} catch (ModelException e) {
			throw new ModelException(e.getMessage());
		} catch (Exception e) {
			//e.printStackTrace();
			tserr = timestep_count;
			if (lexer.celref == true)
				throw new ModelException(e.getMessage() + " cell "
						+ dtm.getColumnName(lpCol) + (lpRow + 1)
						+ " or cell referenced to from: " + "\nCell: "
						+ dtm.getColumnName(lpCol) + (lpRow + 1)
						+ "\nTimestep: " + timestep_count);
			else
				throw new ModelException(e.getMessage() + ":\nCell: "
						+ dtm.getColumnName(lpCol) + (lpRow + 1)
						+ "\nTimestep: " + timestep_count);
		}

		// FIXME experimental fix for integers.
		// Adds bound to every integer variable to override the default bounds /MB
		if (exporting)
			fixIntegers(c_expressions);
	}

	public void setInFlows(Flow[] in) {
		c_inFlows = in;
	}

	public void setOutFlows(Flow[] out) {
		c_outFlows = out;
	}

	// Support for timesteps

	public void setTimestep(int ts) {
		super.setTimestep(ts);
		c_currentTS = ts;
	}

	/**
	 * Inserts empty c_timeStepInfo (TableModel) into a specified
	 * index position.
	 * @param index The index to insert at.
	 */
	protected void timestepInsertAt(int index) {
		c_timeStepInfo.insertElementAt(new DefaultTableModel(DEFAULT_ROWS,
				DEFAULT_COLS), index);
	}

	/**
	 * Removes c_timeStepInfo at specified
	 * index position.
	 * @param index The index.
	 */
	protected void timestepRemoveAt(int index) {
		c_timeStepInfo.remove(index);
	}

	/**
	 * Clears all timestep data.
	 * @param size Size of vector to hold
	 * timestep information.
	 */
	protected void timestepResetData(int size) {
		c_timeStepInfo = new Vector(size);
		for (int i = 0; i < size; i++) {
			c_timeStepInfo.addElement(new DefaultTableModel(DEFAULT_ROWS,
					DEFAULT_COLS));
		}
	}

	/**
	 * Reduces the c_timeStepInformation. A factor gives
	 * which timesteps to be saved.
	 * @param newSize The size of the new vector to hold
	 * timestep information.
	 * @param factor Gives which of the timesteps to save.
	 */

	protected void timestepSetLessDetailed(int newSize, int factor) {
		//System.out.println("timestepSetLessDetailed(" + factor + ")");
		Vector newTSinfo = new Vector(newSize);

		for (int i = 0, oldindex = 0; i < newSize; i++, oldindex += factor) {
			newTSinfo.addElement(modelCopy((DefaultTableModel) c_timeStepInfo
					.elementAt(oldindex)));
		}
		c_timeStepInfo = newTSinfo;
		c_currentTS = 1;
	}

	/**
	 * Increases the number of timesteps. Old timestep
	 * info will be copied into the new ones.
	 * @param factor Gives the number of new timesteps
	 * to be added for every old one.
	 */

	protected void timestepSetMoreDetailed(int factor) {
		//System.out.println("timestepSetMoreDetailed(" + factor + ")");
		int oldSize = c_timeStepInfo.size();
		int newSize = factor * oldSize;
		Vector newTSinfo = new Vector(newSize);

		for (int i = 0; i < oldSize; i++) {
			for (int j = 0; j < factor; j++) {
				newTSinfo
						.addElement(modelCopy((DefaultTableModel) c_timeStepInfo
								.elementAt(i)));
			}
		}

		c_timeStepInfo = newTSinfo;
	}

	public String toXML(ResourceControl resource, int indent) {
		String xml = XML.indent(indent) + "<functionEditor>" + XML.nl();
		//save label
		if (getLabel() != null) {
			xml = xml + XML.indent(indent + 1) + "<label>" + getLabel()
					+ "</label>" + XML.nl();
		}

		//---intVar
		xml = xml + XML.indent(indent + 1) + "<intVar>[";
		for (Enumeration e = c_intVars.elements(); e.hasMoreElements();) {
			xml += e.nextElement();
			if (e.hasMoreElements())
				xml += ",";
		}
		xml += "]</intVar>" + XML.nl();

		//---floatVar
		xml = xml + XML.indent(indent + 1) + "<floatVar>[";
		for (Enumeration e = c_floatVars.elements(); e.hasMoreElements();) {
			xml += e.nextElement();
			if (e.hasMoreElements())
				xml += ",";
		}
		xml += "]</floatVar>" + XML.nl();

		//maxIntVar
		xml = xml + XML.indent(indent + 1) + "<maxIntVar>"
				+ String.valueOf(maxIntVar) + "</maxIntVar>" + XML.nl();

		//maxFloatVar
		xml = xml + XML.indent(indent + 1) + "<maxFloatVar>"
				+ String.valueOf(maxFloatVar) + "</maxFloatVar>" + XML.nl();

		//----timestep
		for (Enumeration e = c_timeStepInfo.elements(); e.hasMoreElements();) {
			DefaultTableModel model = (DefaultTableModel) e.nextElement();
			xml += XML.indent(indent + 1) + "<timestep>" + XML.nl();

			//save number of rows and columns
			xml += XML.indent(indent + 2) + "<rows>" + model.getRowCount()
					+ "</rows>" + XML.nl();

			xml += XML.indent(indent + 2) + "<columns>"
					+ model.getColumnCount() + "</columns>" + XML.nl();

			//save cell contents
			for (int i = 0; i < model.getRowCount(); i++) {
				xml += XML.indent(indent + 2) + "<row>" + XML.nl();
				for (int j = 0; j < model.getColumnCount(); j++) {
					String cellContent = (String) model.getValueAt(i, j);

					//replaces '<' and '>'
					if (cellContent != null) {
						cellContent = cellContent.replaceAll(">", "&gt;");
						cellContent = cellContent.replaceAll("<", "&lt;");
					}
					xml += XML.indent(indent + 3) + "<cell>" + cellContent
							+ "</cell>" + XML.nl();
				}
				xml += XML.indent(indent + 2) + "</row>" + XML.nl();
			}
			//----/timestep
			xml += XML.indent(indent + 1) + "</timestep>" + XML.nl();
		}

		xml = xml + XML.indent(indent) + "</functionEditor>" + XML.nl();
		return xml;
	}

	/**
	 * Puts EXML data in the given ExmlSheet
	 * PUM5 Added 2007-12-12
	 * @param resources A ResourceControl
	 * @param sheet The ExmlSheet to be changed
	 */
	public void toEXML(ResourceControl resources,ExmlSheet sheet)
	{

    	//Find Label
    	String label = ((this.getLabel()==null)?"":this.getLabel());

		//Add function header
		sheet.addFunctionHeader("FunctionEditor", label);

    	// Add integer variables in a comma-seperated list
		String intvarData = "";
		for (Enumeration e = c_intVars.elements(); e.hasMoreElements();) {
			intvarData += e.nextElement();
			if (e.hasMoreElements())
				intvarData += ",";
		}
    	sheet.addRow(sheet.addLockedCell("IntVar")+sheet.addLockedCell(intvarData));

    	// Add Float variables in a comma-seperated list
		String fltvarData = "";
		for (Enumeration e = c_floatVars.elements(); e.hasMoreElements();) {
			fltvarData += e.nextElement();
			if (e.hasMoreElements())
				fltvarData += ",";
		}
    	sheet.addRow(sheet.addLockedCell("FloatVar")+sheet.addLockedCell(fltvarData));

    	//maxIntVar
    	sheet.addRow(sheet.addLockedCell("MaxIntVar")+sheet.addLockedCell(maxIntVar+""));

    	//maxFloatVar
    	sheet.addRow(sheet.addLockedCell("MaxFloatVar")+sheet.addLockedCell(maxFloatVar+""));

		//----timestep
    	Integer timestep = 1;
    	Integer maxColumns = 0;
		for (Enumeration e = c_timeStepInfo.elements(); e.hasMoreElements();) {
			DefaultTableModel model = (DefaultTableModel) e.nextElement();
			//save number of rows and columns and timestep number

			sheet.addRow(sheet.addCell("String","Time Step","TimeStep",0)
					+sheet.addCell("Number",timestep.toString(),"TimeStep",0));
			sheet.addRow(sheet.addLockedCell("Rows")+sheet.addCell(model.getRowCount()));
			sheet.addRow(sheet.addLockedCell("Columns")+sheet.addCell(model.getColumnCount()));

			//save cell contents
			String cellData;
			//Update max columns if higher
			maxColumns = ((model.getColumnCount() > maxColumns)?model.getColumnCount():maxColumns);
			for (int i = 0; i < model.getRowCount(); i++) {
				cellData = "";
				for (int j = 0; j < model.getColumnCount(); j++) {
					String cellContent = (String) model.getValueAt(i, j);
					cellData += sheet.addCell(((cellContent == null)?"":XML.toXML(cellContent)));
				}
				sheet.addRow(cellData);
			}
			timestep++;
		}
		//adding a distinct delimiter row after the function
		sheet.addRow(sheet.addCell("String", "", "Locked", maxColumns-1));
	}

    /**
    * Implementera den h�r den som kan!!!!
    * Should return the answer to the question: "does this function use the Flow 'flow'
    * by any means in any timestep?" This info is needed to find out if it is okey to delete the flow without causing
    * any problems. This function however, shall not perform any actual deletion, just check for any relations.
    * @param flow
    * @return
    */
  public boolean isRelatedToFlow(ID flow) {
    // josa
    // false -> true
    return true;
  }
}
