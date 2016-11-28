package rudi.support;

/**
 * A line of source
 */
public class RudiLine {

    private final int lineNumber;
    private final String source;

    public RudiLine(int lineNumber, String source) {
        this.lineNumber = lineNumber;
        this.source = source;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getSource() {
        return source;
    }
}
