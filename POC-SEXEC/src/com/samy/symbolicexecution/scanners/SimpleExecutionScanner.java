package com.samy.symbolicexecution.scanners;

import com.samy.symbolicexecution.AssignementFinder;
import com.samy.symbolicexecution.AssignementFinderImpl;
import com.samy.symbolicexecution.reporters.SymbolicExecutionReporter;
import com.samy.symbolicexecution.reporters.SymbolicExecutionReporterImpl;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Samy Badjoudj
 */
public class SimpleExecutionScanner extends TreeScanner<Void, Void> {

    private static final AssignementFinder ASSIGNEMENT_FINDER = new AssignementFinderImpl();
    private static final SymbolicExecutionReporter IF_CONDITION_TRANSLATOR = new SymbolicExecutionReporterImpl();
    @Override
    public Void visitMethod(MethodTree arg0, Void arg1) {

        printMethodInformations(arg0);
        Map<String, List<String>> allSymbolicStatements = new HashMap<String, List<String>>();
        final List<? extends StatementTree> statements = arg0.getBody().getStatements();
        for (StatementTree statementTree : statements) {
            if (statementTree instanceof JCTree.JCIf) {
                IF_CONDITION_TRANSLATOR.translate((JCTree.JCIf) statementTree, allSymbolicStatements);
            } else {
                final Pair<String, String> var = ASSIGNEMENT_FINDER.findVar(statementTree.toString());
                if (allSymbolicStatements.containsKey(var.fst)){
                    allSymbolicStatements.get(var.fst).add(var.snd);
                }else {
                    final ArrayList<String> value = new ArrayList<String>();
                    value.add(var.snd);
                    allSymbolicStatements.put(var.fst, value);
                }
            }
        }

        return super.visitMethod(arg0, arg1);
    }

    private void printMethodInformations(MethodTree arg0) {
        System.out.println("METHOD : " + arg0.getName() + " ");
        System.out.println("");
        final List<? extends VariableTree> parameters = arg0.getParameters();
        for(VariableTree variableTree : parameters){
            System.out.println(variableTree.getName() + " " + variableTree.getType());

        }
    }


}
