package rudi.process.control;

import rudi.error.CannotProcessLineException;
import rudi.process.LineProcessor;
import rudi.support.RudiConstant;
import rudi.support.RudiContext;
import rudi.support.RudiStack;
import rudi.support.RudiUtils;

import static rudi.support.RudiConstant.IF;
import static rudi.support.RudiConstant.THEN;

/**
 * Created by davidiamyou on 2016-12-03.
 */
public class IfThenElseLineProcessor implements LineProcessor {

    private static IfThenElseLineProcessor instance;

    private IfThenElseLineProcessor() {
    }

    public static IfThenElseLineProcessor getInstance() {
        if (null == instance)
            instance = new IfThenElseLineProcessor();
        return instance;
    }

    @Override
    public boolean canProcess(String line) {
        String s = RudiUtils.stripComments(line).trim().toLowerCase();
        return s.startsWith(IF + RudiConstant.SPACE);
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        if (!RudiStack.currentContext().isExecutionMode())
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    "Illegal placement for the if statement."
            );

        // Extract and evaluate condition expression
        line = RudiUtils.stripComments(line).trim();
        if (!line.endsWith(THEN)) {
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    "Bad syntax for if statement, forgot 'then'?"
            );
        }

        String expression = line.substring(IF.length() + 1, line.length() - THEN.length()).trim();
        RudiStack.currentContext().setControlExpressionBracketDepth(RudiStack.currentContext().getBracketDepth());
        RudiStack.currentContext().setControlExpressionLineNumber(lineNumber);
        RudiStack.currentContext().setControlExpression(expression);
        RudiStack.currentContext().setControlType(RudiContext.ControlType.IF);
        RudiStack.currentContext().setControlBranch(RudiContext.ControlBranch.TRUE);
        RudiStack.currentContext().setSkipMode(true);
    }
}
