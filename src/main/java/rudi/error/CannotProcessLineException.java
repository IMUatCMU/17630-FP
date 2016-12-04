package rudi.error;

/**
 * Thrown when processor does not understand how to deal with a certain line.
 * Usually indicates program execution fails.
 */
public class CannotProcessLineException extends RuntimeException {

    private final int lineNumber;

    public CannotProcessLineException(int lineNumber) {
        super("Processing failed");
        this.lineNumber = lineNumber;
    }

    public CannotProcessLineException(int lineNumber, String message) {
        super(message);
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
