package expression.logical;

import expression.Expression;
import token.Token;

/**
 * Created by samy on 5/2/15.
 */
public class AndLogicalExpression extends LogicalExpression {
    public AndLogicalExpression(Token tok, Expression x1, Expression x2) {
        super(tok, x1, x2);
    }
    public void jumping ( int t , int f) throws Exception {
            int label = f != 0 ? f : nextLabelNumber() ;
            expression1.jumping ( 0 , label ) ;
            expression2.jumping (t , f ) ;
            if ( f == 0 ) printLabel(label) ;
    }
}
