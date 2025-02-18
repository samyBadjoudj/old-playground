package expression.statements;

/**
 * Created by samy on 5/2/15.
 */
public class BreakSatement extends BaseStatement {
    private final BaseStatement baseStatement;

    public BreakSatement() throws Exception {

        if ( BaseStatement.getEnclosing() == null ) error ( " unenc losed break" ) ;
        baseStatement = BaseStatement.getEnclosing();

    }
    public void generateStatement(int before, int whereToGoAfter) {
        printStatement("goto L" + baseStatement.getAfter()) ;
    }
}
