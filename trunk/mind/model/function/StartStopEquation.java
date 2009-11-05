/*
 * StartStopEquation.java
 *Added by Nawzad Mardan 080530
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
 *
 *
 * Created on den 11 februari 2008, 15:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mind.model.function;
import java.util.Vector;
import java.util.LinkedList;
import mind.model.*;
import mind.io.*;

/**
 *
 * @author nawma77
 */
public class StartStopEquation  extends NodeFunction implements Cloneable
{
    
   
    
    /** Creates a new instance of StartStopEquation */
    public StartStopEquation() 
    {
        super(new ID(ID.FUNCTION), "StartStopEquation", null);
    }
    
  /**
     * Set method for c_percentageOfPreviousFlow
     * @param value to set
     */
    
    
    protected void timestepResetData(int size)
    {
	/*Create vector and reset to zero
	c_isMin = new Vector(size,1);
	c_isMax = new Vector(size,1);
	c_minimum = new Vector(size,1);
	c_maximum = new Vector(size,1);

	for(int i=0; i<size; i++) {
	    c_minimum.add(new Float(0));
	    c_maximum.add(new Float(0));
	    c_isMax.add(new Boolean(false));
	    c_isMin.add(new Boolean(false));
	}*/
    }
    protected void timestepSetLessDetailed(int newSize, int factor)
    {
    }
    protected void timestepSetMoreDetailed(int factor)
    {
    }
    protected void timestepRemoveAt(int index)
    {
        
    }
    
    protected void timestepInsertAt(int index)
    {
        
    }
    
    protected int getTimesteps()
    {
	return 10;
    }
    
     public String toXML(ResourceControl resources, int indent)
    {
        String sInd  = XML.indent(indent);
        String sInd1 = XML.indent(indent+1);
        String xml   = sInd + "<startStopEquation>" + XML.nl();

        if (getLabel() != null) {
            xml += sInd1 + "<label>" + XML.toXML(getLabel()) + "</label>" + XML.nl();
        }

       

        return xml;
    }
     /**
	 * Puts EXML data in the given ExmlSheet
	 * @param resources A ResourceControl
	 * @param sheet The ExmlSheet to be changed 
	 */
    public void toEXML(ResourceControl resources,ExmlSheet sheet) 
	{
		//Find Label
		String label = ((this.getLabel()==null)?"":this.getLabel());

		//Add function header
		sheet.addFunctionHeader("StartStopEquation", label);
		
		//Add Resource description
		//String resource = ((c_resource==null)?"":resources.getLabel(c_resource));
		//sheet.addRow(sheet.addLockedCell("Resource")+sheet.addCell(XML.toXML(resource)));
		
		
    }
    
    public boolean isRelatedToFlow(ID flow) 
    {
    return false;
    }
    
     public Object clone()
	throws CloneNotSupportedException
    {
	StartStopEquation clone = (StartStopEquation) super.clone();
        return clone;
     }
     
     
     /**
     * parseData parses a linked list with data and initializes the class
     * with the values found in the linked list.
     * The function is used when loading a model or node from disk.
     *
     * @param data A linked list with data.
     * @param rc A control with all available resources.
     * @param createMissingResources If this is true then
     * if data contains resources not found in rc, these resources
     * should be created and added to rc.
      */
    public void parseData(LinkedList data, ResourceControl rc, boolean createMissingResource)throws RmdParseException
    {
        c_timesteplevel = (Timesteplevel)data.removeLast();

        //Assumes the following optional data as pairs in any order:
        //label tag, label,
        //is "<choiceOne>", is choice flag,
        //percentageOfPreviousFlow value tag, percentageOfPreviousFlow value
        //thresholdValue value tag, thresholdValue value

        ID TmpRC = null;

       
    }// End method parseData(....)
    
     public EquationControl getEquationControl(int maxTimesteps, ID node, Vector toFlows, Vector fromFlows)
      throws ModelException
  {
      EquationControl control = new EquationControl();
     
      return control;
  }
    
    

}
