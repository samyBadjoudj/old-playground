package expression;

import token.Token;
import token.TypeWordToken;

/**
 * samy on 5/2/15.
 */
public class Expression extends Node {

    private Token operator;
    private TypeWordToken typeWord;
    private static final String GOTO_LABEL = " goto L";
    ;

    public Expression(Token tok, TypeWordToken p) {
        setOperator(tok);
        setTypeWord(p);
    }


    public void jumping(int t, int f) throws Exception {
        emitjumps(toString(), t, f);
    }

    public void emitjumps(String condition, int _true, int _false) {
        if (_true != 0 && _false != 0) {
            printStatement("if " + condition + GOTO_LABEL + _true);
            printStatement(GOTO_LABEL + _false);
        } else if (_true != 0) {
            printStatement("if " + condition + GOTO_LABEL + _true);
        } else if (_false != 0) {
            printStatement(" if not true " + condition + GOTO_LABEL + _false);
        }
    }



    public TypeWordToken getTypeWord() {
        return typeWord;
    }
    public Expression generateExpression() throws Exception {
        return this;
    }

    public Expression reduce() throws Exception {
        return this;
    }

    public void setOperator(Token operator) {
        this.operator = operator;
    }

    public void setTypeWord(TypeWordToken typeWord) {
        this.typeWord = typeWord;
    }
    public Token getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return  "(" + getTypeWord() + ") " + getOperator();
    }


}
