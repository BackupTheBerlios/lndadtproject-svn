package unitn.dadtln.samples;


import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;


import polimi.ln.runtime.LNDeliver;

import unitn.dadt.LNSupport.*;
import unitn.dadt.internals.ExpressionTree;
import unitn.dadt.internals.ResultData;


/*
 * PC-based aplication which requests sensor nodes in the WSN to provide data 
 * according to the DADT dataview definition
 */
public class ClientNode implements LNDeliver {

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
		ds.clearReadings();							

		
		//runRequest();
				
		int requestsNum = 1;
 
		for (int i = 0; i < requestsNum; i++) {
			
			ExpressionTree expTree = new ExpressionTree(new DSensor_isOfType_Property(Sensor.TEMP));
					
			double reqResult = ds.average(expTree); 
			
			if( reqResult != Integer.MIN_VALUE) {
				System.out.println("DADT request returned average value = " + reqResult);			

				ds.clearReadings();													
			}
			else
				System.out.println("DADT request was successfully executed");				
		}	
		System.exit(0);
	}

	public void deliver(byte[] data) {
		
		ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
		DataInputStream deserializer = new DataInputStream(byteStream);
		String msgType = null;
		try {
			msgType = deserializer.readUTF();
			if (msgType == "LNSupportReplyMsg") 
			{
				// manual "serialization" of the recieved array of bytes
				LNSupportReplyMsg replyMsg = new LNSupportReplyMsg(deserializer);
			
				System.out.println("Received a reply from " + replyMsg.getSource());
				
				// collection of reading to be used further
				ds.collectReadings(replyMsg.getReadings(), replyMsg.getSource());
			}
			else 
				System.out.println("Unknown message type");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
}
