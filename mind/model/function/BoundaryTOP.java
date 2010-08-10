/*
 * Copyright 2002:
 * Urban Liljedahl <ul@sm.luth.se>
 *
 * Copyright 2007:
 * Per Fredriksson <perfr775@student.liu.se>
 * David Karlslätt <davka417@student.liu.se>
 * Tor Knutsson	<torkn754@student.liu.se>
 * Daniel Källming <danka053@student.liu.se>
 * Ted Palmgren <tedpa175@student.liu.se>
 * Freddie Pintar <frepi150@student.liu.se>
 * Mårten Thuren <marth852@student.liu.se>
 *
 * Copyright 2010:
 * Nawzad Mardan <nawzad.mardan@liu.se>
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

import java.util.Vector;
import java.util.LinkedList;

import mind.io.*;
import mind.model.*;

/**
 * The function Boundary limits flows.
 *
 * @author Urban Liljedahl
 * @author Freddie Pintar
 * @author Tor Knutsson
 * @version 2007-12-12
 */
public class BoundaryTOP
    extends NodeFunction
    implements Cloneable
{
    //variables
    private ID c_resource = null;
    private float c_minimum;
    private float c_maximum;
    private boolean c_isMinimum;
    private boolean c_isMaximum;
    // Added by Nawzad Mardan 080910
    private boolean c_radin;
    private boolean c_radout;
    // Added by Nawzad Mardan 20100125
    private Object [][] c_data ;
    private String c_currentTimestep = "TOP";
    private int c_numberOfRow = 0;
    private boolean usrsetZero=false;

    /**
     * Creates an empty function
     */
    public BoundaryTOP()
    {
	super(new ID(ID.FUNCTION), "BoundaryTOP", null);
    }

    /**
     * Creates a new copy of the function
     * @return A complete copy
     */
    public Object clone()
	throws CloneNotSupportedException
    {
	BoundaryTOP clone = (BoundaryTOP) super.clone();
	return clone;
    }



    /*******************************************************
     * Inherited functions overridden
     ******************************************************/

    /**
     * Returns optimizationinformation from BoundaryTOP
     * @param maxTimesteps The maximum number of timesteps in the model
     * @param node The ID for the node that generates the equations
     * @return Some equations that model the source's behaviour
     * @throws ModelException if it cannot optimize
     */
    public EquationControl getEquationControl(int maxTimesteps, ID node,
					      Vector toFlows, Vector fromFlows)
	throws ModelException
    {

	EquationControl control = new EquationControl();
	Vector foundFlows = new Vector(0);
    Equation lowerbound;
    Equation upperbound;
    Equation connection;
    int eqId =1;

	if (c_resource == null)
	    throw new ModelException("In BoundaryTop Function: Resource in boundary "
				     +getLabel()+" not specified.");
         // Check if any flow is selected
        // Added by Nawzad Mardan 080909
       if((toFlows.size() <= 0) && (fromFlows.size() <= 0))
        throw new ModelException("In BoundaryTop Function:No flow going in from or out to node (" +
				     node + ")\n has the same resource as " +
				     "the source function inside it.\n" +
				     "Can not optimize.");
        // Check what ingoing flows has the same resource as
	// this source functions
        // Added by Nawzad Mardan 080909
        // Default is generating equations for the inflow. 
	// Check what ingoing flows has the same resource as
	// this source functions
        if(c_radin)
        {
            if(toFlows.size()<=0)
            {
              throw new ModelException("In BoundaryTop Function: No flow going in from  " +
					 "Node( "+node+").\n\n"+
					 "Can not optimize.");  
            }
        else
        {       
        for (int i = 0; i < toFlows.size(); i++) 
            {
	    if (((Flow) toFlows.get(i)).getResource() == null)
		throw new ModelException("In BoundaryTop Function: Resource for Source\n function in " +
					 "Node ("+node+") not specified.\n\n"+
					 "Can not optimize.");
	    if (((Flow) toFlows.get(i)).getResource().equals(getResource())) 
                {
		foundFlows.addElement(toFlows.get(i));
		// add variable for flow
                }
              }//END FOR
        if (foundFlows.size() <= 0)
            {
	    throw new ModelException("In BoundaryTop Function: The Inflows to the \nNode (" + node+" ) are not contain selected recourse " +"\n"+
					 "Can not optimize.");
            }
         }// END ELSE
        }// END IF ingoing flows
        // Chek outgoing flows Added by Nawzad Mardan 080909
        else
        {
             if(fromFlows.size()<=0)
            {
              throw new ModelException("In BoundaryTop Function: No flow going out from  " +
					 "Node "+node+".\n"+
					 "Can not optimize.");  
            }
            for (int i = 0; i < fromFlows.size(); i++) {
	    if (((Flow) fromFlows.get(i)).getResource() == null)
		throw new ModelException("In BoundaryTop Function: Resource for Source function in \n" +
					 "Node ("+node+") not specified.\n\n"+
					 "Can not optimize.");
	    if (((Flow) fromFlows.get(i)).getResource().equals(getResource())) {
		foundFlows.addElement(fromFlows.get(i));
		// add variable for flow
	    }
	}
              if (foundFlows.size() <= 0)throw new ModelException("In BoundaryTop Function: The Outflows to the\nNode (" + node+" ) are not contain selected recourse " +"\n"+
					 "Can not optimize.");    
        }// END IF outgoing flows

if(c_currentTimestep.equals("TOP"))
    {
	//make an connection equation with these variables together with the variable
	//constructed from foundFlows + T0
    connection = new Equation(node, getID(),node.toString()+"BoundaryTopFun", 0, 0, Equation.EQUAL, (float)0);

	//make a boundary equation lower and/or upper bounds
    lowerbound =
	    new Equation(node, getID(),node.toString()+"BoundaryTopFun", 0, 1,
			 Equation.GREATEROREQUAL,
			 c_minimum);

	upperbound =
	    new Equation(node, getID(),node.toString()+"BoundaryTopFun", 0, 2,
			 Equation.LOWEROREQUAL,
			 c_maximum);

	for (int j = 0; j < foundFlows.size(); j++) {
	    Vector variables = new Vector(0);

	    //generate variable FjTi for each flow j
	    for (int i = 0; i < maxTimesteps ; i++) {
		//generate a variable FjT(i) for each timestep

		Variable var = new Variable(((Flow) foundFlows.get(j)).getID(),
					    i+1, (float) -1);
		variables.addElement(var);
	    }

	    //the BoundaryTOP variable for this flow
	    Variable aVar = new Variable(((Flow) foundFlows.get(j)).getID(),
					 0, (float) 1);
	    //add BoundaryTOP variable for this flow
	    connection.addVariable(aVar);
	    //add Boundary variables for this flow (one for each timestep)
	    for( int i = 0; i < maxTimesteps; i++){
		connection.addVariable((Variable)variables.elementAt(i));
	    }

	    //add boundary values to lower and/or upper bound
	    if (c_isMinimum) {//min
		lowerbound.addVariable(aVar);
	    }

	    if (c_isMaximum) {//max
		upperbound.addVariable(aVar);
	    }
	}
	//the connection equation is added here, once for the resource spanning
	//one or more flows and one or more timesteps
	control.add(connection);

	//add bounds lower and/or upper
	if (c_isMinimum) {//min
	    control.add(lowerbound);
	}

	if (c_isMaximum) {//max
	    control.add(upperbound);
	}// END IF The node's current timestep is TOP
   }
  // IF the current timestep is not Top
  else
    {
    int startTimestep = 0;
    int endTimestep =0;
    float minimumBound = 0;
    float maximumBound = 0;
    String sts = "";
    String ets = "";
    String minBound = "";
    String maxBound = "";

    for (int i = 0; i < c_data.length; i++)
        {
        sts= (String)c_data[i][0];
        if(!sts.equals(""))
            startTimestep = new Integer(sts).intValue();
        ets= (String)c_data[i][1];
        if(!ets.equals(""))
            endTimestep = new Integer(ets).intValue();
        minBound= (String)c_data[i][2];
        if(!minBound.equals(""))
            minimumBound = new Float(minBound).floatValue();
        maxBound= (String)c_data[i][3];
        if(!maxBound.equals(""))
            {
            maximumBound = new Float(maxBound).floatValue();
            // To distingish between default Zero and user setting to Zero
            if(maximumBound >= 0)
                usrsetZero = true;
            }


         upperbound = new Equation(node, getID(),node.toString()+"BoundaryTopFu", i+2, eqId++,Equation.LOWEROREQUAL, maximumBound);
         lowerbound = new Equation(node, getID(),node.toString()+"BoundaryTopFun", i+1, eqId++,Equation.GREATEROREQUAL,minimumBound);
         connection = new Equation(node, getID(),node.toString()+"BoundaryTopFun", i+1, eqId++, Equation.EQUAL, (float)0);
        //make a boundary equation lower and/or upper bounds
        for (int j = 0; j < foundFlows.size(); j++)
          {

          //generate variable FjTi for each flow j
          for (int k = startTimestep; k <= endTimestep ; k++)
            {
            //generate a variable FjT(i) for each timestep
            Variable var = new Variable(((Flow) foundFlows.get(j)).getID(),k, (float) -1);
            connection.addVariable(var);
            }
           }// END FOR NUMBER OF FLOWS
         //the BoundaryTOP variable for this flow
          Variable aVar = new Variable("Fsum"+((Flow) foundFlows.get(0)).getID(),eqId++, (float) 1);
          //add BoundaryTOP variable for this flow
          connection.addVariable(aVar);
          control.add(connection);

          //add boundary values to lower and/or upper bound
          if(minimumBound!=0) {//min
            lowerbound.addVariable(aVar);
            control.add(lowerbound);
            }

          //if(maximumBound >=0) {//max
          if(usrsetZero)
            {
            upperbound.addVariable(aVar);
            control.add(upperbound);
            usrsetZero = false;
            }
            //}
        }// END FOR LOOP DATA
    }// END ELSE
	return control;
    }

    /**
     * Returns a short overview of the current equations of the function
     * @return A short overview of the equation returned by the function
     */
    public String getOverview()
    {
	String result = "";
     	return result;
    }

    /**
     * parseData parses a linked list with data and initializes the class
     * with the values found in the linked list.
     * The function is used when loading a model or node from disk.
     *
     * @param data A linked list with data.
     * @param rc A control with all available resources.
     * @param createMissingResources If this is true then
     * if data contains resources not found in rc, these resources
     * should be created and added to rc.
     */
    public void parseData(LinkedList data, ResourceControl rc,
			  boolean createMissingResource)
        throws RmdParseException
    {
        c_timesteplevel = (Timesteplevel)data.removeLast();

        //Assumes the following optional data as pairs in any order:
        //label tag, label,
        //resource tag, resource,
        //is min tag, is min flag,
        //is max tag, is max flag,
        //min value tag, min value
        //max value tag, max value

        ID TmpRC = null;
        boolean newModel = false;

        while (data.size() > 0) {
            String tag   = (String)data.removeFirst();
            String value = "";
            if (data.size() > 0) value = (String)data.removeFirst();

            if (tag.equals("label")) setLabel(value);
            // Added by Nawzad Mardan 080910
            if (tag.equals("inflow")) 
            {
            newModel = true; 
            c_radin = value.equals("true") ? true : false;
            }

            // Added by Nawzad Mardan 080910
            if (tag.equals("outflow")) 
            {
            newModel = true; 
            c_radout = value.equals("true") ? true : false;
            }
         //This is for the old models which is not containing neither inflow radio button nor outflow radio button
            if(!newModel)
                c_radin = true;

            if (tag.equals("isMin")) c_isMinimum = value.equals("true") ? true : false;

            if (tag.equals("isMax")) c_isMaximum = value.equals("true") ? true : false;

            if (tag.equals("MinLim")) {
                try {
                    c_minimum = Float.parseFloat(value);
                } catch (NumberFormatException e) {
                    throw new RmdParseException("The 'min' field must be a float > 0");
                }
                if (c_minimum < 0) {
                    throw new RmdParseException("The 'min' field must be a float > 0");
                }
            }

            if (tag.equals("MaxLim")) {
                try {
                    c_maximum = Float.parseFloat(value);
                } catch (NumberFormatException e) {
                    throw new RmdParseException("The 'max' field must be a float > 0");
                }
                if (c_maximum < 0) {
                    throw new RmdParseException("The 'max' field must be a float > 0");
                }
            }

            if (tag.equals("resource.type")) {
                String resource = value;
                TmpRC = rc.getResourceID(resource);
                //Check if the resource exists
                if (TmpRC == null) {
                    if (createMissingResource) {
                        TmpRC = rc.addResource(resource,"",""); //we do not know the unit or prefix
                    } else {
                        throw new RmdParseException("The resource '" + resource + "' is not defined.");
                    }
                }
            }
            if (tag.equals("NumOfRow")) {
                try {
                    c_numberOfRow = Integer.parseInt(value);
                    c_currentTimestep = "Timesteps";

                } catch (NumberFormatException e) {
                    throw new RmdParseException("The number of table's row must be a integer > 0");
                }
                if (c_numberOfRow < 0) {
                    throw new RmdParseException("The number of table's row must be a integer > 0");
                }
            }
            if (tag.equals("tableData"))
               {
               String[] sVec;
               Object[][] tableData = { { "", "", "", ""},};
               boolean emptyStringflag = false;
               c_currentTimestep = "Timesteps";
               try
                 {
                 int sTmplength = value.length();
                 // Check if the last string is empty string
                 // When using split method if the last strings in the array string contain empty string the method
                 // cann't splited thats why you must chek it and see if you have an empty string'
                 char c = value.charAt(sTmplength-2);
                 if (c ==' ')
                     emptyStringflag = true;

                  String sTmp = (value.length() < 2) ? "" : value.substring(1, value.length() - 1); // sTmp = s without "[]"
                  sVec = sTmp.split(",");
                  int x = sVec.length;
                  int k = 0;
                  //timestepNum =Integer.parseInt(value)
                  //datalen = analysPeriod.intValue();
                  tableData = new Object[c_numberOfRow][4];
                  for (int i = 0; i < c_numberOfRow; i++)
                     {
                     for(int j = 0; j< 4; j++)
                       {
                       if(sVec[k].equals(" "))
                           tableData[i][j]= "";
                       else
                            tableData[i][j]= sVec[k];
                       k++;
                       if(k == x)
                          break;
                       }
                     }
                  if(emptyStringflag)
                     {
                     tableData[c_numberOfRow-1][3]= "";
                     }
                  // Chek it again and see if the data array have not initiated all cells
                  for (int i = 0; i < c_numberOfRow; i++)
                     {
                     for(int j = 0; j< 4; j++)
                        {
                        if (tableData[i][j]==null)
                           tableData[i][j]="";
                        }
                     }
                  c_data = tableData;
                  if(c_data.length == 1)
                  {
                      c_data = tableData;
                  }

                 }// END TRY

               catch (NumberFormatException e)
                 {
                 throw new RmdParseException("The 'Data' field must be a float > 0");
                 }

               }// END IF TAG VECTOR
        }
        c_resource = TmpRC;
    }



    public String toXML(ResourceControl resources, int indent)
    {
        String sInd  = XML.indent(indent);
        String sInd1 = XML.indent(indent+1);
        String xml   = sInd + "<boundaryTOP>" + XML.nl();

        if (getLabel() != null) {
            xml += sInd1 + "<label>" + XML.toXML(getLabel()) + "</label>" + XML.nl();
        }
        // Added by Nawzad Mardan 080910
        xml += sInd1 + "<inflow>"  + (c_radin ? "true" : "false") + "</inflow>"  + XML.nl();
        // Added by Nawzad Mardan 080910
        xml += sInd1 + "<outflow>"  + (c_radout ? "true" : "false") + "</outflow>"  + XML.nl();

        if (c_resource != null) {
            xml += sInd1 + "<resource.type>" + XML.toXML(resources.getLabel(c_resource));
            xml += "</resource.type>" + XML.nl();
        }
        if(c_currentTimestep.equals("TOP"))
            {
            xml += sInd1 + "<isMin>"  + (c_isMinimum ? "true" : "false") + "</isMin>"  + XML.nl();
            xml += sInd1 + "<isMax>"  + (c_isMaximum ? "true" : "false") + "</isMax>"  + XML.nl();
            xml += sInd1 + "<MinLim>" + Float.toString(c_minimum)        + "</MinLim>" + XML.nl();
            xml += sInd1 + "<MaxLim>" + Float.toString(c_maximum)        + "</MaxLim>" + XML.nl();
            }
        else
            {
            if(c_data!=null)
             {
             xml += sInd1 + "<NumOfRow>" + c_data.length        + "</NumOfRow>" + XML.nl();

             String str = "[";
             for (int i = 0; i < c_data.length; i++)
               {
               for(int j= 0; j< 4; j++)
                {
                str = str+ c_data[i][j]+",";
                }
               }
             String sTmp = (str.length() < 2) ? "" : str.substring(0, str.length() - 1);// sTmp = str without last ","

             // Check if the last data is an empty string
             if(c_data[c_data.length-1][3].equals(""))
                sTmp = sTmp+" ]";
             else
                sTmp = sTmp+"]";
             xml += sInd1 + "<tableData>"  + sTmp  + "</tableData>" + XML.nl();

             }//END IF Data ! = null
            }//END ELSE


        xml += sInd + "</boundaryTOP>" + XML.nl();

        return xml;
    }
    
	/**
	 * Puts EXML data in the given ExmlSheet
	 * PUM5 Added 2007-12-12
	 * @param resources A ResourceControl
	 * @param sheet The ExmlSheet to be changed 
	 */
    public void toEXML(ResourceControl resources,ExmlSheet sheet) 
	{
		//Find Label
		String label = ((this.getLabel()==null)?"":this.getLabel());

		//Add function header
		sheet.addFunctionHeader("BoundaryTOP", label);
                // Added by Nawzad Mardan 080910
                if(c_radin)
                sheet.addRow(sheet.addLockedCell("Inflows")+sheet.addCell((new Boolean(c_radin))));
                if(c_radout)
		sheet.addRow(sheet.addLockedCell("Outflows")+sheet.addCell((new Boolean(c_radout))));
		
		//Add Resource description
		String resource = ((c_resource==null)?"":resources.getLabel(c_resource));
		sheet.addRow(sheet.addLockedCell("Resource")+sheet.addCell(XML.toXML(resource)));
		
		//Add data
        if(c_currentTimestep.equals("TOP"))
            {
            sheet.addRow(sheet.addLockedCell("Min")+sheet.addCell(c_minimum));
            sheet.addRow(sheet.addLockedCell("Max")+sheet.addCell(c_maximum));
            sheet.addRow(sheet.addCell(""));
            }
        else
        {
        sheet.addRow(sheet.addLockedCell("NumberOfRow")+sheet.addCell(c_numberOfRow));
        sheet.addRow(sheet.addLockedCell("StartTimestep")+sheet.addLockedCell("EndTimestep")+sheet.addLockedCell("Minimum")+sheet.addLockedCell("Maximum"));
         //sheet.addRow(sheet.addCell(""));
       String cellData;
       for (int i = 0; i < c_data.length; i++) {
				cellData = "";
				for (int j = 0; j < c_data[i].length; j++) {
					String cellContent = (String) c_data[i][j];
					cellData += sheet.addCell(((cellContent == null)?"":cellContent));
				}
				sheet.addRow(cellData);
			}
       sheet.addRow(sheet.addLockedCell("EndTableData"));
        }

    }

    public ID getResource(){
	return c_resource;
    }
    /**
     * Set the BoundaryTOP resource.
     * @param resource The new resource for this Boundary.
     */
    public void setResource(ID resource){
	c_resource = resource;
    }
    /**
     * Set method for c_minimum
     * @param value to set
     */
    public void setMinimum(float value){
	c_minimum = value;
    }

    /**
     * Set method for c_maximum
     * @param value to set
     */
    public void setMaximum(float value){
	c_maximum = value;
    }

    /**
     * Set method for c_isMinimum
     * @param value to set
     */
    public void setIsMinimum(boolean value){
	c_isMinimum = value;
    }

    /**
     * Set method for c_isMaximum
     * @param value to set
     */
    public void setIsMaximum(boolean value){
	c_isMaximum = value;
    }

    /**
     * Get method for c_minimum
     * @return c_minimum
     */
    public float getMinimum(){
	return c_minimum;
    }

    /**
     * Get method for c_maximum
     * @return c_maximum
     */
    public float getMaximum(){
	return c_maximum;
    }

    /**
     * Get method for c_isMinimum
     * @return c_isMinimum
     */
    public boolean getIsMinimum(){
	return c_isMinimum;
    }

    /**
     * Get method for c_isMaximum
     * @return c_isMaximum
     */
    public boolean getIsMaximum(){
	return c_isMaximum;
    }


    /*
     * All these procted methods below are used for timesteps.
     * These methods are abstract in NodeFunction and are called
     * from there.
     */

    protected int getTimesteps()
    {
	return 1;
    }

    protected void timestepInsertAt(int index)
    {
    }

    protected void timestepRemoveAt(int index)
    {
    }

    protected void timestepSetMoreDetailed(int factor)
    {
    }

    protected void timestepSetLessDetailed(int newSize, int factor)
    {
    }

    protected void timestepResetData(int size)
    {
    }

  public boolean isRelatedToFlow(ID flow) {
    return false;
  }
  
  // Added by Nawzad Mardan 080910
    /**
     * Gets the c_radout value.
     * @return The c_radout values as a true or false.
     */
    public boolean isRadOut()
    {
        return c_radout;
    }
    
    /**
     * Gets the c_radin value.
     * @return The c_radin values as a true or false.
     */
    public boolean isRadIn()
    {
        return c_radin;
    }
    
    /**
     * Sets the variable's b to the c_radin
     * @param  b The  c_radin radio button to be set
     */
    public void setRadIn(boolean b)
    {
        c_radin = b;
    }
    
    /**
     * Sets the variable's b to the c_radout
     * @param  b The  c_radout radio button to be set
     */
    public void setRadOut(boolean b)
    {
        c_radout = b;
    }
    // Added by Nawzad 2010-01-25
    /**
     * Gets the table data.
     * @return The table's data as a two dimension array of object.
     */

    public Object [][]getTableData()
    {
        return c_data;
    }

 // Added by Nawzad 2010-01-25
    /**
     * Sets the variable's data to the table's data
     * @param  data The data to be set
     */

    public void setTableData(Object [][] data)
    {
        c_data = data;
    }
// Added by Nawzad 2010-01-25
      /**
     * Gets the table data. If the tabel have only one row
     * @return The table data's upper bound.
     */

    public String getTabelsMaximum()
    {
        if(c_data == null)
            return "";
        else
          {
           String max =  (String)c_data[0][3];
           if(!max.equals(""))
           return max;
           else
               return "0.0";
        }
    }

    // Added by Nawzad 2010-01-25
      /**
     * Gets the table data. If the tabel have only one row
     * @return The table data's lower bound.
     */
    public String getTabelsMinimum()
    {
    if(c_data == null)
       return "";
    else
       {
       String min =  (String)c_data[0][2];
       if(!min.equals(""))
            return min;
       else
            return "0.0";
       }
    }

    // Added by Nawzad 2010-01-25
    /**
     * Gets the current Timestep for the Nod.
     * @return The Nod's current Timestep.
     */

    public String getCurrentTimestep()
    {
        return c_currentTimestep;
    }

 // Added by Nawzad 2010-01-25
    /**
     * Sets the variable's cts to the Nod's current Timestep
     * @param  cts The cts to be set
     */

    public void setCurrentTimestep(String cts)
    {
        c_currentTimestep = cts;
    }
}
