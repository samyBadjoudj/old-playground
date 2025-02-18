package com.samy.symbolicexecution.reporters;

import com.sun.tools.javac.tree.JCTree;

import java.util.List;
import java.util.Map;

/**
 * User: Samy Badjoudj
 */
public class SymbolicExecutionReporterImpl implements SymbolicExecutionReporter {


    private static final String EQUALS = "==";
    private static final String GET = ">=";
    private static final String LET = "<=";
    private static final String GT = ">";
    private static final String LT = "<";
    private static final String DIFF = "!=";

    @Override
    public void translate(JCTree.JCIf jcIf, Map<String, List<String>> vars) {
        String ifWithoutBrackets = jcIf.getCondition().toString().replaceAll("[() ]", "");
        String[] varsCompared = new String[]{};
        if (ifWithoutBrackets.contains(EQUALS)) {
            varsCompared = ifWithoutBrackets.split(EQUALS);
        } else if (ifWithoutBrackets.contains(GET)) {
            varsCompared = ifWithoutBrackets.split(GET);
        } else if (ifWithoutBrackets.contains(LET)) {
            varsCompared = ifWithoutBrackets.split(LET);
        } else if (ifWithoutBrackets.contains(GT)) {
            varsCompared = ifWithoutBrackets.split(GT);
        } else if (ifWithoutBrackets.contains(LT)) {
            varsCompared = ifWithoutBrackets.split(LT);
        } else if (ifWithoutBrackets.contains(DIFF)) {
            varsCompared = ifWithoutBrackets.split(DIFF);
        }


        printReport(jcIf, vars, ifWithoutBrackets, varsCompared);

    }

    private void printReport(JCTree.JCIf jcIf, Map<String, List<String>> vars, String ifWithoutBrackets, String[] varsCompared) {
        System.out.println("--------------------------------------");
        System.out.println("   ");
        printDependency(vars.get(varsCompared[0]), varsCompared[0]);
        System.out.println("   ");
        printDependency(vars.get(varsCompared[1]), varsCompared[1]);
        System.out.println("");
        System.out.println("To execute the then part : " + ifWithoutBrackets);
        System.out.println("   ");
        System.out.println(jcIf.getThenStatement().toString());
        if (jcIf.getElseStatement() != null) {
            System.out.println("   ");
            System.out.println("else part" );
            System.out.println("   ");
            System.out.println(jcIf.getElseStatement().toString());
        } else {
            System.out.println(" no else part  ");

        }
        System.out.println("   ");
        System.out.println("--------------------------------------");
    }

    private void printDependency(List<String> assignements, String var) {
        System.out.println("Var [" + var + "] depends on : ");
        if (assignements != null) {
            for (String assignement : assignements) {
                System.out.println(var + " <= " + assignement);
            }
        }
    }
}
