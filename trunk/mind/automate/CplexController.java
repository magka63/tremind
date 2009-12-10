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

package mind.automate;

import mind.automate.Pair;
import mind.io.OptimizationCommandFile;
import mind.io.CplexOut;
import mind.GlobalStringConstants;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.DataInputStream;
import java.io.File;
import javax.swing.JOptionPane;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.event.WindowEvent;
/**
 * Class for automatic optimization using the external program CPLEX.
 *
 * @author Andreas Remar
 * @version $Revision: 1.17 $ $Date: 2005/05/06 10:22:45 $
 */
public class CplexController
    extends OptimizeController
{
    /**
     * Perform automatic optimization of the MPS file using Runtime.
     *
     * @param filename The MPS file to optimize.
     * @param cplexPath The path including exe-file to CPLEX executable
     * @return A pair containing stdout from CPLEX and an OptimizationResult.
     *         If the OptimizationResult is null, there was an error.
     * @throws IOException
     * @throws FileNotFoundException
     */
    private  JFrame frame = null;
    private JLabel label = null;
    private JButton button = null;
    private JPanel upperpanel=null;
    private JPanel lowerpanel=null;
    private JPanel mainpanel=null;
    private String exitMessage=null;
    private Process cplex = null;
    private  int versonNumber;
    /**
   * method is called from the optimize method to
     * initialize the form.
     * A Dialog that shows that the program running CPlex or optimizing 
     */
    private void initComponents () 
    {
        exitMessage ="You requested to exit this application.\n" +
	"The current model is not saved and will be destroyed\n " +
	"if you exit. Do you want to exit without saving the\n" +
	"current model?";
        frame = new JFrame("reMIND: Please wait... running optimazation");
        label = new JLabel("Information: Please wait... running Optimization");
        button = new JButton("Quit");
        upperpanel = new JPanel();
        lowerpanel = new JPanel();
        mainpanel = new JPanel();
        upperpanel.add(label);
        lowerpanel.add(button);
        mainpanel.add(upperpanel);
        mainpanel.add(lowerpanel);
        frame.getContentPane().add(mainpanel);
        frame.addWindowListener(new WindowAdapter() { public void windowClosing(WindowEvent evt) 
        {
        int  select = JOptionPane.showConfirmDialog(frame, exitMessage,"Confirm dialog",JOptionPane.YES_NO_OPTION);
        if (select == JOptionPane.YES_OPTION)
            {
            // End cplex running 
             cplex.destroy();
             System.exit(0);
            }
        }  });  
        button.addActionListener(new ActionListener()
        {public void actionPerformed(ActionEvent e) 
         {
             
           int  selection = JOptionPane.showConfirmDialog(frame, exitMessage,"Confirm dialog",JOptionPane.YES_NO_OPTION);
        if (selection == JOptionPane.YES_OPTION)
            {
             // End cplex running
             cplex.destroy();
             frame.setVisible(false);
             frame.dispose();
             System.exit(0);
            }
        }
        } );
        
        
        frame.setSize(350,120);
       
        Dimension d = frame.getToolkit().getScreenSize();
        Rectangle b = frame.getBounds();
        frame.setLocation((d.width-b.width)/2, (d.height-b.height)/2);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
         
        frame.setVisible(true);
  
    }
    
    /* method is called from the optimize method to
     * try to sleep 1 second for user's sake to have time see the information in the frame
     * A Dialog shows that the Optimization is going on
     */
    private void waiting()
    {
     
        try{
         Thread.sleep(1000);
        }
        catch(Exception e)
        {
            System.out.print(e.getMessage());
        }
        //Destroy the resources for the farme, and return the memory to the OS, and marked as undisplayable
        frame.dispose();
    }
    
    
    /* method is called from the optimize method to
     * initialize the form.
     * A Dialog shows that the Optimization is going on
     */   
    public Pair optimize(String filename, String cplexPath)
	throws IOException, FileNotFoundException
    {
	boolean optimizationSuccessful = true;
	OptimizationResult result = null;
	String cplexOutput = "";
        
       
	if(filename == null) {
	    throw new IllegalArgumentException("Filename is empty (null).");
	}
	if(cplexPath == null) {
	    throw new IllegalArgumentException("Path to CPLEX is"
					       + " empty (null).");
	}

	/* fix double backslash for MS-DOG */
	filename = filename.replaceAll("\\\\", "\\\\\\\\");

	String fileWithoutExtension = getFilenameWithoutExtension(filename);

	/* read in CPLEX commands from an OCF file */
	String commands = null;
        /* Write the command files content into a string instate of reading file
        // reMIND must execute and run CEPLEX with out need of any extra file like commands.ocf 
        //commands = "r MPS\r\no\r\nc p f\r\no\r\nw OPT\r\nq\r\n";*/
	try 
        {
	    commands = OptimizationCommandFile.load(GlobalStringConstants.CPLEX_COMMAND_FILE);
	}
	catch(FileNotFoundException e) {
	    throw new FileNotFoundException("Can't find command.ocf.");
	}
	catch(IOException e) {
	    throw new IOException("Couldn't read command file.");
	}

	
    initComponents();
        
	/* run CPLEX with Runtime */
	Runtime runtime = Runtime.getRuntime();
	//Process cplex = null;
	try {
	    cplex = runtime.exec(cplexPath);
	} catch(SecurityException e) {
            //Destroy the resources for the farme, and return the memory to the OS, and marked as undisplayable
            frame.dispose();
	    throw new IOException(e.getMessage());
	} catch(IOException e) {
            //Destroy the resources for the farme, and return the memory to the OS, and marked as undisplayable
            frame.dispose();
	    throw new IOException(e.getMessage());
	} catch(NullPointerException e) {
            //Destroy the resources for the farme, and return the memory to the OS, and marked as undisplayable
            frame.dispose();
	    throw new IllegalArgumentException(e.getMessage());
	} catch(IllegalArgumentException e) {
            //Destroy the resources for the farme, and return the memory to the OS, and marked as undisplayable
            frame.dispose();
	    throw new IllegalArgumentException(e.getMessage());
	}

	InputStream stdout = cplex.getInputStream();
	DataInputStream in = new DataInputStream(stdout);

	OutputStream stdin = cplex.getOutputStream();
	PrintStream out = new PrintStream(stdin);

	String line = null;
	String temp = "";

	int nrOfWaits = 0;

	/* wait for "CPLEX>" */
	while(true) {
	    while(in.available() > 0) {
		byte[] bytes = new byte[in.available()];
		in.read(bytes);
		line = new String(bytes);

		temp += line;
	    }
        // Added by Nawzad Mardan 090320
        // SEEK CPLEX VERSION

        if(temp.lastIndexOf("CPLEX Interactive Optimizer") != -1)
        {
		String	token = null;
        String  version ;
        java.util.StringTokenizer  tokenizer = new java.util.StringTokenizer(temp);
        do{
            try{
                token = tokenizer.nextToken();
                }
            catch(Exception e) {
            /* we probably found an empty line, continue */
            }
           } while (!token.equals("Optimizer"));
        version = tokenizer.nextToken();
        int x = version.indexOf('.');
        //version.replace(' ','.' );
        String sVec = version.substring(0, x);//sTmp.split(r)
        versonNumber = Integer.parseInt(sVec);

        System.out.println("CEPLEX VERSION IS "+ versonNumber + " : "+ version);

	    }
	    if(temp.lastIndexOf("CPLEX>") != -1) {
		cplexOutput += temp;
		break;
	    }

	    /* sleep a little so that we don't lock up the system */
	    try {
		Thread.sleep(10);
	    } catch(InterruptedException e) {
		/* ignore */
	    }
	    nrOfWaits++;
	    if(nrOfWaits > 500) {
		/* you're slow mister! 5 seconds is too long */
		cplexOutput += temp;
		String newline = System.getProperty("line.separator");
		cplexOutput += newline + "The path you set to CPLEX doesn't"
		    + " seem to be correct." + newline
		    + "Please change search path of CPLEX in Options.";
		Pair p = new Pair();
		p.left = cplexOutput;
		p.right = null;
                //Destroy the resources for the farme, and return the memory to the OS, and marked as undisplayable
                frame.dispose();

		return p;
	    }
	}

    /* replace "MPS" and "OPT" with "filename mps" and
	   "filename.opt txt" */
	String optFile = fileWithoutExtension+".opt";


	commands = commands.replaceAll("MPS", filename + " mps");
	// Decide kind or type of output file (versonNumber) Added by Nawzad Mardan 090325
    if(versonNumber <= 10)
        commands = commands.replaceAll("OPT", optFile + " txt");
    else
        commands = commands.replaceAll("OPT", optFile + " sol");

	/* delete OPT file if it exists */
	if((new File(optFile)).exists()) {
	    (new File(optFile)).delete();
	}

	String coms[] = commands.split("\n");
	boolean quit = false;

	for(int i = 0;i < coms.length;i++) {

	    /* perform command */
	    cplexOutput += coms[i] + System.getProperty("line.separator");
	    out.println(coms[i]);
	    out.flush();

	    /* wait for "CPLEX>" */
	    temp = "";
	    while(!quit) {
		while(in.available() > 0) {
		    byte[] bytes = new byte[in.available()];
		    in.read(bytes);
		    line = new String(bytes);

		    temp += line;
		}
		/* check if any problem or error has occured */
		if(temp.lastIndexOf("CHANGE PROBLEM") != -1) {
		    /* CPLEX wants you to change the problem type. */
		    optimizationSuccessful = false;
		} else if(temp.lastIndexOf("No file read.") != -1) {
		    /* There was a problem with reading a file. */
		    optimizationSuccessful = false;
		} else if(temp.lastIndexOf("infeasible") != -1) {
		    /* Unable to optimize. */
		    optimizationSuccessful = false;
		} else if(temp.lastIndexOf("Type 'help' for a "
						  + "list of commands.")
			  != -1) {
		    /* Command not found. */
		    optimizationSuccessful = false;
		} else if(temp.lastIndexOf("Error") != -1) {
		    /* General failure. */
		    optimizationSuccessful = false;
		} else if(temp.lastIndexOf("File type options") != -1){
		    /* Fail if we must select filetype */
		    optimizationSuccessful = false;
		}

		if(temp.lastIndexOf("CPLEX>") != -1) {
		    cplexOutput += temp;
		    break;
		}

		if(optimizationSuccessful == false) {
		    cplexOutput += temp;
		    break;
		}

		/* have we quit cplex? */
		try {
		    cplex.exitValue();
		    quit = true;
		} catch(IllegalThreadStateException e) {
		    /* nope */
		}

		/* sleep a little so that we don't lock up the system */
		try {
		    Thread.sleep(10);
		} catch(InterruptedException e) {
		    /* ignore */
		}
	    }// END WHILE LOOP FOR CHECKING ERORR FRON CPLEX TERMINAL OR OUTPUT
	    if(quit || !optimizationSuccessful) {
		break;
	    }
	}// END FOR LOOP FOR RUNNING CPLEX

	/* take care of any text output after last command */
	temp = "";
	while(in.available() > 0) {
	    byte[] bytes = new byte[in.available()];
	    in.read(bytes);
	    line = new String(bytes);
	    temp += line;
	}
	cplexOutput += temp;

	try {
	    cplex.exitValue();
	}
	catch(IllegalThreadStateException e) {
	    /* kill CPLEX, it doesn't deserve to live */
	    cplex.destroy();
            //Destroy the resources for the farme, and return the memory to the OS, and marked as undisplayable
            frame.dispose();
	}

	if(optimizationSuccessful) {
	    /* load result with CplexOut */
	    try 
        {
            // Added by Nawzad Mardan 090325
        if(versonNumber <= 10)
            result = CplexOut.load(optFile);
        else
            result = CplexOut.newVersionload(optFile);//System.out.println("New convertings rutin must be created");

	    }
	    catch(FileNotFoundException e) {
                //Destroy the resources for the farme, and return the memory to the OS, and marked as undisplayable
                frame.dispose();
		throw new FileNotFoundException(e.getMessage());
	    }
	    catch(IOException e) {
                //Destroy the resources for the farme, and return the memory to the OS, and marked as undisplayable
                frame.dispose();
		throw new IOException("Unable to parse " + optFile + " "
				      + e.getMessage());
	    }
	}

	/* pair stdout from CPLEX with return value from CplexOut.load */
	Pair p = new Pair();
	p.left = cplexOutput;
	p.right = result;
      waiting();
	/* return this Pair */
	return p;
    }

    /**
     * Load in result from an OPT file.
     *
     * @param filename Name of OPT file.
     * @return A pair with a bogus string and the result from a previous run
     *         of CPLEX.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Pair load(String filename)
	throws FileNotFoundException, IOException
    {
	/* load result with CplexOut */
	OptimizationResult result = null;
	try {
	    result = CplexOut.load(filename);
	} catch(FileNotFoundException e) {
	    throw new FileNotFoundException(e.getMessage());
	}catch(IOException e) {
	    throw new IOException(e.getMessage());
	}

	/* pair "Optimizer not called" with return value from CplexOut.load */
	Pair p = new Pair();
	p.left = "Optimizer not called";
	p.right = result;

	/* return this Pair */
	return p;
    }

}
