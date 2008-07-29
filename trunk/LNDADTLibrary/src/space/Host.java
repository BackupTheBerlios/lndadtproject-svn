/*
 * Created on Aug 9, 2005
 *
 */
package space;

import java.io.Serializable;

/**
 * This is the base class for SpaceADTs
 * 
 * For now it only contains an ID which is used to compose DataADTs idenfiers.
 *  
 * @author migliava
 *
 */
public class Host implements Serializable {
    HostID id;

    /**
     * Returns the identifier of the Site
     * @return
     */
    public HostID getID() { return id; }
}
