package rudi.error;

/**
 * Thrown when variable declaration not found.
 */
public class VariableNotInRegistrarException extends RuntimeException {

    private final String name;

    public VariableNotInRegistrarException(String name) {
        super("Variable with name " + name + " is not declared.");
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
