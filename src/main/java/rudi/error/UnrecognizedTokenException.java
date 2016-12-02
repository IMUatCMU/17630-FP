package rudi.error;

/**
 * Thrown when the specified token is not valid.
 */
public class UnrecognizedTokenException extends RuntimeException {

    private final String faceValue;

    public UnrecognizedTokenException(String faceValue) {
        super("<" + faceValue + "> is not a valid expression token.");
        this.faceValue = faceValue;
    }

    public String getFaceValue() {
        return faceValue;
    }
}
