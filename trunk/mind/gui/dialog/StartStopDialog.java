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
import javax.swing.*;
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
    }
    

 /** This method is called from  the constructor to
   * initialize the form of the Start and Stop dialog.
   * 
  */
private void initComponent()
{
    setName ("Start Stop Equation");
    
    
}



    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) 
    {
    setVisible (false);
    dispose ();
    }
    



}
