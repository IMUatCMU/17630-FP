package rudi.error;

import rudi.support.variable.Variable;

/**
 * Thrown during value modification if the two types are not equal.
 */
public class TypeMismatchException extends RuntimeException {

    private final Variable variable;
    private final Object newValue;

    public TypeMismatchException(Variable variable, Object newValue) {
        super("Type mismatch. Expected: " + variable.getType().toString() + " Got: " + newValue.getClass().getSimpleName().toUpperCase());
        this.variable = variable;
        this.newValue = newValue;
    }

    public Variable getVariable() {
        return variable;
    }

    public Object getNewValue() {
        return newValue;
    }
}
