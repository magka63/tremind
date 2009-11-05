/*
 * Copyright 2004: 
 * Marcus Bergendorff <amaebe-1@student.luth.se> 
 * Jan Sköllermark <jansok-1@student.luth.se>
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

package	mind.model.function.parser;

class SFFloat{
    private float c_value;
		
    SFFloat(String value){
	c_value = Float.parseFloat(value);
    }
	
    SFFloat(float value){
	c_value = value;
    }

    SFFloat(SFFloat value) {
	c_value = value.floatValue();
    }

    public SFFloat mult(String value){
	float tmp = Float.parseFloat(value);
	tmp *= this.c_value;
	return new SFFloat(tmp);
    }
	
    public SFFloat div(String value){
	float tmp = Float.parseFloat(value);
	tmp = this.c_value / tmp;
	return new SFFloat(tmp);
    }
	
    public SFFloat mult(SFFloat value){
	return new SFFloat(this.c_value * value.floatValue());
    }
	
    public SFFloat div(SFFloat value){
	return new SFFloat(this.c_value / value.floatValue());
    }	

    public SFFloat add(SFFloat value){
	return new SFFloat(this.c_value + value.floatValue());
    }

    public SFFloat sub(SFFloat value){
	return new SFFloat(this.c_value - value.floatValue());
    }	

    public float floatValue(){
	return c_value;
    }
	
    // for debugging purposes
    public String toString(){
	Float f = new Float(c_value);
	return f.toString();
    }
}
