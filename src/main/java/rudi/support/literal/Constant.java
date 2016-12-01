package rudi.support.literal;

import rudi.support.variable.VarType;

/**
 * An object to represent a literal constant
 */
public class Constant {

    private final VarType type;
    private final Object value;

    public Constant(VarType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public VarType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
