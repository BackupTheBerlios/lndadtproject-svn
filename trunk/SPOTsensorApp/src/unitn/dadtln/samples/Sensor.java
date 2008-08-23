/*
 * Created on Apr 2, 2008
 *
 */
package unitn.dadtln.samples;

import java.util.Vector;

import polimi.ln.nodeAttributes.DoubleAttribute;
import polimi.ln.nodeAttributes.IntegerAttribute;

import polimi.ln.nodeAttributes.dynamic.DynamicBooleanAttribute;

import com.sun.spot.sensorboard.EDemoBoard;
import com.sun.spot.sensorboard.peripheral.ILightSensor;
import com.sun.spot.sensorboard.peripheral.ITemperatureInput;
import com.sun.spot.sensorboard.peripheral.ITriColorLED;
import com.sun.spot.sensorboard.peripheral.LEDColor;
import com.sun.spot.util.Utils;




/**
 * @author G.Khasanova
 * 
 * This class represents a sensor instance. 
 * Each sensor is specified by its type and precision and can provide reading of data 
 * and also information about its state (if it is active) 
 */
public class Sensor {
  	static final int TEMP = 0;
    static final int LIGHT = 1;
    static final int PRESSURE = 2; 
    static final int HUMIDITY = 3;
    
    /**
     * type of the sensor
     */
    public int type;
    
    /**
     * precision of the sensor 
     */
    public double precision = 2.0;
    
    private double value;
    private boolean active; 
    private Object sensorMonitor;
    
    ITriColorLED[] leds = EDemoBoard.getInstance().getLEDs();
   
    /**
     * Constructor for the Sensor class
     * @param type Sensor type
     * @param precision	Precision of the sensor
     */
    public Sensor(int type, double precision) {

    	
    	
    	this.precision = precision;
        this.active = true;        // by default sensor is active
    
        this.type = type;
        
        switch(this.type) {
        	case(TEMP):{
        		sensorMonitor = EDemoBoard.getInstance().getADCTemperature();
        		break;
        	}
        	case(LIGHT):{
        		sensorMonitor = EDemoBoard.getInstance().getLightSensor();  
        		break;
        	}
        	default: {
        		sensorMonitor = null;
        		break;
        	}
        } 
        
       
        for (int i = 0; i < leds.length; i ++)
        {
        	leds[i].setColor(LEDColor.PUCE);
        	leds[i].setOn();
            Utils.sleep(100);             
        	leds[i].setOff();
        }
        System.out.println("Sensor constructor");
        
    }
    
    
    /**
     * Return reading of the sensor
     * 
     * @return Sensed sensor reading
     */
    public double read() {
    	try
    	{
	    	switch(this.type){
	    		case(TEMP):{
	    			this.value = ((ITemperatureInput)sensorMonitor).getCelsius();
	    			
	    	        // for debug reasons //
	    	        for (int i = 0; i < leds.length; i ++)
	    	        {
	    	        	leds[i].setColor(LEDColor.BLUE);
	    	        	leds[i].setOn();
	    	            Utils.sleep(150);             
	    	        	leds[i].setOff();
	    	        }	
	    	       // --- // 
	    			
	    			break;
	    		}
	    		case(LIGHT):{
	    			this.value = (double)((ILightSensor)sensorMonitor).getAverageValue();
	    			
	    	        // for debug reasons //
	    	        for (int i = 0; i < leds.length; i ++)
	    	        {
	    	        	leds[i].setColor(LEDColor.YELLOW);
	    	        	leds[i].setOn();
	    	            Utils.sleep(150);             
	    	        	leds[i].setOff();
	    	        }	
	    	       // --- // 
	    			
	    			break;
	    		}
	    	}
    	}
    	catch (Exception e) {
			this.value = -1;
		}
        
      //this.value = (int)(generateNextValue() * 100);
        return this.value;
    }
   
    
    /**
     * Reset sensor
     */
    public void reset() {
       
    	System.out.println("reset()");
    	this.value = 0;
        this.active = true;
        
        // for debug reasons //
        for (int j = 0; j < 2; j ++) {
        	for (int i = 0; i < leds.length; i ++)
	        {
	        	leds[i].setColor(LEDColor.CHARTREUSE);
	        	leds[i].setOn();
	        }
	        Utils.sleep(500);             
	        for (int i = 0; i < leds.length; i ++)
	        	leds[i].setOff();
        }     
       // --- // 
    }
    
    
    /**
     * Return state of the sensor (active or not), "fake" value
     * @return Current state of the sensor
     */ 
    public boolean isActive() {

    	return this.active;
    
    }

    
    /**
     * Creates list of sensor's attributes described in terms of LN, mapped to a sesor type
     * @return list of LN attributes
     */
    //TODO: we can't have a sensor node with sensors of the same type in this case! 
    
    public Vector collectAttributesForLN() {
    	
    	Vector attributes = new Vector();
    	
    	attributes.addElement(new IntegerAttribute("type", type));
    	attributes.addElement(new DoubleAttribute("precision", precision));
		attributes.addElement(new DynamicBooleanAttribute("isActive", new LNSensorIsActiveProvider(this)));
	
		return attributes;
		
    }
    
    public void changeActiveState(int ledNum){
    	this.active = !this.active;
    }
    
	public void debugShowErrorOnSPOT(LEDColor color){
	       
		  
			for (int i = 0; i < leds.length; i ++)
	        {
	        	leds[i].setColor(color);
	        	leds[i].setOn();
	        }
	        Utils.sleep(100);   
	        for (int i = 0; i < leds.length; i ++)
	        {
	        	leds[i].setOff();
	        }
	        
	}

}
