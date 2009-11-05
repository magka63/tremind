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
package mind.gui.dialog;

import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

import mind.gui.*;
import mind.model.*;

/**
 * Dialog to keep track of a nodes properties and change them at need
 *
 * @author  Tim Terlegård
 * @version 2001-07-24
 */
public class NodePropertiesDialog
    extends JDialog implements MouseListener
{
    private GUI c_gui;
    private ID c_nodeID;
    // A Vector of function IDs
    private Vector c_functions;

    private String c_undoTimestepLevel = null;

    /** Creates new form NodePropertiesDialog */
    public NodePropertiesDialog(java.awt.Frame parent,boolean modal,
				GUI gui, ID nodeID) {
	super (parent, modal);
	c_gui = gui;
	c_nodeID = nodeID;

	String selected = c_gui.getTimesteplevel(nodeID).getLabel();
	Vector levels = c_gui.getTimesteplevelsVector();
	cmbLevel = new JComboBox(levels);
	cmbLevel.setSelectedItem(selected);
	c_undoTimestepLevel = selected;
	initComponents ();
	listFunctions.setListData(c_gui.getAllFunctionLabels(nodeID));
        listFunctions.addMouseListener(this);

	pack ();
        pack();
    }

    private void initComponents () {
	jTabbedPane1 = new JTabbedPane ();
	pnlGeneral = new JPanel ();
	lblID = new JLabel ();
	lblIDText = new JLabel ();
	lblTimestep = new JLabel ();
	lblLevel = new JLabel ();
	cmbTimestep = new JComboBox ();
	//	cmbLevel = new JComboBox ();
	lblLabel = new JLabel ();
	txtLabel = new JTextField ();
	pnlFunctions = new JPanel ();
	pnlButtons = new JPanel ();
	listFunctions = new JList ();
	//2002-12-16 scrollbars for listFunctions
	listpane = new JScrollPane();
	btnAdd = new JButton ();
	btnRemove = new JButton ();
	btnProperties = new JButton ();
	pnlProperties = new JPanel ();
	lblProperties = new JLabel ();
	pnlPosition = new JPanel ();
	jPanel8 = new JPanel ();
	lblX = new JLabel ();
	txtX = new JTextField ();
	lblY = new JLabel ();
	txtY = new JTextField ();
	pnlOkButtons = new JPanel ();
	btnOK = new JButton ();
	btnCancel = new JButton ();
	getContentPane ().setLayout (new java.awt.GridBagLayout ());
	java.awt.GridBagConstraints gridBagConstraints1;
	addWindowListener (new java.awt.event.WindowAdapter () {
		public void windowClosing (java.awt.event.WindowEvent evt) {
		    closeDialog (evt);
		}
	    }
			   );


	pnlGeneral.setLayout (new java.awt.GridBagLayout ());
	java.awt.GridBagConstraints gridBagConstraints2;

        lblID.setText ("ID:");

        gridBagConstraints2 = new java.awt.GridBagConstraints ();
        gridBagConstraints2.insets = new java.awt.Insets (10, 10, 5, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlGeneral.add (lblID, gridBagConstraints2);


        gridBagConstraints2 = new java.awt.GridBagConstraints ();
        gridBagConstraints2.insets = new java.awt.Insets (10, 6, 5, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        pnlGeneral.add (lblIDText, gridBagConstraints2);

	lblIDText.setText(c_nodeID.toString());
        lblTimestep.setText ("Timestep:");

        gridBagConstraints2 = new java.awt.GridBagConstraints ();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.insets = new java.awt.Insets (10, 10, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
	//        pnlGeneral.add (lblTimestep, gridBagConstraints2);

        lblLevel.setText ("Level of timestep:");
        gridBagConstraints2 = new java.awt.GridBagConstraints ();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 3;
        gridBagConstraints2.insets = new java.awt.Insets (10, 10, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
	pnlGeneral.add (lblLevel, gridBagConstraints2);

	/*
        gridBagConstraints2 = new java.awt.GridBagConstraints ();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.insets = new java.awt.Insets (10, 6, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
	pnlGeneral.add (cmbTimestep, gridBagConstraints2);
	*/

        gridBagConstraints2 = new java.awt.GridBagConstraints ();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 3;
        gridBagConstraints2.insets = new java.awt.Insets (10, 6, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
	cmbLevel.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e)
		{
		    c_gui.setTimesteplevel(c_nodeID, (String) cmbLevel.
					   getSelectedItem());
		}
	    });
        pnlGeneral.add (cmbLevel, gridBagConstraints2);

        lblLabel.setText ("Label:");
        gridBagConstraints2 = new java.awt.GridBagConstraints ();
        gridBagConstraints2.gridx = 0;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.insets = new java.awt.Insets (10, 10, 10, 10);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        pnlGeneral.add (lblLabel, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints ();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.gridy = 1;
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets (10, 6, 10, 10);
	txtLabel.setColumns(20);
        pnlGeneral.add (txtLabel, gridBagConstraints2);
	txtLabel.setText(c_gui.getLabel(c_nodeID));

	jTabbedPane1.addTab ("General", pnlGeneral);

	pnlFunctions.setLayout (new java.awt.GridBagLayout ());
	java.awt.GridBagConstraints gridBagConstraints3;

        pnlButtons.setLayout (new java.awt.GridBagLayout ());
        java.awt.GridBagConstraints gridBagConstraints4;

	listFunctions.setPreferredSize (new java.awt.Dimension(130, 1000));
	listFunctions.setMinimumSize (new java.awt.Dimension(30, 0));
	listFunctions.setSelectionMode (ListSelectionModel.SINGLE_SELECTION);
	//add listFunctions to the scrollpane
	listpane.setPreferredSize(new java.awt.Dimension(150, 170));
	listpane.getViewport().setView(listFunctions);

	gridBagConstraints4 = new java.awt.GridBagConstraints ();
	gridBagConstraints4.gridheight = 3;
	gridBagConstraints4.insets = new java.awt.Insets (10, 10, 10, 10);
	pnlButtons.add (listpane, gridBagConstraints4);

	btnAdd.setPreferredSize (new java.awt.Dimension(85, 27));
	btnAdd.setMaximumSize (new java.awt.Dimension(85, 27));
	btnAdd.setText ("Add...");
	btnAdd.setMinimumSize (new java.awt.Dimension(85, 27));
	btnAdd.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    btnAddActionPerformed (evt);
		}
	    });

	gridBagConstraints4 = new java.awt.GridBagConstraints ();
	gridBagConstraints4.insets = new java.awt.Insets (10, 0, 0, 10);
	pnlButtons.add (btnAdd, gridBagConstraints4);

	btnRemove.setPreferredSize (new java.awt.Dimension(85, 27));
	btnRemove.setMaximumSize (new java.awt.Dimension(85, 27));
	btnRemove.setText ("Remove");
	btnRemove.setMinimumSize (new java.awt.Dimension(85, 27));
	btnRemove.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    btnRemoveActionPerformed (evt);
		}
	    });

	gridBagConstraints4 = new java.awt.GridBagConstraints ();
	gridBagConstraints4.gridx = 1;
	gridBagConstraints4.insets = new java.awt.Insets (10, 0, 0, 10);
	pnlButtons.add (btnRemove, gridBagConstraints4);

	btnProperties.setText ("Properties");
	btnProperties.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    btnPropertiesActionPerformed (evt);
		}
	    }
					 );

	gridBagConstraints4 = new java.awt.GridBagConstraints ();
	gridBagConstraints4.gridx = 1;
	gridBagConstraints4.insets = new java.awt.Insets (10, 0, 10, 10);
	pnlButtons.add (btnProperties, gridBagConstraints4);

        gridBagConstraints3 = new java.awt.GridBagConstraints ();
        pnlFunctions.add (pnlButtons, gridBagConstraints3);

        pnlProperties.setBorder (new TitledBorder("Function properties"));


	pnlProperties.add (lblProperties);

        gridBagConstraints3 = new java.awt.GridBagConstraints ();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.insets = new java.awt.Insets (4, 10, 10, 10);
        pnlFunctions.add (pnlProperties, gridBagConstraints3);

	jTabbedPane1.addTab ("Functions", pnlFunctions);

	pnlPosition.setLayout (new java.awt.GridBagLayout ());
	java.awt.GridBagConstraints gridBagConstraints5;

        jPanel8.setLayout (new java.awt.GridBagLayout ());
        java.awt.GridBagConstraints gridBagConstraints6;

	lblX.setText ("X:");

	gridBagConstraints6 = new java.awt.GridBagConstraints ();
	gridBagConstraints6.insets = new java.awt.Insets (10, 10, 10, 10);
	jPanel8.add (lblX, gridBagConstraints6);

	txtX.setColumns (4);
	txtX.setText("" + c_gui.getX(c_nodeID));

	gridBagConstraints6 = new java.awt.GridBagConstraints ();
	gridBagConstraints6.insets = new java.awt.Insets (10, 10, 10, 10);
	jPanel8.add (txtX, gridBagConstraints6);

	lblY.setText ("Y:");

	gridBagConstraints6 = new java.awt.GridBagConstraints ();
	gridBagConstraints6.gridx = 0;
	gridBagConstraints6.gridy = 1;
	gridBagConstraints6.insets = new java.awt.Insets (10, 10, 10, 10);
	jPanel8.add (lblY, gridBagConstraints6);

	txtY.setColumns (4);
	txtY.setText("" + c_gui.getY(c_nodeID));

	gridBagConstraints6 = new java.awt.GridBagConstraints ();
	gridBagConstraints6.gridx = 1;
	gridBagConstraints6.gridy = 1;
	gridBagConstraints6.insets = new java.awt.Insets (10, 10, 10, 10);
	jPanel8.add (txtY, gridBagConstraints6);

        gridBagConstraints5 = new java.awt.GridBagConstraints ();
        gridBagConstraints5.insets = new java.awt.Insets (10, 10, 10, 10);
        gridBagConstraints5.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints5.weightx = 1.0;
        gridBagConstraints5.weighty = 1.0;
        pnlPosition.add (jPanel8, gridBagConstraints5);

	jTabbedPane1.addTab ("Position", pnlPosition);

	//added 2002-08-22
	lblNote = new JLabel();
	lblNote.setText( "Notes" );
	txtArea = new JTextArea(15,40);
	//set text in area
	txtArea.setText(c_gui.getNote(c_nodeID));
	txtArea.setLineWrap( true );
	txtArea.setFont(new Font("Monospaced", Font.PLAIN, 12) );
	JScrollPane scrollpane = new JScrollPane(txtArea);
	scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	pnlNote = new JPanel();
	pnlNote.add(scrollpane);
	jTabbedPane1.addTab ("Notes", pnlNote);

	gridBagConstraints1 = new java.awt.GridBagConstraints ();
	gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
	getContentPane ().add (jTabbedPane1, gridBagConstraints1);

	pnlOkButtons.setLayout (new java.awt.GridBagLayout ());
	java.awt.GridBagConstraints gridBagConstraints7;

	btnOK.setText ("OK");
	getRootPane().setDefaultButton(btnOK);
	btnOK.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    btnOKActionPerformed (evt);
		}
	    });

	gridBagConstraints7 = new java.awt.GridBagConstraints ();
	gridBagConstraints7.insets = new java.awt.Insets (15, 10, 10, 10);
	gridBagConstraints7.anchor = java.awt.GridBagConstraints.EAST;
	gridBagConstraints7.weightx = 1.0;
	pnlOkButtons.add (btnOK, gridBagConstraints7);

	btnCancel.setText ("Cancel");
	btnCancel.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    btnCancelActionPerformed (evt);
		}
	    }
				     );

	gridBagConstraints7 = new java.awt.GridBagConstraints ();
	gridBagConstraints7.insets = new java.awt.Insets (15, 10, 10, 10);
	gridBagConstraints7.anchor = java.awt.GridBagConstraints.EAST;
	pnlOkButtons.add (btnCancel, gridBagConstraints7);


	gridBagConstraints1 = new java.awt.GridBagConstraints ();
	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridy = 2;
	gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
	getContentPane ().add (pnlOkButtons, gridBagConstraints1);

    }

    private void btnCancelActionPerformed (java.awt.event.ActionEvent evt) {
	c_gui.setTimesteplevel(c_nodeID, c_undoTimestepLevel);
	closeDialog(null);
    }

    private void btnOKActionPerformed (java.awt.event.ActionEvent evt) {

	String x = txtX.getText();
	String y = txtY.getText();
	int xNumber = 0;
	int yNumber = 0;

	if (x != "") {
	    xNumber = Integer.parseInt(x);

	    if (y != "")
		yNumber = Integer.parseInt(y);
	}

	if (xNumber != 0 && yNumber != 0)
	    c_gui.setNodeLocation(c_nodeID, xNumber, yNumber);

	c_gui.setNodeLabel(c_nodeID, txtLabel.getText());
	c_gui.setNodeNote(c_nodeID, txtArea.getText());
	c_gui.setTimesteplevel(c_nodeID, (String) cmbLevel.getSelectedItem());

	setVisible(false);
	dispose();
    }

    private void btnPropertiesActionPerformed (java.awt.event.ActionEvent evt)
    {
	// put the frame in the middle of the screan
	JDialog dialog = null;

	if (listFunctions.getSelectedIndex() >= 0) {
	    String functionLabel = (String) listFunctions.getSelectedValue();
	    String functionType = c_gui.getFunctionType(c_nodeID, functionLabel);
	    if (functionType.equals("Source"))
		dialog = new SourceDialog(this, true, c_nodeID,
					  c_gui.getFunction(c_nodeID,
							    functionLabel),
					  c_gui);
	    else if (functionType.equals("Destination"))
		dialog = new DestinationDialog(this, true, c_nodeID,
					       c_gui.getFunction(c_nodeID,
								 functionLabel),
					       c_gui);
	    else if (functionType.equals("Boundary"))
		dialog = new BoundaryDialog(this, true, c_nodeID,
					    c_gui.getFunction(c_nodeID,
							      functionLabel),
					    c_gui);
            // Added by Nawzad Mardan 2008-02-11
            // String "StartStopEquation" is the same string as defined in the constructor of class StartStopEquation.java
            else if (functionType.equals("StartStopEquation"))
		dialog = new StartStopDialog(this, true, c_nodeID, c_gui.getFunction(c_nodeID, functionLabel),c_gui);
            
	    else if (functionType.equals("BoundaryTOP"))
		dialog = new BoundaryTOPDialog(this, true, c_nodeID,
					    c_gui.getFunction(c_nodeID,
							      functionLabel),
					    c_gui);
	    else if (functionType.equals("FlowRelation")) {
		dialog = new FlowRelationDialog(this, true, c_nodeID,
						c_gui.getFunction(c_nodeID,functionLabel),
						c_gui,
						c_gui.getInFlows(c_nodeID),
						c_gui.getOutFlows(c_nodeID));

	    }
            else if (functionType.equals("FlowDependency")) {
		dialog = new FlowDependencyDialog(this, true, c_nodeID,
						  c_gui.getFunction(c_nodeID,
								    functionLabel),
						  c_gui);
            }
	    else if (functionType.equals("FlowEquation")) {
		dialog = new FlowEquationDialog(this, true, c_nodeID,
						  c_gui.getFunction(c_nodeID,
								    functionLabel),
						  c_gui);

            }
	    /* Added by Johan Sjöstrand 2003-11-20 */
	    else if (functionType.equals("InvestmentCost")) {
		dialog = new InvestmentCostDialog(this, true, c_nodeID,
						  c_gui.getFunction(c_nodeID,
								    functionLabel),
						  c_gui);

            }
            /* Added by  Jonas Sääv 2004-04-25 */
            else if (functionType.equals("StorageEquation")) {
                dialog = new StorageDialog(this, true, c_nodeID,
                                                  c_gui.getFunction(c_nodeID,
                                                                    functionLabel),
                                                  c_gui);

            }
            else if (functionType.equals("BatchEquation")) {
                dialog = new BatchDialog(this, true, c_nodeID,
                                                  c_gui.getFunction(c_nodeID,
                                                                    functionLabel),
                                                  c_gui);

            }

            else if (functionType.equals("LogicalEquation")) {
                dialog = new BinaryFunctionDialog(this, true, c_nodeID,
                                                  c_gui.getFunction(c_nodeID,
                                                                    functionLabel),
                                                  c_gui);

            }
            
            /*Tillagt av Marcus Bergendorff*/
            else if (functionType.equals("FunctionEditor")) {
    			dialog = new FunctionEditorDialog(this, true, c_nodeID,
    							  c_gui.getFunction(c_nodeID,
    									    functionLabel),
    							  c_gui);

    	            }


	    if (dialog != null) {
		int height = getLocation().y + getSize().height/2;
		int width = getLocation().x + getSize().width/2;
		int x = (int) (width - dialog.getSize().width/2);
		int y = (int) (height - dialog.getSize().height/2);
		dialog.setLocation(x, y);
                //System.out.println(functionType);

		dialog.show();
		listFunctions.setListData(c_gui.getAllFunctionLabels(c_nodeID));
	    }
	}

    }

    private void btnRemoveActionPerformed (java.awt.event.ActionEvent evt)
    {
      int selection = JOptionPane.showConfirmDialog(this,
          "Are you sure you want to delete the function " +
          (String) listFunctions.getSelectedValue() +
          "? \n The operation cannot be undone", "Remove Function",
          JOptionPane.YES_NO_OPTION);
      if (selection == JOptionPane.YES_OPTION) {
        c_gui.removeFunction(c_nodeID, (String) listFunctions.getSelectedValue());
        listFunctions.setListData(c_gui.getAllFunctionLabels(c_nodeID));
      }
    }

    private void btnAddActionPerformed (java.awt.event.ActionEvent evt)
    {
	c_gui.showMessageDialog("This is not implemented yet.");
    }

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {
	setVisible (false);
	dispose ();
    }

    /**
     * Handles doubleclicking in the function list
     * A doubleclick opens the property dialog.
     * @param e
     */
    public void mouseClicked(MouseEvent e) {
      if (e.getSource() == listFunctions) {
        if (e.getClickCount() == 2) {
          btnProperties.doClick();
        }
      }
    }
    public void mousePressed(MouseEvent e) {
    }
    public void mouseReleased(MouseEvent e) {
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }

    // Variables declaration - do not modify
    private JTabbedPane jTabbedPane1;
    private JPanel pnlGeneral;
    private JLabel lblID;
    private JLabel lblIDText;
    private JLabel lblTimestep;
    private JLabel lblLevel;
    private JComboBox cmbTimestep;
    private JComboBox cmbLevel;
    private JLabel lblLabel;
    private JTextField txtLabel;
    private JPanel pnlFunctions;
    private JPanel pnlButtons;
    private JList listFunctions;
    private JButton btnAdd;
    private JButton btnRemove;
    private JButton btnProperties;
    private JPanel pnlProperties;
    private JLabel lblProperties;
    private JPanel pnlPosition;
    private JPanel jPanel8;
    private JLabel lblX;
    private JTextField txtX;
    private JLabel lblY;
    private JTextField txtY;
    //added 2002-08-22
    private JPanel pnlNote;
    private JLabel lblNote;
    private JTextArea txtArea;
    private JScrollPane scrollpane;

    private JPanel pnlOkButtons;
    private JButton btnOK;
    private JButton btnCancel;
    //added 2002-12-16
    private JScrollPane listpane;
}
