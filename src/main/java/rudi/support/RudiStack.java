package rudi.support;

import java.util.Stack;
import java.util.stream.Stream;

/**
 * Stack for {@link RudiContext}
 */
public class RudiStack extends Stack<RudiContext> {

    private static RudiStack instance;

    public static RudiStack getInstance() {
        if (null == instance) {
            instance = new RudiStack();
        }
        return instance;
    }

    private RudiStack() {
        super();
    }

    @Override
    public Stream<RudiContext> stream() {
        return super.stream();
    }

    @Override
    public Stream<RudiContext> parallelStream() {
        return super.parallelStream();
    }
}
