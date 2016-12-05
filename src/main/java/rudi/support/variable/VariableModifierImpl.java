package rudi.support.variable;

import rudi.error.TypeMismatchException;
import rudi.support.RudiUtils;

import java.util.Map;

/**
 * A default implementation of {@link VariableModifier}. It holds
 * the reference of the variable registrar from {@link rudi.support.RudiContext}
 * so we can achieve "pass by reference"
 */
public class VariableModifierImpl implements VariableModifier {

    private final String name;
    private final Map<String, Variable> registrar;

    public VariableModifierImpl(String name, Map<String, Variable> registrar) {
        this.name = name;
        this.registrar = registrar;

        assert this.registrar.containsKey(this.name);
    }

    @Override
    public void modify(Object newValue) {
        Variable var = registrar.get(name);
        // check if it's a match type
        if (!RudiUtils.typeMatches(var, newValue))
            throw new TypeMismatchException(var, newValue);

        var.setValue(newValue);
    }
}
