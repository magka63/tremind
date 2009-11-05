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
 * FlowOptimization extends optimization to hold information relevant to flows.
 *
 * @author Peter Andersson
 * @version 2001-03-14.
 */
package mind.model;

public class FlowOptimization
    extends Optimization
{
    private float c_inputCost;
    private float c_reducedCost;

    /**
     * A nullconstructor
     */
    public FlowOptimization()
    {
	super();
    }

    /**
     * Full constructor for all information
     * @param AT An AT-enumeration, {"**", "BS", "EQ", "UL", "LL"}
     * @param activity The activity of the flow
     * @param lowerLimit Lower limit
     * @param upperLimit Upper limit
     * @param inputCost The inputcost
     * @param reducedCost Reduced cost for the flow
     * @throws IllegalArgumentException if the AT-enumeration is erronous
     */
    public FlowOptimization(String AT,
			    float activity,
			    float lowerLimit,
			    float upperLimit,
			    float inputCost,
			    float reducedCost)
	throws IllegalArgumentException
    {
	super(AT, activity, lowerLimit, upperLimit);
	c_inputCost = inputCost;
	c_reducedCost = reducedCost;
    }

    /**
     * Gets the inputcost
     * @return the inputcost
     */
    public float getInputCost()
    {
	return c_inputCost;
    }

    /**
     * Set the inputcost
     * @param inputCost the cost to be set
     */
    public void setInputCost(float inputCost)
    {
	c_inputCost = inputCost;
    }

    /**
     * Gets the reduced cost
     * @return the reduced cost
     */
    public float getReducedCost()
    {
	return c_reducedCost;
    }

    /**
     * Sets the reduced cost
     * @param reducedCost The reduced cost
     */
    public void setReducedCost(float reducedCost)
    {
	c_reducedCost = reducedCost;
    }
}
