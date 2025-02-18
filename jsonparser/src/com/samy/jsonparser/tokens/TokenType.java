package com.samy.jsonparser.tokens;

/**
 * Created by Samy Badjoudj
 */
public enum TokenType implements Type {
    STRING, LEFT_C_B("{"),RIGHT_C_B("}"),COMMA(","),LEFT_S_B("["), VALUE, RIGHT_S_B("]"), MEMBER, ROOT, COLON(":"), OBJECT, NUMBER,EOF;

    private String label ="";
    TokenType(String s) {
        this.label = s;
    }

    TokenType() {

    }

    @Override
    public String getLabel() {
        if(label.equals("")){
            return this.name();
        }
        return this.label;
    }
}
