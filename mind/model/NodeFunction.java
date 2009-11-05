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
import java.util.LinkedList;

import mind.io.*;

/**
 * Abstract class that is extended by all functions and works like an interface
 * to the functions
 *
 * Please keep in mind that timesteps start at 1 and not 0
 * as is convention for indexes in programming.
 *
 * @author Peter Andersson
 * @author Johan Trygg
 * @version 2001-06-30
 */

public abstract class NodeFunction
    implements Cloneable
{
    private String c_label;
    private String c_description;
    private String c_type;
    private ID c_ID;
    protected Timesteplevel c_timesteplevel;
    protected int c_timestep;

    /**
     * A nullconstructor
     */
    public NodeFunction()
    {
	c_label = "";
	c_description = "No description available";

	// Only accessible when constructing. Should always be set in
	// the inheriting functions constructors.
	c_type = "Functiontype not changed";

	c_timesteplevel = null; //Must be set later
	c_timestep = 1;
    }

    /**
     * Full constructor
     * @param functionID The functions ID
     * @param type The functions Type
     * @param timesteplevel Which level of detail for timesteps
     */
    public NodeFunction( ID functionID, String type, Timesteplevel timesteplevel)
    {
	c_ID = functionID;
	c_label = type + functionID;
	c_type = type;
	c_timesteplevel = timesteplevel;
	c_timestep = 1;
    }

    /**
     * Adds a number of timesteps to the current (negative numbers are allowed)
     * @param timestep The number to add
     */
    public void addToTimestep(int timestep)
    {
	c_timestep += timestep;
	if (c_timestep > c_timesteplevel.getMaxTimesteps())
	    c_timestep -= c_timesteplevel.getMaxTimesteps();
	else if (c_timestep < 1)
	    c_timestep += c_timesteplevel.getMaxTimesteps();
    }

    /**
     * Changes how many times the timesteplevel divides the above level
     * in this function
     * @param levelToChange the Timesteplevel to change
     * @param steps new number of steps (divides)
     */
    public void changeTimesteplevel(Timesteplevel levelToChange, int newSteps)
    {
	//System.out.println("(change) oldsize=" + getTimesteps());

	int oldSteps = levelToChange.getTimesteps();
	int newSize = getTimesteps() / oldSteps * newSteps;

	if (newSteps > oldSteps) {
	    //add more timesteps
	    int stepsToAddOnHighestLevel = newSteps - oldSteps;
	    int tsDiff = c_timesteplevel.timestepDifference(levelToChange) /
		levelToChange.getTimesteps();
	    int addChunk = stepsToAddOnHighestLevel * tsDiff;
	    int skipChunk = oldSteps * tsDiff;
	    int startAdd=skipChunk;
	    while (startAdd <= newSize) {
		int endAdd = startAdd + addChunk; //Moved back inside the loop by JT 010726
                                                  //(and it should be inside!)
		for (int k = startAdd; k < endAdd; ++k)
		    timestepInsertAt(k);

		startAdd = endAdd + skipChunk;
	    }
	}
	else if (newSteps < oldSteps) {
	    //remove timesteps
	    int stepsToRemoveOnHighestLevel = oldSteps - newSteps;
	    int tsDiff = c_timesteplevel.timestepDifference(levelToChange) /
		levelToChange.getTimesteps();
	    int removeChunk = stepsToRemoveOnHighestLevel * tsDiff;
	    int skipChunk = newSteps * tsDiff;
	    int startRemove = skipChunk;
	    while (startRemove <= newSize) {
		int endRemove = startRemove + removeChunk; //Moved back inside the loop by JT 010726

		for(int k = startRemove; k < endRemove; ++k)
		    timestepRemoveAt(startRemove);

		startRemove += skipChunk;
	    }
	}
	//System.out.println("newsize=" + getTimesteps());
    }

    /**
     * Creates a new copy of the function
     * @return A complete copy
     */
    public Object clone()
	throws CloneNotSupportedException
    {
	Object clone = super.clone();
	((NodeFunction)clone).c_ID = new ID( ID.FUNCTION );
	return clone;
    }

    /**
     * Checks if two NodeFunctions are equal
     * @param function The function to compare with
     * @return true if equal
     */
    public boolean equals(NodeFunction function)
    {
	return c_ID.equals( function.getID() );
    }

    /**
     * Returns a description of the function
     * @return A short description
     */
    public String getDescription()
    {
	return c_description;
    }

    /**
     * Returns optimizationinformation from the function
     * @param maxTimesteps The number of timesteps to generate for
     * @param node The node that owns the function, ID used to set equationID
     * @return Some equations that model the functions behaviour
     * @throws ModelException If error occurs
     */
    public EquationControl getEquationControl(int maxTimesteps,
					      ID node, Vector toFlows,
					      Vector fromFlows)
	throws ModelException
    {
	return new EquationControl();
    }

    
     /**
     * Returns optimizationinformation from the function
     * @param maxTimesteps The number of timesteps to generate for
     * @param node The node that owns the function, ID used to set equationID
     * @return Some equations that model the functions behaviour
     * @throws ModelException If error occurs
      *Added by Nawzad Mardan 070801 used by Discountedsystemcost function
     */
    public EquationControl getEquationControl2(int maxTimesteps,
					      ID node, Vector toFlows,
					      Vector fromFlows)
	throws ModelException
    {
	return new EquationControl();
    }
    
      /**
     * Returns optimizationinformation from the function
     * @param data The timestep numbers for each year
     * @param annualRate The annualy rate 
     * @param timeStepValues The length of timesteps
     * @param node The node that owns the function, ID used to set equationID
     * @return Some equations that model the functions behaviour
     * @throws ModelException If error occurs
      *Added by Nawzad Mardan 070801 used by Discountedsystemcost function
     */
    public EquationControl getEquationControl2(Object [][] data,Vector annualRate,Vector timeStepValues,
					      ID node, Vector toFlows,
					      Vector fromFlows)
	throws ModelException
    {
	return new EquationControl();
    }
    /**
     * Gets which type of function the nodefunction is
     * @return A string announcing the type of function
     */
    public String getFunctionType()
    {
	return c_type;
    }

    /**
     * Returns the functions unique ID
     * @return The functions ID
     */
    public ID getID()
    {
	return c_ID;
    }

    /**
     * Gets the userdefined name of the function
     * @return The label of the function
     */
    public String getLabel()
    {
	return c_label;
    }

    /**
     * Returns a short overview of the current equations of the function
     * @return A short overview of the equation returned by the function
     */
    public String getOverview()
    {
	return "Overview not specified";
    }

    /**
     * Returns which timestep is currently defined
     * @return Current timestep
     */
    public int getTimestep()
    {
	return c_timestep;
    }

    /**
     * Current timesteplevel
     * @return The timesteplevel
     */
    public Timesteplevel getTimesteplevel()
    {
	return c_timesteplevel;
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
    public abstract void parseData(LinkedList data, ResourceControl rc,
				   boolean createMissingResources)
        throws RmdParseException;


    /**
     * Remove the timesteplevel levelToDelete from this function
     * @param levelToRemove level to delete
     */
    public void removeTimesteplevel(Timesteplevel levelToRemove)
    {
	//System.out.println("(remove) oldsize=" + getTimesteps());

	//Find level above levelToRemove
	Timesteplevel levelAbove = levelToRemove.getPrevLevel();

 	if (levelToRemove == c_timesteplevel) {
	    c_timesteplevel = levelAbove;
	}

	int oldsize = getTimesteps();
	int newsize = oldsize / levelToRemove.getTimesteps();

	/* There are two ways of removing a timesteplevel: (there are
         * more ways of course, but two is more than enough...)
         * 1. Remove from the end as many elements as the number of
         *    levels on the removed level.
         * 2. 'split' the timestep data in levelToRemove.getTimesteps()
         *     parts and remove timesteps so that every part is composed
	 *     by just one timstep. (This is much easier to draw than
	 *     to explain in words...)
	 * Which method is best?
         * Probably the first, but since the later is much easier to
         * implement, this is the way it is done.  /JT
	 */
	for (int i = oldsize-1; i >= newsize; --i)
	    timestepRemoveAt(i);

	//System.out.println("newsize=" + getTimesteps());
    }

    /**
     * Set the unique function ID
     * @param functionID The ID to set
     */
    public void setID(ID functionID)
    {
	c_ID = functionID;
    }

    /**
     * Sets a name for the function
     * @param label A new name for the function
     */
    public void setLabel(String label)
    {
	c_label = label;
    }

    /**
     * Lets the program set the current timestep
     * @param timestep The timestep to be set
     */
    public void setTimestep(int timestep)
    {
	c_timestep = timestep;
    }

    /**
     * Sets current timesteplevel
     * @param timesteplevel Which level to set
     */
    public void setTimesteplevel(Timesteplevel newLevel)
    {

	Timesteplevel oldLevel = c_timesteplevel;
	Timesteplevel topLevel = newLevel.getTopLevel();
	c_timesteplevel = newLevel;

	if (newLevel == oldLevel) return; //nothing to change

	if (oldLevel == null) { //A new function

	    //Calculate size of cost vecor
	    int size = newLevel.timestepDifference(topLevel);

	    //Create vector and reset to zero
	    timestepResetData(size);

	    return;
	}

	if (newLevel.isMoreDetailed(oldLevel)) {
	    //newLevel is more detailed, add more elements

	    //Find out how many elements we need
	    int factor = newLevel.timestepDifference(oldLevel.getNextLevel());

	    // call function specific class
	    timestepSetMoreDetailed(factor);
	}
	else { //oldLevel is more detailed, remove elements

	    //Find out how many elements we need
	    int newSize = newLevel.timestepDifference(topLevel);

	    //Find out...
	    int factor = oldLevel.timestepDifference(newLevel)
		/ newLevel.getMaxTimesteps();

	    timestepSetLessDetailed(newSize, factor);
	}
	//	System.out.println("newsize=" + getTimesteps());
    }

    /**
     * Same as getLabel()
     * @return The label of the function
     */
    public String toString()
    {
	return getLabel();
    }

    /**
     * Abstract function declared by the class NodeFunction
     * Is used to determine if a node function in any way is related to a specific flow
     * This information is needed when deleting flows in order to maintain consistency of
     * the model.
     * If the NodeFunction doesn't have any references to flows just let the implementation of
     * this function always return false.
     * @param flow ID
     * @return  true means that the function references to this flow and sholdn't be removed.
     */
    public abstract boolean isRelatedToFlow(ID flow);

    public abstract String toXML(ResourceControl resource, int indent);
    // Pum5 Added:
    public abstract void toEXML(ResourceControl resource, ExmlSheet sheet);
    protected abstract int getTimesteps();
    protected abstract void timestepInsertAt(int index);
    protected abstract void timestepRemoveAt(int index);
    protected abstract void timestepSetMoreDetailed(int factor);
    protected abstract void timestepSetLessDetailed(int newSize, int factor);
    protected abstract void timestepResetData(int size);
}

