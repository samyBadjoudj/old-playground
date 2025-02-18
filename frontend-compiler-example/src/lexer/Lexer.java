package lexer;

import token.*;

import java.util.Hashtable;

/**
 * Created by samy on 5/2/15.
 */
public class Lexer {
    public static int currentLine = 1;
    private int index = -1;

    private char currentChar = ' ';
    private Hashtable wordTokens = new Hashtable();
    private static final char EXCLAMATION_SIGN = '!';
    private static final char AND_SIGN = '&';
    private static final char OR_SIGN = '|';
    private static final char EQUAL_SIGN = '=';
    private static final char GREATER_SIGN = '>';
    private static final char LESS_SIGN = '<';

    private String contentToParse;

    private void reserve(WordToken w) {
        wordTokens.put(w.getValue(), w);
    }

    public Lexer(String contentToParse) {
        this.contentToParse = contentToParse;
        reserve(WordToken.If);
        reserve(WordToken.Else);
        reserve(WordToken.While);
        reserve(WordToken.Do);
        reserve(WordToken.Break);
        reserve(WordToken.Continue);
        reserve(WordToken.True);
        reserve(WordToken.False);
        reserve(TypeWordToken.Int);
        reserve(TypeWordToken.Char);
        reserve(TypeWordToken.Bool);
        reserve(TypeWordToken.Float);
    }


    public Token scan(){


        while (index < contentToParse.length()) {
            readch();
            if (currentChar == '\n') {
                currentLine++;
            } else if (currentChar != ' ' && currentChar != '\t') {
                break;
            }
        }
        switch (currentChar) {
            case AND_SIGN:
                if (readch(AND_SIGN)) return WordToken.and;
                else return new Token(AND_SIGN);
            case OR_SIGN:
                if (readch(OR_SIGN)) return WordToken.or;
                else return new Token(OR_SIGN);
            case EQUAL_SIGN:
                if (readch(EQUAL_SIGN)) return WordToken.eq;
                else return new Token(EQUAL_SIGN);
            case EXCLAMATION_SIGN:
                if (readch(EQUAL_SIGN)) return WordToken.ne;
                else return new Token(EXCLAMATION_SIGN);
            case LESS_SIGN:
                if (readch(EQUAL_SIGN)) return WordToken.le;
                else return new Token(LESS_SIGN);
            case GREATER_SIGN:
                if (readch(EQUAL_SIGN)) return WordToken.ge;
                else return new Token(GREATER_SIGN);
        }


        if (Character.isDigit(currentChar)) {
            return parseNumber();
        }

        if (Character.isLetter(currentChar)) {
            String identifierParsed = parseId();
            WordToken wordFound = (WordToken) wordTokens.get(identifierParsed);
            if (wordFound != null) {
                return wordFound;
            }
            wordFound = new WordToken(identifierParsed, TagToken.ID);
            wordTokens.put(identifierParsed, wordFound);
            return wordFound;
        }
        return new Token(currentChar);
    }

    private Token parseNumber()  {
        int parsedNonDecimal = 0;
        for (; ; ) {
            parsedNonDecimal = 10 * parsedNonDecimal + Character.digit(currentChar, 10);
            if (Character.isDigit(consultNextChar())) {
                readch();
            } else {
                break;
            }
        }

        if (consultNextChar() != '.') {
            return new NumToken(parsedNonDecimal);
        }
        readch();
        float decimalNumber = parsedNonDecimal;
        float d = 10;
        for (; ; ) {
            if (Character.isDigit(consultNextChar())) {
                readch();
            } else {
                break;
            }
            if (!Character.isDigit(currentChar)) break;
            decimalNumber = decimalNumber + Character.digit(currentChar, 10) / d;
            d = d * 10;
        }
        return new RealToken(decimalNumber);
    }

    private String parseId() {
        StringBuilder parsedId = new StringBuilder();

        for (;;) {
            parsedId.append(currentChar);
            if (Character.isLetterOrDigit(consultNextChar())) {
                readch();
            } else {
                break;
            }

        }
        return parsedId.toString();
    }

    private void readch(){
        index++;
        if (index < contentToParse.length()) {
            currentChar = contentToParse.charAt(index);
        }
    }

    private char consultNextChar() {
        if (index + 1 < contentToParse.length()) {
            return contentToParse.charAt(index + 1);
        }
        return 0;
    }

    private boolean readch(char c) {
        readch();
        if (currentChar != c) {
            index--;
            return false;
        }
        currentChar = ' ';
        return true;
    }

}
