/*
 * Created on Sep 14, 2005
 * Updated on Jun 06, 2008
 *
 */
package DADT;


/**
 * This interface should be implemented by Action that get executed on ADTS.
 * @author migliava, G.Khasanova
 */
public interface Action {
    public Object evaluate(Object o);
}