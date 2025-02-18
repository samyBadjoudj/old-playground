package expression.operator;

import expression.Expression;
import token.Token;
import token.TypeWordToken;

/**
 * Created by samy on 5/2/15.
 */
public class UnaryExpression extends OperationExpression {

    private Expression expression;

    public UnaryExpression(Token tok, Expression x) throws Exception {
        super(tok, null);
        this.expression = x;
        this.setTypeWord(TypeWordToken.max(TypeWordToken.Int, expression.getTypeWord()));

        if (getTypeWord() == null){
            error("typeWord error");
        }
    }

    public Expression generateExpression() throws Exception {
        return new UnaryExpression(getOperator(), expression.reduce());
    }

    public String toString() {
        return getOperator().toString() + " " + expression.toString();
    }
}
