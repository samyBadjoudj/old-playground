package com.samy.jsonparser.parsers;


import com.samy.jsonparser.JSONObjectLexer;
import com.samy.jsonparser.tokens.Token;
import com.samy.jsonparser.tokens.TokenType;
import com.samy.jsonparser.visitors.Visitor;

/**
 * Created by Samy Badjoudj
 */
public class JSONParser {

    protected JSONObjectLexer jsonObjectLexer;

    public JSONParser(JSONObjectLexer jsonObjectLexer) {
        this.setJsonObjectLexer(jsonObjectLexer);
    }

    public void parse(Visitor visitor) {
       Token token = jsonObjectLexer.nextToken();
        while (TokenType.EOF != token.getType()) {
            if (TokenType.LEFT_C_B == token.getType()) {
                new ObjectParser(this.jsonObjectLexer).parse(visitor);
            } else if (TokenType.LEFT_S_B == token.getType()) {
              new ArrayParser(jsonObjectLexer).parse(visitor);
            } else if (TokenType.COMMA == token.getType()) {
                new MemberParser(jsonObjectLexer).parse(visitor);
            }
            token = jsonObjectLexer.nextToken();
        }

    }

    public JSONObjectLexer getJsonObjectLexer() {
        return jsonObjectLexer;
    }

    public void setJsonObjectLexer(JSONObjectLexer jsonObjectLexer) {
        this.jsonObjectLexer = jsonObjectLexer;
    }


}
