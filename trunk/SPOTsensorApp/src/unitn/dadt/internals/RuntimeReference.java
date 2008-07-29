/*
 * Created on Nov 21, 2005
 *
 */
package unitn.dadt.internals;

import java.lang.ref.WeakReference;

/**
 * This class is necessary when we need to store Java objects into a Set or a Map. As
 * these collection equals (and Hash) of the object to verify it the object (or key) is 
 * already contained in the collection.
 * 
 * This is a problem as we need to store information for each object instance, independently
 * of their "equality". (i.e. if the object equals is redefined giving an applicative
 * semantic we still want to register different instance of "equals" object as separate keys) . 
 * 
 * As in these situations we usually want to keep weak references to these objects we defined 
 * this class which extends WeakReference. 
 * 
 * However according to what said above we redefined the WeakReference equals 
 * using == on the contained objects which compares the two references (basically the two addresses)
 * thus recovering the Object's equals semantics even if the object contained redefined the equals with
 * an applicative sematic.
 * 
 * We also redefine the hashCode to be the hash of the contained reference. (In this case we cannot call
 * directly the Object hashCode. Fortunately this does not matter as what is ultimately used in
 * comparisons is equals anyway and the contained object hashcode is a correct hascode for the address
 * as the only requirement is that if two addresses are equals then the two hascodes are equals which is 
 * trivially true as in this case hashCode is invoked twice on the same object)
 * 
 * @author migliava
 */
public class RuntimeReference extends WeakReference {

    public RuntimeReference(Object referent) {
        super(referent);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof WeakReference)) return false;
        return (this.get()==((WeakReference)obj).get());
    }
    
    public int hashCode() {
        
        //We could try to invoke Object.hashcode using reflection.
        return get().hashCode();
    }

}
