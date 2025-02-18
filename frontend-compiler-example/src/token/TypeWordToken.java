package token;

/**
 * Created by samy on 5/2/15.
 */
public class TypeWordToken extends WordToken {

    private final int width;


    public TypeWordToken(String value, int t, int width) {
        super(value, t);
        this.width = width;
    }

    public static final TypeWordToken
            Int = new TypeWordToken("int", TagToken.BASIC, 4),
            Float = new TypeWordToken("float", TagToken.BASIC, 8),
            Char = new TypeWordToken("char", TagToken.BASIC, 1),
            Bool = new TypeWordToken("bool", TagToken.BASIC, 1);

    public static TypeWordToken max(TypeWordToken typeWord, TypeWordToken typeWord1) {
        return Float == typeWord || Float == typeWord1 ? Float : Int;
    }

    public static boolean numeric(TypeWordToken p2) {

        return p2 == Int || p2 == Float;
    }

    public int getWidth() {
        return width;
    }
}
