/*
 * Copyright 2004:
 * Marcus Bergendorff <amaebe-1@student.luth.se>
 * Jan Sköllermark <jansok-1@student.luth.se>
 * Nils-Oskar Spett <nilspe-1@student.luth.se>
 * Richard Harju <richar-1@student.luth.se>
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

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.*;

import mind.model.*;
import mind.gui.*;

/**
 * Creates a Box with Buttons representing avialble Flows 
 * @version 2004-03-04
 * @author Marcus Bergendorff
 */

class FlowBox extends Box{
	private JTextField c_editor;
	private JTextField c_exprTxt;
	private FunctionEditorDialog c_parent;
	
	/**
	 * Constructor creates a new Flow Box with buttons repressenting flows
	 * @param flows The flows to display as buttons
	 * @param table The parent Window's table
	 * @param editor The editor component 
	 */
	
	FlowBox (Flow [] flows, FunctionEditorDialog parent, JTextField editor, GUI gui, JTextField exprTxt) {
		super(BoxLayout.Y_AXIS);
		setAlignmentY(0);
		
		if(!(flows == null)) {
			setBorder(BorderFactory.createCompoundBorder
					(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
					BorderFactory.createEmptyBorder(4, 4, 4, 4)));
			add(Box.createVerticalStrut(5));
			c_editor = editor;
			c_exprTxt = exprTxt;
			c_parent = parent;		
				
			for (int i = 0; i < flows.length ; i++ ) {
				final String flowstr = flows[i].toString();
				JButton btn = new JButton(flowstr);
				ID resID = flows[i].getResource();
				if(	resID != null) 
					btn.setForeground((Color) gui.getResource(resID).getColor());
								
				btn.addActionListener(new ActionListener(){					
				/* adds the flow´s ID and "#" to the cell */	
	      			public void actionPerformed (ActionEvent e) {
	      				if (c_parent.isEditedLast()) {
		      				String str = c_editor.getText();
		      				int pos = c_editor.getCaretPosition();
		      				str = insert(flowstr, str, pos);
		      				c_editor.setText(str);
		      				c_exprTxt.setText(str);
		      				c_editor.requestFocus();
		      				c_editor.setCaretPosition(pos + flowstr.length());
						} else {
							String str = c_exprTxt.getText();
							if(str == null) str ="";
							int pos = c_exprTxt.getCaretPosition();
	      					str = insert(flowstr, str, pos);
							c_exprTxt.setText(str);
							c_editor.setText(str);
							c_exprTxt.requestFocus();
							c_exprTxt.setCaretPosition(pos + flowstr.length());    							
						}
	      			} 
	      		});
	      		
	      		btn.setMargin(new Insets(1,1,1,1));
	      		add(btn);
			} 
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