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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.*;
import javax.swing.text.*;

import mind.EventHandlerClient;
import mind.gui.*;
import mind.model.*;



/**
 * The dialog for controling timesteps levels
 * @author Johan Trygg
 * @version 2001-06-30
 */

public class TimestepsDialog extends javax.swing.JDialog
    implements ListSelectionListener, MouseListener
{
    private GUI c_gui;
    private EventHandlerClient c_eventHandlerClient;
    private Timesteplevel c_topTimesteplevel;
    private java.awt.Frame c_parent;

    private javax.swing.JList timestepList;
    private javax.swing.JLabel timestepListLabel;
    private javax.swing.JButton addTimestepButton;
    private javax.swing.JButton editTimestepButton;
    private javax.swing.JButton deleteTimestepButton;
    private javax.swing.JButton okButton;

    /** Creates new form ResourcesDialog */
    public TimestepsDialog(java.awt.Frame parent, boolean modal, GUI gui)
    {
        super(parent, modal);
	c_gui = gui;
	c_eventHandlerClient = c_gui.getEventHandlerClient();
	c_parent = parent;
	c_topTimesteplevel = c_eventHandlerClient.getTopTimesteplevel();
        initComponents();
	updateTimesteps();
        pack();
    }

    /** Updates the timesteplist */
    private void updateTimesteps()
    {
	Timesteplevel level;
	level = c_topTimesteplevel;
	Vector list = new Vector(0);
	while (level != null)
	{
	    list.addElement(level);
	    level = level.getNextLevel();
	}
	timestepList.setListData(list);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents()
    {
        timestepList = new javax.swing.JList();
        timestepList.addMouseListener(this);
        timestepListLabel = new javax.swing.JLabel();
        addTimestepButton = new javax.swing.JButton();
	editTimestepButton = new javax.swing.JButton();
        deleteTimestepButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        getContentPane().setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1;
        addWindowListener(new java.awt.event.WindowAdapter() {
		public void windowClosing(java.awt.event.WindowEvent evt) {
		    closeDialog(evt);
		}
	    });

	//The timestep list
        timestepList.setPreferredSize(new java.awt.Dimension(100, 100));
        timestepList.setMinimumSize(new java.awt.Dimension(10, 10));
	timestepList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	timestepList.addListSelectionListener(this);

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.gridheight = 6;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(timestepList, gridBagConstraints1);

	//Scrollpane for the list
	/*
	  gridBagConstraints1 = new java.awt.GridBagConstraints ();
	  gridBagConstraints1.gridx = 0;
	  gridBagConstraints1.gridy = 1;
	  gridBagConstraints1.gridheight = 6;
	  JScrollPane scrollPane = new JScrollPane(timestepList);
	  //scrollPane.setPreferredSize(new Dimension(100, 150));
	  getContentPane().add(scrollPane, gridBagConstraints1);
	*/

	//The timestep list label
	timestepListLabel.setText("Timestep levels:");

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(timestepListLabel, gridBagConstraints1);

        //The "Edit..." button
        editTimestepButton.setText("Edit...");
        //editTimestepButton.setEnabled(false);
	editTimestepButton.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    editTimestepButtonActionPerformed (evt);
		}
	    });

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(editTimestepButton, gridBagConstraints1);

        //The "OK" button
        okButton.setText("OK");
	getRootPane().setDefaultButton(okButton);
	okButton.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    okButtonActionPerformed (evt);
		}
	    });

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 7;
	//gridBagConstraints1.gridwidth = 1;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
	gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(okButton, gridBagConstraints1);

        //The "Delete" button
        deleteTimestepButton.setText("Delete");
	deleteTimestepButton.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    deleteTimestepButtonActionPerformed (evt);
		}
	    });

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 5;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(deleteTimestepButton, gridBagConstraints1);

        //The "Add..." button
        addTimestepButton.setText("Add...");
	addTimestepButton.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    addTimestepButtonActionPerformed (evt);
		}
	    });

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(addTimestepButton, gridBagConstraints1);

    }

    private void okButtonActionPerformed (java.awt.event.ActionEvent evt)
    {
	closeDialog(null);
    }

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt)
    {
        setVisible (false);
        dispose ();
    }

    private void addTimestepButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
	// show dialog for making a new timestep
	JDialog dialog = new AddTimestepDialog2(this, true, c_gui);
	int height = getLocation().y + getSize().height/2;
	int width = getLocation().x + getSize().width/2;
	int x = (int) (width - dialog.getSize().width/2);
	int y = (int) (height - dialog.getSize().height/2);
	dialog.setLocation(x, y);
	dialog.show();

	// update the timestep list
	updateTimesteps();
    }

    private void editTimestepButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
	int timesteplevelIndex = timestepList.getSelectedIndex();
	if (timesteplevelIndex == -1)
	    return;

	Timesteplevel timesteplevel = (Timesteplevel) timestepList.getSelectedValue();

	// show dialog for editing a timestep
	JDialog dialog = new AddTimestepDialog2(this, true, c_gui, timesteplevel,
						timesteplevelIndex);
	int height = getLocation().y + getSize().height/2;
	int width = getLocation().x + getSize().width/2;
	int x = (int) (width - dialog.getSize().width/2);
	int y = (int) (height - dialog.getSize().height/2);
	dialog.setLocation(x, y);
	dialog.show();

	// update the timestep list
	updateTimesteps();
    }

    private void deleteTimestepButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
	int selected = timestepList.getSelectedIndex();
	int answer;
	if (selected == -1)
	    return;
	else if (selected == 0) {
	    JOptionPane.showMessageDialog(c_parent,"The toplevel cannot be deleted!",
					  "Remove Timesteplevel",JOptionPane.OK_OPTION);
	    return;
	}

	Timesteplevel tsl = (Timesteplevel) timestepList.getSelectedValue();

	String message =
	    "When a timesteplevel is deleted all functions using this level\n" +
	    "will be using the level above, and all their settings for this\n" +
	    "level will be removed.\n" +
	    "Are you sure you want to remove the timesteplevel \"" +
	    tsl.getLabel() + "\" ?";

	answer = JOptionPane.showConfirmDialog(c_parent,
					       message,
					       "Warning!",
					       JOptionPane.YES_NO_OPTION);

	if (answer != JOptionPane.YES_OPTION)
	    return;

	//Remove the timesteplevel (and remove it from every function)
	c_eventHandlerClient.removeTimesteplevel(tsl);

	// update the timestep list
	updateTimesteps();
    }

/* Implementation of the ListSelectionListener interface */
    public void valueChanged(javax.swing.event.ListSelectionEvent e) {

    }

  /* Implementation of the MouseListener interface */
  public void mouseClicked(MouseEvent e) {
    if (e.getSource() == timestepList) {
      if (e.getClickCount() == 2) {
        editTimestepButton.doClick();
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

}
