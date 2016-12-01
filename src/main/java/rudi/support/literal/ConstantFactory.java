package rudi.support.literal;

import rudi.error.NotAConstantException;
import rudi.support.RudiConstant;
import rudi.support.variable.VarType;

/**
 * A factory that returns a Constant.
 * The order of resolution is:
 * - test if is an integer
 * - test if is a float
 * - test if is a string (wrapped with double quotes)
 * - throw exception to indicate cannot resolve as exception
 */
public class ConstantFactory {

    /**
     * Resolve the string based literal constant as a constant.
     * Throws {@link rudi.error.NotAConstantException} when cannot resolve
     *
     * @param literal
     * @return
     */
    public static Constant create(String literal) {
        if (isInteger(literal)) {
            return new Constant(VarType.INTEGER, Integer.parseInt(literal.trim()));
        } else if (isFloat(literal)) {
            return new Constant(VarType.FLOAT, Float.parseFloat(literal.trim()));
        } else if (isString(literal)) {
            return new Constant(VarType.STRING,
                    literal.trim().substring(1, literal.trim().length() - 1));
        } else {
            throw new NotAConstantException(literal);
        }
    }

    private static boolean isInteger(String literal) {
        try {
            Integer.parseInt(literal.trim());
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private static boolean isFloat(String literal) {
        try {
            Float.parseFloat(literal.trim());
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private static boolean isString(String literal) {
        return literal.trim().startsWith(RudiConstant.DOUBLE_QUOTE) &&
                literal.trim().endsWith(RudiConstant.DOUBLE_QUOTE);
    }
}
