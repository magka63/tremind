/*
 * Copyright 2004:
 * Marcus Bergendorff <amaebe-1@student.luth.se>
 * Jan Sköllermark <jansok-1@student.luth.se>
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

package mind.gui.dialog;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A simple graphical component used by Function Editor
 * 
 * @author Marcus Bergendorff
 * @author Jan Sköllermark
 * 
 */

public class FloatVarCombo extends JPanel implements ActionListener{
	
	private FunctionEditorDialog c_parent;
	private JTextField c_editor;
	private JTextField c_exprTxt;
	private JComboBox combo;
	private JButton addBtn;
	private JButton removeBtn;
	
	public FloatVarCombo(Vector v, FunctionEditorDialog parent, JTextField editor, JTextField exprTxt){
		super();
		c_parent = parent;
		c_editor = editor;
		c_exprTxt = exprTxt;
		
		combo = new JComboBox(v);
		
		addBtn = new JButton("+");
		addBtn.setMargin(new Insets(1,3,1,3));
		addBtn.setToolTipText("Add Float Variable");
		
		removeBtn = new JButton("-");
		removeBtn.setMargin(new Insets(1,3,1,3));
		removeBtn.setToolTipText("Remove Float Variable");
		
		add(combo);
		add(addBtn);
		add(removeBtn);
		setBorder(BorderFactory.createTitledBorder("Float Variables"));
		
		combo.addActionListener(this);
		addBtn.addActionListener(this);
		removeBtn.addActionListener(this);
	}
		
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(combo)) {
			int index = combo.getSelectedIndex();
			// add selected variable to cell...
			String binStr = (String) combo.getSelectedItem();
			if (binStr == null)
				binStr = "";
			if (c_parent.isEditedLast()) {
				String str = c_editor.getText();
				int pos = c_editor.getCaretPosition();
				str = insert(binStr, str, pos);
				c_editor.setText(str);
				c_exprTxt.setText(str);
				c_editor.requestFocus();
				c_editor.setCaretPosition(pos + binStr.length());
			} else {
				String str = c_exprTxt.getText();
				if (str == null)
					str = "";
				int pos = c_exprTxt.getCaretPosition();
				str = insert(binStr, str, pos);
				c_exprTxt.setText(str);
				c_editor.setText(str);
				c_exprTxt.requestFocus();
				c_exprTxt.setCaretPosition(pos + binStr.length());
			}
		}else if (e.getSource().equals(addBtn)){
			String str ="Flt" + String.valueOf(c_parent.getNextFloatVar());
			combo.addItem(str);
		}else if (e.getSource().equals(removeBtn)){ 
			int index = combo.getSelectedIndex();
			if(index != -1) combo.removeItemAt(index);
		}
	}
	
	/*
	 * Helper method that inserts a string into another at given position
	 */
	
	private String insert ( String in, String original, int position){
		String newString = original.substring(0,position) + in;
		newString += original.substring(position, original.length());
		return newString;
	}
}

