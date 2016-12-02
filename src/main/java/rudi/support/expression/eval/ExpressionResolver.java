package rudi.support.expression.eval;

import rudi.error.FailedToEvaluateExpressionException;
import rudi.support.RudiConstant;
import rudi.support.expression.token.*;
import rudi.support.literal.Constant;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An expression resolver that accepts a list of string and evaluate
 * to a meaningful result. Under the hood, it parses each string
 * to a {@link rudi.support.expression.token.Token} using
 * {@link rudi.support.expression.token.TokenFactory} and runs them
 * through shunting yard algorithm with the modification that the
 * output stack will always try to reduce operator tokens to operand
 * token. At the end of the algorithm, there expects to be a single
 * operand token in the output stack, which is our evaluation result.
 *
 * Possible exceptions:
 * - {@link rudi.error.FailedToEvaluateExpressionException}
 * - {@link rudi.error.UnrecognizedTokenException}
 */
public class ExpressionResolver {

    private Stack<Token> outputStack;
    private Stack<Token> operatorStack;

    public static Constant resolve(List<String> rawTokens) {
        List<Token> tokens = rawTokens
                .stream()
                .map(TokenFactory::create)
                .collect(Collectors.toList());
        return new ExpressionResolver().doResolve(tokens).getConstant();
    }

    private ExpressionResolver() {
        this.outputStack = new Stack<>();
        this.operatorStack = new Stack<>();
    }

    private ConstantToken doResolve(List<Token> tokens) {
        tokens.forEach(token -> {
            if (token.isOperand()) {
                pushOntoOutputStack(token);
            } else {
                try {
                    pushOntoOperatorStack(token);
                } catch (InsufficientPriorityException ipe) {
                    Queue<Token> popped = popFromOperatorStackWithPriorityNoLessThan(token.priority());
                    while (popped.size() > 0)
                        pushOntoOutputStack(popped.poll());

                    try {
                        pushOntoOperatorStack(token);
                    } catch (Exception ex) {
                        throw new FailedToEvaluateExpressionException("Failed to evaluate operator <" + token.getFaceValue() + ">");
                    }
                } catch (RightParenthesisPushException rpe) {
                    Queue<Token> popped = popFromOperatorStackToLeftParenthesis();
                    while (popped.size() > 0)
                        pushOntoOutputStack(popped.poll());
                    assert isLeftParenthesis(this.operatorStack.pop());
                }
            }
        });

        while (this.operatorStack.size() > 0) {
            pushOntoOutputStack(this.operatorStack.pop());
        }

        if (this.outputStack.size() != 1 || !(this.outputStack.peek() instanceof ConstantToken))
            throw new FailedToEvaluateExpressionException("Evaluation failed for expression: non-single result");

        return (ConstantToken) this.outputStack.pop();
    }

    private Queue<Token> popFromOperatorStackToLeftParenthesis() {
        Queue<Token> queue = new LinkedList<>();
        for (Token first = this.operatorStack.peek(); this.operatorStack.size() > 0; first = this.operatorStack.peek()) {
            if (isLeftParenthesis(first))
                break;
            queue.add(this.operatorStack.pop());
        }
        if (this.operatorStack.size() == 0 || !isLeftParenthesis(this.operatorStack.peek()))
            throw new FailedToEvaluateExpressionException("Mismatched parenthesis");
        return queue;
    }

    private Queue<Token> popFromOperatorStackWithPriorityNoLessThan(int priority) {
        Queue<Token> queue = new LinkedList<>();
        try {
            while (this.operatorStack.size() > 0) {
                Token first = this.operatorStack.peek();
                if (first.isOperator()) {
                    if (first.priority() >= priority)
                        queue.add(this.operatorStack.pop());
                    else
                        break;
                } else {
                    break;
                }
            }
        } catch (EmptyStackException ex) {
            throw new FailedToEvaluateExpressionException("Invalid expression");
        }
        return queue;
    }

    private void pushOntoOperatorStack(Token token) throws InsufficientPriorityException, RightParenthesisPushException {
        if (isRightParenthesis(token))
            throw new RightParenthesisPushException();

        if (token.isOperator()) {
            if (this.operatorStack.size() > 0) {
                Token peek = this.operatorStack.peek();
                if (peek.isOperator()) {
                    // left associative
                    if (!isLogicalNot(token) && token.priority() <= peek.priority())
                        throw new InsufficientPriorityException();
                    // right associative (we only have 'not')
                    else if (isLogicalNot(token) && token.priority() < peek.priority())
                        throw new InsufficientPriorityException();
                }
            }
        }

        this.operatorStack.push(token);
    }

    private void pushOntoOutputStack(Token token) {
        if (token.isOperator()) {
            Token first = this.outputStack.pop();
            if (null == first)
                throw new FailedToEvaluateExpressionException("Insufficient operands");

            Token second;
            if (isLogicalNot(token)) {
                second = first;
            } else {
                second = this.outputStack.pop();
            }
            if (null == second)
                throw new FailedToEvaluateExpressionException("Insufficient operands");

            this.outputStack.push(token.evaluator().evaluate(second, first));
        } else if (token.isParenthesis()) {
            throw new FailedToEvaluateExpressionException("internal error: parenthesis pushed onto output stack");
        } else {
            this.outputStack.push(token);
        }
    }

    private boolean isLogicalNot(Token token) {
        return (token instanceof LogicalOperatorToken) &&
                ((LogicalOperatorToken) token).isNot();
    }

    private boolean isLeftParenthesis(Token token) {
        return token.isParenthesis() && RudiConstant.LEFT_PAREN.equals(token.getFaceValue());
    }

    private boolean isRightParenthesis(Token token) {
        return token.isParenthesis() && RudiConstant.RIGHT_PAREN.equals(token.getFaceValue());
    }

    private static class InsufficientPriorityException extends Exception {}

    private static class RightParenthesisPushException extends Exception {}
}
