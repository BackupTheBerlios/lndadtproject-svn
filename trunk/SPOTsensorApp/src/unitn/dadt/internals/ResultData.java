/*
 * Created on Sep 15, 2005
 *
 */
package unitn.dadt.internals;

import java.util.Enumeration;
import java.util.Vector;

/* javaME doesn't support any of these
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
*/

/**
 * @author migliava
 *
 */
public class ResultData /*implements Serializable */{

    /*Object*/ double data;
    /*Object*/ String source;
    
    /**
     * @param data
     * @param source
     */
    public ResultData(/*Object*/ double data, /*Object*/ String source) {
        super();
        this.data = data;
        this.source = source;
    }
    public /*Object*/ double getData() {
        return data;
    }
    public /*Object*/ String getSource() {
        return source;
    }
    /**
     * This function is automatically inserted by the compiler
     * when Ids are not requested
     * @param resultData
     * @return
     */
    /*javaME doesn't support Collections
    public static Collection getData(Collection resultData) { 
        ArrayList l = new ArrayList();
        Iterator it = resultData.iterator();
        while (it.hasNext()) {
            ResultData el = (ResultData) it.next();
            l.add(el.data);
        }
        return l;
    }
    */
    public static Vector getData(Vector resultData) { 
        Vector l = new Vector();
        Enumeration it = resultData.elements();
        while (it.hasMoreElements()) {
            ResultData el = (ResultData) it.nextElement();
            l.addElement(new Double(el.data));
        }
        return l;
    }
}
