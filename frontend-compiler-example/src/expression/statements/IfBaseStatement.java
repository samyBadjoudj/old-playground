package expression.statements;

import expression.Expression;
import token.TypeWordToken;

/**
 * Created by samy on 5/2/15.
 */
public class IfBaseStatement extends BaseStatement {
    private Expression expression;
    private BaseStatement statement;

    public IfBaseStatement(Expression expression, BaseStatement statement) throws Exception {
        this.expression = expression;
        this.statement = statement;
        if (expression.getTypeWord() != TypeWordToken.Bool){
            expression.error("boolean required in if ");
        }

    }

    @Override
    public void generateStatement(int before, int whereToGoAfter) throws Exception {
        int label = nextLabelNumber();
        expression.jumping(0, whereToGoAfter);
        printLabel(label);
        statement.generateStatement(label, whereToGoAfter);
    }
}
