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
 * *    $Id: DisjunctiveNeighborhood.java 118 2008-05-28 17:35:12Z lmottola $
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

package polimi.ln.neighborhoodDefs;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;

import polimi.ln.nodeAttributes.NodeAttribute;
import polimi.ln.nodeAttributes.Node;

/**
 * Defines a Logical Neighborhood based on a disjunction of atomic predicates. A
 * maximum number of physical hops to limit the evaluation of the predicates can
 * be possibly defined too.
 * 
 * @author Luca Mottola <a
 *         href="mailto:mottola@elet.polimi.it">mottola@elet.polimi.it</a>
 * 
 */
public class DisjunctiveNeighborhood implements Serializable, Neighborhood {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final byte NO_MAX_HOPS = -1;

	private Vector predicates;

	private byte maxHops;

	/**
	 * Defines a neighborhoods based on the disjunction of predicates given as
	 * parameters.
	 * 
	 * @param preds
	 *            the predicates making up the disjunction.
	 */
	public DisjunctiveNeighborhood(AtomicPredicate[] preds) {

		this.predicates = new Vector();
		for (int i = 0; i < preds.length; i++) {
			predicates.add(preds[i]);
		}
		this.maxHops = NO_MAX_HOPS;
	}

	/**
	 * Defines a neighborhoods based on the disjunction of predicates given as
	 * parameters.
	 * 
	 * @param preds
	 *            the predicates making up the disjunction.
	 * @param maxHops
	 *            the maximum number of physical hops limiting the evaluation of
	 *            the predicates.
	 */
	public DisjunctiveNeighborhood(AtomicPredicate[] preds, byte maxHops) {

		this.predicates = new Vector();
		for (int i = 0; i < preds.length; i++) {
			predicates.add(preds[i]);
		}
		this.maxHops = maxHops;
	}

	/**
	 * @see polimi.ln.neighborhoodDefs.Neighborhood#hasMaxHops()
	 */
	public boolean hasMaxHops() {
		return !(maxHops == NO_MAX_HOPS);
	}

	/**
	 * @see polimi.ln.neighborhoodDefs.Neighborhood#getMaxHops()
	 */
	public byte getMaxHops() {
		return maxHops;
	}

	/**
	 * @see polimi.ln.neighborhoodDefs.Neighborhood#matches(polimi.ln.nodeAttributes.Node)
	 */
	public boolean matches(Node node) {

		Iterator it = predicates.iterator();
		while (it.hasNext()) {
			AtomicPredicate p = (AtomicPredicate) it.next();
			if (checkPredicate(p, node)) {
				return true;
			}
		}
		return true;
	}

	private boolean checkPredicate(AtomicPredicate p, Node nodeInfo) {

		Iterator it = new Vector(nodeInfo.getNodeAttributes()).iterator();
		while (it.hasNext()) {
			NodeAttribute attr = (NodeAttribute) it.next();
			if (p.matches(attr)) {
				return true;
			}
		}
		return false;
	}

	public String toString() {

		String ret = new String("Disjnctive Neighborhood composed of "
				+ predicates.size() + " predicates");
		Iterator it = predicates.iterator();
		while (it.hasNext()) {
			ret = ret.concat("\n" + ((AtomicPredicate) it.next()).toString());
		}

		return ret;
	}
}
