package expression.statements;

import expression.Expression;
import token.TypeWordToken;

/**
 * Created by samy on 5/2/15.
 */
public class DoBaseStatement extends BaseStatement {

    private Expression expression;
    private BaseStatement statement;


    public DoBaseStatement() {
        expression = null;
        statement = null;
    }

    public void init(Expression expression, BaseStatement statement) throws Exception {
        this.expression = expression;
        this.statement = statement;
        if (expression.getTypeWord() != TypeWordToken.Bool){
            expression.error("error boolan required");
        }

    }
    public void generateStatement(int whereToGoBack, int whereToGoAfter) throws Exception {
        this.setAfter(whereToGoAfter);
        this.setBefore(whereToGoBack);
        int label = nextLabelNumber();
        statement.generateStatement(whereToGoBack, label);
        printLabel(label);
        expression.jumping(whereToGoBack, 0);

    }
}
