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

import mind.gui.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.util.Enumeration;

import mind.model.*;
import mind.gui.dnd.*;

/**
 * This is a class representing a dialog where you can save a new
 * nodetype. You have a tree you can browse and you select in what
 * folder you want the nodetype added.
 *
 * @author Tim Terlegård
 */

public class AddNodeToCollectionDialog
    extends JDialog
{
    private FoldersTreeNode c_root = null;
    private JTree c_tree = new JTree();
    private ID c_nodeID;
    private GUI c_gui;
    private JFrame c_parent;
    private JTextField c_txtName = new JTextField();
    private String c_nodeName;
    private NodeToDrag c_folder;
    private JScrollPane c_scrollTree = new JScrollPane(c_tree);

    private final String SPECIFY_FOLDER =
	"You specified an empty string. Folders\n" +
	"must be at least one character long.\n";

    public class NewFolderListener
	implements ActionListener
    {
	public void actionPerformed(ActionEvent e)
	{
	    String folder = JOptionPane.
		showInputDialog(AddNodeToCollectionDialog.this,
				"Name", "New Folder",
				JOptionPane.PLAIN_MESSAGE);

	    if (folder == null) {
		c_gui.showMessageDialog(SPECIFY_FOLDER);
		return;
	    }

	    TreePath selected = c_tree.getSelectionPath();

	    if (selected != null) {
		NodeToDrag node = ((FoldersTreeNode) selected.
				   getLastPathComponent()).getNode();
		if (node != null)
		    c_gui.addFolderToDatabase(folder, node);
	    }
	    else {
		c_gui.showMessageDialog(SPECIFY_FOLDER);
		return;
	    }

	    DefaultMutableTreeNode top = c_gui.getAvailableNodes();
	    c_root =
		new FoldersTreeNode((NodeToDrag) top.getUserObject(), false);
	    setTree(c_root, top);
	    c_tree.setModel(new FoldersTreeModel(c_root));
	    //	    c_tree.setModel(c_gui.getAvailableNodes());
	    //	    c_tree = new JTree(c_gui.getAvailableNodes());
	    //c_scrollTree.setViewportView(c_tree);
	}
    }

    public class OKListener
	implements ActionListener
    {
	public void actionPerformed(ActionEvent e)
	{
	    TreePath selected = c_tree.getSelectionPath();

	    if (selected != null) {
		NodeToDrag folder = ((FoldersTreeNode) selected.
				     getLastPathComponent()).getNode();
		if (folder != null) {
		    c_gui.addNodeToCollection(c_txtName.getText(), folder);
		    closeDialog();
		}
	    }
	    else {
		c_gui.showMessageDialog(SPECIFY_FOLDER);
		return;
	    }
	}
    }

    public class CancelListener
	implements ActionListener
    {
	public void actionPerformed(ActionEvent e)
	{
	    closeDialog();
	}
    }

    public AddNodeToCollectionDialog(JFrame parent, boolean modal,
				     final GUI gui, ID nodeID)
    {
	super(parent, modal);

	JLabel lblTree = new JLabel("Select node folder:");
	JLabel lblName = new JLabel("Name");
	JButton btnOK = new JButton("OK");
	JButton btnCancel = new JButton("Cancel");
	JButton btnNewFolder = new JButton("New folder...");
	//	c_tree = new JTree();
	JPanel pnlTree = new JPanel();
	JPanel pnlButtons = new JPanel();
	GridBagConstraints constraints;
	JSeparator sep = new JSeparator();
	JPanel pnlMain = new JPanel();

	c_parent = parent;
	c_gui = gui;
	c_nodeID = nodeID;

	//	setTree(c_root, c_gui.getAvailableNodes());
	//	c_tree.setModel(new FoldersTreeModel(c_root));
	//	c_tree.setModel(c_gui.getAvailableNodes());
	//	c_tree = new JTree(c_gui.getAvailableNodes());
	//	c_scrollTree.setViewportView(c_tree);
	//	c_tree.setRootVisible(false);
	DefaultMutableTreeNode top = c_gui.getAvailableNodes();
	c_root =
	    new FoldersTreeNode((NodeToDrag) top.getUserObject(), false);
	setTree(c_root, top);
	c_tree.setModel(new FoldersTreeModel(c_root));

	// add an actionlistener to ok button
	btnOK.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e)
		{
		    closeDialog();
		}
	    });

	// add an actionlistener to cancel button
	btnCancel.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e)
		{
		    closeDialog();
		}
	    });

	pnlMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	pnlMain.setLayout(new GridBagLayout());
	getContentPane().add(pnlMain, BorderLayout.CENTER);
	pnlTree.setLayout(new GridBagLayout());
	pnlButtons.setLayout(new GridBagLayout());

	// add label to tree panel
	constraints = new GridBagConstraints();
	constraints.weightx = 1.0;
	constraints.anchor = GridBagConstraints.WEST;
	pnlTree.add(lblTree, constraints);

	// add jtree to tree panel
	c_scrollTree.setPreferredSize(new Dimension(150, 200));
	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 1;
	constraints.weightx = 1.0;
	constraints.weighty = 1.0;
	constraints.fill = GridBagConstraints.BOTH;
	pnlTree.add(c_scrollTree, constraints);

	// add new folder button to tree panel
	constraints = new GridBagConstraints();
	constraints.gridx = 1;
	constraints.gridy = 1;
	constraints.weighty = 1.0;
	constraints.anchor = GridBagConstraints.SOUTH;
	constraints.insets = new Insets(0, 6, 0, 0);
	btnNewFolder.addActionListener(new NewFolderListener());
	pnlTree.add(btnNewFolder, constraints);

	// add tree panel to rootpane
	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 0;
	constraints.gridwidth = 2;
	constraints.weightx = 1.0;
	constraints.weighty = 1.0;
	constraints.fill = GridBagConstraints.BOTH;
	pnlMain.add(pnlTree, constraints);

	// add label Name to rootpane
	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 1;
	constraints.insets = new Insets(20, 0, 10, 5);
	pnlMain.add(lblName, constraints);

	// add Name textfield to rootpane
	constraints = new GridBagConstraints();
	constraints.gridx = 1;
	constraints.gridy = 1;
	constraints.insets = new Insets(20, 0, 10, 0);
	constraints.weightx = 1.0;
	constraints.fill = GridBagConstraints.HORIZONTAL;
	constraints.anchor = GridBagConstraints.WEST;
	pnlMain.add(c_txtName, constraints);

	// add a separator
	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 2;
	constraints.weightx = 1.0;
	constraints.gridwidth = 2;
	constraints.fill = GridBagConstraints.HORIZONTAL;
	pnlMain.add(sep, constraints);

	// add ok button to button panel
	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 0;
	constraints.insets = new Insets(10, 0, 0, 5);
	constraints.weightx = 1.0;
	constraints.anchor = GridBagConstraints.EAST;
	btnOK.addActionListener(new OKListener());
	getRootPane().setDefaultButton(btnOK);
	pnlButtons.add(btnOK, constraints);

	// add cancel button to button panel
	constraints = new GridBagConstraints();
	constraints.gridx = 1;
	constraints.gridy = 0;
	constraints.insets = new Insets(10, 0, 0, 0);
	btnCancel.addActionListener(new CancelListener());
	pnlButtons.add(btnCancel, constraints);

	// add buttons panel to rootpane
	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 3;
	constraints.gridwidth = 2;
	constraints.weightx = 1.0;
	constraints.fill = GridBagConstraints.HORIZONTAL;
	pnlMain.add(pnlButtons, constraints);

	addWindowListener(new WindowAdapter () {
		public void windowClosing(WindowEvent e)
		{
		    closeDialog();
		}});

	pack();
    }

    public String getText()
    {
	return c_txtName.getText();
    }

    private void setTree(FoldersTreeNode folderNode,
			 DefaultMutableTreeNode treeNode)
    {
	Enumeration children = treeNode.children();
	FoldersTreeNode nodeToAdd;
	DefaultMutableTreeNode node;

	while (children.hasMoreElements()) {
	    node = (DefaultMutableTreeNode) children.nextElement();

	    nodeToAdd =
		new FoldersTreeNode((NodeToDrag) node.
				    getUserObject(),
				    !node.getAllowsChildren());

	    folderNode.addChild(nodeToAdd);
	    setTree(nodeToAdd, node);
	}
    }

    private void closeDialog()
    {
	setVisible(false);
	dispose();
    }
}
