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

import javax.swing.DefaultListModel;
import java.util.Enumeration;
import java.util.Vector;
import java.util.LinkedList;

import mind.io.*;

/**
 * A node is a central part of the MIND model. It contains a number of
 * functions that are transformed to mathematical functions when optimizing.
 *
 * @author Peter Åstrand
 * @author Peter Andersson
 * @author Johan Trygg
 * @version 2001-08-01z
 */
public class Node
{
    // All functions for this node.
    private FunctionControl c_functions;

    // This nodes unique ID.
    private ID c_ID;

    // This nodes label.
    private String c_label;

    //This nodes note
    private String c_note;

    // All current resources for this node.
    //    private DefaultListModel c_resources;

    // The timestep level for a node
    // (all functions in the same node have the same level)
    private Timesteplevel c_timesteplevel = null;
   
    //Added by Nawzad Mardan 070801 
    // A flag checking if Discountedsystemcost is running
    private boolean c_discountRun = false;
    
    //Added by Nawzad Mardan 070801 
    // An array of timesteps nummber initiate by Discount function when it runs  
    private Object [][] c_data = null;
    
    //Added by Nawzad Mardan 070801 
    //Annualy rate initiate by Discount function when it runs
    private Vector c_annualRate = null;
    
    //Added by Nawzad Mardan 070801 
    //Timesteps length for each timestep nummber in the data array, initiate by Discount function when it runs
    private Vector c_timeStepLength = null;
    /**
     * This class is used for generating equations to connect the in flows of
     * to a node with resource X with the out flows of the same resource.
     *
     * FlowResourceInfo, FlowResourcePost and frpa is quite stupid names.
     * If someone can find better names, please change them...
     */
    class FlowResourceInfo
    {
	class FlowResourcePost
	{
	    public Vector toFlows;  //The to flows for a resource (elements are Flow objects)
	    public Vector fromFlows; //The from flows for a resource (elements are Flow objects)

	    FlowResourcePost() {
		toFlows = new Vector(5);
		fromFlows = new Vector(5);
	    }
	}

	FlowResourcePost frpa[]; //An array with FlowResourcePosts, one post for every resource
	int numResources;  //Number of resources
	Vector resources;  //A vector with the resources as ID objects

	/**
	 * Create a new objcet of this class.
	 * Given a nodes all in and out flows it will initiate the resources and
	 * the frpa[] variables. It will collect all different resources from
	 * the flows and store them in the resources vector. Then it will store
	 * the in and out flows for each resource in the frpa array. It will
	 * also check if a flow's resoruce is not set (null) and in that case
	 * give an error message.
	 *
	 * @param toFlows A vector with all Flows going into the node.
	 * @param fromFlows A vector with Flows going out from the node.
	 */
	FlowResourceInfo(Vector toFlows, Vector fromFlows)
	    throws ModelException
	{

	    resources = new Vector(10);

	    int toFlowsSize = toFlows.size();
	    int fromFlowsSize = fromFlows.size();
	    int i;
	    ID resource;

	    //Get all resources in toFlows and fromFlows
	    for (i=0; i<toFlowsSize; ++i) {
		resource = ((Flow)toFlows.get(i)).getResource();
		if (resource == null)
		    throw new ModelException("Resource of flow " +
					     (Flow)toFlows.get(i) +
					     " not specified.\n\n" +
					     "Can not optimize.");
		if (!resources.contains(resource))
		    resources.add(resource);
	    }
	    for (i=0; i<fromFlowsSize; ++i) {
		resource = ((Flow)fromFlows.get(i)).getResource();
		if (resource == null)
		    throw new ModelException("Resource of flow " +
					     (Flow)fromFlows.get(i) +
					     " not specified.\n\n" +
					     "Can not optimize.");

		if (!resources.contains(resource))
		    resources.add(resource);
	    }

	    numResources = resources.size();
	    frpa = new FlowResourcePost[numResources];

	    //initiate flows for each resource
	    Flow flow;
	    for(i=0; i<numResources; ++i) {
		frpa[i] = new FlowResourcePost();
		for(int j=0; j<toFlowsSize; ++j) {
		    flow = (Flow)toFlows.get(j);
		    if (flow.getResource().equals((ID)resources.get(i)))
			frpa[i].toFlows.add(flow);
		}
		for(int j=0; j<fromFlowsSize; ++j) {
		    flow = (Flow)fromFlows.get(j);
		    if (flow.getResource().equals((ID)resources.get(i)))
			frpa[i].fromFlows.add(flow);
		}
	    }

	}

	/**
	 * This function will check the consumer and producer variable of
	 * the EquationControl. If they are false nothing will happen.
	 * If the producer is set (to a resource) this resource will be
	 * set to null in the resource vector. This information is used by
	 * this class' getEquationControl.
	 *
	 * @param ec An equation control from a function.
	 */
	public void update(EquationControl ec) {

	    ID consumer = ec.getConsumer();
	    ID producer = ec.getProducer();
	    int i;

	    if (consumer != null) {
		//find the consumer resource in resources vector
		i = resources.indexOf(consumer);
		if (i >= 0) {
		    resources.set(i,null); //remove the resource
		}
		//else the resource is already removed
	    }

	    if (producer != null) {
		i = resources.indexOf(producer);
		if (i >= 0) {
		    resources.set(i,null); //remove the resource
		}
	    }
	}

	//used by SuperFunction when validating in flows and out flows
	public void update2(EquationControl ec) {

	    Vector consumers = ec.getConsumers();
	    Vector producers = ec.getProducers();
	    int i;

		for (int j = 0;j<consumers.size();j++) {
			ID res = (ID)consumers.elementAt(j);
			if (res != null) {
				//find the consumer resource in resources vector
				i = resources.indexOf(res);
				if (i >= 0) {
		    		resources.set(i,null); //remove the resource
				}
	    	}
		}

		for (int j = 0;j<producers.size();j++) {
			ID res = (ID)producers.elementAt(j);
			if (res != null) {
				//find the consumer resource in resources vector
				i = resources.indexOf(res);
				if (i >= 0) {
		    		resources.set(i,null); //remove the resource
				}
	    	}
		}
	}

	/**
	 * This is the heart of this inner class.
	 * The function will go through the resources vector (all resources
	 * in this node). If a resource is not null an equation will be
	 * created that specifies that the in flows should be equal to the
	 * out flows of this resource (Fin - Fout = 0).
	 * If an element in the resource vector is null it means that the
	 * .update() function has set it to null, which means that the node
	 * has a function that produces or/and consumes this resource.
	 * (a destination function is a consumer, a source is a producer and
	 *  a flow dependency can be a consumer and a producer)
	 * If this is the case, the in flows should NOT be equal to the out
	 * flows for this resource, so no equation will be generated.
	 *
	 * I think it would be possible to improve this a bit. Now, if a node
	 * N1 has a consumer function for resource X, and there is a in
	 * flow (F1) and a out flow (F2) of resource X, the out flow will NOT
	 * be connected to the in flow. It would be nice if they would be
	 * connected, and if an equation similar to this would be generated:
	 * F1 - F2 >= 0
	 * Question is how much use one would have of this... maybe it will
	 * only generate equation systems more difficult to solve...
	 *
	 * @param maxTimesteps number of timesteps for the node.
	 * @return An Equation control with the equations that connects
	 * the in and out flows.
	 */
	public EquationControl getEquationControl(int maxTimesteps)
	    throws ModelException
	{

	    EquationControl ec = new EquationControl();
	    Equation eq;
	    Variable var;
	    //Create equation for each resource
	    for(int res=0; res<numResources; ++res){

		ID resource = (ID)resources.get(res);
		if (resource == null)
		    //do not generate equation for this resource
		    continue;

		//One eqution for each timstep
		for(int ts=1; ts<=maxTimesteps; ++ts) {
		    eq = new Equation(getID(),
				      getID().toString() + resource.toString(),
				      ts, Equation.EQUAL);
		    Vector fromFlows = frpa[res].fromFlows;
		    Vector toFlows = frpa[res].toFlows;

		    if (fromFlows.size() == 0 && toFlows.size() > 0)
			throw new ModelException("The flow(s) " +
						 frpa[res].toFlows +
						 " going in to node " +
						 getID() +
						 " is using a resource\n that no out going flow" +
						 " is using. Can not Optimize.");
		    if (fromFlows.size() > 0 && toFlows.size() == 0)
			throw new ModelException("The flow(s) " +
						 frpa[res].fromFlows +
						 " going out from node " +
						 getID() +
						 " is using a resource\n that no in going flow" +
						 " is using. Can not Optimize.");

		    for(int flow=0; flow<fromFlows.size(); ++flow) {
			var = new Variable(((Flow)fromFlows.get(flow)).getID(),
					   ts,(float)1.0);
			eq.addVariable(var);
		    }
		    for(int flow=0; flow<toFlows.size(); ++flow) {
			var = new Variable(((Flow)toFlows.get(flow)).getID(),
					   ts,(float)-1.0);
			eq.addVariable(var);
		    }
		    ec.add(eq);
		}
	    }
	    return ec;
	}

	/*
        //Nice to have when hunting bugs...
	public void print()
	{
	    System.out.println("*** frpa node " + getID() + "***");
	    for(int i = 0; i<numResources; ++i) {
	        System.out.println("resource=" + (ID)resources.get(i));
	 	System.out.println("toFlows=" + frpa[i].toFlows);
	 	System.out.println("fromFlows=" + frpa[i].fromFlows);
	     }
	}
	*/

    }  // End of inner class FlowResourceInfo


    /**
     * Constructor using Functioncollection
     */
    public Node()
    {
	c_functions = new FunctionControl();
	c_ID = new ID(ID.NODE);
	c_label = ""; //Is set in database
	//	c_resources = new DefaultListModel();
    }

    /**
     * Constructor for createing a node with a speceific ID
     * and a Functioncollection
     */
    public Node(ID nodeID)
    {
	c_functions = new FunctionControl();
	c_ID = nodeID;
	c_label = "";
	c_note = "";
    }

    public Node(String label, Timesteplevel tsl)
     {
	 c_functions = new FunctionControl();
	 c_ID = new ID(ID.NODE);
	 c_label = label;
	 c_note = "";//who calls this constructor?
 	 c_timesteplevel = tsl;
     }


    /**
     * A copyconstructor.
     *
     * @param old_node The node to be copied.
     */
    public Node(Node old_node)
    {
	c_functions = new FunctionControl(old_node.c_functions);
	c_ID = new ID(ID.NODE);
	c_label = new String(old_node.getLabel());
	c_note = new String(old_node.getNote());
	c_timesteplevel = old_node.getTimesteplevel();
	// Deep copy of resources
	/* should resource exist for node?
	c_resources = new DefaultListModel();
	DefaultListModel old_resources = old_node.getAllResources();

	for (Enumeration e = old_resources.elements(); e.hasMoreElements();) {
	    // Get the resource
	    Resource r = (Resource)e.nextElement();
	    // Add the resource to this new node
	    addResource(r);
	}
	*/
    }

    /**
     * Adds a function to this node.
     *
     * @param function The function to add.
     */
    public void addFunction(String functionType)
    {
	c_functions.add(functionType, getTimesteplevel());
    }

    /**
     * Adds a function to this node
     * @param function the function to add
     */
    public void addFunction(NodeFunction function)
    {
	c_functions.add(function);
    }

    /**
     * Adds a resource to this node.
     */
    /* should  resource exist for node?
    public void addResource(Resource newResource)
    {
	c_resources.addElement(newResource);
    }
    */

    /**
     * Get all function labels
     *
     */
    public Vector getAllFunctionLabels()
    {
	Vector v = new Vector();
	for (Enumeration e = c_functions.elements(); e.hasMoreElements();) {
	    NodeFunction nf = (NodeFunction)e.nextElement();
	    v.add(nf.getLabel());
	}
	return v;
    }

    /**
     * Gets all functions from this node.
     *
     */
    public FunctionControl getAllFunctions()
    {
	return c_functions;
    }
    
    /*
     * Added pum2007
     * Sets a new functionController
     */
    public void setFunctionController(FunctionControl fControl){
    	c_functions=fControl;
    }

    /**
     * Gets all resources from this node.
     *
     */
    /* should resource exist for node?
    public DefaultListModel getAllResources()
    {
	return c_resources;
    }
    */

    /**
     * Gets the function with a certain ID from this node.
     *
     * @param functionID
     */
    public NodeFunction getFunction(String functionLabel)
    {
	return c_functions.get(functionLabel);
    }

    /**
     * Returns the nodes unique ID.
     *
     */
    public ID getID()
    {
	return c_ID;
    }

    /**
     * Returns the nodes label.
     *
     */
    public String getLabel()
    {
	return c_label;
    }

    /**
     * Returns the nodes note.
     *
     */
    public String getNote()
    {
	return c_note;
    }

    /**
     * Returns the nodes total optimization information.
     * @param maxTimesteps How many equations to generate for each function
     * @return A complete Equationcontrol for this node
     * @throws ModelException when bad things happen
     */
    public EquationControl getEquationControl(Vector toFlows, Vector fromFlows,
					      int maxTimesteps)
	throws ModelException
    {
	EquationControl allEquations = new EquationControl();

	FlowResourceInfo flowConnector = new FlowResourceInfo(toFlows, fromFlows);
	//flowConnector.print();

	// Iterate over all functions
	EquationControl nodeEquations;
	for (Enumeration e = c_functions.elements(); e.hasMoreElements();) {
	    // Get the nodefunction and its EquationControl
	    NodeFunction nf = (NodeFunction) e.nextElement();
            
            //String ft = nf.getFunctionType();

	    //added support for validation of in flows and out flows
	    //with the FunctionEditor
	    if (nf.getFunctionType().equals("FunctionEditor")) {
	    	nodeEquations = nf.getEquationControl(maxTimesteps,
						  c_ID, toFlows, fromFlows);
	    	flowConnector.update2(nodeEquations);
	    }
	    else {
                // In case the user running Discountsystemcost function we have different
                // timesteps and different coefficient
                // Added by Nawzad Mardan 070801
                //if(((nf.getFunctionType().equals("Source")) || (nf.getFunctionType().equals("InvestmentCost"))) && (c_discountRun == true))
                if((nf.getFunctionType().equals("Source")) && (c_discountRun == true))
                    nodeEquations = nf.getEquationControl2(c_data,c_annualRate,c_timeStepLength ,c_ID, toFlows, fromFlows);
                else
	    	nodeEquations = nf.getEquationControl(maxTimesteps,c_ID, toFlows, fromFlows);
                
	    	flowConnector.update(nodeEquations);
	    }

	    // Merge with result
	    allEquations.mergeWith(nodeEquations);
	}
	//Add the equations that connects the in flows with the out flows.
	nodeEquations = flowConnector.getEquationControl(maxTimesteps);
	allEquations.mergeWith(nodeEquations);

	return allEquations;
    }

    /**
     * Gets the timestep level of a certain node.
     */
    public Timesteplevel getTimesteplevel()
    {
	return c_timesteplevel;
    }

    /**
     * Returns the node type.
     *
     *
    public String getType()
    {
	return c_type;
    }
    */

    /**
     * Adds a function of type functionType to the node and
     * initiats the the function to the values found in data.
     * The function is used when loading a model or node from disk.
     *
     * @param functionType type of function
     * @param data A linked list with data.
     * @param rc A control with all available resources.
     * @param createMissingResources If this is true then
     * if data contains resources not found in rc, these resources
     * should be created and added to rc.
     */
    public void parseAndAddFunction(String functionType, LinkedList data,
				    ResourceControl rc,
				    boolean createMissingResource)
	throws RmdParseException
    {
	c_functions.parseAndAdd(functionType, data, rc, createMissingResource);
    }

    /**
     * Removes the function with ID functionID from this node.
     *
     * @param functionID The function to remove.
     */
    public void removeFunction(String functionLabel)
    {
	c_functions.remove(functionLabel);
    }

    /**
     * Removes the resources with the resource removeResource from this node.
     */
    /* Should this exist?
    public void removeResource(Resource removeResource)
    {
	c_resources.removeElement(removeResource);
    }
    */

    /**
     * Sets a new ID for this node.
     *
     * @param functionID The new ID for this node.
     */
    public void setID(ID functionID)
    {
	c_ID = functionID;
    }

    /**
     * Sets the label of a function to something else.
     * @param functionLabel The label of the function to change the label of.
     * @param newFunctionLabel The new label the function will have.
     */
    public void setFunctionLabel(String functionLabel, String newFunctionLabel)
    {
	NodeFunction nf = getFunction(functionLabel);
	nf.setLabel(newFunctionLabel);
    }

    /**
     * Sets a new label for this new.
     *
     * @param label The new label.
     */
    public void setLabel(String label)
    {
	c_label = label;
    }

    /**
     * Sets a new note for this new node.
     *
     * @param note The new label.
     */
    public void setNote(String note)
    {
	c_note = note;
    }
 
    /**
     * Sets discouruning flag to true .
     *Added by Nawzad Mardan 070801
     * @param run A boolean.
     */
    
    public void setDiscountRun(boolean run)
    {
        c_discountRun = run;
    }
    
    public void setDiscountSystemParameter(DiscountedsystemcostControl dc)
    {
        c_data = dc.getTableData();
        c_discountRun = true;
        c_annualRate = dc.getAnnualRate();
        c_timeStepLength = dc.getTimestepValues();
        
        
    }
    
     /**
     * Returns the flag discount .
     *Added by Nawzad Mardan 070801
     */
    public boolean getDiscountRun()
    {
        return c_discountRun;
    }

     /** Sets a two dimension array of timestep nummber 
      * This array is initiate by Discount function when it runs
     *Added by Nawzad Mardan 070801
     * @param data The timestep array.
     */
    public void setTimeStepArray(Object [][] data)
    {
        c_data = data;
    }
    
     /** Sets AnnualRate vector 
      * This vector is initiate by Discount function when it runs
     *Added by Nawzad Mardan 070801
     * @param rate The annualy rate.
     */
    public void setAnnualRate(Vector rate)
    {
        c_annualRate = rate;
    }
    
    
     /** Sets timesteps length vector
      * This vector is initiate by Discount function when it runs
     *Added by Nawzad Mardan 070801
     * @param rate The timesteps length for each timestep nummber in the data array.
     */
    public void setTimeStepLength(Vector timestepValue)
    {
        c_timeStepLength = timestepValue;
    }
    
    /**
     * Sets a the timesteplevel on this node and on all
     * functions in the node.
     *
     * @param level The timesteplevel.
     */
    public void setTimesteplevel(Timesteplevel level)
    {
	c_timesteplevel = level;
	c_functions.setTimesteplevel(level);  // JONAS
    }

    /**
     * Sets the type for this node.
     *
    public void setType(String type)
    {
	c_type = type;
    }
    */

    /**
     * Get all function IDs
     *
     */
    public Vector getAllFunctionIDs()
    {
	Vector v = new Vector();
	for (Enumeration e = c_functions.elements(); e.hasMoreElements();) {
	    NodeFunction nf = (NodeFunction)e.nextElement();
	    v.add(nf.getID());
	}
	return v;
    }

    public String toXML(ResourceControl resources, int indent)
    {
	String xml = XML.indent(indent) + "<node id=\"" +
	    getID() + "\">" + XML.nl();

	xml = xml + XML.indent(indent+1) + "<label>" + XML.toXML(getLabel()) +
	    "</label>" + XML.nl() +
	    //XML.indent(indent+1) + "<note>" + XML.toXML(getLabel()) + "</note>" + XML.nl() +
	    XML.indent(indent+1) + "<note>" + XML.toXML( getNote() ) + "</note>" + XML.nl() +
	    XML.indent(indent+1) + "<tLevel>" +
	    XML.toXML(getTimesteplevel().getLabel()) + "</tLevel>" + XML.nl();

	if (c_functions != null)
	    xml += c_functions.toXML(resources, indent+1);

//	else
//	    System.out.println("functions empty");

	xml = xml + XML.indent(indent) + "</node>" + XML.nl();

	return xml;
    }
    
    /**
     * PUM5 ADDED
     * 
     * */
    public String toEXML(String nodeName, ResourceControl resources, int indent)
    {

    	ExmlSheet sheet = new ExmlSheet(nodeName, indent);
    	//sheet.addRow(sheet.addCell("String", "Function type")+sheet.addCell("String","boundary"), indent);

    	if (c_functions != null)
    		c_functions.toEXML(resources, sheet);

    	//xml = xml + XML.indent(indent) + "</Table>" + XML.nl();


    	return sheet.getXml();
    }
    
}
