package rudi.process.print;

import rudi.error.CannotProcessLineException;
import rudi.process.LineProcessor;
import rudi.support.RudiConstant;
import rudi.support.RudiUtils;

/**
 * An implementation of {@link rudi.process.LineProcessor} that executes
 * the 'print' command.
 */
public class PrintLineProcessor implements LineProcessor {

    private static PrintLineProcessor instance;

    private PrintLineProcessor() {
    }

    public static PrintLineProcessor getInstance() {
        if (null == instance) {
            instance = new PrintLineProcessor();
        }
        return instance;
    }

    @Override
    public boolean canProcess(String line) {
        return line.trim().toLowerCase().startsWith(RudiConstant.PRINT_COMMAND + RudiConstant.SPACE);
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        line = RudiUtils.stripComments(line).trim();
        assert line.startsWith(RudiConstant.PRINT_COMMAND);

        String content = line.substring(RudiConstant.PRINT_COMMAND.length()).trim();
        if (content.startsWith(RudiConstant.DOUBLE_QUOTE) && content.endsWith(RudiConstant.DOUBLE_QUOTE)) {
            content = content.substring(1, content.length() - 1);
            System.out.print(content);
        } else {
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    "Invalid syntax for 'print' command");
        }
    }
}
