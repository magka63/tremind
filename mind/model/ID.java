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

import java.util.Vector;
import java.util.Iterator;

/* IMPORTANT: Of some kind of stange reason ID objects cannot be used
   safely as keys in hashtables. If there exists two different ID objects
   that are equal (same type and number) they are considerd to be
   two DIFFERENT keys by Hashtable.

   Maybe it has something to do with the hashCode... (JT 01-07-13)
*/

/**
 * Class to uniquely define different type of objects
 *
 * @author Tim Terlegård
 * @author Peter Andersson
 * @author Johan Trygg
 * @version 2001-06-15
 */
public class ID
    implements Comparable
{
    public final static String FLOW     = "F";
    public final static String NODE     = "N";
    public final static String RESOURCE = "R";
    public final static String FUNCTION = "Fu";

    private final long ID_START = 1;

    // all IDs are saved
    private static Vector c_flows = new Vector(0);
    private static Vector c_nodes = new Vector(0);
    private static Vector c_resources = new Vector(0);
    private static Vector c_functions = new Vector(0);

    private String c_type;
    private long c_number;
    private String c_ID_as_string;


    public static ID[] getAllFlowIDs()
    {
      ID[] flowIDs;
      int index = 0;

      flowIDs = new ID[c_flows.size()];

      Iterator iter = c_flows.iterator();

      while (iter.hasNext()) {
        flowIDs[index++] = new ID((Long) iter.next(), "F");
      }
      return flowIDs;
    }

    /**
     * A private constructor for creating instances of IDs that already exist.
     * Somewhat dangerous to use
     * @param number
     * @param type
     * @see getAllFlowIDs()
     */
    public ID(Long number, String type)
    {
      c_number = number.longValue();
      c_type = new String(type);
      createToStringRepresentation(); // inits c_ID_as_string
    }

    /**
     * Creates an ID of type
     * @param type the type of ID to create
     * @throws IllegalArgumentException if the type is not in the enumeration
     */
    public ID(String str)
	throws IllegalArgumentException
    {
	if (isEnumeratedType(str)) {
	    //str is a type
	    c_type = new String(str);

	    if (str.equals(FLOW))
		newId(c_flows);
	    else if (str.equals(NODE))
		newId(c_nodes);
	    else if (str.equals(FUNCTION))
		newId(c_functions);
	    else if (str.equals(RESOURCE))
		newId(c_resources);

            createToStringRepresentation(); // inits c_ID_as_string

	}
	else {
	    //Check if the string is a type + number

	    //This is not perfect. This way we cannot say that we want a ID with
	    //the name str of a certain type. (could be nice to use in the
	    //RmdSAXhandler, if we mess with a rmd file now, we can make
            //nodes with ids starting with F and flows with ids starting with N)

	    String num;
	    long idNumber = 1;
	    boolean ok = false;
	    try {
		if (str.startsWith(NODE)) {
		    num = str.substring(NODE.length());
		    idNumber = Long.parseLong(num);
		    ok = newId(c_nodes,idNumber);
		    c_type = NODE;
		}
		else if (str.startsWith(RESOURCE)) {
		    num = str.substring(RESOURCE.length());
		    idNumber = Long.parseLong(num);
		    ok = newId(c_resources,idNumber);
		    c_type = RESOURCE;
		}
		else if (str.startsWith(FUNCTION)) {
		    num = str.substring(FUNCTION.length());
		    idNumber = Long.parseLong(num);
		    ok = newId(c_functions,idNumber);
		    c_type = FUNCTION;
		}
		else if (str.startsWith(FLOW)) {
		    num = str.substring(FLOW.length());
		    idNumber = Long.parseLong(num);
		    ok = newId(c_flows,idNumber);
		    c_type = FLOW;
		}

                createToStringRepresentation();

		if (!ok) throw new IllegalArgumentException("The id '" + str + "' is already used.");
		//replace the line above with the line below to be abel to
		//create several IDs that are equal:
		//c_number = idNumber;
       	    }
	    catch (NumberFormatException e) {
		throw new IllegalArgumentException("Illegal ID");
	    }
	}

    }


    /**
     * Checks if the other IDs number is greater, equal or less
     * @return -1 if less<br>0  if equal<br>1  if greater.
     */
    public int compareTo(Object otherID)
    {
	ID id = (ID) otherID;
	//If different types, the objects are not equal.
	if (!c_type.equals(id.getType()))
	    return c_type.compareTo(id.getType());

	if (c_number < id.getNumber())
	    return -1;
	else if (c_number > id.getNumber())
	    return 1;
	else
	    return 0;
    }

    /**
     * Returns true if two IDs are equal
     * @param otherID ID to compare with
     * @return True if type and number are the same
     */
    public boolean equals(Object otherID)
    {
	if (otherID == null) {
	    return false;
	}

	ID id = (ID) otherID;

	return (c_type.equals(id.getType()) &&
		(id.getNumber() == c_number));

    }

    /**
     * Returns the unique number for this IDtype
     * @return the functions number
     * @deprecated use getNumber instead, returns a long instead of ID
     */
    public long getID()
    {
	return getNumber();
    }

    public int hashCode()
    {
	return (int)c_number;
    }

    /**
     * Returns the unique number for this IDtype
     * @return the functions number
     */
    public long getNumber()
    {
	return c_number;
    }

    /**
     * Returns the type
     * @return The functions type
     */
    public String getType()
    {
	return c_type;
    }

    /**
     * Checks if ID is a flowID
     * @return True if flowID
     */
    public boolean isFlow()
    {
	return c_type.equals( FLOW );
    }

    /**
     * Returns true if ID is nodeID
     * @return True if nodeID
     */
    public boolean isNode()
    {
	return c_type.equals( NODE );
    }

    /**
     * Checks if ID is a resourceID
     * @return True if resourceID
     */
    public boolean isResource()
    {
	return c_type.equals( RESOURCE );
    }

    /**
     * checks if ID is a functionID
     * @return True if functionID
     */
    public boolean isFunction()
    {
	return c_type.equals( FUNCTION );
    }


    public void remove()
    {
	if (c_type.equals(FLOW))
	    c_flows.remove(new Long(getNumber()));
	else if (c_type.equals(NODE))
	    c_nodes.remove(new Long(getNumber()));
	else if (c_type.equals(FUNCTION))
	    c_functions.remove(new Long(getNumber()));
	else if (c_type.equals(RESOURCE))
	    c_resources.remove(new Long(getNumber()));
    }

    /**
     * Reset the all saved ID vectors
     * Must only be used when creating a new scenario
     */
    public static void reset()
    {
	c_flows = new Vector(0);
	c_nodes = new Vector(0);
	c_resources = new Vector(0);
	c_functions = new Vector(0);
    }

    /**
     * Returns a textual representation of the ID
     * This function is called millions and millions of times!!!
     * Optimized by caching the toString representation in c_ID_as_string;
     * @return A string that represents the ID
     */
    public String toString()
    {
	// return c_type + Long.toString(c_number);
        return c_ID_as_string;
    }

    /**
     *
     */
    private void createToStringRepresentation()
    {
      c_ID_as_string = c_type + Long.toString(c_number);
    }

    /**
     * When an ID is eaten by the garbage collector we know that
     * it isn't used anymore. Then we can reuse it.
     * Java's garbage collector is very slow, so you can't know at
     * what time the ID is gonna be able to be reused.
     */
    /*   //This does not work. Don't use.
     protected void finalize()
     {
	 if (isFlow())
	     c_flows.remove(new Long(getNumber()));
	 else if (isNode())
	     c_nodes.remove(new Long(getNumber()));
     }
    */

    private boolean isEnumeratedType( String type )
    {
	return type.equals( NODE ) ||
	       type.equals( FLOW ) ||
	       type.equals( RESOURCE ) ||
	       type.equals( FUNCTION );
    }

    private void newId(Vector ids)
    {
	boolean found = false;

	// when we reach the maximum long, the next will be negative
	// then we stop searching
	for (long i = ID_START; i >= 0 && !found; i++)
	    if (!ids.contains(new Long(i))) {
		ids.addElement(new Long(i));
		c_number = i;
		found = true;
	    }
    }

    private boolean newId(Vector ids, long number)
    {
        if (ids.contains(new Long(number)))
	    //The number already exists
	    return false;

	ids.addElement(new Long(number));
	c_number = number;
	return true;
    }
}
