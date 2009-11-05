/*
 * Copyright 2004:
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

package mind.gui.dialog;

import mind.gui.*;
import mind.model.*;
import mind.model.function.*;
import mind.model.function.helpers.*;

import java.text.*;
import java.util.Vector;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;


/**
 * A dialog used for handling node batch storage properties.
 * @author  Jonas S‰‰v
 * @author  Per Fredriksson
 * @author 	Tor Knutsson
 * @author  pum7
 */
public class BatchDialog extends mind.gui.dialog.FunctionDialog implements FocusListener {
  //"constants"
  private final int TABLE_HEIGHT = 30;
    /* Various variables for this node */
    private ID c_nodeID;
    private GUI c_gui;
    private BatchEquation c_function;
    private Timesteplevel c_tsl[];
    private int c_timestep; //Active timestep
    private int c_timesteplevels; //Number of timesteplevels
    private int c_maxTimesteps = 1;

    /* Dynamic GUI variables */
    private javax.swing.JLabel lblTSL[];
    private SpinButton spinTSL[];

    /* Data views */
    private JList c_lstAvailInflow = new JList();
    private JTable c_lstSelectedInflow;
    private JList c_lstAvailOutflow = new JList();
    private JTable c_lstSelectedOutflow;
    private SelectedFlowsTableModel c_inFlowsTableModel;
    private SelectedFlowsTableModel c_outFlowsTableModel;

    /* Total and Efficiency panel */
    JPanel pnlInFlowTotalAndEfficiency = new JPanel();
    PositiveNumberField txtTotalInFlowMin = new PositiveNumberField();
    NumberField txtTotalInFlowMax = new NumberField(-1.0f, 10);
    PositiveNumberField txtInFlowEfficiency = new PositiveNumberField();

    JPanel pnlOutFlowTotalAndEfficiency = new JPanel();
    PositiveNumberField txtTotalOutFlowMin = new PositiveNumberField();
    NumberField txtTotalOutFlowMax = new NumberField(-1.0f, 10);
    PositiveNumberField txtOutFlowEfficiency = new PositiveNumberField();

    /* local non timestep related paramaters */
    double c_dblMinBatch = 0;
    double c_dblMaxBatch = -1;
    int c_intBatchTime = 0;
    int c_intAdjustingTime = 0;
    int c_intPredetIntervals = 0;


    /* Storage Panel variables*/
    JPanel pnlStorage = new JPanel();
    GridLayout gridLayout1 = new GridLayout();
    JLabel lblUnit = new JLabel();
    PositiveNumberField txtBatchBetweenMin = new PositiveNumberField();
    JLabel jLabel2 = new JLabel();
    NumberField txtBatchBetweenMax = new NumberField(-1.0f, 10);
    NumberField txtAdjustingTime = new PositiveNumberField();
    NumberField txtBatchTime = new PositiveNumberField();
    JLabel jLabel3 = new JLabel();
    JLabel jLabel4 = new JLabel();
    JLabel jLabel5 = new JLabel();
    JLabel jLabel6 = new JLabel();
    JLabel lblBatchTime = new JLabel();
    JLabel jLabel9 = new JLabel();
    JLabel jLabel11 = new JLabel();
    JLabel lblAdjustingTime = new JLabel();
    JLabel jLabel15 = new JLabel();
    /* End storage panel variables */

    /* Predetermined batch intervals */
    JPanel pnlPredeterminedBatchIntervals = new JPanel();
    JLabel lblPredeterminedBatchIntervals = new JLabel();
    JCheckBox checkPredeterminedBatchIntervals = new JCheckBox();

    /*
     * Used to data about which flows that are
     * registered in each timestep
     */
    private Vector c_flowinfo = null;

    private final String UPPER_INVALID_TITLE =
        "Invalid upper value(s)";
    private final String UPPER_INVALID_MESSAGE =
        "The upper values specified are incorrect,\n" +
        "therefore these equations could not be saved.\n" +
        "The upper limit must be bigger than the lower limit.";
    private final String MIN_INVALID_TITLE =
        "Invalid minimum value";
    private final String MIN_INVALID_MESSAGE =
      "The minimum value must not be less than zero.\n" +
      "Please re-enter";
  private final String MIN_INVALID_MESSAGE2 =
      "The minimum value must not be larger than the maximum value.\n" +
      "Please re-enter";

  private final String MAX_INVALID_TITLE =
      "Invalid maximum value";
  private final String MAX_INVALID_MESSAGE =
      "The maximum value must not be less than the .\n" +
      "minimum value. Please re-enter";

  private final String INVALID_VALUE_TITLE = "Value not within range";

  private final String INVALID_PERCENTAGE_VALUE = "Please enter a percentage value (0 - 100 %)";


    /**
     * This class describes the abstract table model
     */
    private class SelectedFlowsTableModel extends AbstractTableModel {

      final String[] columnNames = {"Flow"};
      private Vector flowsvector = null;

        public int getColumnCount() {
            return 1;
        }

        public int getRowCount() {
          if (flowsvector != null)
            return flowsvector.size();
          else
            return 0;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
          Object obj = null;

            if (col == 0)
             obj = flowsvector.get(row);
            else
              throw(new IllegalArgumentException("Only row 0 is valid"));

          return obj;
        }

        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            //There only is one column and it is not editable
            return false;
        }

        /*
         * Not needed because the one and only column is not editable
         */
//        public void setValueAt(Object value, int row, int col) {
//        }



        /**
         * Sets the flow vector.
         * @param flows The vector containing the flows
         */
        public void setData(Vector flows)
        {
          flowsvector = flows;
        }
}


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
     * Creates new form BatchDialog.
     * The parameters are the same as for other functions.
     */
    public BatchDialog(JDialog parent, boolean modal,
                                ID nodeID, NodeFunction function, GUI gui) {
        super (parent, modal);

        /* Init structures */
        c_flowinfo = new Vector();

        /* Init structures from "above" */
        c_gui = gui;
        c_nodeID = nodeID;
        c_function = (BatchEquation) function;

        /* Start dialog with top time step */
        c_timestep = 1;
        c_function.setTimestep(c_timestep);

        Timesteplevel lvl = c_gui.getTopTimesteplevel();
        c_maxTimesteps = lvl.getBottomLevel().timestepDifference(null);

        /* Load values from function */
        load();

        /* Init dialog */
        initComponents();
        initDynamicComponents();

        scrollAvailInflow.getViewport().setView(c_lstAvailInflow);
        scrollSelectedInflow.getViewport().setView(c_lstSelectedInflow);
        scrollAvailOutflow.getViewport().setView(c_lstAvailOutflow);
        scrollSelectedOutflow.getViewport().setView(c_lstSelectedOutflow);

        txtLabel.setText(c_function.getLabel());

        /* Put content in dialog */
        initAvailableFlows();
        updateTimestep();

        pack();
        pack();
    }

    /**
     * Gets the unit of a flow
     * @param flowId
     * @return the unit string
     */
    private String getResourceOfFlow(ID flowId)
    {
      String unit;
      Resource res = (Resource) Model.getResource(flowId);
      if (res != null)
        unit = res.getUnit();
      else
        unit = "no unit";

      return unit;
    }

    private boolean isSameUnits(SelectedFlowsTableModel tm)
    {
      String oldunit = "";
      String newunit = "";
      boolean result = true;

      if (tm.getRowCount()> 0) {


        ID id = (ID) tm.getValueAt(0, 0);
        oldunit = getResourceOfFlow(id);

        for (int i=1; i<tm.getRowCount(); i++) {
          id = (ID) tm.getValueAt(i,0);
          newunit = getResourceOfFlow(id);
          if (newunit != oldunit) {
            result = false;
          }
        }
      }

      return result;
    }

    private String getUnit()
    {

      if (isSameUnits(c_inFlowsTableModel) && isSameUnits(c_outFlowsTableModel)) {
        String out="";
        String in="";

        if (c_inFlowsTableModel.getRowCount() > 0) {
          in = getResourceOfFlow((ID) c_inFlowsTableModel.getValueAt(0, 0));
        }

        if (c_outFlowsTableModel.getRowCount() > 0) {
          out = getResourceOfFlow((ID) c_outFlowsTableModel.getValueAt(0, 0));
        }

        if (in == out)
          return in;  // or return out, whatever.
      }

      return "n/a";
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
    public void load() {
      //Load number of timestepslevels in the function
      c_timesteplevels = 1;
      Timesteplevel level = c_gui.getTopTimesteplevel();
      Timesteplevel thisLevel = c_function.getTimesteplevel();
      c_timesteplevels = thisLevel.toInt() + 1;

  //Load all timesteplevels
      c_tsl = new Timesteplevel[c_timesteplevels];
      level = c_gui.getTopTimesteplevel();

      for (int j = 0; j < c_timesteplevels; j++) {
        c_tsl[j] = level;
        level = level.getNextLevel();
      }

  //Load selected in- and outflows for each time step
      level = c_gui.getTopTimesteplevel();
      thisLevel = c_function.getTimesteplevel();
      int timesteps = thisLevel.timestepDifference(level);
      for (int i = 0; i < timesteps; i++) {
        c_function.setTimestep(i + 1);
        TimestepInfo info = new TimestepInfo();

        info.setInFlow( (Vector) c_function.getInFlow().clone());

        info.setOutFlow( (Vector) c_function.getOutFlow().clone());

        /* Make sure the flows are represented as ID */
        info.convertToID();
        c_flowinfo.addElement(info);
      }

      /* Non timestep related batch storage parameters */
      c_dblMinBatch = c_function.getMinBatch();
      c_dblMaxBatch = c_function.getMaxBatch();
      c_intBatchTime = c_function.getBatchTime();
      c_intAdjustingTime = c_function.getAdjustingTime();
      c_intPredetIntervals = c_function.getPredetIntervals();

      c_function.setTimestep(c_timestep);
    }

    /**
     * Save the data (for current timestep) to function
     * @return true if successful
     */

    private boolean save()
    {
      /* Save selected in- and out flows for all time steps */
      int old_timestep = c_timestep;
      Timesteplevel level = c_gui.getTopTimesteplevel();
      Timesteplevel thisLevel = c_function.getTimesteplevel();
      int timesteps = thisLevel.timestepDifference(level);
      for (int i = 0; i < timesteps; i++) {
        c_function.setTimestep(i + 1);
        TimestepInfo info = getTimestepInfo(i + 1);
        c_function.setTimestepInfo(info.getInFlow(),
                                   info.getOutFlow()
                                   );
      }
      c_function.setTimestep(old_timestep);
      c_function.setLabel(txtLabel.getText());

/* Non timestep related batch storage parameters */
      c_function.setMinBatch(c_dblMinBatch);
      c_function.setMaxBatch(c_dblMaxBatch);
      c_function.setBatchTime(c_intBatchTime);
      c_function.setAdjustingTime(c_intAdjustingTime);
      c_function.setPredetIntervals(c_intPredetIntervals);

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
    private void initComponents() {//GEN-BEGIN:initComponents

      c_inFlowsTableModel = new SelectedFlowsTableModel();
      c_outFlowsTableModel = new SelectedFlowsTableModel();
      c_lstSelectedInflow = new JTable(c_inFlowsTableModel);
      c_lstSelectedOutflow = new JTable(c_outFlowsTableModel);
      c_lstSelectedOutflow.setToolTipText(
          "Selected outgoing flows");
      c_lstSelectedInflow.setToolTipText(
          "Selected ingoing flows");

      /*  Register event listeners  */
      txtBatchBetweenMin.addFocusListener(this);
      txtBatchBetweenMax.addFocusListener(this);
      txtBatchTime.addFocusListener(this);
      txtAdjustingTime.addFocusListener(this);
      checkPredeterminedBatchIntervals.addFocusListener(this);


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
      sep3 = new javax.swing.JSeparator();
      sep4 = new javax.swing.JSeparator();
      pnlButtons = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1;

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        lblDescription.setText("Description: Defines a node batch function.");
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(lblDescription, gridBagConstraints1);

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
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
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        gridBagConstraints1.weightx = 1.0;
        getContentPane().add(pnlLabel, gridBagConstraints1);

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridy = 3;
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
        gridBagConstraints1.gridy = 4;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 10, 0, 10);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(pnlTimestep, gridBagConstraints1);

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

    GridBagConstraints gbc = new GridBagConstraints();
    pnlInFlowTotalAndEfficiency.setLayout(new GridBagLayout());
    pnlOutFlowTotalAndEfficiency.setLayout(new GridBagLayout());
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 3;
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.insets = new java.awt.Insets(0, 0, 0, 5);
//    pnlInFlowTotalAndEfficiency.add(new JLabel("Total Min"), gbc);
//    pnlOutFlowTotalAndEfficiency.add(new JLabel("Total Min"), gbc);
    gbc.gridx = 1;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.insets = new java.awt.Insets(0, 0, 0, 0);
//    pnlInFlowTotalAndEfficiency.add(txtTotalInFlowMin, gbc);
//    pnlOutFlowTotalAndEfficiency.add(txtTotalOutFlowMin, gbc);

    gbc.gridx = 2;
    gbc.weightx = 1;
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.insets = new java.awt.Insets(0, 10, 0, 5);
//    pnlInFlowTotalAndEfficiency.add(new JLabel("Max"), gbc);
//    pnlOutFlowTotalAndEfficiency.add(new JLabel("Max"), gbc);

    gbc.gridx = 3;
    gbc.weightx = 1;
    gbc.insets = new java.awt.Insets(0, 0, 0, 0);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.CENTER;
//    pnlInFlowTotalAndEfficiency.add(txtTotalInFlowMax, gbc);
//    pnlOutFlowTotalAndEfficiency.add(txtTotalOutFlowMax, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.weightx = 0;
    gbc.fill = GridBagConstraints.NONE;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.insets = new java.awt.Insets(0, 0, 0, 5);
//    pnlInFlowTotalAndEfficiency.add(new JLabel("Total Inflow Eff. %"), gbc);
//    pnlOutFlowTotalAndEfficiency.add(new JLabel("Total Outflow Eff. %"), gbc);
    gbc.gridx = 1;
    gbc.gridwidth = 3;
    gbc.weightx = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new java.awt.Insets(0, 0, 0, 0);
//    pnlInFlowTotalAndEfficiency.add(txtInFlowEfficiency, gbc);
//    pnlOutFlowTotalAndEfficiency.add(txtOutFlowEfficiency, gbc);

    gridBagConstraints4.gridx = 2;
    gridBagConstraints4.gridy = 5;
    gridBagConstraints4.gridheight = 1;
    gridBagConstraints4.gridwidth = 1;
    gridBagConstraints4.weightx = 0.5;
    gridBagConstraints4.insets = new java.awt.Insets(0, 5, 0, 15);
    gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
    pnlInflow.add(pnlInFlowTotalAndEfficiency, gridBagConstraints4);


        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 6;
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

        gridBagConstraints5.gridx = 2;
        gridBagConstraints5.gridy = 5;
        gridBagConstraints5.gridheight = 1;
        gridBagConstraints5.gridwidth = 1;
        gridBagConstraints5.weightx = 0.5;
        gridBagConstraints5.insets = new java.awt.Insets(0, 5, 0, 15);
        gridBagConstraints5.fill = java.awt.GridBagConstraints.BOTH;
        pnlOutflow.add(pnlOutFlowTotalAndEfficiency, gridBagConstraints5);


        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 7;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(0, 10, 10, 10);
        gridBagConstraints1.weightx = 1.0;
        getContentPane().add(pnlOutflow, gridBagConstraints1);

        pnlCostAtZero.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints6;

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 8;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(sep4, gridBagConstraints1);


        lblCostAtZero.setText("   Batch storage settings:");
        gridBagConstraints6 = new java.awt.GridBagConstraints();
        gridBagConstraints6.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlCostAtZero.add(lblCostAtZero, gridBagConstraints6);

        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 9;
        gridBagConstraints1.gridwidth = 1;
        getContentPane().add(pnlCostAtZero, gridBagConstraints1);

        /* Storage Panel
           Added by Jonas S‰‰v */

        lblUnit.setHorizontalAlignment(SwingConstants.LEFT);
        lblUnit.setText("n/a");
        gridLayout1.setColumns(5);
        gridLayout1.setHgap(4);
        gridLayout1.setRows(3);
        gridLayout1.setVgap(4);
        pnlStorage.setLayout(gridLayout1);
        txtBatchBetweenMin.setText("0");
        txtBatchBetweenMin.setToolTipText("Minimum batch amount ( >= 0)");
        txtBatchTime.setSelectionStart(3);
        txtBatchTime.setText("100");
        txtBatchTime.setToolTipText("Batch time. An integer between 0 and " +
                              (c_maxTimesteps));
        jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel3.setText("and");
        jLabel5.setToolTipText("");
        jLabel5.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel5.setText("Adjusting time");
        lblBatchTime.setToolTipText("");

        lblBatchTime.setText("Timestep(s)");
        lblAdjustingTime.setToolTipText("");
        lblAdjustingTime.setText("Timestep(s)");
        jLabel15.setToolTipText("");
        jLabel15.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel15.setText("Batch  Between");
        jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel2.setText("Batch time");
        jLabel11.setToolTipText("");
        jLabel11.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel6.setText("");
        jLabel4.setText("");
        jLabel9.setPreferredSize(new Dimension(0, 0));
        jLabel9.setText("");
        txtBatchBetweenMax.setText("");
        txtBatchBetweenMax.setToolTipText("Maximum batch amount");
        txtAdjustingTime.setToolTipText("Adjusting time. An integer between 0 and " +
                              (c_maxTimesteps));
        txtAdjustingTime.setText("");
        pnlStorage.add(jLabel15, null);
        pnlStorage.add(txtBatchBetweenMin, null);
        pnlStorage.add(jLabel3, null);
        pnlStorage.add(txtBatchBetweenMax, null);
        pnlStorage.add(lblUnit, null);
        pnlStorage.add(jLabel2, null);
        pnlStorage.add(txtBatchTime, null);
        pnlStorage.add(lblBatchTime, null);
        pnlStorage.add(jLabel11, null);
        pnlStorage.add(jLabel6, null);
        pnlStorage.add(jLabel5, null);
        pnlStorage.add(txtAdjustingTime, null);
        pnlStorage.add(lblAdjustingTime, null);
        pnlStorage.add(jLabel4, null);
        pnlStorage.add(jLabel9, null);

        pnlStorage.setPreferredSize(new java.awt.Dimension(470, 70));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 10;
        gridBagConstraints1.gridwidth = 5;
        gridBagConstraints1.gridheight = 4;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        gridBagConstraints1.weightx = 1.0;
        getContentPane().add(pnlStorage, gridBagConstraints1);

        if (c_maxTimesteps == 1) {
       //   txtBatchTime.disable();
       //   lblBatchTime.setText("");
       //   txtAdjustingTime.disable();
       //   lblAdjustingTime.setText("");
        }
/* End Storage Panel */

     //   pnlPredeterminedBatchIntervals = new JPanel();
        checkPredeterminedBatchIntervals.setText("Predetermined batch intervals");
        pnlPredeterminedBatchIntervals.add(checkPredeterminedBatchIntervals);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 15;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(pnlPredeterminedBatchIntervals, gridBagConstraints1);


        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 16;
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
        gridBagConstraints1.gridy = 17;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(pnlButtons, gridBagConstraints1);

        pack();
    }//GEN-END:initComponents

    /*
     * create an object matrix and inserts existing flows and coeff to the matrix
     */
    private void loadTables(){

      TimestepInfo currinfo = getTimestepInfo(c_timestep);

      /* Flow Settings */
      c_inFlowsTableModel.setData(currinfo.getInFlow());
      c_inFlowsTableModel.fireTableDataChanged();

      c_outFlowsTableModel.setData(currinfo.getOutFlow());
      c_outFlowsTableModel.fireTableDataChanged();

      /* Batch storage Settings (not timestep related) */
      txtBatchBetweenMin.setText("" + c_dblMinBatch);
      txtBatchBetweenMax.setText("" + c_dblMaxBatch);
      txtAdjustingTime.setText("" + c_intAdjustingTime);
      txtBatchTime.setText("" + c_intBatchTime);
      if (c_intPredetIntervals == 1)
        checkPredeterminedBatchIntervals.setSelected(true);
      else
        checkPredeterminedBatchIntervals.setSelected(false);

      lblUnit.setText(getUnit());
}

    /**
     * Action when the "remove outflow" button is pressed.
     */
    private void btnRemoveOutflowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveOutflowActionPerformed
        int rowNumber = c_lstSelectedOutflow.getSelectedRow();
        /* Do nothing if nothing is selected*/
        if(rowNumber == -1)
           return;

        Object selectedValue = c_lstSelectedOutflow.getValueAt(rowNumber, 0); // selectedValue is an ID
        TimestepInfo currentTimestepInfo = getTimestepInfo(c_timestep);

//        ID theId = parseOutId((String) selectedValue);
        /* Check if value already in list while trying to remove it
         * if so, don't remove it
         */
        if(!currentTimestepInfo.removeOutFlow((ID) selectedValue))
            return;

        loadTables();
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

        ID theId = (ID)parseOutId((String)selectedValue);
        /* Check if value already in list while trying to add it
         * if so, don't add it to the list
         */
        if(!currentTimestepInfo.addOutFlow((ID) theId))
            return;

        loadTables();
        btnRemoveOutflow.setEnabled(true);
    }//GEN-LAST:event_btnAddOutflowActionPerformed

    /**
     * Action when the "remove inflow" button is pressed.
     */
    private void btnRemoveInflowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveInflowActionPerformed
      int rowNumber = c_lstSelectedInflow.getSelectedRow();
        /* Do nothing if nothing is selected*/
        if(rowNumber == -1)
           return;

        Object selectedValue = c_lstSelectedInflow.getValueAt(rowNumber, 0);
        TimestepInfo currentTimestepInfo = getTimestepInfo(c_timestep);

        // ID theId = parseInId((String) selectedValue);
        /* Check if value already in list while trying to remove it
         * if so, don't remove it
         */
        if(!currentTimestepInfo.removeInFlow((ID) selectedValue))
            return;

        loadTables();
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

        ID theId = (ID)parseInId((String)selectedValue);
        /* Check if value already in list while trying to add it
         * if so, don't add it to the list
         */
        if(!currentTimestepInfo.addInFlow((ID) theId))
            return;

        loadTables();
        btnRemoveInflow.setEnabled(true);
    }//GEN-LAST:event_btnAddInflowActionPerformed

    /**
     * Action when the "OK" button is pressed.
     */
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed

      if (c_lstSelectedInflow.isEditing()) {
        int row = c_lstSelectedInflow.getEditingRow();
        int col = c_lstSelectedInflow.getEditingColumn();
        if (!c_lstSelectedInflow.getCellEditor(row, col).stopCellEditing())
        // Couldn't stop editing due to a bad value
        {
          c_gui.showWarningDialog("Bad cell value","Bad value in selected inflows table");
          return;
        }
      }
      if (c_lstSelectedOutflow.isEditing()) {
        int row = c_lstSelectedOutflow.getEditingRow();
        int col = c_lstSelectedOutflow.getEditingColumn();
        c_lstSelectedOutflow.getCellEditor(row, col).stopCellEditing();
        // Couldn't stop editing due to a bad value
        {
          c_gui.showWarningDialog("Bad cell value", "Bad value in selected outflows table");
          return;
        }
      }

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

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

// Variables declaration - do not modify//GEN-BEGIN:variables
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
private javax.swing.JSeparator sep3;
private javax.swing.JSeparator sep4;
private javax.swing.JPanel pnlButtons;
private javax.swing.JButton btnOk;
private javax.swing.JButton btnCancel;
// End of variables declaration//GEN-END:variables



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
        loadTables();
        if (newTimestepInfo.getInFlow().isEmpty())
            btnRemoveInflow.setEnabled(false);
        else
            btnRemoveInflow.setEnabled(true);

        if (newTimestepInfo.getOutFlow().isEmpty())
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
        JOptionPane.showMessageDialog(BatchDialog.this,
                                      message,
                                      title,
                                      JOptionPane.OK_OPTION);
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

  public void focusGained(FocusEvent e) {

  }

  /**
   * Since all textfield have very different constraint I don't find it
   * convenient to use a JFormattedTextField
   * @param e
   */
  public void focusLost(FocusEvent e) {
    Component comp = e.getComponent();

    TimestepInfo info = getTimestepInfo(c_timestep);

    try {
      if (comp instanceof NumberField) {
        ((NumberField)comp).commitEdit();
      }
    }
    catch (ParseException ex1) {
      return;
    }


        if (comp == txtBatchTime) {
           int value = (int) txtBatchTime.getFloatValue();

           if (value < 0)
             txtBatchTime.setText("-1");

        /* value IS allowed to be 0. No equation will be generated. */
        /*   if (value == 0) {
             c_gui.showWarningDialog(INVALID_VALUE_TITLE, "Value can't be zero");
             txtBatchTime.setText("1");
             txtBatchTime.requestFocus();
           }  */
           if (value > c_maxTimesteps) {
             c_gui.showWarningDialog(INVALID_VALUE_TITLE, "Max batch time can't be larger than\n"+
                                                         "the maximum number of timesteps, \n" +
                                                         "i.e. between 1 and " + (c_maxTimesteps));
            txtBatchTime.setText("" + (c_maxTimesteps-1));
            txtBatchTime.requestFocus();
           }
           c_intBatchTime = value;
        }

        if (comp == txtAdjustingTime) {
           int value = (int) txtAdjustingTime.getFloatValue();

           if (value < 0)
             txtAdjustingTime.setText("0");
           if (value > c_maxTimesteps) {
             c_gui.showWarningDialog(INVALID_VALUE_TITLE, "Max adjusting time can't be larger than\n"+
                                                         "the maximum number of timesteps, \n" +
                                                         "i.e. between 0 and " + (c_maxTimesteps));
            txtAdjustingTime.setText("" + (c_maxTimesteps));
            txtAdjustingTime.requestFocus();
           }
           c_intAdjustingTime = value;
        }

        if (comp == checkPredeterminedBatchIntervals) {
          c_intPredetIntervals = checkPredeterminedBatchIntervals.isSelected() ? 1:0;
        }


        if (comp == txtBatchBetweenMin) {
            float value = txtBatchBetweenMin.getFloatValue();

            if (value > c_dblMaxBatch && (c_dblMaxBatch > 0)) {
              c_gui.showWarningDialog(INVALID_VALUE_TITLE, MIN_INVALID_MESSAGE2);
              txtBatchBetweenMin.setText("0");
              txtBatchBetweenMin.requestFocus();
            }
            c_dblMinBatch = value;
        }


        if (comp == txtBatchBetweenMax) {
           float value = txtBatchBetweenMax.getFloatValue();
           if (value < 0) {
             txtBatchBetweenMax.setText("-1");
           } else
           if (value < c_dblMinBatch) {
             c_gui.showWarningDialog(INVALID_VALUE_TITLE, MAX_INVALID_MESSAGE);
             txtBatchBetweenMax.setText("-1");
             txtBatchBetweenMax.requestFocus();
           }
           c_dblMaxBatch = value;
        }

    }

}
