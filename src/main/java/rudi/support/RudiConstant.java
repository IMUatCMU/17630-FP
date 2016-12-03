package rudi.support;

import java.util.Arrays;
import java.util.List;

/**
 * Constants
 */
public class RudiConstant {

    public static final String SPACE = " ";
    public static final String START_COMMENT = "/*";
    public static final String END_COMMENT = "*/";
    public static final String PRINT_COMMAND = "print";
    public static final String PROGRAM_COMMAND = "program";
    public static final String SUBROUTINE_COMMAND = "subroutine";
    public static final String STOP_COMMAND = "stop";
    public static final String BEGIN_COMMAND = "begin";
    public static final String END_COMMAND = "end";
    public static final String RETURN_COMMAND = "return";
    public static final String DOUBLE_QUOTE = "\"";
    public static final String DECS = "decs";
    public static final String TYPE_INTEGER = "integer";
    public static final String TYPE_FLOAT = "float";
    public static final String TYPE_STRING = "string";
    public static final String ADD = "+";
    public static final String MINUS = "-";
    public static final String MULTIPLY = "*";
    public static final String DIVIDE = "/";
    public static final String EQ = ":eq:";
    public static final String NE = ":ne:";
    public static final String GT = ":gt:";
    public static final String LT = ":lt:";
    public static final String GE = ":ge:";
    public static final String LE = ":le:";
    public static final String AND = "^";
    public static final String OR = "|";
    public static final String NOT = "~";
    public static final String LEFT_PAREN = "(";
    public static final String RIGHT_PAREN = ")";
    public static final String EQUAL_SIGN = "=";
    public static final String CR = "cr";
    public static final String START_BRAC = "[";
    public static final String END_BRAC = "]";
    public static final String MAIN_PROGRAM_KEY = "main";
    public static final String CONTINUATION = "&";

    public static final List<String> RESERVED_WORDS = Arrays.asList(
            START_COMMENT, END_COMMENT,
            PROGRAM_COMMAND, SUBROUTINE_COMMAND,
            STOP_COMMAND,
            BEGIN_COMMAND, END_COMMAND, RETURN_COMMAND,
            PRINT_COMMAND,
            DECS,
            TYPE_INTEGER, TYPE_FLOAT, TYPE_STRING,
            ADD, MINUS, MULTIPLY, DIVIDE,
            EQ, NE, GT, GE, LT, LE,
            AND, OR, NOT,
            LEFT_PAREN, RIGHT_PAREN,
            START_BRAC, END_BRAC,
            EQUAL_SIGN,
            CONTINUATION,
            CR
    );
}
