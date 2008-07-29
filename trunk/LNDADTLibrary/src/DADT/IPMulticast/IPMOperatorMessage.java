/*
 * Created on Sep 29, 2005
 *
 */
package DADT.IPMulticast;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import DADT.CompleteView;
import DADT.InvocationData;

/**
 * @author migliava
 */
class IPMOperatorMessage extends InvocationData {

    SocketAddress sender;

    /**
     * @param operator
     * @param view
     * @param action
     */
    public IPMOperatorMessage(String operator, CompleteView view, SocketAddress sender) {
        super(operator, view);
        this.sender = sender;
    }

}