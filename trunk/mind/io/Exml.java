/*
 * Copyright 2007:
 * Per Fredriksson <perfr775@student.liu.se>
 * David Karlslätt <davka417@student.liu.se>
 * Tor Knutsson	<torkn754@student.liu.se>
 * Daniel Källming <danka053@student.liu.se>
 * Ted Palmgren <tedpa175@student.liu.se>
 * Freddie Pintar <frepi150@student.liu.se>
 * Mårten Thurén <marth852@student.liu.se> 
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
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import mind.model.Model;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handles export/import of model properties to/from an Excel 2003 XML file.
 *
 * @author Per Fredriksson
 * @author David Karlslätt
 * @author Tor Knutsson
 * @author Freddie Pintar
 * @version 2007-12-02
 */
public class Exml {
	public Exml() {

	}

	/**
	 * Loads EXML file.
	 * Added by PUM5 2007-12-02
	 * @param filename The file to be loaded.
	 * @param model The model to be affected.
	 * */
	public void load(File filename, Model model)
	throws FileInteractionException
	{
		// System.out.println("Loading an RMD file...");
		
		SAXParser saxParser=null;
		
		try {
			// Use the RmdSAXHandler as the SAX event handler
			DefaultHandler handler = new ExmlSAXHandler(model);
			SAXParserFactory factory = SAXParserFactory.newInstance();
			
			
			// Use the default parser
			
			factory.setValidating(false);
			factory.setNamespaceAware(true);

			
			saxParser= factory.newSAXParser();
			// Parse the input
			
			saxParser.parse(filename, handler);
		
		}
		catch (SAXException e) {
			 e.printStackTrace(System.out);

			throw new FileInteractionException(e.getMessage());
			
		}
		catch (Exception e) {  // Catch execptions from saxParser
			 e.printStackTrace(System.out);
			throw new FileInteractionException("Invalid Exml/Remind file : " +filename.getAbsolutePath());
		}
		/*finally
		{
			if (saxParser != null)
			{
				saxParser.reset();
			
			}
		}*/
}

	/**
	 * Returns file extension for EXML.
	 * Added by PUM5 2007-12-02
	 * @return A String with the extension.
	 * */
	public static String getExtension() 
	{
		return "xml";
	}

	/**
	 * Returns description of the file format.
	 * Added by PUM5 2007-12-02
	 * @return A String with the description.
	 * */
	public static String getDescription() 
	{
		return "Excel 2003 XML";
	}

	/**
	 * Saves a model to EXML.
	 * Added by PUM5 2007-12-02
	 * @param idVector A Vector of node ID:s.
	 * @param locked Wether the file is locked or not.
	 * @param model The model to be exported.
	 * @param file The file to save as.
	 * */
	public void save(Vector idVector, boolean locked, Model model, File file)
			throws FileInteractionException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.applyPattern("yyyy-MM-dd'T'hh:mm:ss");
		String strDate = sdf.format(new Date());
		String xml = XML.getExmlHeader()
				+ XML.nl()
				+

				"<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\""
				+ XML.nl()
				+ XML.indent(1)
				+ "xmlns:o=\"urn:schemas-microsoft-com:office:office\""
				+ XML.nl()
				+ XML.indent(1)
				+ "xmlns:x=\"urn:schemas-microsoft-com:office:excel\""
				+ XML.nl()
				+ XML.indent(1)
				+ "xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\""
				+ XML.nl()
				+ XML.indent(1)
				+ "xmlns:html=\"http://www.w3.org/TR/REC-html40\">"
				+ XML.nl()
				+ XML.indent(1)
				+ "<DocumentProperties xmlns=\"urn:schemas-microsoft-com:office:office\">"
				+ XML.nl()
				+ XML.indent(2)
				+ "<Author>reMIND</Author>"
				+ XML.nl()
				+ XML.indent(2)
				+ "<LastAuthor>reMIND</LastAuthor>"
				+ XML.nl()
				+ XML.indent(2)
				+ "<Created>"
				+ strDate
				+ "</Created>"
				+ XML.nl()
				+ XML.indent(2)
				+ "<LastSaved>"
				+ strDate
				+ "</LastSaved>"
				+ XML.nl()
				+ XML.indent(2)
				+ "<Company>PUM5</Company>"
				+ XML.nl()
				+ XML.indent(2)
				+ "<Version>11.8036</Version>"
				+ XML.nl()
				+ XML.indent(1)
				+ "</DocumentProperties>"
				+ XML.nl()
				+ XML.indent(1)
				+ "<Styles>"
				+ XML.nl()
				+ XML.indent(2)
				+ "<Style ss:ID=\"Default\" ss:Name=\"Normal\">"
				+ XML.nl()
				+ XML.indent(3)
				+ "<Alignment ss:Vertical=\"Bottom\"/>"
				+ XML.nl()
				+ XML.indent(3)
				+ "<Borders/>"
				+ XML.nl()
				+ XML.indent(3)
				+ "<Font/>"
				+ XML.nl()
				+ XML.indent(3)
				+ "<Interior/>"
				+ XML.nl()
				+ XML.indent(3)
				+ "<NumberFormat/>"
				+ XML.nl()
				+ XML.indent(3)
				+ "<Protection/>"
				+ XML.nl()
				+ XML.indent(2)
				+ "</Style>"
				+ XML.nl()
				+ XML.indent(2)
				+ "<Style ss:ID=\"FuncType\">"
				+ XML.nl()
				+ XML.indent(3)
				+ "<Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Bold=\"1\"/>"
				+ XML.nl()
				+ XML.indent(3)
				+ "<Interior ss:Color=\"#95B3D7\" ss:Pattern=\"Solid\"/>"
				+ XML.nl()
				+ XML.indent(3)
				+ "<Protection/>"
				+ XML.nl()
				+ XML.indent(2)
				+ "</Style>"
				+ XML.nl()
				+ "<Style ss:ID=\"BoldType\">"
				+ XML.nl()
				+ XML.indent(3)
				+ "<Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Bold=\"1\"/>"
				+ XML.nl()
				+ XML.indent(3)
				+ "<Protection/>"
				+ XML.nl()
				+ XML.indent(2)
				+ "</Style>"

				+ XML.nl()
				+ XML.indent(2)
				+ "<Style ss:ID=\"SideBorders\">"
				+ XML.nl()
				+ XML.indent(3)
				+ "<Borders>"
				+ XML.nl()
				+ XML.indent(4)
				+ "<Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>"
				+ XML.nl()
				+ XML.indent(4)
				+ "<Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>"
				+ XML.nl()				
				+ XML.indent(3)
				+ "</Borders>"
				+ XML.nl()
				+ XML.indent(3)
				+ "<Protection ss:Protected=\"0\"/>"
				+ XML.nl()
				+ XML.indent(2)
				+ "</Style>"
	
				
				+ XML.nl()
				+ XML.indent(2)
				+ "<Style ss:ID=\"SideBottomBorders\">"
				+ XML.nl()
				+ XML.indent(3)
				+ "<Borders>"
				+ XML.nl()
				+ XML.indent(4)
				+ "<Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>"
				+ XML.nl()
				+ XML.indent(4)
				+ "<Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>"
				+ XML.nl()
				+ XML.indent(4)
				+ "<Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>"
				+ XML.nl()	
				+ XML.indent(3)
				+ "</Borders>"
				+ XML.nl()
				+ XML.indent(3)
				+ "<Protection ss:Protected=\"0\"/>"
				+ XML.nl()
				+ XML.indent(2)
				+ "</Style>"				
				

				+ XML.nl()
				+ XML.indent(2)
				+ "<Style ss:ID=\"SideTopBorders\">"
				+ XML.nl()
				+ XML.indent(3)
				+ "<Borders>"
				+ XML.nl()
				+ XML.indent(4)
				+ "<Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>"
				+ XML.nl()
				+ XML.indent(4)
				+ "<Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>"
				+ XML.nl()
				+ XML.indent(4)
				+ "<Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>"
				+ XML.nl()	
				+ XML.indent(3)
				+ "</Borders>"
				+ XML.nl()
				+ XML.indent(3)
				+ "<Protection ss:Protected=\"0\"/>"
				+ XML.nl()
				+ XML.indent(2)
				+ "</Style>"	
				
				
				+ XML.nl()
				+ "<Style ss:ID=\"SheetHeading\">"
				+ XML.nl()
				+ XML.indent(3)
				+ "<Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Size=\"16\" ss:Bold=\"1\"/>"
				+ XML.nl()
				+ XML.indent(3)
				+ "<Protection/>"
				+ XML.nl()
				+ XML.indent(2)
				+ "</Style>"
				+ XML.nl()
				+ XML.indent(2)
				+ "<Style ss:ID=\"FuncLabel\">"
				+ XML.nl()
				+ XML.indent(3)
				+ "<Borders>"
				+ XML.nl()
				+ XML.indent(4)
				+ "<Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>"
				+ XML.nl()
				+ XML.indent(3)
				+ "</Borders>"
				+ XML.nl()
				+ XML.indent(3)
				+ "<Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Bold=\"1\"/>"
				+ XML.nl()
				+ XML.indent(3)
				+ "<Interior ss:Color=\"#D8D8D8\" ss:Pattern=\"Solid\"/>"
				+ XML.nl()
				+ XML.indent(3)
				+ "<Protection/>"
				+ XML.nl()
				+ XML.indent(2)
				+ "</Style>"
				+ XML.nl()
				+ XML.indent(2)
				+ "<Style ss:ID=\"TimeStep\">"
				+ XML.nl()
				+ XML.indent(3)
				+ "<Borders>"
				+ XML.nl()
				+ XML.indent(4)
				+ "<Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>"
				+ XML.nl()
				+ XML.indent(3)
				+ "</Borders>"
				+ XML.nl()
				+ XML.indent(3)
				+ "<Font ss:FontName=\"Arial\" x:Family=\"Swiss\" ss:Italic=\"1\"/>"
				+ XML.nl() + XML.indent(3) + "<Protection/>" + XML.nl()
				+ XML.indent(2) + "</Style>" + XML.nl() + XML.indent(2)
				+ "<Style ss:ID=\"TableStyle\">" + XML.nl() + XML.indent(3)
				+ "<Protection ss:Protected=\"0\"/>" + XML.nl() + XML.indent(2)
				+ "</Style>" + XML.nl() + XML.indent(2)
				+ "<Style ss:ID=\"Locked\">" + XML.nl() + XML.indent(3)
				+ "<Protection/>" + XML.nl() + XML.indent(2) + "</Style>"
				+ XML.nl() + XML.indent(1) + "</Styles>" + XML.nl() +

				model.toEXML(idVector, locked, 1) + XML.nl() +

				"</Workbook>" + XML.nl();

		try {
			FileWriter fr = new FileWriter(file);
			fr.write(xml, 0, xml.length());
			fr.close();
		} catch (IOException e) {
			throw new FileInteractionException(e.getMessage());
		}

	}
}
