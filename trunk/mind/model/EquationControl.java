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

import java.util.Vector;

/**
 * Keeps track of equations and implements common function to interface and sum up equations
 *
 * @author Peter Andersson
 * @author Johan Trygg
 * @version 2001-07-27
 */
public class EquationControl
    extends Vector
{
    ID c_consumerResource = null;
    ID c_producerResource = null;

    
//  used by FunctionEditor when validating in flows and out flows
    Vector c_consumerResources = new Vector(5);
    Vector c_producerResources = new Vector(5);

    /**
     * Creates an empty equationcontrol
     */
    public EquationControl()
    {
	super();
    }

    /**
     * Gets all equations not bounds nor goalfunction
     * @return All equations not bounds nor goalfunction
     */
    public Vector getAllEquations()
    {
	Vector allEquations = new Vector(size());
	for (int i = 0; i < size(); i++){
	    if (((Equation)get(i)).isEquation()){
		allEquations.add(get(i));
	    }
	}
	return allEquations;
    }

    /**
     * Gets all equations defined as bounds
     * @return A vector with all equations defined as bounds
     */
    public Vector getAllBounds()
    {
	Vector allBounds = new Vector(size());
	for (int i = 0; i < size(); i++){
	    if (((Equation)get(i)).isBound()) {
		allBounds.add(get(i));
	    }
	}
	return allBounds;
    }

    /**
     * Gets all equations that should be defined under RHS
     * @return a vector with equations
     */
    public Vector getAllRHS()
    {
	Vector allRHS = new Vector(size());
	for (int i = 0;i<size();i++) {
	  if (((Equation)get(i)).isEquation() &&
	      !((Equation)get(i)).getOperator().equals(Equation.GOALORFREE))
	  {
	      allRHS.add(get(i));
	  }
	}
	return allRHS;
    }


    /**
     * Gets all variables from the Equations into a Vector
     * @return A vector with all variables
     */
    public Vector getAllVariables()
    {
	Vector allVariables = new Vector(size(),5);
	Vector variables;
	for (int i = 0; i < size(); i++){
	    variables = ((Equation)get(i)).getAllVariables();

	    // Remove all variables in variables that are in
	    // allVariables first so that we won't get doubles.
	    allVariables.removeAll(variables);
	    allVariables.addAll(variables);
	}
	return allVariables;
    }

    /**
     * Merges two Equationcontrols into one.
     * @param control The EquationControl to merge with
     */
    public void mergeWith(EquationControl control)
    {
	for (int i = 0; i < control.size(); i++) {
	    if (contains(control.get(i))) {
		Equation existingEquation =
		    (Equation) get(indexOf(control.get(i)));
		existingEquation.addWith((Equation)control.get(i));
	    }
	    else {
		add(control.get(i));
	    }
	}
	//	control.removeAllElements();
    }

    /**
     * Adds an Equation to an equationcontrol
     * @param equation The equation to add
     * @throws IllegalArgumentException if the equation
     * already exists in the control
     */
    public void add(Equation equation)
	throws IllegalArgumentException
    {
	if (equation == null)
	    return;
	if (contains(equation))
	    throw new IllegalArgumentException("Equation already exists");

	// if the we only have one variable we can use
	// BOUNDS instead of having a new equation in the mps file
	/*
        //No we cannot, if we do Cplex cannot solve the equation system. /JT 010727

 	if (equation.getAllVariables().size() == 1) {
	     if (equation.getOperator().equals(Equation.LOWEROREQUAL))
	 	equation.setOperator(Equation.UPPERBOUND);
	     else if (equation.getOperator().equals(Equation.GREATEROREQUAL))
		 equation.setOperator(Equation.LOWERBOUND);
 	}
	*/

	super.add(equation);
    }

    /*    public void setFunctionType(int type, Resource res);
    {
	c_type = type;
	c_resource = res;
    }
    */


    /* These functions are used when generating the equations that connect the in
     * flows of a resource with the out flows of the same resource for a node.
     * (look in Node.java)
     */
    public void setConsumer(ID res)
    {
	//A function can only be consumer of one resource for now
	c_consumerResource = res;
    }

    public void setProducer(ID res)
    {
	//A function can only be producer of one resource for now
	c_producerResource = res;
    }

    public ID getConsumer()
    {
	return c_consumerResource;
    }

    public ID getProducer()
    {
	return c_producerResource;
    }
    
//  used by FunctionEditor when validating in flows and out flows
    public void setConsumers(ID res)
    {
		if (!(c_consumerResources.contains(res)))
			c_consumerResources.add(res);
    }
    
	//used by FunctionEditor when validating in flows and out flows
    public void setProducers(ID res)
    {
		if (!(c_producerResources.contains(res)))
			c_producerResources.add(res);
    }
	
	//used by FunctionEditor when validating in flows and out flows
    public Vector getConsumers()
    {
		return c_consumerResources;
    }
	
	//used by FunctionEditor when validating in flows and out flows
    public Vector getProducers()
    {
		return c_producerResources;
    }

}
