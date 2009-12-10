/*
 * Copyright 2002:
 * Urban Liljedahl
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import mind.gui.dnd.*;
import mind.io.*;
import mind.model.function.*;

/**
 * Uses XML to save nodes and its functions.
 * The node tree is created from the file hierarchy. That makes you
 * able to create the nodes by adding new directories and files.
 * The function list is
 *
 * @author Urban Liljedahl
 * @version 2002-03-11
 */

public class NodeInteraction
{
    private ResourceControl c_resourceControl;
    private Timesteplevel c_topTSL;
    //Added by Nawzad Mardan 070601
    private DiscountedsystemcostControl c_discountedsystemcostControl;

    private final String NODE_ROOT = "nodes";
    private final String BLANK_NODE = "Blank node";
    private File c_root = null;

    public NodeInteraction(ResourceControl control, Timesteplevel topTSL)
    {
	c_resourceControl = control;
	c_topTSL = topTSL;
	c_root = new File(/*getClass().getResource(".").getPath() +*/ NODE_ROOT);
	if (!c_root.exists())
	    c_root.mkdir();
    }

    /**
     * Adds a folder in the Nodetype tree, it does this by
     * adding a folder to the filesystem
     * @param newFolder The folder to add
     * @param parent The folder's parent
     * @return A new dragable node to use in folder tree
     */
    public void addFolder(String newFolder, NodeToDrag parent)
    {
	File file = new File(parent.getFile(), newFolder);
	file.mkdir();
    }

    /**
     * Adds a new nodetype to the tree
     * @param nodeToAdd The node to be added
     * @param parent The folder to add the node in
     * @return The NodeToDrag for the created Nodetype
     * @throws ModelException when nodeToAdds label already
     * exists as a nodetype or something else happens
     */
    public void addNode(Node nodeToAdd, NodeToDrag folder)
	throws ModelException
    {
	File file = new File(folder.getFile());
	if (!file.isDirectory())
	    throw new ModelException("NodeInteraction: parent's not " +
				     "a directory");

	try {
	    File newFile = new File(file, nodeToAdd.getLabel() + ".xml");
	    newFile.createNewFile();

	    String xml = XML.getHeader("node") + XML.nl() +
		nodeToAdd.toXML(c_resourceControl, 0);
	    FileWriter writer = new FileWriter(newFile);
	    writer.write(xml);
	    writer.close();
	}
	catch (IOException e) {
	    throw new ModelException("Node couldn't be added.\n" +
				     "Was unable to write to file.");
	}
    }

    /**
     * Gets the available functions.
     * This method needs to be changed when a function is added.
     */
    public static Vector getAvailableFunctions()
    {
	Vector functions = new Vector(10);
	functions.addElement("Boundary");
	functions.addElement("BoundaryTOP");
	functions.addElement("Destination");
	functions.addElement("Source");
	functions.addElement("Flow Dependency");
	functions.addElement("Flow Relation");
	functions.addElement("Flow Equation");
	/*Tillagt av Mattias Gylin*/
	functions.addElement("Investment Cost");
        /*Tillagt av Jonas S‰‰v*/
        functions.addElement("Storage Equation");
        functions.addElement("Batch Equation");
        functions.addElement("Logical Equation (global)");
        /*Tillagt av Marcus Bergendorff*/
        functions.addElement("Function Editor");
        // Added by Nawzad Mardan 2008-02-01
        //functions.addElement("StartStop Equation");
	return functions;
    }

    /**
     * Gets the available functions as XML tags (used in the RMD file)
     * This method needs to be changed when a function is added.
     */
    public static Vector getAvailableFunctionsXML()
    {
	Vector functions = new Vector(10);
	functions.addElement("boundary");
	functions.addElement("boundaryTOP");
	functions.addElement("destination");
	functions.addElement("source");
	functions.addElement("flowDependency");
	functions.addElement("flowRelation");
	functions.addElement("flowEquation");
	/*Tillagt av Mattias Gylin*/
	functions.addElement("investmentCost");
        /*Tillagt av Jonas S‰‰v*/
        functions.addElement("storageEquation");
        functions.addElement("batchEquation");
        functions.addElement("logicalEquation");
        /*Tillagt av Marcus Bergendorff*/
        functions.addElement("functionEditor");
        // Added by Nawzad Mardan 2008-02-01
        //functions.addElement("startStopEquation");
	return functions;
    }

    /**
     * Maps the function names used in the RMD file to the names
     * uesed in other parts of reMIND (eg getFunction)
     * This method needs to be changed when a function is added.
     */
    public static String XMLFunction2Function(String xmlName)
    {
	if (xmlName.equals("boundary"))
	    return new String("Boundary");
	if (xmlName.equals("boundaryTOP"))
	    return new String("BoundaryTOP");
	if (xmlName.equals("destination"))
	    return new String("Destination");
	if (xmlName.equals("source"))
	    return new String("Source");
	if (xmlName.equals("flowDependency"))
	    return new String("Flow Dependency");
	if (xmlName.equals("flowRelation"))
	    return new String("Flow Relation");
	if (xmlName.equals("flowEquation"))
	    return new String("Flow Equation");
	/*Tillagt av Mattias Gylin*/
	if (xmlName.equals("investmentCost"))
	    return new String("Investment Cost");
          /*Tillagt av Jonas S‰‰v*/
        if (xmlName.equals("storageEquation"))
            return new String("Storage Equation");
        if (xmlName.equals("batchEquation"))
          return new String("Batch Equation");
        if (xmlName.equals("logicalEquation"))
          return new String("Logical Equation (global)");
        /*Tillagt av Marcus Bergendorff*/
        if (xmlName.equals("functionEditor"))
    	    return new String("Function Editor");
        // Added by Nawzad Mardan 2008-02-01
        //if (xmlName.equals("startStopEquation"))
    	  //  return new String("StartStop Equation");

	throw new IllegalArgumentException("Function '" + xmlName +
					   "' does not exist.");
    }


    /**
     * Gets a functions defaultvalues
     * This method needs to be changed when a function is added.
     * @param function The function to get the defaultvalues for.
     * @return A Nodefunction with all values set.
     * @throws IllegalArgumentException when function is not a
     * valid functiontype
     */
    public NodeFunction getFunction(String function)
    {
	if (function.equals("Boundary"))
	    return new Boundary();
	if (function.equals("BoundaryTOP"))
	    return new BoundaryTOP();
	if (function.equals("Destination"))
	    return new Destination();
	if (function.equals("Source"))
	    return new Source();
	if (function.equals("Flow Dependency"))
	    return new FlowDependency();
	if (function.equals("Flow Relation"))
	    return new FlowRelation();
	if (function.equals("Flow Equation"))
	    return new FlowEquation();
	/*Tillagt av Mattias Gylin*/
	if (function.equals("Investment Cost"))
	    return new InvestmentCost();
        /*Tillagt av Jonas S‰‰v*/
        if (function.equals("Storage Equation"))
          return new StorageEquation();
        if (function.equals("Batch Equation"))
          return new BatchEquation();
        if (function.equals("Logical Equation (global)"))
          return new BinaryFunction();
        /*Tillagt av Marcus Bergendorff*/
        if (function.equals("Function Editor"))
    	    return new FunctionEditor();
        // Added by Nawzad Mardan 2008-02-01
     //   if (function.equals("StartStop Equation"))
       // {
    	 //   return new StartStopEquation();
            //System.out.println("Start stop equation");
        //}

	throw new IllegalArgumentException("Function '" + function +
					   "' does not exist.");
    }

    /**
     * Creates a new node of a nodeType
     * @param nodeType The type of node to be created
     * @return A new node of the nodeType
     */
    public Node getNode(String nodeType)
	throws ModelException, RmdParseException
    {
	if (!c_root.exists())
	    c_root.mkdir();

	if (nodeType.equals(BLANK_NODE)) {
	    Node node = new Node(BLANK_NODE, c_topTSL);
	    return node;
	}

	File file = new File(nodeType);
	if (!file.exists())
	    throw new ModelException("Nodetype '" + nodeType + "' does not " +
				     "exist. " + nodeType + ".xml " +
				     "was probably deleted. Add it again " +
				     "or you are not able to use this " +
				     "node type.");

        //System.out.println("Loading the node " + file.getPath());

	Node newNode = new Node();

        try {
	    // Use the RmdSAXHandler as the SAX event handler
	    DefaultHandler handler = new RmdSAXHandler(newNode,c_resourceControl,c_topTSL);

	    // Use the default parser
	    SAXParserFactory factory = SAXParserFactory.newInstance();
	    factory.setValidating(true);
	    factory.setNamespaceAware(false);

             // Parse the input
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(file, handler);
        }
	catch (SAXException e) {
	    throw new RmdParseException(e.getMessage());
	}
	catch (Exception e) {  //Catch execptions from saxParser
	    //e.printStackTrace(System.out);
	    throw new ModelException(e.toString());
	}

	return newNode;
    }

    /**
     * Creates a Tree of all available NodeTypes as strings and returns it
     * @return A complete tree of all NodeTypes in folders
     */
    public DefaultMutableTreeNode getNodeTree()
    {
	if (!c_root.exists())
	    c_root.mkdir();

	DefaultMutableTreeNode node = getTree(c_root);
	NodeToDrag drag = new NodeToDrag(BLANK_NODE, BLANK_NODE);
	node.add(new DefaultMutableTreeNode(drag, false));
	return node;
    }

    public DefaultMutableTreeNode getTree(File file)
    {
	DefaultMutableTreeNode node = null;
	File[] children = null;
	NodeToDrag drag;

	if (file == null)
	    return null;
	if (file.isFile()) {
	    String filename = file.getName();
	    if (filename.endsWith(".xml"))
		filename = filename.substring(0, filename.length()-4);
	    drag = new NodeToDrag(filename, file.getPath());
	    return new DefaultMutableTreeNode(drag, false);
	}
	else if (file.isDirectory()) {
	    drag = new NodeToDrag(file.getName(), file.getPath());
	    node = new DefaultMutableTreeNode(drag, true);
	    children = file.listFiles();
	    for (int i = 0; i < children.length; i++)
		node.add(getTree(children[i]));

	    return node;
	}
	else
	    return null;
    }

    /**
     * Removes a folder from the nodetype tree, it does this by
     * removing a file in the filesystem
     * @param folder The folder to remove
     */
    public void removeFolder(NodeToDrag folder)
    {
	File file = new File(folder.getFile());
	file.delete();
    }

    /**
     * Removes a node from the Database
     * @param nodeToRemove The nodetype to remove
     */
    public void removeNode(NodeToDrag nodeToRemove)
    {
	File file = new File(nodeToRemove.getFile());
	file.delete();
    }

    /**
     * Renames a folder in the nodeType Tree
     * @param folder The folder to be renamed
     * @param newName The folder's new name
     */
    public void renameFolder(NodeToDrag folder, String newName)
    {
	File file = new File(folder.getFile());
	if (file.isDirectory())
	    file.renameTo(new File(file.getParentFile(), newName));
    }
    
    /*
     * Added pum2007
     */
    
    public ResourceControl getResourceControl(){
    	return c_resourceControl;
    }
}
