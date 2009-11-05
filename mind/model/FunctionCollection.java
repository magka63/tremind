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
 * Keeps track of the existing function
 *
 * @author Peter Andersson
 * @version 2001-04-26
 */
package mind.model;

public class FunctionCollection
{

    private NodeInteraction c_database;

    /**
     * Creates a new collection that interfaces to the database
     * and gets functions with defaultvalues
     * @param database The database to interface with
     */
    public FunctionCollection(NodeInteraction database)
    {
	c_database = database;
    }

    /**
     * Gets a function with defaultvalues of a specified type
     * @param FunctionType The type of function to get
     * @return A NodeFunction with the defaultvalues set
     */
    public NodeFunction getFunction(String functionType)
    {
	NodeFunction nodefunction = null;
	try {
	    nodefunction = c_database.getFunction(functionType);
	}
	catch (Exception e) {
	    e.printStackTrace(System.out);
	    System.exit(1);
	}
	return nodefunction;
    }

    /**
     * Set defaultvalues for a function
     * @param function The function to set as default
     * FIXME remove this one?
    public void setFunctionDefaults(NodeFunction function)
    {
	c_database.setFunctionDefaultValues(function);
    }
    */
}
