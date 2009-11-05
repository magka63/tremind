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

/*
 * The nodes, flows, string and all other possible graph components
 * are in a list. The newest added node is at the back of the list.
 * When we mark a node, we put it at the back of the list. This way we can
 * handle overlapping of nodes. A node that is on top of another in the graph
 * is further back in the list than the other one. If a component is on top
 * of another it should be drawn after the other one.
 */
package mind.gui;

import java.util.Vector;
import java.awt.Graphics2D;

import mind.model.*;

public abstract class GraphControl
    extends Vector
{
    public GraphControl()
    {
	super(0);
    }

    public GraphIterator createGraphIterator()
    {
	return new GraphIterator(this);
    }

    public GraphMarkedIterator createGraphMarkedIterator()
    {
	return new GraphMarkedIterator(this);
    }

    public GraphComponent getComponent(int i)
    {
	return (GraphComponent) super.get(i);
    }

    public GraphComponent getComponent(GraphIterator iterator)
    {
	return (GraphComponent) super.get(iterator.getIndex());
    }

    public int getIndexOf(ID component)
    {
	GraphIterator iterator = createGraphIterator();

	for (iterator.first(); !iterator.isDone(); iterator.next())
	    if (iterator.getCurrent().getID().equals(component))
		return iterator.getIndex();

	return -1;
    }

    public int getLast()
    {
	return getSize()-1;
    }

    public Vector getMarked()
    {
 	GraphIterator iterator = createGraphIterator();
	Vector marked = new Vector(0);

	for (iterator.first(); !iterator.isDone(); iterator.next()) {
	    if (iterator.getCurrent().isMarked())
		marked.addElement(iterator.getCurrent().getID());
	}

	return marked;
    }

    public int getSize()
    {
	return size();
    }

    void move(ID componentID, int x, int y)
    {
 	GraphIterator iterator = createGraphIterator();

	for (iterator.first(); !iterator.isDone(); iterator.next()) {
	    if (iterator.getCurrent().getID().equals(componentID))
		iterator.getCurrent().move(x, y);
	}
    }

    void moveTo(ID componentID, int x, int y)
    {
 	GraphIterator iterator = createGraphIterator();

	for (iterator.first(); !iterator.isDone(); iterator.next()) {
	    if (iterator.getCurrent().getID().equals(componentID))
		iterator.getCurrent().moveTo(x, y);
	}
    }

    void remove(GraphIterator iterator) {
	removeElementAt(iterator.getIndex());
    }

    public void paint(GUI gui, Graphics2D graphics)
    {
	GraphIterator iterator = createGraphIterator();

	for (iterator.first(); !iterator.isDone(); iterator.next()) {
	    iterator.getCurrent().paint(gui, graphics);
	}
    }

    public void scaleByProcent(int procent)
    {
	GraphIterator iterator = createGraphIterator();

	for (iterator.first(); !iterator.isDone(); iterator.next())
	    iterator.getCurrent().scaleByProcent(procent);
    }

    public void touch(ID component)
    {
	if (component == null)
	    return;

	int index = getIndexOf(component);
	Object comp = getComponent(index);

	if (index >= 0) {
	    remove(index);
	    addElement(comp);
	}
    }
}
