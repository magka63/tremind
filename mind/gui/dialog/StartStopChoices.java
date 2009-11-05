/*
 * StartStopChoices.java
 *
 * Created on den 12 februari 2008, 09:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/*
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
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import mind.gui.*;
import mind.model.*;
import mind.model.function.*;
import mind.EventHandlerClient;
import java.awt.event.*;
import javax.swing.*;


/**
 *
 * @author Nawzad Mardan
 */
public class StartStopChoices extends mind.gui.dialog.FunctionDialog
{
    private JCheckBox alltOneCheckBox;
    private JCheckBox alltTwoCheckBox;
    private JCheckBox alltThreeCheckBox;
    private JCheckBox alltFourCheckBox;
    private JDialog dialog;
    private boolean c_choiceOneSelected = false;
    private boolean c_choiceTowSelected = false;
    private boolean c_choiceThreeSelected = false;
    private boolean c_choiceFourSelected = false;
    private ID c_nodeID;
   // private Vector c_resources;
    private GUI c_gui;
    private StartStopEquation c_function;
   // private EventHandlerClient c_eventhandler;
    private Timesteplevel tsl[];
   // /** Creates a new instance of StartStopChoices */
    //public StartStopChoices() {}
    public StartStopChoices(javax.swing.JDialog parent, boolean modal, ID nodeID, NodeFunction function, GUI gui) 
        {
	super (parent, modal);
	c_nodeID = nodeID;
	c_gui = gui;
        c_function = (StartStopEquation)function;
        initChoices();
    }
    
    /** This method is called from  the constructor to
   * initialize the form of the Choices.
   * 
   */
    

    public void initChoices()
    {
        //Create the check boxes.
        alltOneCheckBox = new JCheckBox("Choice ONE");
        alltOneCheckBox.setMnemonic(KeyEvent.VK_C);
        alltOneCheckBox.setSelected(false);

        alltTwoCheckBox = new JCheckBox("Choice TWO");
        alltTwoCheckBox.setMnemonic(KeyEvent.VK_G);
        alltTwoCheckBox.setSelected(false);

        alltThreeCheckBox = new JCheckBox("Choice THREE");
        alltThreeCheckBox.setMnemonic(KeyEvent.VK_H);
        alltThreeCheckBox.setSelected(false);

        alltFourCheckBox = new JCheckBox("Choice FOUR");
        alltFourCheckBox.setMnemonic(KeyEvent.VK_T);
        alltFourCheckBox.setSelected(false);

        //Register a listener for the check boxes.
        alltOneCheckBox.addItemListener(new QItemListener());
        alltTwoCheckBox.addItemListener(new QItemListener());
        alltThreeCheckBox.addItemListener(new QItemListener());
        alltFourCheckBox.addItemListener(new QItemListener());
        
        //Put the check boxes in a column in a panel
        JPanel checkPanel = new JPanel(new GridLayout(0, 1));
        checkPanel.add(alltOneCheckBox);
        checkPanel.add(alltTwoCheckBox);
        checkPanel.add(alltThreeCheckBox);
        checkPanel.add(alltFourCheckBox);
        //Creates Separator line 
        JSeparator sep1=new javax.swing.JSeparator();
        JPanel buttonPanel = new JPanel();
        
        // Add line sperator and the buttons to the panel
        
        checkPanel.add(sep1);
        
        JButton btnOK = new JButton("OK");
        JButton btnCancel = new JButton("Cancel");
        btnOK.addActionListener(new ActionListener() 
        {
	public void actionPerformed(ActionEvent evt) 
            {
            // Check if user select any choice
            if((c_choiceOneSelected == false) && (c_choiceTowSelected == false) && (c_choiceThreeSelected == false) && (c_choiceFourSelected == false))
                closeDialog(null);    
            else 
                initStartStopComponent();
                            
            }}); 
        btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
                            closeDialog(null);
			}
		});
        
         addWindowListener(new java.awt.event.WindowAdapter() 
        {
        public void windowClosing(java.awt.event.WindowEvent evt) {closeDialog(evt); return;
        }});
                
        buttonPanel.add(btnOK);
        buttonPanel.add(btnCancel);
        checkPanel.add(buttonPanel);
        add(checkPanel);   
        setSize(400,250);
        pack();             
    }
    
   /** This method is called from the intChoices() method
   * to initialize the form of the Start and Stop dialog.
   * 
   */
    
    
    public void initStartStopComponent()
    {
        closeDialog(null);   
    
        dialog = new StartStopDialog(this, true, c_nodeID, c_function, c_gui, c_choiceOneSelected, c_choiceTowSelected, 
         c_choiceThreeSelected, c_choiceFourSelected);
                              
        Dimension d = getToolkit().getScreenSize();
        Rectangle b = getBounds();
        dialog.setLocation((d.width-b.width)/2, (d.height-b.height)/2);
        dialog.setSize(300,600);
        dialog.pack();
        dialog.setVisible(true);
 
     }

      /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) 
    {
    setVisible (false);
    dispose ();
    }
    
  /** Inner class
   Register a listener to the checkboxes
  */    
public class QItemListener implements ItemListener
 {
   public void itemStateChanged(ItemEvent e) 
   {
        Object source = e.getItemSelectable();

        if (source == alltOneCheckBox) 
        {
            //if selected
            if(alltOneCheckBox.getSelectedObjects()!=null)
                c_choiceOneSelected = true;
            else 
                c_choiceOneSelected = false;
        } 
        else if (source == alltTwoCheckBox) 
        {
            if(alltTwoCheckBox.getSelectedObjects()!=null)
                c_choiceTowSelected = true;
            else
                c_choiceTowSelected=false;
        } 
        else if (source == alltThreeCheckBox) 
        {
             if(alltThreeCheckBox.getSelectedObjects()!=null)
                c_choiceThreeSelected = true;
             else
                 c_choiceThreeSelected = false;
        } 
        else if (source == alltFourCheckBox) 
        {
           if(alltFourCheckBox.getSelectedObjects()!=null)
                c_choiceFourSelected = true;
           else
               c_choiceFourSelected = false;
        }

        
    }
 }// finish inner class QItemListener
    
}// END StartStopChoices class 
