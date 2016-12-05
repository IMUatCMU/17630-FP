package rudi.process.comment;

import rudi.process.LineProcessor;
import rudi.support.RudiConstant;
import rudi.support.RudiStack;

/**
 * Implementation of {@link LineProcessor} that deals with the end comment symbol.
 * It will leave the comment mode by setting {@link rudi.support.RudiContext#comment} to false.
 */
public class EndCommentModeLineProcessor implements LineProcessor {

    private static EndCommentModeLineProcessor instance;

    private EndCommentModeLineProcessor() {

    }

    public static EndCommentModeLineProcessor getInstance() {
        if (null == instance) {
            instance = new EndCommentModeLineProcessor();
        }
        return instance;
    }

    @Override
    public boolean canProcess(String line) {
        return RudiStack.getInstance().peek().isComment()
                && line.endsWith(RudiConstant.END_COMMENT);
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        RudiStack.getInstance().peek().setComment(false);
    }
}
