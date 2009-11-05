/*
 * Copyright 2002:
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
import java.util.Enumeration;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.*;
import javax.swing.text.*;

import mind.gui.*;
import mind.model.*;


/**
 * PreviewDialog.java
 * Present a report preview. First version shows all node notes in one
 * report. Future work: Possibility to setup preferences what to include
 * in the report, e.g. header, model name, node notes, the graph with
 * nodes, arcs, ids, labels.
 *
 * @author Urban Liljedahl
 * @version 2002-08-29
 */
public class PreviewDialog extends javax.swing.JDialog
{
    private GUI c_gui;
    private java.awt.Frame c_parent;

    /**
     * Creates the new form PreviewDialog with default settings
     */
    public PreviewDialog(java.awt.Frame parent, boolean modal, GUI gui) {
        super(parent, modal);
	setTitle("Preview");
	c_gui = gui;
	c_parent = parent;
	c_reportGenerator = new ReportGenerator(c_gui);
        initComponents();
        pack();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents()
    {
        //printButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

	txtArea = new JTextArea(30,80);
	//txtArea = new JTextArea();
	//txtArea.setPreferredSize( new Dimension(500, 400) );
	String reportText = c_reportGenerator.getReport(ReportGenerator.ASCII);
	txtArea.setText(reportText);
	txtArea.setEditable( false );
	txtArea.setLineWrap( true );
	txtArea.setFont(new Font("Monospaced", Font.PLAIN, 12) );
	JScrollPane scrollpane = new JScrollPane(txtArea);
	scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	pnlReport = new JPanel();
	pnlReport.add(scrollpane);

        getContentPane().setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1;
        addWindowListener(new java.awt.event.WindowAdapter() {
		public void windowClosing(java.awt.event.WindowEvent evt) {
		    closeDialog(evt);
		}
	    }
			  );

	//Add the report panel
	gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
	gridBagConstraints1.gridwidth = 2;
	gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.CENTER;
        getContentPane().add(pnlReport, gridBagConstraints1);

        // Add new print button
	/*
        printButton.setText("Print");
	printButton.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    printButtonActionPerformed (evt);
		}
	    });


        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        getContentPane().add(printButton, gridBagConstraints1);

	*/
        // Add Close button
        closeButton.setText("Close");
	getRootPane().setDefaultButton(closeButton);
	closeButton.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    closeButtonActionPerformed (evt);
		}
	    });


        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 1;
	gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(closeButton, gridBagConstraints1);

    }

    private void closeButtonActionPerformed (java.awt.event.ActionEvent evt)
    {
	closeDialog(null);
    }


    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        setVisible (false);
        dispose ();
    }


    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void previewButtonActionPerformed(java.awt.event.ActionEvent evt) {
    }

    // Variables declaration
    private ReportGenerator c_reportGenerator;
    private JTextArea txtArea;
    private JPanel pnlReport;
    private javax.swing.JButton closeButton;
    //private javax.swing.JButton printButton;

}
