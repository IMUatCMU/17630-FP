package rudi.process.program;

import rudi.error.CannotProcessLineException;
import rudi.process.LineProcessor;
import rudi.support.RudiConstant;
import rudi.support.RudiStack;
import rudi.support.RudiUtils;

/**
 * An implementation of {@link LineProcessor} that handles 'begin' command.
 */
public class BeginLineProcessor implements LineProcessor {

    private static BeginLineProcessor instance;

    private BeginLineProcessor() {
    }

    public static BeginLineProcessor getInstance() {
        if (null == instance)
            instance = new BeginLineProcessor();
        return instance;
    }

    @Override
    public boolean canProcess(String line) {
        return line.trim().toLowerCase().startsWith(RudiConstant.BEGIN_COMMAND);
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        if (RudiStack.currentContext().isDeclarationMode())
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    "begin program before ending declaration.");
        else if (RudiStack.currentContext().getBracketDepth() != 0)
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    "begin should be a top level command.");

        RudiStack.currentContext().setExecutionMode(true);
    }
}
