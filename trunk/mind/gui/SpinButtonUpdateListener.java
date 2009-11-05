/*
 * Copyright 2007:
 * Per Fredriksson <perfr775@student.liu.se>
 * David Karlslätt <davka417@student.liu.se>
 * Tor Knutsson	<torkn754@student.liu.se>
 * Daniel Källming <danka053@student.liu.se>
 * Ted Palmgren <tedpa175@student.liu.se>
 * Freddie Pintar <frepi150@student.liu.se>
 * Mårten Thurén <marth852@student.liu.se> 
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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * This class is used by dialogs to update timesteps after they
 * have been edited manually.
 * 
 * @author Per Fredriksson
 * @author Tor Knutsson
 * @version 2007-12-02
 */

public abstract class SpinButtonUpdateListener {
	private SpinButton spinButton;
	/**
	 * The valueUpdated method should be overlayed and call the updateTimestep()
	 * method from classes which wish to implement this feature.
	 * @param spin - the SpinButton to whom this text field belongs
	 */
	public SpinButtonUpdateListener(SpinButton spin) {
		spinButton = spin;
	}
	public abstract void valueUpdated();
	
	public FocusListener getFocusListener() {
		return new FocusListener() {
			private int originalValue;

			public void focusLost(FocusEvent e) {
				//System.out.println("focus was lost!");
				if (spinButton.getValue() >= spinButton.c_begin && spinButton.getValue() <= spinButton.c_end) {
					spinButton.setValue(spinButton.getValue());
				}
				else {
					spinButton.setValue(originalValue);
				}
				valueUpdated();
			}
			public void focusGained(FocusEvent e){
				originalValue = spinButton.getValue();
			}
		};
	}
}
