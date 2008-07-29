/*
 * Created on Oct 3, 2005
 *
 */
package DADT.IPMulticast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import DADT.CompleteView;
import DADT.DADTMgr;
import DADT.Operator;
import DADT.InvocationData;

/**
 * @author migliava
 */
public class IPMSharp implements Operator {

    DatagramSocket out; 
    
    /**
     * 
     */
    public IPMSharp() {
        
    	try 
        {
            out = new DatagramSocket();
        } 
        catch (SocketException e) 
        {
            e.printStackTrace();
        }
    }
    
    /* (non-Javadoc)
     * @see DADT.Operator#performRemote(DADT.CompleteView)
     */
    public Object performRemote(CompleteView view) {
        try 
        {
            String groupName = view.getDataView().getDADTClass().getName();
			
            InetSocketAddress dest = new InetSocketAddress(Algorithms.hash(groupName), 
            		IPMDADTMgr.IPM_DADT_MGR_MULTICASTPORT);
			
			DatagramPacket request = Algorithms.makeUDPPacketFromObject(
			        new IPMOperatorMessage(this.getClass().getName(), view,
			                 out.getLocalSocketAddress()), dest);
			
			//DatagramSocket replies = new DatagramSocket(group.getPort() + 1,
			//		group.getAddress());
			out.send(request);
			DADTMgr.l.info("Sent IPMSharp message to " + groupName + " at address" + dest);

			//l.info("Buffer size:" + replies.getReceiveBufferSize());
			
			//TODO here I'm not using the consolidate. Does it make sense in the
			//global "algorithms" picture?
			//instead of consolidating here we want to sum directly the
			//various integers.
			//Collection resultData = Algorithms.consolidateCollectionReplies(out);
		
			int size = 0;
			
			Collection resultData = Algorithms.collectReplies(out);
			

			 for (Iterator iter = resultData.iterator(); iter.hasNext();) {
			    Integer element;
                try {
                    element = (Integer) Algorithms.makeObjectFromUdpPacket((DatagramPacket) iter.next());
                    size += element.intValue();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
			return new Integer(size);
			
			//!!ArrayList<DatagramPacket> resultData = Algorithms.collectReplies(out);
			
			
			
		} 
        catch (IOException e) 
        {
			e.printStackTrace();
		}
		return null;

    }

    /* (non-Javadoc)
     * @see DADT.Operator#performLocal(DADT.InvocationData)
     */
    public void performLocal(InvocationData message) {
//      INVOKE THE RELATIONS;
        
  		Collection localADTInstances = DADTMgr.mgr.getInstances(message.getDv().getDADTClass().getName());
      	Collection instancesInDataview = message.getDv().filterMatchingInstances(localADTInstances);

  		DatagramPacket reply = Algorithms.makeUDPPacketFromObject(
  		        new Integer(instancesInDataview.size()), ((IPMOperatorMessage)message).sender);
  		DADTMgr.l.fine("sto per inviare la risposta Membership");
  		try {
              out.send(reply);
              DADTMgr.l.info("risposta Membership inviata");
          } catch (IOException e) {
              //TODO auto generated bla bla
              e.printStackTrace();
          }

    }

}
