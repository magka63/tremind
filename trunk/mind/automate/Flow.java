/*
 * Copyright 2004:
 * Johan Bengtgsson <johbe496@student.liu.se>
 * Daniel Campos <danca226@student.liu.se>
 * Martin Fagerfj?ll <marfa233@student.liu.se>
 * Daniel Ferm <danfe666@student.liu.se>
 * Able Mahari <ablma616@student.liu.se>
 * Andreas Remar <andre063@student.liu.se>
 * Haider Shareef <haish292@student.liu.se>
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

package mind.automate;

import mind.model.ID;

/**
 * Struct for storing flows in the automization process.
 *
 * @author Andreas Remar
 * @version $Revision: 1.6 $ $Date: 2004/12/09 15:51:21 $
 */
public class Flow
{
    public Flow() {
	id = null;
	label = null;
	values = null;
	selected = true;
    }

    /** Id of this flow. */
    public ID id;

    /** Textual representation of this flow. */
    public String label;

    /** Values of this flow at different timesteps. */
    public double values[];

    /** True if this flow should be included in the excel readable file. */
    public boolean selected;
    
    public String toString() {
	String str = null;
	if (id != null) {
	    str = id.toString();
	    if (label != null) {
		str = str + ": " + label;
	    }
	}
	return str;
    }
}
