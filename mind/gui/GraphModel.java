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
 * reMIND is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * reMIND is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * reMIND; if not, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA
 */
package mind.gui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D.Double;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

import mind.model.*;
import mind.io.*;

public class GraphModel {
	NodeFlowTable c_connections;

	private GraphFlowControl c_graphFlowControl;

	private GraphNodeControl c_graphNodeControl;

	/**
	 * Creates a new model for holding all objects in the GUI.
	 */
	public GraphModel() {
		c_graphFlowControl = new GraphFlowControl();
		c_graphNodeControl = new GraphNodeControl();
		c_connections = new NodeFlowTable();
	}

	/**
	 * Add a component to the workarea.
	 *
	 * @param componentID
	 *            he ID of the component to be added.
	 */
	public void add(ID componentID) {
		add(componentID, 0, 0);
	}

	/**
	 * Adds a component to the workarea and specifies its coordinates
	 *
	 * @param componentID
	 *            The ID of the component to be added
	 * @param x
	 *            The x-coordinate in the workspace
	 * @param y
	 *            The y-coordinate in the workspace
	 */
	public void add(ID componentID, int x, int y) {
		if (componentID.isNode())
			c_graphNodeControl.add(componentID, x, y);
	}

	/**
	 * Adds a flow to the graphmodel
	 *
	 * @param flowID
	 *            The ID of the new flow
	 * @param fromID
	 *            The ID of the node to flow from.
	 * @param toID
	 *            the ID of the node to flow to.
	 */
	public void add(ID flowID, ID fromID, ID toID) {
		Point p = getIntersection(c_graphNodeControl.getBorder(toID), c_graphNodeControl
				.getMiddlePoint(fromID), c_graphNodeControl.getMiddlePoint(toID));
		// if the intersections is null, that means the two nodes are
		// overlapping. In that case we put the flow topoint to middle
		// of the "to"-node.
		if (p == null)
			p = c_graphNodeControl.getMiddlePoint(toID);

		if (flowID.isFlow() && fromID.isNode() && toID.isNode()) {
			c_graphFlowControl.add(flowID, c_graphNodeControl.getMiddlePoint(fromID), p);
			c_connections.addFlow(flowID, fromID, toID);
		}
	}

	/**
	 * Gets the component at coordinates x,y NOT IMPLEMENTED YET!
	 *
	 * @param x
	 *            The x-coordinate
	 * @param y
	 *            The y-coordinate
	 * @return The ID of the object at point (x,y)
	 */
	public ID get(int x, int y) {
		return null;
	}

	/**
	 * Returns IDs on components that will be affected by this node's removal.
	 *
	 * @param componentIDs
	 *            The removed components.
	 * @return an array with the IDs of the affected components.
	 */
	public ID[] getAffectedByRemove(ID[] componentIDs) {
		if (componentIDs == null)
			return null;

		// If we use a set we don't need to worry about duplicates
		// they just get thrown away
		TreeSet set = new TreeSet();
		ID[] flows;
		ID[] affected;

		for (int i = 0; i < componentIDs.length; i++) {
			set.add(componentIDs[i]);
		}

		for (int i = 0; i < componentIDs.length; i++) {
			if (componentIDs[i].isNode()) {
				flows = c_connections.getFromFlows(componentIDs[i]);
				if (flows != null) {
					for (int j = 0; j < flows.length; j++) {
						set.add(flows[j]);
					}
				}
				flows = c_connections.getToFlows(componentIDs[i]);
				if (flows != null) {
					for (int j = 0; j < flows.length; j++) {
						set.add(flows[j]);
					}
				}
			}
		}

		affected = new ID[set.size()];
		set.toArray(affected);

		return affected;
	}

	/**
	 * Gets the area of the graphmodel.
	 *
	 * @return The dimensions of the area.
	 */
	public Dimension getAreaSize() {
		GraphIterator iterator = c_graphNodeControl.createGraphIterator();
		Rectangle rect;
		int right = 0, bottom = 0;

		iterator.first();
		if (!iterator.isDone()) {
			rect = iterator.getCurrent().getRectangle();
			right = rect.x + rect.width;
			bottom = rect.y + rect.height;

			for (iterator.next(); !iterator.isDone(); iterator.next()) {
				rect = iterator.getCurrent().getRectangle();
				if (rect.x + rect.width > right)
					right = rect.x + rect.width;
				if (rect.y + rect.height > bottom)
					bottom = rect.y + rect.height;
			}
		}
		return new Dimension(right, bottom);
	}

	/**
	 * Gets all the marked objects in the model
	 *
	 * @return An array with the IDs of the marked objects.
	 */
	public ID[] getMarked() {
		Vector marked;

		marked = c_graphFlowControl.getMarked();
		marked.addAll(c_graphNodeControl.getMarked());

		// makes a ID[] of the Vector
		ID[] componentIDs = new ID[marked.size()];
		marked.toArray(componentIDs);

		if (marked == null)
			return null;
		else
			return componentIDs;
	}

	/**
	 * Gets all the marked flows of the current graphmodel
	 *
	 * @return An array with all the marked flows
	 */
	public ID[] getMarkedFlows() {
		Vector marked = c_graphFlowControl.getMarked();
		ID[] nodes = new ID[marked.size()];

		marked.toArray(nodes);

		return nodes;
	}

	/**
	 * Gets all the marked nodes in the current graphmodel
	 *
	 * @return An array with all the marked nodes.
	 */
	public ID[] getMarkedNodes() {
		Vector marked = c_graphNodeControl.getMarked();
		ID[] nodes = new ID[marked.size()];

		marked.toArray(nodes);

		return nodes;
	}

	/**
	 * Gets the size of the nodes.
	 *
	 * @return The first nodesize encountered.
	 */
	public Dimension getNodeSize() {
		GraphIterator iterator = c_graphNodeControl.createGraphIterator();

		iterator.first();
		if (!iterator.isDone())
			return new Dimension(iterator.getCurrent().getRectangle().width,
					iterator.getCurrent().getRectangle().height);

		return null;
	}

	/**
	 * Gets the top node that is clickable at the coordinates x,y
	 *
	 * @param x
	 *            The x-coordinate to check.
	 * @param y
	 *            The y-coordinate to check.
	 * @return The ID of the node at the point, null if no node exists.
	 */
	public ID getTopNode(int x, int y) {
		GraphIterator iterator = c_graphNodeControl.createGraphIterator();

		for (iterator.last(); !iterator.isDone(); iterator.prev())
			if (iterator.getCurrent().isInsideClickableArea(x, y))
				return iterator.getCurrent().getID();

		return null;
	}

	/**
	 * Gets the top component that is clickable at the coordinates x,y
	 *
	 * @param x
	 *            The x-coordinate to check.
	 * @param y
	 *            The y-coordinate to check.
	 * @return The ID of the node at the point, null if no node exists.
	 */
	public ID getTopComponent(int x, int y) {
		ID node = getTopNode(x, y);
		if (node != null)
			return node;

		GraphIterator iterator = c_graphFlowControl.createGraphIterator();

		for (iterator.last(); !iterator.isDone(); iterator.prev()) {
			ExtendedGraphFlow egf = (ExtendedGraphFlow) iterator.getCurrent();
			if (egf.isInsideClickableArea(x, y)
					|| egf.isInsideHandleClickableArea(x, y)) {
				return egf.getID();
			}
		}

		return null;
	}

	/**
	 * Gets the top left corner coordinate of the component
	 *
	 * @param component
	 *            The component to get the coordinate for.
	 * @return -1 if no component found, the x-coordinate of the top left corner
	 *         elsewise.
	 */
	public int getX(ID component) {
		GraphIterator nodeIterator = c_graphNodeControl.createGraphIterator();
		GraphIterator flowIterator = c_graphFlowControl.createGraphIterator();

		if (component == null)
			return -1;

		if (component.isNode()) {
			for (nodeIterator.first(); !nodeIterator.isDone(); nodeIterator
					.next())
				if (nodeIterator.getCurrent().getID().equals(component))
					return nodeIterator.getCurrent().getX();
		} else if (component.isFlow()) {
			for (flowIterator.first(); !flowIterator.isDone(); flowIterator
					.next())
				if (flowIterator.getCurrent().getID().equals(component))
					return flowIterator.getCurrent().getX();
		}

		return -1;
	}

	/**
	 * Gets the top left corner coordinate of the component
	 *
	 * @param component
	 *            The component to get the coordinate for.
	 * @return -1 if no component found, the y-coordinate of the top left corner
	 *         elsewise.
	 */
	public int getY(ID component) {
		GraphIterator nodeIterator = c_graphNodeControl.createGraphIterator();
		GraphIterator flowIterator = c_graphFlowControl.createGraphIterator();

		if (component == null)
			return -1;

		if (component.isNode()) {
			for (nodeIterator.first(); !nodeIterator.isDone(); nodeIterator
					.next())
				if (nodeIterator.getCurrent().getID().equals(component))
					return nodeIterator.getCurrent().getY();
		} else if (component.isFlow()) {
			for (flowIterator.first(); !flowIterator.isDone(); flowIterator
					.next())
				if (flowIterator.getCurrent().getID().equals(component))
					return flowIterator.getCurrent().getY();
		}

		return -1;
	}

	/**
	 * Checks if a component is marked
	 *
	 * @param componentID
	 *            The component to check if marked
	 * @return True if marked.
	 */
	public boolean isMarked(ID componentID) {
		GraphIterator nodeIterator = c_graphNodeControl.createGraphIterator();
		GraphIterator flowIterator = c_graphFlowControl.createGraphIterator();

		if (componentID.isNode()) {
			for (nodeIterator.first(); !nodeIterator.isDone(); nodeIterator
					.next())
				if (nodeIterator.getCurrent().getID().equals(componentID))
					return nodeIterator.getCurrent().isMarked();
		} else if (componentID.isFlow()) {
			for (flowIterator.first(); !flowIterator.isDone(); flowIterator
					.next())
				if (flowIterator.getCurrent().getID().equals(componentID))
					return flowIterator.getCurrent().isMarked();
		}

		return false;
	}

	/**
	 * Moves one or more components by x,y
	 * @param components The components to move
	 * @param x The x-delta to move them with.
	 * @param y The y-delta to move them with.
	 */
	public void move(ID[] components, int x, int y)
	{
	    ID[] flows;
	    ID node;
	    Point intersection;

	    for (int i = 0; i < components.length; i++)
	        if (components[i].isNode()) {
	            c_graphNodeControl.move(components[i], x, y);

	            flows = c_connections.getFromFlows(components[i]);
	            if (flows != null)
	                for (int j = 0; j < flows.length; j++) {

	                    c_graphFlowControl.moveTo(flows[j],
	                    (int)c_graphNodeControl.getMiddlePoint(components[i]).getX(),
	                    (int)c_graphNodeControl.getMiddlePoint(components[i]).getY());
	                    node = c_connections.getToNode(flows[j]);
	                	int index = c_graphFlowControl.getIndexOf(flows[j]);
	                	ExtendedGraphFlow egf = (ExtendedGraphFlow) c_graphFlowControl.getComponent(index);
	                	// 	 If it is a bent flow set destination to the intersektion
	                	//   on a line from the last handle to the nodes midpoint
	                	if(egf.isBent()){
	                		FlowHandle h = egf.getLastHandle();
	                		Point p = new Point(h.getX(),h.getY());
	                		intersection = getIntersection(
	                				c_graphNodeControl.getBorder(node),
									p,
									c_graphNodeControl.getMiddlePoint(node));
	                	} else {

	                		intersection = getIntersection(
	                				c_graphNodeControl.getBorder(node),
									c_graphNodeControl.getMiddlePoint(components[i]),
									c_graphNodeControl.getMiddlePoint(node));
	                	}
	                    if (intersection != null)
	                        c_graphFlowControl.moveDestinationTo(flows[j],
	                        (int) intersection.getX(),
	                        (int) intersection.getY());
	                }
	        }

	    for (int i = 0; i < components.length; i++)
	        if (components[i].isNode()) {
	            flows = c_connections.getToFlows(components[i]);
	            if (flows != null)
	                for (int j = 0; j < flows.length; j++) {
	                	int index = c_graphFlowControl.getIndexOf(flows[j]);
	                	ExtendedGraphFlow egf = (ExtendedGraphFlow) c_graphFlowControl.getComponent(index);
	                	// 	 If it is a bent flow set destination to the intersektion
	                	//   on a line from the last handle to the nodes midpoint
	                	if(egf.isBent()){
	                		FlowHandle h = egf.getLastHandle();
	                		Point p = new Point(h.getX(),h.getY());
	                		intersection = getIntersection(
	                				c_graphNodeControl.getBorder(components[i]),
									p,
									c_graphNodeControl.getMiddlePoint(components[i]));
	                	}else{
	                		intersection = getIntersection(
	                				c_graphNodeControl.getBorder(components[i]),
									c_graphFlowControl.getFrom(flows[j]),
									c_graphNodeControl.getMiddlePoint(components[i]));
	                	}
	                		if (intersection != null)
	                			c_graphFlowControl.moveDestinationTo(flows[j],
	                					(int) intersection.getX(),
										(int) intersection.getY());
	                }
	        }
	}

	/**
	 * Moves one or more components to a specified point.
	 * @param components The components to move.
	 * @param x The x-coordinate to move them to.
	 * @param y The y-coordinate to move them to.
	 */
	public void moveTo(ID[] components, int x, int y)
	{
	    ID[] flows;
	    ID node;
	    Point intersection;

	    for (int i = 0; i < components.length; i++)
	        if (components[i].isNode()) {
	            c_graphNodeControl.moveTo(components[i], x, y);

	            flows = c_connections.getFromFlows(components[i]);
	            if (flows != null)
	                for (int j = 0; j < flows.length; j++) {
	                    c_graphFlowControl.moveTo(flows[j],
	                    (int)c_graphNodeControl.getMiddlePoint(components[i]).getX(),
	                    (int)c_graphNodeControl.getMiddlePoint(components[i]).getY());

	                    node = c_connections.getToNode(flows[j]);
	                    int index = c_graphFlowControl.getIndexOf(flows[j]);
	                	ExtendedGraphFlow egf = (ExtendedGraphFlow) c_graphFlowControl.getComponent(index);
	                	// 	 If it is a bent flow set destination to the intersektion
	                	//   on a line from the last handle to the nodes midpoint
	                	if(egf.isBent()){
	                		FlowHandle h = egf.getLastHandle();
	                		Point p = new Point(h.getX(),h.getY());
	                		intersection = getIntersection(
	                				c_graphNodeControl.getBorder(node),
									p,
									c_graphNodeControl.getMiddlePoint(node));
	                	}else{
	                		intersection = getIntersection(
	                				c_graphNodeControl.getBorder(node),
									c_graphNodeControl.getMiddlePoint(components[i]),
									c_graphNodeControl.getMiddlePoint(node));
	                	}

	                    if (intersection != null)
	                        c_graphFlowControl.moveDestinationTo(flows[j],
	                        (int) intersection.getX(),
	                        (int) intersection.getY());
	                }
	        }

	    for (int i = 0; i < components.length; i++)
	        if (components[i].isNode()) {
	            flows = c_connections.getToFlows(components[i]);
	            if (flows != null)
	                for (int j = 0; j < flows.length; j++) {
	                	int index = c_graphFlowControl.getIndexOf(flows[j]);
	                	ExtendedGraphFlow egf = (ExtendedGraphFlow) c_graphFlowControl.getComponent(index);
	                	// 	 If it is a bent flow set destination to the intersektion
	                	//   on a line from the last handle to the nodes midpoint
	                	if(egf.isBent()){
	                		FlowHandle h = egf.getLastHandle();
	                		Point p = new Point(h.getX(),h.getY());
	                		intersection = getIntersection(
	                				c_graphNodeControl.getBorder(components[i]),
									p,
									c_graphNodeControl.getMiddlePoint(components[i]));
	                	}else{
	                		intersection = getIntersection(
	                				c_graphNodeControl.getBorder(components[i]),
									c_graphFlowControl.getFrom(flows[j]),
									c_graphNodeControl.getMiddlePoint(components[i]));
	                	}
	                	if (intersection != null)
	                		c_graphFlowControl.moveDestinationTo(flows[j],
	                				(int) intersection.getX(),
									(int) intersection.getY());
	                }
	        }
	}

	/**
	 * Paints the gui.
	 *
	 * @param gui
	 *            The GUI to be painted.
	 * @param graphics
	 *            The graphicsmodel to be used to paint.
	 */
	public void paint(GUI gui, Graphics2D graphics) {
		// gui has to be passed to the paint methods
		// is used to get the label of a node for instance
		c_graphFlowControl.paint(gui, graphics);
		c_graphNodeControl.paint(gui, graphics);
	}

	/**
	 * Removes one or more components from the model
	 *
	 * @param components
	 *            The components to remove.
	 */
	public void remove(ID[] components) {
		GraphIterator nodeIterator = c_graphNodeControl.createGraphIterator();
		GraphIterator flowIterator = c_graphFlowControl.createGraphIterator();

		for (int i = 0; i < components.length; i++) {
			if (components[i].isNode()) {
				for (nodeIterator.first(); !nodeIterator.isDone(); nodeIterator
						.next())
					if (nodeIterator.getCurrent().getID().equals(components[i])) {
						c_graphNodeControl.remove(nodeIterator);
					}
			} else if (components[i].isFlow()) {
				for (flowIterator.first(); !flowIterator.isDone(); flowIterator
						.next())
					if (flowIterator.getCurrent().getID().equals(components[i])) {
						c_graphFlowControl.remove(flowIterator);
						c_connections.remove(components[i]);
					}
			}
		}
	}

	/**
	 * Scales the model by a percentage.
	 *
	 * @param procent
	 *            The percentage to scale with.
	 */
	public void scaleByProcent(int procent) {
		GraphComponent.setScaleByProcent(procent);

		// for instance the width of the nodes are static and
		// only need to be changed once
		GraphNode.scaleCommonByProcent(procent);
		c_graphNodeControl.scaleByProcent(procent);

		GraphFlow.scaleCommonByProcent(procent);
		c_graphFlowControl.scaleByProcent(procent);
	}

	/**
	 * Sets a component to be dragged over or not.
	 *
	 * @param componentID
	 *            The component to set dragged over for.
	 * @param isDraggedOver
	 *            If the component is dragged over or not.
	 */
	public void setDraggedOver(ID componentID, boolean isDraggedOver) {
		// only nodes wants do know if they are dragged over
		GraphIterator nodeIterator = c_graphNodeControl.createGraphIterator();

		for (nodeIterator.first(); !nodeIterator.isDone(); nodeIterator.next())
			if (nodeIterator.getCurrent().getID().equals(componentID)) {
				nodeIterator.getCurrent().setDraggedOver(isDraggedOver);
				return;
			}
	}

	/**
	 * Unmarks all components
	 */
	public void unmarkAll() {
		GraphIterator nodeIterator = c_graphNodeControl.createGraphIterator();
		GraphIterator flowIterator = c_graphFlowControl.createGraphIterator();

		for (nodeIterator.first(); !nodeIterator.isDone(); nodeIterator.next())
			nodeIterator.getCurrent().setMarked(false);

		for (flowIterator.first(); !flowIterator.isDone(); flowIterator.next()) {
			flowIterator.getCurrent().setMarked(false);
			if (flowIterator.getCurrent() instanceof ExtendedGraphFlow) {
				ExtendedGraphFlow exg = (ExtendedGraphFlow) flowIterator
						.getCurrent();
				exg.unmarkAllHandles();
			}
		}
	}

	/**
	 * Sets the marked atribute of a component
	 *
	 * @param componentID
	 *            The component to set the attribute for.
	 * @param marked
	 *            Is the component marked or not?
	 */
	public void setMarked(ID componentID, boolean marked) {
		GraphIterator nodeIterator = c_graphNodeControl.createGraphIterator();
		GraphIterator flowIterator = c_graphFlowControl.createGraphIterator();

		if (componentID.isNode()) {
			for (nodeIterator.first(); !nodeIterator.isDone(); nodeIterator
					.next())
				if (nodeIterator.getCurrent().getID().equals(componentID)) {
					nodeIterator.getCurrent().setMarked(marked);
					return;
				}
		} else if (componentID.isFlow()) {
			for (flowIterator.first(); !flowIterator.isDone(); flowIterator
					.next())
				if (flowIterator.getCurrent().getID().equals(componentID)) {
					flowIterator.getCurrent().setMarked(marked);
					return;
				}
		}
	}

	/**
	 * Sets if the IDs of the flows should be set or not.
	 *
	 * @param show
	 *            true if they should be shown.
	 */
	public void setShowFlowIDs(boolean show) {
		GraphIterator flowIterator = c_graphFlowControl.createGraphIterator();

		for (flowIterator.first(); !flowIterator.isDone(); flowIterator.next())
			flowIterator.getCurrent().setShowID(show);
	}

	/**
	 * Sets if the Labels of the flows should be shown
	 *
	 * @param show
	 *            true if they should.
	 */
	public void setShowFlowLabels(boolean show) {
		GraphIterator flowIterator = c_graphFlowControl.createGraphIterator();

		for (flowIterator.first(); !flowIterator.isDone(); flowIterator.next())
			flowIterator.getCurrent().setShowLabel(show);
	}

	/**
	 * Sets if the nodes ID should be shown or not.
	 *
	 * @param show
	 *            true if they should.
	 */
	public void setShowNodeIDs(boolean show) {
		GraphIterator nodeIterator = c_graphNodeControl.createGraphIterator();

		for (nodeIterator.first(); !nodeIterator.isDone(); nodeIterator.next())
			nodeIterator.getCurrent().setShowID(show);
	}

	/**
	 * Sets if the nodes labels should be shown or not.
	 *
	 * @param show
	 *            true if they should be shown.
	 */
	public void setShowNodeLabels(boolean show) {
		GraphIterator nodes = c_graphNodeControl.createGraphIterator();

		for (nodes.first(); !nodes.isDone(); nodes.next())
			nodes.getCurrent().setShowLabel(show);
	}

	/**
	 * Touches a component
	 *
	 * @param componentID
	 *            The component to be touched.
	 */
	public void touch(ID componentID) {
		if (componentID.isNode())
			c_graphNodeControl.touch(componentID);
		else if (componentID.isFlow())
			c_graphFlowControl.touch(componentID);
	}

	/**
	 * Transfers the model to XML format.
	 *
	 * @param indent
	 *            The number of whitespaces to use for intendentation.
	 * @return The model in XML format.
	 */
	public String toXML(int indent) {
		float zoom = (GUI.getStaticToolbar().c_combo.getSelectedIndex() + 1) * 10.f / 100;
		/*
		 * The zoomlevel is embedded in the coordinates, this must be removed
		 * when The model is saved by multiplying with the zoomlevel
		 */

		GraphNode node = null;
		String xml = XML.indent(indent) + "<gui>" + XML.nl();

		ExtendedGraphFlow flow = null;
		GraphIterator flows = c_graphFlowControl.createGraphIterator();
		for (flows.first(); !flows.isDone(); flows.next()) {
			flow = (ExtendedGraphFlow) flows.getCurrent();
			if(flow.isBent()){
				xml = xml + XML.indent(indent + 1) + "<guiFlow id=\""+
				flow.getID() + "\">" + XML.nl() +
				flow.getHandlesXML(indent + 2, zoom) +
				XML.indent(indent + 1) + "</guiFlow>" + XML.nl();
			}
		}

		GraphIterator nodes = c_graphNodeControl.createGraphIterator();
		for (nodes.first(); !nodes.isDone(); nodes.next()) {
			node = (GraphNode) nodes.getCurrent();
			xml = xml + XML.indent(indent + 1) + "<guiNode id=\""
					+ node.getID() + "\">" + XML.nl() + XML.indent(indent + 2)
					+ "<x>" + (int) (node.getX() / zoom) + "</x>" + XML.nl()
					+ XML.indent(indent + 2) + "<y>"
					+ (int) (node.getY() / zoom) + "</y>" + XML.nl()
					+ XML.indent(indent + 1) + "</guiNode>" + XML.nl();
		}

		xml = xml + XML.indent(indent) + "</gui>" + XML.nl();

		return xml;
	}

	/**
	 * Gets the intersectionpoint between a rectangle and a line.
	 *
	 * @param node
	 *            A rectangle to get the intersection for.
	 * @param from
	 *            the line's starting point.
	 * @param to
	 *            The line's ending point.
	 * @return The point where the line and the rectangle intersects. Null if no
	 *         intersection.
	 */
	public Point getIntersection(Rectangle node, Point from, Point to) {
		Point p = null;

		// Get coordinates of the line (flow)
		int x0 = (int) from.getX();
		int y0 = (int) from.getY();
		int x1 = (int) to.getX();
		int y1 = (int) to.getY();

		// Get coordinates of the rectangle (node)
		int x2 = (int) node.getX();
		int y2 = (int) node.getY();
		int width = (int) node.getWidth();
		int height = (int) node.getHeight();

		p = lineIntersect(x0, y0, x1, y1, x2, y2, x2, y2 + height);

		if (p == null) {
			p = lineIntersect(x0, y0, x1, y1, x2, y2, x2 + width, y2);

			if (p == null) {
				p = lineIntersect(x0, y0, x1, y1, x2, y2 + height, x2 + width,
						y2 + height);
				if (p == null)
					p = lineIntersect(x0, y0, x1, y1, x2 + width, y2, x2
							+ width, y2 + height);
			}
		}
		return p;
	}

	/**
	 * Computes an intersectionpoint between two lines.
	 *
	 * @param x0
	 *            The first line's x startpoint
	 * @param y0
	 *            The first line's y startpoint
	 * @param x1
	 *            The first line's x endpoint
	 * @param y1
	 *            The first line's y endpoint
	 * @param x2
	 *            The second line's x startpoint
	 * @param y2
	 *            The second line's y startpoint
	 * @param x3
	 *            The second line's x endpoint
	 * @param y3
	 *            The second line's y endpoint
	 * @return The point of intersection, null if no intersection.
	 */
	public Point lineIntersect(float x0, float y0, float x1, float y1,
			float x2, float y2, float x3, float y3) {
		float num1, num2, denom, x, y;
		float ua, ub;

		num1 = (x3 - x2) * (y0 - y2) - (y3 - y2) * (x0 - x2);
		num2 = (x1 - x0) * (y0 - y2) - (y1 - y0) * (x0 - x2);
		denom = (y3 - y2) * (x1 - x0) - (x3 - x2) * (y1 - y0);

		// if lines are parallel, return null
		if (denom == 0)
			return null;

		ua = num1 / denom;
		ub = num2 / denom;

		x = x0 + ua * (x1 - x0);
		y = y0 + ua * (y1 - y0);

		if ((ua >= 0) && (ua <= 1)) { // intersect line 1
			if ((ub >= 0) && (ub <= 1)) { // intersect line 2
				return new Point(Math.round(x), Math.round(y));
			}
		}

		return null;
	}

	/**
	 * @return Returns the c_graphFlowControl.
	 */
	public GraphFlowControl getGraphFlowControl() {
		return c_graphFlowControl;
	}

	/**
	 * @param graphComponent
	 * @return
	 */
	public ExtendedGraphFlow getGraphFlow(ID graphComponent) {
		GraphIterator gi = c_graphFlowControl.createGraphIterator();
		for (; !gi.isDone(); gi.next()) {
			ExtendedGraphFlow gf = (ExtendedGraphFlow) gi.getCurrent();
			if (gf.getID().equals(graphComponent))
				return gf;
		}
		return null;
	}

	/**
	 * @param handle
	 * @param flow
	 * @param x
	 * @param y
	 */
	public void moveHandle(FlowHandle handle, ExtendedGraphFlow flow, int x, int y) {
		//update to-point
		ID flowID = flow.getID();
    	ID node = c_connections.getToNode(flowID);
    	FlowHandle h = flow.getLastHandle();
    	Point p = new Point(h.getX(),h.getY());
    	Point intersection = getIntersection(
    				c_graphNodeControl.getBorder(node),
					p,
					c_graphNodeControl.getMiddlePoint(node));
    	if (intersection != null)
            c_graphFlowControl.moveDestinationTo(flowID,
            (int) intersection.getX(),
            (int) intersection.getY());
		//update handle position
		handle.setX(x);
		handle.setY(y);
	}

	/**
	 * @param strID
	 * @return
	 */
	public ID getFlowWithIdString(String strID) {
		GraphIterator gi = c_graphFlowControl.createGraphIterator();
		for (; !gi.isDone(); gi.next()) {
			ExtendedGraphFlow gf = (ExtendedGraphFlow) gi.getCurrent();
			if (gf.getID().toString().equals(strID))
				return gf.getID();
		}
		return null;
	}
}