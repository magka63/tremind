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
import java.awt.Point;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.io.IOException;

import mind.io.*;

/**
 * This class adds scrollbars to the graph area.
 *
 * @author Tim Terlegård
 * @author Jonas Sääv
 * @version 2003-09-15
 */

public class RightArea
    extends JScrollPane
{
    private GraphArea c_graphArea;
    private Ini c_userSettings = null;

    /**
     * Constructs a GraphArea with scrollbars
     */
    public RightArea(GUI gui, Ini ini)
    {
	c_graphArea = new GraphArea(gui, ini);
	c_userSettings = null;

	setHorizontalScrollBarPolicy(ScrollPaneConstants.
				     HORIZONTAL_SCROLLBAR_ALWAYS);
	setVerticalScrollBarPolicy(ScrollPaneConstants.
				   VERTICAL_SCROLLBAR_ALWAYS);
	setViewportView(c_graphArea);

        getVerticalScrollBar().setUnitIncrement(5);  // Boosts the mouse wheel response
    }

    public GraphArea getGraphArea()
    {
	return c_graphArea;
    }

    public void settingsUpdated()
    {
	// do what you are supposed to do and the make the update
	// propogate further
	c_graphArea.settingsUpdated();
    }
}
