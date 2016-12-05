package rudi.process.program;

import rudi.error.CannotProcessLineException;
import rudi.process.LineProcessor;
import rudi.support.RudiConstant;
import rudi.support.RudiStack;
import rudi.support.RudiUtils;

/**
 * An implementation of {@link LineProcessor} that deals with ']'
 */
public class EndingBracketLineProcessor implements LineProcessor {

    private static EndingBracketLineProcessor instance;

    private EndingBracketLineProcessor() {
    }

    public static EndingBracketLineProcessor getInstance() {
        if (null == instance)
            instance = new EndingBracketLineProcessor();
        return instance;
    }

    @Override
    public boolean canProcess(String line) {
        return line.trim().startsWith(RudiConstant.END_BRAC);
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        line = RudiUtils.stripComments(line).trim();
        // bracket should be its own line
        if (!RudiConstant.END_BRAC.equals(line))
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    "End bracket should be its own line."
            );

        // disallow placement outside decs block and program body
        if (!RudiStack.currentContext().isDeclarationMode() && !RudiStack.currentContext().isExecutionMode()) {
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    "Illegal placement of end bracket."
            );
        }

        // decrement bracket level
        RudiStack.currentContext().decreaseBracketDepth();

        // if we are in decs block, seeing a ] means declaration ended
        // one context can only enter decs block once
        if (RudiStack.currentContext().isDeclarationMode()) {
            RudiStack.currentContext().setDeclarationMode(false);
            RudiStack.currentContext().setDeclarationConcluded(true);
        }

        // set the ending line number for the branch source if we see the bracket level
        // has decreased to the level of the control expression
        if (RudiStack.currentContext().getBracketDepth() == RudiStack.currentContext().getControlExpressionBracketDepth() &&
                RudiStack.currentContext().getBranchStartLineNumber() != 0) {
            RudiStack.currentContext().setBranchEndLineNumber(lineNumber - 1);
        }
    }
}
