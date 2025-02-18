package com.samy.jsonparser.tokens;

import com.samy.jsonparser.SourceCode;

/**
 * Created by Samy Badjoudj
 */
public class NumberToken implements Token {

    public String value;
    public TokenType type;
    public SourceCode sourceCode;


    public NumberToken(SourceCode sourceCode, TokenType type) {
        this.sourceCode=sourceCode;
        this.type = type;
        extractToken();
    }

    public void extractToken(){
        StringBuilder stringBuilder = new StringBuilder();
        char currentChar = sourceCode.currentChar();
        while(Character.isDigit(currentChar) || currentChar == '.'){
            stringBuilder.append(currentChar);
            currentChar = sourceCode.nextChar();
        }
        value=stringBuilder.toString();
        if(type == null) type = TokenType.NUMBER;
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
