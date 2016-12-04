package rudi.process.var;

import rudi.error.CannotProcessLineException;
import rudi.error.VariableNotInRegistrarException;
import rudi.process.LineProcessor;
import rudi.support.RudiConstant;
import rudi.support.RudiStack;
import rudi.support.RudiUtils;
import rudi.support.expression.eval.ExpressionResolver;
import rudi.support.expression.token.Tokenizer;
import rudi.support.literal.Constant;
import rudi.support.variable.VariableModifier;

import java.util.Arrays;

/**
 * An implementation of {@link LineProcessor} that deals with variable value assignment.
 */
public class VariableAssignmentProcessor implements LineProcessor {

    private static VariableAssignmentProcessor instance;

    private VariableAssignmentProcessor() {
    }

    public static VariableAssignmentProcessor getInstance() {
        if (null == instance)
            instance = new VariableAssignmentProcessor();
        return instance;
    }

    @Override
    public boolean canProcess(String line) {
        String[] split = line.split(RudiConstant.EQUAL_SIGN);
        if (split.length >= 2) {
            try {
                RudiStack.currentContext().accessor(split[0].trim());
                return true;
            } catch (VariableNotInRegistrarException ex) {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        if (!RudiStack.currentContext().isExecutionMode()) {
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    "Misplaced value assignment: " + line
            );
        }

        line = RudiUtils.stripComments(line).trim();

        String variableName = line.substring(0, line.indexOf(RudiConstant.EQUAL_SIGN)).trim();
        String expression = line.substring(line.indexOf(RudiConstant.EQUAL_SIGN) + 1).trim();

        try {
            VariableModifier modifier = RudiStack.currentContext().modifier(variableName);
            //Constant newValue = ExpressionResolver.resolve(Arrays.asList(expression.split(RudiConstant.SPACE)));
            Constant newValue = ExpressionResolver.resolve(new Tokenizer(expression).allTokens());
            modifier.modify(newValue.getValue());
        } catch (Exception ex) {
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    ex.getMessage());
        }
    }
}
