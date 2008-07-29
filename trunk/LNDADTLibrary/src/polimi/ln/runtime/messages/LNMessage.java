/***
 * * PROJECT
 * *    Logical Neighborhoods - J2ME Run-Time Support 
 * * VERSION
 * *    $LastChangedRevision: 22 $
 * * DATE
 * *    $LastChangedDate: 2007-05-06 11:55:23 +0200 (Sun, 06 May 2007) $
 * * LAST_CHANGE_BY
 * *    $LastChangedBy: lmottola $
 * *
 * *    $Id: LNMessage.java 22 2007-05-06 09:55:23Z lmottola $
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

package polimi.ln.runtime.messages;

import java.io.Serializable;
import java.util.Collection;
import java.util.Vector;

/**
 * @author Luca Mottola <a href="mailto:mottola@elet.polimi.it">mottola@elet.polimi.it</a>
 *
 */
public class LNMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private int senderId;

	private int originatorId;

	private long seqNumber;

	private Vector destinations;

	private byte hops;

	public LNMessage(int id, long seqNumber) {

		this.hops = 0;
		this.seqNumber = seqNumber;
		this.originatorId = id;
	}

	public int getSenderId() {
		return senderId;
	}

	public long getSeqNumber() {
		return seqNumber;
	}

	public void setDestinations(Collection dests) {
		destinations = new Vector(dests);
	}

	public int getOriginatorId() {
		return originatorId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public byte getHops() {
		return hops;
	}

	public void setHops(byte hops) {
		this.hops = hops;
	}
}
