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

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

import mind.gui.dnd.NodeToDrag;
import mind.io.ExmlSheet;
import mind.io.RmdParseException;
import mind.io.XML;

/**
 * Keeps track of all nodes in the class.
 *
 * @author Peter Åstrand
 * @author Peter Andersson
 * @author Johan Trygg
 * @author Jonas Sääv
 * @author Tor Knutsson
 * @author Freddie Pintar
 * @version 2007-10-01
 */
public class NodeControl
    extends Hashtable {

    // Functiontypes.
    private static NodeInteraction c_database;

    /**
     * A constructor.
     */
    public NodeControl(NodeInteraction interaction)
	throws ModelException
    {
	c_database = interaction;
    }

    /**
     * Adds a function to a node.
     *
     * @param ID The node ID.
     * @param function The function to add.
     * @throws ModelException is thrown when FIXME.
     */
    public void addFunction(ID nodeID, String functionType)
	throws ModelException
    {
	Node node = (Node) get(nodeID);
	if (node == null) {
	    throw new ModelException();
	}
	node.addFunction(functionType);
    }

    /**
     * Adds a node folder to the database.
     * @param folder The name of the new folder to add.
     * @param parent The parent node of the new folder.
     */
    public void addFolderToDatabase(String folder, NodeToDrag parent)
    {
	c_database.addFolder(folder, parent);
    }

    /**
     * Adds a new node to the hash table.
     *
     * @param newNode The node to add.
     */
    public ID addNode(String nodeType)
	throws ModelException, RmdParseException
    {
	Node new_node = c_database.getNode(nodeType);
	ID node_id = new_node.getID();
	put(node_id, new_node);
	return node_id;
    }

    /**
     * Adds a new node with the ID nodeID.
     * @param newNode The ID as a String of the node
     * @return The node ID
     */
    public ID addNodeWithID(String nodeID)
	throws ModelException
    {
	ID node_id = null;
	try {
	    node_id = new ID(nodeID);
	}
	catch (IllegalArgumentException e) {
	    throw new ModelException("Cannot create node with the id '" +
				     nodeID + "'");
	}
	//FunctionControl fc = new FunctionControl();
	Node new_node = new Node(node_id);

	put(node_id, new_node);
	return node_id;
    }

    /**
     * Adds a new node to the hash table.
     * @param newNode The node to add.
     */
    public ID addNode(Node old_node)
	throws ModelException
    {
	//Node new_node = c_database.getNode(old_node.getType()); //fungerade inte så bra
	Node new_node = new Node(old_node);
	new_node.setTimesteplevel(old_node.getTimesteplevel());
	ID node_id = new_node.getID();
	put(node_id, new_node);
	return node_id;
    }

    /**
     * Adds a node to the database as a new nodetype.
     * @param nodeID The ID of the node to add.
     * @param nodeLabel The name of the new nodetype to be stored in database.
     * @param folder The folder that the node will be stored in.
     */
    public void addNodeToCollection(ID nodeID, String nodeLabel,
				    NodeToDrag folder)
	throws ModelException
    {
	Node newNode = null;
	Node node = (Node) get(nodeID);

	if (node != null) {
	    newNode = new Node(node);
	    newNode.setLabel(nodeLabel);
	    c_database.addNode(newNode, folder);
	}
    }

    /**
     * Closes the NodeControl and everything under it.
     * @throws ModelException if shutdown failed.
     */
    public void close()
	throws ModelException
    {
	//	c_database.close();
    }

    /**
     * Get all function IDs
     * @return A vector with IDs
     */
    //FIXME
    public Vector getAllFunctionIDs(ID nodeID)
    {
	Node node = (Node) get(nodeID);

	return node.getAllFunctionIDs();
    }

    /**
     * Get all function labels
     * @param nodeID The ID of the node
     *
     */
    public Vector getAllFunctionLabels(ID nodeID)
    {
	Node node = (Node) get(nodeID);

	return node.getAllFunctionLabels();
    }

    /**
     * Gets a certain function from a node.
     * @param nodeID The ID of the node the function belongs to.
     * @param functionLabel The label of the function we want.
     * @return The function asked for.
     */
    public NodeFunction getFunction(ID nodeID, String functionLabel)
    {
	Node node = (Node) get(nodeID);
	if (node != null)
	    return node.getFunction(functionLabel);
	else
	    return null;
    }

    public String getFunctionType(ID nodeID, String functionLabel)
    {
	Node node = (Node) get(nodeID);

	NodeFunction nf = node.getFunction(functionLabel);

	return nf.getFunctionType();
    }

    /**
     * Gets a node from the nodecontrol.
     * @param nodeID The ID of the node to get.
     * @return The node with the ID nodeID.
     */
    public Object getNode(ID nodeID)
    {
	 return (Node) get(nodeID);
    }

   /**
     * Gets the timestep level of a certain node.
     */
    public Timesteplevel getTimesteplevel(ID nodeID)
    {
	Node node = (Node) get(nodeID);
	if (node == null)
	    return null;

	return node.getTimesteplevel();
    }

    /**
     * Adds a function of type functionType to node nodeID and
     * initiats the the function to the valuse found in data.
     *
     * @param nodeID the node ID
     * @param functionType type of function
     * @param data A linked list with data.
     * @param rc A control with all available resources.
     */
    public void parseAndAddFunction(ID nodeID, String functionType,
				    LinkedList data, ResourceControl rc)
	throws ModelException, RmdParseException
    {
	Node node = (Node)get(nodeID);
	if (node == null) {
	    throw new ModelException();
	}
	node.parseAndAddFunction(functionType,data,rc,false); //false = do not create missing resources

    }

    /**
     * Remove a function from a node.
     * @param ID The node ID.
     * @param functionLabel The label of the function to be removed.
     * @throws ModelException is thrown when FIXME.
     */
    public void removeFunction(ID nodeID, String functionLabel)
	throws ModelException
    {
	Node n = (Node)get(nodeID);
	n.removeFunction(functionLabel);
	return;
    }

    /**
     * Remove node from hash table.
     * @param ID The node ID.
     */
    public void removeNode(ID nodeID)
    {
	remove(nodeID);
	nodeID.remove();
    }

    /**
     * Gets all available functions from the database and returns them as a Vector
     * @return Vector with functiontypes as Strings
     */
    public Vector getAvailableFunctions()
    {
	return c_database.getAvailableFunctions();
    }

    /**
     * Gets the label of a certain node.
     * @param nodeID The ID of the node.
     * @return The label of this node.
     */
    public String getLabel(ID nodeID)
    {
	Node node = (Node) get(nodeID);

	return node.getLabel();
    }

    /**
     * Gets the note of a certain node.
     * @param nodeID The ID of the node.
     * @return The label of this node.
     */
    public String getNote(ID nodeID)
    {
	Node node = (Node) get(nodeID);

	return node.getNote();
    }

    /**
     * Creates a Tree of all available NodeTypes as strings and returns it
     * @return A complete tree of all NodeTypes in folders
     */
    public DefaultMutableTreeNode getNodeTree()
    {
	return c_database.getNodeTree();
    }

    /**
     * Sets the label of a function to something else.
     * @param nodeID The ID of the node the function belongs to.
     * @param functionLabel The label of the function to change the label of.
     * @param newFunctionLabel The new label the function will have.
     */
    public void setFunctionLabel(ID nodeID, String functionLabel,
				 String newFunctionLabel)
    {
	Node node = (Node) get(nodeID);
	node.setFunctionLabel(functionLabel, newFunctionLabel);
    }

    /**
     * Sets the label of a certain node.
     * @param nodeID The ID of the node.
     * @return The label of this node.
     */
    public void setLabel(ID nodeID, String label)
    {
	Node node = (Node) get(nodeID);
	if (node == null)
	    return;
	node.setLabel(label);
    }

    /**
     * Set the note of a certain node.
     * @param nodeID The ID of the node.
     */
    public void setNote(ID nodeID, String note)
    {
	Node node = (Node) get(nodeID);
	if (node == null)
	    return;

	node.setNote(note);
    }

    /**
     * Retrives a vector with the labels of all functions in the model that
     * has references to a certain flowID. In order to delete a flow, all these
     * references must first be resolved.
     * @param flowID
     * @param nodeID  can be null.
     * @return a Vector of Strings containing function labels.
     */
    public Vector getFunctionsRelatedToFlow(ID flowID, ID nodeID)
    {
      Vector stringVector = new Vector();
      Enumeration allNodes = elements();
      Node node;

      /* Loop over all nodes */
      while (allNodes.hasMoreElements()) {
        node = (Node) allNodes.nextElement();

        Vector functionVector = node.getAllFunctionLabels();
        Iterator functionIterator = functionVector.iterator();

        /* Loop over all functions in a node and ask the functions if they are
           related to the flowID
           if that is the case, add it to the returning string vector*/
        while (functionIterator.hasNext()) {
          NodeFunction nf = (NodeFunction) node.getFunction((String) functionIterator.next());
          if (nf.isRelatedToFlow(flowID)) {
            if (nodeID != null) {
              if (nodeID != node.getID())
                stringVector.add(nf.getLabel() + " in node " + node.getID().toString() + " is using flow " + flowID.toString());

            } else {
              stringVector.add(nf.getLabel() + " in node " + node.getID().toString() + " is using flow " + flowID.toString());
            }
          }
        } // End loop over all functions i a node
      } /* end loop over all nodes */

      return stringVector;
    }

    /**
     * Retrieves a vector with the labels of all functions in the model that
     * has references to the flows associated to this node
     * @param nodeID
     * @return a Vector of Strings containing function labels.
     */
    public Vector getFunctionsRelatedToNode(ID nodeID, Flow[] inFlows, Flow[] outFlows)
    {
      /*
         getFunctionsRelatedToFlow for each flow. If the functions that are found
         all are contained with in this node it is permitted to delete the node, otherwise
         not */
      int i, j;
      Vector stringVector = new Vector();
      Vector tmpStringVector = new Vector();

      if (inFlows != null)
      for (i=0; i<inFlows.length; i++)
      {
        tmpStringVector = getFunctionsRelatedToFlow(inFlows[i].getID(), nodeID);

        Iterator it = tmpStringVector.iterator();
        // merge tmpStringVector with stringVector
        while (it.hasNext()) {
          stringVector.add(it.next());
        }

      }

      if (outFlows != null)
      for (i=0; i<outFlows.length; i++)
      {
        tmpStringVector = getFunctionsRelatedToFlow(outFlows[i].getID(), nodeID);
        Iterator it = tmpStringVector.iterator();
        // merge tmpStringVector with stringVector
        while (it.hasNext()) {
          stringVector.add(it.next());
        }

      }
      return stringVector;
    }

   /**
     * Sets the timestep level of a certain node.
     * @param nodeID ID of the node to change timestep level of.
     * @param label The label of the timestep to change to.
     */
    public void setTimesteplevel(ID nodeID, Timesteplevel level)
    {
	Node node = (Node) get(nodeID);
	if (node == null)
	    return;

	node.setTimesteplevel(level);
    }

    /**
     * Changes how many times the timesteplevel divides the above level
     * of all nodes in the nodeControl.
     * @param levelToChange the Timesteplevel to change
     * @param steps new number of steps (divides)
     */
    public void changeTimesteplevel(Timesteplevel levelToChange, int steps)
    {
	FunctionControl fc = null;
	Node n;

	// Iterate over all nodes
	for (Enumeration e = elements(); e.hasMoreElements();) {
	    n = (Node)e.nextElement();
	    int nLevelID = n.getTimesteplevel().toInt();
	    int changeLevelID = levelToChange.toInt();
	    if (nLevelID >= changeLevelID) {
		//Update all functions in this node
		fc = n.getAllFunctions();
		fc.changeTimesteplevel(levelToChange,steps);
	    }
	}
    }

    /**
     * Remove the timesteplevel levelToDelete from all nodes
     * in the nodeControl
     * @param levelToDelete The level to delete
     */
    public void removeTimesteplevel(Timesteplevel levelToDelete)
    {
	FunctionControl fc = null;
	Node n;

	// Iterate over all nodes
	for (Enumeration e = elements(); e.hasMoreElements();) {
	    n = (Node)e.nextElement();
	    int nLevelID = n.getTimesteplevel().toInt();
	    int deleteLevelID = levelToDelete.toInt();
	    if (nLevelID >= deleteLevelID) {
		//Update all functions in this node
		fc = n.getAllFunctions();
		fc.removeTimesteplevel(levelToDelete);
	    }
	    if (nLevelID == deleteLevelID) {
		//Change the selected timesteplevel of the node
		n.setTimesteplevel(levelToDelete.getPrevLevel());
	    }

	}

    }

    public String toXML(ResourceControl resources, int indent)
    {
	Collection nodes = values();
	if (nodes == null)
	    return "";
	Iterator iterator = nodes.iterator();
	String xml = "";
	Node node;

	while (iterator.hasNext()) {
	    node = (Node) iterator.next();
	    xml = node.toXML(resources, indent) + xml;
	}

	return xml;
    }
    
	/**
	 * Creates EXML data on Node level.
	 * Added by PUM5 2007-12-12
	 * @param idVector Vector of node ID:s.
	 * @param locked Wether lock should be used.
	 * @param resources A ResourceControl.
	 * @param indent Current indent level.
	 * @return The EXML data.
	 */
    public String toEXML(Vector idVector, boolean locked, ResourceControl resources, int indent)
    {
	Collection nodes = values();
	if (nodes == null)
	    return "";
	Iterator iterator = nodes.iterator();
	String xml = "";
	Node node;

	while (iterator.hasNext()) {
	    node = (Node) iterator.next();
	    if (idVector.contains(node.getID())) {
		    xml = XML.indent(indent) + 
		    	"<Worksheet ss:Name=\""+ExmlSheet.flapName(node.getID()+"-"+node.getLabel())+"\" ss:Protected=\""+(locked?"1":"0")+"\">"  + XML.nl() + 
		    	node.toEXML(node.getID()+"-"+node.getLabel(),resources, indent+1) + XML.nl() +
		    	XML.indent(indent+1) +"<WorksheetOptions xmlns=\"urn:schemas-microsoft-com:office:excel\">" + XML.nl() +
		    	XML.indent(indent +2) +"<PageSetup/>" + XML.nl() +
		    	XML.indent(indent +2) +"<Selected/>" + XML.nl() +
		    	XML.indent(indent +2) +"<Panes/>" + XML.nl() +
		    	XML.indent(indent +2) +"<ProtectObjects>True</ProtectObjects>" + XML.nl() +
		    	XML.indent(indent +2) +"<ProtectScenarios>True</ProtectScenarios>" + XML.nl() +
		    	XML.indent(indent +2) +"<EnableSelection>UnlockedCells</EnableSelection>" + XML.nl() +
		    	XML.indent(indent +2) +"<AllowSizeCols/>" + XML.nl() +
		    	XML.indent(indent +2) +"<AllowSizeRows/>" + XML.nl() +
		    	XML.indent(indent +1) +"</WorksheetOptions>" + XML.nl() +
		    	XML.indent(indent) +"</Worksheet> " + XML.nl() + 
		    	xml;
	    }
	}

	return xml;
    }
    
}


