package rudi.process;

import rudi.error.CannotProcessLineException;

import java.util.List;
import java.util.Optional;

/**
 * An implementation of {@link LineProcessor} that delegates work
 * to other configured {@link LineProcessor}s. It always feed work
 * to the first capable {@link LineProcessor} in the list. If no
 * {@link LineProcessor} is capable of the work, a {@link CannotProcessLineException}
 * is thrown.
 */
public class DelegatingLineProcessor implements LineProcessor {

    private final List<LineProcessor> processors;

    public DelegatingLineProcessor(List<LineProcessor> processors) {
        this.processors = processors;
    }

    @Override
    public boolean canProcess(String line) {
        return true;
    }

    @Override
    public void doProcess(int lineNumber, String line) {
        Optional<LineProcessor> lpCandidate = this.processors
                .stream()
                .filter(lp -> lp.canProcess(line))
                .findFirst();
        if (lpCandidate.isPresent()) {
            lpCandidate.get().doProcess(lineNumber, line);
        } else {
            throw new CannotProcessLineException(lineNumber);
        }
    }

    public List<LineProcessor> getProcessors() {
        return processors;
    }
}
