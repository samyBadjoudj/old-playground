package com.samy.maths.evaluator;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * User: Samy Badjoudj
 * Date: 21/06/14
 */
public class FunctionEvaluatorEngine {


    private static final ScriptEngineManager SCRIPT_ENGINE_MANAGER = new ScriptEngineManager();
    private static final ScriptEngine FUNCTION_EVALUATOR = SCRIPT_ENGINE_MANAGER.getEngineByName("js");

    public static ScriptEngine get(){
        return FUNCTION_EVALUATOR;
    }
}
