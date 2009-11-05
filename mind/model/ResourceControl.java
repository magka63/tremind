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
package mind.model;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import mind.gui.*;

/**
 * Keeps track of all resources in the model.
 *
 * @author Peter Åstrand
 * @version 2001-04-01
 */
public class ResourceControl
    extends Hashtable
{
    /**
     * Constructor.
     */
    public ResourceControl()
    {

    }

    /**
     * Adds a new resource.
     *
     * @param label The label of the new resource.
     * @param unit The unit for the new resource.
     * @param prefix The prefix for the new resource.
     */
    public ID addResource(String label, String unit, String prefix, ExtendedColor color)
    {
	ID resource_id = new ID(ID.RESOURCE);
	Resource new_resource = new Resource(resource_id, label,
					     unit, prefix, color);

	put(new_resource.getID(), new_resource);
	return resource_id;
    }
    /**
     * Adds a new resource.
     *
     * @param label The label of the new resource.
     * @param unit The unit for the new resource.
     * @param prefix The prefix for the new resource.
     */
    public ID addResource(String label, String unit, String prefix)
    {
	ID resource_id = addResource( label, unit, prefix, null);
	return resource_id;
    }
    /**
     * Get the IDs of all resources.
     *
     *
    public Vector getAllResources()
    {
	return new Vector(keySet());
    }*/

    /**
     * Gets the label of a certain resource.
     * @param resourceID The ID of the resource.
     * @return The label of this resource.
     */
    public String getLabel(ID resourceID)
    {
	if (resourceID == null)
	    return null;

	Resource resource = (Resource) get(resourceID);
	if (resource != null)
	    return resource.getLabel();

	return null;
    }

    /**
     * Gets a resource using the resources label
     * @param label The label of the resource to get
     * @param unit The unit of the resource to get
     * @param prefix The prefix of the resource to get
     */
    public ID getResourceID(String label, String unit, String prefix)
    {
	// Iterate over all resources in hash table
	for (Enumeration e = elements(); e.hasMoreElements();) {
	    Resource r = (Resource)e.nextElement();
	    if (r.getLabel().equals(label) &&
		r.getUnit().equals(unit) &&
		r.getPrefix().equals(prefix))
		return r.getID();
	}
	return null;
    }

    /**
     * Gets a resource using the resources label
     * @param label The label of the resource to get
     */
    public ID getResourceID(String label)
    {
	// Iterate over all resources in hash table
	for (Enumeration e = elements(); e.hasMoreElements();) {
	    Resource r = (Resource)e.nextElement();
	    if (r.getLabel().equals(label))
		return r.getID();
	}
	return null;
    }


    /**
     * Gets all resources.
     * @return All resources available.
     */
    public Vector getResources()
    {
	return new Vector(values());
    }

    /**
     * Returns true if control contains defined resource
     * @param label The label of the resource to check
     * @param unit The unit of the resource to check
     * @param prefix The prefix of the resource to check
     */
    public boolean contains(String label, String unit, String prefix)
    {
	return getResourceID(label, unit, prefix) != null;
    }

    /**
     * Removes a resource from the model.
     *
     * @param resourceID
     * @throws ModelException is thrown if trying to remove
     * non-existent resourceID.
     */
    public ID removeResource(ID resourceID)
	throws ModelException
    {
	if (!containsKey(resourceID)) {
	    throw new ModelException();
	}
	remove(resourceID);
	return resourceID;
    }

    /**
     * Finds a Resource object from its ID
     * @param resource the ID of the resource
     * @return The resource
     */
    public Resource getResource(ID resource)
    {
	return ((Resource)get(resource));
    }

    /**
     * Converts all resources to XML format.
     * @param indent The number of whitespaces to use as indentation
     * @return All resources in XML-format
     */
    public String toXML(int indent)
    {
	Collection resources = values();
	if (resources == null)
	    return "";
	Iterator iterator = resources.iterator();
	Resource resource = null;
	String xml = "";

	while (iterator.hasNext()) {
	    resource = (Resource) iterator.next();
	    xml += resource.toXML(indent);
	}

	return xml;
    }
}
