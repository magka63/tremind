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
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.event.*; 

/**
 * Help window for Function Editor
 * @author Nils-Oskar Spett och Richard Harju
 * @version 2004-03-04
 */

public class FunctionEditorHelpDialog extends JDialog implements HyperlinkListener{
	final String imagesDir = "images/";
 
	/**
     * Constructor
     * Creates a help-dialog
     * and loads the file sfunchelp.html into it
     */
	public FunctionEditorHelpDialog(JDialog jd) {
 		super(jd);
 		setTitle("Function Editor help");
 		setSize(350,450);
 		setResizable(true);
	
		JButton button = new JButton("OK");
        button.setMnemonic(KeyEvent.VK_O);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            dispose();
             }
        });
	
	
		JEditorPane helpText=null;
		try{
			helpText = new JEditorPane(getClass().getResource(imagesDir + "sfunchelp.html"));
			helpText.setBackground(new Color(237,237,237));
			helpText.setMargin(new Insets(15,15,15,15));
			helpText.setEditable(false);
			helpText.addHyperlinkListener(this);
		}
	
		catch (IOException e){
			System.out.println(e.getMessage());
			dispose();
		}
       
        JPanel buttonp = new JPanel();
	    buttonp.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        buttonp.setLayout(new FlowLayout());
        buttonp.add(button);
        
        JScrollPane textp = new JScrollPane(helpText);
        textp.setBackground(Color.WHITE);       
        
       	getContentPane().add(textp,BorderLayout.CENTER);
		getContentPane().add(buttonp,BorderLayout.SOUTH);
        setVisible(true);
       
        
    }
    
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
	    JEditorPane pane = (JEditorPane) e.getSource();  
	    try {
		      pane.setPage(e.getURL());
	    } catch (Throwable t) {
		      t.printStackTrace();
	          }
		}
	}
}