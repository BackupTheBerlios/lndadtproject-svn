package polimi.ln.examples.swans;

import polimi.ln.examples.ExampleMsg;
import polimi.ln.examples.ExampleReply;
import polimi.ln.neighborhoodDefs.AtomicPredicate;
import polimi.ln.neighborhoodDefs.ConjunctiveNeighborhood;
import polimi.ln.neighborhoodDefs.IntegerSimplePredicate;
import polimi.ln.neighborhoodDefs.Neighborhood;
import polimi.ln.neighborhoodDefs.StringSimplePredicate;
import polimi.ln.nodeAttributes.Node;
import polimi.ln.runtime.LNDeliver;
import polimi.ln.runtime.LogicalNeighborhoods;

/**
 * An example application using Logical Neighborhoods to be run in the SWANS
 * simulator. The application sends periodic messages to a neighborhood
 * including nodes whose "Type" attribute is equal to "sensor".
 * 
 * @author Luca Mottola <a
 *         href="mailto:mottola@elet.polimi.it">mottola@elet.polimi.it</a>
 * 
 */
public class ExampleApplication implements LNDeliver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		LogicalNeighborhoods ln;

		if (args[0].equals("periodicSend")) {

			ln = SimulationReferences.getLN(Integer.parseInt(args[1]));

			
			for (int i = 0; i < Integer.parseInt(args[2]); i++) {
				System.out.println("Message #" + i);
				Neighborhood temperatureNodes = new ConjunctiveNeighborhood(
						new AtomicPredicate[] { new StringSimplePredicate(
								"Type", IntegerSimplePredicate.EQUAL,
								"temperature") });
				Node n = SimulationReferences.getNodeInfo(Integer
						.parseInt(args[1]));
				n
						.debugPrint("APPLICATION: Sending message to temperature nodes");
				ln.send(new ExampleMsg(n.getMyId(), 0),
						new Neighborhood[] { temperatureNodes });
				try {
					Thread.sleep(Integer.parseInt(args[3]));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private Node info;

	private LogicalNeighborhoods ln;

	/**
	 * The object being created from this class are used to print some debug
	 * information upon reception of messages.
	 * 
	 * @param ln
	 *            a reference to the logical neighborhood layer.
	 * @param info
	 *            a reference to the logical node associated to this physical
	 *            device, used to print debug messages.
	 */
	public ExampleApplication(LogicalNeighborhoods ln, Node info) {
		this.info = info;
		this.ln = ln;
	}

	/**
	 * @see polimi.ln.runtime.LNDeliver#deliver(java.lang.Object)
	 */
	public void deliver(Object o) {

		if (o instanceof ExampleMsg) {
			info.debugPrint("APPLICATION: Got message!");
			info.debugPrint("APPLICATION: Sending reply back!");
			ExampleMsg m = (ExampleMsg) o;
			ln.sendReply(new ExampleReply(1), m.getSender());
		} else if (o instanceof ExampleReply) {
			info.debugPrint("APPLICATION: Got reply message!");
		}

	}
}
