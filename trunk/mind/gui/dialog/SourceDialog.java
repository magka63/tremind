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
 * Copyright 2007:
 * Per Fredriksson <perfr775@student.liu.se>
 * David Karlslätt <davka417@student.liu.se>
 * Tor Knutsson	<torkn754@student.liu.se>
 * Daniel Källming <danka053@student.liu.se>
 * Ted Palmgren <tedpa175@student.liu.se>
 * Freddie Pintar <frepi150@student.liu.se>
 * Mårten Thurén <marth852@student.liu.se>
 *
 * Copyright 2010
 * Nawzad Mardan <nawzad.mardan@liu.se>
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
package mind.gui.dialog;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import javax.swing.event.*;
import javax.swing.text.*;

import mind.gui.*;
import mind.model.*;
import mind.model.function.*;
import mind.EventHandlerClient;

/*
 * SourceDialog.java
 *
 * Created on den 25 april 2001, 13:02
 */

/**
 *
 * @author Tim Terlegård
 * @author Johan Trygg
 * @author Urban Liljedahl
 * @author  Per Fredriksson
 * @author 	Tor Knutsson
 * @version 2007-12-10
 */

public class SourceDialog extends mind.gui.dialog.FunctionDialog {
	private ID c_nodeID;

	private String c_functionLabel;

	private Vector c_resources;

	private int c_timesteplevels; //Number of timesteplevels

	private EventHandlerClient c_eventhandler;

	private GUI c_gui;

	private Source c_function;

	private Timesteplevel tsl[];

	private int c_timestep; //Active timestep

	private MyTableModel c_myTableModel;

	//private Source c_backupFunction;

	//turn on debug mode for MyTableModel
	private final boolean DEBUG = false;

	private javax.swing.JLabel lblDescription;

	private javax.swing.JSeparator sep1;

	private javax.swing.JSeparator sep2;

	private javax.swing.JPanel pnlTimestep;

	private javax.swing.JLabel lblTSL[];

	private SpinButton spinTSL[];

	private javax.swing.JLabel lblCostPerUnit;

	private javax.swing.JTextField txtCostPerUnit;

	private javax.swing.JPanel pnlCost;

//	private javax.swing.JPanel pnlCopyPaste;

	//private javax.swing.JButton btnCopyFrom;

	//private javax.swing.JButton btnCopy;

	//private javax.swing.JButton btnPasteTo;

//	private javax.swing.JButton btnPaste;

	private javax.swing.JLabel lblTimestep;

	private javax.swing.JPanel pnlLabel;

	private javax.swing.JLabel lblLabel;

	private javax.swing.JTextField txtLabel;

	private javax.swing.JSeparator sep3;

	private javax.swing.JPanel pnlButtons;

	private javax.swing.JButton btnOk;

	private javax.swing.JButton btnCancel;

	private javax.swing.JPanel pnlResource;

	private javax.swing.JLabel lblTypeResource;

	private javax.swing.JList listResource;

	private javax.swing.JButton btnNewResource;

	//2003-12-10
	private javax.swing.JTable tblCostPerUnit;
    // Added by Nawzad Mardan 20100209
    private int c_maxTimeSteps = 1;
    private String c_currentTimestep;

	/** Creates new form SourceDialog */
	public SourceDialog(javax.swing.JDialog parent, boolean modal, ID nodeID,
			NodeFunction function, GUI gui) {
		super(parent, modal);
		c_nodeID = nodeID;
		c_gui = gui;
		c_eventhandler = gui.getEventHandlerClient();
		c_function = (Source) function;

		//calculate number of timestepslevels in the function
		c_timesteplevels = 1;
		Timesteplevel level = c_eventhandler.getTopTimesteplevel();
		Timesteplevel thisLevel = c_function.getTimesteplevel();

        // Added by Nawzad Mardan 20100221
        NodeControl nodeControl = c_gui.getAllNodes();
        Timesteplevel tsl2 = nodeControl.getTimesteplevel(c_nodeID);
        c_currentTimestep  = tsl2.getLabel();
        Timesteplevel tstl = level;
        if(tstl.getNextLevel() == null)
            c_maxTimeSteps = 0;

        while ((tstl = tstl.getNextLevel()) != null)
            {
            c_maxTimeSteps *= tstl.getTimesteps();
            }
		while (level != thisLevel) {
			c_timesteplevels++;
			level = level.getNextLevel();
		}
		//Get all timesteplevels
		tsl = new Timesteplevel[c_timesteplevels];
		level = c_eventhandler.getTopTimesteplevel();
		for (int j = 0; j < c_timesteplevels; j++) {
			tsl[j] = level;
			level = level.getNextLevel();
		}

		c_timestep = 1;
		c_function.setTimestep(c_timestep);

		initComponents();
		txtLabel.setText(c_function.getLabel());
		updateCostTable();
		updateResources();
		pack();
	}

	private void updateCostTable() {
		//read all object-function labels from
		//GUI->EventHandlerClient->EventHandlerServer->Model->
		//ObjectFunction
		Vector labels = c_gui.getObjectFunctionLabels();
		int n = labels.size();

		if (DEBUG) {
			System.out.println("DEBUG: Labels from ObjectFunction...");
			for (int i = 0; i < labels.size(); i++)
				System.out.println(labels.get(i));
		}

		//set table with all labels and all costs with a matching label
		//only right column is editable

                 /* Added by Nawzad Mardan 20100322 at 22.00
    To solve the bug in the Source function. If the user add a new Source function in a node
    which have several levels of time steps and user enter only the values for the first time steps instead for alls
    time steps and save the model. If the user try to open the model an errer  occur and the model can not be opened
    */
             if(!(c_currentTimestep.equals("TOP")) && (c_maxTimeSteps > 1))
                {
                boolean dataNotEnterd = false;
                for(int i = 2; i <= c_maxTimeSteps; i++)
                  {
                 //Vector tempCost =  c_function.getCost(i);
                  if(c_function.getCost(i) == null)//c_function.getCost(i).isEmpty())
                    {
                    dataNotEnterd =true;
                    break;
                    }
                  }
                if(dataNotEnterd)
                   {
                   c_function.setDetailedDataToRemainedTimesteps(c_maxTimeSteps);
                   }
                //if(!c_function.getCostSize())
                  //  c_function.updateCost(c_maxTimeSteps);
                }

		Object[][] data = new Object[n][2];

		Vector costs = c_function.getCost();
		for (int i = 0; i < n; i++) {
			data[i][0] = labels.get(i);
			if (costs != null && costs.size() > i)
				data[i][1] = getMatchingCost((CostTuple) costs.get(i), labels);
			else
				data[i][1] = new Float(0.0f);
		}


    /* Added by Nawzad Mardan 20100322 at 22.00
    To solve the bug in the Source function. If the user add a new Source function in a node
    which have several levels of time steps and user enter only the values for the first time steps instead for alls
    time steps and save the model. If the user try to open the model an errer  occur and the model can not be opened
    */
             if(!(c_currentTimestep.equals("TOP")) && (c_maxTimeSteps > 1))
                {
                boolean dataNotEnterd = false;
                for(int i = 2; i <= c_maxTimeSteps; i++)
                  {
                 //Vector tempCost =  c_function.getCost(i);
                  if(c_function.getCost(i) == null)//c_function.getCost(i).isEmpty())
                    {
                    dataNotEnterd =true;
                    break;
                    }
                  }
                if(dataNotEnterd)
                   {
                   c_function.setDetailedDataToRemainedTimesteps(c_maxTimeSteps);
                   }
                //if(!c_function.getCostSize())
                  //  c_function.updateCost(c_maxTimeSteps);
                }
		c_myTableModel.setData(data);
		c_myTableModel.fireTableDataChanged();
	}

	/*
	 * Get the matching cost from the cost tuple
	 */
	private Float getMatchingCost(CostTuple tuple, Vector labels) {
		int n = labels.size();
		for (int i = 0; i < n; i++) {
			if (((String) labels.get(i)).equalsIgnoreCase(tuple.getLabel()))
				return new Float(tuple.getValue());
		}
		return new Float(0.0f);
	}

	private void updateResources() {
		ID resourceID;
		c_resources = c_gui.getResources();
		listResource.setListData(c_resources);
		if (c_function.getResource() != null)
			listResource.setSelectedIndex(c_resources.indexOf(c_gui
					.getResource(c_function.getResource())));
	}

	/*
	 * Initialize all components in the dialog window
	 */
	private void initComponents() {
		getContentPane().setLayout(new java.awt.GridBagLayout());
		java.awt.GridBagConstraints gridBagConstraints1;
		setName("Source");
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				closeDialog(evt);
			}
		});

		//Description text
		lblDescription = new javax.swing.JLabel();
		lblDescription
				.setText("Description: Source is a function from which resources emerges.");
		gridBagConstraints1 = new java.awt.GridBagConstraints();
		gridBagConstraints1.gridwidth = 2;
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
		gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
		getContentPane().add(lblDescription, gridBagConstraints1);

		//Separator line 1
		sep1 = new javax.swing.JSeparator();
		gridBagConstraints1 = new java.awt.GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridwidth = 2;
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		getContentPane().add(sep1, gridBagConstraints1);

		//Label
		pnlLabel = new javax.swing.JPanel();
		pnlLabel.setLayout(new java.awt.GridBagLayout());
		lblLabel = new javax.swing.JLabel();
		lblLabel.setText("Label");
		//txtLabel.setMinimumSize(new java.awt.Dimension(40, 21));

		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		//gridBagConstraints5.insets = new java.awt.Insets (10, 10, 10, 10);
		gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
		pnlLabel.add(lblLabel, gridBagConstraints5);

		//Label text box
		txtLabel = new javax.swing.JTextField();
		gridBagConstraints5 = new java.awt.GridBagConstraints();
		gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints5.insets = new java.awt.Insets(0, 10, 0, 0);
		gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints5.weightx = 1.0;
		pnlLabel.add(txtLabel, gridBagConstraints5);

		gridBagConstraints1 = new java.awt.GridBagConstraints();
		gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 2;
		gridBagConstraints1.gridwidth = 2;
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
		gridBagConstraints1.weightx = 1.0;
		getContentPane().add(pnlLabel, gridBagConstraints1);

		//Separator line 2
		sep2 = new javax.swing.JSeparator();
		gridBagConstraints1 = new java.awt.GridBagConstraints();
		gridBagConstraints1.gridy = 3;
		gridBagConstraints1.gridwidth = 2;
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		getContentPane().add(sep2, gridBagConstraints1);

		//Timestep control

		//Label "Timestep"
		lblTimestep = new javax.swing.JLabel();
		lblTimestep.setText("Timestep:");
		gridBagConstraints1 = new java.awt.GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 4;
		gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 5);
		gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
		getContentPane().add(lblTimestep, gridBagConstraints1);

		//Main timestep panel
		pnlTimestep = new javax.swing.JPanel();
		pnlTimestep.setLayout(new java.awt.GridBagLayout());
		gridBagConstraints1 = new java.awt.GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 4;
		gridBagConstraints1.insets = new java.awt.Insets(30, 25, 0, 20);
		gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
		getContentPane().add(pnlTimestep, gridBagConstraints1);

		lblTSL = new javax.swing.JLabel[c_timesteplevels];
		spinTSL = new SpinButton[c_timesteplevels];
		Timesteplevel level = c_eventhandler.getTopTimesteplevel();
		Timesteplevel thisLevel = c_function.getTimesteplevel();
		level = c_eventhandler.getTopTimesteplevel();

		for (int i = 0; i < c_timesteplevels; i++) {
			//Add timestep labels
			gridBagConstraints1 = new java.awt.GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = i;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;

			lblTSL[i] = new javax.swing.JLabel();
			lblTSL[i].setText(level.getLabel());
			pnlTimestep.add(lblTSL[i], gridBagConstraints1);

			//Add timestep spinbuttons
			gridBagConstraints1 = new java.awt.GridBagConstraints();
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = i;
			gridBagConstraints1.insets = new java.awt.Insets(0, 15, 0, 0);
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.weightx = 1.0;

			spinTSL[i] = new SpinButton(1, level.getMaxTimesteps(), 1, 1);
			pnlTimestep.add(spinTSL[i], gridBagConstraints1);

			//final int button = i;
			spinTSL[i].addListener(new SpinButtonListener() {
				public void valueDecreased() {
					updateTimestep();
				}

				public void valueIncreased() {
					updateTimestep();
				}
			});
		    spinTSL[i].addFocusListener(new SpinButtonUpdateListener(spinTSL[i]) {
		    	public void valueUpdated() {
		    		updateTimestep();	
		    	}
		    });
			level = level.getNextLevel();
		}

		//Copy & paste panel
//		pnlCopyPaste = new javax.swing.JPanel();
//		btnCopyFrom = new javax.swing.JButton();
//		btnCopy = new javax.swing.JButton();
//		btnPasteTo = new javax.swing.JButton();
//		btnPaste = new javax.swing.JButton();

		//pnlCopyPaste.setLayout(new java.awt.GridBagLayout());
		java.awt.GridBagConstraints gridBagConstraints3;

	/*	btnCopyFrom.setEnabled(false);
		btnCopyFrom.setText("Copy from range");
		btnCopyFrom.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnCopyFromActionPerforme(evt);
				copyFromRange(evt);
				btnCopyFromActionPerformed(evt);
			}
		});

		gridBagConstraints3 = new java.awt.GridBagConstraints();
		//	gridBagConstraints3.insets = new java.awt.Insets (10, 10, 10, 10);
		pnlCopyPaste.add(btnCopyFrom, gridBagConstraints3);*/

	/*	btnCopy.setEnabled(false);
		btnCopy.setPreferredSize(new java.awt.Dimension(85, 27));
		btnCopy.setText("Copy");
		btnCopy.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnCopyActionPerformed(evt);
			}
		});

		gridBagConstraints3 = new java.awt.GridBagConstraints();
		gridBagConstraints3.insets = new java.awt.Insets(10, 10, 10, 10);
		pnlCopyPaste.add(btnCopy, gridBagConstraints3);*/

		/*btnPasteTo.setEnabled(false);
		btnPasteTo.setPreferredSize(new java.awt.Dimension(117, 27));
		btnPasteTo.setText("Paste to range");
		btnPasteTo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnPasteToRangeActionPerformed(evt);
			}
		});

		gridBagConstraints3 = new java.awt.GridBagConstraints();
		gridBagConstraints3.gridx = 0;
		//	gridBagConstraints3.insets = new java.awt.Insets (10, 10, 10, 10);
		pnlCopyPaste.add(btnPasteTo, gridBagConstraints3); */

		/*btnPaste.setEnabled(false);
		btnPaste.setPreferredSize(new java.awt.Dimension(85, 27));
		btnPaste.setText("Paste");
		btnPaste.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnPasteActionPerformed(evt);
			}
		});

		gridBagConstraints3 = new java.awt.GridBagConstraints();
		gridBagConstraints3.gridx = 1;
		//	gridBagConstraints3.insets = new java.awt.Insets (10, 10, 10, 10);
		pnlCopyPaste.add(btnPaste, gridBagConstraints3); */

		/*gridBagConstraints1 = new java.awt.GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.gridy = 4;
		gridBagConstraints1.insets = new java.awt.Insets(0, 6, 0, 10);
		gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
		getContentPane().add(pnlCopyPaste, gridBagConstraints1);*/

		//Resource list panel
		pnlResource = new javax.swing.JPanel();
		pnlResource.setLayout(new java.awt.GridBagLayout());
		java.awt.GridBagConstraints gridBagConstraints4;

		//Resource list label
		lblTypeResource = new javax.swing.JLabel();
		lblTypeResource.setText("Type of resource:");

		gridBagConstraints4 = new java.awt.GridBagConstraints();
		//      gridBagConstraints4.insets = new java.awt.Insets (10, 10, 0, 10);
                //gridBagConstraints4.insets = new java.awt.Insets(10, 10, 10, 5);
		gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints4.anchor = GridBagConstraints.CENTER;
		pnlResource.add(lblTypeResource, gridBagConstraints4);

		listResource = new javax.swing.JList();
                //listResource.setMinimumSize(new Dimension(150, 100));

		gridBagConstraints4 = new java.awt.GridBagConstraints();
		gridBagConstraints4.gridx = 0;
                //gbc.gridx = 1;
                //gridBagConstraints4.gridy = 0;
                //gridBagConstraints4.gridwidth = 1;
                //gridBagConstraints4.weightx = 1.0;
               // gridBagConstraints4.gridy =5;
               // gridBagConstraints4.gridheight = 30;//gridBagConstraints4.RELATIVE;
                //gridBagConstraints4.gridwidth = 30;
		//      gridBagConstraints4.insets = new java.awt.Insets (4, 10, 9, 10);

		JScrollPane scrollPane = new JScrollPane(listResource);
		
                scrollPane.setPreferredSize(new Dimension(135, 100));

                pnlResource.add(scrollPane, gridBagConstraints4);

                listResource.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				newResourceSelected();
			}
		});
		//New resource button
		/*
		 //Do we really want to be able to create resources from inside this dialog?
		 //If we can, a created resource should be deleted when pressing cancel.

		 btnNewResource = new javax.swing.JButton ();
		 btnNewResource.setText ("New Resource...");
		 btnNewResource.addActionListener (new java.awt.event.ActionListener () {
		 public void actionPerformed (java.awt.event.ActionEvent evt) {
		 btnNewResourceActionPerformed (evt);
		 }
		 });

		 gridBagConstraints4 = new java.awt.GridBagConstraints ();
		 gridBagConstraints4.gridx = 0;
		 //      gridBagConstraints4.insets = new java.awt.Insets (5, 10, 10, 10);
		 pnlResource.add (btnNewResource, gridBagConstraints4);
		 */

		gridBagConstraints1 = new java.awt.GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 5;
		gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 5);
		getContentPane().add(pnlResource, gridBagConstraints1);

		//Cost per unit panel
		pnlCost = new javax.swing.JPanel();
		pnlCost.setLayout(new BoxLayout(pnlCost, BoxLayout.Y_AXIS));
		pnlCost.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		gridBagConstraints1 = new java.awt.GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.gridy = 5;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.insets = new java.awt.Insets(10, 5, 10, 10);
		lblCostPerUnit = new javax.swing.JLabel("Cost per unit");
		getContentPane().add(pnlCost, gridBagConstraints1);
		pnlCost.add(lblCostPerUnit);

		txtCostPerUnit = new javax.swing.JTextField(8);
		//pnlCost.add(txtCostPerUnit);
		c_myTableModel = new MyTableModel();
		tblCostPerUnit = new JTable(c_myTableModel);
		//pnlCost.add(tblCostPerUnit);
                //tblCostPerUnit.setSize(new Dimension(10, 10));
		tblCostPerUnit.setPreferredScrollableViewportSize(new Dimension(135, 100));

		//Create the scroll pane and add the table to it.
		JScrollPane scrollPane2 = new JScrollPane(tblCostPerUnit);
                scrollPane2.setPreferredSize(new Dimension(135, 100));
		//Add the scroll pane to this panel.
		//this.getContentPane().add(scrollPane2);
		//gridBagConstraints1 = new java.awt.GridBagConstraints();
		//gridBagConstraints1.gridx = 0;
		//gridBagConstraints1.gridwidth = 2;
		//gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		//this.getContentPane().add(scrollPane2, gridBagConstraints1);

		pnlCost.add(scrollPane2);

		//Separator 3
		sep3 = new javax.swing.JSeparator();
		gridBagConstraints1 = new java.awt.GridBagConstraints();
		gridBagConstraints1.gridy = 7;
		gridBagConstraints1.gridwidth = 2;
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		getContentPane().add(sep3, gridBagConstraints1);

		//OK and Cancel button panel
		pnlButtons = new javax.swing.JPanel();
		gridBagConstraints1 = new java.awt.GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 8;
		gridBagConstraints1.gridwidth = 2;
		gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
		gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
		getContentPane().add(pnlButtons, gridBagConstraints1);

		//OK button
		btnOk = new javax.swing.JButton();
		btnOk.setText("OK");
		getRootPane().setDefaultButton(btnOk);
		btnOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnOkActionPerformed(evt);
			}
		});
		pnlButtons.add(btnOk);

		//Cancel button
		btnCancel = new javax.swing.JButton();
		btnCancel.setText("Cancel");
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnCancelActionPerformed(evt);
			}
		});

		pnlButtons.add(btnCancel);
               // this.setResizable(false);

	}

	private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {
		setVisible(false);
		dispose();
	}

	private void btnOkActionPerformed(java.awt.event.ActionEvent evt)
        {
		try
            {
			float cost = 0;
			if (tblCostPerUnit.isEditing()) {
				tblCostPerUnit.getCellEditor().stopCellEditing();
			}
			// save the function label
			c_function.setLabel(txtLabel.getText());

			Resource resource = (Resource) listResource.getSelectedValue();
			if (resource != null)
				c_function.setResource(resource.getID());
            else
                {
                JOptionPane.showMessageDialog(null, "The Resource for Source function is not specified", "Can not optimize ",JOptionPane.WARNING_MESSAGE);
                return;
                
                }
            //"Resource for Source function in Node "
			//		+ node + " not specified.\n\n" + "Can not optimize.");

			//Save the values for the selected timestep
			updateTimestep();

  /* Added by Nawzad Mardan 20100321 at 23.51
    To solve the bug in the Source function. If the user add a new Source function in a node
    which have several levels of time steps and user enter only the values for the first time steps instead for alls
    time steps and save the model. If the user try to open the model an errer  occur and the model can not be opened
    */
             if(!(c_currentTimestep.equals("TOP")) && (c_maxTimeSteps > 1))
                {
                boolean dataNotEnterd = false;
                for(int i = 2; i <= c_maxTimeSteps; i++)
                  {
                 //Vector tempCost =  c_function.getCost(i);
                  if(c_function.getCost(i) == null)//c_function.getCost(i).isEmpty())
                    {
                    dataNotEnterd =true;
                    break;
                    }
                  }
                if(dataNotEnterd)
                   {
                   c_function.setDetailedDataToRemainedTimesteps(c_maxTimeSteps);
                   }
                //if(!c_function.getCostSize())
                  //  c_function.updateCost(c_maxTimeSteps);
                }

			// close the dialog
			closeDialog(null);
		} catch (NumberFormatException e) {
			c_gui.showMessageDialog("Entered cost value is not a float value.");
		}
	}

	private void newResourceSelected() {
		Resource resource = (Resource) listResource.getSelectedValue();
		lblCostPerUnit.setText("Cost per unit (" + resource.getPrefix()
				+ resource.getUnit() + ")");
	}

	private void btnPasteActionPerformed(java.awt.event.ActionEvent evt) {
		// Add your handling code here:
	}

	private void btnCopyActionPerformed(java.awt.event.ActionEvent evt) {
		// Add your handling code here:
	}

	private void btnPasteToRangeActionPerformed(java.awt.event.ActionEvent evt) {
		// Add your handling code here:
	}

	private void btnCopyFromActionPerformed(java.awt.event.ActionEvent evt) {
		// Add your handling code here:
	}

	private void copyFromRange(java.awt.event.ActionEvent evt) {
		// Add your handling code here:
	}

	private void btnCopyFromActionPerforme(java.awt.event.ActionEvent evt) {

		// Add your handling code here:
	}

	/** Closes the dialog */
	private void closeDialog(java.awt.event.WindowEvent evt) {
		setVisible(false);
		dispose();
	}

	private void updateTimestep() {

		//float newCost;
		Vector newCost = new Vector();
		//Save value for previous timestep
		c_function.setTimestep(c_timestep);
		//get values from the table
		int height = tblCostPerUnit.getRowCount();
		for (int i = 0; i < height; i++) {
			//newCost.add(new Float(tblCostPerUnit.getValueAt(i,1)));
			//newCost = java.lang.Float.parseFloat(txtCostPerUnit.getText());
			newCost.add(new CostTuple((String) c_myTableModel.getValueAt(i, 0),
					((Float) c_myTableModel.getValueAt(i, 1)).floatValue()));
		}
		c_function.setCost(newCost);

		//Calculate new timestep
		c_timestep = 1; //The first timestep is 1 (not 0)
		int factor = 1;
		for (int i = c_timesteplevels - 1; i > 0; i--) {
			c_timestep = c_timestep + (spinTSL[i].getValue() - 1) * factor;
			factor = factor * tsl[i].getMaxTimesteps();
		}

		//Get value for new timestep
		c_function.setTimestep(c_timestep);
		//txtCostPerUnit.setText(Float.toString(c_function.getCost()));
		updateCostTable();
	}

	/*
	 * Inner class of the table model
	 */
	class MyTableModel extends javax.swing.table.AbstractTableModel {

		private String[] columnNames = { "Object", "cost" };

		private Object[][] data = { { "", new Float(0.0) } };

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

		/*
		 * JTable uses this method to determine the default renderer/
		 * editor for each cell.  If we didn't implement this method,
		 * then the last column would contain text ("true"/"false"),
		 * rather than a check box.
		 */
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		/*
		 * Don't need to implement this method unless your table's
		 * editable.
		 */
		public boolean isCellEditable(int row, int col) {
			//Note that the data/cell address is constant,
			//no matter where the cell appears onscreen.
			if (col < 1) {
				return false;
			} else {
				return true;
			}
		}

		/*
		 * set a new data body for this model
		 */
		public void setData(Object[][] d) {
			data = d;
		}

		/*
		 * Don't need to implement this method unless your table's
		 * data can change.
		 */
		public void setValueAt(Object value, int row, int col) {

			if (DEBUG) {
				System.out.println("Setting value at " + row + "," + col
						+ " to " + value + " (an instance of "
						+ value.getClass() + ")");
			}

			data[row][col] = value;
			fireTableCellUpdated(row, col);

			if (DEBUG) {
				System.out.println("New value of data:");
				printDebugData();
			}

		}

		private void printDebugData() {
			int numRows = getRowCount();
			int numCols = getColumnCount();

			for (int i = 0; i < numRows; i++) {
				System.out.print("    row " + i + ":");
				for (int j = 0; j < numCols; j++) {
					System.out.print("  " + data[i][j]);
				}
				System.out.println();
			}
			System.out.println("--------------------------");
		}
	}
}