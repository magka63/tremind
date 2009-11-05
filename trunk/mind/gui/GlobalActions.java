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
 * 
 * Copyright 2007:
 * Daniel Källming <danka053@student.liu.se>
 * David Karlslätt <davka417@student.liu.se>
 * Freddie Pintar <frepi150@student.liu.se>
 * Mårten Thurén <marth852@student.liu.se>
 * Per Fredriksson <perfr775@student.liu.se>
 * Ted Palmgren <tedpa175@student.liu.se>
 * Tor Knutsson <torkn754@student.liu.se>
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
package mind.gui;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
// temporary
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.MenuElement;
import javax.swing.JCheckBoxMenuItem;

/*
 * This class contains common actions, actions
 * that are not specialized to only the menu or only
 * to the toolbar. These actions are common ones that
 * are both for menus, toolbars and keyshortcuts.
 * @author Daniel Källming
 * @author Ted Palmgren
 * Other authors were not mentioned before the work of the PUM5 group.
 */
public class GlobalActions {
    public Action c_newScenario;
    public Action c_openScenario;
    public Action c_saveScenario;
    public Action c_saveAsScenario;
    public Action c_export;
    public Action c_optimize;
    public Action c_exit;
    public Action c_undo;
    public Action c_cut;
    public Action c_copy;
    public Action c_paste;
    public Action c_options;
    public Action c_newNode;
    public Action c_addNodeToCollection;
    public Action c_manageNodeCollection;
    public Action c_deleteNode;
    public Action c_nodeProperties;
    public Action c_showNodeIDs;
    public Action c_showNodeIDsButtonPressed;
    public Action c_showNodeLabels;
    public Action c_showNodeLabelsButtonPressed;
    public Action c_newFlow;
    public Action c_deleteFlow;
    public Action c_flowProperties;
    public Action c_showFlowIDs;
    public Action c_showFlowIDsButtonPressed;
    public Action c_showFlowLabels;
    public Action c_showFlowLabelsButtonPressed;
    public Action c_objectfunctions;
    public Action c_resources;
    public Action c_timesteps;
    public Action c_about;
    public Action c_zoom;
    //added 020829
    public Action c_print;
    public Action c_printPreview;
    public GUI c_gui;
    boolean c_optimizeWithSettingsAction = false;
    boolean c_exportAction = false;
    boolean c_optimizeAction = false;
    /*Added PUM16 2004-11-23*/
    public Action c_optimizeWithSettings;
    /* Added by Nawzad Mardan 2007-06-01*/
    public Action c_discountedsystemcost;
    //Added by PUM5 2007-10-31
    public Action c_exportXml;
    public Action c_importScenario;
    boolean c_exportXmlAction = false;
	//PUM5 end
    final String imagesDir = "images/";

    public GlobalActions(GUI gui) {
	c_gui = gui;

	c_newScenario = new AbstractAction("New",
					   new ImageIcon(getClass().getResource(imagesDir + "new.gif")))
	    {
		public void actionPerformed(ActionEvent e)
		{
		    c_gui.newScenarioAction();
		}
	    };
	c_openScenario = new AbstractAction("Open...",
					    new ImageIcon(getClass().getResource(imagesDir + "open.gif")))
	    {
		public void actionPerformed(ActionEvent e)
		{
		    c_gui.openScenarioAction();
		}
	    };
	c_saveScenario = new AbstractAction("Save",
					    new ImageIcon(getClass().getResource(imagesDir + "save.gif")))
	    {
		public void actionPerformed(ActionEvent e)
		{
		    c_gui.saveScenarioAction(true);
		}
	    };
	c_saveAsScenario = new AbstractAction("Save As...", null) {
		public void actionPerformed(ActionEvent e)
		{
		    c_gui.saveAsScenarioAction();
		}
	    };
	c_export = new AbstractAction("to MPS",
                                      new ImageIcon(getClass().getResource(imagesDir + "left.gif")))
	    {
		public void actionPerformed(ActionEvent e)
		{
		    //c_gui.exportAction();
                    c_exportAction = true;
                    new SaveThread();            
		}
	    };
	    
	    //Added by PUM5 2007-10-31 
	    c_exportXml = new AbstractAction("to XML",
	    		new ImageIcon(getClass().getResource(imagesDir + "left.gif")))
	    		{
	    	public void actionPerformed(ActionEvent e)
	    	{
	    		//c_gui.exportAction();
	    		c_exportXmlAction = true;
	    		new SaveThread();            
	    	}
	    	};
	    	
	    c_importScenario = new AbstractAction("from XML",
			    new ImageIcon(getClass().getResource(imagesDir + "open.gif")))
			    {
				public void actionPerformed(ActionEvent e)
				{
				    c_gui.importScenarioAction();
				}
			    };
		//PUM5 end
			    
	c_exit = new AbstractAction("Exit", null)
	    {
		public void actionPerformed(ActionEvent e)
		{
		    c_gui.exitAction();
		}
	    };
	c_undo = new AbstractAction("Undo",
				    new ImageIcon(getClass().getResource(imagesDir + "open.gif")))
	    {
		public void actionPerformed(ActionEvent e)
		{
		    c_gui.undoAction();
		}
	    };
	c_cut = new AbstractAction("Cut",
				   new ImageIcon(getClass().getResource(imagesDir + "cut.gif")))
	    {
		public void actionPerformed(ActionEvent e)
		{
		    c_gui.cutAction();
		}
	    };
	c_copy = new AbstractAction("Copy",
				    new ImageIcon(getClass().getResource(imagesDir + "copy.gif")))
	    {
		public void actionPerformed(ActionEvent e)
		{
		    c_gui.copyAction();
		}
	    };
	c_paste = new AbstractAction("Paste",
				     new ImageIcon(getClass().getResource(imagesDir + "paste.gif")))
	    {
		public void actionPerformed(ActionEvent e)
		{
		    c_gui.pasteAction();
		}
	    };
	c_options = new AbstractAction("Options...",
				       new ImageIcon(getClass().getResource(imagesDir + "open.gif")))
	    {
		public void actionPerformed(ActionEvent e)
		{
		    c_gui.optionsAction();
		}
	    };
	c_newNode = new AbstractAction("New Node", null)
	    {
		public void actionPerformed(ActionEvent e)
		{
		    c_gui.newNodeAction();
		}
	    };
	c_addNodeToCollection = new AbstractAction("Add node to collection",
						   null)
	    {
		public void actionPerformed(ActionEvent e)
		{
		    c_gui.addNodeToCollectionAction();
		}
	    };
	c_manageNodeCollection = new AbstractAction("Manage Node Collection...", null)
	    {
		public void actionPerformed(ActionEvent e)
		{
		    c_gui.manageNodeCollectionAction();
		}
	    };
	c_deleteNode = new AbstractAction("Delete Node", null)
	    {
		public void actionPerformed(ActionEvent e)
		{
		    c_gui.deleteComponentsAction();
		}
	    };
	c_nodeProperties = new AbstractAction("Properties...", null)
	    {
		public void actionPerformed(ActionEvent e)
		{
		    c_gui.nodePropertiesAction();
		}
	    };

        c_showNodeIDsButtonPressed = new AbstractAction("IDs", null)
        {
          public void actionPerformed(ActionEvent e)
          {
            c_gui.clickShowNodeIDsCheckBox();
          }
        };

	c_showNodeIDs = new AbstractAction("IDs", null)
	    {
		public void actionPerformed(ActionEvent e)
		{
		    JCheckBoxMenuItem box = (JCheckBoxMenuItem) e.getSource();
		   // c_gui.showNodeIDsAction(box.isSelected());
                      c_gui.showNodeIDs(box.isSelected());
		}
	    };

          c_showNodeLabelsButtonPressed = new AbstractAction("IDs", null)
          {
            public void actionPerformed(ActionEvent e)
            {
              c_gui.clickShowNodeLabelsCheckBox();
            }
          };

	c_showNodeLabels = new AbstractAction("Labels", null)
	    {
		public void actionPerformed(ActionEvent e)
		{
		    JCheckBoxMenuItem box = (JCheckBoxMenuItem) e.getSource();
		    // c_gui.showNodeLabelsAction(box.isSelected());
                       c_gui.showNodeLabels(box.isSelected());
		}
	    };
	c_newFlow = new AbstractAction("New Flow",
				       new ImageIcon(getClass().getResource(imagesDir + "flowarrow.gif")))
	    {
		public void actionPerformed(ActionEvent e)
		{
		    c_gui.newFlowAction();
		}
	    };
	c_deleteFlow = new AbstractAction("Delete Flow", null)
	    {
		public void actionPerformed(ActionEvent e)
		{
		    c_gui.deleteComponentsAction();
		}
	    };
	c_flowProperties = new AbstractAction("Properties...", null)
	    {
		public void actionPerformed(ActionEvent e)
		{
		    c_gui.flowPropertiesAction();
		}
	    };

        c_showFlowIDsButtonPressed = new AbstractAction("IDs", null)
        {
          public void actionPerformed(ActionEvent e)
          {
            c_gui.clickShowFlowIDsCheckBox();
          }
        };

	c_showFlowIDs = new AbstractAction("IDs", null)
	    {
		public void actionPerformed(ActionEvent e)
		{
		    JCheckBoxMenuItem box = (JCheckBoxMenuItem) e.getSource();
		 //   c_gui.showFlowIDsAction(box.isSelected());
                    c_gui.showFlowIDs(box.isSelected());
		}
	    };

        c_showFlowLabelsButtonPressed = new AbstractAction("IDs", null)
        {
          public void actionPerformed(ActionEvent e)
          {
            c_gui.clickShowFlowLabelsCheckBox();
          }
        };

	c_showFlowLabels = new AbstractAction("Labels", null)
	    {
		public void actionPerformed(ActionEvent e)
		{
		    JCheckBoxMenuItem box = (JCheckBoxMenuItem) e.getSource();
		 //   c_gui.showFlowLabelsAction(box.isSelected());
                    c_gui.showFlowLabels(box.isSelected());
		}
	    };
	c_objectfunctions = new AbstractAction("Object Functions...", null)
	    {
		public void actionPerformed(ActionEvent e)
		{
		    c_gui.objectfunctionAction();
		}
	    };
	c_resources = new AbstractAction("Resources...", null)
	    {
		public void actionPerformed(ActionEvent e)
		{
		    c_gui.resourcesAction();
		}
	    };
	c_timesteps = new AbstractAction("Timesteps...", null)
	    {
		public void actionPerformed(ActionEvent e)
		{
		    c_gui.timestepsAction();
		}
	    };
	c_optimize = new AbstractAction("Optimize",
					new ImageIcon(getClass().getResource(imagesDir + "optimize.gif")))
	    {
		public void actionPerformed(ActionEvent e)
		{
                    c_optimizeAction = true;
                    new SaveThread();
		   // c_gui.optimizeAction();
		}
	    };
	c_about = new AbstractAction("About", null)
	    {
		public void actionPerformed(ActionEvent e)
		{
                    c_gui.aboutAction();
		}
	    };
	c_zoom = new AbstractAction("Zoom", null)
	    {
		public void actionPerformed(ActionEvent e)
		{
		    String selected;

		    if (e.getSource() instanceof JComboBox) {
			selected = ((JComboBox) e.getSource()).
			    getSelectedItem().toString();
			c_gui.zoomAction(selected);
		    }
		}
	    };
	c_print = new AbstractAction("Print", null)
	    {
		public void actionPerformed(ActionEvent e) {
		    c_gui.printAction();
		}
	    };
	c_printPreview = new AbstractAction("Preview", null)
	    {
		public void actionPerformed(ActionEvent e)
		{
		    c_gui.previewAction();
		}
	    };

	/*Added PUM16 2004-11-23*/
	c_optimizeWithSettings = new AbstractAction("Optimize With Settings",
						    new ImageIcon(getClass().getResource(imagesDir + "optimizewithsettings.gif")))
	    {
		public void actionPerformed(ActionEvent e)
		{
		   // c_gui.optimizeWithSettingsAction();
                    c_optimizeWithSettingsAction = true;
                     new SaveThread();
		}
	    };

       /* Added by Nawzad Mardan 2007-06-01*/
        c_discountedsystemcost = new AbstractAction("Discounted system cost..", null)
	    {
		public void actionPerformed(ActionEvent e) {
		   c_gui.discountdcostAction();
		}
	    };
    }
  /** New Thread inner class, to enable canceling or ending the program at any time
   * 2007-05-04 Added by (Nawzad Mardan)
   * för att göra det lättare för användaren att avbryta programmet när som helst
   */  
     private class SaveThread extends Thread // inner class
      {
      
      public SaveThread()
        {
          start();
        }
      public void run() // det kan man inte ha throws IOException, FileInteractionExcption
        {
          if(c_optimizeAction)
          {
            c_gui.optimizeAction();
            c_optimizeAction = false;
          }
          if(c_optimizeWithSettingsAction)
          {
            c_gui.optimizeWithSettingsAction();
            c_optimizeWithSettingsAction = false;
          }
          if(c_exportAction)
          {
            c_gui.exportAction();
            c_exportAction = false;
          }
          //Added by PUM5 2007-10-31
          if(c_exportXmlAction)
          {
        	 c_gui.exportXmlAction();
             c_exportXmlAction = false; 
          }
		  //PUM5 end
        }//END RUN
      }// END SaveThread
}
