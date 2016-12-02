package rudi.error;

/**
 * Thrown when expression evaluation encounters error.
 */
public class FailedToEvaluateExpressionException extends RuntimeException {

    public FailedToEvaluateExpressionException(String message) {
        super(message);
    }
}
