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
 * is that this text field only takes floats as input.
 * Following examples are valid inputs:
 * 1) 34
 * 2) 34.
 * 3) 34.56
 * 4) -3.3333
 *
 * @author Tim Terlegård
 */

package mind.gui;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.event.*;
import java.text.*;
import javax.swing.JFormattedTextField;
import javax.swing.text.*;
import javax.swing.JOptionPane;

public class NumberField
    extends JFormattedTextField
{

  private static DecimalFormat c_nf = (DecimalFormat) DecimalFormat.getNumberInstance();

  public NumberField()
  {
  }

  public NumberField(float value, int columns)
  {
    super(c_nf);
    DecimalFormatSymbols df = new DecimalFormatSymbols();
    df.setDecimalSeparator('.');
    c_nf.setMaximumFractionDigits(30);
    c_nf.setDecimalFormatSymbols(df);
    c_nf.setGroupingUsed(false);
    setText(Float.toString(value));
    setColumns(columns);
    setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
  }

  /**
   * Overridden to perform an automatic commitEdit when calling setText
   * @param t a string
   */
  public void setText(String t) {
    super.setText(t);
    try {
      commitEdit();
    }
    catch (ParseException ex) {
    }
  }

  /**
   * getText should not be called when using NumberField. A NumberField has
   * an internal representation of the value which is retrieved using getFloatValue
   * @return a float value
   */
  public float getFloatValue()
  {

    if (getValue() != null) {
      if (getValue().getClass().getName().equals("java.lang.Long"))
        return ( (Long) getValue()).floatValue();
      else
      if (getValue().getClass().getName().equals("java.lang.Integer"))
        return ( (Integer) getValue()).floatValue();
      else
      if (getValue().getClass().getName().equals("java.lang.Float"))
        return ( (Float) getValue()).floatValue();
      else
      if (getValue().getClass().getName().equals("java.lang.Double"))
        return ( (Double) getValue()).floatValue();
      else {
        JOptionPane.showMessageDialog(null, "Internal error in NumberField",
                                      "Internal Error",
                                      JOptionPane.ERROR_MESSAGE);
        return 99.99f;
      }
    }
    else {
      JOptionPane.showMessageDialog(null, "Internal error in NumberField",
                              "Internal Error",
                              JOptionPane.ERROR_MESSAGE);
      return 999.99f;
    }
  }

}

/*
public class NumberField
    extends JTextField
{
    public NumberField()
    {
    }

    public NumberField(float value, int columns)
    {
	super(String.valueOf(value), columns);
    }

    public float getValue()
    {
	if (getText().length() > 0)
	    return Float.valueOf(getText()).floatValue();
	else
	    return 0;
    }

    protected Document createDefaultModel()
    {
        return new NumberDocument();
    }

    // "1d" and "1f" is valid for Float.parseFloat(String),
    // therefore common loops are used instead
    protected boolean isValid(String str)
    {
	int index = 0;

	//if first character is '-', we just ignore it
	if (str.length() > index && str.charAt(index) == '-')
	    index++;

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
    }

    protected class NumberDocument
	extends PlainDocument
    {
        public void insertString(int offset, String str, AttributeSet a)
	    throws BadLocationException
	{
	    String newText;
	    String text = NumberField.this.getText();

	    if (text.length() == 0)
		newText = new String(str);
	    else if (offset == text.length())
		newText = text.substring(0, offset) + str;
	    else
		newText = text.substring(0, offset) + str +
		    text.substring(offset + 1);

	    if (isValid(newText))
		super.insertString(offset, str, a);
        }
    }
} */
