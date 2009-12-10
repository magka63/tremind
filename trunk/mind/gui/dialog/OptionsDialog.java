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
 * Copyright 2004
 * Johan Bengtgsson <johbe496@student.liu.se>
 * Daniel Campos <danca226@student.liu.se>
 * Martin Fagerfjäll <marfa233@student.liu.se>
 * Daniel Ferm <danfe666@student.liu.se>
 * Able Mahari <ablma616@student.liu.se>
 * Andreas Remar <andre063@student.liu.se>
 * Haider Shareef <haish292@student.liu.se>
 *
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

/**
 * This class is a dialog for options. It is a Singleton, which means
 * only one instance of it can be created.
 *
 * @author Tim Terlegård
 * @author Johan Bengtsson
 * @version 2004-12-01
 */
package mind.gui.dialog;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import mind.gui.*;
import mind.io.*;
import java.io.File;

public class OptionsDialog
    extends JDialog
    implements UserSettingConstants, mind.GlobalStringConstants
{
    private static OptionsDialog c_instance = null;
    private Ini c_userSettings;
    private javax.swing.JTabbedPane c_tabbedPane;

    /** Creates new form OptionsDialog */
    private OptionsDialog(Frame parent, boolean modal,
			  Ini userSettings)
    {
	super (parent, modal);
	c_userSettings = userSettings;
	initComponents();
	pack ();
    }

    public static synchronized OptionsDialog
	createInstance(Frame parent, boolean modal, Ini userSettings)
    {
	if (c_instance == null)
	    c_instance = new OptionsDialog(parent, modal, userSettings);

	return c_instance;
    }

    /* method is called from within the constructor to
     * initialize the form.
     * Extended 2004 by PUM16
     * Extended 2007 by PUM5
     */
    private void initComponents () {
        setTitle("Options");
	c_tabbedPane = new JTabbedPane();
    mainPanel = new JPanel();
    optPanel = new JPanel();
	mainPanel.setLayout(new GridBagLayout());
	optPanel.setLayout(new GridBagLayout());

	/*For mainPanel*/
	chkDateOutputFile = new javax.swing.JCheckBox();
	chkDateMPSFile = new javax.swing.JCheckBox();
	/*Added by PUM5 2007-12-06*/
	chkDateXMLFile = new javax.swing.JCheckBox();
	//End PUM5
	chkConsoleOutput = new javax.swing.JCheckBox();
	chkAutosave = new javax.swing.JCheckBox();
	sepButtons = new javax.swing.JSeparator();
	sepButtons2 = new javax.swing.JSeparator();
	txtAutosave = new JTextField(c_userSettings.getProperty(AUTOSAVE), 3);
	jLabel1 = new javax.swing.JLabel();
	jPanel1 = new javax.swing.JPanel();
	jPanel2 = new javax.swing.JPanel();
	btnOK = new javax.swing.JButton();
	btnCancel = new javax.swing.JButton();


	/*For optPanel*/
	optChooser = new JComboBox();
	optChooser.addItem(OPT_NONE);
	optChooser.addItem(OPT_CPLEX);
        optChooser.addItem(OPT_LPSOLVE);
	optChooser.addItemListener(new ItemListener() {
		public void itemStateChanged(ItemEvent evt) {
		    optChooserChanged(evt);
		}
	    });
	String optimizer = c_userSettings.getProperty(OPTIMIZER);
	if (optimizer == null) {
	    optChooser.setSelectedItem(OPT_NONE);
	} else {
	    if (optimizer.equals(OPT_CPLEX))
		optChooser.setSelectedItem(OPT_CPLEX);
	    else if (optimizer.equals(OPT_LPSOLVE))
                optChooser.setSelectedItem(OPT_LPSOLVE);
            else
		optChooser.setSelectedItem(OPT_NONE);
	}

	lblOptimizer = new javax.swing.JLabel("Optimizer");
	lblOptimizerPath = new javax.swing.JLabel("Optimizer path");
	txtOptimizerPath = new javax.swing.JTextField(20);
	btnBrowse = new javax.swing.JButton();
	btnOK2 = new javax.swing.JButton();
	btnCancel2 = new javax.swing.JButton();

	String path = null;
	if(optimizer != null && optimizer.equals(OPT_CPLEX)) {
	    path = c_userSettings.getProperty(CPLEX_PATH);
	}
        if(optimizer != null && optimizer.equals(OPT_LPSOLVE)) {
            path = c_userSettings.getProperty(LPSOLVE_PATH);
        }
	if(path != null) {
	    txtOptimizerPath.setText(path);
	    txtOptimizerPath.setEditable(true);
	} else {
	    txtOptimizerPath.setEditable(false);
	}

	GridBagConstraints constraints;
	addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent evt) {
		closeDialog(null);
	    }
	});

	/*Get current values from settings.ini for checkboxes*/
	if(c_userSettings.getProperty(AUTOSAVE_STATE) != null)
	    chkAutosave.setSelected(c_userSettings.getProperty(AUTOSAVE_STATE).equals("1"));
	if(c_userSettings.getProperty(DATE_IN_OPT_FILE) != null)
	    chkDateOutputFile.setSelected(c_userSettings.getProperty(DATE_IN_OPT_FILE).equals("1"));

	if(c_userSettings.getProperty(DATE_IN_MPS_FILE) != null)
	    chkDateMPSFile.setSelected(c_userSettings.getProperty(DATE_IN_MPS_FILE).equals("1"));
	/*Added by PUM5 2007-12-06*/
	if(c_userSettings.getProperty(DATE_IN_XML_FILE) != null)
	    chkDateXMLFile.setSelected(c_userSettings.getProperty(DATE_IN_XML_FILE).equals("1"));
	//End PUM5
	if(c_userSettings.getProperty(SHOW_OPT_OUT) != null)
	    chkConsoleOutput.setSelected(c_userSettings.getProperty(SHOW_OPT_OUT).equals("1"));

	/*For mainPanel*/
	chkDateOutputFile.setText ("Include current date in the result filename ");
	constraints = new GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridy = 0;
	constraints.insets = new Insets (10, 10, 0, 0);
	constraints.anchor = GridBagConstraints.NORTHWEST;
	mainPanel.add (chkDateOutputFile, constraints);

	chkDateMPSFile.setText ("Include current date in the MPS filename ");
	constraints = new GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridy = 1;
	constraints.insets = new Insets (10, 10, 0, 0);
	constraints.anchor = GridBagConstraints.NORTHWEST;
	mainPanel.add (chkDateMPSFile, constraints);

	chkDateXMLFile.setText ("Include current date in the XML filename ");
	constraints = new GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridy = 2;
	constraints.insets = new Insets (10, 10, 0, 0);
	constraints.anchor = GridBagConstraints.NORTHWEST;
	mainPanel.add (chkDateXMLFile, constraints);
	
	chkConsoleOutput.setText ("Show optimizer console output ");
	constraints = new GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridy = 3;
	constraints.insets = new Insets (10, 10, 0, 0);
	constraints.anchor = GridBagConstraints.NORTHWEST;
	mainPanel.add (chkConsoleOutput, constraints);

	constraints = new GridBagConstraints ();
	txtAutosave.setToolTipText("Number of minutes between autosaves");
	constraints.gridx = 1;
	constraints.gridy = 4;
	constraints.fill = GridBagConstraints.HORIZONTAL;
	constraints.insets = new Insets(10, 0, 0, 10);
	mainPanel.add (txtAutosave, constraints);

	chkAutosave.setText ("Autosave every ");
	constraints = new GridBagConstraints ();
	constraints.gridx = 0;
	constraints.insets = new Insets (10, 10, 0, 0);
	constraints.anchor = GridBagConstraints.NORTHWEST;
	mainPanel.add (chkAutosave, constraints);

	JLabel text =
	    new JLabel("Width between rightmost node and border:");
	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 5;
	constraints.insets = new Insets(10, 10, 0, 5);
	constraints.anchor = GridBagConstraints.NORTHWEST;
	mainPanel.add(text, constraints);

	txtAreaIncWidth =
	    new JTextField(c_userSettings.getProperty(AREA_INC_WIDTH), 5);
	txtAreaIncWidth.setToolTipText("Width between rightmost node and border");
	constraints = new GridBagConstraints();
	constraints.gridx = 1;
	constraints.gridy = 5;
	constraints.insets = new Insets(10, 0, 0, 10);
	mainPanel.add(txtAreaIncWidth, constraints);

	text =	    new JLabel("Height between lower node and border:");
	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 6;
	constraints.insets = new Insets(10, 10, 0, 5);
	constraints.anchor = GridBagConstraints.NORTHWEST;
	mainPanel.add(text, constraints);

	txtAreaIncHeight =
	    new JTextField(c_userSettings.getProperty(AREA_INC_HEIGHT), 5);
	txtAreaIncHeight.setToolTipText("Height between lower node and border");
	constraints = new GridBagConstraints();
	constraints.gridx = 1;
	constraints.gridy = 6;
	constraints.insets = new Insets(10, 0, 0, 10);
	mainPanel.add(txtAreaIncHeight, constraints);

	constraints = new GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridwidth = 4;
	constraints.fill = GridBagConstraints.HORIZONTAL;
	constraints.insets = new Insets (40, 0, 0, 0);
	mainPanel.add (sepButtons, constraints);

	jLabel1.setText ("minutes.");

	constraints = new GridBagConstraints ();
	constraints.gridx = 2;
	constraints.gridy = 4;
	constraints.insets = new Insets (10, 0, 0, 34);
	mainPanel.add (jLabel1, constraints);

	jPanel1.setLayout (new FlowLayout (2, 5, 5));

	btnOK.setText ("OK");
	getRootPane().setDefaultButton(btnOK);
	btnOK.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		buttonOkPressed(evt);
	    }
	});

	jPanel1.add (btnOK);

	btnCancel.setText ("Cancel");
	btnCancel.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		closeDialog(null);
	    }
	});

	jPanel1.add (btnCancel);


	constraints = new GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridwidth = 3;
	constraints.fill = GridBagConstraints.BOTH;
	constraints.anchor = GridBagConstraints.SOUTH;
	constraints.insets = new Insets (10, 10, 10, 10);
        mainPanel.add (jPanel1, constraints);

	/*For optPanel*/

	/* add lblOptimizer */
	constraints = new GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridy = 0;
	constraints.insets = new Insets (10, 10, 0, 0);
	constraints.anchor = GridBagConstraints.NORTHWEST;
	optPanel.add (lblOptimizer, constraints);

	/* add drop-menu */
	constraints = new GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridy = 1;
    constraints.insets = new Insets (10, 10, 0, 0);
	constraints.anchor = GridBagConstraints.NORTHWEST;
	optPanel.add (optChooser, constraints);

	constraints = new GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridy = 2;
	constraints.anchor = GridBagConstraints.NORTHWEST;
	constraints.insets = new Insets (10, 10, 0, 0);
    optPanel.add (lblOptimizerPath, constraints);

	constraints = new GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridy = 3;
	constraints.anchor = GridBagConstraints.NORTHWEST;
	constraints.insets = new Insets (10, 10, 0, 0);
    optPanel.add (txtOptimizerPath, constraints);

	/*add browse button*/
	btnBrowse.setText("Browse");
	constraints = new GridBagConstraints ();
	constraints.gridx = 1;
	constraints.gridy = 3;
	constraints.anchor = GridBagConstraints.NORTHWEST;
	constraints.insets = new Insets (10, 10, 0, 104);
	optPanel.add(btnBrowse, constraints);
	btnBrowse.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    JFileChooser openDialog = new JFileChooser();
		    openDialog.showOpenDialog(new JFrame());
		    File file = openDialog.getSelectedFile();
		    if(file != null)
			txtOptimizerPath.setText(file.getAbsolutePath());
		}
	    });

	constraints = new GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridwidth = 3;
	constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.insets = new Insets (113, 0, 0, 0);
	optPanel.add (sepButtons2, constraints);


	/*Panel containing the OK and Cancel button*/
	jPanel2.setLayout (new FlowLayout (2, 5, 5));

	btnOK2.setText ("OK");
	getRootPane().setDefaultButton(btnOK2);
	btnOK2.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		buttonOkPressed(evt);
	    }
	});
	jPanel2.add (btnOK2);

	btnCancel2.setText ("Cancel");
	btnCancel2.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		closeDialog(null);
	    }
	});
	jPanel2.add (btnCancel2);

	constraints = new GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridwidth = 3;
	constraints.fill = GridBagConstraints.BOTH;
	constraints.anchor = GridBagConstraints.SOUTH;
	constraints.insets = new Insets (10, 10, 10, 10);
        optPanel.add (jPanel2, constraints);


	/*Add panels to tabbed Pane*/
	getContentPane().add(c_tabbedPane);
	c_tabbedPane.add("General", mainPanel);
        c_tabbedPane.add("Optimizer", optPanel);

    }

    private void optChooserChanged(ItemEvent evt)
    {
	if (evt.getStateChange() == ItemEvent.SELECTED)
	{
	    if(txtOptimizerPath == null)
		return;

	    String optimizer = (String)optChooser.getSelectedItem();
	    if(optimizer == null)
		return;

	    if(optimizer.equals(OPT_NONE)) {
		txtOptimizerPath.setText("");
		txtOptimizerPath.setEditable(false);
	    } else if(optimizer.equals(OPT_CPLEX)) {
		txtOptimizerPath.setText(c_userSettings
					 .getProperty(CPLEX_PATH));
		txtOptimizerPath.setEditable(true);
            } else if(optimizer.equals(OPT_LPSOLVE)) {
                txtOptimizerPath.setText(c_userSettings
                                           .getProperty(LPSOLVE_PATH));
                txtOptimizerPath.setEditable(true);
              }

	}
    }

    private void buttonOkPressed(ActionEvent evt)
    {
	String autosaveTime;
	int autosaveTimeNumber;
	String autosaveState = "0"; //autosave disabled
	String dateOutputFile = "0"; //disabled
	String dateMPSFile = "0"; //disabled
	String consoleOutput = "0"; //disabled
	/*Added by PUM5 2007-12-06*/
	String dateXMLFile ="0";	//disabled, apparently :)
	//End PUM5
	
	if (chkAutosave.isSelected()) {
	    autosaveTime = txtAutosave.getText();
	    autosaveTimeNumber = Integer.parseInt(autosaveTime);
	    autosaveState = "1"; //autosave enabled
	    if (autosaveTimeNumber > 0)
		c_userSettings.setProperty(AUTOSAVE, autosaveTime);
	}
	//else
	//    c_userSettings.remove(AUTOSAVE)

	/*Save autosave state*/
	c_userSettings.setProperty(AUTOSAVE_STATE,autosaveState);

	/*Save dateOutdataFile setting*/
	if (chkDateOutputFile.isSelected() == true)
	    dateOutputFile = "1";
	c_userSettings.setProperty(DATE_IN_OPT_FILE, dateOutputFile);

	/*Save dateMPSFile setting*/
	if (chkDateMPSFile.isSelected() == true)
	    dateMPSFile = "1";
	c_userSettings.setProperty(DATE_IN_MPS_FILE, dateMPSFile);
	
	/*Save dateXMLFile setting
	 * Added by PUM5 2007-12-06*/
	if (chkDateXMLFile.isSelected() == true)
	    dateXMLFile = "1";
	c_userSettings.setProperty(DATE_IN_XML_FILE, dateXMLFile);

	/*Save SHOW_OPT_OUT setting*/
	if (chkConsoleOutput.isSelected() == true)
	    consoleOutput = "1";
	c_userSettings.setProperty(SHOW_OPT_OUT, consoleOutput);


	String optimizer = (String)optChooser.getSelectedItem();
	/* Save OPTIMIZER */
	c_userSettings.setProperty(OPTIMIZER,optimizer);

	/*Save optimizer PATH*/
	if(optimizer != null && optimizer.equals(OPT_CPLEX)) {
	    c_userSettings.setProperty(CPLEX_PATH,txtOptimizerPath.getText());
	}
        if(optimizer != null && optimizer.equals(OPT_LPSOLVE)) {
            c_userSettings.setProperty(LPSOLVE_PATH,txtOptimizerPath.getText());
        }

	String text = txtAreaIncWidth.getText();
	try {
	    Integer.parseInt(text);
	    // if no exception was thrown, the textfield included
	    // a valid number
	    c_userSettings.setProperty(AREA_INC_WIDTH, text);
	}
	catch (NumberFormatException e) {
	}

	text = txtAreaIncHeight.getText();
	try {
	    Integer.parseInt(text);
	    // if no exception was thrown, the textfield included
	    // a valid number
	    c_userSettings.setProperty(AREA_INC_HEIGHT, text);
	}
	catch (NumberFormatException e) {
	}

	closeDialog(null);
    }

    /** Closes the dialog */
    private void closeDialog(WindowEvent evt) {
	setVisible (false);
	dispose ();
    }

    // Variables declaration
    private JPanel mainPanel;
    private JPanel optPanel;
    private JCheckBox chkDateOutputFile;
    private JCheckBox chkDateMPSFile;
    /*Added by PUM5 2007-12-06*/
    private JCheckBox chkDateXMLFile;
    //End PUM5
    private JCheckBox chkConsoleOutput;
    private JCheckBox chkAutosave;
    private JSeparator sepButtons;
    private JSeparator sepButtons2;
    private JTextField txtAutosave;
    private JLabel jLabel1;
    private JLabel lblOptimizer;
    private JLabel lblOptimizerPath;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JButton btnOK;
    private JButton btnCancel;
    private JButton btnOK2;
    private JButton btnCancel2;
    private JButton btnBrowse;
    private JTextField txtAreaIncWidth;
    private JTextField txtAreaIncHeight;
    private JTextField txtOptimizerPath;
    private JComboBox optChooser;
   // End of variables declaration
}
