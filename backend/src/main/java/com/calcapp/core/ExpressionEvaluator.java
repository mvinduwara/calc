package com.calcapp.core;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class ExpressionEvaluator {

    public double evaluate(String expression) {
        String sanitized = expression
                .replace("×", "*")
                .replace("÷", "/")
                .replace("−", "-")
                .replace("π", String.valueOf(Math.PI))
                .replace("e", String.valueOf(Math.E))
                .trim();

        Expression expr = new ExpressionBuilder(sanitized).build();
        double result = expr.evaluate();

        if (Double.isNaN(result)) throw new ArithmeticException("Result is not a number");
        if (Double.isInfinite(result)) throw new ArithmeticException("Cannot divide by zero");
        return result;
    }
}