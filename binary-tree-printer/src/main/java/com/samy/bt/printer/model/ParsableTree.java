package com.samy.bt.printer.model;

public class ParsableTree {
    private String content;
    private Integer currentPosition= 0;

    public ParsableTree(String content) {
        this.content = content;
    }

    public void shiftRight(){
        currentPosition++;
    }

    public char getCurrentChar(){
        return content.charAt(currentPosition);
    }

    public boolean parsingNotFinished(){
        return currentPosition < content.length();
    }
}
