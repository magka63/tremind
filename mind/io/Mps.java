/*
 * Copyright 2001:
 * Peter Andersson <petan117@student.liu.se>
 * Martin Hagman <marha189@student.liu.se>
 * Henrik Norin <henno776@student.liu.se>
 * Anna Stjerneby <annst566@student.liu.se>
 * Tim Terleg�rd <timte878@student.liu.se>
 * Johan Trygg <johtr599@student.liu.se>
 * Peter �strand <petas096@student.liu.se>
 * Copyright 2002:
 * Urban Liljedahl <lilje@sm.luth.se>
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

import java.util.Hashtable;
import java.util.TreeSet;
import java.util.Vector;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.event.WindowEvent;


import mind.model.*;
import mind.gui.*;

/**
 * Handles save/load a model to/from a mps file.
 *
 * @author Henrik Norin
 * @author Johan Trygg
 * @version 2001-06-26
 */

public class Mps
    implements ModelFileFormat
{
    // the space from column 0 to the variables field
    private final String SPACE_VARIABLE = "    ";
    // the minimum space between variable and equation field
    private final int SPACE_EQUATION = 3;
    // the minimum space between equation and coefficient field
    private final int SPACE_COEFF = 4;
    // space between  'MARKER' and 'INTORG'
    private final String SPACE_MARKER = "         ";
    // An equation control used during dev. process 2002-03-11
    private EquationControl myEquationControl;
    // A frame that show information about what is going on
    private  JFrame frame = null;
    private JLabel label = null;
    private JButton btnQuit = null;
    private JPanel upperpanel=null;
    private JPanel lowerpanel=null;
    private JPanel mainpanel=null;
    private String exitMessage=null;
    /**
     * the main constructor
     */
    public Mps()
    {
    }
    /**
     * Constructor used for test purpose
     * @version 2002-03-11
     */
    public Mps(EquationControl e){
	myEquationControl = e;
    }

    /**
     * Gets the file formats extension.
     * @return The extension as a string.
     */
    public static String getExtension()
    {
	return "mps";
    }

    /**
     * Gets the file formats description.
     * @return The description as a string.
     */
    public static String getDescription()
    {
	return "MPS - optimizable file";
    }
    
    /**
     * Saves the model in the mps format.
     * @param model The model to be saved.
     * @param filename The file to save to.
     * @throws ModelException If unable to optimize
     * @throws IOException If unable to write to file
     */
    public void save(Model model, File filename)
	throws IOException, FileInteractionException
    {
	EquationControl equationControl = null;
        initComponents();
        label.setText("Please wait.....generating and controlling the equations");
	if( myEquationControl == null ){
	    try {
		equationControl = model.getEquationControl();
               
	    }
	    catch (ModelException e) {
                frame.setVisible(false);
                frame.dispose();
		throw new FileInteractionException(e.getMessage());
                 
	    }
	}
	else{
	    equationControl = myEquationControl;//dev. 2002-03-11
	}
        label.setText("Information: Please wait... Creating MPS File      ");
        //initComponents();
	// Sort the equations into our three categories
	Vector equations = equationControl.getAllEquations();
	Vector rhs = equationControl.getAllRHS();
	Vector bounds = equationControl.getAllBounds();

	// Generate rows for all Equations and their variables
	// and sort them after the variable IDs
	Vector variables = null;
	Variable variable = null;
	//	Vector columnData = new Vector(0);
	Equation equation;
	Hashtable allVariables = new Hashtable(0);
	Vector variablesSorted;
	String[] eqElement;
	Vector varElement;
	int maxVariableLength = 0;
	int maxEquationLength = 0;
	String variableID;
	String equationID;
	String coeff;
	int mark = 1;
       
	/*
	 * Creates a hashtable with all variables, equations and
	 * coefficients that will be in the columns section.
	 * The hashtable has the variable name as key
	 * As value we have a Vector that contains
	 * String[] { equation, coeff } for every equation this
	 * variable (the key) exist in. So
	 * (key, value) = (variableID, Vector(String[equationID][coeff],
	 * String[equatioinID][coeff]...))
	 */
	// iterate through all equations
	// System.out.println("Iterate through equations");

	for (int i = 0; i < equations.size(); i++) {
	    equation = (Equation) equations.get(i);
	    equationID = equation.getID();
	    variables = equation.getAllVariables();
	    // we want to store the longest equation name length
	    if (equationID.length() > maxEquationLength)
		maxEquationLength = equationID.length();

	    // iterate through all variables in current equation
	    for (int j = 0; j < variables.size(); j++) {
		variable = (Variable) variables.get(j);
		variableID = variable.toString();
		coeff = String.valueOf(variable.getCoefficient());
		// we want to store the longest variable name length
		if (variableID.length() > maxVariableLength)
		    maxVariableLength = variableID.length();

		eqElement = new String[] { equationID, coeff };
		//2002-09-16
		//System.out.println(eqElement[0] + "  " + eqElement[1]);
		//System.out.println("variable ID: " + variableID);

		if (allVariables.containsKey(variableID))
		    ((Vector) ((Vector) allVariables.get(variableID)).
			get(1)).addElement(eqElement);
		else {
		    varElement = new Vector(0);
		    varElement.addElement(new Boolean(variable.isInteger()));
		    varElement.addElement(new Vector(0));
		    ((Vector) varElement.get(1)).addElement(eqElement);
		    allVariables.put(variableID, varElement);
		}
	    }
	}

	// Get a writer for writing to the file
	BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

	// Write top info
    // Addef by Nawzad Mardan 20100222
    String versionName =  mind.GlobalStringConstants.reMIND_version;
	writer.write("NAME          reMIND version "+versionName );
	writer.newLine();

	// Write all our rows (or equations)
	writer.write("ROWS");
	writer.newLine();
	Equation eq;
	for (int i = 0 ; i < equations.size() ; i++) {
	    eq = (Equation)equations.elementAt(i);

	    writer.write(" " + eq.getOperator());
	    writer.write("  " + eq.getID());
	    writer.newLine();
	}

	// Write all our columns (or variables)
	writer.write("COLUMNS");
	writer.newLine();

	String row;
	Vector eqs;
	String marker;
	variablesSorted = new Vector(new TreeSet(allVariables.keySet()));
	// iterate through all variables that should be written to the file
	for (int i = 0; i < variablesSorted.size(); i++) {
	    variableID = (String) variablesSorted.get(i);
	    varElement = (Vector) allVariables.get(variableID);
	    //System.out.println("var = " + varElement);
	    // write a marker line meaning that this is an integer only
	    if (((Boolean) varElement.get(0)).booleanValue()) {
		//System.out.println("booleanvalue true");
		marker = "MARK" + (mark++);
		writer.write(SPACE_VARIABLE + marker +
			     spaces(SPACE_EQUATION + maxVariableLength -
				    marker.length()) + "'MARKER'" +
			     SPACE_MARKER + "'INTORG'");
		writer.newLine();
	    }

	    // we have a variable, now we get every { equation, coeff }
	    // belonging to that variable. they are found by the value
	    // of the hashtable (the value was a vector)
	    eqs = (Vector) varElement.get(1);
	    for (int j = 0; j < eqs.size(); j++) {
		equationID = ((String[]) eqs.get(j))[0];
		coeff = ((String[]) eqs.get(j))[1];
		row = SPACE_VARIABLE + variableID +
		    spaces(SPACE_EQUATION + maxVariableLength -
			   variableID.length()) + equationID +
		    spaces(SPACE_COEFF + maxEquationLength -
			   equationID.length()) + coeff;
		writer.write(row);
		writer.newLine();
	    }

	    // write an end marker meaning end of integer only declaration
	    if (((Boolean) varElement.get(0)).booleanValue()) {
		marker = "MARK" + (mark++);
		writer.write(SPACE_VARIABLE + marker +
			     spaces(SPACE_EQUATION + maxVariableLength -
				    marker.length()) + "'MARKER'" +
			     SPACE_MARKER + "'INTEND'");
		writer.newLine();
	    }
	}

	// Write all our right hand sides
	writer.write("RHS");
	writer.newLine();
	for ( int i = 0 ; i < rhs.size() ; i++ ) {
	    writer.write( "    " + "RHS" + "       " );

	    String id = ((Equation)rhs.get(i)).getID();
	    float value = ((Equation)rhs.get(i)).getRHS();
	    String theSpace = spaces(maxEquationLength + 3);

	    writer.write( id );
	    //writer.write( "          ".substring( id.length() ) );
	    writer.write(theSpace.substring( id.length() ) );
	    writer.write( Float.toString(value) );
	    writer.newLine();
	}

	// Write all our bounds
	writer.write("BOUNDS");
	writer.newLine();
	for (int i = 0 ; i < bounds.size() ; i++) {

		Equation bound = (Equation)bounds.get(i);
		Variable var = (Variable)bound.getAllVariables().get(0);

	    writer.write(" " + bound.getOperator() + " ");
	    writer.write("BOUNDS    ");
	    writer.write(var.toString());
	    writer.write("          ".substring( var.toString().length()));
	    writer.write(Float.toString(bound.getRHS()));
		writer.newLine();
	}

	writer.write("ENDATA");
	writer.close();
        waiting();
    }

    /**
     * Saves the model in the mps format.
     * @param model The model to be saved.
     * @param filename The file to save to.
     * @throws ModelException If unable to optimize
     * @throws IOException If unable to write to file
     */
    public void save(Model model, GraphModel graphModel, File filename)
	throws IOException, FileInteractionException
    {
	save(model, filename);
    }
  
    // Nawzad Mardan lagt till 2007-07-01
  /**
   * method is called from the save method to
     * initialize the form.
     * A Dialog that shows MPS file is creating
     */
    private void initComponents () 
    {
      exitMessage ="You requested to exit this application.\n" +
      "The current model is not saved and will be destroyed\n " +
       "if you exit. Do you want to exit without saving the\n" +
       "current model?";
     frame = new JFrame("reMIND: Please wait... Creating MPS File");
     label = new JLabel("Information: Please wait... Creating MPS File");
     btnQuit = new JButton("Quit");
     upperpanel = new JPanel();
     lowerpanel = new JPanel();
     mainpanel = new JPanel();
     upperpanel.add(label);
     lowerpanel.add(btnQuit);
     mainpanel.add(upperpanel);
     mainpanel.add(lowerpanel);
     frame.getContentPane().add(mainpanel);
     frame.addWindowListener(new WindowAdapter() 
     {
      public void windowClosing(WindowEvent evt)
        {
        int  select = JOptionPane.showConfirmDialog(frame, exitMessage,"Confirm dialog",JOptionPane.YES_NO_OPTION);
        if (select == JOptionPane.YES_OPTION)
            {
            frame.setVisible(false);
            frame.dispose();
             System.exit(0);
            }
        }
     });

     btnQuit.addActionListener(new ActionListener()
       {
       public void actionPerformed(ActionEvent e)
         {
         int  selection = JOptionPane.showConfirmDialog(frame, exitMessage,"Confirm dialog",JOptionPane.YES_NO_OPTION);
         if (selection == JOptionPane.YES_OPTION)
            {   
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
    
    /* method is called from the save method to
     * try to sleep 1 second for user's sake to have time see the information in the frame
     * A Dialog that shows MPS file is creating
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
    /**
     * It is not possible to load an Mps file.
     * @param model
     * @param filename
     */
    public void load(Model model, File filename)
	throws IOException
    {
    }

    private String spaces(int spaces)
    {
	String str = "";
	for (int i = 0; i < spaces; i++)
	    str += " ";
	return str;
    }
    
    
}





