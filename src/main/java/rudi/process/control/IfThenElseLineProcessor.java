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
 * An implementation of {@link LineProcessor} that handles the if statement.
 * This processor does not directly handle the evaluation of the condition or
 * execution of the branches. Instead, it parses the condition and cache it
 * on the current context ({@link RudiContext#controlExpression}). Then it
 * puts the program execution into skip mode ({@link RudiContext#skipMode}).
 * Under skip mode, most lines will be picked up by {@link SkipLineProcessor}
 * which is responsible for collecting the source codes for the two branches
 * of if statement and eventually pick one to execute.
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

        // parse expression
        String expression = line.substring(IF.length() + 1, line.length() - THEN.length()).trim();

        // cache bracket level, this will help us determine the last line of a branch
        RudiStack.currentContext().setControlExpressionBracketDepth(RudiStack.currentContext().getBracketDepth());

        // cache expression line number, this will make line reporting more accurate (since we don't immediately execute)
        RudiStack.currentContext().setControlExpressionLineNumber(lineNumber);

        // cache expression
        RudiStack.currentContext().setControlExpression(expression);

        // mark this is an if statement (the other type is while statement)
        RudiStack.currentContext().setControlType(RudiContext.ControlType.IF);

        // mark that we are about to enter the TRUE-branch of the if statement
        RudiStack.currentContext().setControlBranch(RudiContext.ControlBranch.TRUE);

        // start the skip mode, subsequent statements will only be collected but not executed until turned off.
        RudiStack.currentContext().setSkipMode(true);
    }
}
