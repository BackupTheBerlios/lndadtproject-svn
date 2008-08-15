package unitn.dadtln.samples;


import unitn.dadt.internals.*;
import unitn.dadt.LNSupport.DistrNodeMgr;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Vector;



/**
 * This class describes behavior of the Distributed Sensor (DADTview on the WSN). 
 * Provides methods of aggregating sensor data
 * Uses Logical neighbourhood model to perform communication between distributed nodes (simulated in SWANS)
 *  
 */
public class DSensor {
	
	private Class distributes = Sensor.class;			// reference to the sensors, which are distributed around the field
	private boolean spaceType = false;					// 
	
	private Vector readings;							// data received from distributed sensors
	private DistrNodeMgr dadtMgr = new DistrNodeMgr(); 	// dadt manager that is responsible for interaction with communication layer
	
	
	/** 
	 * Performs aggregation of data received from distributed sensors
	 * @param dataviewProperties defines a dataview on the sensors (conditions that specify if sensor's reading is relevant)
	 * @param pcNodeId specifies Id of the node that requests data (and connected with PC-application)
	 * @return average value of sensor readings
	 */
	
	public Vector average(ExpressionTree expTree) {
		
		//=== DADT request ===
		
		// perform a request 
		String action = "DSensor_read_Action";
		
		// get DADTClassName
		String DADTClassName = this.getClass().getName();
		
		dadtMgr.performDADTRequest("all", expTree, action, DADTClassName);
		
		//=== waiting for WSN reply ===
		
		// after request was sent into WSN, we wait over a timeout to collect replies from the distributed sensors 
		// we assume that within the timeout all nodes in the neighbourhood can reply
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}		
		
		HashMap tempRes = new HashMap();	
			
		if (readings != null) {

			Object mapKey = null;
			int count = 0;
			double sensorReading = 0;
			
			for (Enumeration e = readings.elements(); e.hasMoreElements(); ) 
			{	
				ResultData r = (ResultData)e.nextElement();
				
				mapKey = r.getSource();
				
				if (tempRes.containsKey(mapKey)) 
				{	
					count = (Integer) ((Object[]) tempRes.get(mapKey))[0] + 1;
					sensorReading = (Double) ((Object[]) tempRes.get(mapKey))[1] + (Double) r.getData();
					
				}	
				else
				{
					count = 1;
					sensorReading = (Double)r.getData();
				}
				
				tempRes.put(mapKey, new Object[]{count, sensorReading});
			}
			
			try {
				Vector results = new Vector();

				for (Object k:tempRes.keySet()){
					
					System.out.println("debug info: " + (Double) ((Object[]) tempRes.get(k))[1] + ", " + (Integer) ((Object[]) tempRes.get(k))[0]);
					
					double averageValue = (Double) ((Object[]) tempRes.get(k))[1] / (Integer) ((Object[]) tempRes.get(k))[0];
					results.addElement(new ResultData(averageValue, (String)k));
				}
				return results;
			}
			catch (Exception e) {
				return null;
			}
			
		}
		else {
			System.out.println("DADT request of average over dataview didn't receive any sensor readings");	// [*]
			return null;
		}
	}
	
	public void resetAll(ExpressionTree expTree)  {
		
		//=== DADT request ===
		
		// perform a request 
		String action = "DSensor_reset_Action";
		
		// get DADTClassName
		String DADTClassName = this.getClass().getName();
		
		dadtMgr.performDADTRequest("all", expTree, action, DADTClassName);
	
	}
	
	/**
	 * Appends sensor readings in the current list of data readings 
	 * 
	 * @param vector Sensor reading data to be added into the list of readings
	 */
	public void collectReadings(Vector vector, int sender) {
		if (readings == null) {
			readings = new Vector();
		}
		
		int index = readings.size();
		
		for (Enumeration e = vector.elements(); e.hasMoreElements();)
		{
			readings.insertElementAt(e.nextElement(), index ++);
		}
	}

	/**
	 * Clears list of sensor readings, can be used when requests are repeated over the period of time 
	 */
	public void clearReadings() {
		if ((readings != null) && (!readings.isEmpty())) 
		{
			readings.removeAllElements();
		}	
	}
	
}
