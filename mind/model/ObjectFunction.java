/*
 * Copyright 2004:
 * Urban Liljedahl <ul@sm.luth.se>
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
package mind.model;

import java.util.Iterator;
import java.util.Vector;
import mind.io.*;

/**
 * The ObjectFunction let the user define more than one goal function
 * 
 * @author Urban Liljedahl
 * @version 2004-10-20
 */

public class ObjectFunction {

	// Container class for bounds
	private class Bound {
		private String bound;

		private float limit1;

		private float limit2;

		public Bound() {
			bound = "off";
			limit1 = 0f;
			limit2 = 0f;
		}

		public String getBound() {
			return bound;
		}

		public float getLimit1() {
			return limit1;
		}

		public float getLimit2() {
			return limit2;
		}

		public void setBound(String type) {
			this.bound = type;
		}

		public void setLimit1(float limit1) {
			this.limit1 = limit1;
		}

		public void setLimit2(float limit2) {
			this.limit2 = limit2;
		}
	}

	private class Function {

		private Vector bounds;

		private float k1;

		private float k2;

		private String label;

		Function() {
			bounds = new Vector();
			// add bound needed for the TOP timestep
			bounds.add(new Bound());
		}

		public void addTimestep() {
			bounds.add(new Bound());
		}

		public String getBound(int timestep) {
			String str = null;
			Bound b = (Bound) bounds.get(timestep);
			str = b.getBound();
			return str;
		}

		public float getK1() {
			return k1;
		}

		public float getK2() {
			return k2;
		}

		public String getLabel() {
			return label;
		}

		public float getLimit1(int timestep) {
			Bound b = (Bound) bounds.get(timestep);
			return b.getLimit1();
		}

		public float getLimit2(int timestep) {
			Bound b = (Bound) bounds.get(timestep);
			return b.getLimit2();
		}

		/**
		 * @return number of timesteps including TOP
		 */
		public int getNrOfTimesteps() {
			return bounds.size();
		}

		public void removeLastTimestep() {
			bounds.remove(bounds.size() - 1);
		}

		public void setBound(String value, int timestep) {
			Bound b = (Bound) bounds.get(timestep);
			b.setBound(value);
		}

		public void setK1(float value) {
			k1 = value;
		}

		public void setK2(float value) {
			k2 = value;
		}

		public void setLabel(String str) {
			label = str;
		}

		public void setLimit1(float value, int timestep) {
			Bound b = (Bound) bounds.get(timestep);
			b.setLimit1(value);
		}

		public void setLimit2(float value, int timestep) {
			Bound b = (Bound) bounds.get(timestep);
			b.setLimit2(value);
		}

		public String toXML(int indent) {
			String xml = "";

			xml = XML.indent(indent) + "<label>" + getLabel() + "</label>"
					+ XML.nl();
			xml += XML.indent(indent) + "<k1>" + getK1() + "</k1>" + XML.nl();
			xml += XML.indent(indent) + "<k2>" + getK2() + "</k2>" + XML.nl();

			for (int i = 0; i < getNrOfTimesteps(); i++)
				xml += XML.indent(indent + 1) + "<boundstring>"
						+ sym2ascii(getBound(i)) + "</boundstring>" + XML.nl();
			for (int i = 0; i < getNrOfTimesteps(); i++)
				xml += XML.indent(indent + 1) + "<limit1value>" + getLimit1(i)
						+ "</limit1value>" + XML.nl();
			for (int i = 0; i < getNrOfTimesteps(); i++)
				xml += XML.indent(indent + 1) + "<limit2value>" + getLimit2(i)
						+ "</limit2value>" + XML.nl();

			return xml;
		}

	}

	//container class
	private class Tuple {

		String label;

		Variable var;
	}

	private static ObjectFunction c_thisInstance;

	/* Support method for inner class 
	 * Changes all &lt; and &gt; to < and >
	 */
	private static String ascii2sym(String str) {
		String temp = "";
		int index;
		if ((index = str.indexOf("&lt;")) > -1){
			temp = str.substring(index) + "<" + str.substring(index + 4,str.length());
		}else if ((index = str.indexOf("&gt;")) > -1){
			temp = str.substring(index) + ">" + str.substring(index + 4,str.length());
		}else 
			temp = new String(str);
			
		return temp;
	}

	/**
	 * A solution that makes it possible for Source to access the values in
	 * ObjectFunction
	 */
	public static ObjectFunction getInstance() {
		return c_thisInstance;
	}

	/* Support method for inner class 
	 * Changes all < to &lt; and > to &gt;
	 */
	private static String sym2ascii(String str) {
		String temp = "";
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '>')
				temp += "&gt;";
			else if (str.charAt(i) == 60)
				temp += "&lt;";
			else
				temp += str.charAt(i);
		}
		return temp;
	}

	private Vector c_functions;
	private Vector c_variableCollection;

	/**
	 * Creates a new Object Function
	 *  
	 */
	public ObjectFunction() {
		c_functions = new Vector();
		c_thisInstance = this;
		c_variableCollection = new Vector();
	}

	/**
	 * Creates a new Object Function
	 * @param needsDefaultFunction If the function needs to have a default
	 * 	function. Used when the user creates a new model.							 
	 */
	public ObjectFunction(boolean needsDefaultFunction) {
		c_functions = new Vector();
		c_thisInstance = this;
		c_variableCollection = new Vector();
		if (needsDefaultFunction) {
			Function fun = addObjectFunction("default", 1f, 1f, "off", 0f, 0f);
			int nrOfTimesteps = fun.getNrOfTimesteps();
			for (int i = 1; i < nrOfTimesteps; i++) {
				fun.setBound("off", i);
				fun.setLimit1(0f, i);
				fun.setLimit2(0f, i);
			}
		}
	}

	
	/**
	 * Adds boundary equations belonging to a certain timestep
	 * to the equation control
	 * 
	 * @param control The control
	 * @param timestep The timestep to add to
	 * @param node A fake node needed to create the equations ID:s
	 * @param functionID A fake function needed to create the equations ID:s
	 */
	private void addEquationOfTimestep(EquationControl control, int timestep,
			ID node, ID functionID) {
		int n = c_functions.size();
	
		for (int i = 0, id = 0; i < n; i++, id++) {
			Function function = (Function) c_functions.get(i);
			if (!function.getBound(timestep).equals("")
					&& !function.getBound(timestep).equals("off")) {
				//create an equation
				Equation eq = new Equation();
				Equation eq2 = new Equation();
				boolean interval = false;
				//a fake node ID is created...
				eq.setID(node, functionID, timestep, id);
				//create one more equation for the right side
				//increment id in order to generate unique equation id
				id++;
				eq2.setID(node, functionID, timestep, id);
	
				if (function.getBound(timestep).equals("L1<b")) {
					eq.setOperator(Equation.GREATEROREQUAL);
					eq.setRHS(function.getLimit1(timestep));
				} else if (function.getBound(timestep).equals("b=L2")) {
					eq.setOperator(Equation.EQUAL);
					eq.setRHS(function.getLimit2(timestep));
				} else if (function.getBound(timestep).equals("b<L2")) {
					eq.setOperator(Equation.LOWEROREQUAL);
					eq.setRHS(function.getLimit2(timestep));
				} else if (function.getBound(timestep).equals("L1<b<L2")) {
					interval = true;
					eq.setOperator(Equation.GREATEROREQUAL);
					eq.setRHS(function.getLimit1(timestep));
	
					eq2.setOperator(Equation.LOWEROREQUAL);
					eq2.setRHS(function.getLimit2(timestep));
				}
				
				//load equation with matching variables
				String label = function.getLabel();
				if (timestep == 0) {
					// TOP timestep
					int nr = c_variableCollection.size();
					for (int index = 0; index < nr; index++) {
						int m = ((Vector) c_variableCollection.get(index)).size();
						for (int j = 0; j < m; j++) {
							Tuple tuple = (Tuple) ((Vector) c_variableCollection
									.get(index)).get(j);
	
							if (tuple.label.equalsIgnoreCase(label)) {
								//add this variable
								eq.addVariable(tuple.var);
								if (interval)
									eq2.addVariable(tuple.var);
							}
						}
					}
					control.add(eq);
					if (interval)
						control.add(eq2);
	
				} else {
					int m = ((Vector) c_variableCollection.get(timestep - 1))
							.size();					
					for (int j = 0; j < m; j++) {
						Tuple tuple = (Tuple) ((Vector) c_variableCollection
													.get(timestep - 1)).get(j);
	
						if (tuple.label.equalsIgnoreCase(label)) {
							//add this variable
							eq.addVariable(tuple.var);
							if (interval)
								eq2.addVariable(tuple.var);
						}
					}
					control.add(eq);
					if (interval)
						control.add(eq2);
				}
			}
		}
	}
	
	/**
	 * Create an function and add values to the TOP timestep.
	 * 
	 * @param label
	 *            The label and identifier of the specific function
	 * @param k1
	 *            The first scaling factor
	 * @param k2
	 *            The second scaling factor
	 * @param bound 
	 * 			  The bound type
	 * @param limit1
	 * 			first limit for the bound
	 * @param limit2
	 * 			Second limit for the bound	
	 */
	public Function addObjectFunction(String label, float k1, float k2,
			String bound, float limit1, float limit2) {
		Function current = new Function();
		c_functions.add(current);
		current.setLabel(label);
		current.setK1(k1);
		current.setK2(k2);		
		current.setLimit1(limit1, 0);
		current.setBound(bound, 0);		
		current.setLimit2(limit2, 0);	
		return current;
	}

	/**
	 * Create an function and add values, called from RmdSAXParser via Model
	 * 
	 * @param label
	 *            The label and identifier of the specific function
	 * @param k1
	 *            The first scaling factor
	 * @param k2
	 *            The second scaling factor
	 * @param bound 
	 * 			  The bound type
	 * @param limit1
	 * 			first limit for the bound
	 * @param limit2
	 * 			Second limit for the bound	
	 */
	public Function addObjectFunction(String label, float k1, float k2,
			Vector bounds, Vector limits1, Vector limits2) {
		Function current = new Function();
		c_functions.add(current);
		current.setLabel(label);
		current.setK1(k1);
		current.setK2(k2);
		// set values on TOP timestep
		float l1 = ((Float) limits1.get(0)).floatValue();
		current.setLimit1(l1, 0);
		String bound =(String)bounds.get(0);
		current.setBound(bound, 0);
		float l2 = ((Float) limits2.get(0)).floatValue();
		current.setLimit2(l2, 0);
		
		for (int i = 1; i < bounds.size(); i++) {
			current.addTimestep();
			l1 = ((Float) limits1.get(i)).floatValue();
			current.setLimit1(l1, i);
			bound =(String)bounds.get(i);
			current.setBound(bound, i);
			l2 = ((Float) limits2.get(i)).floatValue();
			current.setLimit2(l2, i);
		}		
		return current;
	}
	
	/**
	 * Adds tuples to the collection. During call of the
	 * getEquationControl method. (MPS export)
	 * @param tuple The tuple to add
	 * @param timestep The timestep to add the tuple to
	 */
	
	private void addToCollection(Tuple tuple, int timestep) {
		//check if c_variableCollection is long enough
		while (c_variableCollection.size() < timestep)
			c_variableCollection.add(new Vector());
		((Vector) c_variableCollection.get(timestep - 1)).add(tuple);
	}

	/**
	 * Adds variables to the equation specified by its label. During call of the
	 * getEquationControl method. (MPS export)
	 */
	public void addVariable(Variable var, String label, int timestep) {
		//System.out.println("Add variable timestep="+timestep);
		Tuple tuple = new Tuple();
		tuple.var = var;
		tuple.label = label;
		addToCollection(tuple, timestep);
	}

	/**
	 * @param levelToChange
	 * @param levelID
	 * @param steps
	 */
	public void changeTS(Timesteplevel levelToChange, int levelID, int steps) {
		// Do we need to do anything???
		//System.out.println("changeTS..." + steps);
	}

	/**
	 * Model starts to clear the variableCollection before collecting them from
	 * Source instances
	 */
	public void clearVarCollection() {
		c_variableCollection.clear();
	}

	/**
	 * @param index
	 *            Index of the function
	 */
	public String getBound(int index, int timestep) {
		return ((Function) c_functions.get(index)).getBound(timestep);
	}

	/**
	 * Add all equations produced by boundary in the ObjectFunction
	 * 
	 * @return An EquationControl object loaded with all equations
	 */
	public EquationControl getEquationControl(int max) {
		EquationControl control = new EquationControl();
		ID fakeNode = new ID("N");
		ID functionID = new ID("Fu");
		if(max == 1) 
			max = 0; // adjust for the TOP timestep
		//build all equations
		for (int timestep = 0; timestep <= max; timestep++) {
			addEquationOfTimestep(control, timestep, fakeNode, functionID);
		}
		return control;
	}

	/**
	 * @return returns the index of the functions with a matching identifier.
	 *         The return value is -1 if no match exist.
	 */
	public int getIndex(String label) {
		for (int i = 0; i < c_functions.size(); i++) {
			if (((Function) c_functions.get(i)).getLabel() != null
					&& ((Function) c_functions.get(i)).getLabel()
							.equalsIgnoreCase(label))
				return i;
		}
		return -1;
	}

	/**
	 * @param index
	 *            Index of the function
	 */
	public float getK1(int index) {
		return ((Function) c_functions.get(index)).getK1();
	}

	/**
	 * @param index
	 *            Index of the function
	 */
	public float getK2(int index) {
		return ((Function) c_functions.get(index)).getK2();
	}

	/**
	 * @param index
	 *            Index of the function
	 */
	public String getLabel(int index) {
		return ((Function) c_functions.get(index)).getLabel();
	}

	/**
	 * @param index
	 *            Index of the function
	 */
	public float getLimit1(int index, int timestep) {
		return ((Function) c_functions.get(index)).getLimit1(timestep);
	}

	/**
	 * @param index
	 *            Index of the function
	 */
	public float getLimit2(int index, int timestep) {
		return ((Function) c_functions.get(index)).getLimit2(timestep);
	}

	/**
	 * Return a Vector of labels to the source-dialog object
	 * 
	 * @return Vector of labels
	 */
	public Vector getObjectFunctionLabels() {
		Vector labels = new Vector();
		int n = numberOfFunctions();
		for (int i = 0; i < n; i++) {
			labels.add(((Function) c_functions.get(i)).getLabel());
		}
		return labels;
	}

	/**
	 * @return Get the number of functions.
	 */
	public int numberOfFunctions() {
		return c_functions.size();
	}

	/**
	 * @param str The label of the function to remove 
	 */
	public void removeFunction(String str) {
		int index = getIndex(str);
		if (index > -1)
			c_functions.remove(index);
	}

	/**
	 * Removes the last timestep on all functions
	 */
	private void removeLastTimestep() {
		for (Iterator iter = c_functions.iterator(); iter.hasNext();) {
			Function func = (Function) iter.next();
			func.removeLastTimestep();
		}
	}

	/**
	 * @param levelToRemove
	 */
	public void removeTS(Timesteplevel levelToRemove) {
		if (levelToRemove.getBottomLevel().equals(levelToRemove)) {
			// 	We are going to remove the bottom level
			//	Remove the last timesteps 
			int n = levelToRemove.getTimesteps();
			for (int i = 0; i < n; i++) {
				removeLastTimestep();
			}
		}
	}

	/**
	 * @param steps
	 *            changes the function to allow more timsteps
	 */
	public void setMoreDetailedTS(int steps) {
		for (Iterator iter = c_functions.iterator(); iter.hasNext();) {
			Function element = (Function) iter.next();
			int currNrOfTS = element.getNrOfTimesteps();
			//adjust for the TOP timestep
			if (currNrOfTS != 1)
				currNrOfTS--;
			int newNrOfTs = currNrOfTS * steps;
			int nrToAdd = newNrOfTs - element.getNrOfTimesteps() + 1;
			for (int i = 0; i < nrToAdd; i++) {
				element.addTimestep();
			}
			// DEBUG  
			System.out.println("Antal tidssteg: " + element.getNrOfTimesteps());
		}
	}

	/**
	 * Set values to a function identified by the label. Use a created function
	 * if the label exists, else create a new function.
	 * 
	 * @param label
	 *            The label and identifier of the specific function
	 * @param k1
	 *            The first scaling factor
	 * @param k2
	 *            The second scaling factor
	 * @param bound
	 * 			  Specifies the boundary type	
	 * @param limit1
	 *            The first limit for the boundary
	 * @param limit2
	 * 			  The second limit for the boundary
	 * @param timestep
	 *            The timestep on which the boundary should be set
	 */
	public void setObjectFunctionValues(String label, float k1, float k2,
			String bound, float limit1, float limit2, int timestep) {
		//Set Object Function Values and limits at a specified timestep
		int index = getIndex(label);
		if (index == -1) {//create a new function
			Function fun = new Function();
			int nrTS = ((Function) c_functions.firstElement())
					.getNrOfTimesteps();
			for (int i = 0; i < nrTS - 1; i++) {
				fun.addTimestep();
			}
			c_functions.add(fun);
			index = c_functions.size() - 1;
		}
		Function current = (Function) c_functions.get(index);
		current.setLabel(label);
		current.setK1(k1);
		current.setK2(k2);
		current.setBound(bound, timestep);
		current.setLimit1(limit1, timestep);
		current.setLimit2(limit2, timestep);
	}

	/**
	 * Save object function in rmd-format
	 */
	public String toXML(int indent) {
		String xml = "";
		int numberOfFunctions = c_functions.size();
		for (int i = 0; i < numberOfFunctions; i++) {
			Function func = ((Function) c_functions.get(i));
			xml = xml + XML.indent(indent) + "<objectfunction>" + XML.nl();
			xml = xml + func.toXML(indent + 1);
			xml = xml + XML.indent(indent) + "</objectfunction>" + XML.nl();
		}
		xml += XML.nl();
		return xml;
	}

	/**
	 * @return
	 */
	public int getNrOfTimesteps() {		
		return ((Function) c_functions.get(0)).getNrOfTimesteps();
	}
}