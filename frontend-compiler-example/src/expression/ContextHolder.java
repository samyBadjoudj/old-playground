package expression;

import token.Token;

import java.util.Hashtable;

/**
 * samy on 5/2/15.
 */
public class ContextHolder {

    private Hashtable context;
    private ContextHolder previousContextHolder;

    public ContextHolder(ContextHolder currentContextHolder) {
        this.context = new Hashtable();
        this.previousContextHolder = currentContextHolder;
    }

    public void put(Token w, IdExpression i) {
        context.put(w, i);
    }

    public IdExpression getVariable(Token variableToken) {
        IdExpression foundVariable = null;
        for (ContextHolder e = this; e != null; e = e.previousContextHolder) {
            IdExpression found = (IdExpression) (e.context.get(variableToken));
            if (found != null) {
                foundVariable =  found;
                break;
            }
        }
        return foundVariable;
    }
}

