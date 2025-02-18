package expression.statements;

import expression.Expression;
import expression.IdExpression;
import expression.operator.AccessArrayExpression;
import token.ArrayTypeWordToken;
import token.TypeWordToken;

/**
 * Created by samy on 5/2/15.
 */
public class SetElemBaseStatement extends BaseStatement {

    private IdExpression array;
    private Expression index;
    private Expression expression;

    public SetElemBaseStatement(AccessArrayExpression accessArrayExpression, Expression expression) throws Exception {
        this.array = accessArrayExpression.getArray();
        this.index = accessArrayExpression.getIndexToAccess();
        this.expression = expression;
        if (check(accessArrayExpression.getTypeWord(), this.expression.getTypeWord()) == null){
            error("Wrong access to array");
        }
    }

    private TypeWordToken check(TypeWordToken p1, TypeWordToken p2) {
        if ( p1 instanceof ArrayTypeWordToken || p2 instanceof ArrayTypeWordToken) return null ;
        else if (p1 == p2) return p2;
        else if (TypeWordToken.numeric(p1) && TypeWordToken.numeric(p2)) return p2;
        else return null;

    }

    @Override
    public void generateStatement(int before, int whereToGoAfter) throws Exception {
        String s1 = index.reduce().toString();
        String s2 = expression.reduce().toString();
        printStatement(array.toString() + "[ " + s1 + " ] = " + s2);
    }
}
