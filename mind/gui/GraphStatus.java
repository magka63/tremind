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

import java.awt.*;
import java.awt.image.*;

import mind.model.*;

public class GraphStatus
{
    public final static int FRESH = 1;
    public final static int FROM_FLOW = 2;
    public final static int TO_FLOW = 3;

    private static Cursor c_defaultCursor;
    private static Cursor c_fromCursor;
    private static Cursor c_toCursor;
    private int c_status;
    private GraphArea c_area;
    private String imageDir = "images/";

    GraphStatus(int status, GraphArea area)
    {
	c_status = status;
	c_area = area;
	Image flowFromImage;
	Image flowToImage;

	Toolkit tk = Toolkit.getDefaultToolkit();
	flowFromImage = tk.createImage(getClass().getResource(imageDir + "from-arrow.gif"));
	flowToImage = tk.createImage(getClass().getResource(imageDir + "to-arrow.gif"));
	Dimension dim = tk.getBestCursorSize(24, 24);

	// save current cursor
	c_defaultCursor = area.getCursor();

	// create flow-from cursor
	// is creation of cursor supported?
	if ((dim.width != 0) && (dim.height != 0))
	    c_fromCursor = tk.createCustomCursor(flowFromImage,
					       new Point(0, 0), "Flow from");
	else
	    c_fromCursor = Cursor.getDefaultCursor();

	// create flow-to cursor
	if ((dim.width != 0) && (dim.height != 0))
	    c_toCursor = tk.createCustomCursor(flowToImage,
					       new Point(0, 0), "Flow from");
	else
	    c_toCursor = Cursor.getDefaultCursor();
    }

    public void setNewFlow()
    {
	if (c_status == FRESH) {
	    c_status = FROM_FLOW;
	    c_area.setCursor(c_fromCursor);
	}
    }

    // precondition: allowed to set status to TO_FLOW
    public void setToFlow()
    {
	c_status = TO_FLOW;
	c_area.setCursor(c_toCursor);
    }

    public int getStatus()
    {
	return c_status;
    }

    public void setFresh()
    {
	c_status = FRESH;
	c_area.setCursor(c_defaultCursor);
    }
}
