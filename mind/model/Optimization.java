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
   * Optimization sums up all information got through optimization into one object.
   * <br>
   * Optimization in itself is an abstract class, tho FlowOpt and EquationOpt
   * inherits it's functionality
   * @author Peter Andersson
   * @version 2001-03-24.
   */
package mind.model;

public abstract class Optimization
{
    //Finals for the different values of AT
    public static final String NOSOLUTION = "**";
    public static final String BASESOLUTION = "BS";
    public static final String NONBASESOLUTION = "EQ";
    public static final String LOWERLIMITNONBASESOLUTION = "LL";
    public static final String UPPERLIMITNONBASESOLUTION = "UL";

    // AT is an enumeration of { "**", "BS", "EQ", "LL", "UL" }
    private String c_AT;
    private float c_activity;
    private float c_lowerLimit;
    private float c_upperLimit;

    /**
     * A null-constructor
     */
    public Optimization()
    {
    }

    /**
     * The constructor sets all available information
     * @param AT A string denoting the optimizations status
     * @param activity The activity of the variable
     * @param lowerLimit The variables lower limit
     * @param upperLimit The variables upper limit
     * @throws IllegalArgumentException If AT-enumeration is erronous.
     */
    public Optimization(String AT, float activity, float lowerLimit, float upperLimit)
	throws IllegalArgumentException
    {
	if (!isATEnumeration(AT))
	    throw new IllegalArgumentException();
	c_AT = AT;
	c_activity = activity;
	c_lowerLimit = lowerLimit;
	c_upperLimit = upperLimit;
    }

    /**
     * Gets the AT-value
     * @return An AT-enumeration
     */
    public String getAT()
    {
	return c_AT;
    }

    /**
     * Sets the AT-value
     * @param AT The AT-value to be set
     * @throws IllegalArgumentException if AT-value is erronous.
     */
    public void setAT(String AT)
	throws IllegalArgumentException
    {
	if (!isATEnumeration(AT))
	    throw new IllegalArgumentException();
	c_AT = AT;
    }

    /**
	 * Gets the activity of the optimization
	 * @return the activity value
	 */
    public float getActivity()
    {
	return c_activity;
    }

    /**
	 * Sets the activity value
	 * @param activity The activity to be set.
	 */
    public void setActivity(float activity)
    {
	c_activity = activity;
    }

    /**
	 * Gets the lower limit
	 * @return The lower Limit of the optimization.
	 */
    public float getLowerLimit()
    {
	return c_lowerLimit;
    }

    /**
	 * Sets the lower limit
	 * @param lowerLimit The lower limit of the optimization
	 */
    public void setLowerLimit(float lowerLimit)
    {
	c_lowerLimit = lowerLimit;
    }

    /**
	 * Gets the upper limit
	 * @return The upper limit.
	 */
    public float getUpperLimit()
    {
	return c_upperLimit;
    }

    /**
	 * Sets the upper limit
	 * @param upperLimit The upper limit to be set.
	 */
    public void setUpperLimit(float upperLimit)
    {
	c_upperLimit = upperLimit;
    }

    /**
	 * AT can only contain 5 different types, this checks if it is ok.
	 * @return If AT:s value is in the enumeration or not.
	 */
    private boolean isATEnumeration(String AT)
    {
	return (AT.equals(NOSOLUTION) ||
		AT.equals(BASESOLUTION) ||
		AT.equals(LOWERLIMITNONBASESOLUTION) ||
		AT.equals(UPPERLIMITNONBASESOLUTION) ||
		AT.equals(NONBASESOLUTION));
    }
}

