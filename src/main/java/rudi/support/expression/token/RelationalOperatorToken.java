package rudi.support.expression.token;

import rudi.support.RudiConstant;

/**
 * A relational operator token in expression
 */
public class RelationalOperatorToken extends Token {

    public RelationalOperatorToken(String faceValue) {
        super(faceValue);
    }

    public boolean isEqual() {
        return RudiConstant.EQ.equals(faceValue.toLowerCase());
    }

    public boolean isNotEqual() {
        return RudiConstant.NE.equals(faceValue.toLowerCase());
    }

    public boolean isGreaterThan() {
        return RudiConstant.GT.equals(faceValue.toLowerCase());
    }

    public boolean isGreaterEqual() {
        return RudiConstant.GE.equals(faceValue.toLowerCase());
    }

    public boolean isLessThan() {
        return RudiConstant.LT.equals(faceValue.toLowerCase());
    }

    public boolean isLessEqual() {
        return RudiConstant.LE.equals(faceValue.toLowerCase());
    }
}
