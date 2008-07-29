package polimi.ln.runtime;

import java.util.Random;
import java.util.Vector;

import polimi.ln.examples.ExampleMsg;
import polimi.ln.examples.swans.SimulationReferences;
import polimi.ln.neighborhoodDefs.AtomicPredicate;
import polimi.ln.neighborhoodDefs.ConjunctiveNeighborhood;
import polimi.ln.neighborhoodDefs.IntegerSimplePredicate;
import polimi.ln.neighborhoodDefs.Neighborhood;
import polimi.ln.neighborhoodDefs.StringSimplePredicate;
import polimi.ln.nodeAttributes.Node;

public class LogicalNeighborhoodsSwans extends LogicalNeighborhoods {

	/**
	 * This constructor takes the network layer substrate for LN as an external
	 * object given as parameter. It is used to be able to run LN inside the
	 * SWANS simulator. It should not be used on standard J2ME-compliant
	 * devices.
	 * 
	 * @param info
	 *            the node instance.
	 * @param objectIO
	 *            the network layer substrate.
	 */
	public LogicalNeighborhoodsSwans(Node info, DatagramObjectIO objectIO) {

		this.ticks = 0;
		this.neighborSet = new NeighborSet(EXPIRATION_TICKS);
		this.objectIO = objectIO;
		this.nodeInfo = info;
		this.ssd = new SSD(info, this);
		this.appRouting = new LNRouting(info, neighborSet,
				DISSEMINATION_FAN_OUT, this);
		objectIO.addListener(this);
		this.rnd = new Random(info.getMyId());
		this.messageQueue = new Vector();
	}

	public static void main(String[] args) {
			LogicalNeighborhoods ln;

			if (args[0].equals("tickStart")) {
				ln = SimulationReferences.getLN(Integer.parseInt(args[1]));
				//ln.nodeInfo.debugPrint("Starting LN Tick thread");
				ln.tick();
			} else if (args[0].equals("queueStart")) {
				ln = SimulationReferences.getLN(Integer.parseInt(args[1]));
				//ln.nodeInfo.debugPrint("Starting LN Queue thread");
				ln.queueSend();
			}

	}
}
