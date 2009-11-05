/*
 * Copyright 2003:
 * Almsted Åsa <asaal288@student.liu.se>
 * Anliot Manne <manan699@student.liu.se>
 * Fredriksson Linus <linfr529@student.liu.se>
 * Gylin Mattias <matgy024@student.liu.se>
 * Sjölinder Mattias <matsj509@student.liu.se>
 * Sjöstrand Johan <johsj438@student.liu.se>
 * Åkerlund Anders <andak893@student.liu.se>  
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

package mind.model.function.helpers;


/**
 * Describes a linear function
 * defined at a certain interval.
 * @author Johan Sjöstrand
 * @version 2003-10-30
 */
public class LinearFunction
{
    /*
     * Defines the startingpoint of the interval in
     * where the function is valid.
     */
    private float c_begin = 0;

    /*
     * Defines the endpoint of the interval in
     * where the function is valid.
     */
    private float c_end = 0;
    
    /*
     * Function offset.
     */
    private float c_offset = 0;

    /*
     * Function slope.
     */
    private float c_slope = 0;

    /**
     * Null constructor.
     */
    public LinearFunction()
    {
    }
    
    /**
     * Constructor where all attributes
     * is set.
     */
    public LinearFunction(float begin, float end, float offset, float slope)
    {
	c_begin = begin;
	c_end = end;
	c_offset = offset;
	c_slope = slope;
	}

    /**
     * Set begin value.
     * @param begin Begin value
     */
    public void setBegin(float begin)
    {
	c_begin = begin;
    }
    /**
     * Get begin value.
     * @return Begin value
     */
    public float getBegin()
    {
	return c_begin;
    }
    /**
     * Set end value.
     * @param end End value
     */
    public void setEnd(float end)
    {
	c_end = end;
    }
    /**
     * Get end value.
     * @return End value
     */
    public float getEnd()
    {
	return c_end;
    }
    /**
     * Set offset value.
     * @param offset Offset value
     */
    public void setOffset(float offset)
    {
	c_offset = offset;
    }
    /**
     * Get offset value.
     * @return Offset value
     */
    public float getOffset()
    {
	return c_offset;
    }
    /**
     * Set slope value.
     * @param slope Slope value
     */
    public void setSlope(float slope)
    {
	c_slope = slope;
    }
    /**
     * Get slope value.
     * @return Slope value
     */
    public float getSlope()
    {
	return c_slope;
    }
}