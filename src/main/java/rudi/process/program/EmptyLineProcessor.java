package rudi.process.program;

import rudi.process.LineProcessor;

/**
 * An implementation of {@link LineProcessor} to skip empty lines.
 */
public class EmptyLineProcessor implements LineProcessor {

    private static EmptyLineProcessor instance;

    private EmptyLineProcessor() {
    }

    public static EmptyLineProcessor getInstance() {
        if (null == instance)
            instance = new EmptyLineProcessor();
        return instance;
    }

    @Override
    public boolean canProcess(String line) {
        return line.trim().length() == 0;
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        // do nothing
    }
}
