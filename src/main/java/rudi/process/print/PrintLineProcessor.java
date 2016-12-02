package rudi.process.print;

import rudi.error.CannotProcessLineException;
import rudi.error.VariableNotInRegistrarException;
import rudi.process.LineProcessor;
import rudi.support.RudiConstant;
import rudi.support.RudiStack;
import rudi.support.RudiUtils;
import rudi.support.literal.Constant;
import rudi.support.literal.ConstantFactory;
import rudi.support.variable.VariableAccessor;

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
        if (RudiConstant.CR.equals(content.toLowerCase())) {
            System.out.print("\n");
        } else if (content.startsWith(RudiConstant.DOUBLE_QUOTE) && content.endsWith(RudiConstant.DOUBLE_QUOTE)) {
            Constant c = ConstantFactory.create(content);
            System.out.print(c.getValue());
        } else {
            try {
                VariableAccessor accessor = RudiStack.currentContext().accessor(content);
                if (null != accessor.access().getValue())
                    System.out.print(accessor.access().getValue().toString());
                else
                    System.out.print("<null>");
            } catch (VariableNotInRegistrarException e) {
                throw new CannotProcessLineException(
                        RudiUtils.resolveGlobalLineNumber(lineNumber),
                        "Invalid syntax for 'print' command");
            }
        }
    }
}
