/*
 * Copyright 2001:
 * Peter Andersson <petan117@student.liu.se>
 * Martin Hagman <marha189@student.liu.se>
 * Henrik Norin <henno776@student.liu.se>
 * Anna Stjerneby <annst566@student.liu.se>
 * Tim Terleg�rd <timte878@student.liu.se>
 * Johan Trygg <johtr599@student.liu.se>
 * Peter �strand <petas096@student.liu.se>
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
 *
 * @author Tim Terleg�rd
 * @author Johan Trygg
 */
package mind.io;

public class XML
{
    public static String INDENT = "  ";

 // PUM5: Added this function
    public static String getExmlHeader()
    {
        //XML xml = new XML();
        String str = "<?xml version='1.0' encoding='ISO-8859-1'?>" + nl() +
        "<?mso-application progid=\"Excel.Sheet\"?>" + nl();

        return str;
    }

    public static String getHeader(String root)
    {
        XML xml = new XML();
        // Added MIND version to the rmd file by Nawzad Mardan 20100223
        String str = "<?xml version='1.0' encoding='ISO-8859-1'?>" + nl() +
            "<!DOCTYPE " + root + " PUBLIC \""  + root + ".dtd\" \"/" + root + ".dtd\">" + nl()+
            "<!-- reMIND version "+ mind.GlobalStringConstants.reMIND_version +" -->"+ nl();

        return str;
    }

    public static String indent(int indentLevel)
    {
	String str = "";
	for (int i = 0; i < indentLevel; i++)
	    str += INDENT;

	return str;
    }

    public static String nl()
    {
	return System.getProperty("line.separator");
    }


    public static String toXML(String str)
    {
	String newStr;

	//Replace all &,>,< with &amp, &gt, &lt;
	newStr = strReplace(str, '&', "&amp;");
	newStr = strReplace(newStr, '>', "&gt;");
	newStr = strReplace(newStr, '<', "&lt;");
	newStr = strReplace(newStr, '"', "&quot;");

	return newStr;
    }

    /* Replaces all characters oldCh with the string newStr
     *
     */
    private static String strReplace(String str, char oldCh, String newStr)
    {
	int begin,end;
	int fromIndex=0;
	int newStrLen = newStr.length();
	begin = str.indexOf(oldCh,fromIndex);
	while(begin != -1) {
	    end = begin + newStrLen;
	    str = str.substring(0,begin) + newStr + str.substring(begin+1,str.length());
	    fromIndex = end;
	    begin = str.indexOf(oldCh,fromIndex);
	}
	return str;
    }
}
