/*
 * Created on Aug 11, 2005
 * Edited on Jul 05, 2008
 */
package unitn.dadt.internals;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

import polimi.ln.neighborhoodDefs.Predicate;



/**
 * @author migliava, G.Khasanova
 */
public class ExpressionTree{
    
    //
	private static final long serialVersionUID = 8524266403127534553L;

	public final static byte LEAF = 0;
    public final static byte NOT = 1;
    public final static byte AND = 2;
    public final static byte OR = 3;
     
    byte boolOp;
    
    Property property;
    
    ExpressionTree op1;
    ExpressionTree op2;

    
    protected ExpressionTree(byte boolOp) { this.boolOp = boolOp; }
    
    public ExpressionTree(Property p) {
        boolOp = LEAF;
        property = p;
    }

    public ExpressionTree not() {
        ExpressionTree result = new ExpressionTree(NOT);
        result.op1 = this;
        return result;
    }
    
    public ExpressionTree and(ExpressionTree e) {
        ExpressionTree result = new ExpressionTree(AND);
        result.op1 = this;
        result.op2 = e;
        return result;
    }
    
    public ExpressionTree or(ExpressionTree e) {
        ExpressionTree result = new ExpressionTree(OR);
        result.op1 = this;
        result.op2 = e;
        return result;
    }

    public boolean evaluateTree(Object o) {
        switch (boolOp) {
        case LEAF:
        	return property.evaluate(o);
        case NOT:
            return !op1.evaluateTree(o);
        case AND:
            if (!op1.evaluateTree(o)) return false;
            return op2.evaluateTree(o);
        case OR:
            if (op1.evaluateTree(o)) return true;
            return op2.evaluateTree(o);
        }
       // assert false;
        return false;
    }
    
    public String getDADTClass() {
        if (boolOp != LEAF)
            return op1.getDADTClass();
        return property.getDADTClass();
    }
    
	/**
	 * @author G.Khasanova
	 * 
	 * Allows to perform traverse along the expression tree that defines DADT view. 
	 * Traverse results in a list of predicates, that are used to define Logical Neighbourhood
	 *
	 * @param list List of LN predicates, it is being collected during the traverse 
	 * @return generated list of atomic predicates, that is used to build Logical Neighbourood.
	 */
	public Object[] traverseExpTree(Vector list) {
		switch(boolOp) 
		{
			case(LEAF): 
			{
				list = processLeaf(list);
				break;
			}
			case(AND): case(OR): case (NOT): 
			{
				op1.traverseExpTree(list);
				if (op2 != null)
				{
					op2.traverseExpTree(list);
				}
				break;
			}

		}
		
		Predicate[] resArray = new Predicate[list.size()];
		list.copyInto(resArray); 
		return resArray;
	}

	/**
	 * @author G.Khasanova
	 * 
	 * Process leaves of the expression tree, and generate relevant Logical neighbourhood predicates.
	 * 
	 * @param list existing list of predicates which needs to be updated
	 * @return list of predicates
	 */
	private Vector processLeaf(Vector list)
	{

		list.addElement(property.getDescriptionForLN());

		return list;
	}
	
	/**
	 * @author G.Khasanova
	 * 
	 * Allows to serialize expTree by traversing along its branches. 
	 */
	public void serializeExpTree(DataOutputStream stream) throws IOException {
		switch(boolOp) 
		{
			
			case(LEAF): 
			{
				serializeLeaf(stream);
				break;
			}
			case(AND): case(OR): case (NOT): 
			{
				op1.serializeExpTree(stream);
				if (op2 != null)
				{
					op2.serializeExpTree(stream);
				}
				stream.writeUTF(String.valueOf(boolOp));
			}
		}

	}

	/**
	 * @author G.Khasanova
	 * 
	 * Process leaves of the expression tree, and generate relevant Logical neighbourhood predicates.
	 * 
	 * @param list existing list of predicates which needs to be updated
	 * @param property property that provides description of the associated predicate
	 * @param masterProperty property that may contain other property in its predicate list
	 * @param  masterPropertyName name of the "master" property class
	 * @return list of predicates
	 */
	private void serializeLeaf(DataOutputStream stream) throws IOException{
		
		property.serialize(stream);
	
	}
	
}
