package com.samy.jsonparser;

/**
 * Created by Samy Badjoudj
 */
public class SourceCode {
    public String source;
    public int    currPos;

    public char currentChar(){
        return source.charAt(currPos);
    }

    public char peekChar(){
        return source.charAt(currPos+1);
    }
    public char nextChar(){
        if(currPos+1<source.length()){currPos++; }else { return '$';}
        return source.charAt(currPos);
    }


    public SourceCode(String source) {
        this.source = source;
        this.currPos = 0;
    }

    public void skipWhiteSpaces(){
        while(source.charAt(currPos) == ' ' || source.charAt(currPos) == '\n' ) currPos++;
    }

}
