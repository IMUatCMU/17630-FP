package rudi.process;

/**
 * Interface for all processors that is capable to deal with a line of program.
 * Implementations are expected to mutate the state of {@link rudi.support.RudiContext}
 * for the current stack.
 */
public interface LineProcessor {

    /**
     * Whether this {@link LineProcessor} can deal with this line.
     * @param line A line of program
     * @return true if it can deals with the line, false otherwise
     */
    boolean canProcess(String line);

    /**
     * Actually process the given line of program.
     * @param lineNumber line number of {@param line}
     * @param line A line of program
     */
    void doProcess(int lineNumber, String line);
}
