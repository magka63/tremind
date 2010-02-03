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

/*
 * InvestmentCostDialog.java
 *
 * Created on November 17, 2003, 6:42 PM
 */

package mind.gui.dialog;

import java.awt.*;
import java.util.Vector;
import javax.swing.*;
import mind.gui.*;
import mind.model.*;
import mind.model.function.*;
//
import java.awt.event.*;
import javax.swing.JDialog;

/**
 * A dialog used for handling Investment cost properties.
 * @author  Per Fredriksson
 * @author 	Tor Knutsson
 * @version 2007-12-10 
 * @author  pum7
 */
public class InvestmentCostDialog extends mind.gui.dialog.FunctionDialog {
    /* Various variables for this node */
    private ID c_nodeID;
    private GUI c_gui;
    private InvestmentCost c_function;
    private int c_nrLimits = 1;
    private Timesteplevel c_tsl[];
    private int c_timestep; //Active timestep
    private int c_timesteplevels; //Number of timesteplevels
    private int c_maxTimeSteps;
    private Vector c_timeStepValues;

    /* Dynamic GUI variables */
    private javax.swing.JLabel lblTSL[];
    private SpinButton spinTSL[];
 
    /* Data views */
    private JPanel c_pnlLimits = new JPanel();
    private JList c_lstAvailInflow = new JList();
    private JList c_lstSelectedInflow = new JList();
    private JList c_lstAvailOutflow = new JList();
    private JList c_lstSelectedOutflow = new JList();

    /* Function value size constants */
    private final int C_ENTRY_SIZE = 4;
    private final int BEGIN_ENTRY_SIZE = 4;
    private final int END_ENTRY_SIZE = 4;
    private final int OFFSET_ENTRY_SIZE = 4;
    private final int SLOPE_ENTRY_SIZE = 4;
   
    //private DiscountedsystemcostDialog c_discountedsystemcostDialog;

    /*
     * Used to data about which flows that are
     * registered in each timestep
     */
    private Vector c_flowinfo = null;

    /*
     * Info about the cost function relation
     */
    private Vector c_begin;
    private Vector c_end;
    private Vector c_slope;
    private Vector c_offset;

    /*
     * The cost when no investment is made */
    private PositiveNumberField c_costWhenNoInvest;

    private final String UPPER_INVALID_TITLE =
	"Invalid upper value(s)";
    private final String UPPER_INVALID_MESSAGE =
	"The upper values specified are incorrect,\n" +
	"therefore these equations could not be saved.\n" +
	"The upper limit must be bigger than the lower limit.";

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

        void convertToID()
        {
            Vector new_c_outFlow = new Vector(0);
            for(int i = 0; i <c_outFlow.size(); i++)
            {
                ID theId = (ID)parseOutId((String)c_outFlow.get(i).toString());
                new_c_outFlow.addElement((ID) theId);
            }
            setOutFlow(new_c_outFlow);

            Vector new_c_inFlow = new Vector(0);
            for(int i = 0; i <c_inFlow.size(); i++)
            {
                ID theId = (ID)parseInId((String)c_inFlow.get(i).toString());
                new_c_inFlow.addElement((ID) theId);
            }
            setInFlow(new_c_inFlow);

        }
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

	/**
	 * Clears both inflows and
	 * outflows
	 */
	void clear()
	{
	    c_inFlow.clear();
	    c_outFlow.clear();
        }

        /**
         * Add inflow
         * @param flowId ID of inflow to add
         * @return true if successful
         */
        boolean addInFlow(ID flowId)
        {
             if(flowId == null || c_inFlow.contains(flowId))
                return false;

             c_inFlow.addElement(flowId);
             return true;
        }

        /**
         * Add outflow
         * @param flowId ID of outflow to add
         * @return true if successful
         */
        boolean addOutFlow(ID flowId)
        {
             if(flowId == null || c_outFlow.contains(flowId))
                return false;

             c_outFlow.addElement(flowId);
             return true;
        }

        /**
         * Remove inflow
         * @param flowId ID of inflow to remove
         * @return true if successful
         */
        boolean removeInFlow(ID flowId)
        {
             if(flowId == null)// || !c_inFlow.contains(flow))
                return false;

             return c_inFlow.removeElement(flowId);
        }

        /**
         * Remove outflow
         * @param flowId ID of outflow to remove
         * @return true if successful
         */
        boolean removeOutFlow(ID flowId)
        {
             if(flowId == null) //|| !c_outFlow.contains(flowId))
                return false;

             return c_outFlow.removeElement(flowId);
        }
    } /* End class TimestepInfo */

    /**
     * Creates new form InvestmentCostDialog.
     * The parameters are the same as for other functions.
     */
    public InvestmentCostDialog(JDialog parent, boolean modal,
                                ID nodeID, NodeFunction function, GUI gui) {
	super (parent, modal);
        c_parent = parent;
        c_modal = modal;
        /* Init structures */
	c_flowinfo = new Vector();

	c_begin = new Vector();
	c_end = new Vector();
	c_slope = new Vector();
	c_offset = new Vector();

	/* Init structures from "above" */
	c_gui = gui;
	c_nodeID = nodeID;
	c_function = (InvestmentCost) function;

	/* Start dialog with top time step */
        c_timestep = 1;
	c_function.setTimestep(c_timestep);

    Timesteplevel tsl = gui.getTopTimesteplevel();

	if(tsl.getNextLevel() == null)
            c_maxTimeSteps = 0;

        while ((tsl = tsl.getNextLevel()) != null)
            {
             if(tsl.getNextLevel() == null)
             {
                c_timeStepValues = tsl.getLengthsVector();
             }
            c_maxTimeSteps *= tsl.getTimesteps();

            //lblString = tsl.getLabel();
            }
        

        /* Load values from function */
	load();

        /* Init dialog */
        c_pnlLimits.setLayout(new GridBagLayout());
        initComponent();
        initDynamicComponents();

        scrollAvailInflow.getViewport().setView(c_lstAvailInflow);
        scrollSelectedInflow.getViewport().setView(c_lstSelectedInflow);
        scrollAvailOutflow.getViewport().setView(c_lstAvailOutflow);
        scrollSelectedOutflow.getViewport().setView(c_lstSelectedOutflow);
        scrollEquations.getViewport().setView(c_pnlLimits);

	txtLabel.setText(c_function.getLabel());

	/* Put content in dialog */
        initAvailableFlows();
        updateTimestep();
	updateLimitPanel();

	pack();
        pack();
    }

    /**
     * Set available flows in the flow lists.
     */
    private void initAvailableFlows()
    {
	Flow[] availInFlow = c_gui.getInFlows(c_nodeID);
	Flow[] availOutFlow = c_gui.getOutFlows(c_nodeID);

	String[] availInFlowString =  convertToString(availInFlow);
	String[] availOutFlowString = convertToString(availOutFlow);

        c_lstAvailInflow.setListData(availInFlowString);
        c_lstAvailOutflow.setListData(availOutFlowString);

        if(availInFlowString[0].equals("No flow"))
            btnAddInflow.setEnabled(false);

        if(availOutFlowString[0].equals("No flow"))
            btnAddOutflow.setEnabled(false);
    }

    /**
     * Load data for current timestep
     */
    public void load()
    {
       	//Load number of timestepslevels in the function
	c_timesteplevels = 1;
	Timesteplevel level = c_gui.getTopTimesteplevel();
	Timesteplevel thisLevel = c_function.getTimesteplevel();
	c_timesteplevels = thisLevel.toInt() + 1;

	//Load all timesteplevels
	c_tsl = new Timesteplevel[c_timesteplevels];
	level = c_gui.getTopTimesteplevel();
	for(int j = 0; j < c_timesteplevels; j++) {
	    c_tsl[j] = level;
	    level = level.getNextLevel();
	}

	//Load "cost_when_no_invest"
        c_costWhenNoInvest = new PositiveNumberField(c_function.getCostWhenNoInvest(),C_ENTRY_SIZE);

        //Load selected in- and outflows for each time step
	level = c_gui.getTopTimesteplevel();
        thisLevel = c_function.getTimesteplevel();
	int timesteps = thisLevel.timestepDifference(level);
	for (int i = 0; i < timesteps; i++) {
	    c_function.setTimestep(i+1);
	    TimestepInfo info = new TimestepInfo();

            info.setInFlow((Vector)c_function.getInFlow().clone());
            info.setOutFlow((Vector)c_function.getOutFlow().clone());
            /* Make sure the flows are represented as ID */
            info.convertToID();
            c_flowinfo.addElement(info);
        }
        c_function.setTimestep(c_timestep);

        //Load function values
        for(int i = 0; i<c_function.getNrFunctions(); i++)
        {
            float begin = c_function.getBegin(i+1);
            float end = c_function.getEnd(i+1);
            float slope = c_function.getSlope(i+1);
            float offset = c_function.getOffset(i+1);

            addCostFunction(new PositiveNumberField(begin,BEGIN_ENTRY_SIZE),
                            new NumberField(end,END_ENTRY_SIZE),
                            new NumberField(slope,SLOPE_ENTRY_SIZE),
                            new NumberField(offset,OFFSET_ENTRY_SIZE));
        }
    }

    /**
     * Save the data (for current timestep) to function
     * @return true if successful
     */
    private boolean save()
    {
	/*
	 * First save the investment cost function
	 */
	float begin;
	float end;
	float offset;
	float slope;
        float c;
        boolean flag = true;

	boolean valid = true;

	/* former "end"*/
	float formerEnd = 0;
        
        if(technicallifespanField.getFloatValue() < 0)
                {
                 JOptionPane.showMessageDialog(null, "Please enter correct value of the technical lifespan.\n "+"The 'Lifespan' field must be  > 0",
					"Lifespan <= 0", JOptionPane.WARNING_MESSAGE);
                valid = false;
                return false;
                }
        if(economiclifespanField.getFloatValue() < 0)
                {
                 JOptionPane.showMessageDialog(null, "Please enter correct value of the economic lifespan.\n "+"The 'Lifespan' field must be  > 0",
					"Lifespan <= 0", JOptionPane.WARNING_MESSAGE);
                 valid = false;
                 return false;
                }
         if(fixedscrapvalueField.getFloatValue() < 0)
                {
                 JOptionPane.showMessageDialog(null, "Please enter correct value for the fixed value of the scrap .\n "+"The 'Fixed value of scrap' field must be  > 0",
					"Fixed valu <= 0", JOptionPane.WARNING_MESSAGE);
                valid = false;
                return false;
                }
        
        if(percentagescrapvalueField.getFloatValue() < 0)
                {
                 JOptionPane.showMessageDialog(null, "Please enter correct value for the precentage value of the scrap .\n "+"The 'Precentage value of the scrap' field must be  > 0",
					"Precentage valu <= 0", JOptionPane.WARNING_MESSAGE);
                valid = false;
                return false;
                }
        /*if(!c_function.getDiscounsystemStaus())
        {
        if((economiclifespanField.getFloatValue() > 0)|| (technicallifespanField.getFloatValue() > 0))
               {
                JOptionPane.showMessageDialog(null, "Pleas insert length of analyses period and the mount of the annually interest. ",
					"Empty Interest", JOptionPane.WARNING_MESSAGE);  
                return false;
               }
        }*/
            
	/* Get the cost when no invest */
	c = ((NumberField) getCostWhenNoInvest()).getFloatValue();

	/* Check that "begin" is smaller than "end", and
	 * that the "begin" of the next function is larger
	 * than the "end" of the previous
	 */

        /* We let "formerEnd" of the first function be
         * the same as "begin" value of the first function
         */
        if(getNrFunctions() > 0)
           formerEnd = ((NumberField) getBegin(1)).getFloatValue();

	for(int j = 1; (j <= getNrFunctions() && valid); j++)
	    {
		begin = ((NumberField) getBegin(j)).getFloatValue();
		end = ((NumberField) getEnd(j)).getFloatValue();

		if((end <= begin) ||
		   (begin < formerEnd))
		    valid = false;

		formerEnd = end;
	    }

	if(!valid)
	    {
                showWarningDialog(UPPER_INVALID_TITLE, UPPER_INVALID_MESSAGE);

		return false;
	    }

	c_function.setCostWhenNoInvest(c);

        /* Remove old data from function */
        c_function.clearAllFunctions();

	for(int j = 1; j <= getNrFunctions(); j++)
	    {
		begin = ((NumberField) getBegin(j)).getFloatValue();
		end = ((NumberField) getEnd(j)).getFloatValue();
		offset = ((NumberField) getOffset(j)).getFloatValue();
		slope = ((NumberField) getSlope(j)).getFloatValue();

		/*
		 * Check wether we need to add a function
		 * and do so if necessary
		 */
		if(c_function.getNrFunctions() < j)
		    c_function.addCostFunction();

		/* Write info to function */
		c_function.setCostFunAt(j, begin, end, offset, slope);
	    }

        /* Save label */
        c_function.setLabel(txtLabel.getText());

        /* Save selected in- and out flows for current time step */


        /* Save selected in- and out flows for all time steps */
	int old_timestep = c_timestep;
	Timesteplevel level = c_gui.getTopTimesteplevel();
        Timesteplevel thisLevel = c_function.getTimesteplevel();
	int timesteps = thisLevel.timestepDifference(level);
        // Check if any in flow or out flow is selected
        TimestepInfo inf = getTimestepInfo(1);
        Vector vec = inf.getInFlow();
        if(vec.size()== 0)
        {
            vec = inf.getOutFlow();
            if(vec.size()== 0)
                flag = false;
        }
        
	for (int i = 0; i < timesteps; i++) {
	    c_function.setTimestep(i+1);
            TimestepInfo info = getTimestepInfo(i+1);
            c_function.setTimestepInfo(info.getInFlow(), info.getOutFlow());
        }
        if(valid)
        {
            if(flag)
            {
            c_function.setTechnicalLife(technicallifespanField.getFloatValue());
            c_function.setEconomicalLife(economiclifespanField.getFloatValue());
            c_function.setPercentageValueOfScrap(percentagescrapvalueField.getFloatValue());
            c_function.setFixedValueOfScrap(fixedscrapvalueField.getFloatValue());
            c_function.setTimestepValue(c_timeStepValues);
            }
        }
        c_function.setTimestep(old_timestep);

	return true;
    }
    /*
     * Get the ID of an inflow.
     * @param stringID the ID of an inflow as a string
     * @return the ID as an ID-object
     */
    private ID parseInId(String stringID){
	Flow[] currentFlow = c_gui.getInFlows(c_nodeID);
	for( int i = 0; i < currentFlow.length; i++){
	    if( stringID.equals(currentFlow[i].toString()))
		return currentFlow[i].getID();//should only by one with this ID
	}
	return null;
    }

    /*
     * Get the ID of an outflow.
     * @param stringID the ID of an outflow as a string
     * @return the ID as an ID-object
     */
    private ID parseOutId(String stringID){
	Flow[] currentFlow = c_gui.getOutFlows(c_nodeID);
	for( int i = 0; i < currentFlow.length; i++){
	    if( stringID.equals(currentFlow[i].toString()))
		return currentFlow[i].getID();//should only by one with this ID
	}
	return null;
    }

    /**
     * This method is called from within the constructor
     * initialize dynamic components in the form.
     * For this dialog, the only dynamic component is timesteps.
     */
    private void initDynamicComponents() {
        java.awt.GridBagConstraints constraints;
        lblTSL = new javax.swing.JLabel[c_timesteplevels];
	spinTSL = new SpinButton[c_timesteplevels];
        Timesteplevel level = c_gui.getTopTimesteplevel();
	Timesteplevel thisLevel = c_function.getTimesteplevel();

        //Cost when no invest
        pnlCostAtZero.add(c_costWhenNoInvest);
        pnlCostAtZero.updateUI();

        //TOP
        for(int i = 0; i < c_timesteplevels; i++)
	{
	    final int j = i;
	    //Add timestep labels
	    constraints = new java.awt.GridBagConstraints();
	    constraints.gridx = 0;
	    constraints.gridy = i+1;
	    constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;

	    lblTSL[i] = new javax.swing.JLabel();
	    lblTSL[i].setText(level.getLabel());
	    pnlTimestep.add(lblTSL[i],constraints);

	    //Add timestep spinbuttons
	    constraints = new java.awt.GridBagConstraints();
	    constraints.gridx = 1;
	    constraints.gridy = i+1;
	    constraints.insets = new java.awt.Insets (0, 15, 0, 0);
	    constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    constraints.weightx = 1.0;

	    spinTSL[i] = new SpinButton(1,level.getMaxTimesteps(),1,1);
	    pnlTimestep.add(spinTSL[i],constraints);

	    spinTSL[i].addListener(new SpinButtonListener()
	    {
		SpinButton button = spinTSL[j];
		public void valueDecreased()
		{
		    if (!updateTimestep()) {
			button.incValue();
			showWarningDialog(UPPER_INVALID_TITLE,
					  UPPER_INVALID_MESSAGE);
		    }
		}
		public void valueIncreased()
		{
		    if (!updateTimestep()) {
			button.decValue();
			showWarningDialog(UPPER_INVALID_TITLE,
					  UPPER_INVALID_MESSAGE);
		    }
		}
	    });
	    spinTSL[i].addFocusListener(new SpinButtonUpdateListener(spinTSL[i]) {
	    	public void valueUpdated() {
	    		updateTimestep();	
	    	}
	    });
	    level = level.getNextLevel();
	}
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
   

    private void initComponent() {                          
        lblDescription = new javax.swing.JLabel();
        sep1 = new javax.swing.JSeparator();
        pnlLabel = new javax.swing.JPanel();
        lblLabel = new javax.swing.JLabel();
        txtLabel = new javax.swing.JTextField();
        sep2 = new javax.swing.JSeparator();
        pnlTimestep = new javax.swing.JPanel();
        lblTimestep = new javax.swing.JLabel();
        pnlInflow = new javax.swing.JPanel();
        scrollAvailInflow = new javax.swing.JScrollPane();
        btnAddInflow = new javax.swing.JButton();
        btnRemoveInflow = new javax.swing.JButton();
        scrollSelectedInflow = new javax.swing.JScrollPane();
        lblAvailInflow = new javax.swing.JLabel();
        lblSelectedInflow = new javax.swing.JLabel();
        pnlOutflow = new javax.swing.JPanel();
        scrollAvailOutflow = new javax.swing.JScrollPane();
        btnAddOutflow = new javax.swing.JButton();
        btnRemoveOutflow = new javax.swing.JButton();
        scrollSelectedOutflow = new javax.swing.JScrollPane();
        lblAvailOutflow = new javax.swing.JLabel();
        lblSelectedOutflow = new javax.swing.JLabel();
        pnlCostAtZero = new javax.swing.JPanel();
        lblCostAtZero = new javax.swing.JLabel();
        scrollEquations = new javax.swing.JScrollPane();
        pnlAddRem = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        sep3 = new javax.swing.JSeparator();
        pnlButtons = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1;
        java.awt.GridBagConstraints gridBagConstraints11;
        java.awt.GridBagConstraints gridBagConstraints12;
        java.awt.GridBagConstraints gridBagConstraints13;
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        lblDescription.setText("Description: Defines an investment cost.");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        //gridBagConstraints1.gridy = 0;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
        /*
         *
         *public Insets(int top,int left,int bottom,int right)
         *
         */
        getContentPane().add(lblDescription, gridBagConstraints1);

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        //gridBagConstraints1.gridy = 1;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(sep1, gridBagConstraints1);

        pnlLabel.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints2;

        lblLabel.setText("Label");
        gridBagConstraints2 = new java.awt.GridBagConstraints();
        pnlLabel.add(lblLabel, gridBagConstraints2);

        gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints2.insets = new java.awt.Insets(0, 10, 0, 0);
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints2.weightx = 1.0;
        pnlLabel.add(txtLabel, gridBagConstraints2);

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        //gridBagConstraints1.gridy = 2;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        gridBagConstraints1.weightx = 1.0;
        getContentPane().add(pnlLabel, gridBagConstraints1);

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        //gridBagConstraints1.gridy = 3;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(sep2, gridBagConstraints1);

        pnlTimestep.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints3;

        lblTimestep.setText("Timestep:");
        gridBagConstraints3 = new java.awt.GridBagConstraints();
        gridBagConstraints3.gridx = 0;
        gridBagConstraints3.gridy = 0;
        gridBagConstraints3.gridwidth = 2;
        gridBagConstraints3.insets = new java.awt.Insets(10, 0, 10, 5);
        gridBagConstraints3.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints3.weightx = 1.0;
        pnlTimestep.add(lblTimestep, gridBagConstraints3);

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        //gridBagConstraints1.gridy = 4;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 10, 0, 10);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(pnlTimestep, gridBagConstraints1);
        
        // Added by Nawzad Mardn 080624
        lblTechnicallifespan = new JLabel("Technical lifespan   ");
        pnltechnicallife = new JPanel();
        pnltechnicallife.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints22,gridBagConstraints23,gridBagConstraints24;

        gridBagConstraints22 = new java.awt.GridBagConstraints();
        gridBagConstraints22.gridx = 0;
        gridBagConstraints22.gridy = 0;
        gridBagConstraints22.gridwidth = 2;
        gridBagConstraints22.insets = new java.awt.Insets(5, 5, 0, 10);
        gridBagConstraints22.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints22.weightx = 1.0;
        pnltechnicallife.add(lblTechnicallifespan, gridBagConstraints22);
        
        technicallifespanField =new PositiveNumberField();
        technicallifespanField.setToolTipText("Technical lifespan");
        technicallifespanField.setText(new Float(c_function.getTechnicalLife()).toString());  
        gridBagConstraints23 = new java.awt.GridBagConstraints();
        gridBagConstraints23.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints23.insets = new java.awt.Insets(5, 5, 0, 10);
        gridBagConstraints23.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints23.weightx = 1.0;
        pnltechnicallife.add(technicallifespanField, gridBagConstraints23);

        gridBagConstraints24 = new java.awt.GridBagConstraints();
        gridBagConstraints24.gridx = 0;
        //gridBagConstraints24.gridy = 5;
        gridBagConstraints24.fill = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints24.insets = new java.awt.Insets(5, 5, 0, 10);
        gridBagConstraints24.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        getContentPane().add(pnltechnicallife, gridBagConstraints24);
        
        lblEconomiclifespan = new JLabel("Economical lifespan");
        economiclifespanField =new PositiveNumberField();
        economiclifespanField.setToolTipText("Economical lifespan");
        economiclifespanField.setText(new Float(c_function.getEconomicalLife()).toString());   
        pnleconomiclife = new JPanel();
        pnleconomiclife.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints22 = new java.awt.GridBagConstraints();
        gridBagConstraints22.gridx = 0;
        gridBagConstraints22.gridy = 0;
        gridBagConstraints22.gridwidth = 2;
        gridBagConstraints22.insets = new java.awt.Insets(5, 5, 0, 10);
        gridBagConstraints22.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints22.weightx = 1.0;
        pnleconomiclife.add(lblEconomiclifespan, gridBagConstraints22);
        
        gridBagConstraints23 = new java.awt.GridBagConstraints();
        gridBagConstraints23.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints23.insets = new java.awt.Insets(5, 5, 0, 10);
        gridBagConstraints23.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints23.weightx = 1.0;
        pnleconomiclife.add(economiclifespanField, gridBagConstraints23);

        gridBagConstraints24 = new java.awt.GridBagConstraints();
        gridBagConstraints24.gridx = 0;
        //gridBagConstraints24.gridy = 6;
        gridBagConstraints24.fill = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints24.insets = new java.awt.Insets(5, 5, 0, 10);
        gridBagConstraints24.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        getContentPane().add(pnleconomiclife, gridBagConstraints24);
        /*
         *private PositiveNumberField  percentagescrapvalueField,fixedscrapvalueField;
          private javax.swing.JLabel lblpercentagescrapvalue, lblfixedscrapvalue;
         *private javax.swing.JPanel pnlpercentagescrapvalue, pnlfixedscrapvalue;
         *
         */
        //Create the check boxes.
        //percentagevalueCheckBox, fixedvalueCheckBox
        percentagevalueCheckBox = new JCheckBox("Percentage value of the scrap");
        percentagevalueCheckBox.setMnemonic(KeyEvent.VK_C);
        percentagevalueCheckBox.setSelected(c_function.getPercentagevalueChoice());
        percentagevalueCheckBox.addItemListener(new QItemListener());
       // lblpercentagescrapvalue = new JLabel("Percentage value of the scrap");
        pnlpercentagescrapvalue = new JPanel();
        pnlpercentagescrapvalue.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints32,gridBagConstraints33,gridBagConstraints34;

        gridBagConstraints32 = new java.awt.GridBagConstraints();
        gridBagConstraints32.gridx = 0;
        gridBagConstraints32.gridy = 0;
        gridBagConstraints32.gridwidth = 2;
        gridBagConstraints32.insets = new java.awt.Insets(5, 5, 0, 0);
        gridBagConstraints32.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints32.weightx = 1.0;
        pnlpercentagescrapvalue.add(percentagevalueCheckBox, gridBagConstraints32);
        
        percentagescrapvalueField =new PositiveNumberField();
        percentagescrapvalueField.setToolTipText("Percentage value of the scrap");
        percentagescrapvalueField.setText(new Float(c_function.getPercentageValueOfScrap()).toString());
        percentagescrapvalueField.setEnabled(c_function.getPercentagevalueChoice());
        gridBagConstraints33 = new java.awt.GridBagConstraints();
        gridBagConstraints33.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints33.insets = new java.awt.Insets(5, 5, 0, 10);
        gridBagConstraints33.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints33.weightx = 1.0;
        pnlpercentagescrapvalue.add(percentagescrapvalueField, gridBagConstraints33);

        gridBagConstraints34 = new java.awt.GridBagConstraints();
        gridBagConstraints34.gridx = 0;
        //gridBagConstraints24.gridy = 5;
        gridBagConstraints34.fill = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints34.insets = new java.awt.Insets(5, 5, 0, 10);
        gridBagConstraints34.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        getContentPane().add(pnlpercentagescrapvalue, gridBagConstraints34);
        
        /*******/
        
        /*
         *private PositiveNumberField  percentagescrapvalueField,fixedscrapvalueField;
          private javax.swing.JLabel lblpercentagescrapvalue, lblfixedscrapvalue;
         *private javax.swing.JPanel pnlpercentagescrapvalue, pnlfixedscrapvalue;
         */
        fixedvalueCheckBox = new JCheckBox("Fixed value of the scrap   ");
        fixedvalueCheckBox.setMnemonic(KeyEvent.VK_C);
        fixedvalueCheckBox.setSelected(c_function.getFixedagevalueChoice());
        fixedvalueCheckBox.addItemListener(new QItemListener());
        //lblfixedscrapvalue = new JLabel("Fixed value of the scrap   ");
        pnlfixedscrapvalue = new JPanel();
        pnlfixedscrapvalue.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints42,gridBagConstraints43,gridBagConstraints44;

        gridBagConstraints42 = new java.awt.GridBagConstraints();
        gridBagConstraints42.gridx = 0;
        gridBagConstraints42.gridy = 0;
        gridBagConstraints42.gridwidth = 2;
        gridBagConstraints42.insets = new java.awt.Insets(5, 5, 0, 25);
        gridBagConstraints42.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints42.weightx = 1.0;
        pnlfixedscrapvalue.add(fixedvalueCheckBox, gridBagConstraints42);
        
        fixedscrapvalueField =new PositiveNumberField();
        fixedscrapvalueField.setToolTipText("Fixed value of the scrap   ");
        fixedscrapvalueField.setText(new Float(c_function.getFixedValueOfScrap()).toString()); 
        fixedscrapvalueField.setEnabled(c_function.getFixedagevalueChoice());
        gridBagConstraints43 = new java.awt.GridBagConstraints();
        gridBagConstraints43.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints43.insets = new java.awt.Insets(5, 5, 0, 10);
        gridBagConstraints43.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints43.weightx = 1.0;
        pnlfixedscrapvalue.add(fixedscrapvalueField, gridBagConstraints43);

        gridBagConstraints44 = new java.awt.GridBagConstraints();
        gridBagConstraints44.gridx = 0;
        //gridBagConstraints24.gridy = 5;
        gridBagConstraints44.fill = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints44.insets = new java.awt.Insets(5, 5, 0, 10);
        gridBagConstraints44.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        getContentPane().add(pnlfixedscrapvalue, gridBagConstraints44);
         
        
        
        /*********************************
         
        btnRate =new javax.swing.JButton("Rate & analyses period");
        btnRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRateActionPerformed(evt);
            }
        });
        lblRatAnalys = new JLabel("Insert annualy interest and analyses period");
        pnlRate = new JPanel();
        pnlRate.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints22 = new java.awt.GridBagConstraints();
        gridBagConstraints22.gridx = 0;
        gridBagConstraints22.gridy = 0;
        gridBagConstraints22.gridwidth = 2;
        gridBagConstraints22.insets = new java.awt.Insets(5, 5, 0, 10);
        gridBagConstraints22.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints22.weightx = 1.0;
        pnlRate.add(lblRatAnalys, gridBagConstraints22);
        
        gridBagConstraints23 = new java.awt.GridBagConstraints();
        gridBagConstraints23.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints23.insets = new java.awt.Insets(5, 5, 0, 10);
        gridBagConstraints23.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints23.weightx = 1.0;
        pnlRate.add(btnRate, gridBagConstraints23);

        gridBagConstraints24 = new java.awt.GridBagConstraints();
        gridBagConstraints24.gridx = 0;
        //gridBagConstraints24.gridy = 6;
        gridBagConstraints24.fill = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints24.insets = new java.awt.Insets(5, 5, 0, 10);
        gridBagConstraints24.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        getContentPane().add(pnlRate, gridBagConstraints24);*/

  

        pnlInflow.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints4;

        scrollAvailInflow.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollAvailInflow.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollAvailInflow.setPreferredSize(new java.awt.Dimension(100, 100));
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridy = 1;
        gridBagConstraints4.gridheight = 4;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints4.weightx = 0.5;
        pnlInflow.add(scrollAvailInflow, gridBagConstraints4);

        btnAddInflow.setText("=>");
        btnAddInflow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddInflowActionPerformed(evt);
            }
        });

        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 1;
        gridBagConstraints4.gridy = 2;
        gridBagConstraints4.insets = new java.awt.Insets(10, 0, 0, 0);
        pnlInflow.add(btnAddInflow, gridBagConstraints4);

        btnRemoveInflow.setText("<=");
        btnRemoveInflow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveInflowActionPerformed(evt);
            }
        });

        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 1;
        gridBagConstraints4.gridy = 3;
        gridBagConstraints4.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlInflow.add(btnRemoveInflow, gridBagConstraints4);

        scrollSelectedInflow.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollSelectedInflow.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollSelectedInflow.setPreferredSize(new java.awt.Dimension(100, 100));
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 2;
        gridBagConstraints4.gridy = 1;
        gridBagConstraints4.gridheight = 4;
        gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints4.insets = new java.awt.Insets(0, 5, 0, 0);
        gridBagConstraints4.weightx = 0.5;
        pnlInflow.add(scrollSelectedInflow, gridBagConstraints4);
        
       
        lblAvailInflow.setText("Available inflow(s)");
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.gridy = 0;
        pnlInflow.add(lblAvailInflow, gridBagConstraints4);

        lblSelectedInflow.setText("Selected inflow(s)");
        gridBagConstraints4 = new java.awt.GridBagConstraints();
        gridBagConstraints4.gridx = 2;
        gridBagConstraints4.gridy = 0;
        pnlInflow.add(lblSelectedInflow, gridBagConstraints4);

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        //gridBagConstraints1.gridy = 7;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        gridBagConstraints1.weightx = 1.0;
        getContentPane().add(pnlInflow, gridBagConstraints1);

        pnlOutflow.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints5;

        scrollAvailOutflow.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollAvailOutflow.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollAvailOutflow.setPreferredSize(new java.awt.Dimension(100, 100));
        gridBagConstraints5 = new java.awt.GridBagConstraints();
        gridBagConstraints5.gridx = 0;
        gridBagConstraints5.gridy = 1;
        gridBagConstraints5.gridheight = 4;
        gridBagConstraints5.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints5.insets = new java.awt.Insets(0, 0, 0, 5);
        gridBagConstraints5.weightx = 0.5;
        pnlOutflow.add(scrollAvailOutflow, gridBagConstraints5);

        btnAddOutflow.setText("=>");
        btnAddOutflow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddOutflowActionPerformed(evt);
            }
        });

        gridBagConstraints5 = new java.awt.GridBagConstraints();
        gridBagConstraints5.gridx = 1;
        gridBagConstraints5.gridy = 2;
        gridBagConstraints5.insets = new java.awt.Insets(10, 0, 0, 0);
        pnlOutflow.add(btnAddOutflow, gridBagConstraints5);

        btnRemoveOutflow.setText("<=");
        btnRemoveOutflow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveOutflowActionPerformed(evt);
            }
        });

        gridBagConstraints5 = new java.awt.GridBagConstraints();
        gridBagConstraints5.gridx = 1;
        gridBagConstraints5.gridy = 3;
        gridBagConstraints5.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlOutflow.add(btnRemoveOutflow, gridBagConstraints5);

        scrollSelectedOutflow.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollSelectedOutflow.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollSelectedOutflow.setPreferredSize(new java.awt.Dimension(100, 100));
        gridBagConstraints5 = new java.awt.GridBagConstraints();
        gridBagConstraints5.gridx = 2;
        gridBagConstraints5.gridy = 1;
        gridBagConstraints5.gridheight = 4;
        gridBagConstraints5.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints5.insets = new java.awt.Insets(0, 5, 0, 0);
        gridBagConstraints5.weightx = 0.5;
        pnlOutflow.add(scrollSelectedOutflow, gridBagConstraints5);

        lblAvailOutflow.setText("Available outflow(s)");
        gridBagConstraints5 = new java.awt.GridBagConstraints();
        gridBagConstraints5.gridx = 0;
        gridBagConstraints5.gridy = 0;
        pnlOutflow.add(lblAvailOutflow, gridBagConstraints5);

        lblSelectedOutflow.setText("Selected outflow(s)");
        gridBagConstraints5 = new java.awt.GridBagConstraints();
        gridBagConstraints5.gridx = 2;
        gridBagConstraints5.gridy = 0;
        pnlOutflow.add(lblSelectedOutflow, gridBagConstraints5);

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        //gridBagConstraints1.gridy = 8;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 10, 10, 10);
        gridBagConstraints1.weightx = 1.0;
        getContentPane().add(pnlOutflow, gridBagConstraints1);

        pnlCostAtZero.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints6;

        lblCostAtZero.setText("Cost when <res-x> = 0:    C =");
        gridBagConstraints6 = new java.awt.GridBagConstraints();
        gridBagConstraints6.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlCostAtZero.add(lblCostAtZero, gridBagConstraints6);

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        //gridBagConstraints1.gridy = 9;
        gridBagConstraints1.gridwidth = 2;
        getContentPane().add(pnlCostAtZero, gridBagConstraints1);

        scrollEquations.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollEquations.setPreferredSize(new java.awt.Dimension(100, 100));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        //gridBagConstraints1.gridy = 10;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.gridheight = 4;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        gridBagConstraints1.weightx = 1.0;
        getContentPane().add(scrollEquations, gridBagConstraints1);

        pnlAddRem.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints7;

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        gridBagConstraints7 = new java.awt.GridBagConstraints();
        gridBagConstraints7.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlAddRem.add(btnAdd, gridBagConstraints7);

        btnRemove.setText("Remove");
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        gridBagConstraints7 = new java.awt.GridBagConstraints();
        gridBagConstraints7.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlAddRem.add(btnRemove, gridBagConstraints7);

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        //gridBagConstraints1.gridy = 11;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.insets = new java.awt.Insets(0, 10, 10, 10);
        getContentPane().add(pnlAddRem, gridBagConstraints1);

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        //gridBagConstraints1.gridy = 14;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(sep3, gridBagConstraints1);

        pnlButtons.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints8;

        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        gridBagConstraints8 = new java.awt.GridBagConstraints();
        gridBagConstraints8.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlButtons.add(btnOk, gridBagConstraints8);

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        gridBagConstraints8 = new java.awt.GridBagConstraints();
        gridBagConstraints8.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlButtons.add(btnCancel, gridBagConstraints8);

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        //gridBagConstraints1.gridy = 15;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(pnlButtons, gridBagConstraints1);

        pack();
    }  
    /**
     * Action when the "remove outflow" button is pressed.
     */
    private void btnRemoveOutflowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveOutflowActionPerformed
        Object selectedValue = c_lstSelectedOutflow.getSelectedValue();
        /* Do nothing if nothing is selected*/
        if(selectedValue == null)
           return;
        TimestepInfo currentTimestepInfo = getTimestepInfo(c_timestep);

        ID theId = (ID) selectedValue;
        /* Check if value already in list while trying to remove it
         * if so, don't remove it
         */
        if(!currentTimestepInfo.removeOutFlow((ID) theId))
            return;

        c_lstSelectedOutflow.setListData(currentTimestepInfo.getOutFlow());
        if(currentTimestepInfo.getOutFlow().isEmpty())
            btnRemoveOutflow.setEnabled(false);
    }//GEN-LAST:event_btnRemoveOutflowActionPerformed

    /**
     * Action when the "add outflow" button is pressed.
     */
    private void btnAddOutflowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddOutflowActionPerformed
        Object selectedValue = c_lstAvailOutflow.getSelectedValue();
        /* Do nothing if nothing is selected*/
        if(selectedValue == null)
           return;
        TimestepInfo currentTimestepInfo = getTimestepInfo(c_timestep);

        ID theId;
        theId = (ID)parseOutId((String)selectedValue);
        /* Check if value already in list while trying to add it
         * if so, don't add it to the list
         */
        if(!currentTimestepInfo.addOutFlow((ID) theId))
            return;

        c_lstSelectedOutflow.setListData(currentTimestepInfo.getOutFlow());
        btnRemoveOutflow.setEnabled(true);
    }//GEN-LAST:event_btnAddOutflowActionPerformed

    /**
     * Action when the "remove inflow" button is pressed.
     */
    private void btnRemoveInflowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveInflowActionPerformed
        Object selectedValue = c_lstSelectedInflow.getSelectedValue();
        /* Do nothing if nothing is selected*/
        if(selectedValue == null)
           return;
        TimestepInfo currentTimestepInfo = getTimestepInfo(c_timestep);

        ID theId = (ID) selectedValue;
        /* Check if value already in list while trying to remove it
         * if so, don't remove it
         */
        if(!currentTimestepInfo.removeInFlow((ID) theId))
            return;

        c_lstSelectedInflow.setListData(currentTimestepInfo.getInFlow());
        if(currentTimestepInfo.getInFlow().isEmpty())
            btnRemoveInflow.setEnabled(false);
    }//GEN-LAST:event_btnRemoveInflowActionPerformed

    /**
     * Action when the "add inflow" button is pressed.
     */
    private void btnAddInflowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddInflowActionPerformed
        Object selectedValue = c_lstAvailInflow.getSelectedValue();
        /* Do nothing if nothing is selected*/
        if(selectedValue == null)
           return;
        TimestepInfo currentTimestepInfo = getTimestepInfo(c_timestep);

        ID theId;
        theId = (ID)parseInId((String)selectedValue);
        /* Check if value already in list while trying to add it
         * if so, don't add it to the list
         */
        if(!currentTimestepInfo.addInFlow((ID) theId))
            return;

        c_lstSelectedInflow.setListData(currentTimestepInfo.getInFlow());
        btnRemoveInflow.setEnabled(true);
    }//GEN-LAST:event_btnAddInflowActionPerformed

    /**
     * Action when the "OK" button is pressed.
     */
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed

        if(!save())
        {
            return;
        }

	closeDialog(null);
    }//GEN-LAST:event_btnOkActionPerformed

    /**
     * Action when the "cancel" button is pressed.
     */
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        
        
        setVisible(false);
        
	dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    /**
     * Action when the "add function" button is pressed.
     */
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        
        
        addNewCostFunction();
        updateLimitPanel();
        btnRemove.setEnabled(true);
    }//GEN-LAST:event_btnAddActionPerformed

   /* private void btnRateActionPerformed(java.awt.event.ActionEvent evt)
    {
        JDialog dialog =new DiscountedsystemcostDialog(c_parent, c_modal, c_gui, c_function);
        int height = getLocation().y + getSize().height/2;
	int width = getLocation().x + getSize().width/2;
	int x = (int) (width - dialog.getSize().width/2);
	int y = (int) (height - dialog.getSize().height/2);
	dialog.setLocation(x, y);
	dialog.setVisible(true);
    }*/
    /**
     * Action when the "remove function" button is pressed.
     */
    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        removeLast();
        updateLimitPanel();
        if(getNrFunctions() == 0)
            btnRemove.setEnabled(false);
    }//GEN-LAST:event_btnRemoveActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

// Variables declaration - do not modify                     
private javax.swing.JLabel lblDescription;
private javax.swing.JSeparator sep1;
private javax.swing.JPanel pnlLabel;
private javax.swing.JLabel lblLabel;
private javax.swing.JTextField txtLabel;
private javax.swing.JSeparator sep2;
private javax.swing.JPanel pnlTimestep;
private javax.swing.JLabel lblTimestep;
private javax.swing.JPanel pnlInflow;
private javax.swing.JScrollPane scrollAvailInflow;
private javax.swing.JButton btnAddInflow;
private javax.swing.JButton btnRemoveInflow;
private javax.swing.JScrollPane scrollSelectedInflow;
private javax.swing.JLabel lblAvailInflow;
private javax.swing.JLabel lblSelectedInflow;
private javax.swing.JPanel pnlOutflow;
private javax.swing.JScrollPane scrollAvailOutflow;
private javax.swing.JButton btnAddOutflow;
private javax.swing.JButton btnRemoveOutflow;
private javax.swing.JScrollPane scrollSelectedOutflow;
private javax.swing.JLabel lblAvailOutflow;
private javax.swing.JLabel lblSelectedOutflow;
private javax.swing.JPanel pnlCostAtZero;
private javax.swing.JLabel lblCostAtZero;
private javax.swing.JScrollPane scrollEquations;
private javax.swing.JPanel pnlAddRem;
private javax.swing.JButton btnAdd;
private javax.swing.JButton btnRemove;
private javax.swing.JSeparator sep3;
private javax.swing.JPanel pnlButtons;
private javax.swing.JButton btnOk;
private javax.swing.JButton btnCancel;
// Added by Nawzad Mardan 080626
private JDialog c_parent;
boolean c_modal;
//private JButton btnRate;
private PositiveNumberField  technicallifespanField,economiclifespanField;
//private javax.swing.JLabel lblRatAnalys, lblTechnicallifespan, lblEconomiclifespan;
private javax.swing.JLabel lblTechnicallifespan, lblEconomiclifespan;
//private javax.swing.JPanel pnlRate, pnltechnicallife, pnleconomiclife, pnlMain;
private javax.swing.JPanel pnltechnicallife, pnleconomiclife, pnlMain;
private PositiveNumberField  percentagescrapvalueField,fixedscrapvalueField;
private javax.swing.JLabel lblpercentagescrapvalue, lblfixedscrapvalue;
private javax.swing.JPanel pnlpercentagescrapvalue, pnlfixedscrapvalue;
private JCheckBox percentagevalueCheckBox, fixedvalueCheckBox;
// End of variables declaration     

/**
     * Update the functions panel.
     */

    private void updateLimitPanel()
    {
	Component begin;
	Component end;
	Component slope;
	Component offset;
	JPanel pnlLimit;
	JPanel pnlEquation;
	GridBagConstraints constraints;

        c_pnlLimits.removeAll();

        /* If there are no functions then inactivate
         * remove button
         */
        if(getNrFunctions() == 0)
            btnRemove.setEnabled(false);

	// for every limit, add two new rows to the panel
	// first row specifies the limit ... lowerlimit < limit < upperlimit
	// second row specifies the equation ... y = k*x + m
	int j = -1;
	for (int i = 0; i < getNrFunctions(); i++) {
	    begin = (Component) getBegin(i+1);
	    end = (Component) getEnd(i+1);
	    slope = (Component) getSlope(i+1);
	    offset = (Component) getOffset(i+1);

	    // --- add limits to the first panel ---
	    j++;
	    pnlLimit = new JPanel();
	    pnlLimit.setLayout(new BoxLayout(pnlLimit, BoxLayout.X_AXIS));
	    pnlLimit.add(new JLabel("Between "));
	    pnlLimit.add(begin);
	    pnlLimit.add(new JLabel(" and "));
	    pnlLimit.add(end);

	    constraints = new GridBagConstraints();
	    constraints.gridx = 0;
	    constraints.gridy = j;
	    constraints.weightx = 1.0;
	    constraints.anchor = GridBagConstraints.WEST;
	    constraints.insets = new Insets(0, 3, 3, 3);
	    c_pnlLimits.add(pnlLimit, constraints);

	    // --- add equation to second panel ---
	    j++;
	    pnlEquation = new JPanel();
	    pnlEquation.setLayout(new BoxLayout(pnlEquation,
						BoxLayout.X_AXIS));
	    pnlEquation.add(new JLabel("<Y> = "));
	    pnlEquation.add(slope);
	    pnlEquation.add(new JLabel(" * <X> + "));
	    pnlEquation.add(offset);
	    constraints = new GridBagConstraints();
	    constraints.gridx = 0;
	    constraints.gridy = j;
	    constraints.weightx = 1.0;
	    constraints.anchor = GridBagConstraints.WEST;
	    constraints.insets = new Insets(0, 3, 0, 3);
	    c_pnlLimits.add(pnlEquation, constraints);

	    j++;
	    constraints = new GridBagConstraints();
	    constraints.gridx = 0;
	    constraints.gridy = j;
	    constraints.weightx = 1.0;
	    if (i == getNrFunctions()-1)
		constraints.weighty = 1.0;
	    constraints.fill = GridBagConstraints.HORIZONTAL;
	    constraints.anchor = GridBagConstraints.NORTHWEST;
	    constraints.insets = new Insets(0, 3, 3, 0);
	    c_pnlLimits.add(new JSeparator(), constraints);
	}
	showLastEquation();
	c_pnlLimits.updateUI();
    }

    /**
     * Scroll functions panel to the last equation.
     */
    private void showLastEquation()
    {
	Dimension dim = c_pnlLimits.getSize();
	Rectangle bounds = new Rectangle();
	c_pnlLimits.getBounds(bounds);

	Rectangle rect = new Rectangle(0, dim.height-1, dim.width, dim.height);
	c_pnlLimits.scrollRectToVisible(rect);
	c_pnlLimits.revalidate();
    }


    /**
     * Update selected in- and outflows according to new timestep.
     * @return true if all "old" timestep info is correct
     */
    public boolean updateTimestep()
    {
        Vector currentInflow, currentOutflow;

        //Calculate and set new timestep
	c_timestep=1; //The first timestep is 1 (not 0)
	int factor=1;
	for(int i=c_timesteplevels-1; i>0; i--) {
	    c_timestep = c_timestep + (spinTSL[i].getValue() - 1) * factor;
	    factor = factor * c_tsl[i].getMaxTimesteps();
	}
        c_function.setTimestep(c_timestep);

        //Update GUI for flows and flow buttons
        TimestepInfo newTimestepInfo = getTimestepInfo(c_timestep);
        currentInflow = newTimestepInfo.getInFlow();
        currentOutflow = newTimestepInfo.getOutFlow();
        c_lstSelectedInflow.setListData(currentInflow);
        c_lstSelectedOutflow.setListData(currentOutflow);

        if (currentInflow.isEmpty())
            btnRemoveInflow.setEnabled(false);
        else
            btnRemoveInflow.setEnabled(true);

        if (currentOutflow.isEmpty())
            btnRemoveOutflow.setEnabled(false);
        else
            btnRemoveOutflow.setEnabled(true);

	return true;
    }

    /**
     * Shows warning dialog.
     * @param title Title of dialog.
     * @param message Message in dialog.
     */
    private void showWarningDialog(String title, String message)
    {
	JOptionPane.showMessageDialog(InvestmentCostDialog.this,
				      message,
				      title,
				      JOptionPane.OK_OPTION);
    }


    /**
     * Adds a new (empty) cost function to internal representation.
     */
    public void addNewCostFunction()
    {
        float begin = 0;
        float end = 0;
        float slope = 0;
        float offset = 0;
        /* if something there make sure "begin" is set
         * to former "end"
         */
        int lastfun = getNrFunctions();
        if(lastfun > 0)
            begin = ((NumberField) getEnd(lastfun)).getFloatValue();


        addCostFunction(new PositiveNumberField(begin,BEGIN_ENTRY_SIZE),
                            new NumberField(end,END_ENTRY_SIZE),
                            new NumberField(slope,SLOPE_ENTRY_SIZE),
                            new NumberField(offset,OFFSET_ENTRY_SIZE));
    }

    /**
     * Adds a cost function to internal representation.
     */
    public void addCostFunction(Component begin, Component end, Component slope, Component offset)
    {
	c_begin.addElement(begin);
	c_end.addElement(end);
	c_slope.addElement(slope);
	c_offset.addElement(offset);
    }

    /**
     * Get number of cost function(s).
     * @return number of cost function(s)
     */
    private int getNrFunctions()
    {
        /*
         * Pick any of the four vectors
         * to represent the size
         */
        return c_begin.size();
    }

    /**
     * Remove last function.
     */
    public void removeLast()
    {
	if(getNrFunctions() > 0)
	    {
		c_begin.removeElementAt(c_begin.size()-1);
		c_end.removeElementAt(c_end.size()-1);
		c_offset.removeElementAt(c_offset.size()-1);
		c_slope.removeElementAt(c_slope.size()-1);
	    }
    }


    /**
     * Remove all function(s).
     */
    public void clearAllFunctions()
    {
	c_begin.removeAllElements();
	c_end.removeAllElements();
	c_offset.removeAllElements();
	c_slope.removeAllElements();
    }

    /**
     * Get "begin" for a function.
     * @param fidx Function number
     * @return the "begin" for this function
     */
    public PositiveNumberField getBegin(int fidx)
    {
	return (PositiveNumberField) c_begin.get(fidx-1);
    }


    /**
     * Get "end" for a function.
     * @param fidx Function number
     * @return the "end" for this function
     */
    public NumberField getEnd(int fidx)
    {
	return (NumberField) c_end.get(fidx-1);
    }

    /**
     * Get "offset" for a function.
     * @param fidx Function number
     * @return the "offset" for this function
     */
    public NumberField getOffset(int fidx)
    {
	return (NumberField) c_offset.get(fidx-1);
    }

    /**
     * Get "slope" for a function.
     * @param fidx Function number
     * @return the "slope" for this function
     */
    public NumberField getSlope(int fidx)
    {
	return (NumberField) c_slope.get(fidx-1);
    }

    /**
     * Get "cost when no investment".
     * @return the "cost when no investment"
     */
    public NumberField getCostWhenNoInvest()
    {
	return (NumberField) c_costWhenNoInvest;
    }

    /**
     * Get timestep info for a timestep.
     * @param timestep Timestep number
     * @return timestep info for this timestep
     */
    public TimestepInfo getTimestepInfo(int timestep)
    {
        try {
            return (TimestepInfo) c_flowinfo.elementAt(timestep-1);
        } catch(ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
    /*
     * converts a flow array into a string array representing the ID's
     */
    private String[] convertToString(Flow[] f){

	if( f == null ){
	    String[] theStrings = {"No flow"};
	    return theStrings;
	}
	else{
	    String[] theStrings = new String[f.length];
	    for( int i = 0; i < f.length; i++){
		theStrings[i] = f[i].toString();
	    }
	    return theStrings;
	}
    }
    
    // Added by Nawzad Mardan 080903
    public class QItemListener implements ItemListener
    {
        public void itemStateChanged(ItemEvent e) 
            {
            Object source = e.getItemSelectable();

            if (source == fixedvalueCheckBox) 
                {
                //if selected
                if(fixedvalueCheckBox.getSelectedObjects()!=null)
                    {
                    fixedscrapvalueField.setEnabled(true);
                    percentagescrapvalueField.setEnabled(false);
                    percentagevalueCheckBox.setSelected(false);
                    percentagescrapvalueField.setText("0.0");      
                    }
                    /*else 
                    {
                        c_choiceOneSelected = false;
                        setAltOneEnabeld(false);
                        setAltOneDefaultValue();
                    }*/
                }
            else if(source == percentagevalueCheckBox)
                {
                if(percentagevalueCheckBox.getSelectedObjects()!=null)
                    {
                    fixedscrapvalueField.setEnabled(false);
                    fixedvalueCheckBox.setSelected(false);
                    percentagescrapvalueField.setEnabled(true);
                    fixedscrapvalueField.setText("0.0");      
                    }
                }
            }
    }
}