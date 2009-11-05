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
package mind.io;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * This class acts as a file filter.
 *
 * @version 2001-05-26
 * @author Tim Terlegård
 */
public class SaveAsFileFilter
    extends FileFilter
{
    private String c_description = null;
    private String c_extension = null;

    public SaveAsFileFilter(String extension, String description) {
	c_extension = extension;
	c_description = description;
    }

    /**
     * Checks if the current file fits the filters.
     * Files that begin with "." are ignored.
     * @param file The file that is to be checked.
     * @return True if this file should be shown in the directory pane,
     * false if it shouldn't.
     */
    public boolean accept(File file) {
	if(file != null) {
	    if(file.isDirectory())
		return true;

	    String extension = getExtension(file);
	    if (extension != null)
		if(extension.equals(c_extension))
		    return true;
	}
	return false;
    }

    /**
     * Gets the extension portion of the file's name.
     * @return The extension portion of the file's name.
     */
     public String getExtension(File file) {
	if(file != null) {
	    String filename = file.getName();
	    int i = filename.lastIndexOf('.');
	    if(i > 0 && i < filename.length()-1)
		return filename.substring(i+1).toLowerCase();
	}
	return null;
    }

    /**
     * Gets the description of this filter.
     * @return The description of this filter.
     */
    public String getDescription()
    {
	return c_description;
    }
}
