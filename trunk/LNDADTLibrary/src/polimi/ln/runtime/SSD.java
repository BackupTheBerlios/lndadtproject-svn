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
 * *    $Id: SSD.java 118 2008-05-28 17:35:12Z lmottola $
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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import polimi.ln.nodeAttributes.NodeAttribute;
import polimi.ln.nodeAttributes.Node;
import polimi.ln.runtime.messages.ProfAdv;

/**
 * @author Luca Mottola <a
 *         href="mailto:mottola@elet.polimi.it">mottola@elet.polimi.it</a>
 * 
 */
public class SSD {

	private static long idEntryCounter = 0;

	private Node nodeInfo;

	private LogicalNeighborhoods ln;

	private Vector ssdData;

	public SSD(Node nodeInfo, LogicalNeighborhoods ln) {

		this.nodeInfo = nodeInfo;
		this.ln = ln;
		this.ssdData = new Vector();
		
	}

	protected void parseProfAdv(ProfAdv adv, long ssdTime) {

		// TODO: Add links between entries to compress State Space
		boolean modified = false;
		adv.setCost(adv.getCost() + nodeInfo.getSendingCost());
		Iterator profAdvIt = new Vector(adv.getNodeAttributes()).iterator();
		while (profAdvIt.hasNext()) {
			NodeAttribute profAdvAttr = (NodeAttribute) profAdvIt.next();
			if (needsInsertion(profAdvAttr, adv.getCost())) {
				modified = true;
				ssdData.add(new SSDAttribute(idEntryCounter++, profAdvAttr, adv
						.getCost(), adv.getOriginatorId(), ssdTime));
			}
		}
		if (modified) {
			nodeInfo.debugPrint("Propagating node profile for "
					+ adv.getOriginatorId() + " received from "
					+ adv.getSenderId());
			ln.forwardMessage(adv);
		}
	}

	private void pruneExpiredEntries(long ssdTime) {
		HashSet toBeRemoved = new HashSet();
		Iterator it = ssdData.iterator();
		while (it.hasNext()) {
			SSDAttribute ssdAttr = (SSDAttribute) it.next();
			if (ssdAttr.expirationTime > ssdTime) {
				toBeRemoved.add(ssdAttr);
			}
		}
		ssdData.removeAll(toBeRemoved);
	}

	protected void disseminateProfAdv(long ssdTime) {
		ProfAdv profAdv = new ProfAdv(nodeInfo.getMyId(), ssdTime, new Vector(
				nodeInfo.getNodeAttributes()));
		nodeInfo.debugPrint("Disseminating node profile at time " + ssdTime);
		ln.sendMessage(profAdv);
	}

	private boolean needsInsertion(NodeAttribute attribute, int cost) {

		Iterator it = ssdData.iterator();
		while (it.hasNext()) {
			SSDAttribute ssdAttr = (SSDAttribute) it.next();
			if (ssdAttr.attribute.equals(attribute) && ssdAttr.cost < cost) {
				return false;
			}
		}
		return true;
	}

	class SSDAttribute {

		private long entryId;

		private NodeAttribute attribute;

		private int cost;

		private int decreasingPath;

		private Vector increasingPaths;

		private int originatorId;

		private long expirationTime;

		public SSDAttribute(long entryId, NodeAttribute attr, int cost,
				int originatorId, long ssdTime) {

			this.entryId = entryId;
			this.attribute = attr;
			this.cost = cost;
			this.originatorId = originatorId;
			this.expirationTime = ssdTime
					+ LogicalNeighborhoods.EXPIRATION_TICKS;
		}

		protected void update(long ssdTime) {
			this.expirationTime = ssdTime
					+ LogicalNeighborhoods.EXPIRATION_TICKS;
		}
	}
}
