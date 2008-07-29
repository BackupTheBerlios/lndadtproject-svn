/***
 * * PROJECT
 * *    Logical Neighborhoods - J2ME Run-Time Support 
 * * VERSION
 * *    $LastChangedRevision: 125 $
 * * DATE
 * *    $LastChangedDate: 2008-06-28 13:07:26 +0100 (Sat, 28 Jun 2008) $
 * * LAST_CHANGE_BY
 * *    $LastChangedBy: lmottola $
 * *
 * *    $Id: ExampleMsg.java 125 2008-06-28 12:07:26Z lmottola $
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

package polimi.ln.examples;

import java.io.Serializable;

/**
 * This class defines a message in the sample applications of Logical
 * Neighborhoods. It just contains an integer value. Besides this specific
 * example, the LN API is designed to carry any serializable object.
 * 
 * @author Luca Mottola <a
 *         href="mailto:mottola@elet.polimi.it">mottola@elet.polimi.it</a>
 * 
 */
public class ExampleMsg implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int value;

	private int sender;

	/**
	 * Creates a new message.
	 * 
	 * @param sender
	 *            the original message sender.
	 * @param i
	 *            the integer value to be stored in the message.
	 */
	public ExampleMsg(int sender, int i) {
		this.sender = sender;
		this.value = i;
	}

	/**
	 * @return the integer value stored in this message.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @return the original message sender (used to reply back).
	 */
	public int getSender() {
		return sender;
	}
}
