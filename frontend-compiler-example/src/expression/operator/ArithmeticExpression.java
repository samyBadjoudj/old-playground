package expression.operator;

import expression.Expression;
import token.Token;
import token.TypeWordToken;

/**
 * Created by samy on 5/2/15.
 */
public class ArithmeticExpression extends OperationExpression {


    private Expression expression1, expression2;

    public ArithmeticExpression(Token tok, Expression expression, Expression expression2) throws Exception {
        super(tok, null);
        this.expression1 = expression;
        this.expression2 = expression2;
        this.setTypeWord(TypeWordToken.max(expression1.getTypeWord(), this.expression2.getTypeWord()));
        if (getTypeWord() == null) error(" typeWord error");
    }

    public Expression generateExpression() throws Exception {
        return new ArithmeticExpression(getOperator(), expression1.reduce(), expression2.reduce());
    }

    public String toString() {
        return expression1.toString() + " " + getOperator().toString() + " " + expression2.toString();
    }
}
