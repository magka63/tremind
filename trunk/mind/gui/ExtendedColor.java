/*
 * ExtendedColor.java
 *
 * Created on den 18 oktober 2001, 13:29
 */

/**
 *
 * @author  Peter Andersson
 * @version 20001-10-18
 */
package mind.gui;

public class ExtendedColor extends java.awt.Color {

    private String c_name;

    /**
     * Creates new ExtendedColor
     * @param name The name of the color
     * @param color The color
     */
    public ExtendedColor(String name, java.awt.Color color) {
        super(color.getRGB());
        c_name = name;
    }

    /**
     * Creates a new extended color
     * @param name The name of the color
     * @param colorvalue The RGB value of the color
     */
    public ExtendedColor(String name, int colorvalue) {
        super(colorvalue);
        c_name = name;
    }

    public boolean equals(ExtendedColor color)
    {
        return c_name.equals(color.c_name) && super.equals((java.awt.Color)color);
    }

    /**
     * Returns the name of the color
     * @return the name of the color.
     */
    public String toString() {
        return c_name;
    }
}
