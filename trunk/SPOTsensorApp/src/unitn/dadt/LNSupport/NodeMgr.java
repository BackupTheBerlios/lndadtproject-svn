/*
 * Created on Jul 01, 2008
 * @author G.Khasanova
 */
package unitn.dadt.LNSupport;

import polimi.ln.nodeAttributes.Node;
import polimi.ln.runtime.LNDeliver;
import polimi.ln.runtime.LogicalNeighborhoods;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.Datagram;

import unitn.dadt.LNSupport.LNSupportRequestMsg;

import com.sun.spot.io.j2me.radiogram.RadiogramConnection;

import unitn.dadt.internals.CompleteView;
import unitn.dadt.internals.ResultData;
import unitn.dadt.internals.Action;
import unitn.dadt.internals.DADTMgr;
import unitn.dadtln.samples.SensorNode;

/**
 * Node manager (ADTs) that provides link between DADT layer and LN communication layer on the nodes. 
 * @author G.Khasanova
 */
public class NodeMgr extends DADTMgr{
    
	protected LNADTIdentification adtMgrID; 					// ID of the ADT instance
	protected NodeMgr spaceMgr;

	private int sensorNodeId;
	private LogicalNeighborhoods ln;
	
	
	/**
	 * Constructor of the node manager
	 * Specifies ID of the ADT instance
	 */
	public NodeMgr(SensorNode sensorNodeAbstraction){
		adtMgrID = new LNADTIdentification();
		
		sensorNodeId = System.getProperty("IEEE_ADDRESS").hashCode();
		
		
		// Instantiating the logical node
		Node sensorDevice = new Node(sensorNodeId, sensorNodeAbstraction.setLNAttributes(), 1);
		// Setting up the LN run-time using the logical node instance
		// previously instantiated
		ln = new LogicalNeighborhoods(sensorDevice);
		ln.setReceiver(sensorNodeAbstraction);
	}
	
	public NodeMgr(boolean isSpaceADTsAvailable) {
		adtMgrID = new LNADTIdentification();
		
		if (isSpaceADTsAvailable) 
		{
			/*
			spaceMgr = new NodeMgr();
			super.Initialize(spaceMgr); // no use of space is available currently
			 */			
			//super.setSpaceADT(new Host(), spaceDADTClass);
		}	
	}
	
	/**
	 * Binding of the ADT instance to DADT Class, by registering ADT instance object in the binding registry
	 * 
	 * @param adtInstance ADT instance to be binded
	 * @param DADTClassName	Name of the DADT class
	 */
	public void bind(Object adtInstance, String DADTClassName) { 
		super.bind(adtInstance, DADTClassName);
	}

    /**
	 * Unbinding of the ADT instance from DADT Class, by unregistering ADT instance object in the binding registry
	 * 
	 * @param instance ADT instance to be unbinded
	 * @param DADTClassName	Name of the DADT class
     */
    public void unbind(Object adtInstance, String DADTClassName) {
    	super.unbind(adtInstance, DADTClassName);
    }

   
    /**
     * This method reads the requestMsg, identifies an object Action to be executed over 
     * the ADT instance (Sensor), perform this action, collects the results over all ADTinstances 
	 *
	 * @param reqMsg Request message
	 * @param ADTinstances ADT instances of the sensor node
     * @param ln Logical neighbourhood object
	 * @param nodeInfo debug information about simulated node
	 */
    public void processRequestMsg(LNSupportRequestMsg reqMsg, Action reqAction, CompleteView DADTview ) {
    								
    	try
    	{							
    		// read requested DADT
    		String DADTClassName = 	reqMsg.getDADTClassName();		
    	
    		
    		
    		//filter ADT instances which satisfy the given DADT View
			Vector reqADTInstances = DADTview.getDataView().filterMatchingInstances(super.getInstances(DADTClassName));		
    		//Vector reqADTInstances = super.getInstances(DADTClassName);
    		
    		
			//perform required DADT Action for the selected ADT instances
			Vector resultList = null;
			if (!reqADTInstances.isEmpty()){
				resultList = new Vector();
			}
			
			for (Enumeration e = reqADTInstances.elements(); e.hasMoreElements(); ) {
				Object element = e.nextElement();
				
				resultList.addElement((ResultData) reqAction.evaluate(element)); 		
			}	
			
			// if DADT Action requires sending a reply - proceed with constructing reply message 
			if (resultList != null) {
				
				sendReplyMsg(reqMsg.getSender(), resultList, ln);	// send reply message
			}
		
    	} catch (Exception e) {
    		System.out.println("processRequestMsg " + e.getMessage());
    	}
	  	
    }
	
	/**
	 * Sends reply message containing sensor readings back to the node-requester 
	 * @param resultList List with sensor readings
	 * @param nodeInfo debug information about simulated node
	 * @param 
	 */
    
    
    private void sendReplyMsg(int destNodeId, Vector resultList, LogicalNeighborhoods ln) {
		
		//---- debug message
		/*
    	for(Enumeration e = resultList.elements(); e.hasMoreElements(); )
		{
			System.out.println("I'm a sensor node and my reading is (" + ((ResultData)(e.nextElement())).getSource() + 
								"," + ((ResultData)(e.nextElement())).getData() + ") ");
		}
		*/
		//----
		
	
		// construct reply message
		LNSupportReplyMsg replyMsg = new LNSupportReplyMsg(sensorNodeId, resultList); 
		
		// send reply message over LN
		ln.sendReply(replyMsg.toByteArray(), destNodeId);	
		
	}
	
	
}
