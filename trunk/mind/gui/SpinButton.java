/*
 * Note: This comment header was added by the 2007 PUM5 group, we did
 * however not create all content in this file. Our work here was 
 * making it editable and connecting it to the SpinButtonListener class.
 * 
 * Copyright 2007:
 * Daniel Källming <danka053@student.liu.se>
 * David Karlslätt <davka417@student.liu.se>
 * Freddie Pintar <frepi150@student.liu.se>
 * Mårten Thurén <marth852@student.liu.se>
 * Per Fredriksson <perfr775@student.liu.se>
 * Ted Palmgren <tedpa175@student.liu.se>
 * Tor Knutsson <torkn754@student.liu.se>
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

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * Class SpinButton.  Build spinbutton widget
 *
 *  @param int - Start position
 *  @param int - End position
 *  @param int - Increment value
 *  @param int - Initial value
 *  @param boolean - (true)  Size SpinButton to Max Size.
 *                   (false) Size SpinButton based on range
 */
public class SpinButton
    extends JPanel
{
    JTextField c_txtValue;
    int c_begin = 0;
    int c_end = 999;
    int c_increment = 1;
    int c_initial = 0;
    int c_width = 3;
    boolean c_align = false;
    boolean c_enabled = true;
    boolean c_isEditable = false;
    boolean c_editableValue = false;
    boolean c_incButtonDown = false;
    boolean c_decButtonDown = false;

    SpinCanvas c_spin;
    Thread c_runner;
    public Vector c_listeners = new Vector(0);

    class SpinCanvas
	extends Canvas
	implements MouseListener, Runnable
    {
	public SpinCanvas()
	{
	    addMouseListener(this);
	}

	public void count() {
	    try {
		int current = Integer.valueOf(c_txtValue.getText()).intValue();

		if (current - c_increment >= c_begin && c_decButtonDown) {
		    setValue(current - c_increment);
		    triggerListeners(-1);
		}
		else if (c_decButtonDown)
		    if (current != c_begin) {
			setValue(c_begin);
			triggerListeners(-1);
		    }

		if (current + c_increment <= c_end && c_incButtonDown) {
		    setValue(current + c_increment);
		    triggerListeners(1);
		}
		else if (c_incButtonDown)
		    if (current != c_end) {
			setValue(c_end);
			triggerListeners(1);
		    }
	    }
	    catch (NumberFormatException e) {}
	}

	/**
	 * Dimension minimumSize()
	 * return minimum Dimension for SpinButtonHelper widget
	 */
	public Dimension getMinimumSize()
	{
	    return new Dimension (15, 22);
	}

	/**
	 * Dimension preferredSize()
	 * return preferred Dimension for SpinButtonHelper widget
	 */
	public Dimension getPreferredSize()
	{
	    return new Dimension (15, 22);
	}

	/**
	 * Insets insets()
	 * returns insets for SpinButtonHelper widget
	 */
	public Insets insets()
	{
	    return new Insets(0, 0, 0, 0);
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e)
	{
	    if  (e.getY() <= 9) {
		c_incButtonDown = true;
		c_decButtonDown = false;
	    }
	    else {
		c_incButtonDown = false;
		c_decButtonDown = true;
	    }

	    if (c_enabled) {
		c_runner = new Thread(this);
		c_runner.start();
	    }
	}

	public void mouseReleased(MouseEvent e)
	{
	    c_incButtonDown = false;
	    c_decButtonDown = false;
	}

	public void paint(Graphics g)
	{
	    g.setColor(Color.white);
	    g.draw3DRect(0,1, 13,9,true);
	    g.setColor(Color.gray);
	    g.draw3DRect(1,2, 12,8,true);

	    g.setColor(Color.white);
	    g.draw3DRect(0,11, 13,9,true);
	    g.setColor(Color.gray);
	    g.draw3DRect(1,12, 12,8,true);

	    if (c_enabled)
		g.setColor(Color.black);
	    else
		g.setColor(Color.gray);

	    g.drawLine(7,4,7,4);
	    g.drawLine(6,5,8,5);
	    g.drawLine(5,6,9,6);
	    g.drawLine(4,7,10,7);

	    g.drawLine(4,14,10,14);
	    g.drawLine(5,15,9,15);
	    g.drawLine(6,16,8,16);
	    g.drawLine(7,17,7,17);
	}

	/**
	 * run()
	 * Thread to enable SpinButton incrementation
	 */
	public void run()
	{
	    int times = 0;
	    int speed = 300;

	    while (c_runner.isAlive() &&
		   (c_incButtonDown || c_decButtonDown)) {
		count();
		times += 1;
		if (times > 5 && times < 15)
		    speed = 100;
		else if (times >15)
		    speed = 10;

		try {
		    c_runner.sleep(speed);
		}
		catch (InterruptedException e) {}
	    }
	}
    }

    public SpinButton() {
	c_txtValue = new JTextField("" + c_initial, c_width);
	c_txtValue.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    //System.out.println("size44 = " + c_listeners.size());
		}
	    });
	
	c_txtValue.setEditable(true);
	
	c_txtValue.setBackground(Color.white);
	
	c_spin = new SpinCanvas();
	createGUI();
    }

    /**
     * Constructor for SpinButton Class
     * int (starting value of button
     * int (ending value of button
     * int (increment value for each button click
     * int (initial value of button
     * boolean (size button based on end value or 5 chars)
     */
    public SpinButton(int begin, int end, int increment,
		      int initial)
    {
	c_begin = begin;
	c_end = end;
	c_increment = increment;
	c_initial = initial;

	String width = String.valueOf(end);
	c_width = width.length();

	c_txtValue = new JTextField("" + initial, c_width);
	c_txtValue.setEditable(true);
	
	c_txtValue.setBackground(Color.white);

	c_spin = new SpinCanvas();
	createGUI();
    }

    public void addListener(SpinButtonListener listener)
    {
	c_listeners.addElement(listener);
    }

    public void addFocusListener(SpinButtonUpdateListener listener)
    {
    	c_txtValue.addFocusListener(listener.getFocusListener());
    }
    
    public void decValue()
    {
	setValue(getValue() - 1);
    }

    /**
     * Dimension minimumSize()
     * returns minimum Dimension of SpinButton
     */
    public Dimension getMinimumSize () {
	/*
	int d = 10;
	if (c_width == 1) d = 47;
	if (c_width == 2) d = 67;
	if (c_width == 3) d = 87;
	*/
	return new Dimension (15+c_width*20, 22);
    }

    /**
     * Dimension preferredSize()
     * returns preferred Dimension of SpinButton
     */
    public Dimension getPreferredSize () {
	return getMinimumSize();
    }

    public Dimension getMaximumSize() {
	return getMinimumSize();
    }

    /**
     * getValue()
     * @return current int value of SpinButton
     */
    public int getValue() {
	try {
		return Integer.valueOf(c_txtValue.getText()).intValue();
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		return -1; 
	}
    }

    public void incValue()
    {
	setValue(getValue() + 1);
    }

    public void removeListener(SpinButtonListener listener)
    {
	c_listeners.removeElement(listener);
    }

    /**
     * setEnd()
     * @param value sets the ending value of SpinButton
     */
    public void setEnd(int value)
    {
	c_end = value;
    }

    /**
     * enable
     * enables SpingButton for input
     */
    public void setEnabled(boolean enable)
    {
	c_enabled = enable;
	//c_txtValue.setEditable(enable);  //Use setEditable to do this... /JT
	c_txtValue.setEnabled(enable);
	c_spin.repaint();
    }

    /**
     * setEditable(boolean)
     * @param false - disables direct user input of SpinButton value
     * @param true - enables direct user input of SpinButton value
     */
    public void setEditable(boolean isEditable) {
	c_isEditable = isEditable;
	if (isEditable) {
	    c_txtValue.setEditable(true);
	}
	else {
	    c_txtValue.setEditable(false);
	}
    }

    /**
     * setStart()
     * @param value sets the starting value of SpinButton
     */
    public void setStart(int value) {
	c_begin = value;
    }

    /**
     * setValue(int value)
     * @param value sets the current value of SpinButton
     */
    public void setValue(int value) {
	if (value <= c_end && value >= c_begin)
	    c_txtValue.setText("" + value);
    }

    private void createGUI() {
	//	setFont(new Font("Dialog", Font.PLAIN, 12));
	setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	add(c_txtValue);
	add(c_spin);
    }

    private void triggerListeners(int trigger)
    {
	if (trigger == -1)
	    for (int i = 0; i < c_listeners.size(); i++) {
		((SpinButtonListener) c_listeners.elementAt(i)).
		    valueDecreased();
	    }

	if (trigger == 1)
	    for (int i = 0; i < c_listeners.size(); i++)
		((SpinButtonListener) c_listeners.elementAt(i)).
		    valueIncreased();
    }
}
