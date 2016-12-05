package rudi.process.program;

import rudi.process.LineProcessor;
import rudi.support.RudiConstant;
import rudi.support.RudiContext;
import rudi.support.RudiSourceRegistry;
import rudi.support.RudiStack;

/**
 * An implementation of {@link rudi.process.LineProcessor} that executes
 * the 'program' command. The source registry should already have set
 * the main source.
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
        return line.trim().toLowerCase().equals(RudiConstant.PROGRAM_COMMAND);
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        // create empty context
        RudiContext context = RudiContext.defaultContext();
        // set the main source
        context.setSourceCode(RudiSourceRegistry.getInstance().get(RudiConstant.MAIN_PROGRAM_KEY));
        // push context onto call stack
        RudiStack.getInstance().push(context);
    }
}
