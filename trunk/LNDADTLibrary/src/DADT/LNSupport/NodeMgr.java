/*
 * Created on Jul 01, 2008
 * @author G.Khasanova
 */
package DADT.LNSupport;


import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Logger;
import polimi.ln.nodeAttributes.Node;
import polimi.ln.runtime.LogicalNeighborhoods;
import DADT.BindingRegistry;
import DADT.DataView;
import DADT.ResultData;

/**
 * Node manager (ADTs) that provides link between DADT layer and LN communication layer on the nodes. 
 * @author G.Khasanova
 */
public class NodeMgr {
    
    protected BindingRegistry registry = new BindingRegistry(); // used for binding of ADT instances
	protected LNADTIdentification adtMgrID; 					// ID of the ADT instance
	
	
	/**
	 * Constructor of the node manager
	 * Specifies ID of the ADT instance
	 */
	public NodeMgr() {
		adtMgrID = new LNADTIdentification();
	}
	
	/**
	 * Binding of the ADT instance to DADT Class, by registering ADT instance object in the binding registry
	 * 
	 * @param adtInstance ADT instance to be binded
	 * @param DADTClassName	Name of the DADT class
	 */
	public void bind(Object adtInstance, String DADTClassName) { 
		registry.register(adtInstance, DADTClassName);
	}

    /**
	 * Unbinding of the ADT instance from DADT Class, by unregistering ADT instance object in the binding registry
	 * 
	 * @param instance ADT instance to be unbinded
	 * @param DADTClassName	Name of the DADT class
     */
    public void unbind(Object instance, String DADTClassName) {
    	registry.unregister(instance, DADTClassName);
    	assert registry.getLocalInstances(DADTClassName).size() > 0;
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
	public void processRequestMsg(Object reqMsg/*, Collection ADTinstances*/, LogicalNeighborhoods ln, Node nodeInfo) {		
		LinkedList<ResultData> resultList = new LinkedList<ResultData>();
		
		// collect parameters of the request
		DADT.Action reqAction = ((LNSupportRequestMsg)reqMsg).getAction();					// action to be executed
		DataView dataview = ((LNSupportRequestMsg)reqMsg).getDataView();					// defined DADT dataview
		String DADTCLassName = ((LNSupportRequestMsg)reqMsg).getDADTClassName();					// requested DADT Class
		
		Collection reqADTInstances = dataview.filterMatchingInstances(registry.getLocalInstances(DADTCLassName));		//ADT instances which satisfy the given DADT
		
		for (Object s : reqADTInstances) {
			resultList.add((ResultData) reqAction.evaluate(s)); 		// perform action over selected ADT instances
		}	
		
		sendReplyMsg(((LNSupportRequestMsg) reqMsg).getSender(), resultList, ln, nodeInfo);	// send reply message
  	}
	
	/**
	 * Sends reply message containing sensor readings back to the node-requester 
	 * @param resultList List with sensor readings
	 * @param nodeInfo debug information about simulated node
	 * @param 
	 */
	private void sendReplyMsg(int destNodeId, LinkedList<ResultData> resultList, LogicalNeighborhoods ln, Node nodeInfo) {
		
		//---- debug message
		for(int i = 0; i < resultList.size(); i++)
		{
			nodeInfo.debugPrint("I'm a sensor node and my reading is (" + resultList.get(i).getSource() + "," + resultList.get(i).getData() + ") ");
		}
		//----
		
		ln.sendReply(new LNSupportReplyMsg(nodeInfo.getMyId(), resultList), destNodeId);	// send reply message over LN
	
	}
	
}
