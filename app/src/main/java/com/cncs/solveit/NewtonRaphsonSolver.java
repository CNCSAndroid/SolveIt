package com.cncs.solveit;


import android.util.Log;

import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


public class NewtonRaphsonSolver {

    private static final String LOG_TAG = NewtonRaphsonSolver.class.getName();

    private static final String MATH_POWER = "Math.pow";

    private static final String RAISE_TO = "^";

    private static final String BRACKET_OPEN = "(";

    private static final String BRACKET_CLOSE = ")";

    private static final String CHARACTER_REGULAR_EXPRESSION = "[a-zA-Z]";

    private static final String NUMBER_REGULAR_EXPRESSION = "[0-9]";

    private static final String MULTIPLY_SIGN = "*";

    static final double EPSILON = 0.001;

    private final double INITIAL_GUESS = 1000;

    private final int INFINITY_ERROR = 144477;

    private final int NON_INFINITY_ERROR = 144478;

    private final int NON_CONVERGENT_ERROR = 144479;

    private final int INPUT_NOT_SUPPORTED = 144480;

    String[] items = new String[]{"sin", "cos", "tan", "cot", "sec", "cosec", "sinh", "cosh", "tanh", "coth", "sech", "cosech", "asinh", "acosh", "atanh", "acoth", "asech", "acosech", "log"};

    public static String getOrgExpression() {
        return orgExpression;
    }

    public static String getOrgDerivative() {
        return orgDerivative;
    }

    /**
     * This variable is used to store original expression value
     */
    private static String orgExpression;

    private ScriptEngineManager manager = new ScriptEngineManager();
    private ScriptEngine engine = manager.getEngineByName("js");

    public static void setOrgExpression(String orgExpression) {
        NewtonRaphsonSolver.orgExpression = orgExpression;
    }

    public static void setOrgDerivative(String orgDerivative) {
        NewtonRaphsonSolver.orgDerivative = orgDerivative;
    }

    /**
     * This variable is used to store original derivative value
     */
    private static String orgDerivative;

    private double getExpressionValue(double x) {
        Object result;
        String evaluate = getOrgExpression();
        evaluate = evaluate.replaceAll("\\s", "");
        int evaluateExpression = indexOf(Pattern.compile("[a-zA-Z]"), evaluate);
        if (evaluate.length() > evaluateExpression + 1 && evaluateExpression != -1) {
            if (evaluateExpression != -1 && Character.isDigit(evaluate.charAt(evaluateExpression + 1))) {
                throw new IllegalArgumentException();
            }
        }
        if (stringMatcher(evaluate, items)) {
            throw new InputMismatchException();
        }
        String replaceString = BRACKET_OPEN + Double.toString(x) + BRACKET_CLOSE;
        evaluate = evaluate.replaceAll(CHARACTER_REGULAR_EXPRESSION, replaceString);
        for (int index = evaluate.indexOf(BRACKET_OPEN); index >= 0; index = evaluate.indexOf(BRACKET_OPEN, index + 1)) {
            if (index != 0) {
                if (evaluate.substring(index - 1, index).matches(NUMBER_REGULAR_EXPRESSION)) {
                    evaluate = evaluate.substring(0, index) + MULTIPLY_SIGN + evaluate.substring(index);
                }
            }

        }

        int length = replaceString.length();
        while (evaluate.contains(RAISE_TO)) {
            String expression1 = evaluate.substring(evaluate.indexOf(RAISE_TO) + 1, evaluate.indexOf(RAISE_TO) + 2);
            evaluate = evaluate.substring(0, (evaluate.indexOf(RAISE_TO) - length)) + MATH_POWER + BRACKET_OPEN + replaceString + "," + expression1 + BRACKET_CLOSE + evaluate.substring(evaluate.indexOf(RAISE_TO) + 2, evaluate.length());
        }

        try {
            result = engine.eval(evaluate);

        } catch (ScriptException e) {
            Log.e(LOG_TAG, "New script evaluation has occurred", e);
            return 0;
        }
        if (null != result) {
            return (double) result;
        } else {
            return 0;
        }

    }

    private double getDerivativeValue(double x) {
        Object result;
        String evaluate = getOrgDerivative();
        evaluate = evaluate.replaceAll("\\s", "");
        int evaluateExpression = indexOf(Pattern.compile("[a-zA-Z]"), evaluate);
        if (evaluate.length() > evaluateExpression + 1 && evaluateExpression != -1) {
            if (Character.isDigit(evaluate.charAt(evaluateExpression + 1))) {
                throw new IllegalArgumentException();
            }
        }
        if (stringMatcher(evaluate, items)) {
            throw new InputMismatchException();
        }

        String replaceString = BRACKET_OPEN + Double.toString(x) + BRACKET_CLOSE;
        evaluate = evaluate.replaceAll(CHARACTER_REGULAR_EXPRESSION, replaceString);
        for (int index = evaluate.indexOf(BRACKET_OPEN); index >= 0; index = evaluate.indexOf(BRACKET_OPEN, index + 1)) {
            if (index != 0) {
                if (evaluate.substring(index - 1, index).matches(NUMBER_REGULAR_EXPRESSION)) {
                    evaluate = evaluate.substring(0, index) + MULTIPLY_SIGN + evaluate.substring(index);
                }
            }

        }
        int length = replaceString.length();
        while (evaluate.contains(RAISE_TO)) {
            String expression1 = evaluate.substring(evaluate.indexOf(RAISE_TO) + 1, evaluate.indexOf(RAISE_TO) + 2);
            evaluate = evaluate.substring(0, (evaluate.indexOf(RAISE_TO) - length)) + MATH_POWER + BRACKET_OPEN + replaceString + "," + expression1 + BRACKET_CLOSE + evaluate.substring(evaluate.indexOf(RAISE_TO) + 2, evaluate.length());
        }

        try {
            result = engine.eval(evaluate);

        } catch (ScriptException e) {
            Log.e(LOG_TAG, "New script evaluation has occured", e);
            return 0;
        }
        if (null != result) {
            return (double) result;
        } else {
            return 0;
        }
    }


    private LinkedHashMap<Integer, Double> newtonRaphson(int iterations, double initialGuess) {
        LinkedHashMap<Integer, Double> mapGraph = new LinkedHashMap<Integer, Double>();
        double x = initialGuess;
        try {
            double f_value = getExpressionValue(x);
            int iteration_counter = 0;
            mapGraph.put(iteration_counter, initialGuess);
            while (Math.abs(f_value) >= EPSILON && iteration_counter < iterations) {
                try {
                    Log.i(LOG_TAG, "Value with iteration number: " + iteration_counter + "is: " + x);

                    x -= ((f_value) / getDerivativeValue(x));
                    if (x == Double.POSITIVE_INFINITY || x == Double.NEGATIVE_INFINITY) {
                        mapGraph.put(INFINITY_ERROR, (double) 0);
                        return mapGraph;
                    }
                    iteration_counter = iteration_counter + 1;
                    mapGraph.put(iteration_counter, x);

                } catch (IllegalArgumentException ex) {
                    mapGraph.put(NON_CONVERGENT_ERROR, (double) 0);
                    return mapGraph;

                } catch (InputMismatchException ex) {
                    mapGraph.put(INPUT_NOT_SUPPORTED, (double) 0);
                    return mapGraph;
                } catch (Exception e) {
                    mapGraph.put(NON_INFINITY_ERROR, (double) 0);
                    Log.e(LOG_TAG, "Error occurred in newton Raphson");
                    return mapGraph;
                }

                f_value = getExpressionValue(x);

            }
        } catch (IllegalArgumentException e) {
            mapGraph.put(NON_CONVERGENT_ERROR, (double) 0);
            return mapGraph;
        } catch (InputMismatchException ex) {
            mapGraph.put(INPUT_NOT_SUPPORTED, (double) 0);
            return mapGraph;
        }
        //NON_CONVERGENT_ERROR
        double ans = getExpressionValue(mapGraph.get(mapGraph.size() - 1));
        if (ans > EPSILON) {
            mapGraph.put(NON_CONVERGENT_ERROR, mapGraph.get(mapGraph.size() - 1));
        }
        return mapGraph;
    }

    public LinkedHashMap<Integer, Double> solveNewtonRaphson(String expression, String derivative, int iterations, double initialGuess) {
        try {
            if (iterations == 0) {
                iterations = 100;
            }

            if (initialGuess == 0) {
                initialGuess = INITIAL_GUESS;
            }
            this.setOrgExpression(expression);
            this.setOrgDerivative(derivative);
            LinkedHashMap<Integer, Double> solution = this.newtonRaphson(iterations, initialGuess);
            return solution;
        } finally {
            this.setOrgExpression(null);
            this.setOrgDerivative(null);
        }
    }

    public static int indexOf(Pattern pattern, String s) {
        Matcher matcher = pattern.matcher(s);
        return matcher.find() ? matcher.start() : -1;
    }

    private boolean stringMatcher(String expression, String[] matcherArray) {
        for (int i = 0; i <= matcherArray.length - 1; i++) {
            if (expression.contains(matcherArray[i])) {
                return true;
            }
        }
        return false;
    }
}
