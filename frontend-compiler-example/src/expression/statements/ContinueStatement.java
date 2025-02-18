package expression.statements;

/**
 * Created by samy on 5/2/15.
 */
public class ContinueStatement extends BaseStatement {


    private final BaseStatement baseStatement;

    public ContinueStatement() throws Exception {
        if ( BaseStatement.getEnclosing() == null ) error ( " un enclosed break" ) ;
        baseStatement = BaseStatement.getEnclosing();

    }

    @Override
    public void generateStatement(int before, int whereToGoAfter) throws Exception {
        printStatement("goto L" + baseStatement.getBefore()) ;

    }
}
