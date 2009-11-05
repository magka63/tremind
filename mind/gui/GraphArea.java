/*
 * Copyright 2001:
 * Peter Andersson <petan117@student.liu.se>
 * Martin Hagman <marha189@student.liu.se>
 * Henrik Norin <henno776@student.liu.se>
 * Anna Stjerneby <annst566@student.liu.se>
 * Tim Terlegård <timte878@student.liu.se>
 * Johan Trygg <johtr599@student.liu.se>
 * Peter Åstrand <petas096@student.liu.se>
 * Jonas Sääv <js@acesimulation.com>
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
import java.awt.*;
import java.awt.event.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.io.IOException;
import java.util.Vector;

import mind.io.*;
import mind.model.*;
import mind.gui.dnd.*;

/**
 * This is a class that represents the graph area. It handles all
 * action that can be made to/with graph area. User can draw nodetypes
 * and functions to this area. It handles keyevents for shortcuts.
 *
 * @author Tim Terlegård
 * @author Jonas Sääv
 * @version 2003-10-02
 */

public class GraphArea extends JPanel implements DropTargetListener,
		MouseListener, MouseMotionListener, KeyListener, UserSettingConstants {
	// when we increase/decrease the size of the graph, we
	// increase/decrease it by a certain amount every time
	final int DEFAULT_AREA_INC_WIDTH = 100;

	final int DEFAULT_AREA_INC_HEIGHT = 100;

	private int c_areaIncWidth = 0;

	private int c_areaIncHeight = 0;

	// when we zoomed in/out we changed the positioning system
	private float c_scaleByProcent = 100;

	// when we drag a node, the pointer is almost in the middle of the node
	final int DRAG_X_OFFSET = 20;

	final int DRAG_Y_OFFSET = 20;

	private GUI c_gui;

	private GraphModel c_graphModel;

	private GraphStatus c_status;

	// the graphcomponent that was last touched
	private ID c_pickedComponent;

	// points out what node we drag over
	private ID c_dragOverNode;

	// when you press mousebutton on a node
	// where on the node did you click
	private int c_fromX, c_fromY;

	// this is how many steps components will be
	// moved when keyboard arrows are pressed
	final int c_keyStep = 3;

	private boolean c_isDragged;

	private Graphics2D c_graphics;

	private Ini c_userSettings = null;

	private FlowHandle c_draggedHandle;

	private ExtendedGraphFlow c_pickedFlow;

	private int lastX;

	private int lastY;

	/**
	 * Constructs a graph area
	 * @param app The window that will be the parent for user
	 * messages (dialogs)
	 */
	public GraphArea(GUI gui, Ini ini) {
		c_graphModel = new GraphModel();

		// makes this graph area a target for drag-and-dropactions
		setDropTarget(new DropTarget(this, this));

		// we want to listen for mouse and key events
		addMouseListener(this);
		addKeyListener(this);

		//We want a nice background
		setBackground(Color.gray);

		// when a component is dragged, the graph will scroll
		//	setAutoscrolls(true);

		c_status = new GraphStatus(GraphStatus.FRESH, this);
		c_gui = gui;
		c_userSettings = ini;

		readUserSettings();
	}

	public void createNewModel() {
		c_graphModel = new GraphModel();
		repaint();
	}

	/**
	 * Paints all the components that is in the graph
	 * @param g The graphics object on which we draw
	 */
	public void paintComponent(Graphics g) {
		// paint the background
		super.paintComponent(g);

		Graphics2D c_graphics = (Graphics2D) g;
		c_graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			      RenderingHints.VALUE_ANTIALIAS_ON);

		c_graphModel.paint(c_gui, c_graphics);
	}

	/**
	 * Deletes all marked components in the graph area
         * Components that has relations to other components are not
         * deleted. E.g. flows that are used by functions
	 */
	public void deleteComponents() {
		c_gui.deleteComponentsAction();
	}

        /**
         * Deletes nodes and flows given by the array ids. Use this if you have already
         * figured out the relations between the components to be deleted, i.e. if you delete
         * a node the ids array _should_ also contain ids of the in/out flows. This function is
         * used by GUI.deleteNodeAction().
         * @param ids Array containing ids of components that will be deleted unconditionally.
         */
        public void deleteNodesAndFlows(ID[] ids)
        {
          if (ids != null) {
            c_graphModel.remove(ids); // this removes both flows and nodes
          }
        }

	/**
	 * Deletes all marked flows
         * @deprecated
         * replaced by <code>mind.gui.GraphArea.deleteNodesAndFlows(ID[] ids)</code>
	 */
	public void deleteFlow() {
		ID[] componentIDs = c_graphModel.getAffectedByRemove(c_graphModel
				.getMarkedFlows());
		if (componentIDs != null)
			c_graphModel.remove(componentIDs);

		repaint();
	}

	/**
	 * Deletes all marked nodes and all the flows connected to
	 * these nodes
         * @deprecated
         * replaced by <code>mind.gui.GraphArea.deleteNodesAndFlows(ID[] ids)</code>
	 */
	public void deleteNode() {
		ID[] componentIDs = c_graphModel.getAffectedByRemove(c_graphModel
				.getMarkedNodes());
		if (componentIDs != null) {
			c_graphModel.remove(componentIDs);
			componentRemoved(componentIDs);
		}

		repaint();
	}

	/**
	 * Delete the node with the specified ID
	 * @param nodeID The ID of the node that should be deleted
         * @deprecated
         * replaced by <code>mind.gui.GraphArea.deleteNodesAndFlows(ID[] ids)</code>
         */
	public void deleteNode(ID nodeID) {
		ID[] componentIDs = c_graphModel.getAffectedByRemove(new ID[] { nodeID });
		if (componentIDs != null) {
			c_graphModel.remove(componentIDs);
			componentRemoved(componentIDs);
		}

		repaint();
	}

	/**
	 * Gets all nodes and flows affected by the removal of all marked nodes.
	 * @return An array of IDs of nodes and flows that will be affected.
	 */
	public ID[] getAffectedByRemove(ID[] componentIDs) {
		return c_graphModel.getAffectedByRemove(componentIDs);
	}

	/**
	 * Returns a location for a component
	 * @param ID The ID of the component to get the location for.
	 * @return A point of the upper-left corner of the component.
	 */
	public Point getComponentLocation(ID componentID) {
		return new Point(c_graphModel.getX(componentID), c_graphModel.getY(componentID));
	}

	/**
	 * Gets all marked components in the current model.
	 * @return An array of IDs of marked graph components.
	 */
	public ID[] getMarked() {
		return c_graphModel.getMarked();
	}

	/**
	 * Gets all marked flows in the current model.
	 * @return An array of IDs of marked graph flows.
	 */
	public ID[] getMarkedFlows() {
		return c_graphModel.getMarkedFlows();
	}

	/**
	 * Gets all marked nodes in the current model.
	 * @return An array of IDs of marked graph nodes.
	 */
	public ID[] getMarkedNodes() {
		return c_graphModel.getMarkedNodes();
	}

	/**
	 * Gets the graph model.
	 * @return The graph model.
	 */
	public GraphModel getModel() {
		return c_graphModel;
	}

	/**
	 * Scales the x to the zoomlevel and returns it.
	 * @param x the point to scale.
	 * @return a scaled coordinate.
	 */
	public int getX(int x) {
		return (int) (x * c_scaleByProcent / 100);
	}

	/**
	 * Scales the x to the zoomlevel and returns it.
	 * @param x the point to scale.
	 * @return a scaled coordinate.
	 */
	public int getX(double x) {
		return (int) (x * c_scaleByProcent / 100);
	}

	/**
	 * Scales the x to the zoomlevel and returns it.
	 * @param x the point to scale.
	 * @return a scaled coordinate.
	 */
	public int getY(int y) {
		return (int) (y * c_scaleByProcent / 100);
	}

	/**
	 * Scales the x to the zoomlevel and returns it.
	 * @param x the point to scale.
	 * @return a scaled coordinate.
	 */
	public int getY(double y) {
		return (int) (y * c_scaleByProcent / 100);
	}

	/**
	 * Gets the x-coordinate of the component with the specified ID.
	 * @param componentID The ID of the component.
	 * @return The x-coordinate of the component.
	 */
	public int getX(ID componentID) {
		return c_graphModel.getX(componentID);
	}

	/**
	 * Gets the y-coordinate of the component with the specified ID.
	 * @param componentID The ID of the component.
	 * @return The y-coordinate of the component.
	 */
	public int getY(ID componentID) {
		return c_graphModel.getY(componentID);
	}

	/**
	 * Sets mode to "flow mode"
	 */
	public void setFlowMode() {
		c_graphModel.unmarkAll();
		c_status.setNewFlow();
		repaint();
	}

	/**
	 * Sets new coordinates for the specified node.
	 * @param nodeID The ID of the node to get new location coordinates.
	 * @param x The x-coordinate of the new location.
	 * @param y The y-coordinate of the new location.
	 */
	public void setNodeLocation(ID nodeID, int x, int y) {
		c_graphModel.move(new ID[] { nodeID }, x - c_graphModel.getX(nodeID), y
				- c_graphModel.getY(nodeID));
		componentMoved(nodeID);
		repaint();
	}

	/**
	 * Adds a new flow to the graph area and the underlying model
	 * @param from The node that is the new flow's source
	 * @param to The node that is the new flow's destination
	 */
	public void newFlow(ID flowID, ID from, ID to) {
		c_graphModel.add(flowID, from, to);

		repaint();
	}

	/**
	 * Adds a new node to the graph area and the underlying model.
	 * @param x x-coordinate of where the node should be placed.
	 * @param y y-coordinate of where the node should be placed.
	 */
	public void newNode(ID nodeID, int x, int y) {
		c_graphModel.add(nodeID, x, y);
		componentMoved(nodeID);
		repaint();
	}

	/**
	 * Updates the internal settings if the user has made some new
	 * usersettings.
	 */
	public void settingsUpdated() {
		try {
			int width = Integer.parseInt(c_userSettings
					.getProperty(AREA_INC_WIDTH));
			if (width >= 0)
				c_areaIncWidth = width;
		} catch (NumberFormatException e) {
		}
		try {
			int height = Integer.parseInt(c_userSettings
					.getProperty(AREA_INC_HEIGHT));
			if (height >= 0)
				c_areaIncHeight = height;
		} catch (NumberFormatException e) {
		}
		componentRemoved(null);
	}

	/**
	 * Draws all flow IDs on the graph.
	 */
	public void showFlowIDs(boolean show) {
		c_graphModel.setShowFlowIDs(show);
		repaint();
	}

	/**
	 * Draws all flow labels on the graph.
	 */
	public void showFlowLabels(boolean show) {
		c_graphModel.setShowFlowLabels(show);
		repaint();
	}

	/**
	 * Draws all flow IDs on the graph.
	 */
	public void showNodeIDs(boolean show) {
		c_graphModel.setShowNodeIDs(show);
		repaint();
	}

	/**
	 * Draws all flow labels on the graph.
	 */
	public void showNodeLabels(boolean show) {
		c_graphModel.setShowNodeLabels(show);
		repaint();
	}

	/**
	 * Makes everything in the graph smaller/bigger, zooms the graph.
	 * @param scale The procent value of the scaling.
	 * scale equal to 100 is normal size
	 */
	public void zoom(int procent) {
		c_scaleByProcent = procent;
		c_graphModel.scaleByProcent(procent);
		componentRemoved(null);
		repaint();
	}

	/**
	 * Accepts drag. Is invoked when you are dragging over the DropSite.
	 */
	public void dragEnter(DropTargetDragEvent event) {
		event.acceptDrag(DnDConstants.ACTION_MOVE);
	}

	/**
	 * Does nothing. Is needed because we implement DropTargetListener
	 */
	public void dragExit(DropTargetEvent event) {
	}

	/**
	 * If a function is dragged over a node, the node changes color
	 * @param event The drag-and-drop event that triggered this action
	 */
	public void dragOver(DropTargetDragEvent event) {

		if (!event.isDataFlavorSupported(DragTreeNode.flavor)) {

			ID nodeID = c_graphModel.getTopNode((int) event.getLocation().getX(),
					(int) event.getLocation().getY());

			// if last mouseover was over a node
			if (c_dragOverNode != null) {
				if (nodeID == null) {
					c_graphModel.setDraggedOver(c_dragOverNode, false);
					c_dragOverNode = null;
				}
			} else if (nodeID != null) {
				c_dragOverNode = nodeID;
				c_graphModel.setDraggedOver(c_dragOverNode, true);
			}
			repaint();
		}
	}

	/**
	 * If a node is dropped on the graph area, we add it
	 * If a function is dropped on a node, we add the function to the node
	 * @param event The drag-and-drop event that triggered this action
	 */
	public void drop(DropTargetDropEvent event) {
		try {
			Transferable transferable = event.getTransferable();

			// we accept String and DragTreeNode as Transferable
			if (transferable.isDataFlavorSupported(DragTreeNode.flavor)) {
				event.acceptDrop(DnDConstants.ACTION_MOVE);
				NodeToDrag s = (NodeToDrag) transferable
						.getTransferData(DragTreeNode.flavor);
				event.getDropTargetContext().dropComplete(true);

				c_gui.newNodeAction((int) event.getLocation().getX()
						- getX(DRAG_X_OFFSET), (int) event.getLocation().getY()
						- getY(DRAG_Y_OFFSET), s.getFile());
				repaint();
			} else if (transferable
					.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				event.acceptDrop(DnDConstants.ACTION_MOVE);
				String s = (String) transferable
						.getTransferData(DataFlavor.stringFlavor);
				event.getDropTargetContext().dropComplete(true);

				if (c_dragOverNode != null) {
					c_gui.addFunction(c_dragOverNode, s);
					//c_gui.showMessageDialog("A function " + s + " is added to the node");
					c_graphModel.setDraggedOver(c_dragOverNode, false);
					c_dragOverNode = null;
				}

				repaint();
			} else {
				event.rejectDrop();
			}
		} catch (IOException exception) {
			exception.printStackTrace(System.out);
			System.err.println("Exception" + exception.getMessage());
			event.rejectDrop();
		} catch (UnsupportedFlavorException ufException) {
			ufException.printStackTrace(System.out);
			System.err.println("Exception" + ufException.getMessage());
			event.rejectDrop();
		}
	}

	public void dropActionChanged(DropTargetDragEvent event) {
	}

	/**
	 * A node is pressed. Depending on mode we do different things
	 * If mode is "FRESH" we don't do anyting.
	 * If mode is "FROM_FLOW" the node will be registered as a from-node
	 * and the user is notified the status is changed to "TO_FLOW"
	 * IF mode is "TO_FLOW" a new flow is created between two nodes
	 * @param nodeID The ID of the node that is pressed
	 */
	void makeNodeAction(ID nodeID) {
		if (nodeID == null)
			return;

		if (c_status.getStatus() == GraphStatus.FROM_FLOW)
			c_status.setToFlow();
		else if (c_status.getStatus() == GraphStatus.TO_FLOW) {
			if (!c_pickedComponent.equals(nodeID))
				c_gui.newFlowAction(c_pickedComponent, nodeID);

			c_status.setFresh();
		}

		c_pickedComponent = nodeID;
	}

	/**
	 * Mouse is pressed. If no component in the graph is pressed all
	 * components are unmarked. If a component is pressed the component
	 * is marked. If control is not down, all other components will be
	 * unmarked
	 * @param e The mouse event that triggered this action
	 */
	public void mousePressed(MouseEvent e) {
		// The grapharea must get the focus to be able to get keyevents
		requestFocus();

		// prepare for dragging node
		addMouseMotionListener(this);

		c_pickedFlow = null;

		ID graphComponent = c_graphModel.getTopComponent((int) e.getX(), (int) e.getY());

		if (graphComponent == null) {
			c_graphModel.unmarkAll();
			c_status.setFresh();
			c_pickedComponent = null;
		} else {
			if (graphComponent.isNode()) {
				c_fromX = e.getX() - c_graphModel.getX(graphComponent);
				c_fromY = e.getY() - c_graphModel.getY(graphComponent);
			}

			if (e.isControlDown())
				c_graphModel.setMarked(graphComponent, !c_graphModel.isMarked(graphComponent));
			else if (!c_graphModel.isMarked(graphComponent)) {
				c_graphModel.unmarkAll();
				c_graphModel.setMarked(graphComponent, true);
			}

			c_graphModel.touch(graphComponent);

			if (graphComponent.isNode())
				makeNodeAction(graphComponent);

			// support for dragging handles
			if(graphComponent.isFlow()){
				c_pickedFlow = c_graphModel.getGraphFlow(graphComponent);
				if(c_pickedFlow != null){
					c_draggedHandle = c_pickedFlow.getHandleAtLocation(e.getX(),e.getY());
					if(c_draggedHandle != null)
						c_draggedHandle.toggleSelected();
				}
			}
		}

		c_isDragged = false;

		e.consume();
		repaint();
		lastX = e.getX();
		lastY = e.getY();
	}

	/**
	 * Mouse is released
	 * @param e The mouse event that triggered this action
	 */
	public void mouseReleased(MouseEvent e) {
		ID[] ids;
		// stop listening to mouse moves
		removeMouseMotionListener(this);

		if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
			ids = getMarked();
			if (ids != null && ids.length == 1) {
				if (ids[0].isNode()) {
					JPopupMenu popup = new NodePopupMenu();
					popup.show(this, e.getX(), e.getY());
				} else if (ids[0].isFlow()) {
					JPopupMenu popup = new FlowPopupMenu(c_gui, ids[0]);
					popup.show(this, e.getX(), e.getY());
				}
			}
		}

		// if the graph gets bigger when we moved a component
		// scroll it
		if (c_pickedComponent != null && c_pickedComponent.isNode())
			componentMoved(c_pickedComponent);

		c_draggedHandle = null;

		e.consume();
		repaint();
	}

	/**
	 * Mouse is dragged. If a node is dragged it will be moved in
	 * the graph.
	 * @param e The mouse event that triggered this action
	 */
	public void mouseDragged(MouseEvent e) {
		if ((c_pickedComponent != null) && (c_pickedComponent.isNode())) {
			if (c_status.getStatus() == GraphStatus.FRESH) {
				c_isDragged = true;
				c_graphModel.move(c_graphModel.getMarked(), e.getX()
						- c_graphModel.getX(c_pickedComponent) - c_fromX, e.getY()
						- c_graphModel.getY(c_pickedComponent) - c_fromY);
				c_gui.setChanged(true);
			}
		}

		if(c_draggedHandle != null && c_pickedFlow != null){
			int dx = e.getX() - lastX;
			int dy = e.getY() - lastY;
			c_draggedHandle.setSelected(false);
			c_pickedFlow.moveSelectedHandles(dx, dy);
			c_graphModel.moveHandle(c_draggedHandle, c_pickedFlow, e.getX(), e.getY());
		}
		e.consume();
		repaint();
		lastX = e.getX();
		lastY = e.getY();
	}

	/**
	 * Takes care of doubleclicking Nodes and Flows to show the corresponding
	 * properties dialog
	 * @param e the MouseEvent
	 */
	public void mouseClicked(MouseEvent e) {

		ID[] ids;

		ids = getMarked();
		if (ids.length == 1)
			if (e.getClickCount() == 2) {
				if (ids[0].isNode()) {
					c_gui.doubleClickNodeAction();
				} else {
					c_gui.doubleClickFlowAction();
				}
			}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	/**
	 * A Key is pressed. We make appropriate action
	 * @param e Key event that triggered this action
	 */
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			c_graphModel.move(c_graphModel.getMarked(), -c_keyStep, 0);
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			c_graphModel.move(c_graphModel.getMarked(), c_keyStep, 0);
		if (e.getKeyCode() == KeyEvent.VK_UP)
			c_graphModel.move(c_graphModel.getMarked(), 0, -c_keyStep);
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			c_graphModel.move(c_graphModel.getMarked(), 0, c_keyStep);
		if (e.getKeyCode() == KeyEvent.VK_DELETE)
			deleteComponents();

		repaint();
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
	}

	/*
	 * If a node or any other component is removed, added or moved,
	 * the scrollbars should be adjusted
	 */
	private void componentMoved(ID component) {
		boolean changed = false;

		int width = c_graphModel.getNodeSize().width;
		int height = c_graphModel.getNodeSize().height;

		if (component != null) {
			Rectangle rect = new Rectangle(c_graphModel.getX(component), c_graphModel
					.getY(component), width, height);
			scrollRectToVisible(rect);
		}

		// Update client's preferred size because
		// the area taken up by the graphics has
		// gotten larger or smaller
		Dimension d = new Dimension(c_graphModel.getAreaSize());
		d.width += c_areaIncWidth;
		d.height += c_areaIncHeight;
		setPreferredSize(d);

		// Let the scroll pane know to update itself
		// and its scrollbars.
		revalidate();
	}

	/*
	 * If a node or any other component is removed, added or moved,
	 *  the scrollbars should be adjusted
	 */
	private void componentMoved(ID[] components) {
		if (components == null)
			return;

		for (int i = 0; i < components.length; i++)
			componentMoved(components[i]);
	}

	/*
	 * If a node or any other component is removed,
	 *  the scrollbars should be adjusted
	 */
	private void componentRemoved(ID[] components) {
		// Update client's preferred size because
		// the area taken up by the graphics has
		// gotten larger or smaller
		Dimension d = new Dimension(c_graphModel.getAreaSize());
		d.width += c_areaIncWidth;
		d.height += c_areaIncHeight;
		setPreferredSize(d);

		// Let the scroll pane know to update itself
		// and its scrollbars.
		revalidate();
	}

	private void readUserSettings() {
		String areaIncWidth = c_userSettings
				.getProperty(UserSettingConstants.AREA_INC_WIDTH);
		String areaIncHeight = c_userSettings
				.getProperty(UserSettingConstants.AREA_INC_HEIGHT);

		try {
			c_areaIncWidth = Integer.parseInt(areaIncWidth);
		} catch (NumberFormatException e) {
			c_areaIncWidth = DEFAULT_AREA_INC_WIDTH;
		}
		try {
			c_areaIncHeight = Integer.parseInt(areaIncHeight);
		} catch (NumberFormatException e) {
			c_areaIncHeight = DEFAULT_AREA_INC_HEIGHT;
		}
	}

	/**
	 * @return Returns the c_graphModel.
	 */
	public GraphModel getGraphModel() {
		return c_graphModel;
	}
}