package com.samy.jsonparser.visitors;


import com.samy.jsonparser.parsers.*;

/**
 * Created by Samy Badjoudj
 */
public interface Visitor {

    public void visit(ArrayParser parser);
    public void visit(ElementParser parser);
    public void visit(MemberParser parser);
    public void visit(ObjectParser parser);
    public void visit(PairParser parser);
    public void visit(ValueParser parser);

}
