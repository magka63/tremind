/*
 * Copyright 2003:
 * Urban Liljedahl <urban.liljedahl@home.se>
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
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import mind.gui.*;
import mind.model.*;
import mind.EventHandlerClient;

/**
 * Object function dialog
 * 
 * @author Urban Liljedahl
 * @version 2003-12-05
 */

public class ObjectFunctionDialog extends javax.swing.JDialog {
	//debug flag
	private static final boolean DEBUG = false;

	//
	private EventHandlerClient c_eventhandler;

	private GUI c_gui;

	private ObjectFunction c_function;

	//short description of the dialog window
	private javax.swing.JLabel lblDescription;

	private javax.swing.JSeparator sep1;

	private javax.swing.JSeparator sep2;

	//buttons new, edit, remove, ok and cancel
	private javax.swing.JButton btnNew;

	private javax.swing.JButton btnEdit;

	private javax.swing.JButton btnRemove;

	private JButton btnOk;

	private JButton btnCancel;

	//List for all created object functions
	private javax.swing.JList functionList;

	private javax.swing.JScrollPane scrollPane;

	//the objectives

	private javax.swing.JLabel lblObjectives;
	
	private javax.swing.JLabel lblTimeSteps;

	private JTable tblObjectives;

	//a new label, created from the dialog window
	private String c_newLabel = "";

	//my table model
	private MyTableModel c_myTableModel;

	private JSeparator sep3;

	private JPanel timestepPnl;

	private JSpinner timestepSpinner;

	private Vector timesteps;
	
	private int currentTimestep = 0; // 0 == TOP, 1 == 1 osv.

	/** Creates new forObjectFunctionDialogm ObjectFunctionDialog */
	public ObjectFunctionDialog(java.awt.Frame parent, boolean modal, GUI gui,
			ObjectFunction function) {
		super(parent, modal);
		setTitle("Object function(s)");
		c_gui = gui;
		c_eventhandler = gui.getEventHandlerClient();
		c_function = function;
		
		//support for different bounds on different timesteps
		timesteps = new Vector();
		timesteps.add(" TOP");
		//calculate number of timesteps
		Timesteplevel tsl = gui.getTopTimesteplevel();
		int maxTimeSteps = 1;
		String lblString ="";
		if(tsl.getNextLevel() == null)
			maxTimeSteps = 0;
		while ((tsl = tsl.getNextLevel()) != null) {
			maxTimeSteps *= tsl.getTimesteps();
			lblString = tsl.getLabel();
		}
		for (int i = 1; i < maxTimeSteps + 1; i++) {
			timesteps.add(" "+ lblString + i);
		}
		
		initComponents();
		//Init. the list column's cell editors/renderers.
		setUpListColumn(tblObjectives, tblObjectives.getColumnModel()
				.getColumn(3));
		//set column width
		TableColumn column = null;
		for (int i = 0; i < 5; i++) {
			column = tblObjectives.getColumnModel().getColumn(i);
			if (i == 3)
				column.setPreferredWidth(70); //label and bound string column
			else
				column.setPreferredWidth(45); //K1, K2, L1, L2 with num. values
		}
		initValues();
		pack();
	}

	//init if c_function contain any values
	private void initValues() {
		int n = 0;
		//set values in the table
		//if(c_function != null)
		n = c_function.numberOfFunctions();

		Object[][] data = new Object[n][7];
		//initialize the data with objects
		int i;
		for (i = 0; i < n; i++) {
			data[i][0] = c_function.getLabel(i);
			data[i][1] = new Float(c_function.getK1(i));
			data[i][2] = new Float(c_function.getK2(i));
			data[i][3] = new String(c_function.getBound(i, currentTimestep));
			data[i][4] = new Float(c_function.getLimit1(i, currentTimestep));
			data[i][5] = new Float(c_function.getLimit2(i, currentTimestep));
		}
		c_myTableModel.setData(data);
		c_myTableModel.fireTableDataChanged();
	}

	private void initComponents() {
		getContentPane().setLayout(new java.awt.GridBagLayout());
		java.awt.GridBagConstraints gridBagConstraints1;
		setName("Object function");
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				closeDialog(evt);
			}
		});

		//Description text
		lblDescription = new javax.swing.JLabel();
		lblDescription.setText("Object Function");
		//lblDescription.setText("Description: Object Function is a function
		// from which one or more object functions emerges.");

		gridBagConstraints1 = new java.awt.GridBagConstraints();
		gridBagConstraints1.gridwidth = 2;
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
		gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
		getContentPane().add(lblDescription, gridBagConstraints1);

		//Separator line 1
		sep1 = new javax.swing.JSeparator();
		gridBagConstraints1 = new java.awt.GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridwidth = 2;
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		getContentPane().add(sep1, gridBagConstraints1);

		//button panel
		btnNew = new JButton("New");
		//btnEdit = new JButton("Edit");
		btnRemove = new JButton("Remove");
		JPanel buttonpanel = new JPanel(new FlowLayout());
		buttonpanel.add(btnNew);
		buttonpanel.add(btnRemove);
		gridBagConstraints1 = new java.awt.GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.gridwidth = 1;
		gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
		getContentPane().add(buttonpanel, gridBagConstraints1);

		//add listeners to new and remove buttons
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnNewActionPerformed(evt);
			}
		});
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnRemoveActionPerformed(evt);
			}
		});

		//separator 2
		sep2 = new javax.swing.JSeparator();
		gridBagConstraints1 = new java.awt.GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridwidth = 2;
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		getContentPane().add(sep2, gridBagConstraints1);
		
		//Timesteps label
		lblTimeSteps = new javax.swing.JLabel("Timestep ");
		timestepPnl  = new JPanel();
		SpinnerModel model = new SpinnerListModel(timesteps);

		timestepSpinner = new JSpinner(model);
		timestepSpinner.setEditor(new JSpinner.DefaultEditor(timestepSpinner));
		timestepSpinner.getEditor().setPreferredSize(new Dimension(100,16));
		timestepSpinner.addChangeListener(new ChangeListener(){
		
			public void stateChanged(ChangeEvent evt) {
				timestepSpinnerChanged(evt);				
			}

		});
		timestepPnl.add(lblTimeSteps);
		timestepPnl.add(timestepSpinner);
		
		gridBagConstraints1 = new java.awt.GridBagConstraints();
		//gridBagConstraints1.gridwidth = 1;
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
		gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
		getContentPane().add(timestepPnl, gridBagConstraints1);
		
		//		separator 3
		sep3 = new javax.swing.JSeparator();
		gridBagConstraints1 = new java.awt.GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridwidth = 2;
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		getContentPane().add(sep3, gridBagConstraints1);

		//objectives (label)
		lblObjectives = new JLabel("Objectives");
		gridBagConstraints1 = new java.awt.GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridwidth = 0;
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.insets = new java.awt.Insets(4, 10, 4, 10);
		getContentPane().add(lblObjectives, gridBagConstraints1);

		c_myTableModel = new MyTableModel();
		tblObjectives = new JTable(c_myTableModel);
		tblObjectives
				.setPreferredScrollableViewportSize(new Dimension(300, 100));

		//Create the scroll pane and add the table to it.
		JScrollPane scrollPane2 = new JScrollPane(tblObjectives);

		//Add the scroll pane to this panel.
		//this.getContentPane().add(scrollPane2);
		gridBagConstraints1 = new java.awt.GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridwidth = 2;
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		this.getContentPane().add(scrollPane2, gridBagConstraints1);

		//button panel 2
		btnOk = new JButton("OK");
		btnCancel = new JButton("Cancel");

		JPanel buttonpanel2 = new JPanel(new FlowLayout());
		buttonpanel2.add(btnOk);
		buttonpanel2.add(btnCancel);
		gridBagConstraints1 = new java.awt.GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		//gridBagConstraints1.gridy = 3
		gridBagConstraints1.gridwidth = 1;
		getContentPane().add(buttonpanel2, gridBagConstraints1);

		//add listeners to new and remove buttons
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnOkActionPerformed(evt);
			}
		});
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnCancelActionPerformed(evt);
			}
		});

	}

	/**
	 * Called when the timestep spinner has changed value 
	 * 
	 * @param evt The change event
	 */
	protected void timestepSpinnerChanged(ChangeEvent evt) {
		int newTimestep = timesteps.indexOf(timestepSpinner.getValue());
		//DEBUG
		//System.out.println(newTimestep);
		if (tblObjectives.isEditing()) {
			tblObjectives.getCellEditor().stopCellEditing();
		}
		updateObjectFunctionFromTable();
		currentTimestep = newTimestep;		
		initValues();
	}

	/**
	 * @param evt
	 */
	private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {
		String warningMessage = "Removing an object function can make"
				+ " \nall source nodes inconsistent."
				+ " \nDo you want to continue anyway?";
		int response = JOptionPane.showOptionDialog(null, warningMessage,
				"Warning", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE, null, null, null);
		if (response == JOptionPane.NO_OPTION
				|| response == JOptionPane.CLOSED_OPTION)
			return;
		//read the table into a new data structure
		int selectedRow = tblObjectives.getSelectedRow();
		if (selectedRow > -1) {//some row is seleced
			String str = (String)tblObjectives.getValueAt(selectedRow, 0);
			c_function.removeFunction(str);
			initValues();
			c_myTableModel.fireTableDataChanged();
		}
	}

	private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {
		//add one more line to the table
		int numberOfRows = c_myTableModel.getRowCount();		

		Object[][] data = new Object[numberOfRows + 1][6];
		int i;
		for (i = 0; i < numberOfRows; i++) {
			data[i][0] = c_myTableModel.getValueAt(i, 0);
			data[i][1] = c_myTableModel.getValueAt(i, 1);
			data[i][2] = c_myTableModel.getValueAt(i, 2);
			data[i][3] = c_myTableModel.getValueAt(i, 3);
			data[i][4] = c_myTableModel.getValueAt(i, 4);
			data[i][5] = c_myTableModel.getValueAt(i, 5);
		}
		data[i][0] = c_newLabel;
		data[i][1] = new Float((float) 0.0);
		data[i][2] = new Float((float) 0.0);
		data[i][3] = "off";
		data[i][4] = new Float(0);
		data[i][5] = new Float(0);

		c_myTableModel.setData(data);
		c_myTableModel.fireTableDataChanged();
	}

	private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {
		setVisible(false);
		dispose();
	}

	private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {
		try {

			// close the dialog
			closeDialog(null);
		} catch (NumberFormatException e) {
			c_gui.showMessageDialog("Not valid number format");
		}
	}

	/**
	 * Closes the dialog Add arguments for the extended table
	 */
	private void closeDialog(java.awt.event.WindowEvent evt) {
		
		//c_function.clear();
		if (tblObjectives.isEditing()) {
			tblObjectives.getCellEditor().stopCellEditing();
		}
		//add all table rows as functions to c_functions
		if (!allLabelsValid()) {
			String message = "All Objectfunctions need unique labels."
					+ "\nAt least one label is not unique or empty.";
			JOptionPane.showMessageDialog(null, message,
					"Empty or Non-Unique Label", JOptionPane.WARNING_MESSAGE);
			return;
		}
		 
		updateObjectFunctionFromTable();
		c_gui.setChanged(true);
		setVisible(false);
		dispose();
	}

	/** 
	 * 	Saves table data into the ObjectFunction.
	 *  Used by "exit" and spinner updates (timestepSpinnerChanged) 
	 */
	private void updateObjectFunctionFromTable() {
		
		int n = c_myTableModel.getRowCount();
		for (int i = 0; i < n; i++) {
			c_function.setObjectFunctionValues((String) tblObjectives
					.getValueAt(i, 0), ((Float) tblObjectives.getValueAt(i, 1))
					.floatValue(), ((Float) tblObjectives.getValueAt(i, 2))
					.floatValue(), (String) tblObjectives.getValueAt(i, 3),
					((Float) tblObjectives.getValueAt(i, 4)).floatValue(),
					((Float) tblObjectives.getValueAt(i, 5)).floatValue(),
					currentTimestep);
		}		
	}

	/**
	 * @return false if any label is empty or there are non-unique labels. 
	 * Otherwise true.
	 */
	private boolean allLabelsValid() {
		int nrRows = tblObjectives.getRowCount();
		//check for empty labels
		for (int i = 0; i < nrRows; i++) {
			if (!isValidLabel((String) tblObjectives.getValueAt(i, 0)))
				return false;
			//			 check for multiple occurences of a name
			for (int j = i + 1; j < nrRows; j++) {
				if (((String) tblObjectives.getValueAt(i, 0))
						.equalsIgnoreCase((String) tblObjectives.getValueAt(j,
								0)))
					return false;
			}
		}
		return true;
	}

	/**
	 * @param string
	 */
	private boolean isValidLabel(String string) {
		if (string.equals(""))
			return false;
		else
			return true;
	}

	public void setUpListColumn(JTable table, TableColumn listColumn) {
		//Set up the editor for the operator cells.
		JComboBox comboBox = new JComboBox();
		comboBox.addItem("off");
		comboBox.addItem("L1<b");
		comboBox.addItem("b<L2");
		comboBox.addItem("b=L2");
		comboBox.addItem("L1<b<L2");

		listColumn.setCellEditor(new DefaultCellEditor(comboBox));

		//Set up tool tips for the cells.
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setToolTipText("Click for combo box");
		listColumn.setCellRenderer(renderer);
	}

	/*
	 * Inner class of the table model
	 */
	class MyTableModel extends AbstractTableModel {

		private String[] columnNames = { "Label", "K1", "K2", "bound", "L1",
				"L2" };

		private Object[][] data = { { "", new Float(0.0), new Float(0.0),
				new Object(), new Float(0.0), new Float(0.0) }, };
		
		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		public boolean isCellEditable(int row, int col) {
			//return true for all columns except L1 and L2. They
			//depend on the bound, e.g. L1<b or L1<b<L2
			String bound = "";
			switch (col) {
			case 0:
				return true; //label
			case 1:
				return true; //k1
			case 2:
				return true; //k2
			case 3:
				return true; //bound
			case 4:
				bound = (String) getValueAt(row, 3);
				if (bound.equals("L1<b"))
					return true;
				else if (bound.equals("L1<b<L2"))
					return true;
				else
					return false;
			case 5:
				bound = (String) getValueAt(row, 3);
				if (bound.equals("b<L2"))
					return true;
				else if (bound.equals("b=L2"))
					return true;
				else if (bound.equals("L1<b<L2"))
					return true;
				else
					return false;
			default:
				System.out.println("ObjectFunctionDialog.MyTableModel." +
						"isCellEditable reached default case. (Should not " +
						"happen...)");
				return false;
			}

		}

		private boolean lastRow(int row) {
			return getRowCount() == row + 1;
		}

		public void setData(Object[][] d) {
			data = d;
		}

		/**
		 * Update table on change
		 */
		public void setValueAt(Object value, int row, int col) {

			if (DEBUG) {
				System.out.println("Setting value at " + row + "," + col
						+ " to " + value + " (an instance of "
						+ value.getClass() + ")");
			}

			data[row][col] = value;
			fireTableCellUpdated(row, col);

			if (DEBUG) {
				System.out.println("New value of data:");
				printDebugData();
			}

		}

		private void printDebugData() {
			int numRows = getRowCount();
			int numCols = getColumnCount();

			for (int i = 0; i < numRows; i++) {
				System.out.print("    row " + i + ":");
				for (int j = 0; j < numCols; j++) {
					System.out.print("  " + data[i][j]);
				}
				System.out.println();
			}
			System.out.println("--------------------------");
		}
	}

}