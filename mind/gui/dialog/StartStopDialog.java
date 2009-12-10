/*
 * StartStopDialog.java
 *
 * Created on den 11 februari 2008, 14:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mind.gui.dialog;
import java.awt.*;
import java.util.Vector;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import mind.gui.*;
import mind.model.*;
import mind.model.function.*;
import mind.EventHandlerClient;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JDialog;

/**
 *
 * @author nawma77
 */
public class StartStopDialog extends mind.gui.dialog.FunctionDialog{
    
    private ID c_nodeID;
    private Vector c_resources;
    private GUI c_gui;
    private StartStopEquation c_function;
    private EventHandlerClient c_eventhandler;
    private Timesteplevel tsl[];
    private int c_timestep; //Active timestep
    private int c_timesteplevels; //Number of timesteplevels
    private boolean c_choiceOneSelected = false;
    private boolean c_choiceTowSelected = false;
    private boolean c_choiceThreeSelected = false;
    private boolean c_choiceFourSelected = false;
    private boolean c_noOutFlow = false;
   // private boolean c_startCostSelected  = false;
   // private boolean c_stopCostSelected  = false;
    private JCheckBox alltOneCheckBox;
    private JCheckBox alltTwoCheckBox;
    private JCheckBox alltThreeCheckBox;
    private JCheckBox alltFourCheckBox;
    private JCheckBox startCostCheckBox;
    private JCheckBox stopCostCheckBox;
    //  Alt 1
    private JCheckBox startAltOneCB, stopAltOneCB;
    private JCheckBox startFisrtTimestepAltOneCB, stopLastTimestepAltOneCB;
    
    //  Alt 3 
    private JCheckBox startAltThreeCB, stopAltThreeCB;
    private JCheckBox startFisrtTimestepAltThreeCB, stopLastTimestepAltThreeCB;
           
    private JCheckBox startCostOfFisrtTimestepCheckBox;
    private JCheckBox stopCostOfFisrtTimestepCheckBox;
    // ALT 4
    private JCheckBox startWasteCheckBox, stopWasteCheckBox;
    private JCheckBox startWasteOfFisrtTimestepCheckBox,percentagevalueCheckBox;
    private JCheckBox stopWasteOfLasttTimestepCheckBox, fixedvalueCheckBox;
    private PositiveNumberField percentageValueField; 
    private PositiveNumberField fixedValueField, outFlowField;
    private JList lstOutFlow;
    private JButton btnOk;
    private JButton btnCancel;  
    private JLabel lblDescriptionAlltOne;
    private JLabel lblDescriptionAlltTwo;
    private JLabel lblDescriptionAlltThree;
    private JLabel lblDescriptionAlltFour;
    private JLabel  percentageOfPreviousFlowLabel;
    private JLabel labelTwoAltOne;
    private PositiveNumberField  percentageOfPreviousFlowField,startCostField;//technicallifespan, economicllifespan
    private JLabel thresholdValueLabel ;
    private PositiveNumberField thresholdValueField, stopCostField, operateCostField ; 
    private JLabel labelOneAltTwo;
    private JLabel labelThreeAltTwo;
    private JLabel minFlowAltTwoLabel;
    private PositiveNumberField minFlowAltTwoField;
    private JLabel labelFourAltTwo;
    private JLabel labelThreeAltThree, minFlowLabel;
    private PositiveNumberField fieldTwoAltThree, minFlowField; 
    private JLabel labelThreeAltFour, operatcostAltFourLabel, minFlowAltFourLabel;
    private PositiveNumberField operatcostAltFourField, minFlowAltFourField; 
    private JTextField txtLabel;
    private final String descriptionText =
            "Description: Defines a start and stop for the production. " +
            "Observe that a small difference in the values between the different time steps" +
            " needs to be included in at least one of the Source functions when using the " +
            "StartStop Equation. Otherwise there will be an error in the solution.";
    // Resuce list
    private javax.swing.JPanel pnlResource;
    private javax.swing.JLabel lblTypeResource;
    private javax.swing.JList listResource;
    private JRadioButton radioLt,radioEq, radioGt, radioLtGt;
    private JRadioButton radioBP, radioCP;
    private ButtonGroup OperatorButtons;
    private ButtonGroup ProcessButtons;
    private PositiveNumberField spinLimit1;
    private PositiveNumberField spinLimit2;
    private JPanel pnlFlowSettings;
    private JPanel pnlRadioButton;
    private JLabel lblSpin;
    private  JScrollPane scrollPanel;

	
    /** Creates a new instance of StartStopDialog */
    
    public StartStopDialog(){}
    
     public StartStopDialog(javax.swing.JDialog parent, boolean modal, ID nodeID, NodeFunction function, GUI gui) 
        {
	super (parent, modal);
        setTitle("Start and Stop Equation");
	c_nodeID = nodeID;
	c_gui = gui;
        c_function = (StartStopEquation)function;
        initComponent();
        updateResources();
        updateFlow();
    }
   
    public StartStopDialog(JDialog parent, boolean modal, ID nodeID, NodeFunction function, GUI gui, boolean choiceOne,
             boolean choiceTwo, boolean choiceThree, boolean choiceFour) 
        {
	super (parent, modal);
        setTitle("Start and Stop Equation");
	c_nodeID = nodeID;
	c_gui = gui;
	c_function = (StartStopEquation) function;
	c_eventhandler = c_gui.getEventHandlerClient();
        c_choiceOneSelected=choiceOne ; 
        c_choiceTowSelected =  choiceTwo;
        c_choiceThreeSelected =  choiceThree;
        c_choiceFourSelected = choiceFour ;

	//calculate number of timestepslevels in the function
	c_timesteplevels = 1;
	Timesteplevel level = c_eventhandler.getTopTimesteplevel();
	//Timesteplevel thisLevel = c_function.getTimesteplevel();
	//while (level != thisLevel) {
	  //  c_timesteplevels++;
	    //level = level.getNextLevel();
	//}

	//Get all timesteplevels
	//tsl = new Timesteplevel[c_timesteplevels];
	//level = c_eventhandler.getTopTimesteplevel();
	//for(int j=0; j<c_timesteplevels; j++) {
	  //  tsl[j] = level;
	    //level = level.getNextLevel();
	//}
        initComponent();
        updateResources();
        updateFlow();
    }
    

 /** This method is called from  the constructor to
   * initialize the form of the Start and Stop dialog.
   * 
  */
private void initComponent()
{
    setName ("Start Stop Equation");
    String s;
    GridBagLayout gridbag = new GridBagLayout();
    getContentPane().setLayout(gridbag);
    GridBagConstraints gridBagConstraints1;
   /* JLabel lblDescription = new JLabel();
    lblDescription.setText ("Description: Defines a start and stop for the production.");
    gridBagConstraints1 = new java.awt.GridBagConstraints ();
    gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints1.insets = new java.awt.Insets (10, 10, 0, 10);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    getContentPane ().add (lblDescription, gridBagConstraints1);*/
 
    javax.swing.JPanel pnlDescr = new javax.swing.JPanel();
    JTextArea descr = new javax.swing.JTextArea(descriptionText);
    descr.setFont(new Font("SansSerif", Font.BOLD, 12));
    descr.setLineWrap(true);
    descr.setWrapStyleWord(true);
    descr.setEnabled(false);
    descr.setDisabledTextColor(Color.black);
    descr.setBackground(new Color(255,255,255));
    descr.setPreferredSize(new java.awt.Dimension(600, 50));
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    //gridBagConstraints1.gridy = 2;
    gridBagConstraints1.gridwidth = 2;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    //gridBagConstraints1.insets = new java.awt.Insets(10, 10, 0, 10);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    pnlDescr.add(descr, gridBagConstraints1);
    gridBagConstraints1 = new java.awt.GridBagConstraints ();
    gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridy = 1;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    //gridBagConstraints1.insets = new java.awt.Insets (10, 10, 0, 10);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    getContentPane ().add (pnlDescr, gridBagConstraints1);
    JSeparator lnsep = new javax.swing.JSeparator();
    gridBagConstraints1 = new java.awt.GridBagConstraints ();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridy = 2;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    getContentPane ().add (lnsep, gridBagConstraints1);
    
    JPanel pnlLabel = new JPanel();
    pnlLabel.setLayout (new java.awt.GridBagLayout ());
	
    JLabel lblLabel = new JLabel();
    lblLabel.setText ("Label");

    GridBagConstraints gridBagConstraints5 = new GridBagConstraints ();
    //gridBagConstraints5.insets = new java.awt.Insets (10, 10, 10, 10);
    gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
    pnlLabel.add (lblLabel, gridBagConstraints5);
    
    txtLabel = new JTextField();
    txtLabel.setText(c_function.getLabel());
    gridBagConstraints5 = new java.awt.GridBagConstraints ();
    gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
   // gridBagConstraints5.insets = new java.awt.Insets (0, 10, 0, 0);
    gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints5.weightx = 1.0;
    pnlLabel.add (txtLabel, gridBagConstraints5);
    
    gridBagConstraints1 = new java.awt.GridBagConstraints ();
    //gridBagConstraints1.insets = new java.awt.Insets (10, 10, 10, 10);
    gridBagConstraints1.gridx = 0;
    //gridBagConstraints1.gridy = 2;
    gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints1.insets = new java.awt.Insets (3, 5, 2, 0);
    gridBagConstraints1.weightx = 1.0;
    gridBagConstraints1.gridy = 3;
    getContentPane ().add (pnlLabel, gridBagConstraints1);

    gridBagConstraints1 = new java.awt.GridBagConstraints ();
    gridBagConstraints1.gridy = 3;
    gridBagConstraints1.gridwidth = 2;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    
    JSeparator sepl2 = new javax.swing.JSeparator();
    getContentPane ().add (sepl2, gridBagConstraints1);

    //Create the check boxes.
    alltOneCheckBox = new JCheckBox("Alternative 1 ");
    alltOneCheckBox.setForeground(Color.BLUE);
    alltOneCheckBox.setFont(new Font("SansSerif", Font.BOLD, 13));
    //alltOneCheckBox.setMnemonic(KeyEvent.VK_C);
    alltOneCheckBox.setToolTipText("This alternative is included to picture processes that are not " +
            "possible to start up or shut down immediately");
    alltOneCheckBox.setSelected(c_function.getChoiceOne());
    alltTwoCheckBox = new JCheckBox("Alternative 2 ");
    alltTwoCheckBox.setForeground(Color.BLUE);
    alltTwoCheckBox.setFont(new Font("SansSerif", Font.BOLD, 13));
    //alltTwoCheckBox.setMnemonic(KeyEvent.VK_G);
    alltTwoCheckBox.setToolTipText("Each start and/or stop is associated with a cost");
    alltTwoCheckBox.setSelected(c_function.getChoiceTwo());

    alltThreeCheckBox = new JCheckBox("Alternative 3 ");
    alltThreeCheckBox.setForeground(Color.BLUE);
    alltThreeCheckBox.setFont(new Font("SansSerif", Font.BOLD, 13));
    //alltThreeCheckBox.setMnemonic(KeyEvent.VK_H);
    alltThreeCheckBox.setToolTipText("In this alternative the number of starts and stops are limited");
    alltThreeCheckBox.setSelected(c_function.getChoiceThree());

    alltFourCheckBox = new JCheckBox("Alternative 4");
    alltFourCheckBox.setForeground(Color.BLUE);
    alltFourCheckBox.setFont(new Font("SansSerif", Font.BOLD, 13));
    //alltFourCheckBox.setMnemonic(KeyEvent.VK_T);
    //JToolTip tTt = new JToolTip();
    //tTt.setToolTipText("Each start or stop is associated with a waste flow");
    //tTt.setFont(new Font("SansSerif", Font.BOLD, 13));

    alltFourCheckBox.setToolTipText("Each start or stop is associated with a waste flow");
    alltFourCheckBox.setSelected(c_function.getChoiceFour());
  
    //Register a listener for the check boxes.
    alltOneCheckBox.addItemListener(new QItemListener());
    alltTwoCheckBox.addItemListener(new QItemListener());
    alltThreeCheckBox.addItemListener(new QItemListener());
    alltFourCheckBox.addItemListener(new QItemListener());
        
    //Put the check boxes in a column in a panel
    JPanel checkAndRecPanel = new JPanel(new GridLayout(1, 4));
    //checkAndRecPanel.setLayout (new java.awt.GridBagLayout ());
    JPanel checkPanel = new JPanel(new GridLayout(0, 1));
    checkPanel.add(alltOneCheckBox);
    checkPanel.add(alltTwoCheckBox);
    checkPanel.add(alltThreeCheckBox);
    checkPanel.add(alltFourCheckBox);
    
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridwidth = 1;
    //gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(10, 5, 5, 5);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    checkAndRecPanel.add(checkPanel);
   /* gridBagConstraints1 = new java.awt.GridBagConstraints();
    //gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    //gridBagConstraints1.insets = new java.awt.Insets(10, 5, 5, 5);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    getContentPane().add(checkPanel, gridBagConstraints1);*/
    /*JSeparator sepOne = new javax.swing.JSeparator();
    GridBagConstraints gridBagConstraints;
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    getContentPane().add(sepOne, gridBagConstraints);*/
    
    JPanel AltOnePanel = new JPanel();    
    AltOnePanel.setLayout(new GridBagLayout());
    // List Resouces
    pnlResource = new javax.swing.JPanel ();
    lblTypeResource = new javax.swing.JLabel ();
    listResource = new javax.swing.JList ();
    pnlResource.setLayout (new java.awt.GridBagLayout ());
    java.awt.GridBagConstraints gridBagConstraints4;
    lblTypeResource.setText ("Type of resource:");
    lblTypeResource.setForeground(Color.BLUE);
    lblTypeResource.setFont(new Font("SansSerif", Font.BOLD, 13));
    gridBagConstraints4 = new java.awt.GridBagConstraints ();
    gridBagConstraints4.gridx = 0;
    gridBagConstraints4.insets = new java.awt.Insets(0,0, 5, 0);
    gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
    //getContentPane().add(lblTypeResource, gridBagConstraints4);
    //checkAndRecPanel.add(lblTypeResource, gridBagConstraints4);
    pnlResource.add(lblTypeResource, gridBagConstraints4);
    //AltOnePanel.add(lblTypeResource, gridBagConstraints4);
    //      listResource.setPreferredSize (new java.awt.Dimension(10, 50));
    listResource.addListSelectionListener(new ListSelectionListener() {
	      public void valueChanged(ListSelectionEvent e)
	      {
		  newResourceSelected();
	      }
	  });

      gridBagConstraints4 = new java.awt.GridBagConstraints ();
      gridBagConstraints4.gridx = 0;
      //gridBagConstraints4.insets = new java.awt.Insets(5, 5, 5, 5);
      gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
      
      JScrollPane scrollPane = new JScrollPane(listResource);
      scrollPane.setPreferredSize(new Dimension(140, 70));
      pnlResource.add (scrollPane, gridBagConstraints4);
      
      gridBagConstraints1 = new java.awt.GridBagConstraints();
      //gridBagConstraints1.gridwidth = 2;
      //gridBagConstraints1.gridx = 0;
      gridBagConstraints1.insets = new java.awt.Insets(5, 0, 15, 5);
      gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
      JPanel emptyPanel1 = new JPanel();
      emptyPanel1.setSize(10,10);
      JPanel emptyPanel2 = new JPanel();
      emptyPanel2.setSize(10,10);
      checkAndRecPanel.add(emptyPanel1);
      checkAndRecPanel.add(emptyPanel2);
      checkAndRecPanel.add(pnlResource);
      //checkAndRecPanel.add(pnlResource);
      
      gridBagConstraints4 = new java.awt.GridBagConstraints ();
      gridBagConstraints4.gridx = 0;
      gridBagConstraints4.insets = new java.awt.Insets(0, 5,2, 0);
      gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
      getContentPane().add(checkAndRecPanel, gridBagConstraints4);
      //AltOnePanel.add(pnlResource, gridBagConstraints4);
      JSeparator lsp = new javax.swing.JSeparator();
      gridBagConstraints1 = new java.awt.GridBagConstraints ();
      gridBagConstraints1.gridx = 0;
      gridBagConstraints1.gridwidth = 1;
      gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 0);
      gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
      getContentPane ().add (lsp, gridBagConstraints1);
        
    // Alternative one is selected 
   // GridBagConstraints gridBagConstraints1;
    JPanel AltOneTowPanel = new JPanel();  
    AltOneTowPanel.setLayout(new GridBagLayout());
    
    JPanel AltThreeFourPanel = new JPanel();    
    AltThreeFourPanel.setLayout(new GridBagLayout());
    
    lblDescriptionAlltOne = new javax.swing.JLabel();
    lblDescriptionAlltOne.setFont(new Font("SansSerif", Font.BOLD, 16));
    //lblDescriptionAlltOne.setBackground(Color.BLUE);//setDisabledTextColor(Color.black);
    lblDescriptionAlltOne.setForeground(Color.BLUE);
    lblDescriptionAlltOne.setText("Alternative 1");
    lblDescriptionAlltOne.setToolTipText("This alternative is included to picture processes that are not " +
            "possible to start up or shut down immediately");
    //setFont(new Font("SansSerif", Font.BOLD, 12));
    //lblDescriptionAlltTwo.setText("Description: Discounted system cost is a function from which .");
    JPanel labelDescriptionPanelOne = new JPanel();
    labelDescriptionPanelOne.add(lblDescriptionAlltOne);
    //AltOnePanel.add(lblDescriptionAlltOne);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.insets = new java.awt.Insets(5, 11, 15, 0);
    //new java.awt.Insets(tlbr)
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    //getContentPane().add(labelDescriptionPanelOne, gridBagConstraints1);
    AltOnePanel.add(lblDescriptionAlltOne, gridBagConstraints1);

    //startAltOneCB, stopAltOneCB, startFisrtTimestepAltOneCB, //stopLastTimestepAltOneCB;

    startAltOneCB = new JCheckBox("Start up");
    startAltOneCB.setSelected(c_function.getStartAltOneCB());
    //Register a listener for the check boxes.
    startAltOneCB.addItemListener(new QItemListener());
    
    stopAltOneCB = new JCheckBox("Shut down");
    stopAltOneCB.setSelected(c_function.getStopAltOneCB());
    //Register a listener for the check boxes.
    stopAltOneCB.addItemListener(new QItemListener());
    
    startFisrtTimestepAltOneCB = new JCheckBox("Start up within the analysis period");    
    startFisrtTimestepAltOneCB.setSelected(c_function.getStartFirstTimestepAltOneCB());
    //Register a listener for the check boxes.
    startFisrtTimestepAltOneCB.addItemListener(new QItemListener());
    startFisrtTimestepAltOneCB.setEnabled(c_function.getStartAltOneCB());
    
    stopLastTimestepAltOneCB = new JCheckBox("Shut down within the analysis period");
    stopLastTimestepAltOneCB.setSelected(c_function.getStopLastTimestepAltOneCB());
    //Register a listener for the check boxes.
    stopLastTimestepAltOneCB.addItemListener(new QItemListener());
    stopLastTimestepAltOneCB.setEnabled(c_function.getStopAltOneCB());

    JPanel checkPanelAltOne = new JPanel();
    checkPanelAltOne.add(startAltOneCB); 
    checkPanelAltOne.add(stopAltOneCB);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(7, 5, 7, 0);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    AltOnePanel.add(checkPanelAltOne, gridBagConstraints1);

    JPanel startFisrtTimestepAltOnePanel = new JPanel();
    startFisrtTimestepAltOnePanel.add(startFisrtTimestepAltOneCB);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(7, 5, 7, 0);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    AltOnePanel.add(startFisrtTimestepAltOnePanel, gridBagConstraints1);

    JPanel stopLastTimestepAltOnePanel = new JPanel();
    stopLastTimestepAltOnePanel.add(stopLastTimestepAltOneCB);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(7, 5, 7, 0);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    AltOnePanel.add(stopLastTimestepAltOnePanel, gridBagConstraints1);

     percentageOfPreviousFlowLabel= new JLabel("Proportion of flow:");
     percentageOfPreviousFlowLabel.setToolTipText("The proportion of the flow in the bransches, in the previous and/or the \nfollowing time step, that have to pass the bransches in the actual time step");
     labelTwoAltOne = new JLabel(" %");
     percentageOfPreviousFlowField = new PositiveNumberField();
     if(c_function.getChoiceOne())
     {
       percentageOfPreviousFlowField.setText(new Float(c_function.getPercentageOfPreviousFlow()).toString());  
     }
     else
     {
         percentageOfPreviousFlowField.setText("0.0");
     }
     percentageOfPreviousFlowField.setToolTipText("The proportion of the flow in the bransches, in the previous and/or the \nfollowing time step, that have to pass the bransches in the actual time step");
    
    thresholdValueLabel  = new JLabel("Threshold value :   ");
    thresholdValueField = new PositiveNumberField();
    thresholdValueField.setToolTipText("Threshold value ");
    if(c_function.getChoiceOne())
     {
      thresholdValueField.setText(new Float(c_function.getThresholdValue()).toString());  
    }
    else
    {
      thresholdValueField.setText("0.0");  
    }
    /*labelFourAltOne= new JLabel("Alt 1 Label3 :");
    fieldThreeAltOne = new JTextField(10);
    fieldThreeAltOne.setToolTipText("Alt 1 Label3 :");*/

    JPanel labelOnePanelAllOne = new JPanel();
    JPanel labeTwoPanelAllOne = new JPanel();
    JPanel labeThreePanelAllOne = new JPanel();
  
    // Add labels and field to the panel 
    labelOnePanelAllOne.add(percentageOfPreviousFlowLabel);
    labelOnePanelAllOne.add(percentageOfPreviousFlowField);
    labelOnePanelAllOne.add(labelTwoAltOne);
   // AltOnePanel.add(labelOnePanelAllOne);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(7, 6, 7, 70);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    //getContentPane().add(labelOnePanelAllOne, gridBagConstraints1);
    AltOnePanel.add(labelOnePanelAllOne, gridBagConstraints1);
    
    // Add labels and field to the panel
    labeTwoPanelAllOne.add(thresholdValueLabel );
    labeTwoPanelAllOne.add(thresholdValueField);
    //AltOnePanel.add(labeTwoPanelAllOne);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(5, 6, 7, 70);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    //getContentPane().add(labeTwoPanelAllOne, gridBagConstraints1);
    AltOnePanel.add(labeTwoPanelAllOne, gridBagConstraints1);
    // Add labels and field to the panel
    /*labeThreePanelAllOne.add(labelFourAltOne);
    llabeThreePanelAllOne.add(fieldThreeAltOne);*/
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    //gridBagConstraints1.insets = new java.awt.Insets(0, 5, 5, 5);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    //getContentPane().add(AltOnePanel, gridBagConstraints1);
    AltOneTowPanel.add(AltOnePanel);
    JSeparator separator = new JSeparator(JSeparator.VERTICAL);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    //gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridwidth = 2;
    gridBagConstraints1.insets = new java.awt.Insets(0, 0, 0, 0);
    gridBagConstraints1.fill = java.awt.GridBagConstraints.VERTICAL;
    //getContentPane().add(se, gridBagConstraints1);
    AltOneTowPanel.add(separator, gridBagConstraints1);
    
    if(!c_function.getChoiceOne())
        setAltOneEnabeld(false);
    //pack();
    //setVisable(true);
    
    //=================================================================================================================
    // Alternative two is selected
                
    lblDescriptionAlltTwo = new javax.swing.JLabel();
    lblDescriptionAlltTwo.setFont(new Font("SansSerif", Font.BOLD, 16));
     lblDescriptionAlltTwo.setForeground(Color.BLUE);
    lblDescriptionAlltTwo.setText("Alternative 2");
    lblDescriptionAlltTwo.setToolTipText("Each start and/or stop is associated with a cost");
    JPanel AltTwoPanel = new JPanel();
    AltTwoPanel.setLayout(new GridBagLayout());
    JPanel labelDescriptionPanel = new JPanel();
    labelDescriptionPanel.add(lblDescriptionAlltTwo);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    //gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(5, 10, 15, 0);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
   // getContentPane().add(labelDescriptionPanel, gridBagConstraints1);
    AltTwoPanel.add(labelDescriptionPanel, gridBagConstraints1);
    //Create the check boxes.
    startCostCheckBox = new JCheckBox("Start cost ");
    startCostCheckBox.setMnemonic(KeyEvent.VK_C);
    startCostCheckBox.setSelected(c_function.getStartCostChoice());
   // startCostCheckBox.setEnabled(c_function.getChoiceTwo());
    //Register a listener for the check boxes.
    startCostCheckBox.addItemListener(new QItemListener());
    
    startCostOfFisrtTimestepCheckBox = new JCheckBox("Start cost for the first timestep");
    startCostOfFisrtTimestepCheckBox.setMnemonic(KeyEvent.VK_C);
    startCostOfFisrtTimestepCheckBox.setSelected(c_function.getStartCostofFirstTimestepChoice());
    startCostOfFisrtTimestepCheckBox.addItemListener(new QItemListener());
    startCostOfFisrtTimestepCheckBox.setEnabled(c_function.getStartCostChoice());
    
    stopCostCheckBox = new JCheckBox("Stop cost ");
    stopCostCheckBox.setMnemonic(KeyEvent.VK_C);
    stopCostCheckBox.setSelected(c_function.getStopCostChoice());
    stopCostCheckBox.addItemListener(new QItemListener());
  //  stopCostCheckBox.setEnabled(c_function.getChoiceTwo());
    
    
    labelOneAltTwo= new JLabel("Start cost :        ");
     
    startCostField = new PositiveNumberField();
     if(c_function.getChoiceTwo())
     {
       startCostField.setText(new Float(c_function.getStartCostValue()).toString());
       //startCostField.setEditable(true);
       //startCostCheckBox.setEnabled(true);
     }
     else
     {
         startCostField.setText("0.0");
     }
    //startCostField = new JTextField(10);
    startCostField.setToolTipText("Start cost");
    labelOneAltTwo.setEnabled(c_function.getStartCostChoice());
    startCostField.setEnabled(c_function.getStartCostChoice());
    
    labelThreeAltTwo = new JLabel("Stop cost :         ");
    stopCostField = new PositiveNumberField();
    if(c_function.getChoiceTwo())
     {
       stopCostField.setText(new Float(c_function.getStopCostValue()).toString());
       //stopCostField.setEditable(true);
       //stopCostCheckBox.setEnabled(true);
     }
     else
     {
         stopCostField.setText("0.0");
     }
    stopCostField.setToolTipText("Stop cost");
    labelThreeAltTwo.setEnabled(c_function.getStopCostChoice());
    stopCostField.setEnabled(c_function.getStopCostChoice()); 
    
    stopCostOfFisrtTimestepCheckBox = new JCheckBox("Stop cost for the last timestep");
    stopCostOfFisrtTimestepCheckBox.setMnemonic(KeyEvent.VK_C);
    stopCostOfFisrtTimestepCheckBox.setSelected(c_function.getStopCostofFirstTimestepChoice());
    stopCostOfFisrtTimestepCheckBox.addItemListener(new QItemListener());
    stopCostOfFisrtTimestepCheckBox.setEnabled(c_function.getStopCostChoice());
    
    labelFourAltTwo= new JLabel("Operating cost:");
    operateCostField =new PositiveNumberField();
    operateCostField.setToolTipText("Operating cost");
    if(c_function.getChoiceTwo())
     {
       operateCostField.setText(new Float(c_function.getOperateCostValue()).toString());  
     }
     else
     {
         operateCostField.setText("0.0");
     }
    JPanel checkPanelAllTwo = new JPanel();
    JPanel startCostOfFisrtTimestepPanel = new JPanel();
    JPanel labelOnePanelAllTwo = new JPanel();
    JPanel llabeTwoPanelAllTwo = new JPanel();
    JPanel stopCostOfFisrtTimestepPanel = new JPanel();
    JPanel llabeThreePanelAllTwo = new JPanel();
    //JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
  
    checkPanelAllTwo.add(startCostCheckBox);
    checkPanelAllTwo.add(stopCostCheckBox);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(3, 5, 3, 0);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    AltTwoPanel.add(checkPanelAllTwo, gridBagConstraints1);
    //getContentPane().add(checkPanelAllTwo, gridBagConstraints1);
    
    startCostOfFisrtTimestepPanel.add(startCostOfFisrtTimestepCheckBox);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(3, 5, 3, 0);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    //getContentPane().add(startCostOfFisrtTimestepPanel, gridBagConstraints1);
    AltTwoPanel.add(startCostOfFisrtTimestepPanel, gridBagConstraints1);
    // Add rate label and rate field
    labelOnePanelAllTwo.add(labelOneAltTwo);
    labelOnePanelAllTwo.add(startCostField);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    //gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(3, 10, 3, 0);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    //getContentPane().add(labelOnePanelAllTwo, gridBagConstraints1);
    AltTwoPanel.add(labelOnePanelAllTwo, gridBagConstraints1);
    
    stopCostOfFisrtTimestepPanel.add(stopCostOfFisrtTimestepCheckBox);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(3, 5, 3, 0);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    //getContentPane().add(stopCostOfFisrtTimestepPanel, gridBagConstraints1);
    AltTwoPanel.add(stopCostOfFisrtTimestepPanel, gridBagConstraints1);
    llabeTwoPanelAllTwo.add(labelThreeAltTwo);
    llabeTwoPanelAllTwo.add(stopCostField);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(3, 10, 3, 0);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    //getContentPane().add(llabeTwoPanelAllTwo, gridBagConstraints1);
    AltTwoPanel.add(llabeTwoPanelAllTwo, gridBagConstraints1);
    llabeThreePanelAllTwo.add(labelFourAltTwo);
    llabeThreePanelAllTwo.add(operateCostField);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    //gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(3, 10, 3, 0);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    //getContentPane().add(llabeThreePanelAllTwo, gridBagConstraints1);
    AltTwoPanel.add(llabeThreePanelAllTwo, gridBagConstraints1);
    //AltOneTowPanel.add(AltTwoPanel);
    // minFlowAltTwoLabel, minFlowAltTwoField;
    minFlowAltTwoLabel= new JLabel("Minimum flow:  ");
    minFlowAltTwoField = new PositiveNumberField();
    minFlowAltTwoField.setToolTipText("Minimum flow");
    if(c_function.getChoiceTwo())
     {
       minFlowAltTwoField.setText(new Float(c_function.getMinimumFlowAltTwo()).toString());  
     }
     else
     {
         minFlowAltTwoField.setText("0.0");
     }
    
    JPanel miniFlowPanel = new JPanel();
    miniFlowPanel.add(minFlowAltTwoLabel);
    miniFlowPanel.add(minFlowAltTwoField);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(3, 10, 3, 0);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    //getContentPane().add(mimimumFlowPanel, gridBagConstraints1);
    AltTwoPanel.add(miniFlowPanel, gridBagConstraints1);
    
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    //gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(3, 0, 3, 105);
    //gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    AltThreeFourPanel.add(AltTwoPanel, gridBagConstraints1);
    if(!c_function.getChoiceTwo())
    setAltTwoEnabeld(false);
    

//System.out.println("Alternative 3 is selected");
   
    //=================================================================================================================     
     //System.out.println("Alternative 3 is selected");
     //GridBagConstraints gridBagConstraints1;    
    JPanel AltThreePanel = new JPanel();    
    AltThreePanel.setLayout(new GridBagLayout()); 
    
    lblDescriptionAlltThree = new javax.swing.JLabel();
    lblDescriptionAlltThree.setFont(new Font("SansSerif", Font.BOLD, 16));
    lblDescriptionAlltThree.setForeground(Color.BLUE);
    lblDescriptionAlltThree.setText("Alternative 3");
    lblDescriptionAlltThree.setToolTipText("In this alternative the number of starts and stops are limited");
    
    JPanel labelDescriptionPanelAlltThree = new JPanel();
    labelDescriptionPanelAlltThree.add(lblDescriptionAlltThree);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    //gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(5, 10, 5, 30);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    //getContentPane().add(labelDescriptionPanelAlltThree, gridBagConstraints1);
    AltThreePanel.add(labelDescriptionPanelAlltThree, gridBagConstraints1);
    
    startAltThreeCB = new JCheckBox("Start limitation");
    startAltThreeCB.setSelected(c_function.getStartAltThreeCB());
    //Register a listener for the check boxes.
    startAltThreeCB.addItemListener(new QItemListener());
    
    stopAltThreeCB = new JCheckBox("Stop limitation");
    stopAltThreeCB.setSelected(c_function.getStopAltThreeCB());
    //Register a listener for the check boxes.
    stopAltThreeCB.addItemListener(new QItemListener());
    
    startFisrtTimestepAltThreeCB = new JCheckBox("First time step is included");    
    startFisrtTimestepAltThreeCB.setSelected(c_function.getStartFirstTimestepAltThreeCB());
    //Register a listener for the check boxes.
    startFisrtTimestepAltThreeCB.addItemListener(new QItemListener());
    startFisrtTimestepAltThreeCB.setEnabled(c_function.getStartAltThreeCB());
    
    stopLastTimestepAltThreeCB = new JCheckBox("Last time step is included ");
    stopLastTimestepAltThreeCB.setSelected(c_function.getStopLastTimestepAltThreeCB());
    //Register a listener for the check boxes.
    stopLastTimestepAltThreeCB.addItemListener(new QItemListener());
    stopLastTimestepAltThreeCB.setEnabled(c_function.getStopAltThreeCB());

    JPanel checkPanelAltThree = new JPanel();
    checkPanelAltThree.add(startAltThreeCB); 
    checkPanelAltThree.add(stopAltThreeCB);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 0);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    AltThreePanel.add(checkPanelAltThree, gridBagConstraints1);

    JPanel startFisrtTimestepAltThreePanel = new JPanel();
    startFisrtTimestepAltThreePanel.add(startFisrtTimestepAltThreeCB);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 50);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    AltThreePanel.add(startFisrtTimestepAltThreePanel, gridBagConstraints1);

    JPanel stopLastTimestepAltThreePanel = new JPanel();
    stopLastTimestepAltThreePanel.add(stopLastTimestepAltThreeCB);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 50);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    AltThreePanel.add(stopLastTimestepAltThreePanel, gridBagConstraints1);

    
    labelThreeAltThree = new JLabel("Operating cost:");
    fieldTwoAltThree = new PositiveNumberField();
    fieldTwoAltThree.setToolTipText("Operating cost:");
   /* if(c_function.getChoiceThree())
     {
       fieldOneAltThree.setText(new Float(c_function.getPercentageOfPreviousFlow3()).toString());  
     }
     else
     {
         fieldOneAltThree.setText("0.0");
     }*/
     if(c_function.getChoiceThree())
     {
       fieldTwoAltThree.setText(new Float(c_function.getOperateCostValue3()).toString());  
     }
     else
     {
         fieldTwoAltThree.setText("0.0");
     }
    //JPanel labelOnePanelAllThree = new JPanel();
    JPanel llabeTwoPanelAllThree = new JPanel();
 
    llabeTwoPanelAllThree.add(labelThreeAltThree);
    llabeTwoPanelAllThree.add(fieldTwoAltThree);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(2, 10, 2, 30);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    //getContentPane().add(llabeTwoPanelAllThree, gridBagConstraints1);
    AltThreePanel.add(llabeTwoPanelAllThree, gridBagConstraints1);
    
    minFlowLabel= new JLabel("Minimum flow: ");
    minFlowField = new PositiveNumberField();
    fieldTwoAltThree.setToolTipText("Minimum flow");
    if(c_function.getChoiceThree())
     {
       minFlowField.setText(new Float(c_function.getMinimumFlow()).toString());  
     }
     else
     {
         minFlowField.setText("0.0");
     }
    
    JPanel mimimumFlowPanel = new JPanel();
    mimimumFlowPanel.add(minFlowLabel);
    mimimumFlowPanel.add(minFlowField);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(2, 10, 2, 30);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    //getContentPane().add(mimimumFlowPanel, gridBagConstraints1);
    AltThreePanel.add(mimimumFlowPanel, gridBagConstraints1);
    pnlFlowSettings = new javax.swing.JPanel();
    pnlFlowSettings.setLayout(new java.awt.GridBagLayout());
    GridBagConstraints gbc;
    
    pnlRadioButton = new javax.swing.JPanel();
    pnlRadioButton.setLayout(new java.awt.GridBagLayout());

    //Free/Less/equal/greater radio button
    radioLt = new JRadioButton("R <");
    radioLt.setActionCommand("LT");
    radioLt.setSelected(c_function.getRadioButtonLt()); 
    
    radioEq = new JRadioButton("R =");
    radioEq.setActionCommand("EQ");
    radioEq.setSelected(c_function.getRadioButtonEq());
    
    radioGt = new JRadioButton("R >");
    radioGt.setActionCommand("GT");
    radioGt.setSelected(c_function.getRadioButtonGt());
    
    radioLtGt = new JRadioButton("< R <");
    radioLtGt.setActionCommand("LTGT");
    radioLtGt.setSelected(c_function.getRadioButtonLtGt());
    
    OperatorListener ol  = new OperatorListener();
    radioLt.addActionListener(ol);
    radioGt.addActionListener(ol);
    radioEq.addActionListener(ol);
    radioLtGt.addActionListener(ol);

    OperatorButtons = new ButtonGroup();
    OperatorButtons.add(radioLt);
    OperatorButtons.add(radioEq);
    OperatorButtons.add(radioGt);
    OperatorButtons.add(radioLtGt);

    gbc = new java.awt.GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.insets = new java.awt.Insets(0, 5, 0, 0);
    gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
    pnlRadioButton.add(radioLt, gbc);

    gbc = new java.awt.GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.insets = new java.awt.Insets(0, 5, 0, 0);
    gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
    pnlRadioButton.add(radioEq, gbc);

    gbc = new java.awt.GridBagConstraints();
    gbc.gridx = 3;
    gbc.gridy = 0;
    gbc.insets = new java.awt.Insets(0, 5, 0, 0);
    gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
    pnlRadioButton.add(radioGt, gbc);

    gbc = new java.awt.GridBagConstraints();
    gbc.gridx = 4;
    gbc.gridy = 0;
    gbc.insets = new java.awt.Insets(0, 5, 0, 0);
    gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
    pnlRadioButton.add(radioLtGt, gbc);
    
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(2, 5, 2, 30);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    //getContentPane().add(pnlRadioButton, gridBagConstraints1);
    AltThreePanel.add(pnlRadioButton, gridBagConstraints1);    
    //Limit spinbox 1
    spinLimit1 = new PositiveNumberField((float) 0.0, 10);
    spinLimit1.setEditable(true);
     if(!c_function.getOperator().equals(""))
      spinLimit1.setText(new Long(c_function.getLimit1()).toString());  
    gbc = new java.awt.GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.insets = new java.awt.Insets (10, 5, 0, 0);
    gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
    pnlFlowSettings.add(spinLimit1,gbc);

    //Label between spinbox1 and 2
    lblSpin = new JLabel();
    lblSpin.setText("        <X<");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.insets = new java.awt.Insets (12, 95, 5, 0);
    gbc.anchor = GridBagConstraints.NORTHWEST;
    pnlFlowSettings.add(lblSpin, gbc);

    //Limit spinbox 2
    spinLimit2 = new PositiveNumberField( (float)0.0, 10);
    spinLimit2.setEditable(true);
    if(!c_function.getOperator().equals(""))
      spinLimit2.setText(new Long(c_function.getLimit2()).toString());  
    gbc = new java.awt.GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.insets = new java.awt.Insets (10, 144, 5, 5);
    gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
    pnlFlowSettings.add(spinLimit2,gbc);
    
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(2, 10, 2, 30);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    //getContentPane().add(pnlFlowSettings, gridBagConstraints1);
    AltThreePanel.add(pnlFlowSettings, gridBagConstraints1);  
     if(!c_function.getChoiceThree())
        setAltThreeEnabeld(false);
    //updateChoiceThreeSettings();
    updateSpin();
    AltOneTowPanel.add(AltThreePanel);
    
    JSeparator separator2 = new JSeparator(JSeparator.VERTICAL);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    //gridBagConstraints1.gridx = 0;
    //gridBagConstraints1.gridwidth = 2;
    //gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 0);
    gridBagConstraints1.fill = java.awt.GridBagConstraints.VERTICAL;
    AltThreeFourPanel.add(separator2, gridBagConstraints1);  
   
//System.out.println("Alternative 4 is selected");
  // =======================================================================================================0      
  // Alternative Four is selected
    
    JPanel AltFourPanel = new JPanel();    
    AltFourPanel.setLayout(new GridBagLayout()); 
    
    lblDescriptionAlltFour = new javax.swing.JLabel();
    lblDescriptionAlltFour.setFont(new Font("SansSerif", Font.BOLD, 16));
    lblDescriptionAlltFour.setForeground(Color.BLUE);
    lblDescriptionAlltFour.setText("Alternative 4");
    lblDescriptionAlltFour.setToolTipText("Each start or stop is associated with a waste flow");
    
    JPanel labelDescriptionPanelAlltFour = new JPanel();
    labelDescriptionPanelAlltFour.add(lblDescriptionAlltFour);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    //gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 50);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    //getContentPane().add(labelDescriptionPanelAlltFour, gridBagConstraints1);
    AltFourPanel.add(labelDescriptionPanelAlltFour, gridBagConstraints1);

    radioBP= new JRadioButton("Batch process");
    radioBP.setActionCommand("BP");
    radioBP.setSelected(c_function.getBatchWestProcess());

    radioCP = new JRadioButton("Continuous process");
    radioCP.setActionCommand("CP");
    radioCP.setSelected(c_function.getContinuousWestProcess());
    if((!radioCP.isSelected())  && (!radioBP.isSelected()))
    {
    radioBP.setSelected(true);
    c_function.setBatchWestProcess(true);
    }

    ProcessListener pl  = new ProcessListener();
    radioBP.addActionListener(pl);
    radioCP.addActionListener(pl);

    ProcessButtons = new ButtonGroup();
    ProcessButtons.add(radioBP);
    ProcessButtons.add(radioCP);

    JPanel processPanel = new JPanel();
    processPanel.add(radioBP);
    processPanel.add(radioCP);

    gridBagConstraints1 = new java.awt.GridBagConstraints();
    //gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 10);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    //getContentPane().add(labelDescriptionPanelAlltFour, gridBagConstraints1);
    AltFourPanel.add(processPanel, gridBagConstraints1);


//..................................................
    //Create the check boxes.
    startWasteCheckBox = new JCheckBox("Start waste ");
    startWasteCheckBox.setSelected(c_function.getStartWasteChoice());
    //Register a listener for the check boxes.
    startWasteCheckBox.addItemListener(new QItemListener());
    
    stopWasteCheckBox = new JCheckBox("Stop waste ");
    //stopWasteCheckBox.setMnemonic(KeyEvent.VK_C);
    stopWasteCheckBox.setSelected(c_function.getStopWasteChoice());
    stopWasteCheckBox.addItemListener(new QItemListener());
    
    startWasteOfFisrtTimestepCheckBox = new JCheckBox("Start waste for the first timestep");
    startWasteOfFisrtTimestepCheckBox.setToolTipText("If the process starts in the first time step, \nthen there is a waste if a percentage value ore fixed value is entered.");
    startWasteOfFisrtTimestepCheckBox.setSelected(c_function.getStartWasteOfFirstTimestepChoice());
    startWasteOfFisrtTimestepCheckBox.addItemListener(new QItemListener());
    startWasteOfFisrtTimestepCheckBox.setEnabled(c_function.getStartWasteChoice());
    
    stopWasteOfLasttTimestepCheckBox = new JCheckBox("Stop waste for the last timestep");
    stopWasteOfLasttTimestepCheckBox.setToolTipText("If the process stops at the last time step, \nthen there is a waste if a percentage value ore fixed value is entered.");
    stopWasteOfLasttTimestepCheckBox.setSelected(c_function.getStopWasteOfLastTimestepChoice());
    stopWasteOfLasttTimestepCheckBox.addItemListener(new QItemListener());
    stopWasteOfLasttTimestepCheckBox.setEnabled(c_function.getStopWasteChoice());
    percentagevalueCheckBox = new JCheckBox("Percentage value");
    //percentagevalueCheckBox.setMnemonic(KeyEvent.VK_C);
    percentagevalueCheckBox.setSelected(c_function.getPercentagevalueChoice());
    percentagevalueCheckBox.addItemListener(new QItemListener());
    //labelOneAltFour = new JLabel("Percentage value :");
    percentageValueField = new PositiveNumberField();
     if(c_function.getChoiceFour())
     {
       percentageValueField.setText(new Float(c_function.getWatsePercentageValue()).toString());
     }
     else
     {
         percentageValueField.setText("0.0");
     }
    percentageValueField.setToolTipText("Percentage value of the waste");
    percentageValueField.setEnabled(c_function.getPercentagevalueChoice());
    
   //labelTwoAltFour = new JLabel("Fixed value :            ");
    fixedvalueCheckBox = new JCheckBox("Fixed value :          ");
    //fixedvalueCheckBox.setMnemonic(KeyEvent.VK_C);
    fixedvalueCheckBox.setSelected(c_function.getFixedvalueChoice());
    fixedvalueCheckBox.addItemListener(new QItemListener());
    fixedValueField = new PositiveNumberField();
    if(c_function.getChoiceFour())
     {
       fixedValueField.setText(new Float(c_function.getWasteFixedValue()).toString());
    }
       
     else
     {
         fixedValueField.setText("0.0");
     }
    fixedValueField.setToolTipText("Fixed value of the waste");
    fixedValueField.setEnabled(c_function.getFixedvalueChoice());
    
    labelThreeAltFour= new JLabel("Waste flow :                ");

    // Add checkboxes
    JPanel checkPanelAllFour = new JPanel();
    checkPanelAllFour.add(startWasteCheckBox);
    checkPanelAllFour.add(stopWasteCheckBox);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    //gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 50);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    //getContentPane().add(checkPanelAllFour, gridBagConstraints1);
    AltFourPanel.add(checkPanelAllFour, gridBagConstraints1);
    JPanel startWestOfFisrtTimestepPanel = new JPanel();
    startWestOfFisrtTimestepPanel.add(startWasteOfFisrtTimestepCheckBox);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    //gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 50);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    //getContentPane().add(startWestOfFisrtTimestepPanel, gridBagConstraints1);
    AltFourPanel.add(startWestOfFisrtTimestepPanel, gridBagConstraints1);
    JPanel stopWestOfLastTimestepPanel = new JPanel();
    stopWestOfLastTimestepPanel.add(stopWasteOfLasttTimestepCheckBox);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    //gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 50);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    //getContentPane().add(stopWestOfLastTimestepPanel, gridBagConstraints1);
    AltFourPanel.add(stopWestOfLastTimestepPanel, gridBagConstraints1);
    // ADD TEXTFIELD
    JPanel percentageValuePanel = new JPanel();
//    percentageValuePanel.add(labelOneAltFour);
    percentageValuePanel.add(percentagevalueCheckBox);
    percentageValuePanel.add(percentageValueField);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    //gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 50);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    //getContentPane().add(percentageValuePanel, gridBagConstraints1);
    AltFourPanel.add(percentageValuePanel, gridBagConstraints1);
    
    JPanel fixedValuePanel = new JPanel();
    //fixedValuePanel.add(labelTwoAltFour);
    fixedValuePanel.add(fixedvalueCheckBox);
    fixedValuePanel.add(fixedValueField);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    //gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 50);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    //getContentPane().add(fixedValuePanel, gridBagConstraints1);
    AltFourPanel.add(fixedValuePanel, gridBagConstraints1);
    
    operatcostAltFourLabel = new JLabel("Operating cost:           ");
    operatcostAltFourField = new PositiveNumberField();
    operatcostAltFourField.setToolTipText("Operating cost");
    if(c_function.getChoiceFour())
     {
       operatcostAltFourField.setText(new Float(c_function.getOperateCostAltFour()).toString());  
     }
     else
     {
         operatcostAltFourField.setText("0.0");
     }
    
    JPanel operatcostAltFourPanel = new JPanel();
 
    operatcostAltFourPanel.add(operatcostAltFourLabel);
    operatcostAltFourPanel.add(operatcostAltFourField);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(0, 10, 0, 0);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    AltFourPanel.add(operatcostAltFourPanel, gridBagConstraints1);
    
    minFlowAltFourLabel= new JLabel("Minimum flow:             ");
    minFlowAltFourField = new PositiveNumberField();
    minFlowAltFourField.setToolTipText("Minimum flow");
    if(c_function.getChoiceFour())
     {
       minFlowAltFourField.setText(new Float(c_function.getMinimumFlowAltFour()).toString());  
     }
     else
     {
         minFlowAltFourField.setText("0.0");
     }
    
    JPanel minimumFlowAltFourPanel = new JPanel();
    minimumFlowAltFourPanel.add(minFlowAltFourLabel);
    minimumFlowAltFourPanel.add(minFlowAltFourField);
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(0, 10, 0, 0);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    //getContentPane().add(minimumFlowAltFourPanel, gridBagConstraints1);
    AltFourPanel.add(minimumFlowAltFourPanel, gridBagConstraints1);

    
    JPanel outFlowPanel = new JPanel();
    lstOutFlow = new JList();
    lstOutFlow.addListSelectionListener(new ListSelectionListener() {
	      public void valueChanged(ListSelectionEvent e)
	      {
		  newFlowSelected();
	      }
	  });
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    //gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 0);
    gridBagConstraints4 = new java.awt.GridBagConstraints ();
    gridBagConstraints4.gridx = 0;
    outFlowPanel.add (labelThreeAltFour, gridBagConstraints1);
    gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
    scrollPanel = new JScrollPane(lstOutFlow);
    scrollPanel.setPreferredSize(new Dimension(100, 40));
    outFlowPanel.add (scrollPanel, gridBagConstraints4);
    gridBagConstraints4 = new java.awt.GridBagConstraints ();
    gridBagConstraints4.gridx = 0;
    gridBagConstraints4.insets = new java.awt.Insets(0, 10, 0, 30);
    gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
    //getContentPane().add(outFlowPanel, gridBagConstraints4);
    AltFourPanel.add(outFlowPanel, gridBagConstraints4);
    
    AltThreeFourPanel.add(AltFourPanel);
    
    if(!c_function.getChoiceFour())
        setAltFourEnabeld(false);
    
    
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.insets = new java.awt.Insets(0, 5, 0, 0);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
    getContentPane().add(AltOneTowPanel, gridBagConstraints1);
    
    GridBagConstraints gridBagConstraints1n = new java.awt.GridBagConstraints();
    gridBagConstraints1n.gridx = 0;
    gridBagConstraints1n.gridwidth = 2;
    gridBagConstraints1n.fill = java.awt.GridBagConstraints.HORIZONTAL;
    getContentPane().add(AltThreeFourPanel, gridBagConstraints1n);
   
    JSeparator seplin = new javax.swing.JSeparator();
    gridBagConstraints1 = new java.awt.GridBagConstraints();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridwidth = 0;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    getContentPane().add(seplin, gridBagConstraints1);
    
    gridBagConstraints1n = new java.awt.GridBagConstraints();
    gridBagConstraints1n.gridx = 0;
    gridBagConstraints1n.gridwidth = 0;
    gridBagConstraints1n.fill = java.awt.GridBagConstraints.HORIZONTAL;
    getContentPane().add(AltThreeFourPanel, gridBagConstraints1n);
    
    JSeparator lnsp = new javax.swing.JSeparator();
    gridBagConstraints1 = new java.awt.GridBagConstraints ();
    gridBagConstraints1.gridx = 0;
    gridBagConstraints1.gridwidth = 1;
    gridBagConstraints1.insets = new java.awt.Insets(0, 0, 4, 0);
    gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
    getContentPane ().add (lnsp, gridBagConstraints1);
    
   

   JPanel okcancelPanel = new JPanel();
   btnOk = new JButton("OK");
   btnCancel = new JButton("Cancel");
   okcancelPanel.add(btnOk);
   okcancelPanel.add(btnCancel);
   //JSeparator sepln = new javax.swing.JSeparator();
   GridBagConstraints gridBagConstra = new java.awt.GridBagConstraints();
   gridBagConstra.gridx = 0;
   gridBagConstra.gridwidth = 2;
   gridBagConstra.fill = java.awt.GridBagConstraints.HORIZONTAL;
   getContentPane().add(okcancelPanel, gridBagConstra);
    
  btnCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        closeDialog(null);
      }
    });
    
    btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnOkActionPerformed();
			}
    });
    
    addWindowListener(new java.awt.event.WindowAdapter() { public void windowClosing(java.awt.event.WindowEvent evt) {
                        closeDialog(evt);}});
   
    setSize(500,700);
    //Dimension d = getToolkit().getScreenSize();
    //Rectangle b = getBounds();
    //setLocation((d.width-b.width)/2, (d.height-b.height)/2);
    //setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    
    //this.setPreferredSize(new Dimension(500, 500));
//this.setVisible(true);
    pack();
    
}

/**
     * Called when one of the Process radio buttons is pressed
     */
private class ProcessListener implements ActionListener {
public void actionPerformed(ActionEvent e)
    {
    String action = e.getActionCommand();
    if (action.equals("BP"))
    {
    c_function.setBatchWestProcess(true);
    c_function.setContinuousWestProcess(false);
    }
    else if (action.equals("CP"))
    {
    c_function.setContinuousWestProcess(true);
    c_function.setBatchWestProcess(false);
    }
    }
}


/**
     * Called when one of the operator radio buttons is pressed
     */
    private class OperatorListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    String action = e.getActionCommand();
	   
	    if (action.equals("LTGT"))
            {
		c_function.setOperator("LESS_GREATER");
                c_function.setLimit1(0);
                c_function.setLimit2(0);
            }
	    else if (action.equals("LT"))
            {
		c_function.setOperator("LESS");
                c_function.setLimit1(0);
                c_function.setLimit2(0);
            }
	    else if (action.equals("GT"))
            {
		c_function.setOperator("GREATER");
                c_function.setLimit1(0);
                c_function.setLimit2(0);
            }
	    else if (action.equals("EQ"))
            {
		c_function.setOperator("EQUAL");
                c_function.setLimit1(0);
                c_function.setLimit2(0);
            }
            updateChoiceThreeSettings();
            updateSpin();
	    
	}

    }
private void updateResources()
    {
	c_resources = c_gui.getResources();

	listResource.setListData(c_resources);
	if (c_function.getResource() != null)
	    listResource.setSelectedIndex(c_resources.indexOf(c_gui.getResource(c_function.getResource())));
    }
    
private void newResourceSelected()
  {
      Resource resource = (Resource)listResource.getSelectedValue();
  }

private void updateFlow()
    {
      Flow[] outFlow = c_gui.getOutFlows(c_nodeID);
      Vector outFlowVectorString = new Vector();
       
      if(outFlow == null )
      {
	  outFlowVectorString.add("No flow");  
          c_noOutFlow = true;
      }
      else
      {
        for( int i = 0; i < outFlow.length; i++)
        {
	outFlowVectorString.add(outFlow [i].toString());
	}
      }
    
      lstOutFlow.setListData(outFlowVectorString);
	if(c_function.getOutFlow() != null)
	    lstOutFlow.setSelectedIndex(outFlowVectorString.indexOf(c_function. getOutFlow ()));
    }

//
private void newFlowSelected()
  {
      String flow = (String)lstOutFlow.getSelectedValue();
  }    


public class QItemListener implements ItemListener
 {
   public void itemStateChanged(ItemEvent e) 
   {
        Object source = e.getItemSelectable();

        if (source == alltOneCheckBox) 
        {
            //if selected
            if(alltOneCheckBox.getSelectedObjects()!=null)
            {
                c_choiceOneSelected = true;
                setAltOneEnabeld(true);
                
            }
            else 
            {
                c_choiceOneSelected = false;
                setAltOneEnabeld(false);
                //if(c_choiceThreeSelected)
                  // setAltThreeEnabeld(true); 
                setAltOneDefaultValue();
            }
        }
        
        else if(source == stopAltOneCB) 
        {
             if(stopAltOneCB.getSelectedObjects()!=null)
             {
                //c_startCostSelected = true;
                 stopLastTimestepAltOneCB.setEnabled(true);
             }
             else
             {
                 stopLastTimestepAltOneCB.setEnabled(false);
                 c_function.setStopAltOneCB(false);
                 stopLastTimestepAltOneCB.setSelected(false);
                 c_function.setStopLastTimestepAltOneCB(false);
                 //c_startCostSelected = false;
             }
        }
        
        else if(source == startAltOneCB) 
        {
             if(startAltOneCB.getSelectedObjects()!=null)
             {
                //c_stopCostSelected = true;
                 startFisrtTimestepAltOneCB.setEnabled(true);
             }
             else
             {
                 //c_stopCostSelected = false;
                  startFisrtTimestepAltOneCB.setEnabled(false);
                 c_function.setStartAltOneCB(false);
                 startFisrtTimestepAltOneCB.setSelected(false);
                 c_function.setStartFirstTimestepAltOneCB(false);
             }
        }
        
       else if (source == alltTwoCheckBox) 
        {
            if(alltTwoCheckBox.getSelectedObjects()!=null)
            {
                c_choiceTowSelected = true;
                setAltTwoEnabeld(true);
            }
            else
            {
                c_choiceTowSelected=false;
                setAltTwoEnabeld(false);
                setStartCostEnabeld(false);
                setStopCostEnabeld(false);
                stopCostCheckBox.setSelected(false);
                startCostCheckBox.setSelected(false);
                setAltTwoDefaultValue();
            }
        }
        
        else if(source == startCostCheckBox) 
        {
             if(startCostCheckBox.getSelectedObjects()!=null)
             {
                //c_startCostSelected = true;
                 setStartCostEnabeld(true);
             }
             else
             {
                 setStartCostEnabeld(false);
                 //c_startCostSelected = false;
             }
        }
        
        else if(source == stopCostCheckBox) 
        {
             if(stopCostCheckBox.getSelectedObjects()!=null)
             {
                //c_stopCostSelected = true;
                setStopCostEnabeld(true);
             }
             else
             {
                 //c_stopCostSelected = false;
                 setStopCostEnabeld(false);
                 
                 c_function.setStopCostValue(0.f);
             }
        }
        
        else if (source == alltThreeCheckBox) 
        {
             if(alltThreeCheckBox.getSelectedObjects()!=null)
             {
                c_choiceThreeSelected = true;
                setAltThreeEnabeld(true);
             }
             else
             {
                 c_choiceThreeSelected = false;
                 resetAll(false);
                 setAltThreeEnabeld(false);
              //   if(c_choiceOneSelected)
                //   setAltOneEnabeld(true); 
                
             }
        }
        
        else if(source == stopAltThreeCB) 
        {
             if(stopAltThreeCB.getSelectedObjects()!=null)
             {
                //c_startCostSelected = true;
                 stopLastTimestepAltThreeCB.setEnabled(true);
             }
             else
             {
                 stopLastTimestepAltThreeCB.setEnabled(false);
                 c_function.setStopAltThreeCB(false);
                 stopLastTimestepAltThreeCB.setSelected(false);
                 c_function.setStopLastTimestepAltThreeCB(false);
                 //c_startCostSelected = false;
             }
        }
        
        else if(source == startAltThreeCB) 
        {
             if(startAltThreeCB.getSelectedObjects()!=null)
             {
                //c_stopCostSelected = true;
                 startFisrtTimestepAltThreeCB.setEnabled(true);
             }
             else
             {
                 //c_stopCostSelected = false;
                  startFisrtTimestepAltThreeCB.setEnabled(false);
                 c_function.setStartAltThreeCB(false);
                 startFisrtTimestepAltThreeCB.setSelected(false);
                 c_function.setStartFirstTimestepAltThreeCB(false);
             }
        }
        
        else if (source == alltFourCheckBox) 
        {
           if(alltFourCheckBox.getSelectedObjects()!=null)
           {
                c_choiceFourSelected = true;
                setAltFourEnabeld(true);
           }
           else
           {
               resetAltFoure(); 
           }
        }
        
        else if(source == startWasteCheckBox) 
        {
             if(startWasteCheckBox.getSelectedObjects()!=null)
             {
                //c_stopCostSelected = true;
                startWasteOfFisrtTimestepCheckBox.setEnabled(true);
             }
             else
             {
                 //c_stopCostSelected = false;
                 c_function.setStartWasteChoice(false);
                 c_function.setStartWasteOfFirstTimestepChoice(false);
                 startWasteOfFisrtTimestepCheckBox.setSelected(false);
                 startWasteOfFisrtTimestepCheckBox.setEnabled(false);
             }
        }
        
        else if(source == stopWasteCheckBox) 
        {
             if(stopWasteCheckBox.getSelectedObjects()!=null)
             {
                //c_stopCostSelected = true;
                stopWasteOfLasttTimestepCheckBox.setEnabled(true);
             }
             else
             {
                 //c_stopCostSelected = false;
                 c_function.setStopWasteChoice(false);
                 c_function.setStopWasteOfLastTimestepChoice(false);
                 stopWasteOfLasttTimestepCheckBox.setSelected(false);
                 stopWasteOfLasttTimestepCheckBox.setEnabled(false);
             }
        }
        
        //percentagevalueCheckBox
       else if(source == percentagevalueCheckBox) 
        {
             if(percentagevalueCheckBox.getSelectedObjects()!=null)
             {
                //c_stopCostSelected = true;
                percentageValueField .setEnabled(true);
                fixedValueField .setEnabled(false);
                fixedvalueCheckBox.setSelected(false);
                c_function.setFixedvalueChoice(false);
                c_function.setWatseFixedValue(0);       
             }
             else
             {
                 //c_stopCostSelected = false;
               percentageValueField .setEnabled(false);
               fixedValueField .setEnabled(true);
               percentagevalueCheckBox.setSelected(false);
               percentageValueField.setText("0.0");
               c_function.setPercentagevalueChoice(false);
               c_function.setWatsePercentageValue(0); //operateCostField
               
             }
        }
        else if(source == fixedvalueCheckBox) 
        {
             if(fixedvalueCheckBox.getSelectedObjects()!=null)
             {
                //c_stopCostSelected = true;
                percentageValueField .setEnabled(false);
                c_function.setPercentagevalueChoice(false);
                c_function.setWatsePercentageValue(0); //operateCostField
                fixedValueField .setEnabled(true);
                percentagevalueCheckBox.setSelected(false);
             }
             else
             {
                 //c_stopCostSelected = false;
               percentageValueField .setEnabled(true);
               fixedValueField .setEnabled(false);
               fixedvalueCheckBox.setSelected(false);
               fixedValueField.setText("0.0");
               c_function.setFixedvalueChoice(false);
               c_function.setWatseFixedValue(0);
             }
        }
        
    }
 }// finish inner class QItemListener


    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) 
    {
    setVisible (false);
    dispose ();
    }
    


public void setAltOneEnabeld(boolean stat)
{
   lblDescriptionAlltOne.setEnabled(stat);
   startAltOneCB.setEnabled(stat);
   stopAltOneCB.setEnabled(stat);
   percentageOfPreviousFlowLabel.setEnabled(stat);
   labelTwoAltOne.setEnabled(stat);
   percentageOfPreviousFlowField.setEnabled(stat);
   thresholdValueLabel .setEnabled(stat);
   thresholdValueField.setEnabled(stat); 
   if(!stat)
   {
    percentageOfPreviousFlowField.setText("0.0"); 
    thresholdValueField.setText("0.0");
   }
   if(!alltOneCheckBox.isSelected())
   {
    startFisrtTimestepAltOneCB.setSelected(false);
    startAltOneCB.setSelected(false);
    stopAltOneCB.setSelected(false);
    startFisrtTimestepAltOneCB.setEnabled(false);
    stopLastTimestepAltOneCB.setSelected(false);
    stopLastTimestepAltOneCB.setEnabled(false);
    
   }
   /*labelFourAltOne.setEnabled(stat);
   fieldThreeAltOne.setEnabled(stat);*/
}
 
public void setAltTwoEnabeld(boolean stat)
{
   lblDescriptionAlltTwo.setEnabled(stat);
   startCostCheckBox.setEnabled(stat);
   stopCostCheckBox.setEnabled(stat);
   labelFourAltTwo.setEnabled(stat);
   operateCostField.setEnabled(stat);
   //minFlowAltTwoLabel, minFlowAltTwoField;
   minFlowAltTwoLabel.setEnabled(stat);
   minFlowAltTwoField.setEnabled(stat);
}
public void setStartCostEnabeld(boolean stat)
{
   labelOneAltTwo.setEnabled(stat);
   if(!stat){
    startCostField.setText("0.0");   
    startCostOfFisrtTimestepCheckBox.setSelected(stat);}
    startCostField.setEnabled(stat);
    startCostOfFisrtTimestepCheckBox.setEnabled(stat);
}

public void setStopCostEnabeld(boolean stat)
{
  
   labelThreeAltTwo.setEnabled(stat);
   if(!stat)
   {
    stopCostField.setText("0.0");  
    stopCostOfFisrtTimestepCheckBox.setSelected(stat);
    c_function.setStopCostChoice(stat);
   }
    stopCostField.setEnabled(stat);
    stopCostOfFisrtTimestepCheckBox.setEnabled(stat);
}

public void setAltThreeEnabeld(boolean stat)
{
   lblDescriptionAlltThree.setEnabled(stat);
  // labelOneAltThree.setEnabled(stat);
   startAltThreeCB.setEnabled(stat);
   stopAltThreeCB.setEnabled(stat);
   radioLt.setEnabled(stat);
   radioEq.setEnabled(stat);
   radioGt.setEnabled(stat);
   radioLtGt.setEnabled(stat);
   
 if(!alltThreeCheckBox.isSelected())
   {
   radioLt.setSelected(false);
   radioEq.setSelected(false);
   radioGt.setSelected(false);
   radioLtGt.setSelected(false);
   spinLimit1.setEnabled(false);
   spinLimit2.setEnabled(false);
   lblSpin.setEnabled(false);
   lblSpin.setVisible(false);
   c_function.setOperator(""); 
   }
   
   String op = c_function.getOperator();
   if(op.equals(""))
   {
   spinLimit1.setEnabled(false);
   spinLimit2.setEnabled(false);
   lblSpin.setEnabled(false);
   lblSpin.setVisible(false);
   }
   else if(op.equals("GREATER") || op.equals("LESS") || op.equals("EQUAL"))
   {
   spinLimit1.setEnabled(false);
   spinLimit2.setEnabled(true);
   lblSpin.setEnabled(true);
   lblSpin.setVisible(true);
   }
   else if(op.equals("LESS_GREATER"))
   {
   spinLimit1.setEnabled(true);
   spinLimit2.setEnabled(true);
   lblSpin.setEnabled(true);
   lblSpin.setVisible(true);
   }
   
   fieldTwoAltThree.setEnabled(stat);
   
   labelThreeAltThree.setEnabled(stat);
   minFlowLabel.setEnabled(stat);
   minFlowField.setEnabled(stat);
   if(alltThreeCheckBox.isSelected())
   {
   updateChoiceThreeSettings();
   updateSpin();
   }
   
}

/**
     * Updates the settings part of the dialog
     */

private void updateChoiceThreeSettings()
    {

    spinLimit1.setText("" + c_function.getLimit1());
    spinLimit2.setText("" + c_function.getLimit2());	

    String op = c_function.getOperator(); 
    if(op.equals("GREATER"))
	    radioGt.setSelected(true);
    else if(op.equals("LESS"))
	    radioLt.setSelected(true);
    else if(op.equals("EQUAL"))
	    radioEq.setSelected(true);
    else if(op.equals("LESS_GREATER"))
	    radioLtGt.setSelected(true);	
    }

/**
     * Updates the limit1 and limit2 spin buttons
     */
    /**
     * Updates the limit1 and limit2 spin buttons
     */
    private void updateSpin()
    {
	if(alltThreeCheckBox.isSelected())
        {
        String op = c_function.getOperator(); 
	if(op.equals("GREATER"))
        {
	    spinLimit1.setVisible(false);
	    spinLimit2.setVisible(true);
            spinLimit2.setEnabled(true);
	    lblSpin.setText("          R > ");
            lblSpin.setVisible(true);
            lblSpin.setEnabled(true);
        }
	else if(op.equals("LESS"))
        {
	    spinLimit1.setVisible(false);
	    spinLimit2.setVisible(true);
            spinLimit2.setEnabled(true);
	    lblSpin.setText("     R <");
            lblSpin.setVisible(true);
            lblSpin.setEnabled(true);
        }
        
	else if(op.equals("EQUAL"))
        {
	    spinLimit1.setVisible(false);
	    spinLimit2.setVisible(true);
            spinLimit2.setEnabled(true);
	    lblSpin.setText("        R =");
            lblSpin.setVisible(true);
            lblSpin.setEnabled(true);
        }
	else if(op.equals("LESS_GREATER"))
        {
	    spinLimit1.setVisible(true);
	    spinLimit2.setVisible(true);
	    spinLimit1.setEnabled(true);
            spinLimit2.setEnabled(true);
            lblSpin.setText("        <R<");
            lblSpin.setVisible(true);
            lblSpin.setEnabled(true);
        }
        }
       /* else
        {
         resetAll(false);
         //setAltThreeEnabeld(false);
        }*/
    }
    
    private void resetAll(boolean set)
    {    
    //startAltThreeCB, stopAltThreeCB, startFisrtTimestepAltThreeCB, stopLastTimestepAltThreeCB;
    startAltThreeCB.setSelected(set);
    stopAltThreeCB.setSelected(set);
    startFisrtTimestepAltThreeCB.setSelected(set);
    stopLastTimestepAltThreeCB.setSelected(set);
    radioLt.setSelected(set);
    radioEq.setSelected(set);
    radioGt.setSelected(set);
    radioLtGt.setSelected(set);
    c_function.setOperator("");
//    fieldOneAltThree.setText("0.0");
    c_function.setChoiceThree(false);
    c_function.setOperateCostValue3(0);
  //  c_function.setPercentageOfPreviousFlow3(0);
    fieldTwoAltThree.setText("0.0");
    minFlowField.setText("0.0");
    spinLimit2.setText("");
    c_function.setLimit2(0);
    c_function.setLimit1(0);
    spinLimit1.setText("");
    }

   
public void setAltFourEnabeld(boolean stat)
{
   lblDescriptionAlltFour.setEnabled(stat);
   radioCP.setEnabled(stat);
   radioBP.setEnabled(stat);
   fixedvalueCheckBox.setEnabled(stat);
   percentagevalueCheckBox.setEnabled(stat);
   labelThreeAltFour.setEnabled(stat);
   startWasteCheckBox.setEnabled(stat);
   stopWasteCheckBox.setEnabled(stat);
   lstOutFlow.setEnabled(stat); 
   operatcostAltFourLabel.setEnabled(stat);
   minFlowAltFourLabel.setEnabled(stat);
   operatcostAltFourField.setEnabled(stat);
   minFlowAltFourField.setEnabled(stat);
   //operatcostAltFourLabel, minFlowAltFourLabel, operatcostAltFourField, minFlowAltFourField; 
}

public void resetAltFoure()
{
  c_choiceFourSelected = false;
  setAltFourEnabeld(false);
  startWasteCheckBox.setSelected(false);
  stopWasteCheckBox.setSelected(false);
  startWasteOfFisrtTimestepCheckBox.setSelected(false);
  stopWasteOfLasttTimestepCheckBox.setSelected(false);
  if(percentagevalueCheckBox.isSelected())
     {
     percentageValueField.setText("0.0");
     percentagevalueCheckBox.setSelected(false);
     percentageValueField.setEnabled(false);
     fixedvalueCheckBox.setSelected(false);
     fixedValueField.setEnabled(false);
     }
  if(fixedvalueCheckBox.isSelected())
     {
     fixedValueField.setText("0.0");
     fixedvalueCheckBox.setSelected(false);
     fixedValueField.setEnabled(false);
     percentagevalueCheckBox.setSelected(false);
     percentageValueField.setEnabled(false);
     }
   setAltFourDefaultValue();
}

public void setAltOneDefaultValue()
{
    c_function.setChoiceOne(false);
    c_function.setPercentageOfPreviousFlow(0.f);
    c_function.setThresholdValue(0.f);
}

public void setAltTwoDefaultValue()
{
    c_function.setChoiceTwo(false);
    c_function.setStartCostChoice(false);
    c_function.setStartCostValue(0.f);
    c_function.setStopCostChoice(false);
    c_function.setStopCostValue(0.f); //operateCostField
    c_function.setOperateCostValue(0.f);
    startCostField.setText("0.0");
    stopCostField.setText("0.0");
    operateCostField.setText("0.0");
   //minFlowAltTwoLabel, minFlowAltTwoField;
    minFlowAltTwoField.setText("0.0");
}
public void setAltFourDefaultValue()
{
    c_function.setChoiceFour(false);
    c_function.setStartWasteChoice(false); 
    c_function.setStartWasteOfFirstTimestepChoice(false);
    c_function.setStopWasteChoice(false);
    c_function.setStopWasteOfLastTimestepChoice(false); 
    c_function.setPercentagevalueChoice(false);
    c_function.setFixedvalueChoice(false);
    c_function.setWatsePercentageValue(0.f); //operateCostField
    c_function.setWatseFixedValue(0.f);
    c_function.setMinimumFlowAltFour(0.f);
    c_function.setOutFlow("");
}
 private void btnOkActionPerformed ()
    {
     boolean recourceSleclected = false;
     int errorCount =1;
	// save the function label
	c_function.setLabel(txtLabel.getText());

	Resource resource = (Resource)listResource.getSelectedValue();
        if (resource != null)
        {
	  c_function.setResource(resource.getID());
          recourceSleclected = true;
        }
        
        else
        {
            JOptionPane.showMessageDialog(null, "The resource must be specified", "Resouce is not selected ",JOptionPane.WARNING_MESSAGE);
                return;
        }
                   
	//save choice one values
	if( alltOneCheckBox.isSelected() )
        {
	    
            c_function.setChoiceOne(true);
            if(startAltOneCB.isSelected())
            {
             c_function.setStartAltOneCB(true);
             if(startFisrtTimestepAltOneCB.isSelected())
               c_function.setStartFirstTimestepAltOneCB(true);  
            }
            
            if(stopAltOneCB.isSelected())
            {
             c_function.setStopAltOneCB(true);
             if(stopLastTimestepAltOneCB.isSelected())
               c_function.setStopLastTimestepAltOneCB(true);  
            }
	    c_function.setPercentageOfPreviousFlow(percentageOfPreviousFlowField.getFloatValue());
            c_function.setThresholdValue(thresholdValueField.getFloatValue());
            //startAltOneCB, stopAltOneCB, startFisrtTimestepAltOneCB, //stopLastTimestepAltOneCB;
            
            if(!recourceSleclected)
            {
            errorCount++;
            if(errorCount == 2)
            {
            JOptionPane.showMessageDialog(null, "The resource must be specified", "Resouce is not selected ",JOptionPane.WARNING_MESSAGE);
                return;
            }
            }

	}
        
//........................................................................	
       if( alltTwoCheckBox.isSelected() )
        {
	    
            c_function.setChoiceTwo(true);
            if(startCostCheckBox.isSelected())
            {
                if(startCostField.getFloatValue() == 0)
                {
                 JOptionPane.showMessageDialog(null, "Please enter correct value of start cost .\n "+"The 'Start cost' field must be  > 0",
					"Start cost <= 0", JOptionPane.WARNING_MESSAGE);
                return;
                }
                else
                 c_function.setStartCostChoice(startCostCheckBox.isSelected());   
            }
            c_function.setStartCostChoice(startCostCheckBox.isSelected());
            c_function.setStartCostofFirstTimestepChoice(startCostOfFisrtTimestepCheckBox.isSelected());
            c_function.setStartCostValue(startCostField.getFloatValue());
             if(stopCostCheckBox.isSelected())
            {
                if(stopCostField.getFloatValue() == 0)
                {
                 JOptionPane.showMessageDialog(null, "Please enter correct value of stop cost .\n "+"The 'Stop cost' field must be  > 0",
					"Stop cost <= 0", JOptionPane.WARNING_MESSAGE);
                return;
                }
                else
                 c_function.setStopCostChoice(stopCostCheckBox.isSelected());   
            }
            c_function.setStopCostofFirstTimestepChoice(stopCostOfFisrtTimestepCheckBox.isSelected());
            c_function.setStopCostValue(stopCostField.getFloatValue()); //operateCostField
            c_function.setOperateCostValue(operateCostField.getFloatValue());
            c_function.setMinimumFlowAltTwo(minFlowAltTwoField.getFloatValue());
            
            if(!recourceSleclected)
             {
             errorCount++;
             if(errorCount == 2)
               {
               JOptionPane.showMessageDialog(null, "The resource must be specified", "Resouce is not selected ",JOptionPane.WARNING_MESSAGE);
               return;
               }                
            }
	}

// ...........................................................................	
        if( alltThreeCheckBox.isSelected() )
        {
	    String op = c_function.getOperator();
            c_function.setChoiceThree(true);
            //startAltThreeCB, stopAltThreeCB, startFisrtTimestepAltThreeCB, stopLastTimestepAltThreeCB;
            if(startAltThreeCB.isSelected())
            {
             c_function.setStartAltThreeCB(true);
             if(startFisrtTimestepAltThreeCB.isSelected())
               c_function.setStartFirstTimestepAltThreeCB(true);  
            }
            
            if(stopAltThreeCB.isSelected())
            {
             c_function.setStopAltThreeCB(true);
             if(stopLastTimestepAltThreeCB.isSelected())
               c_function.setStopLastTimestepAltThreeCB(true);  
            }

            // c_function.setPercentageOfPreviousFlow3(fieldOneAltThree.getFloatValue());
            c_function.setOperateCostValue3(fieldTwoAltThree.getFloatValue());
            /*if(minFlowField.getFloatValue() <= 0)
                {
                JOptionPane.showMessageDialog(null, "The 'mimimum flow value' field must be a float > 0 ", "Minimum flow value  must be larger than zero",JOptionPane.WARNING_MESSAGE);
                    return;    
                }
            else*/
            c_function.setMinimumFlow(minFlowField.getFloatValue());
            c_function.setRadioButtonLt(radioLt.isSelected());
            c_function.setRadioButtonEq(radioEq.isSelected());
            c_function.setRadioButtonGt(radioGt.isSelected());
            c_function.setRadioButtonLtGt(radioLtGt.isSelected());
            if(op.equals("GREATER") || op.equals("LESS") || op.equals("EQUAL"))
            {
            Float ln = new Float(spinLimit2.getFloatValue());
            ln = new Float(spinLimit2.getFloatValue());
            c_function.setLimit2(ln.intValue());
            }
            else if(op.equals("LESS_GREATER")) 
            {
            Float ln = new Float(spinLimit2.getFloatValue());
            ln = new Float(spinLimit2.getFloatValue());
            c_function.setLimit2(ln.intValue());
            ln = new Float(spinLimit1.getFloatValue());
            c_function.setLimit1(ln.intValue());
            }
            else
            {
             c_function.setLimit1(0);
             c_function.setLimit2(0);
            }
            
             if(!recourceSleclected)
                {
                errorCount++;
                if(errorCount == 2)
                    {
                    JOptionPane.showMessageDialog(null, "The resource must be specified", "Resouce is not selected ",JOptionPane.WARNING_MESSAGE);
                    return;
                    }                
                }
	}

        
 //.....................................................................
        if( alltFourCheckBox.isSelected() )
        {
	    
            c_function.setChoiceFour(true);
            if(radioCP.isSelected())
                c_function.setContinuousWestProcess(true);
            if(radioBP.isSelected())
                c_function.setBatchWestProcess(true);
           
            if(startWasteCheckBox.isSelected())
              {
                c_function.setStartWasteChoice(startWasteCheckBox.isSelected()); 
                c_function.setStartWasteOfFirstTimestepChoice(startWasteOfFisrtTimestepCheckBox.isSelected());
              }
            
            if(stopWasteCheckBox.isSelected())
              {
                c_function.setStopWasteChoice(stopWasteCheckBox.isSelected()); 
                c_function.setStopWasteOfLastTimestepChoice(stopWasteOfLasttTimestepCheckBox.isSelected()); 
              }
            
            if((!fixedvalueCheckBox.isSelected())&&(!percentagevalueCheckBox.isSelected()))
                {
                JOptionPane.showMessageDialog(null, "Percentage or Fixed value for the waste must be specified in Alternative 4", "Percentage or Fixed value of the waste is not specified",JOptionPane.WARNING_MESSAGE);
                    return;
                }
            
            if((fixedvalueCheckBox.isSelected()) && (fixedValueField.getFloatValue() <= 0))
                {
                JOptionPane.showMessageDialog(null, "The 'fixed waste value' field must be a float > 0 ", "Fixed waste value must be larger than zero",JOptionPane.WARNING_MESSAGE);
                    return;
                
                }
            
             if((percentagevalueCheckBox.isSelected()) && (percentageValueField.getFloatValue() <= 0))
                {
                JOptionPane.showMessageDialog(null, "The 'percentage waste value' field must be a float > 0 ", "Percentage waste value  must be larger than zero",JOptionPane.WARNING_MESSAGE);
                    return;    
                }
            
            if(percentagevalueCheckBox.isSelected())
                {
                c_function.setPercentagevalueChoice(percentagevalueCheckBox.isSelected());
                c_function.setWatsePercentageValue(percentageValueField.getFloatValue()); //operateCostField
                }
             
            if(fixedvalueCheckBox.isSelected())
                {
                c_function.setFixedvalueChoice(fixedvalueCheckBox.isSelected());
                c_function.setWatseFixedValue(fixedValueField.getFloatValue());
                }
            
            if(c_noOutFlow)
            {
             JOptionPane.showMessageDialog(null,"No flow is going out from node " +
				     c_nodeID + "\nAlternative 4 can not be chosen.","Out flow error",JOptionPane.WARNING_MESSAGE);   
             c_function.setChoiceFour(true);
             alltFourCheckBox.setSelected(false);
             resetAltFoure(); 
             
             return;
            }
            else
            {    
            String outFlow = (String)lstOutFlow.getSelectedValue();
            if(outFlow != null)
                c_function.setOutFlow(outFlow);
            else
                {
                JOptionPane.showMessageDialog(null, "The out Flow must be specified in Alternative 4", "Out Flow is not selected ",JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            
            if(!recourceSleclected)
                {
                errorCount++;
                if(errorCount == 2)
                    {
                    JOptionPane.showMessageDialog(null, "The resource must be specified", "Resouce is not selected ",JOptionPane.WARNING_MESSAGE);
                    return;
                    }                
                }
            //operatcostAltFourField, minFlowAltFourField
            c_function.setOperateCostAltFour(operatcostAltFourField.getFloatValue());
            c_function.setMinimumFlowAltFour(minFlowAltFourField.getFloatValue());
            
	}


// close the dialog
	closeDialog(null);
    }
}
