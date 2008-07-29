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
 * *    $Id: AppMsg.java 118 2008-05-28 17:35:12Z lmottola $
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

import polimi.ln.neighborhoodDefs.Neighborhood;

/**
 * @author Luca Mottola <a href="mailto:mottola@elet.polimi.it">mottola@elet.polimi.it</a>
 *
 */
public class AppMsg extends LNMessage {

	private static final long serialVersionUID = 1L;

	private Object data;

	private Neighborhood scope;

	private byte maxHops;

	public AppMsg(int id, long seqNumber, Object data,
			Neighborhood scope, byte maxHops) {
		super(id, seqNumber);
		this.data = data;
		this.maxHops = maxHops;
		this.scope = scope;
	}

	public AppMsg(int id, long seqNumber, Object data, Neighborhood scope) {
		super(id, seqNumber);
		this.data = data;
		this.maxHops = -1;
		this.scope = scope;
	}

	public Object getData() {
		return data;
	}

	public Neighborhood getLNScope() {
		return scope;
	}

	public byte getMaxHops() {
		return maxHops;
	}

	public boolean isMaxHops() {
		return (maxHops != -1);
	}
}
