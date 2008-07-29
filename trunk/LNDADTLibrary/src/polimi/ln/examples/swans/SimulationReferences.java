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
 * *    $Id: SimulationReferences.java 118 2008-05-28 17:35:12Z lmottola $
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

package polimi.ln.examples.swans;

import java.util.HashMap;

import polimi.ln.nodeAttributes.Node;
import polimi.ln.runtime.DatagramObjectIO;
import polimi.ln.runtime.LogicalNeighborhoods;

/**
 * This class is used in SWANS simulation to pass around references to objects
 * used by different threads on the same simulated node. This is required
 * because of the way concurrent threads running on the same simulated node must
 * be started in SWANS. 
 * 
 * @author Luca Mottola <a
 *         href="mailto:mottola@elet.polimi.it">mottola@elet.polimi.it</a>
 * 
 */
public class SimulationReferences {

	private static HashMap nodeInfos = new HashMap();

	private static HashMap lns = new HashMap();

	private static HashMap objectIOs = new HashMap();

	public static void setLN(int id, LogicalNeighborhoods ln) {
		lns.put(new Integer(id), ln);
	}

	public static LogicalNeighborhoods getLN(int id) {
		return (LogicalNeighborhoods) lns.get(new Integer(id));
	}

	public static void setNodeInfo(int id, Node info) {
		nodeInfos.put(new Integer(id), info);
	}

	public static Node getNodeInfo(int id) {
		return (Node) nodeInfos.get(new Integer(id));
	}

	public static void setObjectIO(int id, DatagramObjectIO objectIO) {
		objectIOs.put(new Integer(id), objectIO);
	}

	public static DatagramObjectIO getObjectIO(int id) {
		return (DatagramObjectIO) objectIOs.get(new Integer(id));
	}
}
