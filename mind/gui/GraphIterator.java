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
 * This class is an iterator. It iterates over the graph components of
 * a certain kind (flow, node etc).
 * It iterates from index 0 and forward
 *
 * @author Tim Terlegård
 * @version 2001-04-08
 */
package mind.gui;

public class GraphIterator
{
    protected GraphControl c_control;
    protected int c_current;

    public GraphIterator(GraphControl control)
    {
	c_control = control;
	c_current = 0;
    }

    public void first()
    {
	c_current = 0;
    }

    public GraphComponent getCurrent()
	throws IndexOutOfBoundsException
    {
	if (c_current >= c_control.getSize())
	    throw new IndexOutOfBoundsException("Outside GraphControl bounds");

	return c_control.getComponent(c_current);
    }

    public int getIndex()
    {
	return c_current;
    }

    public boolean isDone()
    {
	return (c_current >= c_control.getSize() || c_current < 0);
    }

    public void last()
    {
	c_current = c_control.getSize() - 1;
    }

    public void next()
    {
	c_current++;
    }

    public void prev()
    {
	c_current--;
    }

    public void remove()
    {
    }
}
