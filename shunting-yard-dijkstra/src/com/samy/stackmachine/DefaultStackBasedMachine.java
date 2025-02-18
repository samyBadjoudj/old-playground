package com.samy.stackmachine;

import com.samy.shuntingyard.Operators;

import java.util.*;

/**
 * User: Samy Badjoudj
 * Date: 15/06/14
 */
public class DefaultStackBasedMachine implements StackBasedMachine {

    @Override
    public Double compute(Queue<String> queue) {
           Stack<Double> stack  = new Stack<Double>();

        while(!queue.isEmpty()){
            if(!Operators.isOperator(queue.element()) ) {
                stack.push(Double.parseDouble(queue.poll()));
            }else{
                stack.push(Operators.get(queue.poll()).operate(stack.pop(), stack.pop()));
            }
        }

        return stack.pop();
    }

    @Override
    public Double compute(String queue) {
        return compute(new LinkedList<String>(Arrays.asList(queue.split(" "))));
    }
}
