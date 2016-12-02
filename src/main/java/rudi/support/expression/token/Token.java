package rudi.support.expression.token;

/**
 * A token in expression
 */
abstract public class Token {

    protected final String faceValue;

    public Token(String faceValue) {
        this.faceValue = faceValue;
    }

    public String getFaceValue() {
        return faceValue;
    }
}
