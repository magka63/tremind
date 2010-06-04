/*
 * Copyright 2002
 * Urban Liljedahl <ul@sm.luth.se>
 *
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

import java.awt.*;
import javax.swing.*;
import java.util.Vector;
import java.util.Iterator;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import mind.gui.*;
import mind.model.*;
import mind.model.function.*;
import mind.EventHandlerClient;
import javax.swing.table.AbstractTableModel;
import java.awt.event.*;

/*
 * BoundaryTOPDialog.java
 * Created as a first approach to the problem of connecting
 * different timesteps at TOP level.
 *
 * @author Urban Liljedahl
 * @version 2002-09-18
 */

public class BoundaryTOPDialog extends mind.gui.dialog.FunctionDialog {
    private ID c_nodeID;
    private Vector c_resources;
    private GUI c_gui;
    private BoundaryTOP c_function;
    private EventHandlerClient c_eventhandler;
    private NodeControl c_nodeControl;
    private String c_currentTimestep = new String("");
    private int c_maxTimeSteps = 1;


    /** Creates new form */
    public BoundaryTOPDialog(javax.swing.JDialog parent, boolean modal, ID nodeID,
			NodeFunction function, GUI gui) {
	super (parent, modal);
	c_nodeID = nodeID;
	c_gui = gui;
	c_function = (BoundaryTOP) function;
	c_eventhandler = c_gui.getEventHandlerClient();
    //c_nodeControl.getTimesteplevel(c_nodeID);
    //Timesteplevel tsl = gui.getTopTimesteplevel();
    NodeControl nodeControl = c_gui.getAllNodes();
    Timesteplevel tsl = nodeControl.getTimesteplevel(c_nodeID);
    c_currentTimestep  = tsl.getLabel();
    Timesteplevel tstl = gui.getTopTimesteplevel();

	if(tstl.getNextLevel() == null)
            c_maxTimeSteps = 0;

        while ((tstl = tstl.getNextLevel()) != null)
            {
             //if(tstl.getNextLevel() == null)
             //{
               // c_timeStepValues = tsl.getLengthsVector();
             //}
            c_maxTimeSteps *= tstl.getTimesteps();
            }

	initComponents();
	updateResources();
	pack();pack(); // Twice as good!!!
	//setVisible( true );
    }


    private void initComponents(){
	lblDescription = new javax.swing.JLabel ();
	sep1 = new javax.swing.JSeparator ();
	sep2 = new javax.swing.JSeparator ();
	lblMaximumUnit = new javax.swing.JLabel ();
    if(c_currentTimestep.equals("TOP"))
    {
	chkMaximum = new javax.swing.JCheckBox("Maximum");
	chkMaximum.setSelected( c_function.getIsMaximum() );
	txtMaximum = new PositiveNumberField();
    if(c_function.getTableData()== null)
        {
        if( c_function.getIsMaximum() ){
            txtMaximum.setText( new Float(c_function.getMaximum()).toString() );
            }
        else
            txtMaximum.setText( "" );
        }
	else
        {
        // Alrady used table data but the curren timestep is TOP and user chose max
        //if(c_function.getMaximum() != 0)
          //  {
            //txtMaximum.setText( new Float(c_function.getMaximum()).toString() );
            //}
        //else
          //  {
            txtMaximum.setText(c_function.getTabelsMaximum());
            if(txtMaximum.getFloatValue()!=0)
              {
              txtMaximum.setText(c_function.getTabelsMaximum());
              c_function.setMaximum( txtMaximum.getFloatValue() );
              chkMaximum.setSelected(true);
              c_function.setIsMaximum(true);
              }
            else
              {
              txtMaximum.setText("");
              chkMaximum.setSelected(false);
              c_function.setIsMaximum(false);
              }
            //} // END ELSE
    }
	lblMinimumUnit = new javax.swing.JLabel ();
	chkMinimum = new javax.swing.JCheckBox("Minimum");
	chkMinimum.setSelected( c_function.getIsMinimum() );
	txtMinimum = new PositiveNumberField();
	if(c_function.getTableData()== null)
        {
        if( c_function.getIsMinimum() ){
            txtMinimum.setText( new Float(c_function.getMinimum()).toString() );
            }
        else
             txtMinimum.setText( "0.0" );
        }
	else
        {
        // Alrady used table data but the curren timestep is TOP and user chose min
        //if(c_function.getMinimum() != 0)
          //  {
            //txtMinimum.setText( new Float(c_function.getMinimum()).toString() );
            //}
        //else
          //  {
            txtMinimum.setText(c_function.getTabelsMinimum());
            if(txtMinimum.getFloatValue()!=0)
              {
              txtMinimum.setText(c_function.getTabelsMinimum() );
              chkMinimum.setSelected(true);
              c_function.setIsMinimum(true);
              c_function.setMinimum( txtMinimum.getFloatValue() );
              }
            else
              {
              chkMinimum.setSelected(false);
              c_function.setIsMinimum(false);
              txtMinimum.setText("");
              }
            //}// END ELSE
         }
        
    pnlBoundaries = new javax.swing.JPanel();
    }
	pnlLabel = new javax.swing.JPanel ();
	lblLabel = new javax.swing.JLabel ();
	txtLabel = new javax.swing.JTextField ();
	//pnlBoundaries = new javax.swing.JPanel();
	sep3 = new javax.swing.JSeparator ();
	pnlButtons = new javax.swing.JPanel ();
	btnOk = new javax.swing.JButton ();
	btnCancel = new javax.swing.JButton ();
	pnlResource = new javax.swing.JPanel ();
	lblTypeResource = new javax.swing.JLabel ();
	listResource = new javax.swing.JList ();
	btnNewResource = new javax.swing.JButton ();
	getContentPane ().setLayout (new java.awt.GridBagLayout ());
	java.awt.GridBagConstraints gridBagConstraints1;
	setName ("BoundaryTOP");
	addWindowListener (new java.awt.event.WindowAdapter () {
		public void windowClosing (java.awt.event.WindowEvent evt) {
		    closeDialog (evt);//call within the Comes app.
		}
	    }
			   );

	lblDescription.setText ("Description: Defines a boundaryTOP for a " +
				"resource.");

	gridBagConstraints1 = new java.awt.GridBagConstraints ();
	gridBagConstraints1.gridwidth = 2;
	gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints1.insets = new java.awt.Insets (10, 10, 10, 10);
	gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
	getContentPane ().add (lblDescription, gridBagConstraints1);

	gridBagConstraints1 = new java.awt.GridBagConstraints ();
	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridwidth = 2;
	gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
	getContentPane ().add (sep1, gridBagConstraints1);

	pnlLabel.setLayout (new java.awt.GridBagLayout ());
	lblLabel.setText ("Label");

	GridBagConstraints gridBagConstraints5 = new GridBagConstraints ();
	//gridBagConstraints5.insets = new java.awt.Insets (10, 10, 10, 10);
	gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
	pnlLabel.add (lblLabel, gridBagConstraints5);

	txtLabel.setText(c_function.getLabel());
	gridBagConstraints5 = new java.awt.GridBagConstraints ();
	gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints5.insets = new java.awt.Insets (0, 10, 0, 0);
	gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
	gridBagConstraints5.weightx = 1.0;
	pnlLabel.add (txtLabel, gridBagConstraints5);

	gridBagConstraints1 = new java.awt.GridBagConstraints ();
	gridBagConstraints1.insets = new java.awt.Insets (10, 10, 10, 10);
	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridy = 2;
	gridBagConstraints1.gridwidth = 2;
	gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints1.insets = new java.awt.Insets (10, 10, 10, 10);
	gridBagConstraints1.weightx = 1.0;
	getContentPane ().add (pnlLabel, gridBagConstraints1);

	gridBagConstraints1 = new java.awt.GridBagConstraints ();
	gridBagConstraints1.gridy = 3;
	gridBagConstraints1.gridwidth = 2;
	gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
	getContentPane ().add (sep2, gridBagConstraints1);


	//Timestep control


	pnlResource.setLayout (new java.awt.GridBagLayout ());
	java.awt.GridBagConstraints gridBagConstraints4;
	lblTypeResource.setText ("Type of resource:");

	gridBagConstraints4 = new java.awt.GridBagConstraints ();
	//      gridBagConstraints4.insets = new java.awt.Insets (10, 10, 0, 10);
	gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
	gridBagConstraints4.anchor = GridBagConstraints.CENTER;
	pnlResource.add (lblTypeResource, gridBagConstraints4);
         // Added by Nawzad Mardan 080910 
      // Add in & out radio button
      pnlRadbut = new JPanel();
      ButtonGroup group = new ButtonGroup();
      group.add(c_radIn);
      group.add(c_radOut);
      pnlRadbut.add(c_radIn);
      pnlRadbut.add(c_radOut);
      if (c_function.isRadOut())
	    c_radOut.setSelected(true);
	else
	    c_radIn.setSelected(true);
      java.awt.GridBagConstraints gridBagConstraints44;
      gridBagConstraints44 = new java.awt.GridBagConstraints ();
      gridBagConstraints44.anchor = java.awt.GridBagConstraints.WEST;
      //gridBagConstraints44.weightx = 1.0;
      gridBagConstraints44.gridx = 0;
     pnlResource.add (pnlRadbut, gridBagConstraints44);

	//      listResource.setPreferredSize (new java.awt.Dimension(10, 50));
	listResource.addListSelectionListener(new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent e)
		{
		    newResourceSelected();
		}
	    });

	gridBagConstraints4 = new java.awt.GridBagConstraints ();
	gridBagConstraints4.gridx = 0;
    gridBagConstraints1 = new java.awt.GridBagConstraints ();
	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridy = 6;
	//      gridBagConstraints4.insets = new java.awt.Insets (4, 10, 9, 10);

	JScrollPane scrollPane = new JScrollPane(listResource);
    if(c_currentTimestep.equals("TOP"))
        {
        scrollPane.setPreferredSize(new Dimension(150, 100));
        gridBagConstraints1.insets = new java.awt.Insets (10, 10, 10, 5);
        }
    else
        {
        scrollPane.setPreferredSize(new Dimension(150, 100));
        //(int top, int left, int bottom, int right)
        gridBagConstraints1.insets = new java.awt.Insets (5, 5, 5, 50);
        }
	pnlResource.add (scrollPane, gridBagConstraints4);
    
	getContentPane ().add (pnlResource, gridBagConstraints1);
    if(c_currentTimestep.equals("TOP"))
       {
        pnlBoundaries.setLayout(new GridBagLayout());
        pnlBoundaries.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.gridwidth = 3;
        gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.anchor = GridBagConstraints.WEST;
        pnlBoundaries.add(chkMinimum, gridBagConstraints1);

        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 1;
        pnlBoundaries.add(Box.createHorizontalStrut(10), gridBagConstraints1);

    	txtMinimum.setColumns(20);
        gridBagConstraints1.gridx = 1;
    	gridBagConstraints1.gridy = 1;
    	pnlBoundaries.add(txtMinimum, gridBagConstraints1);

    	gridBagConstraints1.gridx = 2;
    	gridBagConstraints1.gridy = 1;
    	pnlBoundaries.add(lblMinimumUnit);

        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 2;
        gridBagConstraints1.gridwidth = 3;
        pnlBoundaries.add(Box.createHorizontalStrut(10), gridBagConstraints1);

        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.gridwidth = 3;
        gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.anchor = GridBagConstraints.WEST;
        pnlBoundaries.add(chkMaximum, gridBagConstraints1);

        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 4;
        pnlBoundaries.add(Box.createHorizontalStrut(10), gridBagConstraints1);

    	txtMaximum.setColumns(20);
    	gridBagConstraints1.gridx = 1;
    	gridBagConstraints1.gridy = 4;
    	pnlBoundaries.add(txtMaximum, gridBagConstraints1);

    	gridBagConstraints1.gridx = 1;
    	gridBagConstraints1.gridy = 4;
    	pnlBoundaries.add(lblMaximumUnit);

        gridBagConstraints1 = new java.awt.GridBagConstraints ();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 6;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.insets = new java.awt.Insets (10, 5, 10, 10);
        getContentPane ().add (pnlBoundaries, gridBagConstraints1);
        }

	gridBagConstraints1 = new java.awt.GridBagConstraints ();
	gridBagConstraints1.gridy = 7;
	gridBagConstraints1.gridwidth = 2;
	gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
	getContentPane ().add (sep3, gridBagConstraints1);
// The table
    if(!c_currentTimestep.equals("TOP"))
        {
        c_tableModel = new MyTableModel();
        c_table = new JTable(c_tableModel);
        c_table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        c_table.getTableHeader().setForeground(Color.BLUE);
        c_table.setFont(new Font("SansSerif", Font.BOLD, 12));
        //c_table.setBackground(Color.BLUE);
        c_table.setForeground(Color.BLUE);
        /* IF the RMD FILE IS AN OLD FILE AND ALRADY CONTAIN INFORMATION
        if(c_function.getIsMaximum())
        {
        c_tableModel.setTablsDataValue(0,3, c_function.getMaximum());
        c_tableModel.setTablsDataValue(0,0, 1);
        c_tableModel.setTablsDataValue(0,2, 1);
        }*/
        c_table.setRowSelectionAllowed(false);
        c_table.setColumnSelectionAllowed(false);
        c_table.setPreferredScrollableViewportSize(new Dimension(400, 100));
        c_scrollPane = new JScrollPane(c_table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollBar jb = new JScrollBar();
        c_scrollPane.setHorizontalScrollBar(jb);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 8;
        gridBagConstraints1.gridwidth = 2;
        gridBagConstraints1.insets = new java.awt.Insets (5, 5, 5, 5);
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        this.getContentPane().add(c_scrollPane, gridBagConstraints1);

        btnAddRow = new JButton("Add Row");
        btnDeleteRow = new JButton("Delete Row");
        btnAddRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnAddRowActionPerformed();
			}
		});

        btnDeleteRow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnDeleteRowActionPerformed();
			}
		});
        JPanel showPanel = new JPanel();

        showPanel.add(btnAddRow);
        showPanel.add(btnDeleteRow);
        gridBagConstraints1 = new java.awt.GridBagConstraints();
    //gridBagConstraints1.gridwidth = 1;
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(showPanel, gridBagConstraints1);
        updateTable();
        }

//pnlTable.
	btnOk.setText ("OK");
	getRootPane().setDefaultButton(btnOk);
	btnOk.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    btnOkActionPerformed (evt);
		}
	    });

	pnlButtons.add (btnOk);

	btnCancel.setText ("Cancel");
	btnCancel.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    btnCancelActionPerformed (evt);
		}
	    });

	pnlButtons.add (btnCancel);

	gridBagConstraints1 = new java.awt.GridBagConstraints ();
	gridBagConstraints1.gridx = 0;
	gridBagConstraints1.gridy = 9;
	gridBagConstraints1.gridwidth = 2;
	gridBagConstraints1.insets = new java.awt.Insets (10, 10, 10, 10);
	gridBagConstraints1.anchor = java.awt.GridBagConstraints.EAST;
	getContentPane ().add (pnlButtons, gridBagConstraints1);

    }

    private void btnCancelActionPerformed (java.awt.event.ActionEvent evt) {
	setVisible(false);
	dispose();
    }

    private void btnOkActionPerformed (java.awt.event.ActionEvent evt)
    {
	// save the function label
	c_function.setLabel(txtLabel.getText());
    // Added by Nawzad Mardan 080910
    c_function.setRadIn(c_radIn.isSelected());
    // Added by Nawzad Mardan 080910
    c_function.setRadOut(c_radOut.isSelected());

	Resource resource = (Resource)listResource.getSelectedValue();
	if (resource != null)
	    c_function.setResource(resource.getID());
    else
    {
         JOptionPane.showMessageDialog(null, "Resource for Source function. not specified.\n\n"+
					 "Can not optimize.","Input error", JOptionPane.WARNING_MESSAGE);
                    return;
    }
    if(c_currentTimestep.equals("TOP"))
      {
      //save boundary values
      if( chkMinimum.isSelected() ){
	    c_function.setMinimum( txtMinimum.getFloatValue() );
	    c_function.setIsMinimum( true );
       }
      if(!chkMinimum.isSelected() ){
	    c_function.setMinimum(0);
	    c_function.setIsMinimum( false );
       }
      if( chkMaximum.isSelected() ){
	    c_function.setMaximum( txtMaximum.getFloatValue() );
	    c_function.setIsMaximum( true );
        }
      if(!chkMaximum.isSelected() ){
	    //c_function.setMaximum(0);
	    c_function.setIsMaximum( false );
        }
      }
    else
      {
      // Check table data
      String check = checkTableData(c_tableModel.getData()) ;

      if(!check.equals("OK"))
        {
          JOptionPane.showMessageDialog(null, check+"\nCan not optimize.","Input error", JOptionPane.WARNING_MESSAGE);
         return;
        }
      c_function.setTableData(c_tableModel.getData());
      c_function.setCurrentTimestep(c_currentTimestep);
      }


	// close the dialog
	closeDialog(null);
    }

    private void newResourceSelected()
    {
	Resource resource = (Resource)listResource.getSelectedValue();
    }

    private void closeDialog(java.awt.event.WindowEvent evt) {
	setVisible (false);
	dispose ();
    }

    private void updateResources()
    {
	c_resources = c_gui.getResources();

	listResource.setListData(c_resources);
	if (c_function.getResource() != null)
	    listResource.setSelectedIndex(c_resources.indexOf(c_gui.getResource(c_function.getResource())));
    }

    // Variables declaration
    private javax.swing.JLabel lblDescription;
    private javax.swing.JSeparator sep1;
    private javax.swing.JSeparator sep2;
    //private javax.swing.JPanel pnlTimestep;
    //private javax.swing.JLabel lblTSL[];
    //private SpinButton spinTSL[];
    private PositiveNumberField txtMaximum;
    private PositiveNumberField txtMinimum;
    private javax.swing.JLabel lblMaximumUnit;
    private javax.swing.JCheckBox chkMaximum;
    private javax.swing.JLabel lblMinimumUnit;
    private javax.swing.JCheckBox chkMinimum;
    private javax.swing.JPanel pnlBoundaries;
    private javax.swing.JPanel pnlCopyPaste;
    private javax.swing.JButton btnCopyFrom;
    //private javax.swing.JButton btnCopy;
    //private javax.swing.JButton btnPasteTo;
    //private javax.swing.JButton btnPaste;
    //private javax.swing.JLabel lblTimestep;
    private javax.swing.JPanel pnlLabel;
    private javax.swing.JLabel lblLabel;
    private javax.swing.JTextField txtLabel;
    private javax.swing.JSeparator sep3;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnCancel;
    private javax.swing.JPanel pnlResource;
    private javax.swing.JLabel lblTypeResource;
    private javax.swing.JList listResource;
    private javax.swing.JButton btnNewResource;
    // Added by Nawzad Mardan 080910
    private JRadioButton c_radIn = new JRadioButton("In");
    private JRadioButton c_radOut = new JRadioButton("Out");
    private javax.swing.JPanel pnlRadbut;
    // 20100121
    private JScrollPane c_scrollPane;
    private JTable c_table;
    // Table to insert timestep numbers
    private MyTableModel c_tableModel;
    private JButton btnAddRow;
    private JButton btnDeleteRow;
    // End of variables declaration

    //test
    private int getNumberOfTimesteps(){
	//calculate number of timesteps in this node
	//call to static method in class GUI referring to latest created instance of gui

	Timesteplevel thisTsl = c_gui.getTimesteplevel(c_nodeID);
	Timesteplevel topTsl = thisTsl.getTopLevel();
	Timesteplevel curTsl = thisTsl.getPrevLevel();
	int res = thisTsl.getTimesteps();
	while(curTsl != topTsl && curTsl != null){
	    res *= curTsl.getTimesteps();
	    curTsl = curTsl.getPrevLevel();
	    System.out.println("res: " + res);
	}
	System.out.println("The timestep level of node containing this function: " + thisTsl);
	System.out.println("Resolution: " + res);
	return res;
    }
    //predict variable names in mps file
    private void predictNames(){
	int res = getNumberOfTimesteps();
	//FxT1, FxT2, ... , FxT(res)
	ID resource = c_function.getResource();
	Flow[] inflow = c_gui.getInFlows(c_nodeID);
	//filter flows. Only flows of current resource of interest
	inflow = filter(inflow, resource);
	//build all names
	System.out.println("Prediction of names*****************************");
	for(int tstep=1; tstep<=res; tstep++){
	    for(int i=0; i<inflow.length; i++){
		//build variable with flowid + T + tstep
		String var = inflow[i].getID() + "T" + tstep;
		System.out.println(var);
	    }
	}
    }
    private Flow[] filter(Flow[] inflow, ID resource){
	Vector newFlows = new Vector();
	for(int i=0; i<inflow.length; i++){
	    //test a match
	    if( inflow[i].getResource()==resource){
		newFlows.addElement(inflow[i]);
	    }
	}
	Flow[] newFlow = new Flow[newFlows.size()];
	Iterator list = newFlows.iterator();
	int index = 0;
	while( list.hasNext()){
	    newFlow[index]=(Flow)list.next();
	    index++;
	}
	return newFlow;
    }
    
    
    /**
     * MyTableModel is inner klass that hold its data in an array and kan  get the data from an outside source such as a database. 
     * MyTableModel is simple, can easily determine the data's type, helping the JTable display the data in the best format.
     * MyTableModel let you edit the name columns; it does, however, let you edit the other columns.  
*/

   private class MyTableModel extends AbstractTableModel
    {
    private String[] columnNames = {"Start Timestep", "End Timestep", "Minimum", "Maximum"};
    private Object[][] data = { { "TOP ", "TOP", "", ""}};

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

    public void setTablsDataValue(int row, int col, float value)
    {
        data[row][col] = value;
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
       // System.out.println("ROW : "+row+"   COLUM : "+col);
    if (col>=0)
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

private void btnAddRowActionPerformed()
  {
  int numberOfColumns = c_tableModel.getColumnCount();
  int numberOfRows = c_tableModel.getRowCount();
  Object[][] data = new Object[numberOfRows+1][numberOfColumns];
 for (int i = 0; i < numberOfRows; i++)
          {
          for (int j = 0; j < numberOfColumns; j++)
              {
               data[i][j] = c_tableModel.getValueAt(i, j);
              }
           }

      for(int i = 0; i < numberOfColumns;i++)
      {
       data[numberOfRows][i]= "";
      }

     c_tableModel.setData(data);
     c_tableModel.fireTableDataChanged();
     c_tableModel.fireTableStructureChanged();
}

/**
  *Button Delete is pressd
  */

private void btnDeleteRowActionPerformed()
{
      int numberOfColumns = c_tableModel.getColumnCount();
      int numberOfRows = c_tableModel.getRowCount();
      //boolean accept = true;

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

        // Copy Row //Delete Row

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
}
private void updateTable()
  {
     
   if(c_function.getTableData()== null)
       {
       String startTimestep = "TOP";
       String endTimestep = "TOP";
       String maximumValue = "";
       String minimumValue = "";
       if( c_function.getIsMaximum() )
           {
           maximumValue = new Float(c_function.getMaximum()).toString();
           startTimestep = "1";
           endTimestep = new Integer(c_maxTimeSteps).toString();
           }
       else
           {
           maximumValue = "";
           }
       if( c_function.getIsMinimum() )
           {
           minimumValue= new Float(c_function.getMinimum()).toString() ;
           startTimestep = "1";
           endTimestep = new Integer(c_maxTimeSteps).toString();
           }
       else
           {
           minimumValue = "";
           }
        
       if((endTimestep.equals("TOP")) && (startTimestep.equals("TOP")))
       {
         if(c_maxTimeSteps>1)
           {
           startTimestep = "1";
           endTimestep = new Integer(c_maxTimeSteps).toString();
           }

       }
       Object[][] temp_data = { { startTimestep, endTimestep, minimumValue, maximumValue}}; 
       c_tableModel.setData(temp_data);
       c_function.setTableData(temp_data);
       c_tableModel.fireTableDataChanged();
       c_tableModel.fireTableStructureChanged();        
       }
   else
      {
       // Alrady have table data but user changes the minimum och maximum in Top timestep
       if(c_function.getTableData().length == 1)
       {
       String startTimestep = "TOP";
       String endTimestep = "TOP";
       String maximumValue = "";
       String minimumValue = "";
       if((c_function.getIsMaximum()) || (c_function.getIsMinimum()))
       {
       if( c_function.getIsMaximum() )
           {
           maximumValue = new Float(c_function.getMaximum()).toString();
           startTimestep = "1";
           endTimestep = new Integer(c_maxTimeSteps).toString();
           }
       else
           {
           maximumValue = "";
           }
       if( c_function.getIsMinimum() )
           {
           minimumValue= new Float(c_function.getMinimum()).toString() ;
           startTimestep = "1";
           endTimestep = new Integer(c_maxTimeSteps).toString();
           }
       else
           {
           minimumValue = "";
           }
       Object[][] temp_data = { { startTimestep, endTimestep, minimumValue, maximumValue}};
       c_tableModel.setData(temp_data);
       c_function.setTableData(temp_data);
       c_tableModel.fireTableDataChanged();
       c_tableModel.fireTableStructureChanged();
       }
      else
        {
        c_tableModel.setData(c_function.getTableData());
        c_tableModel.fireTableDataChanged();
        c_tableModel.fireTableStructureChanged();
       }
        }
       else
       {
       c_tableModel.setData(c_function.getTableData());
       c_tableModel.fireTableDataChanged();
       c_tableModel.fireTableStructureChanged();
       }

      }
        
      //}
      
  }
// Added by Nawzad Mardan 20100222
// Check table data return Ok or an error massage
private String checkTableData(Object [][]data)
{
 String check = "OK";
 String sts1,sts2;
 int startTimestep,endTimestep;
 float min,max;
if(data != null)
    {
    for ( int raw = 0; raw < data.length; raw++ )
        {
        sts1= (String)data[raw][0];
        sts2= (String)data[raw][1];
        if(!sts1.equals("") && !sts2.equals("") )
            {
            startTimestep = new Integer(sts1).intValue();
            endTimestep = new Integer(sts2).intValue();
            if(startTimestep > endTimestep)
                {
                check = "Start time step should be equal or less than end time step in BoundaryTop function's table data";
                break;
                }
            if(startTimestep > c_maxTimeSteps)
                {
                check = "Start time step value should be equal or less than "+c_maxTimeSteps+" in BoundaryTop function's table data";
                break;
                }
            if(endTimestep > c_maxTimeSteps)
                {
                check = "End time step value should be equal or less than "+c_maxTimeSteps+" in BoundaryTop function's table data";
                break;
                }
            }
        sts1= (String)data[raw][2];
        sts2= (String)data[raw][3];
         if(!sts1.equals("") && !sts2.equals("") )
            {
            min = new Float(sts1).floatValue();
            max = new Float(sts2).floatValue();
            if(min > max)
                {
                check = "The minumum value should be equal or less than the maximum value in BoundaryTop function's table data";
                break;
                }
            }

        }
    }
 return check;
}
}
