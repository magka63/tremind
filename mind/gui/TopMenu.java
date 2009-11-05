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
 * Per Fredriksson <perfr775@student.liu.se>
 * David Karlslätt <davka417@student.liu.se>
 * Tor Knutsson	<torkn754@student.liu.se>
 * Daniel Källming <danka053@student.liu.se>
 * Ted Palmgren <tedpa175@student.liu.se>
 * Freddie Pintar <frepi150@student.liu.se>
 * Mårten Thurén <marth852@student.liu.se> 
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

import java.awt.event.*;
import javax.swing.*;

/**
 * This class represents the main top menu.
 *
 * @author Tim Terlegård
 * @version 2001-04-04
 */
public class TopMenu
    extends JMenuBar
{
    public JCheckBoxMenuItem c_showNodeLabelsCheckBox = null;
    public JCheckBoxMenuItem c_showNodeIDsCheckBox = null;
    public JCheckBoxMenuItem c_showFlowLabelsCheckBox = null;
    public JCheckBoxMenuItem c_showFlowIDsCheckBox = null;
    // Added by Nawzad Mardan 2007-09-11
    private JMenu model=null;
    private JMenu file=null;

    public TopMenu(GlobalActions actions)
    {
	file = new JMenu("File");
	JMenu export = new JMenu("Export");
	// Added by PUM5 2007-10-31
	JMenu menuImport = new JMenu("Import");
	// End PUM5
	JMenu edit = new JMenu("Edit");
        model = new JMenu("Model");
	//JMenu model = new JMenu("Model");
	JMenu help = new JMenu("Help");
	JMenu modelNode = new JMenu("Node");
	JMenu modelFlow = new JMenu("Flow");
	JMenu flowShow = new JMenu("Show");
	JMenu nodeShow = new JMenu("Show");

 	file.add(newMenuItem(actions.c_newScenario, 'N', KeyEvent.VK_N));
	file.add(newMenuItem(actions.c_openScenario, 'O', KeyEvent.VK_O));
	file.add(newMenuItem(actions.c_saveScenario, 'S', KeyEvent.VK_S));
	file.add(newMenuItem(actions.c_saveAsScenario, 'A', KeyEvent.VK_A));
	export.add(newMenuItem(actions.c_export, 'E', KeyEvent.VK_E)); // MPS
	// Added by PUM5 2007-10-31
	export.add(newMenuItem(actions.c_exportXml, 'T', KeyEvent.VK_T)); // EXML
	file.add(export);
	menuImport.add(newMenuItem(actions.c_importScenario, 'I', KeyEvent.VK_I)); // EXML
	file.add(menuImport);
	// End PUM5
	file.add(newMenuItem(actions.c_printPreview, 0, 0));
	file.add(newMenuItem(actions.c_print, 'P', KeyEvent.VK_P));
	//file.add(newMenuItem(actions.c_optimize, 'p', 0));
	file.addSeparator();
	file.add(newMenuItem(actions.c_exit, 'x', 0));
	add(file);

	edit.add(newMenuItem(actions.c_undo, 'U', KeyEvent.VK_Z));
	//edit.addSeparator();
        /*  2005-05-11 Jonas Sääv
            These functions has been deactivated in order to maintain consistency
            in the model database. cut-copy-paste actions are not dealt with properly
            in the current system architecture. Think twice before re-activation

            To use copy, it is needed to implement a functionallity that removes the
            functions in the node that refers to Flows. Because these flows will be invalid
            after the copy operation
        */
	//edit.add(newMenuItem(actions.c_cut, 'C', KeyEvent.VK_X));
	//edit.add(newMenuItem(actions.c_copy, 'Y', KeyEvent.VK_C));
	//edit.add(newMenuItem(actions.c_paste, 'D', KeyEvent.VK_V));
	edit.addSeparator();
	edit.add(newMenuItem(actions.c_options, 't', 0));
	add(edit);

	modelNode.add(newMenuItem(actions.c_newNode, 'N', 0));
	modelNode.add(newMenuItem(actions.c_deleteNode, 'D', 0));
	modelNode.add(newMenuItem(actions.c_addNodeToCollection, 'A', 0));
	modelNode.add(newMenuItem(actions.c_manageNodeCollection, 'M', 0));
	modelNode.add(newMenuItem(actions.c_nodeProperties, 'P', 0));
	modelNode.addSeparator();
	nodeShow.add(c_showNodeIDsCheckBox = newCheckBoxItem(actions.c_showNodeIDs, 't', 0));
	nodeShow.add(c_showNodeLabelsCheckBox = newCheckBoxItem(actions.c_showNodeLabels, 't', 0));
	modelNode.add(nodeShow);
	model.add(modelNode);
	modelFlow.add(newMenuItem(actions.c_newFlow, 't', KeyEvent.VK_F));
	modelFlow.add(newMenuItem(actions.c_deleteFlow, 't', 0));
	modelFlow.add(newMenuItem(actions.c_flowProperties, 't', 0));
	modelFlow.addSeparator();
	flowShow.add(c_showFlowIDsCheckBox = newCheckBoxItem(actions.c_showFlowIDs, 't', 0));
	flowShow.add(c_showFlowLabelsCheckBox = newCheckBoxItem(actions.c_showFlowLabels, 't', 0));
	modelFlow.add(flowShow);
	model.add(modelFlow);

	//added 2003-12-05 Urban Liljedahl, 'O' should maybe be changed
	model.add(newMenuItem(actions.c_objectfunctions, 'O', 0));

	model.add(newMenuItem(actions.c_resources, 'R', 0));
	model.add(newMenuItem(actions.c_timesteps, 'T', 0));
        // added by Nawzad Mardan 2007-06-01
        model.add(newMenuItem(actions.c_discountedsystemcost, 'D', 0));
	model.addSeparator();
	model.add(newMenuItem(actions.c_optimize, 'O', 0));
	model.add(newMenuItem(actions.c_optimizeWithSettings,'P',0));
	add(model);

	help.add(newMenuItem(actions.c_about, 'H', 0));
	add(help);
    }

    private JMenuItem newMenuItem(Action action, int mnemonic,
				 int acceleratorKey)
    {
	JMenuItem item = new JMenuItem(action);
	if (mnemonic != 0)
	    item.setMnemonic((char) mnemonic);
	if (acceleratorKey != 0)
	    item.setAccelerator(KeyStroke.getKeyStroke(acceleratorKey,
						       java.awt.Event.CTRL_MASK));
	// We dont want the icons that are set to the menuitem by Action
	item.setIcon(null);
	return item;
    }
// Added by Nawzad Mardan 070911
    public JMenu getFileMenu()
    {
            
        if(file!=null)
            return file;
        else
            return new JMenu("File");
    }
    
    public JMenu getModelMenu()
    {
            
        if(model!=null)
            return model;
        else
            return new JMenu("Model");
    }
    private JCheckBoxMenuItem newCheckBoxItem(Action action, int mnemonic,
					      int acceleratorKey)
    {
	JCheckBoxMenuItem item = new JCheckBoxMenuItem(action);
	if (mnemonic != 0)
	    item.setMnemonic((char) mnemonic);
	if (acceleratorKey != 0)
	    item.setAccelerator(KeyStroke.getKeyStroke(acceleratorKey,
						       java.awt.Event.CTRL_MASK));
	// We dont want the icons that are set to the menuitem by Action
	item.setIcon(null);
	return item;
    }
}
