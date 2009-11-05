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

import mind.automate.Pair;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * The class that implements this interface is used for perfoming the
 * actual optimization.
 *
 * @author Andreas Remar
 * @version $Revision: 1.8 $ $Date: 2005/05/06 10:22:45 $
 */
public abstract class OptimizeController
{
    /**
     * Optimizes the MPS file given as argument using some optimizer.
     *
     * @param filename An MPS file.
     * @throws IOException Thrown if there was some general error while
     *                     starting the optimizer.
     * @throws FileNotFoundException Thrown if the OCF, MPS or OPT file
     *                               can't be found.
     * @return A pair containing stdout/stderr from the optimizer and
     *         an OptimizationResult.
     */
    public abstract Pair optimize(String filename,
			 String optimizerPath)
	throws IOException, FileNotFoundException;

    /**
     * Loads optimization result from a previous run of the optimizer.
     *
     * @param filename An OPT file.
     * @return A pair containing a bogus string and an OptimizationResult.
     */
    public abstract Pair load(String filename)
	throws FileNotFoundException, IOException;

    protected String getFilenameWithoutExtension(String filename) {
      int index = filename.lastIndexOf('.');
      if (index == -1)
        return filename;
      return filename.substring(0, index);
  }

}
