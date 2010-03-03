/*
 * Copyright 2010:

 * Nawzad Mardan <nawzad.mardan.liu.se>
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
 * The dialog for changing a resource name
 * @author Nawzad Mardan
 * @version 2010-03-02
 */

package mind.gui.dialog;
import mind.model.ResourceName;

public class EditResourceDialog extends javax.swing.JDialog {

private javax.swing.JButton btnOK;
private javax.swing.JButton btnCancel;
private javax.swing.JPanel pnlName;
private javax.swing.JLabel lblName;
private javax.swing.JTextField txtName;
private javax.swing.JPanel pnlButtons;
private ResourceName c_resourceName;

public EditResourceDialog  (javax.swing.JDialog parent, boolean modal, ResourceName rn)
{
super (parent, modal);
this.setTitle("Change Resource Name ");
c_resourceName = rn;
initComponents ();
pack ();
}

private void initComponents()
 {
 pnlName = new javax.swing.JPanel();
 lblName = new javax.swing.JLabel();
 txtName = new javax.swing.JTextField(10);
 btnOK = new javax.swing.JButton();
 btnCancel = new javax.swing.JButton();
 pnlButtons = new javax.swing.JPanel();

 getContentPane().setLayout (new java.awt.GridBagLayout());
 java.awt.GridBagConstraints gridBagConstraints;
 addWindowListener(new java.awt.event.WindowAdapter() {
		public void windowClosing(java.awt.event.WindowEvent evt) {
		    closeDialog(evt);
		}
	    });
 lblName.setText("Resource Name: ");
 pnlName.add(lblName);
 txtName.setText(c_resourceName.getResourceName());
 pnlName.add(txtName);

 gridBagConstraints = new java.awt.GridBagConstraints();
 gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
 gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
 gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
 gridBagConstraints.weightx = 1.0;
 getContentPane().add(pnlName, gridBagConstraints);
 //OK button
 btnOK.setText("OK");
 getRootPane().setDefaultButton(btnOK);
 btnOK.addActionListener(new java.awt.event.ActionListener() {
 public void actionPerformed(java.awt.event.ActionEvent evt) {
		    btnOKEditPerformed(evt);
		}
	    });

 pnlButtons.add (btnOK);

 //Cancel button
 btnCancel.setText("Cancel");
 btnCancel.addActionListener(new java.awt.event.ActionListener() {
 public void actionPerformed(java.awt.event.ActionEvent evt) {
 btnCancelEditPerformed(evt);}});
 pnlButtons.add (btnCancel);
 //Ok and Cancel buttons pandel
 gridBagConstraints = new java.awt.GridBagConstraints();
 gridBagConstraints.gridx = 0;
 gridBagConstraints.gridy = 2;
 gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
 gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
 gridBagConstraints.weightx = 1.0;
 getContentPane().add(pnlButtons, gridBagConstraints);

 }
private void btnCancelEditPerformed (java.awt.event.ActionEvent evt)
    {
	closeDialog(null);
    }

private void btnOKEditPerformed (java.awt.event.ActionEvent evt)
 {
  if(!txtName.getText().equals(""))
     c_resourceName.setNewResourceName(txtName.getText());
  closeDialog(null);
 }

private void closeDialog(java.awt.event.WindowEvent evt)
 {
 setVisible(false);
 dispose();
 }

}
