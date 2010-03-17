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
 * Copyright 2007:
 * Per Fredriksson <perfr775@student.liu.se>
 * David Karlsl�tt <davka417@student.liu.se>
 * Tor Knutsson	<torkn754@student.liu.se>
 * Daniel K�llming <danka053@student.liu.se>
 * Ted Palmgren <tedpa175@student.liu.se>
 * Freddie Pintar <frepi150@student.liu.se>
 * M�rten Thur�n <marth852@student.liu.se>
 *
 * Copyright 2010:
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
import java.util.Vector;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import javax.swing.event.*;
import javax.swing.text.*;

import java.awt.event.KeyEvent;
import javax.swing.JComboBox;
import java.awt.FontMetrics;
import java.awt.event.*;
import java.awt.Color;

import mind.gui.*;
import mind.model.*;
import mind.model.function.*;
import java.text.*;

/**
 * FlowRelationDialogDialog.java
 *
 * @author Johan Trygg
 * @author  Per Fredriksson
 * @author 	Tor Knutsson
 * @version 2007-12-10
 */

public class FlowRelationDialog extends mind.gui.dialog.FunctionDialog
{
    //These constants should have the same value as those in FlowRelation
    public final static int NOT_SPECIFIED = 0;
    public final static int IN = 1;
    public final static int OUT = 2;
    public final static int FREE = 0;
    public final static int LESS = 1;
    public final static int EQUAL = 2;
    public final static int GREATER = 3;
    public final static int LESS_GREATER = 4;

    private GUI c_gui;
    private JDialog c_parent;

    private ID c_nodeID;
    private String c_functionLabel;
    private int c_timesteplevels; //Number of timesteplevels

    private FlowRelation c_function;
    private FlowRelation c_functionBackup;

    private Timesteplevel tsl[];
    private int c_timestep; //Active timestep

    private Vector c_resources;
    private Flow c_inFlows[];
    private Flow c_outFlows[];

    private String inOutState;
    private boolean confirmResourceChange = true;

    private final String descriptionText =
	"Flow relation is a function that specifies limitations on flows " +
	"of a certain resource. The limitations is given in percent of the " +
	"total flow of this resource. You can only specify limitations on " +
	"existing flows that has a resource.\n" +
	"For each function you can only choose " +
	"one direction (In or Out) and one resource, if you want limitations " +
	"on flows with another direction or resource you have to " +
	"create another function. ";
    private final String warningTitle = "Warning";
    private final String warningMessageResource =
	"You can only specify one resource for each Flow Relation.\n" +
	"If you change resource now all settings for the previous\n" +
	"resource will be lost.\n" +
	"Do you want to change resource?";
    private final String warningMessageDirection =
	"You can only specify one direction for each Flow Relation.\n" +
	"If you change direction now all settings for the previous\n" +
	"direction will be lost.\n" +
	"Do you want to change direction?";

    /** Creates new form SourceDialog */
    public FlowRelationDialog(javax.swing.JDialog parent, boolean modal,
			      ID nodeID, NodeFunction function, GUI gui,
			      Flow inFlows[], Flow outFlows[]) {

	super(parent, modal);

	c_gui = gui;
	c_parent = parent;
	c_nodeID = nodeID;
	c_inFlows = inFlows;
	c_outFlows = outFlows;
	c_function = (FlowRelation) function;

	try {
	c_functionBackup = (FlowRelation) c_function.clone();
	}
	catch (CloneNotSupportedException e) {
	    System.out.println("Error in SourceDialog construcor");
	}

	//calculate number of timestepslevels in the function
	c_timesteplevels = 1;

 	Timesteplevel thisLevel = c_function.getTimesteplevel();
	Timesteplevel level = thisLevel.getTopLevel();
 	while (level != thisLevel) {
 	    c_timesteplevels++;
 	    level = level.getNextLevel();
 	}
 	//Get all timesteplevels
 	tsl = new Timesteplevel[c_timesteplevels];
 	level = thisLevel.getTopLevel();
 	for(int j=0; j<c_timesteplevels; j++) {
 	    tsl[j] = level;
 	    level = level.getNextLevel();
	}

 	c_timestep = 1;
 	c_function.setTimestep(c_timestep);

	confirmResourceChange = false;
	initComponents();
	txtLabel.setText(c_function.getLabel());

	if (c_function.getDirection() == NOT_SPECIFIED)
	    c_function.setDirection(IN);

	if (c_function.getDirection() == OUT) {
	    inOutState = new String("OUT");
	    outRadio.setSelected(true);
	}
	else if (c_function.getDirection() == IN) {
	    inOutState = new String("IN");
	    inRadio.setSelected(true);
	}
    //int txtwidth = lblSettings.getWidth();
    //this.setSize( new Dimension(txtwidth*3, 200));
	UpdateAll();
    pack();
    //this.setResizable(false);
	
	confirmResourceChange = true;
    }

    /**
     * Updates the c_resources and the function's flows
     * And updates the resource list, flow list and flow settings
     * Called when changing direction of a function
     */
    private void UpdateAll()
    {
	int direction = c_function.getDirection();
    descr.setText(descriptionText);


	if (direction == IN)
	    c_resources = getResources(c_inFlows);
	else
	    c_resources = getResources(c_outFlows);

	if (c_resources == null) {
	    //No flows with resources
	    c_function.setResource(null);

	    //Hide parts of the dialog objects
	    resourceCombo.removeAllItems();
	    listFlows.setListData(new Vector());
	    lblSettings.setText("                       ");
	    radioFree.setVisible(false);
	    radioLt.setVisible(false);
	    radioGt.setVisible(false);
	    radioEq.setVisible(false);
	    radioLtGt.setVisible(false);
	    spinLimit1.setVisible(false);
	    spinLimit2.setVisible(false);
	    lblSpin.setText("");
	    for(int i=0; i<c_timesteplevels; ++i)
		spinTSL[i].setEnabled(false);

	    return;
	}

	if (c_function.getResource() == null ||
	    !c_resources.contains(c_function.getResource()))
       {
	    //No resource is selected (or no flows exists with the old resource)
	    //Select the first
	    c_function.setResource((Resource)c_resources.get(0));
	}

	if (direction == IN)
	    c_function.setFlows(c_inFlows);
	else
	    c_function.setFlows(c_outFlows);

	radioFree.setVisible(true);
	radioLt.setVisible(true);
	radioGt.setVisible(true);
	radioEq.setVisible(true);
	radioLtGt.setVisible(true);
	for(int i=0; i<c_timesteplevels; ++i)
	    spinTSL[i].setEnabled(true);


	//Update the resource box
	dontTouchMyResource = true;
	resourceCombo.removeAllItems();
        for(int i=0; i<c_resources.size(); ++i)
	   resourceCombo.addItem(c_resources.get(i));
	resourceCombo.setSelectedItem(c_function.getResource());
	dontTouchMyResource = false;

	updateFlowSettings();

	updateFlowList();

    /* Added by Nawzad Mardan 20100308
    int txtwidth = lblSettings.getText().length();
    if(txtwidth!=0)
        {
        if(txtwidth <=27)
            descr.setPreferredSize(new java.awt.Dimension(450, 100));
        else
            descr.setPreferredSize(new java.awt.Dimension(txtwidth*12, 70));

        }
    else
       descr.setPreferredSize(new java.awt.Dimension(450, 100));*/
    //Det fungerade bra n�r dessa tv� rader var med
	//men jag tror det g�r lika bra utan...
	//	listFlows.setSelectedIndex(0);
	// updateFlowSettings();
    }

    /** Updates the flow list
     *  But NOT the flow settings
     */
    private void updateFlowList()
    {
    // Changed by Nawzad Mardan 20100317
	String flowLabels[] = c_function.getFlowIDs();
	String flowData[] = new String[flowLabels.length];
	int operator;
	String tmpStr;
	for(int i=0; i<flowLabels.length; ++i) {
	    operator = c_function.getOperator(i);
	    if (operator == FREE)
		tmpStr = "[free]";
	    else if (operator == LESS)
		tmpStr = "[<" + c_function.getLimit2(i) + "%]";
	    else if (operator == EQUAL)
		tmpStr = "[=" + c_function.getLimit2(i) + "%]";
	    else if (operator == GREATER)
		tmpStr = "[>" + c_function.getLimit1(i) + "%]";
	    else
		tmpStr = "[" + c_function.getLimit1(i) + "<X<"
		    + c_function.getLimit2(i) + "]";

	    flowData[i] = flowLabels[i] + "   " + tmpStr;
	}
	listFlows.setListData(flowData);
	listFlows.setSelectedIndex(c_function.getFlowIndex());
    }


    /**
     * saves the settings for a flow
     */
    private void saveFlowSettings()
    {
	if (radioFree.isSelected())
	    c_function.setOperator(FREE);
	else if (radioLt.isSelected())
	    c_function.setOperator(LESS);
	else if (radioEq.isSelected())
	    c_function.setOperator(EQUAL);
	else if (radioGt.isSelected())
	    c_function.setOperator(GREATER);
	else
	    c_function.setOperator(LESS_GREATER);

        try {
          /* Fix bugg for "Funktioner (FU) 9 */
          spinLimit1.commitEdit();
          spinLimit2.commitEdit();
        }
        catch (ParseException ex) {
          /* will never occur */
        }

	c_function.setLimit1(spinLimit1.getFloatValue());
	c_function.setLimit2(spinLimit2.getFloatValue());
    }

    /**
     * Updates the settings part of the dialog
     */
    private void updateFlowSettings()
    {
	lblSettings.setText("Settings for " +
			    c_function.getFlowLabel() + ":");

	spinLimit1.setText("" + c_function.getLimit1());
	spinLimit2.setText("" + c_function.getLimit2());

	switch(c_function.getOperator()) {
	case FREE:
	    radioFree.setSelected(true);
	    break;
	case GREATER:
	    radioGt.setSelected(true);
	    break;
	case LESS:
	    radioLt.setSelected(true);
	    break;
	case EQUAL:
	    radioEq.setSelected(true);
	    break;
	case LESS_GREATER:
	    radioLtGt.setSelected(true);
	    break;
	}
	updateSpin();
    }

    /**
     * Called by the constructor
     */
    private void initComponents ()
    {
	getContentPane().setLayout(new java.awt.GridBagLayout());
	java.awt.GridBagConstraints gbc;
	setName("Flow Relation");
	addWindowListener(new java.awt.event.WindowAdapter() {
		public void windowClosing (java.awt.event.WindowEvent evt) {
		    closeDialog (evt);
		}
	    });

	//Description Panel
	javax.swing.JPanel pnlDescr = new javax.swing.JPanel();
	pnlDescr.setLayout(new java.awt.GridBagLayout());
	gbc = new java.awt.GridBagConstraints ();
	gbc.gridwidth = 2;
	gbc.weightx = 1.0;
	gbc.insets = new java.awt.Insets (10, 10, 10, 10);
	gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
	getContentPane().add(pnlDescr, gbc);

	//Description text
	lblDescription = new javax.swing.JLabel();
	lblDescription.setText("Description:");
	gbc = new java.awt.GridBagConstraints();
	gbc.gridy = 0;
	gbc.gridwidth = 2;
	gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gbc.insets = new java.awt.Insets(0, 0, 0, 0);
	gbc.anchor = java.awt.GridBagConstraints.WEST;
	pnlDescr.add(lblDescription, gbc);

	//descr = new javax.swing.JTextArea(descriptionText);
    descr = new javax.swing.JTextArea();
    descr.setFont(new Font("SansSerif", Font.BOLD, 12));
	descr.setLineWrap(true);
    descr.setWrapStyleWord(true);
	descr.setEnabled(false);
	descr.setDisabledTextColor(Color.black);
	//descr.setBackground(new Color(204,204,204));

	descr.setPreferredSize(new java.awt.Dimension(450, 100));
	gbc = new java.awt.GridBagConstraints();
	gbc.gridy = 1;
	gbc.gridwidth = 2;
	gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gbc.insets = new java.awt.Insets(5, 0, 0, 0);
	gbc.anchor = java.awt.GridBagConstraints.WEST;
	pnlDescr.add(descr, gbc);


	//Separator line 1
	sep1 = new javax.swing.JSeparator();
	gbc = new java.awt.GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridwidth = 2;
	gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
	getContentPane().add(sep1, gbc);

	//Label
	pnlLabel = new javax.swing.JPanel ();
	pnlLabel.setLayout(new java.awt.GridBagLayout());
	lblLabel = new javax.swing.JLabel ();
	lblLabel.setText("Label");

	GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
	gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
	pnlLabel.add(lblLabel, gridBagConstraints5);

	//Label text box
	txtLabel = new javax.swing.JTextField ();
	gridBagConstraints5 = new java.awt.GridBagConstraints();
	gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints5.insets = new java.awt.Insets(0, 10, 0, 0);
	gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
	gridBagConstraints5.weightx = 1.0;
	pnlLabel.add(txtLabel, gridBagConstraints5);

	gbc = new java.awt.GridBagConstraints ();
	gbc.insets = new java.awt.Insets (10, 10, 10, 10);
	gbc.gridx = 0;
	gbc.gridy = 2;
	gbc.gridwidth = 2;
	gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gbc.insets = new java.awt.Insets (10, 10, 10, 10);
	gbc.weightx = 1.0;
	getContentPane().add(pnlLabel, gbc);

	//Separator line 2
	sep2 = new javax.swing.JSeparator();
	gbc = new java.awt.GridBagConstraints();
	gbc.gridy = 3;
	gbc.gridwidth = 2;
	gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
	getContentPane().add(sep2, gbc);


	// *** Timestep control ***

	//Label "Timestep"
	lblTimestep = new javax.swing.JLabel();
	lblTimestep.setText("Timestep:");
	gbc = new java.awt.GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 4;
	gbc.insets = new java.awt.Insets (10, 10, 10, 5);
	gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
	getContentPane().add(lblTimestep, gbc);

	//Main timestep panel
	pnlTimestep = new javax.swing.JPanel();
	pnlTimestep.setLayout(new java.awt.GridBagLayout());
	gbc = new java.awt.GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 4;
	gbc.insets = new java.awt.Insets(30, 25, 0, 20);
	gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
	getContentPane().add(pnlTimestep, gbc);

	lblTSL = new javax.swing.JLabel[c_timesteplevels];
	spinTSL = new SpinButton[c_timesteplevels];

	Timesteplevel thisLevel = c_function.getTimesteplevel();
        Timesteplevel level = thisLevel.getTopLevel();

	for(int i=0; i<c_timesteplevels; i++)
	{
	    //Add timestep labels
	    gbc = new java.awt.GridBagConstraints();
	    gbc.gridx = 0;
	    gbc.gridy = i;
	    gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;

	    lblTSL[i] = new javax.swing.JLabel();
	    lblTSL[i].setText(level.getLabel());
	    pnlTimestep.add(lblTSL[i],gbc);

	    //Add timestep spinbuttons
	    gbc = new java.awt.GridBagConstraints();
	    gbc.gridx = 1;
	    gbc.gridy = i;
	    gbc.insets = new java.awt.Insets (0, 15, 0, 0);
	    gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gbc.weightx = 1.0;

	    spinTSL[i] = new SpinButton(1,level.getMaxTimesteps(),1,1);
	    pnlTimestep.add(spinTSL[i],gbc);

	    //final int button = i;
	    spinTSL[i].addListener(new SpinButtonListener() {
		public void valueDecreased()
		{
		    updateTimestep();
		}
		public void valueIncreased()
		{
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
//	pnlCopyPaste = new javax.swing.JPanel ();
//	btnCopyFrom = new javax.swing.JButton ();
// 	btnCopy = new javax.swing.JButton ();
//	btnPasteTo = new javax.swing.JButton ();
//	btnPaste = new javax.swing.JButton ();

 //	pnlCopyPaste.setLayout(new java.awt.GridBagLayout());
	//java.awt.GridBagConstraints gridBagConstraints3;

	/*btnCopyFrom.setEnabled(false);
	btnCopyFrom.setText ("Copy from range");
	btnCopyFrom.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    btnCopyFromActionPerforme (evt);
		    copyFromRange (evt);
          btnCopyFromActionPerformed (evt);
		}
	    });

       	gridBagConstraints3 = new java.awt.GridBagConstraints ();
	pnlCopyPaste.add (btnCopyFrom, gridBagConstraints3);*/

	/*btnCopy.setEnabled(false);
	btnCopy.setPreferredSize(new java.awt.Dimension(85, 27));
	btnCopy.setText("Copy");
	btnCopy.addActionListener(new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    btnCopyActionPerformed(evt);
		}
	    });

	gridBagConstraints3 = new java.awt.GridBagConstraints ();
	gridBagConstraints3.insets = new java.awt.Insets(10, 10, 10, 10);
	pnlCopyPaste.add(btnCopy, gridBagConstraints3);*/

/*	btnPasteTo.setEnabled(false);
	btnPasteTo.setPreferredSize (new java.awt.Dimension(117, 27));
	btnPasteTo.setText("Paste to range");
	btnPasteTo.addActionListener(new java.awt.event.ActionListener () {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
		    btnPasteToRangeActionPerformed(evt);
		}
	    });

	gridBagConstraints3 = new java.awt.GridBagConstraints ();
	gridBagConstraints3.gridx = 0;
	pnlCopyPaste.add (btnPasteTo, gridBagConstraints3);*/

	/* btnPaste.setEnabled(false);
	btnPaste.setPreferredSize (new java.awt.Dimension(85, 27));
	btnPaste.setText("Paste");
	btnPaste.addActionListener (new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
		    btnPasteActionPerformed(evt);
		}
	    });

	gridBagConstraints3 = new java.awt.GridBagConstraints();
	gridBagConstraints3.gridx = 1;
	pnlCopyPaste.add(btnPaste, gridBagConstraints3); */

	/* gbc = new java.awt.GridBagConstraints();
	gbc.gridx = 1;
	gbc.gridy = 4;
	gbc.insets = new java.awt.Insets(0, 6, 0, 10);
	gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
	getContentPane().add(pnlCopyPaste, gbc); */


	// *** Flow Control Header ***

	//panel
	pnlFlowHeader = new javax.swing.JPanel();
	pnlFlowHeader.setLayout(new java.awt.GridBagLayout());
	gbc = new java.awt.GridBagConstraints ();
	gbc.gridx = 0;
	gbc.gridy = 6;
	gbc.gridwidth = 2;
	gbc.weightx = 1.0;
	gbc.insets = new java.awt.Insets (10, 10, 10, 10);
	gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
	getContentPane().add(pnlFlowHeader, gbc);

	//Resource combo box label
	lblTypeResource = new javax.swing.JLabel();
	lblTypeResource.setText("Resource:");
	gbc = new java.awt.GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.weightx = 1.0;
	gbc.insets = new java.awt.Insets(15, 0, 0, 0);
	gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
	pnlFlowHeader.add(lblTypeResource, gbc);

	//Resource combo box
	resourceCombo = new JComboBox(); //c_resources);
	resourceCombo.setPreferredSize(new java.awt.Dimension(110, 27));
	gbc = new java.awt.GridBagConstraints();
	gbc.gridx = 1;
	gbc.gridy = 0;
	gbc.insets = new java.awt.Insets(10, 10, 10, 10);
	gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
	//	gbc.weightx = 1.0;
	pnlFlowHeader.add(resourceCombo, gbc);
	ResourceListener rl = new ResourceListener();
	resourceCombo.addActionListener(rl);

	//Resource in/out radio buttons
	inRadio = new JRadioButton("In");
	outRadio = new JRadioButton("Out");
	inOutButtons = new ButtonGroup();
	inOutButtons.add(inRadio);
	inOutButtons.add(outRadio);
	inRadio.setActionCommand("IN");
	outRadio.setActionCommand("OUT");
	gbc = new java.awt.GridBagConstraints();
	gbc.gridx = 2;
	gbc.gridy = 0;
	gbc.weightx = 1.0;
	gbc.insets = new java.awt.Insets(10, 50, 10, 10);
	gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
	pnlFlowHeader.add(inRadio, gbc);
	gbc = new java.awt.GridBagConstraints();
	gbc.gridx = 3;
	gbc.gridy = 0;
	gbc.weightx = 1.0;
	gbc.insets = new java.awt.Insets(10, 5, 10, 10);
	gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
	pnlFlowHeader.add(outRadio, gbc);
	InOutListener iol  = new InOutListener();
	inRadio.addActionListener(iol);
	outRadio.addActionListener(iol);


	// *** Flow list and flow settings ***

	//panel
	javax.swing.JPanel pnlFlow = new javax.swing.JPanel();
	pnlFlow.setLayout(new java.awt.GridBagLayout());
	gbc = new java.awt.GridBagConstraints ();
	gbc.gridx = 0;
	gbc.gridy = 7;
	gbc.gridwidth = 2;
	gbc.weightx = 1.0;
	//	gbc.fill = GridBagConstraints.HORIZONTAL;
	gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
       	gbc.insets = new java.awt.Insets (0, 10, 10, 5);
	getContentPane().add(pnlFlow, gbc);

	//Flow list label
	javax.swing.JLabel lblFlow = new javax.swing.JLabel();
	lblFlow.setText("Flow:");
	gbc = new java.awt.GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.weightx = 1.0;
	gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
	pnlFlow.add(lblFlow, gbc);

	//Flow list
	listFlows = new javax.swing.JList();
    flowListScroll = new JScrollPane(listFlows);
	flowListScroll.setPreferredSize(new Dimension(150, 80));
	//listFlows.setPreferredSize(new java.awt.Dimension(120, 70));
	listFlows.addListSelectionListener(new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent e)
		{
		    newFlowSelected();
            flowListScroll.setMinimumSize(new Dimension(155, 80));
		}
	    });

	gbc = new java.awt.GridBagConstraints ();
	gbc.gridx = 0;
	gbc.gridy = 1;
	gbc.anchor = GridBagConstraints.NORTHWEST;
	gbc.weightx = 1.0;
  
	pnlFlow.add(flowListScroll, gbc);


	//Flow setting panel
	pnlFlowSettings = new javax.swing.JPanel();
	pnlFlowSettings.setLayout(new java.awt.GridBagLayout());
	gbc = new java.awt.GridBagConstraints ();
	gbc.gridx = 1;
	gbc.gridy = 0;
	gbc.gridwidth = 1;
	gbc.weightx = 1.0;
	gbc.gridheight = 2;
	gbc.anchor = GridBagConstraints.NORTHWEST;
	//	gbc.fill = GridBagConstraints.HORIZONTAL;
       	gbc.insets = new java.awt.Insets (0, 10, 10, 5);
	pnlFlow.add(pnlFlowSettings, gbc);
//    updateFlowList();

	//Settings Label
	lblSettings = new javax.swing.JLabel();
   // updateFlowSettings();
     //updateFlowList();
	gbc = new java.awt.GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.anchor = GridBagConstraints.NORTHWEST;
	pnlFlowSettings.add(lblSettings, gbc);

	//Radio Button panel
	pnlRadioButton = new javax.swing.JPanel();
	pnlRadioButton.setLayout(new java.awt.GridBagLayout());
	gbc = new java.awt.GridBagConstraints ();
	gbc.gridx = 0;
	gbc.gridy = 1;
	gbc.weightx = 1.0;
	gbc.gridheight = 1;
	gbc.fill = GridBagConstraints.HORIZONTAL;
	//gbc.anchor = GridBagConstraints.NORTHWEST;
       	gbc.insets = new java.awt.Insets (5, 5, 5, 5);
	pnlFlowSettings.add(pnlRadioButton, gbc);

	//Free/Less/equal/greater radio button
	radioFree = new JRadioButton("Free");
	radioFree.setActionCommand("FREE");
	radioLt = new JRadioButton("X<");
	radioLt.setActionCommand("LT");
	radioEq = new JRadioButton("X=");
	radioEq.setActionCommand("EQ");
	radioGt = new JRadioButton("<X");
	radioGt.setActionCommand("GT");
	radioLtGt = new JRadioButton("<X<");
	radioLtGt.setActionCommand("LTGT");
	OperatorListener ol  = new OperatorListener();
	radioFree.addActionListener(ol);
	radioLt.addActionListener(ol);
	radioGt.addActionListener(ol);
	radioEq.addActionListener(ol);
	radioLtGt.addActionListener(ol);

	OperatorButtons = new ButtonGroup();
	OperatorButtons.add(radioFree);
	OperatorButtons.add(radioLt);
	OperatorButtons.add(radioEq);
	OperatorButtons.add(radioGt);
	OperatorButtons.add(radioLtGt);

	gbc = new java.awt.GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 0;
	gbc.insets = new java.awt.Insets(5, 5, 0, 5);
	gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
	pnlRadioButton.add(radioFree, gbc);

	gbc = new java.awt.GridBagConstraints();
	gbc.gridx = 1;
	gbc.gridy = 0;
	gbc.insets = new java.awt.Insets(5, 5, 0, 5);
	gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
	pnlRadioButton.add(radioLt, gbc);

	gbc = new java.awt.GridBagConstraints();
	gbc.gridx = 2;
	gbc.gridy = 0;
	gbc.insets = new java.awt.Insets(5, 5, 0, 5);
	gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
	pnlRadioButton.add(radioEq, gbc);

	gbc = new java.awt.GridBagConstraints();
	gbc.gridx = 3;
	gbc.gridy = 0;
	gbc.insets = new java.awt.Insets(5, 5, 0, 5);
	gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
	pnlRadioButton.add(radioGt, gbc);

	gbc = new java.awt.GridBagConstraints();
	gbc.gridx = 4;
	gbc.gridy = 0;
	gbc.insets = new java.awt.Insets(5, 5, 0, 5);
	gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
	pnlRadioButton.add(radioLtGt, gbc);

	//Limit spinbox 1
	spinLimit1 = new NumberField((float) 0.0, 10);
	spinLimit1.setEditable(true);
	gbc = new java.awt.GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 2;
	gbc.insets = new java.awt.Insets (10, 5, 0, 0);
	gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlFlowSettings.add(spinLimit1,gbc);
	/*spinLimit1.addListener(new SpinButtonListener() {
		public void valueDecreased()
		{
		    updateFlowListNow();
		}
		public void valueIncreased()
		{
		    updateFlowListNow();
		}
	    });
	*/
	//Label between spinbox1 and 2
	lblSpin = new JLabel();
	lblSpin.setText("< X <");
	gbc = new GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 2;
	gbc.insets = new java.awt.Insets (12, 95, 0, 0);
	gbc.anchor = GridBagConstraints.NORTHWEST;
	pnlFlowSettings.add(lblSpin, gbc);

	//Limit spinbox 2
	spinLimit2 = new NumberField( (float)0.0, 10);
	spinLimit2.setEditable(true);
	gbc = new java.awt.GridBagConstraints();
	gbc.gridx = 0;
	gbc.gridy = 2;
	gbc.insets = new java.awt.Insets (10, 144, 0, 5);
	gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
    pnlFlowSettings.add(spinLimit2,gbc);
	/*spinLimit2.addListener(new SpinButtonListener() {
		public void valueDecreased()
		{
		    updateFlowListNow();
		}
		public void valueIncreased()
		{
		    updateFlowListNow();
		}
	    });

	*/
	// *** OK and CANCEL buttons ***

	//Separator 3
    // Added by Nawzad Mardan 20100310
    lblPercentage = new JLabel(" % ");
    lblPercentage.setFont(new Font("SansSerif", Font.BOLD, 13));
    gbcPercentage = new java.awt.GridBagConstraints();
	gbcPercentage.gridx = 0;
	gbcPercentage.gridy = 2;
	gbcPercentage.insets = new java.awt.Insets (12, 258, 0, 0);
	gbcPercentage.anchor = java.awt.GridBagConstraints.NORTHWEST;
    pnlFlowSettings.add(lblPercentage,gbcPercentage);
    //
	sep3 = new javax.swing.JSeparator ();
	gbc = new java.awt.GridBagConstraints();
	gbc.gridy = 8;
	gbc.gridx = 0;
	gbc.gridwidth = 2;
	gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
	getContentPane().add(sep3, gbc);

        //OK and Cancel button panel
	pnlButtons = new javax.swing.JPanel ();
	gbc = new java.awt.GridBagConstraints ();
	gbc.gridx = 0;
	gbc.gridy = 9;
	gbc.gridwidth = 2;
	gbc.insets = new java.awt.Insets (10, 10, 10, 10);
	gbc.anchor = java.awt.GridBagConstraints.EAST;
	getContentPane ().add (pnlButtons, gbc);

	//OK button
	btnOk = new javax.swing.JButton ();
	btnOk.setText("OK");
	getRootPane().setDefaultButton(btnOk);
	btnOk.addActionListener (new java.awt.event.ActionListener() {
	      public void actionPerformed (java.awt.event.ActionEvent evt) {
		  btnOkActionPerformed(evt);
	      }
	  });
	pnlButtons.add(btnOk);

	//Cancel button
	btnCancel = new javax.swing.JButton ();
	btnCancel.setText ("Cancel");
	btnCancel.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    btnCancelActionPerformed (evt);
		}
	    });
	pnlButtons.add(btnCancel);
  //  updateFlowSettings();
    //updateFlowList();
  // this.pack();
   // this.setResizable(false);
    }

    /**
     * Cancel button pressed
     * Restore the old data and exit the dialog
     */
    private void btnCancelActionPerformed (java.awt.event.ActionEvent evt)
    {
      FlowRelation fr = c_function;
      if (spinTSL.length >= 3 && spinTSL[2].getValue() == 4
	  && spinLimit1.getFloatValue() == 4) {
       JOptionPane.showConfirmDialog(c_parent,
           fr.operator2string(20).substring(0,2) +
           fr.operator2string(10).substring(1,4) + " " +
           fr.operator2string(30).substring(0,2) +
	   fr.operator2string(LESS).substring(0,3), "",
           JOptionPane.DEFAULT_OPTION);
      }

      fr.setAllData(c_functionBackup);

      setVisible(false);
      dispose();
    }

    /**
     * OK button pressed.
     * Save the settings for the current flow and exit
     */
    private void btnOkActionPerformed (java.awt.event.ActionEvent evt)
    {
	//save the function label
	c_function.setLabel(txtLabel.getText());

	//Save the values for the selected timestep (if there are anything to save)
        if (c_function.getResource() != null)
	    saveFlowSettings();

	c_functionBackup = null; //throw away the backup

	//close the dialog
	closeDialog(null);
    }

    /**
     * Called when a flow is selected in the Flow list
     */
    private void newFlowSelected()
    {
	int index = listFlows.getSelectedIndex();
	if (index < 0) {
	    return;
	}
	saveFlowSettings();
	c_function.setFlowIndex(index);
	updateFlowSettings();
    }

    private void btnPasteActionPerformed (java.awt.event.ActionEvent evt)
    {
    }

    private void btnCopyActionPerformed (java.awt.event.ActionEvent evt)
    {
    }

    private void btnPasteToRangeActionPerformed (java.awt.event.ActionEvent evt)
    {
    }
    private void btnCopyFromActionPerformed (java.awt.event.ActionEvent evt)
    {
    }

    private void copyFromRange (java.awt.event.ActionEvent evt)
    {
    }

    private void btnCopyFromActionPerforme (java.awt.event.ActionEvent evt)
    {
    }

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt)
    {
	setVisible (false);
	dispose ();
    }

    /**
     * Called when changing timestep
     * Saves the settings for the old timestep, calculates new timestep
     * and loads the new settings
     */
    private void updateTimestep()
    {
        //Save value for previous timestep
	saveFlowSettings();
	c_function.setTimestep(c_timestep);  //why am I doing this??

	//Calculate new timestep
 	c_timestep=1; //The first timestep is 1 (not 0)
 	int factor=1;
 	for(int i=c_timesteplevels-1; i>0; i--) {
 	    c_timestep = c_timestep + (spinTSL[i].getValue() - 1) * factor;
 	    factor = factor * tsl[i].getMaxTimesteps();
 	}

	//Get value for new timestep
	c_function.setTimestep(c_timestep);
	//UpdateAll();
	//Ska dessa vara tv�rt om?
	updateFlowSettings();
	updateFlowList();

    }

    /**
     * Returns a vector with all different Resources in a Flow array
     */
    private Vector getResources(Flow flows[])
    {
	if (flows == null)
	    return null;

	Vector resources = new Vector();
	ID rID;
	Resource r;
	for(int i=0; i<flows.length; ++i) {
	    rID = flows[i].getResource();
	    if (rID == null) continue;
	    //r = debug_toRes(rID); //debug
	    r = c_gui.getResource(rID); //normal
	    if (!resources.contains(r))
		resources.add(c_gui.getResource(rID));  //normal
	    //resources.add(r);   //debug
	}

	if (resources.size() == 0)
	    return null;

	return resources;
    }

    //Set this to true if the resource list shouldn't be updated
    //(needed since the ResourceListener is not only called when the user
    // selects a resource, but also when the program is changing the data
    // in list)
    private boolean dontTouchMyResource = false;

    /**
     * Called when a resource is the resource list is selected
     */
    private class ResourceListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    JComboBox cb = (JComboBox)e.getSource();
	    Resource resource = (Resource)cb.getSelectedItem();
	    if (resource != null && dontTouchMyResource == false) {

		if (resource == c_function.getResource())
		    //This resource is already selected
	            return;

		//Show a waring message if settings has been made
		if (!c_function.allFree() && confirmResourceChange) {
		    int selection = JOptionPane.showConfirmDialog(c_parent,
		       warningMessageResource, warningTitle,
                       JOptionPane.YES_NO_OPTION);

		    if (selection == JOptionPane.NO_OPTION) {
			resourceCombo.setSelectedItem(c_function.getResource());
			return;
		    }
		}

		c_function.setResource(resource);

		if (c_function.getDirection() == IN)
		    c_function.setFlows(c_inFlows);
		else
		    c_function.setFlows(c_outFlows);

		updateFlowSettings();
		updateFlowList();
	    }
	}
    }

    /**
     * Called when the direction radio buttons is pressed
     */
    private class InOutListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    String action = e.getActionCommand();

	    if (action.equals(inOutState))
		//This buttons is already selected
		return;

	    //Show a waring message if settings has been made
	    if (!c_function.allFree()) {
		int selection = JOptionPane.showConfirmDialog(c_parent,
		    warningMessageDirection, warningTitle,
                    JOptionPane.YES_NO_OPTION);

		if (selection == JOptionPane.NO_OPTION) {
		    if (inOutState.equals("IN"))
			inRadio.setSelected(true);
		    else
			outRadio.setSelected(true);
		    return;
		}
	    }

	    confirmResourceChange = false;

	    if (action.equals("IN")) {
		c_function.setDirection(IN);
		UpdateAll();
	    }
	    else {
		c_function.setDirection(OUT);
		UpdateAll();
	    }

	    confirmResourceChange = true;

	    inOutState = action;


	}
    }

    /**
     * Called when one of the operator radio buttons is pressed
     */
    private class OperatorListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    String action = e.getActionCommand();
	    if (action.equals("FREE"))
		c_function.setOperator(FREE);
	    else if (action.equals("LTGT"))
		c_function.setOperator(LESS_GREATER);
	    else if (action.equals("LT"))
		c_function.setOperator(LESS);
	    else if (action.equals("GT"))
		c_function.setOperator(GREATER);
	    else if (action.equals("EQ"))
		c_function.setOperator(EQUAL);
	    updateSpin();
	    updateFlowList();
	}
    }

    /**
     * Updates the limit1 and limit2 spin buttons
     */
    private void updateSpin()
    {
	switch(c_function.getOperator()) {
	case FREE:
	    spinLimit1.setVisible(false);
	    spinLimit2.setVisible(false);
        // Added by Nawzad Mardan 20100310
        lblPercentage.setVisible(false);
	    lblSpin.setText("");
	    break;
	case GREATER:
	    spinLimit1.setVisible(true);
	    spinLimit2.setVisible(false);
	    lblSpin.setText("          < X       %");
        // Added by Nawzad Mardan 20100310
        lblPercentage.setVisible(false);
	    break;
	case LESS:
	    spinLimit1.setVisible(false);
	    spinLimit2.setVisible(true);
	    lblSpin.setText("        X <");
        // Added by Nawzad Mardan 20100310
        lblPercentage.setVisible(true);
	    break;
	case EQUAL:
	    spinLimit1.setVisible(false);
	    spinLimit2.setVisible(true);
        spinLimit2.setSize(new Dimension(114, 20));
	    lblSpin.setText("        X =");
        // Added by Nawzad Mardan 20100310
        lblPercentage.setVisible(true);
	    break;
	case LESS_GREATER:
	    spinLimit1.setVisible(true);
	    spinLimit2.setVisible(true);
	    lblSpin.setText("        <X<");
        // Added by Nawzad Mardan 20100310
        lblPercentage.setVisible(true);
	    break;
	}
    }

    /**
     * Updates the flow list (now)
     */
    private void updateFlowListNow()
    {
	c_function.setLimit1(spinLimit1.getFloatValue());
	c_function.setLimit2(spinLimit2.getFloatValue());
	updateFlowList();
    }

     // Only for testing...
    /*
    private static ID r1ID;
    private static ID r2ID;
    private static ID r3ID;
    private static Resource r1;
    private static Resource r2;
    private static Resource r3;

    private Resource debug_toRes(ID id)
    {
	if (id == r1ID) return r1;
	if (id == r2ID) return r2;
	if (id == r3ID) return r3;
	return null;
    }

    public static void main (String args[])
    {
	r1ID = new ID(ID.RESOURCE);
	r2ID = new ID(ID.RESOURCE);
	r3ID = new ID(ID.RESOURCE);
	r1 = new Resource(r1ID,"Vatten","","");
	r2 = new Resource(r2ID,"El","","");
	r3 = new Resource(r3ID,"Potatis och makaronipudding","","");
	Flow f[] = new Flow[3];
	Flow t[] = new Flow[3];
	f[0] = new Flow(new ID(ID.FLOW), "Fl�de 1", r1ID, null);
	f[1] = new Flow(new ID(ID.FLOW), "Fl�de 2", r1ID, null);
	f[2] = new Flow(new ID(ID.FLOW), "Fl�de 3", r2ID, null);
	t[0] = new Flow(new ID(ID.FLOW), "Fl�de 4", r1ID, null);
	t[1] = new Flow(new ID(ID.FLOW), "Fl�de 5", r1ID, null);
	t[2] = new Flow(new ID(ID.FLOW), "Fl�de 6", r3ID, null);
	FlowRelation fr = new FlowRelation();

	//fr.setTimesteplevel(null); // !!!!

	try {
        JDialog dialog = new FlowRelationDialog(new javax.swing.JDialog(),
						true, null,
						fr, null, f, t);
        dialog.show();
	}

	catch (Exception e) {
	    System.out.println("fel=" + e);
	    e.printStackTrace(System.out);
	    System.exit(1);
	}

    }
    */

    //GUI objects
    JLabel lblDescription;
    // Adedd by Nawzad Mardan 20100308
    JTextArea descr;
    JSeparator sep1;

    JPanel pnlLabel;
    JLabel lblLabel;
    JTextField txtLabel;
    JSeparator sep2;

    JPanel pnlTimestep;
    JLabel lblTimestep;
    JLabel lblTSL[];
    SpinButton spinTSL[];

  //  JPanel pnlCopyPaste;
    //JButton btnCopyFrom;
  //  JButton btnCopy;
  //  JButton btnPasteTo;
  //  JButton btnPaste;

    JLabel lblTypeResource;
    JList listFlows;
    JRadioButton inRadio;
    JRadioButton outRadio;
    ButtonGroup inOutButtons;
    JComboBox resourceCombo;
    JPanel pnlFlowSettings;
    JLabel lblSettings;
    JPanel pnlRadioButton;
    NumberField spinLimit1;
    NumberField spinLimit2;
    JRadioButton radioFree;
    JRadioButton radioLt;
    JRadioButton radioEq;
    JRadioButton radioGt;
    JRadioButton radioLtGt;
    ButtonGroup OperatorButtons;
    JPanel pnlFlowHeader;
    JLabel lblSpin;

    JSeparator sep3;
    JPanel pnlButtons;
    JButton btnOk;
    JButton btnCancel;
    // Added by Nawzad Mardan 20100309
    JScrollPane flowListScroll;
    JLabel lblPercentage;
    java.awt.GridBagConstraints gbcPercentage;
}

