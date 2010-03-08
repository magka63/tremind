/*
 * Copyright 2004:
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

package mind.gui.dialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.*;
import java.text.*;

import mind.gui.*;
import mind.model.*;
import mind.model.function.*;
import mind.model.function.helpers.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * A dialog used for handling global Logical Equation properties.
 * Logical Equation was previosly named Binary Function, hence the name confusion.
 * You will just have to live with it, I'm afraid.
 * @author  Jonas S��v
 * @author  Per Fredriksson
 * @author 	Tor Knutsson
 */
public class BinaryFunctionDialog extends mind.gui.dialog.FunctionDialog implements FocusListener, ItemListener {
  //"constants"
  private final int TABLE_HEIGHT = 30;
  /* Various variables for this node */
  private ID c_nodeID;
  private GUI c_gui;
  private BinaryFunction c_function;
  private Timesteplevel c_tsl[];
  private int c_timestep; //Active timestep
  private int c_timesteplevels; //Number of timesteplevels
  private int c_maxTimesteps = 1;

  /* Dynamic GUI variables */
  private javax.swing.JLabel lblTSL[];
  private SpinButton spinTSL[];

  /* Data views */
  private JList c_lstAvailFlow = new JList();
  private JTable c_lstSelectedFlow;
  private SelectedFlowsTableModel c_flowsTableModel;

  /* RHS Panel variables*/
  GridLayout gridLayout1 = new GridLayout();
  JPanel pnlRHS = new JPanel();
  JLabel lblRHS = new JLabel("RHS-value  ");
  PositiveNumberField txtRHSValue = new PositiveNumberField();
  String[] items = {
      "=", ">=", "<="};
  JComboBox rhsConstraintComboBox = new JComboBox(items);
  /* End RHS panel variables */

  /*
   * Used for storing data about which flows that are
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

  /**
   * This class describes the abstract table model used for the selected flows
   * tables.
   */
  private class SelectedFlowsTableModel
      extends AbstractTableModel {

    final String[] columnNames = {
        "Flow", "Min.", "Max.", "Coeff"};
    private Vector flowsvector = null;
    private Vector minvector = null;
    private Vector maxvector = null;
    private Vector coeffvector = null;

    public int getColumnCount() {
      return columnNames.length;
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
      switch (col) {
        case 0:
          obj = flowsvector.get(row);
          break;
        case 1:
          obj = minvector.get(row);
          break;
        case 2:
          obj = maxvector.get(row);
          break;
        case 3:
          obj = coeffvector.get(row);
          break;
      }
      return obj;
    }

    public Class getColumnClass(int c) {
      return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
      //Note that the data/cell address is constant,
      //no matter where the cell appears onscreen.
      if (col < 1) { // the first column is label and is not editable
        return false;
      }
      else {
        return true;
      }
    }

    /**
     * Inputted data is verified here. Optimal and neatest solution is to
     * use userdefined celleditors instead. Anyone care to try?
     **/
    public void setValueAt(Object value, int row, int col) {
      boolean value_ok = false;
      double doublevalue = 0.0;
      int intvalue = 1;

      if (value != null) {
        if (col < 3)
          doublevalue = ( (Double) value).doubleValue();
        else
          intvalue = ( (Integer) value).intValue();
      }
      else {
        if (col == 2) {
          value = new Double( -1.0);
          doublevalue = -1.0;
        }
        else
          return;
      }

      switch (col) {
        case 1:
          if (doublevalue < 0.0) {
            c_gui.showWarningDialog(MIN_INVALID_TITLE, MIN_INVALID_MESSAGE);
            value_ok = false;
          }
          else if (doublevalue > ( (Double) maxvector.get(row)).doubleValue() &&
                   ( (Double) maxvector.get(row)).doubleValue() != -1) {
            c_gui.showWarningDialog(MIN_INVALID_TITLE, MIN_INVALID_MESSAGE2);
          }
          else {
            minvector.set(row, value);
            value_ok = true;
          }
          break;
        case 2:
          if (doublevalue < ( (Double) minvector.get(row)).doubleValue() &&
              doublevalue > 0) {
            c_gui.showWarningDialog(MAX_INVALID_TITLE, MAX_INVALID_MESSAGE);
            value_ok = false;
          }
          else {
            maxvector.set(row, value);
            value_ok = true;
          }
          break;
          case 3:
              coeffvector.set(row, new Integer(intvalue));
              value_ok = true;
            break;
      }
      if (value_ok)
        fireTableCellUpdated(row, col);
    }

    /**
     * Sets the data vectors. They must be of equal lengths.
     * @param namesVector The vector containing the labels of the length values
     * @param lengthsVector The vector containing the length values (floats)
     */
    public void setData(Vector flows, Vector min, Vector max, Vector coeffs) {
      flowsvector = flows;
      minvector = min;
      maxvector = max;
      coeffvector = coeffs;
    }
  }

  /**
   * Internal class which holds information about one
   * timestep.
   */
  class TimestepInfo {
    /*
     * List of all selected outflows/inflows
     */
    private Vector c_Flow = new Vector(0);
    private Vector c_FlowMin = new Vector(0);
    private Vector c_FlowMax = new Vector(0);
    private Vector c_Coeff = new Vector(0);

    private int c_RHSValue = 0;
    private String c_RHSConstraint = "E"; // Can be  "G" and "L" and "E"

    void convertToID() {
      Vector new_c_Flow = new Vector(0);
      for (int i = 0; i < c_Flow.size(); i++) {
        ID theId = (ID) parseId( (String) c_Flow.get(i).toString());
        new_c_Flow.addElement( (ID) theId);
      }
      setFlow(new_c_Flow);
    }

    /**
     * Constructor
     */
    public TimestepInfo() {
    }

    /**
     * Constructor wich initializes the class
     * according to an instance of the same class
     * wich is given to it.
     * @param info An instance of this class.
     */
    public TimestepInfo(TimestepInfo info) {
      c_Flow = new Vector(info.getFlow());
      c_FlowMin = new Vector(info.getFlowMin());
      c_FlowMax = new Vector(info.getFlowMax());
      c_Coeff = new Vector(info.getCoeff());

      c_RHSValue = info.getRHSValue();
      c_RHSConstraint = info.getRHSConstraint();

    }

    /**
     * Return selected inflow min coefficients
     * @return Vector of inflows
     */
    Vector getFlowMin() {
      return c_FlowMin;
    }

    /**
     * Return selected inflow max coefficients
     * @return Vector of inflows
     */
    Vector getFlowMax() {
      return c_FlowMax;
    }

    /**
     * Return selected inflows
     * @return Vector of inflows
     */
    Vector getFlow() {
      return c_Flow;
    }

    /**
     * Return seleced flows coefficients
     */
    Vector getCoeff() {
      return c_Coeff;
    }

    /**
     * Return RHS Value
     */
    int getRHSValue() {
      return c_RHSValue;
    }

    /**
     * Return RHS Constraint type
     */
    String getRHSConstraint() {
      return c_RHSConstraint;
    }

    /**
     * Select inflows (all at once)
     * @param f Vector of inflows
     */
    void setFlow(Vector f) {
      c_Flow = f;
    }

    /**
     * Set flows min boundary vector
     * @param f Vector of inflows
     */
    void setFlowMin(Vector f) {
      c_FlowMin = f;
    }

    /**
     * Set flows max max boundary vector
     * @param f Vector of inflows
     */
    void setFlowMax(Vector f) {
      c_FlowMax = f;
    }

    /**
     * Set the binary variable coefficient vector
     * @param f
     */
    void setCoeff(Vector f) {
      c_Coeff = f;
    }

    /**
     * Set RHS Value
     * @param int RHS Value
     */

    void setRHSValue(int value) {
      c_RHSValue = value;
    }

    void setRHSConstraint(String constraint) {
      c_RHSConstraint = constraint;
    }

    /**
     * Clears both inflows and
     * outflows
     */
    void clear() {
      c_Flow.clear();
      c_FlowMin.clear();
      c_FlowMax.clear();
      c_Coeff.clear();
    }

    /**
     * Add inflow
     * @param flowId ID of inflow to add
     * @return true if successful
     */
    boolean addFlow(ID flowId) {
      if (flowId == null || c_Flow.contains(flowId))
        return false;

      c_Flow.addElement(flowId);
      c_FlowMin.addElement(new Double(0.0));
      c_FlowMax.addElement(new Double( -1.0)); // negative means that there is no upper boundary
      c_Coeff.addElement(new Integer(1));

      return true;
    }

    /**
     * Remove inflow
     * @param flowId ID of inflow to remove
     * @return true if successful
     */
    boolean removeFlow(ID flowId) {
      if (flowId == null) // || !c_Flow.contains(flow))
        return false;

      int index = c_Flow.indexOf(flowId);
      c_FlowMin.remove(index);
      c_FlowMax.remove(index);
      c_Coeff.remove(index);

      return c_Flow.removeElement(flowId);
    }

  }
  /* End class TimestepInfo */

  /**
   * Creates new form BinaryFunctionDialog.
   * The parameters are the same as for other functions.
   */
  public BinaryFunctionDialog(JDialog parent, boolean modal,
                              ID nodeID, NodeFunction function, GUI gui) {
    super(parent, modal);


    /* Init structures */
    c_flowinfo = new Vector();

    /* Init structures from "above" */
    c_gui = gui;
    c_nodeID = nodeID;
    c_function = (BinaryFunction) function;

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

    scrollAvailFlow.getViewport().setView(c_lstAvailFlow);
    scrollSelectedFlow.getViewport().setView(c_lstSelectedFlow);

    txtLabel.setText(c_function.getLabel());

    /* Put content in dialog */
    initAvailableFlows();
    updateTimestep();

    pack();
    pack();

    setSize(450, 450);
  }

  /**
   * Gets the unit of a flow
   * @param flowId
   * @return the unit string
   */
  private String getResourceOfFlow(ID flowId) {
    String unit;
    Resource res = (Resource) Model.getResource(flowId);
    if (res != null)
      unit = res.getUnit();
    else
      unit = "no unit";

    return unit;
  }

  /**
   * Set available flows in the flow lists.
   */
  private void initAvailableFlows() {
    Flow[] availFlow = c_gui.getAllFlows();

    String[] availFlowString = convertToString(availFlow);

    c_lstAvailFlow.setListData(availFlowString);

    if (availFlowString[0].equals("No flow"))
      btnAddFlow.setEnabled(false);

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

      info.setFlow( (Vector) c_function.getFlow().clone());
      info.setFlowMin( (Vector) c_function.getMin().clone());
      info.setFlowMax( (Vector) c_function.getMax().clone());
      info.setCoeff( (Vector) c_function.getCoeff().clone());

      info.setRHSValue(c_function.getRHSValue());
      info.setRHSConstraint(c_function.getRHSConstraint());

      /* Make sure the flows are represented as ID */
      info.convertToID();
      c_flowinfo.addElement(info);
    }

    c_function.setTimestep(c_timestep);
  }

  /**
   * Save the data (for current timestep) to function
   * @return true if successful
   */

  private boolean save() {
    /* Save selected in- and out flows for all time steps */
    int old_timestep = c_timestep;
    Timesteplevel level = c_gui.getTopTimesteplevel();
    Timesteplevel thisLevel = c_function.getTimesteplevel();
    int timesteps = thisLevel.timestepDifference(level);
    for (int i = 0; i < timesteps; i++) {
      c_function.setTimestep(i + 1);
      TimestepInfo info = getTimestepInfo(i + 1);
      c_function.setTimestepInfo(info.getFlow(),
                                 info.getFlowMin(),
                                 info.getFlowMax(),
                                 info.getCoeff(),
                                 info.getRHSValue(),
                                 info.getRHSConstraint()
                                 );
    }
    c_function.setTimestep(old_timestep);
    c_function.setLabel(txtLabel.getText());
    return true;
  }

  /*
   * Get the ID of an inflow.
   * @param stringID the ID of an inflow as a string
   * @return the ID as an ID-object
   */
  private ID parseId(String stringID) {
    Flow[] currentFlow = c_gui.getAllFlows();
    for (int i = 0; i < currentFlow.length; i++) {
      if (stringID.equals(currentFlow[i].toString()))
        return currentFlow[i].getID(); //should only by one with this ID
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
    for (int i = 0; i < c_timesteplevels; i++) {
      final int j = i;
      //Add timestep labels
      constraints = new java.awt.GridBagConstraints();
      constraints.gridx = 0;
      constraints.gridy = i + 1;
      constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;

      lblTSL[i] = new javax.swing.JLabel();
      lblTSL[i].setText(level.getLabel());
      pnlTimestep.add(lblTSL[i], constraints);

      //Add timestep spinbuttons
      constraints = new java.awt.GridBagConstraints();
      constraints.gridx = 1;
      constraints.gridy = i + 1;
      constraints.insets = new java.awt.Insets(0, 15, 0, 0);
      constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
      constraints.weightx = 1.0;

      spinTSL[i] = new SpinButton(1, level.getMaxTimesteps(), 1, 1);
      pnlTimestep.add(spinTSL[i], constraints);

      spinTSL[i].addListener(new SpinButtonListener() {
        SpinButton button = spinTSL[j];
        public void valueDecreased() {
          if (!updateTimestep()) {
            button.incValue();
            showWarningDialog(UPPER_INVALID_TITLE,
                              UPPER_INVALID_MESSAGE);
          }
        }

        public void valueIncreased() {
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
  private void initComponents() { //GEN-BEGIN:initComponents

    c_flowsTableModel = new SelectedFlowsTableModel();
    c_lstSelectedFlow = new JTable(c_flowsTableModel);
    c_lstSelectedFlow.setToolTipText(
        "type a negative max value for a free maximum");

    /*  Register event listeners  */
    // Added by Nawzad Mardan 20100308
    c_lstSelectedFlow.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
        public void valueChanged(ListSelectionEvent event) {

            save();
            updateLable();
            }
    });
    txtRHSValue.addFocusListener(this);
    rhsConstraintComboBox.addItemListener(this);

    lblDescription = new javax.swing.JLabel();
    sep1 = new javax.swing.JSeparator();
    pnlLabel = new javax.swing.JPanel();
    lblLabel = new javax.swing.JLabel();
    txtLabel = new javax.swing.JTextField();
    sep2 = new javax.swing.JSeparator();
    pnlTimestep = new javax.swing.JPanel();
    lblTimestep = new javax.swing.JLabel();
    pnlFlow = new javax.swing.JPanel();
    scrollAvailFlow = new javax.swing.JScrollPane();
    btnAddFlow = new javax.swing.JButton();
    btnRemoveFlow = new javax.swing.JButton();
    scrollSelectedFlow = new javax.swing.JScrollPane();
    lblAvailFlow = new javax.swing.JLabel();
    lblSelectedFlow = new javax.swing.JLabel();
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

    lblDescription.setText("Description: Defines a global logical equation.");
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

    pnlFlow.setLayout(new java.awt.GridBagLayout());
    java.awt.GridBagConstraints gridBagConstraints4;

    scrollAvailFlow.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.
                                                 HORIZONTAL_SCROLLBAR_NEVER);
    scrollAvailFlow.setVerticalScrollBarPolicy(javax.swing.JScrollPane.
                                               VERTICAL_SCROLLBAR_ALWAYS);
    scrollAvailFlow.setPreferredSize(new java.awt.Dimension(100, 100));
    gridBagConstraints4 = new java.awt.GridBagConstraints();
    gridBagConstraints4.gridx = 0;
    gridBagConstraints4.gridy = 1;
    gridBagConstraints4.gridheight = 4;
    gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 5);
    gridBagConstraints4.weightx = 0;
    gridBagConstraints4.weighty = 0.5;  // h�r!!!
    pnlFlow.add(scrollAvailFlow, gridBagConstraints4);

    btnAddFlow.setText("=>");
    btnAddFlow.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnAddInflowActionPerformed(evt);
      }
    });

    gridBagConstraints4 = new java.awt.GridBagConstraints();
    gridBagConstraints4.gridx = 1;
    gridBagConstraints4.gridy = 2;
    gridBagConstraints4.insets = new java.awt.Insets(10, 0, 0, 0);
    pnlFlow.add(btnAddFlow, gridBagConstraints4);

    btnRemoveFlow.setText("<=");
    btnRemoveFlow.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnRemoveInflowActionPerformed(evt);
      }
    });

    gridBagConstraints4 = new java.awt.GridBagConstraints();
    gridBagConstraints4.gridx = 1;
    gridBagConstraints4.gridy = 3;
    gridBagConstraints4.insets = new java.awt.Insets(5, 0, 0, 0);
    pnlFlow.add(btnRemoveFlow, gridBagConstraints4);

    scrollSelectedFlow.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.
        HORIZONTAL_SCROLLBAR_NEVER);
    scrollSelectedFlow.setVerticalScrollBarPolicy(javax.swing.JScrollPane.
                                                  VERTICAL_SCROLLBAR_ALWAYS);
    scrollSelectedFlow.setPreferredSize(new java.awt.Dimension(100, 100));
    gridBagConstraints4 = new java.awt.GridBagConstraints();
    gridBagConstraints4.gridx = 2;
    gridBagConstraints4.gridy = 1;
    gridBagConstraints4.gridheight = 4;
    gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints4.insets = new java.awt.Insets(0, 5, 0, 0);
    gridBagConstraints4.weightx = 0.5;
    pnlFlow.add(scrollSelectedFlow, gridBagConstraints4);

    lblAvailFlow.setText("Available flow(s)");
    gridBagConstraints4 = new java.awt.GridBagConstraints();
    gridBagConstraints4.gridx = 0;
    gridBagConstraints4.gridy = 0;
    pnlFlow.add(lblAvailFlow, gridBagConstraints4);

    lblSelectedFlow.setText("Selected flow(s)");
    gridBagConstraints4 = new java.awt.GridBagConstraints();
    gridBagConstraints4.gridx = 2;
    gridBagConstraints4.gridy = 0;
    pnlFlow.add(lblSelectedFlow, gridBagConstraints4);

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridy = 6;
    gridBagConstraints1.gridwidth = 2;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH; // h�r!!!
    gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
    gridBagConstraints1.weightx = 1.0;
    gridBagConstraints1.weighty = 0.5;
    // pnlFlow.setBorder(BorderFactory.createTitledBorder("Title is here"));
    getContentPane().add(pnlFlow, gridBagConstraints1);

    /* Storage Panel
       Added by Jonas S��v */

    JPanel pnlRHS = new JPanel();
    pnlRHS.setLayout(new java.awt.GridBagLayout());
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridy = 0;
    gridBagConstraints1.gridwidth = 1;
    pnlRHS.add(lblRHS, gridBagConstraints1);

    gridBagConstraints1.gridx = 1;
    pnlRHS.add(rhsConstraintComboBox, gridBagConstraints1);

    gridBagConstraints1.gridx = 2;
    gridBagConstraints1.weightx = 30;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 0);
    pnlRHS.add(txtRHSValue, gridBagConstraints1);

    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridy = 15;
    gridBagConstraints1.gridwidth = 3;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);

    getContentPane().add(pnlRHS, gridBagConstraints1);

    // Added by Nawzad Mardan 20100307
    lblEquationP1 = new JLabel("");
    lblEquationP2 = new JLabel("");
    lblEquationP3 = new JLabel("");
    lblEquationP1.setFont(new Font("SansSerif", Font.ITALIC, 15));
    lblEquationP1.setBackground(Color.BLUE);
    lblEquationP1.setForeground(Color.BLUE);
    lblEquationP2.setFont(new Font("SansSerif", Font.ITALIC, 15));
    lblEquationP2.setBackground(Color.BLUE);
    lblEquationP2.setForeground(Color.BLUE);
    lblEquationP3.setFont(new Font("SansSerif", Font.ITALIC, 15));
    lblEquationP3.setBackground(Color.BLUE);
    lblEquationP3.setForeground(Color.BLUE);
    updateLable();
    pnlEquation = new JPanel();
    pnlEquation.add(lblEquationP1);
    pnlEquation.add(lblEquationP2);
    pnlEquation.add(lblEquationP3);
   // gc.insets = new java.awt.Insets(5, 0, 5, 50);
    //gridBagConstraints3 = new java.awt.GridBagConstraints();
    //gridBagConstraints3.gridx = 0;
    gridBagConstraints1.gridy = 16;
    //gridBagConstraints3.gridwidth = 2;
    //gridBagConstraints3.insets = new java.awt.Insets(0, 0, 10, 5);
    //gridBagConstraints3.anchor = java.awt.GridBagConstraints.HORIZONTAL;
    //gridBagConstraints3.weightx = 1.0;
    this.getContentPane().add( pnlEquation, gridBagConstraints1);
    /* End Storage Panel */

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridy = 17;
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
    gridBagConstraints1.gridy = 18;
    gridBagConstraints1.gridwidth = 2;
    gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
    getContentPane().add(pnlButtons, gridBagConstraints1);

    pack();
  } //GEN-END:initComponents

  /*
   * create an object matrix and inserts existing flows and coeff to the matrix
   */
  private void loadTables() {

    TimestepInfo currinfo = getTimestepInfo(c_timestep);

    /* Flow Settings */
    c_flowsTableModel.setData(currinfo.getFlow(), currinfo.getFlowMin(),
                              currinfo.getFlowMax(), currinfo.getCoeff());
    c_flowsTableModel.fireTableDataChanged();

    /* load the RHS value */
    txtRHSValue.setText("" + currinfo.getRHSValue());

    //load the constraint value
    int tempindex=0;
    String tempstring=null;
    tempstring = currinfo.getRHSConstraint();
    if (tempstring.equals("E"))
      tempindex = 0;
    if (tempstring.equals("G"))
      tempindex = 1;
    if (tempstring.equals("L"))
      tempindex = 2;
    rhsConstraintComboBox.setSelectedIndex(tempindex);

  }

  /**
   * Action when the "remove inflow" button is pressed.
   */
  private void btnRemoveInflowActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnRemoveInflowActionPerformed
    int rowNumber = c_lstSelectedFlow.getSelectedRow();
    /* Do nothing if nothing is selected*/
    if (rowNumber == -1)
      return;

    Object selectedValue = c_lstSelectedFlow.getValueAt(rowNumber, 0);
    TimestepInfo currentTimestepInfo = getTimestepInfo(c_timestep);

    // ID theId = parseInId((String) selectedValue);
    /* Check if value already in list while trying to remove it
     * if so, don't remove it
     */
    if (!currentTimestepInfo.removeFlow( (ID) selectedValue))
      return;

    loadTables();
    if (currentTimestepInfo.getFlow().isEmpty())
      btnRemoveFlow.setEnabled(false);
    // Added by Nawzad Mardan 20100308
    //save();
    //updateLable();
  } //GEN-LAST:event_btnRemoveInflowActionPerformed

  /**
   * Action when the "add inflow" button is pressed.
   */
  private void btnAddInflowActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnAddInflowActionPerformed
    Object selectedValue = c_lstAvailFlow.getSelectedValue();
    /* Do nothing if nothing is selected*/
    if (selectedValue == null)
      return;
    TimestepInfo currentTimestepInfo = getTimestepInfo(c_timestep);

    ID theId = (ID) parseId( (String) selectedValue);
    /* Check if value already in list while trying to add it
     * if so, don't add it to the list
     */
    if (!currentTimestepInfo.addFlow( (ID) theId))
      return;

    loadTables();
    btnRemoveFlow.setEnabled(true);
    // Added by Nawzad Mardan 20100308
    save();
    updateLable();
  } //GEN-LAST:event_btnAddInflowActionPerformed

  /**
   * Action when the "OK" button is pressed.
   */
  private void btnOkActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnOkActionPerformed

    if (c_lstSelectedFlow.isEditing()) {
      int row = c_lstSelectedFlow.getEditingRow();
      int col = c_lstSelectedFlow.getEditingColumn();
      if (!c_lstSelectedFlow.getCellEditor(row, col).stopCellEditing())
      // Couldn't stop editing due to a bad value
      {
        c_gui.showWarningDialog("Bad cell value",
                                "Bad value in selected inflows table");
        return;
      }
    }

    if (!save()) {
      return;
    }

    closeDialog(null);
  } //GEN-LAST:event_btnOkActionPerformed

  /**
   * Action when the "cancel" button is pressed.
   */
  private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnCancelActionPerformed
    setVisible(false);
    dispose();
  } //GEN-LAST:event_btnCancelActionPerformed

  /** Closes the dialog */
  private void closeDialog(java.awt.event.WindowEvent evt) { //GEN-FIRST:event_closeDialog
    setVisible(false);
    dispose();
  } //GEN-LAST:event_closeDialog

// Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel lblDescription;
  private javax.swing.JSeparator sep1;
  private javax.swing.JPanel pnlLabel;
  private javax.swing.JLabel lblLabel;
  private javax.swing.JTextField txtLabel;
  private javax.swing.JSeparator sep2;
  private javax.swing.JPanel pnlTimestep;
  private javax.swing.JLabel lblTimestep;
  private javax.swing.JPanel pnlFlow;
  private javax.swing.JScrollPane scrollAvailFlow;
  private javax.swing.JButton btnAddFlow;
  private javax.swing.JButton btnRemoveFlow;
  private javax.swing.JScrollPane scrollSelectedFlow;
  private javax.swing.JLabel lblAvailFlow;
  private javax.swing.JLabel lblSelectedFlow;
  private javax.swing.JSeparator sep3;
  private javax.swing.JSeparator sep4;
  private javax.swing.JPanel pnlButtons;
  private javax.swing.JButton btnOk;
  private javax.swing.JButton btnCancel;
 // Added by Nawzad Mardan 20100308
  private JPanel pnlEquation;
  private JLabel lblEquationP1;
  private JLabel lblEquationP2;
  private JLabel lblEquationP3;
  // End of variables declaration//GEN-END:variables

  /**
   * Update selected in- and outflows according to new timestep.
   * @return true if all "old" timestep info is correct
   */
  public boolean updateTimestep() {
    Vector currentInflow, currentOutflow;

    //Calculate and set new timestep
    c_timestep = 1; //The first timestep is 1 (not 0)
    int factor = 1;
    for (int i = c_timesteplevels - 1; i > 0; i--) {
      c_timestep = c_timestep + (spinTSL[i].getValue() - 1) * factor;
      factor = factor * c_tsl[i].getMaxTimesteps();
    }
    c_function.setTimestep(c_timestep);

    //Update GUI for flows and flow buttons
    TimestepInfo newTimestepInfo = getTimestepInfo(c_timestep);
    loadTables();
    if (newTimestepInfo.getFlow().isEmpty())
      btnRemoveFlow.setEnabled(false);
    else
      btnRemoveFlow.setEnabled(true);

    return true;
  }

  /**
   * Shows warning dialog.
   * @param title Title of dialog.
   * @param message Message in dialog.
   */
  private void showWarningDialog(String title, String message) {
    JOptionPane.showMessageDialog(BinaryFunctionDialog.this,
                                  message,
                                  title,
                                  JOptionPane.OK_OPTION);
  }

  /**
   * Get timestep info for a timestep.
   * @param timestep Timestep number
   * @return timestep info for this timestep
   */
  public TimestepInfo getTimestepInfo(int timestep) {
    try {
      return (TimestepInfo) c_flowinfo.elementAt(timestep - 1);
    }
    catch (ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }

  /*
   * converts a flow array into a string array representing the ID's
   */
  private String[] convertToString(Flow[] f) {

    if (f == null) {
      String[] theStrings = {
          "No flow"};
      return theStrings;
    }
    else {
      String[] theStrings = new String[f.length];
      for (int i = 0; i < f.length; i++) {
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
        ( (NumberField) comp).commitEdit();
      }
    }
    catch (ParseException ex1) {
      return;
    }

    if (comp == txtRHSValue) {
        float value = txtRHSValue.getFloatValue();
        info.setRHSValue((int) value);
        // Added by Nawzad Mardan 20100308
        save();
        updateLable();
    }

  }

  public void itemStateChanged(ItemEvent e) {

    TimestepInfo info = getTimestepInfo(c_timestep);

    String tempstring = null;
    switch (rhsConstraintComboBox.getSelectedIndex()) {
      case 0:
        tempstring = "E";
        break;
      case 1:
        tempstring = "G";
        break;
      case 2:
        tempstring = "L";
        break;
    }
    info.setRHSConstraint(tempstring);
    // Added by Nawzad Mardan 20100308
    save();
    updateLable();

  }

  private void updateLable()
    {
    TimestepInfo info = getTimestepInfo(c_timestep);
    if(info!=null)
        {
        Vector flows, coeff;
        flows = info.getFlow();
        coeff = info.getCoeff();
        String equation = " ";
        Integer o;
        int coef;
        for(int i = 0; i< coeff.size();i++)
            {
            //Objeck o equation + = (String)incoeff.elementAt(i);
             o  = (Integer)coeff.elementAt(i);
             coef = o.intValue();
             if((coef == -1))
                 equation  = equation + "-";
             //else if((coef == -1) && (i != 0))
               //  equation  = equation + "+ -";
             else if((coef == 1)&& (i == 0))
                 equation = equation +"";
             else if((coef == 1) && (i != 0))
                 equation = equation + " + " ;
             else
                {
                 if(coef > 1)
                    {
                     if(i != 0)
                        equation = equation + " + "+ coef ;
                     else
                         equation = equation + coef ;
                    }
                 else
                    equation = equation + " "+ coef ;
                }
             if(flows!=null)
               equation = equation +  flows.elementAt(i) + " ";

            }
        lblEquationP1.setText(equation);
        if((lblEquationP1.getText().equals(" ")))
            lblEquationP2.setText("");
        else
            lblEquationP2.setText(getRHS(info.getRHSConstraint()));
        if((lblEquationP1.getText().equals(" ")) && (lblEquationP2.getText().equals("")) )
            lblEquationP3.setText("  " + " ");
        else
            lblEquationP3.setText("  " + " "+ info.getRHSValue());
        }

    }

  private String getRHS(String rhs)
  {

    if(rhs!=null)
        {
        if(rhs.equals("E"))
            rhs = " = ";
        else if(rhs.equals("G"))
            rhs = " >= ";
        else if(rhs.equals("L"))
            rhs = " <= ";
        }
    else
        rhs = " = ";
   return rhs;

  }
}