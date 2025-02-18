package token;

/**
 * samy on 5/2/15.
 */
public class ArrayTypeWordToken extends TypeWordToken {


    private TypeWordToken of;
    private int size = 1;

    public ArrayTypeWordToken(int sz, TypeWordToken p) {
        super("[", TagToken.INDEX, sz * p.getWidth());
        size = sz;
        setOf(p);
    }

    public String toString() {
        return "[" + size + "]" + getOf().toString();
    }

    public TypeWordToken getOf() {
        return of;
    }

    public void setOf(TypeWordToken of) {
        this.of = of;
    }
}
