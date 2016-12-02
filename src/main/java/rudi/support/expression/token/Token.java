package rudi.support.expression.token;

import rudi.support.expression.eval.Evaluator;

/**
 * A token in expression
 */
abstract public class Token {

    protected final String faceValue;

    public Token(String faceValue) {
        this.faceValue = faceValue;
    }

    public String getFaceValue() {
        return faceValue;
    }

    public abstract Evaluator evaluator();

    public abstract boolean isOperand();

    public abstract int priority();
}
