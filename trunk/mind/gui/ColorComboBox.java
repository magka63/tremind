/*
 * ColorComboBox.java
 *
 * Created on den 18 oktober 2001, 09:36
 */
package mind.gui;

import javax.swing.JComboBox;
import java.awt.Color;

/**
 *
 * @author  Peter
 * @version 2001-10-18
 */
public class ColorComboBox extends JComboBox {

    /** Creates new ColorComboBox */
    public ColorComboBox()
    {
        //Adding the ComboBox for the colors
        setEnabled(true);
        setEditable(false);
        //Adding colors to the list
        addItem(new ExtendedColor("Blue", Color.blue));
        addItem(new ExtendedColor("Red", Color.red));
        addItem(new ExtendedColor("Green", Color.green));
        addItem(new ExtendedColor("Orange", Color.orange));
        addItem(new ExtendedColor("Yellow", Color.yellow));
        addItem(new ExtendedColor("Cyan", Color.cyan));
        addItem(new ExtendedColor("Magenta", Color.magenta));
        addItem(new ExtendedColor("White", Color.white));
        addItem(new ExtendedColor("Light gray", Color.lightGray));
        addItem(new ExtendedColor("Gray", Color.gray));
        addItem(new ExtendedColor("Dark gray", Color.darkGray));
        addItem(new ExtendedColor("Black", Color.black));
    }
}
