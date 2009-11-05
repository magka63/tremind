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

import java.io.*;

import mind.gui.UserSettingConstants;
import java.util.*;

/**
 * This class saves and loads from a .ini file. The ini file
 * contains sections with keys and values.
 *
 * @author Tim Terlegård
 * @version 2001-04-07
 */
public class Ini
    extends Properties
    implements GUIFileFormat, UserSettingConstants
{
    private String c_filename = SAVE_FILE;

    /**
     * A nullconstructor.
     */
    public Ini()
    {
	super();
        // Nawzad Mardan lagt till 2007-07-01
          try {
	    creatCommandFile();
            } 
        catch(IOException e) 
            {
	    System.out.println(e.getMessage());
            }
	load();
    }

    /**
     * A constructor that loads the specified file.
     * @param filename
     */
    public Ini(String filename)
    {
	super();
	c_filename = filename;
        
	load();
    }

    /**
     * Loads a file into memory.
     * @return Returns 1 if load succeeded, 0 if file not found
     * and -1 if io exception.
     */
    public int load()
    {
	if (c_filename != null)
	    return load(c_filename);

	return 0;
    }

    /**
     * Loads a file into memory.
     * Some very primitive functionality for default values is implemented here.
     * @param filename The file to load.
     * @return Returns 1 if load succeeded, 0 if file not found
     * and -1 if io exception.
     */
    public int load(String filename)
    {
	if (c_filename != null) {
	    // clear hashtable
	    clear();
	    c_filename = filename;
	    try {
		load(new FileInputStream(c_filename));
		if (getProperty(AUTOSAVE) == null) 
                    {
		    setProperty(AUTOSAVE, AUTOSAVE_TIMER);
                    }
                
                if (getProperty(AUTOSAVE_STATE) == null) 
                    {
                  setProperty(AUTOSAVE_STATE, "0");
                    }

		return 1;
	    }
	    catch (FileNotFoundException exception) {
		setProperty(AUTOSAVE, AUTOSAVE_TIMER);
                setProperty(AUTOSAVE_STATE, "0");
		save();
		return 0;
	    }
	    catch (IOException exception) {
		System.out.println("IO Exception");
		return -1;
	    }

	}
	return 0;
    }

    /**
     * Gets a very short description of this file format.
     * @return The description.
     */
    public String getDescription()
    {
	return "User settings";
    }

    /**
     * Gets this' file format's extension.
     * @return The extension.
     */
    public String getExtension()
    {
	return "ini";
    }

    /**
     * Saves a file.
     * @return Returns 1 if save succeeded, 0 if file not found
     * and -1 if io exception.
     */
    public int save()
    {
	if (c_filename != null)
	    return save(c_filename);

	return 0;
    }

    /**
     * Saves a file.
     * @param filename The filename of the file to save into.
     * @return Returns 1 if save succeeded, 0 if file not found
     * and -1 if io exception.
     */
    public int save(String filename)
    {
	if (filename != null) {
	    c_filename = filename;
	    try {
		store(new FileOutputStream(c_filename), HEADER);
		return 1;
	    }
	    catch (FileNotFoundException exception) {
		return 0;
	    }
	    catch (IOException exception) {
		return -1;
	    }
	}

	return 0;
    }

    /**
     * Sets the (key, value) pair and then saves to file.
     * @param key The key of the property to add or change.
     * @param value The value of the property to add or change.
     * @return An object of no use.
     */
    public Object setProperty(String key, String value)
    {
	Object o = super.setProperty(key, value);
	save();
	return o;
    }


    /**
     * Gets a property with a default value if the property does not exist. If
     * the property doesn't exist it is also created with the given default value
     * @author Jonas Sääv - Ace Simultion 2005-05-12
     * @param key
     * @param defaultValue
     * @return
     */
  public String getProperty(String key, String defaultValue) {
    String prop = getProperty(key);
    if (prop == null) {
      setProperty(key, defaultValue);
    }
    return super.getProperty(key);
  }
  
   // Nawzad Mardan lagt till 2007-07-01
  
  /**
     * Create "commands.ocf" and the commands to be saved.
     * @throws IOException Thrown when unable to write.
     * @throws IllegalArgumentException Thrown if commands == null.
     * Automate the creation of the command file 
     * The commnds inside this file used when CPLEX  is executes
     */ 
   private void creatCommandFile() throws IOException, IllegalArgumentException
    {
      String newline = System.getProperty("line.separator");
      //commands = "r MPS\r\no\r\nc p f\r\no\r\nw OPT\r\nq\r\n";*/
	String commands = "r MPS" + newline
	    + "o" + newline
	    + "c p f" + newline
	    + "o" + newline
	    + "w OPT" + newline
	    + "q" + newline;

        String commandFile = "commands.ocf";
	FileWriter fw = null;

	if(commands == null) {
	    throw new
		IllegalArgumentException("Command string is empty (null).");
	}
        if(!(new File(commandFile)).exists())
        {

	try {
	    fw = new FileWriter(new File(commandFile));
	} catch(IOException e) {
	    throw new IOException("Can't create/write to command file. "
				  + e.getMessage());
	}

	/* witre to the file  char by char (very efficient) */
	for(int i = 0;i < commands.length();i++) {
	    try {
		fw.write((int)(commands.charAt(i)));
	    } catch(IOException e) {
		throw new IOException("Can't write command to file. "
				      + e.getMessage());
	    }
	}

	try {
	    fw.close();
	} catch(IOException e) {
	    throw new IOException("Couldn't close command file. "
				  + e.getMessage());
	}
    }
   }
}
