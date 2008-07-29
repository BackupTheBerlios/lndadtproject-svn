/*
 * Created on Aug 31, 2005
 *
 */
package DADT.IPMulticast;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

///import sensorExample.Mica;

import DADT.Action;
import DADT.CompleteView;
import DADT.DADTMgr;
import DADT.InvocationData;
import DADT.ResultData;
import DADT.SelectionOperator;
import DADT.SelectionOperatorIntf;
/**
 * @author migliava
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IPMAll {
	//!! public class IPMAll extends SelectionOperator {
	
	protected Action action;
	DatagramSocket out = null;
	/**
     * @param action
     */
    public IPMAll(Action action) {
        
    	this.action = action; //!!    	super(action);						// initialise with a defined action
		
        try 
		{
		  //!! out = new DatagramSocket();		// a socket to be used for a multcast sending    
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
    }
    
    /** 
     * the cast should be done by the translator. 
     * @return an Object containing a Collection <ResultData>
     */

    public Object performRemote(CompleteView view) {
        try 
        {
            String groupName = view.getDataView().getDADTClass().getName(); 	// read groupname
            
            InetSocketAddress dest = new InetSocketAddress(Algorithms.hash(groupName), 
            		IPMDADTMgr.IPM_DADT_MGR_MULTICASTPORT); 					//calculate an address of the socket
            
            DatagramPacket request = Algorithms.makeUDPPacketFromObject(		//create a datagram packet 
                    new IPMSelectionOPMessage(this.getClass().getName(), view,
                            action, out.getLocalSocketAddress()), dest);
            
            //DatagramSocket replies = new DatagramSocket(group.getPort() + 1,
            //		group.getAddress());
            
            //TODO we do not take the space view into consideration for routing
            
            
          //!!  out.send(request);
            IPMDADTMgr.l.info("Sent IPALL message to " + groupName + " at address" + dest);
            
            //FIXME is there a race condition? Should we listen before we send? 
          //!! Collection resultData = Algorithms.collectAndConsolidateReplies(out); 
            
          //!! return resultData;
            return null;
            
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public void performLocal(InvocationData message) {
        
//      INVOKE THE RELATIONS;
      
        //DANGER we do not take the space view into consideration for filtering!!!
        //(actually we do it here in all but never in the other operators.... we should fix
        //them (and refactor this)
           	
        if (message.getSv()!=null && !(message.getSv().isMember(DADTMgr.getSpaceADT()))) return;
        
		Collection localADTInstances = DADTMgr.mgr.getInstances(message.getDv().getDADTClass().getName());
		Collection instancesInDataview = message.getDv().filterMatchingInstances(localADTInstances);

        Serializable replies = (Serializable) performActionLocally(instancesInDataview);
		
		DADTMgr.l.fine("Ho processato il messaggio sto preparando la risposta");
		DADTMgr.l.info("Ho processato il messaggio sto preparando la risposta");
		
		
		// SEND OBJECTS
		DatagramPacket reply = Algorithms.makeUDPPacketFromObject(
		        replies, ((IPMSelectionOPMessage)message).sender);
		//reply.setPort(5001);
		DADTMgr.l.fine("sto per inviare la risposta Membership");
		DADTMgr.l.info("sto per inviare la risposta Membership");
		try {
            out.send(reply);
            DADTMgr.l.info("risposta Membership inviata");
        } catch (IOException e) {
            //TODO auto generated bla bla
            e.printStackTrace();
        }
    }

	/* (non-Javadoc)
	 * @see DADT.SelectionOperatorIntf#performActionLocally(java.util.Collection)
	 */
	public Collection performActionLocally(Collection ADTs) {
		LinkedList<ResultData> resultList = new LinkedList<ResultData>();
        Iterator iter = ADTs.iterator();
        while (iter.hasNext()) {
            Object el = (Object) iter.next();
            resultList.add(new ResultData(action.evaluate(el),DADTMgr.mgr.getAdtIdentificator().getID(el)));
        }
        return resultList;
	}
}


