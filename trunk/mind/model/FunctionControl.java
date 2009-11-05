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

import java.util.Vector;
import java.util.Enumeration;
import java.util.LinkedList;

import mind.io.*;
import mind.model.function.*;

/**
 * Keeps track of all node functions and acts as a interface between
 * FunctionCollection and Node.
 *
 * @author Peter Åstrand
 * @author Johan Trygg
 * @author Jonas Sääv
 * @author Tor Knutsson
 * @author Freddie Pintar
 * @version 2007-10-01
 */
public class FunctionControl
{
    // This controller's functions.
    private Vector c_functions;

    // Functiontypes.
    private static FunctionCollection c_functionCollection;

    /**
     * A nullconstructor
     */
    public FunctionControl()
    {
	c_functions = new Vector();
    }

    /**
     * A copyconstructor
     */
    public FunctionControl(FunctionControl orgFunctionControl)
    {
	// Copy functions
	c_functions = new Vector(0);
	for (Enumeration e = orgFunctionControl.elements();
	     e.hasMoreElements();) {
	    NodeFunction nf = (NodeFunction)e.nextElement();
	    // Add a clone of this function to this functionvector.
	    try {
		NodeFunction newNf = (NodeFunction)nf.clone();
		add(newNf);
	    }
	    catch (Exception ex) {
		ex.printStackTrace(System.out);
	    };
	}

	//	c_functionCollection = orgFunctionControl.c_functionCollection;
    }

    /**
     * Adds a functiontype.
     *
     * @param functionType A new functiontype.
     */
    public void add(String functionType, Timesteplevel level)
    {
	NodeFunction func = c_functionCollection.getFunction(functionType);
	func.setTimesteplevel(level);
	c_functions.add(func);
    }

    /**
     * Adds a function
     * @param function The function to add
     */
    public void add(NodeFunction function)
    {
	c_functions.addElement(function);
    }

    /**
     * Change a timesteplevel for all functions in the functionCollection
     * @param levelToChange Timesteplevle to change
     * @param steps new number of steps to divide the above level in
     */
    public void changeTimesteplevel(Timesteplevel levelToChange, int steps)
    {
	NodeFunction nf;
	Timesteplevel nodeTSL;
	int nodeTSLi;
	//Loop over every function
	for (int i=0; i<c_functions.size(); ++i) {
	    nf = (NodeFunction)c_functions.get(i);
	    nodeTSL = nf.getTimesteplevel();
	    nodeTSLi = nodeTSL.toInt();
	    if (nodeTSLi >= levelToChange.toInt()) {
		nf.changeTimesteplevel(levelToChange, steps);
	    }
	}
    }

    /**
     * Get all elements.
     *
     */
    public Enumeration elements()
    {
	return c_functions.elements();
    }

    /**
     * Gets a function.
     *
     * @param functionLabel The label of the function to get.
     */
    public NodeFunction get(String functionLabel)
    {
	for (Enumeration e = c_functions.elements(); e.hasMoreElements();) {
	    NodeFunction nf = (NodeFunction)e.nextElement();
	    if (functionLabel.equals(nf.getLabel())) {
		return nf;
	    }
	}
	return null;
    }

    /**
     * Gets the timestep of a certain node.
     */
    public Timesteplevel getTimesteplevel()
    {
	if (c_functions != null && c_functions.size() > 0)
	    return ((NodeFunction) c_functions.elementAt(0)).getTimesteplevel();

	return null;
    }

    /**
     * Adds a function of type functionType to this functionControl and
     * initiats the the function to the values found in data.
     * The function is used when loading a model or node from disk.
     *
     * @param functionType type of function
     * @param data A linked list with data.
     * @param rc A control with all available resources.
     * @param createMissingResources If this is true then
     * if data contains resources not found in rc, these resources
     * should be created and added to rc.
     */
    public void parseAndAdd(String functionType, LinkedList data,
			    ResourceControl rc, boolean createMissingResource)
	throws RmdParseException
    {
	String funcType = NodeInteraction.XMLFunction2Function(functionType);
	NodeFunction newFunction=null;
	newFunction = c_functionCollection.getFunction(funcType);
	newFunction.parseData(data,rc,createMissingResource);

	c_functions.addElement(newFunction);
    }


    /**
     * Removes a functiontype.
     *
     * @param functionLabel The label of the function to remove.
     */
    public void remove(String functionLabel)
    {
	for (Enumeration e = c_functions.elements(); e.hasMoreElements();) {
	    NodeFunction nf = (NodeFunction)e.nextElement();
	    if (functionLabel.equals(nf.getLabel())) {
		c_functions.remove(nf);
	    }
	}
    }

    /**
     * Remove a timesteplevel for all functions in the functionCollection
     * @param levelToRemove Timesteplevle to remove
     */
    public void removeTimesteplevel(Timesteplevel levelToRemove)
    {
	NodeFunction nf;
	Timesteplevel nodeTSL;
	int nodeTSLi;
	//Loop over every function
	for (int i=0; i<c_functions.size(); ++i) {
	    nf = (NodeFunction)c_functions.get(i);
	    nodeTSL = nf.getTimesteplevel();
	    nodeTSLi = nodeTSL.toInt();
	    if (nodeTSLi >= levelToRemove.toInt()) {
		nf.removeTimesteplevel(levelToRemove);
	    }
	}
    }

    public static void setFunctionCollection(FunctionCollection collection)
    {
	if (c_functionCollection == null)
	    c_functionCollection = collection;
    }

    /**
     * Set all functions to this timesteplevel
     * @param level Timesteplevel to set
     */
    public void setTimesteplevel(Timesteplevel level)
    {
	for (Enumeration e = c_functions.elements(); e.hasMoreElements();) {
	    NodeFunction nf = (NodeFunction) e.nextElement();
	    nf.setTimesteplevel(level);
	}
    }

    // FIXME, remove this  (why?)
    public int size()
    {
	return c_functions.size();
    }

    public String toXML(ResourceControl resources, int indent)
    {
	if (c_functions == null || c_functions.size() == 0)
	    return "";

	String xml =
	    xml = XML.indent(indent) + "<functions>" + XML.nl();

	// the functions must come in a certain order, according to
	// the dtd file. first source then destination etc
	for (int i = 0; i < c_functions.size(); i++)
	    if (c_functions.get(i) instanceof Source)
		xml += ((NodeFunction) c_functions.get(i)).
		    toXML(resources, indent+1);

	for (int i = 0; i < c_functions.size(); i++)
	    if (c_functions.get(i) instanceof Destination)
		xml += ((NodeFunction) c_functions.get(i)).
		    toXML(resources, indent+1);

	for (int i = 0; i < c_functions.size(); i++)
	    if (c_functions.get(i) instanceof Boundary)
		xml += ((NodeFunction) c_functions.get(i)).
		    toXML(resources, indent+1);

	for (int i = 0; i < c_functions.size(); i++) {
	    if (c_functions.get(i) instanceof BoundaryTOP) {
            xml += ((NodeFunction) c_functions.get(i)).toXML(resources, indent+1);
        }
      }

	for (int i = 0; i < c_functions.size(); i++)
	    if (c_functions.get(i) instanceof FlowDependency)
		xml += ((NodeFunction) c_functions.get(i)).
		    toXML(resources, indent+1);

	for (int i = 0; i < c_functions.size(); i++)
	    if (c_functions.get(i) instanceof FlowRelation)
		xml += ((NodeFunction) c_functions.get(i)).
		    toXML(resources, indent+1);

	for (int i = 0; i < c_functions.size(); i++)
	    if (c_functions.get(i) instanceof FlowEquation)
		xml += ((NodeFunction) c_functions.get(i)).
		    toXML(resources, indent+1);

	/*Tillagt av Mattias Gylin*/
	for (int i = 0; i < c_functions.size(); i++)
	    if (c_functions.get(i) instanceof InvestmentCost)
		xml += ((NodeFunction) c_functions.get(i)).
		    toXML(resources, indent+1);

        /*Tillagt av  Jonas Sääv*/
        for (int i = 0; i < c_functions.size(); i++)
          if (c_functions.get(i)instanceof StorageEquation)
            xml += ( (NodeFunction) c_functions.get(i)).
                toXML(resources, indent + 1);

        for (int i = 0; i < c_functions.size(); i++)
          if (c_functions.get(i)instanceof BatchEquation)
            xml += ( (NodeFunction) c_functions.get(i)).
                toXML(resources, indent + 1);

        for (int i = 0; i < c_functions.size(); i++)
          if (c_functions.get(i)instanceof BinaryFunction)
            xml += ( (NodeFunction) c_functions.get(i)).
                toXML(resources, indent + 1);
          
        /*Tillagt av Marcus Bergendorff*/
          for (int i = 0; i < c_functions.size(); i++)
    	    if (c_functions.get(i) instanceof FunctionEditor)
    		xml += ((NodeFunction) c_functions.get(i)).
    		    toXML(resources, indent+1); 
        // Added by Nawzad Mardan 2008-02-15
          for (int i = 0; i < c_functions.size(); i++)
    	    if (c_functions.get(i) instanceof StartStopEquation)
    		xml += ((NodeFunction) c_functions.get(i)).
    		    toXML(resources, indent+1); 

	xml = xml + XML.indent(indent) + "</functions>" + XML.nl();

	return xml;
    }
    
    /**
     * PUM5 ADDED
     * Calls toEXML on all functions
     * @param resources 
     * @param sheet Structure for exml spreadsheet
     * @return
     */
    public void toEXML(ResourceControl resources, ExmlSheet sheet)
    {
    	if (c_functions == null || c_functions.size() == 0)
    		return;


    	// Functions dont have to come in a certain order.
    	
    	for (int i = 0; i < c_functions.size(); i++)
    		((NodeFunction) c_functions.get(i)).toEXML(resources, sheet);

    }
    
}
