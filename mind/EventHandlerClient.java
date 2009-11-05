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

package mind;

import java.util.Vector;
import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;

import mind.model.*;
import mind.gui.*;
import mind.gui.dnd.*;
import mind.io.*;

/* Added by reMIND2004 */
import java.io.IOException;
import java.io.FileNotFoundException;
import mind.automate.OptimizationResult;
import mind.automate.OptimizationException;

/**
 * Interface between GUI and Model, through EventHandlerServer.
 *
 * @author reMIND2001
 * @author Andreas Remar
 * @version $Revision: 1.13 $ $Date: 2005/05/14 07:53:24 $
 */
public class EventHandlerClient 
{
    private EventHandlerServer c_eventHandler;
    private boolean c_isChanged;

    public EventHandlerClient()
    {
	c_eventHandler = new EventHandlerServer(this);
    }

    /**
     * Adds a node folder to the database.
     * @param folder The name of the new folder to add.
     * @param parent The parent node of the new folder.
     */
    public void addFolderToDatabase(String folder, NodeToDrag parent)
    {
	c_eventHandler.addFolderToDatabase(folder, parent);
    }

    /**
     * Adds a function to a node.
     *
     * @param nodeID The node ID.
     * @param functionType The functiontype to add.
     * @throws ModelException is thrown when FIXME.
     */
    public void addFunction(ID nodeID, String functionType)
	throws ModelException
    {
	c_eventHandler.addFunction(nodeID, functionType);
    }

    /**
     * Adds a node to the database as a new nodetype.
     * @param nodeID The ID of the node to add.
     * @param node The name of the new nodetype to be stored in database.
     * @param folder The folder that the node will be stored in.
     */
    public void addNodeToCollection(ID nodeID, String node, NodeToDrag folder)
	throws ModelException
    {
	c_eventHandler.addNodeToCollection(nodeID, node, folder);
    }

    /**
     * Add a new resource to the model.
     * @param name The name of the resource.
     * @param unit The unit the resource will have (e.g. liter).
     * @param prefix The prefix of the resource (e.g. k as in kilo).
     */
    public void addResource(String name, String unit, String prefix, ExtendedColor color)
    {
	c_eventHandler.addResource(name, unit, prefix, color);
    }

    /**
     * Adds a timesteplevel to the model
     * @param label The label of the new timesteplevel
     * @param steps Number of steps that this timestep divides the previous one
     */
    public boolean addTimesteplevel(String label, int steps, Vector names, Vector lengths)
    {
	return c_eventHandler.addTimesteplevel(label, steps, names, lengths);
    }

    /**
     * Changes a timesteplevel
     * @param level The timesteplevel index
     * @param label The label of the new timesteplevel
     * @param steps Number of steps that this timestep divides the previous one
     */
    public boolean changeTimesteplevel(int level, String label, int steps, Vector names, Vector lengths)
    {
	return c_eventHandler.changeTimesteplevel(level, label, steps, names, lengths);
    }

    /**
     * Closes the database.
     */
    public void close()
	throws ModelException
    {
	c_eventHandler.close();
    }

    /**
     * Creates a new model.
     */
    public void createNewModel()
    {
	c_isChanged = false;
	c_eventHandler.createNewModel();
    }

    /**
     * Creates a new model.
     * @param needsDefaultObjectFunction Indikates if the new model need a default
     * function.
     */
    public void createNewModel(boolean needsDefaultObjectFunction)
    {
	c_isChanged = false;
	c_eventHandler.createNewModel(needsDefaultObjectFunction);
    }


    /**
     * Deletes a node or flow from the model.
     * @param id The node/flow that should be removed from the model.
     * @param deleted A vector of IDs indicating which components that were deleted successfully
     * @param problems A vector of Strings explaining which components that were not deleted successfully an the reason to this
     * has elemenents, nothing was deleted.
     */
    public void deleteComponent(ID id, Vector deleted, Vector problems)
    {
	c_eventHandler.deleteComponent(id, deleted, problems);
    }

    /**
     * Returns the object function if it exists. Added 2004-01-07
     */
    public ObjectFunction getObjectFunction(){
	return c_eventHandler.getObjectFunction();
    }
    /**
     * Return a Vector of labels to the source-dialog object
     * @return Vector of labels
     */
    public Vector getObjectFunctionLabels(){
	return c_eventHandler.getObjectFunctionLabels();
    }

    /**
     * Gets all functions labels that belongs to the specified node.
     * @param nodeID The ID of the node to get the function labels from.
     * @return A Vector with function labels.
     */
    public Vector getAllFunctionLabels(ID nodeID)
    {
	return c_eventHandler.getAllFunctionLabels(nodeID);
    }

    /**
     * Asks the event handler server what functions are available.
     * @return A Vector with the available functions.
     */
    public Vector getAvailableFunctions()
    {
	return c_eventHandler.getAvailableFunctions();
    }

    /**
     * Asks the event handler server what nodes are available.
     * @return A tree with the available nodes.
     */
    public DefaultMutableTreeNode getAvailableNodes()
    {
	return c_eventHandler.getAvailableNodes();
    }

    /**
     * Gets the all the available resources.
     * @return The resources that are available.
     *
     public Vector getAvailableResources()
     {
     return c_eventHandler.getAvailableResources();
     }*/

    /**
     * Gets a certain function from a node.
     * @param nodeID The ID of the node the function belongs to.
     * @param functionLabel The label of the function we want.
     * @return The function.
     */
    public NodeFunction getFunction(ID nodeID, String functionLabel)
    {
	return c_eventHandler.getFunction(nodeID, functionLabel);
    }

    /**
     * Gets the function type of the specified function.
     * @param nodeID The ID of the node the function belongs to.
     * @param functionLabel The label of the function we want the type of.
     * @return The function type.
     */
    public String getFunctionType(ID nodeID, String functionLabel)
    {
	return c_eventHandler.getFunctionType(nodeID, functionLabel);
    }

    /**
     * Gets all flows that exists in the model
     * @return An array with all flows
     */
    public Flow[] getAllFlows()
    {
      return c_eventHandler.getAllFlows();
    }

    /**
     * Gets all in flows of the node with node ID nodeID
     * @param nodeID the ID of the node
     * @return An Array with Flows
     */
    public Flow[] getInFlows(ID nodeID)
    {
	return c_eventHandler.getInFlows(nodeID);
    }

    /**
     * Gets all out flows of the node with node ID nodeID
     * @param nodeID the ID of the node
     * @return An Array with Flows
     */
    public Flow[] getOutFlows(ID nodeID)
    {
	return c_eventHandler.getOutFlows(nodeID);
    }

    /**
     * Gets the label of a certain component.
     * @param componentID The ID of the component.
     * @return The label of this component.
     */
    public String getLabel(ID componentID)
    {
	return c_eventHandler.getLabel(componentID);
    }

    /**
     * Gets the notes of a node.
     * @param componentID The ID of the component.
     * @return The label of this component.
     */
    public String getNote(ID componentID)
    {
	return c_eventHandler.getNote(componentID);
    }

    /**
     * Gets the last (finest) timesteplevel in the model
     * @return A Timesteplevel object
     */
    public Timesteplevel getLastTimesteplevel()
    {
 	return c_eventHandler.getLastTimesteplevel();
    }

    /**
     * Gets the current model
     * @return The current model
     */
    public Model getModel()
    {
	return c_eventHandler.getModel();
    }

    /**
     * Gets a node from the model.
     * @param nodeID ID of the node to get.
     * @return The node with the ID node�D.
     */
    public Object getNode(ID nodeID)
    {
	return c_eventHandler.getNode(nodeID);
    }

    /**
     * Gets a resource from somewhere down below
     * @param resource The ID of the flow/Resource to get the resource from.
     * @return A resource object, null if not found.
     */
    public Resource getResource(ID resource)
    {
	return c_eventHandler.getResource( resource );
    }

    /**
     * Gets all resources.
     * @return All resources available.
     */
    public Vector getResources()
    {
	return c_eventHandler.getResources();
    }
    
    /**
      *Added by Nawzad Mardan 2007-06-07
      * Returns the DiscountedsystemcostControl
    */
    public DiscountedsystemcostControl getDiscountedsystemcostControl()
     {
        return c_eventHandler.getDiscountedsystemcostControl();
     }

    /**Get all Nodes from the modell
         *@param void
         *@return all nodes
         *Added by Nawzad Mardan 070801
         *used by Discountedsystemcost function
      */
         
     public NodeControl getAllNodes()
     {
             return c_eventHandler.getAllNodes();
     }
     
      /**
      *Added by Nawzad Mardan 2007-06-07
      * Sets the rate, period analyses, tabelHeader and the table data in the DiscountedsystemcostControl
    */
    
    public void setDiscountedsystemcostControl(Float rate, Long year, String head [], Object [][] data, Vector timestepValues)
    {
        c_eventHandler.setDiscountedsystemcostControl(rate, year , head, data, timestepValues);
    } 
    
   /**
     * Gets the number of timesteplevels in the model
     * @return A Integer
     */
    public int getTimesteplevels()
    {
	//This function is not used right now, but maybe it will be...
 	return c_eventHandler.getTimesteplevels();
    }

    /**
     * Gets the timesteplevel with the number level
     * @param level the level number
     * @return the Timesteplevel
     */
    public Timesteplevel getTimesteplevel(int level)
    {
	return c_eventHandler.getTimesteplevel(level);
    }

    /**
     * Gets the timesteplevel of the node with ID nodeID
     * @param nodeID the ID of the node
     * @return the Timesteplevel
     */
    public Timesteplevel getTimesteplevel(ID nodeID)
    {
	return c_eventHandler.getTimesteplevel(nodeID);
    }

    /**
     * Gets the name of the timestep levels.
     * @return A vector of strings, where each string is a timestep level.
     */
    public Vector getTimesteplevelsVector()
    {
	return c_eventHandler.getTimesteplevelsVector();
    }

    /**
     * Gets the top (max) timesteplevel from the model
     * @return The top Timesteplevel
     */
    public Timesteplevel getTopTimesteplevel()
    {
	return c_eventHandler.getTopTimesteplevel();
    }

    /*
    public String modelToXML(int indent)
    {
	return c_eventHandler.modelToXML(indent);
    }
    */

    /**
     * Returns the nodes.
     *
     * @return an Enumeration object representing the nodes
     */
    public Enumeration getNodes()
    {
	return c_eventHandler.getNodes();
    }

    /**
     * Creates a flow that gets added to the model.
     * @param from The ID of the flow's source node.
     * @param to The ID of the flow's destination node.
     * @return ID of the created flow.
     */
    public ID newFlow(ID from, ID to)
    {
	return c_eventHandler.newFlow(from, to);
    }

    /**
     * Creates a node that gets added to the model.
     * @param nodeType The type of node that should be created.
     * @return ID of the created node.
     */
    public ID newNode(String nodeType)
	throws ModelException, RmdParseException
    {
	return c_eventHandler.newNode(nodeType);
    }

    /**
     * Creates a node that gets added to the model.
     * @param node The node that should be copied
     * @return ID of the created node.
     */
    public ID newNode(Node node)
	throws ModelException, RmdParseException
    {
        return c_eventHandler.newNode(node);
    }

    /**
     * Removes a function from a node.
     * @param nodeID The ID of the node.
     * @param function The function to remove.
     */
    public void removeFunction(ID nodeID, String function)
	throws ModelException
    {
	c_eventHandler.removeFunction(nodeID, function);
    }

    /**
     * Remove a resource.
     * @param ID The resource to remove
     */
    public ID removeResource(ID resourceID)
	throws ModelException
    {
	return c_eventHandler.removeResource(resourceID);
    }

    /**
     * Remove a timestep level.
     *
     * @param tsl The timestep level to remove
     */
    public void removeTimesteplevel(Timesteplevel tsl)
    {
	c_eventHandler.removeTimesteplevel(tsl);
    }

    /**
     * Sets the label of a certain flow.
     * @param flowID The ID of the flow.
     * @return The label that should be set on this flow.
     */
    public void setFlowLabel(ID flowID, String label)
	throws ModelException
    {
	c_eventHandler.setFlowLabel(flowID, label);
    }

    /**
     * Sets the label of a function to something else.
     * @param nodeID The ID of the node the function belongs to.
     * @param functionLabel The label of the function to change the label of.
     * @param newFunctionLabel The new label the function will have.
     */
    public void setFunctionLabel(ID nodeID, String functionLabel,
				 String newFunctionLabel)
    {
	c_eventHandler.setFunctionLabel(nodeID, functionLabel,
					newFunctionLabel);
    }

    /**
     * Sets the label of a certain node.
     * @param nodeID The ID of the node.
     * @return The label that should be set on this node.
     */
    public void setNodeLabel(ID nodeID, String label)
	throws ModelException
    {
	c_eventHandler.setNodeLabel(nodeID, label);
    }
    /**
     * Sets the note of a certain node.
     * @param nodeID The ID of the node.
     * @return The label that should be set on this node.
     */
    public void setNodeNote(ID nodeID, String note)
	throws ModelException
    {
	c_eventHandler.setNodeNote(nodeID, note);
    }

    /**
     * Sets a resource for something
     * @param where Where to set the resource
     * @param what What resource to set
     */
    public void setResource(ID where, ID what) {
	c_eventHandler.setResource(where,what);
    }

    /**
     * Sets the timestep level of a certain node.
     * @param nodeID ID of the node to change timestep level of.
     * @param label The label of the timestep to change to.
     */
    public void setTimesteplevel(ID nodeID, String label)
    {
	c_eventHandler.setTimesteplevel(nodeID, label);
    }

    /* BEGIN reMIND2004 ADDITIONS */

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
     * @see mind.automate.Automate
     */
    public OptimizationResult
	optimize(String filename, String optimizer, String optimizerPath)
	throws FileInteractionException, OptimizationException,
	       FileNotFoundException, IOException
    {
	return c_eventHandler.optimize(filename, optimizer, optimizerPath);
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
     * @see mind.automate.Automate
     */
    public OptimizationResult
	optimizeWithSettings(String filename, String optimizer,
			     String optimizerPath,
			     String mpsFile, String optFile)
	throws FileInteractionException, OptimizationException,
	       FileNotFoundException, IOException
    {
	return c_eventHandler.optimizeWithSettings(filename, optimizer,
						   optimizerPath,
						   mpsFile, optFile);
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
     * @see mind.automate.Automate
     */
    public void output(String filename, OptimizationResult result)
	throws IOException, IllegalArgumentException
    {
	c_eventHandler.output(filename, result);
    }

    /**
     * Return stdout/stderr from the optimizer.
     *
     * @return Stdout/stderr from the optimizer
     * @see mind.automate.Automate
     */
    public String getOptimizerOutput()
    {
	return c_eventHandler.getOptimizerOutput();
    }

    /**
     * Return the commands that will be sent to the optimizer at optimization.
     *
     * @return The commands.
     * @throws IOException If commandfile wasn't found or there was some
     *                     problem with reading the command file.
     * @see #setOptimizationCommands
     * @see mind.automate.Automate
     */
    public String getOptimizationCommands(String optimizer)
	throws IOException
    {
	return c_eventHandler.getOptimizationCommands(optimizer);
    }

    /**
     * Saves the optimization commands in a file.
     *
     * @param commands The commands to save in the file
     * @throws IOException Thrown when optimization command file can't
     *                     be written
     * @see #getOptimizationCommands
     * @see mind.automate.Automate
     */
    public void setOptimizationCommands(String optimizer, String commands)
	throws IOException
    {
	c_eventHandler.setOptimizationCommands(optimizer, commands);
    }

    /* END reMIND2004 ADDITIONS */
}
