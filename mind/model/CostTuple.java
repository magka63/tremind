/*
 * Copyright 2005: 
 * Urban Liljedahl  <ul@sm.luth.se>
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

package mind.model;

/*
 * Inner class defining a tuple (label,value)
 */
public class CostTuple implements Cloneable{
    private String label;
    private float value;
    public CostTuple(){
	label = "";
	value = 0.0f;
    }
    public CostTuple(String l, float v){
	label = l;
	value = v;
    }
    public Object clone(){
	return new CostTuple(this.getLabel(), this.getValue());
    }
    public void setLabel(String l){
	label = l;
    }
    public void setValue(float f){
	value = f;
    }
    public String getLabel(){
	return label;
    }
    public float getValue(){
	return value;
    }
}
