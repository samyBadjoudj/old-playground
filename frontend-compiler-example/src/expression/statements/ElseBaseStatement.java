package expression.statements;

import expression.Expression;
import token.TypeWordToken;

/**
 * Created by samy on 5/2/15.
 */
public class ElseBaseStatement extends BaseStatement {
    private Expression expression;
    private BaseStatement firstStatement, secondStatement;

    public ElseBaseStatement(Expression expression, BaseStatement firstStatement, BaseStatement secondStatement) throws Exception {
        this.expression = expression;
        this.firstStatement = firstStatement;
        this.secondStatement = secondStatement;
        if (this.expression.getTypeWord() != TypeWordToken.Bool) {
            this.expression.error("Must be a boolean");
        }
    }


    public void generateStatement(int before, int whereToGoAfter) throws Exception {
        int firstStatementLabel = nextLabelNumber();
        int secondStatmentLabel = nextLabelNumber();
        expression.jumping(0, secondStatmentLabel);
        printLabel(firstStatementLabel);
        firstStatement.generateStatement(firstStatementLabel, whereToGoAfter);
        printStatement("goto L" + whereToGoAfter);
        printLabel(secondStatmentLabel);
        secondStatement.generateStatement(secondStatmentLabel, whereToGoAfter);
    }
}
