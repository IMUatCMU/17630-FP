package rudi.process;

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
 * A default collection of all the processors.
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
                ProgramLineProcessor.getInstance(),
                SubRoutineLineProcessor.getInstance(),
                DecsLineProcessor.getInstace(),
                StartingBracketLineProcessor.getInstance(),
                EndingBracketLineProcessor.getInstance(),
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
