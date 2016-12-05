package rudi.process.func;

import rudi.error.CannotProcessLineException;
import rudi.error.VariableNotInRegistrarException;
import rudi.process.DefaultLineProcessor;
import rudi.process.LineProcessor;
import rudi.support.*;
import rudi.support.variable.ModifyAccessPair;
import rudi.support.variable.VariableAccessor;
import rudi.support.variable.VariableModifier;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An implementation of {@link LineProcessor} that executes subroutine.
 * It will locate the source from {@link RudiSourceRegistry}, configure
 * a new context on the call stack will all the parameters set. And finally run
 * it through the configured {@link DefaultLineProcessor} (a chain of processors)
 */
public class CallSubRoutineLineProcessor implements LineProcessor {

    private static CallSubRoutineLineProcessor instance;

    private CallSubRoutineLineProcessor() {
    }

    public static CallSubRoutineLineProcessor getInstance() {
        if (null == instance)
            instance = new CallSubRoutineLineProcessor();
        return instance;
    }

    @Override
    public boolean canProcess(String line) {
        String s = RudiUtils.stripComments(line).trim();
        if (s.endsWith(RudiConstant.RIGHT_PAREN) &&
                s.contains(RudiConstant.LEFT_PAREN)) {
            return RudiSourceRegistry.getInstance().containsKey(s.split("\\(")[0]);
        } else {
            return false;
        }
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        if (!RudiStack.currentContext().isExecutionMode())
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    "Cannot call subroutine outside the body of a routine."
            );

        // Get the source
        line = RudiUtils.stripComments(line).trim();
        RudiSource subSource = RudiSourceRegistry.getInstance().get(line.split("\\(")[0]);

        // Prepare a new context
        RudiContext subContext = RudiContext.defaultContext();
        subContext.setSourceCode(subSource);

        // Match argument and parameter list
        List<String> argList = Arrays.stream(
                line.substring(line.indexOf(RudiConstant.LEFT_PAREN) + 1, line.indexOf(RudiConstant.RIGHT_PAREN))
                        .split(RudiConstant.COMMA)
        ).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        List<String> paramList = subSource.getParameterList();
        if (argList.size() != paramList.size()) {
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    "Mismatched parameter count."
            );
        }
        if (argList.size() > 0) {
            if (paramList.stream().distinct().count() != paramList.stream().count()) {
                throw new CannotProcessLineException(
                        RudiUtils.resolveGlobalLineNumber(lineNumber),
                        "Duplicate parameter names."
                );
            }
            // for every argument, get it's modifier and accessor and set them on the new context
            // under the parameter name
            for (int i = 0; i < argList.size(); i++) {
                try {
                    VariableModifier modifier = RudiStack.currentContext().modifier(argList.get(i));
                    VariableAccessor accessor = RudiStack.currentContext().accessor(argList.get(i));
                    subContext.getParamRegistrar().put(paramList.get(i), new ModifyAccessPair(modifier, accessor));
                } catch (VariableNotInRegistrarException e) {
                    throw new CannotProcessLineException(
                            RudiUtils.resolveGlobalLineNumber(lineNumber),
                            "Variable <" + argList.get(i) + "> is not defined."
                    );
                }
            }
        }

        // Get line processors and kick start
        RudiStack.getInstance().push(subContext);
        for (int i = 1; i <= subSource.totalLines(); i++) {
            DefaultLineProcessor.getInstance().doProcess(i, subSource.getLine(i));
        }
    }
}
