package com.samy.jsonparser;


import com.samy.jsonparser.tokens.*;

/**
 * Created by Samy Badjoudj
 */
public class JSONObjectLexer {

    private SourceCode code;
    private Token token;


    public JSONObjectLexer(SourceCode code) {
        this.code = code;
    }

    public Token nextToken() {

        this.code.skipWhiteSpaces();
        char currchar = code.currentChar();
        if (Character.isDigit(currchar)) {
            this.token = new NumberToken(code, TokenType.NUMBER);
        } else {
            switch (currchar) {
                case '{':
                    token = new SymbolToken(code, TokenType.LEFT_C_B);
                    break;
                case ':':
                    token = new SymbolToken(code, TokenType.COLON);
                    break;
                case ',':
                    token = new SymbolToken(code, TokenType.COMMA);
                    break;
                case '}':
                    token = new SymbolToken(code, TokenType.RIGHT_C_B);
                    break;
                case '[':
                    token = new SymbolToken(code, TokenType.LEFT_S_B);
                    break;
                case ']':
                    token = new SymbolToken(code, TokenType.RIGHT_S_B);
                    break;
                case '\"':
                    token = new StringToken(code, TokenType.STRING);
                    break;
                case '$':
                    token = new SymbolToken(code, TokenType.EOF);
                    break;
            }
        }


        return token;
    }


    public Token getToken() {
        return token;
    }

    public SourceCode getCode() {
        return code;
    }
}
