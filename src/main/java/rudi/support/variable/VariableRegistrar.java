package rudi.support.variable;

import rudi.error.DuplicateVariableDeclarationException;

/**
 * A variable registrar should maintain the state of all variables and
 * expose functions to access and modify its variable.
 */
public interface VariableRegistrar {

    /**
     * Declare a new variable. Should check against
     * this context and the variables borrowed from parent context
     * if this is a duplicate declaration.
     *
     * @throws DuplicateVariableDeclarationException when declaration with same name already exists
     * @param variable
     */
    void declare(Variable variable);

    /**
     * Get a modifier which can be called to modify the value of
     * an existing variable. Should check current context declarations
     * and borrowed variables.
     *
     * @throws rudi.error.VariableNotInRegistrarException when declaration not found
     * @param name
     * @return
     */
    VariableModifier modifier(String name);

    /**
     * Get an accessor which can be called to access the value of an existing variable.
     * Should check against current context and borrowed variables.
     *
     * @throws rudi.error.VariableNotInRegistrarException when declaration not found.
     * @param name
     * @return
     */
    VariableAccessor accessor(String name);
}
