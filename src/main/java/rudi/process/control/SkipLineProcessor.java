package rudi.process.control;

import rudi.error.CannotProcessLineException;
import rudi.error.FailedToEvaluateExpressionException;
import rudi.error.UnrecognizedTokenException;
import rudi.process.DefaultLineProcessor;
import rudi.process.LineProcessor;
import rudi.support.*;
import rudi.support.expression.eval.ExpressionResolver;
import rudi.support.expression.token.Tokenizer;
import rudi.support.literal.Constant;
import rudi.support.variable.VarType;

import java.util.ArrayList;

import static rudi.support.RudiContext.ControlBranch;

/**
 * Created by davidiamyou on 2016-12-03.
 */
public class SkipLineProcessor implements LineProcessor {

    private static SkipLineProcessor instance;

    private SkipLineProcessor() {
    }

    public static SkipLineProcessor getInstance() {
        if (null == instance)
            instance = new SkipLineProcessor();
        return instance;
    }

    @Override
    public boolean canProcess(String line) {
        return RudiStack.currentContext().isSkipMode();
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        // we are in true branch
        if (ControlBranch.TRUE == RudiStack.currentContext().getControlBranch()) {
            if (bracketLevel() == 1 &&
                    RudiStack.currentContext().getBranchStartLineNumber() == 0) {
                RudiStack.currentContext().setBranchStartLineNumber(lineNumber);
            } else if (bracketLevel() == 0) {
                RudiStack.currentContext().setTrueSource(new RudiSource(
                        RudiStack.currentContext().getSourceCode(),
                        RudiStack.currentContext().getBranchStartLineNumber(),
                        RudiStack.currentContext().getBranchEndLineNumber()));
                RudiStack.currentContext().setBranchStartLineNumber(0);
                RudiStack.currentContext().setBranchEndLineNumber(0);

                if (RudiUtils.stripComments(line).trim().toLowerCase().equals(RudiConstant.ELSE)) {
                    RudiStack.currentContext().setControlBranch(ControlBranch.FALSE);
                } else {
                    RudiStack.currentContext().setControlBranch(null);
                }
            }
        }
        // we are in false branch
        else if (ControlBranch.FALSE == RudiStack.currentContext().getControlBranch()) {
            if (bracketLevel() == 1 &&
                    RudiStack.currentContext().getBranchStartLineNumber() == 0) {
                RudiStack.currentContext().setBranchStartLineNumber(lineNumber);
            } else if (bracketLevel() == 0) {
                RudiStack.currentContext().setFalseSource(new RudiSource(
                        RudiStack.currentContext().getSourceCode(),
                        RudiStack.currentContext().getBranchStartLineNumber(),
                        RudiStack.currentContext().getBranchEndLineNumber()));
                RudiStack.currentContext().setControlBranch(null);
            }
        }

        // we evaluate the branch based on the condition
        if (null == RudiStack.currentContext().getControlBranch()) {
            RudiContext ctx = RudiStack.currentContext().contextInheritingVariablesAndParameters();
            ctx.setExecutionMode(true);

            RudiContext.ControlType controlType = RudiStack.currentContext().getControlType();
            if (RudiContext.ControlType.IF == controlType) {
                Constant condition = evaluateCondition(RudiStack.currentContext().getControlExpressionLineNumber());
                if ((Boolean) condition.getValue()) {
                    ctx.setSourceCode(RudiStack.currentContext().getTrueSource());
                } else {
                    ctx.setSourceCode(RudiStack.currentContext().getFalseSource());
                }

                RudiStack.getInstance().push(ctx);
                for (int i = 1; i <= ctx.getSourceCode().totalLines(); i++) {
                    DefaultLineProcessor.getInstance().doProcess(i, ctx.getSourceCode().getLine(i));
                }
                RudiStack.getInstance().pop();
            } else if (RudiContext.ControlType.WHILE == controlType) {
                Constant condition = evaluateCondition(RudiStack.currentContext().getControlExpressionLineNumber());
                while ((Boolean) condition.getValue()) {
                    ctx.setSourceCode(RudiStack.currentContext().getTrueSource());
                    RudiStack.getInstance().push(ctx);
                    for (int i = 1; i <= ctx.getSourceCode().totalLines(); i++) {
                        DefaultLineProcessor.getInstance().doProcess(i, ctx.getSourceCode().getLine(i));
                    }
                    RudiStack.getInstance().pop();
                    condition = evaluateCondition(RudiStack.currentContext().getControlExpressionLineNumber());
                }
            } else {
                throw new IllegalStateException("control type not set");
            }

            // reset
            RudiStack.currentContext().setControlExpressionLineNumber(0);
            RudiStack.currentContext().setControlType(null);
            RudiStack.currentContext().setControlExpression(null);
            RudiStack.currentContext().setControlBranch(null);
            RudiStack.currentContext().setSkipMode(false);
            RudiStack.currentContext().setBranchStartLineNumber(0);
            RudiStack.currentContext().setBranchEndLineNumber(0);
            RudiStack.currentContext().setTrueSource(null);
            RudiStack.currentContext().setFalseSource(new RudiSource(new ArrayList<>()));

            // execute current line
            DefaultLineProcessor.getInstance().doProcess(lineNumber, line);
        }
    }

    private Constant evaluateCondition(int lineNumber) {
        try {
            String expression = RudiStack.currentContext().getControlExpression();
            Constant condition = ExpressionResolver.resolve(new Tokenizer(expression).allTokens());
            if (VarType.BOOLEAN != condition.getType()) {
                throw new CannotProcessLineException(
                        RudiUtils.resolveGlobalLineNumber(lineNumber),
                        "Non-boolean condition in if statement"
                );
            }
            return condition;
        } catch (FailedToEvaluateExpressionException | UnrecognizedTokenException e) {
            throw new CannotProcessLineException(
                    RudiUtils.resolveGlobalLineNumber(lineNumber),
                    e.getMessage()
            );
        }
    }

    private int bracketLevel() {
        return RudiStack.currentContext().getBracketDepth();
    }
}
