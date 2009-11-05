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

import javax.swing.tree.*;
import javax.swing.event.*;
import java.util.Vector;

public class FoldersTreeModel
    implements TreeModel
{
    protected FoldersTreeNode c_root;

    public FoldersTreeModel(FoldersTreeNode root)
    {
	c_root = root;
    }

    public Object getRoot()
    {
	return c_root;
    }

    public boolean isLeaf(Object node)
    {
	return ((FoldersTreeNode) node).isLeaf();
    }

    public int getChildCount(Object parent)
    {
	Vector children = ((FoldersTreeNode) parent).getChildren();
	int number = 0;

	for (int i = 0; i < children.size(); i++)
	    if (!((FoldersTreeNode) children.elementAt(i)).isLeaf())
		number++;

	return number;
    }

    public Object getChild(Object parent, int index)
    {
	Vector children = ((FoldersTreeNode) parent).getChildren();
	int number = 0;

	if (children == null || children.size() < 0 ||
	    index >= children.size())
	    return null;

	for (int i = 0; i < children.size(); i++)
	    if (!((FoldersTreeNode) children.elementAt(i)).isLeaf()) {
		number++;
		if (number-1 == index)
		    return (FoldersTreeNode) children.elementAt(i);
	    }

	return null;
    }

    public int getIndexOfChild(Object parent, Object child)
    {
	Vector children = ((FoldersTreeNode) parent).getChildren();

	if (children == null)
	    return -1;

	String childName = ((FoldersTreeNode) child).toString();
	for (int i = 0; i < children.size(); i++)
	    if (!((FoldersTreeNode) children.elementAt(i)).isLeaf())
		if (childName.equals(children.get(i)))
		    return i;

	return -1;
    }

    // This method is invoked by the JTree if tree is changed
    public void valueForPathChanged(TreePath path, Object newValue) {}

    public void addTreeModelListener(TreeModelListener l) {}
    public void removeTreeModelListener(TreeModelListener l) {}
}
