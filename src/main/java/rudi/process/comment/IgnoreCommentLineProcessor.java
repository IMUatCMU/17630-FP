package rudi.process.comment;

import rudi.process.LineProcessor;
import rudi.support.RudiStack;

/**
 * An implementation of {@link LineProcessor} to ignore current line
 * while we are in comment mode.
 */
public class IgnoreCommentLineProcessor implements LineProcessor {

    private static IgnoreCommentLineProcessor instance;

    private IgnoreCommentLineProcessor() {

    }

    public static IgnoreCommentLineProcessor getInstance() {
        if (null == instance) {
            instance = new IgnoreCommentLineProcessor();
        }
        return instance;
    }

    @Override
    public boolean canProcess(String line) {
        return RudiStack.getInstance().peek().isComment();
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        // ignore this line
    }
}
