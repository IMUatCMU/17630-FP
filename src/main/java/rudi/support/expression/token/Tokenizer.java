package rudi.support.expression.token;

import rudi.support.RudiConstant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A tokenizer that turns an expression into a list of tokens, ready for expression evaluation.
 */
public class Tokenizer {

    private final String expression;

    public Tokenizer(String expression) {
        this.expression = expression;
    }

    // TODO fix this, this is wrong
    public List<String> allTokens() {
        return Arrays.stream(this.expression.split(RudiConstant.SPACE))
                .filter(s -> s.length() > 0).collect(Collectors.toList());
    }
}
