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

import java.util.ArrayList;
import java.util.ListIterator;
import mind.model.ID;

/**
 * This class holds all information about an optimization that is needed
 * by the rest of the automization process.
 *
 * @author Andreas Remar
 * @version $Revision: 1.5 $ $Date: 2004/12/09 15:51:21 $
 */
public class OptimizationResult
{
    /** The global optimum (objective value). */
    public double globalOptimum;
    ArrayList flows;

    public OptimizationResult()
    {
	flows = new ArrayList();
    }

    /**
     * Adds a flow to the result.
     *
     * @param id The id of this flow.
     * @param values The values at different timesteps for this flow.
     * @throws IllegalArgumentException Thrown if one of id or values == null.
     */
    public void addFlow(ID id, double values[])
	throws IllegalArgumentException
    {
	if(id == null || values == null) {
	    throw new IllegalArgumentException();
	}

	Flow flow = new Flow();
	flow.id = id;
	flow.values = (double[])values.clone();

	flows.add(flow);
    }

    /**
     * Gets the flows in this result.
     *
     * @return An iterator over the flows in this result.
     */
    public ListIterator getFlows()
    {
	return flows.listIterator();
    }
}
