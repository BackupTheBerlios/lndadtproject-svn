package unitn.dadt.internals;

import java.util.Enumeration;
import java.util.Vector;


/*
 * 10/10/04, Migliava, Created
 * 25/07/08, Khasanova, Changed to be used on Sun SPOTs under CLDC specification
 * 
 */

 public class Functions {

	public static final int PKT_LENGHT = 2000;

	
	/**
	 * This utility method returns a new collection of objects that satisfy a given predicate
	 * @param c the input collection
	 * @param p the predicate
	 * @return the output collection
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
	public static void filter(Vector source, Predicate p, Vector destination) {
		Enumeration i = source.elements();
		while (i.hasMoreElements()) {
			Object o = i.nextElement();
			if (p.evaluate(o))
				destination.addElement(o);
		}
	}
}
