/*
 * Created on Sep 15, 2005
 *
 */
package DADT;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author migliava
 *
 */
public class ResultData implements Serializable {

    Object data;
    Object source;
    
    /**
     * @param data
     * @param source
     */
    public ResultData(Object data, Object source) {
        super();
        this.data = data;
        this.source = source;
    }
    public Object getData() {
        return data;
    }
    public Object getSource() {
        return source;
    }
    /**
     * This function is automatically inserted by the compiler
     * when Ids are not requested
     * @param resultData
     * @return
     */
    public static Collection getData(Collection resultData) { 
        ArrayList l = new ArrayList();
        Iterator it = resultData.iterator();
        while (it.hasNext()) {
            ResultData el = (ResultData) it.next();
            l.add(el.data);
        }
        return l;
    }
}
