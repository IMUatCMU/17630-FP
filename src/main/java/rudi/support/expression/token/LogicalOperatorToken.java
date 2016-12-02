package rudi.support.expression.token;

import rudi.support.RudiConstant;

/**
 * A logical operator in expression
 */
public class LogicalOperatorToken extends Token {

    public LogicalOperatorToken(String faceValue) {
        super(faceValue);
    }

    public boolean isAnd() {
        return RudiConstant.AND.equals(faceValue.toLowerCase());
    }

    public boolean isOr() {
        return RudiConstant.OR.equals(faceValue.toLowerCase());
    }

    public boolean isNot() {
        return RudiConstant.NOT.equals(faceValue.toLowerCase());
    }
}
