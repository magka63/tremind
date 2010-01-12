/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mind.gui.dialog;

/**
 *
 * @author nawma77
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import mind.gui.*;
import mind.io.*;
import java.io.File;
import java.io.IOException;

public class OpenWithDialog extends JDialog implements UserSettingConstants, mind.GlobalStringConstants
{
private Ini c_userSettings;
private JPanel optPanel;
private JComboBox optChooser;
private JButton btnOK;
private JButton btnCancel;
private JLabel lblopenWith;
private JLabel lblopenWithPath;
private JTextField txtOpenWithPath;
private JButton btnBrowse;
private String c_filename;
/** Creates a new instance of StartStopDialog */
public OpenWithDialog(){}

public OpenWithDialog(Frame parent, boolean modal,Ini userSettings, String filename)
    {
	super (parent, modal);
	c_userSettings = userSettings;
    c_filename = filename;
	initComponents();
    }

private void initComponents ()
{
setTitle("Open With Dialog");
GridBagLayout gridbag = new GridBagLayout();
getContentPane().setLayout(gridbag);
optPanel = new JPanel();
optPanel.setLayout(new GridBagLayout());

optChooser = new JComboBox();
optChooser.addItem(OPT_NONE);
optChooser.addItem(OPT_EXCEL);
optChooser.addItem(OPT_NOTEPAD);
optChooser.addItemListener(new ItemListener() {
		public void itemStateChanged(ItemEvent evt) {
		    optChooserChanged(evt);
		}
	    });

String openWith = c_userSettings.getProperty(OPENWITH);
if (openWith == null) {
	    optChooser.setSelectedItem(OPT_NONE);
	}
else
{
if (openWith.equals(OPT_EXCEL))
    optChooser.setSelectedItem(OPT_EXCEL);
else if (openWith.equals(OPT_NOTEPAD))
    optChooser.setSelectedItem(OPT_NOTEPAD);
else
	optChooser.setSelectedItem(OPT_NONE);
}

lblopenWith = new javax.swing.JLabel("Open With");
lblopenWith.setForeground(Color.BLUE);
lblopenWith.setFont(new Font("SansSerif", Font.BOLD, 14));
lblopenWithPath = new javax.swing.JLabel("The path");
lblopenWithPath.setForeground(Color.BLUE);
if(openWith != null)
    lblopenWithPath.setText(openWith+ " path");
lblopenWithPath.setFont(new Font("SansSerif", Font.BOLD, 14));
txtOpenWithPath = new javax.swing.JTextField(20);
String path = null;

if(openWith != null && openWith.equals(OPT_EXCEL)) 
	path = c_userSettings.getProperty(EXCEL_PATH);
	
if(openWith != null && openWith.equals(OPT_NOTEPAD)) 
    path = c_userSettings.getProperty(NOTEPAD_PATH);
        
if(path != null)
    {
    txtOpenWithPath.setText(path);
    txtOpenWithPath.setEditable(true);
    }
else
    txtOpenWithPath.setEditable(false);


GridBagConstraints constraints;
constraints = new GridBagConstraints ();
constraints.gridx = 0;
constraints.gridy = 0;
constraints.insets = new Insets (10, 10, 0, 0);
constraints.anchor = GridBagConstraints.NORTHWEST;
optPanel.add (lblopenWith, constraints);

/* add drop-menu */
constraints = new GridBagConstraints ();
constraints.gridx = 0;
constraints.gridy = 1;
constraints.insets = new Insets (10, 10, 0, 0);
constraints.anchor = GridBagConstraints.NORTHWEST;
optPanel.add (optChooser, constraints);

constraints = new GridBagConstraints ();
constraints.gridx = 0;
constraints.gridy = 2;
constraints.anchor = GridBagConstraints.NORTHWEST;
constraints.insets = new Insets (10, 10, 0, 0);
optPanel.add (lblopenWithPath, constraints);

constraints = new GridBagConstraints ();
constraints.gridx = 0;
constraints.gridy = 3;
constraints.anchor = GridBagConstraints.NORTHWEST;
constraints.insets = new Insets (10, 10, 0, 0);
optPanel.add (txtOpenWithPath, constraints);

/*add browse button*/
btnBrowse = new javax.swing.JButton();
btnBrowse.setText("Browse");
constraints = new GridBagConstraints ();
constraints.gridx = 1;
constraints.gridy = 3;
constraints.anchor = GridBagConstraints.NORTHWEST;
constraints.insets = new Insets (10, 10, 0, 104);
optPanel.add(btnBrowse, constraints);
btnBrowse.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent evt) {
JFileChooser openDialog = new JFileChooser();
openDialog.showOpenDialog(new JFrame());
File file = openDialog.getSelectedFile();
if(file != null)
    txtOpenWithPath.setText(file.getAbsolutePath());
}});
constraints = new java.awt.GridBagConstraints();
constraints.gridx = 0;
constraints.gridwidth = 0;
constraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
getContentPane().add(optPanel, constraints);

JSeparator lnsp = new javax.swing.JSeparator();
constraints = new java.awt.GridBagConstraints ();
constraints.gridx = 0;
constraints.gridwidth = 1;
constraints.insets = new java.awt.Insets(10, 0, 10, 0);
constraints = new GridBagConstraints();
constraints.gridwidth = 3;
constraints.gridy = 4;
constraints.fill = GridBagConstraints.HORIZONTAL;
constraints.insets = new Insets(40, 0, 0, 0);
getContentPane ().add(lnsp, constraints);
//new java.awt.Insets(T0p, Left, B, R)

JPanel okcancelPanel = new JPanel();
btnOK = new javax.swing.JButton("OK");
btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				btnOkActionPerformed();
			}
    });

btnCancel = new javax.swing.JButton("Cancel");
btnCancel.addActionListener(new java.awt.event.ActionListener() {
     public void actionPerformed(java.awt.event.ActionEvent evt) {
        closeDialog(null);
      }
    });

okcancelPanel.add(btnOK);
okcancelPanel.add(btnCancel);
//JSeparator sepln = new javax.swing.JSeparator();
GridBagConstraints gridBagConstra = new java.awt.GridBagConstraints();
gridBagConstra.gridx = 0;
gridBagConstra.gridwidth = 5;
//gridBagConstra.fill = java.awt.GridBagConstraints.HORIZONTAL;
constraints.insets = new java.awt.Insets(20, 5, 20, 5);
getContentPane().add(okcancelPanel, gridBagConstra);



addWindowListener(new java.awt.event.WindowAdapter() { public void windowClosing(java.awt.event.WindowEvent evt) {
                        closeDialog(evt);}});

setSize(500,700);

pack();
}// END INITCOMP

private void btnOkActionPerformed ()
{
String open = (String)optChooser.getSelectedItem();
	/* Save OPTIMIZER */
c_userSettings.setProperty(OPENWITH,open);

	/*Save optimizer PATH*/
if(open != null && open.equals(OPT_EXCEL))
    {
    c_userSettings.setProperty(EXCEL_PATH,txtOpenWithPath.getText());
    }
if(open != null && open.equals(OPT_NOTEPAD))
    {
    c_userSettings.setProperty(NOTEPAD_PATH,txtOpenWithPath.getText());
    }

if(!(txtOpenWithPath.getText().equals("")))
    {
    Process excel = null;
    Runtime runtime = Runtime.getRuntime();
    try
        {
        //String file = "C:\\test.txt";
        String [] cmdArray = {txtOpenWithPath.getText(),c_filename};
        excel = runtime.exec(cmdArray);
        }
     catch(IOException e)
        {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "The path of "+ open+ " program is not correct\n "+
                   "Cannot run program "+ txtOpenWithPath.getText()+"\n" +
                   "The system cannot find the path specified", "File not Found",JOptionPane.WARNING_MESSAGE);
            return;
        }
    }
closeDialog(null);
}

private void closeDialog(java.awt.event.WindowEvent evt)
 {
  setVisible (false);
  dispose ();
}

private void optChooserChanged(ItemEvent evt)
{
if (evt.getStateChange() == ItemEvent.SELECTED)
    {
	if(txtOpenWithPath == null)
        return;

	String openWith = (String)optChooser.getSelectedItem();
	if(openWith == null)
        return;

    if(openWith.equals(OPT_NONE)) {
        txtOpenWithPath.setText("");
		txtOpenWithPath.setEditable(false);
        lblopenWithPath.setText("The path");
	    }
    else if(openWith.equals(OPT_EXCEL)) {
		txtOpenWithPath.setText(c_userSettings.getProperty(EXCEL_PATH));
		txtOpenWithPath.setEditable(true);
        lblopenWithPath.setText(openWith+ " path");
            }
    else if(openWith.equals(OPT_NOTEPAD)) {
                txtOpenWithPath.setText(c_userSettings.getProperty(NOTEPAD_PATH));
                txtOpenWithPath.setEditable(true);
                lblopenWithPath.setText(openWith+ " path");
              }

	}
}


}
