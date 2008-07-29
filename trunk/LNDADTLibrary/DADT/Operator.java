/*
 * Created on Nov 11, 2005
 *
 */
package DADT;

public interface Operator {

    /**
     * @param view
     * @return
     */
    public Object performRemote(CompleteView view);

    public void performLocal(InvocationData message);

}