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
 * 
 * Copyright 2007:
 * Daniel Källming <danka053@student.liu.se>
 * David Karlslätt <davka417@student.liu.se>
 * Freddie Pintar <frepi150@student.liu.se>
 * Mårten Thurén <marth852@student.liu.se>
 * Per Fredriksson <perfr775@student.liu.se>
 * Ted Palmgren <tedpa175@student.liu.se>
 * Tor Knutsson <torkn754@student.liu.se>
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

/******************
 *  N O T E
 *
 * vid vidareskickning av timesteplevel till funktioner
 * kolla om timesteplevel är större än Max
 * o sätt om isf.
 */
package mind.model;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.tree.*;
import java.util.LinkedList;
import java.util.Iterator;

import mind.gui.*;
import mind.gui.dnd.*;
import mind.io.*;

/**
 * This class contains the state of the whole model (excluding the
 * graphics). Contains all nodes, flows, functions and resources in
 * the model.
 *
 * @author Tim Terlegård
 * @author Peter Andersson
 * @author Johan Trygg
 * @author Freddie Pintar
 * @author Tor Knutsson
 * @version 2007-12-12
 */

public class Model implements UserSettingConstants {
	private NodeFlowTable c_connections;

	private static FlowControl c_flowControl;

	private NodeControl c_nodeControl;

	private static ResourceControl c_resourceControl;

	private Timesteplevel c_topTimesteplevel;

	private Timesteplevel c_lastTimesteplevel;

	private int c_timesteplevels;

	private ObjectFunction c_objectFunction;
        // Added by Nawzad Mardan 07-06-01
        private DiscountedsystemcostControl c_discountedsystemcostControl;
        private boolean c_discountedsystemcostExecutetable = false;

	/**
	 * Creates a node-flow table that knows how the flows and nodes
	 * are connected to each other. Controls are created that will
	 * contain all nodes, flows and resources.
	 */
	public Model() {
		c_connections = new NodeFlowTable();
		c_flowControl = new FlowControl();

		c_topTimesteplevel = new Timesteplevel("TOP", 1, null, null);
		Vector lengthsVector = new Vector(1);
		Vector namesVector = new Vector(1);
		lengthsVector.add(new Float(1.));
		namesVector.add("TOP");
		c_topTimesteplevel.setLengthsVector(lengthsVector);
		c_topTimesteplevel.setNamesVector(namesVector);
		c_topTimesteplevel.setTopLevel(c_topTimesteplevel);
		c_lastTimesteplevel = c_topTimesteplevel;
		c_timesteplevels = 1;

		c_resourceControl = new ResourceControl();
                // Added bay Nawzad Mardan 070601
                c_discountedsystemcostControl = new DiscountedsystemcostControl();

		// NodeInteraction is used by NodeControl and FunctionCollection
		NodeInteraction interaction = new NodeInteraction(c_resourceControl,
				c_topTimesteplevel);
		FunctionCollection collection = new FunctionCollection(interaction);
		// this functioncollection only needs to be set once
		FunctionControl.setFunctionCollection(collection);

		try {
			c_nodeControl = new NodeControl(interaction);
		} catch (ModelException e) {
			System.out.println(e);
			e.printStackTrace(System.out);
			//System.exit(1);
		}
		c_objectFunction = new ObjectFunction();
		//Reset the counters in the ID class
		ID.reset();
	}

	/**
	 * Creates a node-flow table that knows how the flows and nodes
	 * are connected to each other. Controls are created that will
	 * contain all nodes, flows and resources.
	 */
	public Model(boolean needsDefaultObjectFunction) {
		c_connections = new NodeFlowTable();
		c_flowControl = new FlowControl();

		c_topTimesteplevel = new Timesteplevel("TOP", 1, null, null);
		Vector lengthsVector = new Vector(1);
		Vector namesVector = new Vector(1);
		lengthsVector.add(new Float(1.));
		namesVector.add("TOP");
		c_topTimesteplevel.setLengthsVector(lengthsVector);
		c_topTimesteplevel.setNamesVector(namesVector);
		c_topTimesteplevel.setTopLevel(c_topTimesteplevel);
		c_lastTimesteplevel = c_topTimesteplevel;
		c_timesteplevels = 1;

		c_resourceControl = new ResourceControl();
                // Added bay Nawzad Mardan 070601
                 c_discountedsystemcostControl = new DiscountedsystemcostControl();

		// NodeInteraction is used by NodeControl and FunctionCollection
		NodeInteraction interaction = new NodeInteraction(c_resourceControl,
				c_topTimesteplevel);
		FunctionCollection collection = new FunctionCollection(interaction);
		// this functioncollection only needs to be set once
		FunctionControl.setFunctionCollection(collection);

		try {
			c_nodeControl = new NodeControl(interaction);
		} catch (ModelException e) {
			System.out.println(e);
			e.printStackTrace(System.out);
			//System.exit(1);
		}
		//added 2004-01-07
		c_objectFunction = new ObjectFunction(needsDefaultObjectFunction);

		//Reset the counters in the ID class
		ID.reset();
	}
        
        /**Get all Nodes from the modell
         *@param void
         *@return all nodes
         *Added by Nawzad Mardan 070801
         *used by Discountedsystemcost function
         */
         
         public NodeControl getAllNodes()
         {
             return c_nodeControl;
         }
         

	/**
	 * Creates and adds a flow to the model
	 * @param fromNode The node that is the flow's source
	 * @param toNode The node that is the flow's destination
	 * @return The ID of the new flow
	 */
	public ID addFlow(ID fromNode, ID toNode) {
		ID newFlow = null;
		try {
			newFlow = c_flowControl.addFlow();
			if (newFlow != null)
				c_connections.addFlow(newFlow, fromNode, toNode);
			return newFlow;
		} catch (ModelException e) {
			e.printStackTrace(System.out);
		}
		return newFlow;
	}

	/**
	 * Creates and adds a flow to the model with a specific ID
	 * @param flowIDstr The ID of the flow as a string
	 * @param fromNode The node that is the flow's source
	 * @param toNode The node that is the flow's destination
	 * @return The ID of the new flow
	 */
	public ID addFlowWithID(String flowIDstr, ID fromNode, ID toNode) {
		ID newFlow = null;
		try {
			newFlow = c_flowControl.addFlow(flowIDstr);
			if (newFlow != null)
				c_connections.addFlow(newFlow, fromNode, toNode);
			return newFlow;
		} catch (ModelException e) {
			e.printStackTrace(System.out);
		}
		return newFlow;
	}

	/**
	 * Adds a node folder to the database.
	 * @param folder The name of the new folder to add.
	 * @param parent The parent node of the new folder.
	 */
	public void addFolderToDatabase(String folder, NodeToDrag parent) {
		c_nodeControl.addFolderToDatabase(folder, parent);
	}

	/**
	 * Adds a function to a node.
	 *
	 * @param nodeID The node ID.
	 * @param functionType The functiontype to add.
	 * @throws ModelException is thrown when FIXME.
	 */
	public void addFunction(ID nodeID, String functionType)
			throws ModelException {
		c_nodeControl.addFunction(nodeID, functionType);
	}

	/**
	 * Creates and adds a node to the model.
	 * @param nodeType The type of the node that will be created
	 * @return The ID of the new node
	 */
	public ID addNode(String nodeType) throws ModelException, RmdParseException {
		ID newNode = c_nodeControl.addNode(nodeType);
		return newNode;
	}

	/**
	 * Creates and adds a node to the model with the ID nodeID.
	 * @param nodeID The nodeID as a string
	 * @return The ID of the new node
	 */
	public ID addNodeWithID(String nodeID) throws ModelException {
		ID newNode = c_nodeControl.addNodeWithID(nodeID);
		return newNode;
	}

	/**
	 * Creates and adds a node to the model.
	 * @param node The node to be copied.
	 * @return The ID of the new node.
	 */
	public ID addNode(Node node) throws ModelException, RmdParseException {
		if (node instanceof Node) {
			//System.out.println("instance of node");
			ID newNode = c_nodeControl.addNode((Node) node);
			//System.out.println("we have old node " + ((Node)node).getID());
			//System.out.println("we have a newnode " + newNode);

			return newNode;
		}
		//System.out.println("null from nodecontrol");
		return null;
	}

	/**
	 * Adds a node to the database as a new nodetype.
	 * @param nodeID The ID of the node to add.
	 * @param node The name of the new nodetype to be stored in database.
	 * @param folder The folder that the node will be stored in.
	 */
	public void addNodeToCollection(ID nodeID, String node, NodeToDrag folder)
			throws ModelException {
		c_nodeControl.addNodeToCollection(nodeID, node, folder);
	}

	/**
	 * Creates a resource.
	 * @param label The label of the resource that will be added
	 */
	public void addResource(String label, String unit, String prefix,
			ExtendedColor color) {
		c_resourceControl.addResource(label, unit, prefix, color);
	}

	/**
	 * Adds a timesteplevel to the model with userdefined names and lengths
	 * For backwards compatibility this function can be called with zero-length
	 * names and lengths vectors, in this case, default vectors are constructed
	 * automatically
	 * @param label The label of the new timesteplevel
	 * @param steps Number of steps that this timestep divides the previous one
	 */
	public boolean addTimesteplevel(String label, int steps, Vector names,
			Vector lengths, boolean fromfile) {
		//Check so a timesteplevel with the same label doesn't already exist
		Timesteplevel level = c_topTimesteplevel;
		while (level != null) {
			if (label.equals(level.getLabel())) {
				return false;
			}
			level = level.getNextLevel();
		}
		//Add the timesteplevel
		Timesteplevel newlevel = new Timesteplevel(label, steps);
		newlevel.setPrevLevel(c_lastTimesteplevel);

		if (names.size() == 0) {
			Vector lengthsVector = new Vector();
			Vector namesVector = new Vector();
			int counter = 1;
			for (int i = 0; i < c_lastTimesteplevel.getTimesteps(); i++) {
				for (int j = 0; j < steps; j++) {
					float length = ((Float) c_lastTimesteplevel
							.getLengthsVector().get(i)).floatValue();
					length = length / steps;
					lengthsVector.add(new Float(length));
					namesVector.add(label + counter);
					System.out.println(label + counter + " length = " + length);
					counter++;
				}
			}
			newlevel.setLengthsVector(lengthsVector);
			newlevel.setNamesVector(namesVector);
		} else {
			newlevel.setLengthsVector(lengths);
			newlevel.setNamesVector(names);
		}

		// End of default lengthsVector calculation
		c_lastTimesteplevel.setNextLevel(newlevel);
		c_lastTimesteplevel = newlevel;

		c_timesteplevels++;
		if(!fromfile)
			c_objectFunction.setMoreDetailedTS(steps);
		return true;
	}

	/**
	 * Changes a timesteplevel to the model
	 * For backwards compatibility this function can be called with zero-length
	 * names and lengths vectors, in this case, default vectors are constructed
	 * automatically
	 * @param levelID The timesteplevel index (0 -> MAX)
	 * @param label The label of the new timesteplevel
	 * @param steps Number of steps that this timestep divides the previous one
	 */
	public boolean changeTimesteplevel(int levelID, String label, int steps,
			Vector names, Vector lengths) {
		//System.out.println("TRACE: in changeTimesteplevel");
		Timesteplevel levelToChange = null;
		int oldNumberOfSteps = getTimesteplevel(levelID).getTimesteps();

		//Check so that another timesteplevel with this name do not already exist
		Timesteplevel tsl = c_topTimesteplevel;
		int i = 0;
		while (tsl != null) {
			if (levelID == i)
				levelToChange = tsl; //find out which timesteplevel we want to change
			if (label.equals(tsl.getLabel()) && levelID != i)
				return false;
			tsl = tsl.getNextLevel();
			i++;
		}

		//Find out what we must change (and make the changes)
		if (!label.equals(levelToChange.getLabel())) {
			levelToChange.setLabel(label);
		}
		if (levelToChange.getTimesteps() != steps) {
			c_nodeControl.changeTimesteplevel(levelToChange, steps); //Update all nodes
			levelToChange.setMaxTimesteps(steps);
		}

		if ((names.size() == 0)
				&& (steps != getTimesteplevel(levelID).getTimesteps())) {
			/* If names and lengths  have zero length at the same time as steps is changed
			 I don't know what to do */
			javax.swing.JOptionPane.showMessageDialog(null, "Serious error #1");
			return false;
		}

		// Set the new name and length vector
		if (names.size() != 0) {
			getTimesteplevel(levelID).setNamesVector(names);
			getTimesteplevel(levelID).setLengthsVector(lengths);
		} else { // Create default vectors
			Vector lengthsVector = new Vector();
			Vector namesVector = new Vector();
			Vector aboveVector = getTimesteplevel(levelID - 1)
					.getLengthsVector();
			float size = steps * aboveVector.size();

			int counter = 1;
			for (i = 0; i < getTimesteplevel(levelID - 1).getLengthsVector()
					.size(); i++) {
				for (int j = 0; j < steps; j++) {
					float length = ((Float) (aboveVector.get(i))).floatValue();
					length = length / steps;
					lengthsVector.add(new Float(length));
					namesVector.add(label + counter);
					System.out.println(label + counter + " length = " + length);
					counter++;
				}
			}
			getTimesteplevel(levelID).setNamesVector(namesVector);
			getTimesteplevel(levelID).setLengthsVector(lengthsVector);
		}

		/* if the the divisor (step) has been changed we must do this:
		 This will also reset the lengths of all descending timestep levels */
		if (steps != oldNumberOfSteps) {
			levelToChange = getTimesteplevel(levelID).getNextLevel();
			while (levelToChange != null) {
				levelToChange.adjustNumTimesteps();
				levelToChange = levelToChange.getNextLevel();
			}
		}

		// Now adjust the step lengths for each level
		levelToChange = c_topTimesteplevel.getNextLevel(); // don't have to adjust the top level
		while (levelToChange != null) {
			levelToChange.adjustStepLengths();
			levelToChange = levelToChange.getNextLevel();
		}
		c_objectFunction.changeTS(levelToChange, levelID, steps);
		return true;
	}

	/**
	 * Closes the model and saves the database (does not save the model
	 * @throws ModelException if shutdown unsuccessful
	 */
	public void close() throws ModelException {
		c_nodeControl.close();
	}



        private void mergeVectors(Vector toVect, Vector fromVect)
        {
          if (toVect != null && fromVect != null) {
            Iterator it = fromVect.iterator();

            while (it.hasNext()) {
              toVect.add(it.next());
            }
          }
        }

	/**
	 * Removes a node or flow from the model.
	 * @param id ID of the node/flow that will be removed
         * @param deleted (in/out) A vector of IDs indicating which components that were deleted successfully
         * @param problems (in/out) A vector of Strings explaining which components that were not deleted successfully an the reason to this
	 */
        public void deleteComponent(ID id, Vector deleted, Vector problems) {

          Vector tmpProblems = new Vector();

          if (id.isFlow()) {
            //System.out.println("Removed the flow " + id);
            tmpProblems = c_nodeControl.getFunctionsRelatedToFlow(id, null);

            if (tmpProblems.size() == 0) {
              c_connections.remove(id);
              c_flowControl.removeFlow(id);
              deleted.add(id);
            } else {
              mergeVectors(problems, tmpProblems);
              /* The user is trying to remove a flow that is used by some functions.
                 These functions must be changed or deleted in order to permit removing of
                 the flow */
            }
          }

          else if (id.isNode()) {
            tmpProblems = c_nodeControl.getFunctionsRelatedToNode(id, getInFlows(id), getOutFlows(id));

            if (tmpProblems.size() == 0) {
              c_nodeControl.removeNode(id);
              deleted.add(id);
            } else {
              mergeVectors(problems, tmpProblems);
               /* The user i trying to remove a node that has node connections that are
                  used by functions that not reside within this node */
            }
          }
        }

	/**
	 * Removes a function from a node.
	 * @param nodeID The node that the function should be removed from
	 * @param functionLabel The function that should be removed
	 */
	public void deleteFunction(ID nodeID, String functionLabel)
			throws ModelException {
		removeFunction(nodeID, functionLabel);
	}

	/**
	 * Gets all functions labels that belongs to the specified node.
	 * @param nodeID The ID of the node to get the function labels from.
	 * @return A Vector with function labels.
	 */
	public Vector getAllFunctionLabels(ID nodeID) {
		return c_nodeControl.getAllFunctionLabels(nodeID);
	}

	/**
	 * Gets all available functions
	 * @return A vector consisting of Strings with the names
	 * of the available functions
	 */
	public Vector getAvailableFunctions() {
		return c_nodeControl.getAvailableFunctions();
	}

	/**
	 * Gives the most detailed timesteplevel that is used by all
	 * nodes in the model.
	 * @return Timesteplevel
	 */
	public Timesteplevel getMostDetailedTSLUsed() {
		Timesteplevel most_detailed_tsl = c_topTimesteplevel;
		Enumeration nodes = c_nodeControl.elements();
		Node node;
		Timesteplevel node_tsl;
		while (nodes.hasMoreElements()) {
			node = (Node) nodes.nextElement();
			node_tsl = node.getTimesteplevel();
			if (node_tsl.isMoreDetailed(most_detailed_tsl))
				most_detailed_tsl = node_tsl;
		}
		return most_detailed_tsl;
	}

	/**
	 * Gets all optimizationinformation from the model.
	 * Assumes all flows has resources set.
	 * @return An equationcontrol with all optimization info
	 * @throws ModelException when unable to optimize
	 */
	public EquationControl getEquationControl() throws ModelException {
		//   start to clear the variable collection in ObjectFunction
		c_objectFunction.clearVarCollection();
		EquationControl allEquations = new EquationControl();
		ID[] toFlows;
		ID[] fromFlows;
		Vector toFlowsVec;
		Vector fromFlowsVec;
		Ini inifile = new Ini();
		int maxTimesteps;

		String exportall = inifile
				.getProperty(MPS_SETTINGS_EXPORT_ALL_TIMESTEPS);
		if (exportall == null) {
			inifile.setProperty(MPS_SETTINGS_EXPORT_ALL_TIMESTEPS, "true");
			exportall = "true";
		}

		if (exportall.equals("true"))
			maxTimesteps = c_lastTimesteplevel.timestepDifference(c_topTimesteplevel);
		else
			maxTimesteps = getMostDetailedTSLUsed().timestepDifference(null);

		// Iterate over all nodes
		for (Enumeration e = c_nodeControl.elements(); e.hasMoreElements();)
        {
		Node node = (Node) e.nextElement();
        // Added by Nawzad Mardan 2007-07-07 if user select discount system cost from the model menu
        if(c_discountedsystemcostExecutetable)
         {
         node.setDiscountSystemParameter(c_discountedsystemcostControl);
         }
//                        node.setDiscountRun(true);
        toFlows = c_connections.getToFlows(node.getID());
		fromFlows = c_connections.getFromFlows(node.getID());
		toFlowsVec = new Vector(0);
		fromFlowsVec = new Vector(0);
		if (toFlows != null)
            for (int i = 0; i < toFlows.length; i++)
                toFlowsVec.addElement(c_flowControl.get(toFlows[i]));
		if (fromFlows != null)
			for (int i = 0; i < fromFlows.length; i++)
				fromFlowsVec.addElement(c_flowControl.get(fromFlows[i]));

		EquationControl nodeEquations = node.getEquationControl(toFlowsVec,
					fromFlowsVec, maxTimesteps);

		// Merge with result
		allEquations.mergeWith(nodeEquations);
		}
		//	add object-function boundary inequalities
		EquationControl objEquations = c_objectFunction.getEquationControl(maxTimesteps);
		allEquations.mergeWith(objEquations);

		return allEquations;
	}

	/**
	 * Gets a certain function from a node.
	 * @param nodeID The ID of the node the function belongs to.
	 * @param functionLabel The label of the function we want.
	 * @return The function asked for.
	 */
	public NodeFunction getFunction(ID nodeID, String functionLabel) {
		return c_nodeControl.getFunction(nodeID, functionLabel);
	}

	/**
	 * Gets the function type of the specified function.
	 * @param nodeID The ID of the node the function belongs to.
	 * @param functionLabel The label of the function we want the type of.
	 * @return The function type.
	 */
	public String getFunctionType(ID nodeID, String functionLabel) {
		return c_nodeControl.getFunctionType(nodeID, functionLabel);
	}

	public Flow[] getAllFlows() {
		ID[] FlowIDs;
		Flow[] Flows;

		FlowIDs = ID.getAllFlowIDs();
		if (FlowIDs == null || FlowIDs.length == 0)
			return null;

		Flows = new Flow[FlowIDs.length];
		for (int i = 0; i < FlowIDs.length; ++i)
			Flows[i] = (Flow) c_flowControl.get(FlowIDs[i]);

		return Flows;
	}

	/**
	 * Gets all in flows of the node with node ID nodeID
	 * @param nodeID the ID of the node
	 * @return An Array with Flows
	 */
	public Flow[] getInFlows(ID nodeID) {
		ID[] inFlowIDs;
		Flow[] inFlows;

		inFlowIDs = c_connections.getToFlows(nodeID);
		if (inFlowIDs == null || inFlowIDs.length == 0)
			return null;

		inFlows = new Flow[inFlowIDs.length];
		for (int i = 0; i < inFlowIDs.length; ++i)
			inFlows[i] = (Flow) c_flowControl.get(inFlowIDs[i]);

		return inFlows;
	}

	/**
	 * Gets all out flows of the node with node ID nodeID
	 * @param nodeID the ID of the node
	 * @return An Array with Flows
	 */
	public Flow[] getOutFlows(ID nodeID) {
		ID[] outFlowIDs;
		Flow[] outFlows;

		outFlowIDs = c_connections.getFromFlows(nodeID);
		if (outFlowIDs == null || outFlowIDs.length == 0)
			return null;

		outFlows = new Flow[outFlowIDs.length];
		for (int i = 0; i < outFlowIDs.length; ++i)
			outFlows[i] = (Flow) c_flowControl.get(outFlowIDs[i]);

		return outFlows;
	}

	/**
	 * Gets the label of a certain component.
	 * @param componentID The ID of the component.
	 * @return The label of this component.
	 */
	public String getLabel(ID componentID) {
		String result = new String();

		if (componentID.isNode())
			result = c_nodeControl.getLabel(componentID);
		else if (componentID.isFlow())
			result = c_flowControl.getLabel(componentID);
		else if (componentID.isResource())
			result = c_resourceControl.getLabel(componentID);
		else {
			System.out.println("getLabel called with unknown ID type.");
			return null;
		}

		if (result == null)
			System.out.println("getLabel returned null. "
					+ "This means an internal error.");
		return result;

	}

	/**
	 * Gets the notes of a certain node.
	 * @param componentID The ID of the component.
	 * @return The note of this component.
	 */
	public String getNote(ID componentID) {
		String result = new String();

		if (componentID.isNode())
			result = c_nodeControl.getNote(componentID);
		else {
			System.out.println("getNote called with unknown ID type.");
			return null;
		}

		if (result == null)
			System.out.println("getNote returned null. "
					+ "This means an internal error.");
		return result;

	}

	/**
	 * Return an enumeration of node keys.
	 * @return Enumeration Enumeration of nodes.
	 */
	public Enumeration getNodes() {
		return c_nodeControl.keys();
	}

	/**
	 * Gets the last (finest) timesteplevel in the model
	 * @return A Timesteplevel object
	 */
	public Timesteplevel getLastTimesteplevel() {
		return c_lastTimesteplevel;
	}

	/**
	 * Gets a node from the model
	 * @param nodeID The ID of the node to get
	 * @return The whole node with the ID nodeID
	 */
	public Object getNode(ID nodeID) {
		return c_nodeControl.getNode(nodeID);
	}

	/**
	 * Creates a Tree of all available NodeTypes as strings and returns it
	 * @return A complete tree of all NodeTypes in folders
	 */
	public DefaultMutableTreeNode getNodeTree() {
		return c_nodeControl.getNodeTree();
	}

	/**
	 * Gets a resource from somewhere down below
	 * @param resource The ID of the flow/Resource to get the resource from.
	 * @return A resource object, null if not found.
	 */
	public static Resource getResource(ID resource) {
		if (resource.isFlow()) {
			resource = c_flowControl.getResource(resource);
			if (resource != null)
				return c_resourceControl.getResource(resource);
			else
				return null;
		} else if (resource.isResource()) {
			return c_resourceControl.getResource(resource);
		} else {
			return null;
		}
	}

	/**
	 * Gets a resource using the resources label
	 * @param label The label of the resource to get
	 */
	public ID getResourceID(String label) {
		return c_resourceControl.getResourceID(label);
	}

	/**
	 * Gets all resources.
	 * @return All resources available.
	 */
	public Vector getResources() {
		return c_resourceControl.getResources();
	}

	/**
	 * Gets the timesteplevel of the node with ID nodeID
	 * @param nodeID the ID of the node
	 * @return the Timesteplevel
	 */
	public Timesteplevel getTimesteplevel(ID nodeID) {
		return c_nodeControl.getTimesteplevel(nodeID);
	}

	/**
	 * Gets the timesteplevel with the number level
	 * @param level the level number
	 * @return the Timesteplevel
	 */
	public Timesteplevel getTimesteplevel(int level) {
		Timesteplevel tsl = c_topTimesteplevel;
		for (int i = 0; i < level; i++) {
			if (tsl == null) {
				javax.swing.JOptionPane.showMessageDialog(null,
						"null in getTimesteplevel");
				return null;
			}
			tsl = tsl.getNextLevel();
		}
		return tsl;
	}

	/**
	 * Get a timesteplevel from the model
	 * @param label The timesteplevel label
	 */
	public Timesteplevel getTimesteplevel(String level) {
		Timesteplevel tsl = c_topTimesteplevel;
		while (tsl != null) {
			if (level.equals(tsl.getLabel()))
				return tsl;
			tsl = tsl.getNextLevel();
		}
		return null;
	}

	/**
	 * Gets the number of timesteplevels in the model
	 * @return A Integer
	 */
	public int getTimesteplevels() {
		return c_timesteplevels;
	}

	/**
	 * Gets the name of the timestep levels.
	 * @return A vector of strings, where each string is a timestep level.
	 */
	public Vector getTimesteplevelsVector() {
		return c_topTimesteplevel.getAllLevels();
	}

	/**
	 * Gets the top (max) timesteplevel from the model
	 * @return The top Timesteplevel
	 */
	public Timesteplevel getTopTimesteplevel() {
		return c_topTimesteplevel;
	}

	/**
	 * Adds a function of type functionType to node nodeID and
	 * initiates the the function to the valuse found in data.
	 *
	 * @param nodeID the node ID
	 * @param functionType type of function
	 * @param data A linked list with data.
	 */
	public void parseAndAddFunction(ID nodeID, String functionType,
			LinkedList data) throws ModelException, RmdParseException {
		c_nodeControl.parseAndAddFunction(nodeID, functionType, data,
				c_resourceControl);
	}

	/**
	 * Removes a function from a node.
	 * @param nodeID The ID of the node.
	 * @param function The function to remove.
	 */
	public void removeFunction(ID nodeID, String function)
			throws ModelException {
		c_nodeControl.removeFunction(nodeID, function);
	}

	/**
	 * Removes a resource.
	 * @param label The label of the resource that will be removed
	 */
	public ID removeResource(ID resourceID) throws ModelException {
		return c_resourceControl.removeResource(resourceID);
	}

	/**
	 * Removes a timesteplevel from the model and all nodes and functions
	 * @param levelToRemove The level to remove
	 */
	public void removeTimesteplevel(Timesteplevel levelToRemove) {
		if (levelToRemove == c_topTimesteplevel) {
			//We cannot remove the toplevel
			return;
		}

		//Remove the timesteplevel from every function
		c_nodeControl.removeTimesteplevel(levelToRemove);

		//Change the nextLevel pointer at the previous level
		Timesteplevel prevLevel = levelToRemove.getPrevLevel();
		prevLevel.setNextLevel(levelToRemove.getNextLevel());

		if (levelToRemove == c_lastTimesteplevel) {
			//We are removeintg the last level, change c_lastTimesteplevel
			c_lastTimesteplevel = c_lastTimesteplevel.getPrevLevel();
		} else {
			//We are not removing the last level, we must
			//change the prevLevel pointer on the next level
			// and change the division number for the next timesteplevel
			Timesteplevel nextLevel = levelToRemove.getNextLevel();
			nextLevel.setPrevLevel(levelToRemove.getPrevLevel());
			nextLevel.setMaxTimesteps(nextLevel.getMaxTimesteps()
					* levelToRemove.getMaxTimesteps());
		}

		c_timesteplevels--;
		c_objectFunction.removeTS(levelToRemove);
	}

	/**
	 * Sets the label of a certain flow.
	 * @param flowID The ID of the flow.
	 * @return The label that should be set on this flow.
	 */
	public void setFlowLabel(ID flowID, String label) throws ModelException {
		c_flowControl.setLabel(flowID, label);
	}

	/**
	 * Sets the label of a function to something else.
	 * @param nodeID The ID of the node the function belongs to.
	 * @param functionLabel The label of the function to change the label of.
	 * @param newFunctionLabel The new label the function will have.
	 */
	public void setFunctionLabel(ID nodeID, String functionLabel,
			String newFunctionLabel) {
		c_nodeControl.setFunctionLabel(nodeID, functionLabel, newFunctionLabel);
	}

	/**
	 * Sets the label of a certain node.
	 * @param nodeID The ID of the node.
	 * @return The label that should be set on this node.
	 */
	public void setNodeLabel(ID nodeID, String label) {
		c_nodeControl.setLabel(nodeID, label);
	}

	/**
	 * Sets the note of a certain node.
	 * @param nodeID The ID of the node.
	 */
	public void setNodeNote(ID nodeID, String note) {
		c_nodeControl.setNote(nodeID, note);
	}

	public void setOptimization(Vector optimizeData) {
		for (int i = 0; i < optimizeData.size(); i++) {
			Object[] elem = (Object[]) optimizeData.elementAt(i);

			if (elem[1] instanceof FlowOptimization) {
				try {
					c_flowControl.setOptimization((ID) elem[0],
							(FlowOptimization) elem[1]);
				} catch (ModelException e) {
					System.out.println(e);
					e.printStackTrace(System.out);
					//System.exit(1);
				}
			}
			if (elem[1] instanceof EquationOptimization) {
				/**
				 * Finns ingen setOptimization i EquationControl
				 *
				 try {
				 c_equationControl.setOtimization((ID)elem[0], elem[1]);
				 }
				 catch(ModelException e) {
				 System.out.println(e);
				 e.printStackTrace(System.out);
				 System.exit(1);
				 }
				 */
			}
		}
	}

	/**
	 * Sets a resource for something
	 * @param where Where to set the resource
	 * @param what What resource to set
	 */
	public void setResource(ID where, ID what) {
		if (where.isFlow()) {
			c_flowControl.setResource(where, what);
		}
		;
	}

	/**
	 * Sets the timestep level of a certain node.
	 * @param nodeID ID of the node to change timestep level of.
	 * @param label The label of the timestep to change to.
	 */
	public void setTimesteplevel(ID nodeID, String label) {
		c_nodeControl.setTimesteplevel(nodeID, getTopTimesteplevel().getLevel(
				label));
	}

	/**
	 * Creates an object function.
	 * @param label The label of the resource that will be added
	 */
	public void addObjectFunction(String label, float k1, float k2,
			Vector bounds, Vector limits1, Vector limits2) {
		c_objectFunction.addObjectFunction(label, k1, k2, bounds, limits1, limits2);
	}

	/**
	 * Returns the object function if it exists. Added 2004-01-07
	 */
	public ObjectFunction getObjectFunction() {
		return c_objectFunction;
	}
        
        /**
         *Added by Nawzad Mardan 2007-06-31
	 * Returns the DiscountedsystemcostControl
	 */
        public DiscountedsystemcostControl getDiscountedsystemcostControl()
        {
            return c_discountedsystemcostControl.getDiscountedsystemcostControl();
        }

       /**
        *Added by Nawzad Mardan 2007-06-07
        * Sets the rate, period analyses, tabelHeader and the table data in the DiscountedsystemcostControl
        */
    
       public void setDiscountedsystemcostControl(Float rate, Long year, String head [], Object [][] data, Vector timestepValues)
        {
          c_discountedsystemcostControl = new DiscountedsystemcostControl(rate, year , head, data, timestepValues);
          if((rate != 0) && (year != 1))
            c_discountedsystemcostExecutetable = true;
          
        } 
        
       /**
        *Added by Nawzad Mardan 2008-12-12 Reads from rmd file
        * Sets the rate, period analyses, tabelHeader and the table data in the DiscountedsystemcostControl
        */
    
       public void setDiscountedsystemcostParameter(Float rate, Long year, String head [], Object [][] data, Vector timestepsVector)
        {
          c_discountedsystemcostControl = new DiscountedsystemcostControl(rate, year , head, data, timestepsVector);
          if((rate != 0) && (year != 1))
          c_discountedsystemcostExecutetable = true;
        } 
	/**
	 * Return a Vector of labels to the source-dialog object
	 * @return Vector of labels
	 */
	public Vector getObjectFunctionLabels() {
		return c_objectFunction.getObjectFunctionLabels();
	}

	public String toXML(int indent) {
		String xml = c_objectFunction.toXML(indent)
				+ c_resourceControl.toXML(indent) + XML.nl()
				+ c_topTimesteplevel.toXML(indent) + XML.nl()
				+ c_nodeControl.toXML(c_resourceControl, indent) + XML.nl()
				+ c_flowControl.toXML(c_connections, c_resourceControl, indent)+ XML.nl()
                                + c_discountedsystemcostControl.toXML(indent);
                // c_discountedsystemcostControl.toXML(indent) Added by Nawzad 20081211

		return xml;
	}
	
	/**
	 * Orders the nodeControl to create EXML data.
	 * Added by PUM5 2007-12-12
	 * @param idVector Vector of node ID:s.
	 * @param locked Wether lock should be used or not.
	 * @param indent Current indent level.
	 * @return EXML data
	 */
	public String toEXML(Vector idVector, boolean locked, int indent) {
		String xml = 
				c_nodeControl.toEXML(idVector, locked, c_resourceControl, indent); 
		return xml;
	}
	
	/**
	 * Returns the nodeControl.
	 * Added by PUM5 2007-12-12
	 * @return nodeControl
	 */
	public NodeControl getNodeControl()
	{
		return c_nodeControl;
	}

	/**
	 * Returns the resourceControl
	 * Added by PUM5 2007-12-12
	 * @return resourceControl
	 */
	public ResourceControl getResourceController()
	{
		return c_resourceControl;
	}
}