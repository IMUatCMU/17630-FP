package rudi.error;

import rudi.support.variable.Variable;

/**
 * Thrown when a variable with the same name are declared twice.
 */
public class DuplicateVariableDeclarationException extends RuntimeException {

    private final Variable variable;

    public DuplicateVariableDeclarationException(Variable variable) {
        super("Variable with name " + variable.getName() + " are declared twice.");
        this.variable = variable;
    }

    public Variable getVariable() {
        return variable;
    }
}
