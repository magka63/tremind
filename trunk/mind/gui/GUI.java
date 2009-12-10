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
 * Copyright 2004
 * Johan Bengtgsson <johbe496@student.liu.se>
 * Daniel Campos <danca226@student.liu.se>
 * Martin Fagerfjäll <marfa233@student.liu.se>
 * Daniel Ferm <danfe666@student.liu.se>
 * Able Mahari <ablma616@student.liu.se>
 * Andreas Remar <andre063@student.liu.se>
 * Haider Shareef <haish292@student.liu.se>
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

import java.awt.Point;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;

import mind.EventHandlerClient;
import mind.GlobalStringConstants;
import mind.automate.OptimizationException;
import mind.automate.OptimizationResult;
import mind.gui.dialog.AboutDialog;
import mind.gui.dialog.AddNodeToCollectionDialog;
import mind.gui.dialog.DiscountedsystemcostDialog;
import mind.gui.dialog.FlowPropertiesDialog;
import mind.gui.dialog.MainDialog;
import mind.gui.dialog.NodePropertiesDialog;
import mind.gui.dialog.NodeSelectionDialog;
import mind.gui.dialog.ObjectFunctionDialog;
import mind.gui.dialog.OptimizeSettingsDialog;
import mind.gui.dialog.OptimizerOutputDialog;
import mind.gui.dialog.OptionsDialog;
import mind.gui.dialog.OutputFlowChooserDialog;
import mind.gui.dialog.PreviewDialog;
import mind.gui.dialog.PrintDialog;
import mind.gui.dialog.ResourcesDialog;
import mind.gui.dialog.TimestepsDialog;
import mind.gui.dnd.NodeToDrag;
import mind.io.FileInteraction;
import mind.io.FileInteractionException;
import mind.io.Ini;
import mind.io.RmdParseException;
import mind.io.SaveAsFileFilter;
import mind.io.XML;
import mind.model.DiscountedsystemcostControl;
import mind.model.Flow;
import mind.model.FunctionControl;
import mind.model.ID;
import mind.model.Model;
import mind.model.ModelException;
import mind.model.Node;
import mind.model.NodeControl;
import mind.model.NodeFunction;
import mind.model.Resource;
import mind.model.ResourceControl;
import mind.model.Timesteplevel;

//
import java.awt.event.*;
import java.awt.event.WindowEvent;
import java.awt.*;
import java.io.*;
import javax.swing.event.*;
import javax.swing.JEditorPane;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JButton;

/**
 * This class is the main actor in the client. It handles all actions
 * the user makes. GUI is a singleton, hence the GUI() constructor is private and
 * the getInstance method should be used instead.
 *
 * @author Tim Terlegård
 * @author Johan Trygg
 * @author Jonas Sääv
 * @author Daniel Källming
 * @author Ted Palmgren
 * @version 2007-12-14
*/
public class GUI
    implements UserSettingConstants, GlobalStringConstants
{
    private class Editable {
	private Object c_component = null;
	private int c_x = 0;
	private int c_y = 0;

	public Editable(Object component, int x, int y) {
	    c_component = component;
	    c_x = x;
	    c_y = y;
	}

	public Object getComponent()
	{
	    return c_component;
	}

	public int getX()
	{
	    return c_x;
	}

	public int getY()
	{
	    return c_y;
	}
    }

    private class Tasks
	extends TimerTask
    {
	private int c_autosaveTime = -1;
	private int c_currentAutosaveTime = 0;

	public Tasks()
	{
	}

	public void run()
	{
	    c_currentAutosaveTime++;

	    if (c_autosaveTime > 0)
		if (c_currentAutosaveTime >= c_autosaveTime) {
		    saveScenarioAction(false);
		    c_currentAutosaveTime = 0;
		}
	}

	public void setAutosave(int minutes)
	{
	    c_autosaveTime = minutes;
	}
    }

    private static GUI instance = null; // Singelton

    private static TopToolbar s_topToolbar = null;
    public TopToolbar c_topToolbar = null;

    private MainDialog c_app = null;
    private EventHandlerClient c_eventHandler = null;
    private FileInteraction c_fileInteraction = null;
    private boolean c_isChanged = false;
    private Editable c_editable = null;
    private Tasks c_tasks;
    private Timer c_timer;
    private Ini c_userSettings = null;
    private Zoom c_zoom;
    private PrintSettings c_printsettings;


    private File c_tmpOptimized = null;
    private File c_tmpMps = null;
    private File c_tmpInput = null;

    /* PUM16 2004 */
    public String c_savedModel = null;

    private String c_modelsDir  = null;
    private String c_exportDir  = null;
    
	//Added by PUM5 2007-11-30 
    private String c_exportXmlDir = null;
    private String rmdFileName = "Untitled";
   // private Thread s;
    // Added  by Nawzad Mardan 2008-02-10
    private  JDialog dialog;
    /*
     * For test purposes.
     * If automatic testing isn't used the following variable can be removed.
     */
    private boolean testVar=false;
    // end pum5
    
    private boolean c_showNodeIDs = false;
    private boolean c_showNodeLabels = true;
    private boolean c_showFlowIDs = false;
    private boolean c_showFlowLabels = false;

    private final String cutManyMessage =
	"You tried to cut several components.\n" +
	"You can only cut one component, so you have to\n" +
	"deselect component(s) in order to succeed with the cut.";
    private final String cutNonCutableGraphMessage =
	"You tried to cut a non-cutable component.\n" +
	"You can only cut nodes.";
    private final String confirmTitle =
	"Confirm dialog";
    private final String copyManyMessage =
	"You tried to copy several components.\n" +
	"You can only copy one component, so you have to\n" +
	"deselect component(s) in order to succeed with the copy.";
    private final String copyNonCopyableGraphMessage =
	"You tried to cut a non-copyable component.\n" +
	"You can only copy nodes.";
    private final String exitMessage =
	"You requested to exit this application.\n" +
	"The current model is not saved and will be destroyed\n " +
	"if you exit. Do you want to exit without saving the\n" +
	"current model?";
    private final String messageTitle =
	"Message dialog";
    private final String newMessage =
	"You want to create a new model.\n" +
	"The current model is not saved and will be destroyed\n " +
	"when you create a new model. Do you want to create a\n " +
	"new model without saving the current model?";
    private final String openMessage =
	"You want to open a new model.\n" +
	"The current model is not saved and will be destroyed\n " +
	"when you open a new model. Do you want to open a\n " +
	"model without saving the current model?";
    private final String PROPERTIES_FOR_ONE_FLOW_ONLY =
	"You have selected to see properties for several flows.\n " +
	"You can only see the properties for one single flow.\n " +
	"Unmark flow(s) so you have only one flow selected.";
    private final String PROPERTIES_FOR_ONE_NODE_ONLY =
	"You have selected to see properties for several nodes.\n " +
	"You can only see the properties for one single node.\n " +
	"Unmark node(s) so you have only one node selected.";
    private final String ADDNODE_FOR_ONE_NODE_ONLY =
	"You have selected to add several nodes to node collection\n " +
	"You can only add one node at a time.\n " +
	"Unmark node(s) so you have only one node selected.";

    /**
     * Returns the one and only instance of the singleton class GUI. The first call
     * creates the instance. Later calls just returns the static 'instance'.
     * getInstance is public and static, hence the GUI instance can be accessed anywhere
     * at all times by calling GUI.getInstance().
     * @return a GUI instance (singleton)
     */
    public static GUI getInstance() {
      if (instance == null)
        instance = new GUI();

      return instance;
    }

    /**
     * Private constructor of the class GUI. GUI is a singleton and the one and
     * only instance of this object is retrieved using the getInstance method.
     * The first call to getInstance will create an actual instance. Later calls
     * will return the static pointer 'instance'.
     */
    private GUI()
    {
	GlobalActions actions = new GlobalActions(this);
	NodePopupMenu.c_actions = actions;
	FlowPopupMenu.c_actions = actions;
	c_eventHandler = new EventHandlerClient();
	c_fileInteraction = new FileInteraction();

	c_timer = new Timer();
	c_userSettings = new Ini();
	c_zoom = new Zoom();

	c_app = new MainDialog(this, actions, c_userSettings);
        c_topToolbar = c_app.c_topToolbar;
	c_app.setAvailableFunctions(c_eventHandler.getAvailableFunctions());
	c_app.setAvailableNodes(c_eventHandler.getAvailableNodes());

	// A Timer is created that will do the autosaving.
	c_tasks = new Tasks();
	String minutes = c_userSettings.getProperty(AUTOSAVE); // default is 3
        String state   = c_userSettings.getProperty(AUTOSAVE_STATE); // default is 0 = off

        setAutosaveState((state == "1")? true:false, Short.parseShort(minutes));

        retrieveUserSettings();

	// put the frame in the middle of the screen
	int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int x = (int) (width/2 - c_app.getSize().width/2);
        int y = (int) (height/2 - c_app.getSize().height/2);
	c_app.setLocation(x, y);
	c_app.show();
        initInformationComponent();
        waiting();
    }

    /**
     * Turns on/off the autosave feature
     * @param state true=on false=off
     * @param time period for autosave in minutes
     */
    private void setAutosaveState(boolean state, int time)
    {
      if (state == false) {
        c_tasks.cancel();
        c_timer.cancel();
      }
        else {
          /* tasks and timers cannot be reused after having been canceled */
          c_tasks = new Tasks();
          c_timer = new Timer();

          c_tasks.setAutosave(time);
          c_timer.schedule(c_tasks, 60 * 1000, 60 * 1000);
        }
    }

    private void retrieveUserSettings()
    {
      c_exportDir = c_userSettings.getProperty(MPS_EXPORT_FOLDER);
      c_modelsDir = c_userSettings.getProperty(RMD_MODEL_FOLDER);
      c_exportXmlDir = c_userSettings.getProperty(XML_EXPORT_FOLDER);
      
  /* Read settings from ini-file for node and flow labels/ids  */
      String temp;

      temp = c_userSettings.getProperty(GUI_SETTINGS_SHOW_NODE_LABELS);
      if (temp != null)
        c_showNodeLabels = (temp.equalsIgnoreCase("true")) ? true : false;

      temp = c_userSettings.getProperty(GUI_SETTINGS_SHOW_NODE_IDS);
      if (temp != null)
        c_showNodeIDs = (temp.equalsIgnoreCase("true")) ? true : false;

      temp = c_userSettings.getProperty(GUI_SETTINGS_SHOW_FLOW_LABELS);
      if (temp != null)
        c_showFlowLabels = (temp.equalsIgnoreCase("true")) ? true : false;

      temp = c_userSettings.getProperty(GUI_SETTINGS_SHOW_FLOW_IDS);
      if (temp != null)
        c_showFlowIDs = (temp.equalsIgnoreCase("true")) ? true:false;

      /* Adjust menu checkboxes and togglebuttons  */
      c_app.c_topMenu.c_showFlowIDsCheckBox.setSelected(c_showFlowIDs);
      c_app.c_topMenu.c_showFlowLabelsCheckBox.setSelected(c_showFlowLabels);
      c_app.c_topMenu.c_showNodeIDsCheckBox.setSelected(c_showNodeIDs);
      c_app.c_topMenu.c_showNodeLabelsCheckBox.setSelected(c_showNodeLabels);

      c_app.c_topToolbar.c_FlowIDsButton.setSelected(c_showFlowIDs);
      c_app.c_topToolbar.c_FlowLabelsButton.setSelected(c_showFlowLabels);
      c_app.c_topToolbar.c_NodeIDsButton.setSelected(c_showNodeIDs);
      c_app.c_topToolbar.c_NodeLabelsButton.setSelected(c_showNodeLabels);
    }

    /**
     * "Remote" click of CheckBox (see GlobalActions).
     */
    public void clickShowFlowLabelsCheckBox()
    {
      c_app.c_topMenu.c_showFlowLabelsCheckBox.doClick();
    }
    /**
     * "Remote" click of CheckBox (see GlobalActions).
     */
    public void clickShowFlowIDsCheckBox()
    {
      c_app.c_topMenu.c_showFlowIDsCheckBox.doClick();
    }
    /**
     * "Remote" click of CheckBox (see GlobalActions).
     */
    public void clickShowNodeLabelsCheckBox()
    {
      c_app.c_topMenu.c_showNodeLabelsCheckBox.doClick();
    }
    /**
     * "Remote" click of CheckBox (see GlobalActions).
     */
    public void clickShowNodeIDsCheckBox()
    {
      c_app.c_topMenu.c_showNodeIDsCheckBox.doClick();
    }

    /**
     * Sets status of show node labels.
     * @param show true for show false for hide
     * @return previous show state
     */
    public boolean showNodeLabels(boolean show)
    {
      boolean temp = c_showNodeLabels;
      c_showNodeLabels = show;
      if (show)
        c_userSettings.setProperty(GUI_SETTINGS_SHOW_NODE_LABELS, "true");
      else
        c_userSettings.setProperty(GUI_SETTINGS_SHOW_NODE_LABELS, "false");

      c_app.getGraphArea().repaint();

      return temp;
    }

    /**
     * Gets the current show state of node labels
     * @return current show state
     */
    public boolean showNodeLabels()
    {
        return c_showNodeLabels;
    }

     /**
     * Sets status of show node IDs.
     * @param show true for show false for hide
     * @return previous show state
     */
    public boolean showNodeIDs(boolean show)
    {
      boolean temp = c_showNodeIDs;
      c_showNodeIDs = show;
      if (show)
        c_userSettings.setProperty(GUI_SETTINGS_SHOW_NODE_IDS, "true");
      else
        c_userSettings.setProperty(GUI_SETTINGS_SHOW_NODE_IDS, "false");

      c_app.getGraphArea().repaint();

      return temp;
    }

    /**
     * Gets the current show state of node IDs
     * @return current show state
     */
    public boolean showNodeIDs()
    {
        return c_showNodeIDs;
    }


     /**
     * Sets status of show Flow labels.
     * @param show true for show false for hide
     * @return previous show state
     */
    public boolean showFlowLabels(boolean show)
    {
      boolean temp = c_showFlowLabels;
      c_showFlowLabels = show;
      if (show)
          c_userSettings.setProperty(GUI_SETTINGS_SHOW_FLOW_LABELS, "true");
      else
          c_userSettings.setProperty(GUI_SETTINGS_SHOW_FLOW_LABELS, "false");

      c_app.getGraphArea().repaint();

      return temp;
    }

    /**
     * Gets the current show state of flow labels
     * @return current show state
     */
    public boolean showFlowLabels()
    {
        return c_showFlowLabels;
    }

     /**
     * Sets status of show flow IDs.
     * @param show true for show false for hide
     * @return previous show state
     */
    public boolean showFlowIDs(boolean show)
    {
      boolean temp = c_showFlowIDs;
      c_showFlowIDs = show;
      if (show)
          c_userSettings.setProperty(GUI_SETTINGS_SHOW_FLOW_IDS, "true");
      else
          c_userSettings.setProperty(GUI_SETTINGS_SHOW_FLOW_IDS, "false");

      c_app.getGraphArea().repaint();

      return temp;
    }

    /**
     * Gets the current show state of flow IDs
     * @return current show state
     */
    public boolean showFlowIDs()
    {
        return c_showFlowIDs;
    }

    /**
     * Adds a node folder to the database.
     * @param folder The name of the new folder to add.
     * @param parent The parent flow of the new folder.
     */
    public void addFolderToDatabase(String folder, NodeToDrag parent)
    {
	c_eventHandler.addFolderToDatabase(folder, parent);
    }

    /**
     * Adds a function to a node.
     *
     * @param nodeID The node ID.
     * @param functionType The functiontype to add.
     * @throws ModelException is thrown when FIXME.
     */
    public void addFunction(ID nodeID, String functionType)
    {
	try {
	    c_eventHandler.addFunction(nodeID, functionType);
	    setChanged(true);
	}
	catch(ModelException e) {
	    System.out.println("something");
	}
    }

    /**
     * Adds a node to the database as a new nodetype.
     * @param node The name of the new nodetype to be stored in database.
     * @param folder The folder that the node will be stored in.
     */
    public void addNodeToCollection(String node, NodeToDrag folder)
    {
	ID[] ids = c_app.getGraphArea().getMarkedNodes();

	if (ids.length == 1) {
	    try {
		c_eventHandler.addNodeToCollection(ids[0], node, folder);
		c_app.setAvailableNodes(c_eventHandler.getAvailableNodes());
	    }
	    catch (ModelException e) {
		// FIXME, show message to user
		e.printStackTrace(System.out);
	    }
	}
    }

    /**
     * Adds a node to the database as a new nodetype.
     */
    public void addNodeToCollectionAction()
    {
	ID[] ids = c_app.getGraphArea().getMarkedNodes();
	AddNodeToCollectionDialog dialog;

	if (ids.length > 1)
	    showMessageDialog(ADDNODE_FOR_ONE_NODE_ONLY);
	else if (ids.length > 0) {
	    //	    DefaultMutableTreeNode tree = c_eventHandler.getAvailableNodes();
	    dialog = new AddNodeToCollectionDialog(c_app, true, this, ids[0]);

	    // put the frame in the middle of the screan
	    int height = c_app.getLocation().y + c_app.getSize().height/2;
	    int width = c_app.getLocation().x + c_app.getSize().width/2;
	    int x = (int) (width - dialog.getSize().width/2);
	    int y = (int) (height - dialog.getSize().height/2);
	    dialog.setLocation(x, y);
	    dialog.show();
	}
        else
            showWarningDialog("You must atleast mark one node\n"+
                              "in the graph to add it to the collection.");
    }

    /**
     * Add a new resource to the model.
     * @param name The name of the resource.
     * @param unit The unit the resource will have (e.g. liter).
     * @param prefix The prefix of the resource (e.g. k as in kilo).
     */
    public void addResource(String name, String unit, String prefix, ExtendedColor color)
    {
	c_eventHandler.addResource(name, unit, prefix, color);
    }

    /**
     * A callback that will be triggered when user selects "Cut"
     */
    public void cutAction()
    {
	// we have to find out if grapharea or collection is selected
	// if nodecollection is selected we cut out a nodetype
	// if functioncollection is selected we cut a function
	// if node in grapharea is selected we cut that

//	ID[] marked = c_app.getGraphArea().getMarked();
//	Point point;
//        Vector problems = new Vector();
//        Vector deleted = new Vector();

//	if (marked.length > 1)
//	    showMessageDialog(cutManyMessage);
//	else if (marked.length < 1) ;
//	else if (marked[0].isNode()) {
//	    point = c_app.getGraphArea().getComponentLocation(marked[0]);
//	    c_editable = new Editable(c_eventHandler.getNode(marked[0]),
//				      (int)point.getX(), (int)point.getY());
//	    c_eventHandler.deleteNode(marked[0]);
//	    c_eventHandler.deleteComponent(marked[0], deleted, problems);
//            if (problems.size() == 0) {
//               c_app.getGraphArea().deleteNode(marked[0]);
//            } else {  /* we have som problems! with functions that relate to flows that
//                         we are trying to delete. Show a warning */
//               showMessageDialog("You have problems with your relations dude!!!");
//            }
//	}
//	else
//	    showMessageDialog(cutNonCutableGraphMessage);
//
//	setChanged(true);
    }

    /**
     * A callback that will be triggered when user selects "Copy"
     */
    public void copyAction()
    {
	// depending what is in the editable instance we copy different
	// things

	ID[] marked = c_app.getGraphArea().getMarked();
	Point point;

	if (marked.length > 1)
	    showMessageDialog(copyManyMessage);
	else if (marked.length < 1) ;
	else if (marked[0].isNode()) {
	    point = c_app.getGraphArea().getComponentLocation(marked[0]);
	    c_editable = new Editable(c_eventHandler.getNode(marked[0]),
				      (int)point.getX(), (int)point.getY());
	}
	else
	    showMessageDialog(copyNonCopyableGraphMessage);
    }

    /**
     * A callback that will be triggered when user wants do delete graph components
     * This callback deletes both nodes and flows!
     */
    public void deleteComponentsAction()
    {
        Vector problems = new Vector(); // Vector with strings containing reasons for components that were not deleted
        Vector deleted = new Vector();  // Vector with IDs of those components that were actually deleted
        ID[] sortedToRemove = new ID[1]; // needs to be initialized for JAVA to be happy

	ID[] marked = c_app.getGraphArea().getMarked();
	//System.out.println("Det finns " + marked.length + " objekt markerade");
	ID[] toRemove = c_app.getGraphArea().getAffectedByRemove(marked);
	//System.out.println("Det finns " + toRemove.length + " objekt att ta bort");

        /* toRemove must be sorted so that nodes are deleted before flows */
        if (toRemove.length >=1) {
          sortedToRemove = new ID[toRemove.length];
          int first=0;
          int last=toRemove.length-1;

          for (int i=0; i<toRemove.length; i++) {
            if (toRemove[i].isNode())
              sortedToRemove[first++] = toRemove[i];
            else
              sortedToRemove[last--] = toRemove[i];
          }
        }

	for(int i=0; i<toRemove.length; i++) {
	    c_eventHandler.deleteComponent(sortedToRemove[i], deleted, problems);
	}

        /* Now delete the components that were actually deleted
           And show a message box that shows which components that were not deleted */

        ID[] deleted_arr = new ID[1];
        deleted_arr = (ID[]) deleted.toArray((Object[]) deleted_arr);

        if (deleted.size() > 0) {
          c_app.getGraphArea().deleteNodesAndFlows( deleted_arr);
          setChanged(true);
        }

        if (problems.size() != 0) {
          /* ShowProblemsMessage()
             if c_eventHandler.deleteComponents above couldn't delete all comonents due to realtion
             problems, the vector problems will contain strings with descriptions of these relation problems.
             Show these descriptions. */
          java.util.Iterator it = problems.iterator();
          String message = new String();
          String sep = System.getProperty("line.separator");
          message += "Some components could not be removed due to the following reason(s)" + sep + sep;

          String toadd;
          while (it.hasNext()) {
            toadd = (String) it.next();
            /* remove annoying duplicates
             I'm to tired to figure out why they appear so I just remove them!!! */
            if (message.indexOf(toadd) < 0)
              message += (String) toadd + sep; // the string does not already exist so lets add it
          }
          JOptionPane.showMessageDialog(c_app, message, "Could not delete components",
				      JOptionPane.OK_OPTION);
        }
    }

    /**
     * A callback that will be triggered when user wants to quit reMIND
     */
    public void exitAction()
    {
	int selection = JOptionPane.CLOSED_OPTION;

	cleanTmpFiles();

	if (c_isChanged)
	    selection = JOptionPane.showConfirmDialog(c_app, exitMessage,
						      confirmTitle,
						      JOptionPane.
						      YES_NO_OPTION);
	else {
	    try {
		c_eventHandler.close();
	    }
	    catch (ModelException ex) {
		System.err.println("Database couldn't be closed");
	    }
	    System.exit(0);
	}

	if (selection == JOptionPane.YES_OPTION) {
	    try {
		c_eventHandler.close();
	    }
	    catch (ModelException ex) {
		System.err.println("Database couldn't be closed");
	    }

	    System.exit(0);
	}
    }

    public void exportAction()
    {
        // note: if network connection, files on server should be browsed
        JFileChooser saveAsDialog = new JFileChooser();
        setExportFilters(saveAsDialog);
        saveAsDialog.setDialogTitle("Export as MPS...");
        saveAsDialog.setDialogType(JFileChooser.SAVE_DIALOG);
        saveAsDialog.setApproveButtonText("Export");
        saveAsDialog.setApproveButtonToolTipText("Saves your model " +
						 "in the MPS format.");
      
        if (c_exportDir != null) saveAsDialog.setCurrentDirectory(new File(c_exportDir));

        // show and lock until user has defined a file to save in.
        int returnval = saveAsDialog.showDialog(c_app, "Export");

        if (returnval == JFileChooser.APPROVE_OPTION) {
            // back, we have a filename to save in
            Model model = c_eventHandler.getModel();
            File   fSel  = saveAsDialog.getSelectedFile();
            File   fDir  = fSel.getParentFile();
            String sDir  = (fDir == null) ? null : fDir.getAbsolutePath();
            if (sDir != null) {
                if ((c_exportDir == null) || (!sDir.equals(c_exportDir))) {
                    c_exportDir = fDir.getPath();
                    c_userSettings.setProperty(MPS_EXPORT_FOLDER, c_exportDir);
                }
            }

            try {
                //begin with creating mps file
                // JOptionPane.showMessageDialog(null,"Pleas wait... Creating MPS File");
               
                c_fileInteraction.save(null,model, FileInteraction.MPS, fSel);
               
            } 
            catch (FileInteractionException e) {
                showWarningDialog(e.getMessage());
            }
        // try to sleep 1 second for user's sake to have time see the information in the frame
        
        }
        
        //System.out.println("to export");
    }
    
	/**
	 * Exports model to XML file.
	 * Added by PUM5 2007-10-31
	 * */
    public void exportXmlAction()
    {
    	Model model = c_eventHandler.getModel();
    	Vector <ID> idVect = new Vector <ID>();
    	ID[] ids = c_app.getGraphArea().getMarkedNodes();
    	NodeSelectionDialog selectNodes = 
    		new NodeSelectionDialog(c_app, model, idVect, ids, rmdFileName);
    	
    	//put the frame in the middle of the parent frame
    	int height = c_app.getLocation().y + c_app.getSize().height/2;
    	int width = c_app.getLocation().x + c_app.getSize().width/2;
    	int x = (int) (width - selectNodes.getSize().width/2);
    	int y = (int) (height - selectNodes.getSize().height/2);
    	selectNodes.setLocation(x, y);
    	selectNodes.setVisible(true);
    	
    	boolean locked = selectNodes.getLock();
    	if(selectNodes.exitOption == 1){
    		
    		JFileChooser saveAsDialog = new JFileChooser();
    		setExportXmlFilters(saveAsDialog);
    		saveAsDialog.setDialogTitle("Export as XML...");
    		saveAsDialog.setDialogType(JFileChooser.SAVE_DIALOG);
    		saveAsDialog.setApproveButtonText("Export");
    		saveAsDialog.setApproveButtonToolTipText("Exports your model " + "to the Excel XML format.");
    		
    		//Add date?
    		String withDate = c_userSettings.getProperty(DATE_IN_XML_FILE);
    		String rmdFileNameDate = rmdFileName;
			if (withDate != null) {
			    if (withDate.equals("1")) {
			    	rmdFileNameDate = null;
			    	rmdFileNameDate = getFilenameWithDate(rmdFileName);
			    }
			}
    		saveAsDialog.setSelectedFile(new File(rmdFileNameDate + ".xml")); //Set the default name

    		if (c_exportXmlDir != null) saveAsDialog.setCurrentDirectory(new File(c_exportXmlDir));
    		
    		int returnval = saveAsDialog.showDialog(c_app, "Export");
    		if (returnval == JFileChooser.APPROVE_OPTION) {
    			// back, we have a filename to save to
    			File   fSel  = saveAsDialog.getSelectedFile();
    			File   fDir  = fSel.getParentFile();
    			String sDir  = (fDir == null) ? null : fDir.getAbsolutePath();
    			if (sDir != null) {
    				if ((c_exportXmlDir == null) || (!sDir.equals(c_exportXmlDir))) {
    					c_exportXmlDir = fDir.getPath();
    					c_userSettings.setProperty(XML_EXPORT_FOLDER, c_exportXmlDir);
    				}
    			}
    					
    			//Have to append extension before checking existence
    			String nameTest = fSel.getName();
    			nameTest = getFileNameWithExtension(nameTest, "xml");
    			File fSelWithExtension = new File(fSel.getParent() + File.separatorChar + nameTest);

    			int okToSave = JOptionPane.OK_OPTION;
    			if (fSelWithExtension.exists()) {
    				okToSave = JOptionPane.showConfirmDialog(null,
    						"File already exists - overwrite?", "File exists",
    						JOptionPane.OK_CANCEL_OPTION,
    						JOptionPane.WARNING_MESSAGE);
    			}

    			if (okToSave == JOptionPane.OK_OPTION) {          
    				try {
    					c_fileInteraction.save(idVect, locked, model, FileInteraction.XML, fSel);
    				}
    				catch (FileInteractionException e) {
    					showWarningDialog(e.getMessage());
    				}
    			}
    		}
    	}
    			
    		
    }
    
	/**
	 * Imports XML file to model.
	 * Added by PUM5 2007-10-31
	 * @return A String[] with the formats.
	 * */
    public void importScenarioAction()
    {
        int selection = JOptionPane.CLOSED_OPTION;

        if (selection == JOptionPane.NO_OPTION) {
            return;
        }

        JFileChooser importDialog = new JFileChooser();
        setImportFilters(importDialog);
        importDialog.setDialogTitle("Import...");
        importDialog.setDialogType(JFileChooser.OPEN_DIALOG);
        importDialog.setApproveButtonText("Import");
        importDialog.setApproveButtonToolTipText("Imports a model.");
   
        if (c_exportXmlDir != null) importDialog.setCurrentDirectory(new File(c_exportXmlDir));
        
        // show and lock until user has defined a file to import from.
        int returnval = importDialog.showDialog(c_app, "Import");

        if (returnval == JFileChooser.APPROVE_OPTION) {
        	
            // Get import file from disk
            File   fSel = importDialog.getSelectedFile();
            File   fDir = fSel.getParentFile();
            String sDir = (fDir == null) ? null : fDir.getAbsolutePath();
            zoomAction("100 %");
            try {
                c_fileInteraction.load(c_eventHandler.getModel(),
                                       c_app.getGraphArea().getModel(),
                                       FileInteraction.XML,
                                       fSel);
  
                zoomAction(c_topToolbar.c_combo.getSelectedItem().toString());
                JOptionPane.showMessageDialog(null, "Model successfully updated", "Import", 1);
            }
            catch (FileInteractionException e) {
            	//FIXME this seems to release the lock
            	c_eventHandler.getModel().getAllNodes().toXML(c_eventHandler.getModel().getResourceController(),0);
            	showWarningDialog(e.getMessage());
            }
            c_app.settingsUpdated();
           
        }
    }

    public void doubleClickFlowAction()
    {
      JDialog dialog;

      ID[] ids = c_app.getGraphArea().getMarkedFlows();

      if (ids.length > 1)
        showMessageDialog(PROPERTIES_FOR_ONE_NODE_ONLY);
      else if (ids.length == 1) {
        dialog = new FlowPropertiesDialog(c_app, true, this, ids[0]);
        // put the frame in the middle of the screan
        int height = c_app.getLocation().y + c_app.getSize().height / 2;
        int width = c_app.getLocation().x + c_app.getSize().width / 2;
        int x = (int) (width - dialog.getSize().width / 2);
        int y = (int) (height - dialog.getSize().height / 2);
        dialog.setLocation(x, y);
        dialog.show();
      }
    }

    public void flowPropertiesAction()
    {
	ID[] ids = c_app.getGraphArea().getMarkedFlows();

	if (ids.length > 1)
	    showMessageDialog(PROPERTIES_FOR_ONE_FLOW_ONLY);
	else if (ids.length == 1) {
	    JDialog dialog;

	    dialog = new FlowPropertiesDialog(c_app, true, this, ids[0]);

	    // put the frame in the middle of the screan
	    int height = c_app.getLocation().y + c_app.getSize().height/2;
	    int width = c_app.getLocation().x + c_app.getSize().width/2;
	    int x = (int) (width - dialog.getSize().width/2);
	    int y = (int) (height - dialog.getSize().height/2);
	    dialog.setLocation(x, y);

	    dialog.show();
	}

    }

    /**
     * Gets all function labels that belongs to the specified node.
     * @param nodeID The ID of the node to get the functions from.
     * @return A Vector with function labels.
     */
    public Vector getAllFunctionLabels(ID nodeID)
    {
	return c_eventHandler.getAllFunctionLabels(nodeID);
    }

    /**
     * Gets the main window of the GUI.
     */
    public JFrame getApp()
    {
	return c_app;
    }

    /**
     * Gets the tree of nodes from database.
     * @return The root node that contains the whole tree.
     */
    public DefaultMutableTreeNode getAvailableNodes()
    {
	return c_eventHandler.getAvailableNodes();
    }

    /**
     * Gets the eventHandlerCLinet
     * @return The event handler for the client
     */
    // FIXME FIXME
    public EventHandlerClient getEventHandlerClient()
    {
	return c_eventHandler;
    }

    /**
     * Gets a certain function from a node.
     * @param nodeID The ID of the node the function belongs to.
     * @param functionLabel The label of the function we want.
     * @return The function asked for.
     */
    public NodeFunction getFunction(ID nodeID, String functionLabel)
    {
	return c_eventHandler.getFunction(nodeID, functionLabel);
    }

    /**
     * Gets the function type of the specified function.
     * @param nodeID The ID of the node the function belongs to.
     * @param functionLabel The label of the function we want the type of.
     * @return The function type.
     */
    public String getFunctionType(ID nodeID, String functionLabel)
    {
	return c_eventHandler.getFunctionType(nodeID, functionLabel);
    }

    public Flow[] getAllFlows()
    {
      return c_eventHandler.getAllFlows();
    }

    public Flow[] getInFlows(ID nodeID)
    {
	return c_eventHandler.getInFlows(nodeID);
    }

    public Flow[] getOutFlows(ID nodeID)
    {
	return c_eventHandler.getOutFlows(nodeID);
    }

    /**
     * Gets the label of a certain component.
     * @param componentID The ID of the component.
     * @return The label of this component.
     */
    public String getLabel(ID componentID)
    {
	return c_eventHandler.getLabel(componentID);
    }

    /**
     * Gets the notes of a node.
     * @param componentID The ID of the component.
     * @return The label of this component.
     */
    public String getNote(ID componentID)
    {
	return c_eventHandler.getNote(componentID);
    }


    /**
     * Gets a resource from somewhere down below
     * @param resource The ID of the flow/Resource to get the resource from.
     * @return A resource object, null if not found.
     */
    public Resource getResource(ID resource)
    {
	return c_eventHandler.getResource(resource);
    }

    /**
     * Gets all resources.
     * @return All resources available.
     */
    public Vector getResources()
    {
	return c_eventHandler.getResources();
    }
    
    /**
      *Added by Nawzad Mardan 2007-06-07
      * Returns the DiscountedsystemcostControl
    */
    public DiscountedsystemcostControl getDiscountedsystemcostControl()
     {
        return c_eventHandler.getDiscountedsystemcostControl();
     }
    
     /**Get all Nodes from the modell
         *@param void
         *@return all nodes
         *Added by Nawzad Mardan 070801
         *used by Discountedsystemcost function
      */
         
     public NodeControl getAllNodes()
     {
             return c_eventHandler.getAllNodes();
     }
         
     
     /**
      *Added by Nawzad Mardan 2007-06-07
      * Sets the rate, period analyses, tabelHeader and the table data in the DiscountedsystemcostControl
    */
    
    public void setDiscountedsystemcostControl(Float rate, Long year, String head [], Object [][] data, Vector timestepValues)
    {
        c_eventHandler.setDiscountedsystemcostControl(rate, year , head, data,timestepValues);
    }
    /**
     * Gets the timestep level of a certain node.
     */
    public Timesteplevel getTimesteplevel(ID nodeID)
    {
	return c_eventHandler.getTimesteplevel(nodeID);
    }

    /**
     * Gets the name of the timestep levels.
     * @return A vector of strings, where each string is a timestep level.
     */
    public Vector getTimesteplevelsVector()
    {
	return c_eventHandler.getTimesteplevelsVector();
    }

    public Timesteplevel getTopTimesteplevel()
    {
	return c_eventHandler.getTopTimesteplevel();
    }

    /**
     * Gets the x-coordinate of the component with the specified ID.
     * @param componentID The ID of the component.
     * @return The x-coordinate of the component.
     */
    public int getX(ID componentID)
    {
	return c_app.getGraphArea().getX(componentID);
    }

    /**
     * Gets the y-coordinate of the component with the specified ID.
     * @param componentID The ID of the component.
     * @return The y-coordinate of the component.
     */
    public int getY(ID componentID)
    {
	return c_app.getGraphArea().getY(componentID);
    }

    /**
     * Opens a dialog where you can remove node types.
     */
    public void manageNodeCollectionAction()
    {
        showWarningDialog("Function is not yet implemented.");
    }

    /**
     * A callback that will be triggered when user wants do create
     * new flow(s)
     */
    public void newFlowAction()
    {
	c_app.getGraphArea().setFlowMode();
	setChanged(true);
    }

    /**
     * This call comes from GraphArea, it's called when a new flow is added
     */
    public void newFlowAction(ID from, ID to)
    {
	ID flowID = c_eventHandler.newFlow(from, to);
	if (flowID != null) {
	    setChanged(true);
	    c_app.getGraphArea().newFlow(flowID, from, to);
	}
    }

    /**
     * Adds a component to the model.
     * @param graphComponent The component that should be added.
     */
    public void newNodeAction(Editable editable)
    {
	try {
        Object obj = editable.getComponent();
        ID nodeID  = null;
        if (obj instanceof Node) {
            nodeID = c_eventHandler.newNode((Node)obj);

            if (nodeID != null) {
                int x = editable.getX() - 20;
                int y = editable.getY() - 20;
                c_app.getGraphArea().newNode(nodeID, x, y);
                setChanged(true);
            }
        }
	}
	catch(ModelException e) {
	    e.printStackTrace(System.out);
	    System.out.println("Unknown nodeID.");
	}
	catch(RmdParseException e) {
	    showWarningDialog(e.getMessage());
	}

    }

    /**
     * Adds a component to the model.
     * @param x The x-coordinate where the node should be added.
     * @param y The y-coordinate where the node should be added.
     * @param nodeType The type of the node to create.
     */
    public void newNodeAction(int x, int y, String nodeType)
    {
	ID nodeID = null;

	try {
	    nodeID = c_eventHandler.newNode(nodeType);
	    if (nodeID != null) {
		c_app.getGraphArea().newNode(nodeID, x, y);
		setChanged(true);
	    }
	}
	catch(ModelException e) {
	    e.printStackTrace(System.out);
	}
	catch(RmdParseException e) {
	    showWarningDialog(e.getMessage());
	}
    }

    /**
     * A callback that will be triggered when user wants do create
     * new node(s). The default will be "Blank node".
     */
    public void newNodeAction()
    {
	newNodeAction(0, 0, "Blank node");
    }

    /**
     * A callback that will be triggered when user wants a new scenario
     */
    public void newScenarioAction()
    {
	int selection = JOptionPane.CLOSED_OPTION;

	if (c_isChanged)
	{
	    selection = JOptionPane.showConfirmDialog(c_app, newMessage,
						      messageTitle,
						      JOptionPane.
						      YES_NO_OPTION);
	    if (selection != JOptionPane.YES_OPTION)
		//Return to the old scenario
		return;
	}

	//Close the database so createNewModel can open it again.
	try {
		c_eventHandler.close();
	}
	catch (ModelException e) {
	    System.err.println("Database couldn't be closed");
	    e.printStackTrace(System.out);
	    //System.exit(1);
	};
	c_eventHandler.createNewModel(true);
	c_app.getGraphArea().createNewModel();
	setChanged(false);
	c_savedModel = null;
        c_app.setTitleAddon("Untitled");
    rmdFileName = "Untitled";
    }

    public void doubleClickNodeAction() {
      JDialog dialog;

      ID[] ids = c_app.getGraphArea().getMarkedNodes();

      if (ids.length > 1)
        showMessageDialog(PROPERTIES_FOR_ONE_NODE_ONLY);
      else if (ids.length == 1) {
        dialog = new NodePropertiesDialog(c_app, true, this, ids[0]);
        // put the frame in the middle of the screan
        int height = c_app.getLocation().y + c_app.getSize().height / 2;
        int width = c_app.getLocation().x + c_app.getSize().width / 2;
        int x = (int) (width - dialog.getSize().width / 2);
        int y = (int) (height - dialog.getSize().height / 2);
        dialog.setLocation(x, y);
        dialog.show();
      }
    }


    /**
     * Opens the properties for the nodes that are marked.
     */
    public void nodePropertiesAction()
    {
	JDialog dialog;
	ID[] ids = c_app.getGraphArea().getMarkedNodes();

	if (ids.length > 1)
	    showMessageDialog(PROPERTIES_FOR_ONE_NODE_ONLY);
	else if (ids.length == 1) {
	    dialog = new NodePropertiesDialog(c_app, true, this, ids[0]);

	    // put the frame in the middle of the screan
	    int height = c_app.getLocation().y + c_app.getSize().height/2;
	    int width = c_app.getLocation().x + c_app.getSize().width/2;
	    int x = (int) (width - dialog.getSize().width/2);
	    int y = (int) (height - dialog.getSize().height/2);
	    dialog.setLocation(x, y);

	    dialog.show();
	}
    }

    /**
     * A callback that will be triggered when user wants to open a saved
     * scenario
     */
    public void openScenarioAction()
    {
        int selection = JOptionPane.CLOSED_OPTION;

        if (c_isChanged)
            selection = JOptionPane.showConfirmDialog(c_app, openMessage,
                                                      messageTitle,
                                                      JOptionPane.
                                                      YES_NO_OPTION);
        if (selection == JOptionPane.NO_OPTION) {
            return;
        }

        // FIXME
        // files on server should be browsed

        JFileChooser openDialog = new JFileChooser();
        setLoadFilters(openDialog);
        openDialog.setDialogTitle("Open...");
        openDialog.setDialogType(JFileChooser.OPEN_DIALOG);
        openDialog.setApproveButtonText("Open");
        openDialog.setApproveButtonToolTipText("Opens a model.");
        openDialog.setCurrentDirectory(getModelsDir());

        // show and lock until user has defined a file to save in.
        int returnval = openDialog.showDialog(c_app, "Open");
        

        if (returnval == JFileChooser.APPROVE_OPTION) {

            //Throw away the old model (we must do this)
            try {
                c_eventHandler.close();
            
            }
            catch (ModelException e) {
                e.printStackTrace(System.out);
                System.err.println("Database couldn't be closed");
            };
            //Create new model
            c_eventHandler.createNewModel();
            c_app.getGraphArea().createNewModel();
            setChanged(false);

            //Load model from disk
            File   fSel = openDialog.getSelectedFile();
            //Added by PUM5 2007-11-30
            rmdFileName = fSel.getName(); //Needed if EXML file is supposed to have the same name
            rmdFileName = rmdFileName.substring(0, rmdFileName.length() - 4);
            //End PUM5
            File   fDir = fSel.getParentFile();
            String sDir = (fDir == null) ? null : fDir.getAbsolutePath();
            zoomAction("100 %");
            try {
                c_fileInteraction.load(c_eventHandler.getModel(),
                                       c_app.getGraphArea().getModel(),
                                       FileInteraction.RMD,
                                       fSel);
                c_savedModel = fSel.getAbsolutePath();
                c_app.setTitleAddon(c_savedModel); // long name incl. path

                if (sDir != null) {
                    if ((c_modelsDir == null) || (!sDir.equals(c_modelsDir))) {
                        c_modelsDir  = sDir;
                        c_userSettings.setProperty(RMD_MODEL_FOLDER, c_modelsDir);
                    }
                }
                // c_app.setTitleAddon(openDialog.getSelectedFile().getName()); // short name excl. path
                zoomAction(c_topToolbar.c_combo.getSelectedItem().toString());
                
            }
            catch (FileInteractionException e) {
                //Erase the model so we don't get a "half finished" model
                try {
                    c_eventHandler.close();
                }
                catch (ModelException e2) {
                    e2.printStackTrace(System.out);
                    System.err.println("Database couldn't be closed");
                };
                c_eventHandler.createNewModel();
                c_app.getGraphArea().createNewModel();
                c_isChanged = false;

                showWarningDialog(e.getMessage());
            }
            
            c_app.settingsUpdated();
        }
    }
/* Added by Nawzad Mardan 090304*/
    public void openArgAction(File fSel)
    {
            //Create new model
            c_eventHandler.createNewModel();
            c_app.getGraphArea().createNewModel();
            setChanged(false);

            //Load model from disk
            //File   fSel = openDialog.getSelectedFile();
            //openDialog.
            //Added by PUM5 2007-11-30
            rmdFileName = fSel.getName(); //Needed if EXML file is supposed to have the same name
            rmdFileName = rmdFileName.substring(0, rmdFileName.length() - 4);
            //End PUM5
            File   fDir = fSel.getParentFile();
            String sDir = (fDir == null) ? null : fDir.getAbsolutePath();
            zoomAction("100 %");
            try {
                c_fileInteraction.load(c_eventHandler.getModel(),c_app.getGraphArea().getModel(),FileInteraction.RMD,fSel);
                c_savedModel = fSel.getAbsolutePath();
                c_app.setTitleAddon(c_savedModel); // long name incl. path

                if (sDir != null) 
                    {
                    if ((c_modelsDir == null) || (!sDir.equals(c_modelsDir))) 
                        {
                        c_modelsDir  = sDir;
                        c_userSettings.setProperty(RMD_MODEL_FOLDER, c_modelsDir);
                        }
                    }
                // c_app.setTitleAddon(openDialog.getSelectedFile().getName()); // short name excl. path
                zoomAction(c_topToolbar.c_combo.getSelectedItem().toString());
                
                }
            catch (FileInteractionException e) 
                {
                //Erase the model so we don't get a "half finished" model
                try {
                    c_eventHandler.close();
                    }
                catch (ModelException e2) 
                    {
                    e2.printStackTrace(System.out);
                    System.err.println("Database couldn't be closed");
                    };
                c_eventHandler.createNewModel();
                c_app.getGraphArea().createNewModel();
                c_isChanged = false;

                showWarningDialog(e.getMessage());
                }
            
            c_app.settingsUpdated();
        }
    
    /**
     * Optimizes the model.
     * PUM16 reMind 2004
     */
    public void optimizeAction()
    {
	String filename = c_savedModel;
        
        //Nawzad Mardan added 2007-07-07
        setButtonEnabled(false);
        //c_topToolbar.c_OptimizeButton.setEnabled(false);
        //c_topToolbar.c_OptimizeWithSettingsButton.setEnabled(false);
       // c_app.setEnabled(false);
	
        if(filename == null || filename.equals("")) {
	    JOptionPane.showMessageDialog(null, "Load a model to"
					  + " optimize first.");
            //Nawzad Mardan added 2007-07-07   
            setButtonEnabled(true);
             return;
	}

	filename = getFilenameWithoutExtension(filename);

	String withDate =
	    c_userSettings.getProperty(DATE_IN_MPS_FILE);
	if (withDate != null) {
	    if (withDate.equals("1")) {
		filename = getFilenameWithDate(filename);
	    }
	}
	//Added by PUM5 2007-12-06
	//filename += ".mps";
	filename = getFileNameWithExtension(filename, "mps");
	//End PUM5
	
//String getProperty(String key) Searches for the property with the specified key in this property list. 
	String optimizer = c_userSettings.getProperty(OPTIMIZER);
	String path = null;
	if(optimizer == null) {
	    optimizer = "None";
	} else if(optimizer.equals(OPT_CPLEX)) {
	    path = c_userSettings.getProperty(CPLEX_PATH);
	} else if (optimizer.equals(OPT_LPSOLVE)) {
            path = c_userSettings.getProperty(LPSOLVE_PATH);
        }

	OptimizationResult result = null;
	boolean optimizationSuccessful = true;
       
        
	try {
            // JOptionPane.showMessageDialog(null,"Pleas wait... Optimizing");
         
	    result = c_eventHandler.optimize(filename, optimizer, path);
            } 
        catch(IllegalArgumentException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    optimizationSuccessful = false;
	} catch(FileInteractionException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    optimizationSuccessful = false;
 	} catch(OptimizationException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    optimizationSuccessful = false;
	} catch(FileNotFoundException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    optimizationSuccessful = false;
	} catch(IOException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    optimizationSuccessful = false;
	}

	if(!optimizationSuccessful) {
            //Nawzad Mardan added 2007-07-07
            setButtonEnabled(true);
            return;
	}

	boolean showOptimizerOutput = false;
	String showOptOut =
	    c_userSettings.getProperty(SHOW_OPT_OUT);
	if (showOptOut != null) {
	    if (showOptOut.equals("1")) {
		showOptimizerOutput = true;
	    }
	}

	if(result == null || showOptimizerOutput) {
	    /* OptimizeController encountered some error while running,
	     * display optimization output */
	    OptimizerOutputDialog dialog =
		OptimizerOutputDialog.createInstance(c_app, true);
	    dialog.pack();

	    dialog.setOptimizerOutput(c_eventHandler.getOptimizerOutput());

	    // put the frame in the middle of the parent frame
	    int height = c_app.getLocation().y + c_app.getSize().height/2;
	    int width = c_app.getLocation().x + c_app.getSize().width/2;
	    int x = (int) (width - dialog.getSize().width/2);
	    int y = (int) (height - dialog.getSize().height/2);
	    dialog.setLocation(x, y);

	    dialog.show();
	}

	if(result != null) {
	    OutputFlowChooserDialog dialog =
		OutputFlowChooserDialog.createInstance(c_app, true,
						       c_userSettings);
	    dialog.pack();

	    dialog.loadFlows(result);

	    // put the frame in the middle of the parent frame
	    int height = c_app.getLocation().y + c_app.getSize().height/2;
	    int width = c_app.getLocation().x + c_app.getSize().width/2;
	    int x = (int) (width - dialog.getSize().width/2);
	    int y = (int) (height - dialog.getSize().height/2);
	    dialog.setLocation(x, y);

	    dialog.show();
            //Added by Nawzad Mardan 2007-07-07
            setButtonEnabled(true);
            
	}
        //Added by Nawzad Mardan 2007-07-07
        setButtonEnabled(true);
           
            
    }
    
    /*
     * Set all button disable when user clicks on optimize button
     *
     */
    private void setButtonEnabled(boolean enabled)
    {
       c_app.c_topMenu.getFileMenu().setEnabled(enabled);
       c_app.c_topMenu.getModelMenu().setEnabled(enabled);
      // c_app.setEnabled(enabled);
       
       c_topToolbar.c_OptimizeButton.setEnabled(enabled);
       c_topToolbar.c_OptimizeWithSettingsButton.setEnabled(enabled);
       c_topToolbar.c_ExportButton.setEnabled(enabled);
       c_topToolbar.c_FlowIDsButton.setEnabled(enabled);
       c_topToolbar.c_FlowLabelsButton.setEnabled(enabled);
       c_topToolbar.c_NodeIDsButton.setEnabled(enabled);
       c_topToolbar.c_NodeLabelsButton.setEnabled(enabled);
       c_topToolbar.c_SaveButton.setEnabled(enabled);
       c_topToolbar.c_combo.setEnabled(enabled);
       
    }

    /**
     * Optimizes the model with settings.
     * PUM16 reMind 2004
     */
    public void optimizeWithSettingsAction()
    {
   	JDialog dialog =
	    OptimizeSettingsDialog.createInstance(c_app, true, c_userSettings,
						  c_eventHandler);
	dialog.pack();

	// put the frame in the middle of the parent frame
	int height = c_app.getLocation().y + c_app.getSize().height/2;
        int width = c_app.getLocation().x + c_app.getSize().width/2;
        int x = (int) (width - dialog.getSize().width/2);
        int y = (int) (height - dialog.getSize().height/2);
	dialog.setLocation(x, y);

	dialog.show();
    }

    /**
     * A callback that is triggered when user wants the "Options" dialog
     */
    public void optionsAction()
    {
	JDialog dialog = OptionsDialog.createInstance(c_app, true,
						      c_userSettings);
	dialog.pack();

	// put the frame in the middle of the parent frame
	int height = c_app.getLocation().y + c_app.getSize().height/2;
        int width = c_app.getLocation().x + c_app.getSize().width/2;
        int x = (int) (width - dialog.getSize().width/2);
        int y = (int) (height - dialog.getSize().height/2);
	dialog.setLocation(x, y);

	dialog.show();

        int autosavestate = Short.parseShort(c_userSettings.getProperty(mind.gui.UserSettingConstants.AUTOSAVE_STATE));
        int savetime = Short.parseShort(c_userSettings.getProperty(mind.gui.UserSettingConstants.AUTOSAVE));

        setAutosaveState((autosavestate == 1) ? true:false, savetime);

        c_app.settingsUpdated();

	//Removed 2004-11-23 PUM16
	// update settings
	/*String minutes = c_userSettings.getProperty(AUTOSAVE);
	if (minutes != null) {
	    short minutesNumber = Short.parseShort(minutes);
	    if (minutesNumber > 0)
		c_tasks.setAutosave(minutesNumber);
	}
	else
	    c_tasks.setAutosave(0);
	// tell maindialog that settings got updated
	c_app.settingsUpdated();*/
    }



    /**
     * A callback that is triggered when user wants the "Resources" dialog
     */
    public void resourcesAction()
    {
	JDialog dialog = new ResourcesDialog(c_app, true, this);

	dialog.pack();

	// put the frame in the middle of the parent frame
	int height = c_app.getLocation().y + c_app.getSize().height/2;
        int width = c_app.getLocation().x + c_app.getSize().width/2;
        int x = (int) (width - dialog.getSize().width/2);
        int y = (int) (height - dialog.getSize().height/2);
	dialog.setLocation(x, y);

	dialog.show();

	// assuming something was changed with the resources
	setChanged(false);
    }
    
    
    /** Added by Nawzad Mardan 07-06-01
     * A callback that is triggered when user selects
     */
    
    public void discountdcostAction()
    {
        JDialog dialog = new DiscountedsystemcostDialog(c_app, true, this);

	dialog.pack();

	// put the frame in the middle of the parent frame
	int height = c_app.getLocation().y + c_app.getSize().height/2;
        int width = c_app.getLocation().x + c_app.getSize().width/2;
        int x = (int) (width - dialog.getSize().width/2);
        int y = (int) (height - dialog.getSize().height/2);
	dialog.setLocation(x, y);

	dialog.setVisible(true);
        setChanged(true);
    }


    /**
     * A callback that is triggered when user selects "Paste"
     */
    public void pasteAction()
    {
	if (c_editable != null) {
	    //test
	    Node n = (Node) c_editable.getComponent();
	    FunctionControl fc = n.getAllFunctions();
	    //System.out.println("Vid Paste-antal funktioner=" + fc.size());
	    //test slut

	    newNodeAction(c_editable);
	}
    }

    /**
     * Removes a function from a node.
     * @param nodeID The ID of the node.
     * @param function The function to remove.
     */
    public void removeFunction(ID nodeID, String function)
    {
	try {
	    c_eventHandler.removeFunction(nodeID, function);
	    setChanged(true);
	}
	catch (ModelException e) {
	    showWarningDialog("Couldn't remove function from node");
	}
    }

    /**
     * A callback that will be triggered when user wants to save a scenario
     * @return true if the file was saved false if not saved
     */
    public boolean saveAsScenarioAction()
    {
        // note: if network connection, files on server should be browsed
        JFileChooser saveAsDialog = new JFileChooser();
        setSaveFilters(saveAsDialog);
        saveAsDialog.setDialogTitle("Save as...");
        saveAsDialog.setDialogType(JFileChooser.SAVE_DIALOG);
        saveAsDialog.setApproveButtonText("Save");
        saveAsDialog.setApproveButtonToolTipText("Saves your model " +
                                                 "in the specified format.");
        saveAsDialog.setCurrentDirectory(getModelsDir());

        // show and lock until user has defined a file to save in.
        int returnval = saveAsDialog.showDialog(c_app, "Save");
        if (returnval == JFileChooser.APPROVE_OPTION) {
          // back, we have a filename to save in
          Model model = c_eventHandler.getModel();
          File fSel = saveAsDialog.getSelectedFile();
          File fDir = fSel.getParentFile();
          String sDir = (fDir == null) ? null : fDir.getAbsolutePath();
          //Added by PUM5 2007-11-30
          rmdFileName = getFilenameWithoutExtension(fSel.getName()); //Needed if EXML file is supposed to have the same name 
          //End PUM5
          
          int okToSave = JOptionPane.OK_OPTION;

          if (fSel.exists()) {
            okToSave = JOptionPane.showConfirmDialog(null,
                "File already exists - overwrite?", "File exists",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);
          }

          if (okToSave == JOptionPane.OK_OPTION) {
            try {
              c_fileInteraction.save(model, c_app.getGraphArea().getModel(), this,
                                     FileInteraction.RMD, fSel);
              c_savedModel = fSel.getAbsolutePath();
              c_app.setTitleAddon(c_savedModel);

              if (sDir != null) {
                if ( (c_modelsDir == null) || (!sDir.equals(c_modelsDir))) {
                  c_modelsDir = sDir;
                  c_userSettings.setProperty(RMD_MODEL_FOLDER, c_modelsDir);
                }
              }
              //System.out.println("saved = " + c_savedModel);
              setChanged(false);
            }
            catch (FileInteractionException e) {
              showWarningDialog(e.getMessage());
            }

            c_topToolbar.c_SaveButton.setEnabled(false);
            return true;
          }
          return false;
        }
        return false;
    }

    /**
     * A callback that will be triggered when user wants to save a scenario
     * @param force Should be true
     */
    public void saveScenarioAction(boolean force)
    {
	// FIXME
	// model should be saved on server
	// print "save" to statusbar
        boolean saved = false;

	if (c_isChanged) {
	    if (c_savedModel == null) {
		if (force) {
		    saved = saveAsScenarioAction();
		}
	    }
	    else {
		try {
		    c_fileInteraction.save(c_eventHandler.getModel(),
					   c_app.getGraphArea().getModel(), this,
					   FileInteraction.RMD,
					   new File(c_savedModel));
		    setChanged(false);
                    saved = true;
		}
		catch (FileInteractionException e) {
		    showWarningDialog(e.getMessage());
		}
	    }
            if (saved)
              c_topToolbar.c_SaveButton.setEnabled(false);
	}
    }

    /**
     * Sets the label of a function to something else.
     * @param nodeID The ID of the node the function belongs to.
     * @param functionLabel The label of the function to change the label of.
     * @param newFunctionLabel The new label the function will have.
     */
    public void setFunctionLabel(ID nodeID, String functionLabel,
				 String newFunctionLabel)
    {
	c_eventHandler.setFunctionLabel(nodeID, functionLabel,
					newFunctionLabel);
	setChanged(true);
    }

    /**
     * Sets the label of a certain flow.
     * @param flowID The ID of the flow.
     * @return The label that should be set on this flow.
     */
    public void setFlowLabel(ID flowID, String label)
    {
	try {
	    c_eventHandler.setFlowLabel(flowID, label);
	    setChanged(true);
	}
	catch(ModelException e) {
	    e.printStackTrace(System.out);
	}
    }

    /**
     * Sets the label of a certain node.
     * @param nodeID The ID of the node.
     * @return The label that should be set on this node.
     */
    public void setNodeLabel(ID nodeID, String label)
    {
	try {
	    c_eventHandler.setNodeLabel(nodeID, label);
	    setChanged(true);
	}
	catch(ModelException e) {
	    e.printStackTrace(System.out);
	}
    }

    /**
     * Sets the note of a certain node.
     * @param nodeID The ID of the node.
     * @return The label that should be set on this node.
     */
    public void setNodeNote(ID nodeID, String note)
    {
	try {
	    c_eventHandler.setNodeNote(nodeID, note);
	    setChanged(true);
	}
	catch(ModelException e) {
	    e.printStackTrace(System.out);
	}
    }

    /**
     * Sets new coordinates for the specified node.
     * @param nodeID The ID of the node to get new location coordinates.
     * @param x The x-coordinate of the new location.
     * @param y The y-coordinate of the new location.
     */
    public void setNodeLocation(ID nodeID, int x, int y)
    {
	c_app.getGraphArea().setNodeLocation(nodeID, x, y);
	setChanged(true);
    }

    /**
     * Sets the timestep level of a certain node.
     * @param nodeID ID of the node to change timestep level of.
     * @param label The label of the timestep to change to.
     */
    public void setTimesteplevel(ID nodeID, String label)
    {
	c_eventHandler.setTimesteplevel(nodeID, label);
	setChanged(true);
    }

    /**
     * Shows a message dialog to the user
     * @return The option the user chose
     */
    public void showMessageDialog(String message)
    {
	JOptionPane.showMessageDialog(c_app, message, messageTitle,
				      JOptionPane.OK_OPTION);
    }

    public void showFlowIDsAction(boolean show)
    {
	c_app.getGraphArea().showFlowIDs(show);
    }

    public void showFlowLabelsAction(boolean show)
    {
	c_app.getGraphArea().showFlowLabels(show);
    }

    public void showNodeIDsAction(boolean show)
    {
	c_app.getGraphArea().showNodeIDs(show);
    }

    public void showNodeLabelsAction(boolean show)
    {
	c_app.getGraphArea().showNodeLabels(show);
    }


    public void showWarningDialog(String message)
    {
	JOptionPane.showMessageDialog(c_app, message, messageTitle,
				      JOptionPane.OK_OPTION);
    }

    public void showWarningDialog(String messageTitle, String message)
    {
	JOptionPane.showMessageDialog(c_app, message, messageTitle,
				      JOptionPane.OK_OPTION);
    }


    /**
     * A callback that is triggered when user wants the "Timesteps" dialog
     */
    public void timestepsAction()
    {
	JDialog dialog = new TimestepsDialog(c_app, true, this);

	dialog.pack();

	// put the frame in the middle of the parent frame
	int height = c_app.getLocation().y + c_app.getSize().height/2;
        int width = c_app.getLocation().x + c_app.getSize().width/2;
        int x = (int) (width - dialog.getSize().width/2);
        int y = (int) (height - dialog.getSize().height/2);
	dialog.setLocation(x, y);

	dialog.show();

	// assuming something was changed
	setChanged(true);
    }

    /**
     * A callback that is triggered when user selects "Undo"
     */
    public void undoAction()
    {
	// FIXME
	// should we really support undo?
        showWarningDialog("Function is not implemented");
    }

    /**
     * Shows the about dialog
     */
    public void aboutAction()
    {
	JDialog dialog = new AboutDialog(c_app, true);

	// dialog.pack();

	// put the frame in the middle of the parent frame
	int height = c_app.getLocation().y + c_app.getSize().height/2;
        int width = c_app.getLocation().x + c_app.getSize().width/2;
        int x = (int) (width - dialog.getSize().width/2);
        int y = (int) (height - dialog.getSize().height/2);
	dialog.setLocation(x, y);

	dialog.show();
    }

    public void setChanged(boolean changed)
    {
	c_isChanged = changed;
        c_topToolbar.c_SaveButton.setEnabled(true);
    }

    /**
     * Sets a resource for something
     * @param where Where to set the resource
     * @param what What resource to set
     */
    public void setResource(ID where, ID what)
    {
	c_eventHandler.setResource(where,what);
	setChanged(true);
    }

    /**
     * Remove a resource.
     * @param ID The resource to remove
     */
    public ID removeResource(ID resourceID)
	throws ModelException
    {
	setChanged(true);
	return c_eventHandler.removeResource(resourceID);
    }

    /**
     * User requests to zoom in or out.
     * @param selected The zoom procent selected.
     */
    public void zoomAction(String selected)
    {
	c_zoom.setSelected(selected);
	if (c_app != null) {
          c_app.getGraphArea().zoom(c_zoom.getValue());
          setChanged(true);
        }
    }

    /**
     * User request to print model report. GUI respond with a
     * print dialog window
     */
    public void printAction()
    {
	//open a print dialog window
	JDialog dialog = new PrintDialog(c_app, true, this);

	dialog.pack();

	// put the frame in the middle of the parent frame
	int height = c_app.getLocation().y + c_app.getSize().height/2;
        int width = c_app.getLocation().x + c_app.getSize().width/2;
        int x = (int) (width - dialog.getSize().width/2);
        int y = (int) (height - dialog.getSize().height/2);
	dialog.setLocation(x, y);

	dialog.show();

    }

    /**
     * User request to preview the model report. GUI respond with a
     * preview dialog with a preview of the report and the buttons
     * Close (Close the preview window)
     * Print (Print the preview to printer)
     */
    public void previewAction()
    {
	//open a print preview dialog window
	JDialog dialog = new PreviewDialog(c_app, true, this);

	dialog.pack();

	// put the frame in the middle of the parent frame
	int height = c_app.getLocation().y + c_app.getSize().height/2;
        int width = c_app.getLocation().x + c_app.getSize().width/2;
        int x = (int) (width - dialog.getSize().width/2);
        int y = (int) (height - dialog.getSize().height/2);
	dialog.setLocation(x, y);

	dialog.show();//opens a preview dialog window

    }

    /**
     * Get an enumeration of nodes used by report builder
     */
    public Enumeration getNodes()
    {
	return c_eventHandler.getNodes();
    }
    /**
     * Set print settings
     */
    public void setPrintSettings(PrintSettings settings){
	c_printsettings = settings;
    }
    /**
     * get print settings
     */
    public PrintSettings getPrintSettings(){
	return c_printsettings;
    }

    /**
     * Adds a date to a filename
     * @param filename Filename to add date to
     */
    public String getFilenameWithDate(String filename) {
	SimpleDateFormat dateFormat =
	    new SimpleDateFormat("_yyyy-MM-dd_HH_mm_ss");
	String date = dateFormat.format(new Date());
	return filename + date;
    }

    /**
     * Removed the extension from a filenamen
     * @param filename Filename to remove extension from
     */
    public String getFilenameWithoutExtension(String filename) {
	int index = filename.lastIndexOf('.');
	if (index == -1)
	    return filename;
	return filename.substring(0, index);
    }
    
    /**
     * PUM5 2007-12-07
     * Adds given extension to file, if no current extension
     * @param fileName File name to add extension to
     * @param extension Extension to add
     * @return fileName.extension
     */
    private String getFileNameWithExtension (String fileName, String extension){
    	int extIndex = fileName.lastIndexOf(".");
    	if (extIndex == -1){
    		fileName = fileName + "." + extension;
    	}
    	return fileName;
    }

    /*
     * Remove all tmp-files that is saved during modelling and optimization.
     */
    private void cleanTmpFiles()
    {
	if (c_tmpMps != null) {
	    c_tmpMps.delete();
	    c_tmpMps = null;
	}
	if (c_tmpOptimized != null) {
	    c_tmpOptimized.delete();
	    c_tmpOptimized = null;
	}
	if (c_tmpInput != null) {
	    c_tmpInput.delete();
	    c_tmpInput = null;
	}
    }

    private File getModelsDir()
    {
        // File dir = new File(/*getClass().getResource(".").getFile() +*/ "models");
        String sDir = (c_modelsDir == null) ? "models" : c_modelsDir;
        File   dir  = new File(sDir);

        // if the directory doesn't exist, create it
        if (!dir.exists())
            dir.mkdir();

        //System.out.println("dir = " + dir.getPath());
        return dir;
    }

    private void inputToFile(BufferedInputStream is, File file)
	throws IOException
    {
	FileWriter fr = new FileWriter(file);
	int ch;

	while ((ch = is.read()) != -1)
	    fr.write(ch);
	fr.close();
    }

   private void setExportFilters(JFileChooser fileDialog)
    {
	SaveAsFileFilter filter;
	String extension;
	String description;

	Vector formats = c_fileInteraction.getAvailableExportFileFormats();
	if (formats != null)
	    for (int i = 0; i < formats.size(); i++) {
		extension = ((String[]) formats.elementAt(i))[0];
		description = ((String[]) formats.elementAt(i))[1];
		filter = new SaveAsFileFilter(extension, description);
		fileDialog.addChoosableFileFilter(filter);
	    }
    }

	/**
	 * Sets export filters for XML export.
	 * Added by PUM5 2007-11-12
	 * @param fileDialog the JFileChooser to filter
	 * */
   private void setExportXmlFilters(JFileChooser fileDialog)
   {
	SaveAsFileFilter filter;
	String extension;
	String description;

	Vector formats = c_fileInteraction.getAvailableExportXmlFileFormats();
	if (formats != null)
	    for (int i = 0; i < formats.size(); i++) {
		extension = ((String[]) formats.elementAt(i))[0];
		description = ((String[]) formats.elementAt(i))[1];
		filter = new SaveAsFileFilter(extension, description);
		fileDialog.addChoosableFileFilter(filter);
	    }
   }

    /*
     * Sets the filters for the file dialog. Gets them from FileInteraction.
     */
    private void setLoadFilters(JFileChooser fileDialog)
    {
	SaveAsFileFilter filter;
	String extension;
	String description;;

	Vector formats = c_fileInteraction.getAvailableLoadFileFormats();
	if (formats != null)
	    for (int i = 0; i < formats.size(); i++) {
		extension = ((String[]) formats.elementAt(i))[0];
		description = ((String[]) formats.elementAt(i))[1];
		filter = new SaveAsFileFilter(extension, description);
		fileDialog.addChoosableFileFilter(filter);
	    }
    }

    /*
     * Sets the filters for the file dialog. Gets them from FileInteraction.
     */
    private void setImportFilters(JFileChooser fileDialog)
    {
	SaveAsFileFilter filter;
	String extension;
	String description;;

	Vector formats = c_fileInteraction.getAvailableImportFileFormats();
	if (formats != null)
	    for (int i = 0; i < formats.size(); i++) {
		extension = ((String[]) formats.elementAt(i))[0];
		description = ((String[]) formats.elementAt(i))[1];
		filter = new SaveAsFileFilter(extension, description);
		fileDialog.addChoosableFileFilter(filter);
	    }
    }
    
    /*
     * Sets the filters for the file dialog. Gets them from FileInteraction.
     */
    private void setSaveFilters(JFileChooser fileDialog)
    {
	SaveAsFileFilter filter;
	String extension;
	String description;;

	Vector formats = c_fileInteraction.getAvailableSaveFileFormats();
	if (formats != null)
	    for (int i = 0; i < formats.size(); i++) {
		extension = ((String[]) formats.elementAt(i))[0];
		description = ((String[]) formats.elementAt(i))[1];
		filter = new SaveAsFileFilter(extension, description);
		fileDialog.addChoosableFileFilter(filter);
	    }
    }

    public String toXML(int indent)
    {
	String zoom = ((String) c_app.c_topToolbar.c_combo.getSelectedItem()).replaceFirst(" %", "");
	String xml = XML.indent(indent) + "<guiprefs>" + XML.nl() + XML.indent(indent+1) + "<zoom>" +
	    zoom + "</zoom>" +
	    XML.nl() + XML.indent(indent) + "</guiprefs>" + XML.nl();
	return xml;
    }

    static public void setStaticToolbar(TopToolbar ttb)
    {
	s_topToolbar = ttb;
    }

    static public TopToolbar getStaticToolbar()
    {
	return s_topToolbar;
    }

    /* PUM16 2004 */
    /*public
      savedModel*/

    public void
	optimizeWithSettings(String filename, String optimizer,
			     String optimizerPath,
			     String mpsFile, String optFile)
    {
	OptimizationResult result = null;
	boolean optimizationSuccessful = true;
      
	try {
             // JOptionPane.showMessageDialog(null,"Pleas wait... Optimizing");
	    result = c_eventHandler.optimizeWithSettings(filename, optimizer,
						optimizerPath,mpsFile, optFile);
              
            } 
        catch(IllegalArgumentException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    optimizationSuccessful = false;
	} catch(FileInteractionException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    optimizationSuccessful = false;
	} catch(OptimizationException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    optimizationSuccessful = false;
	} catch(FileNotFoundException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    optimizationSuccessful = false;
	} catch(IOException e) {
	    JOptionPane.showMessageDialog(null, e.getMessage());
	    optimizationSuccessful = false;
	}


	if(!optimizationSuccessful)
	    return;

	boolean showOptimizerOutput = false;
	String showOptOut =
	    c_userSettings.getProperty(SHOW_OPT_OUT);
	if (showOptOut != null) {
	    if (showOptOut.equals("1")) {
		showOptimizerOutput = true;
	    }
	}

	if(result == null || showOptimizerOutput) {
	    /* OptimizeController encountered some error while running,
	     * display optimization output */
	    OptimizerOutputDialog dialog =
		OptimizerOutputDialog.createInstance(c_app, true);
	    dialog.pack();

	    dialog.setOptimizerOutput(c_eventHandler.getOptimizerOutput());

	    // put the frame in the middle of the parent frame
	    int height = c_app.getLocation().y + c_app.getSize().height/2;
	    int width = c_app.getLocation().x + c_app.getSize().width/2;
	    int x = (int) (width - dialog.getSize().width/2);
	    int y = (int) (height - dialog.getSize().height/2);
	    dialog.setLocation(x, y);

	    dialog.show();
	}

	if(result != null)
	{
	    OutputFlowChooserDialog dialog =
		OutputFlowChooserDialog.createInstance(c_app, true,
						       c_userSettings);
	    dialog.pack();

	    dialog.loadFlows(result);

	    // put the frame in the middle of the parent frame
	    int height = c_app.getLocation().y + c_app.getSize().height/2;
	    int width = c_app.getLocation().x + c_app.getSize().width/2;
	    int x = (int) (width - dialog.getSize().width/2);
	    int y = (int) (height - dialog.getSize().height/2);
	    dialog.setLocation(x, y);

	    dialog.show();

	}
    }

    public void output(String filename, OptimizationResult result)
    {
	try {
	    c_eventHandler.output(filename, result);
	} catch(IOException e) {
	    JOptionPane.showMessageDialog(null, "Unable to write file. "
					  + e.getMessage());
	} catch(IllegalArgumentException e) {
	    JOptionPane.showMessageDialog(null, "Illegal argument. "
					  + e.getMessage());
	}
    }

    /**
	 * Return a Vector of labels.
	 * Caller: source-dialog object
	 * @return Vector of labels
	 */
	public Vector getObjectFunctionLabels() {
		return c_eventHandler.getObjectFunctionLabels();
	}

	/**
	 * A callback that is triggered when user wants the "ObjectFunctions" dialog
	 */
	public void objectfunctionAction() {
		JDialog dialog = new ObjectFunctionDialog(c_app, true, this,
				c_eventHandler.getObjectFunction());

		dialog.pack();

		// put the frame in the middle of the parent frame
		int height = c_app.getLocation().y + c_app.getSize().height / 2;
		int width = c_app.getLocation().x + c_app.getSize().width / 2;
		int x = (int) (width - dialog.getSize().width / 2);
		int y = (int) (height - dialog.getSize().height / 2);
		dialog.setLocation(x, y);

		dialog.show();

		// assuming something was changed with the resources
		//setChanged(false);//I would not assume that. Urban Liljedahl
	}
	public MainDialog getMainDialog() {
		return c_app;
	}
	/*Added by PUM5 2007-12-03*/
	/*
	 * Added by PUM5 2007-12-02
	 * For testing purposes.
	 * If automatic testing isn't used these method can be removed
	 */
	 /* 
	 * Description: setTestVar is used by automatic test cases to enable unit testing.
	 */
	public boolean setTestVar(){
		if (!testVar){
			testVar=true;
			return false;
			}
		else
			return true;	
	}
    // Added by Nawzad Mardan 090319

   /*
	 * Get User setting
	 */
	public Ini getUserSetting()
    {
		return c_userSettings;
	}


	/*
	 * Get current model
	 */
	public Model getModel() {
		return c_eventHandler.getModel();
	}
	/*
	 * Pum5 2007: End methods for testing purposes
	 */

	//End PUM5
        
        //Added by Nawzad Mardan 2008-02-10
  /**
   * method is called from the constructor method to
     * to show information about software reMIND.
     * 
     */
    private void initInformationComponent () 
    {
       
        /*JLabel label;
        JButton button;
        JPanel upperpanel,lowerpanel,mainpanel;
        frame = new JFrame("reMIND");
        label = new JLabel("Information: Please wait... Creating MPS File");
        button = new JButton("Cancel");
        upperpanel = new JPanel();
        lowerpanel = new JPanel();
        mainpanel = new JPanel();
        upperpanel.add(label);
        lowerpanel.add(button);
        mainpanel.add(upperpanel);
        mainpanel.add(lowerpanel);
        frame.getContentPane().add(mainpanel);
        frame.addWindowListener(new WindowAdapter() {
     public void windowClosing(WindowEvent evt) 
        {
        int  select = JOptionPane.showConfirmDialog(frame, exitMessage,"Confirm dialog",JOptionPane.YES_NO_OPTION);
        if (select == JOptionPane.YES_OPTION)
            {
            frame.setVisible(false);
            frame.dispose();
             System.exit(0);
            }
        }  });  

        button.addActionListener(new ActionListener() 
        {public void actionPerformed(ActionEvent e)
         {
             
           int  selection = JOptionPane.showConfirmDialog(frame, exitMessage,"Confirm dialog",JOptionPane.YES_NO_OPTION);
        if (selection == JOptionPane.YES_OPTION)
            {
               
            frame.setVisible(false);
            frame.dispose();
            System.exit(0);
            //throw new FileInteractionException("ddad");
            }
        }
        } );
        frame.setSize(350,120);
       
        Dimension d = frame.getToolkit().getScreenSize();
        Rectangle b = frame.getBounds();
        frame.setLocation((d.width-b.width)/2, (d.height-b.height)/2);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
         
        frame.setVisible(true);*/
    /*    import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.event.*;*/

/**
 * Help window for Function Editor
 * @author Nils-Oskar Spett och Richard Harju
 * @author Jonas Sääv
 * @version 2004-03-04
 */
/*
public class AboutDialog extends JDialog implements HyperlinkListener{
    /**
     * Constructor
     * Creates an about dialog
     * and loads the file about.htm into it
     */
    /*public AboutDialog(java.awt.Frame parent, boolean modal) {
      super(parent, modal);*/
      dialog = new JDialog();
      dialog.setTitle("reMIND - version " + mind.GlobalStringConstants.reMIND_version);
      dialog.setSize(450, 520);
      dialog.setResizable(true);
      
      JButton button = new JButton("OK");
      button.setMnemonic(KeyEvent.VK_O);
      button.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          /*try{
            s.join(10);
          }
          catch(Exception m) {
                System.out.println(" "+ m);
            }
         if(!s.isInterrupted())
                s.interrupt();
            /*   if(s.isAlive())
           // if(s!=null)
            try{
            s.interrupt();
            }
            catch (Exception m) {
                System.out.println(" "+ m);
            }
            
            if(!s.isInterrupted())
                s.interrupt(); 
            if(!s.isInterrupted())
                s.interrupt(); 
            if(!s.isInterrupted())
                s.interrupt();
             if(!s.isInterrupted())
                s.interrupt(); 
            if(!s.isInterrupted())
                s.interrupt(); 
            if(!s.isInterrupted())
                s.interrupt();*/ 
           
          dialog.setVisible(false);
          dialog.dispose();
        }
      });

        JEditorPane aboutText = null;
        try {
          aboutText = new JEditorPane(getClass().getResource("about.htm"));
          //aboutText.setBackground(new Color(237, 237, 237));
          aboutText.setMargin(new Insets(15, 15, 15, 15));
          aboutText.setEditable(false);
         // aboutText.addHyperlinkListener(this);
        }

        catch (IOException e) {
          System.out.println(e.getMessage());
          dialog.dispose();
        }

        JPanel buttonp = new JPanel();
        buttonp.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        buttonp.setLayout(new FlowLayout());
        buttonp.add(button);

        JScrollPane textp = new JScrollPane(aboutText);
        textp.setBackground(Color.WHITE);

        dialog.getContentPane().add(textp,BorderLayout.CENTER);
        dialog.getContentPane().add(buttonp,BorderLayout.SOUTH);
        Dimension d = dialog.getToolkit().getScreenSize();
        Rectangle b = dialog.getBounds();
        dialog.setLocation((d.width-b.width)/2, (d.height-b.height)/2);
        //frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setVisible(true);

    /*}

    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            JEditorPane pane = (JEditorPane) e.getSource();
            try {
                   //   pane.setPage(e.getURL());
                   Runtime rt = Runtime.getRuntime();
                   rt.exec("rundll32 url.dll, FileProtocolHandler " + e.getURL().toString());
            } catch (Throwable t) {
                      t.printStackTrace();
                  }
                }
        }
}*/


  
    }
    
    // Added by Nawzad Mardan 2008-02-10
   /**
   * method is called from the constructor method to
     * to wait about 18 second and show information about software reMIND.
     * 
     */
    
    
     private void waiting()
    {
     
        try{
           //s = new Thread("Info");
           //s.sleep(16000);
         Thread.sleep(6000);
        }
        catch(Exception e)
        {
            System.out.print(e.getMessage());
        }
        //Destroy the resources for the farme, and return the memory to the OS, and marked as undisplayable
        dialog.dispose();
    }
}
