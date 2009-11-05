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

/**
 * This class is instantiated when the GraphModel is created.
 * NodeFlowTable keeps track of what flows are coming into a node
 * and what flows are going out of a certain node.
 * Only IDs are kept in the table. The key field in the hashtable is
 * nodeID and the value field is a vector of flowIDs.
 *
 * @version 2001-04-08
 * @author Tim Terlegård
 */
package mind.model;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class NodeFlowTable
{
    // Hashtable with nodeID as the key. As value it has a vector with
    // the flows coming (pointing) out from the node.
    private Hashtable c_from;

    // Hashtable with nodeID as the key. As value it has a vector with
    // the flows coming (pointing) into the node.
    private Hashtable c_to;

    public NodeFlowTable()
    {
	c_from = new Hashtable(0);
	c_to = new Hashtable(0);
    }

    /**
     * Adds a flow and its two nodes to this node-flow-table.
     * @param flow The ID of the flow to add.
     * @param from The ID of the node that is the flow's source.
     * @param to The ID of the node that is the flow's destination.
     */
    public void addFlow(ID flow, ID from, ID to)
    {
	if (from.isNode() && to.isNode() && flow.isFlow()) {
	    // add the flow to the "from" hashtable
	    if (c_from.get(from) == null) {
		Vector flowVector = new Vector(0);
		flowVector.addElement(flow);
		c_from.put(from, flowVector);
	    }
	    else
		((Vector) c_from.get(from)).addElement(flow);

	    // add the flow to the "to" hashtable
	    if (c_to.get(to) == null) {
		Vector flowVector = new Vector(0);
		flowVector.addElement(flow);
		c_to.put(to, flowVector);
	    }
	    else
		((Vector) c_to.get(to)).addElement(flow);
	}
	else
	    System.out.println("NodeFlowTable: IDs are wrong");

        }



    /**
     * Gets the the flows and nodes that will be affected by removing
     * the specified flow or node.
     * @param componentID The ID of the flow or node that user
     * wants to remove.
     * @return IDs of the flows and nodes that are affected by the
     * removal.
     */
    public ID[] getAffectedByRemove(ID componentID)
    {
	return null;
    }

    /**
     * Gets all flows that flows out from a certain node.
     * @param nodeID The ID of the node.
     * @return The IDs of the flows that goes out from the node.
     */
    public ID[] getFromFlows(ID nodeID)
    {
	if (nodeID == null || !nodeID.isNode())
	    return null;

	ID[] flowIDs;
	Vector flowVector = (Vector) c_from.get(nodeID);

	if (flowVector != null) {
	    flowIDs = new ID[flowVector.size()];
	    flowVector.toArray(flowIDs);
	    return flowIDs;
	}
	else
	    return null;
    }

    /**
     * Gets all flows that flows (points) to a certain node.
     * @param nodeID The ID of the node.
     * @return The IDs of the flows that goes to the specified node.
     */
    public ID[] getToFlows(ID nodeID)
    {
	if (!nodeID.isNode())
	    return null;

	ID[] flowIDs;
	Vector flowVector = (Vector) c_to.get(nodeID);

	if (flowVector != null) {
	    flowIDs = new ID[flowVector.size()];
	    flowVector.toArray(flowIDs);
	    return flowIDs;
	}
	else
	    return null;
    }

    /**
     * Gets the node that a certain flow goes out from.
     * @param flowID The ID of the flow.
     * @return The ID of the node that is the specified flow's source.
     */
    public ID getFromNode(ID flowID)
    {
	if (!flowID.isFlow())
	    return null;

	Enumeration nodes = c_from.keys();
	Object node;
	Vector row;

	while (nodes.hasMoreElements()) {
	    node = nodes.nextElement();
	    row = (Vector) c_from.get(node);;
	    for (int i = 0; i < row.size(); i++)
		if (row.get(i).equals(flowID))
		    return (ID) node;
	}

	return null;
    }

    /**
     * Gets the node that a certain flow points at.
     * @param flowID The ID of the flow.
     * @return The ID of the node that is the specified flow's destination.
     */
    public ID getToNode(ID flowID)
    {
	if (!flowID.isFlow())
	    return null;

	Enumeration nodes = c_to.keys();
	Object node;
	Vector row;

	while (nodes.hasMoreElements()) {
	    node = nodes.nextElement();
	    row = (Vector) c_to.get(node);;
	    for (int i = 0; i < row.size(); i++)
		if (row.get(i).equals(flowID))
		    return (ID) node;
	}

	return null;
    }

    public void remove(ID componentID)
    {
	Enumeration fromEnum;
	Enumeration toEnum;
	Vector flowVector;
	boolean found = false;

	if (componentID.isFlow()) {
	    fromEnum = c_from.elements();
	    toEnum = c_to.elements();

	    while(fromEnum.hasMoreElements() && !found) {
		flowVector = (Vector) fromEnum.nextElement();
		if (flowVector.contains(componentID)) {
		    flowVector.removeElement(componentID);

		    // a flow can only have one from-node so we don't
		    // have to search any further
		    found = true;
		}
	    }
	    // if we didnt find the flow in from-table we don't have
	    // to search further. A flow always has two ends and two nodes
	    if (!found)
		return;
	    found = false;

	    while(toEnum.hasMoreElements() && !found) {
		flowVector = (Vector) toEnum.nextElement();
		if (flowVector.contains(componentID)) {
		    flowVector.removeElement(componentID);

		    // a flow can only have one from-node so we don't
		    // have to search any further
		    found = true;
		}
	    }
	}
	else if (componentID.isNode()) {
	    c_from.remove(componentID);
	    c_to.remove(componentID);
	}
    }
}
