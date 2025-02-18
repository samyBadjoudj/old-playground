package expression.operator;

import expression.Expression;
import expression.IdExpression;
import token.TagToken;
import token.TypeWordToken;
import token.WordToken;

/**
 * Created by samy on 5/2/15.
 */
public class AccessArrayExpression extends OperationExpression {

    private IdExpression array;
    private Expression indexToAccess;

    public AccessArrayExpression(IdExpression a, Expression i, TypeWordToken p) {
        super(new WordToken(" [] ", TagToken.INDEX), p);
        this.setArray(a);
        this.setIndexToAccess(i);
    }

    public Expression generateExpression() throws Exception {
        return new AccessArrayExpression(getArray(), getIndexToAccess().reduce(), getTypeWord());
    }

    public void jumping(int t, int f) throws Exception {
        emitjumps(reduce().toString(), t, f);

    }

    @Override
    public String toString() {
        return getArray() +"[" + getIndexToAccess() +"]";
    }

    public IdExpression getArray() {
        return array;
    }

    public void setArray(IdExpression array) {
        this.array = array;
    }

    public Expression getIndexToAccess() {
        return indexToAccess;
    }

    public void setIndexToAccess(Expression indexToAccess) {
        this.indexToAccess = indexToAccess;
    }
}
