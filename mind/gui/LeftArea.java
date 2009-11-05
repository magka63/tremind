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
package mind.gui;

import javax.swing.*;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;

import mind.gui.dnd.*;

/**
 * This class shows to the user what nodetypes and what functions are
 * available. Nodes can be dragged to the graph area and a new
 * instantiation of that nodetype will popup in the graph area.
 * Functions can be dragged and if you drop it on a node in the graph
 * area, the function will be added to the node.
 *
 * @author Tim Terlegård
 * @version 2001-04-09
 */

public class LeftArea
    extends JScrollPane
{
    private DnDList c_functionList;
    private DnDTree c_nodeTree;
 //   private GlobalFunctionsPanel c_globalFunctions;
    private DragTreeNode c_rootNode;
    private GUI c_gui;

    /**
     * A nullconstructor
     */
    public LeftArea(GUI gui)
    {
	c_gui = gui;

	JTabbedPane tabbed = new JTabbedPane();
	tabbed.addTab("Nodes", c_nodeTree = createNodeTree());
	tabbed.addTab("Functions", c_functionList = createFunctionsList());
        tabbed.setToolTipTextAt(0, "Drag and drop a node collection to the model graph view");
        tabbed.setToolTipTextAt(1, "Drag and drop a node function on a node in the model graph view");
        tabbed.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
	setViewportView(tabbed);
    }

    /**
     * Creates a Swing list with function names as entries
     * and supports drag-and-drop.
     * @return The created Swing list.
     */
    DnDList createFunctionsList()
    {
	DnDList list = new DnDList();
	return list;
    }

    /**
     * Creates a Swing Tree with the available nodetypes
     * @return A Swing Tree with the available nodetypes
     */
    DnDTree createNodeTree()
    {
	//	c_rootNode =
	//  new DragTreeNode(new NodeToDrag("Nodes", ""), false);

	//       	DnDTree tree = new DnDTree(c_rootNode);
	DnDTree tree = new DnDTree();

	return tree;
    }

    /**
     * Tells the 'function'-tab what functions are available.
     * @param functions The functions that will be displayed in
     * the function list.
     */
    public void setAvailableFunctions(Vector functions)
    {
	for (int i = 0; i < functions.size(); i++)
	    c_functionList.addElement(functions.elementAt(i));
    }

    /**
     * Tells the 'node'-tab what nodes are available.
     * @param treeNode The root node of the tree to be displayed.
     */
    public void setAvailableNodes(DefaultMutableTreeNode treeNode)
    {
	c_rootNode =
	    new DragTreeNode((NodeToDrag) treeNode.getUserObject(), false);
	setTree(c_rootNode, treeNode);
	c_nodeTree.setTopNode(c_rootNode);
	c_nodeTree.updateUI();
    }

    public void setTree(DragTreeNode dragNode, DefaultMutableTreeNode treeNode)
    {
	Enumeration children = treeNode.children();
	DragTreeNode dragNodeToAdd;
	DefaultMutableTreeNode node;
	dragNode.removeChildren();

	while (children.hasMoreElements()) {
	    node = (DefaultMutableTreeNode) children.nextElement();

	    dragNodeToAdd =
		new DragTreeNode((NodeToDrag)node.
				 getUserObject(),
				 !node.getAllowsChildren());

	    dragNode.addChild(dragNodeToAdd);
	    setTree(dragNodeToAdd, node);
	}
    }
}
