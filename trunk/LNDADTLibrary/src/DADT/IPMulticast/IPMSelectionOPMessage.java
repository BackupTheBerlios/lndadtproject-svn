/*
 * Created on Sep 29, 2005
 *
 */
package DADT.IPMulticast;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import DADT.Action;
import DADT.CompleteView;
import DADT.SelectionOperatorInvocationData;

/**
 * @author migliava
 */
class IPMSelectionOPMessage extends SelectionOperatorInvocationData {

    SocketAddress sender;

    /**
     * @param operator
     * @param view
     * @param action
     */
    public IPMSelectionOPMessage(String operator, CompleteView view, Action action, SocketAddress sender) {
        super(operator, view, action);
        this.sender = sender;
    }

}