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
 * The dialog for adding a timestep
 * @author Johan Trygg
 * @version 2001-06-19
 */

/** @todo This class is obsolete. REMOVE!  */

package mind.gui.dialog;

import mind.*;
import mind.gui.*;
import mind.model.*;
import javax.swing.*;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @version 1.0
 * @deprecated Use AddTimestepDialog2 instead
 */

public class AddTimestepDialog extends javax.swing.JDialog
{
    private GUI c_gui;
    private EventHandlerClient c_eventHandlerClient;
    private javax.swing.JPanel pnlName;
    private javax.swing.JLabel lblName;
    private javax.swing.JTextField txtName;
    private javax.swing.JPanel pnlSteps;
    private javax.swing.JLabel lblSteps;
    private javax.swing.JTextField txtSteps;
    private javax.swing.JPanel pnlPrefix;
    private javax.swing.JLabel lblPrefix;
    private javax.swing.JTextField txtPrefix;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnCancel;
    private javax.swing.JSeparator sepNewResource;

    /** Creates new form */
    public AddTimestepDialog(javax.swing.JDialog parent, boolean modal,
			     GUI gui)
    {
	super(parent, modal);
	c_gui = gui;
	c_eventHandlerClient = gui.getEventHandlerClient();
	initComponents();
	pack();
    }

    private void initComponents ()
    {
	pnlName = new javax.swing.JPanel();
	lblName = new javax.swing.JLabel();
	txtName = new javax.swing.JTextField();
	pnlSteps = new javax.swing.JPanel();
	lblSteps = new javax.swing.JLabel();
	txtSteps = new javax.swing.JTextField();
	pnlPrefix = new javax.swing.JPanel();
	lblPrefix = new javax.swing.JLabel();
	txtPrefix = new javax.swing.JTextField();
	pnlButtons = new javax.swing.JPanel();
	btnOK = new javax.swing.JButton();
	btnCancel = new javax.swing.JButton();
	sepNewResource = new javax.swing.JSeparator();
	getContentPane().setLayout (new java.awt.GridBagLayout());
	java.awt.GridBagConstraints gridBagConstraints1;
	addWindowListener(new java.awt.event.WindowAdapter() {
		public void windowClosing(java.awt.event.WindowEvent evt) {
		    closeDialog(evt);
		}
	    });

	//Name
	pnlName.setLayout(new java.awt.GridBagLayout());
	java.awt.GridBagConstraints gridBagConstraints2;

	lblName.setText("Name:");

	gridBagConstraints2 = new java.awt.GridBagConstraints();
	gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
	pnlName.add(lblName, gridBagConstraints2);

	gridBagConstraints2 = new java.awt.GridBagConstraints();
	gridBagConstraints2.gridx = 0;
	gridBagConstraints2.gridy = 1;
	gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints2.insets = new java.awt.Insets(3, 0, 0, 0);
	gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
	gridBagConstraints2.weightx = 1.0;
	pnlName.add(txtName, gridBagConstraints2);

	gridBagConstraints1 = new java.awt.GridBagConstraints();
	gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
	gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
	gridBagConstraints1.weightx = 1.0;
	getContentPane().add(pnlName, gridBagConstraints1);

	//Number of steps
	pnlSteps.setLayout(new java.awt.GridBagLayout());
	java.awt.GridBagConstraints gridBagConstraints3;

	lblSteps.setText ("Divides " +
			  c_eventHandlerClient.getLastTimesteplevel().getLabel() +
			  " in this many number of steps:");

	gridBagConstraints3 = new java.awt.GridBagConstraints();
	gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
	pnlSteps.add(lblSteps, gridBagConstraints3);

	gridBagConstraints3 = new java.awt.GridBagConstraints();
	gridBagConstraints3.gridx = 0;
	gridBagConstraints3.gridy = 1;
	gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints3.insets = new java.awt.Insets(3, 0, 0, 0);
	gridBagConstraints3.weightx = 1.0;
	pnlSteps.add(txtSteps, gridBagConstraints3);

	gridBagConstraints1 = new java.awt.GridBagConstraints();
	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridy = 1;
	gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
	gridBagConstraints1.weightx = 1.0;
	getContentPane().add(pnlSteps, gridBagConstraints1);

	//OK button
	btnOK.setText("OK");
	getRootPane().setDefaultButton(btnOK);
	btnOK.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
		    btnOKActionPerformed(evt);
		}
	    });

	pnlButtons.add (btnOK);

	//Cancel button
	btnCancel.setText("Cancel");
	btnCancel.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
		    btnCancelActionPerformed(evt);
		}
	    });
	pnlButtons.add (btnCancel);

	//Ok and Cancel buttons pandel
	gridBagConstraints1 = new java.awt.GridBagConstraints();
	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridy = 4;
	gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
	gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
	gridBagConstraints1.weightx = 1.0;
	getContentPane().add(pnlButtons, gridBagConstraints1);

	sepNewResource.setPreferredSize(new java.awt.Dimension(10, 2));

	//The line over the ok and cancel button
	gridBagConstraints1 = new java.awt.GridBagConstraints();
	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridy = 3;
	gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints1.weightx = 1.0;
	getContentPane().add(sepNewResource, gridBagConstraints1);
    }

    private void btnCancelActionPerformed (java.awt.event.ActionEvent evt)
    {
	closeDialog(null);
    }

    private void btnOKActionPerformed (java.awt.event.ActionEvent evt)
    {
	int steps;
	try {
	    steps = Integer.parseInt(txtSteps.getText());
	}
	catch (NumberFormatException e) {
	    c_gui.showWarningDialog("Add Timesteplevel",
				    "Only integers > 0 is allowed in the 'devides' field!");
	    return;
	}
	if (steps < 1)
	{
	    c_gui.showWarningDialog("Add Timesteplevel",
				    "Only integers > 0 is allowed in the 'devides' field!");
	    return;
	}
	boolean ok = c_eventHandlerClient.addTimesteplevel(txtName.getText(), steps, null, null);
	if (!ok) {
	    c_gui.showWarningDialog("Add Timesteplevel",
				    "A timesteplevel with this name already exists.");
	    return;
	}
	closeDialog(null);
    }

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt)
    {
	setVisible(false);
	dispose();
    }

}
