/*
 * 15/09/05, Migliava, Created
 * 25/07/08, Khasanova, Changed to be used on Sun SPOTs under CLDC specification
 * 
 */
package unitn.dadt.internals;

import java.util.Enumeration;
import java.util.Vector;


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
