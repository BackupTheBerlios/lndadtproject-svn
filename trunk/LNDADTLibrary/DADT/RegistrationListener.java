/*
 * Created on Sep 4, 2005
 *
 */
package DADT;

/**
 * @author migliava
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface RegistrationListener {
    
    final static int REGISTRATION = 1;
    final static int DEREGISTRATION = 2; 
    
    void registrationPerformed (Class DADTClass, Object adt, int event);
}
