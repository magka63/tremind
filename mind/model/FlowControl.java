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
package mind.model;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

/**
 * A hashtable containing all flows in the model.
 *
 * @author Peter Åstrand
 * @version 2001-04-01
 */
public class FlowControl
    extends Hashtable
{
    /**
     * A nullconstructor
     */
    public FlowControl()
    {

    }

    /**
     * Adds a flow a returns its ID.
     *
     * @param flowResource The flow to add.
     * @throws ModelException is thrown when FIXME.
     */
    public ID addFlow(ID flowResource)
	throws ModelException
    {
	ID flow_id = new ID(ID.FLOW);
	FlowOptimization flow_optimization = new FlowOptimization();
	Flow new_flow = new Flow(flow_id, flow_id.toString(), flowResource,
				 flow_optimization);
	put(flow_id, new_flow);
	return flow_id;
    }

    /**
     * Adds a flow a returns its ID (without resource)
     *
     * @throws ModelException is thrown when FIXME.
     */
    public ID addFlow()
	throws ModelException
    {
	ID flow_id = new ID(ID.FLOW);
	FlowOptimization flow_optimization = new FlowOptimization();
	Flow new_flow = new Flow(flow_id, flow_id.toString(),
				 flow_optimization);
	put(flow_id, new_flow);
	return flow_id;
    }

    public ID addFlow(String IDstr)
	throws ModelException
    {
	ID flow_id = new ID(IDstr);
	FlowOptimization flow_optimization = new FlowOptimization();
	Flow new_flow = new Flow(flow_id, flow_id.toString(),
				 flow_optimization);
	put(flow_id, new_flow);
	return flow_id;
    }

    /**
     * Gets the flow with this name
     * @param flowIDstr The ID of the flow.
     * @return The label of this flow.
     */
    /*
        //Remove this function
    public Flow getFlow(String flowIDStr)
    {
	Enumeration keys = keys();
	ID id;
	while(keys.hasMoreElements()) {
	    id = (ID)keys;
	    if (flowIDStr.equals(id.toString()))
		return (Flow)get(id);
	    keys = (ID)keys.nextElement();
	}
	return null;
    }
    */

    /**
     * Gets the label of a certain flow.
     * @param flowID The ID of the flow.
     * @return The label of this flow.
     */
    public String getLabel(ID flowID)
    {
	Flow flow = (Flow) get(flowID);
	if (flow != null)
	    return flow.getLabel();

	return null;
    }

    /**
     * Gets a flows resource
     * @param flow ID of the flow to find resource for
     * @return The ID of the resource
     */
    public ID getResource(ID flow)
    {
	return ((Flow)get(flow)).getResource();
    }

    /**
     * Gets optimization information for the flow flowID.
     *
     * @param flowID The flow.
     * @throws ModelException is thrown when the flow is not found.
     */
    public Optimization getOptimization(ID flowID)
	throws ModelException
    {
	Flow flow = (Flow)get(flowID);
	if (flow == null) {
	    throw new ModelException();
	}
	return flow.getOptimization();
    }

    /**
     * Removes a flow.
     *
     * @param flowID The flow to remove.
     */
    public void removeFlow(ID flowID)
    {
	remove(flowID);
	flowID.remove();
    }

     /**
     * Sets the label of a certain flow.
     * @param flowID The ID of the flow.
     * @return The label of this flow.
     */
    public void setLabel(ID flowID, String newLabel)
	throws ModelException
    {
	Flow flow = (Flow) get(flowID);
	if (flow != null)
	    flow.setLabel(newLabel);
    }

    /**
     * Sets flow optimization information.
     *
     * @param flowID The flow.
     * @param optimizationInfo The new optimizationInfo.
     * @throws ModelException is thrown if the flow is not found.
     */
    public void setOptimization(ID flowID, Optimization optimizationInfo)
	throws ModelException
    {
	Flow flow = (Flow)get(flowID);
	if (flow == null) {
	    throw new ModelException();
	}
	flow.setOptimization(optimizationInfo);
    }

    /**
     * Sets a resource for something
     * @param where Where to set the resource
     * @param what What resource to set
     */
    public void setResource(ID where, ID what) {
	((Flow)get(where)).setResource(what);
    }

    public String toXML(NodeFlowTable nodeFlow, ResourceControl resources,
			int indent)
    {
	Collection flows = values();
	if (flows == null)
	    return "";
	Iterator iterator = flows.iterator();
	String xml = "";
	Flow flow;

	while (iterator.hasNext()) {
	    flow = (Flow) iterator.next();
	    xml = flow.toXML(nodeFlow, resources, indent) + xml;
	}

	return xml;
    }
}
