/*
 * Created on Aug 9, 2005
 *
 */
package space;

/**
 * @author migliava
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Network {
    
    public static final Class distributes = Host.class;
    
    public boolean isReachable(Host receivingHost,Host h, int hops) {
        return true; //non va bene perch?? il filtro non filtra (? vero che per ora....)
        // per fare le cose pulite si potrebbe mantenere una tabella del numero di 
        // hop dai vari nodi che verrebbe popolata da chi 
        // riceve il InvocationData e controllata da questo metodo
        //throw new RuntimeException("This method should be never invoked"); non va bene perch? viene invocato dal filtro
    }
}
