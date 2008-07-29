/*
 * Created on Nov 11, 2005
 *
 */
package DADT;

public interface OperatorFactory {

    /**
     * @param qualifyingExpr TODO
     * @param operator
     * @param action TODO
     * @return
     */
    public Operator getOperator(String operator, Action action,
            Object operatorParameter);

}