package rudi.support.expression.token;

import rudi.support.RudiConstant;

/**
 * An arithmetic operation token in expression
 */
public class ArithmeticOperatorToken extends Token {

    public ArithmeticOperatorToken(String faceValue) {
        super(faceValue);
    }

    public boolean isAddition() {
        return RudiConstant.ADD.equals(faceValue.toLowerCase());
    }

    public boolean isSubtraction() {
        return RudiConstant.MINUS.equals(faceValue.toLowerCase());
    }

    public boolean isMultiplication() {
        return RudiConstant.MULTIPLY.equals(faceValue.toLowerCase());
    }

    public boolean isDivision() {
        return RudiConstant.DIVIDE.equals(faceValue.toLowerCase());
    }
}
