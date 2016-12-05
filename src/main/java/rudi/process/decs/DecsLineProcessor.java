package rudi.process.decs;

import rudi.error.CannotProcessLineException;
import rudi.process.LineProcessor;
import rudi.support.RudiConstant;
import rudi.support.RudiStack;
import rudi.support.RudiUtils;

/**
 * An implementation of {@link LineProcessor} that handles the 'decs' keyword.
 * The 'decs' keyword needs to be on its own line. This processor marks the
 * current context to enter declaration mode
 */
public class DecsLineProcessor implements LineProcessor {

    private static DecsLineProcessor instace;

    private DecsLineProcessor() {
    }

    public static DecsLineProcessor getInstace() {
        if (null == instace) {
            return new DecsLineProcessor();
        }
        return instace;
    }

    @Override
    public boolean canProcess(String line) {
        return line.trim().toLowerCase().startsWith(RudiConstant.DECS);
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        line = RudiUtils.stripComments(line).trim();

        // decs needs to be on its own line
        if (!line.toLowerCase().equals(RudiConstant.DECS)) {
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    "Does not recognized declaration block syntax: " + line);
        }

        // do not allow entering declaration mode twice (i.e. two decs block)
        if (RudiStack.currentContext().isDeclarationConcluded())
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    "Cannot have two or more declaration blocks");

        // start declaration mode
        RudiStack.getInstance().peek().setDeclarationMode(true);
    }
}
