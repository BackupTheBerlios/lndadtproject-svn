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
 * *    $Id: Neighborhood.java 118 2008-05-28 17:35:12Z lmottola $
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

import polimi.ln.nodeAttributes.Node;

/**
 * Defines the methods that must be implemented by the definition of any logical
 * neighborhood.
 * 
 * @author Luca Mottola <a
 *         href="mailto:mottola@elet.polimi.it">mottola@elet.polimi.it</a>
 * 
 */
public interface Neighborhood {

	/**
	 * Specifies the matching semantics for this neighborhood definition.
	 * 
	 * @param node
	 *            a reference to the logical node that must be analyzed to check
	 *            whether it matches this neighborhood definition.
	 * @return true if the logical node is part of the neighborhood, false
	 *         otherwise.
	 */
	public boolean matches(Node node);

	/**
	 * Checks whether this neighborhood definition has a constraint on the
	 * maximum number of hops to push its evaluation.
	 * 
	 * @return true if this neighborhood has a constraint on the maximum number
	 *         of hops, false otherwise.
	 */
	public boolean hasMaxHops();

	/**
	 * In case the neighborhood includes a constraint on the number of hops,
	 * returns this number.
	 * 
	 * @return returns the number of hops to limit the evaluation of this
	 *         neighborhood.
	 */
	public byte getMaxHops();
}
