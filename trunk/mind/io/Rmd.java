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
package mind.io;

import java.io.File;
import java.io.*;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import java.util.Hashtable;

import mind.model.*;
import mind.gui.*;

/**
 * Handles save/load a model to/from a rmd file.
 *
 * @author Henrik Norin
 * @author Johan Trygg
 * @version 2001-07-10
 */
public class Rmd
{

    public Rmd()
    {
    }

    /**
     * Gets the file formats extension.
     * @return The extension as a string.
     */
    public static String getExtension()
    {
	return "rmd";
    }

    /**
     * Gets the file formats description.
     * @return The description as a string.
     */
    public static String getDescription()
    {
	return "RMD - Complete models";
    }

    /**
     * Load a model into the program. (cannot be used)
     * @param model
     * @param filename The file to load into model.
     */
    public void load(Model model, File filename)
	throws FileInteractionException
    {
	throw new FileInteractionException("This Rmd.load function is not implemented");
    }

    /**
     * Load a model into the program.
     * @param model A model
     * @param graphModel A gui representation of the model.
     * @param filename The file to load into model.
     */
    public void load(Model model, GraphModel graphModel, File filename)
	throws FileInteractionException
    {
	//System.out.println("Loading an RMD file...");

        try {
	    // Use the RmdSAXHandler as the SAX event handler
	    DefaultHandler handler = new RmdSAXHandler(model, graphModel);
	    // Use the default parser
	    SAXParserFactory factory = SAXParserFactory.newInstance();
	    factory.setValidating(true);
	    factory.setNamespaceAware(false);

             // Parse the input
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(filename, handler);
        }
	catch (SAXException e) {

	    throw new FileInteractionException(e.getMessage());
	}
	catch (Exception e) {  //Catch execptions from saxParser
	    //e.printStackTrace(System.out);
	    throw new FileInteractionException(e.toString());
	}
    }

    /**
     * Save a model to a file.
     * @param model
     * @param file
     */
    public void save(Model model, File file)
	throws FileInteractionException
    {
	String xml = XML.getHeader("model") + XML.nl() +
	    "<model>" + XML.nl() +
	    model.toXML(1) + XML.nl() +
	    "</model>" + XML.nl();

	try {
	    FileWriter fr = new FileWriter(file);
	    fr.write(xml, 0, xml.length());
	    fr.close();
	}
	catch (IOException e) {
	    throw new FileInteractionException(e.getMessage());
	}
    }

    /**
     * Save a model to a file.
     * @param model
     * @param graphModel A gui representation of the model.
     * @param file
     */
    public void save(Model model, GraphModel graphModel, GUI gui, File file)
	throws FileInteractionException
    {
	String xml = XML.getHeader("model") + XML.nl() +
	    "<model>" + XML.nl() +
	    model.toXML(1) + XML.nl() +
	    XML.indent(1) + "<extensions>" + XML.nl() +
	    graphModel.toXML(2) +
            gui.toXML(2) +
	    XML.indent(1) + "</extensions>" + XML.nl() +
	    "</model>" + XML.nl();
	try {
	    FileWriter fr = new FileWriter(file);
	    fr.write(xml, 0, xml.length());
	    fr.close();
	}
	catch (IOException e) {
	    throw new FileInteractionException(e.getMessage());
	}
    }

    //Function for debugging
    /*
    public static void main(String argv[])
    {
        if (argv.length != 1) {
            //System.err.println("Usage: cmd filename");

	    String s = "hej&&hej& igen. 2 < 44 men a = 2 och 33 > 1 &så";
	    //          hej&amp;hej&amp; igen . 2";
	    //          012345678901234567890
	    s = XML.toXML(s);
	    System.out.println(s);

            System.exit(1);
        }

	Model m = new Model();
	GraphModel gm = new GraphModel();
	File f = new File(argv[0]);
	Rmd rmd = new Rmd();
	try {
	    rmd.load(m,gm,f);
	}
	catch (FileInteractionException e) {
	    System.out.println(e); //.getMessage());

	    System.exit(1);
	}
	catch (Exception e) {
	    System.out.println(e);
	    e.printStackTrace(System.out);
	    System.exit(1);
	}
	//System.out.println("model:\n" + m.toXML(1));
	System.out.println("gui model:\n" + gm.toXML(1));

    }
    */
}
