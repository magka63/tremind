/*
 * reMIND.java
 *
 * Created on den 11 februari 2008, 13:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/*
 * reMIND.java
 *
 * Created on den 11 februari 2008, 13:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package remindv2;
import mind.gui.GUI;

/**
 *
 * @author nawma77(Nawzad Mardan)
 */
/*
 * Copyright 2001:
 * Peter Andersson <petan117@student.liu.se>
 * Martin Hagman <marha189@student.liu.se>
 * Henrik Norin <henno776@student.liu.se>
 * Anna Stjerneby <annst566@student.liu.se>
 * Tim Terlegård <timte878@student.liu.se>
 * Johan Trygg <johtr599@student.liu.se>
 * Peter Åstrand <petas096@student.liu.se>
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
* java.
* @author Nawzad Mardan
* @author Peter Andersson <petan117@student.liu.se>
* @author Martin Hagman <marha189@student.liu.se>
* @author Henrik Norin <henno776@student.liu.se>
* @author Anna Stjerneby <annst566@student.liu.se>
* @author Tim Terlegård <timte878@student.liu.se>
* @author Johan Trygg <johtr599@student.liu.se>
* @author Peter Åstrand <petas096@student.liu.se>
* @version 2 beta
* @since 2007-02-26 
*/
public class reMIND {
    
    /** Creates a new instance of Main */
    public reMIND() 
    {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
//new InnerThread();
        // TODO code application logic here
        GUI gui = GUI.getInstance();  // GUI is a singleton
        GUI.setStaticToolbar(gui.c_topToolbar);
       
    }
    
/*public static class InnerThread extends Thread
{
public InnerThread()
{
start();
}
public void run()
{
GUI gui = GUI.getInstance();  // GUI is a singleton
GUI.setStaticToolbar(gui.c_topToolbar);
}
}*/
    
}