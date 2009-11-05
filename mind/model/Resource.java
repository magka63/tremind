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

/**
 * A resource is material or energy, flowing in the model.
 *
 * @author Peter Åstrand
 * @version 2001-04-01
 */
package mind.model;

import mind.gui.*;
import mind.io.*;

public class Resource
{
    // The resource ID.
    private ID c_ID = null;

    // The resource label.
    private String c_label = null;

    // The resource unit.
    private String c_unit = null;

    // Unit prefix.
    private String c_prefix = null;

    // The colour of the resource
    private ExtendedColor c_color = null;


    /**
     * A nullconstructor.
     */
    public Resource()
    {

    }

    /**
     * A constructor without color to support upgrade.
     *
     * @param resourceID The ID of the resource
     * @param label The name of the resouce
     * @param unit The unit of the resource (W, kr)
     * @param prefix The prefix of the unit (k, M, ...)
     */
    public Resource(ID resourceID, String label, String unit, String prefix)
    {
	c_ID = resourceID;
	c_label = label;
	c_unit = unit;
	c_prefix = prefix;
        c_color = new ExtendedColor("Black", java.awt.Color.black);
    }

    /**
     * A fullconstructor.
     *
     * @param resourceID The ID of the resource
     * @param label The name of the resouce
     * @param unit The unit of the resource (W, kr)
     * @param prefix The prefix of the unit (k, M, ...)
     * @param color The color of the resource
     */
    public Resource(ID resourceID, String label, String unit, String prefix, ExtendedColor color)
    {
	c_ID = resourceID;
	c_label = label;
	c_unit = unit;
	c_prefix = prefix;
        c_color = color;
    }


    /**
     * Gets the ID of the resource
     * @return the resource ID.
     */
    public ID getID()
    {
	return c_ID;
    }

    /**
     * Returns the resource label.
     * @return The label of the resource.
     *
     */
    public String getLabel()
    {
	return c_label;
    }

    /**
     * Returns the resource prefix.
     * @return The prefix of the resource
     */
    public String getPrefix()
    {
	return c_prefix;
    }

    /**
     * Returns the resource unit.
     * @return The unit of the resource
     */
    public String getUnit()
    {
	return c_unit;
    }

    /**
     * Returns the color of the resource
     * @return The color of the resource.
     */
    public ExtendedColor getColor()
    {
        return c_color;
    }

    /**
     * Sets the resource ID.
     *
     * @param resourceID The resource ID.
     */
    public void setID(ID resourceID)
    {
	c_ID = resourceID;
    }

    /**
     * Sets the resource label.
     *
     * @param label The new label.
     */
    public void setLabel(String label)
    {
	c_label = label;
    }

    /**
     * Sets the resource prefix.
     *
     * @param prefix The new prefix.
     */
    public void setPrefix(String prefix)
    {
	c_prefix = prefix;
    }

    /**
     * Sets the resource unit.
     *
     * @param unit The new unit.
     */
    public void setUnit(String unit)
    {
	c_unit = unit;
    }

    /**
     * Sets the color of the resource
     * @param color The new color of the resource.
     */
    public void setColor(ExtendedColor color)
    {
        c_color = color;
    }

    /**
     * Converts the object to an XML-representation.
     * @param indent The number of whitespaces to use in indentation.
     * @return The XML-representation as a String.
     */
    public String toXML(int indent)
    {
        // Start tag and add label.
        String xml = XML.indent(indent) +
                     "<resource type=\"" +
                     XML.toXML(c_label) +
                     "\">" +
                     XML.nl();
        // Add the unit if we have one
        if (c_unit != null && !c_unit.equals("")) {
            xml += XML.indent(indent+1) +
                   "<unit>" +
                   XML.toXML(c_unit) +
                   "</unit>" +
                   XML.nl();
        }

        // Add the prefix if we have one
        if (c_prefix != null && !c_prefix.equals("")) {
            xml += XML.indent(indent+1) +
                   "<prefix>" +
                   XML.toXML(c_prefix) +
                   "</prefix>" +
                   XML.nl();
        }

        // Add the color if we have one
        if (c_color != null) {
            xml += XML.indent(indent+1) +
                   "<colorname>"+
                   XML.toXML(c_color.toString())+
                   "</colorname>"+
                   XML.nl()+
                   XML.indent(indent+1)+
                   "<colorvalue>"+
                   XML.toXML(String.valueOf(c_color.getRGB()))+
                   "</colorvalue>"+
                   XML.nl();
        }

        // End tag
        xml += XML.indent(indent) + "</resource>" + XML.nl();
        return xml;
    }

    /**
     * Same as getLabel
     * @return a textual representation of the resource
     */
    public String toString()
    {
	return getLabel();
    }
}
