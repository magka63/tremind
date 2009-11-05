/*
 * Copyright 2002:
 * Urban Liljedahl <lilje@sm.luth.se>
 *
 * Copyright 2007:
 * Per Fredriksson <perfr775@student.liu.se>
 * David Karlslätt <davka417@student.liu.se>
 * Tor Knutsson	<torkn754@student.liu.se>
 * Daniel Källming <danka053@student.liu.se>
 * Ted Palmgren <tedpa175@student.liu.se>
 * Freddie Pintar <frepi150@student.liu.se>
 * Mårten Thurén <marth852@student.liu.se> 
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
package mind.model.function;

import java.util.*;

import mind.model.*;
import mind.io.*;
import mind.model.ID;
/**
 * The function class FlowEquation
 *
 * @author Urban Liljedahl
 * @author Freddie Pintar
 * @author Tor Knutsson
 * @version 2007-12-12
 */

public class FlowEquation
    extends NodeFunction
    implements Cloneable
{
    private Vector c_timesteps = null;
    //private TimestepInfo c_info;//used instead of c_timesteps until the timesteps are activated

    /**
     * Inner class representing the lhs flows with coefficients
     */
    class TimestepInfo{
	private Vector c_inFlow = new Vector(0);
	private Vector c_coeffIn = new Vector(0);
	private Vector c_isIntegerIn = new Vector(0);
	private Vector c_outFlow = new Vector(0);
	private Vector c_coeffOut = new Vector(0);
	private Vector c_isIntegerOut = new Vector(0);
	private float c_equationvalue = (float)0;
        private String c_rhsConstraint = "E"; // Can be  "G" and "L" and "E"

	TimestepInfo(){
	}

	Vector getInFlow(){
	    return c_inFlow;
	}
	Vector getCoeffIn(){
	    return c_coeffIn;
	}
	Vector getIsIntegerIn(){
	    return c_isIntegerIn;
	}
	Vector getOutFlow(){
	    return c_outFlow;
	}
	Vector getCoeffOut(){
	    return c_coeffOut;
	}
	Vector getIsIntegerOut(){
	    return c_isIntegerOut;
	}
	float getEquationValue(){
	    return c_equationvalue;
	}
        String getRhsConstraint() {
          return c_rhsConstraint;
        }
	void setInFlow(Vector f){
	    c_inFlow = f;
	}
	void setCoeffIn(Vector c){
	    c_coeffIn = c;
	}
	void setIsIntegerIn(Vector i){
	    c_isIntegerIn = i;
	}
	void setOutFlow(Vector f){
	    c_outFlow = f;
	}
	void setCoeffOut(Vector c){
	    c_coeffOut = c;
	}
	void setIsIntegerOut(Vector i){
	    c_isIntegerOut = i;
	}
	void setEquationValue(float r){
	    c_equationvalue = r;
	}
        void setRhsConstraint(String con) {
          if (!con.equals("E") && !con.equals("G") && !con.equals("L")) {
            javax.swing.JOptionPane.showMessageDialog(null,
                "Internal Error: setRhsConstraint", "Internal Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
          }
          c_rhsConstraint = con;
        }
	/*
	 * Create the timestep information into xml
	 */
	public String toXML(int indent){
	    String xml = "";

	    for( int index = 0; index < c_inFlow.size(); index++ ){
		xml = xml + XML.indent( indent ) + "<flowid_in>" +
		    c_inFlow.elementAt(index).toString() + "</flowid_in>" + XML.nl();
	    }
	    for( int index = 0; index < c_coeffIn.size(); index++ ){
		xml = xml + XML.indent( indent ) + "<coeff_in>" +
		    ((Float)(c_coeffIn.elementAt(index))).floatValue() + "</coeff_in>" + XML.nl();
	    }
	    for( int index = 0; index < c_outFlow.size(); index++ ){
		xml = xml + XML.indent( indent ) + "<flowid_out>" +
		    c_outFlow.elementAt(index).toString() + "</flowid_out>" + XML.nl();
	    }
	    for( int index = 0; index < c_coeffOut.size(); index++ ){
		xml = xml + XML.indent( indent ) + "<coeff_out>" +
		    ((Float)(c_coeffOut.elementAt(index))).floatValue() + "</coeff_out>" + XML.nl();
	    }
	    //save equation value
	    xml = xml + XML.indent( indent ) + "<equationvalue>" +
		getEquationValue() + "</equationvalue>" + XML.nl();

            // save the constraint type
            xml = xml + XML.indent( indent ) + "<constrainttype>" +
                getRhsConstraint() + "</constrainttype>" + XML.nl();

	    return xml;
	}
	/**
	 * Puts EXML data in the given ExmlSheet
	 * PUM5 Added 2007-12-12
	 * @param sheet The ExmlSheet to be changed
	 * @param inFlow 
	 */
	public void toEXML(ExmlSheet sheet,boolean inFlow){
	    if (inFlow) {

	    
	    //save equation value
	    sheet.addTableValue("Number", ((Float)getEquationValue()).toString());
	     // save the constraint type
	    sheet.addTableValue("String", XML.toXML(getRhsConstraint()));
	    sheet.addTableValue("String","");
            
	    for( int index = 0; index < c_inFlow.size(); index++ ){
	    	sheet.addTableValue("String", XML.toXML(c_inFlow.elementAt(index).toString()));
			sheet.addTableValue("Number", ((Float)(c_coeffIn.elementAt(index))).toString());
	    }
	    }
	    else {
	    	sheet.addTableValue("String","");
		    for( int index = 0; index < c_outFlow.size(); index++ ){
		    	sheet.addTableValue("String", XML.toXML(c_outFlow.elementAt(index).toString()));
				sheet.addTableValue("Number", ((Float)(c_coeffOut.elementAt(index))).toString());
		    }
	    }

	}
	
	
	/*
	 * Public clone method that produces a new TimestepInfo with the same
	 * values as orginal vector. I feel that this method should be reimplemented
	 * maybe with a faster algoritm than this. Maybe the class TimestepInfo
	 * should be redesigned??? /Urban
	 */
	public Object clone(){
	    TimestepInfo copy = new TimestepInfo();
	    //in flow
	    Vector temp = new Vector();
	    for( int i = 0; i < this.getInFlow().size(); i++ ){
		temp.addElement( (this.getInFlow().elementAt(i)));
	    }
	    copy.setInFlow( temp );

	    temp = new Vector();
	    for( int i = 0; i < this.getCoeffIn().size(); i++ ){
		temp.addElement( new Float(((Float)(this.getCoeffIn().elementAt(i))).floatValue()));
	    }
	    copy.setCoeffIn( temp );

	    temp = new Vector();
	    for( int i = 0; i < this.getIsIntegerIn().size(); i++ ){
		temp.addElement( new Boolean(((Boolean)(this.getIsIntegerIn().elementAt(i))).booleanValue()));
	    }
	    copy.setIsIntegerIn( temp );

	    //out flow
	    temp = new Vector();
	    for( int i = 0; i < this.getOutFlow().size(); i++ ){
		temp.addElement( (this.getOutFlow().elementAt(i)));
	    }
	    copy.setOutFlow( temp );

	    temp = new Vector();
	    for( int i = 0; i < this.getCoeffOut().size(); i++ ){
		temp.addElement( new Float(((Float)(this.getCoeffOut().elementAt(i))).floatValue()));
	    }
	    copy.setCoeffOut( temp );

	    temp = new Vector();
	    for( int i = 0; i < this.getIsIntegerOut().size(); i++ ){
		temp.addElement( new Boolean(((Boolean)(this.getIsIntegerOut().elementAt(i))).booleanValue()));
	    }
	    copy.setIsIntegerOut( temp );

	    //rhs value

	    copy.setEquationValue( this.getEquationValue() );

            // constraint type
            copy.setRhsConstraint( this.getRhsConstraint() );

	    return copy;
	}

    }
    /**
     * Constructor.
     */
    public FlowEquation()
    {
	super(new ID(ID.FUNCTION), "FlowEquation", null);
	c_timesteps = new Vector();
	TimestepInfo info = new TimestepInfo();
	c_timesteps.add(info);
	// System.out.println("FlowEquation constructor");
    }
    /**
     * set the flow id's, called by FlowEquationDialog
     *
     * @param f the flow id vector
     * @param c the coeff vector
     * @param i the isInteger vector
     * @param r the equation value
     */
    public void setTimestepInfo(Vector f_in, Vector c_in, Vector i_in,
				Vector f_out, Vector c_out, Vector i_out,
				float r, String rhsConstraint){
	TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( getTimestep() - 1 );

	info.setInFlow(f_in);
	info.setCoeffIn(c_in);
	info.setIsIntegerIn(i_in);
	info.setOutFlow(f_out);
	info.setCoeffOut(c_out);
	info.setIsIntegerOut(i_out);
	info.setEquationValue(r);
        info.setRhsConstraint(rhsConstraint);
	//print(info.getInFlow());
    }
    /**
     * Get the flow from this timestep info
     */
    public Vector getInFlow(){
	TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( getTimestep() - 1 );
	return info.getInFlow();
    }
    public Vector getCoeffIn(){
	TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( getTimestep() - 1 );
	return info.getCoeffIn();
    }
    public Vector getIsIntegerIn(){
	TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( getTimestep() - 1 );
	return info.getIsIntegerIn();
    }
    public Vector getOutFlow(){
	TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( getTimestep() - 1 );
	return info.getOutFlow();
    }
    public Vector getCoeffOut(){
	TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( getTimestep() - 1 );
	return info.getCoeffOut();
    }
    public Vector getIsIntegerOut(){
	TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( getTimestep() - 1 );
	return info.getIsIntegerOut();
    }
    public float getEquationValue(){
	TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( getTimestep() - 1 );
	return info.getEquationValue();
    }
    public String getRhsConstraint(){
      TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( getTimestep() -1 );
      return info.getRhsConstraint();
    }
    //used during the develop process
    public static void print(Vector v){
	for( Iterator i = v.iterator(); i.hasNext();){
	    System.out.println(i.next().toString());
	}
    }

    /*
     * Adds equations to the control.
     */
    public void addEquationOfTimestep(EquationControl control,
				      int timestep, int index, ID node,
				      Vector inFlows, Vector outFlows)
    {
	//get the current TimestepInfo
	//TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( getTimestep() - 1 );	//get the current TimestepInfo
	TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( index );	//get the current TimestepInfo using index
	//System.out.println("index = " + index);
	//System.out.println("getTimestep() - 1 =" + (getTimestep() - 1));
	//Fin - Fout = RHS e.g.
	//a1*y1 + a2*y2 + ... + b1*x1 + b2*x2 + ... = RHSValue
	//get all coefficients for selected incoming flows
	Vector inflowid = info.getInFlow();
	Vector inflowcoeff = info.getCoeffIn();
	int insize = inflowcoeff.size();
	//get all coefficients for outgoing flows. (Set neg. sign)
	Vector outflowid = info.getOutFlow();
	Vector outflowcoeff = info.getCoeffOut();
	int outsize = outflowcoeff.size();

	//prepare the inflowid and outflowid. If they still are instances of String,
	//i.e. loaded from a scenario in rmd-format: convert into matching ID. Do nothing
	//if they allready are in the preferred form ID
	//System.out.println("string to id");
	stringToID( inflowid, inFlows);
	stringToID( outflowid, outFlows);

	Equation eq = new Equation( node, new ID("Fu"), timestep,
				    index/*for the equation*/, info.getRhsConstraint(), info.getEquationValue() );
	Equation eqLink[] = new Equation[insize + outsize];//one equation needed for every in coeff to link variables.

	Variable var1, var2, var3;

	//create variables in
	for( int i = 0; i < insize; i++){
	    var1 = (new Variable( (ID)inflowid.elementAt(i),
					 timestep, getID()/*this function id*/, i+1,
					 ((Float)inflowcoeff.elementAt(i)).floatValue()));
	    eq.addVariable(var1);
	    //every variable "in" must be connected to the flow variable like "F1T1 - F1T1Fu3E1 = 0.0"
	    eqLink[i] = new Equation( node, new ID("Fu"), timestep, index, "E", (float)0.0);
	    //recreate a variable as var1 but with another coeff
	    var2 = (new Variable( (ID)inflowid.elementAt(i),
				  timestep, getID()/*this function id*/, i+1, (float)-1.0));
	    //create the variable representing the flow
	    var3 = new Variable( (ID)inflowid.elementAt(i),
				 timestep, (float)1.0);
	    eqLink[i].addVariable(var2);
	    eqLink[i].addVariable(var3);
	}



	//create variables out
	for( int i = 0; i < outsize; i++){
	    var1 = new Variable( (ID)outflowid.elementAt(i),
					 timestep, getID()/*this function id*/, i+1,
				 (((Float)outflowcoeff.elementAt(i)).floatValue()) * (float)-1.0);
	    eq.addVariable(var1);
	    //every variable "out" must be connected to the flow variable like "F1T1 - F1T1Fu3E1 = 0.0"
	    eqLink[i + insize] = new Equation( node, new ID("Fu"), timestep, index, "E", (float)0.0);
	    var2 = new Variable( (ID)outflowid.elementAt(i),
					 timestep, getID()/*this function id*/, i+1, (float)-1.0);
	    var3 = new Variable( (ID)outflowid.elementAt(i),
				 timestep, (float)1.0, true);
	    eqLink[i + insize].addVariable(var2);
	    eqLink[i + insize].addVariable(var3);
	}
	//add to equation
	control.add(eq);
	//add link equations
	for( int i = 0; i < insize + outsize; i++){
	    control.add(eqLink[i]);
	}
            /* Added by Nawzad Mardan 080909
    // To avoid Sorce and Destination function usage when user use Storage function 
    // finally add that the total flow is the sum of every
	// interval
	// for InFlow
        int eqId = insize + outsize;
        Variable var;
    Equation totalEq =new Equation(node, getID(), timestep, eqId++,Equation.EQUAL, (float) 0);
	for (int i = 0; i < insize; i++) 
        {
	    Equation flowTotal = new Equation(node, getID(), timestep, eqId++,Equation.EQUAL, (float) 0);
            //var = new Variable((ID) inFlowId.elementAt(i), timestep, 1.0f);
            var = new Variable( (ID)inflowid.elementAt(i),timestep,getID(),1,(float)-1.0);
            //var = new Variable((ID) inFlowId.get(i),timestep, getID(), 1, (float) -1);
            flowTotal.addVariable(var);
            totalEq.addVariable(var);
	    //var = new Variable((ID) inFlowId.get(i),timestep, (float) 1);
	    var = new Variable( (ID)inflowid.elementAt(i),timestep,(float)1.0);
            flowTotal.addVariable(var);
	    control.add(flowTotal);
	}

	// for OutFlow
	for (int i = 0; i < outsize; i++) 
        {
	   Equation flowTotal =new Equation(node, getID(), timestep, eqId++,Equation.EQUAL, (float) 0);
	   //var = new Variable((ID) outFlowId.get(i),timestep, getID(), 1, (float) -1);
           var = new Variable( (ID)outflowid.elementAt(i),timestep,getID(),1,(float)-1.0);
           flowTotal.addVariable(var);
           var = new Variable( (ID)outflowid.elementAt(i),timestep,getID(),1,(float)1.0);
           totalEq.addVariable(var);
	   //var = new Variable((ID) outFlowId.get(i),timestep, (float) 1);
            var = new Variable( (ID)outflowid.elementAt(i),timestep,(float)1.0);
	    flowTotal.addVariable(var);
	    control.add(flowTotal);
	}
    control.add(totalEq);
    */
    }



    /**
     * Creates a new copy of the function
     * @return A complete copy
     */
    public Object clone()
	throws CloneNotSupportedException
    {
	FlowEquation clone = (FlowEquation) super.clone();
	c_timesteps = new Vector(clone.c_timesteps);
	return clone;
    }

    /**
     * Returns optimizationinformation from FlowEquation
     * @param maxTimesteps The maximum number of timesteps in the model
     * @param node The ID for the node that generates the equations
     * @return Some equations that model the flowEquation's behaviour
     * @throws ModelException if it cannot optimize
     */
    public EquationControl getEquationControl(int maxTimesteps, ID node,
					      Vector toFlows, Vector fromFlows)
	throws ModelException
    {

	EquationControl control = new EquationControl();
	//Vector xFlows = new Vector(0);
	//Vector yFlows = new Vector(0);
	//addEquationOfTimestep( control, 1/* timestep */, 1/*index*/,
	//		       node, toFlows, fromFlows);
	int vectorsize = c_timesteps.size();
	for (int i = 0; i < maxTimesteps ; i++) {
	    /*
	      for every timestep, we need to generate a variable
	      First we find out the index of the cost
	      These two lines makes us use the same value in the vector
	      many times if we don't have enough information for all
	      timesteps in the model
	    */
	    int index =  ((i * vectorsize) / maxTimesteps ) % vectorsize;
	    addEquationOfTimestep(control, i+1, index, node, toFlows, fromFlows);
	}

	return control;
    }

    public String toXML(ResourceControl resources, int indent)
    {
	String xml = XML.indent(indent) + "<flowEquation>" + XML.nl();
	//save label
	if( getLabel() != null ){
	    xml = xml + XML.indent(indent + 1) + "<label>" + getLabel()
		+ "</label>" + XML.nl();
	}
	//support for multiple timesteps
	int numberOfTimesteps = getTimesteps();
	for( int i = 0; i < numberOfTimesteps; i++){
	    TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( i );
	    xml = xml + XML.indent( indent + 1) + "<timestep.flowEquation nr=\"" + (i + 1) + "\">" + XML.nl();
	    xml = xml + info.toXML( indent + 2);
	    xml = xml + XML.indent( indent + 1) + "</timestep.flowEquation>" + XML.nl();
	}
	xml = xml + XML.indent(indent) + "</flowEquation>" + XML.nl();
	return xml;
    }
    
    /**
     *  PUM5 Added
     * 
     */
    public void toEXML(ResourceControl resources,ExmlSheet sheet) {
    	
    	//Find Label
    	String label = ((this.getLabel()==null)?"":this.getLabel());
    	
		//Add function header
		sheet.addFunctionHeader("FlowEquation", label);
		
    	// Add timestep nrs in one Row.
    	int numberOfTimesteps = getTimesteps();
    	sheet.addTimeStepRow(numberOfTimesteps);

    	
    	int inFlow = 0;
    	int outFlow = 0;
    	for( int i = 0; i < numberOfTimesteps; i++){		
    		TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( i );
    		inFlow = (info.getInFlow().size() > inFlow)?info.getInFlow().size():inFlow;
    		outFlow = (info.getOutFlow().size() > outFlow)?info.getOutFlow().size():outFlow;
    	}
    	 
    	// First, initiate inflow table
    	sheet.initTable(inFlow*2+3, numberOfTimesteps);
    	sheet.addLockedTableValue("RHSValue");
    	sheet.addLockedTableValue("RHSConstraint");
    	sheet.addStyledTableValue("Inflows","BoldType");
    	for (int i = 0; i < inFlow; i++) {
    		sheet.addLockedTableValue("Flow");
    		sheet.addLockedTableValue("Coeff");
    	}
    	
    	
    	//support for multiple timesteps
    	
	    for( int i = 0; i < numberOfTimesteps; i++){
    	    TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( i );
    	    info.toEXML(sheet,true);
    	    if (info.c_inFlow.size() < inFlow)
    	    	sheet.fillTableToNextCol();
	    }
    	sheet.endTable();
    	
    	// Second, outflow table
    	sheet.initTable(outFlow*2+1, numberOfTimesteps);
	    sheet.addStyledTableValue("Outflows","BoldType");  
    	for (int i = 0; i < outFlow; i++) {
    		sheet.addLockedTableValue("Flow");
    		sheet.addLockedTableValue("Coeff");
    	}
	    
    	for( int i = 0; i < numberOfTimesteps; i++){
    	    TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( i );
    	    info.toEXML(sheet,false);
    	    if (info.c_outFlow.size() < outFlow)
    	    	sheet.fillTableToNextCol();
    	}
    	
    	sheet.endTable();
    	sheet.addRow(sheet.addCell(""));
    	
    }

    public void parseData(LinkedList data, ResourceControl rc,
			  boolean createMissingResources)
        throws RmdParseException
    {
	setTimesteplevel((Timesteplevel) data.removeLast());

	// first we get the label of the function
	if (((String) data.getFirst()).equals("label")) {
	    data.removeFirst(); //Throw away the tag
	    setLabel((String)data.removeFirst());
	}
	//empty the default c_timesteps
	c_timesteps.clear();
	// iterate through all the timesteps
	for (int i = 0; data.size() > 0 &&
	((String) data.getFirst()).equals("T"); i++) {
		data.removeFirst(); // remove the "T"
		//	    System.out.println("timestep " + i);
		//TimestepInfo info = c_info;//(TimestepInfo) c_timesteps.get(i);
		//TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( i );
		TimestepInfo info = new TimestepInfo();
		Vector flowin = new Vector();
		Vector coeffin = new Vector();
		Vector flowout = new Vector();
		Vector coeffout = new Vector();

		//get all flowid_in
		/*
		 * Changed pum2007
		 */
		for (int j = 0; data.size() > j &&
		!((String) data.get(j)).equals("T"); j++) {
			try {
				if (((String) data.get(j)).equals("flowid_in")) {
					data.remove(j);
					flowin.addElement((String) data.remove(j));
					j--;
				}
			}
			catch (NumberFormatException e) {
				throw new RmdParseException("The flow equation data " +
						"is incorrect. <flowid_in>, " +
						"<coeff_in>, or <isinteger_in> " +
				"tag contains invalid data.");
			}
		}

		/*
		 * Changed pum2007
		 */
		//get all coeff_in
		for (int j = 0; data.size() > j &&
		!((String) data.get(j)).equals("T"); j++) {
			try {
				if (((String) data.get(j)).equals("coeff_in")) {
					data.remove(j);
					coeffin.addElement(new Float(Float.parseFloat((String) data.remove(j))));
					j--;
				}
			}
			catch (NumberFormatException e) {
				throw new RmdParseException("The flow equation data " +
						"is incorrect. <flowid_in>, " +
						"<coeff_in>, or <isinteger_in> " +
				"tag contains invalid data.");
			}
		}

		/*
		 * Changed pum2007
		 */
		//get all flowid_out
		for (int j = 0; data.size() > j &&
		!((String) data.get(j)).equals("T"); j++) {
			try {
				if (((String) data.get(j)).equals("flowid_out")) {
					data.remove(j);
					flowout.addElement((String) data.remove(j));
					j--;
				}
			}
			catch (NumberFormatException e) {
				throw new RmdParseException("The flow equation data " +
						"is incorrect. <flowid_out>, " +
						"<coeff_in>, or <isinteger_out> " +
				"tag contains invalid data.");
			}
		}
		//get all coeff_out
		/*
		 * Changed pum2007
		 */
		for (int j = 0; data.size() > j &&
		!((String) data.get(j)).equals("T"); j++) {
			try {
				if (((String) data.get(j)).equals("coeff_out")) {
					data.remove(j);
					coeffout.addElement(new Float(Float.parseFloat((String) data.remove(j))));
					j--;
				}
			}
			catch (NumberFormatException e) {
				throw new RmdParseException("The flow equation data " +
						"is incorrect. <flowid_out>, " +
						"<coeff_out>, or <isinteger_out> " +
				"tag contains invalid data.");
			}
		}
	    //add vectors to timestepinfo
	    info.setInFlow(flowin);
	    info.setCoeffIn(coeffin);
	    info.setOutFlow(flowout);
	    info.setCoeffOut(coeffout);

	    //get equationvalue
	    if( ((String)data.getFirst()).equals("equationvalue")){
		data.removeFirst();
		info.setEquationValue( Float.parseFloat((String)data.removeFirst()) );
	    }
            //get the rshConstraint
	    if (data.size() > 0) {
	    	if ( ( (String) data.getFirst()).equals("constrainttype")) {
	    		data.removeFirst();
	    		info.setRhsConstraint( (String) data.removeFirst());
	    	}
	    }
	    else {
	    	info.setRhsConstraint("E");
	    }
	    c_timesteps.addElement(info);
	}

    }
    /*
     * Convert the id represented as a string into instance of ID. What happens if inFlows mismatch with
     * the inflowid?
     */
    private void stringToID( Vector flowid, Vector flows){
	//System.out.println("FlowEquation: flowid = " + flowid.size() + " flows = " + flows.size() );
	if( flowid != null && flows != null ){
	    for( int i = 0; i < flowid.size(); i++ ){
		String stringID = flowid.elementAt(i).toString();//this works for both ID or String
		for( int j = 0; j < flows.size(); j++){
		    ID candidate = ((Flow)flows.elementAt(j)).getID();
		    if( stringID.equals(candidate.toString())){
			//System.out.println(stringID + "   " + candidate + " match!");
			//remove current stringID and replace with candidate
			flowid.remove(i);
			flowid.add( i, candidate);
		    }
		    else{
			//System.out.println(stringID + "   " + candidate + " mismatch!");
		    }
		}
	    }
	}
	else{
	    System.out.println("FlowEquation: flowid or flows = null");
	}
    }


    public void timestepResetData(int size){
	//Create vector and reset to zero
	c_timesteps = new Vector(size);
	for(int i = 0; i < size; i++)
	    c_timesteps.add(new TimestepInfo());
    }

    public void timestepSetLessDetailed(int newSize, int factor){
	Vector newTimestepInfo = new Vector(newSize);
	int i, oldindex;

	//Copy old cost values to new cost array
	for(i = 0, oldindex = 0; i < newSize; i++, oldindex += factor)
	    newTimestepInfo.add(c_timesteps.get(oldindex));

	c_timesteps = newTimestepInfo;
    }

    public void timestepSetMoreDetailed(int factor){
		int oldsize = c_timesteps.size();
	int newsize = oldsize * factor;

	//Copy old values to new array
	Vector newTimestepInfo = new Vector(newsize,1);
	TimestepInfo info;
	for(int i = 0; i < oldsize; i++) {
	    info = ((TimestepInfo) c_timesteps.get(i));
	    for(int k = 0; k < factor; k++) {
		newTimestepInfo.add( (TimestepInfo)info.clone() );
	    }
	}
	c_timesteps = newTimestepInfo;
    }

    public void timestepRemoveAt(int index){
	c_timesteps.removeElementAt(index);
    }

    /*
     * Insert a new timestep
     */
    public void timestepInsertAt(int index){
	c_timesteps.insertElementAt( new TimestepInfo(), index);
    }
    public int getTimesteps(){
	return c_timesteps.size();
    }

/**
* Abstract function declared by the interface NodeFunction
* Is used to determine if a node function in any way is related to a specific flow
* This information is needed when deleting flows in order to maintain consistency of
* the model.
* @param flow ID
* @return  true means that the function references to this flow and sholdn't be removed.
*/
  public boolean isRelatedToFlow(ID theFlow) {

    Iterator it = c_timesteps.iterator();
    Iterator flowIterator;
    Object stringOrId;
    boolean isRelated = false;

    while (it.hasNext()) {
      TimestepInfo info = (TimestepInfo) it.next();
      Vector inflowid = info.getInFlow();
      Vector outflowid = info.getOutFlow();

      /* find flow.toString() in inflowid and outflowid
         the elements in inflowid and outflowid might be of the type String or the type ID
         which is realllly stupid   */
      flowIterator = inflowid.iterator();
      while (flowIterator.hasNext()) {
        if (theFlow.toString().equals(flowIterator.next().toString())) {
          isRelated = true;
          break; // no need to search anymore
        }
      }
      flowIterator = outflowid.iterator();

      if (!isRelated) // if isRelated was set to true above there is no need to continue
      while (flowIterator.hasNext()) {
        if (theFlow.toString().equals(flowIterator.next().toString())) {
          isRelated = true;
          break; // no need to search anymore
        }
      }
    }

    return isRelated;
  }
}





