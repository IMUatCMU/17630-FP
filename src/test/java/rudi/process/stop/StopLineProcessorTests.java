package rudi.process.stop;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import rudi.process.LineProcessor;
import rudi.support.RudiSource;

import java.util.Arrays;

@RunWith(JUnit4.class)
public class StopLineProcessorTests {

    @Test
    public void testStop() {
        RudiSource source = new RudiSource(Arrays.asList(
                "stop"
        ));

        Assert.assertTrue(StopLineProcessor.getInstance().canProcess(source.getLine(1)));

        // We need to prevent System.exit() from being called, otherwise other tests won't run.
        // StopLineProcessor.getInstance().doProcess(1, source.getLine(1));
    }
}
