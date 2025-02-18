package expression;

import token.NumToken;
import token.Token;
import token.TypeWordToken;
import token.WordToken;

/**
 * Created by samy on 5/2/15.
 */
public class ConstantExpression extends Expression {


    public static final ConstantExpression True = new ConstantExpression(WordToken.True, TypeWordToken.Bool);

    public static final ConstantExpression False = new ConstantExpression(WordToken.False, TypeWordToken.Bool);

    public ConstantExpression(Token tok, TypeWordToken p) {
        super(tok, p);
    }

    public ConstantExpression(int i){
        super (new NumToken( i ) , TypeWordToken.Int ) ;
    }

    public void jumping(int t, int f){
        if ( this == True && t != 0 ){
            printStatement("goto L " + t) ;
        }
        if ( this == False && f != 0 ) {
            printStatement("goto L " + f) ;
        }

    }

}
