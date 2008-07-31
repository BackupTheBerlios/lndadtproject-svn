package unitn.dadtln.samples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import jist.runtime.JistAPI;

import polimi.ln.examples.swans.SimulationReferences;
import polimi.ln.neighborhoodDefs.AtomicPredicate;
import polimi.ln.neighborhoodDefs.SetMembershipPredicate;
import polimi.ln.nodeAttributes.Node;
import polimi.ln.runtime.LNDeliver;

import DADT.*;
import DADT.LNSupport.LNSupportReplyMsg;


/*
 * Simulated node, that executes distributed application. This application
 * requests simulated sensor nodes in the simulated WSN to provide data 
 * in the scope defined by the distributed data view.
 */
public class ClientNode implements LNDeliver {
	
	private static ArrayList<Property[]> simDataviewProperties = null;
	private static ArrayList simDADTdataview = null;
	
	
	private Node info;		// information about the "client" node being simulated in SWANS
	private static DSensor ds;
	
	
	public static void InitClientNodeSimData() {
		
		
		simDADTdataview = new ArrayList<ExpressionTree>();

		
		simDADTdataview.add(new ExpressionTree(new DSensor_isOfType_Property(Sensor.TEMP)));
	
		/*
		simDADTdataview.add(new ExpressionTree(new DSensor_isOfType_Property(Sensor.TEMP))
							.or(new ExpressionTree(new DSensor_isOfType_Property(Sensor.LIGHT))));
		
		simDADTdataview.add(new ExpressionTree(new DSensor_isOfType_Property(Sensor.TEMP))
							.and(new ExpressionTree(new DSensor_isActive_Property())));
		
		
		simDADTdataview.add((new ExpressionTree(new DSensor_isOfType_Property(Sensor.TEMP))
							.and(new ExpressionTree(new DSensor_isActive_Property()))
						   .or(new ExpressionTree(new DSensor_isOfType_Property(Sensor.LIGHT))
							.and(new ExpressionTree(new DSensor_isPrecise_Property(1.0))))));
							
							
		simDADTdataview.add((new ExpressionTree(new DSensor_isOfType_Property(Sensor.TEMP))
							.and(not(new ExpressionTree(new DSensor_isActive_Property())))
						   .or(new ExpressionTree(new DSensor_isOfType_Property(Sensor.LIGHT))
							.and(not(new ExpressionTree(new DSensor_isActive_Property()))))));				// reset all not active temp and light sensors	
		*/
	}
	
	
	/**
	 * Provides node information for the node simulated in SWANS
	 * @param info
	 */
	public void setSimNodeInfo(Node info) {
		this.info = info;
	}
	
	/**
	 * Support for requests to WSN, has been tested in SWANS simulator
	 * @param args 	0 - Simulation Support: "requestNetwork" application unique name
	 * 				1 - identifier of client node,
					2 - number of request messages to be sent
					3 - timeout period between request messages
				
	 */
	
	public static void main(String[] args)  {
		
			SimulationReferences.getNodeInfo(Integer.parseInt(args[1])).debugPrint("Client node start");//[*]
		
		InitClientNodeSimData();
		int requestsNum;
		try {
			requestsNum = Integer.parseInt(args[2]);
		} 
		catch (Exception e) {	
			requestsNum =1;
		}

		// for simulation purposes (signed with [*]) we use a list of pre-defined properties 
		for (int i = 0; i < requestsNum; i++) {
			ds = new DSensor(); 						// distributed Sensor data type (DADT)
			ds.clearReadings();													// [*]

			int expIndex = 0;														// [*]  
			ExpressionTree expTree = (ExpressionTree) simDADTdataview.get(expIndex);// [*] 	// Specify DADT dataview
			// ExpressionTree expTree = new ExpressionTree(new DSensor_isOfType_Property(Sensor.TEMP))
					
			// calculate average readings, done by means of DADT over specified dataview
			int nodeSelfId = Integer.parseInt(args[1]); // read nodeId 
	 		
			HashMap reqResult = ds.average(expTree, nodeSelfId); // perform DADT request 
			
			if( reqResult != null) {
				System.out.println("-----");														// [*]
				
				System.out.println("DADT request of average over dataview returned: ");				// [*]
				for (Object k: reqResult.keySet())
				{
					System.out.println("Sensor type = " + (String)k + ", average value = " + reqResult.get(k));
				}
				
				
				System.out.println("======\n");														// [*]
			}
			
			// if request is executed periodically - set a timeout between requests
			try {
				Thread.sleep(Integer.parseInt(args[3]));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
			
		JistAPI.end();
	}

	/*
	 *  (non-Javadoc)
	 * @see polimi.ln.runtime.LNDeliver#deliver(java.lang.Object)
	 */
	public void deliver(Object msg) {
		
		if (msg instanceof LNSupportReplyMsg) 
		{
			info.debugPrint("Received a reply from " + ((LNSupportReplyMsg)msg).getReadingsSender());
			ds.collectReadings( ((LNSupportReplyMsg)msg).getReadings(), ((LNSupportReplyMsg)msg).getReadingsSender());
			
		}
		else 
		{
			info.debugPrint("Unknown message type");
		}
	}
	
    

}
