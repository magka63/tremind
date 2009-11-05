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

package mind.gui.dialog;

import java.awt.*;          // for Component
import java.awt.event.*;    // for WindowListener
import javax.swing.*;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;

import mind.gui.*;
import mind.model.*;
import mind.io.*;

/**
 * This is the Main Dialog of the whole application.
 * Most of the user interaction is done with this dialog.
 * It contains a menu, a toolbar, a nodetype/function window and
 * a graph area where the user can create its model.
 *
 * @author Tim Terlegård
 * @version 2001-04-09
 */

public class MainDialog
    extends JFrame
{
    final String c_title = "reMIND";

    private WorkingArea c_workingArea;
    private GridBagConstraints c_constraints;
    private Ini c_userSettings = null;
    private GlobalActions c_actions;
    public TopMenu c_topMenu = null;
    public TopToolbar c_topToolbar = null;

    /**
     * A nullconstructor
     */
    public MainDialog(final GUI gui, GlobalActions actions, Ini ini)
    {
	// set title of the application
	setTitle(c_title);
        setTitleAddon("Untitled");

	c_userSettings = ini;

	// make the app exit when you click the top right cross
	addWindowListener(new WindowAdapter () {
	    public void windowClosing(WindowEvent e)
		{
		    gui.exitAction();
		}});

	setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

	// set the layout of the app frame
	getContentPane().setLayout(new GridBagLayout());
	c_constraints = new GridBagConstraints();

	// add components to the main dialog
	addMenu(c_topMenu = new TopMenu(actions));
        c_topToolbar = new TopToolbar(actions);
	addToolbar(c_topToolbar);
	addWorkingArea(new WorkingArea(gui, ini));

	c_actions = actions;

	pack();
    }

    /**
     * Possibility to get global actions from a dialog box with
     * a reference to MainDialog
     */
    public GlobalActions getActions()
    {
	return c_actions;
    }

    /**
     * Adds a toolbar to this dialog
     * @param toolbar The toolbar to add to the main dialog
     */
    public void addToolbar(Component toolbar)
    {
	c_constraints.gridx = 0;
	c_constraints.gridy = 0;
	getContentPane().add(toolbar, c_constraints);
    }

    /**
     * Sets the main window title to "reMIND - " + addon
     * @param addon The string to add to the title
     **/
    public void setTitleAddon(String addon)
    {
        if ((addon == null) || (addon.length() == 0)) {
            setTitle(c_title);
        } else {
            setTitle(c_title + " - " + addon);
        }
    }

    /**
     * Adds a workingarea to this dialog
     * @param area The workingarea to add to the main dialog
     */
    public void addWorkingArea(WorkingArea area)
    {
	c_constraints.gridx = 0;
	c_constraints.gridy = 1;
	c_constraints.fill = GridBagConstraints.BOTH;
	c_constraints.weightx = 1.0;
	c_constraints.weighty = 1.0;
	c_constraints.gridwidth = 2;
	getContentPane().add(area, c_constraints);
	c_workingArea = area;
    }

    /**
     * Adds a menu to this dialog
     * @param menubar The menu to add to the main dialog
     */
    public void addMenu(JMenuBar menubar)
    {
	setJMenuBar(menubar);
    }

    /**
     * Gets the graph area that is in the working area.
     * @return The graph area.
     */
    public GraphArea getGraphArea()
    {
	return c_workingArea.getGraphArea();
    }

    /**
     * Tells the working area what functions are available.
     * @param functions A vector of strings (functions) that will
     * be displayed in the function list.
     */
    public void setAvailableFunctions(Vector functions)
    {
	c_workingArea.setAvailableFunctions(functions);
    }

    /**
     * Tells the working area what nodes are available.
     * @param rootNode The root node of a tree of nodes that will
     * be displayed in the node list.
     */
    public void setAvailableNodes(DefaultMutableTreeNode rootNode)
    {
	c_workingArea.setAvailableNodes(rootNode);
    }

    public void settingsUpdated()
    {
	// do what you want to do and then make the update propagate further
	c_workingArea.settingsUpdated();
    }
}
