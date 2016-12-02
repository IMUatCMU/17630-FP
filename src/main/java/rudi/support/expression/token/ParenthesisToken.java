package rudi.support.expression.token;

import rudi.support.expression.eval.Evaluator;

/**
 * A parenthesis token
 */
public class ParenthesisToken extends Token {

    public ParenthesisToken(String faceValue) {
        super(faceValue);
    }

    @Override
    public Evaluator evaluator() {
        return null;
    }

    @Override
    public boolean isOperand() {
        return false;
    }

    @Override
    public boolean isOperator() {
        return false;
    }

    @Override
    public boolean isParenthesis() {
        return true;
    }

    @Override
    public int priority() {
        return 0;
    }
}
