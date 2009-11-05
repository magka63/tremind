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

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class saves and loads commands that are used when running
 * the optimization program.
 *
 * @author Andreas Remar
 * @version $Revision: 1.8 $ $Date: 2005/05/06 10:22:45 $
 */
public class OptimizationCommandFile
{
//     private static String commandFile = "commands.ocf";

    /**
     * Loads in the commands from "commands.ocf".
     *
     *
     * @throws FileNotFoundException "commands.ocf" couldn't be found.
     * @throws IOException Thrown when unable to read.
     * @return String The commands separated by newlines.
     */
    public static String load(String commandFile)
	throws FileNotFoundException, IOException
    {
	FileReader fr = null;
	String commands = "";
	int ret = -1;

	try {
	    fr = new FileReader(new File(commandFile));
	} catch(FileNotFoundException e) {
	    throw new FileNotFoundException("Can't find the command file. "
					    + e.getMessage());
	}

	/* read through the file and add char by char (very efficient) */
	while(true)
	    {
		try {
		    ret = fr.read();
		} catch(IOException e) {
		    throw new IOException("Can't read from command file. "
					  + e.getMessage());
		}

		if(ret == -1)
		    break;
		commands += ""+((char)ret);
	    }

	try {
	    fr.close();
	} catch(IOException e) {
	    throw new IOException("Couldn't close command file. "
				  + e.getMessage());
	}

	return commands;
    }

    /**
     * Saves the argument string in "commands.ocf".
     *
     * @param commands The commands to be saved.
     * @throws IOException Thrown when unable to write.
     * @throws IllegalArgumentException Thrown if commands == null.
     */
    public static void save(String commandFile, String commands)
	throws IOException, IllegalArgumentException
    {
	FileWriter fw = null;

	if(commands == null) {
	    throw new
		IllegalArgumentException("Command string is empty (null).");
	}

	try {
	    fw = new FileWriter(new File(commandFile));
	} catch(IOException e) {
	    throw new IOException("Can't create/write to command file. "
				  + e.getMessage());
	}

	/* as above but reversed.. */
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

    /**
     * Main function for testing.
     *
     * @param args Arguments to the test.
     */
    public static void main(String args[])
    {
	String newline = System.getProperty("line.separator");
	String com
	    = "r MPS" + newline
	    + "o" + newline
	    + "c p milp" + newline
	    + "o" + newline
	    + "w OPT" + newline
	    + "q" + newline;

	try {
	    save("commands.ocf", com);
	} catch(IOException e) {
	    System.out.println(e.getMessage());
	}

	try {
	    com = load("commands.ocf");
	}
	catch(FileNotFoundException e) {
	    System.out.println("Couldn't find commands.ocf");
	}
	catch(IOException e) {
	    System.out.println("IOException");
	    e.printStackTrace();
	}
    }
}
