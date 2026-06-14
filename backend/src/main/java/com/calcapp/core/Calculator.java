package com.calcapp.core;

import com.calcapp.core.model.CalculationResult;

public class Calculator {

    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();
    private final ScientificFunctions scientificFunctions = new ScientificFunctions();
    private boolean degrees = true;

    public CalculationResult evaluate(String expression) {
        double result = evaluator.evaluate(expression);
        return new CalculationResult(result, expression);
    }

    public CalculationResult applyFunction(String fn, double value) {
        double result = scientificFunctions.apply(fn, value, degrees);
        return new CalculationResult(result, fn + "(" + value + ")");
    }

    public void setDegrees(boolean degrees) {
        this.degrees = degrees;
    }

    public boolean isDegrees() {
        return degrees;
    }
}