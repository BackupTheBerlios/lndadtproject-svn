/*
 * Created on Jul 01, 2008
 * @author G.Khasanova
 */
package unitn.dadt.LNSupport;

/*
import polimi.ln.nodeAttributes.Node;
import polimi.ln.runtime.LogicalNeighborhoods;
*/
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.Datagram;

import unitn.dadt.LNSupport.LNSupportRequestMsg;

import com.sun.spot.io.j2me.radiogram.RadiogramConnection;

import unitn.dadt.internals.CompleteView;
import unitn.dadt.internals.ResultData;
import unitn.dadt.internals.Action;
import unitn.dadt.internals.DADTMgr;

/**
 * Node manager (ADTs) that provides link between DADT layer and LN communication layer on the nodes. 
 * @author G.Khasanova
 */
public class NodeMgr extends DADTMgr{
    
	protected LNADTIdentification adtMgrID; 					// ID of the ADT instance
	    
	protected NodeMgr spaceMgr;
	
	
	/**
	 * Constructor of the node manager
	 * Specifies ID of the ADT instance
	 */
	public NodeMgr(){
		adtMgrID = new LNADTIdentification();
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
    public void processRequestMsg(Object reqMsg /*, Vector ADTinstances */, RadiogramConnection rCon, Datagram dg, Datagram dgReply) {
    								
    	try
    	{							
			
			Vector resultList = new Vector();
			
			// collect parameters of the request
			Action reqAction = ((LNSupportRequestMsg)reqMsg).getAction();					// action to be executed
				//System.out.println("reqAction = " + reqAction.toString());
			
			CompleteView DADTview = ((LNSupportRequestMsg)reqMsg).getDADTView();						// defined DADT dataview
				//System.out.println("dataview = " + dataview.toString());
			
			String DADTClassName = 	((LNSupportRequestMsg)reqMsg).getDADTClassName();					// requested DADT
				//System.out.println("DADTClassName = " + DADTClassName);
			
				
			Vector reqADTInstances = DADTview.getDataView().filterMatchingInstances(super.getInstances(DADTClassName));		//ADT instances which satisfy the given DADT
			
			
			for (Enumeration e = reqADTInstances.elements(); e.hasMoreElements(); ) {
			    resultList.addElement((ResultData) reqAction.evaluate(e.nextElement())); 		// perform action over selected ADT instances
			}	

			
		    /*//temporary while LN is not available
		    sendReplyMsg(((LNSupportRequestMsg) reqMsg).getSender(), resultList); //, ln, nodeInfo);	// send reply message
		    */			
			
			dgReply.reset();
			dgReply.setAddress(dg);
        	
			Enumeration en = resultList.elements();
			   
			if (en.hasMoreElements()) {
				
		        for (Enumeration e = resultList.elements(); e.hasMoreElements(); ){ 
		        	
		        	ResultData el = (ResultData) e.nextElement(); 
		        	if (el != null){
			        	double data = el.getData();
			        	String src = el.getSource();
	
			        	System.out.println("Sending: " + data + ", " + src);
			        	
			            dgReply.writeDouble(data);		            	
			            dgReply.writeUTF(src);
		        	}
		     	}
		        //rCon.send(dgReply);
		        
			}
			else {
				System.out.println("Nothing to send");
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