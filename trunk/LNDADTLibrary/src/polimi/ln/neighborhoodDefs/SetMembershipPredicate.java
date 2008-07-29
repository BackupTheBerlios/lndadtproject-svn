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
 * *    $Id: SetMembershipPredicate.java 118 2008-05-28 17:35:12Z lmottola $
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

import java.util.HashSet;
import java.util.Set;

import polimi.ln.nodeAttributes.NodeAttribute;
import polimi.ln.nodeAttributes.SetAttribute;

/**
 * Defines a predicate to check membership in a set attribute. Membership (or
 * lack thereof) is specified using numeric constants in this class.
 * 
 * @author Luca Mottola <a
 *         href="mailto:mottola@elet.polimi.it">mottola@elet.polimi.it</a>
 * 
 */
public class SetMembershipPredicate implements AtomicPredicate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Possible operators
	public static final int IS_IN = 0;

	public static final int IS_NOT_IN = 1;

	private String attrName;

	private int operator;

	private Object predicateValue;

	/**
	 * Creates a new instance of this predicate.
	 * 
	 * @param predicateValue the value to check membership.
	 * @param operator specifies membership or lack thereof.
	 * @param attrName the name of the attribute being checked.
	 */
	public SetMembershipPredicate(Object predicateValue, int operator,
			String attrName) {

		this.attrName = attrName;
		this.operator = operator;
		this.predicateValue = predicateValue;
	}

	/**
	 * @see polimi.ln.neighborhoodDefs.AtomicPredicate#matches(polimi.ln.nodeAttributes.NodeAttribute)
	 */
	public boolean matches(NodeAttribute attr) {

		if (!(attr instanceof SetAttribute))
			return false;

		if (!attr.getKey().equals(attrName))
			return false;

		HashSet attrValue = new HashSet((Set) attr.getValue());

		switch (operator) {

		case IS_IN:
			return attrValue.contains(predicateValue);
		
		case IS_NOT_IN:
			return !attrValue.contains(predicateValue);

		default:
			return false;
		}
	}

	public String toString() {

		String ret = "SetMembership predicate for predicateValue "
				+ predicateValue;

		switch (operator) {
		case IS_IN:
			ret = ret.concat(" IS IN ");
			break;

		case IS_NOT_IN:
			ret = ret.concat(" IS NOT IN ");
			break;
		}

		ret = ret.concat(attrName);

		return ret;
	}
	
}
