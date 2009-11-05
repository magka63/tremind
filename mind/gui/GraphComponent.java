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
package mind.gui;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

import mind.model.*;


abstract public class GraphComponent
{
    protected boolean c_isDraggedOver = false;
    protected boolean c_isMarked = false;
    protected ID c_ID;
    private int c_x;
    private int c_y;
    //    private int c_currentX;
    //    private int c_currentY;

    private static int c_scaleByProcent = 100;
    private static int c_oldScaleByProcent = 100;

    public GraphComponent(ID componentID)
    {
	c_ID = componentID;
    }

    public boolean equals(GraphComponent gc)
    {
	if (gc == null)
	    return false;

	return gc.getID().equals(getID());
    }

    public static float getOldScaleByProcent()
    {
	return c_oldScaleByProcent;
    }

    public abstract Rectangle getRectangle();

    public static float getScaleByProcent()
    {
	return c_scaleByProcent;
    }

    public static float getScaleFactor()
    {
	return c_scaleByProcent / 100.0f;
    }

    public boolean isInsideClickableArea(int x, int y)
    {
	return getClickableArea().contains(x, y);
    }

    public boolean isMarked()
    {
	return c_isMarked;
    }

    public void move(int x, int y)
    {
	setX(c_x + x);
	setY(c_y + y);
    }

    // inherits this method from GraphComponent but can't use it
    public void moveTo(int x, int y)
    {
	setX(x);
	setY(y);
    }

    public abstract void paint(GUI gui, Graphics2D g);

    public void setDraggedOver(boolean isDraggedOver)
    {
	c_isDraggedOver = isDraggedOver;
    }

    public void setMarked(boolean marked)
    {
	c_isMarked = marked;
    }

    public static void setScaleByProcent(int procent)
    {
	c_oldScaleByProcent = c_scaleByProcent;
	c_scaleByProcent = procent;
    }

    public void scaleByProcent(int procent) {
	c_x = (int) (c_x * 100 / c_oldScaleByProcent * getScaleFactor());
	c_y = (int) (c_y * 100 / c_oldScaleByProcent * getScaleFactor());
    }

    public abstract void setShowID(boolean show);
    public abstract void setShowLabel(boolean show);

    protected abstract Shape getClickableArea();

    protected int getX()
    {
	//	return c_currentX;
	return c_x;
    }

    protected int getY()
    {
	//	return c_currentY;
	return c_y;
    }

    protected void setX(int x)
    {
	c_x = x;
	//	c_currentX = (int) (c_x * getScaleFactor());
    }

    protected void setY(int y)
    {
	c_y = y;
	//	c_currentY = (int) (c_y * getScaleFactor());
    }

    ID getID()
    {
	return c_ID;
    }
}
