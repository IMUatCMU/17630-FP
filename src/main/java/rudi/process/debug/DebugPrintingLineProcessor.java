package rudi.process.debug;

import rudi.process.LineProcessor;

/**
 * An implementation of {@link LineProcessor} that just prints out
 * this line of source. Useful during debugging and developing.
 */
public class DebugPrintingLineProcessor implements LineProcessor {

    private static DebugPrintingLineProcessor instance;

    private DebugPrintingLineProcessor() {

    }

    public static DebugPrintingLineProcessor getInstance() {
        return instance;
    }

    @Override
    public boolean canProcess(String line) {
        return true;
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        System.out.printf("%d - %s\n", lineNumber, line);
    }
}
