package com.samy.symbolicexecution;

import com.sun.tools.javac.util.Pair;

import java.util.HashMap;

/**
 * User: Samy Badjoudj
 * Date: 19/10/2014
 */
public interface AssignementFinder {
    public Pair<String, String> findVar(String statements);
}
