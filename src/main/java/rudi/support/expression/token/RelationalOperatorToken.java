package rudi.support.expression.token;

import rudi.error.FailedToEvaluateExpressionException;
import rudi.support.RudiConstant;
import rudi.support.expression.eval.Evaluator;
import rudi.support.literal.Constant;
import rudi.support.variable.VarType;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * A relational operator token in expression
 */
public class RelationalOperatorToken extends Token {

    public RelationalOperatorToken(String faceValue) {
        super(faceValue);
    }

    public boolean isEqual() {
        return RudiConstant.EQ.equals(faceValue.toLowerCase());
    }

    public boolean isNotEqual() {
        return RudiConstant.NE.equals(faceValue.toLowerCase());
    }

    public boolean isGreaterThan() {
        return RudiConstant.GT.equals(faceValue.toLowerCase());
    }

    public boolean isGreaterEqual() {
        return RudiConstant.GE.equals(faceValue.toLowerCase());
    }

    public boolean isLessThan() {
        return RudiConstant.LT.equals(faceValue.toLowerCase());
    }

    public boolean isLessEqual() {
        return RudiConstant.LE.equals(faceValue.toLowerCase());
    }

    @Override
    public boolean isOperand() {
        return false;
    }

    @Override
    public boolean isOperator() {
        return true;
    }

    @Override
    public boolean isParenthesis() {
        return false;
    }

    @Override
    public int priority() {
        return 100;
    }

    @Override
    public Evaluator evaluator() {
        return ((lhs, rhs) -> {
            if (!lhs.isOperand() || !rhs.isOperand())
                throw new FailedToEvaluateExpressionException("Evaluation failed for <" + this.faceValue + "> due to not enough operands.");

            Constant c1 = ((ConstantToken) lhs.evaluator().evaluate(null, null)).getConstant();
            Constant c2 = ((ConstantToken) rhs.evaluator().evaluate(null, null)).getConstant();

            int c = this.compare(c1.getValue(), c2.getValue());
            if (isEqual()) {
                Boolean result = (c == 0);
                return new ConstantToken(result.toString(), new Constant(VarType.BOOLEAN, result));
            } else if (isNotEqual()) {
                Boolean result = (c != 0);
                return new ConstantToken(result.toString(), new Constant(VarType.BOOLEAN, result));
            } else if (isGreaterThan()) {
                Boolean result = (c > 0);
                return new ConstantToken(result.toString(), new Constant(VarType.BOOLEAN, result));
            } else if (isGreaterEqual()) {
                Boolean result = (c >= 0);
                return new ConstantToken(result.toString(), new Constant(VarType.BOOLEAN, result));
            } else if (isLessThan()) {
                Boolean result = (c < 0);
                return new ConstantToken(result.toString(), new Constant(VarType.BOOLEAN, result));
            } else if (isLessEqual()) {
                Boolean result = (c <= 0);
                return new ConstantToken(result.toString(), new Constant(VarType.BOOLEAN, result));
            } else {
                throw new FailedToEvaluateExpressionException("Unsupported operator <" + this.faceValue + ">");
            }
        });
    }

    private int compare(Object o1, Object o2) {
        Optional<Class[]> typeMatch = Stream.of(
                new Class[]{String.class, String.class},
                new Class[]{Integer.class, Integer.class},
                new Class[]{Integer.class, Float.class},
                new Class[]{Float.class, Integer.class},
                new Class[]{Float.class, Float.class}
        ).filter(classes ->
                classes[0].equals(o1.getClass()) && classes[1].equals(o2.getClass())
        ).findAny();

        if (!typeMatch.isPresent())
            throw new FailedToEvaluateExpressionException("Cannot perform <" + this.faceValue + "> on incomparable types.");

        if ((o1 instanceof Integer) && (o2 instanceof Integer)) {
            return ((Integer) o1).compareTo((Integer) o2);
        } else if ((o1 instanceof String) && (o2 instanceof String)) {
            return ((String) o1).compareTo((String) o2);
        } else {
            Float f1 = Float.parseFloat(o1.toString());
            Float f2 = Float.parseFloat(o2.toString());
            if (f1.equals(f2))
                return 0;
            else if (f1 > f2)
                return 1;
            else
                return -1;
        }
    }
}
