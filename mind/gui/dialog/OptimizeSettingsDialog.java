/*
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
 * This class is a dialog for optimization settings.
 * It is a Singleton, which means only one instance of it can be
 * created.
 *
 * @author Daniel Campos, Johan Bengtsson
 * @version 2004-12-03
 */
package mind.gui.dialog;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import mind.gui.*;
import mind.io.*;

import java.io.*;
import mind.*;

public class OptimizeSettingsDialog
    extends JDialog
    implements UserSettingConstants
{
    private static OptimizeSettingsDialog c_instance = null;
    private Ini c_userSettings;
    private javax.swing.JTabbedPane c_tabbedPane;
    private EventHandlerClient c_ehc;
    private GUI gui = null;

    /* Creates new form OptimizeSettingsDialog */
    private OptimizeSettingsDialog(Frame parent, boolean modal,
				   Ini userSettings, EventHandlerClient ehc)
    {
	super (parent, modal);
	c_userSettings = userSettings;
	c_ehc = ehc;
	gui = GUI.getInstance();
	initComponents();
	pack ();
    }

    public static synchronized OptimizeSettingsDialog
	createInstance(Frame parent, boolean modal, Ini userSettings,
		       EventHandlerClient ehc)
    {
	if (c_instance == null)
	    c_instance = new OptimizeSettingsDialog(parent, modal,
						    userSettings, ehc);

	try {
	    String optCommands = c_instance.c_ehc.getOptimizationCommands(userSettings.OPTIMIZER);

	    c_instance.rmdTxtArea.setText(optCommands);
	    c_instance.mpsTxtArea.setText(optCommands);
	} catch (FileNotFoundException e) {
	    /* create a default command list and save/load it */
	    String newline = System.getProperty("line.separator");
            String commands;

            if (userSettings.OPTIMIZER == GlobalStringConstants.CPLEX_COMMAND_FILE)
	    commands =
		"r MPS" + newline +
		"o" + newline +
		"c p f" + newline +
		"o" + newline +
		"w OPT" + newline +
		"q" + newline;
            else
              commands = "-fmps"; // not CPLEX so it must be LP_SOLVE

	    try {
		c_instance.c_ehc.setOptimizationCommands(userSettings.OPTIMIZER, commands);
	    } catch(IOException e2) {
		JOptionPane.showMessageDialog(null, e2.getMessage());
	    }

	    try {
		String optCommands
		    = c_instance.c_ehc.getOptimizationCommands(userSettings.OPTIMIZER);

		c_instance.rmdTxtArea.setText(optCommands);
		c_instance.mpsTxtArea.setText(optCommands);
	    } catch(FileNotFoundException e2) {
		JOptionPane.showMessageDialog(null, e2.getMessage());
	    } catch(IOException e2) {
		JOptionPane.showMessageDialog(null, e2.getMessage());
	    }
	} catch (IOException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	}

	String filename = c_instance.gui.c_savedModel;
	if (filename == null)
	    filename = "Untitled";
	filename = c_instance.gui.getFilenameWithoutExtension(filename);
	String withDate =
	    c_instance.c_userSettings.getProperty(DATE_IN_MPS_FILE);
	if (withDate != null) {
	    if (withDate.equals("1")) {
		filename = c_instance.gui.getFilenameWithDate(filename);
	    }
	}

	filename = filename + ".mps";

	c_instance.rmdmpsFileName.setText(filename);

	return c_instance;
    }

    /* method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents () {

        setTitle("Optimize Settings");

	c_tabbedPane = new JTabbedPane();

	/* panel variables */
        rmdPanel = new JPanel();
        mpsPanel = new JPanel();
	optPanel = new JPanel();
	/* end panel variables */

	/* rmd variables */
	rmdTxtArea = new JTextArea(5,50);
	rmdScrollPane = new JScrollPane(rmdTxtArea);
	rmdmpsFileName = new JTextField(25);
	rmdLblOptCommands = new javax.swing.JLabel ("Optimization Commands");
	rmdLblFilename = new javax.swing.JLabel ("MPS Filename");
	rmdjPanel1 = new javax.swing.JPanel ();
	rmdbtnOptimize = new javax.swing.JButton ("Optimize");
	rmdbtnCancel = new javax.swing.JButton ("Cancel");
	rmdPanel.setLayout (new GridBagLayout ());
	rmdSeparator = new javax.swing.JSeparator();
	/* end rmd variables */

	/* mps variables */
	mpsTxtArea = new JTextArea(5,50);
	mpsScrollPane = new JScrollPane(mpsTxtArea);
	mpsmpsFileName = new JTextField(25);
	mpsLblOptCommands = new javax.swing.JLabel ("Optimization Commands");
	mpsLblFilename = new javax.swing.JLabel ("MPS Filename");
	mpsjPanel1 = new javax.swing.JPanel ();
	mpsbtnOptimize = new javax.swing.JButton ("Optimize");
	mpsbtnCancel = new javax.swing.JButton ("Cancel");
	mpsbtnBrowse = new javax.swing.JButton ("Browse");
	mpsPanel.setLayout (new GridBagLayout ());
	mpsBrowsePanel = new javax.swing.JPanel();
	mpsBrowsePanel.setLayout(new GridBagLayout());
	mpsSeparator = new javax.swing.JSeparator();
	/* end mps variables */

	/* opt variables */
	optOutputDataFileName = new JTextField(25);
	optLblFilename = new javax.swing.JLabel ("Optimizer Output Filename");
	optjPanel1 = new javax.swing.JPanel ();
	optbtnOptimize = new javax.swing.JButton ("Optimize");
	optbtnCancel = new javax.swing.JButton ("Cancel");
	optbtnBrowse = new javax.swing.JButton ("Browse");
	optPanel.setLayout (new GridBagLayout ());
	optBrowsePanel = new javax.swing.JPanel();
	optBrowsePanel.setLayout(new GridBagLayout());
	optSeparator = new javax.swing.JSeparator();
	/* end opt variables */

	GridBagConstraints constraints;
	addWindowListener (new WindowAdapter () {
	    public void windowClosing (WindowEvent evt) {
		closeDialog (null);
	    }
	});

	/* Optimize from rmd */

	/* add rmdLblOptCommands */
	constraints = new GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridy = 2;
	constraints.insets = new Insets (10, 10, 0, 0);
	constraints.anchor = GridBagConstraints.NORTHWEST;
	rmdPanel.add (rmdLblOptCommands, constraints);

	/* add textarea */
	constraints = new GridBagConstraints ();
	rmdTxtArea.setLineWrap(true);
	rmdTxtArea.setEditable(true);
	rmdTxtArea.setFont(new Font("Monospaced", Font.PLAIN, 12) );
	rmdScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	constraints.gridx = 0;
	constraints.gridy = 3;
	constraints.fill = GridBagConstraints.HORIZONTAL;
	constraints.insets = new Insets(3, 10, 0, 10);
	rmdPanel.add (rmdScrollPane, constraints);

	/* add rmdLblFilename */
	constraints = new GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridy = 4;
	constraints.insets = new Insets (10, 10, 0, 0);
	constraints.anchor = GridBagConstraints.NORTHWEST;
	rmdPanel.add (rmdLblFilename, constraints);

	/* add textfield */
	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 5;
	constraints.insets = new Insets(3, 10, 0, 5);
	constraints.anchor = GridBagConstraints.NORTHWEST;
	rmdPanel.add(rmdmpsFileName, constraints);

	/* add separator */
	constraints = new GridBagConstraints();
	constraints.gridy = 6;
	constraints.fill = GridBagConstraints.HORIZONTAL;
	constraints.insets = new Insets(46, 0, 0, 0);
	rmdPanel.add(rmdSeparator, constraints);

	/* add buttons and their actions */
	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 7;
	constraints.insets = new Insets(3, 10, 0, 5);
	constraints.anchor = GridBagConstraints.NORTHWEST;
	rmdjPanel1.setLayout (new FlowLayout (2, 5, 5));
	rmdjPanel1.add (rmdbtnOptimize);
	rmdjPanel1.add (rmdbtnCancel);
	getRootPane().setDefaultButton(rmdbtnOptimize);
	rmdbtnOptimize.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		try {
		    c_ehc.setOptimizationCommands(c_userSettings.OPTIMIZER, rmdTxtArea.getText());
    		} catch (IOException e) {
		    JOptionPane.showMessageDialog(null, e.getMessage());
		}

		String optimizer = c_userSettings.getProperty(OPTIMIZER);
		String path = null;
		if(optimizer == null) {
		    optimizer = GlobalStringConstants.OPT_NONE;
		} else if(optimizer.equals(GlobalStringConstants.OPT_CPLEX)) {
		    path = c_userSettings.getProperty(CPLEX_PATH);
                } else if(optimizer.equals(GlobalStringConstants.OPT_LPSOLVE)) {
                   path = c_userSettings.getProperty(LPSOLVE_PATH);
                }


		if(path == null){
		    JOptionPane.showMessageDialog(null, "Optimizer path not defined!\nSet path in Options");
		    return;
		}
		String file = rmdmpsFileName.getText();
		if(file != null) {
		    if(!file.equals("")) {
			gui.optimizeWithSettings(file, optimizer, path,
						 null, null);
		    } else {
			JOptionPane.showMessageDialog(null,
						      "No file selected");
			return;
		    }
		} else {
		    JOptionPane.showMessageDialog(null,
						  "No file selected");
		    return;
		}

		/* add .mps if missing */
		if (!file.toLowerCase().endsWith(".mps"))
		    file = file + ".mps";

		closeDialog(null);
	    }
	});

	rmdbtnCancel.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		closeDialog(null);
	    }
	});

	constraints = new GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridwidth = 3;
	constraints.fill = GridBagConstraints.BOTH;
	constraints.insets = new Insets (10, 10, 10, 10);
        rmdPanel.add (rmdjPanel1, constraints);
	/* End Optimize from rmd */


	/* Optimize from mps */

	/* add mpsLblOptCommands */
	constraints = new GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridy = 2;
	constraints.insets = new Insets (10, 10, 0, 0);
	constraints.anchor = GridBagConstraints.NORTHWEST;
	mpsPanel.add (mpsLblOptCommands, constraints);

	/* add textarea */
	constraints = new GridBagConstraints ();
	mpsTxtArea.setLineWrap(true);
	mpsTxtArea.setEditable(true);
	mpsTxtArea.setFont(new Font("Monospaced", Font.PLAIN, 12) );
	mpsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	constraints.gridx = 0;
	constraints.gridy = 3;
	constraints.fill = GridBagConstraints.HORIZONTAL;
	constraints.insets = new Insets(3, 10, 0, 10);
	mpsPanel.add (mpsScrollPane, constraints);

	/* add panel for mpsLblFilename, mpsmpsFileName mpsbtnBrowse */
	constraints = new GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridy = 4;
	constraints.gridwidth = 3;
	constraints.insets = new Insets (0, 0, 0, 0);
	constraints.anchor = GridBagConstraints.NORTHWEST;
	mpsPanel.add (mpsBrowsePanel, constraints);

	/* add mpsLblFilename */
	constraints = new GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridy = 0;
	constraints.insets = new Insets (10, 10, 0, 0);
	constraints.anchor = GridBagConstraints.NORTHWEST;
	mpsBrowsePanel.add (mpsLblFilename, constraints);

	/* add textfield */
	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 1;
	constraints.insets = new Insets(3, 10, 0, 5);
	constraints.anchor = GridBagConstraints.NORTHWEST;
	mpsBrowsePanel.add(mpsmpsFileName, constraints);

	/* add browser button */
	constraints = new GridBagConstraints();
	constraints.gridx = 1;
	constraints.gridy = 1;
	constraints.insets = new Insets(3, 0, 0, 5);
	constraints.anchor = GridBagConstraints.NORTHWEST;
	mpsBrowsePanel.add(mpsbtnBrowse, constraints);
	mpsbtnBrowse.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    JFileChooser openDialog
			= new JFileChooser(c_userSettings
					   .getProperty(MPS_LOAD_FOLDER));

		    SaveAsFileFilter filter
			= new SaveAsFileFilter("mps",
					       "Standard MPS file");
		    openDialog.addChoosableFileFilter(filter);

		    openDialog.showOpenDialog(new JFrame());
		    File file = openDialog.getSelectedFile();
		    if(file != null) {
			File directory = file.getParentFile();
			String directoryString = directory.getAbsolutePath();
			mpsmpsFileName.setText(file.getAbsolutePath());
			c_userSettings.setProperty(MPS_LOAD_FOLDER,
						   directoryString);
		    }
		}
	    });

	/* add separator */
	constraints = new GridBagConstraints();
	constraints.gridy = 5;
	constraints.fill = GridBagConstraints.HORIZONTAL;
	constraints.insets = new Insets(40, 0, 0, 0);
	mpsPanel.add(mpsSeparator, constraints);

	/* add buttons and their actions */
	mpsjPanel1.setLayout (new FlowLayout (2, 5, 5));
	mpsjPanel1.add (mpsbtnOptimize);
	mpsjPanel1.add (mpsbtnCancel);
	getRootPane().setDefaultButton(mpsbtnOptimize);
	mpsbtnOptimize.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    try {
			c_ehc.setOptimizationCommands(c_userSettings.OPTIMIZER, rmdTxtArea.getText());
		    } catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		    }

		    String optimizer = c_userSettings.getProperty(OPTIMIZER);
		    String path = null;
		    if(optimizer == null) {
			optimizer = GlobalStringConstants.OPT_NONE;
		    } else if(optimizer.equals(GlobalStringConstants.OPT_CPLEX)) {
			path = c_userSettings.getProperty(CPLEX_PATH);
                    } else if(optimizer.equals(GlobalStringConstants.OPT_LPSOLVE)) {
                          path = c_userSettings.getProperty(LPSOLVE_PATH);
                    }


		    if(path == null){
			JOptionPane.showMessageDialog(null, "Optimizer path not defined!\nSet path in Options");
			return;
		    }

		    String file = mpsmpsFileName.getText();
		    if(file != null) {
			if(!file.equals("")) {
			    gui.optimizeWithSettings(file, optimizer,
						     path, file, null);
			} else {
			    JOptionPane.showMessageDialog(null,
							  "No file selected");
			    return;
			}
		    } else {
			JOptionPane.showMessageDialog(null,
						      "No file selected");
			return;
		    }
		    closeDialog(null);
		}
	    });

	mpsbtnCancel.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		closeDialog(null);
	    }
	});
	constraints = new GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridy = 6;
	constraints.gridwidth = 3;
	constraints.fill = GridBagConstraints.BOTH;
	constraints.insets = new Insets (10, 10, 10, 10);
        mpsPanel.add (mpsjPanel1, constraints);
	/* End Optimize from mps */


	/* Optimize from opt */

	/* add panel for optLblFilename, optOutputDataFileName optbtnBrowse */
	constraints = new GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridy = 0;
	constraints.gridwidth = 3;
	constraints.insets = new Insets (117, 0, 0, 0);
	constraints.anchor = GridBagConstraints.NORTHWEST;
	optPanel.add (optBrowsePanel, constraints);

	/* add optLblFilename */
	constraints = new GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridy = 0;
	constraints.insets = new Insets (10, 10, 0, 0);
	constraints.anchor = GridBagConstraints.NORTHWEST;
	optBrowsePanel.add (optLblFilename, constraints);

	/* add textfield */
	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 1;
	constraints.insets = new Insets(3, 10, 0, 5);
	constraints.anchor = GridBagConstraints.NORTHWEST;
	optBrowsePanel.add(optOutputDataFileName, constraints);

	/* add browser button */
	constraints = new GridBagConstraints();
	constraints.gridx = 1;
	constraints.gridy = 1;
	constraints.insets = new Insets(3, 0, 0, 16);
	constraints.anchor = GridBagConstraints.NORTHWEST;
	optBrowsePanel.add(optbtnBrowse, constraints);
	optbtnBrowse.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    JFileChooser openDialog
			= new JFileChooser(c_userSettings
					   .getProperty(OPT_LOAD_FOLDER));

		    SaveAsFileFilter filter
			= new SaveAsFileFilter("opt",
					       "Optimizer output file");
		    openDialog.addChoosableFileFilter(filter);

		    openDialog.showOpenDialog(new JFrame());
		    File file = openDialog.getSelectedFile();
		    if(file != null) {
			File directory = file.getParentFile();
			String directoryString = directory.getAbsolutePath();
			optOutputDataFileName.setText(file.getAbsolutePath());
			c_userSettings.setProperty(OPT_LOAD_FOLDER,
						   directoryString);
		    }
		}
	    });

	/* add separator */
	constraints = new GridBagConstraints();
	constraints.gridy = 1;
	constraints.gridwidth = 3;
	constraints.fill = GridBagConstraints.HORIZONTAL;
	constraints.insets = new Insets(40,0, 0, 0);
	optPanel.add(optSeparator, constraints);

	/* add buttons and their actions */
	optjPanel1.setLayout (new FlowLayout (2, 5, 5));
	optjPanel1.add (optbtnOptimize);
	optjPanel1.add (optbtnCancel);
	getRootPane().setDefaultButton(optbtnOptimize);
	optbtnOptimize.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    try {
			c_ehc.setOptimizationCommands(c_userSettings.OPTIMIZER, rmdTxtArea.getText());
		    } catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		    }
		    String optimizer = c_userSettings.getProperty(OPTIMIZER);
		    String path = null;
		    if(optimizer == null) {
			optimizer = GlobalStringConstants.OPT_NONE;
		    } else if(optimizer.equals(GlobalStringConstants.OPT_CPLEX)) {
			path = c_userSettings.getProperty(CPLEX_PATH);
		    } else if (optimizer.equals(GlobalStringConstants.OPT_LPSOLVE)) {
                        path = c_userSettings.getProperty(LPSOLVE_PATH);
                    }

		    if(path == null){
			JOptionPane.showMessageDialog(null, "Optimizer path not defined!\nSet path in Options");
			return;
		    }

		    String file = optOutputDataFileName.getText();
		    if(file != null) {
			if(!file.equals("")) {
			    gui.optimizeWithSettings(file, optimizer,
						     path, null, file);
			} else {
			    JOptionPane.showMessageDialog(null,
							  "No file selected");
			    return;
			}
		    } else {
			JOptionPane.showMessageDialog(null,
						      "No file selected");
			return;
		    }
		    closeDialog(null);
		}
	    });

	optbtnCancel.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		closeDialog(null);
	    }
	});
	constraints = new GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridy = 2;
	constraints.gridwidth = 3;
	constraints.fill = GridBagConstraints.BOTH;
	constraints.insets = new Insets (10, 10, 10, 10);
        optPanel.add (optjPanel1, constraints);
	/* End Optimize from opt */

	/* Add the panels to the tabbed pane */
        c_tabbedPane.add("From rmd", rmdPanel);
	c_tabbedPane.add("From mps", mpsPanel);
	c_tabbedPane.add("From opt", optPanel);
	getContentPane().add(c_tabbedPane);

    }

    /* Closes the dialog */
    private void closeDialog(WindowEvent evt) {
	setVisible (false);
	dispose ();
    }

    /* rmd variables declaration */
    private JPanel rmdPanel;
    private JLabel rmdLblOptCommands;
    private JLabel rmdLblFilename;
    private JPanel rmdjPanel1;
    private JButton rmdbtnOptimize;
    private JButton rmdbtnCancel;
    private JTextArea rmdTxtArea;
    private JScrollPane rmdScrollPane;
    private JTextField rmdmpsFileName;
    private JSeparator rmdSeparator;
    /* End of rmd variables declaration */

    /* mps variables declaration */
    private JPanel mpsPanel;
    private JPanel mpsBrowsePanel;
    private JLabel mpsLblOptCommands;
    private JLabel mpsLblFilename;
    private JPanel mpsjPanel1;
    private JButton mpsbtnOptimize;
    private JButton mpsbtnCancel;
    private JButton mpsbtnBrowse;
    private JTextArea mpsTxtArea;
    private JScrollPane mpsScrollPane;
    private JTextField mpsmpsFileName;
    private JSeparator mpsSeparator;
    /* End of mps variables declaration */

    /* opt variables declaration */
    private JPanel optPanel;
    private JPanel optBrowsePanel;
    private JLabel optLblFilename;
    private JPanel optjPanel1;
    private JButton optbtnOptimize;
    private JButton optbtnCancel;
    private JButton optbtnBrowse;
    private JTextField optOutputDataFileName;
    private JSeparator optSeparator;
    /* End of opt variables declaration */
}
