package unitn.dadtln.samples;


import java.util.Vector;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import unitn.dadt.internals.ExpressionTree;



/*
 * PC-based aplication which requests sensor nodes in the WSN to provide data 
 * according to the DADT dataview definition
 */
public class ClientNodeOnSPOT extends MIDlet {

	
	private static DSensor ds;
	
	
	/**
	 * Support for requests to WSN, has been tested in SWANS simulator
	 * @param args 	
				
	 */

	public static void main(String[] args)  {

		
		ds = new DSensor(); 						// distributed Sensor data type (DADT)
		
		ExpressionTree expTree = new ExpressionTree(new DSensor_isOfType_Property(Sensor.TEMP));
		/*
		 new ExpressionTree(new DSensor_isOfType_Property(Sensor.TEMP))
							.or(new ExpressionTree(new DSensor_isOfType_Property(Sensor.LIGHT)));
		
		new ExpressionTree(new DSensor_isOfType_Property(Sensor.TEMP))
							.and(new ExpressionTree(new DSensor_isActive_Property()));
		
		
		new ExpressionTree(new DSensor_isOfType_Property(Sensor.TEMP))
							.and(new ExpressionTree(new DSensor_isActive_Property()))
						   .or(new ExpressionTree(new DSensor_isOfType_Property(Sensor.LIGHT))
							.and(new ExpressionTree(new DSensor_isPrecise_Property(1.0)))));
		 */
		
		
		double reqResult = ds.average(expTree); 
		
		if( reqResult != Integer.MIN_VALUE) 
			System.out.println("DADT request returned average value = " + reqResult);			
		else
			System.out.println("DADT request was successfully executed");				
		
		//ds.resetAll(expTree);
		
		//System.exit(0);
	}


	
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		
	}


	protected void pauseApp() {
		// TODO Auto-generated method stub
		
	}

	protected void startApp() throws MIDletStateChangeException {
		main(null);
	}

}
