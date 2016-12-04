package rudi.process.stop;

import rudi.error.CannotProcessLineException;
import rudi.process.LineProcessor;
import rudi.support.RudiConstant;

/**
 * An implementation of {@link rudi.process.LineProcessor} that executes
 * the 'stop' command.
 */
public class StopLineProcessor implements LineProcessor {

    private static StopLineProcessor instance;

    private StopLineProcessor() {
    }

    public static StopLineProcessor getInstance() {
        if (null == instance) {
            instance = new StopLineProcessor();
        }
        return instance;
    }

    @Override
    public boolean canProcess(String line) {
        return line.trim().toLowerCase().equals(RudiConstant.STOP_COMMAND);
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        System.exit(0);
    }
}
