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

/**
 * This class keeps together all GraphNodes in a list specified by
 * GraphControl.
 *
 * @version 0.0 2001-03-26
 * @author Tim Terleg�rd
 */
package mind.gui;

import java.awt.Point;
import java.awt.Rectangle;

import mind.model.*;

public class GraphNodeControl
    extends GraphControl
{
    public void add(ID nodeID)
    {
	addElement(new GraphNode(nodeID));
    }

    public void add(ID nodeID, int x, int y)
    {
	addElement(new GraphNode(nodeID, x, y));
    }

    public Rectangle getBorder(ID nodeID)
    {
	GraphIterator iterator = createGraphIterator();
	GraphNode node;

	for (iterator.first(); !iterator.isDone(); iterator.next())
	    if (iterator.getCurrent().getID().equals(nodeID)) {
		node = (GraphNode) iterator.getCurrent();
		return new Rectangle(node.getX(), node.getY(),
				     node.getWidth(), node.getHeight());
	    }

	return null;
    }

    public Point getMiddlePoint(ID nodeID)
    {
	GraphIterator iterator = createGraphIterator();

	for (iterator.first(); !iterator.isDone(); iterator.next())
	    if (iterator.getCurrent().getID().equals(nodeID))
		return ((GraphNode)iterator.getCurrent()).getMiddlePoint();

	return null;
    }
}
