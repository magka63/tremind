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
 * Mårten Thurén <marth852@student.liu.se>  *
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
import java.util.Hashtable;

import mind.io.*;
import mind.model.*;

/**
 * The function FlowRelation
 * 
 * @author Johan Trygg
 * @author Freddie Pintar
 * @author Tor Knutsson
 * @version 2007-12-12
 */

public class FlowRelation extends NodeFunction implements Cloneable {
	// These constants should have the same value as those in FlowRelationDialog
	public final static int NOT_SPECIFIED = 0;
	public final static int IN = 1;
	public final static int OUT = 2;
	public final static int FREE = 0;
	public final static int LESS = 1;
	public final static int EQUAL = 2;
	public final static int GREATER = 3;
	public final static int LESS_GREATER = 4;

	private int c_direction;
	private Resource c_resource;

	private int c_currentFlow;

	private Vector c_flowData; // One element for each timestep

	private Vector c_flowLabels; // Flow labels (same for all timesteps)
	private Vector c_flowIDs; // Flow ids (as Strings, NOT IDs!) (same for all
								// timesteps)

	class FlowData {
		public Vector operators;
		public Vector limits1;
		public Vector limits2;

		public FlowData(int size) {
			operators = new Vector(size);
			limits1 = new Vector(size);
			limits2 = new Vector(size);
			int i;
			for (i = 0; i < size; ++i) {
				operators.add(i, new Integer(FREE));
				limits1.add(i, new Float(0));
				limits2.add(i, new Float(0));
			}
		}

		public FlowData(Vector operators, Vector limits1, Vector limits2) { // don't
																			// ever
																			// write
																			// imits1...
			this.operators = operators;
			this.limits1 = limits1;
			this.limits2 = limits2;
		}

	}

	/**
	 * Creates an empty function
	 */
	public FlowRelation() {
		super(new ID(ID.FUNCTION), "FlowRelation", null); // Timesteplevel
															// must be set later

		c_direction = NOT_SPECIFIED;
		c_flowData = new Vector(5);
		c_flowIDs = new Vector(5);
		c_flowData.add(null); // used when running without timesteps
	}

	/**
	 * Returns true if all flows for the current timestep and current resource
	 * are free.
	 * 
	 * @return true or false
	 */
	public boolean allFree() {
		if (c_resource == null) // function is empty
			return true;

		int numFlows = getNumberOfFlows();
		int maxTimestep = getTimesteplevel().timestepDifference(null);
		for (int flow = 0; flow < numFlows; ++flow) {
			for (int timestep = 0; timestep < maxTimestep; ++timestep)
				if (getOperator(timestep, flow) != FREE)
					return false;
		}
		return true;
	}

	/**
	 * Creates a new copy of the function
	 * 
	 * @return A complete copy
	 */
	public Object clone() throws CloneNotSupportedException {
		FlowRelation clone = (FlowRelation) super.clone();

		if (c_resource == null)
			return clone; // nothing more to copy

		clone.c_flowIDs = new Vector(c_flowIDs);
		clone.c_flowData = new Vector(c_flowData);
		int i = 0;
		FlowData old_fd;
		Vector old_operators, old_limits1, old_limits2;
		int max_ts_level = getTimesteplevel().timestepDifference(null);
		try {
			for (i = 0; i < max_ts_level; ++i) {
				old_fd = null;
				old_fd = (FlowData) c_flowData.get(i);
				old_operators = old_fd.operators;
				old_limits1 = old_fd.limits1;
				old_limits2 = old_fd.limits2;
				clone.c_flowData.add(new FlowData(new Vector(old_operators),
						new Vector(old_limits1), new Vector(old_limits2)));
			}
		} catch (Exception e) { // FIXME This should not happen. Remove the code
			e.printStackTrace(System.out);
			System.out.println(e);
			System.out.println("Cloning failed! " + "i=" + i
					+ "c_flowData.size()=" + c_flowData.size());
		}

		return clone;

	}

	/**
	 * Gets the direction return IN, OUT, or NOT_SPECIFIED
	 */
	public int getDirection() {
		return c_direction;
	}

	/**
	 * Gets the current flow index
	 * 
	 * @return flow index
	 */
	public int getFlowIndex() {
		return c_currentFlow;
	}

	/**
	 * Get the label of the current flow
	 * 
	 * @return label
	 */
	public String getFlowLabel() {
		return (String) c_flowLabels.get(c_currentFlow);
	}

	/**
	 * Returns an array of strings with all flow labels
	 * 
	 * @return flow labels
	 */
	public String[] getFlowLabels() {
		int numFlows = getNumberOfFlows();
		String flows[] = new String[numFlows];
		for (int i = 0; i < numFlows; i++) {
			flows[i] = (String) c_flowLabels.get(i);
		}
		return flows;
	}

	/**
	 * Returns the limit1 for the current flow and timestep
	 */
	public float getLimit1() {
		return getLimit1(c_currentFlow);
	}

	/**
	 * Returns the limit1 for the given flow and current timestep
	 * 
	 * @param flow
	 *            The flow
	 */
	public float getLimit1(int flow) {
		FlowData fd = (FlowData) c_flowData.get(getTimestep() - 1);
		return ((Float) fd.limits1.get(flow)).floatValue();
	}

	/**
	 * Returns the limit1 for the given flow and timestep
	 * 
	 * @param timestep
	 *            The timestep
	 * @param flow
	 *            The flow
	 */
	public float getLimit1(int timestep, int flow) {
		FlowData fd = (FlowData) c_flowData.get(timestep);
		return ((Float) fd.limits1.get(flow)).floatValue();
	}

	/**
	 * Returns the limit2 for the current flow and timestep
	 */
	public float getLimit2() {
		return getLimit2(c_currentFlow);
	}

	/**
	 * Returns the limit2 for the given flow and current timestep
	 * 
	 * @param flow
	 *            The flow
	 */
	public float getLimit2(int flow) {
		FlowData fd = (FlowData) c_flowData.get(getTimestep() - 1);
		return ((Float) fd.limits2.get(flow)).floatValue();
	}

	/**
	 * Returns the limit2 for the given flow and timestep
	 * 
	 * @param timestep
	 *            The timestep
	 * @param flow
	 *            The flow
	 */
	public float getLimit2(int timestep, int flow) {
		FlowData fd = (FlowData) c_flowData.get(timestep);
		return ((Float) fd.limits2.get(flow)).floatValue();
	}

	/**
	 * Returns the number of flows
	 */
	public int getNumberOfFlows() {
		return c_flowIDs.size();
	}

	/**
	 * Returns the operator for the current flow and timestep
	 */
	public int getOperator() {
		return getOperator(c_currentFlow);
	}

	/**
	 * Returns the operator for the given flow and current timestep
	 * 
	 * @param flow
	 *            The flow
	 */
	public int getOperator(int flow) {
		FlowData fd = (FlowData) c_flowData.get(getTimestep() - 1);
		return ((Integer) fd.operators.get(flow)).intValue();
	}

	/**
	 * Returns the operator for the given flow and timestep
	 * 
	 * @param timestep
	 *            The timestep
	 * @param flow
	 *            The flow
	 */
	public int getOperator(int timestep, int flow) {
		FlowData fd = (FlowData) c_flowData.get(timestep);
		return ((Integer) fd.operators.get(flow)).intValue();
	}

	/**
	 * Returns the resource for the function.
	 */
	public Resource getResource() {
		return c_resource;
	}

	/**
	 * Converts an operator constant to a string.
	 * 
	 * @param operator
	 *            The operator as a integer
	 * @return The operator as a string
	 */
	public String operator2string(int operator) {
		switch (operator) {
		case FREE:
			return new String("Free");
		case GREATER:
			return new String("Greater");
		case LESS:
			return new String("Less");
		case EQUAL:
			return new String("Equal");
		case LESS_GREATER:
			return new String("Less-Greater");
		case 10:
			return new String("Changed");
		case 20:
			return new String("Joind");
		case 30:
			return new String("Running");
		}
		return new String("Illegal operator");
	}

	/**
	 * Gives this FlowRelation the same values as fr has
	 * 
	 * @param fr
	 *            The flow relation with values to copy
	 */
	public void setAllData(FlowRelation fr) {
		setID(fr.getID());
		setLabel(fr.getLabel());
		setTimesteplevel(fr.getTimesteplevel());
		setTimestep(fr.getTimestep());

		c_direction = fr.c_direction;
		c_resource = fr.c_resource;
		c_currentFlow = fr.c_currentFlow;
		c_flowIDs = fr.c_flowIDs;
		c_flowLabels = fr.c_flowLabels;
		c_flowData = fr.c_flowData;
	}

	/**
	 * Sets the direction for the function
	 * 
	 * @param direction
	 *            IN, OUT, or NOT_SPECIFIED
	 */
	public void setDirection(int direction) {
		c_direction = direction;
		c_resource = null;
	}

	/**
	 * Sets the current flow index for the function
	 * 
	 * @param flowIndex
	 *            new flow index
	 */
	public void setFlowIndex(int flowIndex) {
		c_currentFlow = flowIndex;
	}

	/**
	 * Update this function's flows with new flows. If a flow exists in this
	 * function but not in flows[] it will be deleted from this function. If a
	 * flow exists in flows[] but not in this function it will be addad to the
	 * function and get the operator FREE.
	 * 
	 * @param flows[]
	 *            The new flows
	 */
	public void setFlows(Flow flows[]) {
		if (c_resource == null) // This shouldn't happen (I think)
			return;

		// First remove all flows thar don't have c_resource as resource
		Vector new_flowIDs = new Vector(10);
		Vector new_flowLabels = new Vector(10);
		for (int i = 0; i < flows.length; ++i) {
			if ((c_resource.getID().equals(flows[i].getResource()))) {
				new_flowIDs.add(flows[i].getID().toString());
				new_flowLabels.add(flows[i].getLabel());
			}
		}

		if (new_flowIDs.isEmpty())
			c_currentFlow = -1;
		else
			c_currentFlow = 0; // Select the first

		int max_level = getTimesteplevel().timestepDifference(null);
		int level;
		for (level = 0; level < max_level; ++level) {

			FlowData fd = new FlowData(new_flowIDs.size());
			FlowData old_fd = (FlowData) c_flowData.get(level);
			c_flowData.set(level, fd);

			int j;

			for (int i = 0; i < c_flowIDs.size(); ++i) {
				j = new_flowIDs.indexOf(c_flowIDs.get(i));
				if (j != -1) {
					// The flow already exists, use the old flowData post
					fd.operators.set(j, (Integer) old_fd.operators.get(i));
					fd.limits1.set(j, (Float) old_fd.limits1.get(i));
					fd.limits2.set(j, (Float) old_fd.limits2.get(i));
				}
			}
		}

		c_flowIDs = new_flowIDs;
		c_flowLabels = new_flowLabels;
	}

	/**
	 * Set the limit 1 for the current flow and timestep
	 * 
	 * @param limit
	 *            The new limit
	 */
	public void setLimit1(float limit) {
		FlowData fd = (FlowData) c_flowData.get(getTimestep() - 1);
		fd.limits1.set(c_currentFlow, new Float(limit));
	}

	/**
	 * Set the limit 2 for the current flow and timestep
	 * 
	 * @param limit
	 *            The new limit
	 */
	public void setLimit2(float limit) {
		FlowData fd = (FlowData) c_flowData.get(getTimestep() - 1);
		fd.limits2.set(c_currentFlow, new Float(limit));
	}

	/**
	 * Sets the operator for the current flow and timestep
	 * 
	 * @param operator
	 *            The new operator
	 */
	public void setOperator(int operator) {
		FlowData fd = (FlowData) c_flowData.get(getTimestep() - 1);
		fd.operators.set(c_currentFlow, new Integer(operator));
	}

	/**
	 * Sets the resource for the function.
	 * 
	 * @param r
	 *            The new resource
	 */
	public void setResource(Resource r) {
		c_resource = r;
	}

	/***************************************************************************
	 * Inherited functions overridden
	 **************************************************************************/

	/**
	 * Returns optimizationinformation from source
	 * 
	 * @param maxTimesteps
	 *            The maximum number of timesteps in the model
	 * @param node
	 *            The ID for the node that generates the equations
	 * @return Some equations that model the source's behaviour
	 * @throws ModelException
	 *             when resource does not exist
	 */
	public EquationControl getEquationControl(int maxTimesteps, ID node,
			Vector toFlows, Vector fromFlows) throws ModelException {
		EquationControl control = new EquationControl();
		Equation eq, mainEq;
		Variable var;

		// First we update our c_flowIDs vector with the current
		// toFlows/fromFlows
		// (That is: remove all flows from c_flowsIDs that doesn't exist
		// in the model any longer, and add the new flows (which will be FREE))
		if (getDirection() == IN) {
			Flow f[] = new Flow[toFlows.size()];
			for (int i = 0; i < toFlows.size(); ++i)
				// is .toArray better, yes no?
				f[i] = (Flow) toFlows.get(i);
			setFlows(f);
		} else if (getDirection() == OUT) {
			Flow f[] = new Flow[fromFlows.size()];
			for (int i = 0; i < fromFlows.size(); ++i)
				f[i] = (Flow) fromFlows.get(i);
			setFlows(f);
		} else
			// direction is not set, so there are no flows
			return control;

		// Give a name for the flow relation "variable"
		String mainVarStr = new String("Fr");
		int flow, timestep;
		for (flow = 0; flow < getNumberOfFlows(); ++flow)
			mainVarStr = mainVarStr + (String) c_flowIDs.get(flow);

		// Loop over the timesteps
		int numTimesteps = getTimesteplevel().timestepDifference(null);
		int equation = 0;
		for (timestep = 0; timestep < maxTimesteps; ++timestep) {

			int ts = ((timestep * numTimesteps) / maxTimesteps) % numTimesteps;

			// Create the main flow relation equation
			mainEq = new Equation(node, getID(), timestep + 1, 0,
					Equation.EQUAL);
			var = new Variable(mainVarStr, timestep + 1, (float) 1.0);
			mainEq.addVariable(var);
			control.add(mainEq);

			// Loop over each flow
			for (flow = 0; flow < getNumberOfFlows(); ++flow) {
				int operator = getOperator(ts, flow);
				var = new Variable((String) c_flowIDs.get(flow), timestep + 1,
						(float) -1.0);
				mainEq.addVariable(var);

				String eqType;
				float lim;

				if (operator == FREE) {
					// No equations to create
					continue; // Skip the creations of equations
				} else if (operator == LESS) {
					eqType = Equation.LOWEROREQUAL;
					lim = (float) (-(double) getLimit2(ts, flow) / 100);
				} else if (operator == EQUAL) {
					eqType = Equation.EQUAL;
					lim = (float) (-(double) getLimit2(ts, flow) / 100);
				} else if (operator == GREATER) {
					eqType = Equation.GREATEROREQUAL;
					lim = (float) (-(double) getLimit1(ts, flow) / 100);
				} else { // operator == LESS_GREATER

					// Data for the second flow equation (created after else
					// statement)
					eqType = Equation.LOWEROREQUAL;
					lim = -((float) getLimit2(ts, flow)) / 100;

					// Create the first flow equation
					eq = new Equation(node, getID(), timestep + 1, ++equation,
							Equation.GREATEROREQUAL);
					control.add(eq);
					var = new Variable((String) c_flowIDs.get(flow),
							timestep + 1, 1);
					eq.addVariable(var);
					var = new Variable(mainVarStr, timestep + 1,
							(float) (-((double) getLimit1(ts, flow)) / 100));
					eq.addVariable(var);
				}

				// Create the equation for the flow
				eq = new Equation(node, getID(), timestep + 1, ++equation,
						eqType);
				control.add(eq);
				var = new Variable((String) c_flowIDs.get(flow), timestep + 1,
						1);
				eq.addVariable(var);
				var = new Variable(mainVarStr, timestep + 1, lim);
				eq.addVariable(var);
			}
		}

		return control;
	}

	/**
	 * Returns a short overview of the current equations of the function
	 * 
	 * @return A short overview of the equation returned by the function
	 */
	public String getOverview() {
		return "Overview for FlowRelation is not implemented";
	}

	public int getTimesteps() {
		return c_flowData.size();
	}

	/**
	 * parseData parses a linked list with data and initializes the class with
	 * the values found in the linked list. The function is used when loading a
	 * model or node from disk.
	 * 
	 * @param data
	 *            A linked list with data.
	 * @param rc
	 *            A control with all available resources.
	 * @param createMissingResources
	 *            If this is true then if data contains resources not found in
	 *            rc, these resources should be created and added to rc.
	 */
	public void parseData(LinkedList data, ResourceControl rc,
			boolean createMissingResource) throws RmdParseException {
		// Set timesteplevel
		c_timesteplevel = (Timesteplevel) data.removeLast();

		// Set label
		if (((String) data.getFirst()).equals("label")) {
			data.removeFirst(); // Throw away the tag
			setLabel((String) data.removeFirst());
		}

		String tmpstr;
		// Set direction
		if (((String) data.getFirst()).equals("direction")) {
			data.removeFirst(); // Throw away the tag
			tmpstr = (String) data.removeFirst();
			if (tmpstr.equals("In"))
				setDirection(IN);
			else if (tmpstr.equals("Out"))
				setDirection(OUT);
			else
				throw new RmdParseException(
						"The direction must be 'In' or 'Out'");
		} else
			throw new RmdParseException(
					"A Flow Relation must have a 'direction' tag");

		int maxTimestep = c_timesteplevel.timestepDifference(null);

		if (data.isEmpty()) {
			// This function has no flows
			setResource(null);
			c_flowData = new Vector(maxTimestep);
			for (int timestep = 0; timestep < maxTimestep; ++timestep) {
				c_flowData.add(new FlowData(0));
			}
			return;
		}

		// Set resource
		String resourceName = "";
		ID resourceID;
		if (((String) data.getFirst()).equals("resource.type")) {
			data.removeFirst(); // Throw away the tag
			resourceName = (String) data.removeFirst();
			resourceID = rc.getResourceID(resourceName);
			// Check if the resource exists
			if (resourceID == null) {
				if (createMissingResource)
					resourceID = rc.addResource(resourceName, "", "");// we do
																		// not
																		// know
																		// the
				// unit or prefix
				else
					throw new RmdParseException("The resource '" + resourceName
							+ "' is not defined.");
			}
			c_resource = rc.getResource(resourceID);
		} else
			throw new RmdParseException(
					"A Flow Relation must have a 'resource' tag");

		// Read through the data list and collect the flow ID's
		// (we need to know how many there are)
		LinkedList d = new LinkedList(data);
		Vector flowSet = new Vector();
		String idStr, tag = new String("");
		findFlows: while (!d.isEmpty()) {
			while (!((String) d.removeFirst()).equals("flowData")) {
				if (d.isEmpty())
					break findFlows;
			}

			d.removeFirst(); // remove the flow tag

			idStr = (String) d.removeFirst();
			if (!flowSet.contains(idStr))
				flowSet.add(idStr); // Save the flow id
		}

		int numFlows = flowSet.size();

		// Add flows to c_temp_flows
		c_flowIDs = new Vector(numFlows);
		for (int i = 0; i < numFlows; ++i)
			c_flowIDs.add((String) flowSet.get(i));

		c_flowData = new Vector(maxTimestep);
		FlowData fd = null;

		while (!data.isEmpty()) {
			tag = (String) data.getFirst();
			if (tag.equals("T")) {
				data.removeFirst(); // throw away the T tag

				fd = new FlowData(numFlows); // Create new flowData post
				c_flowData.add(fd);

				continue;
			}

			data.removeFirst(); // throw away the flowData tag

			data.removeFirst(); // throw away the flow ID tag
			tag = (String) data.removeFirst(); // read flow ID
			int flowIndex = flowSet.indexOf(tag);

			if (flowIndex < 0) {
				throw new RmdParseException("The flow with ID=\"" + tag
						+ "\" is not found!");
			}

			data.removeFirst(); // throw away the operator tag
			String operatorStr;
			operatorStr = (String) data.removeFirst(); // read operator

			data.removeFirst(); // Throw away limit tag
			String sLim = (String) data.removeFirst();
			float limit = (float) 0.0;

			try {
				limit = parseValue(sLim);
			} catch (NumberFormatException e) {
				throw new RmdParseException("The 'limit' field (" + sLim
						+ ") couldn't be parsed!");
			}
			if (limit < 0 || limit > 100)
				throw new RmdParseException("The 'limit' field (" + sLim
						+ ") must " + "be a value between 0 and 100 (%)");
			//altered By Pum5 2007 error handling added 
			if (operatorStr.equals("Less")) {
				fd.operators.set(flowIndex, new Integer(LESS));
				fd.limits1.set(flowIndex, new Float((float) 0.0));
				fd.limits2.set(flowIndex, new Float(limit));
				if (!data.isEmpty()&&data.getFirst().equals("limit"))
					throw new RmdParseException(
							"Operator Less can't have more than one limit");
			} else if (operatorStr.equals("Equal")) {
				fd.operators.set(flowIndex, new Integer(EQUAL));
				fd.limits1.set(flowIndex, new Float((float) 0.0));
				fd.limits2.set(flowIndex, new Float(limit));
				if (!data.isEmpty()&&data.getFirst().equals("limit"))
					throw new RmdParseException(
							"Operator Equal can't have more than one limit");
			} else if (operatorStr.equals("Greater")) {
				fd.operators.set(flowIndex, new Integer(GREATER));
				fd.limits1.set(flowIndex, new Float(limit));
				fd.limits2.set(flowIndex, new Float((float) 0.0));
				if (!data.isEmpty()&&data.getFirst().equals("limit"))
					throw new RmdParseException(
							"Operator Greater can't have more than one limit");
			} else if (operatorStr.equals("Less-Greater")) {
				fd.operators.set(flowIndex, new Integer(LESS_GREATER));
				fd.limits1.set(flowIndex, new Float(limit));
				if (!((String) data.removeFirst()).equals("limit"))
					throw new RmdParseException(
							"A 'Less-Greater' relation requires "
									+ "two 'limit' fields");
				try {
					limit = parseValue((String) data.removeFirst());
				} catch (NumberFormatException e) {
					throw new RmdParseException("The 'limit' field must "
							+ "be a float value 0 < x < 100");
				}
				if (limit < 0 || limit > 100)
					throw new RmdParseException("The 'limit' field must "
							+ "be an integer 0 < x < 100");

				fd.limits2.set(flowIndex, new Float(limit));
				if (!data.isEmpty()&&data.getFirst().equals("limit"))
					throw new RmdParseException(
							"Operator Greater can't have more than two limits");
			} else if (operatorStr.equals("Free")) {
				setOperator(FREE); // Already done
			} else
				throw new RmdParseException("'" + operatorStr
						+ "' is not a legal " + "operator");

		}
	}

	// adds a dot and zero if necessary and parse to float
	private float parseValue(String strValue) {
		// if( strValue.indexOf('.') == -1 )
		// strValue = strValue + ".0";
		return Float.parseFloat(strValue);
	}

	public String toXML(ResourceControl resource, int indent) {
		String xml = XML.indent(indent) + "<flowRelation>" + XML.nl();

		if (getLabel() != null)
			xml = xml + XML.indent(indent + 1) + "<label>"
					+ XML.toXML(getLabel()) + "</label>" + XML.nl();

		int maxTimestep = getTimesteplevel().timestepDifference(null);
		int maxFlows = getNumberOfFlows();
		int timestep, flow;

		int direction = getDirection();
		String dirStr;
		if (direction == OUT)
			dirStr = new String("Out");
		else
			// IN or NOT_SPECIFIED
			dirStr = new String("In");
		xml = xml + XML.indent(indent + 1) + "<direction>" + dirStr
				+ "</direction>" + XML.nl();

		if (getResource() != null) {
			xml = xml + XML.indent(indent + 1) + "<resource.type>"
					+ getResource().getLabel() + "</resource.type>" + XML.nl();

			for (timestep = 0; timestep < maxTimestep; timestep++) {
				xml = xml + XML.indent(indent + 1)
						+ "<timestep.flowRelation nr=\"" + (timestep + 1)
						+ "\">" + XML.nl();
				if (getResource() != null) { // There exists some flows with
												// resources
					for (flow = 0; flow < maxFlows; flow++) {

						int operator = getOperator(timestep, flow);
						if (operator == FREE)
							continue; // Don't write anything if the flow is
										// free

						xml = xml + XML.indent(indent + 2)
								+ "<flowData flow=\""
								+ ((String) c_flowIDs.get(flow)) + "\">"
								+ XML.nl();
						xml = xml + XML.indent(indent + 3) + "<operator>"
								+ operator2string(operator) + "</operator>"
								+ XML.nl();
						xml = xml + XML.indent(indent + 3) + "<limit>";
						if (operator == EQUAL || operator == LESS)
							xml = xml + getLimit2(timestep, flow);
						else if (operator == GREATER)
							xml = xml + getLimit1(timestep, flow);
						else { // (operator == LESS_GREATER)
							xml = xml + getLimit1(timestep, flow) + "</limit>"
									+ XML.nl() + XML.indent(indent + 3)
									+ "<limit>" + getLimit2(timestep, flow);
						}
						xml = xml + "</limit>" + XML.nl()
								+ XML.indent(indent + 2) + "</flowData>"
								+ XML.nl();
					}
				}
				xml = xml + XML.indent(indent + 1) + "</timestep.flowRelation>"
						+ XML.nl();
			}
		}

		xml = xml + XML.indent(indent) + "</flowRelation>" + XML.nl();

		return xml;
	}

	/**
	 * Puts EXML data in the given ExmlSheet
	 * PUM5 Added 2007-12-12
	 * @param resources A ResourceControl
	 * @param sheet The ExmlSheet to be changed 
	 */
	public void toEXML(ResourceControl resources, ExmlSheet sheet) 
	{

		// Find Label
		String label = ((this.getLabel() == null) ? "" : this.getLabel());

		// Add function header
		sheet.addFunctionHeader("FlowRelation", label);

		int direction = getDirection();
		sheet.addRow(sheet.addLockedCell("Direction")
				+ sheet.addCell(((direction == OUT) ? "Out" : "In")));
		if (getResource() != null) {
			sheet.addRow(sheet.addLockedCell("Resource")
					+ sheet.addCell(XML.toXML(getResource().getLabel())));

			// Add timestep nrs in one Row.
			int numberOfTimesteps = getTimesteps();
			sheet.addTimeStepRow(numberOfTimesteps);


			// Chekc maxflows
			int maxFlows = getNumberOfFlows();
			int operator;
			for (int flow = 0; flow < maxFlows; flow++) {

    			//Add Timestep data
    			//sheet.addRow(sheet.addLockedCell("Flow")+sheet.addCell(XML.toXML(((String)c_flowIDs.get(flow)))));
    			sheet.addRow(sheet.addLockedCell("Flow")+sheet.addCell("String", XML.toXML(((String)c_flowIDs.get(flow))), "", (numberOfTimesteps - 1)));
    			sheet.initTable(4, numberOfTimesteps);
    			sheet.addLockedTableValue("Operator");
    			sheet.addLockedTableValue("Min");
    			sheet.addLockedTableValue("Max");
    			sheet.addLockedTableValue("Equal");
    			for( int i = 0; i < numberOfTimesteps; i++){			
        			operator = getOperator(i,flow);
    				sheet.addTableValue(XML.toXML(operator2string(operator)));
    				if (operator == GREATER || operator == LESS_GREATER) {
    					sheet.addTableValue(getLimit1(i,flow));
    				} else {
    					sheet.addTableValue("-");
    				}
    				
    				if (operator == LESS || operator == LESS_GREATER) {
    					sheet.addTableValue(getLimit2(i,flow));
    				} else {
    					sheet.addTableValue("-");
    				}    				
    				
    				if (operator == EQUAL) {
    					sheet.addTableValue(getLimit2(i,flow));
    				} else {
    					sheet.addTableValue("-");
    				}
    				
    			}
    			sheet.endTable();
    		}	
    	}
    	sheet.addRow(sheet.addCell(""));
   }


	/*
	 * All these protected methods below are used for timesteps. These methods
	 * are abstract in NodeFunction and are called from there.
	 */

	protected void timestepInsertAt(int index) {
		FlowData fd = new FlowData(getNumberOfFlows());
		c_flowData.insertElementAt(fd, index);
	}

	protected void timestepRemoveAt(int index) {
		c_flowData.removeElementAt(index);
	}

	protected void timestepSetMoreDetailed(int factor) {
		int oldsize = c_flowData.size();
		int newsize = oldsize * factor;
		// int num_flows = getNumberOfFlows();

		// Copy old values to new vector
		Vector new_flowData = new Vector(newsize, 1);
		FlowData fd;
		for (int i = 0; i < oldsize; i++) {
			fd = (FlowData) c_flowData.get(i);
			for (int k = 0; k < factor; k++) {
				new_flowData.add(new FlowData(new Vector(fd.operators),
						new Vector(fd.limits1), new Vector(fd.limits2)));
			}
		}
		c_flowData = new_flowData;
	}

	protected void timestepSetLessDetailed(int newSize, int factor) {
		Vector new_flowData = new Vector(newSize);
		int i, oldindex;
		FlowData fd;

		// Copy old cost values to new cost array
		for (i = 0, oldindex = 0; i < newSize; i++, oldindex += factor) {
			fd = (FlowData) c_flowData.get(oldindex);
			new_flowData.add(new FlowData(new Vector(fd.operators), new Vector(
					fd.limits1), new Vector(fd.limits2)));
		}
		c_flowData = new_flowData;

	}

	protected void timestepResetData(int size) {
		c_flowData = new Vector(size);
		for (int i = 0; i < size; ++i)
			c_flowData.add(new FlowData(getNumberOfFlows()));
	}

	/**
	 * Abstract function declared by the class NodeFunction which is used to
	 * determine if a node function in any way is related to a specific flow.
	 * This information is needed when deleting flows in order to maintain
	 * consistency of the model.
	 * 
	 * @param flow
	 *            ID
	 * @return always returns false, since a FlowRelation function has no
	 *         references to flows.
	 */
	public boolean isRelatedToFlow(ID flow) {
		return false;
	}
}
