package com.samy.bt.printer.model.printable.console;

import com.samy.bt.printer.model.printable.Printable;

public class LefVertexConsole implements Printable<String> {
    @Override
    public String getPrintableValue() {
        return Printable.ANSI_GREEN +"/"+ Printable.ANSI_RESET;
        }
}
