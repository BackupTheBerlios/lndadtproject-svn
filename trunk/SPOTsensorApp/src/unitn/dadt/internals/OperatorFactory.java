/*
 * Created on Nov 11, 2005
 *
 */
package unitn.dadt.internals;

public interface OperatorFactory {

    /**
     * @param operator
     * @param action 
     * @param operatorParameter
     * @return
     */
    public Operator getOperator(String operator, Action action,
            Object operatorParameter);

}