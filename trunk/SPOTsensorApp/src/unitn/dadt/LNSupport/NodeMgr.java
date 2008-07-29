/*
 * Created on Jul 01, 2008
 * @author G.Khasanova
 */
package unitn.dadt.LNSupport;

/* javaME doesn't support any of these
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Logger;
*/

/*
import polimi.ln.nodeAttributes.Node;
import polimi.ln.runtime.LogicalNeighborhoods;
*/
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.Datagram;

import com.sun.spot.io.j2me.radiogram.RadiogramConnection;

import unitn.dadt.internals.BindingRegistry;
import unitn.dadt.internals.DataView;
import unitn.dadt.internals.ResultData;
import unitn.dadt.internals.Action;

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
    	registry.unregister(instance,DADTClassName);
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
    /* javeME doesn't support Collections
	public void processRequestMsg(Object reqMsg, Collection ADTinstances, LogicalNeighborhoods ln, Node nodeInfo) {
		
		LinkedList<ResultData> resultList = new LinkedList<ResultData>();
		
		// collect parameters of the request
		Action reqAction = ((LNSupportRequestMsg)reqMsg).getAction();					// action to be executed
		DataView dataview = ((LNSupportRequestMsg)reqMsg).getDataView();					// defined DADT dataview
		Collection reqADTInstances = dataview.filterMatchingInstances(ADTinstances);		//ADT instances which satisfy the given DADT
		
		for (Object s : reqADTInstances) {
			resultList.add((ResultData) reqAction.evaluate(s)); 		// perform action over selected ADT instances
		}	
		
		sendReplyMsg(((LNSupportRequestMsg) reqMsg).getSender(), resultList, ln, nodeInfo);	// send reply message
  	}
  	*/
    public void processRequestMsg(Object reqMsg /*, Vector ADTinstances */, RadiogramConnection rCon, Datagram dg, Datagram dgReply) {
    								
    	try
    	{							
			
			Vector resultList = new Vector();
			
			// collect parameters of the request
			Action reqAction = ((LNSupportRequestMsg)reqMsg).getAction();					// action to be executed
				System.out.println("reqAction = " + reqAction.toString());
			
			DataView dataview = ((LNSupportRequestMsg)reqMsg).getDataView();					// defined DADT dataview
				System.out.println("dataview = " + dataview.toString());
			
			String DADTClassName = 	((LNSupportRequestMsg)reqMsg).getDADTClassName();					// requested DADT
				System.out.println("DADTClassName = " + DADTClassName);
			
				
			Vector reqADTInstances = dataview.filterMatchingInstances(registry.getLocalInstances(DADTClassName));		//ADT instances which satisfy the given DADT
			
			
			for (Enumeration e = reqADTInstances.elements(); e.hasMoreElements(); ) {
			
				resultList.addElement((ResultData) reqAction.evaluate(e.nextElement())); 		// perform action over selected ADT instances
			}	

			dgReply.reset();
        	dgReply.setAddress(dg);
        	
			if (resultList != null) {
		        
				System.out.println(resultList.size());
		        
		        for (Enumeration e = resultList.elements(); e.hasMoreElements(); ){ 
		        	ResultData el = (ResultData) e.nextElement(); 

		        	double data = el.getData();
		        	String src = el.getSource();
		        	
		        	System.out.println("Sending: " + data + ", " + src);
		        	
		            dgReply.writeDouble(data);
		            	
		     		dgReply.writeUTF(src);
		     		
		     		//System.out.println(el.getData() + ", " + el.getSource());
		        }
		        
			}
			else {
				System.out.println("Nothing to send");
			}
		
	  		rCon.send(dgReply);
		
		    /*//temporary while LN is not available
		    sendReplyMsg(((LNSupportRequestMsg) reqMsg).getSender(), resultList); //, ln, nodeInfo);	// send reply message
		    */
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
	  	
    }
	
	/**
	 * Sends reply message containing sensor readings back to the node-requester 
	 * @param resultList List with sensor readings
	 * @param nodeInfo debug information about simulated node
	 * @param 
	 */
    
    /* javaME doesn't support Collection
    private void sendReplyMsg(int destNodeId, LinkedList<ResultData> resultList, LogicalNeighborhoods ln, Node nodeInfo) {
		
		//---- debug message
		for(int i = 0; i < resultList.size(); i++)
		{
			nodeInfo.debugPrint("I'm a sensor node and my reading is (" + resultList.get(i).getSource() + "," + resultList.get(i).getData() + ") ");
		}
		//----
		
		ln.sendReply(new LNSupportReplyMsg(nodeInfo.getMyId(), resultList), destNodeId);	// send reply message over LN
	
	}
	*/
    
    /*
    private void sendReplyMsg(int destNodeId, Vector resultList , LogicalNeighborhoods ln, Node nodeInfo) {
		
		//---- debug message
		for(Enumeration e = resultList.elements(); e.hasMoreElements(); )
		{
			System.out.println("I'm a sensor node and my reading is (" + ((ResultData)(e.nextElement())).getSource() + 
								"," + ((ResultData)(e.nextElement())).getData() + ") ");
		}
		//----
		
		
		ln.sendReply(new LNSupportReplyMsg(nodeInfo.getMyId(), resultList), destNodeId);	// send reply message over LN
		
	}
	*/
	
}
