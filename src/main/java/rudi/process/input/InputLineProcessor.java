package rudi.process.input;

import rudi.error.CannotProcessLineException;
import rudi.error.TypeMismatchException;
import rudi.error.VariableNotInRegistrarException;
import rudi.process.LineProcessor;
import rudi.support.RudiConstant;
import rudi.support.RudiStack;
import rudi.support.RudiUtils;
import rudi.support.variable.VariableAccessor;
import rudi.support.variable.VariableModifier;
import rudi.support.variable.VarType;

import java.lang.Math;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * An implementation of {@link rudi.process.LineProcessor} that executes
 * the 'input' command.
 */
public class InputLineProcessor implements LineProcessor {

    private static InputLineProcessor instance;

    private InputLineProcessor() {
    }

    public static InputLineProcessor getInstance() {
        if (null == instance) {
            instance = new InputLineProcessor();
        }
        return instance;
    }

    @Override
    public boolean canProcess(String line) {
        return line.trim().toLowerCase().startsWith(RudiConstant.INPUT_COMMAND + RudiConstant.SPACE);
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        line = RudiUtils.stripComments(line).trim();
        assert line.startsWith(RudiConstant.INPUT_COMMAND);

        // Get the name of the variable to input to
        String varName = line.substring(RudiConstant.INPUT_COMMAND.length()).trim();
        try {
            // Access the variable to find out it's type
            VariableAccessor accessor = RudiStack.currentContext().accessor(varName);

            VariableModifier modifier = RudiStack.currentContext().modifier(varName);
            Scanner scanner = new Scanner(System.in);
            // Depending on the variable type, try to cast to that
            Object value = null;
            switch (accessor.access().getType()) {
                case INTEGER:
                    float floatValue = scanner.nextFloat();
                    value = Math.round(floatValue);
                    break;
                case FLOAT:
                    value = scanner.nextFloat();
                    break;
                case STRING:
                    value = scanner.nextLine();
                    break;
                default:
                    // This shouldn't be possible
                    // throw exception
                    break;
            }

            modifier.modify(value);
        } catch (VariableNotInRegistrarException e) {
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    "use of undeclared identifier '" + varName + "'");
        } catch (TypeMismatchException e) {
            // Should never come here, implementation issue.
        } catch (InputMismatchException e) {
            // Happens if you ask for an int but user puts in string
        }
    }
}
