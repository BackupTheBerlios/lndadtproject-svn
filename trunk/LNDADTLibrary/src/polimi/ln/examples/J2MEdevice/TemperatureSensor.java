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
 * *    $Id: StartSwansSim.java 22 2007-05-06 11:55:23 +0200 (Sun, 06 May 2007) lmottola $
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

package polimi.ln.examples.J2MEdevice;

import java.util.Vector;

import polimi.ln.examples.ExampleMsg;
import polimi.ln.neighborhoodDefs.ConjunctiveNeighborhood;
import polimi.ln.neighborhoodDefs.IntegerSimplePredicate;
import polimi.ln.neighborhoodDefs.AtomicPredicate;
import polimi.ln.neighborhoodDefs.Neighborhood;
import polimi.ln.neighborhoodDefs.StringSimplePredicate;
import polimi.ln.nodeAttributes.DynamicIntegerAttribute;
import polimi.ln.nodeAttributes.IntegerProvider;
import polimi.ln.nodeAttributes.Node;
import polimi.ln.nodeAttributes.StringAttribute;
import polimi.ln.runtime.LNDeliver;
import polimi.ln.runtime.LogicalNeighborhoods;

/**
 * An example application using Logical Neighborhoods. The application defines a
 * logical node representing a temperature sensor deployed in a given room, and
 * sends periodic messages to a neighborhood including temperature sensor
 * reporting a reading above a threshold.
 * 
 * @author Luca Mottola <a
 *         href="mailto:mottola@elet.polimi.it">mottola@elet.polimi.it</a>
 * 
 */
public class TemperatureSensor implements LNDeliver {

	private static final int THRESHOLD = 70;
	private static final byte MAX_HOPS = 2;
	private static final long PERIOD = 10000;

	public static void main(String[] args) {
		new TemperatureSensor();
	}

	public TemperatureSensor() {

		try {

			// Defining the logical node corresponding to this device
			Vector attributes = new Vector();
			attributes.add(new StringAttribute("Function", "sensor"));
			attributes.add(new StringAttribute("Type", "temperature"));
			attributes.add(new StringAttribute("Location", "room1"));
			attributes.add(new DynamicIntegerAttribute("Reading",
					new FakeTemperatureSensor()));
			attributes.add(new DynamicIntegerAttribute("BatteryPower",
					new FakeBatterySensor()));

			// Instantiating the logical node
			Node temperatureSensor = new Node(0, attributes, 1);

			// Defining and instantiating a neighborhood
			Neighborhood hotSensors = new ConjunctiveNeighborhood(
					new AtomicPredicate[] {
							new StringSimplePredicate("Function",
									StringSimplePredicate.EQUAL, "sensor"),
							new StringSimplePredicate("Type",
									StringSimplePredicate.EQUAL, "temperature"),
							new IntegerSimplePredicate("Reading",
									IntegerSimplePredicate.GREATER_THAN,
									THRESHOLD) }, MAX_HOPS);

			// Sending periodic messages to the neighborhood previously defined
			// Setting up the LN run-time using the logical node instance
			// previously instantiated
			LogicalNeighborhoods ln = new LogicalNeighborhoods(
					temperatureSensor);
			ln.setReceiver(this);

			// Sending periodic messages to the neighborhood previously defined
			while (true) {
				Thread.sleep(PERIOD);
				ln.send(new ExampleMsg(temperatureSensor.getMyId(), 0),
						new Neighborhood[] { hotSensors });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see polimi.ln.runtime.LNDeliver#deliver(java.lang.Object)
	 */
	public void deliver(Object o) {
		System.out.println("Just received a message!");
	}
}

// Fake class representing readings from a temperature sensor
class FakeTemperatureSensor implements IntegerProvider {

	public int getValue() {
		return (int) Math.random();
	}
}

// Fake class monitoring the battery level
class FakeBatterySensor implements IntegerProvider {

	public int getValue() {
		return (int) Math.random();
	}
}
