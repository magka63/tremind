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

import java.util.Vector;
import java.awt.datatransfer.*;
import java.io.*;
import javax.swing.tree.DefaultMutableTreeNode;

import mind.gui.dnd.*;

public class FoldersTreeNode
{
    private NodeToDrag c_node;
    private Vector c_children;

    public FoldersTreeNode(NodeToDrag node, boolean isLeaf)
    {
	c_node = node;

	if (isLeaf)
	    c_children = null;
	else
	    c_children = new Vector(0);
    }

    public void addChild(FoldersTreeNode child)
    {
	c_children.addElement(child);
    }

    public boolean equals(FoldersTreeNode node)
    {
	return c_node.equals(node);
    }

    public boolean isLeaf()
    {
	return (c_children == null);
    }

    public Vector getChildren()
    {
	return c_children;
    }

    public NodeToDrag getNode()
    {
	return c_node;
    }

    public String toString()
    {
	return c_node.toString();
    }
}
