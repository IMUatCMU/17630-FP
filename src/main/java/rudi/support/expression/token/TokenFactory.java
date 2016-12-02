package rudi.support.expression.token;

import rudi.error.NotAConstantException;
import rudi.error.UnrecognizedTokenException;
import rudi.error.VariableNotInRegistrarException;
import rudi.support.RudiStack;
import rudi.support.literal.Constant;
import rudi.support.literal.ConstantFactory;
import rudi.support.variable.VariableAccessor;
import rudi.support.variable.VariableModifier;

import static rudi.support.RudiConstant.*;

/**
 * A factory that creates {@link Token} out of strings
 */
public class TokenFactory {

    /**
     * Create token from string, throws {@link rudi.error.UnrecognizedTokenException}
     * when token cannot be resolved.
     *
     * @param faceValue
     * @return
     */
    public static Token create(String faceValue) {
        faceValue = faceValue.trim();

        if (isArithmetic(faceValue)) {
            return new ArithmeticOperatorToken(faceValue);
        } else if (isLogical(faceValue)) {
            return new LogicalOperatorToken(faceValue);
        } else if (isRelational(faceValue)) {
            return new RelationalOperatorToken(faceValue);
        }

        try {
            Constant constant = ConstantFactory.create(faceValue);
            if (null != constant)
                return new ConstantToken(faceValue, constant);
        } catch (NotAConstantException e1) {}

        try {
            VariableAccessor getter = RudiStack.currentContext().accessor(faceValue);
            if (null != getter)
                return new VariableToken(faceValue, getter);
        } catch (VariableNotInRegistrarException e2) {}

        throw new UnrecognizedTokenException(faceValue);
    }

    private static boolean isArithmetic(String faceValue) {
        return ADD.equals(faceValue.toLowerCase()) ||
                MINUS.equals(faceValue.toLowerCase()) ||
                MULTIPLY.equals(faceValue.toLowerCase()) ||
                DIVIDE.equals(faceValue.toLowerCase());
    }

    private static boolean isLogical(String faceValue) {
        return AND.equals(faceValue.toLowerCase()) ||
                OR.equals(faceValue.toLowerCase()) ||
                NOT.equals(faceValue.toLowerCase());
    }

    private static boolean isRelational(String faceValue) {
        return EQ.equals(faceValue.toLowerCase()) ||
                NE.equals(faceValue.toLowerCase()) ||
                GT.equals(faceValue.toLowerCase()) ||
                GE.equals(faceValue.toLowerCase()) ||
                LT.equals(faceValue.toLowerCase()) ||
                LE.equals(faceValue.toLowerCase());
    }
}
