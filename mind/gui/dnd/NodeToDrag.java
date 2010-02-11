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
package mind.gui.dnd;

public class NodeToDrag
{
    private String c_name;
    private String c_file;
    //    private int c_ID;

    // identifier is used to know where in the tree
    // this node belongs
    public NodeToDrag(String name, String identifier)
    {
	c_name = name;
	c_file = identifier;
    }

    public String getFile()
    {
	return c_file;
    }

    public String getName()
    {
	return c_name;
    }

    public void setName(String name)
    {
	c_name = name;
    }

    public String toString()
    {
	return c_name;
    }
}