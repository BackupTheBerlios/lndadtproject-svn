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
 * *    $Id: IntegerSimplePredicate.java 118 2008-05-28 17:35:12Z lmottola $
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
 * An atomic predicate specifying a constraint on the value of an integer
 * attribute. The possible constraints are defined as numeric constants in this
 * class.
 * 
 * @author Luca Mottola <a
 *         href="mailto:mottola@elet.polimi.it">mottola@elet.polimi.it</a>
 * 
 */
public class IntegerSimplePredicate implements AtomicPredicate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Possible comparators
	public static final int EQUAL = 0;

	public static final int LESS_THAN = 1;

	public static final int GREATER_THAN = 2;

	public static final int LESS_EQUAL_THAN = 3;

	public static final int GREATER_EQUAL_THAN = 4;

	// Predicate data
	private String attrName;

	private int comparator;

	private int value;

	/**
	 * Creates an instance of this predicate.
	 * 
	 * @param attrName the name of the attribute the predicate refers to.
	 * @param comparator the constraint selected.
	 * @param value the integer value to compare the attribute against.
	 */
	public IntegerSimplePredicate(String attrName, int comparator, int value) {
		this.attrName = attrName;
		this.comparator = comparator;
		this.value = value;
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
		switch (comparator) {

		case EQUAL:
			return (attrValue == value);
		case LESS_THAN:
			return (attrValue < value);
		case GREATER_THAN:
			return (attrValue > value);
		case LESS_EQUAL_THAN:
			return (attrValue <= value);
		case GREATER_EQUAL_THAN:
			return (attrValue >= value);
		default:
			return false;
		}
	}

	public String toString() {

		String msg = "IntegerSimple predicate for attribute " + attrName + " ";

		switch (comparator) {

		case EQUAL:
			msg = msg.concat("=");
			break;
		case LESS_THAN:
			msg = msg.concat("<");
			break;
		case GREATER_THAN:
			msg = msg.concat(">");
			break;
		case LESS_EQUAL_THAN:
			msg = msg.concat("=<");
			break;
		case GREATER_EQUAL_THAN:
			msg = msg.concat("=>");
			break;
		default:
			msg = msg.concat("UNKNOWN");
			break;
		}

		return msg.concat(" " + value);
	}
}
