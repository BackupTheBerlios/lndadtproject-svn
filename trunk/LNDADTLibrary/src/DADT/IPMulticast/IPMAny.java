/*
 * Created on Sep 6, 2005
 *
 */
package DADT.IPMulticast;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;

import DADT.Action;
import DADT.CompleteView;
import DADT.DADTMgr;
import DADT.Functions;
import DADT.InvocationData;
import DADT.SelectionOperator;


/**
 * @author migliava
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IPMAny extends SelectionOperator {

    DatagramSocket out;
    
    /**
     * @param action
     */
    public IPMAny(Action action) {
        super(action); 
        try {
            out = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see DADT.Operator#run(DADT.CompleteView)
     */
    public Object performRemote(CompleteView view) {
        String groupName = view.getDataView().getDADTClass().getName();
        try {
        			InetSocketAddress dest = new InetSocketAddress(Algorithms
        			        .hash(groupName), IPMDADTMgr.IPM_DADT_MGR_MULTICASTPORT);
        			DatagramPacket request = Algorithms.makeUDPPacketFromObject(
        			        new IPMSelectionOPMessage(this.getClass().getName(), view,
        			                action, out.getLocalSocketAddress()), dest);


        			//HERE IT SHOULD BE USEFULT TO USE A ANYCAST ROUTING ALGORITHM IF 
        			//DatagramSocket replies = new DatagramSocket(group.getPort() + 1,
        			//		group.getAddress());
        			out.send(request);
        			//l.info("Sent IPALL message to " + groupName + " at address" + dest);
        			
        			//l.info("Buffer size:" + replies.getReceiveBufferSize());
        		
        			Collection candidateADTs = Algorithms.collectAndConsolidateReplies(out); 
        			
        			//INVOKE THE SOME OPERATOR WITH A SINGLE INSTANCE
        			Collection chosen = new ArrayList();
        			chosen.add(((IPIdentifier) (candidateADTs.iterator().next())));
        			
        			//calls the some operator to invoke the specific instance
        			return new IPMSome(action,chosen).performRemote(view);
        			
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
        		return null;
    }
    
    public void performLocal(InvocationData message) {
        
		Collection localADTInstances = DADTMgr.mgr.getInstances(message.getDv().getDADTClass().getName());
    	Collection instancesInDataview = message.getDv().filterMatchingInstances(localADTInstances);
    	
    	Collection identifiedInstances = DADTMgr.mgr.getAdtIdentificator().getIDs(instancesInDataview);

		DatagramPacket reply = Algorithms.makeUDPPacketFromObject(
		        (Serializable) identifiedInstances, ((IPMSelectionOPMessage)message).sender);
		try {
            out.send(reply);
            DADTMgr.l.info("risposta Membership inviata");
        } catch (IOException e) {
            //TODO auto generated bla bla
            e.printStackTrace();
        }
//
//        Serializable replies = (Serializable) performActionLocally(instancesInDataview);
//		
//		DADTMgr.l.fine("Ho processato il messaggio sto preparando la risposta");
//		
//		// SEND OBJECTS
//		DatagramPacket reply = Functions.makeUDPPacketFromObject(
//		        replies,(InetSocketAddress) ((IPMSelectionOPMessage)message).sender);
//		//reply.setPort(5001);
//		DADTMgr.l.fine("sto per inviare la risposta Membership");
    }

}
