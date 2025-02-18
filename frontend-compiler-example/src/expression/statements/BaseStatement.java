package expression.statements;

import expression.Node;

/**
 * samy on 5/2/15.
 */
public class BaseStatement extends Node {

    private int after = 0;
    private int before = 0;
    public static final BaseStatement Null = new BaseStatement();

    private static BaseStatement Enclosing = BaseStatement.Null;

    public BaseStatement() {
    }


    public static BaseStatement getEnclosing() {
        return Enclosing;
    }

    public static void setEnclosing(BaseStatement enclosing) {
        Enclosing = enclosing;
    }

    public void generateStatement(int before, int whereToGoAfter) throws Exception {
    }


    public int getAfter() {
        return after;
    }

    public void setAfter(int after) {
        this.after = after;
    }

    public int getBefore() {
        return before;
    }

    public void setBefore(int before) {
        this.before = before;
    }
}
