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
 * @author Jonas Sääv
 * @version 2004-03-04
 */

public class AboutDialog extends JDialog implements HyperlinkListener{
    /**
     * Constructor
     * Creates an about dialog
     * and loads the file about.htm into it
     */
    public AboutDialog(java.awt.Frame parent, boolean modal) {
      super(parent, modal);
      setTitle("About reMIND - version " + mind.GlobalStringConstants.reMIND_version);
      setSize(450, 450);
      setResizable(true);

      JButton button = new JButton("OK");
      button.setMnemonic(KeyEvent.VK_O);
      button.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          setVisible(false);
          dispose();
        }
      });

        JEditorPane aboutText = null;
        try {
          aboutText = new JEditorPane(getClass().getResource("about.htm"));
          //aboutText.setBackground(new Color(237, 237, 237));
          aboutText.setMargin(new Insets(15, 15, 15, 15));
          aboutText.setEditable(false);
          aboutText.addHyperlinkListener(this);
        }

        catch (IOException e) {
          System.out.println(e.getMessage());
          dispose();
        }

        JPanel buttonp = new JPanel();
        buttonp.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        buttonp.setLayout(new FlowLayout());
        buttonp.add(button);

        JScrollPane textp = new JScrollPane(aboutText);
        textp.setBackground(Color.WHITE);

        getContentPane().add(textp,BorderLayout.CENTER);
        getContentPane().add(buttonp,BorderLayout.SOUTH);

    }

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
}

