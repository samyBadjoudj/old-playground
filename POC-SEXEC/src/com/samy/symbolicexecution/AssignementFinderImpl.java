package com.samy.symbolicexecution;

import com.sun.tools.javac.util.Pair;

/**
 * User: Samy Badjoudj
 */
public class AssignementFinderImpl implements AssignementFinder {

    @Override
    public Pair<String, String> findVar(String statements) {
        Pair<String, String> pair = new Pair<String, String>("","");
        String[] assingement = statements.replaceAll(" ","").split("=");
        if (assingement.length == 2) {
            pair = new Pair<String, String>(assingement[0], assingement[1]);
        }
        return pair;
    }
}
