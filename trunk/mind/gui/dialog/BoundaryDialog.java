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

import mind.gui.*;
import mind.model.*;
import mind.model.function.*;
import mind.EventHandlerClient;

/*
 * BoundaryDialog.java
 *
 * Created on den 25 april 2001, 13:02
 */

/**
 *
 * @author Tim Terlegård
 * @author Johan Trygg
 * @author  Per Fredriksson
 * @author 	Tor Knutsson
 * @version 2007-12-10
 */

public class BoundaryDialog extends mind.gui.dialog.FunctionDialog {
    private ID c_nodeID;
    private Vector c_resources;
    private GUI c_gui;
    private Boundary c_function;
    private EventHandlerClient c_eventhandler;
    private Timesteplevel tsl[];
    private int c_timestep; //Active timestep
    private int c_timesteplevels; //Number of timesteplevels

    /** Creates new form */
    public BoundaryDialog(javax.swing.JDialog parent, boolean modal, ID nodeID,
			NodeFunction function, GUI gui) {
	super (parent, modal);
	c_nodeID = nodeID;
	c_gui = gui;
	c_function = (Boundary) function;
	c_eventhandler = c_gui.getEventHandlerClient();

	//calculate number of timestepslevels in the function
	c_timesteplevels = 1;
	Timesteplevel level = c_eventhandler.getTopTimesteplevel();
	Timesteplevel thisLevel = c_function.getTimesteplevel();
	while (level != thisLevel) {
	    c_timesteplevels++;
	    level = level.getNextLevel();
	}

	//Get all timesteplevels
	tsl = new Timesteplevel[c_timesteplevels];
	level = c_eventhandler.getTopTimesteplevel();
	for(int j=0; j<c_timesteplevels; j++) {
	    tsl[j] = level;
	    level = level.getNextLevel();
	}

	//Set the current timestep to 1
	c_timestep = 1;
	c_function.setTimestep(c_timestep);

	initComponents ();
	txtLabel.setText(c_function.getLabel());
	updateResources();

	//Initiate values for timestep 1
	c_function.setTimestep(c_timestep);
	if (c_function.isMin()) {
	    chkMinimum.setSelected(true);
	    txtMinimum.setText(Float.toString(c_function.getMin()));
	}
	else
	    chkMinimum.setSelected(false);

	if (c_function.isMax()) {
	    chkMaximum.setSelected(true);
	    txtMaximum.setText(Float.toString(c_function.getMax()));
	}
	else
	    chkMaximum.setSelected(false);

	pack();
    }

    private void updateResources()
    {
	c_resources = c_gui.getResources();

	listResource.setListData(c_resources);
	if (c_function.getResource() != null)
	    listResource.setSelectedIndex(c_resources.indexOf(c_gui.getResource(c_function.getResource())));
    }


    public void updateTimestep()
    {
        //Save value for previous timestep
	c_function.setTimestep(c_timestep);

	if (chkMinimum.isSelected()) {
	    c_function.setIsMin(true);
	    c_function.setMin(txtMinimum.getFloatValue());
	}
	else {
	    c_function.setIsMin(false);
	}

	if (chkMaximum.isSelected()) {
	    c_function.setIsMax(true);
	    c_function.setMax(txtMaximum.getFloatValue());
	}
	else {
	    c_function.setIsMax(false);
	}

	//Calculate new timestep
	c_timestep=1; //The first timestep is 1 (not 0)
	int factor=1;
	for(int i=c_timesteplevels-1; i>0; i--) {
	    c_timestep = c_timestep + (spinTSL[i].getValue() - 1) * factor;
	    factor = factor * tsl[i].getMaxTimesteps();
	}

	//Get value for new timestep
	c_function.setTimestep(c_timestep);
	if (c_function.isMin()) {
	    chkMinimum.setSelected(true);
	    txtMinimum.setText(Float.toString(c_function.getMin()));
	}
	else {
	    chkMinimum.setSelected(false);
	    txtMinimum.setText("");
	}
	if (c_function.isMax()) {
	    chkMaximum.setSelected(true);
	    txtMaximum.setText(Float.toString(c_function.getMax()));
	}
	else {
	    chkMaximum.setSelected(false);
	    txtMaximum.setText(" ");
	}
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */

    private void initComponents () {
	lblDescription = new javax.swing.JLabel ();
	sep1 = new javax.swing.JSeparator ();
	sep2 = new javax.swing.JSeparator ();
	pnlTimestep = new javax.swing.JPanel ();
	lblMaximumUnit = new javax.swing.JLabel ();
	chkMaximum = new javax.swing.JCheckBox("Maximum");
	txtMaximum = new PositiveNumberField();
	lblMinimumUnit = new javax.swing.JLabel ();
	chkMinimum = new javax.swing.JCheckBox("Minimum");
	txtMinimum = new PositiveNumberField();
	pnlCopyPaste = new javax.swing.JPanel ();
	btnCopyFrom = new javax.swing.JButton ();
	btnCopy = new javax.swing.JButton ();
	btnPasteTo = new javax.swing.JButton ();
	btnPaste = new javax.swing.JButton ();
	lblTimestep = new javax.swing.JLabel ();
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
	setName ("Boundary");
	addWindowListener (new java.awt.event.WindowAdapter () {
		public void windowClosing (java.awt.event.WindowEvent evt) {
		    closeDialog (evt);
		}
	    }
			   );

	lblDescription.setText ("Description: Defines a boundary for a " +
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

	//Label "Timestep"
	lblTimestep = new javax.swing.JLabel();
	lblTimestep.setText("Timestep:");
	gridBagConstraints1 = new java.awt.GridBagConstraints();
	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridy = 4;
	gridBagConstraints1.insets = new java.awt.Insets (10, 10, 10, 5);
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

	for(int i=0; i<c_timesteplevels; i++)
	{
	    //Add timestep labels
	    gridBagConstraints1 = new java.awt.GridBagConstraints();
	    gridBagConstraints1.gridx = 0;
	    gridBagConstraints1.gridy = i;
	    gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;

	    lblTSL[i] = new javax.swing.JLabel();
	    lblTSL[i].setText(level.getLabel());
	    pnlTimestep.add(lblTSL[i],gridBagConstraints1);

	    //Add timestep spinbuttons
	    gridBagConstraints1 = new java.awt.GridBagConstraints();
	    gridBagConstraints1.gridx = 1;
	    gridBagConstraints1.gridy = i;
	    gridBagConstraints1.insets = new java.awt.Insets (0, 15, 0, 0);
	    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gridBagConstraints1.weightx = 1.0;

	    spinTSL[i] = new SpinButton(1,level.getMaxTimesteps(),1,1);
	    pnlTimestep.add(spinTSL[i],gridBagConstraints1);

	    spinTSL[i].addListener(new SpinButtonListener()
	    {
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


	//Copy & Paste
	pnlCopyPaste.setLayout (new java.awt.GridBagLayout ());
	java.awt.GridBagConstraints gridBagConstraints3;

	btnCopyFrom.setEnabled(false);
	btnCopyFrom.setText ("Copy from range");
	btnCopyFrom.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    btnCopyFromActionPerforme (evt);
		    copyFromRange (evt);
          btnCopyFromActionPerformed (evt);
		}
	    });

       	gridBagConstraints3 = new java.awt.GridBagConstraints ();
	//	gridBagConstraints3.insets = new java.awt.Insets (10, 10, 10, 10);
	pnlCopyPaste.add (btnCopyFrom, gridBagConstraints3);

	btnCopy.setEnabled(false);
	btnCopy.setPreferredSize (new java.awt.Dimension(85, 27));
	btnCopy.setText ("Copy");
	btnCopy.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    btnCopyActionPerformed (evt);
		}
	    });

	gridBagConstraints3 = new java.awt.GridBagConstraints ();
	gridBagConstraints3.insets = new java.awt.Insets (10, 10, 10, 10);
	pnlCopyPaste.add (btnCopy, gridBagConstraints3);

	btnPasteTo.setEnabled(false);
	btnPasteTo.setPreferredSize (new java.awt.Dimension(117, 27));
	btnPasteTo.setText ("Paste to range");
	btnPasteTo.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    btnPasteToRangeActionPerformed (evt);
		}
	    });

	gridBagConstraints3 = new java.awt.GridBagConstraints ();
	gridBagConstraints3.gridx = 0;
	//	gridBagConstraints3.insets = new java.awt.Insets (10, 10, 10, 10);
	pnlCopyPaste.add (btnPasteTo, gridBagConstraints3);

	btnPaste.setEnabled(false);
	btnPaste.setPreferredSize (new java.awt.Dimension(85, 27));
	btnPaste.setText ("Paste");
	btnPaste.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    btnPasteActionPerformed (evt);
		}
	    });

	gridBagConstraints3 = new java.awt.GridBagConstraints ();
	gridBagConstraints3.gridx = 1;
	//	gridBagConstraints3.insets = new java.awt.Insets (10, 10, 10, 10);
	pnlCopyPaste.add (btnPaste, gridBagConstraints3);

	gridBagConstraints1 = new java.awt.GridBagConstraints ();
	gridBagConstraints1.gridx = 1;
	gridBagConstraints1.gridy = 4;
	gridBagConstraints1.insets = new java.awt.Insets (0, 6, 0, 10);
	gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
	getContentPane ().add (pnlCopyPaste, gridBagConstraints1);
	/*
	gridBagConstraints1 = new java.awt.GridBagConstraints ();
	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridy = 4;
	gridBagConstraints1.insets = new java.awt.Insets (10, 10, 5, 10);
	gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
	getContentPane ().add (lblTimestep, gridBagConstraints1);
	*/

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

/*
      //Do we really want to be able to create resources from inside this dialog?
      //If we can, a created resource should be deleted when selecting cancel.

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

      //Save values for the selected timestep
      updateTimestep();

      // close the dialog
      closeDialog(null);
  }

/*
  private void btnNewResourceActionPerformed (java.awt.event.ActionEvent evt) {
      // show dialog for making new resource
      JDialog dialog = new NewResourceDialog(this, true, c_gui);

      int height = getLocation().y + getSize().height/2;
      int width = getLocation().x + getSize().width/2;
      int x = (int) (width - dialog.getSize().width/2);
      int y = (int) (height - dialog.getSize().height/2);
      dialog.setLocation(x, y);
      dialog.show();

      // update the resource list
      updateResources();
  }
*/

  private void newResourceSelected()
  {
      Resource resource = (Resource)listResource.getSelectedValue();
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
  private void closeDialog(java.awt.event.WindowEvent evt) {
    setVisible (false);
    dispose ();
  }


  // Variables declaration
  private javax.swing.JLabel lblDescription;
  private javax.swing.JSeparator sep1;
  private javax.swing.JSeparator sep2;
  private javax.swing.JPanel pnlTimestep;
  private javax.swing.JLabel lblTSL[];
  private SpinButton spinTSL[];
  private PositiveNumberField txtMaximum;
  private PositiveNumberField txtMinimum;
  private javax.swing.JLabel lblMaximumUnit;
  private javax.swing.JCheckBox chkMaximum;
  private javax.swing.JLabel lblMinimumUnit;
  private javax.swing.JCheckBox chkMinimum;
  private javax.swing.JPanel pnlBoundaries;
  private javax.swing.JPanel pnlCopyPaste;
  private javax.swing.JButton btnCopyFrom;
  private javax.swing.JButton btnCopy;
  private javax.swing.JButton btnPasteTo;
  private javax.swing.JButton btnPaste;
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
  // Added by Nawzad Mardan 080910
  private JRadioButton c_radIn = new JRadioButton("In");
  private JRadioButton c_radOut = new JRadioButton("Out");
  javax.swing.JPanel pnlRadbut;
  
  // End of variables declaration

}
