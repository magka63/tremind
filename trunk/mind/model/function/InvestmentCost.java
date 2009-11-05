/*
 * Copyright 2003:
 * Almsted Åsa <asaal288@student.liu.se>
 * Anliot Manne <manan699@student.liu.se>
 * Fredriksson Linus <linfr529@student.liu.se>
 * Gylin Mattias <matgy024@student.liu.se>
 * Sjölinder Mattias <matsj509@student.liu.se>
 * Sjöstrand Johan <johsj438@student.liu.se>
 * Åkerlund Anders <andak893@student.liu.se>
 *
 * Copyright 2007:
 * Per Fredriksson <perfr775@student.liu.se>
 * David Karlslätt <davka417@student.liu.se>
 * Tor Knutsson	<torkn754@student.liu.se>
 * Daniel Källming <danka053@student.liu.se>
 * Ted Palmgren <tedpa175@student.liu.se>
 * Freddie Pintar <frepi150@student.liu.se>
 * Mårten Thurén <marth852@student.liu.se>  *
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

import java.util.LinkedList;
import java.util.Vector;
import java.util.Iterator;

import mind.model.*;
import mind.io.*;
import mind.model.function.helpers.*;


/**
 * The function InvestmentCost
 *
 * @author Johan Sjöstrand
 * @author Freddie Pintar
 * @author Tor Knutsson
 * @version 2007-12-12
 */
public class InvestmentCost
    extends NodeFunction
    implements Cloneable
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

	TimestepInfo info = new TimestepInfo();
	c_timesteps.add(info);
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
    public Object clone()
	throws CloneNotSupportedException
    {
	InvestmentCost clone = (InvestmentCost) super.clone();
	c_timesteps = new Vector(clone.c_timesteps);
	c_costFunction = new Vector(clone.c_costFunction);
	c_costWhenNoInvest = (float)clone.c_costWhenNoInvest;

	return clone;
    }

    public int addEquationOfTimestep(EquationControl control,
                                      int timestep, int index, ID node,
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
					      Vector inFlows, Vector outFlows)
	throws ModelException
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
	Equation xMaxSumEq = new Equation( node, getID(), 0,
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

	Equation ySumEq = new Equation( node, getID(),0,
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
	Equation iiEq = new Equation( node, getID(),0,
					equation++,Equation.EQUAL,(float)1);

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
            yiEq = new Equation(node,getID(),0,
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
                yiCond1Eq = new Equation(node,getID(),0,
                		 equation++,Equation.LOWEROREQUAL,(float)0);
                yiCond2Eq = new Equation(node,getID(),0,
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
	setTimesteplevel((Timesteplevel) data.removeLast());

	/* first we get the label of the function */
	if (((String) data.getFirst()).equals("label")) {
	    data.removeFirst(); //Throw away the tag
	    setLabel((String)data.removeFirst());
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
}
