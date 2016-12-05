package rudi.process.program;

import rudi.error.CannotProcessLineException;
import rudi.process.LineProcessor;
import rudi.support.RudiConstant;
import rudi.support.RudiStack;
import rudi.support.RudiUtils;

/**
 * An implementation of {@link LineProcessor} to deal with 'return' command.
 */
public class ReturnLineProcessor implements LineProcessor {

    private static ReturnLineProcessor instance;

    private ReturnLineProcessor() {
    }

    public static ReturnLineProcessor getInstance() {
        if (null == instance)
            instance = new ReturnLineProcessor();
        return instance;
    }

    @Override
    public boolean canProcess(String line) {
        return RudiUtils.stripComments(line).trim().toLowerCase().equals(RudiConstant.RETURN_COMMAND);
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        if (!RudiStack.currentContext().isExecutionMode())
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    "Illegal placement of return keyword"
            );

        // similar to 'end', turn off execution mode and pop off stack
        RudiStack.currentContext().setExecutionMode(false);
        RudiStack.getInstance().pop();
    }
}
