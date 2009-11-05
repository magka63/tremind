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

/**
 * Class that's almost equivalent to JTextField. The only difference
 * is that this text field only takes positive floats as input.
 * Following examples are valid inputs:
 * 1) 34
 * 2) 34.
 * 3) 34.56
 *
 * @author Tim Terlegård
 */
package mind.gui;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.event.*;
import javax.swing.*;
import java.text.*;

public class PositiveNumberField
    extends NumberField
{

  public class FormattedTextFieldVerifier
      extends InputVerifier {
    public boolean verify(JComponent input) {
      if (input instanceof JFormattedTextField) {
        JFormattedTextField ftf = (JFormattedTextField) input;
        JFormattedTextField.AbstractFormatter formatter = ftf.getFormatter();
        if (formatter != null) {
          String text = ftf.getText();
          try {
            formatter.stringToValue(text);
            float f = Float.parseFloat(text);
            if (f < 0.) {
              return false;
            } else
            return true;
          }
          catch (ParseException pe) {
            return false;
          }
          catch (NumberFormatException nfe) {
            ftf.setText("0.0");
            return false;
          }
        }
      }
      return true;
    }

    public boolean shouldYieldFocus(JComponent input) {
      return verify(input);
    }
  }

  public PositiveNumberField() {
    super(0.0f, 10);
    setInputVerifier(new FormattedTextFieldVerifier());
  }

  public PositiveNumberField(float value, int columns) {
    super(value, columns);
    setInputVerifier(new FormattedTextFieldVerifier());
  }

/*    public PositiveNumberField(float value, int columns)
    {
	setColumns(columns);

	if (value >= 0)
	    setText(String.valueOf(value));
    }

    // "1d" and "1f" is valid for Float.parseFloat(String),
    // therefore common loops are used instead
    protected boolean isValid(String str)
    {
	int index = 0;

	// ignore all first digits
	while (str.length() > index && Character.isDigit(str.charAt(index)))
	    index++;
	if (str.length() == index)
	    return true;

	// if no digits were found, string is no number
	if (index < 1)
	    return false;

	// if no more characters, see if next is a dot
	if (str.charAt(index) == '.')
	    index++;
	else
	    return false;

	// if the last is a dot, it's valid
	if (str.length() == index)
	    return true;

	// if there was a dot, check that the rest are digits
	while (str.length() > index && Character.isDigit(str.charAt(index)))
	    index++;
	if (str.length() == index)
	    return true;
	else
	    return false;
    } */
}
