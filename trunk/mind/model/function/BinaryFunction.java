/*
 * Copyright 2003:
 * Jonas S��v <js@acesimulation.com>
 *
 * Copyright 2007:
 * Per Fredriksson <perfr775@student.liu.se>
 * David Karlsl�tt <davka417@student.liu.se>
 * Tor Knutsson	<torkn754@student.liu.se>
 * Daniel K�llming <danka053@student.liu.se>
 * Ted Palmgren <tedpa175@student.liu.se>
 * Freddie Pintar <frepi150@student.liu.se>
 * M�rten Thur�n <marth852@student.liu.se> 
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
import mind.gui.GUI;
import mind.gui.UserSettingConstants;

import java.util.*;


/**
 * The function class BinaryFunction
 * (is now called Logical Equation instead)
 * @author Jonas S��v
 * @author Freddie Pintar
 * @author Tor Knutsson
 * @version 2007-12-12
 */

public class BinaryFunction
    extends NodeFunction
    implements Cloneable, UserSettingConstants {

  private Vector c_timesteps = null;
  private float c_infinity;

  public BinaryFunction() {
    super(new ID(ID.FUNCTION), "LogicalEquation", null);
    c_timesteps = new Vector();
    TimestepInfo info = new TimestepInfo();
    c_timesteps.add(info);

    /* get the infinity definition from the ini-file */
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

  }


/**
 * set the flow id's, called by StorageDialog
 *
 * @param f the flow id vector
 * @param min  lower boundary vector
 * @param max  upper boundary vector
 * @param coeff integer coefficient vector
 */
public void setTimestepInfo(Vector f, Vector min, Vector max, Vector coeff,
                            int RHSValue, String RHSConstraint)
{
    TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( getTimestep() - 1 );

    info.setFlow(f);
    info.setMin(min);
    info.setMax(max);
    info.setCoeff(coeff);
    info.setRHSValue(RHSValue);
    info.setRHSConstraint(RHSConstraint);

    //print(info.getInFlow());
}

/**
 * Get the flows and coefficients from this timestep info
 */
public Vector getFlow() {
  TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(getTimestep() - 1);
  return info.getFlow();
}

public Vector getMin() {
  TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(getTimestep() - 1);
  return info.getMin();
}

public Vector getMax() {
  TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(getTimestep() - 1);
  return info.getMax();
}

public Vector getCoeff() {
  TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(getTimestep() - 1);
  return info.getCoeff();
}

public int getRHSValue() {
  TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(getTimestep() - 1);
  return info.c_RHSValue;
}

/*public void setRHSValue(int value) {
  c_RHSValue = value;
} */

public String getRHSConstraint() {
  TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(getTimestep() - 1);
  return info.c_RHSConstraint;
}

/*public void setRHSConstraint(String con) {
  c_RHSConstraint = con;
} */

/**
 * Inner class representing the lhs flows with coefficients and other
 * storage parameters
 */
class TimestepInfo{
    private Vector c_flow = new Vector(0);
    private Vector c_min = new Vector(0);
    private Vector c_max = new Vector(0);
    private Vector c_coeff = new Vector(0);

    private int c_RHSValue = 0;
    private String c_RHSConstraint = "E"; // Can be  "G" and "L" and "E"


    TimestepInfo(){
    }

    // in flow
    Vector getFlow(){
        return c_flow;
    }
    // min
    Vector getMin(){
        return c_min;
    }
    // max
    Vector getMax(){
        return c_max;
    }

    // coefficients
    Vector getCoeff() {
      return c_coeff;
    }

    // in flow
    void setFlow(Vector f){
        c_flow = f;
    }
    // min
    void setMin(Vector c){
        c_min = c;
    }
    // max
    void setMax(Vector c){
        c_max = c;
    }

    // coefficients
    void setCoeff(Vector c) {
      c_coeff = c;
    }

    // RHS Constraint and Value
    void setRHSValue(int value) {
      c_RHSValue = value;
    }

    void setRHSConstraint(String constraint) {
      c_RHSConstraint = constraint;
    }

    int getRHSValue() {
      return c_RHSValue;
    }

    String getRHSConstraint() {
      return c_RHSConstraint;
    }

/**
 * Create the timestep information into xml
 */
public String toXML(int indent){
    String xml = "";

    for( int index = 0; index < c_flow.size(); index++ ){
        xml = xml + XML.indent( indent ) + "<flowid>" +
            c_flow.elementAt(index).toString() + "</flowid>" + XML.nl();
    }
    for( int index = 0; index < c_flow.size(); index++ ){
        xml = xml + XML.indent( indent ) + "<c_min>" +
            ((Double)(c_min.elementAt(index))).doubleValue() + "</c_min>" + XML.nl();
    }
    for( int index = 0; index < c_flow.size(); index++ ){
        xml = xml + XML.indent( indent ) + "<c_max>" +
            ((Double)(c_max.elementAt(index))).doubleValue() + "</c_max>" + XML.nl();
    }
    for( int index = 0; index < c_flow.size(); index++ ){
        xml = xml + XML.indent( indent ) + "<c_coeff>" +
            ((Integer)(c_coeff.elementAt(index))).intValue() + "</c_coeff>" + XML.nl();
    }

    xml = xml + XML.indent(indent) + "<c_RHSValue>" + c_RHSValue + "</c_RHSValue>" + XML.nl();
    xml = xml + XML.indent(indent) + "<c_RHSConstraint>" + c_RHSConstraint + "</c_RHSConstraint>" + XML.nl();

    return xml;
}
/**
* Puts EXML data in the given ExmlSheet
* PUM5 Added 2007-12-12
* @param sheet The ExmlSheet to be changed 
*/
public ExmlSheet toEXML(ExmlSheet sheet)
{
    sheet.addTableValue("Number",((Integer)c_RHSValue).toString());
    sheet.addTableValue("String",XML.toXML(c_RHSConstraint));	
    for( int index = 0; index < c_flow.size(); index++ ){
    	
    	sheet.addTableValue("String",XML.toXML(c_flow.elementAt(index).toString() ));
    	sheet.addTableValue("Number",  XML.toXML(((Double)(c_min.elementAt(index))).toString()  ));
    	sheet.addTableValue("Number", XML.toXML(((Double)(c_max.elementAt(index))).toString()   ));
    	sheet.addTableValue("Number",  XML.toXML(((Integer)(c_coeff.elementAt(index))).toString()  ));
    }
    
    return sheet;
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
    for( int i = 0; i < this.getFlow().size(); i++ ){
        temp.addElement( (this.getFlow().elementAt(i)));
    }
    copy.setFlow( temp );

    // Flow min boundary
    temp = new Vector();
    for( int i = 0; i < this.getMin().size(); i++ ){
        temp.addElement( new Double(((Double)(this.getMin().elementAt(i))).doubleValue()));
    }
    copy.setMin( temp );

    // Flow max boundary
    temp = new Vector();
    for( int i = 0; i < this.getMax().size(); i++ ){
        temp.addElement( new Double(((Double)(this.getMax().elementAt(i))).doubleValue()));
    }
    copy.setMax( temp );

    // Binary variable coefficient
    temp = new Vector();
    for( int i = 0; i < this.getCoeff().size(); i++ ){
        temp.addElement( new Integer(((Integer)(this.getCoeff().elementAt(i))).intValue()));
    }
    copy.setCoeff( temp );

    copy.setRHSConstraint(this.getRHSConstraint());
    copy.setRHSValue(this.getRHSValue());

    return copy;
}

}

public String toXML(ResourceControl resources, int indent)
{
    String xml = XML.indent(indent) + "<logicalEquation>" + XML.nl();
    //save label
    if( getLabel() != null ){
        xml = xml + XML.indent(indent + 1) + "<label>" + getLabel()
            + "</label>" + XML.nl();
    }
    //support for multiple timesteps
    int numberOfTimesteps = getTimesteps();
    for( int i = 0; i < numberOfTimesteps; i++){
        TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( i );
        xml = xml + XML.indent( indent + 1) + "<timestep.logicalEquation nr=\"" + (i + 1) + "\">" + XML.nl();
        xml = xml + info.toXML( indent + 2);
        xml = xml + XML.indent( indent + 1) + "</timestep.logicalEquation>" + XML.nl();
    }
    xml = xml + XML.indent(indent) + "</logicalEquation>" + XML.nl();
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
	sheet.addFunctionHeader("LogicalEquation", label);
	
	// Add timestep nrs in one Row.
	int numberOfTimesteps = getTimesteps();
	sheet.addTimeStepRow(numberOfTimesteps);

	// Determine maximum number of flows to initialize table
	int maxFlow = 0;
	for( int i = 0; i < numberOfTimesteps; i++){		
		TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( i );
		maxFlow = (info.getFlow().size() > maxFlow)?info.getFlow().size():maxFlow;
	}
	
	//Add Timestep Data Names
	sheet.initTable(maxFlow*4+2, numberOfTimesteps);
	sheet.addLockedTableValue("RHSValue");
	sheet.addLockedTableValue("RHSConstraint");
	for(int i = 0;i < maxFlow; i++) {
		sheet.addLockedTableValue("Flow");
		sheet.addLockedTableValue("Min");
		sheet.addLockedTableValue("Max");
		sheet.addLockedTableValue("Coefficient");
	}
	
	//Add Timestep Data.
	for( int i = 0; i < numberOfTimesteps; i++){
		TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( i );
		info.toEXML(sheet);	
	    if (info.c_flow.size() < maxFlow)
	    	sheet.fillTableToNextCol();
	}
	sheet.endTable();
	sheet.addRow(sheet.addCell("String", ""));

}


  /*
   * Adds equations to the control.
   */
  public void addEquationOfTimestep(EquationControl control,
                                    int timestep, int index, ID node,
                                    int maxTimesteps) {


    Variable BiFnTi;
    Variable FnTi;
    Equation binEq, lowerBoundaryEq, upperBoundaryEq;
    int eqId = 1;
    double max, min;
    int coeff;

    //get the current TimestepInfo using index
    TimestepInfo info = (TimestepInfo) c_timesteps.elementAt(index);
//get all selected incoming flows
    Vector flowId = info.getFlow();
    int size = flowId.size();

    /* Main binary equation
     BiF1T1 + BiF2T1+� +BiFmT1 = 1	(1)
     BiF1T2 + BiF2T2+� +BiFmT2 = 1
     .
     .
     BiF1Ti + BiF2Ti+� +BiFmTi) = 1
     */

    binEq = new Equation(node, getID() /* This function ID */,
                                        timestep, eqId++ /*Equation number*/,
                                        info.getRHSConstraint(), info.getRHSValue());


    /*
     Lower and upper boundary equations:

     F1T1min* BiF1T1 <= F1T1 <= F1T1max* BiF1T1
     F2T1min* BiF2T1 <= F2T1 <= F2T1max* BiF2T1
     .
     .
     FnT1min* BiFnT1 <= FnT1 <= FnT1max* BiFnT1
     */
    upperBoundaryEq = new Equation(node, getID(), timestep, eqId++ /* Equation number */,
                                   Equation.LOWEROREQUAL);

    // f�r varje fl�de
       // l�gg till en BiFiT1 i binEq
       // l�gg till en lowerBoundaryEq
       // l�gg till en upperBoundaryEq

       for (int i = 0; i < size; i++) {
         // l�gg till en BiFiT1 i binEq
         coeff = ( (Integer) (info.getCoeff().elementAt(i))).intValue();
        // BiFnTi = new Variable("Bi" + getID().toString() + flowId.elementAt(i).toString(),
          //                     timestep, coeff);
          BiFnTi = new Variable("Bi"  + flowId.elementAt(i).toString(),timestep, coeff);

         BiFnTi.setIsInteger(true);
         binEq.addVariable(BiFnTi);

         // l�gg till en upperBoundaryEq
         max = ((Double)info.getMax().elementAt(i)).doubleValue();
         if (max < 0)
           max = c_infinity; // maximum value that can be represented with a float
         upperBoundaryEq = new Equation(node, getID(), timestep, eqId++ /* Equation number */,
                                        Equation.LOWEROREQUAL);
         //BiFnTi = new Variable("Bi" + getID().toString() + flowId.elementAt(i).toString(),
           //                    timestep, (float) -max);
         BiFnTi = new Variable("Bi" + flowId.elementAt(i).toString(),timestep, (float) -max);

         BiFnTi.setIsInteger(true);
         FnTi = new Variable( flowId.elementAt(i).toString(), timestep,1);
         upperBoundaryEq.addVariable(BiFnTi);
         upperBoundaryEq.addVariable(FnTi);
         control.add(upperBoundaryEq);

         // l�gg till en lowerBoundaryEq
         min = ((Double)info.getMin().elementAt(i)).doubleValue();
         if (min > 0) {
           lowerBoundaryEq = new Equation(node, getID(), timestep,
                                          eqId++ /* Equation number */,
                                          Equation.GREATEROREQUAL);
           //BiFnTi = new Variable("Bi" + getID().toString() + flowId.elementAt(i).toString(),
             //                    timestep, (float) - min);
           BiFnTi = new Variable("Bi" + flowId.elementAt(i).toString(),timestep, (float) - min);
         
           BiFnTi.setIsInteger(true);
           // FnTi = new Variable( (ID) flowId.elementAt(i), timestep,-1); already created
           lowerBoundaryEq.addVariable(BiFnTi);
           lowerBoundaryEq.addVariable(FnTi);
           control.add(lowerBoundaryEq);
         }

       }

       control.add(binEq);
  }

  /**
   * Returns optimizationinformation from binaryFunction
   * @param maxTimesteps The maximum number of timesteps in the model
   * @param node The ID for the node that generates the equations
   * @param toFlows Inflow to this node. Not used for binaryFunction
   * @param fromFlows Outflow from this node. Not used for binaryFunction
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
          addEquationOfTimestep(control, i+1, index, node, maxTimesteps);
      }

      return control;
  }

  /**
   * Get the ID of a flow expressed as a string.
   * @param stringID the ID of a flow as a string
   * @return the ID as an ID-object
   */
  private ID parseId(String stringID) {
    Flow[] allFlows = GUI.getInstance().getAllFlows();
    for (int i = 0; i < allFlows.length; i++) {
      if (stringID.equals(allFlows[i].toString()))
        return allFlows[i].getID(); //should only by one with this ID
    }
    return null;
  }


  /**
   * Help procedure used in parseData. It parses the min/max boundary vectors
   * @param tag
   * @param coeffvect
   * @param errormessage
   */
  /*
   * Changed pum2007
   */
  private void parseMinMax(LinkedList data, String tag, Vector coeffvect, String errormessage)
  throws RmdParseException
  {
	  for (int j = 0; data.size() > j&& !((String)data.get(j)).equals("T"); j++) {
		  try {
			  if ( ( (String) data.get(j)).equals(tag)) {
				  data.remove(j);
				  coeffvect.addElement(new Double(Double.parseDouble((String) data.remove(j))));
				  j=j-1;
			  }
		  }
        catch (NumberFormatException e) {
            throw new RmdParseException(errormessage);
        }
    }

  }

  /**
 * Help procedure used in parseData. It parses the coefficient vector (integers)
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
	for (int j = 0; data.size() > j&& !((String)data.get(j)).equals("T"); j++) {
	      try {
	        if ( ( (String) data.get(j)).equals(tag)) {
	          data.remove(j);
	                  coeffvect.addElement(new Integer(Integer.parseInt((String) data.remove(j))));
	                  j=j-1;
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
    for (int j = 0; data.size() > j&& !((String)data.get(j)).equals("T"); j++) {
      try {
        if ( ( (String) data.get(j)).equals(tag)) {
          data.remove(j);
          flowvect.addElement( (String) data.remove(j));
          j=j-1;
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
          Vector flow = new Vector();
          Vector Min = new Vector();
          Vector Max = new Vector();
          Vector Coeff = new Vector();

          parseFlows(data, "flowid", flow, "<flowid> tag contains invalid data");
          parseMinMax(data, "c_min", Min, "<c_min> tag contains invalid data");
          parseMinMax(data, "c_max", Max, "<c_max> tag contains invalid data");
          parseCoeff(data, "c_coeff", Coeff, "<c_coeff> tag contains invalid data");

          //add vectors to timestepinfo
          info.setFlow(flow);
          info.setMin(Min);
          info.setMax(Max);
          info.setCoeff(Coeff);

          /*  RHS Value */
          if( ((String)data.getFirst()).equals("c_RHSValue")){
              data.removeFirst();
              info.setRHSValue(Integer.parseInt((String)data.removeFirst()));
          }
          // RHS Constraint
            if ( ( (String) data.getFirst()).equals("c_RHSConstraint")) {
              data.removeFirst();
              info.setRHSConstraint((String) data.removeFirst());
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
        Vector flowid = info.getFlow();

        /* find flow.toString() in inflowid and outflowid
           the elements in inflowid and outflowid might be of the type String or the type ID
           which is realllly stupid   */
        flowIterator = flowid.iterator();
        while (flowIterator.hasNext()) {
          if (theFlow.toString().equals(flowIterator.next().toString())) {
            isRelated = true;
            break; // no need to search anymore
          } // end if
        } // end while
      } // end while

      return isRelated;
    }
}
