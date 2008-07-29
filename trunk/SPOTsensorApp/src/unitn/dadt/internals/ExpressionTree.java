/*
 * Created on Aug 11, 2005
 * Edited on Jul 05, 2008
 */
package unitn.dadt.internals;

import java.util.Vector;

/* javaME doesn't support any of these
 
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
*/

/*import polimi.ln.neighborhoodDefs.AtomicPredicate;*/



/**
 * @author migliava, G.Khasanova
 */
public class ExpressionTree /*implements Serializable */{
    
    //
	private static final long serialVersionUID = 8524266403127534553L;

	public final static int LEAF = 0;
    public final static int NOT = 1;
    public final static int AND = 2;
    public final static int OR = 3;
    
    int boolOp;
    
    Property property;
    
    ExpressionTree op1;
    ExpressionTree op2;
    
    public String[] propertyDescription; // 
    
    protected ExpressionTree(int boolOp) { this.boolOp = boolOp; }
    
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
    
    public Class getDADTClass() {
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
	 * @param predicateList List of LN predicates, it is being collected during the traverse 
	 * @param masterProperty presents a property, that may be used to define "master" predicate that may contain predicates within itself
	 * @param masterPropertyName presents a name of the property class (to define "master" predicate)
	 * @return generated list of atomic predicates, that is used to build Logical Neighbourood.
	 */
	public  Vector traverseExpTree(Vector predicateList, Property masterProperty, String masterPropertyName) {
		switch(boolOp) 
		{
			case(LEAF): 
			{
				predicateList = processLeaf(predicateList, property, masterProperty, masterPropertyName);
				return predicateList;  
			}
			case(AND) :
			{
				//"master" predicate can be defined in AND-branches, and in this case it may take its sibling as a child predicate
				if (op2 != null)
				{
					Property neighbProperty = op2.property;
					op1.traverseExpTree(predicateList, neighbProperty, masterPropertyName);
					op2.traverseExpTree(predicateList, op1.property, masterPropertyName);
				}
				else
					op1.traverseExpTree(predicateList, null, masterPropertyName);
				return predicateList;      
			}
			case(OR): case (NOT):
			{
				op1.traverseExpTree(predicateList, null, masterPropertyName);
				if (op2 != null)
				{
					op2.traverseExpTree(predicateList, null, masterPropertyName);
				}
				return predicateList;  
			}

		}
		return null;		
	}

	/**
	 * @author G.Khasanova
	 * 
	 * Process leaves of the expression tree, and generate relevant Logical neighbourhood predicates.
	 * 
	 * @param predicateList existing list of predicates which needs to be updated
	 * @param property property that provides description of the associated predicate
	 * @param masterProperty property that may contain other property in its predicate list
	 * @param  masterPropertyName name of the "master" property class
	 * @return list of predicates
	 */
	private Vector processLeaf(Vector predicateList, Property property, Property masterProperty, String masterPropertyName)
	{
		return predicateList;
		//STUB
	}
	/*
	private ArrayList processLeaf(ArrayList predicateList, Property property, Property masterProperty, String masterPropertyName){
		
		if ((masterProperty != null) && (masterProperty.getClassName() == masterPropertyName))
			predicateList.add(masterProperty.getDescriptionForLN((Object)property));
		else
			predicateList.add(property.getDescriptionForLN(null));
			
		return predicateList; 
	}
	*/
}
