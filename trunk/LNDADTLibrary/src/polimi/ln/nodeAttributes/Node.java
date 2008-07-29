/***
 * * PROJECT
 * *    Logical Neighborhoods - J2ME Run-Time Support 
 * * VERSION
 * *    $LastChangedRevision: 118 $
 * * DATE
 * *    $LastChangedDate: 2008-05-28 19:35:12 +0200 (Wed, 28 May 2008) $
 * * LAST_CHANGE_BY
 * *    $LastChangedBy: lmottola $
 * *
 * *    $Id: Node.java 118 2008-05-28 17:35:12Z lmottola $
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

package polimi.ln.nodeAttributes;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import polimi.ln.runtime.Clock;

/**
 * Encapsulates the information to define a logical node instance.
 * 
 * @author Luca Mottola <a
 *         href="mailto:mottola@elet.polimi.it">mottola@elet.polimi.it</a>
 * 
 */
public class Node {

	private Vector attributes;

	private int myId;

	private int sendingCost;

	private Clock clock;

	/**
	 * Creates a new logical node instance.
	 * 
	 * @param myId
	 *            the identifier of the node.
	 * @param nodeAttributes
	 *            the attributes associated to this node.
	 * @param sendingCost
	 *            the node sending cost.
	 */
	public Node(int myId, Collection nodeAttributes, int sendingCost) {

		attributes = new Vector(nodeAttributes);
		this.sendingCost = sendingCost;
		this.myId = myId;
		this.clock = null;
	}

	/**
	 * Creates a new logical node instance.
	 * 
	 * @param myId
	 *            the identifier of the node.
	 * @param nodeAttributes
	 *            the attributes associated to this node.
	 * @param sendingCost
	 *            the node sending cost.
	 * @param clock
	 *            a reference to an object implementing the <code>Clock</code>
	 *            inteface.
	 */
	public Node(int myId, Collection nodeAttributes, int sendingCost,
			Clock clock) {

		attributes = new Vector(nodeAttributes);
		this.sendingCost = sendingCost;
		this.myId = myId;
		this.clock = clock;
	}
	
	/**
	 * Returns the list of attributes associated to this logical node instance.
	 * 
	 * @return the list of attributes for this node.
	 */
	public Collection getNodeAttributes() {
		return attributes;
	}

	/**
	 * Returns the node identifier.
	 *
	 * @return the node identifier.
	 */
	public int getMyId() {
		return myId;
	}

	/**
	 * Returns the node sending cost.
	 * 
	 * @return the node sending cost.
	 */
	public int getSendingCost() {
		return sendingCost;
	}

	/**
	 * Used to print debug messages.
	 * 
	 * @param debugMessage the message to display.
	 */
	public void debugPrint(String debugMessage) {
		System.out.println(myId + ": " + debugMessage + " at time "
				+ clock.getTime() + " " + clock.getDate());
		System.out.flush();
	}

	public String toString() {

		String ret = "NodeInfo id: " + myId;

		Iterator it = attributes.iterator();
		while (it.hasNext()) {
			ret = ret.concat("\nAttribute "
					+ ((NodeAttribute) it.next()).toString());
		}

		return ret;
	}
}
