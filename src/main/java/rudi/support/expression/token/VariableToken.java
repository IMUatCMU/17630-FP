package rudi.support.expression.token;

import rudi.support.variable.VariableAccessor;

/**
 * A variable token in expression
 */
public class VariableToken extends Token {

    private final VariableAccessor accessor;

    public VariableToken(String faceValue, VariableAccessor accessor) {
        super(faceValue);
        this.accessor = accessor;
    }

    public VariableAccessor getAccessor() {
        return accessor;
    }
}
