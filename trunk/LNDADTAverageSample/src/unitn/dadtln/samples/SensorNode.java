package unitn.dadtln.samples;



import java.util.Collection;

import java.util.Vector;


import polimi.ln.nodeAttributes.*;

import polimi.ln.runtime.LNDeliver;
import polimi.ln.runtime.LogicalNeighborhoods;
import polimi.ln.runtime.messages.ReplyMsg;

import DADT.LNSupport.NodeMgr;
import DADT.LNSupport.LNSupportRequestMsg;

/*
 * SensorNode represents a sensor node of the WSN which can have multiple sensors (temperature,
 * pressure, light, etc.). These multiple sensors are referred as ADT instances of the Sensor Node.
 */
public class SensorNode implements LNDeliver {

	private Node info;		 // information about the sensor node being simulated in SWANS 	
	private LogicalNeighborhoods ln;
	
	public Collection ADTinstances; // collection of ADT instances belonging to the sensor node
	private NodeMgr ADTmgr = new NodeMgr(); // ADT manager that coordinates sensors' readings of ADT instances  
	
	/**
	 * Constructor for Sensor node
	 * Specifies ADT instances of the sensor node and bind them into the DADT type 
	 */
	public SensorNode() {
		
		ADTinstances.add(new Sensor[]{new Sensor(Sensor.TEMP, 1.0), 
									  new Sensor(Sensor.PRESSURE, 2.0)});
		
		for (Object s : ADTinstances)
	    	ADTmgr.bind((Sensor)s, "unitn.dadtln.samples.DSensor");
		
	}

	/**
	 * Constructor for Sensor node
	 * Uses given ADT instances (sensors) of the sensor node to bind them into the DADT type 
	 * Used for simulation
	 * @param sensors collection of ADT instances of the Sensor Node
	 */
	public SensorNode(Collection sensors) {
		ADTinstances = sensors;

		for (Object s : ADTinstances)
	    	ADTmgr.bind((Sensor)s, "unitn.dadtln.samples.DSensor");
	}	
	
	/**
	 * Provides node information for the node simulated in SWANS
	 * @param info
	 * @param ln 
	 */
	public void setSimNodeInfo(Node info, LogicalNeighborhoods ln) {
		this.info = info;
		this.ln = ln;
	}	
	
	/**
	 * Specifies attributes of the ADT instances (over LN)
	 */
	public Vector setLNAttributes() {
		
		Vector attributes = new Vector(); // attributes of the sensor node (and its instances) to be used by LN layer
	
		Object[] onBoardSensors = new Object[ADTinstances.size()];
	
		int idx = 0;
		for (Object s : ADTinstances) 
		{
	    	((Sensor)s).collectAttributesForLN(attributes);	// collects all attributes among all ADT instances of SensorNode
		
	    	onBoardSensors[idx] = Sensor.typeToStr(((Sensor)s).type);
			idx ++;
		}
		
		attributes.add(new SetAttribute("OnBoardSensors", onBoardSensors));
    	
		return attributes;
	}
	
	/**
	 * Processing of the request message sent by client node. 
	 * Message is being forwarde to ADT manager, where action over multiple ADT instances is executed 
	 * @param msg a request message sent by client node
	 */
	public void deliver(Object msg) {
		
		if (msg instanceof LNSupportRequestMsg) 
		{
			//get sensor readings (perform specified action) from ADTinstances 
			ADTmgr.processRequestMsg(msg, /*ADTinstances,*/ ln, info); // process request message 
			
			
		} 
		else if (msg instanceof ReplyMsg) 
		{
			info.debugPrint("Sensor Node: Got reply message!");
		}
		
		
		
	}
}
