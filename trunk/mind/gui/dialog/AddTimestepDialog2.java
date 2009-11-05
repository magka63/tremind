/*
 * Copyright 2001:
 * Peter Andersson <petan117@student.liu.se>
 * Martin Hagman <marha189@student.liu.se>
 * Henrik Norin <henno776@student.liu.se>
 * Anna Stjerneby <annst566@student.liu.se>
 * Tim TerlegÂrd <timte878@student.liu.se>
 * Johan Trygg <johtr599@student.liu.se>
 * Peter ≈strand <petas096@student.liu.se>
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

/**
 * The dialog for adding or changing a timestep
 * @author Johan Trygg
 * @author Jonas S‰‰v
 * @version 2003-09-18
 */


package mind.gui.dialog;
import java.text.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import mind.*;
import mind.gui.*;
import mind.model.*;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: GNU General Public License</p>
 * <p>Company: </p>
 * @author Jonas S‰‰v, ACE Simulation
 * @version 1.0
 */

public class AddTimestepDialog2 extends javax.swing.JDialog
    implements TableModelListener, FocusListener, KeyListener, ItemListener
{
  private GUI c_gui;
  private EventHandlerClient c_eventHandlerClient;

  private JFormattedTextField c_txtSteps;
  private JTextField c_txtName;
  private Timesteplevel c_tsl; // Only used in edit mode
  private int c_tslIndex;      // Only used in edit mode
  private boolean c_editMode = false;
  private String c_dialogName;
  private MyTableModel c_tableModel;
  private JLabel c_totalLabel = new JLabel("Total:");
  private JTable c_table;
  private NumberFormat c_numberFormat;
  private JButton c_btnOK;
  private JComboBox c_showSegmentsComboBox;
  private JScrollPane c_scrollPane;
  private String c_name="";
  private int c_steps=0;
  private Timesteplevel c_lastTimesteplevel; // the last created timsteplevel
  private Timesteplevel c_previousTimesteplevel; // used in edit mode

  /* private "special use variables"  */
  private float c_totalShouldBe = 0.0f;
  private float c_toplevelLength = 0.0f;

  /**
   * A help class used for the formatted textfield c_txtSteps
   * It checks that the value is an integer large than zero
   */
  public class FormattedTextFieldVerifier
      extends InputVerifier {
    public boolean verify(JComponent input) {
      if (input instanceof JFormattedTextField) {
        JFormattedTextField ftf = (JFormattedTextField) input;
        JFormattedTextField.AbstractFormatter formatter = ftf.getFormatter();
        if (formatter != null) {
          String text = ftf.getText();
          try {
            formatter.stringToValue(text);
            int i = Integer.parseInt(text);
            if (i <= 0) {
              throw (new ParseException("LE ZERO", 0));
            }
            c_steps = i;
            if (!c_name.equals("")) {
              c_btnOK.setEnabled(true);
            }
            return true;
          }
          catch (ParseException pe) {
            c_btnOK.setEnabled(false);
            return false;
          }
        }
      }
      return true;
    }

    public boolean shouldYieldFocus(JComponent input) {
      return verify(input);
    }
  }

  /**
   * This class describes the abstract table model
   * The table model holds it's data in the c_namesVector and c_lengthsVector
   */
private class MyTableModel extends AbstractTableModel {

    final String[] columnNames = {"Label", "Length"};
    private Vector c_namesVector = null;
    private Vector c_lengthsVector = null;
    private int c_first=0, c_last=-1;

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
      if (c_namesVector != null)
        return c_last - c_first + 1;
 //       return c_namesVector.size();
      else
        return 0;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
      if (col == 0)
        return c_namesVector.get(row+c_first);
      else
        return c_lengthsVector.get(row+c_first);
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col < 1) { // the first column is label and is not editable
            return false;
        } else {
            return true;
        }
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {
        c_lengthsVector.setElementAt(value, row+c_first);
        fireTableCellUpdated(row, col);
        updateTotalLabel(c_totalShouldBe);
    }

    /**
     * Sets the data vectors. They must be of equal lengths.
     * @param namesVector The vector containing the labels of the length values
     * @param lengthsVector The vector containing the length values (floats)
     */
    public void setData(Vector namesVector, Vector lengthsVector)
    {
      if ((namesVector == null) || (lengthsVector == null)) {
        javax.swing.JOptionPane.showMessageDialog(null,"Internal error in MyTableModel::setData vector is null");
        return;
      }
      if (namesVector.size() != lengthsVector.size()) {
        javax.swing.JOptionPane.showMessageDialog(null,"Internal error in MyTableModel::setData vector lengths are not equal");
        return;
      }
      c_namesVector = (Vector) namesVector.clone();
      c_lengthsVector = (Vector) lengthsVector.clone();
      c_first = 0;
      c_last = namesVector.size()-1;
      c_tableModel.fireTableDataChanged();
    }

    /**
     * Gets a copy of the names vector
     * @return the copy of the names vector
     */
    public Vector getNames()
    {
      return (Vector) c_namesVector.clone();
    }

    /**
     * Gets a copy of the length vector
     * @return the copy of the lengths vector
     */
    public Vector getLengths()
   {
     return (Vector) c_lengthsVector.clone();
   }

   /**
    * Clears the table data
    */
   public void clearData()
    {
      c_namesVector.clear();
      c_lengthsVector.clear();
      c_first = 0;
      c_last = -1;
    }

    /**
     * Sets part of the table data that should be viewable
     * @param first Pointer to the first element
     * @param last Pointer to the last element
     */
    public void setDataWindow(int first, int last)
    {
      /**
       * @todo Errorhandling here!!!
       */
      c_first = first;
      c_last = last;
      fireTableDataChanged();
    }

    public void maximizeDataWindow()
    {
      if (c_namesVector != null) {
        c_first = 0;
        c_last = c_namesVector.size()-1;
        fireTableDataChanged();
      }
    }

    /**
     * Calculates the sum of the viewable data
     * @return the sum of the viewable data
     */
    public double getLengthsSum()
    {
      double sum = 0.0;

      for (int i = c_first;i <= c_last;i++) {
        sum+= ((Float) c_lengthsVector.get(i)).floatValue();
      }
      return sum;
    }
  }

  /**
   * Creates a new Timesstep
   * @param parent Parent of the dialog
   * @param modal boolean modal or not modal
   * @param gui The gui
   * @throws HeadlessException
   */
  public AddTimestepDialog2(javax.swing.JDialog parent, boolean modal,
                            GUI gui)
      throws HeadlessException
  {
    super(parent, modal);
    c_gui = gui;
    c_eventHandlerClient = gui.getEventHandlerClient();
    c_lastTimesteplevel = c_eventHandlerClient.getLastTimesteplevel();
    c_dialogName="Add Timestep Level";
    setupFormats();
    c_toplevelLength = ((Float) c_gui.getEventHandlerClient().getTopTimesteplevel().
                        getLengthsVector().get(0)).floatValue();
    c_totalShouldBe = c_toplevelLength;
    initComponents();
    c_previousTimesteplevel = c_lastTimesteplevel;
    setUpComboBox();
    updateTotalLabel(c_toplevelLength);
    addListeners();
  }

  /**
   * This Constructor is used when changing an existing timestep
   * @param parent
   * @param modal
   * @param gui
   * @param tsl
   * @param tslIndex
   * @throws HeadlessException
   */
  public AddTimestepDialog2(javax.swing.JDialog parent, boolean modal,
                            GUI gui, Timesteplevel tsl, int tslIndex)
      throws HeadlessException
  {
    super(parent, modal);
    c_gui = gui;
    c_eventHandlerClient = gui.getEventHandlerClient();
    c_dialogName="Edit Timestep Level";
    c_tsl = tsl;
    c_tslIndex = tslIndex;
    c_name = tsl.getLabel();
    c_steps = tsl.getTimesteps();
    c_editMode = true;
    setupFormats();
    c_toplevelLength = ((Float) c_gui.getEventHandlerClient().getTopTimesteplevel().
                        getLengthsVector().get(0)).floatValue();
    c_totalShouldBe = c_toplevelLength;
    c_previousTimesteplevel = tsl.getPrevLevel();
    initComponents();
    setUpComboBox();
    updateTotalLabel(c_toplevelLength);
    addListeners();
  }

  private void setupFormats()
  {
    c_numberFormat = NumberFormat.getIntegerInstance();
  }
  private void addListeners()
  {
    c_tableModel.addTableModelListener(this);
    c_txtName.addFocusListener(this);
    c_txtName.addKeyListener(this);
    c_txtSteps.addFocusListener(this);
    c_txtSteps.addKeyListener(this);
    c_showSegmentsComboBox.addItemListener(this);
  }
  public void tableChanged(TableModelEvent e)
  {

    int row, col;

    row = e.getFirstRow();
    col = e.getColumn();

    if (col == 1) {
      if (((Float)c_tableModel.getValueAt(row, col)).floatValue() <= 0.) {
        c_gui.showWarningDialog(c_dialogName, "Length can't be less than or equal to zero");
        c_tableModel.setValueAt(new Float(1.0f), row, col);
      }
    }
    try {
      Float.parseFloat(c_tableModel.getValueAt(row,col).toString());
    } catch (NumberFormatException ex) {
      c_gui.showWarningDialog(c_dialogName, ex.toString());
    }
  }

  private void setUpComboBox()
  {
    c_showSegmentsComboBox.removeAllItems();
    c_showSegmentsComboBox.addItem("Show all");

    if (c_previousTimesteplevel == null) {  // we are editing the top level which has no parents
      c_showSegmentsComboBox.setEnabled(false);
      return;
    }

    java.util.Enumeration enu = c_previousTimesteplevel.getNamesVector().elements();
    while (enu.hasMoreElements()) {
      c_showSegmentsComboBox.addItem(enu.nextElement());
    }
  }

  private void initComponents() {
    GridBagLayout gridbag = new GridBagLayout();
    getContentPane().setLayout(gridbag);
    GridBagConstraints constraints = new GridBagConstraints();

    setTitle(c_dialogName);

    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        closeDialog(evt);
      }
    });

    JLabel nameLabel = new JLabel("Name:");
    c_txtName = new JTextField();
    JLabel divisionLabel = new JLabel();
    c_txtSteps = new JFormattedTextField(c_numberFormat);
    c_txtSteps.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
    c_txtSteps.setInputVerifier(new FormattedTextFieldVerifier());
    JLabel showLabel = new JLabel("Show segments:");
    c_showSegmentsComboBox = new JComboBox();
    JPanel emptyPanel = new JPanel();
    // The table
    c_tableModel = new MyTableModel();

    if (!c_editMode) {
      Vector tempName = new Vector(1);
      Vector tempLength = new Vector(1);
      tempName.add((Object) "Noname");
      tempLength.add(new Float(c_toplevelLength));
      c_tableModel.setData(tempName, tempLength);
    } else {
      c_tableModel.setData(c_tsl.getNamesVector(), c_tsl.getLengthsVector());
    }
    c_table = new JTable(c_tableModel);
    c_scrollPane = new JScrollPane(c_table);
    c_scrollPane.setPreferredSize(new Dimension(250,200));

    JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
    c_btnOK = new JButton("OK");
    JButton btnCancel = new JButton("Cancel");

// Name label
    buildConstraints(constraints, 0, 0, 2, 1, 100, 0);
    constraints.fill = GridBagConstraints.NONE;
    constraints.anchor = GridBagConstraints.NORTHWEST;
    constraints.insets = new Insets(10, 8, 0, 8);
    gridbag.setConstraints(nameLabel, constraints);
    getContentPane().add(nameLabel);
// Name text field
    buildConstraints(constraints, 0, 1, 2, 1, 100 , 0);
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.anchor = GridBagConstraints.NORTHWEST;
    constraints.insets = new Insets(0, 8, 0, 8);
    gridbag.setConstraints(c_txtName, constraints);
    getContentPane().add(c_txtName);
    if (c_editMode)
      c_txtName.setText(c_tsl.getLabel());
// Steps label
      if (!c_editMode) {
        divisionLabel.setText("Divides " +
                                c_eventHandlerClient.getLastTimesteplevel().getLabel() +
                                " in this many number of steps:");
      } else if (c_previousTimesteplevel != null) {
        divisionLabel.setText("Divides " +
                                c_previousTimesteplevel.getLabel() +
                                " in this many number of steps:");
      } else {  // It is the top level
        divisionLabel.setText("Divides " +
                                c_tsl.getLabel() +
                                " in this many number of steps:");
      }
    buildConstraints(constraints, 0, 2, 2, 1, 100, 0);
    constraints.fill = GridBagConstraints.NONE;
    constraints.anchor = GridBagConstraints.NORTHWEST;
    constraints.insets = new Insets(10, 8, 0, 8);
    gridbag.setConstraints(divisionLabel, constraints);
    getContentPane().add(divisionLabel);
// Steps text field
    buildConstraints(constraints, 0, 3, 2, 1, 100 , 0);
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.anchor = GridBagConstraints.NORTHWEST;
    constraints.insets = new Insets(0, 8, 0, 8);
    gridbag.setConstraints(c_txtSteps, constraints);
    getContentPane().add(c_txtSteps);
    if (c_editMode) {
      c_txtSteps.setText("" + c_tsl.getMaxTimesteps());
      if (c_tslIndex ==0)
        c_txtSteps.setEnabled(false); //The top level timestep 'divides' must be 1
    }

// Show segments label
    buildConstraints(constraints, 0, 4, 2, 1, 100, 0);
    constraints.fill = GridBagConstraints.NONE;
    constraints.anchor = GridBagConstraints.NORTHWEST;
    constraints.insets = new Insets(10, 8, 0, 0);
    gridbag.setConstraints(showLabel, constraints);
    getContentPane().add(showLabel);
// Show segments combobox
    buildConstraints(constraints, 0, 5, 1, 1, 50 , 0);
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.anchor = GridBagConstraints.NORTHWEST;
    constraints.insets = new Insets(0, 8, 0, 0);
    gridbag.setConstraints(c_showSegmentsComboBox, constraints);
    getContentPane().add(c_showSegmentsComboBox);

// Empty panel
    buildConstraints(constraints, 1, 5, 1, 1, 50, 0);
    constraints.fill = GridBagConstraints.BOTH;
    gridbag.setConstraints(emptyPanel, constraints);
    getContentPane().add(emptyPanel);
// Table scrollpane
    buildConstraints(constraints, 0, 6, 2, 1, 100 , 50);
    constraints.fill = GridBagConstraints.VERTICAL;
    constraints.anchor = GridBagConstraints.NORTHWEST;
    constraints.insets = new Insets(5, 8, 0, 8);
    gridbag.setConstraints(c_scrollPane, constraints);
    getContentPane().add(c_scrollPane);
// Totals label
    buildConstraints(constraints, 0, 7, 2, 1, 100, 0);
    constraints.fill = GridBagConstraints.NONE;
    constraints.anchor = GridBagConstraints.NORTHWEST;
    constraints.insets = new Insets(5, 8, 10, 5);
    gridbag.setConstraints(c_totalLabel, constraints);
    getContentPane().add(c_totalLabel);
// Separator
    buildConstraints(constraints, 0, 8, 2, 1, 100, 0);
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.anchor = GridBagConstraints.NORTHWEST;
    constraints.insets = new Insets(0, 0, 10, 0);
    gridbag.setConstraints(separator, constraints);
    getContentPane().add(separator);

// OK button
    buildConstraints(constraints, 0, 9, 1, 1, 100 , 0);
    constraints.fill = GridBagConstraints.NONE;
    constraints.anchor = GridBagConstraints.SOUTHEAST;
    constraints.insets = new Insets(0, 0, 10, 20);
    gridbag.setConstraints(c_btnOK, constraints);
    getContentPane().add(c_btnOK);
    c_btnOK.setEnabled(false);
//    getRootPane().setDefaultButton(c_btnOK);

    c_btnOK.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnOKActionPerformed(evt);
      }
    });

// Cancel button
    buildConstraints(constraints, 1, 9, 1, 1, 100 , 0);
    constraints.fill = GridBagConstraints.NONE;
    constraints.anchor = GridBagConstraints.SOUTHWEST;
    constraints.insets = new Insets(0, 20, 10, 0);
    gridbag.setConstraints(btnCancel, constraints);
    getContentPane().add(btnCancel);

    btnCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnCancelActionPerformed(evt);
      }
    });

    pack(); // Just once doesn't seem to be enough, but
    pack(); // calling pack() twice really does it!!!
  }

  private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {
    closeDialog(null);
  }

  private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {

/*    if (c_table.isEditing()) {
      c_gui.showWarningDialog(c_dialogName, "Please press ENTER after editing a length value");
    } */

if (c_table.isEditing()) {
  int row = c_table.getEditingRow();
  int col = c_table.getEditingColumn();
  if (!c_table.getCellEditor(row, col).stopCellEditing()) {  // could not stop editing due to a bad value
    c_gui.showWarningDialog(c_dialogName, "Bad value in length column");
    return;
  }
}
    int steps;
    try {
      steps = Integer.parseInt(c_txtSteps.getText());
    }
    catch (NumberFormatException e) {
      c_gui.showWarningDialog(c_dialogName,
          " - Only integers > 0 is allowed in the 'divides' field!");
      return;
    }
    if (steps < 1) {
      c_gui.showWarningDialog(c_dialogName,
          " - Only integers > 0 is allowed in the 'divides' field!");
      return;
    }
    boolean ok;
    if (c_editMode)
      ok = c_eventHandlerClient.changeTimesteplevel(c_tslIndex,
          c_txtName.getText(), steps,
          c_tableModel.getNames(), c_tableModel.getLengths());
    else {
      ok = c_eventHandlerClient.addTimesteplevel(c_txtName.getText(), steps,
                                                 c_tableModel.getNames(),
                                                 c_tableModel.getLengths());
    }
    if (!ok) {
      c_gui.showWarningDialog(c_dialogName, " - A timesteplevel with this name already exists.");
      return;
    }
      closeDialog(null);
  }

  /** Closes the dialog */
  private void closeDialog(java.awt.event.WindowEvent evt)
  {
      setVisible(false);
      dispose();
  }

  private void updateTable()
  {
    /* This function is ugly! Make one updateTable() for editmode and
     another function for non-editmode instead. To much special handling of
     modes, and other things. */

      Vector lengthsVector = new Vector();
      Vector namesVector = new Vector();
      int counter = 1;

      if (c_previousTimesteplevel != null) {
        for (int i = 0; i < c_previousTimesteplevel.getTimesteps(); i++) {
          for (int j = 0; j < c_steps; j++) {
              float length = ( (Float) c_previousTimesteplevel.getLengthsVector().get(i)).
                  floatValue();
            length = length / c_steps;
            lengthsVector.add(new Float(length));
            namesVector.add(c_name + counter);
            //System.out.println(c_name + counter + " length = " + length);
            counter++;
          }
        }
      } else  {// special handling if editing the top level
        lengthsVector.add(c_tsl.getLengthsVector().get(0));
        namesVector.add(c_name);
      }
      if (!c_editMode)
        c_tableModel.setData(namesVector, lengthsVector);
      else {
        if (c_previousTimesteplevel != null) { // see special handling if editing the top level
          if (c_steps != c_tsl.getTimesteps()) {
            // if c_steps is changed we use the default generated vectors from above
            c_tableModel.setData(namesVector, lengthsVector);
          }
          else {// otherwise we use the already available vectors
            c_tableModel.setData(c_tsl.getNamesVector(), c_tsl.getLengthsVector());
            if (!c_name.equals(c_tsl.getLabel())) { // We have changed the name
                c_tableModel.setData(namesVector, c_tsl.getLengthsVector());
            }
          }
        } else {
          c_tableModel.setData(namesVector, lengthsVector); // if editing top level
        }
      }
  }

  /**
   * Constraint builder help function
   * @param gbc the contraint to be setup
   * @param gx gridx
   * @param gy gridy
   * @param gw gridwidth
   * @param gh gridheight
   * @param wx weightx
   * @param wy weighty
   */
  private void buildConstraints(GridBagConstraints gbc, int gx, int gy, int gw, int gh, int wx, int wy)
  {
    gbc.gridx = gx;
    gbc.gridy = gy;
    gbc.gridwidth = gw;
    gbc.gridheight = gh;
    gbc.weightx = wx;
    gbc.weighty = wy;
  }

  /**
   * Disables the OK-button and clears table data when a key is typed
   * @param e
   */
  public void keyTyped(KeyEvent e) {
    if (e.getSource() != c_txtName) {
      c_btnOK.setEnabled(false);
      c_tableModel.clearData();
      repaint();
    }
  }

  /**
   * Checks if the ENTER key is pressed
   * @param e
   */
  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
      ((JTextField)e.getSource()).transferFocus();
    }
  }

  public void keyReleased(KeyEvent e) {

  }

  public void focusGained(FocusEvent e) {
//    if (e.getSource() instanceof JTextField) {
      ((JTextField)e.getSource()).selectAll();
//    }
  }
  public void focusLost(FocusEvent e) {
    if (e.getSource() == c_txtName) {
      if (((JTextField)e.getSource()).getText() != "") {
        c_name = ((JTextField)e.getSource()).getText();
      }
    }
    if ((!c_name.equals("")) && (c_steps > 0)) {
      c_btnOK.setEnabled(true);
      updateTable();
    }
  }

  /**
   * Takes care of the ComboBox
   * @param e the event to be handled
   */
  public void itemStateChanged(ItemEvent e)
  {
    int index = c_showSegmentsComboBox.getSelectedIndex();

    if (index != -1) {
      if (index == 0) { // Show all is selected
        c_tableModel.maximizeDataWindow();
        c_totalShouldBe = c_toplevelLength;
        updateTotalLabel(c_totalShouldBe);
      } else {
        int start = (index-1)*c_steps;
        int end = (index-1)*c_steps + (c_steps -1);
        c_tableModel.setDataWindow(start, end);
        c_totalShouldBe = ((Float)c_previousTimesteplevel.getLengthsVector().get(index-1)).floatValue();
        updateTotalLabel(c_totalShouldBe);
      }
    }
  }

  private void updateTotalLabel(float shouldbe)
  {
    DecimalFormatSymbols df = new DecimalFormatSymbols();
    df.setDecimalSeparator('.');
    DecimalFormat nf = (DecimalFormat) DecimalFormat.getNumberInstance();
    nf.setMaximumFractionDigits(2);
    nf.setDecimalFormatSymbols(df);
    c_totalLabel.setForeground(new Color(0, 0, 0));

    if (java.lang.Math.abs(c_tableModel.getLengthsSum() - shouldbe) >
        0.001 * shouldbe) {
      c_totalLabel.setForeground(new Color(255, 0, 0));
    }
    if (c_previousTimesteplevel != null) {
      c_totalLabel.setText("Total = " + nf.format(c_tableModel.getLengthsSum()) +
                           " (should be: " + nf.format(shouldbe) + ")");
    }
    else {
      c_totalLabel.setText("Total = " + nf.format(c_tableModel.getLengthsSum()));
    }
  }

/*  private double sum(Vector v) {
    double sum=0.0;

    java.util.Enumeration enu = v.elements();

    while (enu.hasMoreElements()) {
      sum += ((Double) enu.nextElement()).doubleValue();
    }

    return sum;
  }  */

/*  public static void main(String[] args) throws HeadlessException {
    addTimestepDialog2 dlg = new addTimestepDialog2();
    dlg.setDefaultCloseOperation(EXIT_ON_CLOSE);
    dlg.show();
  } */
}