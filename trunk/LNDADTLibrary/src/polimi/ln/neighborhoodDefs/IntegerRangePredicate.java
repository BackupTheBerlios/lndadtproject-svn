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
 * *    $Id: IntegerRangePredicate.java 118 2008-05-28 17:35:12Z lmottola $
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

import polimi.ln.nodeAttributes.IntegerAttribute;
import polimi.ln.nodeAttributes.NodeAttribute;

/**
 * An atomic predicate specifying an upper and lower bound for an integer
 * attribute.
 * 
 * @author Luca Mottola <a
 *         href="mailto:mottola@elet.polimi.it">mottola@elet.polimi.it</a>
 * 
 */
public class IntegerRangePredicate implements AtomicPredicate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String attrName;

	private int lowerBound;

	private int upperBound;

	/**
	 * Creates an instance of this range predicate.
	 * 
	 * @param attrName the name of the attribute the predicate refers to.
	 * @param lowerBound the lower bound.
	 * @param upperBound the upper bound.
	 */
	public IntegerRangePredicate(String attrName, int lowerBound,
			int upperBound) {
		this.attrName = attrName;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	/**
	 * @see polimi.ln.neighborhoodDefs.AtomicPredicate#matches(polimi.ln.nodeAttributes.NodeAttribute)
	 */
	public boolean matches(NodeAttribute attr) {

		if (!(attr instanceof IntegerAttribute))
			return false;

		if (!attr.getKey().equals(attrName))
			return false;

		int attrValue = ((Integer) attr.getValue()).intValue();
		return (lowerBound <= attrValue && attrValue <= upperBound);
	}

	public String toString() {

		return "IntegerRangePredicate for attribute " + attrName
				+ ": lowerBound is " + lowerBound + ", higherBound is "
				+ upperBound;

	}
}
