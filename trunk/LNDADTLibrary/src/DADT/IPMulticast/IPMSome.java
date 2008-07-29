/*
 * Created on Sep 30, 2005
 *
 */
package DADT.IPMulticast;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import DADT.Action;
import DADT.CompleteView;
import DADT.DADTMgr;
import DADT.Operator;
import DADT.InvocationData;
import DADT.SelectionOperator;


/**
 * @author migliava
 */
public class IPMSome extends SelectionOperator {

    //we do not want to send all instances to every operator
    Collection adtInstances;
    
    /**
     * @param a
     * @param adtInstances
     */
    public IPMSome(Action a, Collection adtInstances) {
        super(a);
        this.adtInstances = adtInstances;
    }

    //FIXME tutta sta storia dei due metodi di invocazione fa cagare:
    //1 il performRemote ha un parametro e un tipo di ritorno il performLocal ha entrambi diversi
    //2 il costruttore usato nella creazione locale ? completamente diverso da quello della versione remota
    //nella versione remota l'operatore viene creato dal messaggio con un costruttore diverso a seconda del parametro: ma siamo pazzi?
    //3 il modo di passaggio parametri ? assolutamente diverso nei due casi e in entrambi complicatissimo:
    //il parametro locale viene creato come come SomeParameter mentre il parametro remoto ? embedded nel messaggio
    //anche il messaggio fa cacare 
//    protected IPMSome(Action a, IPIdentifier ) {
//        
//    }
    /* (non-Javadoc)
     * @see DADT.Operator#performRemote(DADT.CompleteView)
     */
    public Object performRemote(CompleteView view) {

        try {
            //Here we could 1) either send many small invocations or a big one
            // 2) either route according to the view or with some other mean
            // 3) either reevaluate if the instances are still in the view or not reevaluate.
            //DECISION: we take the view into account and thus we route according to that.
            //as a first step we send many messages leaving the opportunitiy of optimization
            //(one big message? deciding according to the size?) to later
            //However each invocation can either be sent in unicast but we should provide
            //a channel for receiving unicast packets. Or we could just use 
            DatagramSocket out = new DatagramSocket();
            for (Iterator iter = adtInstances.iterator(); iter.hasNext();) {
                IPIdentifier el =  (IPIdentifier) iter.next();
                //IPIdentifier sockAddr = (IPIdentifier) el.getIdentifier();
                try {
                    //TODO here we send an invocation for each adt. We could pack it
                    //sitewise for efficient dispatching 
                    String groupName = view.getDataView().getDADTClass().getName();
        			InetSocketAddress dest = new InetSocketAddress(Algorithms
        			        .hash(groupName), IPMDADTMgr.IPM_DADT_MGR_MULTICASTPORT);
                    IPMSomeMessage msg = new IPMSomeMessage(this.getClass().getName(),view, action, out.getLocalSocketAddress(),(IPIdentifier) el);
                    out.send(Algorithms.makeUDPPacketFromObject(msg,dest));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            
            return Algorithms.collectAndConsolidateReplies(out);
            
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* (non-Javadoc)
     * @see DADT.Operator#performLocal(DADT.InvocationData)
     */
    public void performLocal(InvocationData message) {
        
        //TODO: We should also check inclusion in the view according to DECISION above;
        //we could pick the same code for view inclusion from the all operator;
        //and REFACTOR the whole thing
        
        IPMSomeMessage invocation =  (IPMSomeMessage) message;
        
        ArrayList singleInstance = new ArrayList();
        Object adt = DADTMgr.mgr.getAdtIdentificator().getADT(invocation.getId());
        singleInstance.add(adt);
        Collection result = performActionLocally(singleInstance);
        
        //TODO REFACTORME this is also in IPMALL.
        try {
            DatagramSocket replySocket= new DatagramSocket();
    		DatagramPacket reply = Algorithms.makeUDPPacketFromObject(
    		        (Serializable) result,
                     ((IPMSelectionOPMessage)message).sender);
    		//DADTMgr.l.fine("sto per inviare la risposta Membership");
    		try {
                replySocket.send(reply);
                //DADTMgr.l.info("risposta Membership inviata");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}

class IPMSomeMessage extends IPMSelectionOPMessage {

    IPIdentifier id;
    
    /**
     * @param operator
     * @param view
     * @param action
     * @param sender
     */
    IPMSomeMessage(String operator, CompleteView view, Action action, SocketAddress sender, IPIdentifier id) {
        super(operator, view, action, sender);
        this.id = id;
    }
    /**
     * @return
     */

    public IPIdentifier getId() {
        return id;
    }
    
    protected Operator getNewOperator() {
        try {
            ArrayList singleInstanceColl = new ArrayList();
            singleInstanceColl.add(id);
            //FIXME this is probably wrong as the adtInstances collection is used 
            //on the remote side. On the local side the performLocal can retrieve the
            //id directly from the message as it already does.
            return (Operator) Class.forName(operator).getConstructor(new Class[] {Action.class, Collection.class }).newInstance(new Object[] { action , singleInstanceColl});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
