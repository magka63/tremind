/**
 * Copyright 2007:
 * Nawzad Mardan <nawzad.mardan@liu.se>
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
/*
 * DiscountedsystemcostControl.java
 *
 * Created on den 1 juni 2007, 14:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 * /

 /*
 
 * Discountsystemcost function is a new function which calculate the total system
 * cost depending on the amount of the annually interest and the number of years.
 * This function should apply and go through all nodes to create object equations and all other necessary equations.  
 * To create all equations can the same strategy as a system cost be used except a new method should be add it 
 * to the Source function which depending on the annually rate and the chosen OR selected timestep numbers.   

 */

package mind.model;
import java.util.Vector;
import java.util.Iterator;

/**
 *
 * @author nawma
 */
public class DiscountedsystemcostControl
{
    // Annually rate
    private Float c_intrate;
    // Number of years
    private Long c_analysPeriod;
    // A Vector containing calculated  rate for each year
    private Vector c_annualRateVector;
    // The header of the table 
    private String c_tableHead []= {"Year", "Timestep nr."};
    // An empety table data
    private Object [][] c_data ={ { "Year1", ""},{ "Year2", ""}, {"Year3",""},{"Year4",""},{"Year5",""},{"Year6", ""},}; 
    //A Vector containing the selected timesteps number
    private Vector c_timestepValues;
    
    // An empty constructor (default)
    public DiscountedsystemcostControl() 
    {
        c_intrate = .0f;
        c_analysPeriod = new Long(1);
        c_annualRateVector = new Vector();
    }
    
    public DiscountedsystemcostControl(Float rate, Long year, String head [], Object [][] data, Vector timestepValues) 
    {
        c_data = data;
        c_intrate = rate;
        c_annualRateVector = new Vector();
        c_analysPeriod = year;
        c_tableHead = head;
        c_timestepValues = timestepValues;
        calculateAnnualRate();
    }
    
     /**
     * Gets the table headar.
     * @return The header as a string.
     */
    public String [] getTableHedar()
    {
        return c_tableHead;
    }
      
    /**
     * Sets the variable's th to the table's header
     * @param  th The  header to be set
     */
    
    public void setTableHedar(String []th)
    {
       c_tableHead=th; 
    }
    
    /**
     * Gets the timesteps value.
     * @return The timestep values as a Vector.
     */
    
    public Vector getTimestepValues()
    {
        return c_timestepValues;
    }
   
    /**
     * Gets the table data.
     * @return The table's data as a two dimension array of object.
     */
    
    public Object [][]getTableData()
    {
        return c_data;
    }
    
    /**
     * Sets the variable's data to the table's data
     * @param  data The data to be set
     */
    
    public void setTableData(Object [][] data)
    {
        c_data = data;
    }
    
    /**
     * Gets the annually interest.
     * @return The annually interest as an Integer.
     */
    
    public Float getRate()
    {
        return c_intrate;
    }
    
    /**
     * Sets the variable's rate to the annually interest
     * @param  rate The rate to be set
     */
    
    public void setRate(Float rate)
    {
        c_intrate = rate;
    }
    
    /**
     * Gets the analyses period.
     * @return The analyses period as an Integer.
     */
    
    public Long getAnalysPeriod()
    {
        return c_analysPeriod;
    }
    
    /**
     * Sets the variable's year to the analyses period
     * @param  year The year to be set
     */
    
    public void setAnalysPeriod(Long year)
    {
        c_analysPeriod=year;
    }
    
    /**
     * Calculating the annually rate
     * 
     */
    
    private void calculateAnnualRate()
    {
        double annualRate;
        // Chang rate from integer to foat
        float rate = c_intrate.floatValue();
        // Change it to percent
        rate = rate/100;
        for(int i = 1; i<= c_analysPeriod;i++)
        {
            // annualrate = (1+rate)power(- number of year)
            annualRate = Math.pow((1+rate),-i);
            c_annualRateVector.add(new Float(annualRate));
            
        }
    }
   
    /**
     * Gets the anuual interests.
     * @return The annual interestes period as a Vector.
     */
   
    public Vector getAnnualRate()
    {
        
        return c_annualRateVector;
    }
   /* private void print()
    {
        float  rat;
       Float rateflaot;
       for(int i = 0; i<c_annualRateVector.size() ; i++)
        {
           rateflaot = (Float)c_annualRateVector.get(i);
           rat = rateflaot.floatValue();
           System.out.println(rat);
        }
    }*/
   /* public Object clone()
    {
        return new DiscountedsystemcostControl(this.getRate(),this.getAnalysPeriod(), this.getTableHedar(), this.getTableData());
    }
    public DiscountedsystemcostControl getDiscountedsystemcostControl()
    {
    return (DiscountedsystemcostControl)this.clone();
    }*/
    
     /**
     * Gets the Discountedsystemcontrol.
     * @return this class (Discountedsystemcontrol).
      *
     */
   
    public DiscountedsystemcostControl getDiscountedsystemcostControl()
    {
    //return new DiscountedsystemcostControl(this.getRate(),this.getAnalysPeriod(), this.getTableHedar(), this.getTableData());//this;
    return this;
    }
}