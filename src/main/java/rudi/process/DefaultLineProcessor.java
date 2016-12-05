package rudi.process;

import rudi.process.control.IfThenElseLineProcessor;
import rudi.process.control.SkipLineProcessor;
import rudi.process.control.WhileLineProcessor;
import rudi.process.decs.DecsLineProcessor;
import rudi.process.decs.VariableDeclarationProcessor;
import rudi.process.func.CallSubRoutineLineProcessor;
import rudi.process.input.InputLineProcessor;
import rudi.process.print.PrintLineProcessor;
import rudi.process.program.*;
import rudi.process.stop.StopLineProcessor;
import rudi.process.var.VariableAssignmentProcessor;

import java.util.Arrays;

/**
 * A default collection of all the processors. This is the processor
 * chain used by our RUDI interpreter. It bootstrap all other
 * {@link LineProcessor} implementations in a specific order to provide
 * combined functionality. Order is sensitive.
 */
public class DefaultLineProcessor extends DelegatingLineProcessor {

    private static DefaultLineProcessor instance;

    public static DefaultLineProcessor getInstance() {
        if (null == instance)
            instance = new DefaultLineProcessor();
        return instance;
    }

    private DefaultLineProcessor() {
        super(Arrays.asList(
                EmptyLineProcessor.getInstance(),
                ProgramLineProcessor.getInstance(),
                SubRoutineLineProcessor.getInstance(),
                DecsLineProcessor.getInstace(),
                StartingBracketLineProcessor.getInstance(),
                EndingBracketLineProcessor.getInstance(),
                SkipLineProcessor.getInstance(),
                IfThenElseLineProcessor.getInstance(),
                WhileLineProcessor.getInstance(),
                VariableDeclarationProcessor.getInstance(),
                BeginLineProcessor.getInstance(),
                EndLineProcessor.getInstance(),
                ReturnLineProcessor.getInstance(),
                PrintLineProcessor.getInstance(),
                InputLineProcessor.getInstance(),
                CallSubRoutineLineProcessor.getInstance(),
                VariableAssignmentProcessor.getInstance(),
                StopLineProcessor.getInstance()
        ));
    }
}
