
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

import mind.io.*;

/**
 * Defines a timesteplevel
 *
 * @author Peter Andersson
 * @author Johan Trygg
 * @version 2001-07-30
 */

public class Timesteplevel
{
    private int c_numTimesteps;
    private String c_label;
    private Timesteplevel c_nextLevel = null;
    private Timesteplevel c_prevLevel = null;
    public static Timesteplevel c_topLevel;
    private Vector c_stepLengths = null;
    private Vector c_stepNames = null;

    /**
     * Creates a new timesteplevel
     * @param label A label for the timesteplevel
     * @param maxTimesteps How many timesteps there max is for this level
     */
    public Timesteplevel(String label, int maxTimesteps)
    {
	c_label = label;
	c_numTimesteps = maxTimesteps;
	c_nextLevel = null;
	c_prevLevel = null;
    }

    /**
     * Creates a new timesteplevel
     * @param label A label for the timesteplevel
     * @param maxTimesteps How many timesteps there max is for this level
     * @param nextLevel The next timesteplevel
     * @param prevLevel The previous timesteplevel
     */
    public Timesteplevel(String label, int maxTimesteps,
			 Timesteplevel nextLevel, Timesteplevel prevLevel)
    {
	c_label = label;
	c_numTimesteps = maxTimesteps;
	c_nextLevel = nextLevel;
	c_prevLevel = prevLevel;
    }

    /**
     * Sets the name vector
     * @param namesVector
     */
    public void setNamesVector(Vector namesVector)
    {
      c_stepNames = (Vector) namesVector.clone();
    }
    /**
     * Gets a copy (not reference) to the vector containing step names
     * @return a copy of the names vector
     */
    public Vector getNamesVector()
    {
      if (c_stepNames != null) {
        Vector temp = new Vector();
        temp = (Vector) c_stepNames.clone();
        return temp;
      } else
        return null;
    }

    /**
     * Sets the length vector
     * @param lengthsVector
     */
    public void setLengthsVector(Vector lengthsVector)
    {
      c_stepLengths = (Vector) lengthsVector.clone();
    }

    /**
     * Gets a copy (not reference) to the vector containing step lengths
     * @return a copy of the lengths vector
     */
    public Vector getLengthsVector()
    {
      if (c_stepLengths != null) {
        Vector temp = new Vector();
        temp = (Vector) c_stepLengths.clone();
        return temp;
      } else
        return null;
    }

    /**
     * Adjusts the step length vector for this level according to the parent level
     * This function is used when a length has been changed
     */
    public void adjustStepLengths()
    {
      if (c_prevLevel == null) {
        return;
        // We are trying to adjust the top level.
      }

      int c_prevLevelSteps = c_prevLevel.getTimesteps();
      Vector c_prevLevelLengths = c_prevLevel.getLengthsVector();

      if (c_prevLevelLengths.size() != c_prevLevelSteps) {
        // Serious error throw an exception
        javax.swing.JOptionPane.showMessageDialog(null, "Actual length of lengthsVector did not match the number of steps in Step level");
        return;
      }

      java.util.Enumeration enu = c_prevLevelLengths.elements();

      int stepCounter = 0;
      float sum = 0.0f;
      float oldValue=0.0f;
      Float newValue;
      float prevLength;

      while (enu.hasMoreElements()) {

        prevLength = ((Float)enu.nextElement()).floatValue();
        sum = 0.0f;
        for (int i = stepCounter * c_numTimesteps;
             i <= stepCounter * c_numTimesteps + c_numTimesteps - 1; i++) {
          sum += ( (Float) c_stepLengths.get(i)).floatValue();
        }
        for (int i = stepCounter * c_numTimesteps;
             i <= stepCounter * c_numTimesteps + c_numTimesteps - 1; i++) {
          oldValue = ((Float) c_stepLengths.get(i)).floatValue();
          newValue = new Float(prevLength/sum*oldValue);
          c_stepLengths.setElementAt(newValue, i);
        }

        stepCounter++;
      }
    }

    /**
     * Should be called for each level below a level that has changed the
     * c_numTimesteps. The lenghts vector is reset so adjustStepLengths should be
     * called afterwards
     */
    public void adjustNumTimesteps()
    {
      if (c_prevLevel == null) {
        return;
        // We are trying to adjust the top level.
      }

      int newSize = c_prevLevel.getLengthsVector().size() * getTimesteps();

      Vector newNamesVector = new Vector(newSize);
      Vector newLengthsVector = new Vector(newSize);

      for (int i=0; i < newSize; i++) {
        newLengthsVector.add(new Float(1.0));
        newNamesVector.add(getLabel()+(i+1));
      }

      setNamesVector(newNamesVector);
      setLengthsVector(newLengthsVector);
    }

    /** Compares two timesteplevels
     *
     * Use ordinary == instead! (and compare the address of the
     * timesteplevel objects) The functions in the Timesteplevel class are
     * using ==, so if == thinks two levels are not equal, then no one
     * else should think that either. /JT
     */
    /*
    public boolean equals(Object object)
    {
	Timesteplevel level = (Timesteplevel) object;
	if (level == null)
	    return false;

	if (getPrevLevel() == null && level.getPrevLevel() != null)
	    return false;
	if (getPrevLevel() != null && level.getPrevLevel() == null)
	    return false;
	if (getNextLevel() == null && level.getNextLevel() != null)
	    return false;
	if (getNextLevel() != null && level.getNextLevel() == null)
	    return false;

	//Hm... I don't think this is enougth. To really compare two levels we
	//must loop througt all previous and next levels and see that
	//their labels are equal. To use == is much easier. /JT
	if ((getPrevLevel() == null && level.getPrevLevel() == null) ||
	    (getPrevLevel().getLabel().equals(level.getPrevLevel().getLabel())))
	{
	    if ((getNextLevel() == null && level.getNextLevel() == null) ||
		(getNextLevel().getLabel().equals(level.getNextLevel().getLabel())))
	    {
		return true;
	    }
	}
	return false;
    }
    */


    /**
     * Sets the top timesteplevel
     * @param tsl the top Timesteplevel
     */
    public void setTopLevel(Timesteplevel tsl)
    {
	c_topLevel = tsl;
    }

    /**
     * works just like getLevel...
     * @param levelStr label of level to be found
     * @return Timesteplevel that matches levelStr, null otherwise
     */
    public Timesteplevel findLevel(String levelStr)
    {
	Timesteplevel tsl = c_topLevel;
	while(tsl != null) {
	    if (levelStr.equals(tsl.c_label))
		return tsl;
	    tsl = tsl.c_nextLevel;
	}
	return null;
    }

    /**
     * Gets all timestep levels below this level (includes this level).
     * @return A vector with all timestep levels.
     */
    public Vector getAllLevels()
    {
	Vector levels = new Vector(0);
	Timesteplevel nextLevel;

	levels.addElement(c_label);

	nextLevel = getNextLevel();
	while (nextLevel != null) {
	    levels.addElement(nextLevel.getLabel());
	    nextLevel = nextLevel.getNextLevel();
	}
	return levels;
    }

    /**
     * Gets the top timesteplevel
     * @return the top Timesteplevel
     */
    public Timesteplevel getTopLevel()
    {
	return c_topLevel;
    }

    /**
     * Gets the bottom Timesteplevel
     * @return the bottom Timesteplevel
     */
    public Timesteplevel getBottomLevel()
    {
      Timesteplevel tempLevel = this;

      while (tempLevel.getNextLevel() != null) {
        tempLevel = tempLevel.getNextLevel();
      }
      return tempLevel;
    }

    /**
     * Gets the timesteplevels label
     * @return The label
     */
    public String getLabel()
    {
	return c_label;
    }

    /**
     * Gets the level with the name 'label'
     * If the level is less detailed than this level, or if
     * the level is not found, null will be returned.
     * @param label a String with the label
     * @return the Timesteplevel (or null)
     */
    public Timesteplevel getLevel(String label)
    {
	if (label.equals(getLabel()))
	    return this;

	Timesteplevel nextLevel = getNextLevel();
	while (nextLevel != null) {
	    if (label.equals(nextLevel.getLabel()))
		return nextLevel;
	    nextLevel = nextLevel.getNextLevel();
	}

	return null;
    }

    /**
     * Gets number of timesteps allowed on this level
     * @return The number of timesteps allowed
     */
    public int getMaxTimesteps()
    {
	return c_numTimesteps;
    }

    /**
     * Gets number of timesteps allowed on this level
     * (same as getMaxTimesteps, but with a more logical name...)
     * @return The number of timesteps on this level
     */
    public int getTimesteps()
    {
	return c_numTimesteps;
    }

    /**
     * Gets the next level
     * @return next level
     */
    public Timesteplevel getNextLevel()
    {
	return c_nextLevel;
    }

    /**
     * Gets the previous level
     * @return previous level
     */
    public Timesteplevel getPrevLevel()
    {
	return c_prevLevel;
    }

    /**
     * Returns true if this level is more detailed than
     * (or equal to) level.
     * @param level the level to compare with
     * @return true/false
     */
    public boolean isMoreDetailed(Timesteplevel level)
    {
	while(level != null) {
	    if (level == this) {
		return true;
	    }
	    else
		level = level.getNextLevel();
	}
	return false;
    }

    /**
     * Sets this level's label
     * @param label The label to set
     */
    public void setLabel(String label)
    {
	c_label = label;
    }

    /**
     * Sets the number of timesteps on this level
     * @param maxTimesteps number of timesteps
     */
    public void setMaxTimesteps(int maxTimesteps)
    {
	c_numTimesteps = maxTimesteps;
    }

    /**
     * Sets the next level
     * @param nextLevel the next level
     */
    public void setNextLevel(Timesteplevel nextLevel)
    {
	c_nextLevel = nextLevel;
    }

    /**
     *  Sets the previous level
     * @param prevLevel the previous level
     */
    public void setPrevLevel(Timesteplevel prevLevel)
    {
	c_prevLevel = prevLevel;
    }

    /**
     * Return label and number of timesteps
     * @return A sting with the levels label and number of timesteps
     */
    public String toString()
    {
	return c_label + " (" + c_numTimesteps + ")";
    }

    /**
     * Returns the difference in LEVELS between this level and
     * the toplevel.
     * @return number of levels
     */
    public int toInt()
    {
	Timesteplevel level = c_topLevel;
	int i=0;
	while (level != this) {
	    i++;
	    level = level.getNextLevel();
	}
	return i;
    }

    /**
     * Returns the difference in timesteps (NOT levels) beteween
     * this level and 'level'.
     * The two levels MUST be connected to each other, if not this
     * function will never return.
     * @param level Timesteplevel
     * @return difference in timesteps
     */
    public int timestepDifference(Timesteplevel level)
    {

	int size=1;
	Timesteplevel tsl;

	//Call this function with null to calculate difference with topLevel
	if (level == null)
	    tsl = getTopLevel();
	else
	    tsl = level;

	//assume that 'this' is more detailed than 'level'
	while(tsl != this) {
	    size *= tsl.getMaxTimesteps();
	    tsl = tsl.getNextLevel();
	    if (tsl == null) {
		//assumption was wrong,
		//'level' is more detailed than 'this'
		//(but if the two levels not are connected at all, this will
		// get us stuck in a infinite loop...)
		return level.timestepDifference(this);
	    }
	}
	size *= tsl.getMaxTimesteps();
	return size;
    }

    public String toXML(int indent)
    {
        String xml   = "";

        xml += XML.indent(indent) + "<timesteplevel name=\"" + XML.toXML(getLabel()) + "\">" + XML.nl();
        xml += XML.indent(indent+1) + "<divide>" + getMaxTimesteps() + "</divide>" + XML.nl();

        if ((c_stepLengths != null) && (c_stepNames != null)) {
            String labelStr  = XML.toXML(c_stepNames.toString());
            String lengthStr = c_stepLengths.toString();

            xml += XML.indent(indent+1) + "<labelVector>"  + labelStr  + "</labelVector>"  + XML.nl();
            xml += XML.indent(indent+1) + "<lengthVector>" + lengthStr + "</lengthVector>" + XML.nl();
        }

        xml += XML.indent(indent) + "</timesteplevel>" + XML.nl();

        Timesteplevel level = getNextLevel();
        if (level != null) xml += level.toXML(indent);

        return xml;
    }
}
