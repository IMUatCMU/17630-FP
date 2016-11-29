package rudi.support.variable;

import rudi.error.TypeMismatchException;
import rudi.support.RudiUtils;

import java.util.Map;

/**
 * A default implementation of {@link VariableModifier}
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
        if (!RudiUtils.typeMatches(var, newValue))
            throw new TypeMismatchException(var, newValue);

        var.setValue(newValue);
    }
}
