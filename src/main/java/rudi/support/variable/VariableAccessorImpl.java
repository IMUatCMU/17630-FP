package rudi.support.variable;

import java.util.Map;

/**
 * Default implementation of {@link VariableAccessor}. It holds
 * the reference of the variable registrar from {@link rudi.support.RudiContext}
 * so we can achieve "pass by reference"
 */
public class VariableAccessorImpl implements VariableAccessor {

    private final String name;
    private final Map<String, Variable> registrar;

    public VariableAccessorImpl(String name, Map<String, Variable> registrar) {
        this.name = name;
        this.registrar = registrar;

        assert this.registrar.containsKey(name);
    }

    @Override
    public Variable access() {
        return this.registrar.get(name);
    }
}
