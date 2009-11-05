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

import java.io.*;

import mind.model.*;

/**
 * Abstract class wich tells wich methods needs to be
 * implemented by the file formats.
 *
 * @author Henrik Norin
 * @version 2001-05-07
 */
public interface ModelFileFormat
{
    /**
     * To load a saved model.
     *
     * @param model Model to load.
     * @param filename File to load from.
     */
    public void load(Model model, File filename)
	throws IOException, FileInteractionException;

    /**
     * To save an excisting model.
     *
     * @param model Model to save.
     * @param filename File to save to.
     */
    public void save(Model model, File filename)
	throws IOException, FileInteractionException;
}
