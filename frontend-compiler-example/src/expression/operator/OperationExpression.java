package expression.operator;

import expression.Expression;
import expression.Temp;
import token.Token;
import token.TypeWordToken;

/**
 * Created by samy on 5/2/15.
 */
public class OperationExpression extends Expression {
    public OperationExpression(Token tok, TypeWordToken p) {
        super(tok, p);
    }

    public Expression reduce () throws Exception {
        Expression x = generateExpression() ;
        Temp temp = new Temp (getTypeWord());
        printStatement(temp.toString() + " = " + x.toString());
        return temp;
    }



}
