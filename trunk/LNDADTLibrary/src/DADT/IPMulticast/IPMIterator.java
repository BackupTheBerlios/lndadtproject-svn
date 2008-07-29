/*
 * Created on Oct 4, 2005
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
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.text.View;

///import sensorExample.DSensorAction_getThreshold;

import DADT.Action;
import DADT.CompleteView;
import DADT.DADTMgr;
import DADT.DIterator;
import DADT.Operator;
import DADT.Identificator;
import DADT.InvocationData;
import DADT.SelectionOperator;

/**
 * @author migliava
 */
public class IPMIterator extends DIterator implements Operator {

    ArrayList instances = new ArrayList();
    int position;
    
    //public CompleteView v;

    public IPMIterator(CompleteView v) {
        super(v);
    }
    public IPMIterator() {
        super(null);
    }
//    
//    public static void main(String[] args) {
//        new IPMIterator.Next(new DSensorAction_getThreshold());
//    }
    

    public Operator getFirstOperator(Action action, Object operatorParameter) { return new First(action,this); }
    public Operator getCurrOperator(Action action, Object operatorParameter) { return new Curr(action,this); }
    public Operator getNextOperator(Action action, Object operatorParameter) { return new Next(action,this); }
    public Operator getPrevOperator(Action action, Object operatorParameter) { return new Prev(action,this); }
    public Operator getLastOperator(Action action, Object operatorParameter) { return new Last(action,this); }
    public Operator getMoreOperator(Action action, Object operatorParameter) { return new More(this); }

    public Object performRemote(CompleteView view) {
        //TODO this is almost identical to tha All operator
        //REFACTOR
        
        try {
            DatagramSocket out = new DatagramSocket();
            String groupName = view.getDataView().getDADTClass().getName();
                    InetSocketAddress dest = new InetSocketAddress(Algorithms
                            .hash(groupName), IPMDADTMgr.IPM_DADT_MGR_MULTICASTPORT);
                    DatagramPacket request = Algorithms.makeUDPPacketFromObject(
                            new IPMOperatorMessage(this.getClass().getName(), view,
                                    out.getLocalSocketAddress()), dest);
                    
                    //DatagramSocket replies = new DatagramSocket(group.getPort() + 1,
                    //      group.getAddress());
                    out.send(request);
                    IPMDADTMgr.l.info("Sent IPALL message to " + groupName + " at address" + dest);
        
                    
                    //l.info("Buffer size:" + replies.getReceiveBufferSize());
                    
                    //FIXME is there a race condition? Should we listen before we send?
                    Collection resultData = Algorithms.collectAndConsolidateReplies(out);
                    //************************
                    // THIS IS THE ONLY PART MODIFIED FROM IPMALL (START)
                    //************************
                    instances.addAll(resultData);
                    position = -1;
                    return this;
                    //************************
                    // THIS IS THE ONLY PART MODIFIED FROM IPMALL (END)
                    //************************
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
    }

    public void performLocal(InvocationData message) {
        // only that we get the identifier instead of invoking the action
        
        //TODO this is identical to tha All operator
        //REFACTOR
//      INVOKE THE RELATIONS;
        
        DatagramSocket out;
        try {
            out = new DatagramSocket();
        
            Collection localADTInstances = DADTMgr.mgr.getInstances(message.getDv().getDADTClass().getName());
            Collection instancesInDataview = message.getDv().filterMatchingInstances(localADTInstances);
            
            Serializable replies = (Serializable) DADTMgr.mgr.getAdtIdentificator().getIDs(instancesInDataview);
            
            DADTMgr.l.fine("Ho processato il messaggio sto preparando la risposta");
            
            // SEND OBJECTS
            DatagramPacket reply = Algorithms.makeUDPPacketFromObject(
                    replies, ((IPMOperatorMessage)message).sender);
            //reply.setPort(5001);
            DADTMgr.l.fine("sto per inviare la risposta Membership");
            
            out.send(reply);
            DADTMgr.l.info("risposta Membership inviata");
        } catch (IOException e) {
            //TODO auto generated bla bla
            e.printStackTrace();
        }
        
    }
    
    //This is necessary to allow iterator operators to work on the view they cannot access directly
    CompleteView getView() {
        return v;
    }
    

    //public Operator getFirstOperator(Action action, Object operatorParameter) { return new First(action); }

//    //TODO let's try to see if we can use an adapter to invoke this method
//    public Object first(Action action, Object operatorParameter) {
//        return null;
//    };
    
}

class IPMIteratorOperatorMessage extends IPMSelectionOPMessage {

    IPIdentifier id;
    
    /**
     * @param operator
     * @param view
     * @param action
     * @param sender
     */
    IPMIteratorOperatorMessage(String operator, CompleteView view, Action action, SocketAddress sender, IPIdentifier id) {
        super(operator, view, action, sender);
        this.id = id;
    }
    
    
    //TODO this is the exact code from superclass (SelectionOperatorMessage) 
    //it is reported here as the superclass cannot create e.g. Next class as it is 
    //not public (but package)
    protected Operator getNewOperator() {
        try {
            return (Operator) Class.forName(operator).getConstructor(new Class[] {Action.class}).newInstance(new Object[] { action });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * This will return the identifier of the adt on which the invocation should take place
     * @return
     */

    public IPIdentifier getId() {
        return id;
    }
    
//    protected Operator getNewOperator() {
//        try {
//            ArrayList singleInstanceColl = new ArrayList();
//            singleInstanceColl.add(id);
//            return (Operator) Class.forName(operator).getConstructor(new Class[] {Action.class, Collection.class }).newInstance(new Object[] { action , singleInstanceColl});
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
    
}


abstract class IPMIteratorOperator extends SelectionOperator {

        IPMIterator father;
    
        public IPMIteratorOperator(Action a, IPMIterator father) { super(a); this.father = father;}
        public IPMIteratorOperator(Action a) { super(a); }

        /**
         * @param message
         * @throws SocketException
         * @throws IOException
         */
        protected void genericPerformLocal(InvocationData message) {
            IPMIteratorOperatorMessage invocation = (IPMIteratorOperatorMessage) message;
            //See also IPMSome
            //Should we check that the object is still in the view?
            //No for simmetry with respect to object that join the view after the
            //operator creation
            
            Object adt = DADTMgr.mgr.getAdtIdentificator().getADT(
                    invocation.getId());
            
            ArrayList temp = new ArrayList();
            temp.add(adt);
            
            Collection results = this.performActionLocally(temp);
            
            DatagramSocket replySocket;
            try {
                replySocket = new DatagramSocket();
                DatagramPacket reply = Algorithms.makeUDPPacketFromObject(
                        (Serializable) results,
                        invocation.sender);
                replySocket.send(reply);
                System.out.println("REPLIED, size:"+results.size());
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        public IPMIteratorOperator(Action a) {
//            super(a);
//        }

        public Object performRemote(CompleteView view) { adjustPosition(); return performRemoteOnCurrentPosition(father.getView());}
        public void performLocal(InvocationData message) { genericPerformLocal(message); }
        abstract protected void adjustPosition();

        protected Object performRemoteOnCurrentPosition(CompleteView view) {
            System.out.println("invokeOnCurrentPosition");
            if (!view.equals(father.getView()))
                throw new RuntimeException("View on invocation is not the same of declaration");
        
            IPIdentifier id = (IPIdentifier) father.instances.get(father.position);
            try {
                DatagramSocket out;
                //TODO factor out socket creation for performance
                //FIXME we need to close the socket somewhere!! and it is not possible with
                //the current design
                //old version: then modfied to new DatagramSocket() (i.e. with no parameters)
                //out = new DatagramSocket(new InetSocketAddress("127.0.0.1",15555));
                out = new DatagramSocket();
                
                out.send(Algorithms.makeUDPPacketFromObject(
                        new IPMIteratorOperatorMessage(
                                this.getClass().getName(),
                                view,
                                action,
                                out.getLocalSocketAddress(),
                                id
                        ),
                        id.getAddr()));
                
                
                ;
                
                return Algorithms.collectAndConsolidateReplies(out).toArray()[0];
            } catch (ArrayIndexOutOfBoundsException e) {
                return null;  
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //TODO this null is horrible we should not defer the exception 
            //further down either we decide to raise an exception 
            //or we return an empty collection
            return null;
        }
    }


class First extends IPMIteratorOperator { 
    public First(Action a) { super(a); }
    public First(Action a, IPMIterator i) { super (a,i); }
    protected void adjustPosition() { father.position = 0; }
}

class Curr extends IPMIteratorOperator {
    public Curr(Action a) { super(a); }
    public Curr(Action a, IPMIterator i) { super (a,i); }
    protected void adjustPosition() { }
}

class Last extends IPMIteratorOperator {
    public Last(Action a) { super(a); }
    public Last(Action a, IPMIterator i) { super (a,i); }
    protected void adjustPosition() { father.position=father.instances.size(); }
}

class Next extends IPMIteratorOperator {
    public Next(Action a) { super(a); }
    public Next(Action a, IPMIterator i) { super (a,i); }
    protected void adjustPosition() { father.position++; }
}

class Prev extends IPMIteratorOperator {
    public Prev(Action a) { super(a); }
    public Prev(Action a, IPMIterator i) { super (a,i); }
    protected void adjustPosition() { father.position--; }
}

class More implements Operator {
    
    IPMIterator father;

    //This method is only local so there is no need for a constructor for the "local" side
    //and the performlocal may be empty as it should never be invoked
    public More(IPMIterator iterator) { father = iterator; }
    
    
    public Object performRemote(CompleteView view) {
        return new Boolean(father.position<father.instances.size()-1);
    }
    public void performLocal(InvocationData message) {}
}