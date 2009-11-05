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
 * This class is instantiated when a new flow should be displayed.
 * It contains all graphical properties of a node such as how it should
 * be painted, what color it has, how marked nodes are displayed etc.
 *
 * @version 0.0 2001-03-26
 * @author Tim Terlegård
 */
package mind.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Line2D.Double;
import java.awt.BasicStroke;

import mind.model.*;

class GraphFlow extends GraphComponent {
	static short c_clickableAreaWidth = 4;

	static Color c_idBackgroundColor = Color.white;

	static Color c_markedColor = Color.black;

	final static Color c_textColor = Color.black;

	static short c_idBackgroundBorder = 2;

	static boolean c_isLabelShown = false;

	static boolean c_isIDShown = false;

	int c_toX;

	int c_toY;

	static final short ARROW_LENGTH = 10;

	static final short ARROW_WIDTH = 10;

	static short c_arrowLength = ARROW_LENGTH;

	static short c_arrowWidth = ARROW_WIDTH;

	static final float BORDER_WIDTH = 3.0f;

	static BasicStroke c_thinStroke = new BasicStroke();

	static BasicStroke c_wideStroke = new BasicStroke(BORDER_WIDTH);

	static final String FONT = "SansSerif";

	static final int FONT_SIZE = 9;

	static Font c_idFont = new Font(FONT, Font.PLAIN, FONT_SIZE);

	static Color c_idFontColor = Color.black;

	/**
	 * Creates a new Graphflow
	 * @param flowID the ID of the new flow
	 * @param from Where the flow comes from
	 * @param to To where is is heading
	 */
	public GraphFlow(ID flowID, Point from, Point to) {
		super(flowID);
		setX((int) from.getX());
		setY((int) from.getY());
		setToX((int) to.getX());
		setToY((int) to.getY());
	}

	/**
	 * Gets a boundingbox for where the flow is.
	 * @return A rectangle which the flow resides within.
	 */
	public Rectangle getRectangle() {
		int x, y;
		int width, height;

		x = getX();
		if (x > c_toX)
			x = c_toX;
		y = getY();
		if (y > c_toY)
			y = c_toY;

		return new Rectangle(x, y, Math.abs(c_toX - getX()), Math.abs(c_toY
				- getY()));
	}

	/**
	 * Moves the flow a specified amount
	 * @param x the delta x to move with.
	 * @param y the delta y to move with.
	 */
	public void move(int x, int y) {
		super.move(x, y);
		setToX(getToX() + x);
		setToY(getToY() + y);
	}

	/**
	 * Moves the flows destination
	 * @param x the x-coordinate to move to.
	 * @param y the y-coordinate to move to.
	 */
	public void moveDestinationTo(int x, int y) {
		setToX(x);
		setToY(y);
	}

	/**
	 * Draws the flow.
	 * @param gui The gui in which to draw the flow
	 * @param g The graphics to use to draw.
	 */
	public void paint(GUI gui, Graphics2D g) {
		Shape arrow;

		Resource resource = gui.getResource(getID());
		if (resource != null)
			// set the color of the flow
			g.setColor((Color) gui.getResource(getID()).getColor());
		else
			g.setColor(Color.black);
		// draw the flow
		g.setStroke(c_thinStroke);
		g.draw(new Double(getX(), getY(), getToX(), getToY()));

		// get the flow arrow and draw it
		arrow = getArrow();
		if (arrow != null)
			g.fill(arrow);

		// if flow is marked, we draw the marking area
		if (isMarked())
			g.fill(getClickableArea());

		drawFlowInfo(gui, g);
	}

	/**
	 * Scales the length of the arrow by percent
	 * @param percent The percentage to scale with.
	 */
	public void scaleByProcent(int percent) {
		super.scaleByProcent(percent);
		c_toX = (int) (c_toX * 100 / getOldScaleByProcent() * getScaleFactor());
		c_toY = (int) (c_toY * 100 / getOldScaleByProcent() * getScaleFactor());
	}

	/**
	 * Scales the flow by <code>procent</code>
	 * @param procent The percentage to scale the flow with.
	 */
	public static void scaleCommonByProcent(int procent) {
		c_arrowLength = (short) (c_arrowLength * 100 / getOldScaleByProcent() * getScaleFactor());
		//	c_arrowWidth = (short) (c_arrowWidth * 100 / getOldScaleByProcent() *
		//			      getScaleFactor());
		c_idFont = new Font(FONT, Font.PLAIN,
				(int) (FONT_SIZE * getScaleFactor()));
	}

	/**
	 * Tells the gui to show the IDs of the flows
	 * @param show true if the IDs should be shown.
	 */
	public void setShowID(boolean show) {
		c_isIDShown = show;
	}

	/**
	 * Tells the gui to show the labels on the flows
	 * @param show true if labels should be shown
	 */
	public void setShowLabel(boolean show) {
		c_isLabelShown = show;
	}

	/**
	 * Gets a small area around the flow that is clickable
	 * @return The shape of the area.
	 */
	protected Shape getClickableArea() {
		Polygon p = new Polygon();
		float x1, y1, x2, y2, xt, yt, d;

		x1 = (float) getX();
		y1 = (float) getY();
		x2 = (float) getToX();
		y2 = (float) getToY();

		d = (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));

		xt = c_clickableAreaWidth * (y1 - y2) / (2 * d);
		yt = -c_clickableAreaWidth * (x1 - x2) / (2 * d);

		// makes a new polygon
		p.addPoint(Math.round(x1 + xt), Math.round(y1 + yt));
		p.addPoint(Math.round(x1 - xt), Math.round(y1 - yt));
		p.addPoint(Math.round(x2 - xt), Math.round(y2 - yt));
		p.addPoint(Math.round(x2 + xt), Math.round(y2 + yt));

		return p;
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

		int middleX = getX() + (getToX() - getX()) / 2;
		int middleY = getY() + (getToY() - getY()) / 2;

		g.setColor(c_idFontColor);
		g.setFont(c_idFont);
		FontMetrics metrics = g.getFontMetrics();

		//	if (isIDShown()) {
		if (gui.showFlowIDs()) {
			id = getID().toString();
			idWidth = (int) metrics.stringWidth(id);
			idHeight = (int) metrics.getAscent();
		}
		//	if (isLabelShown()) {
		if (gui.showFlowLabels()) {
			label = gui.getLabel(getID());
			labelWidth = (int) metrics.stringWidth(label);
			labelHeight = (int) metrics.getAscent();
		}
		width = idWidth + labelWidth;
		height = idHeight + labelHeight;
		// paint background of id and label text
		//	if (isIDShown() || isLabelShown()) {
		if (gui.showFlowIDs() || gui.showFlowLabels()) {
			g.setColor(c_idBackgroundColor);
			g.fill(new Rectangle(middleX - width / 2 - c_idBackgroundBorder,
					middleY - height / 2 - c_idBackgroundBorder, width + 2
							* c_idBackgroundBorder, height + 2
							* c_idBackgroundBorder));
		}
		//	if (isIDShown()) {
		if (gui.showFlowIDs()) {
			g.setColor(c_textColor);
			g.drawString(id, middleX - idWidth / 2, middleY - height / 2
					+ idHeight);
		}
		//	if (isLabelShown()) {
		if (gui.showFlowLabels()) {
			g.setColor(c_textColor);
			g.drawString(label, middleX - labelWidth / 2, middleY + height / 2);
		}
	}

	/**
	 * Creates a polygonarrow to stitch to the end of the flow.
	 * @return A triangle that is the point of the arrow.
	 */
	private Polygon getArrow() {
		float d, x1, y1, x2, y2, x3, y3, x4, y4, x5, y5, f1, f2;
		Point p;
		Polygon polygon = new Polygon();

		x1 = (float) getX();
		y1 = (float) getY();

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

	protected int getToX() {
		//	return c_currentToX;
		return c_toX;
	}

	protected int getToY() {
		//	return c_currentToY;
		return c_toY;
	}

	private boolean isIDShown() {
		return c_isIDShown;
	}

	private boolean isLabelShown() {
		return c_isLabelShown;
	}

	private void setToX(int x) {
		c_toX = x;
		//	c_currentToX = (int) (c_toX * getScaleFactor());
	}

	private void setToY(int y) {
		c_toY = y;
		//	c_currentToY = (int) (c_toY * getScaleFactor());
	}
}