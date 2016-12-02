package rudi.support.expression.eval;

import rudi.support.expression.token.Token;

/**
 * Common interface for expression evaluation.
 */
@FunctionalInterface
public interface Evaluator {

    /**
     * Evaluate two tokens and return a new token.
     * If evaluation fails, should throw {@link rudi.error.FailedToEvaluateExpressionException}
     *
     * @param lhs
     * @param rhs
     * @return
     */
    Token evaluate(Token lhs, Token rhs);
}
