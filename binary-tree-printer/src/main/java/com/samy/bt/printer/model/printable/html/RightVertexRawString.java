package com.samy.bt.printer.model.printable.html;

import com.samy.bt.printer.model.printable.Printable;

public class RightVertexRawString implements Printable<String> {
    @Override
    public String getPrintableValue() {
        return "\\";    }


}
