/*
 * Copyright 2001:
 * Urban.Liljedahl <urban.liljedahl@sm.luth.se>
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
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.*;
import javax.swing.text.*;

import mind.gui.*;
import mind.model.*;

/**
 * PrintDialog.java
 * The report setup dialog, eventually with subdialogs or tabbed pane
 * with possibility to set the preferences for the report. (What to
 * include, formats)
 *
 * @author Urban Liljedahl
 * @version 2002-08-29
 */
public class PrintDialog extends javax.swing.JDialog
{
    private GUI c_gui;
    private java.awt.Frame c_parent;

    /**
     * Creates the new form ResourcesDialog
     */
    public PrintDialog(java.awt.Frame parent, boolean modal, GUI gui) {
        super(parent, modal);
	setTitle("Print");
	c_gui = gui;
	c_parent = parent;
        initComponents();
        pack();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents()
    {
	//pnlSelection = new JPanel();
	//pnlRadio = new JPanel();
	lblInclude = new JLabel();
	chkHead = new JCheckBox("Document header", true);
	chkNote = new JCheckBox("Node notes", true);
	radAll = new JRadioButton();
	radAll.setSelected(true);
	radSelect = new JRadioButton();
	ButtonGroup group = new ButtonGroup();
	group.add(radAll);
	group.add(radSelect);
	txtSelection = new JTextField(20);
        previewButton = new javax.swing.JButton();
        printButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
	cancelButton = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1;
        addWindowListener(new java.awt.event.WindowAdapter() {
		public void windowClosing(java.awt.event.WindowEvent evt) {
		    closeDialog(evt);
		}
	    }
			  );
        //add include label
	lblInclude.setText("Include in report:");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
	gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints1.insets = new java.awt.Insets(0, 10, 10, 0);
        getContentPane().add(lblInclude, gridBagConstraints1);

	// Add checkbox 1
        chkHead.setText("Header");
	chkHead.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    System.out.println("Check box 1 is " + chkHead.isSelected() );
		}
	    });

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.insets = new java.awt.Insets(0, 10, 10, 0);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(chkHead, gridBagConstraints1);

	// Add checkbox 2
        chkNote.setText("Node notes");
	chkNote.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    System.out.println("Check box 2 is " + chkNote.isSelected() );
		    //disable/enable the radio-group
		}
	    });

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.insets = new java.awt.Insets(0, 10, 10, 0);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(chkNote, gridBagConstraints1);

	// Add radiobutton 1
        radAll.setText("All");
	//radAll.addActionListener (new java.awt.event.ActionListener () {
	//	public void actionPerformed (java.awt.event.ActionEvent evt) {
	//	    System.out.println("Check box 2 is " + chkHead.isSelected() );
	//	    //disable/enable the radio-group
	//	}
	//    });

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.insets = new java.awt.Insets(0, 20, 10, 0);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(radAll, gridBagConstraints1);

	// Add radiobutton 2
        radSelect.setText("Select nodes:");
	radSelect.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    System.out.println("Radiobutton 2 is " + radSelect.isSelected() );
		    //disable/enable the radio-group
		}
	    });

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 4;
        gridBagConstraints1.insets = new java.awt.Insets(0, 20, 10, 0);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(radSelect, gridBagConstraints1);



	// Add textfield
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 4;
        gridBagConstraints1.gridwidth = 3;
        gridBagConstraints1.insets = new java.awt.Insets(0, 10, 10, 0);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(txtSelection, gridBagConstraints1);



	// Add preview button
        previewButton.setText("Preview");
	//previewButton.setAction(((MainDialog) c_parent).getActions().c_printPreview );
	previewButton.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    previewButtonActionPerformed (evt);
		}
	    });

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 5;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        getContentPane().add(previewButton, gridBagConstraints1);

        // Add new print button
        printButton.setText("Print");
	getRootPane().setDefaultButton(printButton);
	printButton.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    printButtonActionPerformed (evt);
		}
	    });


        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 5;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        getContentPane().add(printButton, gridBagConstraints1);


        // Add save button
        saveButton.setText("Save settings");
	//	getRootPane().setDefaultButton(saveButton);
	saveButton.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    saveButtonActionPerformed (evt);
		}
	    });


        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 2;
        gridBagConstraints1.gridy = 5;
	gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(saveButton, gridBagConstraints1);

        // Add Cancel button
        cancelButton.setText("Cancel");
	//getRootPane().setDefaultButton(cancelButton);
	cancelButton.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    cancelButtonActionPerformed (evt);
		}
	    });


        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 3;
        gridBagConstraints1.gridy = 5;
	gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(cancelButton, gridBagConstraints1);

    }
    private void saveButtonActionPerformed (java.awt.event.ActionEvent evt)
    {
	//Future work: connect the report settings to the model
	//closeDialog(null);
    }

    private void cancelButtonActionPerformed (java.awt.event.ActionEvent evt)
    {
	closeDialog(null);
    }


    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        setVisible (false);
        dispose ();
    }

    /**
     * Get the properties for this model and send to printer
     */
    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {
    }

    /**
     * Saves the print settings to c_gui.setPrintSettings() before call to the
     * c_gui.previewAction()
     */
    private void previewButtonActionPerformed(java.awt.event.ActionEvent evt) {
	PrintSettings settings = new PrintSettings();
	//get information from checkboxes and radiobuttons
	boolean[] check = new boolean[10];
	check[0] = chkHead.isSelected();
	check[1] = chkNote.isSelected();
	if( chkNote.isSelected()){
	    //check radio buttons and parse text
	    settings.setPrintSettings( check, c_gui.getNodes() );
	}
	else{
	    settings.setPrintSettings( check, null );
	}

	c_gui.setPrintSettings( settings );
	c_gui.previewAction();
    }

    // Variables declaration
    private JLabel lblInclude;
    private JCheckBox chkHead;
    private JCheckBox chkNote;
    //private JPanel pnlSelection;
    //private JPanel pnlRadio;
    private JRadioButton radAll;
    private JRadioButton radSelect;
    private JTextField txtSelection;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton previewButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton printButton;
}
