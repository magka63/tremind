/*
 * Copyright 2005
 * Marcus Bergendorff <bermar@users.sourceforge.net>
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
package mind.gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D.Double;

import java.util.Iterator;
import java.util.Vector;

import mind.model.ID;
import mind.model.Resource;
import mind.io.XML;

/**
 * @author bermar 
 */
public class ExtendedGraphFlow extends GraphFlow {

	public static final int BENT = 1;

	public static final int STRAIGHT = 2;


	//Handles
	private Vector handles;
	private int mode = STRAIGHT;

	/**
	 * @param flowID
	 *            the flow
	 * @param from
	 * @param to
	 */
	public ExtendedGraphFlow(ID flowID, Point from, Point to) {
		super(flowID, from, to);
		handles = new Vector();
	}
	
	/**
	 * Adds a handle at specified point at the last line segment
	 * @param p where to add the handle
	 */
	public void addHandle(Point p){
		addHandle(p,false);
	}

	/**
	 * Adds a handle at specified point at the last line segment. If
	 * checkindex is true checks on which linesegment to add the handle.
	 * @param p where to add the handle
	 * @param checkIndex if we want to do the check or just add it 
	 * at the last linesegment
	 */
	public void addHandle(Point p, boolean checkIndex){
		mode = BENT;
		if(checkIndex && (handles.size() > 0)){
			// check where in the vector to add the handle...
			//check first segment
			FlowHandle hndl = (FlowHandle)handles.get(0);
			FlowHandle hndl2 = null;
			if(isCloseToLine(p, new Point(getX(),getY()), new Point(hndl.getX(),hndl.getY()))){
				handles.insertElementAt(new FlowHandle(getID(), p.x, p.y), 0);
				return;
			}
			int size = handles.size();
			// check all other segments..,
			for(int i = 1; i < size; i++){
				hndl = (FlowHandle) handles.get(i - 1);
				hndl2 = (FlowHandle) handles.get(i);
				Point p1 = new Point(hndl.getX(),hndl.getY());
				Point p2 = new Point(hndl2.getX(),hndl2.getY());
				if(isCloseToLine(p, p1, p2)){
					handles.insertElementAt(new FlowHandle(getID(), p.x, p.y), i);
					return;
				}
			}
		} 
		// add on last segment
		handles.add(new FlowHandle(getID(), p.x, p.y));
	}
	
	/**
	 * @param point the first point on the line
	 * @param point2 the second point on the line
	 * @param point2check the point that is checked if it is close to the line
	 * @return if it is close to the line
	 */
	private boolean isCloseToLine(Point point2check, Point point, Point point2) {
		Line2D.Float line = new Line2D.Float(point, point2);
		Polygon poly = new Polygon();
		Point p = getPerpendicularPoint(line, true, 3);
		poly.addPoint(p.x,p.y);
		p = getPerpendicularPoint(line, false, 3);
		poly.addPoint(p.x,p.y);
		p = getPerpendicularPoint(line, false, -3);
		poly.addPoint(p.x,p.y);
		p = getPerpendicularPoint(line, true, -3);
		poly.addPoint(p.x,p.y);
		//System.out.println(poly.contains(point2check));
		return poly.contains(point2check);
	}

	public void drawBentFlow(GUI gui, Graphics2D g) {
		Shape arrow;
		//int arrowDir = getArrowDirection();

		Resource resource = gui.getResource(getID());
		if (resource != null)
			// set the color of the flow
			g.setColor((Color) gui.getResource(getID()).getColor());
		else
			g.setColor(Color.black);
		// draw the flow
		g.setStroke(c_thinStroke);

		//draw first segment
		FlowHandle handle1 = (FlowHandle) handles.get(0);
		g.draw(new Double(getX(), getY(), handle1.getX(), handle1.getY()));
		FlowHandle handle2 = null;
		for (int i = 1; i < handles.size(); i++) {
			handle2 = (FlowHandle) handles.get(i);
			g.draw(new Double(handle1.getX(), handle1.getY(), handle2.getX(),
					handle2.getY()));
			handle1 = handle2;
		}
		//draw last segment
		g.draw(new Double(handle1.getX(), handle1.getY(), getToX(), getToY()));
		// get the flow arrow and draw it
		arrow = getArrow();
		if (arrow != null)
			g.fill(arrow);

		// if flow is marked, we draw the marking area
		if (isMarked()) {
			g.fill(getClickableArea());
			drawHandles(gui, g);
		}
		drawFlowInfo(gui, g);
	}
	
	/**
	 * Draws the extra info like labels and such onto the arrow
	 * @param gui The gui to draw in.
	 * @param g The graphics component to use to draw
	 */
	protected void drawFlowInfo(GUI gui, Graphics2D g) {
		String id = null, label = null;
		int idWidth = 0, idHeight = 0;
		int labelWidth = 0, labelHeight = 0;
		int width = 0, height = 0;

		int middleX = 0;
		int middleY = 0;
		
		if(mode == STRAIGHT){
			middleX = getX() + (getToX() - getX()) / 2;
			middleY = getY() + (getToY() - getY()) / 2;
		} else {
			// if it is bent draw on the middle point
			Point midPoint = getMiddlePoint();
			middleX = midPoint.x;
			middleY = midPoint.y;
		}

		g.setColor(c_idFontColor);
		g.setFont(c_idFont);
		FontMetrics metrics = g.getFontMetrics();

		if (gui.showFlowIDs()) {
			id = getID().toString();
			idWidth = (int) metrics.stringWidth(id);
			idHeight = (int) metrics.getAscent();
		}

		if (gui.showFlowLabels()) {
			label = gui.getLabel(getID());
			labelWidth = (int) metrics.stringWidth(label);
			labelHeight = (int) metrics.getAscent();
		}
		width = idWidth + labelWidth;
		height = idHeight + labelHeight;

		if (gui.showFlowIDs() || gui.showFlowLabels()) {
			g.setColor(c_idBackgroundColor);
			g.fill(new Rectangle(middleX - width / 2 - c_idBackgroundBorder,
					middleY - height / 2 - c_idBackgroundBorder, width + 2
							* c_idBackgroundBorder, height + 2
							* c_idBackgroundBorder));
		}
		
		if (gui.showFlowIDs()) {
			g.setColor(c_textColor);
			g.drawString(id, middleX - idWidth / 2, middleY - height / 2
					+ idHeight);
		}
		
		if (gui.showFlowLabels()) {
			g.setColor(c_textColor);
			g.drawString(label, middleX - labelWidth / 2, middleY + height / 2);
		}
	}

	/**
	 * Draw all handles
	 * @param gui The gui
	 * @param g The graphics object to draw to
	 */
	private void drawHandles(GUI gui, Graphics2D g) {
		for (Iterator iter = handles.iterator(); iter.hasNext();) {
			FlowHandle handle = (FlowHandle) iter.next();
			handle.paint(gui, g);
		}

	}

	/**
	 * Creates a polygonarrow to stitch to the end of the flow.
	 * 
	 * @return A triangle that is the point of the arrow.
	 */
	private Polygon getArrow() {
		float d, x1, y1, x2, y2, x3, y3, x4, y4, x5, y5, f1, f2;
		Point p;
		Polygon polygon = new Polygon();
		FlowHandle lastHandle = (FlowHandle) handles.lastElement();
		x1 = (float) lastHandle.getX();
		y1 = (float) lastHandle.getY();

		x2 = (float) getToX();
		y2 = (float) getToY();

		d = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));

		x3 = x2 - c_arrowLength * (x2 - x1) / d;
		y3 = y2 - c_arrowLength * (y2 - y1) / d;
		x4 = x3 + (c_arrowWidth / 2) * (y2 - y1) / d;
		y4 = y3 - (c_arrowLength / 2) * (x2 - x1) / d;
		x5 = x3 - (c_arrowLength / 2) * (y2 - y1) / d;
		y5 = y3 + (c_arrowLength / 2) * (x2 - x1) / d;

		polygon.addPoint(Math.round(x2), Math.round(y2));
		polygon.addPoint(Math.round(x4), Math.round(y4));
		polygon.addPoint(Math.round(x5), Math.round(y5));

		return polygon;
	}

	/**
	 * @see mind.gui.GraphComponent#getClickableArea()
	 */
	public Shape getClickableArea() {
		if (mode == BENT) {
			int width = 2; 
			Polygon polygon = new Polygon();
			//add 2 points perpendicular to each linesegment
			//upper
			FlowHandle handle1 = (FlowHandle) handles.get(0);
			Point pointToAdd = getPerpendicularPoint(new Line2D.Float(getX(),
					getY(), handle1.getX(), handle1.getY()), true, width);
			polygon.addPoint(pointToAdd.x, pointToAdd.y);
			pointToAdd = getPerpendicularPoint(new Line2D.Float(getX(), getY(),
					handle1.getX(), handle1.getY()), false, width);
			polygon.addPoint(pointToAdd.x, pointToAdd.y);

			FlowHandle handle2 = null;
			for (int i = 1; i < handles.size(); i++) {
				handle2 = (FlowHandle) handles.get(i);
				pointToAdd = getPerpendicularPoint(
						new Line2D.Float(handle1.getX(), handle1.getY(),
								handle2.getX(), handle2.getY()), true, width);
				polygon.addPoint(pointToAdd.x, pointToAdd.y);
				pointToAdd = getPerpendicularPoint(
						new Line2D.Float(handle1.getX(), handle1.getY(),
								handle2.getX(), handle2.getY()), false, width);
				polygon.addPoint(pointToAdd.x, pointToAdd.y);
				handle1 = handle2;
			}
			//last segment
			pointToAdd = getPerpendicularPoint(new Line2D.Float(handle1.getX(),
						handle1.getY(), getToX(), getToY()), true, width);
			polygon.addPoint(pointToAdd.x, pointToAdd.y);
			pointToAdd = getPerpendicularPoint(new Line2D.Float(handle1.getX(),
					handle1.getY(), getToX(), getToY()), false, width);
			polygon.addPoint(pointToAdd.x, pointToAdd.y);
			//lower
			pointToAdd = getPerpendicularPoint(new Line2D.Float(handle1.getX(),
					handle1.getY(), getToX(), getToY()), false, -width);
			polygon.addPoint(pointToAdd.x, pointToAdd.y);
			pointToAdd = getPerpendicularPoint(new Line2D.Float(handle1.getX(),
					handle1.getY(), getToX(), getToY()), true, -width);
			polygon.addPoint(pointToAdd.x, pointToAdd.y);
			
			for (int i = handles.size() - 1; i > 0; i--) {
				handle1 = (FlowHandle) handles.get(i-1);
				pointToAdd = getPerpendicularPoint(
						new Line2D.Float(handle1.getX(), handle1.getY(),
								handle2.getX(), handle2.getY()), false, -width);
				polygon.addPoint(pointToAdd.x, pointToAdd.y);
				pointToAdd = getPerpendicularPoint(
						new Line2D.Float(handle1.getX(), handle1.getY(),
								handle2.getX(), handle2.getY()), true, -width);
				polygon.addPoint(pointToAdd.x, pointToAdd.y);
				handle2 = handle1;
			}
			
			//			last segment lower
			pointToAdd = getPerpendicularPoint(new Line2D.Float(getX(), getY(),
					handle1.getX(), handle1.getY()), false, -width);
			polygon.addPoint(pointToAdd.x, pointToAdd.y);
			pointToAdd = getPerpendicularPoint(new Line2D.Float(getX(), getY(), 
					handle1.getX(), handle1.getY()), true, -width);
			polygon.addPoint(pointToAdd.x, pointToAdd.y);
			return polygon;
		} else
			return super.getClickableArea();
	}

	/**
	 * Returns the handle at the specified location, if there is any.
	 * Else it returns null.
	 * @param x
	 * @param y
	 * @return The handle at the specified location, if there is any, otherwise null.
	 */
	public FlowHandle getHandleAtLocation(int x, int y) {
		for (Iterator iter = handles.iterator(); iter.hasNext();) {
			FlowHandle handle = (FlowHandle) iter.next();
			if (handle.isInsideClickableArea(x, y))
				return handle;
		}
		return null;
	}

	/**
	 * @return Returns the handles.
	 */
	public Vector getHandles() {
		return handles;
	}

	/**
	 * @return Returns the last handle.
	 */
	public FlowHandle getLastHandle() {
		return (FlowHandle) handles.lastElement();
	}
	
	/**
	 * @return 	The middle point on this graph i.e. the middle point of the 
	 *   		middle linesegment.
	 */
	private Point getMiddlePoint() {
		FlowHandle handle1 = (FlowHandle) handles.get(handles.size() / 2);
		if(handles.size() == 1){
			int x = ((handle1.getX() - getX()) / 2) + getX();
			int y = ((handle1.getY() - getY()) / 2) + getY();
			return new Point(x,y);
		}
		FlowHandle handle2 = (FlowHandle) handles.get(handles.size() / 2 - 1);
		int x = ((handle2.getX()-handle1.getX()) / 2) + handle1.getX();
		int y = ((handle2.getY()-handle1.getY()) / 2) + handle1.getY();
		return new Point(x,y);
	}

	/**
	 * @return Returns the mode.
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * @param point
	 * @param float1
	 * @return 	A point on a line perpendicular to the line. 
	 * 			If atFirstPoint is true the point will be a
	 * 			specified offset from that point. If false the 
	 * 			specified offset from the second endpoint.
	 */
	private Point getPerpendicularPoint(Line2D.Float line,
			boolean atFirstPoint, int offset) {

		Point2D.Double p = null;
		if (atFirstPoint){
			p = new Point2D.Double(line.x1,line.y1);
		} else {
			p = new Point2D.Double(line.x2, line.y2);
		}
		// calculate point...
		double dY = line.getP2().getY() - line.getP1().getY();
		double dX = line.getP2().getX() - line.getP1().getX();
		double length = Math.sqrt(dY * dY + dX * dX); //pyttes sats
		//avoid divide by 0
		if(length == 0.0)return new Point((int)p.getX(),(int) p.getY());
		dY = dY / length; //normalize
		dX = dX / length;
		dY = dY * offset; //scale
		dX = dX * offset;
		int newX = new java.lang.Double(p.getX() + dY).intValue();
		int newY = new java.lang.Double(p.getY() - dX).intValue();		
		Point pRet = new Point(newX, newY);
		return pRet;
	}
	
	/**
	 * @return If the flow arrow is bent
	 */
	public boolean isBent(){
		return (mode == BENT);
	}

	/**
	 * @param x The points x coordinate
	 * @param y The points y coordinate
	 * @return True if the point is inside any handles clickable area
	 */
	public boolean isInsideHandleClickableArea(int x, int y) {
		for (Iterator iter = handles.iterator(); iter.hasNext();) {
			FlowHandle handle = (FlowHandle) iter.next();
			if (handle.isInsideClickableArea(x, y))
				return true;
		}
		return false;
	}

	/**
	 * Move all selected handles a given delta
	 * @param dx delta x-coordinate
	 * @param dy delta y-coordinate
	 */
	public void moveSelectedHandles(int dx, int dy) {
		for (Iterator iter = handles.iterator(); iter.hasNext();) {
			FlowHandle element = (FlowHandle) iter.next();
			if(element.isSelected()){
				element.setX(element.getX() + dx);
				element.setY(element.getY() + dy);
			}
		}
		
	}

	/**
	 * Draws the arrow different depending on mode.
	 * @see mind.gui.GraphComponent#paint(mind.gui.GUI, java.awt.Graphics2D)
	 */
	public void paint(GUI gui, Graphics2D g) {
		if (mode == STRAIGHT) {
			super.paint(gui, g);
			return;
		} else {
			// Draw bent arrow
			drawBentFlow(gui, g);
		}
	}

	/**
	 * Removes the handle closest to the point.
	 * @param point The point
	 */
	public void removeHandle(Point point) {
		if(mode == BENT){
			FlowHandle f = getClosestHandle(point);
			handles.remove(f);
			//handles.removeElementAt(handles.size()-1);
			if(handles.isEmpty())
				mode = STRAIGHT;
		}		
	}
	
	/**
	 * @param point The point to check against
	 * @return The handle closest to the point
	 */
	private FlowHandle getClosestHandle(Point point) {
		FlowHandle fh = (FlowHandle) handles.firstElement();
		int dy = fh.getY() - point.y;
		int dx = fh.getX() - point.x;	
		double dist = Math.sqrt(dy * dy + dx *dx);
		for(int i = 1; i < handles.size(); i++){
			FlowHandle newfh = (FlowHandle) handles.get(i);
			dy = newfh.getY() - point.y;
			dx = newfh.getX() - point.x;	
			double newDist = Math.sqrt(dy * dy + dx *dx);
			if(newDist < dist){
				fh = newfh;
				dist = newDist;
			}
		}
		return fh;
	}

	/**
	 * Scales the length of the arrow by percent
	 * @param percent The percentage to scale with.
	 */
	public void scaleByProcent(int percent) {
		super.scaleByProcent(percent);
		//update handles
		for (Iterator iter = handles.iterator(); iter.hasNext();) {
			FlowHandle handle = (FlowHandle) iter.next();
			handle.scaleByProcent(percent);
		}
	}


	/**
	 * Unmarks all handles i.e. sets them not selected
	 */
	public void unmarkAllHandles() {
		for (Iterator iter = handles.iterator(); iter.hasNext();) {
			FlowHandle handle = (FlowHandle) iter.next();
			handle.setSelected(false);
		}	
	}

	/**
	 * Creates the string for the save to RMD
	 * @param zoom what Zoom we're in 
	 * @param indent How much indent it should have
	 * @return The xml string for all handles
	 */
	public String getHandlesXML(int indent, float zoom) {
		String retStr = "";
		for (Iterator iter = handles.iterator(); iter.hasNext();) {
			FlowHandle handle = (FlowHandle) iter.next();
			retStr += XML.indent(indent);
			retStr += "<handle>" + (int)(handle.getX() / zoom) + "," + (int)(handle.getY() / zoom) +"</handle>";
			retStr += XML.nl();
		}
		return retStr;
	}
}