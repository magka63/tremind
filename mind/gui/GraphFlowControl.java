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
 * This class keeps together all GraphFlows in a list specified by GraphControl.
 * 
 * @version 0.0 2001-03-26
 * @author Tim Terlegård
 */
package mind.gui;

import java.awt.Point;

import mind.model.*;


public class GraphFlowControl extends GraphControl {
	public void add(ID flowID, Point from, Point to) {
		// Changed to support more advanced graphical repressentation of flows
		//GraphComponent g = new GraphFlow(flowID, from, to);
		GraphComponent g = new ExtendedGraphFlow(flowID, from, to);
		addElement(g);
	}

	public Point getFrom(ID flowID) {
		GraphIterator iterator = createGraphIterator();
		GraphFlow flow;

		for (iterator.first(); !iterator.isDone(); iterator.next())
			if (iterator.getCurrent().getID().equals(flowID)) {
				flow = (GraphFlow) iterator.getCurrent();
				return new Point(flow.getX(), flow.getY());
			}

		return null;
	}

	public void moveDestinationTo(ID flowID, int x, int y) {
		GraphIterator iterator = createGraphIterator();

		for (iterator.first(); !iterator.isDone(); iterator.next()) {
			if (iterator.getCurrent().getID().equals(flowID))
				((GraphFlow) iterator.getCurrent()).moveDestinationTo(x, y);
		}
	}
	
	
	/**
	 * Adds a handle to the graphical repressentation of the flow
	 * @param flowID The flows ID
	 * @param b If we should check where to put the new handle or not
	 * @param p Where in the grapharea the handle should be
	 */
	public void addGraphFlowHandle(ID flowID, Point p, boolean b){
		GraphIterator iterator = createGraphIterator();

		for (iterator.first(); !iterator.isDone(); iterator.next()) {
			if (iterator.getCurrent().getID().equals(flowID))
				((ExtendedGraphFlow) iterator.getCurrent()).addHandle(p, b);
		}

		
	}
	/**
	 * Removes a handle from the flow
	 * @param flowID The flows ID
	 * @param point The point
	 */
	public void removeGraphFlowHandle(ID flowID, Point point){
		GraphIterator iterator = createGraphIterator();

		for (iterator.first(); !iterator.isDone(); iterator.next()) {
			if (iterator.getCurrent().getID().equals(flowID))
				((ExtendedGraphFlow) iterator.getCurrent()).removeHandle(point);
		}

		
	}
}