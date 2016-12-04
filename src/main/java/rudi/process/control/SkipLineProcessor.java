package rudi.process.control;

import rudi.process.DefaultLineProcessor;
import rudi.process.LineProcessor;
import rudi.support.*;
import rudi.support.literal.Constant;

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
            if ((Boolean) RudiStack.currentContext().getControlCondition().getValue()) {
                ctx.setSourceCode(RudiStack.currentContext().getTrueSource());
            } else {
                ctx.setSourceCode(RudiStack.currentContext().getFalseSource());
            }

            // push
            RudiStack.getInstance().push(ctx);

            // execute
            for (int i = 1; i <= ctx.getSourceCode().totalLines(); i++) {
                DefaultLineProcessor.getInstance().doProcess(i, ctx.getSourceCode().getLine(i));
            }

            // pop
            RudiStack.getInstance().pop();

            // reset
            RudiStack.currentContext().setControlCondition(null);
            RudiStack.currentContext().setControlBranch(null);
            RudiStack.currentContext().setSkipMode(false);
            RudiStack.currentContext().setBranchStartLineNumber(0);
            RudiStack.currentContext().setBranchEndLineNumber(0);
            RudiStack.currentContext().setTrueSource(null);
            RudiStack.currentContext().setFalseSource(new RudiSource(new ArrayList<>()));
        }
    }

    private int bracketLevel() {
        return RudiStack.currentContext().getBracketDepth();
    }
}
