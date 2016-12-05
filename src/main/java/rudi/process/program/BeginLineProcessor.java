package rudi.process.program;

import rudi.error.CannotProcessLineException;
import rudi.process.LineProcessor;
import rudi.support.RudiConstant;
import rudi.support.RudiStack;
import rudi.support.RudiUtils;

/**
 * An implementation of {@link LineProcessor} that handles 'begin' command.
 * It sets the current context in execution mode. Statements like 'print',
 * 'input' and others can only execute in execution mode.
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
        return line.trim().toLowerCase().equals(RudiConstant.BEGIN_COMMAND);
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        // disallow placement in decs block
        if (RudiStack.currentContext().isDeclarationMode())
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    "begin program before ending declaration.");

        // disallow non-top level placement
        else if (RudiStack.currentContext().getBracketDepth() != 0)
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    "begin should be a top level command.");

        // start execution mode
        RudiStack.currentContext().setExecutionMode(true);
    }
}
