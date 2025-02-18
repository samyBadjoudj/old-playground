package expression.statements;

import expression.Expression;
import expression.IdExpression;
import token.TypeWordToken;

/**
 * Created by samy on 5/2/15.
 */
public class SetBaseStatement extends BaseStatement {

    private IdExpression idExpression;
    private Expression expression;

    public SetBaseStatement(IdExpression i, Expression x) throws Exception {
        idExpression = i;
        expression = x;
        if (check(idExpression.getTypeWord(), expression.getTypeWord()) == null){
            error(" not same type");
        }
    }

    public TypeWordToken check(TypeWordToken p1, TypeWordToken p2) {
        TypeWordToken typeWord = null;
        if (TypeWordToken.numeric(p1) && TypeWordToken.numeric(p2)  || (p1 == TypeWordToken.Bool && p2 == TypeWordToken.Bool)){
            typeWord = p2;
        }
        return typeWord;
    }

    public void generateStatement(int before, int whereToGoAfter) throws Exception {
        printStatement(idExpression.toString() + " = " + expression.generateExpression().toString());

    }
}
