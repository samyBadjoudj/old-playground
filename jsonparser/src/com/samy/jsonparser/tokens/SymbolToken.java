package com.samy.jsonparser.tokens;


import com.samy.jsonparser.SourceCode;

/**
 * Created by Samy Badjoudj
 */
public class SymbolToken implements Token {

    private String value;
    private TokenType type;
    private SourceCode sourceCode;

    public String getValue() {
        return value;
    }

    public TokenType getType() {
        return type;
    }


    public SymbolToken(SourceCode sourceCode, TokenType tokenType) {
        this.type = tokenType;
        this.sourceCode = sourceCode;
        extractToken();
    }



    public void extractToken() {
        this.value = String.valueOf(sourceCode.currentChar());
        this.sourceCode.nextChar();
        if (this.type == null) {
            this.type = TokenType.STRING;
        }

    }
}
