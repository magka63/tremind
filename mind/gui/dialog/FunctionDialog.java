/*
 * Copyright 2005:
 * Jonas S‰‰v <js@acesimulation.com>
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

import javax.swing.JDialog;
import java.awt.HeadlessException;

/**
 * <p>Title: FunctionDialog</p>
 * <p>Description: This is the base class for all dialogs that are used to edit
 * node functions. The only common behavior so far is setting the title of the dialog</p>
 * @author Jonas S‰‰v - Ace Simulation AB
 * @version 1.0
 */

public class FunctionDialog extends JDialog {
  public FunctionDialog() throws HeadlessException {
  }
  public FunctionDialog(JDialog parent, boolean modal) {
    super(parent, modal);
    setTitle("Edit Node Function");
  }

}