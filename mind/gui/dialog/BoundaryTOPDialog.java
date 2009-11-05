/*
 * Copyright 2002
 * Urban Liljedahl <ul@sm.luth.se>
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
import java.util.Iterator;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import mind.gui.*;
import mind.model.*;
import mind.model.function.*;
import mind.EventHandlerClient;

/*
 * BoundaryTOPDialog.java
 * Created as a first approach to the problem of connecting
 * different timesteps at TOP level.
 *
 * @author Urban Liljedahl
 * @version 2002-09-18
 */

public class BoundaryTOPDialog extends mind.gui.dialog.FunctionDialog {
    private ID c_nodeID;
    private Vector c_resources;
    private GUI c_gui;
    private BoundaryTOP c_function;
    private EventHandlerClient c_eventhandler;


    /** Creates new form */
    public BoundaryTOPDialog(javax.swing.JDialog parent, boolean modal, ID nodeID,
			NodeFunction function, GUI gui) {
	super (parent, modal);
	c_nodeID = nodeID;
	c_gui = gui;
	c_function = (BoundaryTOP) function;
	c_eventhandler = c_gui.getEventHandlerClient();
	initComponents();
	updateResources();
	pack();pack(); // Twice as good!!!
	//setVisible( true );
    }


    private void initComponents(){
	lblDescription = new javax.swing.JLabel ();
	sep1 = new javax.swing.JSeparator ();
	sep2 = new javax.swing.JSeparator ();
	lblMaximumUnit = new javax.swing.JLabel ();
	chkMaximum = new javax.swing.JCheckBox("Maximum");
	chkMaximum.setSelected( c_function.getIsMaximum() );
	txtMaximum = new PositiveNumberField();
	if( c_function.getIsMaximum() ){
	    txtMaximum.setText( new Float(c_function.getMaximum()).toString() );
	}
	else{
	    txtMaximum.setText( "0.0" );
	}
	lblMinimumUnit = new javax.swing.JLabel ();
	chkMinimum = new javax.swing.JCheckBox("Minimum");
	chkMinimum.setSelected( c_function.getIsMinimum() );
	txtMinimum = new PositiveNumberField();
	if( c_function.getIsMinimum() ){
	    txtMinimum.setText( new Float(c_function.getMinimum()).toString() );
	}
	else{
	    txtMinimum.setText( "0.0" );
	}
	pnlLabel = new javax.swing.JPanel ();
	lblLabel = new javax.swing.JLabel ();
	txtLabel = new javax.swing.JTextField ();
	pnlBoundaries = new javax.swing.JPanel();
	sep3 = new javax.swing.JSeparator ();
	pnlButtons = new javax.swing.JPanel ();
	btnOk = new javax.swing.JButton ();
	btnCancel = new javax.swing.JButton ();
	pnlResource = new javax.swing.JPanel ();
	lblTypeResource = new javax.swing.JLabel ();
	listResource = new javax.swing.JList ();
	btnNewResource = new javax.swing.JButton ();
	getContentPane ().setLayout (new java.awt.GridBagLayout ());
	java.awt.GridBagConstraints gridBagConstraints1;
	setName ("BoundaryTOP");
	addWindowListener (new java.awt.event.WindowAdapter () {
		public void windowClosing (java.awt.event.WindowEvent evt) {
		    closeDialog (evt);//call within the Comes app.
		}
	    }
			   );

	lblDescription.setText ("Description: Defines a boundaryTOP for a " +
				"resource.");

	gridBagConstraints1 = new java.awt.GridBagConstraints ();
	gridBagConstraints1.gridwidth = 2;
	gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints1.insets = new java.awt.Insets (10, 10, 10, 10);
	gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
	getContentPane ().add (lblDescription, gridBagConstraints1);

	gridBagConstraints1 = new java.awt.GridBagConstraints ();
	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridwidth = 2;
	gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
	getContentPane ().add (sep1, gridBagConstraints1);

	pnlLabel.setLayout (new java.awt.GridBagLayout ());
	lblLabel.setText ("Label");

	GridBagConstraints gridBagConstraints5 = new GridBagConstraints ();
	//gridBagConstraints5.insets = new java.awt.Insets (10, 10, 10, 10);
	gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
	pnlLabel.add (lblLabel, gridBagConstraints5);

	txtLabel.setText(c_function.getLabel());
	gridBagConstraints5 = new java.awt.GridBagConstraints ();
	gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints5.insets = new java.awt.Insets (0, 10, 0, 0);
	gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
	gridBagConstraints5.weightx = 1.0;
	pnlLabel.add (txtLabel, gridBagConstraints5);

	gridBagConstraints1 = new java.awt.GridBagConstraints ();
	gridBagConstraints1.insets = new java.awt.Insets (10, 10, 10, 10);
	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridy = 2;
	gridBagConstraints1.gridwidth = 2;
	gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints1.insets = new java.awt.Insets (10, 10, 10, 10);
	gridBagConstraints1.weightx = 1.0;
	getContentPane ().add (pnlLabel, gridBagConstraints1);

	gridBagConstraints1 = new java.awt.GridBagConstraints ();
	gridBagConstraints1.gridy = 3;
	gridBagConstraints1.gridwidth = 2;
	gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
	getContentPane ().add (sep2, gridBagConstraints1);


	//Timestep control


	pnlResource.setLayout (new java.awt.GridBagLayout ());
	java.awt.GridBagConstraints gridBagConstraints4;
	lblTypeResource.setText ("Type of resource:");

	gridBagConstraints4 = new java.awt.GridBagConstraints ();
	//      gridBagConstraints4.insets = new java.awt.Insets (10, 10, 0, 10);
	gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
	gridBagConstraints4.anchor = GridBagConstraints.CENTER;
	pnlResource.add (lblTypeResource, gridBagConstraints4);
         // Added by Nawzad Mardan 080910 
      // Add in & out radio button
      pnlRadbut = new JPanel();
      ButtonGroup group = new ButtonGroup();
      group.add(c_radIn);
      group.add(c_radOut);
      pnlRadbut.add(c_radIn);
      pnlRadbut.add(c_radOut);
      if (c_function.isRadOut())
	    c_radOut.setSelected(true);
	else
	    c_radIn.setSelected(true);
      java.awt.GridBagConstraints gridBagConstraints44;
      gridBagConstraints44 = new java.awt.GridBagConstraints ();
      gridBagConstraints44.anchor = java.awt.GridBagConstraints.WEST;
      //gridBagConstraints44.weightx = 1.0;
      gridBagConstraints44.gridx = 0;
     pnlResource.add (pnlRadbut, gridBagConstraints44);

	//      listResource.setPreferredSize (new java.awt.Dimension(10, 50));
	listResource.addListSelectionListener(new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent e)
		{
		    newResourceSelected();
		}
	    });

	gridBagConstraints4 = new java.awt.GridBagConstraints ();
	gridBagConstraints4.gridx = 0;
	//      gridBagConstraints4.insets = new java.awt.Insets (4, 10, 9, 10);

	JScrollPane scrollPane = new JScrollPane(listResource);
	scrollPane.setPreferredSize(new Dimension(150, 100));
	pnlResource.add (scrollPane, gridBagConstraints4);



	gridBagConstraints1 = new java.awt.GridBagConstraints ();
	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridy = 6;
	gridBagConstraints1.insets = new java.awt.Insets (10, 10, 10, 5);
	getContentPane ().add (pnlResource, gridBagConstraints1);

	pnlBoundaries.setLayout(new GridBagLayout());
	pnlBoundaries.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	gridBagConstraints1 = new GridBagConstraints();
	gridBagConstraints1.weightx = 1.0;
	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridy = 0;
	gridBagConstraints1.gridwidth = 3;
	gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
	gridBagConstraints1.anchor = GridBagConstraints.WEST;
	pnlBoundaries.add(chkMinimum, gridBagConstraints1);

	gridBagConstraints1 = new GridBagConstraints();
	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridy = 1;
	pnlBoundaries.add(Box.createHorizontalStrut(10), gridBagConstraints1);

	txtMinimum.setColumns(20);
	gridBagConstraints1.gridx = 1;
	gridBagConstraints1.gridy = 1;
	pnlBoundaries.add(txtMinimum, gridBagConstraints1);

	gridBagConstraints1.gridx = 2;
	gridBagConstraints1.gridy = 1;
	pnlBoundaries.add(lblMinimumUnit);

	gridBagConstraints1 = new GridBagConstraints();
	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridy = 2;
	gridBagConstraints1.gridwidth = 3;
	pnlBoundaries.add(Box.createHorizontalStrut(10), gridBagConstraints1);

	gridBagConstraints1 = new GridBagConstraints();
	gridBagConstraints1.weightx = 1.0;
	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridy = 3;
	gridBagConstraints1.gridwidth = 3;
	gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
	gridBagConstraints1.anchor = GridBagConstraints.WEST;
	pnlBoundaries.add(chkMaximum, gridBagConstraints1);

	gridBagConstraints1 = new GridBagConstraints();
	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridy = 4;
	pnlBoundaries.add(Box.createHorizontalStrut(10), gridBagConstraints1);

	txtMaximum.setColumns(20);
	gridBagConstraints1.gridx = 1;
	gridBagConstraints1.gridy = 4;
	pnlBoundaries.add(txtMaximum, gridBagConstraints1);

	gridBagConstraints1.gridx = 1;
	gridBagConstraints1.gridy = 4;
	pnlBoundaries.add(lblMaximumUnit);

	gridBagConstraints1 = new java.awt.GridBagConstraints ();
	gridBagConstraints1.gridx = 1;
	gridBagConstraints1.gridy = 6;
	gridBagConstraints1.weightx = 1.0;
	gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
	gridBagConstraints1.insets = new java.awt.Insets (10, 5, 10, 10);
	getContentPane ().add (pnlBoundaries, gridBagConstraints1);

	gridBagConstraints1 = new java.awt.GridBagConstraints ();
	gridBagConstraints1.gridy = 7;
	gridBagConstraints1.gridwidth = 2;
	gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
	getContentPane ().add (sep3, gridBagConstraints1);


	btnOk.setText ("OK");
	getRootPane().setDefaultButton(btnOk);
	btnOk.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    btnOkActionPerformed (evt);
		}
	    });

	pnlButtons.add (btnOk);

	btnCancel.setText ("Cancel");
	btnCancel.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    btnCancelActionPerformed (evt);
		}
	    });

	pnlButtons.add (btnCancel);

	gridBagConstraints1 = new java.awt.GridBagConstraints ();
	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridy = 8;
	gridBagConstraints1.gridwidth = 2;
	gridBagConstraints1.insets = new java.awt.Insets (10, 10, 10, 10);
	gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
	getContentPane ().add (pnlButtons, gridBagConstraints1);

    }

    private void btnCancelActionPerformed (java.awt.event.ActionEvent evt) {
	setVisible(false);
	dispose();
    }

    private void btnOkActionPerformed (java.awt.event.ActionEvent evt)
    {
	// save the function label
	c_function.setLabel(txtLabel.getText());
        // Added by Nawzad Mardan 080910
        c_function.setRadIn(c_radIn.isSelected());
        // Added by Nawzad Mardan 080910
        c_function.setRadOut(c_radOut.isSelected());

	Resource resource = (Resource)listResource.getSelectedValue();
	if (resource != null)
	    c_function.setResource(resource.getID());

	//save boundary values
	if( chkMinimum.isSelected() ){
	    c_function.setMinimum( txtMinimum.getFloatValue() );
	    c_function.setIsMinimum( true );
	}
	if( chkMaximum.isSelected() ){
	    c_function.setMaximum( txtMaximum.getFloatValue() );
	    c_function.setIsMaximum( true );
	}

	// close the dialog
	closeDialog(null);
    }

    private void newResourceSelected()
    {
	Resource resource = (Resource)listResource.getSelectedValue();
    }

    private void closeDialog(java.awt.event.WindowEvent evt) {
	setVisible (false);
	dispose ();
    }

    private void updateResources()
    {
	c_resources = c_gui.getResources();

	listResource.setListData(c_resources);
	if (c_function.getResource() != null)
	    listResource.setSelectedIndex(c_resources.indexOf(c_gui.getResource(c_function.getResource())));
    }

    // Variables declaration
    private javax.swing.JLabel lblDescription;
    private javax.swing.JSeparator sep1;
    private javax.swing.JSeparator sep2;
    //private javax.swing.JPanel pnlTimestep;
    //private javax.swing.JLabel lblTSL[];
    //private SpinButton spinTSL[];
    private PositiveNumberField txtMaximum;
    private PositiveNumberField txtMinimum;
    private javax.swing.JLabel lblMaximumUnit;
    private javax.swing.JCheckBox chkMaximum;
    private javax.swing.JLabel lblMinimumUnit;
    private javax.swing.JCheckBox chkMinimum;
    private javax.swing.JPanel pnlBoundaries;
    private javax.swing.JPanel pnlCopyPaste;
    private javax.swing.JButton btnCopyFrom;
    //private javax.swing.JButton btnCopy;
    //private javax.swing.JButton btnPasteTo;
    //private javax.swing.JButton btnPaste;
    //private javax.swing.JLabel lblTimestep;
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
    // Added by Nawzad Mardan 080910
    private JRadioButton c_radIn = new JRadioButton("In");
    private JRadioButton c_radOut = new JRadioButton("Out");
    private javax.swing.JPanel pnlRadbut;
    // End of variables declaration

    //test
    private int getNumberOfTimesteps(){
	//calculate number of timesteps in this node
	//call to static method in class GUI referring to latest created instance of gui

	Timesteplevel thisTsl = c_gui.getTimesteplevel(c_nodeID);
	Timesteplevel topTsl = thisTsl.getTopLevel();
	Timesteplevel curTsl = thisTsl.getPrevLevel();
	int res = thisTsl.getTimesteps();
	while(curTsl != topTsl && curTsl != null){
	    res *= curTsl.getTimesteps();
	    curTsl = curTsl.getPrevLevel();
	    System.out.println("res: " + res);
	}
	System.out.println("The timestep level of node containing this function: " + thisTsl);
	System.out.println("Resolution: " + res);
	return res;
    }
    //predict variable names in mps file
    private void predictNames(){
	int res = getNumberOfTimesteps();
	//FxT1, FxT2, ... , FxT(res)
	ID resource = c_function.getResource();
	Flow[] inflow = c_gui.getInFlows(c_nodeID);
	//filter flows. Only flows of current resource of interest
	inflow = filter(inflow, resource);
	//build all names
	System.out.println("Prediction of names*****************************");
	for(int tstep=1; tstep<=res; tstep++){
	    for(int i=0; i<inflow.length; i++){
		//build variable with flowid + T + tstep
		String var = inflow[i].getID() + "T" + tstep;
		System.out.println(var);
	    }
	}
    }
    private Flow[] filter(Flow[] inflow, ID resource){
	Vector newFlows = new Vector();
	for(int i=0; i<inflow.length; i++){
	    //test a match
	    if( inflow[i].getResource()==resource){
		newFlows.addElement(inflow[i]);
	    }
	}
	Flow[] newFlow = new Flow[newFlows.size()];
	Iterator list = newFlows.iterator();
	int index = 0;
	while( list.hasNext()){
	    newFlow[index]=(Flow)list.next();
	    index++;
	}
	return newFlow;
    }

}
