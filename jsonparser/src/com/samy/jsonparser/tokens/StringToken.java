package com.samy.jsonparser.tokens;

import com.samy.jsonparser.SourceCode;

/**
 * Created by Samy Badjoudj
 */
public class StringToken implements Token {

    private String value;
    private TokenType type;
    private SourceCode sourceCode;


    public StringToken(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public StringToken(SourceCode sourceCode, TokenType type) {
        this.sourceCode=sourceCode;
        this.type = type;
        extractToken();
    }

    public void extractToken(){
        StringBuilder stringBuilder = new StringBuilder();
        char currentChar = sourceCode.nextChar();
        while(currentChar != '\"'){
            stringBuilder.append(currentChar);
            currentChar = sourceCode.nextChar();
        }
        sourceCode.nextChar();
        value = stringBuilder.toString();
        if(type == null) type = TokenType.STRING;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public TokenType getType() {
        return type;
    }
}
