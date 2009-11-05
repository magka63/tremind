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
 * Class to uniqely indentify and store information on variables
 *
 * @author Peter Andersson
 * @author Tim Terlegård
 * @author Johan Trygg
 * @version 2001-07-30
 */
package mind.model;

public class Variable implements Cloneable
{
    // To uniquely identify the variable
    private ID c_flow = null;
    private ID c_node = null;
    private ID c_resource = null;
    private int c_timestep = -1;
    private ID c_function = null;
    // An equation could be divided into several parts, intervals
    private int c_part = -1;
    // whether this variable only can have integer values
    private boolean c_isInteger = false;
    private boolean c_isIntProgramming = false;
    // Other thingys
    private float c_coefficient;
    // Used if we want to give a variable a special name
    private boolean c_useingName = false;
    // Added by Nawzad Mardan 2008-06-03
    //private boolean c_absolutevalue;
    private String c_flowName;

    /**
     * Creates an empty Variable
     */
    public Variable()
    {
    }

    public Variable(ID flow, int timestep, float coefficient)
    {
	if (!flow.isFlow())
	    throw new IllegalArgumentException("Argument node is not a flowID");
	else if (timestep < 0)
	    throw new IllegalArgumentException("Argument timestep is negative");

	c_flow = flow;
	c_coefficient = coefficient;
	c_resource = null;
	c_timestep = timestep;
	c_node = null;
	c_isInteger = false;
    }

    public Variable(ID flow, int timestep, float coefficient,
		    boolean onlyIntegerAllowed)
    {
	if (!flow.isFlow())
	    throw new IllegalArgumentException("Argument node is not a flowID");
	else if (timestep < 0)
	    throw new IllegalArgumentException("Argument timestep is negative");

	c_flow = flow;
	c_coefficient = coefficient;
	c_resource = null;
	c_timestep = timestep;
	c_node = null;
	c_isInteger = onlyIntegerAllowed;
    }

    public Variable(ID flow, int timestep, ID function,
		    int part, float coefficient)
    {
	if (!flow.isFlow())
	    throw new IllegalArgumentException("Argument node is not a flowID");
	else if (timestep < 0)
	    throw new IllegalArgumentException("Argument timestep is negative");
	else if (!function.isFunction())
	    throw new IllegalArgumentException("Argument function is not a functionID");
	else if (part < 0)
	    throw new IllegalArgumentException("Argument interval is negative");

	c_flow = flow;
	c_coefficient = coefficient;
	c_part = part;
	c_function = function;
	//	c_resource = null;
	c_timestep = timestep;
	c_node = null;
	c_isInteger = false;
    }

    public Variable(ID flow, int timestep, ID function,
		    int part, float coefficient, boolean onlyIntegerAllowed)
    {
	if (!flow.isFlow())
	    throw new IllegalArgumentException("Argument node is not a flowID");
	else if (timestep < 0)
	    throw new IllegalArgumentException("Argument timestep is negative");
	else if (!function.isFunction())
	    throw new IllegalArgumentException("Argument function is not a functionID");
	else if (part < 0)
	    throw new IllegalArgumentException("Argument interval is negative");

	c_flow = flow;
	c_coefficient = coefficient;
	c_part = part;
	c_function = function;
	//	c_resource = null;
	c_timestep = timestep;
	c_node = null;
	c_isInteger = onlyIntegerAllowed;
    }

    /**
     * is used by Flow Dependency, a timestep can be further divided
     * that's what part is for
     */
    public Variable(int timestep, ID function,
		    int part, float coefficient)
    {
	if (timestep < 0)
	    throw new IllegalArgumentException("Argument timestep is negative");
	else if (!function.isFunction())
	    throw new IllegalArgumentException("Argument function is not a functionID");
	else if (part < 0)
	    throw new IllegalArgumentException("Argument interval is negative");

	c_coefficient = coefficient;
	c_part = part;
	c_isIntProgramming = true;
	c_function = function;
	c_timestep = timestep;
	c_node = null;
	c_isInteger = false;
    }

    /**
     * is used by Flow Dependency, a timestep can be further divided
     * that's what part is for
     */
    public Variable(int timestep, ID function, int part,
		    float coefficient, boolean onlyIntegerAllowed)
    {
	if (timestep < 0)
	    throw new IllegalArgumentException("Argument timestep is negative");
	else if (!function.isFunction())
	    throw new IllegalArgumentException("Argument function is not a functionID");
	else if (part < 0)
	    throw new IllegalArgumentException("Argument interval is negative");

	c_coefficient = coefficient;
	c_part = part;
	c_isIntProgramming = true;  // the variable will be Y3...
	c_function = function;
	c_timestep = timestep;
	c_node = null;
	c_isInteger = onlyIntegerAllowed;
    }

    /** This is used when you want to give a variable a special name
     *  (It is used by Flow Relation)
     * @param name The variable name
     * @param timestep the timestep
     * @param coefficient the coefficient for the variable
     */
    public Variable(String name, int timestep, float coefficient)
    {
	if (timestep < 0)
	    throw new IllegalArgumentException("Argument timestep is negative");
	c_flow = null;
	c_coefficient = coefficient;
	c_resource = null;
	c_timestep = timestep;
	c_node = null;
	c_isInteger = false;
	c_flowName = name;
	c_useingName = true;
    }
     /** This is used when you want to give a variable a special name
     *  (It is used by Flow Relation)
     * @param name The variable name
     * @param timestep the timestep
     * @param coefficient the coefficient for the variable
     *
    public Variable(String name, float coefficient)
    {
	c_flow = null;
	c_coefficient = coefficient;
	c_resource = null;
	c_node = null;
	c_isInteger = false;
	c_flowName = name;
	c_absolutevalue = true;
    }*/

    /** This is used when you want to give a an integer variable a special name.
     * (Used by Function Editor)
     * @param name The variable name
     * @param coefficient the coefficient for the variable
     */
    public Variable(float coefficient, int timestep, String name)
    {
	c_flow = null;
	c_coefficient = coefficient;
	c_resource = null;
	c_timestep = timestep;
	c_node = null;
	c_isInteger = true;
	c_flowName = name;
	c_useingName = true;
    }
    
    /**
     * Adds a float to the coefficient
     * @param value Value to add to coefficient
     */
    public void add(float value)
    {
	c_coefficient += value;
    }

    /**
     * Returns true if two variables are equal
     * @param var The variable to check with
     * @return True if both have the same ID
     */
    public boolean equals(Variable var)
    {
	return (toString().equals(var.toString()));
    }

    /**
     * Gets the variable's coefficient
     * @return The coefficient
     */
    public float getCoefficient()
    {
	return c_coefficient;
    }

    public boolean isInteger()
    {
	return c_isInteger;
    }

    /**
     * Sets the variable's coefficient
     * @param coefficient The coefficient to be set
     */
    public void setCoefficient(float coefficient)
    {
	c_coefficient = coefficient;
    }

    /**
     * Sets an unique ID for the variable
     * @param node The node the variable exists in
     * @param resource Which resource it is
     * @param timestep Which timestep the variable exists for
     */
    public void setVariableID(ID node, ID resource, int timestep)
    {
	c_node = node;
	c_resource = resource;
	c_timestep = timestep;
    }

    /**
     * Returns a textual representation of the unique ID
     * @return <NodeID><ResourceID>T<timestep>
     */
    public String toString()
    {
	if (c_useingName)
	    return c_flowName + "T" + c_timestep;
        /* If absolute value used
        if(c_absolutevalue)
            return c_flowName;*/

	// if flow isn't divided into parts
	if (c_part > 0) {
	    // the flow is divided into several intervals
	    // and every interval has to be unique
	    if (c_isIntProgramming)
		return "Y" + c_part + "T" + c_timestep +
		    c_function.toString();
	    else
		return c_flow.toString() + "T" + c_timestep +
		    c_function.toString() + "P" + c_part;
	}
	else
	    return c_flow.toString() + "T" + c_timestep;
    }
    
    public void setIsInteger(boolean b)
    {
        c_isInteger = b;
    }
    
    /**
     * Gets the variable's flow
     * @return The flow
     */
    public ID getFlow()
    {
	return c_flow;
    }
    
    /**
     * Multiplies the variable with the value
     * @param value The multiplier  
     */
    public void multiply(float value)
    {
    	c_coefficient *= value;    	
    }
    
    public Object clone() throws CloneNotSupportedException{
    	Variable clone = (Variable) super.clone();
    	clone.c_flow = this.c_flow;
    	clone.c_function = this.c_function;
    	clone.c_node = this.c_node;
    	clone.c_resource = this.c_resource;
    	return clone;
    }
}
