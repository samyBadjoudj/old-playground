package com.samy.jsonparser.parsers;


import com.samy.jsonparser.JSONObjectLexer;
import com.samy.jsonparser.tokens.Token;
import com.samy.jsonparser.tokens.TokenType;
import com.samy.jsonparser.visitors.Visitor;

/**
 * Created by Samy Badjoudj
 */
public class ElementParser extends JSONParser {
    public ElementParser(JSONObjectLexer jsonObjectLexer) {
        super(jsonObjectLexer);
    }

    @Override
    public void parse(Visitor visitor) {
        new ValueParser(jsonObjectLexer).parse(visitor);
        Token token = jsonObjectLexer.getToken();
        if (TokenType.COMMA == token.getType()) {
            visitor.visit(this);
           new ElementParser(jsonObjectLexer).parse(visitor);
        }

    }
}
