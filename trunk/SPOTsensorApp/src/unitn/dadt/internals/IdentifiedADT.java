/*
 * Created on Sep 30, 2005
 *
 */
package unitn.dadt.internals;

/**
 * @author migliava
 */
public class IdentifiedADT {
    private Object adt;
    private Object identifier;
    
    /**
     * @param adt
     * @param identifier
     */
    public IdentifiedADT(Object adt, Object identifier) {
        super();
        this.adt = adt;
        this.identifier = identifier;
    }
    public Object getAdt() {
        return adt;
    }
    public Object getIdentifier() {
        return identifier;
    }
}
