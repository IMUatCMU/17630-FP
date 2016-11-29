package rudi.support.variable;

import rudi.error.TypeMismatchException;

/**
 * A variable modifier provides the ability to modify the value of
 * a variable from the underlying variable registrar. It should also
 * be responsible of performing a type check.
 */
public interface VariableModifier {

    /**
     * Modify value
     *
     * @throws TypeMismatchException if the type of the new value disagrees
     * with the type of the underlying value this modifier is responsible for.
     * @param newValue
     */
    void modify(Object newValue);
}
