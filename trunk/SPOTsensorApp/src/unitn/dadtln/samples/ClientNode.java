package unitn.dadtln.samples;


import java.util.Vector;

import unitn.dadt.internals.ExpressionTree;



/*
 * PC-based aplication which requests sensor nodes in the WSN to provide data 
 * according to the DADT dataview definition
 */
public class ClientNode {

	
	private static DSensor ds;
	
	//--- temp hacks
	private static Vector simDADTdataview = new Vector();

    //---
	
	public static void InitClientNodeSimData() {
			
		simDADTdataview.addElement(new ExpressionTree(new DSensor_isOfType_Property(Sensor.TEMP)));
	/*
		simDADTdataview.add(new ExpressionTree(new DSensor_isOfType_Property(Sensor.TEMP))
							.or(new ExpressionTree(new DSensor_isOfType_Property(Sensor.LIGHT))));
		
		simDADTdataview.add(new ExpressionTree(new DSensor_isOfType_Property(Sensor.TEMP))
							.and(new ExpressionTree(new DSensor_isActive_Property())));
		
		
		simDADTdataview.add((new ExpressionTree(new DSensor_isOfType_Property(Sensor.TEMP))
							.and(new ExpressionTree(new DSensor_isActive_Property()))
						   .or(new ExpressionTree(new DSensor_isOfType_Property(Sensor.LIGHT))
							.and(new ExpressionTree(new DSensor_isPrecise_Property(1.0))))));
		*/
	}
	
	
	/**
	 * Support for requests to WSN, has been tested in SWANS simulator
	 * @param args 	
				
	 */

	public static void main(String[] args)  {

		
		ds = new DSensor(); 						// distributed Sensor data type (DADT)
		
		ExpressionTree expTree = new ExpressionTree(new DSensor_isOfType_Property(Sensor.TEMP));
				
		double reqResult = ds.average(expTree); 
		
		if( reqResult != Integer.MIN_VALUE) 
			System.out.println("DADT request returned average value = " + reqResult);			
		else
			System.out.println("DADT request was successfully executed");				
		
		System.exit(0);
	}

}
