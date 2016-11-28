package rudi.process.debug;

import rudi.process.LineProcessor;
import rudi.support.RudiLine;

import java.util.LinkedList;
import java.util.List;

/**
 * An implementation of {@link LineProcessor} which collects all lines.
 * Useful for testing.
 */
public class DebugCollectingLineProcessor implements LineProcessor {

    private final List<RudiLine> lines = new LinkedList<>();

    @Override
    public boolean canProcess(String line) {
        return true;
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        this.lines.add(new RudiLine(lineNumber, line));
    }

    public List<RudiLine> getLines() {
        return lines;
    }
}
