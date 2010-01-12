/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mind.gui;

import javax.swing.*;
import java.text.*;

/**
 * Added by Nawzad Mardan 090508
 * @author nawma77
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
 * Class that's almost equivalent to JTextField. This text field takes positive and negatve floats as input.
 * Following examples are valid inputs:
 * 1) 34
 * 2) -34.3
 * 3) 34.56
 *
 * @author Nawzad Mardan
 */
public class PositiveAndNegativeNumberField  extends NumberField
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
              return true;
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

  public PositiveAndNegativeNumberField() {
    super(0.0f, 10);
    setInputVerifier(new FormattedTextFieldVerifier());
  }

  public PositiveAndNegativeNumberField(float value, int columns) {
    super(value, columns);
    setInputVerifier(new FormattedTextFieldVerifier());
  }
}
