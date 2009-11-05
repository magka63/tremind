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

import mind.model.Model;
import mind.io.FileInteraction;
import mind.automate.OptimizationResult;
import mind.io.Txt;
import mind.io.OptimizationCommandFile;
import mind.io.FileInteractionException;
import mind.automate.OptimizationException;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.ListIterator;
import mind.GlobalStringConstants;

/**
 * Performs optimization automatically and writes the result in a
 * humanreadable format.
 *
 * @author Andreas Remar
 * @version $Revision: 1.17 $ $Date: 2005/05/06 10:22:45 $
 */
public class Automate
{
    Model c_model;
    OptimizationResult c_result;
    String c_optimizerOutput;

    /**
     * Constructs an Automate object.
     *
     * @param m A Model object, needed to perform automatic creation
     *          of MPS files
     */
    public Automate(Model m)
    {
	c_model = m;
    }

    /**
     * Optimizes a model automatically with some optimizer.
     *
     * @param filename The model to optimize
     * @param optimizer The optimizer program to use
     * @param optimizerPath Path to the optimizer program executable
     * @param dateInMpsFile If false, overwrite created MPS file, else add
     *                      current date and time to filename.
     * @param dateInOptFile If false, overwrite created OPT file, else add
     *                      current date and time to filename.
     * @throws FileInteractionException Thrown when unable to create MPS.
     * @return An OptimizationResult representing the optimal solution
     *         to the problem. If this is null, there was some problem
     *         while optimizing.
     */
    public OptimizationResult
	optimize(String filename, String optimizer, String optimizerPath,
		 Model model)
	throws FileInteractionException, OptimizationException,
	       FileNotFoundException, IOException
    {
	return optimizeWithSettings(filename, optimizer, optimizerPath,
				    null, null, model);
    }

    /**
     * Optimizes a model automatically with some optimizer.
     * May begin optimization from an old MPS or OPT file.
     *
     * @param filename The model to optimize
     * @param optimizer The optimizer program to use
     * @param optimizerPath Path to the optimizer program executable
     * @param dateInMpsFile If false, overwrite created MPS file, else add
     *                      current date and time to filename.
     * @param dateInOptFile If false, overwrite created OPT file, else add
     *                      current date and time to filename.
     * @param mpsFile Don't create an MPS file, start from this instead
     * @param optFile Don't optimize, just read in the result from the
     *                OPT file.
     * @throws FileInteractionException Thrown when unable to create MPS.
     * @return An OptimizationResult representing the optimal solution
     *         to the problem. If this is null, there was some problem
     *         while optimizing.
     */
    public OptimizationResult
	optimizeWithSettings(String filename, String optimizer,
			     String optimizerPath,
			     String mpsFile, String optFile, Model model)
	throws FileInteractionException, OptimizationException,
	       FileNotFoundException, IOException
    {
	Pair m_pair = new Pair();
	m_pair.left = m_pair.right = null;

	if(filename == null) {
	    throw new IllegalArgumentException("Filename is empty (null).");
	}
	if(optFile == null && optimizerPath == null) {
	    throw new IllegalArgumentException("Optimizer path not defined!\n"
					       + "Set path in Options");
	}

	/* remove extension */
	String m_file = getFilenameWithoutExtension(filename);

	/* warn about illegal paths (unless we open an OPT file) */
	if(optFile == null && m_file.indexOf(" ") != -1) {
	    throw new OptimizationException("Filenames and paths containing"
					    + " SPACE not allowed.");
	}

	/* create the complete MPS filename */
	String m_mpsFilename = m_file + ".mps";

	/* create MPS */
	if(mpsFile == null && optFile == null) {
	    FileInteraction m_fileInteraction = new FileInteraction();
	    try {
		m_fileInteraction.save(null,model, FileInteraction.MPS,
				       new File(m_mpsFilename));
	    }
	    catch(FileInteractionException e) {
		/* Couldn't save MPS file  */
		throw new FileInteractionException(e.getMessage());
	    }
	    catch(NullPointerException e) {
		/* fileInteraction == null */
		throw new FileInteractionException("Can't initialize"
						   + " file interaction");
	    }
	}

	/* utför optimering/ladda resultatet
	 * (CplexController.optimize eller CplexController.load) */
	OptimizeController m_controller;

	if(optimizer.equals(GlobalStringConstants.OPT_CPLEX)) {
	    m_controller = new CplexController();
	} else if (optimizer.equals(GlobalStringConstants.OPT_LPSOLVE)) {
            m_controller = new lp_SolveController();
	}  else {
	    /* no other optimization controller so assume
	     * we want to use this */
	    throw new OptimizationException("No optimizer selected.");
	}

	if(optFile != null) {
	    /* ladda resultat */
	    try {
		m_pair = m_controller.load(optFile);
	    } catch(FileNotFoundException e) {
		throw new FileNotFoundException(e.getMessage());
	    } catch(IOException e) {
		throw new IOException(e.getMessage());
	    }
	}
	else if(mpsFile != null) {
	    try {
		m_pair = m_controller.optimize(mpsFile, optimizerPath);
	    } catch(FileNotFoundException e) {
		throw new FileNotFoundException(e.getMessage());
	    } catch(IOException e) {
		/* unable to initialize optimizer */
		throw new OptimizationException("Unable to start optimizer. "
						+ e.getMessage());
	    }
	}
	else {
	    try {
		m_pair = m_controller.optimize(m_file+".mps", optimizerPath);
	    } catch(FileNotFoundException e) {
		throw new FileNotFoundException(e.getMessage());
	    } catch(IOException e) {
		/* unable to initialize optimizer */
		throw new OptimizationException("Unable to start optimizer. "
						+ e.getMessage());
	    }
	}


	/* lägg till labels i result */
	OptimizationResult result = (OptimizationResult)m_pair.right;
	if(result != null) {
	    ListIterator li = result.getFlows();
	    Flow f;
	    while(li.hasNext()) {
		f = (Flow)li.next();
		f.label = c_model.getLabel(f.id);
	    }
	}

	/* spara resultatet */
	c_optimizerOutput = (String)(m_pair.left);

	/* returnera OptimizationResult */
	return (OptimizationResult)(m_pair.right);
    }

    /**
     * Save the optimization result in an excel (human) readable format.
     *
     * @param filename The file to save the result in
     * @param result The result to save in the file
     * @throws IOException Thrown if the file couldn't be created/written to
     * @throws IllegalArgumentException Thrown if either parameter is
     *                                  null or if the OptimizationResult
     *                                  is malformed
     */
    public void output(String filename, OptimizationResult result)
	throws IOException, IllegalArgumentException
    {
	try {
	    Txt.save(filename, result);
	} catch(IOException e) {
	    throw new IOException(e.getMessage());
	} catch(IllegalArgumentException e) {
	    throw new IllegalArgumentException(e.getMessage());
	}
    }

    /**
     * Return stdout/stderr from the optimizer.
     *
     * @return Stdout/stderr from the optimizer
     */
    public String getOptimizerOutput()
    {
	return c_optimizerOutput;
    }

    /**
     * Return the commands that will be sent to the optimizer at optimization.
     *
     * @return The commands.
     * @throws IOException If commandfile wasn't found or there was some
     *                     problem with reading the command file.
     * @see #setOptimizationCommands
     */
    public String getOptimizationCommands(String optimizer)
	throws IOException, FileNotFoundException
    {
	String commands;
	try {
            if (optimizer.equals(GlobalStringConstants.OPT_CPLEX))
                commands = OptimizationCommandFile.load(GlobalStringConstants.CPLEX_COMMAND_FILE);
             else
                commands = OptimizationCommandFile.load(GlobalStringConstants.LPSOLVE_COMMAND_FILE);
	}
	catch(FileNotFoundException e){
	    throw new FileNotFoundException("Optimizer commandfile not found.");
	}
	catch(IOException e){
	    throw new IOException(e.getMessage());
	}
	return commands;
    }

    /**
     * Saves the optimization commands in a file.
     *
     * @param commands The commands to save in the file
     * @throws IOException Thrown when optimization command file can't
     *                     be written
     * @see #getOptimizationCommands
     */
    public void setOptimizationCommands(String optimizer, String commands)
	throws IOException
    {
	try {
            if (optimizer.equals(GlobalStringConstants.OPT_CPLEX))
              OptimizationCommandFile.save(GlobalStringConstants.CPLEX_COMMAND_FILE, commands);
            else
              OptimizationCommandFile.save(GlobalStringConstants.LPSOLVE_COMMAND_FILE, commands);
	} catch(IOException e) {
	    throw new IOException("Unable to save commands. "
				  + e.getMessage());
	}
    }

    private String getFilenameWithoutExtension(String filename) {
	int index = filename.lastIndexOf('.');
	if (index == -1)
	    return filename;
	return filename.substring(0, index);
    }
}
