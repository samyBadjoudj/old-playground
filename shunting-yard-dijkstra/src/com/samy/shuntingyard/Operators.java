package com.samy.shuntingyard;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Samy Badjoudj
 * Date: 14/06/14
 */
public class Operators
{
    public enum Operator{

        ADDITION(1), SUBTRACTION(1), MULTIPLICATION(5), DIVISION(5);
        final int priority;
        Operator(int p) { priority = p; }

        public Double operate(Double number2, Double number){

            switch (this){
                case ADDITION:
                    return  number + number2;
                case SUBTRACTION:
                    return  number - number2;
                case MULTIPLICATION:
                    return  number * number2;
                case DIVISION:
                    return number / number2;
                default: return 0.0;

            }
        }

    }

    private static Map<String, Operator> allOperators = new HashMap<String, Operator>() {{
        put("*", Operator.MULTIPLICATION);
        put("/", Operator.DIVISION);
        put("-", Operator.SUBTRACTION);
        put("+", Operator.ADDITION);
    }};


    public static boolean isOperator(String operator){
        return allOperators.containsKey(operator);
    }

    public static Operator get(String operator){
        return allOperators.get(operator);
    }

    public static boolean isRightOperatorPrior(String operator, String otherOperator){
        return  isOperator(otherOperator) && allOperators.get(operator).priority <= allOperators.get(otherOperator).priority;
    }


}




