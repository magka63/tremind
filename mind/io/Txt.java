/*
 * Copyright 2004:
 * Johan Bengtgsson <johbe496@student.liu.se>
 * Daniel Campos <danca226@student.liu.se>
 * Martin Fagerfj?ll <marfa233@student.liu.se>
 * Daniel Ferm <danfe666@student.liu.se>
 * Able Mahari <ablma616@student.liu.se>
 * Andreas Remar <andre063@student.liu.se>
 * Haider Shareef <haish292@student.liu.se>
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

package mind.io;

import mind.automate.OptimizationResult;
import mind.automate.Flow;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ListIterator;
import java.io.File;

/**
 * Class for saving an optimization result as an excel readable file.
 *
 * @author Andreas Remar
 * @version $Revision: 1.8 $ $Date: 2004/12/09 15:51:04 $
 */
public class Txt
{
    /**
     * Saves an optimization result as an excel readable file.
     *
     * @param filename The file to save to.
     * @param result An optimization result.
     * @throws IllegalArgumentException Thrown if filname of result == null.
     */
    public static void save(String filename, OptimizationResult result)
	throws IllegalArgumentException, IOException, FileNotFoundException
    {
	if(filename == null)
	    throw new IllegalArgumentException("Empty (null) filename.");

	if(result == null)
	    throw new IllegalArgumentException("Empty (null) result.");

	/* convert MPS filename to TXT filename *****************************/
	String file = (filename.substring(0, filename.length()-4)) + ".txt";

	/* portable newline representation */
	String newline = System.getProperty("line.separator");

	/* open file for writing *****************************************/
	FileWriter fw = null;
	try {
	    fw = new FileWriter(new File(file));
	}
	catch(FileNotFoundException e) {
	    throw new FileNotFoundException("Can't open " + file);
	}
	catch(IOException e) {
	    /* couldn't open or create file */
	    throw new IOException("Couldn't open " + file + " "
					       + e.getMessage());
	}

	/* output optimum *****************************************/
	String ov = "";
	try {
	    ov = "OBJECTIVE VALUE\t" + result.globalOptimum 
		+ newline + newline;
	}
	catch(NullPointerException e) {
	    /* result == null */
	    throw new IllegalArgumentException("Empty (null) result.");
	}
	try {
	    fw.write(ov, 0, ov.length());
	}
	catch(IOException e) {
	    /* couldn't write to file */
	    throw new IOException("Couldn't write to " + file);
	}

	/* output heading *****************************************/
	int timesteps = 0;
	String heading;
	heading = "ID\tLABEL\t";
	ListIterator flows = result.getFlows();
	if(flows.hasNext()) {
	    Flow flow = (Flow)flows.next();

	    /* set number of timesteps using first flow */
	    timesteps = flow.values.length;
	}
	for(int i = 0;i < timesteps;i++) {
	    heading += "T" + (i+1) + "\t";
	}
	heading += newline;
	try {
	    fw.write(heading, 0, heading.length());
	}
	catch(IOException e) {
	    /* couldn't write to file */
	    throw new IOException("Couldn't write to " + file);
	}

	/* output flow activities *****************************************/
	String activities;
	flows = result.getFlows();
	Flow flow;
	while(flows.hasNext()) {
	    flow = (Flow)flows.next();

	    /* make sure that this flow is selected */
	    if(flow.selected == false) {
		continue;
	    }

	    if(flow.values.length != timesteps) {
		try {
		    fw.close();
		}
		catch(IOException e) {
		    /* ignore */
		}
		throw new
		    IllegalArgumentException("Malformed optimization result");
	    }

	    activities = flow.id + "\t" 
		+ ((flow.label == null) ? "" : flow.label)
		+ "\t";
	    for(int i = 0;i < flow.values.length;i++) {
		activities += flow.values[i] + "\t";
	    }
	    activities += newline;
	    try {
		fw.write(activities, 0, activities.length());
	    }
	    catch(IOException e) {
		/* couldn't write to file */
		throw new IOException("Couldn't write to " + file);
	    }
	}

	/* close file *****************************************/
	try {
	    fw.close();
	}
	catch(IOException e) {
	    /* couldn't close file */
	    throw new IOException("Couldn't close " + file);
	}
    }
}
