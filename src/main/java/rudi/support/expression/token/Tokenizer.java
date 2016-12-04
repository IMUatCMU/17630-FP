package rudi.support.expression.token;

import rudi.support.RudiConstant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A tokenizer that turns an expression into a list of tokens, ready for expression evaluation.
 */
public class Tokenizer {

    private final String expression;
    private boolean stringMode = false;
    private String tokenBuffer = "";
    private List<String> tokens = new ArrayList<>();

    public Tokenizer(String expression) {
        this.expression = expression;
    }

    public List<String> allTokens() {
        String expr = this.expression;

        while (expr.length() > 0) {
            String c = expr.substring(0, 1);
            expr = expr.substring(1);

            if (c.equals(RudiConstant.DOUBLE_QUOTE)) {
                tokenBuffer += c;
                this.stringMode = !this.stringMode;
                if (!stringMode)
                    addBufferToTokenIfNonEmpty();
            } else {
                if (stringMode) {
                    tokenBuffer += c;
                } else {
                    if (c.equals(RudiConstant.SPACE)) {
                        addBufferToTokenIfNonEmpty();
                    } else {
                        tokenBuffer += c;
                    }
                }
            }
        }

        addBufferToTokenIfNonEmpty();

        return this.tokens;
    }

    private void addBufferToTokenIfNonEmpty() {
        if (tokenBuffer.trim().length() > 0) {
            this.tokens.add(this.tokenBuffer.trim());
        }
        this.tokenBuffer = "";
    }
}
