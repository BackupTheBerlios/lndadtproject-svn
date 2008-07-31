/*
 * Created on Nov 11, 2005
 *
 */
package DADT;

public interface OperatorFactory {

    /**
     * @param qualifyingExpr 
     * @param operator
     * @param action 
     * @return
     */
    public Operator getOperator(String operator, Action action,
            Object operatorParameter);

}