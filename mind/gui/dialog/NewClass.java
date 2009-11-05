/*
 * NewClass.java
 *
 * Created on den 2 juni 2008, 14:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mind.gui.dialog;
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

/**
 *
 * @author nawma77
 */
public class NewClass extends mind.gui.dialog.FunctionDialog implements FocusListener, ItemListener {
    
    JLabel lblRHS = new JLabel("RHS-value  ");
    PositiveNumberField txtRHSValue = new PositiveNumberField();
    String[] items = { "=", ">=", "<="};
    JComboBox rhsConstraintComboBox = new JComboBox(items);
    /** Creates a new instance of NewClass */
    public NewClass() {
        initComponents();
        
        
    }
    
    
    public void initComponents()
    {
    getContentPane().setLayout(new java.awt.GridBagLayout());
    java.awt.GridBagConstraints gridBagConstraints1;
    txtRHSValue.addFocusListener(this);
    rhsConstraintComboBox.addItemListener(this);
     gridBagConstraints1 = new java.awt.GridBagConstraints();
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
     setSize(300,600);
     this.setVisible(true);

    getContentPane().add(pnlRHS, gridBagConstraints1);
    
    
    }
    public void itemStateChanged(ItemEvent e) {}
     public void focusLost(FocusEvent e) {}
     public void focusGained(FocusEvent e) {

  }
    /*
	 * Inner class of the table model
	 */
	class MyTableModel extends javax.swing.table.AbstractTableModel {

		private String[] columnNames = { "Object", "Value" };

		private Object[][] data = { { "The proportion of previous flow", new Float(0.0) },{"Threshold value", new Float(0.0)} };

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

		/*
		 * JTable uses this method to determine the default renderer/
		 * editor for each cell.  If we didn't implement this method,
		 * then the last column would contain text ("true"/"false"),
		 * rather than a check box.
		 */
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		/*
		 * Don't need to implement this method unless your table's
		 * editable.
		 */
		public boolean isCellEditable(int row, int col) {
			//Note that the data/cell address is constant,
			//no matter where the cell appears onscreen.
			if (col < 1) {
				return false;
			} else {
				return true;
			}
		}

		/*
		 * set a new data body for this model
		 */
		public void setData(Object[][] d) {
			data = d;
		}

		/*
		 * Don't need to implement this method unless your table's
		 * data can change.
		 */
		public void setValueAt(Object value, int row, int col) {

			if (row>0) {
				System.out.println("Setting value at " + row + "," + col
						+ " to " + value + " (an instance of "
						+ value.getClass() + ")");
			}

			data[row][col] = value;
			fireTableCellUpdated(row, col);

			if (row>0) {
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
