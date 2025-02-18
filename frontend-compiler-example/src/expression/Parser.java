package expression;

import expression.logical.AndLogicalExpression;
import expression.logical.NotLogicalExpression;
import expression.logical.OrLogicalExpression;
import expression.logical.RelLogicalExpression;
import expression.operator.AccessArrayExpression;
import expression.operator.ArithmeticExpression;
import expression.operator.UnaryExpression;
import expression.statements.*;
import lexer.Lexer;
import token.*;

/**
 * samy on 5/2/15.
 */
public class Parser {
    private Lexer lex;
    private Token look;
    private ContextHolder top = null;
    private int used = 0;

    public Parser(Lexer lexer) throws Exception {
        lex = lexer;
        move();
    }

    void move() throws Exception {
        look = lex.scan();
    }

    void error(String s) {
        throw new Error("nearl ine " + Lexer.currentLine + " :" + s);
    }

    void match(int t) throws Exception {
        if (look.getTag() == t) move();
        else error(" syntax error ");
    }

    public void program() throws Exception {
        BaseStatement s = block();
        int begin = s.nextLabelNumber();
        int after = s.nextLabelNumber();
        s.printLabel(begin);
        s.generateStatement(begin, after);
        s.printLabel(after);
    }

    private BaseStatement block() throws Exception {
        match('{');
        ContextHolder savedContextHolder = top;
        top = new ContextHolder(top);
        decls();
        BaseStatement s = stmts();
        match('}');
        top = savedContextHolder;
        return s;
    }

    private BaseStatement stmts() throws Exception {
        if (look.getTag() == '}') {
            return BaseStatement.Null;
        } else {
            return new SequenceBaseStatement(stmt(), stmts());
        }
    }

    private BaseStatement stmt() throws Exception {

        switch (look.getTag()) {
            case ';':
                move();
                return BaseStatement.Null;
            case TagToken.IF:
                return tryParseIfElseStatement();
            case TagToken.WHILE:
                WhileStatement whilenode;
                whilenode = tryParseWhileStatement();
                return whilenode;
            case TagToken.DO:
                return tryParseDoStatement();
            case TagToken.BREAK:
                match(TagToken.BREAK);
                match(';');
                return new BreakSatement();
            case TagToken.CONTINUE:
                match(TagToken.CONTINUE);
                match(';');
                return new ContinueStatement();
            case '{':
                return block();
            default:
                return assign();


        }
    }

    private BaseStatement tryParseIfElseStatement() throws Exception {
        Expression expression;
        BaseStatement firstStatment;
        BaseStatement secondeStatement;
        match(TagToken.IF);
        match('(');
        expression = bool();
        match(')');
        firstStatment = stmt();
        if (look.getTag() != TagToken.ELSE) {
            return new IfBaseStatement(expression, firstStatment);
        }
        match(TagToken.ELSE);
        secondeStatement = stmt();
        return new ElseBaseStatement(expression, firstStatment, secondeStatement);
    }

    private WhileStatement tryParseWhileStatement() throws Exception {
        BaseStatement savedDoOrWhileStatement;
        Expression expression;
        BaseStatement firstStatment;
        WhileStatement whilenode = new WhileStatement();
        savedDoOrWhileStatement = BaseStatement.getEnclosing();
        BaseStatement.setEnclosing(whilenode);
        match(TagToken.WHILE);
        match('(');
        expression = bool();
        match(')');
        firstStatment = stmt();
        whilenode.init(expression, firstStatment);
        BaseStatement.setEnclosing(savedDoOrWhileStatement);
        return whilenode;
    }

    private DoBaseStatement tryParseDoStatement() throws Exception {
        BaseStatement savedDoOrWhileStatement;
        BaseStatement firstStatment;
        Expression x;
        DoBaseStatement donode = new DoBaseStatement();
        savedDoOrWhileStatement = BaseStatement.getEnclosing();
        BaseStatement.setEnclosing(donode);
        match(TagToken.DO);
        firstStatment = stmt();
        match(TagToken.WHILE);
        match('(');
        x = bool();
        match(')');
        match(';');
        donode.init(x, firstStatment);
        BaseStatement.setEnclosing(savedDoOrWhileStatement);
        return donode;
    }

    private BaseStatement assign() throws Exception {
        BaseStatement baseStatement;
        Token t = look;
        match(TagToken.ID);
        IdExpression idExpression = top.getVariable(t);
        if (idExpression == null){
            error(t.toString() + " undeclared ");
        }
        if (look.getTag() == '=') {
            move();
            baseStatement = new SetBaseStatement(idExpression, bool());
        } else {
            AccessArrayExpression x = offset(idExpression);
            match('=');
            baseStatement = new SetElemBaseStatement(x, bool());
        }
        match(';');
        return baseStatement;
    }

    private AccessArrayExpression offset(IdExpression a) throws Exception {
        Expression i;
        Expression w;
        Expression t1, t2, loc;

        TypeWordToken typeWord = a.getTypeWord();
        match('[');
        i = bool();
        match(']');
        typeWord = ((ArrayTypeWordToken) typeWord).getOf();
        w = new ConstantExpression(typeWord.getWidth());
        t1 = new ArithmeticExpression(new Token('*'), i, w);
        loc = t1;
        while (look.getTag() == '[') {

            match('[');
            i = bool();
            match(']');
            typeWord = ((ArrayTypeWordToken) typeWord).getOf();
            w = new ConstantExpression(typeWord.getWidth());
            t1 = new ArithmeticExpression(new Token('*'), i, w);
            t2 = new ArithmeticExpression(new Token('+'), loc, t1);
            loc = t2;
        }
        return new AccessArrayExpression(a, loc, typeWord);
    }


    private void decls() throws Exception {
        while (look.getTag() == TagToken.BASIC) {
            TypeWordToken p = type();
            Token tok = look;
            match(TagToken.ID);
            match(';');
            IdExpression idExpression = new IdExpression((WordToken) tok, p, used);
            top.put(tok, idExpression);
            used = used + p.getWidth();
        }
    }

    private TypeWordToken type() throws Exception {

        TypeWordToken p = (TypeWordToken) look;

        match(TagToken.BASIC);
        if (look.getTag() != '[') {
            return p;
        } else {
            return dims(p);
        }


    }

    private TypeWordToken dims(TypeWordToken p) throws Exception {
        match('[');
        Token tok = look;
        match(TagToken.NUM);
        match(']');

        if (look.getTag() == '[') {
            p = dims(p);
        }

        return new ArrayTypeWordToken(((NumToken) tok).value, p);

    }

    private Expression bool() throws Exception {
        Expression x = join();
        while (look.getTag() == TagToken.OR) {
            Token tok = look;
            move();
            x = new OrLogicalExpression(tok, x, join());
        }
        return x;
    }

    private Expression join() throws Exception {
        Expression x = equality();
        while (look.getTag() == TagToken.AND) {
            Token tok = look;
            move();
            x = new AndLogicalExpression(tok, x, equality());
        }
        return x;

    }

    private Expression equality() throws Exception {
        Expression x = rel();
        while (look.getTag() == TagToken.EQ || look.getTag() == TagToken.NE) {
            Token tok = look;
            move();
            x = new RelLogicalExpression(tok, x, rel());
        }
        return x;
    }

    private Expression rel() throws Exception {
        Expression x = expr();
        switch (look.getTag()) {
            case '<':
            case '>':
            case TagToken.LE:
            case TagToken.GE:
                Token tok = look;
                move();
                return new RelLogicalExpression(tok, x, expr());
            default:
                return x;
        }
    }

    private Expression expr() throws Exception {
        Expression x = term();
        while (look.getTag() == '+' || look.getTag() == '-') {
            Token token = look;
            move();
            x = new ArithmeticExpression(token, x, term());

        }
        return x;
    }

    private Expression term() throws Exception {
        Expression x = unary();
        while (look.getTag() == '*' || look.getTag() == '/') {
            Token tok = look;
            move();
            x = new ArithmeticExpression(tok, x, unary());
        }

        return x;
    }

    private Expression unary() throws Exception {
        if (look.getTag() == '-') {
            move();
            return new UnaryExpression(WordToken.minus, unary());

        } else if (look.getTag() == '!') {
            Token tok = look;
            return new NotLogicalExpression(tok, unary());

        } else {
            return factor();
        }
    }

    private Expression factor() throws Exception {

        Expression x = null;
        switch (look.getTag()) {
            case '(':
                move();
                x = bool();
                match(')');
                return x;
            case TagToken.NUM:
                x = new ConstantExpression(look, TypeWordToken.Int);
                move();
                return  x;
            case TagToken.REAL:
                x = new ConstantExpression(look, TypeWordToken.Float);
                move();
                return  x;
            case TagToken.TRUE:
                x = ConstantExpression.True;
                move();
                return  x;
            case TagToken.FALSE:
                x = ConstantExpression.False;
                move();
                return  x;
            default:
                error("Syntax error");
                return null;
            case TagToken.ID:
                String s = look.toString();
                IdExpression idExpression = top.getVariable(look);
                if (idExpression == null){
                    error(look.toString() + "undeclared");
                }
                move();
                if (look.getTag() != '[') return idExpression;
                else return offset(idExpression);
        }
    }


}
