/*
 * Created on Sep 7, 2005
 *
 */
package unitn.dadt.internals;

/**
 * @author migliava
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class DIterator implements Operator, OperatorFactory {
    
    
    protected CompleteView v;
    
    public DIterator(CompleteView v) {
        this.v = v;
    }
    
    //Selection operators
    //Operator first;
//    Operator last;
//    Operator next;
//    Operator previous;
//    Operator curr;
    
    //A condition operator
    //Operator more;
    //    public class Next extends SelectionOperator {
//        DIterator iterator;
//        public Next(DIterator iterator, Action action) {
//            super(action);
//            this.iterator = iterator;
//        }
//        protected Object performRemote(CompleteView view) {
//            //TODO controllo che view == Iterator.view
//            return iterator.next(action);
//        }
//        protected void performLocal(InvocationData message) {
//        }
//    }

    public Operator getOperator(String operator, Action action, Object operatorParameter) {
        if (operator.equals("first")) return getFirstOperator(action,operatorParameter);
        if (operator.equals("curr")) return getCurrOperator(action,operatorParameter);
        if (operator.equals("next")) return getNextOperator(action,operatorParameter);
        if (operator.equals("prev")) return getPrevOperator(action,operatorParameter);
        if (operator.equals("last")) return getLastOperator(action,operatorParameter);
        if (operator.equals("more?")) return getMoreOperator(action,operatorParameter);
//        //the reflection solution
//        try {
//                return (Operator) this.getClass().getMethod("get"+operator+"Operator", new Class[] { Action.class, Object.class }).invoke(this, new Object[] {action, operatorParameter});
//            } catch (Exception e) {
//        }
        throw new RuntimeException("Operator not supported by "+this.getClass().getName());
    }
    
    public abstract Operator getFirstOperator(Action action, Object operatorParameter);
    public abstract Operator getCurrOperator(Action action, Object operatorParameter);
    public abstract Operator getNextOperator(Action action, Object operatorParameter);
    public abstract Operator getPrevOperator(Action action, Object operatorParameter);
    public abstract Operator getLastOperator(Action action, Object operatorParameter);
    public abstract Operator getMoreOperator(Action action, Object operatorParameter);
}
