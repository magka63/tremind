/*
 * StartStopEquation.java
 *Added by Nawzad Mardan 090211
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
 * Created on den 11 februari 2009, 15:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mind.model.function;
import java.util.Vector;
import java.util.LinkedList;
import mind.model.*;
import mind.io.*;
import mind.gui.UserSettingConstants;
import mind.gui.GUI;

/**
 *
 * @author nawma77
 */
public class StartStopEquation  extends NodeFunction implements Cloneable,UserSettingConstants
{
    
    float c_percentageOfPreviousFlow, c_thresholdValue, c_percentageWasteValue;
    float c_startCost, c_stopCost, c_operateCost, c_operateCost3, c_operateCostAltFour;
    float c_fixedWasteValue, c_minimumFlowAltTwo, c_minimumFlowValue, c_minimumFlowAltFour;
    String c_outFlowValue;
    boolean c_choiceOne, c_choiceTwo, c_choiceThree, c_choiceFour, c_startCostChoice, c_stopCostChoice, 
            c_startCostofFirstTimestepChoice, c_stopCostofLastTimestepChoice;
    boolean c_startAltOne, c_stopAltOne, c_startFirstTimestepAltOne, c_stopLastTimestepAltOne;
    boolean c_startAltThree, c_stopAltThree, c_startFirstTimestepAltThree, c_stopLastTimestepAltThree;
    boolean c_startWasteChoice, c_stopWasteChoice, c_percentagevalueCheckBox,c_fixedvalueCheckBox,
            c_startWasteOfFirstTimestepChoice, c_stopWasteOfLastTimestepChoice;
    boolean c_radioLt, c_radioEq, c_radioGt, c_radioLtGt, c_batchProcess, c_continueProcess;
    private ID c_resource = null;
    private String c_operator = "";
    private int c_limit1, c_limit2;
    /** Creates a new instance of StartStopEquation */
    public StartStopEquation() 
    {
        super(new ID(ID.FUNCTION), "StartStopEquation", null);
    }
    
     /** 
     * Set method for c_percentageOfPreviousFlow
     * @param value to set
     */
    public void setPercentageOfPreviousFlow(float value)
    {
        c_percentageOfPreviousFlow = value;
    }
    
    /** 
     * Set method for c_percentageWasteValue
     * @param value to set
     */
    public void setWatsePercentageValue(float value)
    {
        c_percentageWasteValue = value;
    }
    
     /**
     * Get method for c_percentageWasteValue
     * @return c_percentageWasteValue
     */
    public float getWatsePercentageValue()
    {
        return c_percentageWasteValue;
    }
    
    /** 
     * Set method for c_fixedWasteValue
     * @param value to set
     */
    public void setWatseFixedValue(float value)
    {
        c_fixedWasteValue = value;
    }
    
     /**
     * Get method for c_fixedWasteValue
     * @return c_fixedWasteValue
     */
    public float getWasteFixedValue()
    {
        return c_fixedWasteValue;
    }
     
    /** 
     * Set method for c_fixedWasteValue
     * @param value to set
     */
    public void setOutFlow(String value)
    {
        c_outFlowValue = value;
    }
    
     /**
     * Get method for c_fixedWasteValue
     * @return c_fixedWasteValue
     */
    public String getOutFlow()
    {
        return c_outFlowValue;
    }
    
     /** 
     * Set method for c_minimumFlowAltTwo
     * @param value to set
     */
    public void setMinimumFlowAltTwo(float value)
    {
        c_minimumFlowAltTwo = value;
    }
    
     /**
     * Get method for c_minimumFlowAltTwo
     * @return c_minimumFlowAltTwo
     */
    public float getMinimumFlowAltTwo()
    {
        return c_minimumFlowAltTwo;
    }
    
    /** 
     * Set method for c_minimumFlowAltFour
     * @param value to set
     */
    public void setMinimumFlowAltFour(float value)
    {
        c_minimumFlowAltFour = value;
    }
    
     /**
     * Get method for c_minimumFlowAltFour
     * @return c_minimumFlowAltFour
     */
    public float getMinimumFlowAltFour()
    {
        return c_minimumFlowAltFour;
    }
     /** 
     * Set method for c_minimumFlowValue
     * @param value to set
     */
    public void setMinimumFlow(float value)
    {
        c_minimumFlowValue = value;
    }
    
     /**
     * Get method for c_minimumFlowValue
     * @return c_minimumFlowValue
     */
    public float getMinimumFlow()
    {
        return c_minimumFlowValue;
    }
    
    /**
     * Set method for c_percentageOfPreviousFlow for the choice three
     * @param value to set
     *
    public void setPercentageOfPreviousFlow3(float value)
    {
        c_percentageOfPreviousFlow3 = value;
    }*/
     /**
     * Set method for c_thresholdValue
     * @param value to set
     */
    public void setThresholdValue(float value)
    {
        c_thresholdValue = value;
    }
    
    /**
     * Set method for c_choiceOne
     * @param value to set
     */
    public void setChoiceOne(boolean value)
    {
      c_choiceOne = value;  
    }
    
    /**
     * Get method for c_percentageOfPreviousFlow
     * @return c_percentageOfPreviousFlow
     */
    public float getPercentageOfPreviousFlow()
    {
        return c_percentageOfPreviousFlow;
    }
    
    /**
     * Get method for c_percentageOfPreviousFlow
     * @return c_percentageOfPreviousFlow3 for the choice three
     *
    public float getPercentageOfPreviousFlow3()
    {
        return c_percentageOfPreviousFlow3;
    }*/
    /**
     * Get method for c_thresholdValue
     * @return c_thresholdValue
     */
    public float getThresholdValue()
    {
        return c_thresholdValue;
    }
    
     /**
     * Get method for c_choiceOne
     * @return c_choiceOne
     */
    public boolean getChoiceOne()
    {
      return c_choiceOne;  
    }
 
    /**
     * Set method for c_choiceTwo
     * @param value to set
     */
    public void setChoiceTwo(boolean value)
    {
      c_choiceTwo = value;  
    }

     /**
     * Get method for c_choiceTwo
     * @return c_choiceTwo
     */
    public boolean getChoiceTwo()
    {
      return c_choiceTwo;  
    }
    
    /**
     * Set method for c_choiceThree
     * @param value to set
     */
    public void setChoiceThree(boolean value)
    {
      c_choiceThree = value;  
    }

     /**
     * Get method for c_choiceThree
     * @return c_choiceThree
     */
    public boolean getChoiceThree()
    {
      return c_choiceThree;  
    }
    
   /**
     * Set method for c_choiceFour
     * @param value to set
     */
    public void setChoiceFour(boolean value)
    {
      c_choiceFour = value;  
    }

     /**
     * Get method for c_choiceFour
     * @return c_choiceFour
     */
    public boolean getChoiceFour()
    {
      return c_choiceFour;  
    }
    
    /**
     * Set method for c_startCostChoice
     * @param value to set
     */
    public void setStartCostChoice(boolean value)
    {
      c_startCostChoice = value;  
    }

     /**
     * Get method for c_startCostChoice
     * @return c_startCostChoice
     */
    public boolean getStartCostChoice()
    {
      return c_startCostChoice;  
    }
   
     /**
     * Get method for c_percentagevalueCheckBox
     * @return c_percentagevalueCheckBox
     */
    public boolean getPercentagevalueChoice()
    {
      return c_percentagevalueCheckBox;  
    }
    
    /**
     * Set method for c_percentagevalueCheckBox
     * @param value to set
     */
    public void setPercentagevalueChoice(boolean value)
    {
      c_percentagevalueCheckBox = value;  
    }
    
    /**
     * Get method for c_fixedvalueCheckBox
     * @return c_fixedvalueCheckBox
     */
    public boolean getFixedvalueChoice()
    {
      return c_fixedvalueCheckBox;  
    }
    
    /**
     * Set method for c_fixedvalueCheckBox
     * @param value to set
     */
    public void setFixedvalueChoice(boolean value)
    {
      c_fixedvalueCheckBox = value;  
    }
    
    
    /**
     * Set method for c_startWasteChoice
     * @param value to set
     */
    public void setStartWasteChoice(boolean value)
    {
      c_startWasteChoice = value;  
    }

     /**
     * Get method for c_startWasteChoice
     * @return c_startWasteChoice
     */
    public boolean getStartWasteChoice()
    {
      return c_startWasteChoice;  
    }
    
    /**
     * Get method for c_startCostofFirstTimestepChoice
     * @return c_startCostofFirstTimestepChoice
     */
    public boolean getStartCostofFirstTimestepChoice()
    {
        return c_startCostofFirstTimestepChoice;
    }
    
    /**
     * Set method for c_startCostofFirstTimestepChoice
     * @param value to set
     */
    public void setStartCostofFirstTimestepChoice(boolean value)
    {
      c_startCostofFirstTimestepChoice = value;  
    }
     /**
     * Get method for c_startWasteOfFirstTimestepChoice
     * @return c_startWasteOfFirstTimestepChoice
     */
    public boolean getStartWasteOfFirstTimestepChoice()
    {
        return c_startWasteOfFirstTimestepChoice;
    }
    
    /**
     * Set method for c_startWasteOfFirstTimestepChoice
     * @param value to set
     */
    public void setStartWasteOfFirstTimestepChoice(boolean value)
    {
      c_startWasteOfFirstTimestepChoice = value;  
    }
    /**
     * Get method for c_stopCostofLastTimestepChoice
     * @return c_stopCostofLastTimestepChoice
     */
    public boolean getStopCostofFirstTimestepChoice()
    {
        return c_stopCostofLastTimestepChoice;
    }
    
    /**
     * Set method for c_stopCostofLastTimestepChoice
     * @param value to set
     */
    public void setStopCostofFirstTimestepChoice(boolean value)
    {
      c_stopCostofLastTimestepChoice = value;  
    }
    
    /**
     * Get method for c_stopWasteOfLastTimestepChoice
     * @return c_stopWasteOfLastTimestepChoice
     */
     public boolean getStopWasteOfLastTimestepChoice()
    {               
        return c_stopWasteOfLastTimestepChoice;
    }
    
    /**
     * Set method for c_stopWasteOfLastTimestepChoice
     * @param value to set
     */
    public void setStopWasteOfLastTimestepChoice(boolean value)
    {
      c_stopWasteOfLastTimestepChoice = value;  
    }
      /**
     * Set method for c_stopCostChoice
     * @param value to set
     */
    public void setStopCostChoice(boolean value)
    {
      c_stopCostChoice = value;  
    }

     /**
     * Get method for c_stopCostChoice
     * @return c_stopCostChoice
     */
    public boolean getStopCostChoice()
    {
      return c_stopCostChoice;  
    }
    
    /**
     * Set method for c_stopWasteChoice
     * @param value to set
     */
    public void setStopWasteChoice(boolean value)
    {
      c_stopWasteChoice = value;  
    }

     /**
     * Get method for c_stopWasteChoice
     * @return c_stopWasteChoice
     */
    public boolean getStopWasteChoice()
    {
      return c_stopWasteChoice;  
    }
    
     /**
     * Set method for c_startAltOne
     * @param value to set
     */
    public void setStartAltOneCB(boolean value)
    {
      c_startAltOne = value;  
    }

     /**
     * Get method for c_startAltOne
     * @return c_startAltOne
     */
    public boolean getStartAltOneCB()
    {
      return c_startAltOne;  
    }
    
   /**
     * Set method for c_stopAltOne
     * @param value to set
   */
    
    public void setStopAltOneCB(boolean value)
    {
      c_stopAltOne = value;  
    }

     /**
     * Get method for c_stopAltOne
     * @return c_stopAltOne
     */
    public boolean getStopAltOneCB()
    {
      return c_stopAltOne;  
    }

    /**
     * Set method for c_startFirstTimestepAltOne
     * @param value to set
   */
    
    public void setStartFirstTimestepAltOneCB(boolean value)
    {
      c_startFirstTimestepAltOne = value;  
    }

     /**
     * Get method for c_startFirstTimestepAltOne
     * @return c_startFirstTimestepAltOne
     */
    public boolean getStartFirstTimestepAltOneCB()
    {
      return c_startFirstTimestepAltOne;  
    }

    /**
     * Set method for c_stopLastTimestepAltOne
     * @param value to set
   */
    
    public void setStopLastTimestepAltOneCB(boolean value)
    {
      c_stopLastTimestepAltOne = value;  
    }

     /**
     * Get method for c_stopLastTimestepAltOne
     * @return c_stopLastTimestepAltOne
     */
    public boolean getStopLastTimestepAltOneCB()
    {
      return c_stopLastTimestepAltOne;  
    }
    
     /**
     * Set method for c_startAltThree
     * @param value to set
     */
    public void setStartAltThreeCB(boolean value)
    {
      c_startAltThree = value;  
    }

     /**
     * Get method for c_startAltThree
     * @return c_startAltThree
     */
    public boolean getStartAltThreeCB()
    {
      return c_startAltThree;  
    }
    
   /**
     * Set method for c_stopAltThree
     * @param value to set
   */
    
    public void setStopAltThreeCB(boolean value)
    {
      c_stopAltThree = value;  
    }

     /**
     * Get method for c_stopAltThree
     * @return c_stopAltThree
     */
    public boolean getStopAltThreeCB()
    {
      return c_stopAltThree;  
    }

    /**
     * Set method for c_startFirstTimestepAltThree
     * @param value to set
   */
    
    public void setStartFirstTimestepAltThreeCB(boolean value)
    {
      c_startFirstTimestepAltThree = value;  
    }

     /**
     * Get method for c_startFirstTimestepAltThree
     * @return c_startFirstTimestepAltThree
     */
    public boolean getStartFirstTimestepAltThreeCB()
    {
      return c_startFirstTimestepAltThree;  
    }

    /**
     * Set method for c_stopLastTimestepAltThree
     * @param value to set
   */
    
    public void setStopLastTimestepAltThreeCB(boolean value)
    {
      c_stopLastTimestepAltThree = value;  
    }

     /**
     * Get method for c_stopLastTimestepAltThree
     * @return c_stopLastTimestepAltThree
     */
    public boolean getStopLastTimestepAltThreeCB()
    {
      return c_stopLastTimestepAltThree;  
    }
    
    /**
     * Set method for c_startCost
     * @param value to set
     */
    public void setStartCostValue(float value)
    {
      c_startCost = value;  
    }
    
    /**
     * Get method for c_startCost
     * @return c_startCost
     */
    public float getStartCostValue()
    {
        return c_startCost;
    }
    
    /**
     * Set method for c_stopCost
     * @param value to set
     */
    public void setStopCostValue(float value)
    {
      c_stopCost = value;  
    }
    
    /**
     * Get method for c_stopCost
     * @return c_stopCost
     */
    public float getStopCostValue()
    {
        return c_stopCost;
    }
    
     /**
     * Set method for c_operateCost
     * @param value to set
     */
    public void setOperateCostValue(float value)
    {
      c_operateCost = value;  
    }
    
    /**
     * Get method for c_operateCost
     * @return c_operateCost
     */
    public float getOperateCostValue()
    {
        return c_operateCost;
    }
    
    /**
     * Set method for c_operateCost
     * @param value to set (Choice nr.3)
     */
    public void setOperateCostValue3(float value)
    {
      c_operateCost3 = value;  
    }
    
    /**
     * Get method for c_operateCost3 (Choice nr.3)
     * @return c_operateCost
     */
    public float getOperateCostValue3()
    {
        return c_operateCost3;
    }
    
     /**
     * Set method for c_operateCostAltFour
     * @param value to set (Choice nr.4)
     */
    public void setOperateCostAltFour(float value)
    {
      c_operateCostAltFour = value;  
    }
    
    /**
     * Get method for c_operateCostAltFour
     * @return c_operateCostAltFour
     */          
    public float getOperateCostAltFour()
    {
        return c_operateCostAltFour;
    }
    
    /**
     * Gets the resource this StartStop limits.
     */
    public ID getResource()
    {
	return c_resource;
    }

  /**
     * Set the StartStop resource.
     * @param newResource The new resource for this Boundary.
     */
    public void setResource(ID newResource)
    {
	c_resource = newResource;
    }

    /**
	 * Sets the operator for the current resource
	 * 
	 * @param operator
	 *            The new operator
	 */

        public void setOperator(String operator) {
		c_operator = operator;
	}


/**
	 * Returns the operator for the current flow and timestep
	 */
	public String getOperator() {
		return c_operator;
	}
/**
	 * Returns the limit1 for the given resource
	 * 
	 */
	public int getLimit1() {
		
		return c_limit1;
	}
/**
	 * Returns the limit2 for the given resource
	 * 
	 */
	public int getLimit2() {
		
		return c_limit2;
	}

/**
	 * Set the limit 1 for the current resource
	 * 
	 * @param limit
	 *            The new limit
	 */
	public void setLimit1(int limit) {
		c_limit1 = limit;
	}

/**
	 * Set the limit 2 for the current resource
	 * 
	 * @param limit
	 *            The new limit
	 */
	public void setLimit2(int limit) {
		c_limit2 = limit;
	}


 /**
     * Set method for c_radioLt
     * @param value to set
     */
    public void setRadioButtonLt(boolean value)
    {
      c_radioLt = value;  
    }

     /**
     * Get method for c_radioLt
     * @return c_radioLt
     */
    public boolean getRadioButtonLt()
    {
      return c_radioLt;  
    }
    
    /**
     * Set method for c_radioEq
     * @param value to set
     */
    public void setRadioButtonEq(boolean value)
    {
      c_radioEq = value;  
    }

     /**
     * Get method for c_radioEq
     * @return c_radioEq
     */
    public boolean getRadioButtonEq()
    {
      return c_radioEq;  
    }
    
    /**
     * Set method for c_radioGt
     * @param value to set
     */
    public void setRadioButtonGt(boolean value)
    {
      c_radioGt = value;  
    }

     /**
     * Get method for c_radioGt
     * @return c_radioGt
     */
    public boolean getRadioButtonGt()
    {
      return c_radioGt;  
    }
    
    /**
     * Set method for c_radioLtGt
     * @param value to set
     */
    public void setRadioButtonLtGt(boolean value)
    {
      c_radioLtGt = value;  
    }

     /**
     * Get method for c_radioLtGt
     * @return c_radioLtGt
     */
    public boolean getRadioButtonLtGt()
    {
      return c_radioLtGt;  
    }

    /**
     * Set method for c_batchProcess
     * @param value to set
     */
    public void setBatchWestProcess(boolean value)
    {
      c_batchProcess = value;
    }

     /**
     * Get method for c_batchProcess
     * @return c_batchProcess
     */
    public boolean getBatchWestProcess()
    {
      return c_batchProcess;
    }

     /**
     * Set method for c_continueProcess
     * @param value to set
     */
    public void setContinuousWestProcess(boolean value)
    {
      c_continueProcess = value;
    }

     /**
     * Get method for c_continueProcess
     * @return c_continueProcess
     */
    public boolean getContinuousWestProcess()
    {
      return c_continueProcess;
    }
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

        if (c_resource != null) {
            xml += sInd1 + "<resource.type>" + XML.toXML(resources.getLabel(c_resource));
            xml += "</resource.type>" + XML.nl();
        }
//label, choiceOne?, choiceTwo?, percentageOfPreviousFlow?, thresholdValue?, startCostChoice?, stopCostChoice?, startCost?, stopCost?, operateCost?
        if(c_choiceOne)
            xml += sInd1 + "<choiceOne>"  + (c_choiceOne ? "true" : "false") + "</choiceOne>"  + XML.nl();
        if(c_choiceTwo)
            xml += sInd1 + "<choiceTwo>"  + (c_choiceTwo ? "true" : "false") + "</choiceTwo>"  + XML.nl();
        if(c_choiceThree)
            xml += sInd1 + "<choiceThree>"  + (c_choiceThree ? "true" : "false") + "</choiceThree>"  + XML.nl(); 
        if(c_choiceFour)
            xml += sInd1 + "<choiceFour>"  + (c_choiceFour ? "true" : "false") + "</choiceFour>"  + XML.nl(); 
        //xml += sInd1 + "<isMax>"  + (c_isMaximum ? "true" : "false") + "</isMax>"  + XML.nl();
        //c_startAltOne, c_stopAltOne, c_startFirstTimestepAltOne, c_stopLastTimestepAltOne;
         if(c_choiceOne)
         {
            if(c_startAltOne)
                xml += sInd1 + "<startAltOne>" + (c_startAltOne ? "true" : "false") + "</startAltOne>" + XML.nl();
            if(c_stopAltOne)
                xml += sInd1 + "<stopAltOne>" + (c_stopAltOne ? "true" : "false") + "</stopAltOne>" + XML.nl();
            if(c_startFirstTimestepAltOne)
                xml += sInd1 + "<startFirstTimestepAltOne>" + (c_startFirstTimestepAltOne ? "true" : "false") + "</startFirstTimestepAltOne>" + XML.nl();
            if(c_stopLastTimestepAltOne)
                xml += sInd1 + "<stopLastTimestepAltOne>" + (c_stopLastTimestepAltOne ? "true" : "false") + "</stopLastTimestepAltOne>" + XML.nl();
            xml += sInd1 + "<percentageOfPreviousFlow>" + Float.toString(c_percentageOfPreviousFlow) + "</percentageOfPreviousFlow>" + XML.nl();
            xml += sInd1 + "<thresholdValue>" + Float.toString(c_thresholdValue)        + "</thresholdValue>" + XML.nl();
         }
        if(c_choiceTwo)
        {
        if(c_startCostChoice)
            xml += sInd1 + "<startCostChoice>"  + (c_startCostChoice ? "true" : "false") + "</startCostChoice>"  + XML.nl();
        if(c_stopCostChoice)
            xml += sInd1 + "<stopCostChoice>"  + (c_stopCostChoice ? "true" : "false") + "</stopCostChoice>"  + XML.nl();
        if(c_startCostofFirstTimestepChoice)
            xml += sInd1 + "<firstStartCostChoice>"  + (c_startCostofFirstTimestepChoice ? "true" : "false") + "</firstStartCostChoice>"  + XML.nl();
        if(c_stopCostofLastTimestepChoice)
            xml += sInd1 + "<firstStopCostChoice>"  + (c_stopCostofLastTimestepChoice ? "true" : "false") + "</firstStopCostChoice>"  + XML.nl();
        xml += sInd1 + "<startCost>" + Float.toString(c_startCost)        + "</startCost>" + XML.nl();
        xml += sInd1 + "<stopCost>" + Float.toString(c_stopCost)        + "</stopCost>" + XML.nl();
        xml += sInd1 + "<operateCost>" + Float.toString(c_operateCost)        + "</operateCost>" + XML.nl();
        xml += sInd1 + "<miniFlowAltTwo>" + Float.toString(c_minimumFlowAltTwo)  + "</miniFlowAltTwo>" + XML.nl();    
        }
//        xml += sInd1 + "<percentageOfPreviousFlow3>" + Float.toString(c_percentageOfPreviousFlow3) + "</percentageOfPreviousFlow3>" + XML.nl();
        //c_startAltThree, c_stopAltThree, c_startFirstTimestepAltThree, c_stopLastTimestepAltThree;
        if(c_choiceThree)
        {
        if(c_startAltThree)
            xml += sInd1 + "<startAltThree>" + (c_startAltThree ? "true" : "false") + "</startAltThree>" + XML.nl();
        if(c_stopAltThree)
            xml += sInd1 + "<stopAltThree>" + (c_stopAltThree ? "true" : "false") + "</stopAltThree>" + XML.nl();
        if(c_startFirstTimestepAltThree)
            xml += sInd1 + "<startFirstTimestepAltThree>" + (c_startFirstTimestepAltThree ? "true" : "false") + "</startFirstTimestepAltThree>" + XML.nl();
        if(c_stopLastTimestepAltThree)
            xml += sInd1 + "<stopLastTimestepAltThree>" + (c_stopLastTimestepAltThree ? "true" : "false") + "</stopLastTimestepAltThree>" + XML.nl();
        xml += sInd1 + "<operateCost3>" + Float.toString(c_operateCost3)        + "</operateCost3>" + XML.nl();
        xml += sInd1 + "<minimumFlow>" + Float.toString(c_minimumFlowValue)        + "</minimumFlow>" + XML.nl();
        xml += sInd1 + "<operator3>" + c_operator + "</operator3>" + XML.nl();
        xml += sInd1 + "<lim1>" + c_limit1 + "</lim1>" + XML.nl();
        xml += sInd1 + "<lim2>" + c_limit2 + "</lim2>" + XML.nl();
        }
        //c_operateCostAltFour, c_minimumFlowAltFour, operateCostAltFour, miniFlowAltFour;
        // Choice 4
        if(c_choiceFour)
        {
         //c_continueProcess
        if(c_continueProcess)
            xml += sInd1 + "<continueProcess>"  + (c_continueProcess ? "true" : "false") + "</continueProcess>"  + XML.nl();
        //c_batchProcess
       if(c_batchProcess)
            xml += sInd1 + "<batchProcess>"  + (c_batchProcess ? "true" : "false") + "</batchProcess>"  + XML.nl();

        if(c_startWasteChoice)
            xml += sInd1 + "<startWasteChoice>"  + (c_startWasteChoice ? "true" : "false") + "</startWasteChoice>"  + XML.nl();
        if(c_stopWasteChoice)
            xml += sInd1 + "<stopWasteChoice>"  + (c_stopWasteChoice ? "true" : "false") + "</stopWasteChoice>"  + XML.nl();
        if(c_startWasteOfFirstTimestepChoice)
            xml += sInd1 + "<startWasteOfFirstTimestepChoice>"  + (c_startWasteOfFirstTimestepChoice ? "true" : "false") + "</startWasteOfFirstTimestepChoice>"  + XML.nl();
        if(c_stopWasteOfLastTimestepChoice)
            xml += sInd1 + "<stopWasteOfLastTimestepChoice>"  + (c_stopWasteOfLastTimestepChoice ? "true" : "false") + "</stopWasteOfLastTimestepChoice>"  + XML.nl();
        if(c_percentagevalueCheckBox)
            xml += sInd1 + "<percentageWasteValue>" + Float.toString(c_percentageWasteValue) + "</percentageWasteValue>" + XML.nl(); 
        if(c_fixedvalueCheckBox)
            xml += sInd1 + "<fixedWasteValue>" + Float.toString(c_fixedWasteValue) + "</fixedWasteValue>" + XML.nl(); 
       
        xml += sInd1 + "<operateCostAltFour>" + Float.toString(c_operateCostAltFour)        + "</operateCostAltFour>" + XML.nl();
        xml += sInd1 + "<miniFlowAltFour>" + Float.toString(c_minimumFlowAltFour)  + "</miniFlowAltFour>" + XML.nl(); 
        xml += sInd1 + "<outFlowValue>" + c_outFlowValue + "</outFlowValue>" + XML.nl();
        }
        xml += sInd + "</startStopEquation>" + XML.nl();

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
		String resource = ((c_resource==null)?"":resources.getLabel(c_resource));
		sheet.addRow(sheet.addLockedCell("Resource")+sheet.addCell(XML.toXML(resource)));
		
		//Add data
                if(c_choiceOne)
                {
                sheet.addRow(sheet.addCell(""));
                //sheet.addFunctionHeader("Alternative one", label);
                sheet.addRow(sheet.addBoldCell("Alternative one"));
                if(c_startAltOne)
                    sheet.addRow(sheet.addLockedCell("Start up")+sheet.addCell("Yes")); 
            //xml += sInd1 + "<startAltOne>" + (c_startAltOne ? "true" : "false") + "</startAltOne>" + XML.nl();
                if(c_startFirstTimestepAltOne)
                    sheet.addRow(sheet.addLockedCell("Start up within the analysis period")+sheet.addCell("Yes")); 
                //xml += sInd1 + "<startFirstTimestepAltOne>" + (c_startFirstTimestepAltOne ? "true" : "false") + "</startFirstTimestepAltOne>" + XML.nl();
                if(c_stopAltOne)
                    sheet.addRow(sheet.addLockedCell("Shut down")+sheet.addCell("Yes")); 
            //xml += sInd1 + "<stopAltOne>" + (c_stopAltOne ? "true" : "false") + "</stopAltOne>" + XML.nl();
                if(c_stopLastTimestepAltOne)
                    sheet.addRow(sheet.addLockedCell("Shut down within the analysis period")+sheet.addCell("Yes"));
            //xml += sInd1 + "<stopLastTimestepAltOne>" + (c_stopLastTimestepAltOne ? "true" : "false") + "</stopLastTimestepAltOne>" + XML.nl();
		sheet.addRow(sheet.addLockedCell("Percentage of previous flow")+sheet.addCell(c_percentageOfPreviousFlow));
		sheet.addRow(sheet.addLockedCell("Threshold value")+sheet.addCell(c_thresholdValue));
		sheet.addRow(sheet.addCell("================================================================================================"));
                }
                
                if(c_choiceTwo)
                {
                sheet.addRow(sheet.addBoldCell("Alternative two"));
                if(c_startCostofFirstTimestepChoice)
                 sheet.addRow(sheet.addLockedCell("Start cost for the first time step:")+sheet.addCell("Yes"));   
                 //sheet.addRow(sheet.addBoldCell("The model have a start cost for the first time step"));   
                sheet.addRow(sheet.addLockedCell("Start cost value")+sheet.addCell(c_startCost));
                if(c_stopCostofLastTimestepChoice)
                 sheet.addRow(sheet.addLockedCell("Stop cost for the last time step:")+sheet.addCell("Yes"));    
                sheet.addRow(sheet.addLockedCell("Stop cost value")+sheet.addCell(c_stopCost));
                sheet.addRow(sheet.addLockedCell("Operate value")+sheet.addCell(c_operateCost));
                sheet.addRow(sheet.addLockedCell("Minimum flow")+sheet.addCell(c_minimumFlowAltTwo));
                sheet.addRow(sheet.addCell("================================================================================================"));
                
                }
                
                if(c_choiceThree)
                {
                 sheet.addRow(sheet.addBoldCell("Alternative three"));
                // sheet.addRow(sheet.addLockedCell("Percentage Of Previous Flow")+sheet.addCell(c_percentageOfPreviousFlow3));
                //c_startAltThree, c_stopAltThree, c_startFirstTimestepAltThree, c_stopLastTimestepAltThree;
                 if(c_startAltThree)
                    sheet.addRow(sheet.addLockedCell("Start")+sheet.addCell("Yes")); 
                 if(c_stopAltThree)
                    sheet.addRow(sheet.addLockedCell("Stop")+sheet.addCell("Yes")); 
                 if(c_startFirstTimestepAltThree)
                    sheet.addRow(sheet.addLockedCell("First time step is included")+sheet.addCell("Yes")); 
                 if(c_stopLastTimestepAltThree)
                    sheet.addRow(sheet.addLockedCell("Last time step is included")+sheet.addCell("Yes"));  
                 sheet.addRow(sheet.addLockedCell("Operate Cost Value")+sheet.addCell(c_operateCost3));
                 sheet.addRow(sheet.addLockedCell("Minimum Flow Value")+sheet.addCell(c_minimumFlowValue));  
                 sheet.addRow(sheet.addLockedCell("Operator")+sheet.addCell(getOperator()));
                 if(getOperator().equals("LESS_GREATER"))
                    {
                     sheet.addRow(sheet.addLockedCell("Min")+sheet.addCell(getLimit1()));
                     sheet.addRow(sheet.addLockedCell("Max")+sheet.addCell(getLimit2()));
                    }
                 else if(getOperator().equals("LESS"))
                    {
                     sheet.addRow(sheet.addLockedCell("R <")+sheet.addCell(getLimit2()));
                    }
                 else if(getOperator().equals("GREATER"))
                    {
                     sheet.addRow(sheet.addLockedCell("R >")+sheet.addCell(getLimit2()));
                    }
                 else if(getOperator().equals("EQUAL"))
                    {
                     sheet.addRow(sheet.addLockedCell("R =")+sheet.addCell(getLimit2()));
                    }
                 
                sheet.addRow(sheet.addCell("================================================================================================"));
                  
                }
                
                //....................................
                if(c_choiceFour)
                {
                 sheet.addRow(sheet.addBoldCell("Alternative four"));
                 
                 if(c_startWasteChoice)
                     sheet.addRow(sheet.addLockedCell("Start west")+sheet.addLockedCell("Yes"));   
                 if(c_stopWasteChoice)
                     sheet.addRow(sheet.addLockedCell("Stop west")+sheet.addLockedCell("Yes"));
                 if(c_startWasteOfFirstTimestepChoice)
                     sheet.addRow(sheet.addLockedCell("Start west for the first time step")+sheet.addLockedCell("Yes"));
                 if(c_stopWasteOfLastTimestepChoice)
                     sheet.addRow(sheet.addLockedCell("Stop west for the last time step")+sheet.addLockedCell("Yes"));
                 if(c_percentagevalueCheckBox)
                    sheet.addRow(sheet.addLockedCell("Percentage value of the waste")+sheet.addCell(c_percentageWasteValue));
                 if(c_fixedvalueCheckBox)
                    sheet.addRow(sheet.addLockedCell("Fixed value of the waste")+sheet.addCell(c_fixedWasteValue));
                 //c_operateCostAltFour, c_minimumFlowAltFour, operateCostAltFour, miniFlowAltFour, Operate cost, Minimum flow value;
                 sheet.addRow(sheet.addLockedCell("Operate cost")+sheet.addCell(c_operateCostAltFour));
                 sheet.addRow(sheet.addLockedCell("Minimum flow value")+sheet.addCell(c_minimumFlowAltFour));
                 sheet.addRow(sheet.addLockedCell("Out flow")+sheet.addLockedCell(c_outFlowValue));
                 sheet.addRow(sheet.addCell("================================================================================================"));
                 
                }
                
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

        while (data.size() > 0) 
        {
            String tag   = (String)data.removeFirst();
            String value = "";
            if(data.size() > 0) value = (String)data.removeFirst();

            if(tag.equals("label")) setLabel(value);
            if (tag.equals("resource.type")) 
            {
	    //String resource = "";
	    //resource = (String)data.removeFirst();
	    c_resource = rc.getResourceID(value);
	    //Check if the resource exists
	    /*if (c_resource == null) {
		if (createMissingResource)
		    c_resource = rc.addResource(resource,"",""); //we do not know
		                                                 //the unit or prefix
		else
		    throw new RmdParseException("The resource '" +
						resource + "' is not defined.");
	    }*/
	     }

            if(tag.equals("choiceOne")) c_choiceOne = value.equals("true") ? true : false;
            
            if(tag.equals("choiceTwo")) c_choiceTwo = value.equals("true") ? true : false;
            
            if(tag.equals("choiceThree")) c_choiceThree = value.equals("true") ? true : false;
             
            if(tag.equals("choiceFour")) c_choiceFour = value.equals("true") ? true : false;
            //c_startAltOne, c_stopAltOne, c_startFirstTimestepAltOne, c_stopLastTimestepAltOne;
            if(tag.equals("startAltOne")) c_startAltOne = value.equals("true") ? true : false;
            
            if(tag.equals("stopAltOne")) c_stopAltOne = value.equals("true") ? true : false;
            
            if(tag.equals("startFirstTimestepAltOne")) c_startFirstTimestepAltOne = value.equals("true") ? true : false;
            
            if(tag.equals("stopLastTimestepAltOne")) c_stopLastTimestepAltOne = value.equals("true") ? true : false;
            
            if(tag.equals("percentageOfPreviousFlow")) 
            {
                try 
                {
                    c_percentageOfPreviousFlow = Float.parseFloat(value);
                } 
                catch (NumberFormatException e) 
                {
                    throw new RmdParseException("The 'min' field must be a float > 0");
                }
                if (c_percentageOfPreviousFlow < 0) 
                {
                    throw new RmdParseException("The 'min' field must be a float > 0");
                }
            }

            if (tag.equals("thresholdValue")) 
            {
                try 
                {
                    c_thresholdValue = Float.parseFloat(value);
                } 
                catch (NumberFormatException e) 
                {
                    throw new RmdParseException("The 'max' field must be a float > 0");
                }
                if (c_thresholdValue < 0) 
                {
                    throw new RmdParseException("The 'max' field must be a float > 0");
                }
            }
            
            //<startCostChoice>"  + (c_startCostChoice ? "true" : "false") + "</startCostChoice>"
            if(tag.equals("startCostChoice")) c_startCostChoice = value.equals("true") ? true : false;
            
            //<stopCostChoice>"  + (c_stopCostChoice ? "true" : "false") + "</stopCostChoice>
            if(tag.equals("stopCostChoice")) c_stopCostChoice = value.equals("true") ? true : false;
            
            //"<firstStartCostChoice>"  + (c_startCostofFirstTimestepChoice ? "true" : "false") + "</firstStartCostChoice>"
            if(tag.equals("firstStartCostChoice")) c_startCostofFirstTimestepChoice = value.equals("true") ? true : false;
            
            //"<firstStopCostChoice>"  + (c_stopCostofLastTimestepChoice ? "true" : "false") + "</firstStopCostChoice>"
            if(tag.equals("firstStopCostChoice")) c_stopCostofLastTimestepChoice = value.equals("true") ? true : false;
            
            if (tag.equals("startCost")) 
            {
                try 
                {
                    c_startCost = Float.parseFloat(value);
                } 
                catch (NumberFormatException e) 
                {
                    throw new RmdParseException("The 'Start cost' field must be a float > 0");
                }
                if (c_startCost < 0) 
                {
                    throw new RmdParseException("The 'Start cost' field must be a float > 0");
                }
            }
            
            // <stopCost>" + Float.toString(c_stopCost)        + "</stopCost>"
            if (tag.equals("stopCost")) 
            {
                try 
                {
                    c_stopCost = Float.parseFloat(value);
                } 
                catch (NumberFormatException e) 
                {
                    throw new RmdParseException("The 'Stop cost' field must be a float > 0");
                }
                if (c_stopCost < 0) 
                {
                    throw new RmdParseException("The 'Stop cost' field must be a float > 0");
                }
            }
            
           // <operateCost>" + Float.toString(c_operateCost)        + "</operateCost>
            if (tag.equals("operateCost")) 
            {
                try 
                {
                    c_operateCost = Float.parseFloat(value);
                } 
                catch (NumberFormatException e) 
                {
                    throw new RmdParseException("The 'Operate cost' field must be a float > 0");
                }
                if (c_operateCost < 0) 
                {
                    throw new RmdParseException("The 'Operate cost' field must be a float > 0");
                }
            }
           // "<miniFlowAltTwo>" + Float.toString(c_minimumFlowAltTwo) 
           if (tag.equals("miniFlowAltTwo")) 
            {
                try 
                {
                    c_minimumFlowAltTwo = Float.parseFloat(value);
                } 
                catch (NumberFormatException e) 
                {
                    throw new RmdParseException("The 'minimum flow' field must be a float > 0");
                }
               /* if (c_minimumFlowAltTwo < 0) 
                {
                    throw new RmdParseException("The 'Minimum flow' field must be a float > 0");
                }*/
            }
            //startAltThree, stopAltThree, startFirstTimestepAltThree, stopLastTimestepAltThree;
            
            if(tag.equals("startAltThree")) c_startAltThree = value.equals("true") ? true : false;
            
            if(tag.equals("stopAltThree")) c_stopAltThree = value.equals("true") ? true : false;
            
            if(tag.equals("startFirstTimestepAltThree")) c_startFirstTimestepAltThree = value.equals("true") ? true : false;
            
            if(tag.equals("stopLastTimestepAltThree")) c_stopLastTimestepAltThree = value.equals("true") ? true : false;
            
            if (tag.equals("operateCost3")) 
            {
                try 
                {
                    c_operateCost3 = Float.parseFloat(value);
                } 
                catch (NumberFormatException e) 
                {
                    throw new RmdParseException("The 'Operate cost' field must be a float > 0");
                }
                if (c_operateCost3 < 0) 
                {
                    throw new RmdParseException("The 'Operate cost' field must be a float > 0");
                }
            }
            if (tag.equals("minimumFlow")) 
            {
                try 
                {
                    c_minimumFlowValue = Float.parseFloat(value);
                } 
                catch (NumberFormatException e) 
                {
                    throw new RmdParseException("The 'minimum flow value' field must be a float > 0");
                }
                if (c_operateCost3 < 0) 
                {
                    throw new RmdParseException("The 'minimum flow value' field must be a float > 0");
                }
            }
            if (tag.equals("operator3")) 
            {
               c_operator = value;
               if(c_operator.equals("LESS_GREATER"))
                   setRadioButtonLtGt(true);
               else if(c_operator.equals("LESS"))
                   setRadioButtonLt(true);
               else if(c_operator.equals("GREATER"))
                   setRadioButtonGt(true);
               else if(c_operator.equals("EQUAL"))
                   setRadioButtonEq(true);
               
            }
            
            
            if (tag.equals("lim1")) 
            {
                try 
                {
                    Float ln = new Float(Float.parseFloat(value));
                    c_limit1 = ln.intValue();
                } 
                catch (NumberFormatException e) 
                {
                    throw new RmdParseException("The 'Limit1' field must be a float > 0");
                }
                if (c_limit1 < 0) 
                {
                    throw new RmdParseException("The 'Limit1' field must be a float > 0");
                }
            }
            
            if (tag.equals("lim2")) 
            {
                try 
                {
                    Float ln = new Float(Float.parseFloat(value));
                    c_limit2 = ln.intValue();
                } 
                catch (NumberFormatException e) 
                {
                    throw new RmdParseException("The 'Limit2' field must be a float > 0");
                }
                if (c_limit2 < 0) 
                {
                    throw new RmdParseException("The 'Limit2' field must be a float > 0");
                }
            }
            
            // .................................................................
            // ALT 4

            if(tag.equals("continueProcess")) c_continueProcess = value.equals("true") ? true : false;

            if(tag.equals("batchProcess")) c_batchProcess = value.equals("true") ? true : false;

            if(tag.equals("startWasteChoice")) c_startWasteChoice = value.equals("true") ? true : false;
            
            if(tag.equals("stopWasteChoice")) c_stopWasteChoice = value.equals("true") ? true : false;
            
            if(tag.equals("startWasteOfFirstTimestepChoice")) c_startWasteOfFirstTimestepChoice = value.equals("true") ? true : false;
            
            if(tag.equals("stopWasteOfLastTimestepChoice")) c_stopWasteOfLastTimestepChoice = value.equals("true") ? true : false;
            
            if (tag.equals("percentageWasteValue")) 
            {
                try 
                {
                    c_percentageWasteValue = Float.parseFloat(value);
                    c_percentagevalueCheckBox = true;
                    c_fixedvalueCheckBox = false;
                } 
                catch (NumberFormatException e) 
                {
                    throw new RmdParseException("The 'percentage waste value' field must be a float > 0");
                }
                if (c_percentageWasteValue < 0) 
                {
                    throw new RmdParseException("The 'percentage waste value' field must be a float > 0");
                }
            }
            
            if (tag.equals("fixedWasteValue")) 
            {
                try 
                {
                    c_fixedWasteValue = Float.parseFloat(value);
                    c_fixedvalueCheckBox = true;
                    c_percentagevalueCheckBox = false;
                } 
                catch (NumberFormatException e) 
                {
                    throw new RmdParseException("The 'fixed waste value' field must be a float > 0");
                }
                if (c_fixedWasteValue < 0) 
                {
                    throw new RmdParseException("The 'fixed waste value' field must be a float > 0");
                }
            }
            
             //operateCostAltFour, miniFlowAltFour
            else  if (tag.equals("operateCostAltFour")) 
            {
                try 
                {
                    c_operateCostAltFour = Float.parseFloat(value);
                } 
                catch (Exception e) 
                {
                    throw new RmdParseException("The 'operate cost' field must be specified");
                }
            }
            else if (tag.equals("miniFlowAltFour")) 
            {
                try 
                {
                    c_minimumFlowAltFour = Float.parseFloat(value);
                } 
                catch (Exception e) 
                {
                    throw new RmdParseException("The 'minimum flow' field must be specified");
                }
            }
            
            if (tag.equals("outFlowValue")) 
            {
                try 
                {
                    c_outFlowValue = value;
                } 
                catch (Exception e) 
                {
                    throw new RmdParseException("The 'out flow' field must be specified");
                }
                if (c_outFlowValue == null) 
                {
                    throw new RmdParseException("The 'out flow' field must be specified");
                }
            }
        }// END WHILE 
    }// End method parseData(....)
    
     public EquationControl getEquationControl(int maxTimesteps, ID node, Vector toFlows, Vector fromFlows)
      throws ModelException
  {
      EquationControl control = new EquationControl();
      int eqId = 1, index;
      Variable var1, var2, var3, binVar, ssFu1,ssFu2;
      Equation mainEq, mainEq2, ssEq, lowerBoundaryEq, upperBoundaryEq, ssyEq, stpEq, stupEq,stupFuEq ;
      Vector foundFlows = new Vector(0);
      Vector outFlows = new Vector(0);
      float proportion;
      float c_infinity ;
      float qi;
      
       /* get the infinity definition from the ini-file */
        Ini inifile = new Ini();

        String inf = inifile.getProperty(MPS_INFINITY_DEFINITIION, "1E10");

        try {
            c_infinity = Float.parseFloat(inf);
            }
        catch (NumberFormatException ex) 
            {
            GUI.getInstance().showMessageDialog("Invalid value in settings.ini. " +
                                          MPS_INFINITY_DEFINITIION + "= " +
                                          inf + "\n Using 1e10 as infinity");
            c_infinity = 1E10f;
            }
      //Vector xFlows = new Vector(0);
      //Vector yFlows = new Vector(0);
      //addEquationOfTimestep( control, 1/* timestep */, 1/*index*/,
      //		       node, toFlows, fromFlows);
      // int vectorsize = c_timesteps.size();
      // Added by Nawzad Mardan 081210
       if (c_resource == null)
	    throw new ModelException("Resource in StartStop "+getLabel()+" not specified.");
        
       // Check if any flow is selected
       // Added by Nawzad Mardan 081210
        if((toFlows.size() <= 0) && (fromFlows.size() <=0 ))
        throw new ModelException("No flow going in or out from node " +
				     node + " has the same resource as " +
				     "the source function inside it.\n" +
				     "Can not optimize.");
        // Check what ingoing or outgoing flows has the same resource as
	// this source functions
        if((toFlows.size()<=0)  && (fromFlows.size()<=0))
            {
              throw new ModelException("In StartStop Function: No flow going in from  or to" +
					 "Node "+node+".\n\n"+ "Can not optimize.");  
            }
        else
            {
            if(toFlows.size() > 0)
                {
                for (int i = 0; i < toFlows.size(); i++) 
                    {
                    if (((Flow) toFlows.get(i)).getResource().equals(getResource())) 
                        {
                        foundFlows.addElement(toFlows.get(i));
                        // add variable for flow
                        }
                    }//END FOR
                 }
            if((fromFlows.size() > 0) && (foundFlows.size() <= 0))
                {
                for (int i = 0; i < fromFlows.size(); i++) 
                    {
                    if (((Flow) fromFlows.get(i)).getResource() == null)
                        throw new ModelException("In StartStop Function: Resource for Source function in " +
					 "Node "+node+" not specified.\n\n"+
					 "Can not optimize.");
                    if (((Flow) fromFlows.get(i)).getResource().equals(getResource())) 
                        {
                        foundFlows.addElement(fromFlows.get(i));
                        // add variable for flow
                        }
                    }
                }
            
            }
      if (foundFlows.size() <= 0)throw new ModelException("In StartStop Function: The Outflows and Inflows to the Node (" + node+" )\n are not contain selected recourse " +"\n"+
					 "Can not optimize."); 
      if(c_choiceOne)
      {
        proportion = c_percentageOfPreviousFlow/100;
        qi = c_thresholdValue;
        // If user selecte shut down choice
        if(c_stopAltOne)
        {
        
        for (int i = 1; i < maxTimesteps ; i++) 
        {
          /*
            for every timestep, we need to generate a variable*/
            
        /*Infow eguations */
            /* Main equation
            *K1 is proportion of the previous flow
            *Y1 is a binary variable
            *U is a very big number 1.0 E9
            F1T2 + F2T2+… +FmT2 >= K1* F1T1 + F2T1+… +FmT1 - Y1*U	(1)
            F1T2 + F2T2+… +FmT2 - K1* (F1T1 + F2T1+… +FmT1) + Y1*U >=0
            .
            .
            F1Ti + F2Ti+1+… +FmTi)-(Ki-1)*(F1Ti+F2Ti+......FmTi)+Yi-1*U >=0
            
            F1Ti + F2Ti + … + FmTi >= K(i-1) * (F1T(i-1) + F2T(i-1) + … + FmT(i-1)) – Y(i-1) * U

            */
	index = i+1;
	mainEq = new Equation(node, getID() /* This function ID */,i, eqId++ /*Equation number*/,
                                        Equation.GREATEROREQUAL,0);
        /* SSFu equation
            *SSTiFu1, SSTiFu2 are continue variables 
            *Qi is a threshold value
            *U is a very big number 1.0 E9
            F1T1 + F2T1+… +FmT1 = SST1Fu1+ SST1Fu2	(1)
            F1T2 + F2T2+… +FmT2 = SST2Fu1+SST2Fu2
            .
            .
            F1Ti-1 + F2Ti-1+… +FmTi-1= SSTi-1Fu1+SSTi-1Fu2
            */
        
        ssEq = new Equation(node, getID() /* This function ID */,i, eqId++ /*Equation number*/,
                                        Equation.EQUAL,0);
        /* Lowerboundary and Upperbundary equation
         * SSTiFu is continue variable
         * Qi is a threshold value
         * U is a very big number 1.0 E9
         * Y1 is a binary variable
         * Q1 *(1- Y1) <= SST1Fu1 <= (1- Y1)*U
         * Q2 *(1- Y2) <= SST2Fu1 <= (1- Y2)*U
         *.
         *.
         *Qi *(1- Y(i-1)) <= SST(i-1)Fu1 <= (1- Y(i-1))*U
         */
        
        
        /* SST(i-1)Fu2 <= Y(i-1)*Qi */
        ssyEq = new Equation(node, getID() /* This function ID */,i, eqId++ /*Equation number*/,
                                        Equation.LOWEROREQUAL,0);
        
	//for (int j = 0; j < fromFlows.size(); j++) 
        for (int j = 0; j < foundFlows.size(); j++)  
        {
         /* F1Ti+1 + F2Ti+1+… +FmTi+1)-Ki*(F1Ti+F2Ti+......FmTi)+Yi*U >=0*/
         /* F1Ti-1 + F2Ti-1+… +FmTi-1= SSTi-1Fu1+SSTi-1Fu2 */
         /* Qi *(1- Y(i-1)) <= SST(i-1)Fu1 <= (1- Y(i-1))*U */
          
        /* F1Ti+1 + F2Ti+1+… +FmTi+1)*/    
        //var1 = new Variable(((Flow) fromFlows.get(j)).getID(), i+1, 1.0f);
        var1 = new Variable(((Flow) foundFlows.get(j)).getID(), i+1, 1.0f);
        /* -Ki*(F1Ti+F2Ti+......FmTi) */
        //var2 = new Variable(((Flow) fromFlows.get(j)).getID(), i, -1.0f*proportion);
        var2 = new Variable(((Flow) foundFlows.get(j)).getID(), i, -1.0f*proportion);
        /* F1Ti-1 + F2Ti-1+… +FmTi-1 */
        //var3 = new Variable(((Flow) fromFlows.get(j)).getID(), index-1, 1.0f);
        var3 = new Variable(((Flow) foundFlows.get(j)).getID(), i, 1.0f);
        mainEq.addVariable(var2);
        mainEq.addVariable(var1);
        ssEq.addVariable(var3);
	}// END FOR
	/* Yi*U */
        binVar = new Variable("YA1"+getID(), i, 1.0f* c_infinity);
        binVar.setIsInteger(true);
        mainEq.addVariable(binVar);
        control.add(mainEq);
        /* SSTi-1Fu1+SSTi-1Fu2 */
        ssFu1 = new Variable("SSFL1A1"+getID(), i, -1.0f);
        ssFu2 = new Variable("SSFL2A1"+getID(), i, -1.0f);
        ssEq.addVariable(ssFu1);
        ssEq.addVariable(ssFu2);
        control.add(ssEq); 
        /* SST(i-1)Fu1*/
        /* SST(i-1)Fu1 <= (1- Y(i-1))*U = > SST(i-1)Fu1 + Y(i-1)*U < = U*/
        upperBoundaryEq = new Equation(node, getID()/* This function ID */, i, eqId++ /* Equation number */,
                                   Equation.LOWEROREQUAL, c_infinity);
        /* Qi *(1- Y(i-1)) <= SST(i-1)Fu1 =>  SST(i-1)Fu1 + Qi*Y(i-1) >= Qi */
        lowerBoundaryEq = new Equation(node, getID()/* This function ID */, i, eqId++ /* Equation number */,
                                          Equation.GREATEROREQUAL, qi);
        
        ssFu1 = new Variable("SSFL1A1"+getID(), i, 1.0f);
        lowerBoundaryEq.addVariable(ssFu1);
        /*  /* Qi *(1- Y(i-1)) */
        binVar = new Variable("YA1"+getID(), i, 1.0f*qi);
        binVar.setIsInteger(true);
        /* SST(i-1)Fu1 - Qi + Y(i-1)*Qi >= 0 */
        lowerBoundaryEq.addVariable(binVar);
        control.add(lowerBoundaryEq);
        /* SST(i-1)Fu1 <= (1- Y(i-1))*U */
        upperBoundaryEq.addVariable(ssFu1);
        binVar = new Variable("YA1"+getID(), i, 1.0f* c_infinity);
        binVar.setIsInteger(true);
        upperBoundaryEq.addVariable(binVar);
        control.add(upperBoundaryEq);
        
         /* SST(i-1)Fu2 <= Y(i-1)*Qi */
        ssFu2= new Variable("SSFL2A1"+getID(), i, 1.0f);
        ssyEq.addVariable(ssFu2);
        binVar = new Variable("YA1"+getID(), i, -1.0f*qi);
        binVar.setIsInteger(true);
        ssyEq.addVariable(binVar);
        //ssFu2= new Variable("MinYutT1",-1.0f);
        //ssFu2.setIsInteger(true);
        //ssyEq.addVariable(ssFu2);
        control.add(ssyEq);
        
       }// END FOR LOOP
       // If Shut down within analysis period CheckBox is selected
       if(c_stopLastTimestepAltOne)
        {
        //Q >= (F1Ti + F2Ti + … + FmTi), där i är sista tidssteget
        stpEq = new Equation(node, getID(), maxTimesteps, eqId++, Equation.LOWEROREQUAL, qi);
        for (int j = 0; j < foundFlows.size(); j++)  
            {
            var1 = new Variable(((Flow) foundFlows.get(j)).getID(), maxTimesteps, 1.0f);
            stpEq.addVariable(var1);
            }
        control.add(stpEq);
       }
        
      }// END IF Shut down CheckBox is selected
     
     // IF Start CheckBox is selected
     if(c_startAltOne)
     {
      /*
        F1T1 + F2T1 + … + FmT1 >= K2 * (F1T2 + F2T2 + … + FmT2) – Y2 * U
        F1T2 + F2T2 + … + FmT2 >= K3 * (F1T3 + F2T3 + … + FmT3) – Y3 * U
        .
        .
        .
        F1T(i-1) + F2T(i-1) + … + FmT(i-1) >= Ki * (F1Ti + F2Ti + … + FmTi) – Yi * U


        F1T2 + F2T2 + … + FmT2 = SST1Fu1 + SST1Fu2 
        Q * (1 – Y2) <= SST1Fu1 <= (1 – Y2) * U
        SST1Fu2 <= Y2 * Q
        F1T3 + F2T3 + … + FmT3 = SST2Fu1 + SST2Fu2
        Q * (1 – Y3) <= SST2Fu1 <= (1 – Y3) * U
        SST2Fu2 <= Y3 * Q
        .
        .
        .
        F1Ti + F2Ti + … + FmTi = SSTiFu1 + SSTiFu2
        Q * (1 – Yi) <= SSTiFu1 <= (1 – Yi) * U
        SSTiFu2 <= Yi * Q


        Om man väljar Start up within analysis period då ska den skapas.
        Skapas om produktionen startas i första  tidssteget (d.v.s. produktionen ska starta inom analysperioden):
        Q >= (F1T1 + F2T1 + … + FmT1)

       */    
     
        for(int i = 1; i < maxTimesteps; i++)
        {    
        /*  F1T1 + F2T1 + … + FmT1 >= K2 * (F1T2 + F2T2 + … + FmT2) – Y2 * U
            F1T2 + F2T2 + … + FmT2 >= K3 * (F1T3 + F2T3 + … + FmT3) – Y3 * U
            .
            .
            .
            F1T(i-1) + F2T(i-1) + … + FmT(i-1) >= Ki * (F1Ti + F2Ti + … + FmTi) – Yi * U*/
        stupEq = new Equation(node, getID(), i, eqId++, Equation.GREATEROREQUAL, 0);
        // F1T2 + F2T2 + … + FmT2 = SSFL1T2 + SSFL2T2 
        //  ...
        //F1Ti + F2Ti + … + FmTi = SSFL1Ti + SSFL2Ti
        stupFuEq= new Equation(node, getID(), i, eqId++, Equation.EQUAL, 0);
        for (int j = 0; j < foundFlows.size(); j++)  
            {
            var1 = new Variable(((Flow) foundFlows.get(j)).getID(),i, 1.0f);
            var2 = new Variable(((Flow) foundFlows.get(j)).getID(),i+1, -1.0f*proportion);
            stupEq.addVariable(var1);
            stupEq.addVariable(var2);
            var3 = new Variable(((Flow) foundFlows.get(j)).getID(),i+1, 1.0f);
            stupFuEq.addVariable(var3);
            }
            
        binVar = new Variable("YA1"+getID(), i+1, 1.0f* c_infinity);
        binVar.setIsInteger(true);
        stupEq.addVariable(binVar);
        ssFu1 = new Variable("SSFL1A1"+getID(), i+1, -1.0f);
        ssFu2 = new Variable("SSFL2A1"+getID(), i+1, -1.0f);
        stupFuEq.addVariable(ssFu1);
        stupFuEq.addVariable(ssFu2);
        control.add(stupEq);
        control.add(stupFuEq);
        
        }// END FOR
        
        /* 
         Q * (1 – Y2) <= SST2Fu1 <= (1 – Y2) * U
         SST2Fu2 <= Y2 * Q
         .
         .
         Q * (1 – Yi) <= SSTiFu1 <= (1 – Yi) * U
         SSTiFu2 <= Yi * Q
         */
     
        for(int i = 1; i < maxTimesteps; i++)
        {
        //SST2Fu1 <= (1 – Y2) * U ---> SST2Fu1+ Y2*U <= U
        upperBoundaryEq = new Equation(node, getID(), i, eqId++,Equation.LOWEROREQUAL, c_infinity);
        //Q * (1 – Y2) <= SST2Fu1 -----> Qi <= SST2Fu1+ Y2*Q
        lowerBoundaryEq = new Equation(node, getID(), i, eqId++,Equation.GREATEROREQUAL, qi);
        
        ssFu1 = new Variable("SSFL1A1"+getID(), i+1, 1.0f);
        binVar = new Variable("YA1"+getID(), i+1, 1.0f* c_infinity);
        binVar.setIsInteger(true);
        upperBoundaryEq.addVariable(ssFu1);
        upperBoundaryEq.addVariable(binVar);
        lowerBoundaryEq.addVariable(ssFu1);
        binVar = new Variable("YA1"+getID(), i+1, 1.0f* qi);
        binVar.setIsInteger(true);
        lowerBoundaryEq.addVariable(binVar);
     
        //SSTiFu2 <= Yi * Q ----> SSTiFu2 -  Yi * Q <= 0
        stupFuEq= new Equation(node, getID(), i, eqId++, Equation.LOWEROREQUAL, 0);
        ssFu2 = new Variable("SSFL2A1"+getID(), i+1, 1.0f);
        binVar = new Variable("YA1"+getID(), i+1, -1.0f*qi);
        binVar.setIsInteger(true);
        stupFuEq.addVariable(ssFu2);
        stupFuEq.addVariable(binVar);
        control.add(upperBoundaryEq);
        control.add(lowerBoundaryEq);
        control.add(stupFuEq);
        
        }
     /*
        Om man väljar Start up within analysis period då ska den skapas.
        Skapas om produktionen startas i första  tidssteget (d.v.s. produktionen ska starta inom analysperioden):
        Q >= (F1T1 + F2T1 + … + FmT1)*/
    // If Start up within analysis period CheckBox is selected
       if(c_startFirstTimestepAltOne)
        {
        //Q >= (F1T1 + F2T1 + … + FmT1)
        stpEq = new Equation(node, getID(), 1, eqId++, Equation.LOWEROREQUAL, qi);
        for (int j = 0; j < foundFlows.size(); j++)  
            {
            var1 = new Variable(((Flow) foundFlows.get(j)).getID(), 1, 1.0f);
            stpEq.addVariable(var1);
            }
        control.add(stpEq);
       }
       
     }
     }// END IF CHOICE ONE selected
   //•••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••
        
     if(c_choiceTwo)
     {
          
        /*
        SSTi = binär variabel för tidssteg i
        STT0 = konstant, sätts till 1 om det är produktion i ”Tidssteg 0” och om en startkostnad INTE ska inkluderas i modellen vid en eventuell start i första tidssteget, annars sätts konstanten till 0
        SPT0 = konstant, sätts till 1 om det är produktion i ”Tidssteg 0” och om en stoppkostnad ska inkluderas i modellen vid ett eventuellt stopp i första tidssteget, annars sätts konstanten till 0
        STXi = binär hjälpvariabel 
        SPXi = binär hjälpvariabel 
        Csti = konstant, kostnad för att starta processen (föra ett flöde genom FmTi)
        Cspi = konstant, kostnad för att stoppa processen (föra ett flöde genom FmTi)
        Psti = kontinuerlig variabel som ingår i objektfunktionen vid startkostnad
        Pspi = kontinuerlig variabel som ingår i objektfunktionen vid stoppkostnad
        FmTi = kontinuerlig variabel, flöde in till eller ut från den nod som funktionen ligger i
        U = stort tal
        Ki = konstant, kostnad varje tidssteg då processen är igång. Denna kostnad bör avvägas mot Psti/Pspi, dvs startkostnaden och den kontinuerliga kostnaden bör beaktas noga. 

        Startkostnad
        SST1 <= STX1
        STT0 <= STX1
        (STX1 – STT0) * Cst1 = Pst1

        SST2 <= STX2
        SST1 <= STX2
        (STX2 – SST1) * Cst2 = Pst2

        SST3 <= STX3
        SST2 <= STX3
        (STX3 – SST2) * Cst3 = Pst3
        .
        .
        .
        SST(i) <= STX(i)
        SST(i-1) <= STX(i)
        (STX(i) – SST(i-1)) * Csti = Psti
        */
    Variable SSTi, STXi, Psti, Pstsp;
    Equation Eq1, Eq2,Eq3, commonEq1,commonEq2, objEq, PstspEq;
    int STT0, SPT;
 
    Pstsp = SSTi = new Variable("PstspTotal"+getID(), 0, 1.0f);
    objEq = new Equation(node, getID() /* This function ID */,1, eqId++ /*Equation number*/,
                                        Equation.GOALORFREE); 
    objEq.addVariable(Pstsp);
    PstspEq = new Equation(node, getID() /* This function ID */,1, eqId++ /*Equation number*/,Equation.EQUAL,0);
    PstspEq.addVariable(Pstsp);
    //Start cost choice is selected
    if(c_startCostChoice)
    {
    if(!c_startCostofFirstTimestepChoice)
     STT0 = 1;
    else
     STT0 = 0;
    /*
    SST1 <= STX1
    STT0 <= STX1
    (STX1 – STT0) * Cst1 = Pst1
    */
 
    Eq1 = new Equation(node, getID()/* This function ID */, 1, eqId++ /* Equation number */,
                                   Equation.LOWEROREQUAL, 0);
    SSTi = new Variable("SSA2"+getID(), 1, 1.0f);
    SSTi.setIsInteger(true);
    Eq1.addVariable(SSTi);
    STXi = new Variable("STXA2"+getID(), 1, -1.0f);
    STXi.setIsInteger(true);
    Eq1.addVariable(STXi);
    control.add(Eq1);
 
    Eq2 = new Equation(node, getID()/* This function ID */, 1, eqId++ /* Equation number */,
                                   Equation.LOWEROREQUAL, -1.0f*STT0);
 
    STXi = new Variable("STXA2"+getID(), 1, -1.0f);
    STXi.setIsInteger(true);
    Eq2.addVariable(STXi);
    control.add(Eq2);
 
    Eq3 = new Equation(node, getID() /* This function ID */,1, eqId++ /*Equation number*/,
                                        Equation.EQUAL,c_startCost*STT0);
    STXi = new Variable("STXA2"+getID(), 1, c_startCost);
    STXi.setIsInteger(true);
    Eq3.addVariable(STXi);
 
    Psti = new Variable("PstA2"+getID(), 1, -1.0f);
    Eq3.addVariable(Psti);
    PstspEq.addVariable(Psti);
    control.add(Eq3);


    for (int i = 2; i <= maxTimesteps ; i++) 
      {       
      Eq1 = new Equation(node, getID()/* This function ID */, i+1, eqId++ /* Equation number */,
                                   Equation.LOWEROREQUAL, 0);
      SSTi = new Variable("SSA2"+getID(), i, 1.0f);
      SSTi.setIsInteger(true);
      Eq1.addVariable(SSTi);
      STXi = new Variable("STXA2"+getID(), i, -1.0f);
      STXi.setIsInteger(true);
      Eq1.addVariable(STXi);
      control.add(Eq1);
        
      Eq2 = new Equation(node, getID()/* This function ID */, 1, eqId++ /* Equation number */,
                                   Equation.LOWEROREQUAL,0);
 
      SSTi = new Variable("SSA2"+getID(), i-1, 1.0f);
      SSTi.setIsInteger(true);
      Eq2.addVariable(SSTi);
      Eq2.addVariable(STXi);
      control.add(Eq2);
 
      //Eq3 = new Equation(node, getID() /* This function ID */,1, eqId++ /*Equation number*/,
      //                              Equation.EQUAL,c_startCost*STT0);
      Eq3 = new Equation(node, getID(),1, eqId++ /*Equation number*/,
                                        Equation.EQUAL,0);

      STXi = new Variable("STXA2"+getID(), i, c_startCost);
      STXi.setIsInteger(true);
      Eq3.addVariable(STXi);
        
      SSTi = new Variable("SSA2"+getID(), i-1, -1.0f*c_startCost);
      SSTi.setIsInteger(true);
      Eq3.addVariable(SSTi);

      Psti = new Variable("PstA2"+getID(), i, -1.0f);
      Eq3.addVariable(Psti);
      PstspEq.addVariable(Psti);
      control.add(Eq3);
     
      }// END FOR (timesteps
    }// END IF Start cost choice is selected

 // Start cost choice is selected
 if(c_stopCostChoice)
 {
  if(c_stopCostofLastTimestepChoice)
     SPT = 0; // SST(i+1)=0
 else
     SPT = 1; // SST(i+1)=1
 /*
    Stoppkostnad c_stopCost

    SST1 <= SPX1
    SST2 <= SPX1
    (SPX1 – SST2) * Csp2 = Psp2

    SST2 <= SPX2
    SST3 <= SPX2
    (SPX2 – SST3) * Csp3 = Psp3
    .
    .
    .
    SST(i-1) <= SPX(i-1)
    SSTi <= SPX(i-1)
    (SPX(i-1) – SST(i)) * Cspi = Pspi
  */
 /*
 
    Eq2 = new Equation(node, getID(), 1, eqId++ , Equation.LOWEROREQUAL, -1.0f*SPT0);
 
    STXi = new Variable("SPX", 0, -1.0f);
    STXi.setIsInteger(true);
    Eq2.addVariable(STXi);
    control.add(Eq2);
 
    Eq1 = new Equation(node, getID(), 1, eqId++ ,Equation.LOWEROREQUAL, 0);
    SSTi = new Variable("SSA2", 1, 1.0f);
    SSTi.setIsInteger(true);
    Eq1.addVariable(SSTi);
    //STXi = new Variable("SX", 1, -1.0f);
    //STXi.setIsInteger(true);
    Eq1.addVariable(STXi);
    control.add(Eq1);
 
    Eq3 = new Equation(node, getID() ,1, eqId++, Equation.EQUAL,0);
    STXi = new Variable("SPX", 0, c_stopCost);
    STXi.setIsInteger(true);
    Eq3.addVariable(STXi);
 
    SSTi = new Variable("SSA2", 1, -1.0f*c_stopCost);
    SSTi.setIsInteger(true);
    Eq3.addVariable(SSTi);
 
    Psti = new Variable("PspA2", 1, -1.0f);
    Eq3.addVariable(Psti);
    PstspEq.addVariable(Psti);
    control.add(Eq3);

    */
  /*
   *
   SST1 <= SPX1
    SST2 <= SPX1
    (SPX1 – SST2) * Csp1 = Psp1

    SST2 <= SPX2
    SST3 <= SPX2
    (SPX2 – SST3) * Csp2 = Psp2
    .
    .
    .
    SST(i) <= SPX(i)
    SST(i+1) <= SPX(i)
    (SPX(i) – SST(i+1)) * Cspi = Pspi
    */
       for (int i = 1; i < maxTimesteps ; i++) 
       {
        // SST(i) <= SPX(i)   
        Eq2 = new Equation(node, getID()/* This function ID */, i, eqId++, Equation.LOWEROREQUAL,0);
        SSTi = new Variable("SSA2"+getID(), i, 1.0f);
        SSTi.setIsInteger(true);
        Eq2.addVariable(SSTi);
        STXi = new Variable("SPX"+getID(), i, -1.0f);
        STXi.setIsInteger(true);
        Eq2.addVariable(STXi);
        control.add(Eq2);
        
        // SST(i+1) <= SPX(i)
        Eq1 = new Equation(node, getID()/* This function ID */, i, eqId++, Equation.LOWEROREQUAL, 0);
        SSTi = new Variable("SSA2"+getID(), i+1, 1.0f);
        SSTi.setIsInteger(true);
        Eq1.addVariable(SSTi);
        Eq1.addVariable(STXi);
        control.add(Eq1);
        
        //(SPX(i) – SST(i+1)) * Cspi = Pspi
        Eq3 = new Equation(node, getID() /* This function ID */,i, eqId++, Equation.EQUAL,0);
        STXi = new Variable("SPX"+getID(), i, c_stopCost);
        STXi.setIsInteger(true);
        Eq3.addVariable(STXi);
        SSTi = new Variable("SSA2"+getID(), i+1, -1.0f*c_stopCost);
        SSTi.setIsInteger(true);
        Eq3.addVariable(SSTi);
        Psti = new Variable("PspA2"+getID(), i, -1.0f);
        Eq3.addVariable(Psti);
        PstspEq.addVariable(Psti);
        control.add(Eq3);
     
       }// END FOR (timesteps)Stoppkostnad
  /*
   SST(i) <= SPX(i)
    SST(i+1) <= SPX(i)
    (SPX(i) – SST(i+1)) * Cspi = Pspi
   */
  
 // SST(i) <= SPX(i)
 Eq1 = new Equation(node, getID(), 1, eqId++ ,Equation.LOWEROREQUAL, 0);
 SSTi = new Variable("SSA2"+getID(), maxTimesteps, 1.0f);
 SSTi.setIsInteger(true);
 Eq1.addVariable(SSTi);
 STXi = new Variable("SPX"+getID(), maxTimesteps, -1.0f);
 STXi.setIsInteger(true);
 Eq1.addVariable(STXi);
 control.add(Eq1);
 
 //SST(i+1) <= SPX(i)
 Eq2 = new Equation(node, getID(), 1, eqId++ , Equation.GREATEROREQUAL, SPT);
 STXi = new Variable("SPX"+getID(), maxTimesteps, 1.0f);
 STXi.setIsInteger(true);
 Eq2.addVariable(STXi);
 control.add(Eq2);
 // (SPX(i) – SST(i+1)) * Cspi = Pspi
 Eq3 = new Equation(node, getID() ,1, eqId++, Equation.EQUAL,SPT*c_stopCost);
 STXi = new Variable("SPX"+getID(), maxTimesteps, c_stopCost);
 STXi.setIsInteger(true);
 Eq3.addVariable(STXi);
 
 Psti = new Variable("PspA2"+getID(),maxTimesteps, -1.0f);
 Eq3.addVariable(Psti);
 PstspEq.addVariable(Psti);
 control.add(Eq3);
  
 }// END IF Stop cost choice is selected
 
    /*
    Gemensamma funktioner för start- och stoppkostnad

    SST1 * SSmin <= F1T1 + F2T1 + … + FmT1 <= SST1 * U
    SST2 * SSmin <= F1T2 + F2T2 + … + FmT2 <= SST2 * U
    .
    .
    .
    SSTi * SSmin <= F1Ti + F2Ti + … + FmTi <= SSTi * U
    **/
    /*
    *Kontant kostnad när processen är aktiv
    *Ki * SSTi, läggs in i objektsfunktionen
    *
    */
    
    for (int i = 1; i <= maxTimesteps ; i++) 
       {
        //F1T1 + F2T1 + … + FmT1 <= SST1 * U
        commonEq1 = new Equation(node, getID(),i, eqId++, Equation.LOWEROREQUAL,0);                                 
       //c_minimumFlowAltTwo SST1 * SSmin <= F1T1 + F2T1 + … + FmT1
        commonEq2 = new Equation(node, getID(),i, eqId++, Equation.GREATEROREQUAL,0);  
        for (int j = 0; j < foundFlows.size(); j++) 
            {
            /* F1T1 + F2T1 + … + FmT1 <= SST1 * U */
            //var1 = new Variable(((Flow) fromFlows.get(j)).getID(),i , 1.0f);
            var1 = new Variable(((Flow) foundFlows.get(j)).getID(),i , 1.0f);
            commonEq1.addVariable(var1);
            commonEq2.addVariable(var1);
            }// END FOR
        
        var2 = new Variable("SSA2"+getID(), i, -1.0f*c_infinity);
        var2.setIsInteger(true);
        commonEq1.addVariable(var2);
        
        var2 = new Variable("SSA2"+getID(), i, -1.0f*c_minimumFlowAltTwo);
        var2.setIsInteger(true);
        commonEq2.addVariable(var2);
        
        control.add(commonEq1);
        control.add(commonEq2);
        
        var3 = new Variable("SSA2"+getID(), i, 1.0f*c_operateCost);
        var3.setIsInteger(true);
        objEq.addVariable(var3);
        
        }
        control.add(PstspEq);
        control.add(objEq);
       }// END IF CHOICE TWO SELECTED
        
//••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••  
// •	Alternativ 3: Antalet starter begränsas med ett absolut värde och variationen mellan de tidssteg som ingår i starterna begränsas.
                
   if(c_choiceThree)
    {
    Equation eQ1,eQ2,eQ3, objEq;
    Variable binVar1,binVar2,binVar3;
    int SST0 ,SSTMax , max, min;
//    proportion = c_percentageOfPreviousFlow3/100;
    //c_startAltThree, c_stopAltThree, c_startFirstTimestepAltThree, c_stopLastTimestepAltThree;
    //IF Start CheckBox is selected
    if(c_startAltThree)
    {
     /* SST0 = konstant (1 eller 0), bestäms i GNU (anger om första tidssteget ska ingå i 
      antalet starter eller inte, som anges med konstanten R). Sätts till 0 om första tidssteget ska ingå i antalet starter.*/   
    //IF First time step is included CheckBox is selected
    if(c_startFirstTimestepAltThree)
    {
    if(c_startFirstTimestepAltThree)
        SST0 = 0;
    else
        SST0 =1;
    
    //SST0 <= SSX0
    //SST1 <= SSX0
    //(SSX0 – SST0) = SSXT0

    
    // SST0 <= SSX0 
    eQ1 = new Equation(node, getID(),0, eqId++,Equation.GREATEROREQUAL,SST0);
    binVar1 = new Variable("SSXSA3"+getID(), 0, 1.0f);
    binVar1.setIsInteger(true);
    eQ1.addVariable(binVar1);
    control.add(eQ1);
    
    //SST1 <= SSX0    
    eQ2 = new Equation(node, getID(),0, eqId++, Equation.LOWEROREQUAL,0);
    binVar1 = new Variable("SSA3"+getID(), 1, 1.0f);
    binVar1.setIsInteger(true);
    binVar2 = new Variable("SSXSA3"+getID(), 0, -1.0f);
    binVar2.setIsInteger(true);
    eQ2.addVariable(binVar1);
    eQ2.addVariable(binVar2);
    control.add(eQ2);
    
    //(SSX0 – SST0) = SSXT0 --> SSX0 -SSXT0 = SST0
    eQ3 = new Equation(node, getID(), 0, eqId++, Equation.EQUAL,SST0);
    binVar1 = new Variable("SSXSA3"+getID(), 0, 1.0f);
    binVar1.setIsInteger(true);
    binVar2 = new Variable("SSXTA3"+getID(), 0, -1.0f);
    binVar2.setIsInteger(true);
    eQ3.addVariable(binVar1);
    eQ3.addVariable(binVar2);
    control.add(eQ3);
    }   
        
    for (int i = 1; i < maxTimesteps ; i++) 
        {
          /*
            for every timestep, we need to generate a variable
            
        SSTi = binär variabel för tidssteg i
        SXTi = binär variabel för tidssteg i
        SSXi = binär variabel för tidssteg i
        FmTi = kontinuerlig variabel, flöde in till eller ut från den nod som funktionen ligger i
        U = stort tal
        Ki = konstant, kostnad varje tidssteg då processen är igång. 
        R (R1) = konstant, integer, som anger hur många starter som får inträffa under analysperioden 
        Vi = konstant, andel av föregående tidsstegs flöde som måste passera noden under det aktuella tidssteget

        /*Binary equations 
        SST1 <= SSX1
        SST2 <= SSX1
        (SSX1 – SST1) = SXT1


        SST2 <= SSX2
        SST3 <= SSX2
        (SSX2 – SST2) = SXT2

        .
        SST(i-1) <= SSX(i-1)	
        SSTi <= SSX(i-1)
        (SSX(i-1) – SST(i-1)) = SXT(i-1)
         
         */	
        eQ1 = new Equation(node, getID() /* This function ID */,i, eqId++ /*Equation number*/,
                                        Equation.LOWEROREQUAL,0);
        //SST1 <= SSX1
        binVar1 = new Variable("SSA3"+getID(), i, 1.0f);
        binVar1.setIsInteger(true);
        binVar2 = new Variable("SSXSA3"+getID(), i, -1.0f);
        binVar2.setIsInteger(true);
        eQ1.addVariable(binVar1);
        eQ1.addVariable(binVar2);
        control.add(eQ1);
        
        eQ2 = new Equation(node, getID() /* This function ID */,i, eqId++ /*Equation number*/,
                                        Equation.LOWEROREQUAL,0);
        //SST2 <= SSX1
        binVar1 = new Variable("SSA3"+getID(), i+1, 1.0f);
        binVar1.setIsInteger(true);
        binVar2 = new Variable("SSXSA3"+getID(), i, -1.0f);
        binVar2.setIsInteger(true);
        eQ2.addVariable(binVar1);
        eQ2.addVariable(binVar2);
        control.add(eQ2);
        
        eQ3 = new Equation(node, getID() /* This function ID */,i, eqId++ /*Equation number*/,
                                        Equation.EQUAL,0);
        //(SSX1 – SST1) = SSXT1 
        binVar1 = new Variable("SSXSA3"+getID(), i, 1.0f);
        binVar1.setIsInteger(true);
        binVar2 = new Variable("SSA3"+getID(), i, -1.0f);
        binVar2.setIsInteger(true);
        binVar3 = new Variable("SSXTA3"+getID(), i, -1.0f);
        binVar3.setIsInteger(true);
        eQ3.addVariable(binVar1);
        eQ3.addVariable(binVar2);
        eQ3.addVariable(binVar3);
        control.add(eQ3);
        }// END FOR LOOP
    
    
    for (int i = 1; i <= maxTimesteps ; i++) 
        {
         /*
            for every timestep, we need to generate a variable*/
            
        /*Infow eguations */
            /* Main equation
            *SSTi is a binary variable
            *U is a very big number 1.0 E9
            SST1 * miniFlow <= F1T1 + F2T1 + … + FmT1 <= SST1 * U
            SST2 * miniFlow <= F1T2 + F2T2 + … + FmT2 <= SST2 * U
            .
            SSTi * miniFlow <=F1Ti + F2Ti + … + FmTi <= SSTi * U

            */
	mainEq = new Equation(node, getID() /* This function ID */,i, eqId++ /*Equation number*/,
                                        Equation.LOWEROREQUAL,0);
        mainEq2 = new Equation(node, getID() /* This function ID */,i, eqId++ /*Equation number*/,
                                        Equation.GREATEROREQUAL,0);
        
        binVar1 = new Variable("SSA3"+getID(), i, -1.0f*c_infinity);
        binVar1.setIsInteger(true);
        binVar2 = new Variable("SSA3"+getID(), i, -1.0f*c_minimumFlowValue);
        binVar2.setIsInteger(true);
        for (int j = 0; j < foundFlows.size(); j++)  
            {
            /* F1T1 + F2T1 + … + FmT */    
            var1 = new Variable(((Flow) foundFlows.get(j)).getID(), i, 1.0f);
            mainEq.addVariable(var1);
            mainEq2.addVariable(var1);
            }
         mainEq.addVariable(binVar1);
         mainEq2.addVariable(binVar2);
         control.add(mainEq);
         control.add(mainEq2);
        }//END FOR LOOP
         
    // R <= SSXT1 + SSXT2 + ... + SSXTi <= R 
    //SSXT1 + SSXT2 + ... + SSXTi <= R
    if(c_startFirstTimestepAltThree)
        min = 0;
    else min = 1;
    
    if(getOperator().equals("LESS_GREATER"))
    {
    upperBoundaryEq = new Equation(node, getID()/* This function ID */, 1, eqId++ /* Equation number */,
                                   Equation.LOWEROREQUAL, c_limit2);
    // R <= SSXT1 + SSXT2 + ... + SSXTi */
    lowerBoundaryEq = new Equation(node, getID()/* This function ID */, 1, eqId++ /* Equation number */,
                                          Equation.GREATEROREQUAL, c_limit1);
    for (int i= min; i < maxTimesteps ; i++) 
        {
         binVar1 = new Variable("SSXTA3"+getID(), i, 1.0f);
         binVar1.setIsInteger(true);
         upperBoundaryEq.addVariable(binVar1);
         lowerBoundaryEq.addVariable(binVar1);
        }
    control.add(upperBoundaryEq);
    control.add(lowerBoundaryEq);
    }// END IF
    
    // SSXT1 + SSXT2 + ... + SSXTi <=R
    else if(c_operator.equals("LESS"))
    {
     eQ1 = new Equation(node, getID()/* This function ID */, 1, eqId++ /* Equation number */,
                                   Equation.LOWEROREQUAL, c_limit2);
     for (int i = min; i < maxTimesteps ; i++) 
        {
         binVar1 = new Variable("SSXTA3"+getID(), i, 1.0f);
         binVar1.setIsInteger(true);
         eQ1.addVariable(binVar1);
        }
     control.add(eQ1);
     
    }
    
//SXT1 + SXT2 + ... + SXTi >=R
   else if(c_operator.equals("GREATER"))
   {
     eQ1 = new Equation(node, getID()/* This function ID */, 1, eqId++ /* Equation number */,
                                   Equation.GREATEROREQUAL, c_limit2);
     for (int i = min; i < maxTimesteps ; i++) 
        {
         binVar1 = new Variable("SSXTA3"+getID(), i, 1.0f);
         binVar1.setIsInteger(true);
         eQ1.addVariable(binVar1);
        }
     control.add(eQ1); 
   }
    
 // SXT1 + SXT2 + ... + SXTi =R
  else if(c_operator.equals("EQUAL"))
    {
    eQ1 = new Equation(node, getID()/* This function ID */, 1, eqId++ /* Equation number */,
                                   Equation.EQUAL, c_limit2);
     for (int i = min; i < maxTimesteps ; i++) 
        {
         binVar1 = new Variable("SSXTA3"+getID(), i, 1.0f);
         binVar1.setIsInteger(true);
         eQ1.addVariable(binVar1);
        }
     control.add(eQ1);   
    }   
    
    }// END IF Start CheckBox is selected
   //EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
    //IF Stop CheckBox is selected
    if(c_stopAltThree)
    {
    /*
        SST1 <= SSX1                SST2 <= SSX2                        SSTi-1 <= SSXi-1
        SST2 <= SSX1                SST3 <= SSX2            .........   SSTi <= SSXi-1    
        (SSX1 – SST2) = SSP1        (SSX2 – SST3) = SSP2                (SSXi-1 – SSTi) = SSPi-1    
     */
    for (int i = 1; i < maxTimesteps ; i++) 
        {
        //SST1 <= SSX1
        eQ1 = new Equation(node, getID(), i, eqId++, Equation.LOWEROREQUAL,0);
        
        binVar1 = new Variable("SSA3"+getID(), i, 1.0f);
        binVar1.setIsInteger(true);
        binVar2 = new Variable("SSXPA3"+getID(), i, -1.0f);
        binVar2.setIsInteger(true);
        eQ1.addVariable(binVar1);
        eQ1.addVariable(binVar2);
        control.add(eQ1);
        
        //SST2 <= SSX1
        eQ2 = new Equation(node, getID(), i, eqId++, Equation.LOWEROREQUAL,0);
        //SST2 <= SSX1
        binVar1 = new Variable("SSA3"+getID(), i+1, 1.0f);
        binVar1.setIsInteger(true);
        binVar2 = new Variable("SSXPA3"+getID(), i, -1.0f);
        binVar2.setIsInteger(true);
        eQ2.addVariable(binVar1);
        eQ2.addVariable(binVar2);
        control.add(eQ2);
        
        //(SSX1 – SST2) = SSP1 --->SSXi-1 – SSTi) = SSPi-1 
        eQ3 = new Equation(node, getID(), i, eqId++, Equation.EQUAL,0);
        binVar1 = new Variable("SSXPA3"+getID(), i, 1.0f);
        binVar1.setIsInteger(true);
        binVar2 = new Variable("SSA3"+getID(), i+1, -1.0f);
        binVar2.setIsInteger(true);
        binVar3 = new Variable("SSPA3"+getID(), i, -1.0f);
        binVar3.setIsInteger(true);
        eQ3.addVariable(binVar1);
        eQ3.addVariable(binVar2);
        eQ3.addVariable(binVar3);
        control.add(eQ3);
        }// END FOR LOOP
    
// IF Last time step is included CheckBox is selected
    if(c_stopLastTimestepAltThree)
    {
    if(c_stopLastTimestepAltThree)
        SSTMax = 0;
    else
        SSTMax =1;
    
    //SSTi <= SSXi
    //SST(i+1) <= SSXi   SST(i+1)= SSTMax   i = mx timestepa
    //(SSXi – SST(i+1)) = SSPi
    
    // SSTi <= SSXi 
    eQ1 = new Equation(node, getID(),maxTimesteps, eqId++,Equation.LOWEROREQUAL,0);
    binVar1 = new Variable("SSA3"+getID(), maxTimesteps, 1.0f);
    binVar1.setIsInteger(true);
    binVar2 = new Variable("SSXPA3"+getID(), maxTimesteps, -1.0f);
    binVar2.setIsInteger(true);

    eQ1.addVariable(binVar1);
    eQ1.addVariable(binVar2);
    control.add(eQ1);
    
    //SST(i+1) <= SSXi    
    eQ2 = new Equation(node, getID(),maxTimesteps, eqId++, Equation.GREATEROREQUAL,SSTMax);
    binVar2 = new Variable("SSXPA3"+getID(), maxTimesteps, 1.0f);
    binVar2.setIsInteger(true);
    eQ2.addVariable(binVar2);
    control.add(eQ2);
    
    ////(SSXi – SST(i+1)) = SSPi --->  (SSXi – SSPi) = SST(i+1)
    eQ3 = new Equation(node, getID(), maxTimesteps, eqId++, Equation.EQUAL,SSTMax);
    binVar1 = new Variable("SSXPA3"+getID(), maxTimesteps, 1.0f);
    binVar1.setIsInteger(true);
    binVar2 = new Variable("SSPA3"+getID(), maxTimesteps, -1.0f);
    binVar2.setIsInteger(true);
    eQ3.addVariable(binVar1);
    eQ3.addVariable(binVar2);
    control.add(eQ3);
    }// END IF Last time step is included CheckBox is selected
     
    // Do not generate same equations twise. Same equations as first case [Start case]. 
    if(!c_startAltThree)
    {
        for (int i = 1; i <= maxTimesteps ; i++) 
        {
         /*
            for every timestep, we need to generate a variable*/
            
        /*Infow eguations */
            /* Main equation
            *SSTi is a binary variable
            *U is a very big number 1.0 E9
            SST1 * miniFlow <= F1T1 + F2T1 + … + FmT1 <= SST1 * U
            SST2 * miniFlow <= F1T2 + F2T2 + … + FmT2 <= SST2 * U
            .
            SSTi * miniFlow <=F1Ti + F2Ti + … + FmTi <= SSTi * U

            */
	mainEq = new Equation(node, getID() /* This function ID */,i, eqId++ /*Equation number*/,
                                        Equation.LOWEROREQUAL,0);
        mainEq2 = new Equation(node, getID() /* This function ID */,i, eqId++ /*Equation number*/,
                                        Equation.GREATEROREQUAL,0);
        
        binVar1 = new Variable("SSA3"+getID(), i, -1.0f*c_infinity);
        binVar1.setIsInteger(true);
        binVar2 = new Variable("SSA3"+getID(), i, -1.0f*c_minimumFlowValue);
        binVar2.setIsInteger(true);
        for (int j = 0; j < foundFlows.size(); j++)  
            {
            /* F1T1 + F2T1 + … + FmT */    
            var1 = new Variable(((Flow) foundFlows.get(j)).getID(), i, 1.0f);
            mainEq.addVariable(var1);
            mainEq2.addVariable(var1);
            }
         mainEq.addVariable(binVar1);
         mainEq2.addVariable(binVar2);
         control.add(mainEq);
         control.add(mainEq2);
        }//END FOR LOOP
        
    }// END IF startAltThree
    
     if(c_stopLastTimestepAltThree)
        max = maxTimesteps;
    else max = maxTimesteps-1;
    
    // R = SSP1 + SSP2 + ... + SSPi 
    //SSP1 + SSP2 + ... + SSPi <= R
    if(getOperator().equals("LESS_GREATER"))
    {
    upperBoundaryEq = new Equation(node, getID(), 1, eqId++,Equation.LOWEROREQUAL, c_limit2);
    // R <= SSP1 + SSP2 + ... + SSPi */
    lowerBoundaryEq = new Equation(node, getID(), 1, eqId++,Equation.GREATEROREQUAL, c_limit1);
    for (int i = 1; i <= max ; i++) 
        {
         binVar1 = new Variable("SSPA3"+getID(), i, 1.0f);
         binVar1.setIsInteger(true);
         upperBoundaryEq.addVariable(binVar1);
         lowerBoundaryEq.addVariable(binVar1);
        }
    control.add(upperBoundaryEq);
    control.add(lowerBoundaryEq);
    }// END IF
    
    // SSP1 + SSP2 + ... + SSPi <=R
    else if(c_operator.equals("LESS"))
    {
     eQ1 = new Equation(node, getID(), 1, eqId++, Equation.LOWEROREQUAL, c_limit2);
     for (int i = 1; i <= max ; i++) 
        {
         binVar1 = new Variable("SSPA3"+getID(), i, 1.0f);
         binVar1.setIsInteger(true);
         eQ1.addVariable(binVar1);
        }
     control.add(eQ1);
     
    }
    
//SSP1 + SSP2 + ... + SSPi >=R
    else if(c_operator.equals("GREATER"))
    {
     eQ1 = new Equation(node, getID(), 1, eqId++, Equation.GREATEROREQUAL, c_limit2);
     for (int i = 1; i <= max ; i++) 
        {
         binVar1 = new Variable("SSPA3"+getID(), i, 1.0f);
         binVar1.setIsInteger(true);
         eQ1.addVariable(binVar1);
        }
     control.add(eQ1); 
    }
    
 // SSP1 + SSP2 + ... + SSPi =R
    else if(c_operator.equals("EQUAL"))
    {
    eQ1 = new Equation(node, getID(), 1, eqId++, Equation.EQUAL, c_limit2);
     for (int i = 1; i <= max ; i++) 
        {
         binVar1 = new Variable("SSPA3"+getID(), i, 1.0f);
         binVar1.setIsInteger(true);
         eQ1.addVariable(binVar1);
        }
     control.add(eQ1);   
    }   
    
    }// END IF Stop CheckBox is selected
   
    //Kontant kostnad när processen är aktiv
    //Ki * SSTi, läggs in i objektsfunktionen

    objEq = new Equation(node, getID() /* This function ID */,1, eqId++ /*Equation number*/,
                                        Equation.GOALORFREE);  
     for (int i = 1; i <= maxTimesteps ; i++) 
        {
        binVar1 = new Variable("SSA3"+getID(), i, 1.0f*c_operateCost3);
        binVar1.setIsInteger(true);
        objEq.addVariable(binVar1);
        }
    control.add(objEq);
   }// END IF CHOICE THREE SELECTED
  //••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••••  
        
   if(c_choiceFour)
   {
    Equation eQ1,eQ2,eQ3, objEq;
    Variable binVar1,binVar2,binVar3;
    int STT0, STTMax;
   //    proportion = c_percentageOfPreviousFlow3/100;

    // IF percentage value selected, the equations of the waste will be related to the out flows
    // instead of in flows to the nod
    if(c_percentagevalueCheckBox)
     {
     if(fromFlows.size() > 0)
         {
         for (int i = 0; i < fromFlows.size(); i++)
             {
             if (((Flow) fromFlows.get(i)).getResource() == null)
                  throw new ModelException("In StartStop Function: Resource for Source function in " +
					 "Node "+node+" not specified.\n\n"+"Can not optimize.");
             if (((Flow) fromFlows.get(i)).getResource().equals(getResource()))
                {
                 outFlows.addElement(fromFlows.get(i));
                        // add variable for flow
                }
              }
           }// END IF
       }
  //c_startWasteChoice, c_stopWasteChoice, c_startWasteOfFirstTimestepChoice, c_stopWasteOfLastTimestepChoice;
    if(c_startWasteChoice)
    {
    
    if(!c_startWasteOfFirstTimestepChoice)
     STT0 = 1;
    else
     STT0 = 0;
    /*
     *Start
     *SST0 constant zero or one
     SST0 <= SSXT0
     SST1 <= SSXT0
     (SSXT0 – SST0) = SXT0

     */
     // SST0 <= SSXT0
    eQ1 = new Equation(node, getID() /* This function ID */,1, eqId++ /*Equation number*/,
                                        Equation.GREATEROREQUAL,STT0);
        //SST1 <= SSX1
    binVar1 = new Variable("SSXSA4"+getID(), 0, 1.0f);
    binVar1.setIsInteger(true);
    eQ1.addVariable(binVar1);
    control.add(eQ1);
        
    //SST1 <= SSXT0   -->   SST1 -SSXT0 <= 0
    eQ2 = new Equation(node, getID() /* This function ID */,2, eqId++ /*Equation number*/,
                                        Equation.LOWEROREQUAL,0);
    binVar1 = new Variable("SSA4"+getID(), 1, 1.0f);
    binVar1.setIsInteger(true);
    binVar2 = new Variable("SSXSA4"+getID(), 0, -1.0f);
    binVar2.setIsInteger(true);
    eQ2.addVariable(binVar1);
    eQ2.addVariable(binVar2);
    control.add(eQ2);
        
    //(SSXT0 – SST0) = SXT0     -->     SSXT0 -SXT0 = SST0
    eQ3 = new Equation(node, getID() /* This function ID */,3, eqId++ /*Equation number*/,
                                        Equation.EQUAL,STT0);
    binVar1 = new Variable("SSXSA4"+getID(), 0, 1.0f);
    binVar1.setIsInteger(true);
    binVar2 = new Variable("SXSA4"+getID(), 0, -1.0f);
    binVar2.setIsInteger(true);
    eQ3.addVariable(binVar1);
    eQ3.addVariable(binVar2);
    control.add(eQ3);
    
    if(!c_choiceThree)
    {
        for (int i = 1; i < maxTimesteps ; i++) 
        {
          /*
            for every timestep, we need to generate a variable
            
        SSTi = binär variabel för tidssteg i
        SXTi = binär variabel för tidssteg i
        SSXi = binär variabel för tidssteg i
        FmTi = kontinuerlig variabel, flöde in till eller ut från den nod som funktionen ligger i
        U = stort tal
        Ki = konstant, kostnad varje tidssteg då processen är igång. 
        R (R1) = konstant, integer, som anger hur många starter som får inträffa under analysperioden 
        Vi = konstant, andel av föregående tidsstegs flöde som måste passera noden under det aktuella tidssteget

        /*Binary equations 
        SST1 <= SSX1
        SST2 <= SSX1
        (SSX1 – SST1) = SXT1


        SST2 <= SSX2
        SST3 <= SSX2
        (SSX2 – SST2) = SXT2

        .
        SST(i-1) <= SSX(i-1)	
        SSTi <= SSX(i-1)
        (SSX(i-1) – SST(i-1)) = SXT(i-1)
         
         */	
        eQ1 = new Equation(node, getID() /* This function ID */,i, eqId++ /*Equation number*/,
                                        Equation.LOWEROREQUAL,0);
        //SST1 <= SSX1
        binVar1 = new Variable("SSA4"+getID(), i, 1.0f);
        binVar1.setIsInteger(true);
        binVar2 = new Variable("SSXSA4"+getID(), i, -1.0f);
        binVar2.setIsInteger(true);
        eQ1.addVariable(binVar1);
        eQ1.addVariable(binVar2);
        control.add(eQ1);
        
        //SST2 <= SSX1
        eQ2 = new Equation(node, getID() /* This function ID */,i, eqId++ /*Equation number*/,
                                        Equation.LOWEROREQUAL,0);
        binVar1 = new Variable("SSA4"+getID(), i+1, 1.0f);
        binVar1.setIsInteger(true);
        binVar2 = new Variable("SSXSA4"+getID(), i, -1.0f);
        binVar2.setIsInteger(true);
        eQ2.addVariable(binVar1);
        eQ2.addVariable(binVar2);
        control.add(eQ2);
        
        //(SSX1 – SST1) = SXT1 
        eQ3 = new Equation(node, getID() /* This function ID */,i, eqId++ /*Equation number*/,
                                        Equation.EQUAL,0);
        binVar1 = new Variable("SSXSA4"+getID(), i, 1.0f);
        binVar1.setIsInteger(true);
        binVar2 = new Variable("SSA4"+getID(), i, -1.0f);
        binVar2.setIsInteger(true);
        binVar3 = new Variable("SXSA4"+getID(), i, -1.0f);
        binVar3.setIsInteger(true);
        eQ3.addVariable(binVar1);
        eQ3.addVariable(binVar2);
        eQ3.addVariable(binVar3);
        control.add(eQ3);
        }// END FOR LOOP
    
    }// IF Choice three is not selected 
        
    for (int i = 1; i <= maxTimesteps ; i++) 
        {
         /*
            for every timestep, we need to generate a variable*/
            
        /*Infow eguations */
            /* Main equation
            *SSTi is a binary variable
            *U is a very big number 1.0 E9
            SST1 *SSmin <= F1T1 + F2T1 + … + FmT1 <= SST1 * U
            SST2 *SSmin <= F1T2 + F2T2 + … + FmT2 <= SST2 * U
            .
            SSTi *SSmin <= F1Ti + F2Ti + … + FmTi <= SSTi * U

            */
        //F1T1 + F2T1 + … + FmT1 <= SST1 * U
	mainEq = new Equation(node, getID(), i, eqId++, Equation.LOWEROREQUAL,0);
        //SST1 *SSmin <= F1T1 + F2T1 + … + FmT1
        mainEq2 = new Equation(node, getID(), i, eqId++, Equation.GREATEROREQUAL,0);
        
        for (int j = 0; j < foundFlows.size(); j++)  
            {
            /* F1T1 + F2T1 + … + FmT */ 
            var1 = new Variable(((Flow) foundFlows.get(j)).getID(), i, 1.0f);
            mainEq.addVariable(var1);
            mainEq2.addVariable(var1);
            }
        binVar1 = new Variable("SSA4"+getID(), i, -1.0f*c_infinity);
        binVar1.setIsInteger(true);
        mainEq.addVariable(binVar1);
        
        binVar1 = new Variable("SSA4"+getID(), i, -1.0f*c_minimumFlowAltFour);
        binVar1.setIsInteger(true);
        mainEq2.addVariable(binVar1);
        
        control.add(mainEq);
        control.add(mainEq2);
        }//END FOR LOOP
   
    
    // If user select the fixed waste value
    if(c_fixedvalueCheckBox)
      {
       /*
        *QTi = SXT(i-1) * C   ----> QTi - SXT(i-1) * C = 0 
        .QT1 = SXT0 * C -...QTi = SXT(i-1) * C
        *
        SSStFmT1 = SXT0 * C

        SSStFmT2 = SXT1 * C

        SSStFmT3 = SXT2 * C
        .
        .
        .
        SSStFmTi = SXT(i-1) * C


        C = Fixed value som man matar in från guiet
        Q är ett ut flöde som kan väljas från guiet.

      */
       for (int i = 1; i <= maxTimesteps ; i++) 
        {
         eQ1 = new Equation(node, getID(),i, eqId++ ,Equation.EQUAL,0);
         var1 = new Variable("SSSt"+c_outFlowValue, i, 1.0f);
         eQ1.addVariable(var1);
         binVar1 = new Variable("SXSA4"+getID(), i-1, -1.0f*c_fixedWasteValue);
         binVar1.setIsInteger(true);
         eQ1.addVariable(binVar1);
         control.add(eQ1);
         
        }
            
      }// END IF USER SELECTED FIXED VALUE OF WASTE
        
      // If user select the percentage waste value
    if(c_percentagevalueCheckBox)
      {
      proportion = c_percentageWasteValue/100;
      if(c_continueProcess)
       {
       /*
        Om spillet är en andel av utflödet i noden:
        SSStFmT1 >= C/(1-C) * (F1T1 + F2T1 + … + FmT1) – (1 - SXST0) * U (Obs (F1T1 + F2T1 + … + FmT1) ska inte innehålla spillflödet

        SSStFmT2 >= C/(1-C) * (F1T2 + F2T2 + … + FmT2) – (1 - SXST1) * U (Obs (F1T2 + F2T2 + … + FmT2) ska inte innehålla spillflödet


        SSStFmT3 >= C/(1-C) * (F1T3 + F2T3 + … + FmT3) – (1 - SXST2) * U(Obs (F1T3 + F2T3 + … + FmT3) ska inte innehålla spillflödet

        .
        .
        .
        SSStFmTi >= C/(1-C) * (F1Ti + F2Ti + … + FmTi) – (1-SXTS(i-1)) * U (Obs (F1Ti + F2Ti + … + FmTi) ska inte innehålla spillflödet


        C = Percentage value som man matar in från guiet
        Q är ett ut flöde som kan väljas från guiet.

      */
        
       float coff = proportion/(1-proportion);
       
       for (int i = 1; i <= maxTimesteps ; i++) 
        {
         //C * (F1T1 + F2T1 + … + FmT1)+ U*SXT0 -QT1 <= U
         eQ1 = new Equation(node, getID(),i, eqId++ ,Equation.LOWEROREQUAL, c_infinity);  
        // var1 = new Variable(((Flow) foundFlows.get(j)).getID(), i, 1.0f);
         
         for (int j = 0; j < outFlows.size(); j++)
            {   /* F1T1 + F2T1 + … + FmT */
            String utFlow = (((Flow) outFlows.get(j)).getID()).toString();
            if(!utFlow.equals(c_outFlowValue))
               {
                var2 = new Variable(utFlow, i, coff);
                eQ1.addVariable(var2);
                }
            }
         //U*SXT0
         binVar1 = new Variable("SXSA4"+getID(), i-1, 1.0f*c_infinity);
         binVar1.setIsInteger(true);
         eQ1.addVariable(binVar1);
         //-QT1
         var1 = new Variable("SSSt"+c_outFlowValue, i, -1.0f);
         eQ1.addVariable(var1);
         control.add(eQ1);  
         
        }// END FOR EACH TIME STEPS
      } // END IF  c_continueProcess
      if(c_batchProcess)
       {
       /*
        *Om spillet är en andel av inflödet i noden:
        SSStFmT1 >= C * (F1T1 + F2T1 + … + FmT1) – (1 - SXT0) * U

        SSStFmT2 >= C * (F1T2 + F2T2 + … + FmT2) – (1 - SXT1) * U

        SSStFmT3 >= C * (F1T3 + F2T3 + … + FmT3) – (1 - SXT2) * U
        .
        .
        .
        SSStFmTi >= C * (F1Ti + F2Ti + … + FmTi) – (1-SXT(i-1)) * U
        SSStFmTi = QTi

        *QT1 >= C * (F1T1 + F2T1 + … + FmT1) – (1 - SXT0) * U--> C * (F1T1 + F2T1 + … + FmT1)+ U*SXT0 -QT1 <= U

        QT2 >= C * (F1T2 + F2T2 + … + FmT2) – (1 - SXT1) * U

        QT3 >= C * (F1T3 + F2T3 + … + FmT3) – (1 - SXT2) * U
        .
        .
        .
        QTi >= C * (F1Ti + F2Ti + … + FmTi) – SXT(i-1) * U

        C = Percentage value som man matar in från guiet
        Q är ett ut flöde som kan väljas från guiet.

      */

       for (int i = 1; i <= maxTimesteps ; i++)
        {
         //C * (F1T1 + F2T1 + … + FmT1)+ U*SXT0 -QT1 <= U
         eQ1 = new Equation(node, getID(),i, eqId++ ,Equation.LOWEROREQUAL, c_infinity);
        // var1 = new Variable(((Flow) foundFlows.get(j)).getID(), i, 1.0f);


         for (int j = 0; j < foundFlows.size(); j++)
            {   /* F1T1 + F2T1 + … + FmT */
            var2 = new Variable(((Flow) foundFlows.get(j)).getID(), i, 1.0f*proportion);
            eQ1.addVariable(var2);
            }
         //U*SXT0
         binVar1 = new Variable("SXSA4"+getID(), i-1, 1.0f*c_infinity);
         binVar1.setIsInteger(true);
         eQ1.addVariable(binVar1);
         //-QT1
         var1 = new Variable("SSSt"+c_outFlowValue, i, -1.0f);
         eQ1.addVariable(var1);
         control.add(eQ1);
        }// END FOR EACH TIME STEPS
       } // END IF c_batchProcess
      }// END IF USER SELECTED PERCENTAGE VALUE OF WASTE
    if(!c_stopWasteChoice)
      {
        for (int i = 1; i <= maxTimesteps ; i++)
            {
            eQ2 = new Equation(node, getID(),i, eqId++ ,Equation.EQUAL,0);
            var2 = new Variable(c_outFlowValue, i, 1.0f);
            var1 = new Variable("SSSt"+c_outFlowValue, i, -1.0f);
            eQ2.addVariable(var1);
            eQ2.addVariable(var2);
            control.add(eQ2);
            }
      }
    }// IF START WASTE SLECTED

    // EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
   if(c_stopWasteChoice)
    {
     if(!c_stopWasteOfLastTimestepChoice)
        STTMax = 1;
     else
        STTMax = 0;
     
        for (int i = 1; i <= maxTimesteps ; i++) 
        {
          /*
            for every timestep, we need to generate a variable
            
            SST1 <= SSX1
            SST2 <= SSX1
            (SSX1 – SST2) = SXT1

            SST2 <= SSX2
            SST3 <= SSX2
            (SSX2 – SST3) = SXT2
                .
                .
                .
            SST(i) <= SSX(i)	
            SST(i+1) <= SSX(i)
            (SSX(i) – SST(i+1) = SXT(i)
         */	
        eQ1 = new Equation(node, getID() /* This function ID */,i, eqId++ /*Equation number*/,
                                        Equation.LOWEROREQUAL,0);
        //SST1 <= SSX1
        binVar1 = new Variable("SSA4"+getID(), i, 1.0f);
        binVar1.setIsInteger(true);
        binVar2 = new Variable("SSXPA4"+getID(), i, -1.0f);
        binVar2.setIsInteger(true);
        eQ1.addVariable(binVar1);
        eQ1.addVariable(binVar2);
        control.add(eQ1);
        if(i != maxTimesteps)
          {
            //SST2 <= SSX1
            eQ2 = new Equation(node, getID() /* This function ID */,i, eqId++ /*Equation number*/,
                                        Equation.LOWEROREQUAL,0);
            binVar1 = new Variable("SSA4"+getID(), i+1, 1.0f);
            binVar1.setIsInteger(true);
            binVar2 = new Variable("SSXPA4"+getID(), i, -1.0f);
            binVar2.setIsInteger(true);
            eQ2.addVariable(binVar1);
            eQ2.addVariable(binVar2);
            control.add(eQ2);
        
            //(SSX1 – SST1) = SXT1 
            eQ3 = new Equation(node, getID() /* This function ID */,i, eqId++ /*Equation number*/,
                                        Equation.EQUAL,0);
            binVar1 = new Variable("SSXPA4"+getID(), i, 1.0f);
            binVar1.setIsInteger(true);
            binVar2 = new Variable("SSA4"+getID(), i+1, -1.0f);
            binVar2.setIsInteger(true);
            binVar3 = new Variable("SXPA4"+getID(), i, -1.0f);
            binVar3.setIsInteger(true);
            eQ3.addVariable(binVar1);
            eQ3.addVariable(binVar2);
            eQ3.addVariable(binVar3);
            control.add(eQ3);
            }// END IF maxTimestep
        else
            {
            //SSTi+1 <= SSXi ----> SSTi+1 = constant = STTMax
            eQ2 = new Equation(node, getID() /* This function ID */,i, eqId++ /*Equation number*/,
                                        Equation.GREATEROREQUAL, STTMax);
            binVar2 = new Variable("SSXPA4"+getID(), i, 1.0f);
            binVar2.setIsInteger(true);
            eQ2.addVariable(binVar2);
            control.add(eQ2);
        
            //(SSXi – SSTi+1) = SXTi ----> SSTi+1 = constant = STTMax
            eQ3 = new Equation(node, getID() /* This function ID */,i, eqId++ /*Equation number*/,
                                        Equation.EQUAL, STTMax);
            binVar1 = new Variable("SSXPA4"+getID(), i, 1.0f);
            binVar1.setIsInteger(true);
            binVar3 = new Variable("SXPA4"+getID(), i, -1.0f);
            binVar3.setIsInteger(true);
            eQ3.addVariable(binVar1);
            eQ3.addVariable(binVar3);
            control.add(eQ3);
            }
        
        }// END FOR LOOP
     // Do not generat same equations twice
    if(!c_startWasteChoice)
    {    
    for (int i = 1; i <= maxTimesteps ; i++) 
        {
         /*
            for every timestep, we need to generate a variable*/
            
        /*Infow eguations */
            /* Main equation
            *SSTi is a binary variable
            *U is a very big number 1.0 E9
            SST1 *SSmin <= F1T1 + F2T1 + … + FmT1 <= SST1 * U
            SST2 *SSmin <= F1T2 + F2T2 + … + FmT2 <= SST2 * U
            .
            SSTi *SSmin <= F1Ti + F2Ti + … + FmTi <= SSTi * U

            */
        //F1T1 + F2T1 + … + FmT1 <= SST1 * U
    mainEq = new Equation(node, getID(), i, eqId++, Equation.LOWEROREQUAL,0);
    //SST1 *SSmin <= F1T1 + F2T1 + … + FmT1
    mainEq2 = new Equation(node, getID(), i, eqId++, Equation.GREATEROREQUAL,0);
        
        for (int j = 0; j < foundFlows.size(); j++)  
            {
            /* F1T1 + F2T1 + … + FmT */ 
            var1 = new Variable(((Flow) foundFlows.get(j)).getID(), i, 1.0f);
            mainEq.addVariable(var1);
            mainEq2.addVariable(var1);
            }
        binVar1 = new Variable("SSA4"+getID(), i, -1.0f*c_infinity);
        binVar1.setIsInteger(true);
        mainEq.addVariable(binVar1);
        
        binVar1 = new Variable("SSA4"+getID(), i, -1.0f*c_minimumFlowAltFour);
        binVar1.setIsInteger(true);
        mainEq2.addVariable(binVar1);
        
        control.add(mainEq);
        control.add(mainEq2);
        } //END FOR LOOP
     }// END IF 
     
     // If user select the fixed waste value
    if(c_fixedvalueCheckBox)
      {
       /*
        *QTi = SXTi * C   ----> QTi - SXTi * C = 0 
        SSSpFmT1 = SXT1 * C

        SSSpFmT2 = SXT2 * C

        SSSpFmT3 = SXT3 * C
        .
        .
        .
        SSSpFmTi = SXTi * C
        SSpFmTi = Qi

        C = Fixed value som man matar in från guiet
        Q är ett ut flöde som kan väljas från guiet.

      */
       for (int i = 1; i <= maxTimesteps ; i++) 
        {
         eQ1 = new Equation(node, getID(),i, eqId++ ,Equation.EQUAL,0);  
         var1 = new Variable("SSSp"+c_outFlowValue, i, 1.0f);
         eQ1.addVariable(var1);
         binVar1 = new Variable("SXPA4"+getID(), i, -1.0f*c_fixedWasteValue);
         binVar1.setIsInteger(true);
         eQ1.addVariable(binVar1);
         control.add(eQ1);
         
        }
            
      }// END IF USER SELECTED FIXED VALUE OF WASTE
        
      // If user select the percentage waste value
    if(c_percentagevalueCheckBox)
      { 
       proportion = c_percentageWasteValue/100;

       /*
        Om spillet är en andel av utflödet i noden:
        SSSpFmT1 >= C/(1-C) * (F1T1 + F2T1 + … + FmT1) – (1 – SXPT1) * U (Obs (F1T1 + F2T1 + … + FmT1) ska inte innehålla spillflödet

        SSSpFmT2 >= C/(1-C) * (F1T2 + F2T2 + … + FmT2) – (1 – SXPT2) * U (Obs (F1T2 + F2T2 + … + FmT2) ska inte innehålla spillflödet

        .
        .
        .
        SSSpFmTi >= C/(1-C) * (F1Ti + F2Ti + … + FmTi) – (1 – SXPTi) * U (Obs (F1Ti + F2Ti + … + FmTi) ska inte innehålla spillflödet


        C = Percentage value som man matar in från guiet
        Q är ett ut flöde som kan väljas från guiet.

        */
       if(c_continueProcess)
       {
       float coff = proportion/(1-proportion);
       for (int i = 1; i <= maxTimesteps ; i++)
        {
         //SSSpFmT1 >= C/(1-C) * (F1T1 + F2T1 + … + FmT1) – (1 – SXPT1) * U
         eQ1 = new Equation(node, getID(),i, eqId++ ,Equation.LOWEROREQUAL, c_infinity);
        // var1 = new Variable(((Flow) foundFlows.get(j)).getID(), i, 1.0f);

         for (int j = 0; j < outFlows.size(); j++)
            {   /* F1T1 + F2T1 + … + FmT */
            String utFlow = (((Flow) outFlows.get(j)).getID()).toString();
            if(!utFlow.equals(c_outFlowValue))
               {
                var2 = new Variable(utFlow, i, coff);
                eQ1.addVariable(var2);
                }
            }
         //U*SXT0
         binVar1 = new Variable("SXPA4"+getID(), i, c_infinity);
         binVar1.setIsInteger(true);
         eQ1.addVariable(binVar1);
         //-QT1
         var1 = new Variable("SSSp"+c_outFlowValue, i, -1.0f);
         eQ1.addVariable(var1);
         control.add(eQ1);
        }// END FOR EACH TIME STEPS
       }// END IF c_continueProcess
       if(c_batchProcess)
       {
       /*
        *Om spillet är en andel av inflödet i noden:
        SSSpFmT1 >= C * (F1T1 + F2T1 + … + FmT1) – (1 – SXT1) * U

        SSSpFmT2 >= C * (F1T2 + F2T2 + … + FmT2) – (1 – SXT2) * U
        .
        .
        .
        SSSpFmTi >= C * (F1Ti + F2Ti + … + FmTi) – (1 – SXTi) * U
        SSSpFmTi = QTi
        *
        C = Percentage value som man matar in från guiet
        Q är ett ut flöde som kan väljas från guiet.

       */

       for (int i = 1; i <= maxTimesteps ; i++)
        {
         //C * (F1T1 + F2T1 + … + FmT1)+ U*SXT0 -QT1 <= U
         eQ1 = new Equation(node, getID(),i, eqId++ ,Equation.LOWEROREQUAL, c_infinity);
         for (int j = 0; j < foundFlows.size(); j++)
            {
            /* F1T1 + F2T1 + … + FmT */
            var2 = new Variable(((Flow) foundFlows.get(j)).getID(), i, 1.0f*proportion);
            eQ1.addVariable(var2);
            }
         //U*SXT0
         binVar1 = new Variable("SXPA4"+getID(), i, c_infinity);
         binVar1.setIsInteger(true);
         eQ1.addVariable(binVar1);
         //-QT1
         var1 = new Variable("SSSp"+c_outFlowValue, i, -1.0f);
         eQ1.addVariable(var1);
         control.add(eQ1);
       }// END FOR EACH TIME STEPS
       }// END IF c_batchProcess

              
      }// END IF USER SELECTED PERCENTAGE VALUE OF WASTE
    if(!c_startWasteChoice)
      {
        for (int i = 1; i <= maxTimesteps ; i++)
            {
            eQ2 = new Equation(node, getID(),i, eqId++ ,Equation.EQUAL,0);
            var2 = new Variable(c_outFlowValue, i, 1.0f);
            var1 = new Variable("SSSp"+c_outFlowValue, i, -1.0f);
            eQ2.addVariable(var1);
            eQ2.addVariable(var2);
            control.add(eQ2);
            }
      }
    // IF USER Select both
    else
        {
         for (int i = 1; i <= maxTimesteps ; i++)
            {
            eQ2 = new Equation(node, getID(),i, eqId++ ,Equation.EQUAL,0);
            var2 = new Variable(c_outFlowValue, i, 1.0f);
            var1 = new Variable("SSSp"+c_outFlowValue, i, -1.0f);
            var3 = new Variable("SSSt"+c_outFlowValue, i, -1.0f);
            eQ2.addVariable(var1);
            eQ2.addVariable(var2);
            eQ2.addVariable(var3);
            control.add(eQ2);
            }
        } // END ELSE
        
    } // END STOP WASTE SELECTED
    
   objEq = new Equation(node, getID(), 1, eqId++, Equation.GOALORFREE);  
     for (int i = 1; i <= maxTimesteps ; i++) 
        {
        binVar1 = new Variable("SSA4"+getID(), i, 1.0f*c_operateCostAltFour);
        binVar1.setIsInteger(true);
        objEq.addVariable(binVar1);
        }
    control.add(objEq);
   }// // END IF CHOICE FOUR SELECTED
    /*
   * Adds equations to the control.
   */
      return control;
  }
    
    

}
