package rudi.support.variable;

/**
 * A structure encapsulating all the information about a variable.
 * This is to be frequently used in a variable registrar.
 */
public class Variable {

    private final VarType type;
    private final String name;
    private Object value;

    public Variable(VarType type, String name) {
        this.type = type;
        this.name = name;
    }

    public VarType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }
}
