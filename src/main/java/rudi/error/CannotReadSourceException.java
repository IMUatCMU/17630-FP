package rudi.error;

/**
 * Thrown when failure to read source file
 */
public class CannotReadSourceException extends RuntimeException {

    private final String sourceOrigin;

    public CannotReadSourceException(String sourceOrigin) {
        super("Failed to read source from " + sourceOrigin);
        this.sourceOrigin = sourceOrigin;
    }

    public CannotReadSourceException(String sourceOrigin, String message) {
        super("Failed to read source from " + sourceOrigin + " because: " + message);
        this.sourceOrigin = sourceOrigin;
    }

    public String getSourceOrigin() {
        return sourceOrigin;
    }
}
