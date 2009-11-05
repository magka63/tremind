/*
 * Copyright 2002:
 * Urban.Liljedahl <urban.liljedahl@sm.luth.se>
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

import java.util.Vector;
import java.util.Enumeration;
import java.util.Date;

/**
 * A information class that hold the print settings for a report. Maybe
 * useful for other purposes in the future.
 *
 * @author Urban Liljedahl
 * @version 2002-09-06
 */
public class PrintSettings{
    //variables
    private boolean[] c_selected;
    private Enumeration c_nodes;//vector of ID's
    //private GUI c_gui;

    //constructor
    public PrintSettings(){//GUI gui){
	//c_gui = gui;
    }
    /**
     * Set properties
     */
    public void setPrintSettings(boolean[] selected, Enumeration nodes){
	c_selected = selected;
	c_nodes = nodes;
    }


    /**
     * Get header from print settings
     * @return c_nodes A vector of node ID's selected for the report
     */
    public boolean getHeader(){

	if( c_selected != null )
	    return c_selected[0];
	else
	    return false;
    }

    /**
     * Get properties
     * @return c_nodes A vector of node ID's selected for the report
     */
    public Enumeration getNodes(){
	return c_nodes;
    }

}
