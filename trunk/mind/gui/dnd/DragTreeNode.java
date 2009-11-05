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
package mind.gui.dnd;

import java.util.Vector;
import java.awt.datatransfer.*;
import java.io.*;

public class DragTreeNode
    implements Transferable
{
    NodeToDrag c_node;
    Vector c_children;

    public DragTreeNode(NodeToDrag node, boolean isLeaf)
    {
	c_node = node;

	if (isLeaf)
	    c_children = null;
	else
	    c_children = new Vector(0);
    }

    public void addChild(DragTreeNode child)
    {
	c_children.addElement(child);
    }

    public boolean equals(DragTreeNode node)
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

    public void removeChildren()
    {
	if (c_children != null)
	    c_children = new Vector(0);
    }

    public String toString()
    {
	return c_node.toString();
    }

    public static DataFlavor flavor =
	new DataFlavor(NodeToDrag.class, "Java NodeToDrag object");

    public static DataFlavor[] supportedFlavors = {
	flavor, DataFlavor.stringFlavor
    };

    public DataFlavor[] getTransferDataFlavors()
    {
	return supportedFlavors;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
	if (flavor.equals(this.flavor) ||
	    flavor.equals(DataFlavor.stringFlavor))
	    return true;
	return false;
    }

    public Object getTransferData(DataFlavor flavor)
	throws UnsupportedFlavorException, IOException
    {
	if (flavor.equals(this.flavor))
	    return c_node;
	else if (flavor.equals(DataFlavor.stringFlavor))
	    return c_node.toString();
	else
	    throw new UnsupportedFlavorException(this.flavor);
    }
}
