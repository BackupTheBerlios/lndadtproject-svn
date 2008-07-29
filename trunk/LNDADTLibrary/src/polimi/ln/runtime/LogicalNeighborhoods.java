/***
 * * PROJECT
 * *    Logical Neighborhoods - J2ME Run-Time Support 
 * * VERSION
 * *    $LastChangedRevision: 125 $
 * * DATE
 * *    $LastChangedDate: 2008-06-28 13:07:26 +0100 (Sat, 28 Jun 2008) $
 * * LAST_CHANGE_BY
 * *    $LastChangedBy: lmottola $
 * *
 * *    $Id: LogicalNeighborhoods.java 125 2008-06-28 12:07:26Z lmottola $
 * *
 * *   Logical Neighborhoods - 
 * *                   Programming Wireless Sensor Networks
 * *                   (One Slice at a Time)
 * *
 * *   This program is free software; you can redistribute it and/or
 * *   modify it under the terms of the GNU Lesser General Public License
 * *   as published by the Free Software Foundation; either version 2.1
 * *   of the License, or (at your option) any later version.
 * *
 * *   This program is distributed in the hope that it will be useful,
 * *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 * *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * *   GNU General Public License for more details.
 * *
 * *   You should have received a copy of the GNU Lesser General Public 
 * *   License along with this program; if not, you may find a copy at 
 * *   the FSF web site at 'www.gnu.org' or 'www.fsf.org', or you may 
 * *   write to the Free Software Foundation, Inc., 59 Temple 
 * *   Place - Suite 330, Boston, MA  02111-1307, USA
 ***/

package polimi.ln.runtime;

import java.util.Random;
import java.util.Vector;

import polimi.ln.neighborhoodDefs.Neighborhood;
import polimi.ln.nodeAttributes.Node;
import polimi.ln.runtime.messages.AppMsg;
import polimi.ln.runtime.messages.LNMessage;
import polimi.ln.runtime.messages.ProfAdv;
import polimi.ln.runtime.messages.ReplyMsg;

/**
 * This class represents the entry point for applications. Essentially, users
 * should be using the object constructor and <code>send</code> methods,
 * without touching anything else under polimi.ln.*.
 * 
 * @author Luca Mottola <a
 *         href="mailto:mottola@elet.polimi.it">mottola@elet.polimi.it</a>
 * 
 */
public class LogicalNeighborhoods implements ObjectIOListener {

	protected NeighborSet neighborSet;

	protected SSD ssd;

	protected LNRouting appRouting;

	protected DatagramObjectIO objectIO;

	private static final int PROFADV_DISSEMINATION = 20000;

	protected static final int EXPIRATION_TICKS = 3;

	public static final int PORT = 5555;

	public static final int BUFFER_SIZE = 512;

	public static final int RECEIVE_TIMEOUT = 500;

	protected static final byte DISSEMINATION_FAN_OUT = 3;

	private static final int MAX_DELAY = 1000;

	protected Random rnd;

	private LNDeliver appDeliver;

	protected Node nodeInfo;

	protected Vector messageQueue;

	protected long ticks;

	/**
	 * Required to extend this class in LogicalNeighborhoodsSwans.
	 */
	public LogicalNeighborhoods() {
	}

	/**
	 * Creates a new instance of the Logical Neighborhoods run-time based on the
	 * information in a logical node.
	 * 
	 * @param info
	 *            the logical node instance.
	 */
	public LogicalNeighborhoods(Node info) {

		this.ticks = 0;
		this.neighborSet = new NeighborSet(EXPIRATION_TICKS);
		this.nodeInfo = info;
		this.ssd = new SSD(info, this);
		this.appRouting = new LNRouting(info, neighborSet,
				DISSEMINATION_FAN_OUT, this);
		this.objectIO = new DatagramObjectIO(PORT, BUFFER_SIZE, RECEIVE_TIMEOUT);
		objectIO.addListener(this);
		this.rnd = new Random(info.getMyId());
		this.messageQueue = new Vector();
	}

	/**
	 * Sends a serializable object to a set of neighborhoods. The neighborhoods
	 * are processed individually, so no merging occurs for different (and
	 * possibly overlapping) neighborhoods.
	 * 
	 * @param data
	 *            the object to send.
	 * @param scopes
	 *            a series of neighborhoods the object must be sent to.
	 */
	public void send(Object data, Neighborhood[] scopes) {
		appRouting.generateAppMsg(data, scopes);
	}

	/**
	 * Sends a serializable object to a specific node in reply to some previous
	 * message. Note that this cannot be used for general unicast communication.
	 * It can only be used to send some data back to a node that originally
	 * addressed a logical neighborhood.
	 * 
	 * @param data
	 *            the object to send.
	 * @param destination
	 *            the id of the destination node.
	 */
	public void sendReply(Object data, int destination) {
		appRouting.generateReplyMsg(data, destination);

	}

	public void objectReceived(Object o) {

		if (!(o instanceof LNMessage)) {
			nodeInfo.debugPrint("Unknown message received");
		} else {
			LNMessage msg = (LNMessage) o;
			if (msg.getOriginatorId() != nodeInfo.getMyId()
					&& msg.getSenderId() != nodeInfo.getMyId()) {
				neighborSet.update(msg.getSenderId(), ticks);
				if (msg instanceof ProfAdv) {
					/*
					nodeInfo.debugPrint("Got ProfAdv from "
							+ msg.getSenderId() + " generated from "
							+ msg.getOriginatorId());
					*/		
					ssd.parseProfAdv((ProfAdv) msg, ticks);
				} else if (msg instanceof AppMsg) {
					/*
					nodeInfo.debugPrint("Got AppMsg from "
							+ msg.getSenderId() + " [generated by "
							+ msg.getOriginatorId() + "]");
					*/		
					appRouting.parseAppMsg((AppMsg) msg);
				} else if (msg instanceof ReplyMsg) {
					/*
					nodeInfo.debugPrint("Got ReplyMsg message from "
							+ msg.getSenderId() + " generated from "
							+ msg.getOriginatorId());
					*/		
					
					appRouting.parseReplyMsg((ReplyMsg) msg);
				} else {
					nodeInfo.debugPrint("Unknown message received!");
				}
			}
		}
	}

	protected void sendMessage(LNMessage msg) {
		msg.setSenderId(nodeInfo.getMyId());
		messageQueue.add(messageQueue.size(), msg);
	}

	protected void forwardMessage(LNMessage msg) {
		msg.setHops((byte) (msg.getHops() + 1));
		sendMessage(msg);
	}

	protected void tick() {
		while (true) {
			ticks++;
			neighborSet.pruneExpiredNeighbors(ticks);
			// TODO: ssd.disseminateProfAdv(logicalTime);
			try {
				Thread.sleep(PROFADV_DISSEMINATION);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void queueSend() {
		while (true) {
			if (!messageQueue.isEmpty()) {
				
				//nodeInfo.debugPrint("Transmitting message");
				
				objectIO.sendBroadcastObject(messageQueue.firstElement());
				messageQueue.remove(0);
			}
			int delay = rnd.nextInt(MAX_DELAY);
			// To avoid collisions at the network level
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void deliver(Object data) {
		//nodeInfo.debugPrint("Delivering data item");
		appDeliver.deliver(data);
	}

	public void setReceiver(LNDeliver appDeliver) {
		this.appDeliver = appDeliver;
	}
}
