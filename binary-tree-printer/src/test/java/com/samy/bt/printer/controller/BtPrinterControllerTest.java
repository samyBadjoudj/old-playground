package com.samy.bt.printer.controller;

import com.samy.bt.printer.BinaryTreeService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class BtPrinterControllerTest {

    @Test
    public void printBst() {
        BtPrinterController btPrinterController = new BtPrinterController(new BinaryTreeService());
        String  treeFromPatternString= btPrinterController.printBtFromParsable("4(2(3)(1))(6(5)(9))");
        String treeFromArrayString = btPrinterController.printBstFromArrayString("1,2,3");

    Assert.assertEquals(                        "          2\n" +
                                                        "        /  \\\n" +
                                                        "     1       3\n",treeFromArrayString);

        Assert.assertEquals(    "                    4\n" +
                                        "                 /    \\\n" +
                                        "              /         \\\n" +
                                        "          2                 6\n" +
                                        "       /    \\            /    \\\n" +
                                        "   3           1     5           9\n",treeFromPatternString);
    }
}