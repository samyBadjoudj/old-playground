package com.samy.jsonparser.parsers;


import com.samy.jsonparser.JSONObjectLexer;
import com.samy.jsonparser.tokens.TokenType;
import com.samy.jsonparser.visitors.Visitor;

/**
 * Created by Samy Badjoudj
 */
public class ObjectParser extends JSONParser{
    public ObjectParser(JSONObjectLexer jsonObjectLexer) {
        super(jsonObjectLexer);
    }


    @Override
    public void parse(Visitor visitor) {
        visitor.visit(this);
        new MemberParser(jsonObjectLexer).parse(visitor);
        if(TokenType.RIGHT_C_B == jsonObjectLexer.getToken().getType()){
            visitor.visit(this);
            jsonObjectLexer.nextToken();
        }
    }
}
