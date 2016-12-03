package rudi.process.program;


import rudi.error.CannotProcessLineException;
import rudi.process.LineProcessor;
import rudi.support.RudiConstant;
import rudi.support.RudiStack;
import rudi.support.RudiUtils;

/**
 * An implementation of {@link LineProcessor} that deals with 'end' command.
 */
public class EndLineProcessor implements LineProcessor {

    private static EndLineProcessor instance;

    private EndLineProcessor() {
    }

    public static EndLineProcessor getInstance() {
        if (null == instance)
            instance = new EndLineProcessor();
        return instance;
    }

    @Override
    public boolean canProcess(String line) {
        return line.trim().toLowerCase().startsWith(RudiConstant.END_COMMAND);
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        if (!RudiStack.currentContext().isExecutionMode())
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    "Illegal placement of end keyword"
            );

        RudiStack.currentContext().setExecutionMode(false);
        RudiStack.getInstance().pop();
    }
}
