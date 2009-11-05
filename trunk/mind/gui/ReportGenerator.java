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

import java.util.Enumeration;
import java.util.Date;

import mind.model.*;
/**
 * A functional class used as report generator, used by PrintDialog
 * and PreviewDialog to produce a printable resp. viewable report.
 *
 * @author Urban Liljedahl
 * @version 2002-08-29
 */
public class ReportGenerator{
    //constants
    public static final int ASCII = 1;
    public static final int XML = 2;
    public static final int POSTSCRIPT = 3;

    //variables
    private GUI c_gui;//possibility to access all methods necessary to retriev
    //data for the report
    //private Properties c_prop;//The report print properties

    //constructor
    public ReportGenerator(GUI gui){
	c_gui = gui;
    }

    /**
     * Generates and build the report due to print preferences and
     * type of report.
     * @param type Type of report
     */
    public String getReport( int type ){
	String report = "";
	PrintSettings settings = c_gui.getPrintSettings();
	Enumeration enu;
	String header = "";
	if( settings == null ){
	    enu = c_gui.getNodes();
	}
	else{
	    if( settings.getHeader() == true ){
		header = "Version " + new Date().toString();//date
	    }
	    enu = settings.getNodes();
	}
	//build report string
	ID id;
	if( !header.equals("") ){
	    report = header + "\n\n";
	}
	//loop through the enueration and build the text
	while( enu !=  null && enu.hasMoreElements() ){
	    id = (ID) enu.nextElement();
	    report += id.toString() + "\n    " + c_gui.getLabel(id) + "\n" +
		c_gui.getNote(id) + "\n\n";
	}

	return report;
    }

}
