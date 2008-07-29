package unitn.dadtln.samples;


import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import java.util.Random;

import javax.microedition.io.Connector;
import javax.microedition.io.Datagram;
import javax.microedition.io.DatagramConnection;
import javax.swing.JFrame;

import org.sunspotworld.demo.util.PacketTypes;

import com.sun.spot.io.j2me.radiogram.RadiogramConnection;
import com.sun.spot.peripheral.NoAckException;

/*
import polimi.ln.examples.swans.SimulationReferences;
import polimi.ln.neighborhoodDefs.AtomicPredicate;
import polimi.ln.neighborhoodDefs.SetMembershipPredicate;
import polimi.ln.nodeAttributes.Node;
import polimi.ln.runtime.LNDeliver;
*/

import unitn.dadt.LNSupport.*;
import unitn.dadt.internals.ExpressionTree;
import unitn.dadt.internals.ResultData;


/*
 * PC-based aplication which requests sensor nodes in the WSN to provide data 
 * according to the DADT dataview definition
 */
public class ClientNode /*implements LNDeliver */{

	//private Node info;		// information about the "client" node being simulated in SWANS
	private static DSensor ds;
	private static Vector simDADTdataview = new Vector();

	
	//private static SPOTDataHandler dataHandler = new SPOTDataHandler();
    
	private RadiogramConnection conn = null;
    private static Datagram xdg = null;

	
	public static void InitClientNodeSimData() {
			
		simDADTdataview.addElement(new ExpressionTree(new DSensor_isOfType_Property(Sensor.TEMP)));
	/*
		simDADTdataview.add(new ExpressionTree(new DSensor_isOfType_Property(Sensor.TEMP))
							.or(new ExpressionTree(new DSensor_isOfType_Property(Sensor.PRESSURE))));
		
		simDADTdataview.add(new ExpressionTree(new DSensor_isOfType_Property(Sensor.TEMP))
							.and(new ExpressionTree(new DSensor_isActive_Property())));
		
		
		simDADTdataview.add((new ExpressionTree(new DSensor_isOfType_Property(Sensor.TEMP))
							.and(new ExpressionTree(new DSensor_isActive_Property()))
						   .or(new ExpressionTree(new DSensor_isOfType_Property(Sensor.PRESSURE))
							.and(new ExpressionTree(new DSensor_isPrecise_Property(1.0))))));
		*/
	}
	
	
	/**
	 * Provides node information for the node simulated in SWANS
	 * @param info
	 */
	/*
	public void setSimNodeInfo(Node info) {
		this.info = info;
	}
	*/
	/**
	 * Support for requests to WSN, has been tested in SWANS simulator
	 * @param args 	
				
	 */

	public static void main(String[] args)  {

		InitClientNodeSimData();

		
		ds = new DSensor(); 						// distributed Sensor data type (DADT)
		ds.clearReadings();													// [*]
		
		runRequest();
				
		int requestsNum = 1;

		// for simulation purposes (signed with [*]) we use a list of pre-defined properties 
		for (int i = 0; i < requestsNum; i++) {
			
			
			int dataViewIndex = 0;														// [*]  
			ExpressionTree expTree = (ExpressionTree) simDADTdataview.get(dataViewIndex);// [*] 	// Specify DADT dataview
			// ExpressionTree expTree = ... is translated automatically from ClientNode.dadt
					
			// calculate average readings, done by means of DADT over specified dataview
				 		
			//double reqResult = ds.average(definePredicates(expTree), dataview, nodeSelfId); // perform DADT request 
			Vector reqResult = ds.average(expTree); // perform DADT request 
			
			if( reqResult != null) {
				System.out.println("-----");														// [*]
				
				System.out.println("DADT request of average over dataview returned: ");				// [*]
				
				for (Enumeration e = reqResult.elements(); e.hasMoreElements(); )
				
				{
					ResultData elem = (ResultData) e.nextElement();
					System.out.println("Sensor type = " + elem.getSource() + ", average value = " + elem.getData());
				}
				
				
				System.out.println("======\n");														// [*]
				ds.clearReadings();													// [*]
			}
			
			/*
			// if request is executed periodically - set a timeout between requests
				try {
					Thread.sleep(Integer.parseInt(args[3]));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				**/
			
		}	
		System.exit(0);
	}

	
	
	
	/*
	 *  (non-Javadoc)
	 * @see polimi.ln.runtime.LNDeliver#deliver(java.lang.Object)
	 */
	public static void deliver(Object msg) {
		
		if (msg instanceof LNSupportReplyMsg) 
		{
			LNSupportReplyMsg replyMsg = (LNSupportReplyMsg)msg;
			System.out.println(msg.toString());
			
			int src = replyMsg.getReadingsSender();
			Vector resReadings = ((LNSupportReplyMsg)msg).getReadings();


			
			System.out.println("Received a reply from " + src);
			
			try{
				ds.collectReadings(resReadings, src);
			}
			catch(Exception e){
				System.out.println("1");
				e.printStackTrace();
			}
		}
		
		else 
		{
			System.out.println("Unknown message type");
		}
	}
	
    ///===================
	private static void runRequest() {
	       
		
		
	       try {
	    	   sendCmd(PacketTypes.SEND_TEMP_DATA_REQ);
	    	   		System.out.println("!");
	    	   RadiogramConnection conn = (RadiogramConnection)Connector.open("radiogram://:" + PacketTypes.BROADCAST_PORT);
	    	   		System.out.println("!!");
	    	   Datagram xdg = conn.newDatagram(conn.getMaximumLength()); 
	    	   		System.out.println("!!!");
	    	   Vector<ResultData> replyData = new Vector<ResultData>();
	    	   
	    	   int NUMBER_OF_SPOTS = 1;
	    	   
	    	   for (int i = 0; i < NUMBER_OF_SPOTS; i++)
	    	   {
		           try 
		           {
		    		    
		        	   	// Read sensor sample received over the radio
			            conn.receive(xdg);
			            String addr = xdg.getAddress();  // read sender's Id
			            
			            double reading = xdg.readDouble();
			            String adtName= xdg.readUTF();
			            replyData.add(new ResultData(reading, adtName));
		           }
		           catch (Exception e)
		           {
		        	   System.out.println("excepton in run request");
		           }
	    	   }	
	    	   
	           	LNSupportReplyMsg replyMsg = new LNSupportReplyMsg (0, replyData);
	           	deliver(replyMsg);
	        } 
	   		catch (Exception e) {
	         	e.printStackTrace();
	        }   
		       
	    }
    private static void sendCmd (byte cmd) {
        
    	DatagramConnection conn;
		try {
			conn = (DatagramConnection)Connector.open("radiogram://broadcast:" + PacketTypes.BROADCAST_PORT);
			xdg = conn.newDatagram(conn.getMaximumLength());
		
    	
	    	if (conn != null) {
	        	System.out.println("realSendCmd, Command " + cmd);
	            try {
	                xdg.reset();
	                xdg.writeByte(cmd);
	                conn.send(xdg);
	                
	            } catch (NoAckException nex) {
	                
	            } catch (IOException ex) {
	                // ignore any other problems
	            }
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
