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
 * *    $Id: NeighborSet.java 22 2007-05-06 09:55:23Z lmottola $
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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

/**
 * @author Luca Mottola <a
 *         href="mailto:mottola@elet.polimi.it">mottola@elet.polimi.it</a>
 * 
 */
public class NeighborSet {

	private HashSet neighbors;

	private int expirationTicks;

	public NeighborSet(int expirationTime) {

		this.expirationTicks = expirationTime;
		this.neighbors = new HashSet();
	}

	public void update(int id, long logicalTime) {

		boolean found = false;
		Iterator it = neighbors.iterator();
		while (it.hasNext()) {
			Neighbor n = (Neighbor) it.next();
			if (n.id == id) {
				n.lastSeen = logicalTime;
				found = true;
			}
		}
		if (!found) {
			neighbors.add(new Neighbor(id, logicalTime));
		}
	}

	public void pruneExpiredNeighbors(long logicalTime) {

		HashSet toBePruned = new HashSet();
		Iterator it = neighbors.iterator();
		while (it.hasNext()) {
			Neighbor n = (Neighbor) it.next();
			if (n.lastSeen + expirationTicks < logicalTime) {
				toBePruned.add(n);
			}
		}
		neighbors.removeAll(toBePruned);
	}

	public Collection returnRandomNeighbors(int howMany) {
		HashSet result = new HashSet();
		Vector v = new Vector(neighbors);
		Random rnd = new Random();
		for (int i = 0; i < howMany && !v.isEmpty(); i++) {
			int selected = rnd.nextInt(v.size());
			Neighbor n = (Neighbor) v.get(selected);
			result.add(new Integer(n.id));
			v.remove(selected);
		}
		return result;
	}

	class Neighbor {

		private int id;

		private long lastSeen;

		public Neighbor(int id, long lastSeen) {

			this.id = id;
			this.lastSeen = lastSeen;
		}
	}
}
