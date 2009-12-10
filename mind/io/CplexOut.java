/*
 * Copyright 2001:
 * Peter Andersson <petan117@student.liu.se>
 * Martin Hagman <marha189@student.liu.se>
 * Henrik Norin <henno776@student.liu.se>
 * Anna Stjerneby <annst566@student.liu.se>
 * Tim Terleg�rd <timte878@student.liu.se>
 * Johan Trygg <johtr599@student.liu.se>
 * Peter �strand <petas096@student.liu.se>
 *
 * Copyright 2004
 * Johan Bengtgsson <johbe496@student.liu.se>
 * Daniel Campos <danca226@student.liu.se>
 * Martin Fagerfj�ll <marfa233@student.liu.se> 
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

import java.io.File;

import mind.model.*;
import mind.automate.OptimizationResult;
import java.util.NoSuchElementException;
import java.util.Hashtable;

import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Enumeration;
import java.util.LinkedList;
import java.io.*;

/**
 * Manages to load/save a model from/to a txt file generated by CPLEX.
 *
 * @author Henrik Norin
 * @author Andreas Remar
 * @version $Revision: 1.15 $ $Date: 2004/12/09 15:51:04 $
 */
public class CplexOut
    implements ModelFileFormat
{
    public CplexOut()
    {
    }

    /**
     * Gets the file formats extension.
     * @return The extension as a string.
     */
    public static String getExtension()
    {
	return "opt";
    }

    /**
     * Gets the file formats description.
     * @return The description as a string.
     */
    public static String getDescription()
    {
	return "Optimized information for a model";
    }

    /**
     * Loads the model from the opt format.
     * Borked, don't use.
     *
     * @param model The model to load into.
     * @param filename The file to load from.
     * @throws IOException ...
     * @throws FileInteractionException ...
     */
    public void load(Model model, File filename)
	throws IOException, FileInteractionException
    {
	String rad;
	String token;
	String params;
	StringTokenizer tokenizer;
	Vector optimizeData = new Vector();
	BufferedReader reader = null;

	try {
	    reader = new BufferedReader(new FileReader(filename));
	}
	catch(FileNotFoundException e) {
	    System.out.println("File can not be found!");
	    e.printStackTrace(System.out);
	    System.exit(1);
	}

	if (reader != null) {
	    do {
		// Read a new line.
		rad = reader.readLine();
		tokenizer = new StringTokenizer(rad);
	    } while (!(tokenizer.nextToken().equals("OBJECTIVE")));
	}
	else
	    throw new FileInteractionException("File not ready to be read");
	// Objective value..
	tokenizer.nextToken();
	// Take care of the target function value below!
	tokenizer.nextToken();

	do {
	    // Read a new line.
	    rad = reader.readLine();
	    tokenizer = new StringTokenizer(rad);
	    token = tokenizer.nextToken();
	} while (token != "1" || token != "A"); /* KNARK ! */

	do {
	    if (token == "A") tokenizer.nextToken();

	    // Change the order in the string to fit constructor.
	    params = new String();
	    for (int i = 0 ; i != 3 ; i++) params.concat(tokenizer.nextToken() + " ");
	    token = tokenizer.nextToken();
	    for (int i = 0 ; i != 3 ; i++) params.concat(tokenizer.nextToken() + " ");
	    params.concat(token);
	    tokenizer = new StringTokenizer(params);

	    optimizeData.add(new Object[]
		{ tokenizer.nextToken(),
		  new EquationOptimization(tokenizer.nextToken(),
					    Float.parseFloat(tokenizer.nextToken()),
					    Float.parseFloat(tokenizer.nextToken()),
					    Float.parseFloat(tokenizer.nextToken()),
					    Float.parseFloat(tokenizer.nextToken()),
					    Float.parseFloat(tokenizer.nextToken()))});

	    // Read a new line.
	    rad = reader.readLine();
	    tokenizer = new StringTokenizer(rad);
	    token = tokenizer.nextToken();
	} while (token != "SECTION" && tokenizer.hasMoreTokens());

	do {
	    // Read a new line.
	    rad = reader.readLine();
	    tokenizer = new StringTokenizer(rad);
	    token = tokenizer.nextToken();
	} while (token != "SECTION" || !tokenizer.hasMoreTokens());

	do {
	    if (token == "A") tokenizer.nextToken();

	    // Change the order in the string to fit constructor.
	    params = new String();
	    for (int i = 0 ; i != 3 ; i++) params.concat(tokenizer.nextToken() + " ");
	    token = tokenizer.nextToken();
	    for (int i = 0 ; i != 3 ; i++) params.concat(tokenizer.nextToken() + " ");
	    params.concat(token);
	    tokenizer = new StringTokenizer(params);

	    // Add a new object to the optimization vector.

	    /*
	      FIXME
	      Kolla vilken optim. som ska va forst..
	      Samma ordning pa Flow:ens parametrar??
	    */

	    optimizeData.add(new Object[]
		{ tokenizer.nextToken(),
		  new FlowOptimization(tokenizer.nextToken(),
					    Float.parseFloat(tokenizer.nextToken()),
					    Float.parseFloat(tokenizer.nextToken()),
					    Float.parseFloat(tokenizer.nextToken()),
					    Float.parseFloat(tokenizer.nextToken()),
					    Float.parseFloat(tokenizer.nextToken()))});

	    // Read a new line.
	    rad = reader.readLine();
	    tokenizer = new StringTokenizer(rad);
	    token = tokenizer.nextToken();
	} while (tokenizer.hasMoreTokens());
	reader.close();

	//return optimizeData;
	model.setOptimization(optimizeData);
    }

    /**
     * reMIND 2004<br>
     * Read in a CPLEX outfile and put it in an OptimizationResult.
     * The above function that was supposed to read in a CPLEX outfile
     * is borked.
     *
     * @param  filename Name of the OPT-file to load.
     * @return An OptimizationResult containing an internal
     *         representation of the optimal result.
     * @throws IOException Thrown whenever the file is unreadable or if it's
     *         the wrong format.
     * @throws FileNotFoundException Thrown if the file can't be found.
     * @see    OptimizationResult
     * @author Andreas Remar
     */
    public static OptimizationResult load(String filename)
	throws IOException, FileNotFoundException
    {
	OptimizationResult m_result = new OptimizationResult();
	FileReader m_fileReader = null;
	BufferedReader m_reader = null;
	String m_row;
	StringTokenizer m_tokenizer;
	String m_token = null;
	boolean m_store = true;
	Hashtable m_flows = new Hashtable();
	int lineNumber = 0;

	if(filename == null) {
	    throw new IllegalArgumentException("Filename is empty (null).");
	}

	/* open the file */
	try {
	    m_fileReader = new FileReader(filename);
	} catch(FileNotFoundException e) {
	    throw new FileNotFoundException("Can't find file " + filename);
	}
	m_reader = new BufferedReader(m_fileReader);

	/* find the objective value */
	do {
	    try {
		m_row = m_reader.readLine();
		lineNumber++;
	    } catch(IOException e) {
		m_fileReader.close();
		throw new IOException(e.getMessage());
	    }

	    if(m_row == null) {
		/* EOF found */
		m_fileReader.close();
		throw new IOException("Unexpected end of file found.");
	    }

	    m_tokenizer = new StringTokenizer(m_row);

	    try{
		m_token = m_tokenizer.nextToken();
	    }
	    catch(NoSuchElementException e) {
		/* we probably found an empty line, continue */
	    }
	} while (m_token.compareTo("OBJECTIVE") != 0);

	/* get the string "VALUE" */
	try {
	    m_tokenizer.nextToken();
	}
	catch(NoSuchElementException e) {
	    m_fileReader.close();
	    throw new IOException("Couldn't find the string 'VALUE'.");
	}

	/* get the objective value */
	try {
	    m_result.globalOptimum = Double.parseDouble(m_tokenizer.nextToken());
	}
	catch(NumberFormatException e){
	    m_fileReader.close();
	    throw new IOException("Unable to read objective value.");
	}

	/* locate the string "SECTION" twice */
	for(int i = 0;i < 2;i++) {
	    m_token = "";
	    do {
		try {
		    m_row = m_reader.readLine();
		    lineNumber++;
		}
		catch(IOException e) {
		    m_fileReader.close();
		    throw new IOException(e.getMessage());
		}
		
		if(m_row == null) {
		    /* EOF found */
		    m_fileReader.close();
		    throw new IOException("Unexpected end of file found.");
		}
		
		m_tokenizer = new StringTokenizer(m_row);
		
		try{
		    m_token = m_tokenizer.nextToken();
		}
		catch(NoSuchElementException e) {

		    /* we probably found an empty line, do nothing */
		}
	    } while (m_token.compareTo("SECTION") != 0);
	}

	/* "SECTION" has been found twice, extract the IDs and activities */
	/* skip three lines */
	try {
	    m_reader.readLine(); /* empty line */
	    m_reader.readLine(); /* headings */
	    m_row = m_reader.readLine(); /* empty line */
	    lineNumber+=3;
	}
	catch(IOException e) {
	    m_fileReader.close();
	    throw new IOException(e.getMessage());
	}

	/* here comes the data! */
	while(m_row != null)
    {
	    m_row = m_reader.readLine();
	    lineNumber++;

	    /* check if we're at EOF */
	    if(m_row == null)
		break;

	    m_tokenizer = new StringTokenizer(m_row);
	    try {
		m_token = m_tokenizer.nextToken();
	    }
	    catch(NoSuchElementException e) {

		/* probably an empty row, continue */
		continue;
	    }

	    /* make sure we skip both 'A' and the NUMBER */
	    if(m_token.compareTo("A") == 0) {
		m_tokenizer.nextToken();
	    }

	    String m_tempId;
	    try {
		m_tempId = m_tokenizer.nextToken();
		m_tokenizer.nextToken(); /* skip column BS*/
	    }
	    catch(NoSuchElementException e) {
		System.out.println("Row: " + lineNumber);
		System.out.println(m_row);
		System.out.println("Token: " + m_token);
		m_fileReader.close();
		throw new IOException("Unexpected end of tokens found.");
	    }

	    double m_value;
	    try {
		m_value = Double.parseDouble(m_tokenizer.nextToken());
	    }
	    catch(NumberFormatException e){
		System.out.println("Row: " + lineNumber);
		System.out.println(m_row);
		System.out.println("Token: " + m_token);
		m_fileReader.close();
		throw new IOException("Unable to read value.");
	    }
	    catch(NoSuchElementException e) {
		System.out.println("Row: " + lineNumber);
		System.out.println(m_row);
		System.out.println("Token: " + m_token);
		m_fileReader.close();
		throw new IOException("Unexpected end of tokens found.");
	    }

	    /* parse tempId to extract ID and timestep */
	    String m_id = null;
	    String m_timestep = null;
	    int m_intId;
	    int m_intTimestep;

	    m_store = true;

	    /* check if this tempId is a flow */
	    try {
		if(m_tempId.charAt(0) != 'F') 
        {
            //System.out.println("We don't have a flow");
		    continue;
		}
		else if(!(m_tempId.charAt(1) >= '0' 
			  && m_tempId.charAt(1) <= '9')) {
		    continue;
		}
	    } catch(Exception e) {
		/* some kind of OOB exception, ignore this id */
		continue;
	    }

	    /* parse ID in tempId */
	    m_id = "";
	    int j = 1;
	    for(int i = 1;i < m_tempId.length();i++)
        {
		char m_ch = m_tempId.charAt(i);
		if(m_ch >= '0' && m_ch <= '9')
		    m_id += m_ch;
		else
            {
		    j = i + 1;
		    break; /* found end of ID */
            }
	    }
	    try
        {
		m_intId = Integer.parseInt(m_id);
	    }
	    catch(NumberFormatException e) {
		System.out.println("m_tempId = " + m_tempId);
		System.out.println("Row: " + lineNumber);
		System.out.println(m_row);
		System.out.println("Token: " + m_token);
		m_fileReader.close();
		throw new IOException("Bad parsing of id number. (" 
				      + m_tempId + ")");
	    }

	    /* parse timestep */
	    m_timestep = "";

	    /* find 'T' by parsing backwards */
	    for(int k = m_tempId.length()-1;k >= 0;k--) {
		if(m_tempId.charAt(k) == 'T') {
		    /* found it! */
		    j = k + 1;
		    break;
		}
		if(k == 0) {
		    /* bad Id, don't save */
		    m_store = false;
		}
	    }

	    for(;j < m_tempId.length();j++)
        {
		char m_ch = m_tempId.charAt(j);
		if(m_ch >= '0' && m_ch <= '9')
		    m_timestep += m_ch;
		else if(m_ch == ' ')
		    break; /* found end of timestep*/
		else
            {
		    m_store = false; /* found something else, this means
				    * we found a temp variable */
            }
	    }

	    if(m_store == false)
		continue;

	    try {
		m_intTimestep = Integer.parseInt(m_timestep);
	    }
	    catch(NumberFormatException e) {
		System.out.println("m_timestep = " + m_timestep);
		System.out.println("Row: " + lineNumber);
		System.out.println(m_row);
		System.out.println("Token: " + m_token);
		m_fileReader.close();
		throw new IOException("Bad parsing of timestep. (" 
				      + m_timestep + ")");
	    }

	    /* ignore 0 timesteps */
	    if(m_intTimestep == 0)
		continue;

	    if(m_store) {

		/* get the hashtable for this flow */
		Hashtable m_flow = (Hashtable)m_flows.get(new Integer(m_intId));
		if(m_flow == null)
            {

		    /* this is a new flow, create a new hashtable and
		     * insert it into the flow hashtable */
		    m_flow = new Hashtable();
		    m_flows.put(new Integer(m_intId), m_flow);
            }
		m_flow.put(new Integer(m_intTimestep), new Double(m_value));
	    }
	}// END OF FILE

	/* Now everything is stored in a hashtable of hashtables.
	 * Convert this to an OptimizationResult. */

	/* the number of flows */
	double m_values[];

	Enumeration enu = m_flows.keys();
	LinkedList keys = new LinkedList();

	/* reverse list of keys */
	while(enu.hasMoreElements())
        {
	    keys.addLast(enu.nextElement());
        }

	/* iterate over all the flows */
	while(keys.size() > 0) 
        {
	    Integer m_flowId = (Integer)keys.removeLast();

	    Hashtable m_flow = (Hashtable)m_flows.get(m_flowId);
	    m_values = new double[m_flow.size()];
	    m_values[0] = 0.0;

	    /* iterate through all the timesteps */
	    for(int j = 1;j <= m_values.length;j++)
            {
            m_values[j - 1] = ((Double)m_flow.get(new Integer(j))).doubleValue();
            }
	    m_result.addFlow(new ID(new Long(m_flowId.intValue()), "F"), m_values);
        }

	m_fileReader.close();

	return m_result;
    }

    /**
     * reMIND 2009<br> Added by Nawzad Mardan 090421, handel new version of CPLEX,
     * The new version of CPLEX(From 11 and up) have an xml out put(result) file, that is totaly different from the old version
     * Read in a CPLEX new version( from 11 and above)outfile and put it in an OptimizationResult.
     * The above function that was supposed to read in a CPLEX outfile
     * is borked.
     *
     * @param  filename Name of the OPT-file to load.
     * @return An OptimizationResult containing an internal
     *         representation of the optimal result.
     * @throws IOException Thrown whenever the file is unreadable or if it's
     *         the wrong format.
     * @throws FileNotFoundException Thrown if the file can't be found.
     * @see    OptimizationResult
     * @author Nawzad Mardan
     */
    public static OptimizationResult newVersionload(String filename)
	throws IOException, FileNotFoundException
    {
	OptimizationResult m_result = new OptimizationResult();
	FileReader m_fileReader = null;
	BufferedReader m_reader = null;
	String m_row, objValue;
	StringTokenizer m_tokenizer;
	String m_token = null;
	boolean m_store = true;
	Hashtable m_flows = new Hashtable();
	int lineNumber = 0;

	if(filename == null) {
	    throw new IllegalArgumentException("Filename is empty (null).");
	}

	/* open the file */
	try {
	    m_fileReader = new FileReader(filename);
	} catch(FileNotFoundException e) {
	    throw new FileNotFoundException("Can't find file " + filename);
	}
	m_reader = new BufferedReader(m_fileReader);

	/* find the objective value */
	do {
	    try {
		m_row = m_reader.readLine();
		lineNumber++;
	    } catch(IOException e) {
		m_fileReader.close();
		throw new IOException(e.getMessage());
	    }

	    if(m_row == null) {
		/* EOF found */
		m_fileReader.close();
		throw new IOException("Unexpected end of file found.");
	    }
	} while (!(m_row.contains("objectiveValue=")));


	/* get the string "VALUE" */
	try {
	    //m_tokenizer.nextToken();
        int x = m_row.indexOf('"');
        objValue = m_row.substring(x+1,m_row.length() - 1); // objValue  without "
	}
	catch(NoSuchElementException e) {
	    m_fileReader.close();
	    throw new IOException("Couldn't find the string 'objectiveValue'.");
	}

	/* get the objective value */
	try {
	    m_result.globalOptimum = Double.parseDouble(objValue);
	}
	catch(NumberFormatException e){
	    m_fileReader.close();
	    throw new IOException("Unable to read objective value.");
	}

	/* locate the string "<variables>"  */
	
	    m_token = "";
	    do {
		try {
		    m_row = m_reader.readLine();
		    lineNumber++;
		}
		catch(IOException e) {
		    m_fileReader.close();
		    throw new IOException(e.getMessage());
		}

		if(m_row == null) {
		    /* EOF found */
		    m_fileReader.close();
		    throw new IOException("Unexpected end of file found.");
		}
	    } while(!(m_row.contains("<variables>")));

	/* "<variables>" tag has been found twice, extract the IDs and activities */
	
	/* here comes the data! */
	//while(m_row != null)
    do
    {
	    m_row = m_reader.readLine();
	    lineNumber++;

	    /* check if we're at EOF */
	    if(m_row == null)
		break;

	    m_tokenizer = new StringTokenizer(m_row);
	    try {
            // skip String <variable
        m_tokenizer.nextToken();
        m_token = m_tokenizer.nextToken();
	    }
	    catch(NoSuchElementException e) {

		/* probably an empty row, continue */
		continue;
	    }
        String m_tempId, value;
	    /* make sure we skip both 'A' and the NUMBER */
	    if(m_token.contains("name=\"F"))
        {
        int x = m_token.indexOf('"');
        m_tempId = m_token.substring(x+1,m_token.length() - 1);
        //Skip index and status
		m_tokenizer.nextToken(); m_tokenizer.nextToken();
        String valueStr;

	   
	    try
        {
		valueStr = m_tokenizer.nextToken();
        if(valueStr.contains("value="))
            {
             int y = valueStr.indexOf('"');
             value = valueStr.substring(y+1,valueStr.length() - 1);
            }
        else
           continue;
	    }
	    catch(NoSuchElementException e) {
		System.out.println("Row: " + lineNumber);
		System.out.println(m_row);
		System.out.println("Token: " + m_token);
		m_fileReader.close();
		throw new IOException("Unexpected end of tokens found.");
	    }// END IF
        }
        else
            continue;
	    double m_value;
	    try {
		m_value = Double.parseDouble(value);
	    }
	    catch(NumberFormatException e){
		System.out.println("Row: " + lineNumber);
		System.out.println(m_row);
		System.out.println("Token: " + m_token);
		m_fileReader.close();
		throw new IOException("Unable to read value.");
	    }
	    catch(NoSuchElementException e) {
		System.out.println("Row: " + lineNumber);
		System.out.println(m_row);
		System.out.println("Token: " + m_token);
		m_fileReader.close();
		throw new IOException("Unexpected end of tokens found.");
	    }

	    /* parse tempId to extract ID and timestep */
	    String m_id = null;
	    String m_timestep = null;
	    int m_intId;
	    int m_intTimestep;

	    m_store = true;

	    /* check if this tempId is a flow */
	    try {
		if(m_tempId.charAt(0) != 'F')
        {
            //System.out.println("We don't have a flow");
		    continue;
		}
		else if(!(m_tempId.charAt(1) >= '0'
			  && m_tempId.charAt(1) <= '9')) {
		    continue;
		}
	    } catch(Exception e) {
		/* some kind of OOB exception, ignore this id */
		continue;
	    }

	    /* parse ID in tempId */
	    m_id = "";
	    int j = 1;
	    for(int i = 1;i < m_tempId.length();i++)
        {
		char m_ch = m_tempId.charAt(i);
		if(m_ch >= '0' && m_ch <= '9')
		    m_id += m_ch;
		else
            {
		    j = i + 1;
		    break; /* found end of ID */
            }
	    }
	    try
        {
		m_intId = Integer.parseInt(m_id);
	    }
	    catch(NumberFormatException e) {
		System.out.println("m_tempId = " + m_tempId);
		System.out.println("Row: " + lineNumber);
		System.out.println(m_row);
		System.out.println("Token: " + m_token);
		m_fileReader.close();
		throw new IOException("Bad parsing of id number. ("
				      + m_tempId + ")");
	    }

	    /* parse timestep */
	    m_timestep = "";

	    /* find 'T' by parsing backwards */
	    for(int k = m_tempId.length()-1;k >= 0;k--) {
		if(m_tempId.charAt(k) == 'T') {
		    /* found it! */
		    j = k + 1;
		    break;
		}
		if(k == 0) {
		    /* bad Id, don't save */
		    m_store = false;
		}
	    }

	    for(;j < m_tempId.length();j++)
        {
		char m_ch = m_tempId.charAt(j);
		if(m_ch >= '0' && m_ch <= '9')
		    m_timestep += m_ch;
		else if(m_ch == ' ')
		    break; /* found end of timestep*/
		else
            {
		    m_store = false; /* found something else, this means
				    * we found a temp variable */
            }
	    }

	    if(m_store == false)
		continue;

	    try {
		m_intTimestep = Integer.parseInt(m_timestep);
	    }
	    catch(NumberFormatException e) {
		System.out.println("m_timestep = " + m_timestep);
		System.out.println("Row: " + lineNumber);
		System.out.println(m_row);
		System.out.println("Token: " + m_token);
		m_fileReader.close();
		throw new IOException("Bad parsing of timestep. ("
				      + m_timestep + ")");
	    }

	    /* ignore 0 timesteps */
	    if(m_intTimestep == 0)
		continue;

	    if(m_store) {

		/* get the hashtable for this flow */
		Hashtable m_flow = (Hashtable)m_flows.get(new Integer(m_intId));
		if(m_flow == null)
            {

		    /* this is a new flow, create a new hashtable and
		     * insert it into the flow hashtable */
		    m_flow = new Hashtable();
		    m_flows.put(new Integer(m_intId), m_flow);
            }
		m_flow.put(new Integer(m_intTimestep), new Double(m_value));
	    }
	}while(!(m_row.contains("</CPLEXSolution>")));// END OF FILE while 

	/* Now everything is stored in a hashtable of hashtables.
	 * Convert this to an OptimizationResult. */

	/* the number of flows */
	double m_values[];

	Enumeration enu = m_flows.keys();
	LinkedList keys = new LinkedList();

	/* reverse list of keys */
	while(enu.hasMoreElements())
        {
	    keys.addLast(enu.nextElement());
        }

	/* iterate over all the flows */
	while(keys.size() > 0)
        {
	    Integer m_flowId = (Integer)keys.removeLast();

	    Hashtable m_flow = (Hashtable)m_flows.get(m_flowId);
	    m_values = new double[m_flow.size()];
	    m_values[0] = 0.0;

	    /* iterate through all the timesteps */
	    for(int j = 1;j <= m_values.length;j++)
            {
            m_values[j - 1] = ((Double)m_flow.get(new Integer(j))).doubleValue();
            }
	    m_result.addFlow(new ID(new Long(m_flowId.intValue()), "F"), m_values);
        }

	m_fileReader.close();

	return m_result;
    }

    /**
     * It is not possible to save a CplexOut file.
     * @param model Looking good.
     * @param filename Name of file.
     */
    public void save( Model model, File filename )
    {
    }
}
