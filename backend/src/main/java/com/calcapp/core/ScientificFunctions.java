package com.calcapp.core;

public class ScientificFunctions {

    public double apply(String fn, double value, boolean degrees) {
        return switch (fn) {
            case "sin"       -> Math.sin(toRad(value, degrees));
            case "cos"       -> Math.cos(toRad(value, degrees));
            case "tan"       -> {
                double rad = toRad(value, degrees);
                if (Math.abs(Math.cos(rad)) < 1e-10)
                    throw new ArithmeticException("tan is undefined at this angle");
                yield Math.tan(rad);
            }
            case "asin"      -> {
                if (value < -1 || value > 1) throw new ArithmeticException("Domain error: asin requires input in [-1, 1]");
                yield fromRad(Math.asin(value), degrees);
            }
            case "acos"      -> {
                if (value < -1 || value > 1) throw new ArithmeticException("Domain error: acos requires input in [-1, 1]");
                yield fromRad(Math.acos(value), degrees);
            }
            case "atan"      -> fromRad(Math.atan(value), degrees);
            case "log"       -> {
                if (value <= 0) throw new ArithmeticException("log requires a positive number");
                yield Math.log10(value);
            }
            case "ln"        -> {
                if (value <= 0) throw new ArithmeticException("ln requires a positive number");
                yield Math.log(value);
            }
            case "square"    -> value * value;
            case "cube"      -> value * value * value;
            case "sqrt"      -> {
                if (value < 0) throw new ArithmeticException("Cannot take sqrt of a negative number");
                yield Math.sqrt(value);
            }
            case "cbrt"      -> Math.cbrt(value);
            case "factorial" -> {
                if (value < 0 || value != Math.floor(value))
                    throw new ArithmeticException("Factorial requires a non-negative integer");
                yield factorial((int) value);
            }
            case "abs"       -> Math.abs(value);
            default          -> throw new IllegalArgumentException("Unknown function: " + fn);
        };
    }

    private double factorial(int n) {
        if (n > 20) throw new ArithmeticException("Input too large for factorial");
        double result = 1;
        for (int i = 2; i <= n; i++) result *= i;
        return result;
    }

    private double toRad(double value, boolean degrees) {
        return degrees ? Math.toRadians(value) : value;
    }

    private double fromRad(double value, boolean degrees) {
        return degrees ? Math.toDegrees(value) : value;
    }
}