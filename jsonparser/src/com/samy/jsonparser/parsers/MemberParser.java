package com.samy.jsonparser.parsers;


import com.samy.jsonparser.JSONObjectLexer;
import com.samy.jsonparser.tokens.TokenType;
import com.samy.jsonparser.visitors.Visitor;

/**
 * Created by Samy Badjoudj
 */
public class MemberParser extends JSONParser {

    public MemberParser(JSONObjectLexer jsonObjectLexer) {
        super(jsonObjectLexer);
    }

    @Override
    public void parse(Visitor visitor) {
        new PairParser(jsonObjectLexer).parse(visitor);
        if(TokenType.COMMA == jsonObjectLexer.getToken().getType()) {
            new MemberParser(jsonObjectLexer).parse(visitor);
        }

    }
}
