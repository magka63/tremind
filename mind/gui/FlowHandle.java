/*
 * Copyright 2005:
 * Marcus Bergendorff LTU <bermar@users.sorceforge.net>
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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

import mind.model.ID;

/**
 * @author bermar
 *
 * Support for bent arrows. 
 */
public class FlowHandle extends GraphComponent {
	private int size = 6;
	private boolean selected = false;
	/**
	 * @param componentID
	 */
	public FlowHandle(ID componentID) {
		super(componentID);
		setX(0);
		setY(0);
	}
	
	public FlowHandle(ID componentID, int x, int y) {
		super(componentID);
		setX(x);
		setY(y);
	}
	
	
	public Rectangle getRectangle() {
		return new Rectangle(getX() - (size / 2), getY() - (size / 2), size, size);
	}

	
	public void paint(GUI gui, Graphics2D g) {
		if(selected)
			g.setColor(Color.RED);
		else
			g.setColor(Color.YELLOW);
		g.fill(getRectangle());
		g.setColor(Color.DARK_GRAY);
		g.draw(getRectangle());
	}

	
	public void setShowID(boolean show) {
		return;
	}

	
	public void setShowLabel(boolean show) {
		return;
	}

	
	protected Shape getClickableArea() {
		return getRectangle();
	}
	
	
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * 
	 */
	public void toggleSelected() {
		if(selected)
			selected = false;
		else
			selected = true;		
	}
}
