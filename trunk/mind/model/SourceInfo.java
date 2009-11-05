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

// THIS CLASS IS NO LONGER USED!

/**
 * Instances of this object is used to send information from GUI to
 * the functions. Is used to set properties in a function.
 *
 * @author Tim Terlegård
 * 2001-05-11
 */

public class SourceInfo
    extends FunctionInfo
{
    private ID c_resource = null;
    private float c_cost = -1;

    /**
     * Gets the cost for each timestep.
     * @return A vector with cost for each timestep.
     */
    public float getCost()
    {
	return c_cost;
    }

    /**
     * Gets the ID of the resource.
     * @return The ID of the resource.
     */
    public ID getResource()
    {
	return c_resource;
    }

    /**
     * Sets the cost for each timestep.
     * @param cost A vector with cost for each timestep.
     */
    public void setCost(float cost)
    {
	c_cost = cost;
    }

    /**
     * Sets the ID of the resource.
     * @param resourceID The ID of the resource.
     */
    public void setResource(ID resourceID)
    {
	c_resource = resourceID;
    }
}
