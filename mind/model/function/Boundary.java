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
 * The function Boundary limits flows.
 *
 * @author Peter Åstrand
 * @author Peter Andersson
 * @author Johan Trygg
 * @author Freddie Pintar
 * @author Tor Knutsson
 * @version 2007-12-12
 */
public class Boundary
    extends NodeFunction
    implements Cloneable
{
    private ID c_resource = null;
    private Vector c_minimum = null;
    private Vector c_maximum = null;
    private Vector c_isMin = null;
    private Vector c_isMax = null;
    // Added by Nawzad Mardan 080910
    private boolean c_radin;
    private boolean c_radout;

    /**
     * Creates an empty function
     */
    public Boundary()
    {
	super(new ID(ID.FUNCTION), "Boundary", null);
	//Timestep must be set later

	c_minimum = new Vector(1,5);
	c_maximum = new Vector(1,5);
	c_isMin = new Vector(1,5);
	c_isMax = new Vector(1,5);

	c_isMin.add(new Boolean(false));
	c_isMax.add(new Boolean(false));
	c_minimum.add(new Float(0.0));
	c_maximum.add(new Float(0.0));
    }

    /**
     * Creates a function with all values set
     * @param resource The resource to restrict
     * @param isMin A vector with a boolean for every timestep
     * @param min A vector with a float for every timestep
     * @param isMax see isMin
     * @param max see min
     */
    public Boundary(ID resource, Vector isMin, Vector min, Vector isMax, Vector max)
    {
	super(new ID(ID.FUNCTION), "Boundary", null); //Timestep must be set later
	c_isMin = isMin;
	c_minimum = min;
	c_isMax = isMax;
	c_maximum = max;
    }


    /**
     * Creates a new copy of the function
     * @return A complete copy
     */
    public Object clone()
	throws CloneNotSupportedException
    {
	Boundary clone = (Boundary) super.clone();
	c_minimum = new Vector(clone.c_minimum);
        c_maximum = new Vector(clone.c_maximum);
	c_isMin = new Vector(clone.c_isMin);
	c_isMax = new Vector(clone.c_isMax);
	return clone;
    }

    /**
     * Gets the resource this Boundary limits.
     */
    public ID getResource()
    {
	return c_resource;
    }

    /**
     * Check if this Boundary is an upper limit.
     */
    public boolean isMax()
    {
	return ((Boolean)c_isMax.get(getTimestep()-1)).booleanValue();
    }

    /**
     * Returns the maximum value
     * @return The maximum value
     */
    public float getMax()
    {
	return ((Float)c_maximum.get(getTimestep()-1)).floatValue();
    }

    /**
     * Check if this Boundary is a lower limit.
     */
    public boolean isMin()
    {
	return ((Boolean)c_isMin.get(getTimestep()-1)).booleanValue();
    }

    /**
     * Returns the minimum value
     * @return The minimum value
     */
    public float getMin()
    {
	return ((Float)c_minimum.get(getTimestep()-1)).floatValue();
    }

    /**
     * Set this Boundary to have an upper limit.
     * @param isMaximum if this boundary has a maximum value
     */
    public void setIsMax(boolean isMaximum)
    {
	c_isMax.setElementAt(new Boolean(isMaximum), getTimestep()-1);
    }

    /**
     * Set this Boundary to have an lower limit.
     * @param isMinimum if this boundary has a minimum value
     */
    public void setIsMin(boolean isMinimum)
    {
	c_isMin.setElementAt(new Boolean(isMinimum), getTimestep()-1);
    }

    /**
     * Set maximum value.
     * @param maximum The new max value.
     */
    public void setMax(float maximum)
    {
	c_maximum.setElementAt(new Float(maximum), getTimestep()-1);
    }


    /**
     * Set maximum value.
     * @param minimum The new min value.
     */
    public void setMin(float minimum)
    {
	c_minimum.setElementAt(new Float (minimum), getTimestep()-1);
    }



    /*******************************************************
     * Inherited functions overridden
     ******************************************************/

    /**
     * Returns optimizationinformation from Boundary
     * @param maxTimesteps The maximum number of timesteps in the model
     * @param node The ID for the node that generates the equations
     * @return Some equations that model the source's behaviour
     * @throws ModelException if it cannot optimize
     */
    public EquationControl getEquationControl(int maxTimesteps, ID node,
					      Vector toFlows, Vector fromFlows)
	throws ModelException
    {

	EquationControl control = new EquationControl();
	Vector foundFlows = new Vector(0);
	//Vector variables = new Vector(0);  //Moved inside the timestep loop
        //When outside the equations can "almost" be used to optimize
        //"over time" (and solve the "Boundary-Timestep problem" in reMIND
        //User Manual 1.1) /JT 010802

	if (c_resource == null)
	    throw new ModelException("Resource in boundary "
				     +getLabel()+" not specified.");
        
          // Check if any flow is selected
        // Added by Nawzad Mardan 080909
        if((toFlows.size() <= 0) && (fromFlows.size() <=0 ))
        throw new ModelException("No flow going in or out from node " +
				     node + " has the same resource as " +
				     "the source function inside it.\n" +
				     "Can not optimize.");
        
	// Check what ingoing flows has the same resource as
	// this source functions
        // Added by Nawzad Mardan 080909
        // Default is generating equations for the inflow. 
	// Check what ingoing flows has the same resource as
	// this source functions
        if(c_radin)
        {
            if(toFlows.size()<=0)
            {
              throw new ModelException("In Boundary Function: No flow going in from  " +
					 "Node "+node+".\n\n"+
					 "Can not optimize.");  
            }
        else
        {       
        for (int i = 0; i < toFlows.size(); i++) 
            {
	    if (((Flow) toFlows.get(i)).getResource() == null)
		throw new ModelException("In Boundary Function: Resource for Source function in " +
					 "Node "+node+" not specified.\n\n"+
					 "Can not optimize.");
	    if (((Flow) toFlows.get(i)).getResource().equals(getResource())) 
                {
		foundFlows.addElement(toFlows.get(i));
		// add variable for flow
                }
              }//END FOR
        if (foundFlows.size() <= 0)
            {
	    throw new ModelException("In Boundary Function:The Inflows to the Node (" + node+" )\n are not contain selected recourse " +"\n"+
					 "Can not optimize.");
            }
         }// END ELSE
        }// END IF ingoing flows
        // Chek outgoing flows Added by Nawzad Mardan 080909
        else
        {
             if(fromFlows.size()<=0)
            {
              throw new ModelException("In Boundary Function: No flow going out from  " +
					 "Node "+node+".\n"+
					 "Can not optimize.");  
            }
            for (int i = 0; i < fromFlows.size(); i++) {
	    if (((Flow) fromFlows.get(i)).getResource() == null)
		throw new ModelException("In Boundary Function: Resource for Source function in " +
					 "Node "+node+" not specified.\n\n"+
					 "Can not optimize.");
	    if (((Flow) fromFlows.get(i)).getResource().equals(getResource())) {
		foundFlows.addElement(fromFlows.get(i));
		// add variable for flow
	    }
	}
              if (foundFlows.size() <= 0)throw new ModelException("In Boundary Function: The Outflows to the Node (" + node+" )\n are not contain selected recourse " +"\n"+
					 "Can not optimize.");    
        }// END IF outgoing flows
        
	/*if (foundFlows.size() <= 0)
	    throw new ModelException("Now flow going out from node " +
				     node + " has the same resource as " +
				     "the source function inside it.\n" +
				     "Can not optimize.");*/

	for (int i = 0; i < maxTimesteps ; i++) {
	    /*
	      for every timestep, we need to generate a variable
	      First we find out the index of the cost
	      These two lines makes us use the same value in the vector
	      many times if we don't have enough information for all
	      timesteps in the model
	    */
	    int vectorsize = c_isMax.size();
	    int index =  ((i * vectorsize) / maxTimesteps ) % vectorsize;

	    Vector variables = new Vector(0);

	    // Variable var = new Variable(node, c_resource, i, (float)1);
	    for (int j = 0; j < foundFlows.size(); j++) {
		Variable var = new Variable(((Flow) foundFlows.get(j)).getID(),
					    i+1, (float) 1);
		variables.addElement(var);
	    }
            if ( ( (Boolean) c_isMin.get(index)).booleanValue()) {
              Equation lowerbound =
                  new Equation(node, getID(), i + 1, 1,
                               Equation.GREATEROREQUAL,
                               ( (Float) c_minimum.get(index)).floatValue());
              for (int j = 0; j < variables.size(); j++)
                lowerbound.addVariable( (Variable) variables.get(j));

              control.add(lowerbound);
            }

            if ( ( (Boolean) c_isMax.get(index)).booleanValue()) {
		Equation upperbound =
		    new Equation(node, getID(), i+1, 2,
				 Equation.LOWEROREQUAL,
				 ((Float)c_maximum.get(index)).floatValue());

		for (int j = 0; j < variables.size(); j++)
		    upperbound.addVariable((Variable) variables.get(j));

		control.add(upperbound);
	    }
	}

	return control;
    }

    /**
     * Returns a short overview of the current equations of the function
     * @return A short overview of the equation returned by the function
     */
    public String getOverview()
    {
	String result;
	result = "A Boundary with ";
	if (!isMin() && !isMax()) {
	    result += "no limits.";
	}
	if (isMin()) {
	    result += "a lower limit of " + getMin();
	}
	if (isMin() && isMax()) {
	    result += "and";
	}
	if (isMax()) {
	    result += "an upper limit of " + getMax();
	}
	result +=".";
     	return result;
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
			  boolean createMissingResource)
        throws RmdParseException
    {
	//Assumes the data to come in this order:
	//label tag, label,
	//resource tag, resource,
	//min tag timestep 1, min timestep 1, max tag timestep 1, max timestep 1,
	//... , max timestep n,
	//timesteplevel.
	//There must be at least one timestep. Label and resource are optional.
	//Max and min are optinal.

        boolean timestepHasLengths = false; /* if this is false we are reading an old type rmd-file
                 i.e. an rmd-file that contains no timestep lengths */
        boolean newModel = false;
	c_timesteplevel = (Timesteplevel)data.removeLast();

	if (((String)data.getFirst()).equals("label")) {
	    data.removeFirst(); //Throw away the tag
	    setLabel((String)data.removeFirst());
	}
        // Added by Nawzad Mardan 080910
        if (((String)data.getFirst()).equals("inflows")) 
        {
            newModel = true; 
            data.removeFirst(); 
            String value = "";
            if(data.size() > 0) 
                value = (String)data.removeFirst();
            c_radin = value.equals("true") ? true : false;
        }

        // Added by Nawzad Mardan 080910
        if (((String)data.getFirst()).equals("outflows")) 
        {
            newModel = true; 
            data.removeFirst(); 
            String value = "";
            if(data.size() > 0) 
                value = (String)data.removeFirst();
            c_radout = value.equals("true") ? true : false;
           
        }
         //This is for the old models which is not containing neither inflow radio button nor outflow radio button
            if(!newModel)
                c_radin = true;
	String resource = "";
	if (((String)data.getFirst()).equals("resource.type")) {
	    data.removeFirst(); //Throw away the tag
	    resource = (String)data.removeFirst();
	    c_resource = rc.getResourceID(resource);
	    //Check if the resource exists
	    if (c_resource == null) {
		if (createMissingResource)
		    c_resource = rc.addResource(resource,"",""); //we do not know
		                                                 //the unit or prefix
		else
		    throw new RmdParseException("The resource '" +
						resource + "' is not defined.");
	    }
	}
	else {
	    c_resource = null;
	}

	c_minimum = new Vector(5);
	c_maximum = new Vector(5);
	c_isMin = new Vector(5);
	c_isMax = new Vector(5);
	float min, max;

	while(!data.isEmpty()) {

	    data.removeFirst(); //throw away the T tag

	    if (!data.isEmpty() && ((String)data.getFirst()).equals("min")) {
		data.removeFirst(); //throw away tag
		try {
		    min = Float.parseFloat((String)data.removeFirst());
		}
		catch (NumberFormatException e) {
		    throw new RmdParseException("The 'min' field must be a float > 0");
		}
		if (min < 0)
		    throw new RmdParseException("The 'min' field must be a float > 0");
		c_isMin.add(new Boolean(true));
		c_minimum.add(new Float(min));

	    }
	    else {
		c_isMin.add(new Boolean(false));
		c_minimum.add(new Float(0));
	    }

	    if (!data.isEmpty() && ((String)data.getFirst()).equals("max")) {
		data.removeFirst(); //throw away tag
                try {
                  max = Float.parseFloat( (String) data.removeFirst());
		}
		catch (NumberFormatException e) {
		    throw new RmdParseException("The 'max' field must be a float > 0");
		}
		if (max < 0)
		    throw new RmdParseException("The 'max' field must be a float > 0");
		c_isMax.add(new Boolean(true));
		c_maximum.add(new Float(max));
	    }
	    else {
		c_isMax.add(new Boolean(false));
		c_maximum.add(new Float(0));
	    }
	}
    }

    /**
     * Set the Boundary resource.
     * @param newResource The new resource for this Boundary.
     */
    public void setResource(ID newResource)
    {
	c_resource = newResource;
    }

    public String toXML(ResourceControl resources, int indent)
    {
	String xml = XML.indent(indent) + "<boundary>" + XML.nl();

	if (getLabel() != null)
	    xml = xml + XML.indent(indent+1) + "<label>" +
		XML.toXML(getLabel()) + "</label>" + XML.nl();
        // Added by Nawzad Mardan 080910
        xml += XML.indent(indent+1) + "<inflows>"  + (c_radin ? "true" : "false") + "</inflows>"  + XML.nl();
        // Added by Nawzad Mardan 080910
        xml += XML.indent(indent+1) + "<outflows>"  + (c_radout ? "true" : "false") + "</outflows>"  + XML.nl();

	if (c_resource != null)
	    xml = xml + XML.indent(indent+1) + "<resource.type>" +
		XML.toXML(resources.getLabel(c_resource)) +
		"</resource.type>" + XML.nl();

	// c_isMin and c_isMax should be equally  big
	for (int i = 0; i < c_isMin.size(); i++) {
	    xml = xml + XML.indent(indent+1) + "<timestep.boundary nr=\"" +
		(i+1) + "\">" + XML.nl();
	    if (((Boolean) c_isMin.get(i)).booleanValue())
		xml = xml + XML.indent(indent+2) + "<min>" +
		    c_minimum.get(i).toString() + "</min>" + XML.nl();
	    if (((Boolean) c_isMax.get(i)).booleanValue())
		xml = xml + XML.indent(indent+2) + "<max>" +
		    c_maximum.get(i).toString() + "</max>" + XML.nl();

	    xml = xml + XML.indent(indent+1) + "</timestep.boundary>" + XML.nl();
	}
	xml = xml + XML.indent(indent) + "</boundary>" + XML.nl();

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
		sheet.addFunctionHeader("Boundary", label);
                // Added by Nawzad Mardan 080910
                sheet.addRow(sheet.addLockedCell("Inflows")+sheet.addCell((new Boolean(c_radin))));		
		sheet.addRow(sheet.addLockedCell("Outflows")+sheet.addCell((new Boolean(c_radout))));
		//Add Resource description
		String resource = ((c_resource==null)?"":resources.getLabel(c_resource));
		sheet.addRow(sheet.addCell("Resource")+sheet.addCell(XML.toXML(resource)));
		
		// Add timestep nrs in one Row.
		int numberOfTimesteps = getTimesteps();
		sheet.addTimeStepRow(numberOfTimesteps);
			
		//Add Timestep data
		sheet.initTable(2, numberOfTimesteps);
		sheet.addLockedTableValue("Min");
		sheet.addLockedTableValue("Max");
		for( int i = 0; i < numberOfTimesteps; i++){
			sheet.addTableValue("Number",  c_minimum.get(i).toString());
			sheet.addTableValue("Number",  c_maximum.get(i).toString());
		}
		sheet.endTable();
		sheet.addRow(sheet.addCell(""));

    }

    /*
     * All these procted methods below are used for timesteps.
     * These methods are abstract in NodeFunction and are called
     * from there.
     */

    protected int getTimesteps()
    {
	return c_isMax.size();
    }

    protected void timestepInsertAt(int index)
    {
	c_isMax.insertElementAt(new Boolean(false), index);
	c_isMin.insertElementAt(new Boolean(false), index);
	c_maximum.insertElementAt(new Float(0.0), index);
	c_minimum.insertElementAt(new Float(0.0), index);
    }

    protected void timestepRemoveAt(int index)
    {
	c_isMax.removeElementAt(index);
	c_isMin.removeElementAt(index);
	c_maximum.removeElementAt(index);
	c_minimum.removeElementAt(index);
    }

    protected void timestepSetMoreDetailed(int factor)
    {
	int oldsize = c_isMax.size();
	int newsize = oldsize * factor;

	//Copy old cost values to new Vectors
	Vector new_isMax = new Vector(newsize,1);
	Vector new_isMin = new Vector(newsize,1);
	Vector new_maximum = new Vector(newsize,1);
	Vector new_minimum = new Vector(newsize,1);
	float max,min;
	boolean isMin,isMax;
	for(int i=0; i<oldsize; i++) {
	    min = ((Float)c_minimum.get(i)).floatValue();
	    max = ((Float)c_maximum.get(i)).floatValue();
	    isMin = ((Boolean)c_isMin.get(i)).booleanValue();
	    isMax = ((Boolean)c_isMax.get(i)).booleanValue();
	    for(int k=0; k<factor; k++) {
		new_minimum.add(new Float(min));
		new_maximum.add(new Float(max));
		new_isMin.add(new Boolean(isMin));
		new_isMax.add(new Boolean(isMax));
	    }
	}

	c_isMax = new_isMax;
	c_isMin = new_isMin;
	c_maximum = new_maximum;
	c_minimum = new_minimum;
    }

    protected void timestepSetLessDetailed(int newSize, int factor)
    {
	//Copy old cost values to new cost array
	Vector new_isMax = new Vector(newSize,1);
	Vector new_isMin = new Vector(newSize,1);
	Vector new_maximum = new Vector(newSize,1);
	Vector new_minimum = new Vector(newSize,1);
	int i, oldindex;

	for(i = 0, oldindex = 0; i < newSize; i++, oldindex += factor) {
	    new_minimum.add(c_minimum.get(oldindex));
	    new_maximum.add(c_maximum.get(oldindex));
	    new_isMin.add(c_isMin.get(oldindex));
	    new_isMax.add(c_isMax.get(oldindex));
	}

	c_isMax = new_isMax;
	c_isMin = new_isMin;
	c_maximum = new_maximum;
	c_minimum = new_minimum;
    }

    protected void timestepResetData(int size)
    {
	//Create vector and reset to zero
	c_isMin = new Vector(size,1);
	c_isMax = new Vector(size,1);
	c_minimum = new Vector(size,1);
	c_maximum = new Vector(size,1);

	for(int i=0; i<size; i++) {
	    c_minimum.add(new Float(0));
	    c_maximum.add(new Float(0));
	    c_isMax.add(new Boolean(false));
	    c_isMin.add(new Boolean(false));
	}
    }

  public boolean isRelatedToFlow(ID flow) {
    return false;
  }
  
  // Added by Nawzad Mardan 080910
    /**
     * Gets the c_radout value.
     * @return The c_radout values as a true or false.
     */
    public boolean isRadOut()
    {
        return c_radout;
    }
    
    /**
     * Gets the c_radin value.
     * @return The c_radin values as a true or false.
     */
    public boolean isRadIn()
    {
        return c_radin;
    }
    
    /**
     * Sets the variable's b to the c_radin
     * @param  b The  c_radin radio button to be set
     */
    public void setRadIn(boolean b)
    {
        c_radin = b;
    }
    
    /**
     * Sets the variable's b to the c_radout
     * @param  b The  c_radout radio button to be set
     */
    public void setRadOut(boolean b)
    {
        c_radout = b;
    }
  
}
