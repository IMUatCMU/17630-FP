package rudi.process.program;

import rudi.process.LineProcessor;
import rudi.support.RudiConstant;
import rudi.support.RudiUtils;

/**
 * An implementation of {@link LineProcessor} that deals with 'subroutine' command.
 */
public class SubRoutineLineProcessor implements LineProcessor {

    private static SubRoutineLineProcessor instance;

    private SubRoutineLineProcessor() {
    }

    public static SubRoutineLineProcessor getInstance() {
        if (null == instance)
            instance = new SubRoutineLineProcessor();
        return instance;
    }

    @Override
    public boolean canProcess(String line) {
        return RudiUtils.stripComments(line).trim().toLowerCase().startsWith(RudiConstant.SUBROUTINE_COMMAND);
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        // do nothing
    }
}
