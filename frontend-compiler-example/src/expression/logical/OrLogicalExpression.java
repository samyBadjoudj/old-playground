package expression.logical;

import expression.Expression;
import token.Token;

/**
 * Created by samy on 5/2/15.
 */
public class OrLogicalExpression extends LogicalExpression {
    public OrLogicalExpression(Token tok, Expression x1, Expression x2) {
        super(tok, x1, x2);
    }



    public void jumping ( int t , int f) throws Exception {
        int label = t != 0 ? t : nextLabelNumber() ;
        expression1.jumping(label , 0 ) ;
        expression2.jumping(t , f ) ;
        if ( t == 0 ) printLabel(label) ;
    }
}
