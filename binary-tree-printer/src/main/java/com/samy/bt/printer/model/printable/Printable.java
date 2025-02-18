package com.samy.bt.printer.model.printable;

public interface Printable<T> {

    String ANSI_RESET = "\u001B[0m";
    String ANSI_RED = "\u001B[31m";
    String ANSI_GREEN = "\u001B[32m";


    T getPrintableValue();

    default int getPrintableLengthValue(){
        return 1;
    }

    default int getPrintableOffset(){
        return 1;
    }


}
