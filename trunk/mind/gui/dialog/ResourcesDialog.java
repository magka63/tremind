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


/*
 * ResourcesDialog.java
 *
 * @author Peter �strand
 * @author Johan Trygg
 * @author Peter Andersson
 * @version 2001-07-27
 */
public class ResourcesDialog extends javax.swing.JDialog
    implements ListSelectionListener
{
    private GUI c_gui;
    private Vector c_resources;
    private java.awt.Frame c_parent;
    private String c_newResourceName ="";
    private  JDialog dialog;

    /**
     * Creates the new form ResourcesDialog
     */
    public ResourcesDialog(java.awt.Frame parent, boolean modal, GUI gui) {
        super(parent, modal);
	setTitle("Resources");
	c_gui = gui;
	c_parent = parent;
        initComponents();
	updateResources();
        pack();
    }

    private void updateResources()
    {
	ID resourceID;

	c_resources = c_gui.getResources();
	Vector list = new Vector(0);
	for (int i = 0; i < c_resources.size(); i++) {
	    list.addElement((Resource)c_resources.elementAt(i));
	}

	resourceList.setListData(list);
    }


    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents()
    {
        resourceList = new javax.swing.JList();
        resourceListLabel = new javax.swing.JLabel();
        unitLabel = new javax.swing.JLabel();
        unitTextField = new javax.swing.JTextField();
        prefixLabel = new javax.swing.JLabel();
        prefixTextField = new javax.swing.JTextField();
	colorLabel = new javax.swing.JLabel();
	colorBox = new ColorComboBox();

        changeUnitButton = new javax.swing.JButton();
        scalPrefixButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        deleteResourceButton = new javax.swing.JButton();
        newResourceButton = new javax.swing.JButton();
	//added 2002-12-18
	scrollpane = new JScrollPane();

        getContentPane().setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1;
        addWindowListener(new java.awt.event.WindowAdapter() {
		public void windowClosing(java.awt.event.WindowEvent evt) {
		    closeDialog(evt);
		}
	    }
			  );

        resourceList.setPreferredSize(new java.awt.Dimension(100, 1000));
        resourceList.setMinimumSize(new java.awt.Dimension(10, 10));
	resourceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	resourceList.addListSelectionListener(this);
        //add resourceList to the scrollpane
	scrollpane.setPreferredSize(new java.awt.Dimension(120, 170));
	scrollpane.getViewport().setView(resourceList);

	// Adds scrollpane containing resourceList
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.gridheight = 6;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(scrollpane, gridBagConstraints1);

        // Adds resourcelabel
        resourceListLabel.setText("Resources:");

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 2, 2);
        getContentPane().add(resourceListLabel, gridBagConstraints1);

        // Adds unitlabel
        unitLabel.setText("Unit:");

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 2, 2);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(unitLabel, gridBagConstraints1);

	// Adds textfield for unit
        unitTextField.getDocument().addDocumentListener(new MyDocumentListener());
        unitTextField.getDocument().putProperty("name", "unitTextField");
	unitTextField.setEnabled(false);

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(2, 2, 10, 10);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(unitTextField, gridBagConstraints1);

        // Adds Prefixlabel
        prefixLabel.setText("Prefix:");

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 2, 2);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
      //  getContentPane().add(prefixLabel, gridBagConstraints1);

	// Adds textfield for prefix
        prefixTextField.getDocument().addDocumentListener(new MyDocumentListener());
        prefixTextField.getDocument().putProperty("name", "prefixTextField");
	prefixTextField.setEnabled(false);

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 4;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(2, 2, 10, 10);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        //getContentPane().add(prefixTextField, gridBagConstraints1);

	// Add colorLabel
	colorLabel.setText("Color:");

	gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 4;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 2, 2);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(colorLabel, gridBagConstraints1);

	// Add colorchooser
	colorBox.setEnabled(false);
	colorBox.addActionListener(new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent evt) {
		    colorChanged(evt);
		}
	    }
				   );

	gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 1;
    gridBagConstraints1.gridy = 5;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints1.insets = new java.awt.Insets(2, 2, 10, 10);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    getContentPane().add(colorBox, gridBagConstraints1);


    // Added by Nawzad Mardan 20100302 to change resource name
    editResourceButton = new javax.swing.JButton();
    editResourceButton.setText("Edit Resource Name");
    editResourceButton.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    editResourceButtonActionPerformed (evt);
		}
	    });
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 1;
    gridBagConstraints1.gridy = 6;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints1.insets = new java.awt.Insets(2, 2, 10, 10);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    getContentPane().add(editResourceButton, gridBagConstraints1);

        // Add changebutton
	/*
	  changeUnitButton.setText("Change...");
	  changeUnitButton.setEnabled(false);

	  gridBagConstraints1 = new java.awt.GridBagConstraints();
	  gridBagConstraints1.gridx = 2;
	  gridBagConstraints1.gridy = 2;
	  gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
	  gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
	  getContentPane().add(changeUnitButton, gridBagConstraints1);
        */
        // Add Scalebutton
	/*
	  scalPrefixButton.setText("Scale...");
	  scalPrefixButton.setEnabled(false);

	  gridBagConstraints1 = new java.awt.GridBagConstraints();
	  gridBagConstraints1.gridx = 2;
	  gridBagConstraints1.gridy = 4;
	  gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
	  gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
	  getContentPane().add(scalPrefixButton, gridBagConstraints1);
        */

        // Add OK button
        okButton.setText("OK");
	getRootPane().setDefaultButton(okButton);
	okButton.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    okButtonActionPerformed (evt);
		}
	    });


        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 8;
	gridBagConstraints1.gridwidth = 2;
	gridBagConstraints1.anchor = java.awt.GridBagConstraints.CENTER;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(okButton, gridBagConstraints1);

        // Add delete button
        deleteResourceButton.setText("Delete Resource");
	deleteResourceButton.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    deleteResourceButtonActionPerformed (evt);
		}
	    });

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 7;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(deleteResourceButton, gridBagConstraints1);

        // Add new resourcebutton
        newResourceButton.setText("New Resource");
	newResourceButton.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    newResourceButtonActionPerformed (evt);
		}
	    });


        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 7;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(newResourceButton, gridBagConstraints1);

    }

    private void okButtonActionPerformed (java.awt.event.ActionEvent evt)
    {
    c_gui.setChanged(true);
	closeDialog(null);
    }


    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        setVisible (false);
        dispose ();
    }


    private void newResourceButtonActionPerformed(java.awt.event.ActionEvent evt) {
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

    private void deleteResourceButtonActionPerformed(java.awt.event.ActionEvent evt) {
	if (resourceList.getSelectedIndex() == -1)
	    return;

	Resource resource = (Resource) resourceList.getSelectedValue();

	String message =
	    "If you delete a resource that is used in the model, bad things can happen!\n" +
	    "This is a bug that will be fixed in the next version of reMIND.\n"
	    + "If you are sure this resource is not used in the model you can remove it.\n" +
	    "If not, you shouldn't do it.\n" +
	    "Do you want to remove the resource \"" + resource.getLabel() + "\" ?";
	int answer;
	answer = JOptionPane.showConfirmDialog(c_parent,
					       message,
					       "Bug Warning!",
					       JOptionPane.YES_NO_OPTION);

	if (answer != JOptionPane.YES_OPTION)
	    return;

	try {
	    c_gui.removeResource(resource.getID());
	}
	catch(ModelException e) {
	    System.out.println(e);
	    e.printStackTrace(System.out);
	}

	// update the resource list
	updateResources();
    }

    /**
     * Keeps track of selected items in resourceList and changes fields as necessary
     * @param e The event that was fired.
     */
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (resourceList.getSelectedIndex() != -1) {
		// Update unit and prefix
		Resource resource = (Resource) resourceList.getSelectedValue();

		unitTextField.setText(resource.getUnit());
		unitTextField.setEnabled(true);

		prefixTextField.setText(resource.getPrefix());
		prefixTextField.setEnabled(true);

		colorBox.setSelectedItem(resource.getColor());
		colorBox.setEnabled(true);
	    } else {
		unitTextField.setText("");
		unitTextField.setEnabled(false);
		prefixTextField.setText("");
		prefixTextField.setEnabled(false);
		colorBox.setSelectedIndex(0);
		colorBox.setEnabled(false);
	    }
	}
    }

    /**
     * Event that fires when the colorBox changes it's value.
     * @param e The even that was fired.
     */
    public void colorChanged(java.awt.event.ActionEvent e){
	if (resourceList.getSelectedIndex() == -1)
	    return;

	// Sets the color of the resource.
	((Resource)resourceList.getSelectedValue()).setColor((ExtendedColor)colorBox.getSelectedItem());
    //((Resource)resourceList.getSelectedValue()).setLabel("Ahdaf");
    //resourceNameChanged();
    }

    public void resourceNameChanged()
    {
    ((Resource)resourceList.getSelectedValue()).setLabel("Anya");
    }

    class MyDocumentListener implements DocumentListener {
        public void insertUpdate(DocumentEvent e) {
	    Document doc = (Document)e.getDocument();
	    updateValues(e, (String)doc.getProperty("name"));
        }
        public void removeUpdate(DocumentEvent e) {
	    Document doc = (Document)e.getDocument();
	    updateValues(e, (String)doc.getProperty("name"));

        }
        public void changedUpdate(DocumentEvent e) {
            //Plain text components don't fire these events.
        }

	public void updateValues(DocumentEvent e, String name) {
	    if (resourceList.getSelectedIndex() == -1)
		return;

	    Resource resource = (Resource) resourceList.getSelectedValue();
	    if (name.equals("unitTextField")) {
		resource.setUnit(unitTextField.getText());
	    }
	    else if (name.equals("prefixTextField")) {
		resource.setPrefix(prefixTextField.getText());
	    }
        else if(name.equals("name"))
        {
            System.out.println("name");
        }
	}
    }

    // Variables declaration - do not modify
    private javax.swing.JList resourceList;
    private javax.swing.JLabel resourceListLabel;
    private javax.swing.JLabel unitLabel;
    private javax.swing.JTextField unitTextField;
    private javax.swing.JLabel prefixLabel;
    private javax.swing.JTextField prefixTextField;
    private javax.swing.JButton changeUnitButton;
    private javax.swing.JButton scalPrefixButton;
    private javax.swing.JButton okButton;
    private javax.swing.JButton deleteResourceButton;
    private javax.swing.JButton newResourceButton;
    private javax.swing.JLabel colorLabel;
    private ColorComboBox colorBox;
    //added 2002-12-18 to embedd resourceList in a JScrollPane
    private javax.swing.JScrollPane scrollpane;
    // End of variables declaration
    // Added by Nawzad Mardan 20100302 to change resource name
    private javax.swing.JButton editResourceButton;
   
    /**
     * Action when the "Edit..." button is pressed.
     */
    private void editResourceButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
    ResourceName rn;
    String resourceName = "";
    if (resourceList.getSelectedIndex() == -1)
        {
         JOptionPane.showMessageDialog(null, "The resource must be specified", "Resouce is not selected ",JOptionPane.WARNING_MESSAGE);
                    return;
        }
    else
       {
        // show dialog for editing a resource name
        Resource resource = (Resource) resourceList.getSelectedValue();
        resourceName = resource.getLabel();
        rn = new ResourceName();
        rn.setNewResourceName(resourceName);
    
        dialog = new EditResourceDialog  (this, true, rn);
        
        int height = getLocation().y + getSize().height/2;
        int width = getLocation().x + getSize().width/2;
        int x = (int) (width - dialog.getSize().width/2);
        int y = (int) (height - dialog.getSize().height/2);
        dialog.setLocation(x, y);
        dialog.setVisible(true);
        }
    if(!rn.getResourceName().equals("") && !rn.getResourceName().equals(resourceName))
        {
        ((Resource)resourceList.getSelectedValue()).setLabel(rn.getResourceName());
         updateResources();
        }
    }


}
