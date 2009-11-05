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

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import mind.model.*;

/**
 * @author Tim Terlegård
 */

public class Zoom
{
    private static Hashtable c_hash = new Hashtable();
    private static int c_selected = 100;

    public Zoom()
    {
	for (int i = 10; i <= 200; i += 10)
	    c_hash.put(new Integer(i), i + " %");
    }

    public static Object getSelected()
    {
	return c_hash.get(new Integer(c_selected));
    }

    public static Vector getStrings()
    {
	Vector strings = new Vector(0);
	TreeSet set = new TreeSet(c_hash.keySet());
	Iterator i = set.iterator();

	while (i.hasNext())
	    strings.addElement(c_hash.get(i.next()));

	return strings;
    }

    public int getValue()
    {
	return c_selected;
    }

    public void setSelected(String select)
    {
	Set set = c_hash.keySet();
	Iterator i = set.iterator();
	Integer next;

	while(i.hasNext()) {
	    next = (Integer) i.next();
	    if (c_hash.get(next).equals(select))
		c_selected = next.intValue();
	}
    }
}
