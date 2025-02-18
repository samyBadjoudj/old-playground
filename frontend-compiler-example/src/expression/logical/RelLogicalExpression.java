package expression.logical;

import expression.Expression;
import token.Token;
import token.TypeWordToken;

/**
 * Created by samy on 5/2/15.
 */
public class RelLogicalExpression extends LogicalExpression {

    public RelLogicalExpression(Token tok, Expression expression, Expression expression2) {
        super(tok, expression, expression2);
        this.setTypeWord(check(this.expression1.getTypeWord(), this.expression2.getTypeWord()));
    }

    public TypeWordToken check(TypeWordToken pi, TypeWordToken p2) {
        return pi == p2 ? TypeWordToken.Bool : null;
    }

    public void jumping(int t, int f) throws Exception {
        Expression a = expression1.reduce();
        Expression b = expression2.reduce();
        String ifstatementPrinted =  " " + a.toString() + " " + getOperator().toString() + " " + b.toString();
        emitjumps(ifstatementPrinted, t, f);
    }
}
