/*
 * Copyright 2004:
 * Marcus Bergendorff <amaebe-1@student.luth.se>
 * Jan Sköllermark <jansok-1@student.luth.se>
 * Nils-Oskar Spett <nilspe-1@student.luth.se>
 * Richard Harju <richar-1@student.luth.se>
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

package mind.gui.dialog;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.table.*;

import mind.EventHandlerClient;
import mind.gui.*;
import mind.model.*;
import mind.model.function.*;

/**
 * Creates GUI for Function Editor
 * @author Marcus Bergendorff
 * 
 * @author  Per Fredriksson
 * @author 	Tor Knutsson
 * @version 2007-12-10
 */

public class FunctionEditorDialog extends mind.gui.dialog.FunctionDialog{
    private JTable table;
    private JScrollPane scrllPn;
    private JScrollPane flowScrllPn;
    private JButton okBtn;
    private JTextField exprTxt;
    private SpinButton [] timeStepSpinn;
    private JLabel timeStepLbl;
    private JComboBox funCmbbx;
    private JButton addColBtn;
    private JButton addRowBtn;
    private JButton delColBtn;
    private JButton delRowBtn;
    private JButton helpBtn;
    private JButton discardBtn;
    private JButton acceptBtn;

    private JPanel buttonPnl;
    private JPanel labelBar;
    private JPanel exprTxtPnl;
    private JPanel centerPnl;
    private JPanel northPnl;
    private JPanel timeStepPnl;
    private JPanel functionPnl;
    private JPanel topPnl;
    private JPanel leftPnl;
    private JPanel varPnl;
    private JPanel varFunPnl;
    private JLabel label;
    private JTextField nodeText;
    private JTextField editorText;

	private Vector copy;
	private Vector headerCopy;
	private DefaultCellEditor editor;
    private Flow[] inFlow;
	private	Flow[] outFlow;

    private ID	c_nodeID;
    private GUI	c_gui;
    private FunctionEditor c_function;
    private EventHandlerClient c_eventhandler;
    private boolean editedLast = false;
    private String discardText = ""; //used with the discard button

    //timesteps
    private int c_NrOfTSLs;//number of levels
    private Timesteplevel [] c_TSLs;
    private Timesteplevel level;

    private final String imagesDir = "images/";

    /**
     * Constructor
     * Creates a new dialog for Function Editor
     */

    public FunctionEditorDialog (JDialog parent, boolean modal, ID nodeID,
				NodeFunction function, GUI gui) {
		super (parent, modal);

		c_nodeID=nodeID;
		c_gui=gui;
		c_function = (FunctionEditor) function;


		// -Support for Timesteps-
		//calculate number of timestepslevels in the function
		c_NrOfTSLs = 1;
		c_eventhandler = gui.getEventHandlerClient(); //get eventhandler to get timesteps
		Timesteplevel thisLevel = c_eventhandler.getTimesteplevel(nodeID); // get current level
		level = c_gui.getTopTimesteplevel();
		c_NrOfTSLs = thisLevel.toInt() + 1;

		// Get all timesteplevels
		c_TSLs = new Timesteplevel[c_NrOfTSLs];
		for( int j = 0; j < c_NrOfTSLs; j++) {
		    c_TSLs[j] = level;
		    level = level.getNextLevel();
		}
		c_function.setTimestep(1);
		table = new JTable(c_function.getModel());
		//------------------------

		// table settings
		table.getTableHeader().setReorderingAllowed(false);
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(false);
		table.setPreferredScrollableViewportSize(new Dimension(475, 145));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		ToolTipManager.sharedInstance().unregisterComponent(table);
		ToolTipManager.sharedInstance().unregisterComponent(table.getTableHeader());

		//"excell behavior"
		editorText = new JTextField();

		editorText.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent e){
				if(editorText.hasFocus()){
					exprTxt.setText(editorText.getText());
					editedLast = true;
				}
			}
		});

		editorText.addFocusListener(new FocusAdapter(){
			public void focusGained(FocusEvent e){
				if(table.isEditing()){
				    editorText.setBackground(Color.WHITE);
					exprTxt.setText(editorText.getText());
					discardText = editorText.getText();
					editedLast = true;
				}
			}
		});

		editor = new DefaultCellEditor (editorText);
		table.setDefaultEditor(Object.class, editor);
		table.setCellEditor(editor);
		table.setSurrendersFocusOnKeystroke(true);
		editor.setClickCountToStart(1);

		//copy cell contents to expression field when user selects with arrow keys
		table.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent e){
				if(!table.isEditing()){
					int row = table.getSelectedRow();
					int column = table.getSelectedColumn();
					if(row > -1 && column > -1){
						String str = (String) table.getValueAt(row,column);
						exprTxt.setText(str);
					}
				}
			}
		});

		//components
		labelBar = new JPanel();
		labelBar.setLayout(new BoxLayout(labelBar,BoxLayout.Y_AXIS));
		buttonPnl = new JPanel();
		buttonPnl.setLayout(new BoxLayout(buttonPnl,BoxLayout.X_AXIS));
		leftPnl = new JPanel(new BorderLayout());
		exprTxtPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		functionPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		timeStepPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		northPnl = new JPanel();
		northPnl.setLayout(new BoxLayout(northPnl,BoxLayout.Y_AXIS));
		topPnl = new JPanel();
		topPnl.setLayout(new BoxLayout(topPnl,BoxLayout.X_AXIS));
		label = new JLabel("Label:");
		nodeText = new JTextField(25);

		// center
		createLabelBar(table.getRowCount());
		scrllPn = new JScrollPane(table,
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrllPn.setRowHeaderView(labelBar);


		// expression text panel (to simplifie use of long expressions)
		exprTxt = new JTextField(30);
		exprTxt.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent e){
				if(exprTxt.hasFocus()){
					editorText.setText(exprTxt.getText());
					editedLast = false;
				}
			}
		});

		exprTxt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int row = 0, column = 0;
				column = table.getEditingColumn();
				row = table.getEditingRow();
				if( row != -1 && column != -1 ){
					row++;
					if (row > table.getRowCount() - 1){
						row = 0;
						column++;
						if (column > table.getColumnCount() - 1) column = 0;
					}
					table.editCellAt(row, column);
					exprTxt.setText(editorText.getText());
					}
				}
		    });

		discardBtn = new JButton(new ImageIcon(getClass().getResource(imagesDir + "cancel.gif")));
		discardBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				exprTxt.setText(discardText);
				editorText.setText(discardText);
				}
		    });
		discardBtn.setMargin(new Insets(1,3,1,3));
		discardBtn.setToolTipText("Discard Text");

		acceptBtn = new JButton(new ImageIcon(getClass().getResource(imagesDir + "accept.gif")));
		acceptBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				if (table.isEditing()){
					discardText = editorText.getText();
					editor.stopCellEditing();
				}
			}
		});
		acceptBtn.setMargin(new Insets(1,3,1,3));
		acceptBtn.setToolTipText("Accept Text and Update Selected Cell");

		//exprTxtPnl
		exprTxtPnl.add(discardBtn);
		exprTxtPnl.add(acceptBtn);
		exprTxtPnl.add(exprTxt);

		//flowboxes
		inFlow = c_gui.getInFlows(c_nodeID);
		outFlow = c_gui.getOutFlows(c_nodeID);
		Box flowbx = new Box(BoxLayout.X_AXIS);

		// Decorations for the flowbutton panel
		JPanel head = new JPanel();
		head.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		JLabel inLbl = new JLabel("In");
		JLabel outLbl = new JLabel("Out");

		if ( inFlow != null ){
			FlowBox inF = new FlowBox (inFlow, this, editorText, c_gui, exprTxt);
			flowbx.add(inF);
			inLbl.setFont(table.getFont());
			inLbl.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			head.add(inLbl);
		}

		if ( outFlow != null ){
			FlowBox outF = new FlowBox (outFlow, this, editorText, c_gui, exprTxt);
			flowbx.add(outF);
			outLbl.setFont(table.getFont());
			outLbl.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			head.add(outLbl);
		}

		if ( inFlow != null || outFlow != null ){
			flowScrllPn = new JScrollPane(flowbx,
										  JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
										  JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			flowScrllPn.setPreferredSize(new Dimension(flowbx.getPreferredSize().width +
										flowScrllPn.getVerticalScrollBar().getPreferredSize().width,
										table.getTableHeader().getPreferredSize().height +
										scrllPn.getPreferredSize().height));
			flowScrllPn.setColumnHeaderView(head);
			leftPnl.add("Center",flowScrllPn);
			leftPnl.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 3));
		}

		if ( inFlow != null )
		inLbl.setPreferredSize(new Dimension(flowScrllPn.getPreferredSize().width / 2,
								table.getTableHeader().getPreferredSize().height));
		if ( outFlow != null )
		outLbl.setPreferredSize(new Dimension(flowScrllPn.getPreferredSize().width / 2,
								table.getTableHeader().getPreferredSize().height));

		// south button panel
		okBtn = new JButton("OK");
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
			    try {
				editor.stopCellEditing();
   				c_function.setLabel(nodeText.getText());
			    	c_function.setInFlows(inFlow);
			    	c_function.setOutFlows(outFlow);
			    	c_function.parse(c_nodeID);
			    	setVisible(false);
			    	dispose();
				}
				catch (FunctionEditorException errmsg) {
				    //markera cell errmsg.getCol errmsg.getRow
				    if(table.isEditing())
						table.getCellEditor().stopCellEditing();
				    exprTxt.setText("");
				    c_function.setTimestep(errmsg.getTs());
				    table.setModel(c_function.getModel());
				    createLabelBar(table.getModel().getRowCount());
				    scrllPn.setRowHeaderView(labelBar);
				    editorText.setBackground(Color.RED);
				    table.editCellAt(errmsg.getRow(), errmsg.getCol());
				    updateSpinners(errmsg.getTs());
					JOptionPane.showMessageDialog(null, errmsg.getMessage(), "Syntax error", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		// Add / Delete Row / Column Buttons
		addRowBtn = new JButton(new ImageIcon(getClass().getResource(imagesDir + "addRow.gif")));
		addRowBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				((DefaultTableModel) table.getModel()).setRowCount(table.getModel().getRowCount() + 1);
				createLabelBar(table.getRowCount());
			}
		});
		addRowBtn.setMargin(new Insets(1,1,1,1));
		addRowBtn.setToolTipText("Add Row");

		addColBtn = new JButton(new ImageIcon(getClass().getResource(imagesDir + "addCol.gif")));
		addColBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				((DefaultTableModel) table.getModel()).setColumnCount(table.getModel().getColumnCount() + 1);
			}
		});
		addColBtn.setMargin(new Insets(1,1,1,1));
		addColBtn.setToolTipText("Add Column");

		// "Delete" Buttons
		delRowBtn = new JButton(new ImageIcon(getClass().getResource(imagesDir + "delRow.gif")));
		delRowBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				 if (deleteLastRowOK()) {
					((DefaultTableModel) table.getModel()).setRowCount(table.getModel().getRowCount() - 1);
					createLabelBar(table.getRowCount());
				}
			}
		});
		delRowBtn.setMargin(new Insets(1,1,1,1));
		delRowBtn.setToolTipText("Delete Row");

		delColBtn = new JButton(new ImageIcon(getClass().getResource(imagesDir + "delCol.gif")));
		delColBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				 if (deleteLastColOK()) {
				 	((DefaultTableModel) table.getModel()).setColumnCount(table.getModel().getColumnCount() - 1);
				 }
			}
		});
		delColBtn.setMargin(new Insets(1,1,1,1));
		delColBtn.setToolTipText("Delete Column");

		buttonPnl.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		buttonPnl.add(Box.createHorizontalStrut(leftPnl.getPreferredSize().width));
		buttonPnl.add(addRowBtn);
		buttonPnl.add(Box.createHorizontalStrut(5));
		buttonPnl.add(addColBtn);
		buttonPnl.add(Box.createHorizontalStrut(10));
		buttonPnl.add(delRowBtn);
		buttonPnl.add(Box.createHorizontalStrut(5));
		buttonPnl.add(delColBtn);
		buttonPnl.add(Box.createHorizontalGlue());
		buttonPnl.add(okBtn);

		// label and text
		topPnl.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		topPnl.add(label);
		topPnl.add(Box.createHorizontalStrut(5));
		topPnl.add(nodeText);
		topPnl.add(Box.createHorizontalStrut(5));

		helpBtn = new JButton(new ImageIcon(getClass().getResource(imagesDir + "help.gif")));
		helpBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				helpAction();
			}
		});
		helpBtn.setMargin(new Insets(1,3,1,3));
		helpBtn.setToolTipText("Help Window");

		topPnl.add(Box.createHorizontalGlue());
		topPnl.add(Box.createHorizontalStrut(30));
		topPnl.add(helpBtn);

		// timestep panel
		timeStepSpinn = new SpinButton[c_NrOfTSLs];

		for (int ts = 0; ts < c_NrOfTSLs; ts++) {
			timeStepSpinn[ts] = new SpinButton(1,c_TSLs[ts].getMaxTimesteps(),1,1);
			String tSLabel = c_TSLs[ts].getLabel();
			timeStepLbl = new JLabel(tSLabel);
			timeStepPnl.add(timeStepLbl);
			timeStepPnl.add(timeStepSpinn[ts]);
			timeStepSpinn[ts].addListener(new SpinButtonListener() {
				public void valueDecreased() {
				    updateTimestep();
				}
				public void valueIncreased() {
				     updateTimestep();
				}
			});
			timeStepSpinn[ts].addFocusListener(new SpinButtonUpdateListener(timeStepSpinn[ts]) {
		    	public void valueUpdated() {
		    		updateTimestep();	
		    	}
		    });
		}
		timeStepPnl.setBorder(BorderFactory.createTitledBorder("Timesteps"));



		//variables panel
		varPnl = new JPanel(new GridLayout(1,2));
		varPnl.add(new IntVarCombo(c_function.getIntVars(),this,editorText,exprTxt));
		varPnl.add(new FloatVarCombo(c_function.getFloatVars(),this,editorText,exprTxt));

		//function panel - with no function....
		funCmbbx = new JComboBox();
		funCmbbx.setEnabled(false);

		functionPnl.add(funCmbbx);
		functionPnl.setBorder(BorderFactory.createTitledBorder("Function"));

		//varFunPnl containing variables comboboxes and function components
		varFunPnl = new JPanel(new GridLayout(1,2));
		varFunPnl.add(functionPnl);
		varFunPnl.add(varPnl);

		//north part of the dialog
		northPnl.add(topPnl);
		northPnl.add(timeStepPnl);
		northPnl.add(varFunPnl);
		northPnl.add(exprTxtPnl);

		getContentPane().add("West",leftPnl);
		getContentPane().add("Center",scrllPn);
		getContentPane().add("South",buttonPnl);
		getContentPane().add("North",northPnl);

		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
			    editor.stopCellEditing();
			    setVisible(false);
			    dispose();
			}
		});

		//table.editCellAt(0, 0);
		nodeText.setText(c_function.getLabel());
		setTitle("Function Editor");
		setLocationRelativeTo(null);
		pack();
    }

    /*
     *
     * Creates the bar of row labels for the table
     *
     */

    private void createLabelBar(int nr){
    	JButton butt;
    	int height = table.getRowHeight();
    	labelBar.removeAll();
    	for (int i = 1; i < nr + 1; i++) {
    		String str = new Integer(i).toString();
    		butt = new JButton(str);
    		butt.setMargin(new Insets(0,1,0,1));
    		butt.setFont(table.getFont());
    		butt.setPreferredSize(new Dimension(30, height));
    		butt.setSize(butt.getPreferredSize());
    		butt.setMaximumSize(butt.getPreferredSize());
			butt.setFocusPainted(false);
			labelBar.add(butt);
    	}
    }

    /*
     *
     * Checks if it is ok to delete the last column or if it contains data. If
     * so display a dialog.
     *
     */

    private boolean deleteLastColOK () {
    	if( table.getModel().getColumnCount() < 2)
    		return false;
    	int lastCol = table.getModel().getColumnCount() - 1;
    	// check if last column is empty
    	for ( int row = 0; row < table.getModel().getRowCount() ; row++ ) {
    		String str = (String) table.getModel().getValueAt(row, lastCol);
    		if (!(str == null || str.equals(""))){
    			int sel = JOptionPane.showConfirmDialog(null, "You are trying to " +
    										"delete a column with contents. " +
    										"Data will be lost.\nDo you want to " +
    										"continue?",
    										"Warning", JOptionPane.YES_NO_OPTION,
    										JOptionPane.WARNING_MESSAGE);
    			if (sel == JOptionPane.YES_OPTION) return true;
    			return false;
    		}
    	}
    	return true;
    }

    /*
     *
     * Checks if it is ok to delete the last row or if it contains data
     * If so display a dialog.
     *
     */

    private boolean deleteLastRowOK () {
    	if( table.getModel().getRowCount() < 3)
    		return false;
    	int lastRow = table.getModel().getRowCount() - 1;
    	// check if last row is empty
    	for ( int col = 0; col < table.getModel().getColumnCount(); col++ ) {
    		String str = (String) table.getModel().getValueAt(lastRow, col);
    		if (!(str == null || str.equals(""))){
    			int sel = JOptionPane.showConfirmDialog(null, "You are trying to " +
    										"delete a row with contents. " +
    										"Data will be lost.\nDo you want to " +
    										"continue?",
    										"Warning", JOptionPane.YES_NO_OPTION,
    										JOptionPane.WARNING_MESSAGE);
    			if (sel == JOptionPane.YES_OPTION) return true;
    			return false;
    		}
    	}
    	return true;
    }

    /*
     * Display the Help Window
     */

    private void helpAction (){
    	new FunctionEditorHelpDialog(this);
    }

    // Returns true if editorText was used last

    protected boolean isEditedLast(){
    	return editedLast;
    }

    /*
     * Update timestep when user flips spinnbuttons
     *
     */
	private void updateTimestep (){
		//compute which timstep to get
		int currentTimeStep=1; //The first timestep is 1 (not 0)
		int factor=1;
		for(int i = c_NrOfTSLs - 1; i > 0; i--) {
	    	currentTimeStep = currentTimeStep + (timeStepSpinn[i].getValue() - 1) * factor;
	    	factor = factor * c_TSLs[i].getMaxTimesteps();
		}
		if(table.isEditing())
			table.getCellEditor().stopCellEditing();
		exprTxt.setText("");
		c_function.setTimestep(currentTimeStep);
		table.setModel(c_function.getModel());
		createLabelBar(table.getModel().getRowCount());
		scrllPn.setRowHeaderView(labelBar);
		//table.editCellAt(0, 0);
    }

    public int getNextIntVar(){
    	return c_function.getNextIntVar();
    }

    public int getNextFloatVar(){
    	return c_function.getNextFloatVar();
    }

    /*
     * Update spinnbuttons to a certain timestep
     *
     */
    private void updateSpinners (int timestep){
    	int maxTimeSteps = 1;

		for (int i = 0; i < c_TSLs.length; i++){
			maxTimeSteps *= c_TSLs[i].getMaxTimesteps();
		}

		for(int j=0; j < c_NrOfTSLs; j++){
			int factor = 1;
			for ( int p = j; p > 0; p--)
				factor *= c_TSLs[p].getMaxTimesteps();
			int out = 1;
			int nrChildren = maxTimeSteps / factor;
			while(timestep > nrChildren){
				timestep -= nrChildren;
				out++;
			}
			timeStepSpinn[j].setValue(out);
			//System.out.println("Spinn"+j+" = " + out);
		}
    }
}

