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
 * EquationOptimization extends Optimization to bear information about Equations
 * @author Peter Andersson
 * @version 2001-03-23.
 */
package mind.model;

public class EquationOptimization
    extends Optimization
{
    private float c_dualActivity;
    private float c_slackActivity;

    /**
     * A Nullconstructor
     */
    public EquationOptimization()
    {
	super();
    }

    /**
     * Constructor with a full set of arguments
     * @param AT Enumeration {"**", "BS", "EQ", "LL", "UL"}
     * @param activity Activity of Equation
     * @param lowerLimit Lower limit
     * @param upperLimit Upper limit
     * @param dualActivity Dualactivity
     * @param slackActivity slackActivity
     * @throws IllegalArgumentException if the AT-enumeration is erronous
     */
    public EquationOptimization(String AT,
				float activity,
				float lowerLimit,
				float upperLimit,
				float dualActivity,
				float slackActivity)
	throws IllegalArgumentException
    {
	super(AT, activity, lowerLimit, upperLimit);
	c_dualActivity = dualActivity;
	c_slackActivity = slackActivity;
    }

    /**
     * Gets the dual activity
     * @return The dualactivity
     */
    public float getDualActivity()
    {
	return c_dualActivity;
    }

    /**
     * Sets the dualactivity
     * @param dualActivity The dual activity
     */
    public void setDualActivity(float dualActivity)
    {
	c_dualActivity = dualActivity;
    }

    /**
     * Gets the slackactivity
     * @return The slack activity
     */
    public float getSlackActivity()
    {
	return c_slackActivity;
    }

    /**
     * Sets the slack activity
     * @param slackActivity The slack activity
     */
    public void setSlackActivity(float slackActivity)
    {
	c_slackActivity = slackActivity;
    }
}
