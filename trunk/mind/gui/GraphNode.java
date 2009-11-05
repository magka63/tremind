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
 * This class is instantiated when a new node should be displayed.
 * It contains all graphical properties of a node such as how it should
 * be painted, what color it has, how marked nodes are displayed etc.

 * @author Tim Terlegård
 */
package mind.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import mind.model.*;

public class GraphNode
    extends GraphComponent
{
    static Color c_bgColor = Color.white;
    static Color c_borderColor = Color.black;
    static Color c_draggedOverColor = Color.black;
    static Font c_idFont = new Font("SansSerif", Font.PLAIN, 9);
    static Color c_idFontColor = Color.black;
    static boolean c_isLabelShown = false;
    static boolean c_isIDShown = false;
    static final int FONT_SIZE = 9;
    static final String FONT = "arial";

    // two different borders of the node
    static BasicStroke thinBorder = new BasicStroke();
    static BasicStroke wideBorder = new BasicStroke(3.0f);
    static int c_width;
    static int c_height;

    private static final int NODE_WIDTH = 36;
    private static final int NODE_HEIGHT = 40;

    public GraphNode(ID nodeID)
    {
	super(nodeID);
	setX(getWidth());
	setY(getHeight());
	c_width = (int) (NODE_WIDTH * getScaleFactor());
	c_height = (int) (NODE_HEIGHT * getScaleFactor());
    }

    public GraphNode(ID nodeID, int x, int y)
    {
	super(nodeID);
	setX(x);
	setY(y);
	c_width = (int) (NODE_WIDTH * getScaleFactor());
	c_height = (int) (NODE_HEIGHT * getScaleFactor());
    }

    public GraphNode(ID nodeID, int x, int y, int width, int height)
    {
	super(nodeID);
	setX(x);
	setY(y);
	c_width = (int) (NODE_WIDTH * getScaleFactor());
	c_height = (int) (NODE_HEIGHT * getScaleFactor());
    }

    public GraphNode(ID nodeID, GraphNode node)
    {
	super(nodeID);
	setX(node.getX());
	setY(node.getY());
	c_width = node.getWidth();
	c_height = node.getHeight();
    }

    public int getHeight()
    {
	//	return c_currentHeight;
	return c_height;
    }

    public Point getMiddlePoint()
    {
	return new Point(getX() + (int) getWidth()/2,
			 getY() + (int) getHeight()/2);
    }

    public Rectangle getRectangle()
    {
	return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public int getWidth()
    {
	//	return c_currentWidth;
	return c_width;
    }

    public boolean isDraggedOver()
    {
	return c_isDraggedOver;
    }

    public void paint(GUI gui, Graphics2D g)
    {
	if (isDraggedOver())
	    g.setColor(c_draggedOverColor);
	else
	    g.setColor(c_bgColor);

        g.fill(new Rectangle(getX(), getY(), getWidth(), getHeight()));

	if (isMarked())
	    g.setStroke(wideBorder);
	else
	    g.setStroke(thinBorder);

	drawNodeInfo(gui, g);

        g.setColor(c_borderColor);
        g.draw(new Rectangle(getX(), getY(), getWidth(), getHeight()));
    }

    public void scaleByProcent(int procent)
    {
	super.scaleByProcent(procent);
    }

    public static void scaleCommonByProcent(int procent)
    {
	c_width = (int) (NODE_WIDTH * getScaleFactor());
	c_height = (int) (NODE_HEIGHT * getScaleFactor());
	c_idFont = new Font(FONT, Font.PLAIN,
			    (int) (FONT_SIZE * getScaleFactor()));
    }

    public void setBgColor(Color color)
    {
	c_bgColor = color;
    }

    public void setShowID(boolean show)
    {
	c_isIDShown = show;
    }

    public void setShowLabel(boolean show)
    {
	c_isLabelShown = show;
    }

    public void setX(int x)
    {
	if (x < (int) (getWidth()/2))
	    super.setX((int) (getWidth()/2));
	else
	    super.setX(x);
    }

    public void setY(int y)
    {
	if (y < (int) (getHeight()/2))
	    super.setY((int) (getHeight()/2));
	else
	    super.setY(y);
    }

    protected final Shape getClickableArea()
    {
	return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    private void drawNodeInfo(GUI gui, Graphics2D g)
    {
	String id = null, label = null;
	int idWidth = 0, idHeight = 0;
	int labelWidth = 0, labelHeight = 0;
	int width = 0, height = 0;
	Vector subs;

	g.setColor(c_idFontColor);
	g.setFont(c_idFont);

	FontMetrics metrics = g.getFontMetrics();

//	if (isIDShown()) {
        if (gui.showNodeIDs()) {
	    id = getID().toString();
	    idHeight = (int) metrics.getAscent();
	}
//	if (isLabelShown()) {
        if (gui.showNodeLabels()) {
	    label = gui.getLabel(getID());
	    labelHeight = (int) metrics.getAscent();
	}

	height = idHeight + labelHeight;
	// paint background of id and label text
//	if (isIDShown()) {
        if (gui.showNodeIDs()) {
	    g.drawString(id, getX()+3, getY()+idHeight+2);
	}
//	if (isLabelShown()) {
        if (gui.showNodeLabels()) {
	    subs = getSubStrings(label, getWidth()-6, g);
	    for (int i = 0; i < subs.size(); i++) {
		g.drawString(subs.elementAt(i).toString(), getX()+3,
			     getY()+height+i*labelHeight+2);
	    }
	}
    }

    private Color getBgColor()
    {
	return c_bgColor;
    }

    private int getLengthInsideBounds(String str, int maxWidth, Graphics2D g)
    {
	Font font = g.getFont();
	Rectangle2D rect;

	for (int i = str.length()-1; i >= 0; i--) {
	    rect = font.getStringBounds(str, 0, i, g.getFontRenderContext());
	    if (rect.getWidth() <= maxWidth)
		return i+1;
	}
	return 0;
    }

    private Vector getSubStrings(String str, int maxWidth, Graphics2D g)
    {
	int length;
	Vector subs = new Vector(0);
	String newString = new String(str);
	String oldString;
	String sub;

	length = getLengthInsideBounds(newString, maxWidth, g);
	while (length > 0) {
	    sub = newString.substring(0, length);
	    subs.addElement(sub);
	    oldString = newString;
	    if (length == newString.length())
		return subs;
	    newString = new String(newString.substring(length));
	    length = getLengthInsideBounds(newString, maxWidth, g);
	}

	return subs;
    }

    private boolean isIDShown()
    {
	return c_isIDShown;
    }

    private boolean isLabelShown()
    {
	return c_isLabelShown;
    }
}
