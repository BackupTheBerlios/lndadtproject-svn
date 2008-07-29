/*
 * Created on Apr 2, 2008
 *
 */
package unitn.dadtln.samples;

import java.util.Random;



import com.sun.spot.sensorboard.EDemoBoard;
import com.sun.spot.sensorboard.peripheral.ILightSensor;
import com.sun.spot.sensorboard.peripheral.ITemperatureInput;


/*
import polimi.ln.nodeAttributes.DoubleAttribute;
import polimi.ln.nodeAttributes.DynamicBooleanAttribute;
import polimi.ln.nodeAttributes.SetMapAttribute;
*/

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
    
    
    Random rnd = new Random(); 		// a random value variable, which is used for the simulation purposes in generation of the sensor readings
    
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
	    			break;
	    		}
	    		case(LIGHT):{
	    			this.value = (double)((ILightSensor)sensorMonitor).getAverageValue();
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
        value = 0;
    }
    
    
    /**
     * Return state of the sensor (active or not), "fake" value
     * @return Current state of the sensor
     */ 
    public boolean isActive() {
    	
    	this.active = (generateNextValue() >= 0.25); 
    	
    	/*
    	if (this.active) 
    		System.out.println("Sensor says that it is active (sensor type is = " + this.type + ")");
    	*/
    	
    	return (this.active);
    	
    }
    
    private double generateNextValue() {
    	return rnd.nextDouble();
    }
    
    /**
     * Creates list of sensor's attributes described in terms of LN, mapped to a sesor type
     * @return list of LN attributes
     */
    //TODO: we can't have a sensor node with sensors of the same type in this case! 
    
    /*
    public Vector collectAttributesForLN(Vector attributes) {

    	attributes.add(new SetMapAttribute(typeToStr(type)+"_precision", new DoubleAttribute("precision", precision)));
		attributes.add(new SetMapAttribute(typeToStr(type)+"_isActive", new DynamicBooleanAttribute("isActive", new LNSensorIsActiveProvider(this))));

		return attributes;
    }
    */
    
    /**
     * Generate symbolic name of the sensor type
     * @param type sensor type
     * @return name of teh sensor type
     */
    public static String typeToStr(int type) {
    	switch (type) {
    		case (int)TEMP: return "TEMP";
    		case (int)HUMIDITY: return "HUMIDITY";
    		case (int)LIGHT: return "LIGHT";
    		case (int)PRESSURE: return "PRESSURE";
    	}
		return "";	
    }
}
