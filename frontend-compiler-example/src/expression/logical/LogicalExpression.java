package expression.logical;

import expression.Expression;
import expression.Temp;
import token.Token;
import token.TypeWordToken;

/**
 * Created by samy on 5/2/15.
 */
public class LogicalExpression extends Expression {
    protected Expression expression1, expression2;

    public LogicalExpression(Token tok, Expression x1, Expression x2) {
        super(tok, null);
        expression2 = x2;
        expression1 = x1;
        setTypeWord(check(expression1.getTypeWord(), expression2.getTypeWord()));
    }

    private TypeWordToken check(TypeWordToken typeWord, TypeWordToken typeWord1) {
        if (TypeWordToken.Bool == typeWord && TypeWordToken.Bool == typeWord1) return TypeWordToken.Bool;
        return null;
    }

    @Override
    public Expression generateExpression() throws Exception {
        int f = nextLabelNumber();
        int a = nextLabelNumber();
        Temp temp = new Temp(getTypeWord());
        this.jumping(0, f);
        printStatement(temp.toString() + " = true ");
        printStatement(" goto L " + a);
        printLabel(f);
        printStatement(temp.toString() + " = false");
        printLabel(a);
        return temp;
    }

    @Override
    public String toString() {
        return  expression1 +" "+ getOperator() + " " +expression2 ;
    }
}
