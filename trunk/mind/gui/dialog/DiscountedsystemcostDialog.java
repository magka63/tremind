/**
 * Copyright 2007:
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
/*
 * Discountsystemcost Dialog is a new Dialog which contain two Text Field one for annual interest and other
 * for the analyses period's length and one Table. The table contain all timestep numbers. Eeach year or row 
 * should have or cotain a unique number of the timestep.
 * 
/*
 * DiscountedsystemcostDialog.java
 *
 * Created on den 1 juni 2007, 10:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mind.gui.dialog;

/**
 * This class represents the Discounted system cost dialog.
 *
 * @author Nawzad Mardan
 * @version 2007-06-01
 */
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import java.awt.event.KeyEvent;
import javax.swing.table.AbstractTableModel;
import mind.model.function.*;
import mind.gui.*;
import mind.model.*;
import mind.EventHandlerClient;

public class DiscountedsystemcostDialog extends javax.swing.JDialog
{
 // GUI need it to get Timesteps and Save the Discountedsystemcost data in the Model  
  private GUI c_gui;
 // EventHandlerClient need it to get and set data from Model. Ex. to get or set Discountedsystemcost data 
  private EventHandlerClient c_eventHandlerClient;
  // Interest Field
  private JTextField c_interestField;
  // Analyses period Field
  private JTextField c_periodField;
  private String c_dialogName= "Discounted system cost";
  // Table to insert timestep numbers
  private MyTableModel c_tableModel;
  private JLabel c_rateLabel;
  private JLabel lblDescription;
  private JLabel c_lengofAnalysis ;
  private JTable c_table;
  private JButton btnDefault;
  private JButton btnAddColumn;
  private JButton btnAddRow;
  private JButton btnDeleteRow;
  private JButton btnDeleteColumn;
  private JButton btnOk;
  private JButton btnCancel;
  private JScrollPane c_scrollPane;
  private int c_maxTimeSteps = 1;
  private JSeparator sep1;
  private JSeparator sep2;
  private JSeparator sep3;
  // Discountedsystemcost function
  private DiscountedsystemcostControl c_discountedsystemcostControl;
  private Long c_analysperiod ;
  private Integer c_rate = 0;
  private Vector c_timeStepValues;
  //private InvestmentCost c_investmentCost;
  //boolean c_investment;
  private JTextField exprTxt;
  private JTextField editorText;
  private boolean editedLast = false;
  private String discardText = "";
  private DefaultCellEditor editor;
    /** Creates a new instance of DiscountedsystemcostDialog */
    public DiscountedsystemcostDialog() {
    }
    
     /** Creates new form DiscountedsystemcostDialog */
    public DiscountedsystemcostDialog(java.awt.Frame parent, boolean modal, GUI gui) 
        {
        super(parent, modal);
        c_gui = gui;
        setTitle(c_dialogName);
        
        //calculate number of timesteps
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
         initComponents();
         updateTable();
         //c_table.setRowSelectionAllowed(true);
         //c_table.setColumnSelectionAllowed(true);
        }
     
     /** Creates new form DiscountedsystemcostDialog *
    public DiscountedsystemcostDialog(JDialog parent , boolean modal, GUI gui, InvestmentCost inves) 
        {
        super(parent, modal);
        c_gui = gui;
        setTitle(c_dialogName);
        c_investmentCost = inves;
        c_investment = true;
        
        //calculate number of timesteps
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
         initComponents();
         updateTable();
        }*/
   
    /**
     * MyTableModel is inner klass that hold its data in an array and kan  get the data from an outside source such as a database. 
     * MyTableModel is simple, can easily determine the data's type, helping the JTable display the data in the best format.
     * MyTableModel let you edit the name columns; it does, however, let you edit the other columns.  
*/
    
    private class MyTableModel extends AbstractTableModel 
    {
    private String[] columnNames = {"Year", "Timestep nr."};
    private Object[][] data = { { "Year1", ""},{ "Year2",""}, {"Year3", ""},{"Year4", ""},};
 
    public int getColumnCount() 
    {
    return columnNames.length;
    }
                
    public void setColumnName(String [] name)
    {
     columnNames = new String[name.length];
     columnNames = name;
    }
    
    public String [] getTableHedar()
    {
        return columnNames;
    }

    public int getRowCount() 
    {
    return data.length;
    }

    public String getColumnName(int col) 
    {
    return columnNames[col];
    }

    public Object getValueAt(int row, int col) 
    {
    return data[row][col];
    }
    
    /**
     * Returns the type of the column appearing in the view at column position column
     */
    
    public Class getColumnClass(int c) 
    {
    return getValueAt(0, c).getClass();
    }
    
    /*
     * Need it to see if the table's
     * editable.
     */

    public boolean isCellEditable(int row, int col) 
    {
    if (col>0)
    {
        //CalculateTimestepLength(data, getRowCount() ,getColumnCount());
        return true;
    }
        
    else
        return false;
    }

    private boolean lastRow(int row) 
    {
    return getRowCount() == row + 1;
    }

    public void setData(Object[][] d) 
    {
    data = d;
    }
                
    public Object[][] getData()
    {
    return data;
    }
    
    /**
    * Update table on change
     */
    public void setValueAt(Object value, int row, int col) 
    {
    data[row][col] = value;
    //CalculateTimestepLength(data, getRowCount() ,getColumnCount());
    fireTableCellUpdated(row, col);
    
    
    }
    
    
    }// END MYTable
    
  
    /** This method is called from within the constructor to
     * initialize the form.
     */ 
    private void initComponents() 
    {
    GridBagLayout gridbag = new GridBagLayout();
    getContentPane().setLayout(gridbag);
    GridBagConstraints gridBagConstraints1;

    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
         setVisible(false);
		dispose();}});
                
    lblDescription = new javax.swing.JLabel();
    lblDescription.setText("Discounted system cost");
    //lblDescription.setText("Description: Discounted system cost is a function
    // from which .");
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridwidth = 2;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints1.insets = new java.awt.Insets(10, 10, 10, 10);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    getContentPane().add(lblDescription, gridBagConstraints1);

    //Separator line 1
    sep3 = new javax.swing.JSeparator();
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridwidth = 2;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    getContentPane().add(sep3, gridBagConstraints1);
    
    c_rateLabel= new JLabel("Annual interest:");
    JLabel percent = new JLabel(" %");
    c_lengofAnalysis = new JLabel("Length of analyses period:");
    c_interestField = new JTextField(10);
    c_interestField.setToolTipText("Annually interest.");
    c_periodField = new JTextField(10);
    c_periodField.setToolTipText("Length of analyses period");
    JPanel ratePanel = new JPanel();
    JPanel analysPanel = new JPanel();
    JPanel showPanel = new JPanel();
    JPanel tablePanel = new JPanel();
    JPanel okcancelPanel = new JPanel();
    // The table
    c_tableModel = new MyTableModel();
 
    //c_tableModel.
    JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
  
    // Add rate label and rate field 
    ratePanel.add(c_rateLabel);
    ratePanel.add(c_interestField);
    ratePanel.add(percent);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    //gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    getContentPane().add(ratePanel, gridBagConstraints1);
 
    analysPanel.add(c_lengofAnalysis);
    analysPanel.add(c_periodField);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    //gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    getContentPane().add(analysPanel, gridBagConstraints1);
    
    sep1 = new javax.swing.JSeparator();
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridwidth = 2;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    getContentPane().add(sep1, gridBagConstraints1);
    
    btnDefault = new JButton("Default");
    btnAddColumn = new JButton("Add Column");
    btnAddRow = new JButton("Add Row");
    btnDeleteRow = new JButton("Delete Row");
    btnDeleteColumn = new JButton("Delete Column");
    showPanel.add(btnDefault);
    showPanel.add(btnAddColumn);
    showPanel.add(btnAddRow);
    showPanel.add(btnDeleteColumn);
    showPanel.add(btnDeleteRow);
    btnDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
                            btnDefaultActionPerformed();
			}
		});
                
    btnAddColumn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnAddColumnActionPerformed();
			}
		});
                
    btnAddRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnAddRowActionPerformed();
			}
		});
    btnDeleteColumn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnDeleteColumnActionPerformed();
			}
		});
    btnDeleteRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnDeleteRowActionPerformed();
			}
		});
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    //gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    getContentPane().add(showPanel, gridBagConstraints1);
    
    sep2 = new javax.swing.JSeparator();
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridwidth = 2;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    getContentPane().add(sep2, gridBagConstraints1);
    c_table = new JTable(c_tableModel);
  /*  c_table.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) { }
                        public void keyReleased(KeyEvent e){
                            CalculateTimestepLength(c_tableModel.getData(), c_tableModel.getRowCount() ,c_tableModel.getColumnCount());
                            }
                        public void keyTyped(KeyEvent e){}
                        
		});*/
    c_table.setRowSelectionAllowed(false);
    c_table.setColumnSelectionAllowed(false);
    c_table.setPreferredScrollableViewportSize(new Dimension(500, 145));
    //ToolTipManager.sharedInstance().unregisterComponent(c_table);
    //ToolTipManager.sharedInstance().unregisterComponent(c_table.getTableHeader());
   // c_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    //c_table.setPreferredScrollableViewportSize(new Dimension(800, 100));
    //c_table.setAutoResizeMode(5);
    //c_table.setPreferredSize(new Dimension(300, 200));
    //Create the scroll pane and add the table to it.
    
   //"excell behavior"
    // expression text panel (to simplifie use of long expressions)
    exprTxt = new JTextField(30);
    editorText = new JTextField();
    exprTxt.addKeyListener(new KeyAdapter(){
                                public void keyReleased(KeyEvent e)
                                    {
                                        if(exprTxt.hasFocus())
                                            {
                                            editorText.setText(exprTxt.getText());
                                            editedLast = false;
                                            }
                                      //  CalculateTimestepLength(c_tableModel.getData(), c_tableModel.getRowCount() ,c_tableModel.getColumnCount());
                                       // editorText.grabFocus();
                                    }
                        	});

    exprTxt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int row = 0, column = 0;
				column = c_table.getEditingColumn();
				row = c_table.getEditingRow();
				if( row != -1 && column != -1 ){
					row++;
					if (row > c_table.getRowCount() - 1){
						row = 0;
						column++;
						if (column > c_table.getColumnCount() - 1) column = 0;
					}
					c_table.editCellAt(row, column);
					exprTxt.setText(editorText.getText());
					}
				}
		    });
    

    editorText.addKeyListener(new KeyAdapter(){
                                public void keyReleased(KeyEvent e)
                                        {
                                        if(editorText.hasFocus())
                                            {
                                            exprTxt.setText(editorText.getText());
                                            editedLast = true;
                                            }
                                        }
                                        });

    editorText.addFocusListener(new FocusAdapter(){
			public void focusGained(FocusEvent e)
                                {
                            	if(c_table.isEditing())
                                    {
				    editorText.setBackground(Color.WHITE);
                                    exprTxt.setText(editorText.getText());
                                    discardText = editorText.getText();
					editedLast = true;
				}
                               // CalculateTimestepLength(c_tableModel.getData(), c_tableModel.getRowCount() ,c_tableModel.getColumnCount());
                               // editorText.grabFocus();
                                //editorText.notifyAll();
                                //editorText.removeNotify();
                                //editorText.requestFocus(true);
                                //editorText.setFocusable(true);
                               
			}
		});

		editor = new DefaultCellEditor (editorText);
		c_table.setDefaultEditor(Object.class, editor);
		c_table.setCellEditor(editor);
		c_table.setSurrendersFocusOnKeystroke(true);
		editor.setClickCountToStart(1);

		//copy cell contents to expression field when user selects with arrow keys
		c_table.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent e){
                            
				if(!c_table.isEditing()){
                                    
					int row = c_table.getSelectedRow();
					int column = c_table.getSelectedColumn();
                                        //CalculateTimestepLength(c_tableModel.getData(), c_tableModel.getRowCount() ,c_tableModel.getColumnCount());
					if(row > -1 && column > -1){
                                           // CalculateTimestepLength(c_tableModel.getData(), c_tableModel.getRowCount() ,c_tableModel.getColumnCount());
						String str = (String) c_table.getValueAt(row,column);
						exprTxt.setText(str);
					}
				}
                                
			}
                       
		});
    //c_scrollPane = new JScrollPane(c_table);
    c_scrollPane = new JScrollPane(c_table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    JScrollBar jb = new JScrollBar();
    c_scrollPane.setHorizontalScrollBar(jb);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridwidth = 2;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    this.getContentPane().add(c_scrollPane, gridBagConstraints1);
    btnOk = new JButton("OK");
    btnCancel = new JButton("Cancel");
    okcancelPanel.add(btnOk);
    okcancelPanel.add(btnCancel);
    
  btnCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
		dispose();
      }
    });
    
    btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnOkActionPerformed();
			}
    });
    
    // sep3 = new javax.swing.JSeparator();
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridwidth = 3;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    getContentPane().add(okcancelPanel, gridBagConstraints1);
 
   // this.setPreferredSize(new Dimension(500, 500));
    //this.setVisible(true);
  
  
   pack(); // Just once doesn't seem to be enough, but
   pack();// calling pack() twice really does it!!!
    
    }
  
     /** This method is called the user clicks or presses the Ok button
     *
      * The method calulate the length of timesteps for each year(row) and put it in the first cell in each row
   */
   private void CalculateTimestepLength(Object [][]data, int numberOfRows, int numberOfColumns)
   {
      float periodLength =.0f;
      int value =0;
      int yearNum =1;
      for(int i = 0; i< numberOfRows; i++)
      {
          for(int j = 1; j < numberOfColumns; j++ )
          {
           // Check if the data inside the cell is not an empty string 
              if(!(data[i][j].equals("")))
              {
              String strValue = (String) data[i][j];
              value = Integer.parseInt(strValue);
              }
              else
                  continue;
              if((value > c_timeStepValues.size()) || (value <= 0))
                 {
                  int k = i+1;
                  int m = j+1;
                    JOptionPane.showMessageDialog(null,"Wrong Timestep number. Please enter correct timestep number in cell" + 
                            "["+ k + "]"+"["+ m + "].\n"+ "  ( cell [Row][Column] )");
                    c_tableModel.setValueAt("",i,j);
                    break;
                 }
              else
              periodLength = periodLength + ((Float)c_timeStepValues.get(value-1)).floatValue();
              if(periodLength > 8784)
                {
                  int k = i+1;
                  int m = j+1;
                 JOptionPane.showMessageDialog(null,"Incorrect Timestep number. Please type in correct timestep number in cell" + 
                            "["+ k + "]"+"["+ m + "].\n"+ "  ( cell [Row][Column] )"+
                       "The total hours of one year should be 8784.");
                    c_tableModel.setValueAt("",i,j);
                    return;
                }
              
          }//
        
          c_tableModel.setValueAt("Year"+ yearNum++ +"("+periodLength+")",i,0);
          periodLength = 0;
      }// END FOR
       c_tableModel.fireTableDataChanged();
   }
   
   
   /** This method is called when user clicks or presses the Ok button
     *
    * This method control all table's the data celles 
    * Return  isOk string if  every thing is ok  
    * otherwise  error string if any data cell is contain incorrect value
    */
   
   private String tableDataControl(Object [][] data)
   {
    int empty = 0;
    Object temp = "Empty";
    int equalValue = 0;
    int digit = 0;
    int numberOfTimestep=c_maxTimeSteps;
    int maxValue = c_maxTimeSteps;
       
    // Check the tabel 
    if(data != null)
     {
       int numberOfRawe = data.length;
       int numberOfColumn = data[0].length;
       
       for(int k = 0; k < numberOfRawe; k++)
       {
         for(int l = 1; l < numberOfColumn; l++)
         {  
            if(!data[k][l].equals(""))
              {
                temp = data[k][l];
                equalValue = 0;
                
                for(int i = 0; i<numberOfRawe;i++)
                    {
                    for(int j = 1; j < numberOfColumn; j++)
                        {
                        // Check if they have same value
                        if(data[i][j].equals(temp))
                        equalValue++;
                        }
                    }
               }
            // Tow timestep number have the same value
            if(equalValue > 1)
                {
                return "Please enter correct timestep number. "
                            +"The timestep number: "+ temp+ " exist more than one time.\n"+
                            "Each timestep number must exist only one time in the table" ;
                }
            if(!data[k][l].equals(""))
              {
              digit++;
              String strValue = (String)data[k][l];
              //Integer lnum = Integer.getInteger(strValue);
              int intValue;
              //intValue = lnum.intValue();
              intValue = Integer.parseInt(strValue);
              if(intValue > maxValue)
                  return "Wrong Timestep number. Please enter correct timestep number in cell" + 
                            "["+ k + "]"+"["+ l + "].\n"+ "  ( cell [Row][Column] )"+ 
                          "The maximum timestep value should be equal to the maximum timestep number";
              else if (intValue <= 0)
                  return "Wrong Timestep number. Please enter correct timestep number in cell" + 
                            "["+ k + "]"+"["+ l + "].\n"+ "  ( cell [Row][Column] )"+ 
                          "The timestep value should be lager than zero";
              }
           }
       }
       
       if(digit < numberOfTimestep)
         {
           int missingTimestepNumber = numberOfTimestep-digit; 
           return "Plese enter all timestep numbers to the Table.\n"+"Number of missing timestep number = "+missingTimestepNumber ;
         }
           
      }// END IF The table contain a data
    else
      {
        return "Empty Table";
      }
    return "isOK";
   }
   
  
   /**
  *Button Input Manually is pressd
  */ 
  
   private void btnDefaultActionPerformed()
    {
       
    //Check if the model is load it
      if(c_maxTimeSteps==0)
      {
             JOptionPane.showMessageDialog(null, "The medel haven't any Timesteps or have only one Timesteps, Load a model to"
					  + " optimize first.");
            return;
      }
      else
          defaultTable();
      
      /*TableColumn colum = null;
       for (int i = 0; i < numberOfColumns; i++) {
			colum = c_table.getColumnModel().getColumn(i);
			if (i == 0)
				colum.setPreferredWidth(70); //label and bound string column
			else
				colum.setPreferredWidth(25); //K1, K2, L1, L2 with num. values
		}
     /* }//END ELSE*/
    }
   
    /** This method is called the user clicks or presses the default or inputtmanually button
     *
    * Creating a new table depending on the analyeses period and the total number of the timesteps 
    *
    */
   private void defaultTable()
   {
      Object[][] data;
      int counter = 1;
      String textPeriod = c_periodField.getText();
      String rateField = c_interestField.getText();
      int timestepColNum=0 ;
      if(rateField.equals("") || rateField==null ||textPeriod == null || textPeriod.equals("")|| textPeriod.equals("0"))
      {
            if(rateField.equals("")|| rateField==null)
               {
                JOptionPane.showMessageDialog(null, "Pleas write a mount of the annually interest. ",
					"Empty Interest", JOptionPane.WARNING_MESSAGE);  
                return;
               }    
            else
                {
                JOptionPane.showMessageDialog(null, "Pleas write the length of analyses period ",
					"Empty period analayses", JOptionPane.WARNING_MESSAGE);  
                  return;
                }
      }// END IF
      
      else
      {
       c_analysperiod = Long.parseLong(textPeriod);//Integer.parseInt(textPeriod);/
       
       // Check if the analyses period is correct
       if(c_analysperiod > c_maxTimeSteps)
       {
           JOptionPane.showMessageDialog(null, "Please enter correct length of analyses period.\n "+"Analays period is larger than Timestep numbers" ,
					"Analays period larger than Timestep number", JOptionPane.WARNING_MESSAGE);
           return;
       }
       
       if(c_analysperiod <=  1)
       {
           JOptionPane.showMessageDialog(null, "Please enterthe length of analyses period.\n "+"Analays period value error!" ,
					"Analays period should be larger than 1", JOptionPane.WARNING_MESSAGE);
           return;
       }
       // Calculate number of columns
      timestepColNum = c_maxTimeSteps/c_analysperiod.intValue();
     
      //Float f_rate = Float.parseFloat(rateField);
      
      int numberOfRows = c_analysperiod.intValue();
       
      
     // Update table header
     String tabelName[] = new String[timestepColNum+1];
     for(int x = 0; x <= timestepColNum; x++ )
        {
            if(x==0)
                {
                tabelName[0]="Year";
                }
            else
              tabelName[x]="Timestep nr.";
        }// END FOR
      
      c_tableModel.setColumnName(tabelName);
      
      int x =1;
      int yearNum =1;
      
      // Initiate the tabel data 
      data = new Object[numberOfRows][timestepColNum+1];
      for(int i = 0; i< numberOfRows; i++)
      {
          for(int j = 0; j <= timestepColNum; j++ )
          {
              if(j == 0)
              {
               data[i][j]="Year"+ yearNum++;
               //c_tableModel.setValueAt("Year"+ yearNum++, i,j); 
              }
              else
              {
              String value = Integer.toString(x++);
              data[i][j]= new String(value);
              //c_tableModel.setValueAt(value, i, j);
              }
          }//
      }// END FOR
      
     // Update table data
     c_tableModel.setData(data);
    
    // Calulate the length of timesteps for each year an put it in the first cell in each row 
    //CalculateTimestepLength(data, numberOfRows, timestepColNum+1);
      
      c_tableModel.fireTableDataChanged();
      c_tableModel.fireTableStructureChanged();      
      
      
      }//END ELSE
       
   }
   
    /**
     *Button Add Timestep pressed
     *
     */
    
     private void btnAddColumnActionPerformed() 
     {
         
      //Check if the model is load it
      if(c_maxTimeSteps==0)
      {
            JOptionPane.showMessageDialog(null, "The medel haven't any Timesteps or have only one Timesteps, Load a model to"
					  + " optimize first.");
            return;
      }
      //add one more new column to the table
      int numberOfColumns = c_tableModel.getColumnCount();
      int numberOfRows = c_tableModel.getRowCount();
      
      String textPeriod = c_periodField.getText();
      String rateString = c_interestField.getText();
       if(rateString.equals("") || rateString==null ||textPeriod == null || textPeriod.equals(""))
      {
        if(rateString.equals(""))
        JOptionPane.showMessageDialog(null, "Pleas write a mount of the annually interest ",
					"Empty Interest", JOptionPane.WARNING_MESSAGE);  
    
      if(textPeriod.equals("") || textPeriod == null)
      {
        JOptionPane.showMessageDialog(null, "Pleas write the length of analyses period ",
					"Empty period analayses", JOptionPane.WARNING_MESSAGE);  
      }
      }
      else
      {
      
      c_analysperiod = Long.parseLong(textPeriod);//Integer.parseInt(textPeriod);/
      
      // Check if the analyses period is correct
      if(c_analysperiod > c_maxTimeSteps)
       {
           JOptionPane.showMessageDialog(null, "Please enter correct length of analyses period .\n "+"Analays period is larger than Timestep numbers",
					"Analays period larger than Timesteps", JOptionPane.WARNING_MESSAGE);
           return;
       }
      
     // int newColumnNumber = numberOfColumns+1;
     
      // Max number of column must equal to the total timestep numbers 
      if(numberOfColumns <= c_maxTimeSteps)   
      {
      Object[][] data = new Object[numberOfRows][numberOfColumns+1];
      // Add new column           
      for (int i = 0; i < numberOfRows; i++) 
          {
          for (int j = 0; j < numberOfColumns; j++)
              {
                data[i][j] = c_tableModel.getValueAt(i, j);
              }
           }
         
      for(int i = 0; i <numberOfRows;i++)
           data[i][numberOfColumns]= "";
      
    
     int stringLength = c_tableModel.getTableHedar().length;
     
     String tempString[] = c_tableModel.getTableHedar();
     String tableHeder[]= new String [stringLength+1];
     for(int i = 0; i < stringLength; i++)
     {
         tableHeder[i]= tempString[i];
     }
     tableHeder[stringLength]="Timestep nr.";
     
    // Update the table
     c_tableModel.setColumnName(tableHeder);
     c_tableModel.setData(data);
     c_tableModel.fireTableDataChanged();
     c_tableModel.fireTableStructureChanged();
      }
      else
           JOptionPane.showMessageDialog(null, "Cann't add more columns. " +
                   "Maximum number of the columns should be equal to the total number of the timesteps.\n ",
                   "Cann't add columns", JOptionPane.WARNING_MESSAGE);
      }// END ELSE
         
    } 
     
 /**
  *Button OK is pressd
  */ 
  private void btnOkActionPerformed()
  {
      
     //Check if the model is load it
      if(c_maxTimeSteps==0)
      {
             JOptionPane.showMessageDialog(null, "The medel haven't any Timesteps or have only one Timesteps, Load a model to"
					  + " optimize first.");
            return;
      }
      
       Object[][] data;
       data = c_tableModel.getData();
       Float rate;
       int rowNumber = c_tableModel.getRowCount();
       int columnNumber = c_tableModel.getColumnCount();
       if(!c_periodField.getText().equals(""))
       {
       c_analysperiod = Long.parseLong( c_periodField.getText());//Integer.parseInt(textPeriod);/
       }
       else
       {
           JOptionPane.showMessageDialog(null, "Pleas write the length of analyses period ",
					"Empty period analayses", JOptionPane.WARNING_MESSAGE);   
       return;
       }   
    // Check if the analyses period is correct
       if(c_analysperiod > c_maxTimeSteps)
       {
           JOptionPane.showMessageDialog(null, "Please enter correct length of analyses period .\n "+"Analays period is larger than Timestep numbers",
					"Analays period larger than Timesteps", JOptionPane.WARNING_MESSAGE);
           return;
       }
       
    // Check if number of row is equal to the analyses period is correct
       if(rowNumber > c_analysperiod)
       {
           JOptionPane.showMessageDialog(null, "Nubmer of Rows are larger than Analays period\nCann't optimize",
					"Analays period smallar than number of rows", JOptionPane.WARNING_MESSAGE);
           return;
       }
    
       for(int i = 0;  i < rowNumber; i++)
        {
            for(int j = 0; j < columnNumber; j++)
            {
                System.out.print(c_table.getValueAt(i,j)+", ");
            }
            System.out.println("");
         }
        
      String tableDataControl =  tableDataControl(data);
      
      if(tableDataControl.equals("isOK"))
      {
       
       if(c_interestField.getText().equals(""))
        {
           JOptionPane.showMessageDialog(null, "Pleas write a mount of the annually interest ",
					"Empty Interest", JOptionPane.WARNING_MESSAGE);  
        return; 
        }
       
       try
        {
        rate = Float.parseFloat(c_interestField.getText());
        }
       catch(Exception e)
        {
          JOptionPane.showMessageDialog(null, "Please enter correct value of annual interest.\n "+"Decimal numbers writes with dot(.) not comma (,)",
					"Wrong format of the Annually interest", JOptionPane.WARNING_MESSAGE);
        return;
        }
       
       Long  analysperiod = Long.parseLong(c_periodField.getText());
       
       /*if(c_investment)
       {
         c_investmentCost.setDiscountedsystemcost(rate,analysperiod,c_tableModel.getTableHedar(),c_tableModel.getData(), c_timeStepValues);  
       }
       else*/
       c_gui.setDiscountedsystemcostControl(rate,analysperiod,c_tableModel.getTableHedar(),c_tableModel.getData(), c_timeStepValues);
      } // end if
      else
      {
         JOptionPane.showMessageDialog(null,tableDataControl);
        return; 
      }
      //CalculateTimestepLength(c_tableModel.getData(), c_tableModel.getRowCount() ,c_tableModel.getColumnCount());
      setVisible(false);
       dispose();
    }
  
  /**t
  *Button Add Row is pressd
  */ 
  
  private void btnAddRowActionPerformed()
  {
      int numberOfColumns = c_tableModel.getColumnCount();
      int numberOfRows = c_tableModel.getRowCount();
      
       //Check if the model is load it
      if(c_maxTimeSteps==0)
      {
            JOptionPane.showMessageDialog(null, "The medel haven't any Timesteps or have only one Timesteps, Load a model to"
					  + " optimize first.");
            return;
      }
      String textPeriod = c_periodField.getText();
      String rateString = c_interestField.getText();
       if(rateString.equals("") || rateString==null ||textPeriod == null || textPeriod.equals(""))
      {
        if(rateString.equals(""))
        JOptionPane.showMessageDialog(null, "Pleas write a mount of the annually interest ",
					"Empty Interest", JOptionPane.WARNING_MESSAGE);  
    
      if(textPeriod.equals("") || textPeriod == null)
      {
        JOptionPane.showMessageDialog(null, "Pleas write the length of analyses period ",
					"Empty period analayses", JOptionPane.WARNING_MESSAGE);  
      }
      }
      else
      {
      Object[][] data = new Object[numberOfRows+1][numberOfColumns];
      int yearNum = 1;
      // Add new Row           
      for (int i = 0; i < numberOfRows; i++) 
          {
          for (int j = 0; j < numberOfColumns; j++)
              {
              if(j == 0)
               data[i][j]="Year"+ yearNum++;
              else
               data[i][j] = c_tableModel.getValueAt(i, j);
              }
           }
         
      for(int i = 0; i < numberOfColumns;i++)
      {
           if(i == 0)
               data[numberOfRows][i]="Year"+ yearNum++;
           else
            data[numberOfRows][i]= "";
      }
    
     c_tableModel.setData(data);
     c_tableModel.fireTableDataChanged();
     c_tableModel.fireTableStructureChanged();
    
  }
  }
  
  
   /**
  *Button Delete Column is pressd
  */ 
  
private void btnDeleteColumnActionPerformed()
{
      int numberOfColumns = c_tableModel.getColumnCount();
      int numberOfRows = c_tableModel.getRowCount();
      //boolean accept = true;
      String textPeriod = c_periodField.getText();
      String rateString = c_interestField.getText();
       if(rateString.equals("") || rateString==null ||textPeriod == null || textPeriod.equals(""))
      {
        if(rateString.equals(""))
        JOptionPane.showMessageDialog(null, "Pleas write a mount of the annually interest ",
					"Empty Interest", JOptionPane.WARNING_MESSAGE);  
    
      if(textPeriod.equals("") || textPeriod == null)
      {
        JOptionPane.showMessageDialog(null, "Pleas write the length of analyses period ",
					"Empty period analayses", JOptionPane.WARNING_MESSAGE);  
      }
      }
      else
      {
      
      c_analysperiod = Long.parseLong(textPeriod);//Integer.parseInt(textPeriod);/
      
      // Check number of columns
      if(numberOfColumns > 2)
       {
        Object[][] data = new Object[numberOfRows][numberOfColumns-1];
      
     	// check if last column is empty
    	for ( int row = 0; row < numberOfRows; row++ ) 
        {
    		String str = (String) c_tableModel.getValueAt(row, numberOfColumns-1);//table.getModel().getValueAt(row, lastCol);
    		if (!(str == null || str.equals("")))
                {
    			int sel = JOptionPane.showConfirmDialog(null, "You are trying to " +"delete a column with contents. " +
    							"Data will be lost.\nDo you want to " +"continue?",
    						"Warning", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
    			if (sel == JOptionPane.YES_OPTION)
                            break;
                        else
                            return;
    		}
    	} // END FOR

        // Delete column
       
        for (int i = 0; i < numberOfRows; i++) 
                {
                for (int j = 0; j < numberOfColumns-1; j++)
                    {
                    data[i][j] = c_tableModel.getValueAt(i, j);
                    }
                }
              
      
        int stringLength = c_tableModel.getTableHedar().length-1;
     
        String tempString[] = c_tableModel.getTableHedar();
        String tableHeder[]= new String [stringLength];
        for(int i = 0; i < stringLength; i++)
            {
            tableHeder[i]= tempString[i];
            }
     
        // Update the table
        c_tableModel.setColumnName(tableHeder);
        c_tableModel.setData(data);
        c_tableModel.fireTableDataChanged();
        c_tableModel.fireTableStructureChanged();
      } // END if(numberOfColumns > 2)
     }// END ELSE
}
 
/**
  *Button Delete is pressd
  */ 
  
private void btnDeleteRowActionPerformed()
{
      int numberOfColumns = c_tableModel.getColumnCount();
      int numberOfRows = c_tableModel.getRowCount();
      //boolean accept = true;
      String textPeriod = c_periodField.getText();
      String rateString = c_interestField.getText();
       if(rateString.equals("") || rateString==null ||textPeriod == null || textPeriod.equals(""))
      {
        if(rateString.equals(""))
        JOptionPane.showMessageDialog(null, "Pleas write a mount of the annually interest ",
					"Empty Interest", JOptionPane.WARNING_MESSAGE);  
    
      if(textPeriod.equals("") || textPeriod == null)
      {
        JOptionPane.showMessageDialog(null, "Pleas write the length of analyses period ",
					"Empty period analayses", JOptionPane.WARNING_MESSAGE);  
      }
      }
      else
      {
      
      c_analysperiod = Long.parseLong(textPeriod);//Integer.parseInt(textPeriod);/
      
      // Check number of columns
      if(numberOfRows > 1)
       {
          //if(numberOfRows == 1)
            //  return;
        Object[][] data = new Object[numberOfRows-1][numberOfColumns];
      
     	// check if last Row is empty
    	for ( int col = 1; col < numberOfColumns; col++ ) 
        {
    		String str = (String) c_tableModel.getValueAt(numberOfRows-1, col);//table.getModel().getValueAt(row, lastCol);
    		if (!(str == null || str.equals("")))
                {
    			int sel = JOptionPane.showConfirmDialog(null, "You are trying to " +"delete a row with contents. " +
    							"Data will be lost.\nDo you want to " +"continue?",
    						"Warning", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
    			if (sel == JOptionPane.YES_OPTION)
                            break;
                        else
                            return;
    		}
    	} // END FOR

        // Delete Row
       
        for (int i = 0; i < numberOfRows-1; i++) 
                {
                for (int j = 0; j < numberOfColumns; j++)
                    {
                    data[i][j] = c_tableModel.getValueAt(i, j);
                    }
                }
              
      
        
        // Update the table
        //c_tableModel.setColumnName(tableHeder);
        c_tableModel.setData(data);
        c_tableModel.fireTableDataChanged();
        c_tableModel.fireTableStructureChanged();
      } // END if(numberOfColumns > 2)
     }// END ELSE
}
   /** This method is called the user clicks or presses the  Manually button
     * or the model have not binge constructed yet;
  */
  
  private void setDefaultTable()
  {
    String[] colNames = {"Year", "Timestep nr."};
    Object[][] data = { { "Year1", ""},{ "Year2",""}, { "Year3", ""},{"Year4",""},{"Year5", ""},{"Year6", ""},};
    c_tableModel.setColumnName(colNames);
    c_tableModel.setData(data);
    c_tableModel.fireTableStructureChanged(); 
  }
  
  
   /** This method is called from within the constructor to 
    * Update the table.
    * All table data gets from the Model
     */ 
  
  private void updateTable()
  {
      /*if(c_investment)
      {
          c_tableModel.setColumnName(c_investmentCost.getTableHedar());
          c_tableModel.setData(c_investmentCost.getTableData());
          c_interestField.setText(c_investmentCost.getRate().toString());
          c_periodField.setText(c_investmentCost.getAnalysPeriod().toString());
          c_tableModel.fireTableDataChanged();
          c_tableModel.fireTableStructureChanged();
      }
      else
      {*/
        c_discountedsystemcostControl=c_gui.getDiscountedsystemcostControl();
        c_tableModel.setColumnName(c_discountedsystemcostControl.getTableHedar());
        c_tableModel.setData(c_discountedsystemcostControl.getTableData());
        c_interestField.setText(c_discountedsystemcostControl.getRate().toString());
        c_periodField.setText(c_discountedsystemcostControl.getAnalysPeriod().toString());
        c_tableModel.fireTableDataChanged();
        c_tableModel.fireTableStructureChanged();
      //}
      
  }
 
}
