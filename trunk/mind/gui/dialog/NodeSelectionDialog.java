/*
 * Copyright 2007:
 * Daniel Källming <danka053@student.liu.se>
 * David Karlslätt <davka417@student.liu.se>
 * Freddie Pintar <frepi150@student.liu.se>
 * Mårten Thurén <marth852@student.liu.se>
 * Per Fredriksson <perfr775@student.liu.se>
 * Ted Palmgren <tedpa175@student.liu.se>
 * Tor Knutsson <torkn754@student.liu.se>
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


import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import mind.model.ID;
import mind.model.Model;
import mind.model.Node;
import mind.model.NodeControl;

/**
 * Creates a new node selection dialog. This class lets the user select 
 * which nodes to export.
 * 
 * @author Daniel Källming
 * @author Ted Palmgren
 * @version 2007-12-12
 */
public class NodeSelectionDialog 
	extends JDialog implements ActionListener {
		private boolean c_locked;
		private Model c_model;
	    private java.awt.Frame c_parent;
	    private JPanel buttonPanel;
	    private JPanel scrollFill;
	    private JScrollPane scrollPanel;
	    private JPanel confirmPanel;
	    private ID nodeId;
	    private Vector <JCheckBox> chkVect;
	    private Vector <ID> c_idVect;	 
	    private ID[] c_markedNodes;
	    private boolean isMarked;
	    private JCheckBox lockWorkBook;
	  
	    public int exitOption;
	    
	    //Selection Buttons
	    private JButton selAll;
	    private JButton selNone;
	    private JButton ok;
	    private JButton cancel;
	    
	    
	    
	public NodeSelectionDialog(java.awt.Frame parent, Model model, 
			Vector<ID> idVect, ID[] ids, String modelName){
		super(parent, true);
		
		c_parent = parent;
		c_model = model;
		c_idVect = idVect;
		c_markedNodes = ids;
		setTitle(modelName + " - Select Nodes");
		initComponents();
		addComponents();
		getRootPane().setDefaultButton(ok);
	}
	
	/**
	 * Returns whether the user chose to lock the workbook
	 * @return boolean
	 */ 
    public boolean getLock() 
	{
    	return c_locked;
    }

    /**
     * Sets sizes of the node check box area, with max width and min height arguments.
     * Max height is determined by mainDialog size.
	 * @param maxWidth
	 * @param minHeight
     */
	private void setNodeSelectionSize(int maxWidth, int minHeight) {

		int prefWidth = scrollFill.getPreferredSize().width + scrollPanel.getVerticalScrollBar().getWidth() + 20;
		int finalWidth = java.lang.Math.min(maxWidth, prefWidth);
		int maxHeight = c_parent.getSize().height - 200;
		int prefHeight = scrollPanel.getPreferredSize().height;
		int finalHeight;
		
		//Set the height to prefHeight if inside min-max bounds
		if (minHeight > prefHeight)
			finalHeight = minHeight;
		else
			finalHeight = java.lang.Math.min(maxHeight,prefHeight);
		
		Dimension tempDimension = new Dimension(finalWidth, finalHeight);
		scrollPanel.setPreferredSize(tempDimension);
	}
	
	/**
	 * Initiates the components in the dialog window.
	 */
	private void initComponents() {
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		//Selection Buttons
		selAll = new JButton("Select All");
		selNone = new JButton("Deselect All");
		buttonPanel = new JPanel();

		//Check boxes
		lockWorkBook = new JCheckBox("Lock Workbook", false);
		createCheckBox();
		
		scrollFill = new JPanel();
		scrollFill.setLayout(new GridLayout(0,1));
		
		scrollPanel = new JScrollPane(scrollFill);
		
		//Set scroll speeds
		scrollPanel.getVerticalScrollBar().setUnitIncrement(50);
		scrollPanel.getHorizontalScrollBar().setUnitIncrement(50);
		
		//Confirm Buttons
		ok = new JButton("OK");
		cancel = new JButton("Cancel");
		confirmPanel = new JPanel();
		
		//Add action listeners
		ok.addActionListener(this);
		cancel.addActionListener(this);
		selAll.addActionListener(this);
		selNone.addActionListener(this);
	}

	/**
	 * Adds components to the panel.
	 */
	private void addComponents() 
	{
		
		//Top Panel
		buttonPanel.add(selAll);
		buttonPanel.add(selNone);
		buttonPanel.add(lockWorkBook);
		
		getContentPane().add(buttonPanel);

		//Add all check boxes
		if (chkVect == null) {
			System.out.println("No nodes in model!");
		}
		else{
			int i;
			for(i = chkVect.size(); i>0; i--) {
				scrollFill.add(chkVect.get(i-1));
			}		
		}

		getContentPane().add(scrollPanel);
		pack();
		setNodeSelectionSize(500,200);
		
		//Confirm
		confirmPanel.add(ok);
		confirmPanel.add(cancel);
		
		getContentPane().add(confirmPanel);
		pack();pack();pack();
		
		confirmPanel.setMaximumSize(new Dimension(confirmPanel.getWidth(), confirmPanel.getPreferredSize().height));
		buttonPanel.setMaximumSize(new Dimension(buttonPanel.getWidth(), buttonPanel.getPreferredSize().height));

	}
	 
	
	/**
	 * Creates a check box for every node in the model, 
	 * if it's marked in the models graph area it will be
	 * marked in the dialog.
	 */
	private void createCheckBox(){
		
		Node node;
		NodeControl nc = c_model.getAllNodes();
		Enumeration allNodes = nc.elements();
		chkVect = new Vector<JCheckBox>();
		
		/* Loop over all nodes to create check boxes*/
		while (allNodes.hasMoreElements()) {
			node = (Node) allNodes.nextElement();
			nodeId = node.getID();
			c_idVect.add(nodeId);
			
			// Find out if the node is selected in the graphical model
			int i;
			isMarked = false;
			if (!(c_markedNodes.length == 0)){
				for(i = 0; i<c_markedNodes.length; i++){
					// Is the node in the array?
					if(c_markedNodes[i].toString().equals(nodeId.toString())){
						isMarked = true;
						break;
					}
				}
				//Add check box to chkVect
				chkVect.add(new JCheckBox(nodeId.toString() + " - " + node.getLabel(), isMarked));
				chkVect.lastElement().setBackground(Color.WHITE);
			}
			else{ //No nodes marked in the graphical model (all check boxes will be selected)
				isMarked = true;
				//Add check box to chkVect
				chkVect.add(new JCheckBox(nodeId.toString() + " - " + node.getLabel(), isMarked));
				chkVect.lastElement().setBackground(Color.WHITE);
			}
			
		}
	}
	
	
	/**
	 * Constructs a vector with nodes to export
	 * and sets the exitOption to 1 if any nodes are selected.
	 * @param e ActionEvent created when clicking
	 */
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == ok){
			
			//c_idVect initially contains all nodes. Not selected nodes are removed
			int i;
			for(i = chkVect.size(); i>0; i--) {
				if (!chkVect.get(i-1).isSelected()){
					c_idVect.remove(i-1);
				}
			}
			
			//No nodes to export?
			if(c_idVect.isEmpty()){
				JOptionPane noNodes = new JOptionPane
					("No nodes selected or empty model", JOptionPane.INFORMATION_MESSAGE);
				JDialog noNodesInstance = noNodes.createDialog(c_parent, "Note");
				noNodesInstance.setVisible(true);
				exitOption = 0;
			}
			else exitOption = 1;
			
			if (lockWorkBook.isSelected())
				c_locked = true;
			
			setVisible(false);
			dispose();
		}
		else if(e.getSource() == cancel){
			exitOption = 0;
			setVisible(false);
			dispose();
		}
		else if(e.getSource() == selAll){
			int i;
			for(i = 0; i<chkVect.size(); i++) {
				chkVect.get(i).setSelected(true);
			}
		}
		else if(e.getSource() == selNone){
			int i;
			for(i = 0; i<chkVect.size(); i++) {
				chkVect.get(i).setSelected(false);
			}
		}
	}
}
