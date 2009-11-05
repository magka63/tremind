/*
 * 
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

import java.util.Vector;
import java.util.Iterator;

/**
 * Handles wrapping of rows and cells in exml.
 * ADDED by PUM5
 * @author Tor Knutsson
 * @author Freddie Pintar
 * @version 2007-10-29
 */
    
public class ExmlSheet {

	
	// Total nr of cells, used in header.
	private int rowCount;
		
	// Constants we need to determine what is the maximum number of cells in a row. We need this in the header.
	private int cellCount, finalCellCount;
	
	private int indent;
	
	//Matrix status & limit variables
	private int currentRow, currentCol, rowLimit, colLimit;
	
	// Vector used to store cells.
	private Vector <Vector<String>> timeWorkMatrix;
	
	// Content of the XML sheet
	private String xml;


	/**
	 * Creates a new ExmlSheet 
	 * @param name The name of the sheet.
	 * @param indent The base indentation level of the sheet.
	 */
	public ExmlSheet(String name, int indent) {
		xml = "";
		rowCount = 1;
		cellCount = 0;
		// Since function header is 5 cells long, table's smallest cellcount is 5.
		finalCellCount = 5;
		addRow(addCell("String", name, "SheetHeading", 4));
		addRow(addCell(""));
	}
	/**
	 * Prepares a string to be a valid name string for a sheet flap,
	 * by cutting it and appending dots if it is too long (31 chars),
	 * and replacing illegal characters.
	 * @param str
	 * @return
	 */
	public static String flapName(String str){
		String replacethis[] = {"*","/","?",":","\\"};
		for (int i = 0;i < replacethis.length;i++)
			str = str.replace(replacethis[i], "_");
		if (str.length() > 31) {
			return XML.toXML(str.substring(0, 28)+"...");	
		}
		else
			return XML.toXML(str);
	}
	
	/**
	 * Add a row to the current sheet 
	 * Data is one or more cells in XML format. Will return a row with cells in XML.
	 * @param data is one or more cells, use addCell to create this data.
	 */
	public void addRow(String data) {
		cellCount = 0;
		rowCount++;
		xml += XML.indent(indent +1) + "<Row>" + XML.nl() + data + XML.indent(indent +1) +"</Row>" + XML.nl();
	}

	/**
	 * Wraps data into an EXML cell. 
	 * @param type EXML datatype. This should be either "String" or "Number". 
	 * @param data celldata
	 * @param styleID a workbook StyleID for appearances or locking
	 * @param mergeAcross number of cells to the right of this one that should be merged
	 * @return A string to be used by addRow
	 */
	public String addCell(String type, String data, String styleID, int mergeAcross) {
		String cellProperties;
		cellCount++;
		if (cellCount > finalCellCount) {
			finalCellCount = cellCount;	
		}
		cellProperties = "";
		if (mergeAcross != 0){
			cellProperties += " ss:MergeAcross=\""+mergeAcross+"\"";
		}
		if (styleID != "") {
			cellProperties += " ss:StyleID=\""+styleID+"\"";
		}
		if (data == "")
			return XML.indent(indent + 2) + "<Cell"+cellProperties+"><Data ss:Type=\""+type+"\"/></Cell>" + XML.nl();
		else
			return XML.indent(indent + 2) + "<Cell"+cellProperties+"><Data ss:Type=\""+type+"\">"+data+"</Data></Cell>" + XML.nl();    		
	}
	/**
	 * Wraps data into an EXML cell. 
	 * @param type EXML datatype. This should be either "String" or "Number". 
	 * @param data celldata
	 * @return A string to be used by addRow
	 */
	public String addCell(String type, String data) {
		return addCell(type, data, "",0);
	}

	/**
	 * Wraps float data into an EXML cell. 
	 * @param data float celldata
	 * @return A string to be used by addRow
	 */
	public String addCell(float data) {
		String newdata = ((Float) data).toString();
		return addCell("Number", newdata, "",0);
	}

	/**
	 * Wraps double data into an EXML cell. 
	 * @param data double celldata
	 * @return A string to be used by addRow
	 */
	public String addCell(double data) {
		String newdata = ((Double) data).toString();
		return addCell("Number", newdata, "",0);
	}

	
	/**
	 * Wraps boolean data into an EXML cell, as a number. 
	 * @param data boolean celldata
	 * @return A string to be used by addRow
	 */
	public String addCell(boolean data) {
		if (data)
			return addCell("Number", "1", "",0);
		else
			return addCell("Number", "0", "",0);
	}
	
	
	/**
	 * Wraps int data into an EXML cell. 
	 * @param data int celldata
	 * @return A string to be used by addRow
	 */
	public String addCell(int data) {
		String newdata = ((Integer) data).toString();
		return addCell("Number", newdata, "",0);
	}
	
	/**
	 * Wraps string data into an EXML cell, and locks it from editing. 
	 * @param data String celldata
	 * @return A string to be used by addRow
	 */
	public String addLockedCell(String data) {
		return addCell("String", data, "Locked", 0);
	}
	/**
	 * Wraps string data into an EXML cell, and locks it from editing.
	 * Also makes the content bold.
	 * @param data String celldata
	 * @return A string to be used by addRow
	 */
	public String addBoldCell(String data) {
		return addCell("String", data, "BoldType", 0);
	}
	
	/**
	 * Wraps int data into an EXML cell. 
	 * @param data float celldata
	 * @return A string to be used by addRow
	 */
	public String addCell(String data) {
		return addCell("String", data, "",0);
	}

	
/*
 * initTable, addTableValue and endTable is used to build a timestep table, values for timesteps is 
 * coming in a row. We solve this by saving all values in a table and create the xml values after.
 * 
 * currentRow is the current row in the matrix, while rowLimit is the limit. 
 * same for the col. col is updated when a row is filled.
 * 
 */	
	
	
	/**
	 * Initiate a Matrix for a time step table
	 * @param numberOfFunctionValues rows in the table
	 * @param numberOfTimeSteps columns in the table except title col 
	 */
	public void initTable(int numberOfFunctionValues, int numberOfTimeSteps) {
		currentRow = 0;
		rowLimit = numberOfFunctionValues;
		currentCol = 0;
		colLimit = numberOfTimeSteps +1;
		
		timeWorkMatrix = new Vector<Vector<String>>(colLimit);
		for (int i = 0; i < rowLimit; i++) {
			Vector<String> stringVector = new Vector<String>(colLimit);
			/*for (int j = 0; j < colLimit;j++){
				stringVector.add(j,"");
			}*/
			timeWorkMatrix.addElement(stringVector);
		}
		
		//timeWorkMatrix.clear();
	}

	/**
	 * Push a value to the table
	 * @param datatype
	 * @param data
	 */
	public void addTableValue(String datatype, String data) {
		addTableValue(datatype,data, false);
	}

	/**
	 * Push a value to the table
	 * @param datatype
	 * @param data
	 * @param locked
	 */
	public void addTableValue(String datatype, String data, boolean locked) {
		addTableValue(datatype, data, (locked)?"Locked":"");
	}
	
	/**
	 * Push a value to the table
	 * @param datatype
	 * @param data
	 * @param locked
	 */
	public void addTableValue(String datatype, String data, String style) {
		
		//Debugging
		//System.out.println("addTableValue:: row: "+currentRow+ "/"+ rowLimit);
		//System.out.println("addTableValue:: col: "+currentCol + "/" + colLimit);
		//System.out.println("addTableValue:: data:"+data+"\n");
		String styleSheet = style;
		
		timeWorkMatrix.elementAt(currentRow).add(currentCol, addCell(datatype,data, styleSheet, 0));
		//Cellcount is not made for instant tabling, this is added in endTable()instead 
		cellCount = 0;
		
		//Jump to the next row
		currentRow++;
		
		//If it is the end of the row, we go to the next column
		if (currentRow >= rowLimit) {
			currentCol++;
			currentRow = 0;
			
		}
	}

	/**
	 * Push a string value to the table
	 * @param data
	 */
	public void addTableValue(String data) {
		addTableValue("String", data);
	}

	/**
	 * Add a float styled in-table cell
	 * @param data
	 * @param style
	 */
	public void addStyledTableValue(float data, String style){
		addTableValue("Number", ((Float)data).toString(), style);
	}

	/**
	 * Add a double styled in-table cell
	 * @param data
	 * @param style
	 */
	public void addStyledTableValue(double data, String style){
		addTableValue("Number", ((Double)data).toString(), style);
	}
	
	/**
	 * Add an string styled in-table cell
	 * @param data
	 * @param style
	 */
	public void addStyledTableValue(String data, String style){
		addTableValue("String", data, style);
	}
	
	
	/**
	 * Push an int value to the table
	 * @param data
	 */
	public void addTableValue(int data) {
		addTableValue("Number", ((Integer)data).toString());
	}

	/**
	 * Push a float value to the table
	 * @param data
	 */
	public void addTableValue(float data) {
		addTableValue("Number", ((Float)data).toString());
	}

	/**
	 * Push a float value to the table
	 * @param data
	 */
	public void addTableValue(double data) {
		addTableValue("Number", ((Double)data).toString());
	}
	
	/**
	 * Push a locked string value to the table
	 * @param data
	 */
	public void addLockedTableValue(String data) {
		addTableValue("String", data, true);
	}
	
	
	/**
	 * Fill the table row with "-" values and begin at next row
	 */
	public void fillTableToNextCol() {
		fillTableToNextCol("-", false);
	}
	

	/**
	 * Fill the table row with values and begin at next row
	 * @param filler string to fill each cell with
	 * @param locked wheather or not to lock the cell
	 */
	public void fillTableToNextCol(String filler, boolean locked) {
		int currcol = currentCol;
		while(currcol == currentCol) {
			addTableValue("String", filler, locked);
		}
	}
	
	// Build XML table from the values in the Matrix.
	/**
	 * Compile an EXML Table with rows and cols out of the Matrix
	 * and output to the sheet
	 */
	public void endTable() {
		String inrowdata = "";
		for (int rowc = 0; rowc < rowLimit; rowc++) {
		   Iterator <String> it = timeWorkMatrix.elementAt(rowc).iterator();
		   
		   while (it.hasNext ()) {			   
			   inrowdata += it.next();			   
		   }	

			addRow(inrowdata);
			inrowdata = "";			
		}
		// Update finalcellcount if table is bigger than biggest row
		if (colLimit > finalCellCount) {
			finalCellCount = colLimit;	
		}
		timeWorkMatrix.clear();
	}

	/**
	 * Adds a function header with type and label to the sheet.(Helper function)
	 * @param functionType
	 * @param functionLabel 
	 */
	public void addFunctionHeader(String functionType, String functionLabel) {
		addRow(addCell("String","Function Type","FuncType",0)+addCell("String",functionType,"FuncType",3));
		addRow(addCell("String","Label","FuncLabel",0)+addCell("String",functionLabel,"FuncLabel",3));
	}
	
	/**
	 * Add a title row for time steps (helper function)
	 * @param name Usually "Time step", but may be altered
	 * @param numberOfTimeSteps
	 */
	public void addTimeStepRow(String name, int numberOfTimeSteps) {
		String timestepdata = "";

		timestepdata = addCell("String",name,"TimeStep",0);
		for(Integer i = 1; i <= numberOfTimeSteps; i++) {		
			timestepdata = timestepdata + addCell("Number",i.toString(),"TimeStep",0);
		}
		
		addRow(timestepdata);
		
	}
	
	/**
	 * Add a title row for time steps (helper function)
	 * @param numberOfTimeSteps
	 */
	public void addTimeStepRow(int numberOfTimeSteps) {
		addTimeStepRow("Time Step",numberOfTimeSteps);
	}
	
	
	/**
	 * Outputs the result of the created sheet as a string, wrapped in table tags
	 * @return
	 */
	public String getXml() {
		if (xml == "") {			
			return "";
		}

		return   XML.indent(indent) + 
		"<Table ss:ExpandedColumnCount=\""+finalCellCount+"\" ss:ExpandedRowCount=\""+rowCount+"\" x:FullColumns=\"1\" x:FullRows=\"1\" ss:StyleID=\"TableStyle\">" + XML.nl() + 
		XML.indent(indent + 1) + "<Column ss:Width=\"81.75\"/>" +XML.nl() +
		xml +XML.nl() + XML.indent(indent) + "</Table>"+XML.nl() ;
	}

	
}