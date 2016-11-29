package rudi.support;

import rudi.support.variable.VarType;
import rudi.support.variable.Variable;

/**
 * Utility functions
 */
public class RudiUtils {

    /**
     * Strip the ending comment in a line.
     * @param line original source
     * @return the line stripped with the ending comment
     */
    public static String stripComments(String line) {
        if (line.contains(RudiConstant.START_COMMENT) &&
                line.trim().endsWith(RudiConstant.END_COMMENT)) {
            return line.substring(0, line.indexOf(RudiConstant.START_COMMENT));
        } else {
            return line;
        }
    }

    /**
     * Get the global line number. The caller may when in a local context, which
     * reports line numbers relative to a chunk of source code only. To report
     * accurate line number back to user, we need to add whatever the offset to it.
     *
     * @param lineNumber
     * @return
     */
    public static int resolveGlobalLineNumber(int lineNumber) {
        return RudiStack
                .getInstance()
                .peek()
                .getSourceCode()
                .getGlobalLineOffset() + lineNumber;
    }

    /**
     * Test if the given value matches the type of the variable.
     * If the variable has value already, then it's a class comparison.
     * Otherwise, we compare variable type against designated classes.
     * If new value is null, it's always a pass.
     *
     * @param var
     * @param value
     * @return
     */
    public static boolean typeMatches(Variable var, Object value) {
        if (null == value)
            return true;

        if (null != var.getValue()) {
            return var.getValue().getClass().equals(value.getClass());
        } else {
            switch (var.getType()) {
                case INTEGER:
                    return value.getClass().equals(Integer.class);

                case FLOAT:
                    return value.getClass().equals(Float.class);

                case STRING:
                    return value.getClass().equals(String.class);

                default:
                    return false;
            }
        }
    }
}
