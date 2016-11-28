package rudi.support;

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
}
