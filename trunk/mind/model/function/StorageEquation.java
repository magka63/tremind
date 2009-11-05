/*
 * Copyright 2003:
 * Jonas S‰‰v <js@acesimulation.com>
 *
 * Copyright 2007:
 * Per Fredriksson <perfr775@student.liu.se>
 * David Karlsl‰tt <davka417@student.liu.se>
 * Tor Knutsson	<torkn754@student.liu.se>
 * Daniel K‰llming <danka053@student.liu.se>
 * Ted Palmgren <tedpa175@student.liu.se>
 * Freddie Pintar <frepi150@student.liu.se>
 * MÂrten ThurÈn <marth852@student.liu.se> 
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

import mind.model.*;
import mind.io.*;

import java.util.*;
import java.util.LinkedList;

/**
 * The function class StorageEquation
 *
 * @author Jonas S‰‰v
 * @author Freddie Pintar
 * @author Tor Knutsson
 * @version 2007-12-12
 */

public class StorageEquation
    extends NodeFunction
    implements Cloneable {

  private Vector c_timesteps = null;
  private int c_maxStorageTime = -1;
  private double c_InitialStorage = .0;
  private double c_FinalStorage = .0;


  public StorageEquation() {
    super(new ID(ID.FUNCTION), "StorageEquation", null);
    c_timesteps = new Vector();
    TimestepInfo info = new TimestepInfo();
    c_timesteps.add(info);
// System.out.println("FlowEquation constructor");
  }


/**
 * set the flow id's, called by StorageDialog
 *
 * @param f the flow id vector
 * @param c the coeff vector
 * @param i the isInteger vector
 * @param r the equation value
 */
public void setTimestepInfo(Vector f_in, Vector c_in_min, Vector c_in_max,
                            Vector f_out, Vector c_out_min, Vector c_out_max,
                            double c_dblOutEfficiency,
                            double c_dblInEfficiency,
                            double c_dblTotalInFlowMin,
                            double c_dblTotalInFlowMax,
                            double c_dblTotalOutFlowMin,
                            double c_dblTotalOutFlowMax,
                         //   double c_dblInitialStorage,
                         //   double c_dblFinalStorage,
                            double c_dblStorageEfficiency,
                            double c_dblMinStorage,
                            double c_dblMaxStorage
                            ) {
    TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( getTimestep() - 1 );

    info.setInFlow(f_in);
    info.setMinCoeffIn(c_in_min);
    info.setMaxCoeffIn(c_in_max);
    info.setOutFlow(f_out);
    info.setMinCoeffOut(c_out_min);
    info.setMaxCoeffOut(c_out_max);

    info.c_dblOutEfficiency = c_dblOutEfficiency;
    info.c_dblInEfficiency = c_dblInEfficiency;
    info.c_dblTotalInFlowMin = c_dblTotalInFlowMin;
    info.c_dblTotalInFlowMax = c_dblTotalInFlowMax;
    info.c_dblTotalOutFlowMin = c_dblTotalOutFlowMin;
    info.c_dblTotalOutFlowMax = c_dblTotalOutFlowMax;
//    info.c_dblInitialStorage = c_dblInitialStorage;
//    info.c_dblFinalStorage = c_dblFinalStorage;
    info.c_dblStorageEfficiency = c_dblStorageEfficiency;
    info.c_dblMinStorage = c_dblMinStorage;
    info.c_dblMaxStorage = c_dblMaxStorage;


    //print(info.getInFlow());
}

/**
 * Get the flows and coefficients from this timestep info
 */
public Vector getInFlow() {
  TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(getTimestep() - 1);
  return info.getInFlow();
}

public Vector getMinCoeffIn() {
  TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(getTimestep() - 1);
  return info.getMinCoeffIn();
}

public Vector getMaxCoeffIn() {
  TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(getTimestep() - 1);
  return info.getMaxCoeffIn();
}

public Vector getOutFlow() {
  TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(getTimestep() - 1);
  return info.getOutFlow();
}

public Vector getMinCoeffOut() {
  TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(getTimestep() - 1);
  return info.getMinCoeffOut();
}

public Vector getMaxCoeffOut() {
  TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(getTimestep() - 1);
  return info.getMaxCoeffOut();
}

public double getOutEfficiency() {
  TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(getTimestep() - 1);
  return info.c_dblOutEfficiency;
}
public double getInEfficiency() {
  TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(getTimestep() - 1);
  return info.c_dblInEfficiency;
}
public double getTotalInFlowMin() {
  TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(getTimestep() - 1);
  return info.c_dblTotalInFlowMin;
}
public double getTotalInFlowMax() {
  TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(getTimestep() - 1);
  return info.c_dblTotalInFlowMax;
}
public double getTotalOutFlowMin() {
  TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(getTimestep() - 1);
  return info.c_dblTotalOutFlowMin;
}
public double getTotalOutFlowMax() {
  TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(getTimestep() - 1);
  return info.c_dblTotalOutFlowMax;
}
public double getInitialStorage() {
  return c_InitialStorage;
}
public void setInitialStorage(double is) {
  c_InitialStorage = is;
}
public double getFinalStorage() {
  return c_FinalStorage;
}
public void setFinalStorage(double fs) {
  c_FinalStorage = fs;
}
public double getStorageEfficiency() {
  TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(getTimestep() - 1);
  return info.c_dblStorageEfficiency;
}
public double getMinStorage() {
  TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(getTimestep() - 1);
  return info.c_dblMinStorage;
}
public double getMaxStorage() {
  TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(getTimestep() - 1);
  return info.c_dblMaxStorage;
}
public int getMaxStorageTime() {
  return c_maxStorageTime;
}
public void setMaxStorageTime(int st) {
  c_maxStorageTime = st;
}

public static enum TableType {INFLOW,OUTFLOW,CONSTRAINTS};
/**
 * Inner class representing the lhs flows with coefficients and other
 * storage parameters
 */
class TimestepInfo{
    private Vector c_inFlow = new Vector(0);
    private Vector c_minCoeffIn = new Vector(0);
    private Vector c_maxCoeffIn = new Vector(0);
    private Vector c_outFlow = new Vector(0);
    private Vector c_minCoeffOut = new Vector(0);
    private Vector c_maxCoeffOut = new Vector(0);

    public double c_dblOutEfficiency = 100.0;
    public double c_dblInEfficiency = 100.0;
    public double c_dblTotalInFlowMin = 0.0;
    public double c_dblTotalInFlowMax = -1.0;
    public double c_dblTotalOutFlowMin = 0.0;
    public double c_dblTotalOutFlowMax = -1.0;
    public double c_dblStorageEfficiency = 100.0;
    public double c_dblMinStorage = 0.0; // Storage capacity lower bound
    public double c_dblMaxStorage = -1.0; // Storage capacity upper bound

    TimestepInfo(){
    }

    // in flow
    Vector getInFlow(){
        return c_inFlow;
    }
    // min
    Vector getMinCoeffIn(){
        return c_minCoeffIn;
    }
    // max
    Vector getMaxCoeffIn(){
        return c_maxCoeffIn;
    }

    // out flow
    Vector getOutFlow(){
        return c_outFlow;
    }
    // min
    Vector getMinCoeffOut(){
        return c_minCoeffOut;
    }
    // max
    Vector getMaxCoeffOut(){
        return c_maxCoeffOut;
    }

    // in flow
    void setInFlow(Vector f){
        c_inFlow = f;
    }
    // min
    void setMinCoeffIn(Vector c){
        c_minCoeffIn = c;
    }
    // max
    void setMaxCoeffIn(Vector c){
        c_maxCoeffIn = c;
    }

    // out flow
    void setOutFlow(Vector f){
        c_outFlow = f;
    }
    // min
    void setMinCoeffOut(Vector c){
        c_minCoeffOut = c;
    }
    // max
    void setMaxCoeffOut(Vector c){
        c_maxCoeffOut = c;
    }

/**
 * Create the timestep information into xml
 */
public String toXML(int indent){
    String xml = "";

    for( int index = 0; index < c_inFlow.size(); index++ ){
        xml = xml + XML.indent( indent ) + "<flowid_in>" +
            c_inFlow.elementAt(index).toString() + "</flowid_in>" + XML.nl();
    }
    for( int index = 0; index < c_inFlow.size(); index++ ){
        xml = xml + XML.indent( indent ) + "<coeff_in_min>" +
            ((Double)(c_minCoeffIn.elementAt(index))).doubleValue() + "</coeff_in_min>" + XML.nl();
    }
    for( int index = 0; index < c_inFlow.size(); index++ ){
        xml = xml + XML.indent( indent ) + "<coeff_in_max>" +
            ((Double)(c_maxCoeffIn.elementAt(index))).doubleValue() + "</coeff_in_max>" + XML.nl();
    }
    for( int index = 0; index < c_outFlow.size(); index++ ){
        xml = xml + XML.indent( indent ) + "<flowid_out>" +
            c_outFlow.elementAt(index).toString() + "</flowid_out>" + XML.nl();
    }
    for( int index = 0; index < c_outFlow.size(); index++ ){
        xml = xml + XML.indent( indent ) + "<coeff_out_min>" +
            ((Double)(c_minCoeffOut.elementAt(index))).doubleValue() + "</coeff_out_min>" + XML.nl();
    }
    for( int index = 0; index < c_outFlow.size(); index++ ){
        xml = xml + XML.indent( indent ) + "<coeff_out_max>" +
            ((Double)(c_maxCoeffOut.elementAt(index))).doubleValue() + "</coeff_out_max>" + XML.nl();
    }

    xml = xml + XML.indent(indent) + "<c_dblInEfficiency>" + c_dblInEfficiency + "</c_dblInEfficiency>" + XML.nl();
    xml = xml + XML.indent(indent) + "<c_dblTotalInFlowMin>"  + c_dblTotalInFlowMin + "</c_dblTotalInFlowMin>" + XML.nl();
    xml = xml + XML.indent(indent) + "<c_dblTotalInFlowMax>"  + c_dblTotalInFlowMax + "</c_dblTotalInFlowMax>" + XML.nl();

    xml = xml + XML.indent(indent) + "<c_dblOutEfficiency>" + c_dblOutEfficiency + "</c_dblOutEfficiency>" + XML.nl();
    xml = xml + XML.indent(indent) + "<c_dblTotalOutFlowMin>"  + c_dblTotalOutFlowMin + "</c_dblTotalOutFlowMin>" + XML.nl();
    xml = xml + XML.indent(indent) + "<c_dblTotalOutFlowMax>"  + c_dblTotalOutFlowMax + "</c_dblTotalOutFlowMax>" + XML.nl();

    xml = xml + XML.indent(indent) +  "<c_dblInitialStorage>" + c_InitialStorage + "</c_dblInitialStorage>" + XML.nl();
    xml = xml + XML.indent(indent) +  "<c_dblFinalStorage>" + c_FinalStorage + "</c_dblFinalStorage>" + XML.nl();
    xml = xml + XML.indent(indent) +  "<c_dblStorageEfficiency>" + c_dblStorageEfficiency + "</c_dblStorageEfficiency>" + XML.nl();
    xml = xml + XML.indent(indent) +  "<c_dblMinStorage>" + c_dblMinStorage + "</c_dblMinStorage>" + XML.nl();
    xml = xml + XML.indent(indent) +  "<c_dblMaxStorage>" + c_dblMaxStorage + "</c_dblMaxStorage>" + XML.nl();
    xml = xml + XML.indent(indent) +  "<c_intMaxStorageTime>" + c_maxStorageTime + "</c_intMaxStorageTime>" + XML.nl();

    return xml;
}


/**
* Puts EXML data in the given ExmlSheet
* PUM5 Added 2007-12-12
* @param sheet The ExmlSheet to be changed
* @param tabletype 
*/
public void toEXML(ExmlSheet sheet, TableType tabletype)
{

    if (tabletype == TableType.INFLOW) {
    	for( int index = 0; index < c_inFlow.size(); index++ ){
    		sheet.addStyledTableValue(c_inFlow.elementAt(index).toString(),"SideTopBorders");
    		sheet.addStyledTableValue(((Double)(c_minCoeffIn.elementAt(index))),"SideBorders");
    		sheet.addStyledTableValue(((Double)(c_maxCoeffIn.elementAt(index))),"SideBottomBorders");
    	}
    }
    else if (tabletype == TableType.OUTFLOW) {
    	for( int index = 0; index < c_outFlow.size(); index++ ){
    		sheet.addStyledTableValue(c_outFlow.elementAt(index).toString(),"SideTopBorders");
    		sheet.addStyledTableValue(((Double)(c_minCoeffOut.elementAt(index))),"SideBorders");
    		sheet.addStyledTableValue(((Double)(c_maxCoeffOut.elementAt(index))),"SideBottomBorders");
    	}
    }
    else if (tabletype == TableType.CONSTRAINTS) {
    	sheet.addTableValue(c_dblInEfficiency);
    	sheet.addTableValue(c_dblTotalInFlowMin);
    	sheet.addTableValue(c_dblTotalInFlowMax);
    	sheet.addTableValue(c_dblOutEfficiency);
    	sheet.addTableValue(c_dblTotalOutFlowMin);
    	sheet.addTableValue(c_dblTotalOutFlowMax);
    	

    	sheet.addTableValue(c_dblStorageEfficiency);
    	sheet.addTableValue(c_dblMinStorage);
    	sheet.addTableValue(c_dblMaxStorage);

    }
    
}

/**
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

    // in flow min coefficients
    temp = new Vector();
    for( int i = 0; i < this.getMinCoeffIn().size(); i++ ){
        temp.addElement( new Double(((Double)(this.getMinCoeffIn().elementAt(i))).doubleValue()));
    }
    copy.setMinCoeffIn( temp );

    // in flow max coefficients
    temp = new Vector();
    for( int i = 0; i < this.getMaxCoeffIn().size(); i++ ){
        temp.addElement( new Double(((Double)(this.getMaxCoeffIn().elementAt(i))).doubleValue()));
    }
    copy.setMaxCoeffIn( temp );

    //out flow
    temp = new Vector();
    for( int i = 0; i < this.getOutFlow().size(); i++ ){
        temp.addElement( (this.getOutFlow().elementAt(i)));
    }
    copy.setOutFlow( temp );

    // out flow min coefficients
    temp = new Vector();
    for( int i = 0; i < this.getMinCoeffOut().size(); i++ ){
        temp.addElement( new Double(((Double)(this.getMinCoeffOut().elementAt(i))).doubleValue()));
    }
    copy.setMinCoeffOut( temp );

    // out flow max coefficients
    temp = new Vector();
    for( int i = 0; i < this.getMaxCoeffOut().size(); i++ ){
        temp.addElement( new Double(((Double)(this.getMaxCoeffOut().elementAt(i))).doubleValue()));
    }
    copy.setMaxCoeffOut(temp);

    copy.c_dblOutEfficiency = c_dblOutEfficiency;
    copy.c_dblInEfficiency = c_dblInEfficiency;
    copy.c_dblTotalInFlowMin = c_dblTotalInFlowMin;
    copy.c_dblTotalInFlowMax = c_dblTotalInFlowMax;
    copy.c_dblTotalOutFlowMin = c_dblTotalOutFlowMin;
    copy.c_dblTotalOutFlowMax = c_dblTotalOutFlowMax;
    copy.c_dblStorageEfficiency = c_dblStorageEfficiency;
    copy.c_dblMinStorage = c_dblMinStorage;
    copy.c_dblMaxStorage = c_dblMaxStorage;

    return copy;
}

}

public String toXML(ResourceControl resources, int indent)
{
    String xml = XML.indent(indent) + "<storageEquation>" + XML.nl();
    //save label
    if( getLabel() != null ){
        xml = xml + XML.indent(indent + 1) + "<label>" + getLabel()
            + "</label>" + XML.nl();
    }
    //support for multiple timesteps
    int numberOfTimesteps = getTimesteps();
    for( int i = 0; i < numberOfTimesteps; i++){
        TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( i );
        xml = xml + XML.indent( indent + 1) + "<timestep.storageEquation nr=\"" + (i + 1) + "\">" + XML.nl();
        xml = xml + info.toXML( indent + 2);
        xml = xml + XML.indent( indent + 1) + "</timestep.storageEquation>" + XML.nl();
    }
    xml = xml + XML.indent(indent) + "</storageEquation>" + XML.nl();
    return xml;
}

/**
 *  PUM5 Added
 * 
 */
public void toEXML(ResourceControl resources,ExmlSheet sheet) {

	//Add Label
	String label = ((this.getLabel()==null)?"":this.getLabel());
	
	//Add function header
	sheet.addFunctionHeader("StorageEquation", label);
	
	// First, add time step-independent settings.
	sheet.addRow(sheet.addLockedCell("InitialStorage")+sheet.addCell(c_InitialStorage));
	sheet.addRow(sheet.addLockedCell("FinalStorage")+sheet.addCell(c_FinalStorage));
	sheet.addRow(sheet.addLockedCell("MaxStorageTime")+sheet.addCell(c_maxStorageTime));

	// Add timestep nrs in one Row.
	int numberOfTimesteps = getTimesteps();
	sheet.addTimeStepRow(numberOfTimesteps);

	// Calculate maximum rows  for flows
	int inFlow = 0;
	int outFlow = 0;
	for( int i = 0; i < numberOfTimesteps; i++){		
		TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( i );
		inFlow = (info.getInFlow().size() > inFlow)?info.getInFlow().size():inFlow;
		outFlow = (info.getOutFlow().size() > outFlow)?info.getOutFlow().size():outFlow;
	}
	
	//Add Timestep data for inflows
	sheet.addRow(sheet.addBoldCell("InFlows"));
	sheet.initTable(inFlow * 3, numberOfTimesteps);
	for (int i = 0; i< inFlow;i++) {
		sheet.addLockedTableValue("Flow");
		sheet.addLockedTableValue("Min");
		sheet.addLockedTableValue("Max");
	}
	for( int i = 0; i < numberOfTimesteps; i++){
		TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( i );
		info.toEXML(sheet,TableType.INFLOW);
		if (info.c_inFlow.size() <inFlow)
			sheet.fillTableToNextCol();
	}
	sheet.endTable();

	//Add Timestep data for outflows
	
	sheet.addRow(sheet.addBoldCell("OutFlows"));
	sheet.initTable(outFlow * 3, numberOfTimesteps);
	for (int i = 0; i< outFlow;i++) {
		sheet.addLockedTableValue("Flow");
		sheet.addLockedTableValue("Min");
		sheet.addLockedTableValue("Max");		
	}
	for( int i = 0; i < numberOfTimesteps; i++){
		TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( i );
		info.toEXML(sheet,TableType.OUTFLOW);
		if (info.c_outFlow.size() <outFlow)
			sheet.fillTableToNextCol();
	}
	sheet.endTable();

	//Add Timestep data for outflows
	sheet.addRow(sheet.addBoldCell("Constraints"));
	sheet.initTable(9, numberOfTimesteps);
	sheet.addLockedTableValue("InEfficiency");
	sheet.addLockedTableValue("TotalInFlowMin");
	sheet.addLockedTableValue("TotalInFlowMax");
	sheet.addLockedTableValue("OutEfficiency");
	sheet.addLockedTableValue("TotalOutFlowMin");
	sheet.addLockedTableValue("TotalOutFlowMax");

	sheet.addLockedTableValue("StorageEfficiency");
	sheet.addLockedTableValue("MinStorage");
	sheet.addLockedTableValue("MaxStorage");

	for( int i = 0; i < numberOfTimesteps; i++){
		TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( i );
		info.toEXML(sheet,TableType.CONSTRAINTS);		
	}
	sheet.endTable();
	sheet.addRow(sheet.addCell(""));
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


  /*
   * Adds equations to the control.
   */
  public void addEquationOfTimestep(EquationControl control,
                                    int timestep, int index, ID node,
                                    Vector inFlows, Vector outFlows, int maxTimesteps) {
    //get the current TimestepInfo
    //TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( getTimestep() - 1 );	//get the current TimestepInfo
    TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(index); //get the current TimestepInfo using index
    //get all boundaries from selected incoming flows
    Vector inFlowId = info.getInFlow();
    Vector inFlowCoeffMin = info.getMinCoeffIn();
    Vector inFlowCoeffMax = info.getMaxCoeffIn();
    int insize = inFlowId.size();
    //get all boundaries from selected outgoing flows
    Vector outFlowId = info.getOutFlow();
    Vector outFlowCoeffMin = info.getMinCoeffOut();
    Vector outFlowCoeffMax = info.getMaxCoeffOut();
    int outsize = outFlowId.size();
    int eqId = 1; // Equation ID counter

    Vector timeStepLengths = getTimesteplevel().getBottomLevel().getLengthsVector();
    double timeStepLength = ( (Float) timeStepLengths.get(timestep-1)).floatValue();
    // Added by Nawzad Mardan 20080909
    TimestepInfo ino = (TimestepInfo) c_timesteps.get(index); //get the current TimestepInfo using index
    Variable var, varStorage1, varStorage2;

    //prepare the inflowid and outflowid. If they still are instances of String,
    //i.e. loaded from a scenario in rmd-format: convert into matching ID. Do nothing
    //if they allready are in the preferred form ID
    //System.out.println("string to id");
    stringToID(inFlowId, inFlows);
    stringToID(outFlowId, outFlows);

    /*    Main Storage Equation
          Example:   Vin*TimeStepLengthT1(F1T1+F2T1+...+FmT1) + Vtime*StN1T0 -
                     Vout*TimeStepLengthT1(F3T1+F4T1+...+FnT1) - StN1T1 = 0

                     Vin*TimeStepLengthT2(F1T2+F2T2+...+FmT2) + Vtime*StN1T1 -
                     Vout*TimeStepLengthT2(F3T2+F4T2+...+FnT2) - StN1T2 = 0

                     etc.
  */

    Equation mainStorageEq = new Equation(node, getID()/* This function ID */, timestep, eqId++, Equation.EQUAL);
    /* Inflow variables
       Example:   F1T1, F2T1, etc  */
    for (int i=0; i<insize; i++) {
      var = new Variable((ID) inFlowId.elementAt(i), timestep,
                                  (float) ((info.c_dblInEfficiency/100.)*timeStepLength));
      mainStorageEq.addVariable(var);
    }
    /* Outflow variables */
    for (int i=0; i<outsize; i++) {
      var = new Variable((ID) outFlowId.elementAt(i), timestep,
                                  (float) (-1./(info.c_dblOutEfficiency/100.)*timeStepLength));
      mainStorageEq.addVariable(var);
    }
    /* Storage varible for this time step
       Example:  StFu23N1T1, StFu23N1T2, etc     */
    varStorage1 = new Variable("St" + getID().toString() + node.toString(), timestep, -1.0f);
    mainStorageEq.addVariable(varStorage1);
    /* Storage varible previous time step
       Example:  Vtime*StFu23N1T0  */
    varStorage2 = new Variable("St" + getID().toString() + node.toString(), timestep-1, (float) (info.c_dblStorageEfficiency/100.));
    mainStorageEq.addVariable(varStorage2);
    control.add(mainStorageEq);

    /* Special solution for StNxT0 and StNxTmax constraints (initial and final storage) */
    if (timestep == 1) {
      Equation initialStorageEq = new Equation(node, getID(), timestep-1, eqId++, Equation.EQUAL, (float)getInitialStorage());
      initialStorageEq.addVariable(varStorage2);
      control.add(initialStorageEq);
    }
    if (timestep == maxTimesteps) {
      Equation finalStorageEq = new Equation(node, getID(), timestep, eqId++, Equation.EQUAL, (float)-getFinalStorage());
      finalStorageEq.addVariable(varStorage1);
      control.add(finalStorageEq);
    }
    /* End of special solution for StNxT0 and StNxTmax constraints (initial and final storage) */

    /*    Storage Capacity Boundaries
          Example:  StN1T1 <= StN1T1max    and StN1T1 >= StN1T1min  */
    var = new Variable("St" + getID().toString() + node.toString(), timestep, 1.0f);

    if (info.c_dblMinStorage > 0.0) {  // no need to generate zero lower boundary equation
      Equation storageBoundaryMin = new Equation(node, getID(), timestep, eqId++, Equation.GREATEROREQUAL, (float)info.c_dblMinStorage);
      storageBoundaryMin.addVariable(var);
      control.add(storageBoundaryMin);
    }
    if (info.c_dblMaxStorage > 0.0) { // less than 0 means that no upper boundary exists so don't generate equation
      Equation storageBoundaryMax = new Equation(node, getID(), timestep, eqId++, Equation.LOWEROREQUAL, (float)info.c_dblMaxStorage);
      storageBoundaryMax.addVariable(var);
      control.add(storageBoundaryMax);
    }

    /*    Boundary on total Inflow
          Example: F1T1+F2T1+...+FmT1 >= TotInN1T1min
                   F1T1+F2T1+...+FmT1 <= TotInN1T1max   */
    if (info.c_dblTotalInFlowMin > 0.0) {  // no need to generate zero lower boundary equation
      Equation inFlowsBoundaryTotalMin = new Equation(node, getID(), timestep, eqId++, Equation.GREATEROREQUAL, (float)info.c_dblTotalInFlowMin);
      /* Inflow variables
         Example:   F1T1, F2T1, etc  */
      for (int i=0; i<insize; i++) {
        var = new Variable((ID) inFlowId.elementAt(i), timestep, 1.0f);
        inFlowsBoundaryTotalMin.addVariable(var);
      }
      control.add(inFlowsBoundaryTotalMin);

    }
    if (info.c_dblTotalInFlowMax > 0.0) { // less than 0 means that no upper boundary exists so don't generate equation
      Equation inFlowsBoundaryTotalMax = new Equation(node, getID(), timestep, eqId++, Equation.LOWEROREQUAL, (float)info.c_dblTotalInFlowMax);
      /* Inflow variables
         Example:   F1T1, F2T1, etc  */
      for (int i=0; i<insize; i++) {
        var = new Variable((ID) inFlowId.elementAt(i), timestep, 1.0f);
        inFlowsBoundaryTotalMax.addVariable(var);
      }
      control.add(inFlowsBoundaryTotalMax);

    }

    /*    Boundary on total Outflow
          Example: F1T1+F2T1+...+FmT1 >= TotOutN1T1min
                   F1T1+F2T1+...+FmT1 <= TotOutN1T1max   */
    if (info.c_dblTotalOutFlowMin > 0.0) {  // no need to generate zero lower boundary equation
      Equation outFlowsBoundaryTotalMin = new Equation(node, getID(), timestep, eqId++, Equation.GREATEROREQUAL, (float)info.c_dblTotalOutFlowMin);
      /* Outflow variables
         Example:   F3T1, F4T1, etc  */
      for (int i=0; i<outsize; i++) {
        var = new Variable((ID) outFlowId.elementAt(i), timestep, 1.0f);
        outFlowsBoundaryTotalMin.addVariable(var);
      }
      control.add(outFlowsBoundaryTotalMin);

    }
    if (info.c_dblTotalOutFlowMax > 0.0) { // less than 0 means that no upper boundary exists so don't generate equation
      Equation outFlowsBoundaryTotalMax = new Equation(node, getID(), timestep, eqId++, Equation.LOWEROREQUAL, (float)info.c_dblTotalOutFlowMax);
      /* Outflow variables
         Example:   F31, F4T1, etc  */
      for (int i=0; i<outsize; i++) {
        var = new Variable((ID) outFlowId.elementAt(i), timestep, 1.0f);
        outFlowsBoundaryTotalMax.addVariable(var);
      }
      control.add(outFlowsBoundaryTotalMax);

    }

    /*    Individual Inflow Boundaries
          Example: F1T1 >= F1T1min   and  F1T1 <= F1T1max
                   F2T1 >= F2T1min   and  F2T1 <= F2T1max
                                       ...
                   FmT1 >= FmT1min   and  FmT1 <= FmT1max   */

    Equation inFlowBoundaryMin, inFlowBoundaryMax;

    for (int i = 0; i<insize; i++) {
      /* Lower bound don't generate the equation if the bound is <= 0  */
      if (((Double)inFlowCoeffMin.get(i)).doubleValue() > 0.0) {
        inFlowBoundaryMin = new Equation(node, getID(), timestep, eqId++,
                                         Equation.GREATEROREQUAL,
                                         (float) ( (Double) inFlowCoeffMin.get(
            i)).doubleValue());
        var = new Variable( (ID) inFlowId.elementAt(i), timestep, 1.0f);
        inFlowBoundaryMin.addVariable(var);
        control.add(inFlowBoundaryMin);
      }

      /* Upper bound. don't generate the equation if the bound is <= 0  */
      if (((Double)inFlowCoeffMax.get(i)).doubleValue() > 0.0)
      {
        inFlowBoundaryMax = new Equation(node, getID(), timestep, eqId++,
                                           Equation.LOWEROREQUAL, (float) ((Double)inFlowCoeffMax.get(i)).doubleValue());
        var = new Variable((ID) inFlowId.elementAt(i), timestep, 1.0f);
        inFlowBoundaryMax.addVariable(var);
        control.add(inFlowBoundaryMax);
      }
    }

    /*    Individual Outflow Boundaries
          Example: F3T1 >= F1T1min   and  F3T1 <= F1T1max
                   F4T1 >= F2T1min   and  F4T1 <= F2T1max
                                             ...
                   FnT1 >= FmT1min   and  FnT1 <= FmT1max   */
    Equation outFlowBoundaryMin, outFlowBoundaryMax;

    for (int i = 0; i<outsize; i++) {
      /* Lower bound don't generate the equation if the bound is <= 0  */
      if (((Double)outFlowCoeffMin.get(i)).doubleValue() > 0.0) {
        outFlowBoundaryMin = new Equation(node, getID(), timestep, eqId++,
                                          Equation.GREATEROREQUAL,
                                          (float) ( (Double) outFlowCoeffMin.
            get(i)).doubleValue());
        var = new Variable( (ID) outFlowId.elementAt(i), timestep, 1.0f);
        outFlowBoundaryMin.addVariable(var);
        control.add(outFlowBoundaryMin);
      }

      /* Upper bound. don't generate the equation if the bound is <= 0  */
      if (((Double)outFlowCoeffMax.get(i)).doubleValue() > 0.0)
      {
        outFlowBoundaryMax = new Equation(node, getID(), timestep, eqId++,
                                           Equation.LOWEROREQUAL, (float) ((Double)outFlowCoeffMax.get(i)).doubleValue());
        var = new Variable((ID) outFlowId.elementAt(i), timestep, 1.0f);
        outFlowBoundaryMax.addVariable(var);
        control.add(outFlowBoundaryMax);
      }
    }

    /* Maximum storage time equations
       Example when StNjTimaxTime = 2
               F1T1+F2T1+...+FmT1 <=F3T1+F4T1+...+FnT1+F3T2+F4T2+...+FnT2+F3T3+...+FnT3
               F1T2+F2T2+...+FmT2 <=F3T2+F4T2+...+FnT2+F3T3+F4T3+...+FnT3+F3T4+...+FnT4
               F1T3+F2T3+...+FmT3 <=F3T3+F4T3+...+FnT3+F3T4+F4T4+...+FnT4+F3T5+...+FnT5  */

    if ((c_maxStorageTime > 0) &&
        (maxTimesteps > 1)) {  // this is for saftey only. If only one timestep exists
                               // then c_maxStorageTime should be -1

      Equation MaxStorageTimeEquation = new Equation(node, getID(), timestep, eqId++, Equation.LOWEROREQUAL);

      /* Inflow variables
         Example:   F1T1, F2T1, etc  */
      for (int i=0; i<insize; i++) {
        var = new Variable((ID) inFlowId.elementAt(i), timestep, 1.0f);
        MaxStorageTimeEquation.addVariable(var);
      }

      /* Outflow variables */
      /* timestep loop */
        for (int i=timestep;((i<=timestep+c_maxStorageTime)&&(i<=maxTimesteps));i++) {
          /* sum of outflows for the current timestep of the outer loop */
          for (int j=0; j<outsize; j++) {
            var = new Variable((ID) outFlowId.elementAt(j), i, -1.0f);
            MaxStorageTimeEquation.addVariable(var);
          }

        }
      control.add(MaxStorageTimeEquation);

    }
    /* Added by Nawzad Mardan 080909
    // To avoid Sorce and Destination function usage when user use Storage function 
    // finally add that the total flow is the sum of every
	// interval
	// for InFlow
    Equation totalEq =new Equation(node, getID(), timestep, eqId++,Equation.EQUAL, (float) 0);
	for (int i = 0; i < inFlowId.size(); i++) 
        {
	    Equation flowTotal = new Equation(node, getID(), timestep, eqId++,Equation.EQUAL, (float) 0);
            //var = new Variable((ID) inFlowId.elementAt(i), timestep, 1.0f);
            var = new Variable( (ID)inFlowId.elementAt(i),timestep,getID(),1,(float)-1.0);
            //var = new Variable((ID) inFlowId.get(i),timestep, getID(), 1, (float) -1);
            flowTotal.addVariable(var);
            totalEq.addVariable(var);
	    //var = new Variable((ID) inFlowId.get(i),timestep, (float) 1);
	    var = new Variable( (ID)inFlowId.elementAt(i),timestep,(float)1.0);
            flowTotal.addVariable(var);
	    control.add(flowTotal);
	}

	// for OutFlow
	for (int i = 0; i < outFlowId.size(); i++) 
        {
	   Equation flowTotal =new Equation(node, getID(), timestep, eqId++,Equation.EQUAL, (float) 0);
	   //var = new Variable((ID) outFlowId.get(i),timestep, getID(), 1, (float) -1);
           var = new Variable( (ID)outFlowId.elementAt(i),timestep,getID(),1,(float)-1.0);
           flowTotal.addVariable(var);
           var = new Variable( (ID)outFlowId.elementAt(i),timestep,getID(),1,(float)1.0);
           totalEq.addVariable(var);
	   //var = new Variable((ID) outFlowId.get(i),timestep, (float) 1);
            var = new Variable( (ID)outFlowId.elementAt(i),timestep,(float)1.0);
	    flowTotal.addVariable(var);
	    control.add(flowTotal);
	}
    control.add(totalEq);*/
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
          addEquationOfTimestep(control, i+1, index, node, toFlows, fromFlows, maxTimesteps);
      }

      return control;
  }


  /**
   * Help procedure used in parseData. It parses the coefficient vectors
   * @param tag
   * @param coeffvect
   * @param errormessage
   */
  /*
   * Changed pum2007
   */
  private void parseCoeff(LinkedList data, String tag, Vector coeffvect, String errormessage)
      throws RmdParseException
  {
	  for (int j = 0; data.size() > j &&
      !( (String) data.get(j)).equals("T"); j++) {
   try {
     if ( ( (String) data.get(j)).equals(tag)) {
       data.remove(j);
                 coeffvect.addElement(new Double(Double.parseDouble((String) data.remove(j))));
                 j--;
            }
        }
        catch (NumberFormatException e) {
            throw new RmdParseException(errormessage);
        }
    }

  }

  /**
   * Help procedure used in parseData. It parses the flow vectors
   * @param tag
   * @param flowvect
   * @param errormessage
   */
  /*
   * Changed pum2007
   */
  private void parseFlows(LinkedList data, String tag, Vector flowvect,
                          String errormessage) throws RmdParseException {
    for (int j = 0; data.size() > j &&
         !( (String) data.get(j)).equals("T"); j++) {
      try {
        if ( ( (String) data.get(j)).equals(tag)) {
          data.remove(j);
          flowvect.addElement( (String) data.remove(j));
          j--;
        }
      }
      catch (NumberFormatException e) {
        throw new RmdParseException(errormessage);
      }
    }

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
          Vector flowIn = new Vector();
          Vector coeffInMin = new Vector();
          Vector coeffInMax = new Vector();
          Vector flowOut = new Vector();
          Vector coeffOutMin = new Vector();
          Vector coeffOutMax = new Vector();

          
          
          
          
          
          
          //add vectors to timestepinfo
          
          
          
          
          
          

          /*  In Flow storage parameters */
       while (data.size()>=1)
       {
    	   if (((String)data.getFirst()).equals("T"))
    		   break;
    	   else if( ((String)data.getFirst()).equals("flowid_in")) {
    		   parseFlows(data, "flowid_in", flowIn, "<flowid_in> tag contains invalid data");
    		   info.setInFlow(flowIn);
    	  }
    	   else if( ((String)data.getFirst()).equals("flowid_out")) {
    		  parseFlows(data, "flowid_out", flowOut, "<flowid_out> tag contains invalid data");
    		  info.setOutFlow(flowOut);
    	   }
    	   else if( ((String)data.getFirst()).equals("coeff_in_min")) {
    		  parseCoeff(data, "coeff_in_min", coeffInMin, "<coeff_in_min> tag contains invalid data");
    		  info.setMinCoeffIn(coeffInMin);
    	   }
    	   else if( ((String)data.getFirst()).equals("coeff_in_max")) {
    		  parseCoeff(data, "coeff_in_max", coeffInMax, "<coeff_in_max> tag contains invalid data");
    		  info.setMaxCoeffIn(coeffInMax);
    	   }
    	   else if( ((String)data.getFirst()).equals("coeff_out_min")) {
    		  parseCoeff(data, "coeff_out_min", coeffOutMin, "<coeff_out_min> tag contains invalid data");
    		  info.setMinCoeffOut(coeffOutMin);
    	   }
    	   else if( ((String)data.getFirst()).equals("coeff_out_max")) {
    		  parseCoeff(data, "coeff_out_max", coeffOutMax, "<coeff_out_max> tag contains invalid data");
    		  info.setMaxCoeffOut(coeffOutMax);
    	   }
    	   else if( ((String)data.getFirst()).equals("c_dblInEfficiency")){
              data.removeFirst();
              info.c_dblInEfficiency = Double.parseDouble((String)data.removeFirst());
          }
    	   else if( ((String)data.getFirst()).equals("c_dblTotalInFlowMin")){
              data.removeFirst();
              info.c_dblTotalInFlowMin = Double.parseDouble((String)data.removeFirst());
          }
    	   else if( ((String)data.getFirst()).equals("c_dblTotalInFlowMax")){
              data.removeFirst();
              info.c_dblTotalInFlowMax = Double.parseDouble((String)data.removeFirst());
          }
          /*  Out Flow storage parameters */
    	   else if( ((String)data.getFirst()).equals("c_dblOutEfficiency")){
              data.removeFirst();
              info.c_dblOutEfficiency = Double.parseDouble((String)data.removeFirst());
          }
    	   else if( ((String)data.getFirst()).equals("c_dblTotalOutFlowMin")){
              data.removeFirst();
              info.c_dblTotalOutFlowMin = Double.parseDouble((String)data.removeFirst());
          }
    	   else if( ((String)data.getFirst()).equals("c_dblTotalOutFlowMax")){
              data.removeFirst();
              info.c_dblTotalOutFlowMax = Double.parseDouble((String)data.removeFirst());
          }
    	   else if( ((String)data.getFirst()).equals("c_dblInitialStorage")){
              data.removeFirst();
              c_InitialStorage = Double.parseDouble((String)data.removeFirst());
          }
    	   else if( ((String)data.getFirst()).equals("c_dblFinalStorage")){
              data.removeFirst();
              c_FinalStorage = Double.parseDouble((String)data.removeFirst());
          }
    	   else if( ((String)data.getFirst()).equals("c_dblStorageEfficiency")){
              data.removeFirst();
              info.c_dblStorageEfficiency = Double.parseDouble((String)data.removeFirst());
          }
    	   else if( ((String)data.getFirst()).equals("c_dblMinStorage")){
              data.removeFirst();
              info.c_dblMinStorage = Double.parseDouble((String)data.removeFirst());
          }
    	   else if( ((String)data.getFirst()).equals("c_dblMaxStorage")){
              data.removeFirst();
              info.c_dblMaxStorage = Double.parseDouble((String)data.removeFirst());
          }
    	   else if( ((String)data.getFirst()).equals("c_intMaxStorageTime")){
              data.removeFirst();
              c_maxStorageTime = Integer.parseInt((String)data.removeFirst());
          }

       }
          c_timesteps.addElement(info);
      }

  }

/*
 * All the following protected methods below are used for timesteps.
 * These methods are abstract in NodeFunction and are called
 * from there.
 */
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