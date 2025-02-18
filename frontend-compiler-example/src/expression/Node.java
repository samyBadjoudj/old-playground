package expression;

import lexer.Lexer;

/**
 * Created by samy on 5/2/15.
 */
public class Node {

    private static int CURRENT_LINE = 0;
    private static int LABELS = 0;

    public Node() {
        CURRENT_LINE = Lexer.currentLine;
    }


    public void error(String s) throws Error {
        throw new Error("near currentLine " + CURRENT_LINE + ": " + s);
    }

    public int nextLabelNumber() {
        return LABELS++;
    }

    public void printLabel(int i) {
        printStatement("L" + i + ":");
    }

    public void printStatement(String s) {
        System.out.println("\t" + s);
    }
}
