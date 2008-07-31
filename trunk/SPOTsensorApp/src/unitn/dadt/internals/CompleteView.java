/*
 * 10/08/05, Migliava, Created 
 * 31/07/08, Khasanova, Modified to be used with LN
 *
 */
package unitn.dadt.internals;


import unitn.dadt.space.SpaceView;

/**
 * @author migliava
 */
public abstract class CompleteView implements OperatorFactory /*, Serializable*/ {

    protected SpaceView sv;
    protected DataView dv;
    protected boolean spaceInvocation;

    /**
     * @param sv
     * @param dv
     */
    protected CompleteView(SpaceView sv, DataView dv, boolean spaceInvocation) {
        super();
        this.sv = sv;
        this.dv = dv;
        this.spaceInvocation = spaceInvocation;
    }

    protected CompleteView(SpaceView sv) {
        super();
        this.sv = sv;
        this.dv = null;
    }

    protected CompleteView(DataView dv) {
        super();
        this.sv = null;
        this.dv = dv;
    }

    protected CompleteView(CompleteView cv) {
        super();
        this.sv = cv.sv;
        this.dv = cv.dv;
    }
    
    
    /**
     * 
     */
    //TODO after we have operator we should remove this method or make it package
    public DataView getDataView() {
	   return dv;
    }
    /**
     * @return
     */
    public SpaceView getSpaceView() {
        return sv;
    }

    /**
     * @param qualifyingExpr 
     * @param action 
     * @return
     */
    /*
     * Here we could use the same approach used in Actions and Predicates to define
     * parameters for operators.
     */
    public Object apply(DIterator qualifyingExpr, String operatorName, Action action, Object parameter) {
        
        Operator operator; 
        if (qualifyingExpr!=null)
            operator = qualifyingExpr.getOperator(operatorName, action, parameter);
        else 
            operator = getOperator(operatorName, action, parameter);

        //TODO handle spaceInvocations
        return operator.performRemote(this);
    }

    /* (non-Javadoc)
     * @see DADT.OperatorFactory#getOperator(java.lang.String, DADT.Action, java.lang.Object)
     */
    public abstract Operator getOperator(String operator, Action action, Object operatorParameter);
    //public abstract DIterator getIterator(String iterator, Object operatorParameter);

}
