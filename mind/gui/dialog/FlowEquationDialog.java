/*
 * Copyright 2002:
 * Urban Liljedahl <lilje@sm.luth.se>
 * 
 * Copyright 2007:
 * Per Fredriksson <perfr775@student.liu.se>
 * David Karlsl�tt <davka417@student.liu.se>
 * Tor Knutsson	<torkn754@student.liu.se>
 * Daniel K�llming <danka053@student.liu.se>
 * Ted Palmgren <tedpa175@student.liu.se>
 * Freddie Pintar <frepi150@student.liu.se>
 * M�rten Thur�n <marth852@student.liu.se>
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
package mind.gui.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import mind.EventHandlerClient;
import mind.gui.*;
import mind.model.*;
import mind.model.function.*;
// Added by Nawzad Mardan 20100306
import javax.swing.table.AbstractTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



/**
 * The purpose for this class: present a first HIFI prototyp and also
 * connect to the Mps class for exports
 *
 * @author Urban Liljedahl
 * @author  Per Fredriksson
 * @author 	Tor Knutsson
 * @version 2007-12-10
 */
public class FlowEquationDialog extends mind.gui.dialog.FunctionDialog{
    //"constants"
    private final int TABLE_HEIGHT = 30;
    //variables
    private JLabel lblDescription;
    private JLabel lblLabel;
    private JLabel lblTimestep;
    private JLabel lblTSL[];
    private JLabel lblInFlow;
    private JLabel lblOutFlow;
    private JLabel lblIN;
    private JLabel lblOUT;
    private JLabel lblRHSValue;
    private JTextField txtLabel;
    private NumberField txtRHSValue;
    private JComboBox rshConstraintComboBox;
    private JList lstInFlow;
    private JList lstOutFlow;

    private JTable tblIN;
    private JTable tblOUT;
    private JButton btnAddin;
    private JButton btnRemovein;
    private JButton btnAddout;
    private JButton btnRemoveout;
    private SpinButton spinTSL[];
    // Added by Nawzad Mardan 20100306
    private JPanel pnlEquation;
    private JLabel lblEquationP1;
    private JButton btnOk;
    private JButton btnCancel;
    private boolean c_coefFieldEmpty;

    //the selected flows with coefficients for in and out
    private FlowEquationValue flowValues, temFlowValues;
    private ID	c_nodeID;
    private GUI	c_gui;
    private FlowEquation c_function;
    private EventHandlerClient c_eventhandler;
    // Added by Nawzad Mardan 20100326
    private JScrollPane scrollInFlowList;
    private JScrollPane scrollOutFlowList;
    //timestep handling
    private int c_timesteplevels;//number of levels
    private int c_timestep;//current timestep
    private Timesteplevel c_tsl[];

    /**
     * Inner class representing the in flows with coefficients
     */
    class FlowEquationValue{
	private Vector c_flowIn = new Vector(0);
	private Vector c_coeffIn = new Vector(0);
	private Vector c_isIntegerIn = new Vector(0);
	private Vector c_flowOut = new Vector(0);
	private Vector c_coeffOut = new Vector(0);
	private Vector c_isIntegerOut = new Vector(0);
	private float c_equationvalue;
        private String c_rhsConstraint = "E"; // Can be  "E" and "G" and "L"

	FlowEquationValue(){
	}

	Vector getFlowIn(){
	    return c_flowIn;
	}
	Vector getCoeffIn(){
	    return c_coeffIn;
	}
	Vector getIsIntegerIn(){
	    return c_isIntegerIn;
	}
	Vector getFlowOut(){
	    return c_flowOut;
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
	void setFlowIn(Vector f){
	    c_flowIn = f;
	}
	void setCoeffIn(Vector c){
	    c_coeffIn = c;
	}
	void setIsIntegerIn(Vector i){
	    c_isIntegerIn = i;
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

	void setFlowOut(Vector f){
	    c_flowOut = f;
	}
	void setCoeffOut(Vector c){
	    c_coeffOut = c;
	}
	void setIsIntegerOut(Vector i){
	    c_isIntegerOut = i;
	}


	void clear(){
	    c_flowIn.clear();
	    c_coeffIn.clear();
	    c_isIntegerIn.clear();
	    c_flowOut.clear();
	    c_coeffOut.clear();
	    c_isIntegerOut.clear();
	    c_equationvalue = (float)0.0;
	}



	public String toString(){
	    //build a string consisting of the in and out vectors
	    String buffer = "Flow values:\n";
	    for( int i = 0; i < c_flowIn.size(); i++ ){
		ID theFlow = (ID)c_flowIn.elementAt(i);
		Float theCoeff = (Float)c_coeffIn.elementAt(i);
		buffer = buffer + theFlow.toString() + " " + theCoeff.toString() + '\n';
	    }
	    /*
	    for( int i = 0; i < c_flowOut.size(); i++ ){
		ID theFlow = (ID)c_flowOut.elementAt(i);
		Float theCoeff = (Float)c_coeffOut.elementAt(i);
		buffer = buffer + theFlow.toString() + " " + theCoeff.toString() + '\n';
	    }
	    */
	    return buffer;
	}
    }

    //constructor
    public FlowEquationDialog(){
	flowValues = new FlowEquationValue();
	load();
	initComponents();
    }
    public FlowEquationDialog(JDialog parent, boolean modal, ID nodeID,
				NodeFunction function, GUI gui) {
	super (parent, modal);
	flowValues = new FlowEquationValue();
	c_nodeID = nodeID;
	c_gui = gui;
	c_function = (FlowEquation) function;
	c_eventhandler = gui.getEventHandlerClient();
	load();

	//calculate number of timestepslevels in the function
	c_timesteplevels = 1;
	Timesteplevel level = c_eventhandler.getTopTimesteplevel();
	Timesteplevel thisLevel = c_function.getTimesteplevel();
	while (level != thisLevel) {
	    c_timesteplevels++;
	    level = level.getNextLevel();
	}
	//Get all timesteplevels
	c_tsl = new Timesteplevel[c_timesteplevels];
	level = c_eventhandler.getTopTimesteplevel();
	for(int j=0; j<c_timesteplevels; j++) {
	    c_tsl[j] = level;
	    level = level.getNextLevel();
	}

	c_timestep = 1;
	c_function.setTimestep(c_timestep);

	initComponents();
    }
    //metods
    //create the FlowEquationValue consisting of data from the in and out tables.
    private int save(){
	if(flowValues != null){
	    // clear the flowValues
	    flowValues.clear();

	    //store all ID matching all inFlows
	    for(int i = 0; i < tblIN.getRowCount(); i++ ){
		if(tblIN.getValueAt( i, 0) != null && !((String)tblIN.getValueAt( i, 0)).equals("")){
		    try{
			Vector flow, coeff, isInteger;
			flow = flowValues.getFlowIn();
			coeff = flowValues.getCoeffIn();
			isInteger = flowValues.getIsIntegerIn();
			ID theId = null;

			if( (theId = (ID)parseInId((String)tblIN.getValueAt( i, 0))) != null ){
			    flow.addElement(theId);
			    float currentCoeff = this.parseValue((String)tblIN.getValueAt( i, 1));
			    coeff.addElement(new Float(currentCoeff));
			    isInteger.addElement(new Boolean(false));
			}
		    }catch(NullPointerException e){
			JOptionPane.showMessageDialog( this, "Type a coefficient value for each selected in flow",
						       "Type coefficient",
						       JOptionPane.INFORMATION_MESSAGE);
		    }catch(Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog( this, e.getMessage(),
						       "exit and save",
						       JOptionPane.INFORMATION_MESSAGE);
		    }

		}
	    }
	    //store all ID matching all outFlows
	    for(int i = 0; i < tblOUT.getRowCount(); i++ ){
		if(tblOUT.getValueAt( i, 0) != null && !((String)tblOUT.getValueAt( i, 0)).equals("")){
		    try{
			Vector flow, coeff, isInteger;
			flow = flowValues.getFlowOut();
			coeff = flowValues.getCoeffOut();
			isInteger = flowValues.getIsIntegerOut();
			ID theId = null;
			if( (theId = parseOutId((String)tblOUT.getValueAt( i, 0))) != null ){
			    flow.addElement(theId);
			    float currentCoeff = this.parseValue((String)tblOUT.getValueAt( i, 1));
			    coeff.addElement(new Float(currentCoeff));
			    isInteger.addElement(new Boolean(false));
			}
		    }catch(NullPointerException e){
			JOptionPane.showMessageDialog( this, "Type a coefficient value for each selected out flow",
						       "Type coefficient",
						       JOptionPane.INFORMATION_MESSAGE);
		    }catch(Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog( this, e.getMessage(),
						       "exit and save",
						       JOptionPane.INFORMATION_MESSAGE);
		    }

		}
	    }
	    //save OUT value
	    flowValues.setEquationValue( txtRHSValue.getFloatValue() );
	    //set values to function FlowEquation

            String tempstring=null;
            switch (rshConstraintComboBox.getSelectedIndex()) {
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
	    c_function.setTimestepInfo(flowValues.getFlowIn(), flowValues.getCoeffIn(), flowValues.getIsIntegerIn(),
				       flowValues.getFlowOut(), flowValues.getCoeffOut(), flowValues.getIsIntegerOut(),
				       flowValues.getEquationValue(), tempstring);
	    c_function.setLabel(txtLabel.getText());
	    return 0;
	}
	else{
	    return 1;//possibility to send a messagetblIN.getRowCount()
	}
    }
    /**
     * Load the dialog from the function when it's started
     */
    public void load(){
	//FlowEquation.print(c_function.getInFlow());
	flowValues.setFlowIn(c_function.getInFlow());
	flowValues.setCoeffIn(c_function.getCoeffIn());
	flowValues.setIsIntegerIn(c_function.getIsIntegerIn());
	flowValues.setFlowOut(c_function.getOutFlow());
	flowValues.setCoeffOut(c_function.getCoeffOut());
	flowValues.setIsIntegerOut(c_function.getIsIntegerOut());
	flowValues.setEquationValue(c_function.getEquationValue());
    flowValues.setRhsConstraint(c_function.getRhsConstraint());
    }

    /*
     * Get the ID corresponding to the table string
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
     * Get the ID corresponding to the table string
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
     * Parse the coefficient value and accept decimal values
     */
    private float parseValue(String v){
	float floatvalue = (float)0.0;
	try{
	    floatvalue = Float.parseFloat(v);
	}catch(NumberFormatException e){
	    JOptionPane.showMessageDialog( this, "Type a float coefficient value",
						   "FlowEqutaionDialog.parseFloat",
						   JOptionPane.INFORMATION_MESSAGE);
        c_coefFieldEmpty =true;
	}catch(Exception e){
	    e.printStackTrace();
	    JOptionPane.showMessageDialog( this, "Exception",
						   "FlowEqutaionDialog.parseFloat",
						   JOptionPane.INFORMATION_MESSAGE);
        c_coefFieldEmpty =true;
	}
	return floatvalue;
    }
    //listener methods
    private void btnAddinActionPerformed(ActionEvent e){
	//get the selected in-flows
	Object selectedValue = lstInFlow.getSelectedValue();
	//set the selected flows to left hand side table to the currently
	//selected row
	int rowNumber = tblIN.getSelectedRow();
	tblIN.setValueAt( selectedValue, rowNumber, 0);
      // Added by Nawzad Mardan 20100308
    if(c_coefFieldEmpty == false)
        {
        saveEquationLables();
        updateLable();
        }
    }
    private void btnRemoveinActionPerformed(ActionEvent e){
	int rowNumber = tblIN.getSelectedRow();
	tblIN.setValueAt( "", rowNumber, 0);
	tblIN.setValueAt( "", rowNumber, 1);
    // Added by Nawzad Mardan 20100308
    saveEquationLables();
    updateLable();
    
    }
    private void btnAddoutActionPerformed(ActionEvent e){
	Object selectedValue = lstOutFlow.getSelectedValue();
	//set the selected flows to left hand side table to the currently
	//selected row
	int rowNumber = tblOUT.getSelectedRow();
	tblOUT.setValueAt( selectedValue, rowNumber, 0);
      // Added by Nawzad Mardan 20100308
    if(c_coefFieldEmpty == false)
        {
        saveEquationLables();
        updateLable();
        }
    }
    private void btnRemoveoutActionPerformed(ActionEvent e){
	int rowNumber = tblOUT.getSelectedRow();
	tblOUT.setValueAt( "", rowNumber, 0);
	tblOUT.setValueAt( "", rowNumber, 1);
     // Added by Nawzad Mardan 20100308
    saveEquationLables();
    updateLable();
   // save();
    }
    private void btnOkActionPerformed(ActionEvent e){
	save();//current timestep
	c_function.setTimestep( 1 );
	load();//values into flowValues
	loadTables();// to dialog window

	setVisible(false);
	dispose();
    }
    private void btnCancelActionPerformed(ActionEvent e){
    temFlowValues = flowValues;
	this.setVisible(false);
	this.dispose();
    }
    /*
    public static void main(String argv[]){
	new FlowEquationDialog();
	System.out.println("Call to main method in FlowEquationDialog");
    }
    */
    private void initComponents(){
	//JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();
	JPanel panel3 = new JPanel();
	JPanel panel12 = new JPanel();
	JPanel panel13 = new JPanel();
	JSeparator sep1 = new JSeparator();
	JSeparator sep2 = new JSeparator();
	JSeparator sep3 = new JSeparator();
	JSeparator sep4 = new JSeparator();
	JSeparator sep5 = new JSeparator();
    // Added by Nawzad Mardan 2030326
    JPanel   pnlInFlow = new javax.swing.JPanel();
    pnlInFlow.setLayout(new java.awt.GridBagLayout());
    JPanel   pnlOutFlow = new javax.swing.JPanel();
    pnlOutFlow.setLayout(new java.awt.GridBagLayout());
    java.awt.GridBagConstraints gridBagConstraints4;
	//	panel3.setLayout( new java.awt.GridBagLayout());

	//Main timestep panel
	//panel3 = new javax.swing.JPanel();
	panel3.setLayout(new java.awt.GridBagLayout());


	//create components
	lblDescription = new JLabel("Description: Add an equation to this node");
	lblLabel = new JLabel("Label:");
	lblTimestep = new JLabel("Timestep:");
	lblInFlow = new JLabel("Available in flow(s)");
	lblOutFlow = new JLabel("Available out flow(s)");
	lblIN = new JLabel("Incoming flows");
	lblOUT = new JLabel("Outgoing flows");
	lblRHSValue = new JLabel("RHS value    ");
	txtLabel =  new JTextField(20);
	txtRHSValue = new NumberField( 	flowValues.getEquationValue(), 10);
    // Added by Nawzad Mardan 20100306
    txtRHSValue.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		   saveEquationLables();
            updateLable();
		}
	    });
        String[] items = {"=", ">=", "<="};
      rshConstraintComboBox = new JComboBox(items);

  
      // Added by Nawzad Mardan 20100306
    rshConstraintComboBox.addItemListener(new MyItemListener());
    
	//arrays with all existing flows

	Flow[] inFlow = c_gui.getInFlows(c_nodeID);
	Flow[] outFlow = c_gui.getOutFlows(c_nodeID);

	String[] inFlowString =  convertToString(inFlow);
	String[] outFlowString = convertToString(outFlow);

	lstInFlow = new JList(inFlowString);
    scrollInFlowList = new JScrollPane(lstInFlow);
    scrollInFlowList.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.
                                                 HORIZONTAL_SCROLLBAR_NEVER);
    scrollInFlowList.setVerticalScrollBarPolicy(javax.swing.JScrollPane.
                                               VERTICAL_SCROLLBAR_ALWAYS);
    scrollInFlowList.setPreferredSize(new Dimension(80, 110));
	lstOutFlow = new JList(outFlowString);
    scrollOutFlowList = new JScrollPane(lstOutFlow);
    scrollOutFlowList.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.
                                                 HORIZONTAL_SCROLLBAR_NEVER);
    scrollOutFlowList.setVerticalScrollBarPolicy(javax.swing.JScrollPane.
                                               VERTICAL_SCROLLBAR_ALWAYS);
    scrollOutFlowList.setPreferredSize(new Dimension(80, 110));
	String[] columnnames = { "flow", "coeff" };
	tblIN = new JTable( new Object[TABLE_HEIGHT][2], columnnames );
	tblOUT = new JTable( new Object[TABLE_HEIGHT][2], columnnames);
    // Added by Nawzad Mardan 20100306
    tblOUT.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
        public void valueChanged(ListSelectionEvent event) {

           saveEquationLables();
            updateLable();
            }
    });
	tblIN.getSelectionModel().addListSelectionListener(new ListSelectionListener(){ 
        public void valueChanged(ListSelectionEvent event) {
            
          saveEquationLables();
            updateLable();
            }   
    });
    loadTables();
	JScrollPane scrollpane1 = new JScrollPane(tblIN);
    scrollpane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.
                                                 HORIZONTAL_SCROLLBAR_NEVER);
    scrollpane1.setVerticalScrollBarPolicy(javax.swing.JScrollPane.
                                               VERTICAL_SCROLLBAR_ALWAYS);

	tblIN.setPreferredScrollableViewportSize(new Dimension(110,80));
	JScrollPane scrollpane2 = new JScrollPane(tblOUT);
    scrollpane2.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.
                                                 HORIZONTAL_SCROLLBAR_NEVER);
    scrollpane2.setVerticalScrollBarPolicy(javax.swing.JScrollPane.
                                               VERTICAL_SCROLLBAR_ALWAYS);
	tblOUT.setPreferredScrollableViewportSize( new Dimension(110,80));
	btnAddin = new JButton("=>");
	btnRemovein = new JButton("<=");
	btnAddout = new JButton("=>");
	btnRemoveout = new JButton("<=");
	btnOk = new JButton("OK");
	btnCancel = new JButton("Cancel");
	lblTSL = new javax.swing.JLabel[c_timesteplevels];
	spinTSL = new SpinButton[c_timesteplevels];
        Timesteplevel level = c_eventhandler.getTopTimesteplevel();
	Timesteplevel thisLevel = c_function.getTimesteplevel();
	level = c_eventhandler.getTopTimesteplevel();

	//add label
    GridBagConstraints gridBagConstraints1;
	gridBagConstraints1 = new java.awt.GridBagConstraints();
	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridy = 0;
	gridBagConstraints1.gridwidth = 2;
    gridBagConstraints1.weightx = 1.0;
	//constraints.fill = GridBagConstraints.HORIZONTAL;
	gridBagConstraints1.insets = new java.awt.Insets (0, 0, 10, 5);
	gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
	panel3.add(lblTimestep, gridBagConstraints1);


	//panel3.add(lblTimestep, gridBagConstraints1);
	//panel3.add(lblTimestep);




	//add button listeners
	btnAddin.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
		    btnAddinActionPerformed(e);
		}
	    });
	btnRemovein.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
		    btnRemoveinActionPerformed(e);
		}
	    });
	btnAddout.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
		    btnAddoutActionPerformed(e);
		}
	    });
	btnRemoveout.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
		    btnRemoveoutActionPerformed(e);
		}
	    });
	btnOk.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
		    btnOkActionPerformed(e);
		}
	    });
	btnCancel.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
		    btnCancelActionPerformed(e);
		}
	    });


	//add components using nested panels and java.awt.GridBagLayout

    GridBagConstraints gridBagConstraints5 ;
        /*  panel1.setLayout(new GridBagLayout());
    
    
    gridBagConstraints5 = new GridBagConstraints ();
	//gridBagConstraints5.insets = new java.awt.Insets (10, 10, 10, 10);
	gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
	panel1.add (lblDescription, gridBagConstraints5);*/

    panel2.setLayout(new GridBagLayout());
    
    gridBagConstraints5 = new GridBagConstraints ();
	//gridBagConstraints5.insets = new java.awt.Insets (10, 10, 10, 10);
	gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
	panel2.add (lblLabel, gridBagConstraints5);
	gridBagConstraints5 = new GridBagConstraints ();
	gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints5.insets = new Insets (0, 10, 0, 0);
	gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
	gridBagConstraints5.weightx = 1.0;
	panel2.add (txtLabel, gridBagConstraints5);
    // Added By Nawzad Mardan 20100326
    gridBagConstraints4 = new java.awt.GridBagConstraints();
    gridBagConstraints4.gridx = 0;
    gridBagConstraints4.gridy = 0;
    pnlInFlow.add(lblInFlow, gridBagConstraints4);

    gridBagConstraints4 = new java.awt.GridBagConstraints();
    gridBagConstraints4.gridx = 2;
    gridBagConstraints4.gridy = 0;
    pnlInFlow.add(lblIN, gridBagConstraints4);

    gridBagConstraints4 = new java.awt.GridBagConstraints();
    gridBagConstraints4.gridx = 0;
    gridBagConstraints4.gridy = 1;
    gridBagConstraints4.gridheight = 6;
    gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
    //gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 5);
    gridBagConstraints4.weightx = 0;
    gridBagConstraints4.weighty = 0.5;  // h�r!!!
    pnlInFlow.add(scrollInFlowList, gridBagConstraints4);
	//panel6.add(lblInFlow, BorderLayout.NORTH);
	//panel6.add(scrollInFlowList, BorderLayout.CENTER);
    gridBagConstraints4 = new java.awt.GridBagConstraints();
    gridBagConstraints4.gridx = 1;
    gridBagConstraints4.gridy = 2;
    gridBagConstraints4.insets = new java.awt.Insets(30, 0, 0, 0);
    pnlInFlow.add(btnAddin, gridBagConstraints4);
	//panel7.add(btnAddin, BorderLayout.CENTER);
    gridBagConstraints4 = new java.awt.GridBagConstraints();
    gridBagConstraints4.gridx = 1;
    gridBagConstraints4.gridy = 3;
    gridBagConstraints4.insets = new java.awt.Insets(5, 0, 0, 0);
    pnlInFlow.add(btnRemovein, gridBagConstraints4);
	//panel7.add(btnRemovein, BorderLayout.SOUTH);
	//panel8.add(lblIN, BorderLayout.NORTH);
    gridBagConstraints4 = new java.awt.GridBagConstraints();
    gridBagConstraints4.gridx = 2;
    gridBagConstraints4.gridy = 1;
    gridBagConstraints4.gridheight = 6;
    gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
    //gridBagConstraints4.insets = new java.awt.Insets(0, 5, 0, 0);
    gridBagConstraints4.weightx = 0;
    gridBagConstraints4.weighty = 0.5;
    pnlInFlow.add(scrollpane1, gridBagConstraints4);
	//panel8.add(scrollpane1, BorderLayout.CENTER);
	//panel4.add(panel6);
	//panel4.add(panel7);
	//panel4.add(panel8);
	// Added By Nawzad Mardan 20100326
    gridBagConstraints4 = new java.awt.GridBagConstraints();
    gridBagConstraints4.gridx = 0;
    gridBagConstraints4.gridy = 0;
    pnlOutFlow.add(lblOutFlow, gridBagConstraints4);

    gridBagConstraints4 = new java.awt.GridBagConstraints();
    gridBagConstraints4.gridx = 2;
    gridBagConstraints4.gridy = 0;
    pnlOutFlow.add(lblOUT, gridBagConstraints4);

    gridBagConstraints4 = new java.awt.GridBagConstraints();
    gridBagConstraints4.gridx = 0;
    gridBagConstraints4.gridy = 1;
    gridBagConstraints4.gridheight = 6;
    gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 5);
    gridBagConstraints4.weightx = 0;
    gridBagConstraints4.weighty = 0.5;  // h�r!!!
    pnlOutFlow.add(scrollOutFlowList, gridBagConstraints4);
	//panel6.add(lblInFlow, BorderLayout.NORTH);
	//panel6.add(scrollInFlowList, BorderLayout.CENTER);
    gridBagConstraints4 = new java.awt.GridBagConstraints();
    gridBagConstraints4.gridx = 1;
    gridBagConstraints4.gridy = 2;
    gridBagConstraints4.insets = new java.awt.Insets(30, 0, 0, 0);
    pnlOutFlow.add(btnAddout, gridBagConstraints4);
	//panel7.add(btnAddin, BorderLayout.CENTER);
    gridBagConstraints4 = new java.awt.GridBagConstraints();
    gridBagConstraints4.gridx = 1;
    gridBagConstraints4.gridy = 3;
    gridBagConstraints4.insets = new java.awt.Insets(5, 0, 0, 0);
    pnlOutFlow.add(btnRemoveout, gridBagConstraints4);
	//panel7.add(btnRemovein, BorderLayout.SOUTH);
	//panel8.add(lblIN, BorderLayout.NORTH);
    gridBagConstraints4 = new java.awt.GridBagConstraints();
    gridBagConstraints4.gridx = 2;
    gridBagConstraints4.gridy = 1;
    gridBagConstraints4.gridheight = 6;
    gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
    //gridBagConstraints4.insets = new java.awt.Insets(0, 5, 0, 0);
    gridBagConstraints4.weightx = 0;
    gridBagConstraints4.weighty = 0.5;
    pnlOutFlow.add(scrollpane2, gridBagConstraints4);

    gridBagConstraints4 = new java.awt.GridBagConstraints();
    gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 5);
    panel12.add(btnOk, gridBagConstraints4);

	gridBagConstraints4 = new java.awt.GridBagConstraints();
    gridBagConstraints4.insets = new java.awt.Insets(0, 5, 0, 0);
    panel12.add(btnCancel, gridBagConstraints4);

    panel13.setLayout(new java.awt.GridBagLayout());
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridy = 0;
    gridBagConstraints1.gridwidth = 1;
    panel13.add(lblRHSValue, gridBagConstraints1);

    gridBagConstraints1.gridx = 1;
    panel13.add(rshConstraintComboBox, gridBagConstraints1);

    gridBagConstraints1.gridx = 2;
    gridBagConstraints1.weightx = 30;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 0);
    panel13.add(txtRHSValue, gridBagConstraints1);


	//panel13.add(lblRHSValue);
    //panel13.add(rshConstraintComboBox);
	//panel13.add(txtRHSValue);
	GridBagLayout gridbag = new GridBagLayout();
	GridBagConstraints gc = new GridBagConstraints();
	this.getContentPane().setLayout( gridbag);
	//gc.gridy = 0;
    gridBagConstraints5 = new java.awt.GridBagConstraints ();
	gridBagConstraints5.gridx = 0;
	gridBagConstraints5.gridy = 0;
	gridBagConstraints5.gridwidth = 2;
	gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints5.insets = new java.awt.Insets(10, 10, 10, 10);
	gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
	getContentPane().add(lblDescription, gridBagConstraints5);
	//this.getContentPane().add( panel1, gc);
	gc = new GridBagConstraints();
    gc.gridy = 1;
	gc.fill = java.awt.GridBagConstraints.HORIZONTAL;
	this.getContentPane().add( sep1, gc);
	//gc.gridy = 2;
	gridBagConstraints5 = new java.awt.GridBagConstraints ();
	gridBagConstraints5.insets = new java.awt.Insets (10, 10, 10, 10);
	gridBagConstraints5.gridx = 0;
	gridBagConstraints5.gridy = 2;
	gridBagConstraints5.gridwidth = 2;
	gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints5.insets = new java.awt.Insets (10, 10, 10, 10);
	gridBagConstraints5.weightx = 1.0;
	getContentPane ().add (panel2, gridBagConstraints5);
    //this.getContentPane().add( panel2, gc);
    gc = new GridBagConstraints();
	gc.gridy = 3;
	this.getContentPane().add( sep2, gc);
	//gc.gridy = 4;
	//this.getContentPane().add( panel3, gc);
	//Main timestep panel
	gridBagConstraints1 = new GridBagConstraints();
	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridy = 4;
	//	constraints.weighty = 1.0;
	gridBagConstraints1.fill = GridBagConstraints.VERTICAL;
	gridBagConstraints1.insets = new Insets(0, 25, 0, 20);
	gridBagConstraints1.anchor = GridBagConstraints.NORTHWEST;
	getContentPane().add(panel3, gridBagConstraints1);

    	for(int i=0; i<c_timesteplevels; i++){

	    //Add timestep labels
        gridBagConstraints1 = new java.awt.GridBagConstraints();
	    gridBagConstraints1.gridx = 0;
	    gridBagConstraints1.gridy = i + 1;
	    gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;

	    lblTSL[i] = new javax.swing.JLabel();
	    lblTSL[i].setText(level.getLabel());
	    panel3.add(lblTSL[i],gridBagConstraints1);

	    //Add timestep spinbuttons
	    gridBagConstraints1 = new java.awt.GridBagConstraints();
	    gridBagConstraints1.gridx = 1;
	    gridBagConstraints1.gridy = i + 1;
	    gridBagConstraints1.insets = new java.awt.Insets (0, 15, 0, 0);
	    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
	    gridBagConstraints1.weightx = 1.0;

	    spinTSL[i] = new SpinButton(1,level.getMaxTimesteps(),1,1);
	    panel3.add(spinTSL[i],gridBagConstraints1);

	    //final int button = i;
	    spinTSL[i].addListener(new SpinButtonListener() {
		public void valueDecreased()
		{
		    updateTimestep();
            // Added by Nawzad Mardan 20100326
          saveEquationLables();
            updateLable();

		}
		public void valueIncreased()
		{
		    updateTimestep();
            // Added by Nawzad Mardan 20100326
          saveEquationLables();
            updateLable();
		}
	    });
	    spinTSL[i].addFocusListener(new SpinButtonUpdateListener(spinTSL[i]) {
	    	public void valueUpdated() {
	    		updateTimestep();
	    	}
	    });
	    level = level.getNextLevel();
	}
	//getContentPane().add(panel3, gridBagConstraints1);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridy = 5;
    gridBagConstraints1.gridwidth = 2;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH; // h�r!!!
    gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
    gridBagConstraints1.weightx = 1.0;
    gridBagConstraints1.weighty = 0.5;
    //  pnlInFlow.setBorder(BorderFactory.createTitledBorder("Title is here"));
    getContentPane().add( pnlInFlow, gridBagConstraints1);
   
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridy = 6;
    gridBagConstraints1.gridwidth = 2;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH; // h�r!!!
    gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
    gridBagConstraints1.weightx = 1.0;
    gridBagConstraints1.weighty = 0.5;
    getContentPane().add( pnlOutFlow, gridBagConstraints1);
  
    gc = new GridBagConstraints();
	gc.gridy = 7;
	this.getContentPane().add( sep4, gc);
    
    gridBagConstraints1 = new GridBagConstraints();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridy = 8;
    gridBagConstraints1.gridwidth = 3;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
    getContentPane().add(panel13, gridBagConstraints1);
    gc = new GridBagConstraints();
	gc.gridy = 9;

	// Added by Nawzad Mardan 20100307
    lblEquationP1 = new JLabel("");
    lblEquationP1.setFont(new Font("SansSerif", Font.BOLD, 12));
    lblEquationP1.setBackground(Color.BLUE);
    lblEquationP1.setForeground(Color.BLUE);
    updateLable();
    pnlEquation = new JPanel();
    pnlEquation.add(lblEquationP1);
 
    //gc.insets = new java.awt.Insets(5, 0, 5, 50);
    this.getContentPane().add( pnlEquation, gc);

   /* gridBagConstraints1 = new java.awt.GridBagConstraints ();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridy = 10;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 0);
    this.getContentPane().add( sep5, gridBagConstraints1);
*/
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    //gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridy = 11;
    gridBagConstraints1.gridwidth = 2;
    gridBagConstraints1.insets = new java.awt.Insets(2, 10, 5, 10);
    getContentPane().add(panel12, gridBagConstraints1);
    //gc = new GridBagConstraints();
	
	//show this dialog window and commit the layout
	//this.show(); done by parent
	this.pack();
    //this.setResizable(false);

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
    /*
     * create a object matrix and inserts existing flows and coeff to the matrix
     */
    private void loadTables(){
	txtLabel.setText(c_function.getLabel());

	//load tables
	Vector flow = flowValues.getFlowIn();
	Vector coeff = flowValues.getCoeffIn();
	for( int i = 0; i < flow.size(); i++){
	    tblIN.setValueAt( flow.elementAt(i).toString() , i, 0);
	    tblIN.setValueAt( coeff.elementAt(i).toString() , i, 1);
	}
	for( int i = flow.size(); i < TABLE_HEIGHT; i++){
	    tblIN.setValueAt( "", i, 0);
	    tblIN.setValueAt( "", i, 1);
	}
	flow = flowValues.getFlowOut();
	coeff = flowValues.getCoeffOut();
	for( int i = 0; i < flow.size(); i++){
	    tblOUT.setValueAt( flow.elementAt(i).toString() , i, 0);
	    tblOUT.setValueAt( coeff.elementAt(i).toString() , i, 1);
	}
	for( int i = flow.size(); i < TABLE_HEIGHT; i++){
	    tblOUT.setValueAt( "", i, 0);
	    tblOUT.setValueAt( "", i, 1);
	}
	//load the RHS value
	txtRHSValue.setText("" + flowValues.getEquationValue());
        //load the constraint value
        int tempindex=0;
        String tempstring=null;
        tempstring = flowValues.getRhsConstraint();
        if (tempstring.equals("E"))
          tempindex = 0;
        if (tempstring.equals("G"))
          tempindex = 1;
        if (tempstring.equals("L"))
          tempindex = 2;
        rshConstraintComboBox.setSelectedIndex(tempindex);
       // updateLable();
    }
    /*
     * Update timestep
     */
        private void updateTimestep()
    {

	//Save value for previous timestep
	save();

	//Calculate new timestep
	c_timestep=1; //The first timestep is 1 (not 0)
	int factor=1;
	for(int i=c_timesteplevels-1; i>0; i--) {
	    c_timestep = c_timestep + (spinTSL[i].getValue() - 1) * factor;
	    factor = factor * c_tsl[i].getMaxTimesteps();
	}

	//Get value for new timestep
	c_function.setTimestep(c_timestep);
	load();//load function values of the new timestep into the FlowEquationValue
	//move values from flowValues to dialog window
	loadTables();

    }
  // Added by Nawzad Mardan 20100306
  // To display the equations
  private void updateLable()
    {
    if(flowValues!=null)
        {
        //FlowEquationValue  temFlowValues ;//= new FlowEquationValue() ;
        if( temFlowValues == null)
            {
            temFlowValues = new FlowEquationValue();
            Vector incoeff ,inflow, outcoeff, outflow;
            incoeff = flowValues.getCoeffIn();
            for(int i = 0; i< incoeff.size();i++)
                {
                temFlowValues.c_coeffIn.add(incoeff.elementAt(i));
                }
            outcoeff = flowValues.getCoeffOut();
            for(int i = 0; i< outcoeff.size();i++)
                {
                temFlowValues.c_coeffOut.add(outcoeff.elementAt(i));
                }
            temFlowValues.c_equationvalue = flowValues.getEquationValue();
            inflow = flowValues.getFlowIn();
            for(int i = 0; i< inflow.size();i++)
                {
                temFlowValues.c_flowIn.add(inflow.elementAt(i));
                }
            outflow = flowValues.getFlowOut();
            for(int i = 0; i< outflow.size();i++)
                {
                temFlowValues.c_flowOut.add(outflow.elementAt(i));
                }
            
            }
        Vector incoeff ,inflow, outcoeff, outflow;
        incoeff= temFlowValues.getCoeffIn();
        inflow = temFlowValues.getFlowIn();
        outcoeff = temFlowValues.getCoeffOut();
        outflow = temFlowValues.getFlowOut();

        String equation = " ";
        Float o;
        float incoef, outcoef;
        for(int i = 0; i< incoeff.size();i++)
            {
            //Objeck o equation + = (String)incoeff.elementAt(i);
             o  = (Float)incoeff.elementAt(i);
             incoef = o.floatValue();
             if(incoef == -1)
                 equation  = equation + "-";
             else if(incoef == 1)
                 {
                 if(i == 0)
                    equation = equation +"";
                 else
                     equation = equation +" +";
                 }
             else if((incoef > 1) && (i != 0))
                 equation = equation + " + "+ incoef + " * ";
             else
                 equation = equation + " "+ incoef + " * ";
             if(inflow!=null)
               equation = equation +  inflow.elementAt(i) + " ";

            }
        for(int i = 0; i< outcoeff.size();i++)
            {
            o  = (Float)outcoeff.elementAt(i);
            outcoef = o.floatValue();
            outcoef = outcoef * -1;
             if(outcoef == -1)
                 equation  = equation + "-";
             else if((outcoef == 1))
                {
                if(!equation.equals(" "))
                         equation = equation +" +";
                }
            else if(outcoef > 1)
                {//&& (i == 0))
                 if(equation.equals(" "))
                     equation = equation + outcoef + " * ";
                 else
                    equation = equation +" + "+outcoef + " * ";
                }
             else
                 equation = equation + " "+ outcoef + " * ";
             if(outflow!=null)
               equation = equation +  outflow.elementAt(i) + " ";

            }
        if(!equation.equals(" "))
            equation = equation+ " "+ getRHS() + " "+ temFlowValues.getEquationValue();
        lblEquationP1.setText(equation);
        }

    }

   // Added by Nawzad Mardan 20100306
  // To display the equations
  private String getRHS()
  {
   String rhs =" = ";
    if(flowValues!=null)
        {
        rhs = temFlowValues.getRhsConstraint();
        if(rhs.equals("E"))
            rhs = " = ";
        else if(rhs.equals("G"))
            rhs = " >= ";
        else if(rhs.equals("L"))
            rhs = " <= ";
        }
   return rhs;

  }
 // Added by Nawzad Mardan 20100306
  // To display the equations
 public class MyItemListener implements  ItemListener
    {
    public void itemStateChanged(ItemEvent e)
        {
        String rhs = "";
        if(temFlowValues != null)
            {
            switch (rshConstraintComboBox.getSelectedIndex())
                {
                case 0:
                    rhs = "E";
                    break;
                case 1:
                    rhs = "G";
                    break;
                case 2:
                    rhs = "L";
                    break;
                }
            temFlowValues.setRhsConstraint(rhs);
            updateLable();

            }
        }
    }
 
  // Added by Nawzad Mardan 20100326
  // To display the equations
   //metods
    //create the FlowEquationValue consisting of data from the in and out tables.
    private int saveEquationLables(){
	if(temFlowValues != null){
	    // clear the flowValues
        //temFlowValues = new FlowEquationValue();
	    temFlowValues.clear();

	    //store all ID matching all inFlows
	    for(int i = 0; i < tblIN.getRowCount(); i++ ){
		if(tblIN.getValueAt( i, 0) != null && !((String)tblIN.getValueAt( i, 0)).equals("")){
		    try{
			Vector flow, coeff, isInteger;
			flow = temFlowValues.getFlowIn();
			coeff = temFlowValues.getCoeffIn();
			isInteger = temFlowValues.getIsIntegerIn();
			ID theId = null;

			if( (theId = (ID)parseInId((String)tblIN.getValueAt( i, 0))) != null ){
			    flow.addElement(theId);
			    float currentCoeff = this.parseValue((String)tblIN.getValueAt( i, 1));
			    coeff.addElement(new Float(currentCoeff));
			    isInteger.addElement(new Boolean(false));
			}
		    }catch(NullPointerException e){
			JOptionPane.showMessageDialog( this, "Type a coefficient value for each selected in flow",
						       "Type coefficient",
						       JOptionPane.INFORMATION_MESSAGE);
		    }catch(Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog( this, e.getMessage(),
						       "exit and save",
						       JOptionPane.INFORMATION_MESSAGE);
		    }

		}
	    }
	    //store all ID matching all outFlows
	    for(int i = 0; i < tblOUT.getRowCount(); i++ ){
		if(tblOUT.getValueAt( i, 0) != null && !((String)tblOUT.getValueAt( i, 0)).equals("")){
		    try{
			Vector flow, coeff, isInteger;
			flow = temFlowValues.getFlowOut();
			coeff = temFlowValues.getCoeffOut();
			isInteger = temFlowValues.getIsIntegerOut();
			ID theId = null;
			if( (theId = parseOutId((String)tblOUT.getValueAt( i, 0))) != null ){
			    flow.addElement(theId);
			    float currentCoeff = this.parseValue((String)tblOUT.getValueAt( i, 1));
			    coeff.addElement(new Float(currentCoeff));
			    isInteger.addElement(new Boolean(false));
			}
		    }catch(NullPointerException e){
			JOptionPane.showMessageDialog( this, "Type a coefficient value for each selected out flow",
						       "Type coefficient",
						       JOptionPane.INFORMATION_MESSAGE);
		    }catch(Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog( this, e.getMessage(),
						       "exit and save",
						       JOptionPane.INFORMATION_MESSAGE);
		    }

		}
	    }
	    //save OUT value
	    temFlowValues.setEquationValue( txtRHSValue.getFloatValue() );
	    //set values to function FlowEquation

            String tempstring=null;
            switch (rshConstraintComboBox.getSelectedIndex()) {
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

	    return 0;
	}
	else{
	    return 1;//possibility to send a messagetblIN.getRowCount()
	}
    }
}
