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
 * 
 * Copyright 2007:
 * Daniel Källming <danka053@student.liu.se>
 * David Karlslätt <davka417@student.liu.se>
 * Freddie Pintar <frepi150@student.liu.se>
 * Mårten Thurén <marth852@student.liu.se>
 * Per Fredriksson <perfr775@student.liu.se>
 * Ted Palmgren <tedpa175@student.liu.se>
 * Tor Knutsson <torkn754@student.liu.se>
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

import java.io.File;
import java.util.Vector;
import javax.swing.*;
import mind.model.*;
import mind.gui.*;
import java.awt.*;

/**
 * The class FileInteraction is the interface between the module
 * with the same name and other modules.
 * It can hold information about file formats available for saving/restoring.
 *
 * @author Henrik Norin
 * @author Johan Trygg
 * @author David Karlslätt
 * @author Freddie Pintar
 * @author Per Fredriksson
 * @author Tor Knutsson
 * @version 2007-12-01
 */
public class FileInteraction
{
    public final static int RMD = 0;
    public final static int MPS = 1;
    public final static int OPT = 2;
    public final static int XML = 3;

    // A frame that show information about what is going on
    //private  JFrame frame = null;
    //private JLabel label = null;

    public Vector getAvailableExportFileFormats()
    {
	Vector formats = new Vector(0);
	formats.addElement(new String[] { Mps.getExtension(),
					  Mps.getDescription() });
	return formats;
    }
    
    /**
	 * Returns available xml file formats for loading.
	 * Added by PUM5 2007-10-12
	 * @return A String[] with the formats.
	 * */
    public Vector getAvailableExportXmlFileFormats()
    {
	Vector formats = new Vector(0);
	formats.addElement(new String[] { Exml.getExtension(),
					  Exml.getDescription() });
	return formats;
    }
    
    /**
     * Returns available file formats for loadng.
     * @return A String[] with the formats.
     */
    public Vector getAvailableLoadFileFormats()
    {
	Vector formats = new Vector(0);
	formats.addElement(new String[] { Rmd.getExtension(),
					  Rmd.getDescription() });
	//We cannot load a CplexOut file, or???
	//(at least not from the openDialog in GUI)
	//formats.addElement(new String[] { CplexOut.getExtension(),
	//				  CplexOut.getDescription() });
	return formats;
    }

    /**
     * Returns available file formats for saving.
     * @return A String[] with the formats.
     */
    public Vector getAvailableSaveFileFormats()
    {
	Vector formats = new Vector(0);
	formats.addElement(new String[] { Rmd.getExtension(),
					  Rmd.getDescription() });
	return formats;
    }

    /**
	 * Returns available file formats for importing.
	 * Added by PUM5 2007-11-01
	 * @return A String[] with the formats.
	 */
    public Vector getAvailableImportFileFormats()
    {
	Vector formats = new Vector(0);
	formats.addElement(new String[] {Exml.getExtension(),
					  Exml.getDescription() });
	return formats;
    }

    /**
     * Loads the model from the specified file.
     *
     * @param model The model to load into.
     * @param filename The filename to load it from.
     */
    public void load(Model model, int type, File filename)
	throws FileInteractionException
    {
	if (type == RMD) {
	    Rmd rmd = new Rmd();
	    rmd.load(model, filename);
	}
	else if (type == OPT){
	    CplexOut opt = new CplexOut();
	    try {
		opt.load(model, filename);
	    } catch (java.io.IOException e) {
		e.printStackTrace(System.out);
		System.out.println("Couldn't save file!");
	    }
	}
	else
	    throw new FileInteractionException("This file type can't be loaded.");
    }

    /**
     * Loads the model from the specified file.
     *
     * @param model The model to load into.
     * @param graphModel The gui representation of the model.
     * @param filename The filename to load it from.
     */
    public void load(Model model, GraphModel graphModel, int type, File filename)
	throws FileInteractionException
    {
	if (type == RMD) {
	    Rmd rmd = new Rmd();
	    rmd.load(model, graphModel, filename);
	}

	 //Format added by PUM5 2007
	else if (type == XML) {
		Exml xml = new Exml();
		xml.load(filename, model);
	}
	else if(type == OPT) {
	    CplexOut opt = new CplexOut();
	    try {
		opt.load(model, filename);
	    } catch (java.io.IOException e) {
		e.printStackTrace(System.out);
		System.out.println("Couldn't save file!");
	    }
	}
	else
	    throw new FileInteractionException("This file type can't be loaded.");

    }
    
    /**
     * Saves the model in specified format.
     *
     * @param idVector A vector with node id's which should be exported to an Exml workbook
     * @param model The model to be saved.
     * @param filename The file to save it to.
     * @param extension The extension of the filename.
     */
    public void save(Vector idVector, Model model, int type, File filename) 
    throws FileInteractionException {
    	save(idVector, true, model, type, filename);
    }
    
    /**
     * Saves the model in specified format.
     * @param idVector A vector with node id's which should be exported to an Exml workbook
     * @param locked An Exml workbook is locked if this is set to true
     * @param model The model to be saved.
     * @param filename The file to save it to.
     * @param extension The extension of the filename.

     */
    public void save(Vector idVector, boolean locked, Model model, int type, File filename)
	throws FileInteractionException
    {
	String fname = filename.toString();
	int ext_index = fname.lastIndexOf('.');

	if (type == RMD) {
	    Rmd rmd = new Rmd();
	    if (ext_index == -1) //no extension, so add .rmd
	    	fname = fname + ".rmd";
	    File f = new File(fname);
	    rmd.save(model, f);
	}
	else if(type == MPS) {
	    Mps mps = new Mps();
	    if (ext_index == -1) //no extension, so add .mps
	    	fname = fname + ".mps";
	    File f = new File(fname);
	    try {
                 //JOptionPane.showMessageDialog(null,"Pleas wait... Creating MPS File");
		//initComponents () ;
                mps.save(model, f);
	    } catch (java.io.IOException e) {
		e.printStackTrace(System.out);
		System.out.println("Couldn't save file!");
	    }
	}

	//Format added by PUM5 2007
	else if(type == XML) {
	    Exml exml = new Exml();
	    if (ext_index == -1) //no extension, so add .xml
	    	fname = fname + ".xml";
	    File f = new File(fname);
	    exml.save(idVector, locked, model, f);
	}
	else
	    throw new FileInteractionException("This file type can't " +
					       "be saved.");
   // waiting();
    }

    /**
     * Saves the model in specified format.
     *
     * @param model The model to be saved.
     * @param graphModel The gui representation of the model.
     * @param filename The file to save it to.
     */
    public void save(Model model, GraphModel graphModel, GUI gui, int type, File filename)
	throws FileInteractionException
    {
	String fname = filename.toString();
	int ext_index = fname.lastIndexOf('.');

	if (type == RMD) {
	    Rmd rmd = new Rmd();
	    if (ext_index == -1) //no extension, so add .rmd
	    	fname = fname + ".rmd";
	    File f = new File(fname);
	    rmd.save(model, graphModel, gui, f);
	}
	else if(type == MPS) {
	    Mps mps = new Mps();
	    if (ext_index == -1) //no extension, so add .mps
	    	fname = fname + ".mps";
	    File f = new File(fname);
	    try {
                //JOptionPane.showMessageDialog(null,"Pleas wait... Creating MPS File");
               // initComponents () ;
		mps.save(model, f);
	    } catch (java.io.IOException e) {
		e.printStackTrace(System.out);
		System.out.println("Couldn't save file!");
	    }
	}
	else
	    throw new FileInteractionException("This file type can't " +
					       "be saved.");
    //waiting();
    }

}
