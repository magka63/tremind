/*
 * Copyright 2010:

 * Nawzad Mardan <nawzad.mardan.liu.se>
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

/**
 * The class for changing a resource name
 * @author Nawzad Mardan
 * @version 2010-03-02
 */


package mind.model;

/**
 *
 * @author nawma77
 */
public class ResourceName
{
    private String c_newResourceName = "";
     public void setNewResourceName(String nrn)
        {
        c_newResourceName =  nrn;
        }
     public String getResourceName()
        {
        return c_newResourceName;
        }

}
