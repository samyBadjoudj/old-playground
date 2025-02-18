package expression.statements;

/**
 * Created by samy on 5/2/15.
 */
public class SequenceBaseStatement extends BaseStatement {
    private BaseStatement firstSatement;
    private BaseStatement secondStatement;

    public SequenceBaseStatement(BaseStatement firstSatement, BaseStatement statement2) {
        this.firstSatement = firstSatement;
        secondStatement = statement2;
    }

    public void generateStatement(int previousLabel, int whereToGoAfter) throws Exception {
        if (firstSatement == BaseStatement.Null) {
            secondStatement.generateStatement(previousLabel, whereToGoAfter);
        }
        else if (secondStatement == BaseStatement.Null) {
            firstSatement.generateStatement(previousLabel, whereToGoAfter);
        }
        else {
            int label = nextLabelNumber();
            firstSatement.generateStatement(previousLabel, label);
            printLabel(label);
            secondStatement.generateStatement(label, whereToGoAfter);
        }
    }
}
