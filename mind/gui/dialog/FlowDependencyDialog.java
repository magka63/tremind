/*
 * Copyright 2001:
 * Peter Andersson <petan117@student.liu.se>
 * Martin Hagman <marha189@student.liu.se>
 * Henrik Norin <henno776@student.liu.se>
 * Anna Stjerneby <annst566@student.liu.se>
 * Tim Terlegård <timte878@student.liu.se>
 * Johan Trygg <johtr599@student.liu.se>
 * Peter Åstrand <petas096@student.liu.se>
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
 * Copyright 2010
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.*;

import mind.gui.*;
import mind.model.*;
import mind.model.function.*;
import mind.io.Ini;

/*
 * BoundaryDialog.java
 *
 * Created on den 25 april 2001, 13:02
 */

/**
 *
 * @author Tim Terlegård
 * @author  Per Fredriksson
 * @author 	Tor Knutsson
 * @version 2007-12-10
 */

public class FlowDependencyDialog
    extends mind.gui.dialog.FunctionDialog
{
    private Ini inifile = new Ini();
    private float c_largeNumber;

    private ID c_nodeID;
    private ID c_resourceX;
    private ID c_resourceY;
    private Vector c_resources;
    private GUI c_gui;
    private FlowDependency c_function;
    private int c_nrLimits = 1;
    private Timesteplevel tsl[];
    private int c_timestep; //Active timestep
    private int c_timesteplevels; //Number of timesteplevels
    private Vector c_timestepInfo = new Vector(0);

    private JButton c_btnAdd = new JButton("Add");
    private JButton c_btnRemove = new JButton("Remove");
    private JPanel c_pnlLimits = new JPanel();
    private JRadioButton c_radXIn = new JRadioButton("In");
    private JRadioButton c_radXOut = new JRadioButton("Out");
    private JRadioButton c_radYIn = new JRadioButton("In");
    private JRadioButton c_radYOut = new JRadioButton("Out");
    private JScrollPane c_scrollEquations;

    private final String UPPER_INVALID_TITLE =
	"Invalid upper value(s)";
    private final String UPPER_INVALID_MESSAGE =
	"The upper values specified are incorrect,\n" +
	"therefore these equations could not be saved.\n" +
	"The upper limit must be bigger than the lower limit.";

    // Added by Nawzad Mardan 20100319
    private int c_maxTimeSteps = 1;
    private String c_currentTimestep;

    class TimestepInfo
    {
	// Vector of Integers saying what limits the y=k*x+m holds
	// upperlimit = -1 means it holds to x=infinity
	public Vector c_upperLimit = new Vector(0);

	// m in Y=kX+m
	public Vector c_offset = new Vector(0);
	// The slope, k in Y=kX+m.
	public Vector c_slope = new Vector(0);

	public boolean add()
	{
	    int size = getSize();
	    boolean canAdd = false;

	    if (size == 0)
		canAdd = true;
	    else if (size == 1) {
		if (getUpperLimit(size).getFloatValue() > 0)
		    canAdd = true;
	    }
	    else if (getUpperLimit(size).getFloatValue() >
		     getUpperLimit(size-1).getFloatValue())
		canAdd = true;

	    if (canAdd) {
              c_upperLimit.addElement(new PositiveNumberField(c_largeNumber, 9));
              c_offset.addElement(new NumberField(0, 8));
              c_slope.addElement(new NumberField(1, 8));
	    }

	    return canAdd;
	}

	public void add(Component upper, Component slope, Component offset)
	{
	    c_upperLimit.addElement(upper);
	    c_slope.addElement(slope);
	    c_offset.addElement(offset);
	}

	public void clear()
	{
	    c_upperLimit.removeAllElements();
	    c_offset.removeAllElements();
	    c_slope.removeAllElements();
	}

	public float getLowerLimit(int i)
	{
	    if (i <= 1 || c_upperLimit.size() < 2)
		return 0;
	    else
		return getUpperLimit(i-1).getFloatValue();
	}

	public NumberField getOffset(int i)
	{
	    if (i <= getSize())
		return (NumberField) c_offset.get(i-1);
	    else
		return null;
	}

	// we can choose any of the vectors to check the size
	public int getSize()
	{
	    return c_upperLimit.size();
	}

	public NumberField getSlope(int i)
	{
          if (i <= getSize())
            return (NumberField) c_slope.get(i-1);
          else
            return null;
	}

	public PositiveNumberField getUpperLimit(int i)
	{
	    if (i > 0 && i <= getSize())
		return (PositiveNumberField) c_upperLimit.get(i-1);
	    else
		return null;
	}

	public void remove()
	{
	    if (getSize() > 0) {
		c_upperLimit.removeElementAt(c_upperLimit.size()-1);
		c_offset.removeElementAt(c_offset.size()-1);
		c_slope.removeElementAt(c_slope.size()-1);
	    }
	}
	/*
	public void set(Component upper, Component slope, Component offset)
	{
	    c_upperLimit.setElementAt(upper, c_timestep - 1);
	    c_slope.setElementAt(slope, c_timestep - 1);
	    c_offset.setElementAt(offset, c_timestep - 1);
	}
	*/
    }

    /** Creates new form SourceDialog */
    public FlowDependencyDialog(JDialog parent, boolean modal, ID nodeID,
				NodeFunction function, GUI gui) {
	super (parent, modal);

        c_largeNumber = Float.parseFloat(inifile.getProperty(UserSettingConstants.MPS_INFINITY_DEFINITIION, "1E6"));

	c_nodeID = nodeID;
	c_gui = gui;
	c_function = (FlowDependency) function;
	c_resources = c_gui.getResources();

	//calculate number of timestepslevels in the function
	c_timesteplevels = 1;
	Timesteplevel level = c_gui.getTopTimesteplevel();
	Timesteplevel thisLevel = c_function.getTimesteplevel();
	c_timesteplevels = thisLevel.toInt() + 1;


    // Added by Nawzad Mardan 20100319
    NodeControl nodeControl = c_gui.getAllNodes();
    Timesteplevel tsl2 = nodeControl.getTimesteplevel(c_nodeID);
    c_currentTimestep  = tsl2.getLabel();
    Timesteplevel tstl = level;
    if(tstl.getNextLevel() == null)
         c_maxTimeSteps = 0;
    while ((tstl = tstl.getNextLevel()) != null)
            {
            c_maxTimeSteps *= tstl.getTimesteps();
            }

	//Get all timesteplevels
	tsl = new Timesteplevel[c_timesteplevels];
	level = c_gui.getTopTimesteplevel();
	for(int j = 0; j < c_timesteplevels; j++) {
	    tsl[j] = level;
	    level = level.getNextLevel();
	}

	//Set the current timestep to 1
	c_timestep = 1;
	c_function.setTimestep(c_timestep);

	level = c_gui.getTopTimesteplevel();
	int timesteps = thisLevel.timestepDifference(level);
	for (int i = 0; i < timesteps; i++) {
	    c_function.setTimestep(i+1);
	    TimestepInfo info = new TimestepInfo();
	    for (int j = 0; j < c_function.getLimits(); j++) {
		float upper = c_function.getUpperLimit(j+1);
		float offset = c_function.getOffset(j+1);
		float slope = c_function.getSlope(j+1);
		info.add(new PositiveNumberField(upper, 9),
			 new NumberField(slope, 8),
			 new NumberField(offset, 8));
	    }
	    c_timestepInfo.addElement(info);
	}
	c_function.setTimestep(c_timestep);

	initComponents();
	listResourceX.setListData(c_resources);
	listResourceY.setListData(c_resources);

	txtLabel.setText(c_function.getLabel());
	updateResources();

	updateTimestepInfo();
	updateLimitPanel();

	pack();
        pack();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    private void initComponents () {
	lblDescription = new javax.swing.JLabel ();
	sep1 = new javax.swing.JSeparator ();
	sep2 = new javax.swing.JSeparator ();
	pnlTimestep = new javax.swing.JPanel ();
//	pnlCopyPaste = new javax.swing.JPanel ();
//	btnCopyFrom = new javax.swing.JButton ();
//	btnCopy = new javax.swing.JButton ();
//	btnPasteTo = new javax.swing.JButton ();
//	btnPaste = new javax.swing.JButton ();
	lblTimestep = new javax.swing.JLabel ();
	pnlLabel = new javax.swing.JPanel ();
	lblLabel = new javax.swing.JLabel ();
	txtLabel = new javax.swing.JTextField ();
	sep3 = new javax.swing.JSeparator ();
	pnlButtons = new javax.swing.JPanel ();
	btnOk = new javax.swing.JButton ();
	btnCancel = new javax.swing.JButton ();
	pnlResource = new javax.swing.JPanel ();
	listResourceX = new javax.swing.JList ();
    // Added by Nawzad Mardan 20100306
    listResourceX.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Resource resource = (Resource) listResourceX.getSelectedValue();
                updateLimitPanel();
                //lblresX.setText(" * "+resource.getLabel() +"  + ");

            }
    });
	listResourceY = new javax.swing.JList ();
    // Added by Nawzad Mardan 20100306
	listResourceY.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Resource resource = (Resource) listResourceY.getSelectedValue();
            //lblresY.setText(resource.getLabel() + " = ");
            updateLimitPanel();

            }
    });

    getContentPane ().setLayout (new GridBagLayout ());
	GridBagConstraints constraints;
	addWindowListener (new java.awt.event.WindowAdapter () {
		public void windowClosing (java.awt.event.WindowEvent evt) {
		    closeDialog (evt);
		}
	    });

	c_pnlLimits.setLayout(new GridBagLayout());

	// --- set description of the function ---
	lblDescription.setText ("Description: Defines a dependency between " +
				"flows.");

	constraints = new java.awt.GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridy = 0;
	constraints.gridwidth = 2;
	constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	constraints.insets = new java.awt.Insets(10, 10, 10, 10);
	constraints.anchor = java.awt.GridBagConstraints.WEST;
	getContentPane().add(lblDescription, constraints);

	// --- add a separator ---
	constraints = new java.awt.GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridy = 1;
	constraints.gridwidth = 2;
	constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	getContentPane().add(sep1, constraints);

	// --- add label to the panel ---
	pnlLabel.setLayout (new java.awt.GridBagLayout ());
	lblLabel.setText ("Label");

	GridBagConstraints gridBagConstraints5 = new GridBagConstraints ();
	//gridBagConstraints5.insets = new java.awt.Insets (10, 10, 10, 10);
	gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
	pnlLabel.add (lblLabel, gridBagConstraints5);

	gridBagConstraints5 = new GridBagConstraints ();
	gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
	gridBagConstraints5.insets = new Insets (0, 10, 0, 0);
	gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
	gridBagConstraints5.weightx = 1.0;
	pnlLabel.add (txtLabel, gridBagConstraints5);

	constraints = new java.awt.GridBagConstraints ();
	constraints.insets = new java.awt.Insets (10, 10, 10, 10);
	constraints.gridx = 0;
	constraints.gridy = 2;
	constraints.gridwidth = 2;
	constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	constraints.insets = new java.awt.Insets (10, 10, 10, 10);
	constraints.weightx = 1.0;
	getContentPane ().add (pnlLabel, constraints);

	// --- add a new separator ---
	constraints = new java.awt.GridBagConstraints ();
	constraints.gridy = 3;
	constraints.gridwidth = 2;
	constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	getContentPane ().add (sep2, constraints);

	//Timestep control

	pnlTimestep = new javax.swing.JPanel();
	pnlTimestep.setLayout(new java.awt.GridBagLayout());

	//Label "Timestep"
      	lblTimestep = new javax.swing.JLabel();
	lblTimestep.setText("Timestep:");
	constraints = new java.awt.GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 0;
	constraints.gridwidth = 2;
      	constraints.weightx = 1.0;
	//constraints.fill = GridBagConstraints.HORIZONTAL;
	constraints.insets = new java.awt.Insets (0, 0, 10, 5);
	constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
	pnlTimestep.add(lblTimestep, constraints);

	//Main timestep panel
	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 4;
	//	constraints.weighty = 1.0;
	constraints.fill = GridBagConstraints.VERTICAL;
	constraints.insets = new Insets(0, 25, 0, 20);
	constraints.anchor = GridBagConstraints.NORTHWEST;
	getContentPane().add(pnlTimestep, constraints);

	lblTSL = new javax.swing.JLabel[c_timesteplevels];
	spinTSL = new SpinButton[c_timesteplevels];
        Timesteplevel level = c_gui.getTopTimesteplevel();
	Timesteplevel thisLevel = c_function.getTimesteplevel();
	//	level = c_eventhandler.getTopTimesteplevel();

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


	// --- add Copy & Paste buttons ---
//	pnlCopyPaste.setLayout (new java.awt.GridBagLayout ());
	java.awt.GridBagConstraints gridBagConstraints3;

/*	btnCopyFrom.setEnabled(false);
	btnCopyFrom.setText ("Copy from range");
	btnCopyFrom.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    btnCopyFromActionPerforme (evt);
		    copyFromRange (evt);
		    btnCopyFromActionPerformed (evt);
		}
	    });*/

/*       	gridBagConstraints3 = new java.awt.GridBagConstraints ();
	gridBagConstraints3.gridx = 0;
	gridBagConstraints3.gridy = 0;
	gridBagConstraints3.weightx = 1.0;
	gridBagConstraints3.anchor = GridBagConstraints.EAST;
	pnlCopyPaste.add (btnCopyFrom, gridBagConstraints3);*/

/*	btnCopy.setEnabled(false);
	btnCopy.setPreferredSize (new java.awt.Dimension(85, 27));
	btnCopy.setText ("Copy");
	btnCopy.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    btnCopyActionPerformed (evt);
		}
	    });*/

	/*gridBagConstraints3 = new java.awt.GridBagConstraints ();
	gridBagConstraints3.gridx = 1;
	gridBagConstraints3.gridy = 0;
	gridBagConstraints3.insets = new java.awt.Insets (10, 10, 10, 10);
	pnlCopyPaste.add (btnCopy, gridBagConstraints3);*/

/*	btnPasteTo.setEnabled(false);
	btnPasteTo.setPreferredSize (new java.awt.Dimension(117, 27));
	btnPasteTo.setText ("Paste to range");
	btnPasteTo.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    btnPasteToRangeActionPerformed (evt);
		}
	    });*/

	/*gridBagConstraints3 = new java.awt.GridBagConstraints ();
	gridBagConstraints3.gridx = 0;
	gridBagConstraints3.gridy = 1;
	gridBagConstraints3.weightx = 1.0;
	gridBagConstraints3.anchor = GridBagConstraints.EAST;
	pnlCopyPaste.add (btnPasteTo, gridBagConstraints3);*/

	/*btnPaste.setEnabled(false);
	btnPaste.setPreferredSize (new java.awt.Dimension(85, 27));
	btnPaste.setText ("Paste");
	btnPaste.addActionListener (new java.awt.event.ActionListener () {
		public void actionPerformed (java.awt.event.ActionEvent evt) {
		    btnPasteActionPerformed (evt);
		}
	    });*/

	/*gridBagConstraints3 = new java.awt.GridBagConstraints ();
	gridBagConstraints3.gridx = 1;
	gridBagConstraints3.gridy = 1;
	// gridBagConstraints3.insets = new java.awt.Insets (10, 10, 10, 10);
	pnlCopyPaste.add(btnPaste, gridBagConstraints3);*/

	/*constraints = new java.awt.GridBagConstraints ();
	constraints.gridx = 1;
	constraints.gridy = 4;
	constraints.weightx = 1.0;
	constraints.insets = new java.awt.Insets (0, 6, 0, 10);
	constraints.anchor = java.awt.GridBagConstraints.EAST;
	getContentPane().add(pnlCopyPaste, constraints);*/

	// --- add the resources panel ---
	pnlResource = createResourcePanel();
	pnlResource.setPreferredSize(new Dimension(340, 200));
	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 5;
	constraints.gridwidth = 2;
	constraints.insets = new Insets(20, 10, 10, 5);
	getContentPane().add(pnlResource, constraints);

	// --- add the limits scrollpane ---
	c_scrollEquations = new JScrollPane(c_pnlLimits);
	c_scrollEquations.setVerticalScrollBarPolicy(ScrollPaneConstants.
					      VERTICAL_SCROLLBAR_ALWAYS);
	c_scrollEquations.setPreferredSize(new Dimension(150, 100));
	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 6;
	constraints.gridwidth = 2;
	constraints.weightx = 1.0;
	constraints.fill = GridBagConstraints.HORIZONTAL;
	constraints.insets = new Insets(0, 10, 0, 10);
	getContentPane().add(c_scrollEquations, constraints);

	// --- add the "Add" and "Remove" buttons ---
	constraints = new GridBagConstraints();
	constraints.gridy = 7;
	constraints.gridwidth = 2;
	constraints.insets = new Insets(20, 10, 10, 10);
	getContentPane().add(createAddRemovePanel(), constraints);

	constraints = new java.awt.GridBagConstraints ();
	constraints.gridy = 8;
	constraints.gridwidth = 2;
	constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
	getContentPane ().add (sep3, constraints);

	// ---- Ok and Cancel buttons ----
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

	constraints = new java.awt.GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridy = 9;
	constraints.gridwidth = 2;
	constraints.insets = new java.awt.Insets (10, 10, 10, 10);
	constraints.anchor = java.awt.GridBagConstraints.EAST;
	getContentPane ().add (pnlButtons, constraints);
    }

    private void btnCancelActionPerformed (ActionEvent evt) {
	setVisible(false);
	dispose();
    }

    private void btnOkActionPerformed (ActionEvent evt)
    {
	// save the function label
	c_function.setLabel(txtLabel.getText());
	if (!saveAllTimestepInfo()) {
	    showWarningDialog(UPPER_INVALID_TITLE,
			      UPPER_INVALID_MESSAGE);
	    return;
	}

	Resource resource = (Resource) listResourceX.getSelectedValue();
	if (resource != null)
	    c_function.setResourceX(resource.getID());
	resource = (Resource) listResourceY.getSelectedValue();
	if (resource != null)
	    c_function.setResourceY(resource.getID());

	c_function.setXIn(c_radXIn.isSelected());
	c_function.setYIn(c_radYIn.isSelected());

   /* Added by Nawzad Mardan 20100321 at 23.51
    To solve the bug in the FlowDependency function. If the user add a new FlowDependency function in a node
    which have several levels of time steps and user enter only the values for the first time steps instead for alls
    time steps and save the model. If the user try to open the model an errer  occur and the model can not be opened
    */
   
    if(!(c_currentTimestep.equals("TOP")) && (c_maxTimeSteps > 1))
        {
        if(c_function.getTimestep()> 1)
            {
            boolean dataNotEnterd = false;
            for(int i = 1; i < c_maxTimeSteps; i++)
                {
                if((c_function.getSlope(i) ==0) && (c_function.getOffset(i) == 0) &&
                    (c_function.getLowerLimit(1) == 0) && (c_function.getUpperLimit(1) == 0) )
                    {
                    dataNotEnterd =true;
                    break;
                    }
                }
            if(dataNotEnterd)
                 {
                 c_function.setDetailedDataToRemainedTimesteps(c_maxTimeSteps);
                 }
            }
        }
	// close the dialog
	closeDialog(null);
    }

    private void btnPasteActionPerformed (java.awt.event.ActionEvent evt) {
    }

    private void btnCopyActionPerformed (java.awt.event.ActionEvent evt) {
    }

    private void btnPasteToRangeActionPerformed (java.awt.event.ActionEvent evt) {
    }

    private void btnCopyFromActionPerformed (java.awt.event.ActionEvent evt) {
    }

    private void copyFromRange (java.awt.event.ActionEvent evt) {
    }

    private void btnCopyFromActionPerforme (java.awt.event.ActionEvent evt) {
    }

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {
	setVisible (false);
	dispose ();
    }

    private javax.swing.JLabel lblDescription;
    private javax.swing.JSeparator sep1;
    private javax.swing.JSeparator sep2;
    private javax.swing.JPanel pnlTimestep;
    private javax.swing.JLabel lblTSL[];
    private SpinButton spinTSL[];
  //  private javax.swing.JPanel pnlCopyPaste;
    //private javax.swing.JButton btnCopyFrom;
   // private javax.swing.JButton btnCopy;
   // private javax.swing.JButton btnPasteTo;
  //  private javax.swing.JButton btnPaste;
    private javax.swing.JLabel lblTimestep;
    private javax.swing.JPanel pnlLabel;
    private javax.swing.JLabel lblLabel;
    private javax.swing.JTextField txtLabel;
    private javax.swing.JSeparator sep3;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnCancel;
    private javax.swing.JPanel pnlResource;
    private javax.swing.JList listResourceX;
    private javax.swing.JList listResourceY;
    private javax.swing.JButton btnNewResource;
    // Added by Nawzad Mardan 20100305
    private javax.swing.JPanel pnlEquation;
    private javax.swing.JLabel lblresY;
    private javax.swing.JLabel lblresX;

    private JPanel createResourcePanel()
    {
	JPanel pnlResources = new JPanel();
	GridBagConstraints constraints;
	JPanel pnlX = new JPanel();
	JPanel pnlY = new JPanel();
	JPanel pnlDrawing = new JPanel();
	JScrollPane scrollResourceX;
	JScrollPane scrollResourceY;
	ButtonGroup groupX = new ButtonGroup();
	ButtonGroup groupY = new ButtonGroup();
	groupX.add(c_radXIn);
	groupX.add(c_radXOut);
	groupY.add(c_radYIn);
	groupY.add(c_radYOut);

	// setup default of radio buttons
	if (c_function.isXIn())
	    c_radXIn.setSelected(true);
	else
	    c_radXOut.setSelected(true);
	if (c_function.isYIn())
	    c_radYIn.setSelected(true);
	else
	    c_radYOut.setSelected(true);

	// highligh the former selected resources
	if (c_function.getResourceX() != null) {
	    int index = c_resources.
		indexOf(c_gui.getResource(c_function.getResourceX()));
	     listResourceX.setSelectedIndex(index);
	}
	if (c_function.getResourceY() != null) {
	    int index = c_resources.
		indexOf(c_gui.getResource(c_function.getResourceY()));
	    listResourceX.setSelectedIndex(index);
	}

	pnlResources.setLayout(new GridBagLayout ());

	// --- setup of panel for resourceX ---
	pnlX.setLayout(new GridBagLayout());
	pnlX.setBorder(new TitledBorder("Resource X"));

	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 0;
	constraints.insets = new Insets(0, 10, 0, 10);
	pnlX.add(c_radXIn, constraints);

	constraints = new GridBagConstraints();
	constraints.gridx = 1;
	constraints.gridy = 0;
	constraints.weightx = 1.0;
	constraints.anchor = GridBagConstraints.WEST;
	pnlX.add(c_radXOut, constraints);

	scrollResourceX = new JScrollPane(listResourceX);
	scrollResourceX.setPreferredSize(new Dimension(120, 150));

	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 1;
	constraints.gridwidth = 2;
	constraints.weighty = 1.0;
	constraints.fill = GridBagConstraints.VERTICAL;
	pnlX.add(scrollResourceX, constraints);

	constraints = new GridBagConstraints();
	constraints.gridx = 1;
	constraints.gridy = 0;
	pnlResources.add(pnlX, constraints);

	// --- setup of panel for resourceY ---
	pnlY.setLayout(new GridBagLayout());
	pnlY.setBorder(new TitledBorder("Resource Y"));

	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 0;
	constraints.insets = new Insets(0, 10, 0, 10);
	pnlY.add(c_radYIn, constraints);

	constraints = new GridBagConstraints();
	constraints.gridx = 1;
	constraints.gridy = 0;
	constraints.weightx = 1.0;
	constraints.anchor = GridBagConstraints.WEST;
	pnlY.add(c_radYOut, constraints);

	scrollResourceY = new JScrollPane(listResourceY);
	scrollResourceY.setPreferredSize(new Dimension(120, 150));
	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 1;
	constraints.gridwidth = 2;
	constraints.weighty = 1.0;
	constraints.fill = GridBagConstraints.VERTICAL;
	pnlY.add(scrollResourceY, constraints);

	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 0;
	pnlResources.add(pnlY, constraints);

	// --- set up panel for drawing ---
	constraints = new GridBagConstraints();
	constraints.gridx = 2;
	constraints.gridy = 0;
	pnlResources.add(pnlDrawing, constraints);

	return pnlResources;
    }

    private JPanel createAddRemovePanel()
    {
	JPanel panel = new JPanel();

	c_btnAdd.addActionListener(new ActionListener()
	{
	    public void actionPerformed(ActionEvent e)
	    {
		if (getTimestepInfo().add()) {
		    updateLimitPanel();
		    c_btnRemove.setEnabled(true);
		}
		else
		    showWarningDialog(UPPER_INVALID_TITLE,
				      UPPER_INVALID_MESSAGE);
	    }
	    });
	c_btnRemove.addActionListener(new ActionListener()
        {
	    public void actionPerformed(ActionEvent e)
	    {
		getTimestepInfo().remove();
		updateLimitPanel();
		if (getTimestepInfo().getSize() <= 0)
		    c_btnRemove.setEnabled(false);
	    }
	    });

	panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
	panel.add(c_btnAdd);
	panel.add(Box.createHorizontalStrut(10));
	panel.add(c_btnRemove);

	return panel;
    }

    private TimestepInfo getTimestepInfo()
    {
	return (TimestepInfo) c_timestepInfo.get(c_timestep-1);
    }

    private boolean saveAllTimestepInfo()
    {
	float upper;
	float slope;
	float offset;
	float formerUpper;
	boolean valid = true;

	// check all upper limits so they are valid
	// 3 < limit < 5 is valid,  4 < limit < 1 is not
	int timestep = c_timestep;
	for (int i = 0; (i < c_timestepInfo.size()) && valid; i++) {
	    c_timestep = i+1;
	    formerUpper = 0;
	    for (int j = 1; (j <= getTimestepInfo().getSize()) && valid; j++) {
		upper = ((NumberField) getTimestepInfo().
			 getUpperLimit(j)).getFloatValue();
		if (upper <= formerUpper)
		    valid = false;

		formerUpper = upper;
	    }
	}

	if (!valid)
	    return false;

	c_function.clearLimits();
	for (int i = 0; i < c_timestepInfo.size(); i++) {
	    c_function.setTimestep(i+1);
	    c_timestep = i + 1;
	    for (int j = 0; j < getTimestepInfo().getSize(); j++) {
		upper = ((NumberField) getTimestepInfo().
			 getUpperLimit(j+1)).getFloatValue();
		slope = ((NumberField) getTimestepInfo().
			 getSlope(j+1)).getFloatValue();
		offset = ((NumberField) getTimestepInfo().
			  getOffset(j+1)).getFloatValue();

		if (c_function.getLimits() < j+1)
		    c_function.addLimit();
		c_function.setLimitInfo(j+1, upper, slope, offset);
	    }
	}
	c_timestep = timestep;

	return true;
    }

    private void showLastEquation()
    {
	Dimension dim = c_pnlLimits.getSize();
	Rectangle bounds = new Rectangle();
	c_pnlLimits.getBounds(bounds);

	//	JViewport view = c_scrollEquations.getView();


	//	Rectangle rect = new Rectangle(bounds.x, bounds.y+bounds.height-1,
	//		       1, 1);
	Rectangle rect = new Rectangle(0, dim.height-1, dim.width, dim.height);
	c_pnlLimits.scrollRectToVisible(rect);
	c_pnlLimits.revalidate();
    }

    private void showWarningDialog(String title, String message)
    {
	JOptionPane.showMessageDialog(FlowDependencyDialog.this,
				      UPPER_INVALID_MESSAGE,
				      UPPER_INVALID_TITLE,
				      JOptionPane.OK_OPTION);
    }

    private void updateLimitPanel()
    {
	Float lower;
	Component upper;
	Component slope;
	Component offset;
	JPanel pnlLimit;
	GridBagConstraints constraints;
    // Added by Nawzad Mardan 20100305
    String resouceLable = "";

	c_pnlLimits.removeAll();

	// for every limit, add two new rows to the panel
	// first row specifies the limit ... lowerlimit < limit < upperlimit
	// second row specifies the equation ... y = k*x + m
	int j = -1;
	for (int i = 0; i < getTimestepInfo().getSize(); i++) {
	    lower = new Float(getTimestepInfo().getLowerLimit(i+1));
	    upper = (Component) getTimestepInfo().getUpperLimit(i+1);
	    slope = (Component) getTimestepInfo().getSlope(i+1);
	    offset = (Component) getTimestepInfo().getOffset(i+1);

	    // --- add limits to the first panel ---
	    j++;
	    pnlLimit = new JPanel();
	    pnlLimit.setLayout(new BoxLayout(pnlLimit, BoxLayout.X_AXIS));
	    pnlLimit.add(new JLabel("Between "));
	    pnlLimit.add(new JLabel(lower.toString()));
	    pnlLimit.add(new JLabel(" and "));
	    pnlLimit.add(upper);

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
        // Added by Nawzad Mardan 20100305
        lblresY = new JLabel("<res-Y> = ");
        if (listResourceY.getSelectedIndex() != -1)
            {
            Resource resource = (Resource) listResourceY.getSelectedValue();
            resouceLable = resource.getLabel();
            lblresY.setText(resouceLable + " = ");
            }
        pnlEquation.add(lblresY);
        // Added by Nawzad Mardan 20100305
	    pnlEquation.add(slope);
        lblresX = new JLabel(" * <res-X> + ");
        if (listResourceX.getSelectedIndex() != -1)
            {
            Resource resource = (Resource) listResourceX.getSelectedValue();
            resouceLable = resource.getLabel();
            lblresX.setText(" * "+resouceLable +"  + ");
            }
        pnlEquation.add(lblresX);
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
	    if (i == getTimestepInfo().getSize()-1)
		constraints.weighty = 1.0;
	    constraints.fill = GridBagConstraints.HORIZONTAL;
	    constraints.anchor = GridBagConstraints.NORTHWEST;
	    constraints.insets = new Insets(0, 3, 3, 0);
	    c_pnlLimits.add(new JSeparator(), constraints);
	}
	showLastEquation();
	c_pnlLimits.updateUI();
    }

    private void updateResources()
    {
	listResourceX.setListData(c_resources);
	listResourceY.setListData(c_resources);

	if (c_function.getResourceX() != null) {
	    Resource resource = c_gui.getResource(c_function.getResourceX());
	    listResourceX.setSelectedIndex(c_resources.indexOf(resource));
	}
	if (c_function.getResourceY() != null) {
	    Resource resource = c_gui.getResource(c_function.getResourceY());
	    listResourceY.setSelectedIndex(c_resources.indexOf(resource));
	}
    }

    public boolean updateTimestep()
    {
        //Save value for previous timestep
	//	c_function.setTimestep(c_timestep);
	//	if (!saveTimestepInfo())
	//  return false;

	//Calculate new timestep
	c_timestep = 1; //The first timestep is 1 (not 0)
	int factor = 1;
	for(int i = c_timesteplevels - 1; i > 0; i--) {
	    c_timestep = c_timestep + (spinTSL[i].getValue() - 1) * factor;
	    factor = factor * tsl[i].getMaxTimesteps();
	}

	if (getTimestepInfo().getSize() < 1)
	    c_btnRemove.setEnabled(false);
	else
	    c_btnRemove.setEnabled(true);


	//Get value for new timestep
       	c_function.setTimestep(c_timestep);
	//	updateTimestepInfo();
	updateLimitPanel();
	return true;
    }

    // Checks with the function what info there is within current timestep
    // The timestepinfo class is updated with this information
    private void updateTimestepInfo()
    {
	int size = c_function.getLimits();
	float upperLimit;
	float slope;
	float offset;

	c_function.setTimestep(c_timestep);
	getTimestepInfo().clear();
	for (int i = 1; i <= size; i++) {
	    upperLimit = c_function.getUpperLimit(i);
	    slope = c_function.getSlope(i);
	    offset = c_function.getOffset(i);
	    getTimestepInfo().add(new PositiveNumberField(upperLimit, 9),
				  new NumberField(slope, 8),
				  new NumberField(offset, 8));
	}
    }
}
