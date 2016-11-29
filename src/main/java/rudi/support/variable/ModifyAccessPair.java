package rudi.support.variable;

/**
 * DTO for both {@link VariableModifier} and {@link VariableAccessor}
 */
public class ModifyAccessPair {

    private final VariableModifier modifier;
    private final VariableAccessor accessor;

    public ModifyAccessPair(VariableModifier modifier, VariableAccessor accessor) {
        this.modifier = modifier;
        this.accessor = accessor;
    }

    public VariableModifier getModifier() {
        return modifier;
    }

    public VariableAccessor getAccessor() {
        return accessor;
    }
}
