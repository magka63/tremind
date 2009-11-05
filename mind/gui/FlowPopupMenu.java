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

import javax.swing.JPopupMenu;
import javax.swing.JMenu;
import java.util.Vector;
import javax.swing.AbstractAction;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;

import mind.gui.dialog.MainDialog;
import mind.model.*;

public class FlowPopupMenu extends JPopupMenu {
	public static GlobalActions c_actions;
	private int c_x;
	private int c_y;

	public FlowPopupMenu(GUI gui, ID flow) {
		add(c_actions.c_flowProperties);
		add(new AddGraphFlowHandleAction(gui, flow));
		add(new RemoveGraphFlowHandleAction(gui, flow));
		// Add submenu for choosing resource
		JMenu submenu = new JMenu("Resource");

		Vector resources = gui.getResources();
		if (resources == null || resources.size() == 0)
			submenu.add("none");
		else {
			// Add all resources and make actions for them
			for (int i = 0; i < resources.size(); i++) {
				submenu.add(new FlowResourceAction(
						((Resource) resources.get(i)).toString(), gui, flow,
						((Resource) resources.get(i)).getID()));
			}
			add(submenu);
		}
	}
	
	public void show(Component invoker, int x, int y){
		super.show(invoker,x,y);
		c_x = x;
		c_y = y;
	}

	private class FlowResourceAction extends AbstractAction {
		private ID flow;

		private ID resource;

		private GUI gui;

		public FlowResourceAction(String name, GUI gui, ID flow, ID resource) {
			super(name, null);
			this.resource = resource;
			this.flow = flow;
			this.gui = gui;
		}

		public void actionPerformed(ActionEvent e) {
			gui.setResource(flow, resource);
		}
	}

	private class AddGraphFlowHandleAction extends AbstractAction {
		private ID flow;

		private GUI gui;

		public AddGraphFlowHandleAction(GUI gui, ID flow) {
			super("Add handle");
			this.flow = flow;
			this.gui = gui;
		}

		public void actionPerformed(ActionEvent arg0) {
			MainDialog md = (MainDialog) gui.getApp();
			GraphModel gm = md.getGraphArea().getGraphModel();
			gm.getGraphFlowControl().addGraphFlowHandle(flow, new Point(c_x, c_y), true);
			md.getGraphArea().repaint();
		}

	}
	
	private class RemoveGraphFlowHandleAction extends AbstractAction {
		private ID flow;

		private GUI gui;

		public RemoveGraphFlowHandleAction(GUI gui, ID flow) {
			super("Remove handle");
			this.flow = flow;
			this.gui = gui;
		}

		public void actionPerformed(ActionEvent arg0) {
			MainDialog md = (MainDialog) gui.getApp();
			GraphModel gm = md.getGraphArea().getGraphModel();
			gm.getGraphFlowControl().removeGraphFlowHandle(flow, new Point(c_x, c_y));
			md.getGraphArea().repaint();
		}

	}
}