package rudi.process.program;

import rudi.error.CannotProcessLineException;
import rudi.process.LineProcessor;
import rudi.support.RudiConstant;
import rudi.support.RudiContext;
import rudi.support.RudiStack;

/**
 * An implementation of {@link rudi.process.LineProcessor} that executes
 * the 'program' command.
 */
public class ProgramLineProcessor implements LineProcessor {

    private static ProgramLineProcessor instance;

    private ProgramLineProcessor() {
    }

    public static ProgramLineProcessor getInstance() {
        if (null == instance) {
            instance = new ProgramLineProcessor();
        }
        return instance;
    }

    @Override
    public boolean canProcess(String line) {
        return line.trim().toLowerCase().startsWith(RudiConstant.PROGRAM_COMMAND);
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        RudiContext context = RudiContext.defaultContext();
        RudiStack.getInstance().push(context);
    }
}
