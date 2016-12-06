package rudi.process.print;

import rudi.error.CannotProcessLineException;
import rudi.error.FailedToEvaluateExpressionException;
import rudi.error.UnrecognizedTokenException;
import rudi.error.VariableNotInRegistrarException;
import rudi.process.LineProcessor;
import rudi.support.RudiConstant;
import rudi.support.RudiStack;
import rudi.support.RudiUtils;
import rudi.support.expression.eval.ExpressionResolver;
import rudi.support.expression.token.Tokenizer;
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
        if (!RudiStack.currentContext().isExecutionMode()) {
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    "Misplaced value print statement: " + line + ", missing 'begin'?"
            );
        }

        line = RudiUtils.stripComments(line).trim();
        assert line.startsWith(RudiConstant.PRINT_COMMAND);

        String content = line.substring(RudiConstant.PRINT_COMMAND.length()).trim();

        // print a new line
        if (RudiConstant.CR.equals(content.toLowerCase())) {
            System.out.print("\n");
        }
        // print an expression (includes variable, literal or expression)
        else {
            try {
                // ask the expression resolver to resolve the value of the expression
                Constant c = ExpressionResolver.resolve(new Tokenizer(content).allTokens());

                // print the value, <null> if null
                if (null != c.getValue())
                    System.out.print(c.getValue().toString());
                else
                    System.out.print("<null>");
            } catch (FailedToEvaluateExpressionException | UnrecognizedTokenException e) {
                throw new CannotProcessLineException(
                        RudiUtils.resolveGlobalLineNumber(lineNumber),
                        "Invalid expression for print"
                );
            }
        }
    }
}
