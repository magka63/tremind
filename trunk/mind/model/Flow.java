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
 * A flow lets a resource flow between two nodes.
 *
 * @author Peter Åstrand
 * @version 2001-04-01
 */
package mind.model;

import mind.io.*;

public class Flow
{
    // The resource.
    private ID c_resource;

    // A short ID for this flow.
    private ID c_ID;

    // A label for this flow.
    private String c_label;

    // Optimization information
    private Optimization c_optimization;

    /**
     * A nullconstructor
     */
    public Flow()
    {
	c_ID = new ID(ID.FLOW);
    }

    /**
     * A fullconstructor
     *
     * @param flowID The flow ID.
     * @param label The flow label.
     * @param resource The resource.
     * @param optimization The flow optimization.
     */
    public Flow(ID flowID, String label, ID resource, Optimization optimization)
    {
	c_ID = flowID;
	c_label = label;
	c_resource = resource;
	c_optimization = optimization;
    }


    /**
     * A constructor without a resource
     *
     * @param flowID The flow ID.
     * @param label The flow label.
     * @param optimization The flow optimization.
     */
    public Flow(ID flowID, String label, Optimization optimization)
    {
	c_ID = flowID;
	c_label = label;
	c_resource = null;
	c_optimization = optimization;
    }

    /**
     * Returns the flows ID.
     *
     */
    public ID getID()
    {
	return c_ID;
    }

    /**
     * Returns the flows label.
     *
     */
    public String getLabel()
    {
	return c_label;
    }

    /**
     * Returns the flows optimization information.
     *
     */
    public Optimization getOptimization()
    {
	return c_optimization;
    }

    /**
     * Returns thw flows resource.
     *
     */
    public ID getResource()
    {
	return c_resource;
    }

    /**
     * Sets the flow ID.
     *
     */
    public void setID(ID flowID)
    {
	c_ID = flowID;
    }

    /**
     * Sets the flows label.
     *
     * @param label The new label.
     */
    public void setLabel(String label)
    {
	c_label = label;
    }

    /**
     * Sets optimization information.
     *
     * @param optimizationInfo The optimization information.
     */
    public void setOptimization(Optimization optimizationInfo)
    {
	c_optimization = optimizationInfo;
    }

    /**
     * Sets the resource for this flow.
     *
     * @param resource The resource.
     */
    public void setResource(ID resource)
    {
	c_resource = resource;
    }

    public String toString()
    {
	return getID().toString();
    }

    /**
     * Produces an XML section of this flow to be used in the saved
     * model file.
     * @return The XML section corresponding to this object.
     */
    public String toXML(NodeFlowTable nodeFlow, ResourceControl resources,
			int indent)
    {
	String xml = XML.indent(indent) + "<flow id=\"" + getID() +
	    "\" from=\"" +
	    nodeFlow.getFromNode(getID()).toString() + "\" to=\"" +
	    nodeFlow.getToNode(getID()).toString() + "\">" + XML.nl();

	if (c_label != null)
	    xml = xml + XML.indent(indent+1) + "<label>" +
		XML.toXML(c_label) + "</label>" + XML.nl();

	if (c_resource != null)
	    xml = xml + XML.indent(indent+1) + "<resource.type>" +
		XML.toXML(resources.getLabel(c_resource)) + "</resource.type>" + XML.nl();

	xml = xml + XML.indent(indent) + "</flow>" + XML.nl();

	return xml;
    }
}
