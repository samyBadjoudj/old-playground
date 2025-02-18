package com.samy.jsonparser.parsers;


import com.samy.jsonparser.JSONObjectLexer;
import com.samy.jsonparser.tokens.Token;
import com.samy.jsonparser.tokens.TokenType;
import com.samy.jsonparser.visitors.Visitor;

/**
 * Created by Samy Badjoudj
 */
public class ValueParser extends JSONParser {

    public ValueParser(JSONObjectLexer jsonObjectLexer) {
        super(jsonObjectLexer);
    }

    @Override
    public void parse(Visitor visitor) {
        Token token = jsonObjectLexer.nextToken() ;
        if (TokenType.LEFT_C_B == token.getType()) {
            new ObjectParser(jsonObjectLexer).parse(visitor);
        } else if (token.getType() == TokenType.LEFT_S_B) {
             new ArrayParser(jsonObjectLexer).parse(visitor);
        } else {
            visitor.visit(this);
            jsonObjectLexer.nextToken();
        }
    }
}
