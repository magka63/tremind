/*
 * Copyright 2004:
 * Johan Bengtgsson <johbe496@student.liu.se>
 * Daniel Campos <danca226@student.liu.se>
 * Martin Fagerfj?ll <marfa233@student.liu.se>
 * Daniel Ferm <danfe666@student.liu.se>
 * Able Mahari <ablma616@student.liu.se>
 * Andreas Remar <andre063@student.liu.se>
 * Haider Shareef <haish292@student.liu.se>
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
 * This class is a dialog for displaying Optimizer's console output.
 * It is a Singleton, which means only one instance of it can be
 * created.
 *
 * @author Johan Bengtsson, Daniel Ferm
 * @version 2004-11-25
 */
package mind.gui.dialog;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

import mind.gui.*;
import mind.io.Ini;

import mind.automate.Flow;
import mind.automate.OptimizationResult;
//import mind.model.*;
//import mind.io.*;

public class OutputFlowChooserDialog
    extends JDialog
    implements UserSettingConstants
{
    private static OutputFlowChooserDialog c_instance = null;
    private Ini c_userSettings;
    private GUI gui;
    // Added by Nawzad Mardan 090319
    private boolean c_model;
    private Frame  c_frame;//END
    //private javax.swing.JTabbedPane c_tabbedPane;

    /** Creates new form OutputFlowChooserDialog */
    private OutputFlowChooserDialog(Frame parent, boolean modal,
				    Ini userSettings)
    {
	super (parent, modal);
	c_userSettings = userSettings;
	gui = GUI.getInstance();
    c_model =modal;
    c_frame = parent;
	initComponents();
	pack();
    }

    public static synchronized OutputFlowChooserDialog
	createInstance(Frame parent, boolean modal, Ini userSettings)
    {
	if (c_instance == null)
	    c_instance =
		new OutputFlowChooserDialog(parent, modal, userSettings);

	String filename = c_instance.gui.c_savedModel;
	if (filename == null)
	    filename = "Untitled";
	filename = c_instance.gui.getFilenameWithoutExtension(filename);
	String withDate =
	    c_instance.c_userSettings.getProperty(DATE_IN_OPT_FILE);
	if (withDate != null) {
	    if (withDate.equals("1")) {
		filename = c_instance.gui.getFilenameWithDate(filename);
	    }
	}

	filename = filename + ".txt";

	c_instance.textFilename.setText(filename);

	return c_instance;
    }

    /* method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents () {
	leftPanel = new JPanel();
	rightPanel = new JPanel();
	centerPanel = new JPanel();
	filePanel = new JPanel();
	bottomPanel = new JPanel();
	unselectedScrollPane = new JScrollPane();
	selectedScrollPane = new JScrollPane();

	leftPanel.setLayout(new GridBagLayout());
	rightPanel.setLayout(new GridBagLayout());
	centerPanel.setLayout(new GridBagLayout());
	filePanel.setLayout(new FlowLayout (FlowLayout.LEFT, 10, 5));
	bottomPanel.setLayout (new FlowLayout (FlowLayout.RIGHT, 5, 5));

	unselectedData = new DataListFlows();
	unselectedList = new JList(unselectedData);
	unselectedList.setAutoscrolls(true);
	unselectedList.setMinimumSize (new java.awt.Dimension(30, 0));

	selectedData = new DataListFlows();
	selectedList = new JList(selectedData);
	selectedList.setAutoscrolls(true);
	selectedList.setMinimumSize (new java.awt.Dimension(30, 0));

	lblFilename = new JLabel("Result filename");
	lblSelected = new JLabel("Selected");
	lblUnselected = new JLabel("Unselected");

	textFilename = new JTextField(25);

	unselectOne = new javax.swing.JButton("<");
	unselectOne.setPreferredSize(new Dimension(49,20));
	unselectAll = new javax.swing.JButton("<<");
	unselectAll.setPreferredSize(new Dimension(49,20));
	selectOne = new javax.swing.JButton(">");
	selectOne.setPreferredSize(new Dimension(49,20));
	selectAll = new javax.swing.JButton(">>");
	selectAll.setPreferredSize(new Dimension(49,20));
	btnOK = new javax.swing.JButton("OK");
	btnCancel = new javax.swing.JButton("Cancel");
    btnOpenWith = new javax.swing.JButton("Open With..");

	separator = new javax.swing.JSeparator();

	mainPanel = new JPanel();
	mainPanel.setLayout(new GridBagLayout ());
	GridBagConstraints constraints;
	addWindowListener (new WindowAdapter () {
	    public void windowClosing (WindowEvent evt) {
		closeDialog (null);
	    }
	});

	/* leftPanel  */
       	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 0;
	constraints.insets = new Insets(0, 0, 0, 0);
	mainPanel.add(leftPanel, constraints);

       	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 0;
	constraints.insets = new Insets(0, 10, 10, 10);
	leftPanel.add(lblUnselected, constraints);

	unselectedScrollPane.setPreferredSize(new java.awt.Dimension(150,
								     170));
	unselectedScrollPane.getViewport().setView(unselectedList);

      	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 1;
	constraints.insets = new Insets(0, 10, 10, 10);
	leftPanel.add(unselectedScrollPane, constraints);

	/* centerPanel  */
       	constraints = new GridBagConstraints();
	constraints.gridx = 1;
	constraints.gridy = 0;
	constraints.insets = new Insets(0, 10, 10, 10);
	mainPanel.add(centerPanel, constraints);

       	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 0;
	constraints.insets = new Insets(38, 10, 10, 10);
	centerPanel.add(selectOne, constraints);

       	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 1;
	constraints.insets = new Insets(0, 10, 38, 10);
	centerPanel.add(selectAll, constraints);

       	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 2;
	constraints.insets = new Insets(0, 10, 10, 10);
	centerPanel.add(unselectAll, constraints);

       	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 3;
	constraints.insets = new Insets(0, 10, 10, 10);
	centerPanel.add(unselectOne, constraints);

	selectOne.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    buttonSelectOnePressed(evt);
		}
	    });

	selectAll.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    buttonSelectAllPressed(evt);
		}
	    });

	unselectOne.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    buttonUnselectOnePressed(evt);
		}
	    });

	unselectAll.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
		    buttonUnselectAllPressed(evt);
		}
	    });

	/* rightPanel  */
       	constraints = new GridBagConstraints();
	constraints.gridx = 2;
	constraints.gridy = 0;
	constraints.insets = new Insets(10, 0, 10, 0);
	mainPanel.add(rightPanel, constraints);

       	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 0;
	constraints.insets = new Insets(0, 10, 10, 10);
	rightPanel.add(lblSelected, constraints);

	selectedScrollPane.setPreferredSize(new java.awt.Dimension(150, 170));
	selectedScrollPane.getViewport().setView(selectedList);

      	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 1;
	constraints.insets = new Insets(0, 10, 10, 10);
	rightPanel.add(selectedScrollPane, constraints);

	/* filePanel */
      	constraints = new GridBagConstraints();
	constraints.gridx = 0;
	constraints.gridy = 0;
	constraints.insets = new Insets(10, 10, 0, 0);
	filePanel.add(lblFilename, constraints);

      	constraints = new GridBagConstraints();
	constraints.gridx = 1;
	constraints.gridy = 0;
	constraints.insets = new Insets(3, 5, 0, 10);
	filePanel.add(textFilename, constraints);

	constraints = new GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridwidth = 3;
	constraints.fill = GridBagConstraints.BOTH;
	constraints.insets = new Insets (0, 0, 0, 0);
        mainPanel.add (filePanel, constraints);

	/* add separator */
	constraints = new GridBagConstraints();
	constraints.gridwidth = 3;
	constraints.gridy = 4;
	constraints.fill = GridBagConstraints.HORIZONTAL;
	constraints.insets = new Insets(40, 0, 0, 0);
	mainPanel.add(separator, constraints);

	/* bottomPanel */
	getRootPane().setDefaultButton(btnOK);

	btnCancel.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		buttonCancelPressed(evt);
	    }
	});

	btnOK.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		buttonOKPressed(evt);
	    }
	});
btnOpenWith.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		buttonOPenWithPressed(evt);
	    }
	});
	bottomPanel.add(btnOK);
	bottomPanel.add(btnCancel);
    bottomPanel.add(btnOpenWith);
	constraints = new GridBagConstraints ();
	constraints.gridx = 0;
	constraints.gridy = 5;
	constraints.gridwidth = 3;
	constraints.fill = GridBagConstraints.BOTH;
	constraints.insets = new Insets (10, 0, 5, 85);
        mainPanel.add (bottomPanel, constraints);

        getContentPane().add(mainPanel);

	/* Test code */
	/*int numFlows = (int)(Math.random() * 100) + 1;
	int numTidsSteg = (int)(Math.random() * 20) + 1;

	OptimizationResult optRes = new OptimizationResult();
	optRes.globalOptimum = Math.random() * 1000 - 500;

	for (int j=0; j<numFlows; j++) {
	    mind.model.ID id =
		new mind.model.ID(new Long((int)(Math.random()*124)),
				  mind.model.ID.FLOW);
	    double[] values = new double[numTidsSteg];
	    for (int i=0; i<values.length; i++)
		values[i] = Math.random() * 1000 - 500;

	    optRes.addFlow(id, values);
	}

	loadFlows(optRes);*/
    }

    private void buttonSelectOnePressed(ActionEvent evt) {
	int[] indices = unselectedList.getSelectedIndices();
	Flow[] flows = unselectedData.remove(indices);

	for (int i=0; i<flows.length; i++) {
	    flows[i].selected = true;
	    selectedData.add(flows[i]);
	}

	selectedList.clearSelection();
	unselectedList.clearSelection();
    }

    private void buttonSelectAllPressed(ActionEvent evt) {
	int[] indices = new int[unselectedData.getSize()];
	for (int i=0; i<indices.length; i++)
	    indices[i] = i;

	Flow[] flows = unselectedData.remove(indices);
	for (int i=0; i<flows.length; i++) {
	    flows[i].selected = true;
	    selectedData.add(flows[i]);
	}

	selectedList.clearSelection();
	unselectedList.clearSelection();
    }

    private void buttonUnselectOnePressed(ActionEvent evt) {
	int[] indices = selectedList.getSelectedIndices();
	Flow[] flows = selectedData.remove(indices);

	for (int i=0; i<flows.length; i++) {
	    flows[i].selected = false;
	    unselectedData.add(flows[i]);
	}

	selectedList.clearSelection();
	unselectedList.clearSelection();
    }

    private void buttonUnselectAllPressed(ActionEvent evt) {
	int[] indices = new int[selectedData.getSize()];
	for (int i=0; i<indices.length; i++)
	    indices[i] = i;

	Flow[] flows = selectedData.remove(indices);
	for (int i=0; i<flows.length; i++) {
	    flows[i].selected = false;
	    unselectedData.add(flows[i]);
	}

	selectedList.clearSelection();
	unselectedList.clearSelection();
    }

    private void buttonCancelPressed(ActionEvent evt) {
	closeDialog(null);
    }

    private void buttonOKPressed(ActionEvent evt) {
	gui.output(textFilename.getText(), optres);
	closeDialog(null);
    }
    // Added by Nawzad Mardan 090319
    private void buttonOPenWithPressed(ActionEvent evt)
    {
     boolean fileOpen = false;
    try {
        gui.getEventHandlerClient().output(textFilename.getText(), optres);
        }
    catch(Exception e)
        {
        fileOpen = true;
	    JOptionPane.showMessageDialog(null, "The file :"+textFilename.getText()+" is already open."+"\nUnable to write file." +
                " Close the file first ");
        }
    if(!fileOpen)
        {
        closeDialog(null);
        JDialog dialog ;
        dialog = new OpenWithDialog(c_frame,c_model, c_userSettings, textFilename.getText());
        if (dialog != null)
            {
            int height = getLocation().y + getSize().height/2;
            int width = getLocation().x + getSize().width/2;
            int x = (int) (width - dialog.getSize().width/2);
            int y = (int) (height - dialog.getSize().height/2);
            dialog.setLocation(x, y);
                //System.out.println(functionType);

            dialog.setVisible(true);
            }
        }
    }
    /** Closes the dialog */
    private void closeDialog(WindowEvent evt) {
	setVisible (false);
	dispose ();
    }

    // Variables declaration
    private JPanel mainPanel;
    private JScrollPane unselectedScrollPane, selectedScrollPane;
    private JPanel leftPanel, rightPanel, centerPanel, filePanel, bottomPanel;
    private JTextField textFilename;
    private JLabel lblUnselected, lblSelected, lblFilename;
    private JList unselectedList, selectedList;
    private JButton selectOne, selectAll, unselectOne, unselectAll;
    private JButton btnCancel;
    private JButton btnOK;
    private JButton btnOpenWith;
    private DataListFlows selectedData, unselectedData;
    private JSeparator separator;

    private OptimizationResult optres;
    // End of variables declaration

    public void loadFlows(OptimizationResult or) {
	optres = or;

	selectedData.clear();
	unselectedData.clear();

	ListIterator it = optres.getFlows();

	while (it.hasNext()) {
	    Flow flow = (Flow)it.next();

	    if (flow.selected)
		selectedData.add(flow);
	    else
		unselectedData.add(flow);
	}
    }

    class DataListFlows implements ListModel {
	private Hashtable c_listeners;
	public LinkedList c_data;

	public DataListFlows() {
	    c_listeners = new Hashtable();
	    c_data = new LinkedList();
	}

	public Object getElementAt(int index) {
	    return ((Flow)c_data.get(index)).toString();
	}

	public int getSize() {
	    return c_data.size();
	}

	public void addListDataListener(ListDataListener l) {
	    c_listeners.put(l, l);
	}

	public void removeListDataListener(ListDataListener l) {
	    c_listeners.remove(l);
	}

	public void clear() {
	    /* Remove all elements */
	    c_data.clear();

	    ListDataEvent e =
		new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, 0);

	    /* Notify all listeners of change */
	    Enumeration keys = c_listeners.keys();
	    while (keys.hasMoreElements())
		((ListDataListener)keys.nextElement()).contentsChanged(e);
	}

	public void add(Flow element) {
	    int index = 0;
	    boolean added = false;

	    /* Insert new element sorted into the LinkedList */
	    ListIterator it = c_data.listIterator(0);
	    while (it.hasNext()) {
		index++;
		Flow flow = (Flow)it.next();
		if (element.id.compareTo(flow.id) <= 0) {
		    it.previous();
		    it.add(element);
		    added = true;
		    index--;
		    break;
		}
	    }

	    if (!added)
		c_data.addLast(element);

	    /* Notify all listeners of change */
	    ListDataEvent e =
		new ListDataEvent(this,
				  ListDataEvent.INTERVAL_ADDED,
				  index,
				  index);

	    Enumeration keys = c_listeners.keys();
	    while (keys.hasMoreElements())
		((ListDataListener)keys.nextElement()).intervalAdded(e);
	}

	public Flow[] remove(int[] indices) {
	    ListDataEvent e =
		new ListDataEvent(this,
				  ListDataEvent.CONTENTS_CHANGED,
				  0,
				  c_data.size()-1);

	    Flow[] elements = new Flow[indices.length];

	    /* Sort indices so a backwards removal is possible */
	    Arrays.sort(indices);

	    for (int i=indices.length-1; i>=0; i--)
		elements[i] = (Flow)c_data.remove(indices[i]);

	    /* Notify all listeners of change */
	    Enumeration keys = c_listeners.keys();
	    while (keys.hasMoreElements())
		((ListDataListener)keys.nextElement()).contentsChanged(e);

	    return elements;
	}
    }
}
