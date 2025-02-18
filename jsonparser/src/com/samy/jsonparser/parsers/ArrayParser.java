package com.samy.jsonparser.parsers;


import com.samy.jsonparser.JSONObjectLexer;
import com.samy.jsonparser.tokens.TokenType;
import com.samy.jsonparser.visitors.Visitor;

/**
 * Created by Samy Badjoudj
 */
public class ArrayParser extends JSONParser{
    public ArrayParser(JSONObjectLexer jsonObjectLexer) {
        super(jsonObjectLexer);
    }

    @Override
    public void parse(Visitor visitor) {
        visitor.visit(this);
        new ElementParser(jsonObjectLexer).parse(visitor);
        if(TokenType.RIGHT_S_B == jsonObjectLexer.getToken().getType()){
            visitor.visit(this);
            jsonObjectLexer.nextToken();
        }
    }
}
