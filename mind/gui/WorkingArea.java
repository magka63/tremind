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

import javax.swing.*;
import java.awt.Dimension;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;

import mind.io.*;

public class WorkingArea
    extends JSplitPane
{
    private LeftArea c_leftArea;
    private RightArea c_rightArea;
    private Ini c_userSettings = null;

    public WorkingArea(GUI gui, Ini ini)
    {
	setLeftComponent(c_leftArea = new LeftArea(gui));
	setRightComponent(c_rightArea = new RightArea(gui, ini));
	setContinuousLayout(true);
	setOneTouchExpandable(true);
	setDividerLocation(200);
	setPreferredSize(new Dimension(600, 300));
	c_userSettings = ini;
    }

    public GraphArea getGraphArea()
    {
	return c_rightArea.getGraphArea();
    }

    public RightArea getRightArea()
    {
	return c_rightArea;
    }

    /**
     * Tells the left area of the working area what functions
     * are available.
     * @param functions The functions that will be displayed in
     * the function list.
     */
    public void setAvailableFunctions(Vector functions)
    {
	c_leftArea.setAvailableFunctions(functions);
    }

    /**
     * Tells the left area of the working area what nodes
     * are available.
     * @param rootNode The root node of a tree of nodes that will
     * be displayed in the node list.
     */
    public void setAvailableNodes(DefaultMutableTreeNode rootNode)
    {
	c_leftArea.setAvailableNodes(rootNode);
    }

    public void settingsUpdated()
    {
	// do what you are supposed to do and the make the update
	// propogate further
	c_rightArea.settingsUpdated();
    }
}
