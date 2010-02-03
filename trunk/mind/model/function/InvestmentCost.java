/*
 * Copyright 2003:
 * Almsted �sa <asaal288@student.liu.se>
 * Anliot Manne <manan699@student.liu.se>
 * Fredriksson Linus <linfr529@student.liu.se>
 * Gylin Mattias <matgy024@student.liu.se>
 * Sj�linder Mattias <matsj509@student.liu.se>
 * Sj�strand Johan <johsj438@student.liu.se>
 * �kerlund Anders <andak893@student.liu.se>
 *
 * Copyright 2007:
 * Per Fredriksson <perfr775@student.liu.se>
 * David Karlsl�tt <davka417@student.liu.se>
 * Tor Knutsson	<torkn754@student.liu.se>
 * Daniel K�llming <danka053@student.liu.se>
 * Ted Palmgren <tedpa175@student.liu.se>
 * Freddie Pintar <frepi150@student.liu.se>
 * M�rten Thur�n <marth852@student.liu.se>  *
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
 *

 * Nawzad Mardan 2008-2009-05-20<nawzad.mardan@liu.se>
 *
 */

package mind.model.function;


import mind.model.function.helpers.*;
import mind.model.*;
import mind.io.*;
import mind.gui.GUI;
import mind.gui.UserSettingConstants;
import java.util.*;


/**
 * The function InvestmentCost
 *
 * @author Johan Sj�strand
 * @author Freddie Pintar
 * @author Tor Knutsson
 * @version 2007-12-12
 */

/**
 * The function InvestmentCost
 *
 * @author Nawzad Mardan
 * @version 2008-06-24
 */
public class InvestmentCost
    extends NodeFunction
    implements Cloneable,UserSettingConstants
{
    private Vector c_timesteps = null;

    /**
     * Class that describes the cost at a
     * certain interval.
     */
    class CostFunction
	extends LinearFunction
    {
    }

    /*
     * List of all the cost-relations of this
     * timestep.
     */
    private Vector c_costFunction;
    // Added by Nawzad Mardan 080624
    //private Discountedsystemcost c_discountedsystemcost;
    // Annually rate
    //private Float c_intrate;
    // Number of years
    //private Long c_analysPeriod;
    // A Vector containing calculated  rate for each year
    //private Vector c_annualRateVector;
    // The header of the table 
    //private String c_tableHead []= {"Year", "Timestep nr."};
    // An empety table data
   // private Object [][] c_data ={ { "Year1", ""},{ "Year2", ""}, {"Year3",""},{"Year4",""},{"Year5",""},{"Year6", ""},}; 
    //int c_dataLength;
    //Vector containing the selected timesteps number
    private Vector c_timestepValues;
    private float c_technicallifespan = 0.f;
    private float c_economiclifespan = 0.f;
    private float c_percentagescrapvalue=0.f;
    private float c_fastscrapvalue=0.f;
    private boolean c_annualrateCBState, c_fixedValueCB, c_percentageValueCB, c_discountSystemCost;
    private float c_annualratevalue =0.f;
   // boolean c_discountOk = false;
    private float c_infinity;
    private float c_diminutive;

    /*
     * The cost when no investment is made.
     *
     * We must have this because otherwise we could
     * end up with having no resources beeing processed
     * but we still have an initial cost(offset of
     * first function describing cost as a function
     * of "capacity").
     *
     * This is initially set to zero, but can be
     * changed later on.
     */
    private float c_costWhenNoInvest = 0;


    /**
     * Internal class which holds information about one
     * timestep.
     */
    class TimestepInfo
    {


	/*
	 * List of all selected outflows/inflows
	 */
	private Vector c_outFlow = new Vector(0);
	private Vector c_inFlow = new Vector(0);


	/**
	 * Constructor
	 */
	public TimestepInfo()
	{
	}

	/**
	 * Constructor wich initializes the class
	 * according to an instance of the same class
	 * wich is given to it.
	 * @param info An instance of this class.
	 */
	public TimestepInfo(TimestepInfo info)
	{
	    c_outFlow = new Vector(info.getOutFlow());
	    c_inFlow = new Vector(info.getInFlow());
	}

	/**
	 * Return selected inflows
	 * @return Vector of inflows
	 */
	Vector getInFlow()
	{
	    return c_inFlow;
	}

	/**
	 * Select inflows (all at once)
	 * @param f Vector of inflows
	 */
	void setInFlow(Vector f)
	{
	    c_inFlow = f;
	}

	/**
	 * Return selected outflows
	 * @return Vector of outflows
	 */
	Vector getOutFlow()
	{
	    return c_outFlow;
	}

	/**
	 * Select outflows (all at once)
	 * @param f Vector of outflows
	 */
	void setOutFlow(Vector f)
	{
	    c_outFlow = f;
	}

	/*
	 * Create the timestep information into xml
	 */
	public String toXML(int indent)
	{
	    String xml = "";

	    for( int index = 0; index < c_inFlow.size(); index++ )
		{
		    xml = xml + XML.indent( indent ) + "<flowid_in>" +
			c_inFlow.elementAt(index).toString() + "</flowid_in>" + XML.nl();
		}

	    for( int index = 0; index < c_outFlow.size(); index++ )
		{
		    xml = xml + XML.indent( indent ) + "<flowid_out>" +
			c_outFlow.elementAt(index).toString() + "</flowid_out>" + XML.nl();
		}

	    return xml;
	}
	/**
	 * Puts EXML data in the given ExmlSheet
	 * PUM5 Added 2007-12-12
	 * @param sheet The ExmlSheet to be changed 
	 */
	public void toEXML(ExmlSheet sheet)
	{
		String flows = "";
	    for( int index = 0; index < c_inFlow.size(); index++ )
		{
		    flows += c_inFlow.elementAt(index).toString();
		    if (index != c_inFlow.size() -1)
		    	flows += ",";
		}
	    if (flows == "")
	    	flows = "-";
	    sheet.addTableValue(flows);
		flows = "";
	    for( int index = 0; index < c_outFlow.size(); index++ )
		{
		    flows += c_outFlow.elementAt(index).toString();
		    if (index != c_outFlow.size() -1)
		    	flows += ",";
		}
	    if (flows == "")
	    	flows = "-";
	    sheet.addTableValue(flows);
	}


    } /* End class TimestepInfo */


    /**
     * Constructor.
     */
    public InvestmentCost()
    {
	super(new ID(ID.FUNCTION), "InvestmentCost", null);
	c_timesteps = new Vector();
	c_costFunction = new Vector();
      //  c_discountedsystemcost = new Discountedsystemcost();

	TimestepInfo info = new TimestepInfo();
	c_timesteps.add(info);
         
        /* get the infinity definition from the ini-file */
        Ini inifile = new Ini();

        String inf = inifile.getProperty(MPS_INFINITY_DEFINITIION, "1E10");

        try {
            c_infinity = Float.parseFloat(inf);
            }
        catch (NumberFormatException ex) 
            {
            GUI.getInstance().showMessageDialog("Invalid value in settings.ini. " +
                                          MPS_INFINITY_DEFINITIION + "= " +
                                          inf + "\n Using 1e10 as infinity");
            c_infinity = 1E10f;
            }
    //MPS_DIMINUTIVE_DEFINITIION c_diminutive
        String infof = inifile.getProperty(MPS_DIMINUTIVE_DEFINITIION, "1E-6");

        try {
            c_diminutive= Float.parseFloat(infof);
            }
        catch (NumberFormatException ex) 
            {
            GUI.getInstance().showMessageDialog("Invalid value in settings.ini. " +
                                          MPS_DIMINUTIVE_DEFINITIION + "= " +
                                          infof + "\n Using 1E-6 as minimum");
            c_diminutive = 1E-6f;
            }
    }


    /**
     * Parse the function data to XML.
     * @param indent The level to indent to.
     * @return A string of the parsed data.
     */
    public String toXML(ResourceControl resources, int indent)
    {
	String xml = XML.indent(indent) + "<investmentCost>" + XML.nl();
	/* Save label */
	if( getLabel() != null ){
	    xml = xml + XML.indent(indent + 1) + "<label>" + getLabel()
		+ "</label>" + XML.nl();
	}
      // Added by Nawzad Mardan 080627
        //c_dataLength = c_data[0].length;
        xml = xml + "<technicallifespan>" + Float.toString(c_technicallifespan) + "</technicallifespan>" + XML.nl();
        xml = xml + "<economiclifespan>" + Float.toString(c_economiclifespan) + "</economiclifespan>" + XML.nl();
        if(c_annualrateCBState)
            xml = xml + "<annualRateValue>" + Float.toString(c_annualratevalue) + "</annualRateValue>" + XML.nl();
        if((c_percentageValueCB) && (c_percentagescrapvalue != 0))
            xml = xml + "<percentagescrapvalue>" + Float.toString(c_percentagescrapvalue) + "</percentagescrapvalue>" + XML.nl();
        if((c_fixedValueCB) &&(c_fastscrapvalue != 0))
            xml = xml + "<fastscrapvalue>" + Float.toString(c_fastscrapvalue) + "</fastscrapvalue>" + XML.nl();
        /*if((c_intrate > 0) && (c_analysPeriod > 1))
        {
        xml = xml + "<rate>" + Float.toString(c_intrate) + "</rate>" + XML.nl();
        xml = xml + "<analysPeriod>" + Long.toString(c_analysPeriod) + "</analysPeriod>" + XML.nl();
        //c_tableHead
        Vector  tableHeadVec = new Vector();
         for (int i = 0; i < c_tableHead.length; i++) 
           {
            tableHeadVec.add(c_tableHead[i]);
           }
        
        xml = xml + "<tableHead>" + tableHeadVec.toString() + "</tableHead>" + XML.nl();
        xml = xml + "<dataLength>" + c_dataLength + "</dataLength>" + XML.nl();
        xml = xml + "<timestepValue>" + c_dataLength + "</timestepValue>" + XML.nl();
        // Add or save all data in the XML FILE
        String str = "[";
        for (int i = 0; i < c_data.length; i++) 
                {
                   for(int j= 0; j< c_dataLength; j++)
                   {
                       str = str+ c_data[i][j]+","; 
                   }
                 }
      String sTmp = (str.length() < 2) ? "" : str.substring(0, str.length() - 1);// sTmp = str without last ","
               
      // Check if the last data is an empty string
      if(c_data[c_data.length-1][c_dataLength-1].equals(""))
         sTmp = sTmp+" ]";
      else
        sTmp = sTmp+"]";
        xml = xml + "<dataVector>"  + sTmp  + "</dataVector>"  + XML.nl();
        }*/
        
        /*
	 * Save "cost when no invest" variable
	 */
	xml = xml + XML.indent(indent+1) + "<c>" +
	    getCostWhenNoInvest() + "</c>" + XML.nl();

        /*
	 * Save info about each timestep, i.e
	 * wich flows that are registered in each
	 * timestep
	 */
	int numberOfTimesteps = getTimesteps();
	for( int i = 0; i < numberOfTimesteps; i++){
	    TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( i );
	    xml = xml + XML.indent( indent + 1) + "<timestep.investmentCost nr=\"" + (i + 1) + "\">" + XML.nl();
	    xml = xml + info.toXML( indent + 2);
	    xml = xml + XML.indent( indent + 1) + "</timestep.investmentCost>" + XML.nl();
	}

	/*
	 * Save info about the investment cost
	 * function
	 */
	for (int i = 0; i < getNrFunctions(); i++)
	    {
		xml = xml + XML.indent(indent+1) +
		"<costfunlist>" + XML.nl();

		xml = xml +
		    XML.indent(indent+2) + "<start>" + getBegin(i+1) +
		    "</start>" + XML.nl() +
		    XML.indent(indent+2) + "<end>" + getEnd(i+1) +
		    "</end>" + XML.nl() +
		    XML.indent(indent+2) + "<slope>" + getSlope(i+1) +
		    "</slope>" + XML.nl() +
		    XML.indent(indent+2) + "<offset>" + getOffset(i+1) +
		    "</offset>" + XML.nl();

		xml = xml + XML.indent(indent+1) +
		"</costfunlist>" + XML.nl();
	    }



        xml = xml + XML.indent(indent) + "</investmentCost>" + XML.nl();

        return xml;
    }

  /**
     *  PUM5 Added
     * 
     */
    public void toEXML(ResourceControl resources,ExmlSheet sheet) {
    	// Add Label
    	String label = ((this.getLabel()==null)?"":this.getLabel());
 
		//Add function header
		sheet.addFunctionHeader("InvestmentCost", label);
    	
        /*
    	 * Save info about the investment cost
    	 * function
    	 */        
                
                
    	/* Added by Nawzad Mardan 080814
    	 * Save info about the technical lifespan
    	 * and economic lifespan
    	 */
         
        sheet.addRow(sheet.addLockedCell("Technical lifespan")+sheet.addCell(c_technicallifespan));  
        sheet.addRow(sheet.addLockedCell("Economic lifespan")+sheet.addCell(c_economiclifespan));
        if(c_annualrateCBState)
           sheet.addRow(sheet.addLockedCell("Annual rate value")+sheet.addCell(c_annualratevalue));
        if(c_percentageValueCB)
            sheet.addRow(sheet.addLockedCell("Percentage value of the scrap")+sheet.addCell(c_percentagescrapvalue));
        if(c_fixedValueCB)
            sheet.addRow(sheet.addLockedCell("Fixed value of the scrap")+sheet.addCell(c_fastscrapvalue));
    	int numberOfFunctions = getNrFunctions();
		sheet.addRow(sheet.addCell("CostWhenNoInvest")
				+ sheet.addCell(getCostWhenNoInvest()));
    	if (numberOfFunctions > 0) {
			sheet.addTimeStepRow("CostFunction Number", numberOfFunctions);
			sheet.initTable(4, numberOfFunctions);
			sheet.addLockedTableValue("Start");
			sheet.addLockedTableValue("End");
			sheet.addLockedTableValue("Slope");
			sheet.addLockedTableValue("Offset");
			for (int i = 0; i < numberOfFunctions; i++) {
				sheet.addTableValue(getBegin(i + 1));
				sheet.addTableValue(getEnd(i + 1));
				sheet.addTableValue(getSlope(i + 1));
				sheet.addTableValue(getOffset(i + 1));
			}
			sheet.endTable();
		}	
    	
    	// Add timestep nrs in one Row.
    	int numberOfTimesteps = getTimesteps();
    	sheet.addTimeStepRow(numberOfTimesteps);
    	
    	// initiate table for out/inflows
    	sheet.initTable(2, numberOfTimesteps);
    	sheet.addLockedTableValue("InFlows");
    	sheet.addLockedTableValue("OutFlows");
    	for(int i = 0; i < numberOfTimesteps; i++){
    	    TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( i );
    	    info.toEXML(sheet);

    	}
    	sheet.endTable();
    	sheet.addRow(sheet.addCell(""));

    	
    }

    /**
     * Add one function to the end.
     */
    public void addCostFunction()
    {
	c_costFunction.addElement(new CostFunction());
    }

    /**
     * Removes everything about the functions.
     */
    public void clearAllFunctions()
    {
	c_costFunction.removeAllElements();
    }

    /**
     * Gets the beginning point of an interval where
     * a function is valid.
     * @param fidx Wich function.
     * @return The interval lower limit.
     */
    public float getBegin(int fidx)
    {
	return (float)((CostFunction) c_costFunction.get(fidx-1)).getBegin();
    }

    /**
     * Sets the beginning point of an interval where
     * a function is valid.
     * @param fidx Wich function.
     * @param value The interval lower limit.
     */
    public void setBegin(int fidx, float value)
    {
	((CostFunction) c_costFunction.get(fidx-1)).setBegin(value);
    }

    /**
     * Gets the end point of an interval where a function
     * is valid.
     * @param fidx Wich function.
     * @return The interval upper limit.
     */
    public float getEnd(int fidx)
    {
	return (float)((CostFunction) c_costFunction.get(fidx-1)).getEnd();
    }

    /**
     * Sets the end point of an interval where a function
     * is valid.
     * @param fidx Wich function.
     * @param value The interval upper limit.
     */
    public void setEnd(int fidx, float value)
    {
	((CostFunction) c_costFunction.get(fidx-1)).setEnd(value);
    }

    /**
     * Gets the offset for a specified function.
     * @param fidx Wich function.
     * @return The offset value.
     */
    public float getOffset(int fidx)
    {
	return (float)((CostFunction) c_costFunction.get(fidx-1)).getOffset();
    }

    /**
     * Sets the offset for a specified function.
     * @param fidx Wich function.
     * @param value The offset value.
     */
    public void setOffset(int fidx, float value)
    {
	((CostFunction) c_costFunction.get(fidx-1)).setOffset(value);
    }


    /**
     * Gets the slope for a specified function.
     * @param fidx Wich function.
     * @return The slope value.
     */
    public float getSlope(int fidx)
    {
	return (float)((CostFunction) c_costFunction.get(fidx-1)).getSlope();
    }

    /**
     * Sets the slope for a specified function.
     * @param fidx Wich function.
     * @param value The slope value.
     */
    public void setSlope(int fidx, float value)
    {
	((CostFunction) c_costFunction.get(fidx-1)).setSlope(value);
    }

    /**
     * Set all info about cost function.
     * @param fidx Wich function
     * @param begin Begin
     * @param end End
     * @param offset Offset
     * @param slope Slope
     */
    public void setCostFunAt(int fidx, float begin, float end, float offset, float slope)
    {
	((CostFunction) c_costFunction.get(fidx-1)).setBegin(begin);
	((CostFunction) c_costFunction.get(fidx-1)).setEnd(end);
	((CostFunction) c_costFunction.get(fidx-1)).setOffset(offset);
	((CostFunction) c_costFunction.get(fidx-1)).setSlope(slope);
    }


    /**
     * Returns the cost when no investment is made.
     * @return The investment when no cost value.
     */
    public float getCostWhenNoInvest()
    {
	return c_costWhenNoInvest;
    }

    /**
     * Sets the cost when no investment is made.
     */
    public void setCostWhenNoInvest(float costWhenNoInvest)
    {
	c_costWhenNoInvest = costWhenNoInvest;
    }


    /**
     * Get number of functions defined.
     * @return The number of functions (size of vector).
     */
    public int getNrFunctions()
    {
	return c_costFunction.size();
    }


    /**
     * Remove last function in list.
     */
    public void removeLast()
    {
	if (getNrFunctions() > 0)
	    {
		c_costFunction.removeElementAt(getNrFunctions()-1);
	    }
    }


    /**
     * Get the inflows at the current timestep.
     * @return Vector of inflows
     */
    public Vector getInFlow()
    {
	Vector test=c_timesteps;
        int t = getTimestep();
        TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( getTimestep() - 1 );
	return info.getInFlow();
    }

    /**
     * Get the outflows at the current timestep.
     * @return Vector of outflows
     */
    public Vector getOutFlow()
    {
	TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( getTimestep() - 1 );
	return info.getOutFlow();
    }

    /**
     * set the flow id's, called by FlowEquationDialog.
     *
     * @param f_in the in flow id vector
     * @param f_out the out flow id vector
     */
    public void setTimestepInfo(Vector f_in, Vector f_out)
    {
	TimestepInfo info = (TimestepInfo) c_timesteps.elementAt( getTimestep() - 1 );

	info.setInFlow(f_in);
	info.setOutFlow(f_out);
    }


    /**
     * Creates a new copy of the function.
     * @return A complete copy
     */
    public Object clone() throws CloneNotSupportedException
    {
	InvestmentCost clone = (InvestmentCost) super.clone();
	c_timesteps = new Vector(clone.c_timesteps);
	c_costFunction = new Vector(clone.c_costFunction);
	c_costWhenNoInvest = (float)clone.c_costWhenNoInvest;

	return clone;
    }

    public int addEquationOfTimestep(EquationControl control, int timestep, int index, ID node,
                                      Vector inFlows, Vector outFlows)
    {
    TimestepInfo info = (TimestepInfo) c_timesteps.get(index);

    int equation = 1;

	Vector inflowid = info.getInFlow();
	Vector outflowid = info.getOutFlow();

		/* Convert from string to id */
		stringToID( inflowid, inFlows);
		stringToID( outflowid, outFlows);
                Variable xMax;


		/********************************************/

		/*
		 * Generate equation (one for each timestep i)
		 * XTi = F1Ti+F2Ti+...+FnTi
		 * ( XTi-F1Ti-F2Ti-...-FnTi = 0 )
		 */
		Equation xTiEq = new Equation(node,getID(),timestep,
					      equation++,Equation.EQUAL,(float)0);

		/* XT1...XTi */
        String xTiVarString = new String("X" + getID());
		Variable xTi = new Variable(xTiVarString,timestep,(float)1.0);

                /* Add XTi to the XTi equation */
		xTiEq.addVariable(xTi);

		/* Used for both inflows and outflows */
		Variable fvar;

		/*
		 * Add one variable to the equation XTi for each
		 * "selected" inflow
		 * This means:
		 * XTi = F1Ti+...FnTi where all F1Ti flows are inflows
		 * Equation really looks like:
		 * XTi - F1Ti -...- FnTi = 0
		 */
		for(int fin = 0; fin<inflowid.size(); fin++)
		    {
			fvar = new Variable( (ID)inflowid.elementAt(fin),
					      timestep,(float)-1.0);
			xTiEq.addVariable(fvar);
		    }

		/*
		 * Add one variable to the XTi equation for each "selected"
		 * outflow
		 */
		for(int fout = 0; fout<outflowid.size(); fout++)
		    {
			fvar = new Variable( (ID)outflowid.elementAt(fout),
					      timestep,(float)-1.0);
			xTiEq.addVariable(fvar);
		    }

		/* Add equation to control */
		control.add(xTiEq);

		/********************************************/

		/*
		 * Xmax >= XTi
		 * Xmax - XTi >= 0
		 */
		Equation xMaxEq = new Equation(node,getID(),timestep,
					       equation++,Equation.GREATEROREQUAL,(float)0);

		/* independent of which timestep */
                String xMaxVarString = new String("Xm"+getID());
		xMax = new Variable(xMaxVarString,
                                    0 /*timestep always the same*/,
                                    (float)1.0);

		/* Add Xmax to the Xmax equation */
		xMaxEq.addVariable(xMax);

		/* XT1...XTi */
		xTi = new Variable(xTiVarString,timestep,(float)-1.0);

		/* Add XTi to the Xmax equation */
		xMaxEq.addVariable(xTi);

		/* Add equation to control*/
		control.add(xMaxEq);

                return equation;
    }


    /**
     * Returns optimizationinformation from InvestmentCost.
     * @param maxTimesteps The maximum number of timesteps in the model
     * @param node The ID for the node that generates the equations
     * @param inFlows Flows coming in to node
     * @param outFlows Flows going out from node
     * @return Some equations that model the source's behaviour
     * @throws ModelException if it cannot optimize
     */
    public EquationControl getEquationControl(int maxTimesteps, ID node,
					      Vector inFlows, Vector outFlows) throws ModelException
    {

	EquationControl control = new EquationControl();
        
         // ||(c_discountOk == true) )
    control =  getEquationControlOldVersion(maxTimesteps, node, inFlows, outFlows);
        
            
    return control;
        
    }


  
/**
     * Returns optimizationinformation from InvestmentCost.
     * @param maxTimesteps The maximum number of timesteps in the model
     * @param node The ID for the node that generates the equations
     * @param inFlows Flows coming in to node
     * @param outFlows Flows going out from node
     * @return Some equations that model the source's behaviour
     * @throws ModelException if it cannot optimize
     * Added by Nawzad Mardan 20080906
      *This is an old version of investment cost function calls if user didn't insert a value of
      *economic lifespan or technical lifespan or rate and analys period
      *This type of equations didn't take timesteps into consideration
     */
    private EquationControl getEquationControlOldVersion(int maxTimesteps, ID node,Vector inFlows, Vector outFlows)throws ModelException
    {

	EquationControl control = new EquationControl();

	/*
	 * Xmax has same name independent of the timestep
	 */
	Variable xMax;

        /* Equation number */

	int vectorsize = c_timesteps.size();
	for (int i = 0; i < maxTimesteps; i++)
            {
		/*
		  for every timestep, we need to generate a variable
		  First we find out the index of the cost
		  These two lines makes us use the same value in the vector
		  many times if we don't have enough information for all
		  timesteps in the model
		*/
		int index =  ((i * vectorsize) / maxTimesteps ) % vectorsize;

                addEquationOfTimestep(control, i+1, index, node, inFlows, outFlows);
            }


	/********************************************/
	/*
	 * Equation XmaxSum
	 * Xmax = Xmax1+Xmax2+...+Xmaxi
	 * Really looks like:
	 * Xmax-Xmax1-Xmax2-...-Xmaxi = 0
	 */
   int equation = 1;
	Equation xMaxSumEq = new Equation( node, getID(),node.toString()+"InvestmentFun", 0,
					   equation++/*for the equation*/,Equation.EQUAL,
					   (float)0);

	Variable xMaxi;

	/* Create Xmax equation
	 * Xmax - Xmax1 - Xmax2 -...-Xmaxi = 0
	 */
        String xMaxVarString = new String("Xm"+getID());
	xMax = new Variable( xMaxVarString,0,(float)1.0);
	xMaxSumEq.addVariable(xMax);

	for(int i = 1; i<=getNrFunctions(); i++)
	    {
                String xMaxiVarString = new String("Xm" + i + getID());
		xMaxi = new Variable(xMaxiVarString,0/*timestep*/,
                                    (float)-1.0);
		xMaxSumEq.addVariable(xMaxi);
	    }

	/* Xmax equation is done, add to control */
	control.add(xMaxSumEq);


	/********************************************/

	Equation ySumEq = new Equation( node, getID(),node.toString()+"InvestmentFun",0,
					equation++,Equation.EQUAL,(float)0);

	/*
	 * Y has same name independent of timestep
	 */
        String yVarString = new String("IcY"+getID());
	Variable y = new Variable(yVarString,0,(float)1.0);

        /*********************************************/

        /* The Y-variable is the investmentcost, this must be
         * added to the global equation OBJ, which is the one
         * the optimizer tries to minimize
         */
        Equation obj = new Equation(node, getID(), 1, 1, Equation.GOALORFREE);
	y = new Variable(yVarString,0,(float)1.0);
        obj.addVariable(y);
        control.add(obj);

        /*********************************************/

	Variable yi;

	/* Create Y equation
	 * Y - Y0 - Y1 -...- Yi = 0
	 */

	/* Add Y to the ySumEq */
	ySumEq.addVariable(y);

	for(int i = 0; i<=getNrFunctions(); i++)
	    {
                String yiVarString = new String("Y" + i + getID());
		yi = new Variable(yiVarString,0,(float)-1.0);
		ySumEq.addVariable(yi);
	    }

	/* Y equation is done, add to control */
	control.add(ySumEq);

	/********************************************/

	/* The equation
	 * I0+I1+...Ii = 1
	 */
	Equation iiEq = new Equation( node, getID(),node.toString()+"InvestmentFun",0,
					equation++,Equation.LOWEROREQUAL,(float)1);

	/* Binary variables */
	Variable ii;


	for(int i = 0; i<=getNrFunctions(); i++)
	    {
                String IiVarString = new String("I" + i + getID());
		ii = new Variable(IiVarString,0,(float)1.0);
                ii.setIsInteger(true);

		iiEq.addVariable(ii);
	    }
	/* Ii equation done, add to control */
	control.add(iiEq);

	/********************************************/


	/* The equation
	 * Y0 = C*I0;
	 * Y1 = k1*Xmax1+b1*I1
	 * ...
	 * Yi = ki*Xmaxi+bi*Ii
	 */
	Equation yiEq;

	/* The condition connected to every
	 * Y1...Yi
	 *
	 * Z0i*Ii<=Xmaxi<=Z1i*Ii
	 * Condition is split into
	 * two equations 1 and 2
	 */
	Equation yiCond1Eq;
	Equation yiCond2Eq;

	/*
	 * Add equations to describe the investment cost
	 * functions
	 */
	for(int i = 0; i<=getNrFunctions(); i++)
	{
            yiEq = new Equation(node,getID(),node.toString()+"InvestmentFun",0,
				 equation++,Equation.EQUAL,(float)0);

            /* Create yi-variable (coefficient is not changed
             * in the diff. cases)
             */
            String IiVarString = new String("I" + i + getID());
            String yiVarString = new String("Y" + i + getID());
            String xMaxiVarString = new String("Xm" + i + getID());

            yi = new Variable(yiVarString,0,(float)1.0);

            /* Y0 */
            if(0 == i)
            {
                /* Add the variable C*I0
                 * in equation Y0 - C*I0 = 0
                 */
                ii = new Variable(IiVarString,0,(float)-c_costWhenNoInvest);
                ii.setIsInteger(true);

                yiEq.addVariable(yi);
                yiEq.addVariable(ii);

                control.add(yiEq);
            }
            /* Y1...Yi */
            else
            {
                yiCond1Eq = new Equation(node,getID(),node.toString()+"InvestmentFun",0,
                		 equation++,Equation.LOWEROREQUAL,(float)0);
                yiCond2Eq = new Equation(node,getID(),node.toString()+"InvestmentFun",0,
				 equation++,Equation.GREATEROREQUAL,(float)0);

                /* Yi - k*Xmaxi - b*ii = 0 */
                xMaxi = new Variable(xMaxiVarString,0,(float)-getSlope(i));
                ii = new Variable(IiVarString,0,(float)-getOffset(i));
                ii.setIsInteger(true);
                yiEq.addVariable(yi);
                yiEq.addVariable(xMaxi);
                yiEq.addVariable(ii);

                control.add(yiEq);

                /********************************************/

                /* Condition 1
                 * Xmaxi-Z1i*Ii <= 0
                 */
                xMaxi = new Variable(xMaxiVarString,0,(float)1.0);
                ii = new Variable(IiVarString,0,(float)-getEnd(i));
                ii.setIsInteger(true);
                yiCond1Eq.addVariable(xMaxi);
                yiCond1Eq.addVariable(ii);
                control.add(yiCond1Eq);

                /* Condition 2
                 * Xmaxi-Z0i*Ii >= 0
                 */
                xMaxi = new Variable(xMaxiVarString,0/*timestep*/,(float)1.0);
                ii = new Variable(IiVarString,0,(float)-getBegin(i));
                ii.setIsInteger(true);
                yiCond2Eq.addVariable(xMaxi);
                yiCond2Eq.addVariable(ii);
                control.add(yiCond2Eq);
            }
	}
	return control;
    }


  /**
	 * Returns optimizationinformation from source
	 * @param data The timestep numbers for each year
         * @param annualRate The annually interest
         * @param timeStepValues The length of timesteps
	 * @param node The ID for the node that generates the equations
	 * @return Some equations that model the source's behaviour
	 * @throws ModelException when resource does not exist
	 * @author Added by Nawzad Mardan 080906 calls when Discountedsystemcost Function slected from Model menu
         * Returns optimizationinformation from the function.
          */

        /*
         * This method added in order to generate new type of equations, this is not the same equations
         * when the Discountedsystemcost function is not used. These equations have
         * a different timestep numbers and a different coefficient depending on the
         * length of the analyses period. This method calls when the user trigger
         * the Discountedsystemcost function from Model menu.

         */
     /*public EquationControl getEquationControl2(Object [][] data,Vector annualRate,Vector timeStepValues,
					      ID node, Vector inFlows,Vector outFlows)throws ModelException*/
    public EquationControl getEquationControl2(Object [][] data,float rateValue,Vector timeStepValues,
					      ID node, Vector inFlows,Vector outFlows)throws ModelException
       {
       
        EquationControl control = new EquationControl();
        int maxTimesteps = timeStepValues.size();
       
        
        float my = c_diminutive;

        // Added by Nawzad Mardan 2008-08-28
        // This is a new case that added to the investment cost
        // When user inset value of
        // economic lifespan or technical lifespan or rate and analys period will generate
        // new type of equations take timesteps into consideration
         Vector timestepPerYear = new Vector();
         Vector tDoubelPreemVect = new Vector();
         Vector wiVector = new Vector();
         timestepPerYear =  CalculateTimestepPerYear(data);
        //int maxTimesteps = timeStepValues;
        int equation = 1;
        int vectorsize = c_timesteps.size();
       
        for (int i = 0; i < maxTimesteps; i++)
        {
		/*
		  for every timestep, we need to generate a variable
		  First we find out the index of the cost
		  These two lines makes us use the same value in the vector
		  many times if we don't have enough information for all
		  timesteps in the model
		*/
		int index =  ((i * vectorsize) / maxTimesteps ) % vectorsize;
                TimestepInfo info = (TimestepInfo) c_timesteps.get(index);
                 //TimestepInfo inf = (TimestepInfo) c_timesteps.get(i);


		Vector inflowid = info.getInFlow();
		Vector outflowid = info.getOutFlow();

		/* Convert from string to id */
		stringToID( inflowid, inFlows);
		stringToID( outflowid, outFlows);
        Variable xMax;
        int timestep = i+1;

		/********************************************/

		/*
		 * Generate equation (one for each timestep i)
		 * XTi = F1Ti+F2Ti+...+FnTi
		 * ( XTi-F1Ti-F2Ti-...-FnTi = 0 )
		 */
		Equation xTiEq = new Equation(node,getID(),node.toString()+"InvestmentFun",timestep,
					      equation++,Equation.EQUAL,(float)0);

		/* XT1...XTi */
        String xTiVarString = new String("XInv");
		Variable xTi = new Variable(xTiVarString+getID(),timestep,(float)1.0);

                /* Add XTi to the XTi equation */
		xTiEq.addVariable(xTi);

		/* Used for both inflows and outflows */
		Variable fvar;

		/*
		 * Add one variable to the equation XTi for each
		 * "selected" inflow
		 * This means:
		 * XTi = F1Ti+...FnTi where all F1Ti flows are inflows
		 * Equation really looks like:
		 * XTi - F1Ti -...- FnTi = 0
		 */
		for(int fin = 0; fin<inflowid.size(); fin++)
		    {
			fvar = new Variable( (ID)inflowid.elementAt(fin),
					      timestep,(float)-1.0);
			xTiEq.addVariable(fvar);
		    }

		/*
		 * Add one variable to the XTi equation for each "selected"
		 * outflow
		 */
		for(int fout = 0; fout<outflowid.size(); fout++)
		    {
			fvar = new Variable( (ID)outflowid.elementAt(fout),
					      timestep,(float)-1.0);
			xTiEq.addVariable(fvar);
		    }

		/* Add equation to control */
		control.add(xTiEq);

		/********************************************/

		/*
		 * XmaxInvT0 >= XTi
		 * XmaxInvT0 - XTi >= 0
		 */
		Equation xMaxEq = new Equation(node,getID(),node.toString()+"InvestmentFun",timestep,
					       equation++,Equation.GREATEROREQUAL,(float)0);

		/* independent of which timestep */
        String xMaxVarString = new String("XMaxInv");
		xMax = new Variable(xMaxVarString+getID(),0 /*timestep always the same*/,(float)1.0);

		/* Add Xmax to the Xmax equation */
		xMaxEq.addVariable(xMax);

		/* XT1...XTi */
		xTi = new Variable(xTiVarString+getID(),timestep,(float)-1.0);

		/* Add XTi to the Xmax equation */
		xMaxEq.addVariable(xTi);

		/* Add equation to control*/
		control.add(xMaxEq);
      }// END FOR


        /*******************************************/
/*
	 * * Equation XmaxSum
	 * XmaxInvT0 = XmaxInvT1+XmaxInvT2+...+XmaxInTi
	 * Really looks like:
	 * XmaxInvT0-XmaxInvT1-XmaxInvT2-...-XmaxInvTi = 0
	 */
	Equation xMaxSumEq = new Equation( node, getID(),node.toString()+"InvestmentFun", 0,
					   equation++/*for the equation*/,Equation.EQUAL,(float)0);

	Variable xMaxi;

	/* Create XmaxInvT0 equation
         *XmaxInvT0 = XmaxInv1T1 + XmaxInv1T2 +...+ XmaxInv1Ti + XmaxInv2T1 + XmaxInv2T2 +...+ XmaxInv2Ti +...+ XmaxInvnTi
	 * XmaxInvT0 - (XmaxInv1T1 + XmaxInv1T2 +...+ XmaxInv1Ti + XmaxInv2T1 + XmaxInv2T2 +...+ XmaxInv2Ti +...+ XmaxInvnTi)  = 0
         *
	 */
    Variable xMax;
    String xMaxVarString = new String("XMaxInv");
	xMax = new Variable(xMaxVarString+getID(),0 /*timestep always the same*/,(float)1.0);

	xMaxSumEq.addVariable(xMax);
    for(int i = 1; i<=getNrFunctions(); i++)
       {
       for (int j = 1; j <= maxTimesteps; j++)
          {
          String xMaxiVarString = new String("XMaxInv" + i );
          xMaxi = new Variable(xMaxiVarString+getID(),j/*timestep*/,(float)-1.0);
          xMaxSumEq.addVariable(xMaxi);
          }// END FOR LOOP for each timesteps
       }

	/* Xmax equation is done, add to control */
	control.add(xMaxSumEq);

        /********************************************/
        /*

         *YInv0T1+YInv1T1+YInv2T1+...+YInvnT1+...+YInv0T2+YInv1T2+YInv2T2+...+YInvnT2+ YInv0T3+YInv1T3+YInv2T3+...+YInvnT3+...+YInvnTi
         *YInvtotT0 = YInv0T1+YInv1T1+YInv2T1+...+YInvnT1+...+YInv0T2+YInv1T2+YInv2T2+...+YInvnT2+ YInv0T3+YInv1T3+YInv2T3+...+YInvnT3+...+YInvnTi
         *
         **Y0T1+Y1T1+Y2T1+...+YnT1+...+Y0T2+Y1T2+Y2T2+...+YnT2+ Y0T3+Y1T3+Y2T3+...+YnT3+...+YnTi

         *Ytot - Y0T1-Y1T1-Y2T1+...-YnT1-...-Y0T2-Y1T2-Y2T2-...-YnT2-Y0T3-Y1T3-Y2T3-...-YnT3-...-YnTi = 0
         *
         *
         *
         *Ytot = Y0T1+Y1T1+Y2T1+...+YnT1+...+Y0T2+Y1T2+Y2T2+...+YnT2+ Y0T3+Y1T3+Y2T3+...+YnT3+...+YnTi
         *Ytot - Y0T1-Y1T1-Y2T1+...-YnT1-...-Y0T2-Y1T2-Y2T2-...-YnT2-Y0T3-Y1T3-Y2T3-...-YnT3-...-YnTi = 0
         *
         *
         *
         */

	Equation ySumEq = new Equation( node, getID(),node.toString()+"InvestmentFun",0,equation++,Equation.EQUAL,(float)0);

	/*
	 * Y has same name independent of timestep
	 */
    String yVarString = new String("YInvtot");
	Variable y ;//= new Variable(yVarString,0,(float)1.0);

        /*********************************************/

        /* The Y-variable is the investmentcost, this must be
         * added to the global equation OBJ, which is the one
         * the optimizer tries to minimize
         */
    Vector rateVect = null ;
    Equation obj = new Equation(node, getID(), 1, 1, Equation.GOALORFREE);
	y = new Variable(yVarString+getID(),0,(float)1.0);
    Variable YInvtot = new Variable(yVarString+getID(),0,(float)1.0);
    obj.addVariable(y);
    control.add(obj);
       /* Add Y to the ySumEq */
    ySumEq.addVariable(y);
    Variable yi;
        /*********************************************/
       // Chek if user insert analyse period and rate
        // Ysum must multiply with the dicountsytem rate factor (1+r)-n

    float rateCoff;
    int timestepNum, timePeriodLength;
               // for each timestep, we need to generate a variable
                // Each year have own rate and a number of time steps
                // Generate a variable for each timesteps and for each year
    // For Investment rate equation begin from -(n-1) --> (1+rate)power - (number of year- 1)
    // For this reason first value for the Annual Rate Vector will be 1 because power(0) = 1
    int analyperiod = data.length;
    float actualRate;
    if((c_annualrateCBState) && (c_annualratevalue > 0))
        actualRate = c_annualratevalue/100;
    else
        actualRate = rateValue/100;
   
    rateVect = calculateAnnualRate(analyperiod,actualRate );
   

    for (int i = 0; i < data.length; i++)
           {
           // Get anuualy rate from the annual vector
           rateCoff = ((Float)rateVect.get(i)).floatValue();
           timePeriodLength = data[i].length;
           // Each year have his own time steps
           for(int j= 1; j< timePeriodLength; j++)
              {
                       // Check if the array is not empty
                       if(!data[i][j].equals(""))
                       {
                       // Get timestep number from the array
                       String value = (String)data[i][j];
                       timestepNum =Integer.parseInt(value);

                       // Check if the correct timestep insert it
                       if(timestepNum <= 0)
                           throw new ModelException("Please insert correct Timestep number in the Discountsystem's table.\n" +
                                        " Timestep number should be largar than zero.\n Can not optimize.");

                       // Generat new variable for each time step and each flow
                       for(int k = 0; k<=getNrFunctions(); k++)
                          {
                          if(k == 0)
                            {
                            if(getCostWhenNoInvest() != 0)
                             {
                             String yiVarString = new String("YInv" + k );
                             yi = new Variable(yiVarString+getID(),timestepNum,(float)-rateCoff);
                             ySumEq.addVariable(yi);
                             }
                            }
                          else
                            {
                            String yiVarString = new String("YInv" + k );
                            yi = new Variable(yiVarString+getID(),timestepNum,(float)-rateCoff);
                            ySumEq.addVariable(yi);   
                            }
                          } 
                        }// END IF checking
               }// END FOR for each year
          }// END FOR for all timesteps and all years

            /* Y equation is done, add to control */
	control.add(ySumEq);
        /*******************************************/

	/********************************************/
        /* The equation
           The condition connected to every
         *Variabler som beskriver de olika �slopen�:
           YInv0T1 = C*Iinv0T1
           YInv0T2 = C*Iinv0T2
           YInv0T3 = C*Iinv0T3
            .
            .
           YInv0Ti = C*I0Ti

            ..........................................................
	 *  YInv1T1 = k*XMaxInv1T1+b*Iinv1T1    Z0*my*Iinv1T1<= XMaxInv1T1 <= Z1*Iinv1T1
            YInv1T2 = k*XMaxInv1T2+b*Iinv1T2    Z0*my*Iinv1T2<= XMaxInv1T2 <= Z1*Iinv1T2
            .
            .
            YInv1Ti = k*XMaxInv1Ti+b*Iinv1Ti    Z0*my *Iinv1Ti<= XMaxInv1Ti <= Z1*Iinv1Ti


            YInv2T1 = k*XMaxInv2T2+b*Iinv2T1    Z1*my *Iinv2T1<= XMaxInv2T1 <= Z2*Iinv2T1
            YInv2T2 = k*XMaxInv2T2+b*Iinv2T2    Z1*my *Iinv2T2<= XMaxInv2T2 <= Z2*Iinv2T2
                .
                .
            YInv2Ti = k*XMaxInv2Ti+b*Iinv2Ti    Z1*my *Iinv2Ti<= XMaxInv2Ti <= Z2*Iinv2Ti


            .
            .
            .
            YInvnTi = k*XMaxInvnTi+b*IinvnTi    Z(n-1)*my *IinvnTi<= XMaxInvnTi <= Zn*IinvnTi




         */
	Equation yiEq;
    Variable ii;
	/*
	    Z0*my*Iinv1T1<= XMaxInv1T1 <= Z1*Iinv1T1
            Z0*my*Iinv1T2<= XMaxInv1T2 <= Z1*Iinv1T2
            .
            .
            Z0*my *Iinv1Ti<= XMaxInv1Ti <= Z1*Iinv1Ti

            Z1*my *Iinv2T2<= XMaxInv2T2 <= Z2*Iinv2T2
            .
            .
            Z1*my *Iinv2Ti<= XMaxInv2Ti <= Z2*Iinv2Ti
            .
            .
            Z(n-1)*my *IinvnTi<= XMaxInvnTi <= Zn*IinvnTi

	 *  Condition is split into
	 *  two equations 1 and 2
	 */
	Equation yiCond1Eq;
	Equation yiCond2Eq;

	/*
	 * Add equations to describe the investment cost
	 * functions
	 */
   boolean upprepning = false;
       for(int i = 0; i<=getNrFunctions(); i++)
        {
            for (int j = 0; j < maxTimesteps; j++)
            {
            /* Create yi-variable (coefficient is not changed
             * in the diff. cases)
             */
            String IiVarString = new String("Iinv" + i);
            String yiVarString = new String("YInv" + i);

            yi = new Variable(yiVarString+getID(),j+1,(float)1.0);

            /* Y0 */
            if(0 == i)
            {
                /* Add the variable C*I0
                 * in equation Y0 - C*I0 = 0
                 */
            if(getCostWhenNoInvest() != 0)
              {
                          
              yiEq = new Equation(node,getID(),node.toString()+"InvestmentFun",0,equation++,Equation.EQUAL,(float)0);
              ii = new Variable(IiVarString+getID(),j+1,(float)-c_costWhenNoInvest);
              ii.setIsInteger(true);

              yiEq.addVariable(yi);
              yiEq.addVariable(ii);

              control.add(yiEq);
              }
            }
            /* Y1...Yi */
            else
            {
                String xMaxiVarString = new String("XMaxInv" + i);

                /* YInv1T1 - k*XmaxInv1T1 - b*Inv1T1 = 0
                 YInv1T2 - k*XmaxInv1T2 - b*Inv1T2 = 0
                 ..
                 .
                 YInvnTi - k*XmaxInvnTi - b*InvnTi = 0
                 */
                yiEq = new Equation(node,getID(),node.toString()+"InvestmentFun",0,equation++,Equation.EQUAL,(float)0);
                //-k*XmaxInv1T1
                xMaxi = new Variable(xMaxiVarString+getID(),j+1,(float)-getSlope(i));
                // -b*Inv1T1
                ii = new Variable(IiVarString+getID(),j+1,(float)-getOffset(i));
                ii.setIsInteger(true);
                yiEq.addVariable(yi);
                yiEq.addVariable(xMaxi);
                yiEq.addVariable(ii);

                control.add(yiEq);

                /********************************************/

                /* Condition 1
                 * XmaxInv1T1-Z1i*Iinv1T1 <= 0
                 */
                yiCond1Eq = new Equation(node,getID(),node.toString()+"InvestmentFun",0,equation++,Equation.LOWEROREQUAL,(float)0);

                xMaxi = new Variable(xMaxiVarString+getID(),j+1,(float)1.0);
                ii = new Variable(IiVarString+getID(),j+1,(float)-getEnd(i));
                ii.setIsInteger(true);
                yiCond1Eq.addVariable(xMaxi);
                yiCond1Eq.addVariable(ii);
                control.add(yiCond1Eq);


                // If the begin of the slop is zero the equation (Xmaxi-Z0i*my*Ii >= 0) must generate only one time
                if(getBegin(i)== 0)
                    {
                    if(j!=0)
                    upprepning = true;
                    else
                    upprepning = false;
                    }

                if(!upprepning)
                {
                /* Condition 2
                 * Xmaxi-Z0i*my*Ii >= 0
                 */
                xMaxi = new Variable(xMaxiVarString+getID(),j+1/*timestep*/,(float)1.0);
                // XmaxInv1T1-Z0*my*Iinv1T1 >= 0
                yiCond2Eq = new Equation(node,getID(),node.toString()+"InvestmentFun",0,equation++,Equation.GREATEROREQUAL,(float)0);
                ii = new Variable(IiVarString+getID(),j+1,(float)-getBegin(i)*my);
                ii.setIsInteger(true);
                yiCond2Eq.addVariable(ii);
                yiCond2Eq.addVariable(xMaxi);
                control.add(yiCond2Eq);
                }
            }// END ELSE
        }// END FOR LOOP for the all time steps
      }// END FOR LOOP for the number of functions

       /*--------------------------------------------------*/

        /* The equation
	 * I0T1+I0T2+I0T3+...+I0Ti+I1T1+I1T2+...+I1Ti+I2T1+I2T2+�+I2Ti+...+InTi  <= 1
         *Heltal, s� att endast max en �slope� v�ljs:
          Iinv0T1+Iinv0T2+Iinv0T3+...+Iinv0Ti+Iinv1T1+Iinv1T2+...+Iinv1Ti+Iinv2T1+Iinv2T2+�+Iinv2Ti+...+IinvnTi <= 1

	 */
	Equation iiEq = new Equation( node, getID(),node.toString()+"InvestmentFun",0,equation++,Equation.LOWEROREQUAL,(float)1);

	/* Binary variables */
	//Variable ii;
    for(int j = 0; j<=getNrFunctions(); j++)
       {
       for (int i = 1; i <= maxTimesteps; i++)
          {
          if(j == 0)
            {
            if(getCostWhenNoInvest() != 0)
              {
              String IiVarString = new String("Iinv" + j);
              ii = new Variable(IiVarString+getID(),i,(float)1.0);
              ii.setIsInteger(true);
              iiEq.addVariable(ii);
              }
            }
          else
            {
            String IiVarString = new String("Iinv" + j);
            ii = new Variable(IiVarString+getID(),i,(float)1.0);
            ii.setIsInteger(true);
            iiEq.addVariable(ii);  
            }
          }
       }
	/* Ii equation done, add to control */
	control.add(iiEq);


        /*******************************************/
        /* The equation
         *
          Ser till s� att det inte g�r n�got fl�de i de tidssteg som ligger innan investering liksom efter tekniska livsl�ngden:
          QInvT1 = Iinv0T1+Iinv1T1+Iinv2T1+...+IinvnT1
          QInvT2 = Iinv0T2+Iinv1T2+Iinv2T2+...+IinvnT2
          QInvT3 = Iinv0T3+Iinv1T3+Iinv2T3+...+IinvnT3
          .
          .
          .
          QInvTi = Iinv0Ti+Iinv1Ti+Iinv2Ti+...+IinvnTi

	 */
	Equation qiEq ;

	/* Binary variables */
	Variable qi;
        for (int i = 0; i < maxTimesteps; i++)
            {
            qiEq = new Equation( node, getID(),node.toString()+"InvestmentFun",i,equation++,Equation.EQUAL,(float)0);
            qi = new Variable("QInv"+getID(),i+1, (float)1.0);
            qi.setIsInteger(true);
            qiEq.addVariable(qi);
            for(int j = 0; j<=getNrFunctions(); j++)
                {
                if(j == 0)
                  {
                  if(getCostWhenNoInvest() != 0)
                    {
                    String IiVarString = new String("Iinv" + j);
                    ii = new Variable(IiVarString+getID(),i+1,(float)-1.0);
                    ii.setIsInteger(true);
                    qiEq.addVariable(ii);
                    }
                  }
                else
                  {
                  String IiVarString = new String("Iinv" + j);
                  ii = new Variable(IiVarString+getID(),i+1,(float)-1.0);
                  ii.setIsInteger(true);
                  qiEq.addVariable(ii);  
                  }
                }
            /* Qi equation done, add to control */
            control.add(qiEq);
            }// END FOR
	/*
         ...........................................................
        Equations
        Begr�nsningsekvationer (beroende p� v�rdet p� Ri s� kan det g� ett fl�de genom respektive fl�de under respektive tidssteg)
        F1T1+	F2T1+� + FmT1 <= RInv1*U
        F1T2+F2T2+� + FmT2 <= RInv2*U
        .
        .
        .
        F1Ti+F2Ti+� + FmTi <= RInvi*U



        F1Ti+F2Ti+� + FmTi - Ri*U <= 0
         */

        for (int i = 0; i < maxTimesteps; i++)
         {
		/*
		  for every timestep, we need to generate a variable
		  First we find out the index of the cost
		  These two lines makes us use the same value in the vector
		  many times if we don't have enough information for all
		  timesteps in the model
		*/
		int index =  ((i * vectorsize) / maxTimesteps ) % vectorsize;
                TimestepInfo info = (TimestepInfo) c_timesteps.get(index);
                 //TimestepInfo inf = (TimestepInfo) c_timesteps.get(i);


		Vector inflowid = info.getInFlow();
		Vector outflowid = info.getOutFlow();

		/* Convert from string to id */
		stringToID( inflowid, inFlows);
		stringToID( outflowid, outFlows);
        Variable rMax;
        int timestep = i+1;

		/********************************************/

		/*
		 * Generate equation (one for each timestep i)
		 * F1Ti+F2Ti+...+FnTi<=R1*U
		 * (F1Ti+F2Ti+...+FnTi -R1*U <= 0 )
		 */
		Equation rTiEq = new Equation(node,getID(),node.toString()+"InvestmentFun",timestep,equation++,Equation.LOWEROREQUAL,(float)0);

		/* RT1...RTi */
		Variable rTi = new Variable("RInv"+getID(),timestep,(float)-1.0*c_infinity);
        rTi.setIsInteger(true);
                /* Add XTi to the XTi equation */
		rTiEq.addVariable(rTi);

		/* Used for both inflows and outflows */
		Variable fvar;

		/*
		 * Add one variable to the equation XTi for each
		 * "selected" inflow
		 * This means:
		 * F1Ti+F2Ti+...+FnTi<=R1*Uwhere all F1Ti flows are inflows
		 * Equation really looks like:
		 * F1Ti+F2Ti+...+FnTi -R1*U <= 0
		 */
		for(int fin = 0; fin<inflowid.size(); fin++)
		    {
			fvar = new Variable( (ID)inflowid.elementAt(fin),
					      timestep,(float)1.0);
			rTiEq.addVariable(fvar);
		    }

		/*
		 * Add one variable to the XTi equation for each "selected"
		 * outflow
		 */
		for(int fout = 0; fout<outflowid.size(); fout++)
		    {
			fvar = new Variable( (ID)outflowid.elementAt(fout),
					      timestep,(float)1.0);
			rTiEq.addVariable(fvar);
		    }

		/* Add equation to control */
            control.add(rTiEq);
      }// END FOR
        /*...................................................
        *
         Equations Q = maximum time steps
         Ser till s� att inget fl�de g�r genom �processen� i tidsstegen innan investeringen gjorts:
         (F�r alla i ska f�ljande ekvationer genereras s� l�nge som ((i-Q) >= 1))
         QInvTi - RInvT(i-1) <= 1
         QInvTi - RInvT(i-2) <= 1
            .
            .
            .
         QInvTi - RInvT(i-q) <=1

         QInvT2 - RInvT1 <= 1
         QInvT2 - RInvT2 <= 1
         .
         .
         .
         QInvT2 - RInvT(n-Q) <=1
         Must (n-Q) >= 1


         */
        Variable rTi;
            for (int i = 1; i <= maxTimesteps; i++)
            {
             for(int j = 0; j <= maxTimesteps; j++)
              {
                if((i - j) > 0)
                {
                 Equation rqTiEq = new Equation(node,getID(),node.toString()+"InvestmentFun",i,equation++,Equation.LOWEROREQUAL,(float)1.0);
                 qi = new Variable("QInv"+getID(),i, (float)1.0);
                 qi.setIsInteger(true);
                 rTi = new Variable("RInv"+getID(),i-j,(float)1.0);
                 rTi.setIsInteger(true);
                 rqTiEq.addVariable(qi);
                 rqTiEq.addVariable(rTi);
                 control.add(rqTiEq);
                }
                else
                    break;
              }
            }

        // Generate equations if technical lifespan is inserted
        if(c_technicallifespan >0)
        {
            /*
             Ber�kning av ti�

            Antag f�ljande:
            Teknisk livsl�ngd: 2 �r => t = 2
            Analysperiod: 4 �r => q = 4 (10 tidssteg => Q = 10)
            T1, T2, T3 och T4 representerar �ren
            �r1 = T1, T2 och T3 dvs = 3 tidsteg
            �r2 = T4 och T5 dvs = 2 tidsteg
            �r3 = T6, T7 och T8 dvs = 3 tidsteg
            �r4 = T9 och T10 dvs = 2 tidsteg

           t1� = 8 = (T1 - 0) + T2 + T3 = (3 � 0) + 2 + 3
           t2� = 7 = (T1 - 1) + T2 + T3 = (3 � 1) + 2 + 3
           t3� = 6 = (T1 - 2) + T2 + T3 = (3 � 2) + 2 + 3
           t4� = 7 = (T2 - 0) + T3 + T4 = (2 � 0) + 3 + 2
           t5� = 6 = (T2 - 1) + T3 + T4 = (2 � 1) + 3 + 2

           F�r t6�, t7�, t8�, t9� och t10� ska inga ekvationer anges, d.v.s. ekvationerna i ekvationen som anges med ** ovan ska genereras f�r alla tidssteg f�rutom de tidssteg som ligger sist i analysperioden som avg�rs av f�ljande funktion:
           Q � antalet tidssteg i de t �r som ligger sist i analysperioden

            rq2TiEq.addVariable(rTi);
            control.add(rq2TiEq);
            if( x < c_technicallifespan)
             x++;
            else
                break;
            if((x + i) > maxTimesteps)
                break;
            }
            }// END FOR*/

        
        int tpreemNumber = analyperiod - (int)c_technicallifespan;
        Vector tPreemVect = new Vector();
        if(tpreemNumber > 0)
         for(int i = 0; i < tpreemNumber;i++ )
         {
         int numberOfTimesteps = ((Long)timestepPerYear.get(i)).intValue();
         //if((i+2) < timestepPerYear.size() )
         for(int j =0; j < numberOfTimesteps; j++)
            {
            int tPreemi = numberOfTimesteps - j;//
            if(i+c_technicallifespan+1 < timestepPerYear.size())
               {
                for(int k = 0 ,m = i; k < c_technicallifespan; k++)
                    {
                    if( m++ < timestepPerYear.size() )
                        tPreemi = tPreemi + ((Long)timestepPerYear.get(m)).intValue();
                    }
                    tPreemVect.add(new Long(tPreemi));
               }
            else
                break;
            }
         }
        /* ......................................................
            (F�r alla i ska f�ljande ekvationer genereras s� l�nge som ((i+ti�+f)<=Q))
            Se nedan f�r information om hur l�nge ekvationerna ska genereras
            QInvTi + RInvT (i+ti�+0) <= 1
            QInvTi + RInvT (i+ti�+1) <= 1
            QInvTi + RInvT (i+ti�+2) <= 1
            .
            .
            .
            QInvTi + RInvT (i+ti�+f) <=1

            (i+ti+f)<= maxTimesteps
            
        */
       if(tpreemNumber > 0)
        for (int i = 1; i <= maxTimesteps; i++)
            {
            /*
             Qi + R(i+t+0) <= 1
             Qi + R(i+t+1) <= 1
             Qi + R(i+t+2) <= 1
             */
            if(i <= tPreemVect.size())
            {
            int inValue = ((Long)tPreemVect.get(i-1)).intValue();
            int x = i + inValue;
            if(x <= maxTimesteps)
              {
                while(x <= maxTimesteps)
                {
                Equation rqfTiEq = new Equation(node,getID(),node.toString()+"InvestmentFun",i,equation++,Equation.LOWEROREQUAL,(float)1.0);
                rTi = new Variable("RInv"+getID(),x, (float)1.0);
                rTi.setIsInteger(true);
                qi = new Variable("QInv"+getID(),i, (float)1.0);
                qi.setIsInteger(true);
                rqfTiEq.addVariable(qi);
                rqfTiEq.addVariable(rTi);
                control.add(rqfTiEq);
                x++;
                }
              }// END IF
            else
                break;
            }
            else
                break;
           }// END FOR
        }// END IF  technical lifespan is inserted

        

       //  Generate equations if economic lifespan is inserted
      if(c_economiclifespan > 0)
        {
            /*---------------------------------------------------------
             F�ljande funktioner ska inkluderas i m�lfunktionen (Om ingen ekonomisk livsl�ngd anges, ska nedanst�ende ekvationer inte genereras):
             RestY0T1+RestY1T1+RestY2T1+...+RestYnT1+...+RestY0T2+RestY1T2+RestY2T2+...
             + RestYnT2+RestY0T3+RestY1T3+RestY2T3+...+RestYnT3+...+RestYnTi
             *Följande funktioner ska inkluderas i målfunktionen (Om ingen ekonomisk livslängd anges, ska nedanstående ekvationer inte genereras):
            Resttot= RestY0T1+RestY1T1+RestY2T1+...+RestYnT1+...+RestY0T2+RestY1T2+RestY2T2+...+ RestYnT2+RestY0T3+RestY1T3+RestY2T3+...+RestYnT3+...+RestYnTi

             */
            // Calculate the percentage value
            float per = c_percentagescrapvalue/100;


            // The last annually rate
            //float rate = ((Float)rateVect.get(rateVect.size()-1)).floatValue();
            Equation restSumEq = new Equation( node, getID(),node.toString()+"InvestmentFun",1,equation++,Equation.EQUAL,0);

            /*
            * Y has same name independent of timestep
            */
            String restVaString = new String("RestYTot");
            Variable restTot = new Variable(restVaString+getID(),0,(float)-1.0);
            Variable RestYtot = new Variable(restVaString+getID(),0,(float)-1.0);
            obj.addVariable(restTot);
            //control.add(obj);
            /*********************************************/

            /* The restTot-variable in the investmentcost function, this must be
            * added to the global equation OBJ, which is the one
            * the optimizer tries to minimize
            */
            /* Add restTot to the restSumEq */
            restTot = new Variable(restVaString+getID(),0,(float)1.0);
            restSumEq.addVariable(restTot);
            for (int i = 1; i <= maxTimesteps; i++)
                {
                for(int j = 0; j<=getNrFunctions(); j++)
                   {
                   if(j == 0)
                     {
                     if(getCostWhenNoInvest() != 0)
                        {
                        String restVarString = new String("RestInvY" + j);
                        //String yiVarString = new String("YInv" +j);
                        //Variable Yi = new Variable(yiVarString+getID(),i,-per*rate);
                        Variable restyi = new Variable(restVarString+getID(),i,(float)-1);
                        //restSumEq.addVariable(Yi);
                        restSumEq.addVariable(restyi);
                        }
                      }
                   else
                       {
                        String restVarString = new String("RestInvY" + j);
                        //String yiVarString = new String("YInv" +j);
                        //Variable Yi = new Variable(yiVarString+getID(),i,-per*rate);
                        Variable restyi = new Variable(restVarString+getID(),i,(float)-1);
                        //restSumEq.addVariable(Yi);
                        restSumEq.addVariable(restyi);
                        }

                   }
                 }

            control.add(restSumEq);
            /*
               S� l�nge som (e-(q-(wi)) >=0) s� g�rs nedanst�ende ekvationer (i annat fall s�tts RestYnTi = 0) utifr�n tre alternativ:

                Alternativ 1: Anges �ven om varken RestFast eller RestProcent har angetts
                RestY0T1 = YInv0T1*(1/e) *(e-(q-w1)) * (1+r)-q
                RestY1T1 = YInv1T1*(1/e) *(e-(q-w1)) * (1+r)-q
                RestY2T1 = YInv2T1*(1/e) *(e-(q-w1)) * (1+r)-q
                .
                .
                .
                RestYnT1 = YInvnT1*(1/e) *(e-(q-w1)) * (1+r)-q

                RestY0T2 = YInv0T2*(1/e) *(e-(q-w2)) * (1+r)-q
                RestY1T2 = YInv1T2*(1/e) *(e-(q-w2)) * (1+r)-q
                RestY2T2 = YInv2T2*(1/e) *(e-(q-w2)) * (1+r)-q
                .
                .
                .
                RestYnT2 = YInvnT2*(1/e) *(e-(q-w2)) * (1+r)-q

                .
                .
                .

                RestYnTi = YInvnTi*(1/e) *(e-(q-(wi))) * (1+r)-q

                Gr�markerade ekvationer genereras endast om Iinv0Ti?? 0


                e is the economic life span
             *  q is the analyse period
             * 
             * Ber�kning av wi� 
            T1, T2, T3,T4 och T5 representerar �ren
            �r1 = T1, T2 och T3 dvs = 3 tidsteg
            �r2 = T4 och T5 dvs = 2 tidsteg
            �r3 = T6 och T7 dvs = 2 tidsteg
            �r4 = T8  dvs = 1 tidsteg
            �r5 = T9 och T10 dvs = 2 tidsteg 
            w1 = 1  
            w2 = 1 
            w3 = 1 
            w4 = 2 
            w5 = 2 
            w6 = 3 
            w7 = 3 
            w8 = 4 
            w9 = 5 
            w10 = 5 
             */

          // annualrate = (1+rate)power(- number of year)
          //double annrate = Math.pow((1+rate),-analyperiod);
            // The last annually rate
          float rate = ((Float)rateVect.get(rateVect.size()-1)).floatValue();
          //IF (user didn't insert percentage scrap value or fixed scrap value)
          for(int i = 1; i <= analyperiod;i++ )
               {
               int numberOfTimesteps = ((Long)timestepPerYear.get(i-1)).intValue();
               for(int j =0; j < numberOfTimesteps; j++)
                  {
                  wiVector.add(new Long(i));
                  }
               }
         for (int i = 1; i <= maxTimesteps; i++)
             {
             //S� l�nge som (e-(q-(wi)) >=0) s� g�rs nedanst�ende ekvationer (i annat fall s�tts RestYnTi = 0) utifr�n tre alternativ:
             //(1/e) *(e-(q-w1)) * (1+r)-q  -> koff1 = (e-(q-w1))      
             float koff1 = c_economiclifespan-(analyperiod-((Long)wiVector.get(i-1)).intValue());      
             float koff2 = (koff1 * rate) /c_economiclifespan;
                   
             if(koff1 >= 0)
               {
                        for(int j = 0; j<=getNrFunctions(); j++)
                         {
                         if(j == 0)
                            {
                            if(getCostWhenNoInvest() != 0)
                                {
                                Equation restYiTi = new Equation( node, getID(),node.toString()+"InvestmentFun",i,equation++,Equation.EQUAL,(float)0);
                                String restVarString = new String("RestInvY" + j);
                                String yiVarString = new String("YInv" +j);
                                Variable Yi = new Variable(yiVarString+getID(),i,-koff2);
                                Variable restyi = new Variable(restVarString+getID(),i,(float)1.0);
                                restYiTi.addVariable(Yi);
                                restYiTi.addVariable(restyi);
                                control.add(restYiTi);
                                }
                            }
                         else
                            {
                            Equation restYiTi = new Equation( node, getID(),node.toString()+"InvestmentFun",i,equation++,Equation.EQUAL,(float)0);
                            String restVarString = new String("RestInvY" + j);
                            String yiVarString = new String("YInv" +j);
                            Variable Yi = new Variable(yiVarString+getID(),i,-koff2);
                            Variable restyi = new Variable(restVarString+getID(),i,(float)1.0);
                            restYiTi.addVariable(Yi);
                            restYiTi.addVariable(restyi);
                            control.add(restYiTi);
                            }
                        }
                 } // END IF coff >= 0
                   // (i annat fall s�tts RestYnTi = 0)
             else
                {
                for(int j = 0; j<=getNrFunctions(); j++)
                   {
                    if(j == 0)
                      {
                      if(getCostWhenNoInvest() != 0)
                        {
                        Equation restYiTi = new Equation( node, getID(),node.toString()+"InvestmentFun",i,equation++,Equation.EQUAL,(float)0);
                        String restVarString = new String("RestInvY" + j);
                        Variable restyi = new Variable(restVarString+getID(),i,(float)1.0);
                        restYiTi.addVariable(restyi);
                        control.add(restYiTi);
                        }
                      }
                    else
                      {
                      Equation restYiTi = new Equation( node, getID(),node.toString()+"InvestmentFun",i,equation++,Equation.EQUAL,(float)0);
                      String restVarString = new String("RestInvY" + j);
                      Variable restyi = new Variable(restVarString+getID(),i,(float)1.0);
                      restYiTi.addVariable(restyi);
                     control.add(restYiTi);

                      }
                  }
                }
              } // END FOR each timesteps IF (user didn't insert percentage scrap value or fixed scrap value)
                // IF the fixed value of the scap inserted

          if((c_fastscrapvalue != 0) || (c_percentagescrapvalue != 0))
            {
            for(int i = 0, x = 0; i < analyperiod;i++ )
               {
               x++;
               int numberOfTimesteps = ((Long)timestepPerYear.get(i)).intValue();
               for(int j =0; j < numberOfTimesteps; j++)
                  {
                  int tDoubelPreemi = x + (int)c_technicallifespan;
                  if(tDoubelPreemi > analyperiod )
                    tDoubelPreemi = analyperiod;
                  tDoubelPreemVect.add(new Long(tDoubelPreemi));
                  }
               }
            } // END ELSE IF

          /*---------------------------------------------------------------------------
           * * Ber�kning av ti��

                   Antag f�ljande:
                   Teknisk livsl�ngd: 2 �r => t = 2
                   Analysperiod: 5 �r => q = 5 (10 tidssteg => Q = 10)
                   T1, T2, T3,T4 och T5 representerar �ren
                   �r1 = T1, T2 och T3 dvs = 3 tidsteg
                   �r2 = T4 och T5 dvs = 2 tidsteg
                   �r3 = T6 och T7 dvs = 2 tidsteg
                   �r4 = T8  dvs = 1 tidsteg
                   �r5 = T9 och T10 dvs = 2 tidsteg
                 * investering st�kar sig attid under det �r som man har gjort och tekniska livsl�ngden
                 * b�rjar alltid det �r som kommer efter investering
                   t1�� = 3  =  2 + 1 man r�knar alltid tillf�rsta �ret (to begining)
                   t2�� = 3  = 2 + 1
                   t3�� = 3 =2+1
                   t4�� = 4 = 2+2 man har gjort invistering i �r 2 d�r f�r b�rjar T.L.L fr�n �r 3 och str�kar sig till �r 4
                 * d� r�knar man fr�n �r 4 tillbaka till �r 1 dvs blir 2 + 2
                   t5�� = 4 =2+2
                   t6�� = 5 = 2+3
                   t7�� = 5 =2+3
                   t8�� = 5 =2+3  t8 liggar  �r 4 men analys �ret slutar efter ett �r d� vi kan k�ra till �r 5
                   t9�� = 5   t9 liggar  �r 5 men analys �ret slutar i �r 5 d� vi kan k�ra till �r 5
                   t10�� = 5 t10 liggar  �r 5 men analys �ret slutar i �r 5 d� vi kan k�ra till �r 5

           *
            Alternativ 2: om RestFast angetts formuleras f�ljande ekvationer s� l�nge som Ti<=Q)
            RestFastY0T1 = Iinv0T1 * RestFast * (1+r)-t1��
            RestFastY1T1 = Iinv1T1 * RestFast * (1+r)- t1��
            RestFastY2T1 = Iinv2T1 * RestFast * (1+r)- t1��
            .
            .
            .
            RestFastYnT1 = IinvnT1 * RestFast * (1+r)- t1��

            RestFastY0T2 = Iinv0T2 * RestFast * (1+r)-t2��
            RestFastY1T2 = Iinv1T2 * RestFast * (1+r)- t2��
            RestFastY2T2 = Iinv2T2 * RestFast * (1+r)- t2��
            .
            .
            .
            RestFastYnT2 = IinvnT2 * RestFast * (1+r)- t2��

            .
            .
            .

            RestFastYnTi = IinvnTi * RestFast * (1+r)- ti��


        ((Long)tDoubelPreemVect.get(i-1)).intValue()
        double annrate = Math.pow((1+rate),-analyperiod);
          */
          if(c_fastscrapvalue != 0)
           {
           Equation restFastEq = new Equation( node, getID(),node.toString()+"InvestmentFun",1,equation++,Equation.EQUAL,0);

            /*
            * Y has same name independent of timestep
            */
            String restFastString = new String("RestFastTot");
            Variable restFastTotal ;
            restFastTotal = new Variable(restFastString+getID(),0,(float)-1.0);
            obj.addVariable(restFastTotal);

            /*Beräkning för att begränsa tillgodogörandet av restvärdet
            Det visar sig av ekvationerna ovan att även om investeringen inte görs så kan modellresultatet tillgodogöra sig restvärden för investeringen. För att förhindra detta läggs följande ekvation in i modellen.

            YinvtotT0 - Resttot - RestFasttot - RestProctot >= 0*/

             Equation controlEquation = new Equation( node, getID(),node.toString()+"InvestmentFun",1,equation++,Equation.GREATEROREQUAL,0);
             controlEquation.addVariable(YInvtot);
             controlEquation.addVariable(RestYtot);
             controlEquation.addVariable(restFastTotal);
             control.add(controlEquation);

            //control.add(obj);
            /*********************************************/

            /* The restTot-variable in the investmentcost function, this must be
            * added to the global equation OBJ, which is the one
            * the optimizer tries to minimize
            */
            /* Add restTot to the restSumEq */
            restFastTotal = new Variable(restFastString+getID(),0,(float)1.0);
            restFastEq.addVariable(restFastTotal);

                for(int i = 1; i <= maxTimesteps; i++)
                   {
                   double anrate = Math.pow((1+actualRate),-((Long)tDoubelPreemVect.get(i-1)).intValue());
                   float koff1 = c_fastscrapvalue*(float)anrate;
                    
                   for(int j = 0; j<=getNrFunctions(); j++)
                      {
                      if(j == 0)
                       {
                       if(getCostWhenNoInvest() != 0)
                          {
                          Equation restYiTi = new Equation( node, getID(),node.toString()+"InvestmentFun",i,equation++,Equation.EQUAL,0);
                          String restVarString = new String("RestFastY" + j);
                          String yiVarString = new String("Iinv" +j);
                          Variable Yi = new Variable(yiVarString+getID(),i,-koff1);
                          Variable restyi = new Variable(restVarString+getID(),i,(float)1.0);
                          Variable restFastY = new Variable(restVarString+getID(),i,(float)-1.0);
                          restFastEq.addVariable(restFastY);
                          restYiTi.addVariable(Yi);
                          restYiTi.addVariable(restyi);
                          control.add(restYiTi);
                          }
                        }
                       else
                        {
                        Equation restYiTi = new Equation( node, getID(),node.toString()+"InvestmentFun",i,equation++,Equation.EQUAL,0);
                          String restVarString = new String("RestFastY" + j);
                          String yiVarString = new String("Iinv" +j);
                          Variable Yi = new Variable(yiVarString+getID(),i,-koff1);
                          Variable restyi = new Variable(restVarString+getID(),i,(float)1.0);
                          Variable restFastY = new Variable(restVarString+getID(),i,(float)-1.0);
                          restFastEq.addVariable(restFastY);
                          restYiTi.addVariable(Yi);
                          restYiTi.addVariable(restyi);
                          control.add(restYiTi);
                        }
                      } // END FOR
                   } // END FOR EACH TIMESTEP
                 control.add(restFastEq);
                }// END IF the fixed value of the scap is inserted
                /*----------------------------------------------------------------------
                 
                Alternativ 3: om RestProcent angetts formuleras f�ljande ekvationer s� l�nge som Ti<=Q)
                RestProcY0T1 = YInv0T1*RestProcent* (1+r)-t1��
                RestProcY1T1 = YInv1T1*RestProcent* (1+r)-t1��
                RestProcY2T1 = YInv2T1*RestProcent* (1+r)-t1��
                .
                .
                .
                RestProcYnT1 = YInvnT1*RestProcent*(1+r)-t1��

                RestProcY0T2 = YInv0T2*RestProcent* (1+r)-t2��
                RestProcY1T2 = YInv1T2*RestProcent* (1+r)-t2��
                RestProcY2T2 = YInv2T2*RestProcent* (1+r)-t2��
                .
                .
                .
                RestProcYnT2 = YInvnT2*RestProcent*(1+r)-t2��

                .
                .
                .

                RestProcYnTi = YInvnTi *RestProcent*(1+r)-ti��

                Gr�markerade ekvationer genereras endast om Iinv0Ti?? 0


                F�ljande funktioner ska inkluderas i m�lfunktionen (Om ingen �restprocent� angetts (och ingen ekonomisk livsl�ngd angetts), ska nedanst�ende ekvationer inte genereras):
                RestProcY0T1 + RestProcY1T1 + RestProcY2T1 + ... + RestProcYnT1 + RestProcY0T2 + RestProcY1T2 + RestProcY2T2 + ... + RestProcYnT2 + RestProcY0T3 + RestProcY1T3 + RestProcY2T3 + ... + RestProcYnT3 + RestProcYnTi

                 */
                // IF the percentage value of the scap is inserted
          if(c_percentagescrapvalue != 0)
            {
                // Calculate the percentage value
            float psv = c_percentagescrapvalue/100;
            Equation restPercEq = new Equation( node, getID(),node.toString()+"InvestmentFun",1,equation++,Equation.EQUAL,0);

            /*
            * Y has same name independent of timestep
            */
            String restPercString = new String("RestProcentTot");
            Variable restPerc ;
            restPerc = new Variable(restPercString+getID(),0,(float)-1.0);
            obj.addVariable(restPerc);

            /*Beräkning för att begränsa tillgodogörandet av restvärdet
            Det visar sig av ekvationerna ovan att även om investeringen inte görs så kan modellresultatet tillgodogöra sig restvärden för investeringen. För att förhindra detta läggs följande ekvation in i modellen.

            YinvtotT0 - Resttot - RestFasttot - RestProctot >= 0*/

             Equation controlEquation = new Equation( node, getID(),node.toString()+"InvestmentFun",1,equation++,Equation.GREATEROREQUAL,0);
             controlEquation.addVariable(YInvtot);
             controlEquation.addVariable(RestYtot);
             controlEquation.addVariable(restPerc);
             control.add(controlEquation);
            restPerc = new Variable(restPercString+getID(),0,(float)1.0);
            restPercEq.addVariable(restPerc);
            for (int i = 1; i <= maxTimesteps; i++)
              {
              double anrate = Math.pow((1+actualRate),-((Long)tDoubelPreemVect.get(i-1)).intValue());
              float koff1 = psv*(float)anrate;

              for(int j = 0; j<=getNrFunctions(); j++)
                  {
                   if(j == 0)
                            {
                            if(getCostWhenNoInvest() != 0)
                               {
                                Equation restYiTi = new Equation( node, getID(),node.toString()+"InvestmentFun",i,equation++,Equation.EQUAL,0);
                                String restVarString = new String("RestProcY" + j);
                                String yiVarString = new String("YInv" +j);
                                Variable Yi1 = new Variable(yiVarString+getID(),i,-koff1);
                                Variable restyi = new Variable(restVarString+getID(),i,(float)1.0);
                                restPerc = new Variable(restVarString+getID(),i,(float)-1.0);
                                restPercEq.addVariable(restPerc);
                                restYiTi.addVariable(restyi);
                                restYiTi.addVariable(Yi1);
                                control.add(restYiTi);
                                }
                            }
                          else
                            {
                            Equation restYiTi = new Equation( node, getID(),node.toString()+"InvestmentFun",i,equation++,Equation.EQUAL,0);
                            String restVarString = new String("RestProcY" + j);
                            String yiVarString = new String("YInv" +j);
                            Variable Yi1 = new Variable(yiVarString+getID(),i,-koff1);
                            Variable restyi = new Variable(restVarString+getID(),i,(float)1.0);
                            restPerc = new Variable(restVarString+getID(),i,(float)-1.0);
                            restPercEq.addVariable(restPerc);
                            restYiTi.addVariable(restyi);
                            restYiTi.addVariable(Yi1);
                            control.add(restYiTi);
                            }
                          } // END FOR
                        } // END FOR EACH TIME STEP
                    control.add(restPercEq);
                }// END IF */

       }// END IF (if economic lifespan is inserted)



     return control;
     // END ELSE
   }


   
    /**
     * This parses data and initializes this function with values.
     * The linked list of data looks like this:
     *    'label' -> data
     *    '
     *    'c' -> number
     * <iterative>
     *     T
     *     <iterative>
     *        'flow_in' -> string
     *     </iterative>
     *     <iterative>
     *        'flow_out' -> string
     *     </iterative>
     * </iterative>
     * <iterative>
     *    'costfunlist'
     *    'start' -> number
     *    'end' -> number
     *    'slope' -> number
     *    'offset' -> number
     * </iterative>
     * @param data A linked list with data to parse. With this
     * data we initialize this function's settings.
     * @param rc A control with all available resources.
     * @param createMissingResources Is used when this function
     * should have a resource that is not globally defined. Is true
     * if the resource should be globally created, false it it
     * should be ignored.
     */
    public void parseData(LinkedList data, ResourceControl rc,
			  boolean createMissingResources)
        throws RmdParseException
    {
        boolean emptyStringflag = false;
	setTimesteplevel((Timesteplevel) data.removeLast());

	/* first we get the label of the function */
	if (((String) data.getFirst()).equals("label")) {
	    data.removeFirst(); //Throw away the tag
	    setLabel((String)data.removeFirst());
        }
        // Added by Nawzad Mardan 2008-06-27
        if (((String) data.getFirst()).equals("technicallifespan"))
            {
                /* remove tag "technicallifespan" */
                data.removeFirst();
                try 
                {
                    c_technicallifespan = Float.parseFloat((String) data.removeFirst());
                } 
                catch (NumberFormatException e) 
                {
                    throw new RmdParseException("The 'technicallifespan' field must be a float > 0");
                }
                if (c_technicallifespan < 0) 
                {
                    throw new RmdParseException("The 'technicallifespan' field must be a float > 0");
                }
            }

        if (((String) data.getFirst()).equals("economiclifespan"))
            {
                /* remove tag "economiclifespan" */
                data.removeFirst();
                try 
                {
                    c_economiclifespan = Float.parseFloat((String) data.removeFirst());
                } 
                catch (NumberFormatException e) 
                {
                    throw new RmdParseException("The 'economiclifespan' field must be a float > 0");
                }
                if (c_economiclifespan < 0) 
                {
                    throw new RmdParseException("The 'economiclifespan' field must be a float > 0");
                }
            }

            if (((String) data.getFirst()).equals("annualRateValue"))
            {

                data.removeFirst();
                try
                {
                    c_annualratevalue = Float.parseFloat((String) data.removeFirst());
                    c_annualrateCBState = true;
                }

                catch (NumberFormatException e)
                {
                    throw new RmdParseException("The 'annual rate value' field must be a float > 0");
                }
                if (c_annualratevalue < 0)
                {
                    throw new RmdParseException("The 'annual rate value' field must be a float > 0");
                }
            }
        if (((String) data.getFirst()).equals("percentagescrapvalue"))
            {
                /* remove tag "percentagescrapvalue" */
                data.removeFirst();
                try 
                {
                    c_percentagescrapvalue = Float.parseFloat((String) data.removeFirst());
                    if(c_percentagescrapvalue != 0)
                        c_percentageValueCB = true;
                } 
                catch (NumberFormatException e) 
                {
                    throw new RmdParseException("The 'percentage scrap value' field must be a float > 0");
                }
                /*if (c_percentagescrapvalue< 0)
                {
                    throw new RmdParseException("The 'percentage scrap value' field must be a float > 0");
                }*/
            }
        
        if (((String) data.getFirst()).equals("fastscrapvalue"))
            {
                /* remove tag "percentagescrapvalue" */
                data.removeFirst();
                try 
                {
                    c_fastscrapvalue = Float.parseFloat((String) data.removeFirst());
                    if(c_fastscrapvalue != 0)
                        c_fixedValueCB = true;
                } 
                catch (NumberFormatException e) 
                {
                    throw new RmdParseException("The 'fixed scrap value' field must be a float > 0");
                }
                /*if (c_fastscrapvalue < 0)
                {
                    throw new RmdParseException("The 'fixed scrap value' field must be a float > 0");
                }*/
            }
        
      
    
            /* next should be the cost when no invest
             * variable "c"
             */
            if(((String) data.getFirst()).equals("c"))
            {
                /* remove tag "c" */
                data.removeFirst();

                try
                {
                    setCostWhenNoInvest(Float.parseFloat((String) data.removeFirst()));
                }
                catch (NumberFormatException e)
                {
                    throw new RmdParseException("The investmentcost data " +
                                                "is incorrect. <c>" +
                                                "tag contains a non-number " +
                                                "string, should be a number.");
                }
            }


        /* empty the default c_timesteps */
	c_timesteps.clear();

        /* for every timestep */
	for (int i = 0; data.size() > 0 &&
		 ((String) data.getFirst()).equals("T"); i++)
        {
            data.removeFirst(); // remove the "T"

            /* now go through registered flows */
            TimestepInfo info = new TimestepInfo();
            Vector flowin = new Vector();
            Vector flowout = new Vector();

            /* in flows are first */
            for (int j = 0; data.size() > 0 &&
		 ((String) data.getFirst()).equals("flowid_in"); j++)
	    {
		try {
                    if (((String) data.getFirst()).equals("flowid_in")) {
			data.removeFirst();
                        flowin.addElement((String) data.removeFirst());
                    }
		}
		catch (NumberFormatException e)
		{
                    throw new RmdParseException("The flow equation data " +
                                                "is incorrect. <flowid_in> " +
						"tag contains invalid data.");
                }
            }

            /* out flows comes next */
            for (int j = 0; data.size() > 0 &&
		 ((String) data.getFirst()).equals("flowid_out"); j++)
            {
		try {
		    if (((String) data.getFirst()).equals("flowid_out")) {
			data.removeFirst();
                	flowout.addElement((String) data.removeFirst());
                    }
		}
		catch (NumberFormatException e)
		{
                    throw new RmdParseException("The flow equation data " +
                                                "is incorrect. <flowid_out> " +
						"tag contains invalid data.");
                }
            }


            info.setInFlow(flowin);
            info.setOutFlow(flowout);

            /* add to vector of timestepinfo */
            c_timesteps.addElement(info);
        }

	/* investment cost function */
	for (int i = 0; data.size() > 0 &&
		 ((String) data.getFirst()).equals("costfunlist"); i++)
	    {
		data.removeFirst(); //remove costfunlist

		/* add function (one more interval)*/
		addCostFunction();


		try
		    {
			if(((String) data.getFirst()).equals("start"))
			    {
				data.removeFirst();
				setBegin(i+1,
					 Float.parseFloat((String) data.removeFirst()));
			    }

			if (((String) data.getFirst()).equals("end"))
			    {
				data.removeFirst();
				setEnd(i+1,
				       Float.parseFloat((String) data.removeFirst()));
			    }
			if (((String) data.getFirst()).equals("slope"))
			    {
				data.removeFirst();
				setSlope(i+1,
					 Float.parseFloat((String) data.removeFirst()));
			    }
			if (((String) data.getFirst()).equals("offset"))
			    {
				data.removeFirst();
				setOffset(i+1,
					  Float.parseFloat((String) data.removeFirst()));
			    }

		    }
		catch (NumberFormatException e)
		    {
			throw new RmdParseException("The investmentcost data " +
						    "is incorrect. <start>,<end>, " +
						    "<slope>, or <offset> " +
						    "tag contains a non-number " +
						    "string, should be a number.");
		    }
	    } /* end loop over costfunlist */





    }



    /* Remove this and replace with getTimesteps */
    private int getSize()
    {
	return getTimesteps();
    }

    /**
     * Returns the number of timesteps
     * @return number of timesteps
     */
    protected int getTimesteps()
    {
	return c_timesteps.size();
    }

    /**
     * Inserts empty timestepinfo into a specified
     * index position.
     * @param index The index to insert at.
     */
    protected void timestepInsertAt(int index)
    {
	c_timesteps.insertElementAt(new TimestepInfo(), index);
    }

    /**
     * Removes timestepinfo at specified
     * index position.
     * @param index The index.
     */
    protected void timestepRemoveAt(int index)
    {
	c_timesteps.removeElementAt(index);
    }

    /**
     * Increases the number of timesteps. Old timestep
     * info will be copied into the new ones.
     * @param factor Gives the number of new timesteps
     * to be added for every old one.
     */
    protected void timestepSetMoreDetailed(int factor)
    {
	int oldsize = c_timesteps.size();
	int newsize = oldsize * factor;

	//Copy old cost values to new cost array
	Vector newTimestepInfo = new Vector(newsize,1);
	TimestepInfo info;
	for(int i = 0; i < oldsize; i++) {
	    info = ((TimestepInfo) c_timesteps.get(i));
	    for(int k = 0; k < factor; k++) {
		newTimestepInfo.add(new TimestepInfo(info));
	    }
	}
	c_timesteps = newTimestepInfo;
    }

    /**
     * Reduces the timestepinformation. A factor gives
     * which timesteps to be saved.
     * @param newSize The size of the new vector to hold
     * timestep information.
     * @param factor Gives wich of the timesteps to save.
     */
    protected void timestepSetLessDetailed(int newSize, int factor)
    {
	Vector newTimestepInfo = new Vector(newSize);
	int i, oldindex;

	//Copy old cost values to new cost array
	for(i = 0, oldindex = 0; i < newSize; i++, oldindex += factor)
	    newTimestepInfo.add(c_timesteps.get(oldindex));

	c_timesteps = newTimestepInfo;
    }

    /**
     * Clears all timestep data.
     * @param size Size of vector to hold
     * timestep information.
     */
    protected void timestepResetData(int size)
    {
	//Create vector and reset to zero
	c_timesteps = new Vector(size);
	for(int i = 0; i < size; i++)
	    c_timesteps.add(new TimestepInfo());

	/* reset data for every costfunction */
	c_costFunction = new Vector(0);
	c_costWhenNoInvest = 0;
    }


    /*
     * Convert the id represented as a string into instance of ID. What happens if inFlows mismatch with
     * the inflowid?
     */
    private void stringToID(Vector flowid, Vector flows){
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
// Inner class    Added by Nawzad Mardan 080626
  
    /**
     * Gets the timesteps value.
     * @return The timestep values as a Vector.
     */
    
    public Vector getTimestepValues()
    {
        return c_timestepValues;
    }
    
    /**
     * Sets the variable's tims to the Timestep vector
     * @param  tims The  timestep to be set
     */
    public void setTimestepValue( Vector tims)
    {
       c_timestepValues = tims; 
    }
    
  /**
     * Gets the technical lifespan value.
     * @return The technical lifespan values as a float.
     */
    
    public float getTechnicalLife()
    {
        return c_technicallifespan;
    }
    
    /**
     * Sets the variable's technicallifespan to the technical lifespan
     * @param  technicallifespan The  technical lifespan to be set
     */
    public void setTechnicalLife( float technicallifespan)
    {
       c_technicallifespan = technicallifespan; 
    }


    /**
     * Gets the annual  rate CB State value.
     * @return The annualrates checkboxs values as a boolean.
     */

    public boolean getAnnualrateCBState()
    {
        return c_annualrateCBState;
    }

    /**
     * Sets the variable's technicallifespan to the technical lifespan
     * @param  technicallifespan The  technical lifespan to be set
     */
    public void setAnnualrateCBState( boolean anCBS)
    {
       c_annualrateCBState = anCBS;
    }


    /**
     * Gets the fixed  CheckBox State value.
     * @return The fixed checkboxs values as a boolean.
     */

    public boolean getFixedCBState()
    {
        return c_fixedValueCB;
    }

    /**
     * Sets the variable's technicallifespan to the technical lifespan
     * @param  fxCBS The c_fixedValueCB to be set
     */
    public void setFixedCBState( boolean fxCBS)
    {
       c_fixedValueCB = fxCBS;
    }
    /**
     * Gets the percentage  CheckBox State value.
     * @return The percentage checkboxs values as a boolean.
     */

    public boolean getPercentageCBState()
    {
        return c_percentageValueCB;
    }

    /**
     * Sets the variable's pxCBS to the c_percentageValueCB
     * @param  fxCBS The c_fixedValueCB to be set
     */
    public void setPercentageCBState( boolean pxCBS)
    {
       c_percentageValueCB = pxCBS;
    }
    /**
     * Gets the discount system cost value.
     * @return The discount system cost value as a boolean.
     */

    public boolean getDiscountSystemCost()
    {
        return c_discountSystemCost;
    }

    /**
     * Sets the variable's dsc to the c_discountSystemCost
     * @param  dsc The c_discountSystemCost to be set
     */
    public void setDiscountSystemCost( boolean dsc)
    {
       c_discountSystemCost = dsc;
    }
    /**
     * Gets the economicl lifespan value.
     * @return The economic lifespan values as a float.
     */
    
    public float getEconomicalLife()
    {
        return c_economiclifespan;
    }
    
    /**
     * Sets the variable's economicllifespan to the technical lifespan
     * @param  economicllifespan The  economic lifespan to be set
     */
    public void setEconomicalLife( float economiclifespan)
    {
       c_economiclifespan = economiclifespan; 
    }
     
  
    /**
     * Gets the persentage value of the scrap.
     * @return The persentage value of the scrap as a float.
     */
    
    public float getPercentageValueOfScrap()
    {
        return c_percentagescrapvalue ;
    }
    
    /**
     * Sets the variable's percentagescrapvalue to the pvs
     * @param  pvs is  the  persentage value of the scrap to be set
     */
    public void setPercentageValueOfScrap( float pvs)
    {
       c_percentagescrapvalue = pvs; 
    }

     /**
     * Gets the persentage value of the annual rate.
     * @return The c_annualratevalue value  as a float.
     */

    public float getAnnualRateValue()
    {
        return c_annualratevalue ;
    }

    /**
     * Sets the variable's c_annualratevalue to the anv
     * @param  anv is  the  anuual rate value  to be set
     */
    public void setAnnualRateValue( float anv)
    {
       c_annualratevalue = anv;
    }
 /**
     * Gets the Fixed value of the scrap.
     * @return The Fixed value of the scrap as a float.
     */
    
    public float getFixedValueOfScrap()
    {
        return c_fastscrapvalue ;
    }
    
    /**
     * Sets the variable's fastscrapvalu to the fvs
     * @param  fvs is  the  Fixed value of the scrap to be set
     */
    public void setFixedValueOfScrap( float fvs)
    {
       c_fastscrapvalue = fvs; 
    }
    
   
    private Vector calculateAnnualRate(int analysPeriod, float rateValue)
    {
        double annualRate;
        Vector annualRateVector = new Vector();

        // Chang rate from integer to foat
       
        for(int i = 1; i<= analysPeriod;i++)
        {
            // annualrate = (1+rate)power(- number of year)
            annualRate = Math.pow((1+rateValue),-i);
            annualRateVector.add(new Float(annualRate));

        }
        //annualRateVector.add(0, new Float(1.0));
        return annualRateVector;
    }

 private Vector CalculateTimestepPerYear(Object [][] data)
    {
    Vector tspy = new Vector();
    for (int i = 0; i < data.length; i++)
       {
       int counter = 0;
       int timePeriodLength = data[i].length;
           // Each year have his own time steps
       for(int j= 1; j< timePeriodLength; j++)
          {
          if(!data[i][j].equals(""))
             counter++;
          }
      tspy.add(new Long(counter));
                      // END IF checking
       }
    return tspy;
    }
}
