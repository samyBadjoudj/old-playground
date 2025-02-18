package com.samy.shuntingyard;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * User: Samy Badjoudj
 * Date: 14/06/14
 */
public class DefaultShuntingYard implements ShuntingYard {

    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";

    public DefaultShuntingYard() {
    }

    @Override
    public String getPostFixNotation(String mathExpression) {

        StringBuilder outputQueue = new StringBuilder();
        Deque<String> operatorStack = new ArrayDeque<String>();


        for (String member : mathExpression.split(" ")) {

            if (Operators.isOperator(member)) {
                processOperator(outputQueue, operatorStack, member);
            } else if (LEFT_PARENTHESIS.equals(member)) {
                operatorStack.push(member);
            } else if (RIGHT_PARENTHESIS.equals(member)) {
                processRightParenthesis(outputQueue, operatorStack);
            } else {
                outputQueue.append(member).append(' ');
            }
        }

        queueAllOperators(outputQueue, operatorStack);

        return outputQueue.toString();

    }

    private void queueAllOperators(StringBuilder outputQueue, Deque<String> operatorStack) {
        while (!operatorStack.isEmpty()) {

            popOperatorAndQueueIt(operatorStack, outputQueue);
        }
    }

    private void processRightParenthesis(StringBuilder outputQueue, Deque<String> operatorStack) {
        while (!LEFT_PARENTHESIS.equals(operatorStack.peek())) {
            popOperatorAndQueueIt(operatorStack, outputQueue);
        }

        operatorStack.pop();
    }

    private void processOperator(StringBuilder outputQueue, Deque<String> operatorStack, String member) {
        while (!operatorStack.isEmpty() && Operators.isRightOperatorPrior(member, operatorStack.peek())) {
            popOperatorAndQueueIt(operatorStack, outputQueue);
        }

        operatorStack.push(member);
    }

    private void popOperatorAndQueueIt(Deque<String> operatorStack, StringBuilder outputQueue) {
        outputQueue.append(operatorStack.pop()).append(' ');
    }



}
