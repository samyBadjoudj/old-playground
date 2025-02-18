package com.samy.bt.printer.model.printable.console;

import com.samy.bt.printer.model.printable.Printable;

public class RightVertexConsole implements Printable<String> {
    @Override
    public String getPrintableValue() {
        return Printable.ANSI_RED +"\\"+ Printable.ANSI_RESET;
   }

}
