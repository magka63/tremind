/*
 * Copyright 2001:
 * Peter Andersson <petan117@student.liu.se>
 * Martin Hagman <marha189@student.liu.se>
 * Henrik Norin <henno776@student.liu.se>
 * Anna Stjerneby <annst566@student.liu.se>
 * Tim Terleg�rd <timte878@student.liu.se>
 * Johan Trygg <johtr599@student.liu.se>
 * Peter �strand <petas096@student.liu.se>
 *
 * Copyright 2007:
 * Per Fredriksson <perfr775@student.liu.se>
 * David Karlsl�tt <davka417@student.liu.se>
 * Tor Knutsson	<torkn754@student.liu.se>
 * Daniel K�llming <danka053@student.liu.se>
 * Ted Palmgren <tedpa175@student.liu.se>
 * Freddie Pintar <frepi150@student.liu.se>
 * Mårten Thuren <marth852@student.liu.se>
 *
 * Copyright 2010:
 * Nawzad Mardan <nawzad.mardan@liu.se>
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

import java.util.LinkedList;
import java.util.Vector;

import mind.model.*;
import mind.io.*;


/**
 * The function Flow Dependency
 *
 * @author Tim Terleg�rd
 * @author Freddie Pintar
 * @author Tor Knutsson
 * @version 2007-12-12
 */

public class FlowDependency
    extends NodeFunction
    implements Cloneable
{
    // The resource on the X axis.
    private ID c_resourceX = null;
    // The resource on the Y axis.
    private ID c_resourceY = null;

    // Wether the resources are for in-flows or out-flows
    private boolean c_isXIn = true;
    private boolean c_isYIn = false;

    private Vector c_timesteps = null;

    class TimestepInfo
    {
	// Vector of Integers saying what limits the y=k*x+m holds
	// upperlimit = -1 means it holds to x=infinity
	private Vector c_upperLimit = new Vector(0);

	// m in Y=kX+m
	private Vector c_offset = new Vector(0);
	// The slope, k in Y=kX+m.
	private Vector c_slope = new Vector(0);

	public TimestepInfo()
	{
	}

	public TimestepInfo(TimestepInfo info)
	{
	    c_upperLimit = new Vector(info.c_upperLimit);
	    c_offset = new Vector(info.c_offset);
	    c_slope = new Vector(info.c_slope);
	}

	public void add()
	{
	    c_upperLimit.addElement(new Float(-1));
	    c_slope.addElement(new Float(1));
	    c_offset.addElement(new Float(0));
	}

	public void clearLimits()
	{
	    c_upperLimit.removeAllElements();
	    c_offset.removeAllElements();
	    c_slope.removeAllElements();
	}

	public float getLowerLimit(int i)
	{
	    if (i <= 1)
		return 0;
	    else
		return ((Float) c_upperLimit.get(i-2)).floatValue();
	}

	public float getOffset(int i)
	{
	    if (i <= getSize())
		return ((Float) c_offset.get(i-1)).floatValue();
	    else
		return 0;
	}

	public int getSize()
	{
	    // all vectors should have same size, pick one
	    return c_slope.size();
	}

	public float getSlope(int i)
	{
	    if (i <= getSize())
		return ((Float) c_slope.get(i-1)).floatValue();
	    else
		return 0;
	}

	public float getUpperLimit(int i)
	{
	    if (i <= getSize())
		return ((Float) c_upperLimit.get(i-1)).floatValue();
	    else
		return 0;
	}

	public void remove()
	{
	    if (getSize() > 0) {
		c_upperLimit.removeElementAt(c_upperLimit.size()-1);
		c_offset.removeElementAt(c_offset.size()-1);
		c_slope.removeElementAt(c_slope.size()-1);
	    }
	}

	public void setUpperLimit(int i, float limit)
	{
	    if (i <= getSize())
		c_upperLimit.setElementAt(new Float(limit), i-1);
	}

	public void setSlope(int i, float slope)
	{
	    if (i <= getSize())
		c_slope.setElementAt(new Float(slope), i-1);
	}

	public void setOffset(int i, float offset)
	{
	    if (i <= getSize())
		c_offset.setElementAt(new Float(offset), i-1);
	}

	public String toXML(int indent)
	{
	    String xml = "";
	    for (int i = 0; i < getSize(); i++) {
		xml = xml + XML.indent(indent) + "<start>" +
		    getLowerLimit(i+1) + "</start>" + XML.nl() +
		    XML.indent(indent) + "<end>" + getUpperLimit(i+1) +
		    "</end>" + XML.nl() +
		    XML.indent(indent) + "<slope>" + getSlope(i+1) +
		    "</slope>" + XML.nl() +
		    XML.indent(indent) + "<offset>" + getOffset(i+1) +
		    "</offset>" + XML.nl();
	    }
	    return xml;
	}

	/**
	 * Puts EXML data in the given ExmlSheet
	 * PUM5 Added 2007-12-12
	 * @param sheet The ExmlSheet to be changed 
	 */
	public void toEXML(ExmlSheet sheet)
	{
		for (int i = 0; i < getSize(); i++) {
			sheet.addStyledTableValue(getLowerLimit(i+1),"SideBorders");
			sheet.addStyledTableValue(getUpperLimit(i+1),"SideBorders");
			sheet.addStyledTableValue(getSlope(i+1),"SideBorders");
			sheet.addStyledTableValue(getOffset(i+1),"SideBottomBorders");
		}
	}
    }

    /**
     * Constructor.
     */
    public FlowDependency()
    {
	super(new ID(ID.FUNCTION), "FlowDependency", null);
    }

    /*
     * Adds equations to the control.
     */
    public void addEquationOfTimestep(EquationControl control,
				      int timestep, int index, ID node,
				      Vector xFlows, Vector yFlows)
    {
	TimestepInfo info = (TimestepInfo) c_timesteps.get(index);
	int size = info.getSize();
	Variable var;
	int equation = 1;

	// add every interval in this timestep
	for (int i = 0; i < size; i++) 
        {
          Vector variables = new Vector(0);

          // create all variables for  z1 = a1*y1 + b1*x1
          for (int j = 0; j < yFlows.size(); j++) {
              // Z1
            var = new Variable( ( (Flow) yFlows.get(j)).getID(),
                               timestep, getID(), i + 1, (float) 1);
            variables.addElement(var);
          }
          // add -b1*x1
          for (int j = 0; j < xFlows.size(); j++) {
            var = new Variable( ( (Flow) xFlows.get(j)).getID(),
                               timestep, getID(), i + 1,
                               info.getSlope(i + 1) * ( -1));
            variables.addElement(var);
          }
          // add -a1*y1
          if(size != 1)
            {
            var = new Variable(timestep, getID(), i + 1, info.getOffset(i + 1) * ( -1), true);
            variables.addElement(var);
            }

          // create an equation and all the variables to it
          Equation part =
              new Equation(node, getID(),node.toString()+"FlowDependencyFun", timestep, equation++,
                           Equation.EQUAL, (float) 0);
          for (int j = 0; j < variables.size(); j++)
            part.addVariable( (Variable) variables.get(j));

          control.add(part);
          // Added by Nawzad Mardan 20100210
          //  When we have only one slop then we do not need to create any binary variables
          if(size == 1)
            {
            // create variables for x1Lower <= x1
            Equation lowerLimit = new Equation(node, getID(),node.toString()+"FlowDependencyFun", timestep, equation++,
                           Equation.GREATEROREQUAL, info.getLowerLimit(i + 1));
            for (int j = 0; j < xFlows.size(); j++)
                {
                var = new Variable( ( (Flow) xFlows.get(j)).getID(),timestep, getID(), i + 1, (float)  1);
                lowerLimit.addVariable(var);
                }
            control.add(lowerLimit);
            /* Make a binary variable just to operate with fixed integer value commando
            Equation integer = new Equation(node, getID(),node.toString()+"FlowDependencyFun", timestep, equation++,
                           Equation.EQUAL, 1);
            var = new Variable(timestep, getID(), i + 1, 1, true);
            integer.addVariable(var);
            control.add(integer);*/

            // create variables and equation for x1 <= x1Upper
            Equation upperLimit = new Equation(node, getID(),node.toString()+"FlowDependencyFun", timestep, equation++,
                             Equation.LOWEROREQUAL, info.getUpperLimit(i + 1));
            for (int j = 0; j < xFlows.size(); j++)
                {
                var = new Variable( ( (Flow) xFlows.get(j)).getID(),timestep, getID(), i + 1, (float) 1);
                upperLimit.addVariable(var);
                }
            control.add(upperLimit);

            } // END IF there is only one slop
          // IF there is more than one slop
          else
          {  // create variables for x1Lower*y1 <= x1 <= x1Upper*y1
          // first create variables and equation for x1Lower*y1 - x1 <= 0
          Equation lowerLimit =
              new Equation(node, getID(),node.toString()+"FlowDependencyFun", timestep, equation++,
                           Equation.LOWEROREQUAL, (float) 0);
          //  When we have only one slop then we do not need to create a binary variable
          var = new Variable(timestep, getID(), i + 1,info.getLowerLimit(i + 1), true);
          lowerLimit.addVariable(var);


          for (int j = 0; j < xFlows.size(); j++) {
            var = new Variable( ( (Flow) xFlows.get(j)).getID(),
                               timestep, getID(), i + 1, (float) - 1);
            lowerLimit.addVariable(var);
          }
          control.add(lowerLimit);

          /* create variables and equation for x1 - x1Upper*Y1 <= 0
               Jonas S��v - 2005-05-12: but don't create this equation for the last segment
                    E.g.
                   Between 0.0 and 5000  <res-Y> = 1.0*<res-X> + 0.0
                   Between 5000 and 1E10 <res-Y> = 1.2*<res-X> + 0.0   don't create upper limit equations
               for this last segment.
           */
          if (i <= size-1) { // according to the comment above
            Equation upperLimit = new Equation(node, getID(),node.toString()+"FlowDependencyFun", timestep, equation++,
                             Equation.LOWEROREQUAL, (float) 0);
            for (int j = 0; j < xFlows.size(); j++) {
              var = new Variable( ( (Flow) xFlows.get(j)).getID(),timestep, getID(), i + 1, (float) 1);
              upperLimit.addVariable(var);
            }
           
            var = new Variable(timestep, getID(), i + 1, info.getUpperLimit(i + 1) * ( -1), true);
            upperLimit.addVariable(var);

            control.add(upperLimit);
          }// end if (i < size)

        } // END ELSE  When the equation have more than one slop
        } // END FOR

	// create equation and variables for  y1 + y2 + ... + yn = 1
    // Added by Nawzad Mardan 20100210
     //  When we have only one slop then we do not need to create a binary variable
    if(size != 1)
     {
     Equation integer = new Equation(node, getID(),node.toString()+"FlowDependencyFun", timestep, equation++,
					Equation.EQUAL, (float) 1);
	for (int i = 0; i < size; i++) {
	    var = new Variable(timestep, getID(), i+1, (float) 1, true);
	    integer.addVariable(var);
	}
	control.add(integer);
    }
	// finally add that the total flow is the sum of every
	// interval
	// for resource X
	for (int i = 0; i < xFlows.size(); i++) {
	    Equation flowTotal =
		new Equation(node, getID(),node.toString()+"FlowDependencyFun", timestep, equation++,
			     Equation.EQUAL, (float) 0);
	    for (int j = 0; j < size; j++) {
		var = new Variable(((Flow) xFlows.get(i)).getID(),
				   timestep, getID(), j+1, (float) -1);
		flowTotal.addVariable(var);
	    }
	    var = new Variable(((Flow) xFlows.get(i)).getID(),
			       timestep, (float) 1);
	    flowTotal.addVariable(var);
	    control.add(flowTotal);
	}

	// for resource Y
	for (int i = 0; i < yFlows.size(); i++) {
	    Equation flowTotal =
		new Equation(node, getID(),node.toString()+"FlowDependencyFun", timestep, equation++,
			     Equation.EQUAL, (float) 0);
	    for (int j = 0; j < size; j++) {
		var = new Variable(((Flow) yFlows.get(i)).getID(),
				   timestep, getID(), j+1, (float) -1);
		flowTotal.addVariable(var);
	    }
	    var = new Variable(((Flow) yFlows.get(i)).getID(),
			       timestep, (float) 1);
	    flowTotal.addVariable(var);
	    control.add(flowTotal);
	}
    }

    public void addLimit()
    {
	TimestepInfo info = (TimestepInfo) c_timesteps.get(getTimestep()-1);
	info.add();
    }

    public void clearLimits()
    {
	for (int i = 0; i < c_timesteps.size(); i++)
	    ((TimestepInfo) c_timesteps.get(i)).clearLimits();
    }

    /**
     * Creates a new copy of the function
     * @return A complete copy
     */
    public Object clone()
	throws CloneNotSupportedException
    {
	FlowDependency clone = (FlowDependency) super.clone();
	c_timesteps = new Vector(clone.c_timesteps);
	c_resourceX = clone.c_resourceX;
	c_resourceY = clone.c_resourceY;
	return clone;
    }

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
	Vector xFlows = new Vector(0);
	Vector yFlows = new Vector(0);

	if (isXIn() && !isYIn()) {
	    control.setConsumer(getResourceX());
	    control.setProducer(getResourceY());
	}
	else if (!isXIn() && isYIn()) {
	    control.setConsumer(getResourceY());
	    control.setProducer(getResourceX());
	}

	checkIfValid(toFlows, fromFlows, xFlows, yFlows, node);

	int vectorsize = c_timesteps.size();
	for (int i = 0; i < maxTimesteps ; i++) {
	    /*
	      for every timestep, we need to generate a variable
	      First we find out the index of the cost
	      These two lines makes us use the same value in the vector
	      many times if we don't have enough information for all
	      timesteps in the model
	    */
	    int index =  ((i * vectorsize) / maxTimesteps ) % vectorsize;

	    addEquationOfTimestep(control, i+1, index, node, xFlows, yFlows);
	}
	//	System.out.println("flowdep eq = " + control.get(0));

    // Added by Nawzad Mardan 20100210
    // Make a binary variable just to operate with fixed integer value commando
    TimestepInfo info = (TimestepInfo) c_timesteps.get(0);
	int size = info.getSize();
    if(size == 1)
      {
      Equation integer = new Equation(node, getID(),node.toString()+"FlowDependencyFun", maxTimesteps, 1965,
                           Equation.EQUAL, 1);
      Variable var = new Variable(maxTimesteps, getID(), maxTimesteps + 1, 1, true);
      integer.addVariable(var);
      control.add(integer);
     }

	return control;
    }

    public int getLimits()
    {
	TimestepInfo info = (TimestepInfo) c_timesteps.get(getTimestep()-1);
	return info.getSize();
    }

    public float getLowerLimit(int i)
    {
	TimestepInfo info = (TimestepInfo) c_timesteps.get(getTimestep()-1);
	return info.getLowerLimit(i);
    }

    public float getOffset(int i)
    {
	TimestepInfo info = (TimestepInfo) c_timesteps.get(getTimestep()-1);
	return info.getOffset(i);
    }

    public ID getResourceX()
    {
	return c_resourceX;
    }

    public ID getResourceY()
    {
	return c_resourceY;
    }

    public float getSlope(int i)
    {
	TimestepInfo info = (TimestepInfo) c_timesteps.get(getTimestep()-1);
	return info.getSlope(i);
    }

    public float getUpperLimit(int i)
    {
	TimestepInfo info = (TimestepInfo) c_timesteps.get(getTimestep()-1);
	return info.getUpperLimit(i);
    }

    public boolean isXIn()
    {
	return c_isXIn;
    }

    public boolean isYIn()
    {
	return c_isYIn;
    }

    /**
     * This parses data and initializes this function with values-
     * The linked list of data looks like this
     * 1. 'label' -> data
     * 2. 'resourceX.type' -> data  (optional)
     * 3. 'resourceY.type' -> data  (optional)
     * 4. 'x_in' -> boolean
     * 5. 'y_in' -> boolean
     * <iterative>
     * 6. T
     * 7. 'start' -> number
     * 8. 'end' -> number
     * 9. 'slope' -> number
     * 10. 'offset' -> number
     * </iterative>
     *
     * @param data A linked list with data to parse. With this
     * data we initialize this function's settings.
     * @param rc A control with all available resources.
     * @param createMissingResources Is used when this function
     * should have a resource that is not globally defined. Is true
     * if the resource should be globally created, false it it
     * should be ignored.
     */
    public void parseData(LinkedList data, ResourceControl rc,
			  boolean createMissingResources)
        throws RmdParseException
    {
	//for( int testa = 0; testa < data.size(); testa++)
	//    System.out.println(data.get(testa));

	setTimesteplevel((Timesteplevel) data.removeLast());

	// first we get the label of the function
	if (((String) data.getFirst()).equals("label")) {
	    data.removeFirst(); //Throw away the tag
	    setLabel((String)data.removeFirst());
	}

	// second we don't really know, could be a resource
	//Pum5 2007 added Error handling
	if (((String) data.getFirst()).equals("resourceX.type")) {
	    data.removeFirst(); // Ignore the tag
	    String resource = (String) data.removeFirst();
	    ID resourceID = rc.getResourceID(resource);
	    if (resourceID == null)
	    	if(createMissingResources)
	    		rc.addResource(resource, null, null);
	    	else
	    		throw new RmdParseException("No such Resource: "+ resource);
	    setResourceX(rc.getResourceID(resource));
	}
	//Pum5 2007 added Error handling
	if (((String) data.getFirst()).equals("resourceY.type")) {
	    data.removeFirst();
	    String resource = (String) data.removeFirst();
	    ID resourceID = rc.getResourceID(resource);
	    if (resourceID == null && createMissingResources)
	    	if (resourceID == null)
		    	if(createMissingResources)
		    		rc.addResource(resource, null, null);
		    	else
		    		throw new RmdParseException("No such Resource: "+ resource);
	    setResourceY(rc.getResourceID(resource));
	}

	// changed Pum5 2007 added error handling
	if (((String) data.getFirst()).equals("x_in")) {
	    data.removeFirst();
	    String xin = (String) data.removeFirst();
	    if (xin.equals("true")||xin.equals("1"))
		setXIn(true);
	    else if (xin.equals("false")||xin.equals("0")) 
	    	setXIn(false);
	    else
	    	throw new RmdParseException("unknown boolean value: "+xin.toString());
	}
	//changed Pum5 2007
	if (((String) data.getFirst()).equals("y_in")) {
            data.removeFirst();
	    String yin = (String) data.removeFirst();
	    if (yin.equals("true")||yin.equals("1"))
		setYIn(true);
	    else if (yin.equals("false")||yin.equals("0")) 
	    	setYIn(false);
	    else
	    	throw new RmdParseException("unknown boolean value: "+yin.toString());
	}

	// iterate through all the timesteps
	for (int i = 0; data.size() > 0 &&
		 ((String) data.getFirst()).equals("T"); i++) {
	    data.removeFirst(); // remove the "T"
	    //	    System.out.println("timestep " + i);
	    TimestepInfo info = (TimestepInfo) c_timesteps.get(i);
	    for (int j = 0; data.size() > 0 &&
		     ((String) data.getFirst()).equals("start"); j++) {
		data.removeFirst();
		data.removeFirst();
		info.add();
		//		System.out.println("adding a limit to timestep " + i);
		try {
		    if (((String) data.getFirst()).equals("end")) {
			data.removeFirst();
			info.setUpperLimit(j+1, Float.
					   parseFloat((String) data.removeFirst()));
		    }
		    if (((String) data.getFirst()).equals("slope")) {
			data.removeFirst();
			info.setSlope(j+1, Float.
				      parseFloat((String) data.removeFirst()));
		    }
		    if (((String) data.getFirst()).equals("offset")) {
			data.removeFirst();
			info.setOffset(j+1, Float.
				       parseFloat((String) data.removeFirst()));
		    }
		    //		    System.out.println("last is = " + data.getFirst());
		}
		catch (NumberFormatException e) {
		    throw new RmdParseException("The flowdependency data " +
						"is incorrect. <end>, " +
						"<slope>, or <offset> " +
						"tag contains a non-number " +
						"string, should be a number.");
		}
	    }
	}
    }

    public void removeLimit()
    {
	TimestepInfo info = (TimestepInfo) c_timesteps.get(getTimestep()-1);
	info.remove();
    }

    /**
     * Sets the variables in a certain timestep and for a certain
     * part of the flow limits.. lowerLimit < limit < upperLimit
     * @param i What limit to set the variables on
     * @param lowerLimit The lower flow that these constraints
     * will apply on.
     * @param upperLimit The upper flow that these contraints
     * will apply on.
     * @param slope The slope of the linear equation (y = k*x + m)
     * @param offset The offset of the linear equation
     */
    public void setLimitInfo(int limit, float upperLimit,
			     float slope, float offset)
    {
	TimestepInfo info = (TimestepInfo) c_timesteps.get(getTimestep()-1);

	info.setUpperLimit(limit, upperLimit);
	info.setSlope(limit, slope);
	info.setOffset(limit, offset);
    }

    public void setResourceX(ID resource)
    {
	c_resourceX = resource;
    }

    public void setResourceY(ID resource)
    {
	c_resourceY = resource;
    }

    public void setXIn(boolean xin)
    {
	c_isXIn = xin;
    }

    public void setYIn(boolean yin)
    {
	c_isYIn = yin;
    }

    public String toXML(ResourceControl resources, int indent)
    {
	String xml = XML.indent(indent) + "<flowDependency>" + XML.nl();

	if (getLabel() != null)
	    xml = xml + XML.indent(indent+1) + "<label>" +
		XML.toXML(getLabel()) + "</label>" + XML.nl();

	if (c_resourceX != null)
	    xml = xml + XML.indent(indent+1) + "<resourceX.type>" +
		XML.toXML(resources.getLabel(c_resourceX)) +
		"</resourceX.type>" + XML.nl();

	if (c_resourceY != null)
	    xml = xml + XML.indent(indent+1) + "<resourceY.type>" +
		XML.toXML(resources.getLabel(c_resourceY)) +
		"</resourceY.type>" + XML.nl();

	xml = xml + XML.indent(indent+1) +"<x_in>" +
	    (new Boolean(c_isXIn)).toString() + "</x_in>" + XML.nl() +
	    XML.indent(indent+1) + "<y_in>" +
	    (new Boolean(c_isYIn)).toString() + "</y_in>" + XML.nl();

	// add all timesteps
	for (int i = 0; i < c_timesteps.size(); i++) {
	    xml = xml + XML.indent(indent+1) +
		"<timestep.flowDependency nr=\"" +
		(i+1) + "\">" + XML.nl();
	    xml = xml + ((TimestepInfo) c_timesteps.get(i)).toXML(indent+2);

	    xml = xml + XML.indent(indent+1) +
		"</timestep.flowDependency>" + XML.nl();
	}
	xml = xml + XML.indent(indent) + "</flowDependency>" + XML.nl();

	return xml;
    }
    
    /**
     *  PUM5 Added
     * 
     */
    public void toEXML(ResourceControl resources,ExmlSheet sheet) {
		
		//Add Label
		String label = ((this.getLabel()==null)?"":this.getLabel());

		//Add function header
		sheet.addFunctionHeader("FlowDependency", label);

		//Add data
		String resx = ((resources.getLabel(c_resourceX)==null)?"":XML.toXML(resources.getLabel(c_resourceX)));
		String resy = ((resources.getLabel(c_resourceY)==null)?"":XML.toXML(resources.getLabel(c_resourceY)));
		sheet.addRow(sheet.addLockedCell("ResX")+sheet.addCell(resx));		
		sheet.addRow(sheet.addLockedCell("ResY")+sheet.addCell(resy));
		sheet.addRow(sheet.addLockedCell("Xin")+sheet.addCell((new Boolean(c_isXIn))));		
		sheet.addRow(sheet.addLockedCell("Yin")+sheet.addCell((new Boolean(c_isYIn))));

		// Add timestep nrs in one Row.
		int numberOfTimesteps = getTimesteps();
		sheet.addTimeStepRow(numberOfTimesteps);

		// Determine maximum number of flows to initialize table
		int maxSlope = 0;
		TimestepInfo info;
		for( int i = 0; i < numberOfTimesteps; i++){		
			info = (TimestepInfo) c_timesteps.elementAt( i );
			maxSlope = (info.getSize() > maxSlope)?info.getSize():maxSlope;
		}
		
		//Add Timestep data
		sheet.initTable(maxSlope*4, numberOfTimesteps);
		for (int i = 0; i < maxSlope;i++) { 
			sheet.addLockedTableValue("Start");
			sheet.addLockedTableValue("End");
			sheet.addLockedTableValue("Slope");
			sheet.addLockedTableValue("Offset");
		}
		for( int i = 0; i < numberOfTimesteps; i++){
			 info = ((TimestepInfo) c_timesteps.get(i));
			 info.toEXML(sheet);
			    if (info.getSize() < maxSlope)
			    	sheet.fillTableToNextCol();
		}
		sheet.endTable();
		sheet.addRow(sheet.addCell(""));
    }

    /* Remove this and replace with getTimesteps */
    private int getSize()
    {
	return getTimesteps();
    }

    protected int getTimesteps()
    {
	return c_timesteps.size();
    }

    protected void timestepInsertAt(int index)
    {
	c_timesteps.insertElementAt(new TimestepInfo(), index);
    }

    protected void timestepRemoveAt(int index)
    {
	c_timesteps.removeElementAt(index);
    }

    protected void timestepSetMoreDetailed(int factor)
    {
	int oldsize = c_timesteps.size();
	int newsize = oldsize * factor;

	//Copy old cost values to new cost array
	Vector newTimestepInfo = new Vector(newsize,1);
	TimestepInfo info;
	for(int i = 0; i < oldsize; i++) {
	    info = ((TimestepInfo) c_timesteps.get(i));
	    for(int k = 0; k < factor; k++) {
		newTimestepInfo.add(new TimestepInfo(info));
	    }
	}
	c_timesteps = newTimestepInfo;
    }

    protected void timestepSetLessDetailed(int newSize, int factor)
    {
	Vector newTimestepInfo = new Vector(newSize);
	int i, oldindex;

	//Copy old cost values to new cost array
	for(i = 0, oldindex = 0; i < newSize; i++, oldindex += factor)
	    newTimestepInfo.add(c_timesteps.get(oldindex));

	c_timesteps = newTimestepInfo;
    }

    protected void timestepResetData(int size)
    {
	//Create vector and reset to zero
	c_timesteps = new Vector(size);
	for(int i = 0; i < size; i++)
	    c_timesteps.add(new TimestepInfo());
    }

    private void checkIfValid(Vector toFlows, Vector fromFlows,
			      Vector xFlows, Vector yFlows, ID node)
	throws ModelException
    {
	if (c_resourceX == null || c_resourceY == null)
	    throw new ModelException("Resources in node "+node+" function "
				     + getLabel() + " is not properly specified.\n" +
				     "Cannot optimize.");

	// if both flows are going into the node or if both
	// are going out and they use the same resource,
	// then we can't optimize.
	if (c_resourceX.equals(c_resourceY) && (c_isXIn == c_isYIn))
	    throw new ModelException("You can't use the same resource " +
				     "for resourceX and resourceY if you " +
				     "have them both into the node or out.");

	// Check what ingoing flows have the same resource as
	// this function
	if (c_isXIn || c_isYIn) {
	    for (int i = 0; i < toFlows.size(); i++) {
		Flow flow = ((Flow) toFlows.get(i));
		if (flow.getResource() == null)
		    throw new ModelException("Resource for flow "+flow.getID()+
					     " is not specified.\n\n"+
					     "Can not optimize.");
		if (c_isXIn && flow.getResource().equals(getResourceX()))
		    xFlows.addElement(flow);
		else if (c_isYIn && flow.getResource().equals(getResourceY()))
		    yFlows.addElement(flow);
	    }
	    if ((c_isXIn && xFlows.size() <= 0) ||
		(c_isYIn && yFlows.size() <= 0))
		throw new ModelException("No flow going in to node " +
					 node + " has the same resource as " +
					 "the 'Flow Dependency' " +
					 "function inside it.\n" +
					 "Can not optimize.");
	}

	// Check what outgoing flows have the same resource as
	// this function have
	if (!c_isXIn || !c_isYIn) {
	    for (int i = 0; i < fromFlows.size(); i++) {
		Flow flow = ((Flow) fromFlows.get(i));
		if (flow.getResource() == null)
		    throw new ModelException("Resource for flow "+flow.getID()+
					     " is not specified.\n\n"+
					     "Can not optimize.");
		if (!c_isXIn && flow.getResource().equals(getResourceX()))
		    xFlows.addElement(flow);
		if (!c_isYIn && flow.getResource().equals(getResourceY()))
		    yFlows.addElement(flow);
	    }
	}

	// now neither xFlows nor yFlows can be empty
	if (xFlows.size() <= 0 || yFlows.size() <= 0)
	    throw new ModelException("Resource X or Y in 'Flow Dependency'" +
				     " in node " + node + " has no flow " +
				     "with the same resource.\n" +
				     "Can not optimize.");
    }

  public boolean isRelatedToFlow(ID flow) {
    return false;
  }
}
