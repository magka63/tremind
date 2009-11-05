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

/**
 * Sums up information about equations
 *
 * @author Peter Andersson
 * @author Johan Trygg
 * @version 2001-07-26
 */
public class Equation
{
    // Finals for the operator enumeration
    public final static String GOALORFREE = "N";
    public final static String LOWEROREQUAL = "L";
    public final static String EQUAL = "E";
    public final static String GREATEROREQUAL = "G";
    public final static String LOWERBOUND = "LO";
    public final static String UPPERBOUND = "UP";
    public final static String FIXED = "FX";
    public final static String FREE = "FR";
    public final static String MINUSINFINITY = "MI";
    public final static String PLUSINFINITY = "PL";
    public final static String T_string = "T";
    public final static String E_string = "E";

    // To uniquely identify function equations
    private ID c_node;
    private ID c_function;
    private int c_timestep;
    private int c_equation;

    // Other things to keep track of
    private Vector c_variables;
    private String c_operator;
    private float c_RHS;

    private String c_name;
    private boolean c_usingName = false;
    private String c_ID_as_string;
    private boolean c_ID_created = false;

    /**
     * A nullconstructor
     */
    public Equation()
    {
	c_node = null;
	c_function = null;
	c_timestep = 1;
	c_equation = 1;

	c_variables = new Vector(3,3);
	c_operator = GOALORFREE;
	c_RHS = 0;

        create_getID_string();
    }

    /**
     * Constructor for a flowequation
     * RHS default to 0
     * @param node The node the function was generated for
     * @param function The function that generated this equation
     * @param timestep Which timestep
     * @param equation Which equation
     * @param operator Type of operator
     * @throws IllegalArgumentException when arguments mismatch
     */
    public Equation(ID node, ID function, int timestep,
		    int equation, String operator)
    throws IllegalArgumentException
    {
	if (!node.isNode())
	    throw new IllegalArgumentException("Argument node is not a node ID");
	if (!function.isFunction())
	    throw new IllegalArgumentException("Argument flow is not a flow ID");
	else if (timestep < 0)
	    throw new IllegalArgumentException("Argument timestep is negative");

	c_node = node;
	c_function = function;
	c_timestep = timestep;
	c_equation = equation;

	c_variables = new Vector(2,1);
	c_operator = operator;
	c_RHS = 0;

        create_getID_string();
    }

    /**
     * Constructor for a flowequation
     * @param node The node the function was generated for
     * @param function The function that generated this equation
     * @param timestep Which timestep
     * @param equation Which equation
     * @param operator Type of operator
     * @param rhs The right hand side
     * @throws IllegalArgumentException when arguments mismatch
     */
    public Equation(ID node, ID function, int timestep, int equation,
		    String operator, float rhs)
	throws IllegalArgumentException
    {
	if (!node.isNode())
	    throw new IllegalArgumentException("Argument node is not a node ID");
	if (!function.isFunction())
	    throw new IllegalArgumentException("Argument flow is not a flow ID");
	else if (timestep < 0)
	    throw new IllegalArgumentException("Argument timestep is negative");

	c_node = node;
	c_function = function;
	c_timestep = timestep;
	c_equation = equation;

	c_variables = new Vector(2,1);
	c_operator = operator;
	c_RHS = rhs;

        create_getID_string();
    }

    /**
     * Constructor for a flowequation with a specific name
     * RHS default to 0
     * @param node The node the function was generated for
     * @param name The name of theis equation
     * @param timestep Which timestep
     * @param operator Type of operator
     * @throws IllegalArgumentException when arguments mismatch
     */
    public Equation(ID node, String name, int timestep,
		    String operator)
    throws IllegalArgumentException
    {
	if (timestep < 0)
	    throw new IllegalArgumentException("Argument timestep is negative");

	c_node = null;
	c_function = null;
	c_timestep = timestep;
	c_equation = 0;
	c_name = name;
	c_usingName = true;

	c_variables = new Vector(2,1);
	c_operator = operator;
	c_RHS = 0;

        create_getID_string();
    }

    /**
     * Adds a variable and it's coefficient to the equation
     * @param variable A variable to add.
     */
    public void addVariable(Variable var)
    {
	c_variables.add(var);
    }

    /**
     * Removes a variable from the list
     * @param variable The variable to be removed
     * @return True if variable was removed
     */
    public boolean removeVariable(Variable var)
    {
	return c_variables.remove(var);
    }

    /**
     * Gets all of the equations variables
     * @return A vector with all variables.
     */
    public Vector getAllVariables()
    {
	return c_variables;
    }

    /**
     * Gets the equations ID
     * This is called millions of millions of times!!!
     * @return The equations unique ID on the form
     *         <nodeID><functionID>T<timestep>E<equation><br>
     *         or <flowID><timestep><br>
     *	       or OBJ
     */
    public String getID()
    {
        return c_ID_as_string;
    }


    /**
     * inits the private string c_ID_as_string. This is a performance
     * enhancement
     */
    private String create_getID_string()
    {
      if (c_usingName) {
        c_ID_as_string =  c_name + T_string + c_timestep;
        return c_ID_as_string;
      }

      if (c_operator.equals(GOALORFREE)) {
        c_ID_as_string =  "OBJ";
        return c_ID_as_string;
      }
      else  {
        // this is what makes this equation unique

    	// NOTE: If-statement added by Johan Sandberg to avoid exception
    	// when updating a function editor dialog. 
	// What happens is that the c_function is null when this method 
	// is called from a function editor dialog.  
    	if (c_function != null) {
          c_ID_as_string =  c_function.toString() +
                            E_string + 
                            c_equation + 
                            T_string + 
                            c_timestep;
        }
        return c_ID_as_string;
      }
    }

    /**
     * Sets the equations ID<br><br>
     * Set all IDs to null to define goalfunction.
     * @param node The node the equation was generated in
     * @param function The function the equation was generated in
     * @param timestep The timestep the equation is active in
     * @param equation The number of the equation (if a function generates more than
     *                 one equation.
     * @throws IllegalArgumentException if arguments are illegal
     */
    public void setID(ID node, ID function, int timestep, int equation)
    throws IllegalArgumentException
    {
	if ( !node.isNode() )
	    throw new IllegalArgumentException("Argument node is not a Node ID");
	else if ( !function.isFunction() )
	    throw new IllegalArgumentException("Argument function is not a Function ID");
	else if ( timestep < 0 )
	    throw new IllegalArgumentException("Argument timestep is negative");
	else if ( equation < 0 )
	    throw new IllegalArgumentException("Argument equation is negative");

	c_node = node;
	c_function = function;
	c_timestep = timestep;
	c_equation = equation;

        create_getID_string();
    }

    /**
     * Sets the flowequations ID<br><br>
     * @param flow The flow the equation was generated for
     * @param timestep The timestep the flow is active in
     * @throws IllegalArgumentException if arguments are illegal
     */
    public void setID(ID flow, int timestep)
    throws IllegalArgumentException
    {
	if (!flow.isFlow())
	    throw new IllegalArgumentException("Argument flow is not a flow ID");
	else if (timestep < 0)
	    throw new IllegalArgumentException("Argument timestep is negative");

	c_node = null;
	c_function = flow;
	c_timestep = timestep;
	c_equation = 0;

        create_getID_string();
    }


    /**
     * Gets the RHS of the equation
     * @return The RHS of the equation
     */
    public float getRHS()
    {
	return c_RHS;
    }

    /**
     * Sets the RHS of the equation
     * @param RHS The RHS to be set
     */
    public void setRHS(float rhs)
    {
	c_RHS = rhs;
    }

    /**
     * Gets a varible from the equation
     * @param variable The variable to be found.
     * @return The coefficient of the variable
     * @throws MathException If the variable could not be found
     */
    public float getVariable(Variable var)
	throws MathException
    {
	try {
	    return ((Variable)c_variables.get(c_variables.indexOf(var))).getCoefficient();
	}
	catch (ArrayIndexOutOfBoundsException e) {
	    throw new MathException(e.toString());
	}
    }

    /**
     * Gets the equations operator
     * @return A string consisting of the operator. The string corresponds
     *         to mps-file operators.
     */
    public String getOperator()
    {
	return c_operator;
    }

    public ID getNode()
    {
	return c_node;
    }

    public ID getFunction()
    {
	return c_function;
    }

    public int getTimestep()
    {
	return c_timestep;
    }

    /**
     * Sets the operator
     * @param operator The operator to be set, must be in the enumeration
     * @throws IllegalArgumentException If the operator is not in the enumeration
     */
    public void setOperator(String operator)
	throws IllegalArgumentException
    {
	if (!isOperatorEnum(operator))
	    throw new IllegalArgumentException();
	else {
          c_operator = operator;
          create_getID_string();
        }
    }

    /**
     * Returns the equations ID, same as getID() (but not any longer)
     * @return the equations ID in a string form.
     */
    public String toString()
    {
	return ("var=" + c_variables.toString() + ", op=[" + c_operator +
		"], " + "rhs=[" + c_RHS + "]");
	// TT	return getID();
    }

    /**
     * Checks if the current equation is a bound or not
     * @return True if the equation is a bound, false otherwise
     */
    public boolean isBound()
    {
	return (c_operator.equals(LOWERBOUND) ||
		c_operator.equals(UPPERBOUND) ||
		c_operator.equals(FIXED) ||
		c_operator.equals(FREE) ||
		c_operator.equals(MINUSINFINITY) ||
		c_operator.equals(PLUSINFINITY));
    }

    /**
     * Checks if the current equation is a equation or a bound
     * @return true if an equation
     */
    public boolean isEquation()
    {
	return !isBound();
    }

    /**
     * Checks if two equations are equal, equality is defined by
     * equals between the equationIDs
     * @param equation The equation to compare with.
     * @return True if the equations are equal
     */
    public boolean equals(Object eq)
    {
        create_getID_string();
	Equation equation = (Equation) eq;
	return getID().equals(equation.getID());
    }

    /**
     * Adds two equations together<br>
     * Only regards variables and RHS.
     * @param equation the equation to sum with.
     */
    public void addWith(Equation equation)
    {
	Vector equationVariables = equation.getAllVariables();
	for (int i = 0; i < equationVariables.size(); i++) {
	    if (c_variables.contains(equationVariables.get(i))) {
		//Variable exists in both equations, sum it
		int index = c_variables.indexOf(equationVariables.get(i));
		Variable var = (Variable)c_variables.get(index);
		var.add(((Variable)equationVariables.get(i)).getCoefficient());
	    }
	    else {
		//Variable does not exist, add it
		c_variables.add(equationVariables.get(i));
	    }
	}
	c_RHS += equation.getRHS();
    }

    /**
     * Checks enumeration of operator
     * @param operator The operator to be checked
     * @return True if the operator is in the enumeration
     */
    private boolean isOperatorEnum(String operator)
    {
	return (operator.equals(GOALORFREE) ||
		 operator.equals(EQUAL) ||
		 operator.equals(GREATEROREQUAL) ||
		 operator.equals(LOWEROREQUAL) ||
		 operator.equals(LOWERBOUND) ||
		 operator.equals(UPPERBOUND) ||
		 operator.equals(FIXED) ||
		 operator.equals(FREE) ||
		 operator.equals(MINUSINFINITY) ||
		 operator.equals(PLUSINFINITY));
    }
}
