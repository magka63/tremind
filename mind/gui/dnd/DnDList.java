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

import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;

import java.util.Hashtable;
import java.util.List;
import java.util.Iterator;

import java.io.*;
import java.io.IOException;

import javax.swing.JList;
import javax.swing.DefaultListModel;

/**
 * This class is a Drag-and-drop JList.
 *
 * @author Tim Terlegård
 * @version 2001-04-08
 */

public class DnDList
    extends JList
    implements DnDComponentInterface, DragSourceListener, DragGestureListener
{

    // enables this component to be a Drag Source
    DragSource c_dragSource = null;

    public DnDList()
    {
	c_dragSource = new DragSource();
	c_dragSource.
	    createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE,
					       this);
	setModel(new DefaultListModel());
    }

    // a drag gesture has been initiated
    public void dragGestureRecognized(DragGestureEvent event)
    {
	Object selected = getSelectedValue();

	if (selected != null) {
	    StringSelection text = new StringSelection(selected.toString());

	    // as the name suggests, starts the dragging
	    c_dragSource.startDrag (event, DragSource.DefaultMoveDrop, text, this);
	} //else {

	//}
    }

    // this message goes to DragSourceListener, informing it
    // that the dragging  has ended
    public void dragDropEnd (DragSourceDropEvent event)
    {
	if (event.getDropSuccess());
    }

    // this message goes to DragSourceListener, informing it
    // that the dragging has entered the DropSite
    public void dragEnter (DragSourceDragEvent event) {}

    // this message goes to DragSourceListener, informing it
    // that the dragging has exited the DropSite
    public void dragExit (DragSourceEvent event) {}

    // this message goes to DragSourceListener, informing it
    // that the dragging is currently ocurring over the DropSite
    public void dragOver (DragSourceDragEvent event) {}

    // is invoked when the user changes the dropAction
    public void dropActionChanged (DragSourceDragEvent event) {}

    // adds elements to itself
    public void addElement(Object o)
    {
	((DefaultListModel) getModel()).addElement(o.toString());
    }

    // removes an element from itself
    public void removeElement()
    {
	((DefaultListModel) getModel()).removeElement(getSelectedValue());
    }
}
