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
 * *    $Id: NodeAttribute.java 118 2008-05-28 17:35:12Z lmottola $
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

import java.io.Serializable;

/**
 * This class factors out code common to all attribute types. Its methods can be
 * possibly redefined to provide slightly different semantics, as in the case of
 * dynamic attributes.
 * 
 * @author Luca Mottola <a
 *         href="mailto:mottola@elet.polimi.it">mottola@elet.polimi.it</a>
 * 
 */
public abstract class NodeAttribute implements Serializable {

	// The attribute name
	protected String key;
	// The attribute value
	protected Object value;

	/**
	 * Returns the attribute name.
	 * 
	 * @return the attribute name.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Returns the attribute value.
	 * 
	 * @return the attribute value.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Redefines the <code>equals</code> methods for attributes.
	 */
	public boolean equals(Object obj) {

		if (!(obj instanceof NodeAttribute))
			return false;
		NodeAttribute attr = (NodeAttribute) obj;
		return key.equals(attr.key) && value.equals(attr.value);
	}
}
