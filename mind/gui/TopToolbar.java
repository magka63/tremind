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

import javax.swing.*;

/**
 * Creates the toolbar
 *
 * @author Tim Terlegård
 * @version 2001-07-24
 */
public class TopToolbar
    extends JToolBar
{
  public JButton       c_SaveButton = null;
  public JButton       c_ExportButton = null;
  public JButton       c_OptimizeButton = null;
  public JButton       c_OptimizeWithSettingsButton = null;
  public JToggleButton c_NodeLabelsButton = null;
  public JToggleButton c_NodeIDsButton = null;
  public JToggleButton c_FlowLabelsButton = null;
  public JToggleButton c_FlowIDsButton = null;
  public JComboBox c_combo = null;
    /**
     * Constructor, associates all buttons and their tooltips
     *
     * @param actions All the defined actions
     */
    public TopToolbar(GlobalActions actions)
    {
	setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	// add buttons to the toolbar
	newButton(actions.c_newScenario, "", "New model (Ctrl-N)");
	newButton(actions.c_openScenario, "", "Open model (Ctrl-O)");

        // create save button
        c_SaveButton = add(actions.c_saveScenario);
        c_SaveButton.setText(""); //an icon-only button
        c_SaveButton.setToolTipText("Save model (Ctrl-S)");

        // create export button
        c_ExportButton = add(actions.c_export);
        c_ExportButton.setText(""); // an icon-only button
        c_ExportButton.setToolTipText("Export to MPS-file (Ctrl-E)");

        // create Optimize button
        c_OptimizeButton = add(actions.c_optimize);
        c_OptimizeButton.setText(""); // an icon-only button
        c_OptimizeButton.setToolTipText("Start Optimization");

        // create OptimizeWithSettings button
        c_OptimizeWithSettingsButton = add(actions.c_optimizeWithSettings);
        c_OptimizeWithSettingsButton.setText(""); // an icon-only button
        c_OptimizeWithSettingsButton.setToolTipText("Start Optimization With Settings");


	addSeparator();
	newButton(actions.c_newFlow, "", "New flow (Ctrl-F)");
	addSeparator();
	add(new JLabel("Zoom:"));
	add(Box.createVerticalStrut(5));
	newCheckBox(actions.c_zoom, "Zoom in/out");
        addSeparator();
        add(new JLabel("Labels:  "));

        c_NodeLabelsButton = newToggleButton(actions.c_showNodeLabelsButtonPressed, "NLB", "Toggle node labels");
        c_NodeIDsButton = newToggleButton(actions.c_showNodeIDsButtonPressed, "NID", "Toggle node IDs");
        c_FlowLabelsButton = newToggleButton(actions.c_showFlowLabelsButtonPressed, "FLB", "Toggle flow labels");
        c_FlowIDsButton = newToggleButton(actions.c_showFlowIDsButtonPressed, "FID", "Toggle flow IDs");

        setFloatable(false);
    }

    /**
     * Conveniece method to add new buttons
     *
     * @param action The action associated with the button
     * @param tooltip The tooltip to be shown
     */
    void newButton(Action action, String text, String tooltip)
    {
        // create toolbar button
        JButton button = add(action);
	button.setText(text); //an icon-only button
        button.setToolTipText(tooltip);
    }

    JToggleButton newToggleButton(Action action, String text, String tooltip)
    {
        // create toolbar button
        JToggleButton button = new JToggleButton(action);
        button.setText(text);
        button.setToolTipText(tooltip);
        add(button);
        return button;
    }


    /**
     * Convenience class to add check/rolldown menus
     *
     * @param action The action to be associated with the widget
     * @param tooltip The tooltip to be shown
     */
    void newCheckBox(Action action, String tooltip)
    {
        // create toolbar checkbox
	c_combo = new JComboBox(Zoom.getStrings());
	c_combo.setAction(action);
	c_combo.setSelectedItem(Zoom.getSelected());
	JButton button = new JButton();
	button.add(c_combo);
	button.setBorderPainted(false);
	add(button);
	button.setText(""); //an icon-only button
        button.setToolTipText(tooltip);
    }
}
