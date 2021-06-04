package sLang.tokenizer;

public enum TokenType {
    LEFT_PARENTHESE, RIGHT_PARENTHESE, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR, EXCLAMATION_MARK,

    EQUAL, DOUBLE_EQUAL, EXCLAMATION_MARK_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    //Keywords
    CLASS, FUNC, IF, ELSE, FOR, WHILE, NULL, OR, AND, RETURN, THIS, TRUE, FALSE, VAR, ELIF,

    INTEGER,
    FLOAT,
    NAME,
    STRING,
    WHITESPACE,
    EOF,
    COMMENT
}