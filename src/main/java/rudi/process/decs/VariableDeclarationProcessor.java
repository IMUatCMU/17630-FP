package rudi.process.decs;

import rudi.error.CannotProcessLineException;
import rudi.error.DuplicateVariableDeclarationException;
import rudi.process.LineProcessor;
import rudi.support.RudiConstant;
import rudi.support.RudiStack;
import rudi.support.RudiUtils;
import rudi.support.variable.VarType;
import rudi.support.variable.Variable;

import static rudi.support.RudiConstant.*;

/**
 * An implementation of {@link LineProcessor} that handles variable declaration.
 */
public class VariableDeclarationProcessor implements LineProcessor {

    private static VariableDeclarationProcessor instance;

    private VariableDeclarationProcessor() {
    }

    public static VariableDeclarationProcessor getInstance() {
        if (null == instance)
            instance = new VariableDeclarationProcessor();
        return instance;
    }

    @Override
    public boolean canProcess(String line) {
        return (line.trim().toLowerCase().startsWith(TYPE_INTEGER + SPACE)) ||
                (line.trim().toLowerCase().startsWith(TYPE_FLOAT + SPACE)) ||
                (line.trim().toLowerCase().startsWith(TYPE_STRING + SPACE));
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        // disallow variable declaration outside decs block
        if (!RudiStack.getInstance().peek().isDeclarationMode()) {
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    "Misplaced variable declaration: " + line
            );
        }

        line = RudiUtils.stripComments(line).trim();

        // resolve variable type
        VarType type = null;
        if (line.toLowerCase().startsWith(TYPE_INTEGER + SPACE)) {
            type = VarType.INTEGER;
        } else if (line.toLowerCase().startsWith(TYPE_FLOAT + SPACE)) {
            type = VarType.FLOAT;
        } else if (line.toLowerCase().startsWith(TYPE_STRING + SPACE)) {
            type = VarType.STRING;
        } else {
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    "Unknown data type: " + line
            );
        }

        // resolve variable name
        String variableName = null;
        if (line.toLowerCase().startsWith(TYPE_INTEGER + SPACE)) {
            variableName = line.substring((TYPE_INTEGER + SPACE).length()).trim();
        } else if (line.toLowerCase().startsWith(TYPE_FLOAT + SPACE)) {
            variableName = line.substring((TYPE_FLOAT + SPACE).length()).trim();
        } else if (line.toLowerCase().startsWith(TYPE_STRING + SPACE)) {
            variableName = line.substring((TYPE_STRING + SPACE).length()).trim();
        }

        // check if the name violates naming rules (identical to a reserved word)
        if (RudiConstant.RESERVED_WORDS.contains(variableName)) {
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    "<" + variableName + "> is reserved."
            );
        }

        // <decided not to do>
        // maybe do some additional checks for variable name formats
        // i.e. a-zA-Z0-9 and do not start with number

        // declare a variable, handle error when variable with same name already exists
        try {
            RudiStack.getInstance().peek().declare(new Variable(type, variableName));
        } catch (DuplicateVariableDeclarationException ex) {
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    ex.getMessage()
            );
        }
    }
}
