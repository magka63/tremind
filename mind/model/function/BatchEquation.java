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
import mind.gui.UserSettingConstants;
import mind.gui.GUI;

import java.util.*;

/**
 * The function class BatchEquation
 *
 * @author Jonas S‰‰v
 * @author Freddie Pintar
 * @author Tor Knutsson
 * @version 2007-12-12
 */

public class BatchEquation
    extends NodeFunction
    implements Cloneable, UserSettingConstants {

  private Vector c_timesteps = null;
  private double c_dblMinBatch = .0;
  private double c_dblMaxBatch = -1;
  private int c_intBatchTime = 0;
  private int c_intAdjustingTime = 0;
  private int c_intPredetIntervals = 0;
  private float c_infinity;

  public BatchEquation() {
    super(new ID(ID.FUNCTION), "BatchEquation", null);
    c_timesteps = new Vector();
    TimestepInfo info = new TimestepInfo();
    c_timesteps.add(info);

    /* Get the infinity definition from the ini-file "settings.ini"  */
    Ini inifile = new Ini();

    String inf = inifile.getProperty(MPS_INFINITY_DEFINITIION, "1E10");

    try {
      c_infinity = Float.parseFloat(inf);
    }
    catch (NumberFormatException ex) {
      GUI.getInstance().showMessageDialog("Invalid value in settings.ini. " +
                                          MPS_INFINITY_DEFINITIION + "= " +
                                          inf + "\n Using 1e10 as infinity");
      c_infinity = 1E10f;
    }

// System.out.println("BatchEquation constructor");
  }


/**
 * set the flow id's, called by BatchDialog
 * The only timestep related data for BatchDialog is the in/out flows
 *
 * @param f_in inflows vector
 * @param f_out outflows vector
 */
public void setTimestepInfo(Vector f_in, Vector f_out)
{
    TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( getTimestep() - 1 );

    info.setInFlow(f_in);
    info.setOutFlow(f_out);

    //print(info.getInFlow());
}

/**
 * Get/Set methods for function data (timestep related and not timestep related)
 */
public Vector getInFlow() {
  TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(getTimestep() - 1);
  return info.getInFlow();
}

public Vector getOutFlow() {
  TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(getTimestep() - 1);
  return info.getOutFlow();
}

public double getMinBatch() {
  return c_dblMinBatch;
}

public double getMaxBatch() {
  return c_dblMaxBatch;
}

public int getBatchTime() {
  return c_intBatchTime;
}

public int getAdjustingTime() {
  return c_intAdjustingTime;
}

public int getPredetIntervals() {
  return c_intPredetIntervals;
}

public void setMinBatch(double mb) {
  c_dblMinBatch = mb;
}

public void setMaxBatch(double mb) {
  c_dblMaxBatch = mb;
}

public void setBatchTime(int bt) {
  c_intBatchTime = bt;
}

public void setAdjustingTime(int at) {
  c_intAdjustingTime = at;
}

public void setPredetIntervals(int pi) {
  c_intPredetIntervals = pi;
}

public static enum TableType {INFLOW,OUTFLOW,CONSTRAINTS};

/**
 * Inner class representing the lhs flows with coefficients and other
 * storage parameters
 */
class TimestepInfo{
    private Vector c_inFlow = new Vector(0);
    private Vector c_outFlow = new Vector(0);

    TimestepInfo(){
    }

    // in flow
    Vector getInFlow(){
        return c_inFlow;
    }

    // out flow
    Vector getOutFlow(){
        return c_outFlow;
    }

    // in flow
    void setInFlow(Vector f){
        c_inFlow = f;
    }

    // out flow
    void setOutFlow(Vector f){
        c_outFlow = f;
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
    for( int index = 0; index < c_outFlow.size(); index++ ){
        xml = xml + XML.indent( indent ) + "<flowid_out>" +
            c_outFlow.elementAt(index).toString() + "</flowid_out>" + XML.nl();
    }

    xml = xml + XML.indent(indent) + "<c_dblMinBatch>" + c_dblMinBatch + "</c_dblMinBatch>" + XML.nl();
    xml = xml + XML.indent(indent) + "<c_dblMaxBatch>"  + c_dblMaxBatch + "</c_dblMaxBatch>" + XML.nl();
    xml = xml + XML.indent(indent) + "<c_intBatchTime>"  + c_intBatchTime + "</c_intBatchTime>" + XML.nl();
    xml = xml + XML.indent(indent) + "<c_intAdjustingTime>"  + c_intAdjustingTime + "</c_intAdjustingTime>" + XML.nl();
    xml = xml + XML.indent(indent) + "<c_intPredetIntervals>"  + c_intPredetIntervals + "</c_intPredetIntervals>" + XML.nl();

    return xml;
}

/**
* Puts EXML data in the given ExmlSheet
* PUM5 Added 2007-12-12
* @param sheet The ExmlSheet to be changed 
* @param tableType
*/
public void toEXML(ExmlSheet sheet, TableType tableType)
{
    
	/*
    for( int index = 0; index < c_inFlow.size(); index++ ){
    	
        xml = xml + XML.indent( indent ) + "<flowid_in>" +
            c_inFlow.elementAt(index).toString() + "</flowid_in>" + XML.nl();
    }
    for( int index = 0; index < c_outFlow.size(); index++ ){
        xml = xml + XML.indent( indent ) + "<flowid_out>" +
            c_outFlow.elementAt(index).toString() + "</flowid_out>" + XML.nl();
    }*/
		
		if (tableType == TableType.INFLOW) {
    	for( int index = 0; index < c_inFlow.size(); index++ ){
    		sheet.addTableValue(c_inFlow.elementAt(index).toString());
    	}
    }
    else if (tableType == TableType.OUTFLOW) {
    	for( int index = 0; index < c_outFlow.size(); index++ ){
    		sheet.addTableValue(c_outFlow.elementAt(index).toString());
    	}
    }
		else if (tableType == TableType.CONSTRAINTS) {
			sheet.addTableValue(c_dblMinBatch);
			sheet.addTableValue(c_dblMaxBatch);
			sheet.addTableValue(c_intBatchTime);
			sheet.addTableValue(c_intAdjustingTime);
			sheet.addTableValue(c_intPredetIntervals);
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


    //out flow
    temp = new Vector();
    for( int i = 0; i < this.getOutFlow().size(); i++ ){
        temp.addElement( (this.getOutFlow().elementAt(i)));
    }
    copy.setOutFlow( temp );

    return copy;
}

}

public String toXML(ResourceControl resources, int indent)
{
    String xml = XML.indent(indent) + "<batchEquation>" + XML.nl();
    //save label
    if( getLabel() != null ){
        xml = xml + XML.indent(indent + 1) + "<label>" + getLabel()
            + "</label>" + XML.nl();
    }
    //support for multiple timesteps
    int numberOfTimesteps = getTimesteps();
    for( int i = 0; i < numberOfTimesteps; i++){
        TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( i );
        xml = xml + XML.indent( indent + 1) + "<timestep.batchEquation nr=\"" + (i + 1) + "\">" + XML.nl();
        xml = xml + info.toXML( indent + 2);
        xml = xml + XML.indent( indent + 1) + "</timestep.batchEquation>" + XML.nl();
    }
    xml = xml + XML.indent(indent) + "</batchEquation>" + XML.nl();
    return xml;
}

public void toEXML(ResourceControl resources, ExmlSheet sheet)
{
	
	//find Label
	String label = ((this.getLabel()==null)?"":this.getLabel());
	
	//Add function header
	sheet.addFunctionHeader("BatchEquation", label);

	//Add constraint data
	sheet.initTable(5, 1);
	sheet.addLockedTableValue("Min");
	sheet.addLockedTableValue("Max");
	sheet.addLockedTableValue("BatchTime");
	sheet.addLockedTableValue("Adjusting Time");
	sheet.addLockedTableValue("Predetermined Interval");
	TimestepInfo cinfo = (TimestepInfo) c_timesteps.elementAt( 0 );
	cinfo.toEXML(sheet, TableType.CONSTRAINTS);		
	sheet.endTable();
	
	// Add timestep nrs in one Row.
	int numberOfTimesteps = getTimesteps();
	sheet.addTimeStepRow(numberOfTimesteps);
		
	//Add Timestep data
	// Calculate maximum rows for flows
	int inFlow = 0;
	int outFlow = 0;
	for( int i = 0; i < numberOfTimesteps; i++){		
		TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( i );
		inFlow = (info.getInFlow().size() > inFlow)?info.getInFlow().size():inFlow;
		outFlow = (info.getOutFlow().size() > outFlow)?info.getOutFlow().size():outFlow;
	}
	
	//Add Timestep data for inflows
	sheet.addRow(sheet.addCell("InFlows"));
	sheet.initTable(inFlow, numberOfTimesteps);
	for (int i = 0; i< inFlow;i++) {
		sheet.addLockedTableValue("Flow");
	}
	for( int i = 0; i < numberOfTimesteps; i++){
		TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( i );
		info.toEXML(sheet,TableType.INFLOW);
		if (info.c_inFlow.size() < inFlow)
			sheet.fillTableToNextCol();
	}
	sheet.endTable();
	
	//Add Timestep data for outflows
	
	sheet.addRow(sheet.addCell("OutFlows"));
	sheet.initTable(outFlow, numberOfTimesteps);
	for (int i = 0; i< outFlow;i++) {
		sheet.addLockedTableValue("Flow");	
	}
	for( int i = 0; i < numberOfTimesteps; i++){
		TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( i );
		info.toEXML(sheet,TableType.OUTFLOW);
		if (info.c_outFlow.size() < outFlow)
			sheet.fillTableToNextCol();
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

  /**
   * Digs deep into the timestep level hierarchy to find a length
   * @param timestep
   * @return
   */
  private float getTimestepLength(int timestep)
  {
    return ( (Float) getTimesteplevel().getBottomLevel().getLengthsVector().get(
        timestep - 1)).floatValue();
  }

  /**
   * Adds equations to the control.
   */
  private void addEquationOfTimestep(EquationControl control,
                                    int timestep, int index, ID node,
                                    Vector inFlows, Vector outFlows, int maxTimesteps)
     throws ModelException
 {

/*   NedanstÂende taget frÂn "specen":

     FmTi, FnTi, BaNjTitot, Ti = kontinuerliga variabler
     BaNjmin, BaNjmax = koefficienter
     BaNjTz = 0/1-variabel
     Ba = batch
     i = tidssteg
     j = nodnummer
     m = id nummer fˆr flˆde
     n = id nummer fˆr flˆde
     min = minimum
     max = maximum
     x = det tidssteg som batchen frÂn Tidssteg 1 l‰mnar Nod 1
     y = det tidssteg som batchen frÂn Tidssteg 2 l‰mnar Nod 1
     z = det tidssteg som batchen frÂn Tidssteg i l‰mnar Nod 1
     b = batchtid
     q = totalt antal tidssteg
     tot = total
 */

    //get the current TimestepInfo using index
    TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(index);
//get all selected incoming flows
    Vector inFlowId = info.getInFlow();
    int insize = inFlowId.size();
//get all selected outgoing flows
    Vector outFlowId = info.getOutFlow();
    int outsize = outFlowId.size();
    int eqId = 1; // Equation ID counter
    Variable var;  // temporary variable used for lots of things
    Equation eq;   // temporary equation used for lots of things

//prepare the inflowid and outflowid. If they still are instances of String,
//i.e. loaded from a scenario in rmd-format: convert into matching ID. Do nothing
//if they allready are in the preferred form ID
//System.out.println("string to id");
    stringToID(inFlowId, inFlows);
    stringToID(outFlowId, outFlows);

    /*
     Main Storage Equation. Generate only of c_intBatchTime is 1 timestep or
     more, and if timestep <= maxTimesteps - c_intBatchTime
     Example:
        T1*(F1T1+F2T1+Ö +FmT1) = (F3Tx+F4Tx+Ö +FnTx)*Tx     (mainBatchEq)
        T2*(F1T2+F2T2+Ö +FmT2) = (F3Ty+F4Ty+Ö +FnTy)*Ty
        .
        .
        .
        T(q-b)*(F1T(q-b)+F2T(q-b)+Ö +FmT(q-b)) = (F3Tq+F4Tq+Ö +FnTq)*Tq


     (F1T1+F2T1+Ö +FmT1)*T1<= BaN1T1*BaN1max             (mainBatchUpperBoundaryEq)
     (F1T2+F2T2+Ö +FmT2)*T2<= BaN1T2*BaN1max
     .
     .
     .
     (F1T(q-b)+F2T(q-b)+Ö +FmT(q-b))*T(q-b)<= BaN1T(q-b)*BaN1max

     (F1T1+F2T1+Ö +FmT1)*T1 >= BaN1T1*BaN1min          (mainBatchLowerBoundaryEq)
     (F1T2+F2T2+Ö +FmT2)*T2 >= BaN1T2*BaN1min
     .
     .
     .
     (F1T(q-b)+F2T(q-b)+Ö +FmT(q-b)) *T(q-b) >= BaN1T(q-b)*BaN1min
     */

    if ((c_intBatchTime) > 0 && (timestep <= maxTimesteps - c_intBatchTime))
    {
      /* create the main equation and the two boundary equations */
      Equation mainBatchEq = new Equation(node, getID() /* This function ID */,
                                          timestep, eqId++, Equation.EQUAL);

      Equation mainBatchUpperBoundaryEq=null, mainBatchLowerBoundaryEq=null;
      // if (c_dblMaxBatch > 0)  Always create this equation
        mainBatchUpperBoundaryEq = new Equation(node, getID(), timestep,
                                                eqId++, Equation.LOWEROREQUAL);
      if (c_dblMinBatch >0)
        mainBatchLowerBoundaryEq = new Equation(node, getID(), timestep,
                                                eqId++, Equation.GREATEROREQUAL);

        /* Inflow variables
           Example:   F1T1, F2T1, etc  */
        for (int i = 0; i < insize; i++) {
          var = new Variable( (ID) inFlowId.elementAt(i), timestep,
                              getTimestepLength(timestep));

          mainBatchEq.addVariable(var);
          mainBatchUpperBoundaryEq.addVariable(var);
          if (c_dblMinBatch > 0)
            mainBatchLowerBoundaryEq.addVariable(var);
        }

        /* Outflow variables
           Example: F3T4, F4T4, etc */
        for (int i = 0; i < outsize; i++) {
          var = new Variable( (ID) outFlowId.elementAt(i),
                             timestep + c_intBatchTime, -getTimestepLength(timestep + c_intBatchTime));

         checkTimestepLimits(timestep + c_intBatchTime, maxTimesteps, "timestep + c_intBatchTime");
         mainBatchEq.addVariable(var);
        }


        /* Integer batch variable for this time step
           Example:  BaFu23N1T1, BaFu23N1T2, etc     */

        double max;
        if (c_dblMaxBatch < 0) {
          max = c_infinity;  // max float value
        } else {
          max = getMaxBatch();
        }

        {
            Variable BaNjTi = new Variable("Ba" + getID().toString() +
                                           node.toString(),
                                           timestep, (float) - max);

            BaNjTi.setIsInteger(true);
            mainBatchUpperBoundaryEq.addVariable(BaNjTi);
            control.add(mainBatchUpperBoundaryEq);
          }

          if (c_dblMinBatch > 0) {
            Variable BaNjTi = new Variable("Ba" + getID().toString() +
                                           node.toString(),
                                           timestep, (float) -getMinBatch());
            BaNjTi.setIsInteger(true);
            mainBatchLowerBoundaryEq.addVariable(BaNjTi);
            control.add(mainBatchLowerBoundaryEq);
          }


        control.add(mainBatchEq);
    }

    if (c_intBatchTime > 0) {
      /*
      FmT(q-b+1) = 0
      FmT(q-b+2) = 0
      .
      .
      .
      FmT(q) = 0


      FnT(1) = 0
      FnT(2) = 0
      .
      .
      .
      FnT(b) = 0  */

      if (timestep <= c_intBatchTime) {
        /* Inflow variables
            Example:   F1T1, F2T1, etc  */
        for (int i = 0; i < outsize; i++) {
          eq = new Equation(node, getID(), timestep, eqId++, Equation.EQUAL, .0f);
          var = new Variable( (ID) outFlowId.elementAt(i), timestep, 1.0f);

          eq.addVariable(var);
          control.add(eq);
        } // end for
      } // end if

      if (timestep >= maxTimesteps-c_intBatchTime +1) {
        /* Outflow variables
            Example:   F3T1, F4T1, etc  */
        for (int i = 0; i < insize; i++) {
          eq = new Equation(node, getID(), timestep, eqId++, Equation.EQUAL, .0f);
          var = new Variable( (ID) inFlowId.elementAt(i), timestep, 1.0f);

          eq.addVariable(var);
          control.add(eq);
        } // end for
      } // end if

    } // end if (c_intBatchTime > 0)
    /* Added by Nawzad Mardan 080909
    // To avoid Sorce and Destination function usage when user use Batch function 
    // finally add that the total flow is the sum of every
	// interval
	// for InFlow
	for (int i = 0; i < inFlowId.size(); i++) 
        {
	    Equation flowTotal = new Equation(node, getID(), timestep, eqId++,Equation.EQUAL, (float) 0);
            //var = new Variable((ID) inFlowId.elementAt(i), timestep, 1.0f);
            var = new Variable((ID) inFlowId.get(i),timestep, getID(), 1, (float) -1);
            flowTotal.addVariable(var);
	    var = new Variable((ID) inFlowId.get(i),timestep, (float) 1);
	    flowTotal.addVariable(var);
	    control.add(flowTotal);
	}

	// for OutFlow
	for (int i = 0; i < outFlowId.size(); i++) 
        {
	   Equation flowTotal =new Equation(node, getID(), timestep, eqId++,Equation.EQUAL, (float) 0);
	   var = new Variable((ID) outFlowId.get(i),timestep, getID(), 1, (float) -1);
           flowTotal.addVariable(var);
	   var = new Variable((ID) outFlowId.get(i),timestep, (float) 1);
	    flowTotal.addVariable(var);
	    control.add(flowTotal);
	}*/
  } // end of method

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

      /* Integer variable equations are not timestep related so it is better
         to create them here instead of in addEquationOfTimestep  */
      Variable BaNjTi;
      Equation adjEq;

      // Preterermined batch intervals
      if ((c_intPredetIntervals == 1) && (c_intBatchTime > 0)) {

        Equation intEq;

        /* Integer variable relations
        BaN1T1 = 1

        BaN1T1+ BaN1T2+Ö +BaN1Tx = 1				(7)
        BaN1T2+ BaN1T3+Ö +BaN1Ty = 1
         .
         .
         .
        BaN1T(q-b-b+1)+ BaN1T(q-b-b+2)+Ö +BaN1T(q-b) = 1  */

        /* the equation BaN1T1 = 1 */
        intEq = new Equation(node, getID().toString() + "IntRel", 0, Equation.EQUAL);
        intEq.setRHS(1.0f);
        BaNjTi = new Variable("Ba" + getID().toString() + node.toString(), 1, 1.0f);
        BaNjTi.setIsInteger(true);
        intEq.addVariable(BaNjTi);
        control.add(intEq);

        for (int i=1; i <= maxTimesteps - 2*c_intBatchTime + 1 - c_intAdjustingTime; i++) {

          /* intEq name example:  N2T3Fu23IntRel  */
          intEq = new Equation(node, getID().toString() + "IntRel", i, Equation.EQUAL);
          intEq.setRHS(1.0f);

          for (int j=i; j <= i+c_intBatchTime-1+c_intAdjustingTime; j++) {
            BaNjTi = new Variable("Ba"+ getID().toString() + node.toString(), j, 1.0f);
            BaNjTi.setIsInteger(true);
            intEq.addVariable(BaNjTi);
            checkTimestepLimits(j, maxTimesteps, "j");
          }
          control.add(intEq);
        }

      }
      // Free batch intervals
      else
      {
        Equation intEq;

        /* Integer variable relations
        BaN1T1+ BaN1T2+Ö +BaN1Tx <= 1				(4)
        BaN1T2+ BaN1T3+Ö +BaN1Ty <= 1
         .
         .
         .
        BaN1T(q-b-b+1)+ BaN1T(q-b-b+2)+Ö +BaN1T(q-b) <= 1  */
        for (int i=1; i <= maxTimesteps - 2*c_intBatchTime + 1; i++) {

          /* intEq name example:  N2T3Fu23IntRel  */
          intEq = new Equation(node, getID().toString() + "IntRel", i, Equation.LOWEROREQUAL);
          intEq.setRHS(1.0f);

          for (int j=i; j <= i+c_intBatchTime-1; j++) {
            BaNjTi = new Variable("Ba"+ getID().toString() + node.toString(), j, 1.0f);
            BaNjTi.setIsInteger(true);
            intEq.addVariable(BaNjTi);
            checkTimestepLimits(j, maxTimesteps, "j");
          }
          control.add(intEq);
        }
        /*
        BaN1T1 + BaN1Tx <= 1						(5)
        BaN1T2 + BaN1Ty <= 1
         .
         .
         .
        BaN1T(q-b-b) + BaN1T(q-b) <= 1       */

        /* Adjusting time equations
           if adusting time is 3 we have to generate three groups of equations.
           This is controlled by the for int adj = 1 ... loop  */
        for (int adj=1; adj<=c_intAdjustingTime; adj++) {

          /* adjEq group */
          for (int tstep=1; tstep <= maxTimesteps -2*c_intBatchTime -adj+1;tstep++) {
            /* adjEq name example: N2T1Fu23IntAdj_1, N2T2Fu23IntAdj_1, N2T3Fu23IntAdj_1, etc  */
            adjEq = new Equation(node, getID().toString() + "IntAdj_"+adj, tstep, Equation.LOWEROREQUAL);
            adjEq.setRHS(1.0f);
            BaNjTi = new Variable("Ba" + getID().toString() + node.toString(),tstep,1.0f);
            adjEq.addVariable(BaNjTi);
            BaNjTi = new Variable("Ba" + getID().toString() + node.toString(),tstep + c_intBatchTime + adj -1,1.0f);
            adjEq.addVariable(BaNjTi);
            control.add(adjEq);

            checkTimestepLimits(tstep, maxTimesteps, "tstep");
            checkTimestepLimits(tstep + c_intBatchTime + adj -1, maxTimesteps,
                                "tstep + c_intBatchTime + adj -1");
          }
        }

      }


      return control;
  }

  /**
   * A check routine that throws a ModelException if a generic variable representing a
   * timestep should be less than zero or larger than the maximum number of available
   * time steps. Note that this check doesn't deal with logical errors. E.g. if there exists
   * 10 timesteps the variable BaN1T10 is concidered valid even if it is not logical for this
   * variable to exist.
   * @author Jonas S‰‰v
   * @param timestep the value that needs to be checked
   * @param maxTimesteps maximum number of timesteps
   * @param message the name of the value that needs to be checked, e.g. "timestep" or "step + a - b"
   * @throws ModelException
   */
  private void checkTimestepLimits(int timestep, int maxTimesteps, String message)
      throws ModelException
  {
    if ((timestep < 0) || (timestep > maxTimesteps)) {
      throw (new ModelException("Timestep out of range: " + message + "=" + timestep));
    }
  }


  /**
   * Help procedure used in parseData. It parses the coefficient vectors
   * @param tag
   * @param coeffvect
   * @param errormessage
   */
  private void parseCoeff(LinkedList data, String tag, Vector coeffvect, String errormessage)
      throws RmdParseException
  {
    for (int j = 0; data.size() > 0 &&
             ((String) data.getFirst()).equals(tag); j++) {

        try {
            if (((String) data.getFirst()).equals(tag)) {
                data.removeFirst();
                coeffvect.addElement(new Double(Double.parseDouble((String) data.removeFirst())));
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
          Vector flowOut = new Vector();

          parseFlows(data, "flowid_in", flowIn, "<flowid_in> tag contains invalid data");

          parseFlows(data, "flowid_out", flowOut, "<flowid_out> tag contains invalid data");

          //add vectors to timestepinfo
          info.setInFlow(flowIn);

          info.setOutFlow(flowOut);

          /*  Batch storage parameters */
          if( ((String)data.getFirst()).equals("c_dblMinBatch")){
              data.removeFirst();
              c_dblMinBatch = Double.parseDouble((String)data.removeFirst());
          }
          if( ((String)data.getFirst()).equals("c_dblMaxBatch")){
              data.removeFirst();
              c_dblMaxBatch = Double.parseDouble((String)data.removeFirst());
          }

          if( ((String)data.getFirst()).equals("c_intBatchTime")){
              data.removeFirst();
              c_intBatchTime = Integer.parseInt((String)data.removeFirst());
          }
          if( ((String)data.getFirst()).equals("c_intAdjustingTime")){
              data.removeFirst();
              c_intAdjustingTime = Integer.parseInt((String)data.removeFirst());
          }
          if( ((String)data.getFirst()).equals("c_intPredetIntervals")){
              data.removeFirst();
              c_intPredetIntervals = Integer.parseInt((String)data.removeFirst());
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
