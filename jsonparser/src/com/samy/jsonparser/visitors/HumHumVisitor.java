package com.samy.jsonparser.visitors;


import com.samy.jsonparser.parsers.*;

/**
 * Created by Samy Badjoudj
 */
public class HumHumVisitor implements Visitor {

    private final String PREFIX = "HumHun says:";

    @Override
    public void visit(ArrayParser parser) {
        spit(parser);

    }
    private void spit(JSONParser parser) {
        System.out.println(PREFIX +   parser.getClass().getName().substring(parser.getClass().getName().lastIndexOf(".")) + "            " + parser.getJsonObjectLexer().getToken().getValue() + "" );
    }
    @Override
    public void visit(ElementParser parser) {
        spit(parser);
    }

    @Override
    public void visit(MemberParser parser) {
        spit(parser);
    }

    @Override
    public void visit(ObjectParser parser) {
        spit(parser);
    }

    @Override
    public void visit(PairParser parser) {
        spit(parser);
    }

    @Override
    public void visit(ValueParser parser) {
        spit(parser);
    }
}
