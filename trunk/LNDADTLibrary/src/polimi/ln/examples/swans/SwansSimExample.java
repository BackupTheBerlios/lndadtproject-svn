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
 * *    $Id: SwansSimExample.java 125 2008-06-28 12:07:26Z lmottola $
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

import java.util.Vector;

import jist.runtime.JistAPI;
import jist.swans.Constants;
import jist.swans.app.AppJava;
import jist.swans.field.Field;
import jist.swans.field.Mobility;
import jist.swans.mac.MacAddress;
import jist.swans.mac.MacDumb;
import jist.swans.misc.Location;
import jist.swans.misc.Mapper;
import jist.swans.misc.Util;
import jist.swans.net.NetAddress;
import jist.swans.net.NetIp;
import jist.swans.net.PacketLoss;
import jist.swans.radio.RadioInfo;
import jist.swans.radio.RadioNoise;
import jist.swans.radio.RadioNoiseIndep;
import jist.swans.trans.TransUdp;
import polimi.ln.nodeAttributes.Node;
import polimi.ln.nodeAttributes.StringAttribute;
import polimi.ln.runtime.DatagramObjectIO;
import polimi.ln.runtime.LogicalNeighborhoods;
import polimi.ln.runtime.LogicalNeighborhoodsSwans;
import polimi.ln.runtime.platformDependent.JiSTClock;

/**
 * This class is used to setup the simulation environment in SWANS to run the
 * example application in <code>ExampleApplication</code>. The system-wide
 * settings are specified in the <code>main</code> method, whereas the
 * <code>createNode</code> method takes care of the node-specific settings. At
 * the end of this method, the threads required to run the LN substrate, the LN
 * routing layer, and the application on the individual nodes are started.
 * 
 * @author Luca Mottola <a
 *         href="mailto:mottola@elet.polimi.it">mottola@elet.polimi.it</a>
 * 
 */
public class SwansSimExample {

	private static int NUM_MESSAGES = 1;
	private static final int PERIOD = 3000;

	/**
	 * Sets the simulation parameters for the individual nodes.
	 * 
	 * @param id the node identifier
	 * @param field a reference to the simulation field previously created.
	 * @param radioInfoShared a reference to the radio model previously created.
	 * @param protMap the mappings among network protocols running in the simulation.
	 * @param plIn a reference the the packet loss model for incoming messages.
	 * @param plOut a reference the the packet loss model for outgoing messages.
	 * @param info the logical node associated to the simulated device.
	 * @param posX the physical position of the node along the X axis.
	 * @param posY the physical position of the node along the Y axis.
	 * @throws NoSuchMethodException
	 */
	public static void createNode(int id, Field field,
			RadioInfo.RadioInfoShared radioInfoShared, Mapper protMap,
			PacketLoss plIn, PacketLoss plOut, Node info, int posX, int posY)
			throws NoSuchMethodException {

		// Create entities
		RadioNoise radio = new RadioNoiseIndep(id, radioInfoShared);
		MacDumb mac = new MacDumb(new MacAddress(id), radio.getRadioInfo());
		NetAddress netAddr = NetAddress.LOCAL;
		NetIp net = new NetIp(netAddr, protMap, plIn, plOut);
		TransUdp udp = new TransUdp();

		// Hookup entities
		Location location = new Location.Location2D(posX, posY);
		field.addRadio(radio.getRadioInfo(), radio.getProxy(), location);
		field.startMobility(radio.getRadioInfo().getUnique().getID());

		// Radio hookup
		radio.setFieldEntity(field.getProxy());
		radio.setMacEntity(mac.getProxy());

		// Mac hookup
		mac.setRadioEntity(radio.getProxy());
		byte intId = net.addInterface(mac.getProxy());
		mac.setNetEntity(net.getProxy(), intId);

		// Net hookup
		net.setProtocolHandler(Constants.NET_PROTOCOL_UDP, udp.getProxy());

		// Transport hookup
		udp.setNetEntity(net.getProxy());

		SimulationReferences.setNodeInfo(id, info);

		// Starting LN network substrate for UDP
		DatagramObjectIO objectIO = new DatagramObjectIO(
				LogicalNeighborhoods.PORT, LogicalNeighborhoods.BUFFER_SIZE,
				LogicalNeighborhoods.RECEIVE_TIMEOUT);
		SimulationReferences.setObjectIO(id, objectIO);
		AppJava runObjectIO = new AppJava(DatagramObjectIO.class);
		runObjectIO.setUdpEntity(udp.getProxy());
		runObjectIO.getProxy().run(new String[] { String.valueOf(id) });
		JistAPI.sleep(1);

		// Starting Logical Neighborhoods
		LogicalNeighborhoodsSwans ln = new LogicalNeighborhoodsSwans(info,
				objectIO);
		ln.setReceiver(new ExampleApplication(ln,info));
		SimulationReferences.setLN(id, ln);
		// Starting tick thread to perform periodic maintenance of routes
		AppJava runLNTick = new AppJava(LogicalNeighborhoodsSwans.class);
		runLNTick.setUdpEntity(udp.getProxy());
		runLNTick.getProxy().run(
				new String[] { "tickStart", String.valueOf(id) });
		JistAPI.sleep(1);
		// Starting outgoing message queue
		AppJava runLNQueue = new AppJava(LogicalNeighborhoodsSwans.class);
		runLNQueue.setUdpEntity(udp.getProxy());
		runLNQueue.getProxy().run(
				new String[] { "queueStart", String.valueOf(id) });
		JistAPI.sleep(1);

		// Starting the application
		AppJava runTestMsg = new AppJava(ExampleApplication.class);
		runTestMsg.setUdpEntity(udp.getProxy());
		runTestMsg.getProxy().run(
				new String[] { "periodicSend", String.valueOf(id),
						String.valueOf(NUM_MESSAGES), String.valueOf(PERIOD) });
		JistAPI.sleep(1);
	}

	public static void main(String[] args) {
		try {

			// Creating the simulation playground
			Location.Location2D bounds = new Location.Location2D(2000, 2000);
			Mobility mobility = new Mobility.Static();

			// Setting parameters for wireless simulation
			Field field = new Field(bounds);
			field.setMobility(mobility);
			RadioInfo.RadioInfoShared radioInfoShared = RadioInfo.createShared(
					Constants.FREQUENCY_DEFAULT, Constants.BANDWIDTH_DEFAULT,
					Constants.TRANSMIT_DEFAULT, Constants.GAIN_DEFAULT, Util
							.fromDB(Constants.SENSITIVITY_DEFAULT), Util
							.fromDB(Constants.THRESHOLD_DEFAULT),
					Constants.TEMPERATURE_DEFAULT,
					Constants.TEMPERATURE_FACTOR_DEFAULT,
					Constants.AMBIENT_NOISE_DEFAULT);

			// Protocol mapper
			Mapper protMap = new Mapper(Constants.NET_PROTOCOL_MAX);
			protMap.mapToNext(Constants.NET_PROTOCOL_UDP);

			// Packet loss model
			PacketLoss pl = new PacketLoss.Zero();

			// Creating two simulated temperature nodes: a temperature sensor
			// and a vibration sensor
			Vector attributes = new Vector();
			attributes.add(new StringAttribute("Function", "sensor"));
			attributes.add(new StringAttribute("Type", "temperature"));
			Node one = new Node(0, attributes, 1, new JiSTClock());
			createNode(0, field, radioInfoShared, protMap, pl, pl, one, 0, 0);
			Thread.sleep(1000);

			attributes = new Vector();
			attributes.add(new StringAttribute("Function", "sensor"));
			attributes.add(new StringAttribute("Type", "vibration"));
			Node two = new Node(1, attributes, 1, new JiSTClock());
			createNode(1, field, radioInfoShared, protMap, pl, pl, two, 400,
					400);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
