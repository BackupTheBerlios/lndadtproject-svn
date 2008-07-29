package DADT.IPMulticast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.LinkedList;

import DADT.DADTMgr;
import DADT.InvocationData;

//TODO check if this class has no reference to IPDADTMgr and in that
//case move it to DADT package
/**
 * @author galiyashka
 *
 */
public class OperatorInvoker extends Thread {
	
	LinkedList queue;
	DatagramSocket out;
	OperatorInvoker() {
	
		super("OperatorInvoker");
		queue = new LinkedList();
		try 
		{ 
			out = new DatagramSocket();
		} 
		catch (SocketException e) 
		{
			e.printStackTrace();
		}
	
	}
	
	public synchronized void sendDADTinstances(DatagramPacket p) {
		queue.add(p);
		notify();
	}
	
	public void run() {
		DADTMgr.l.info("Starting Listening for Operators...");
		try {
			synchronized (this) {
				while (true) {
					while (!queue.isEmpty()) {

						DADTMgr.l.fine("C'e' almeno un messaggio nella coda");
						DatagramPacket p = (DatagramPacket) queue.getFirst();
						queue.removeFirst();
			
						DADTMgr.l.fine("ho preso il messaggio e sto per spacchettarlo");
						// READ THE RELATION INVOCATION
						final InvocationData message;
						message = (InvocationData) Algorithms.makeObjectFromUdpPacket(p);
						
						//!!
						// This try..catch was added because sender's address was not set in makeObjectFromUdpPacket;
						// @author Khasanova
						try 
						{
    						if ((message.useOperatorMessage("IPMSharp")) || (message.useOperatorMessage("IPMIterator")))
    						{
    							((IPMOperatorMessage)message).sender = p.getSocketAddress();
    							DADTMgr.l.info("message sender now:" + ((IPMOperatorMessage)message).sender.toString());
    						}
    						else 
    						{
    							
    							((IPMSelectionOPMessage)message).sender = p.getSocketAddress();
    							DADTMgr.l.info("message sender now:" + ((IPMSelectionOPMessage)message).sender.toString());	
    						}
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						//!!
						
						DADTMgr.l.fine("Ho spacchettato il messaggio e mi appresto a processarlo");
						
						//Spawns a new operator passing the message to it
						message.runOperator();

						
					}
					try {
						DADTMgr.l.info("Membership Server going to sleep");
						wait();
						DADTMgr.l.info("Membership Server awaken!!");
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}			
	}
	
}
