package rudi.process.control;

import rudi.error.CannotProcessLineException;
import rudi.process.LineProcessor;
import rudi.support.RudiConstant;
import rudi.support.RudiContext;
import rudi.support.RudiStack;
import rudi.support.RudiUtils;

/**
 * An implementation of {@link LineProcessor} to deal with 'while' command
 */
public class WhileLineProcessor implements LineProcessor {

    private static WhileLineProcessor instance;

    private WhileLineProcessor() {
    }

    public static WhileLineProcessor getInstance() {
        if (null == instance)
            instance = new WhileLineProcessor();
        return instance;
    }

    @Override
    public boolean canProcess(String line) {
        return RudiUtils.stripComments(line).trim().toLowerCase().startsWith(RudiConstant.WHILE + RudiConstant.SPACE);
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        if (!RudiStack.currentContext().isExecutionMode())
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    "Illegal placement for the while statement."
            );

        // Extract condition expression
        line = RudiUtils.stripComments(line).trim();
        String expression = line.substring(RudiConstant.WHILE.length() + 1).trim();
        RudiStack.currentContext().setControlExpressionLineNumber(lineNumber);
        RudiStack.currentContext().setControlExpression(expression);
        RudiStack.currentContext().setControlType(RudiContext.ControlType.WHILE);
        RudiStack.currentContext().setControlBranch(RudiContext.ControlBranch.TRUE);
        RudiStack.currentContext().setSkipMode(true);
    }
}
