package com.samy.jsonparser.parsers;


import com.samy.jsonparser.JSONObjectLexer;
import com.samy.jsonparser.tokens.TokenType;
import com.samy.jsonparser.visitors.Visitor;

/**
 * Created by Samy Badjoudj
 */
public class PairParser extends JSONParser {

    public PairParser(JSONObjectLexer jsonObjectLexer) {
        super(jsonObjectLexer);
    }

    @Override
    public void parse(Visitor visitor) {
        jsonObjectLexer.nextToken();
        visitor.visit(this);
        jsonObjectLexer.nextToken();
        visitor.visit(this);
        new ValueParser(jsonObjectLexer).parse(visitor);
        if(TokenType.COMMA == jsonObjectLexer.getToken().getType()){
            visitor.visit(this);
        }

    }
}
