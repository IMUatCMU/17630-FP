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
 * An implementation of {@link LineProcessor} that deals with collecting the
 * source code of the condition branches and execute one based on the outcome
 * of the condition. This processor relies on the flags and data set by
 * {@link IfThenElseLineProcessor} or {@link WhileLineProcessor}.
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
            // if we hit the first line of non-bracket code after the line of the condition, mark it as the
            // the first line of the TRUE-branch's source
            if (bracketLevel() >= RudiStack.currentContext().getControlExpressionBracketDepth() + 1 &&
                    RudiStack.currentContext().getBranchStartLineNumber() == 0) {
                RudiStack.currentContext().setBranchStartLineNumber(lineNumber);
            }
            // if we are back on the same level before entering the TRUE-branch, mark it as the last
            // line of the TRUE-branch's source.
            else if (bracketLevel() <= RudiStack.currentContext().getControlExpressionBracketDepth()) {
                // collect source since we have both start and end line number
                RudiStack.currentContext().setTrueSource(new RudiSource(
                        RudiStack.currentContext().getSourceCode(),
                        RudiStack.currentContext().getBranchStartLineNumber(),
                        RudiStack.currentContext().getBranchEndLineNumber()));
                RudiStack.currentContext().setBranchStartLineNumber(0);
                RudiStack.currentContext().setBranchEndLineNumber(0);

                // if we are on 'else', then there's an else statement waiting, mark the program
                // entering the FALSE portion of the branch. Otherwise, set the control branch to
                // null, which indicates we are done collecting.
                if (RudiUtils.stripComments(line).trim().toLowerCase().equals(RudiConstant.ELSE)) {
                    RudiStack.currentContext().setControlBranch(ControlBranch.FALSE);
                } else {
                    RudiStack.currentContext().setControlBranch(null);
                }
            }
        }

        // we are in false branch
        else if (ControlBranch.FALSE == RudiStack.currentContext().getControlBranch()) {
            // if we hit the first line of non-bracket code after the line of the condition, mark it as the
            // the first line of the FALSE-branch's source
            if (bracketLevel() >= RudiStack.currentContext().getControlExpressionBracketDepth() + 1 &&
                    RudiStack.currentContext().getBranchStartLineNumber() == 0) {
                RudiStack.currentContext().setBranchStartLineNumber(lineNumber);
            }
            // if we are back on the same level before entering the FALSE-branch, mark it as the last
            // line of the FALSE-branch's source.
            else if (bracketLevel() <= RudiStack.currentContext().getControlExpressionBracketDepth()) {
                // collect source since we have both start and end line number
                RudiStack.currentContext().setFalseSource(new RudiSource(
                        RudiStack.currentContext().getSourceCode(),
                        RudiStack.currentContext().getBranchStartLineNumber(),
                        RudiStack.currentContext().getBranchEndLineNumber()));

                // indicate we are done collecting
                RudiStack.currentContext().setControlBranch(null);
            }
        }

        // if done collecting, evaluate the branch based on the condition
        if (null == RudiStack.currentContext().getControlBranch()) {
            // create a new context (to be pushed on to call stack) which inherits all the variables
            // and parameters from the current context. after all, we are not really going to a
            // subroutine, the context for the branch shouldn't lose access to the current scope.
            RudiContext ctx = RudiStack.currentContext().contextInheritingVariablesAndParameters();

            // manually set to execution mode so we can immediately execute all the statements.
            ctx.setExecutionMode(true);

            // we are dealing with control structure 'IF'
            RudiContext.ControlType controlType = RudiStack.currentContext().getControlType();
            if (RudiContext.ControlType.IF == controlType) {
                // evaluate the condition
                Constant condition = evaluateCondition(RudiStack.currentContext().getControlExpressionLineNumber());
                // set TRUE-source for execution if condition is true
                if ((Boolean) condition.getValue()) {
                    ctx.setSourceCode(RudiStack.currentContext().getTrueSource());
                }
                // set FALSE-source for execution if condition otherwise
                // note the FALSE-source will be an empty source (simulating empty branch) in the absence of else.
                else {
                    ctx.setSourceCode(RudiStack.currentContext().getFalseSource());
                }

                // push new context onto call stack
                RudiStack.getInstance().push(ctx);
                // execute line by line
                for (int i = 1; i <= ctx.getSourceCode().totalLines(); i++) {
                    DefaultLineProcessor.getInstance().doProcess(i, ctx.getSourceCode().getLine(i));
                }
                // fake an 'end' commend so cached conditions have a chance to execute
                // this is necessary since we are only executing the entire control structure
                // one line AFTER the end of it. In the case that the branch's code is another
                // control structure, it won't be executed due to lack of "one line AFTER end of control
                // structure". Hence, we manually add in an 'end' statement which will give the source
                // a chance to execute its control structure if it has one, and won't interfere with normal
                // execution if it doesn't.
                // BTW, end command also brings the benefit of popping the context off the call stack so
                // we don't have to do it manually here.
                DefaultLineProcessor.getInstance().doProcess(ctx.getSourceCode().totalLines() + 1, "end");
            }
            // we are dealing with control structure 'WHILE'
            else if (RudiContext.ControlType.WHILE == controlType) {
                // evaluate condition
                Constant condition = evaluateCondition(RudiStack.currentContext().getControlExpressionLineNumber());
                // execute TRUE-branch of the source if condition is true
                while ((Boolean) condition.getValue()) {
                    ctx.setSourceCode(RudiStack.currentContext().getTrueSource());
                    ctx.setExecutionMode(true);
                    // push context onto call stack
                    RudiStack.getInstance().push(ctx);

                    // execute code line by line
                    for (int i = 1; i <= ctx.getSourceCode().totalLines(); i++) {
                        DefaultLineProcessor.getInstance().doProcess(i, ctx.getSourceCode().getLine(i));
                    }
                    // fake an 'end' commend so cached conditions have a chance to execute
                    DefaultLineProcessor.getInstance().doProcess(ctx.getSourceCode().totalLines() + 1, "end");
                    // important!!! refresh the condition result
                    condition = evaluateCondition(RudiStack.currentContext().getControlExpressionLineNumber());
                }
            }
            // shouldn't ever be here.
            else {
                throw new IllegalStateException("control type not set");
            }

            // reset the control structure's flags and cached data
            RudiStack.currentContext().setControlExpressionBracketDepth(0);
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
            // since we are only executing the above ONE-LINE AFTER the end of the control
            // structure's source. we need to take care of executing the current line as well.
            DefaultLineProcessor.getInstance().doProcess(lineNumber, line);
        }
    }

    // evaluate the cached control expression to get a boolean result returned as constant
    private Constant evaluateCondition(int lineNumber) {
        try {
            String expression = RudiStack.currentContext().getControlExpression();
            Constant condition = ExpressionResolver.resolve(new Tokenizer(expression).allTokens());
            // the result must be a boolean
            if (VarType.BOOLEAN != condition.getType()) {
                throw new CannotProcessLineException(
                        RudiUtils.resolveGlobalLineNumber(lineNumber),
                        "Non-boolean condition in control statement"
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
