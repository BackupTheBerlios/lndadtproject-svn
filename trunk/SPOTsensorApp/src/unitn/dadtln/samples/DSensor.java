package unitn.dadtln.samples;


import unitn.dadt.internals.*;
import unitn.dadt.LNSupport.DistrNodeMgr;
import unitn.dadt.LNSupport.LNSupportMsgTypes;
import unitn.dadt.LNSupport.LNSupportReplyMsg;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import polimi.ln.nodeAttributes.Node;
import polimi.ln.runtime.LNDeliver;
import polimi.ln.runtime.LogicalNeighborhoods;



/**
 * This class describes behavior of the Distributed Sensor (DADTview on the WSN). 
 * Provides methods of aggregating sensor data
 * Uses Logical neighbourhood model to perform communication between distributed nodes (simulated in SWANS)
 *  
 */
public class DSensor implements LNDeliver {
	
	private Class distributes = Sensor.class;			// reference to the sensors, which are distributed around the field
	private boolean spaceType = false;					// 
	
	private Vector readings;							// data received from distributed sensors
	private DistrNodeMgr dadtMgr; 	// dadt manager that is responsible for interaction with communication layer

	
	/*
	 * DSensor constructor
	 */
	public DSensor(){
		dadtMgr = new DistrNodeMgr();
		assignLN();
	}
	
	/** 
	 * Performs aggregation of data received from distributed sensors
	 * @param dataviewProperties defines a dataview on the sensors (conditions that specify if sensor's reading is relevant)
	 * @param pcNodeId specifies Id of the node that requests data (and connected with PC-application)
	 * @return average value of sensor readings
	 */
	public double average(ExpressionTree expTree) {
		
		//=== DADT request ===
		
		// perform a request 
		String action = "DSensor_read_Action";
		
		// get DADTClassName
		String DADTClassName = this.getClass().getName();
		
		dadtMgr.performDADTRequest("all", expTree, action, DADTClassName);
		
		//=== waiting for WSN reply ===
		
		// after request was sent into WSN, we wait over a timeout to collect replies from the distributed sensors 
		// we assume that within the timeout all nodes in the neighbourhood can reply
		try
		{
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
		
		
		if (readings != null) 
		{
			return processCollectedData();
		}
		else 
		{
			System.out.println("DADT request of average over dataview didn't receive any sensor readings");	// [*]
			return Integer.MIN_VALUE;
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
	
	private double processCollectedData(){
	
		
		double sensorReading = 0;
		
		for (Enumeration e = readings.elements(); e.hasMoreElements(); ) 
		{	
			sensorReading += ((ResultData)e.nextElement()).getData();
		}
		
		try 
		{
			return sensorReading / readings.size();
		}
		catch (Exception e) 
		{
			return Integer.MIN_VALUE;
		}
	}
	
	
	public void deliver(byte[] data) {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
		DataInputStream deserializer = new DataInputStream(byteStream);
		int msgType;
		try {

			msgType = deserializer.readInt();
			if (msgType == LNSupportMsgTypes.LNSupportReplyMsg) 
			{
				System.out.println("debug (deliver) 4");
				// manual "serialization" of the received array of bytes
				LNSupportReplyMsg replyMsg = new LNSupportReplyMsg(deserializer);
			
				System.out.println("Received a reply from " + replyMsg.getSource());
				
				// collection of reading to be used further
				collectReadings(replyMsg.getReadings(), replyMsg.getSource());
			}
			else 
				System.out.println("Unknown message type");    
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
	public void assignLN(){
		dadtMgr.ln = new LogicalNeighborhoods(new Node(dadtMgr.lnClientNodeId, new Vector(), 1));
		dadtMgr.ln.setReceiver(this);
	}
}
