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
 * @author Johan Bengtsson
 * @version 2004-11-23
 */
package mind.gui.dialog;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class OptimizerOutputDialog
    extends JDialog
	    //implements UserSettingConstants
{
    private static OptimizerOutputDialog c_instance = null;
    //private Ini c_userSettings;
    //private javax.swing.JTabbedPane c_tabbedPane;

    /** Creates new form OptimizerOutputDialog */
    private OptimizerOutputDialog(Frame parent, boolean modal)
    {
	super (parent, modal);
	//c_userSettings = userSettings;
	initComponents();
	pack();
    }

    public static synchronized OptimizerOutputDialog
	createInstance(Frame parent, boolean modal)
    {
	if (c_instance == null)
	    c_instance = new OptimizerOutputDialog(parent, modal);

	return c_instance;
    }

    /* method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents () {
        setTitle("Optimizer Output");
        JPanel mainPanel = new JPanel();
	btnClose = new javax.swing.JButton ();
	mainPanel.setLayout (new GridBagLayout ());
	GridBagConstraints constraints;
	addWindowListener (new WindowAdapter () {
	    public void windowClosing (WindowEvent evt) {
		closeDialog (null);
	    }
	});

	/*Create the text area and put it in a scrollpane*/
	txtArea = new JTextArea(30, 80);
	txtArea.setLineWrap(true);
	txtArea.setEditable(false);
	txtArea.setFont(new Font("Monospaced", Font.PLAIN, 12) );
	scrollPane = new JScrollPane(txtArea);
	scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	constraints = new GridBagConstraints();
	constraints.gridx = 1;
	constraints.gridy = 0;
	constraints.insets = new Insets(10, 10, 10, 10);
	mainPanel.add(scrollPane, constraints);

	/*Create a close button and an action listener for it*/
	btnClose.setText ("Close");
	getRootPane().setDefaultButton(btnClose);
	btnClose.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		buttonClosePressed(evt);
	    }
	});

       	constraints = new GridBagConstraints();
	constraints.gridx = 1;
	constraints.gridy = 1;
	constraints.insets = new Insets(0, 10, 10, 10);
	mainPanel.add(btnClose, constraints);

        getContentPane().add(mainPanel);
    }

    private void buttonClosePressed(ActionEvent evt)
    {
	closeDialog(null);
    }

    /** Closes the dialog */
    private void closeDialog(WindowEvent evt) {
	setVisible (false);
	dispose ();
    }


    /**
     * Function for setting the text shown in the text area.
     *
     * @author Johan Bengtsson
     */
    public void setOptimizerOutput(String txt)
    {
	txtArea.setText(txt);
    }

    // Variables declaration
    private JTextArea txtArea;
    private JScrollPane scrollPane;
    private JButton btnClose;
    // End of variables declaration
}
