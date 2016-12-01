package rudi.error;

/**
 * Thrown when the literal constant cannot be resolved as constant
 */
public class NotAConstantException extends RuntimeException {

    private final String literal;

    public NotAConstantException(String literal) {
        super("<" + literal + "> is not a constant.");
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }
}
