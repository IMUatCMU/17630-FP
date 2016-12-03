package rudi.process.program;

import rudi.error.CannotProcessLineException;
import rudi.process.LineProcessor;
import rudi.support.RudiConstant;
import rudi.support.RudiStack;
import rudi.support.RudiUtils;

/**
 * An implementation of {@link LineProcessor} to deal with '['
 */
public class StartingBracketLineProcessor implements LineProcessor {

    private static StartingBracketLineProcessor instance;

    private StartingBracketLineProcessor() {
    }

    public static StartingBracketLineProcessor getInstance() {
        if (null == instance)
            instance = new StartingBracketLineProcessor();
        return instance;
    }

    @Override
    public boolean canProcess(String line) {
        return line.trim().startsWith(RudiConstant.START_BRAC);
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        line = RudiUtils.stripComments(line).trim();
        if (!RudiConstant.START_BRAC.equals(line))
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    "Start bracket should be its own line."
            );

        // increase count
        RudiStack.currentContext().increaseBracketDepth();

        // declaration mode
        if (RudiStack.currentContext().isDeclarationMode() && RudiStack.currentContext().getBracketDepth() > 1) {
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    "Illegal placement of start bracket in declaration block."
            );
        }
    }
}
