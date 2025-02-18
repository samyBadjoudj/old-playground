package expression.statements;

import expression.Expression;
import token.TypeWordToken;

/**
 * Created by samy on 5/2/15.
 */
public class WhileStatement extends BaseStatement {

    private Expression expression;
    private BaseStatement baseStatement;

    public WhileStatement() {
        expression = null;
        baseStatement = null;
    }

    public void init(Expression expression, BaseStatement statement) throws Exception {

        this.expression = expression;
        this.baseStatement = statement;

        if (this.expression.getTypeWord() != TypeWordToken.Bool){
            this.expression.error("Must be a boolean");
        }

    }

    public void generateStatement(int before, int whereToGoAfter) throws Exception {
        this.setAfter(whereToGoAfter);
        this.setBefore(before);
        expression.jumping(0, whereToGoAfter);
        int label = nextLabelNumber();
        printLabel(label);
        baseStatement.generateStatement(label, before);
        printStatement("goto L" + before);
    }
}
