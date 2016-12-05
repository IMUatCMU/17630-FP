package rudi.process.comment;

import rudi.process.LineProcessor;
import rudi.support.RudiConstant;
import rudi.support.RudiStack;

/**
 * An implementation of {@link rudi.process.LineProcessor} that
 * attempts to enter the comment mode by setting {@link rudi.support.RudiContext#comment}
 * to true. In comment mode, we will not process anything.
 */
public class StartCommentModeLineProcessor implements LineProcessor {

    private static StartCommentModeLineProcessor instance;

    private StartCommentModeLineProcessor() {

    }

    public static StartCommentModeLineProcessor getInstance() {
        if (null == instance) {
            instance = new StartCommentModeLineProcessor();
        }
        return instance;
    }

    @Override
    public boolean canProcess(String line) {
        return line.trim().startsWith(RudiConstant.START_COMMENT);
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        // This is a single line comment, no need to enter
        // comment mode, just ignore it
        if (line.trim().endsWith(RudiConstant.END_COMMENT)) {
            // do nothing
        }

        // This is the first line of a block comment, enter
        // comment mode here.
        else {
            RudiStack.getInstance().peek().setComment(true);
        }
    }
}
