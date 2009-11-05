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
package mind.model.function;

import java.util.Vector;
import java.util.LinkedList;

import mind.model.*;
import mind.io.*;


/**
 * The function Destination limits flows.
 *
 * @author Peter Åstrand
 * @author Johan Trygg
 * @author Freddie Pintar
 * @author Tor Knutsson
 * @version 2007-12-12
 */
public class Destination
    extends NodeFunction
    implements Cloneable
{
    private ID c_resource;

    /**
     * Creates an empty function
     */
    public Destination()
    {
	super(new ID(ID.FUNCTION),"Destination", null); //Timestepleve must be set later
    }

    /**
     * Creates a new Destination for a resource
     * @param resource The resource to act as destination for
     */
    public Destination(ID resource)
    {
	super(new ID(ID.FUNCTION),"Destination", null); //Timesteplevel must be set later
	c_resource = resource;
    }

    /**
     * Creates a new copy of the function
     * @return A complete copy
     */
    public Object clone()
	throws CloneNotSupportedException
    {
     	return super.clone();
    }

    /**
     * Gets the resource this Destination limits.
     * @return The resource's ID
     */
    public ID getResource()
    {
	return c_resource;
    }

    /**
     * Set the Destination resource.
     * @param newResource The new resource for this Destination.
     */
    public void setResource(ID newResource)
    {
	c_resource = newResource;
    }


    /*******************************************************
     * Inherited functions overridden
     ******************************************************/


    public EquationControl getEquationControl(int maxTimesteps, ID node,
					      Vector toFlows, Vector fromFlows)
	throws ModelException
    {
	EquationControl control = new EquationControl();
	control.setConsumer(getResource());
	return control;
    }

    /**
     * Returns a short overview of the current equations of the function
     * @return A short overview of the equation returned by the function
     */
    public String getOverview()
    {
     	return "Destination with resource " + c_resource;
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
			  boolean createMissingResource) //Model model)
        throws RmdParseException
    {
	//Assumes the data to come in this order:
	//label tag, label, resource tag, resource, timesteplevel.
	//Label and resource are optinal.

	c_timesteplevel = (Timesteplevel)data.removeLast();

	if (!data.isEmpty() && ((String)data.getFirst()).equals("label")) {
	    data.removeFirst(); //Throw away the tag
	    setLabel((String)data.removeFirst());
	}

	if (!data.isEmpty() && ((String)data.getFirst()).equals("resource.type")) {
	    data.removeFirst(); //Throw away the tag
	    String resource = (String)data.removeFirst();
	    c_resource = rc.getResourceID(resource);
	    //Check if the resource exists
	    if (c_resource == null) {
		if (createMissingResource) {
		    c_resource = rc.addResource(resource,"",""); //we do not know
		                                                 //the unit or prefix
		}
		else
		    throw new RmdParseException("The resource '" +
						resource + "' is not defined.");
	    }
	}
	else {
	    c_resource = null;
	}
    }

    public String toXML(ResourceControl resource, int indent)
    {
	String xml = XML.indent(indent) + "<destination>" +
	    XML.nl();

	if (getLabel() != null)
	    xml = xml + XML.indent(indent+1) + "<label>" +
		XML.toXML(getLabel()) + "</label>" + XML.nl();

	if (c_resource != null)
	    xml = xml + XML.indent(indent+1) + "<resource.type>" +
		XML.toXML(resource.getLabel(c_resource)) + "</resource.type>" + XML.nl();

	xml = xml + XML.indent(indent) + "</destination>" + XML.nl();

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
		String label = ((this.getLabel()==null)?"":XML.toXML(this.getLabel()));

		//Add function header
		sheet.addFunctionHeader("Destination", label);
		
		//Add Resource description
		String resource = ((c_resource==null)?"":resources.getLabel(c_resource));
		sheet.addRow(sheet.addLockedCell("Resource")+sheet.addCell(XML.toXML(resource)));
		sheet.addRow(sheet.addCell(""));
    }
    
    protected int getTimesteps()
    {
	return 0;
    }

    protected void timestepInsertAt(int index) {}
    protected void timestepRemoveAt(int index) {}
    protected void timestepSetMoreDetailed(int factor) {}
    protected void timestepSetLessDetailed(int newSize, int factor) {}
    protected void timestepResetData(int size) {}

  public boolean isRelatedToFlow(ID flow) {
    return false;
  }
}
