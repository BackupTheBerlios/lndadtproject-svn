package unitn.dadt.internals;

import java.util.Enumeration;
import java.util.Vector;

/* javaME doesn't support any of these
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;
*/

/*
 * Created on Oct 10, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author migliava
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
 public class Functions {

	//static Logger l = Logger.getLogger("DADT.Functions");
	public static final int PKT_LENGHT = 2000;

	
	/**
	 * This utility method returns a new collection of objects that satisfy a given predicate
	 * @param c the input collection
	 * @param p the predicate
	 * @return the output collection
	 */
	/* java ME doesn't suppot Collections
	public static Collection filter(Collection c, Predicate p) {
		Collection c2;
		try {
			c2 = (Collection) c.getClass().newInstance();
			Iterator i = c.iterator();
			while (i.hasNext()) {
				Object o = i.next();
				if (p.evaluate(o))
					c2.add(o);
			}
			return c2;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	*/
	public static Vector filter(Vector c, Predicate p) {
		Vector c2;
		try {
			c2 = (Vector) c.getClass().newInstance();
			Enumeration i = c.elements();
			while (i.hasMoreElements()) {
				Object o = i.nextElement();
				if (p.evaluate(o))
					c2.addElement(o);
			}
			return c2;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * This utility method returns a new collection of objects that satisfy a given predicate
	 * @param c the input collection
	 * @param p the predicate
	 * @return the output collection
	 */
	/* javaME doesn't support Collections
	public static Collection transform(Collection c, Transformation t) {
		Collection c2;
		try {
			c2 = (Collection) c.getClass().newInstance();
			Iterator i = c.iterator();
			while (i.hasNext()) {
				Object o = i.next();
				c2.add(t.transform(o));
			}
			return c2;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	*/
	public static Vector transform(Vector c, Transformation t) {
		Vector c2;
		try {
			c2 = (Vector) c.getClass().newInstance();
			Enumeration i = c.elements();
			while (i.hasMoreElements()) {
				Object o = i.nextElement();
				c2.addElement(t.transform(o));
			}
			return c2;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * This utility method adds to a dest collection objects from another collection filtering with a given predicate
	 * @param c the input collection
	 * @param p the predicate
	 * @return the output collection
	 */
	/* javaMe doesn't suppor Collections
	public static void filter(Collection source, Predicate p,Collection destination) {
		Iterator i = source.iterator();
		while (i.hasNext()) {
			Object o = i.next();
			if (p.evaluate(o))
				destination.add(o);
		}
	}
	*/
	public static void filter(Vector source, Predicate p, Vector destination) {
		Enumeration i = source.elements();
		while (i.hasMoreElements()) {
			Object o = i.nextElement();
			if (p.evaluate(o))
				destination.addElement(o);
		}
	}
}
