package com.samy.symbolicexecution.reporters;

import com.sun.tools.javac.tree.JCTree;

import java.util.List;
import java.util.Map;

/**
 * User: Samy Badjoudj
 */
public interface SymbolicExecutionReporter {
    public void translate(JCTree.JCIf jcIf,Map<String, List<String>> vars);
}
