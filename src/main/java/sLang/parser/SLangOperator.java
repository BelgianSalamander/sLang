package sLang.parser;

import sLang.tokenizer.TokenType;

import java.util.HashMap;
import java.util.Map;

public enum SLangOperator {
    ADD, SUBTRACT, MULTIPLY, DIVIDE,
    NEGATE, NOT,

    EQUAL, NOT_EQUAL, LESS_EQUAL, GREATER_EQUAL, LESS, GREATER, AND, OR;

    private final String functionName;

    private SLangOperator(String functionName){
        this.functionName = functionName;
    }

    private SLangOperator(){
        this.functionName = "operator_" + this.toString().toLowerCase();
    }

    private static Map<TokenType, SLangOperator> tokenToOperator;

    public static SLangOperator fromTokenType(TokenType tokenType){
        return tokenToOperator.get(tokenType);
    }

    static {
        tokenToOperator = new HashMap<>();

        tokenToOperator.put(TokenType.PLUS, ADD);
        tokenToOperator.put(TokenType.MINUS, SUBTRACT);
        tokenToOperator.put(TokenType.STAR, MULTIPLY);
        tokenToOperator.put(TokenType.SLASH, DIVIDE);
        
        tokenToOperator.put(TokenType.EXCLAMATION_MARK, NOT);
        
        tokenToOperator.put(TokenType.DOUBLE_EQUAL, EQUAL);
        tokenToOperator.put(TokenType.EXCLAMATION_MARK_EQUAL, NOT_EQUAL);
        tokenToOperator.put(TokenType.LESS_EQUAL, LESS_EQUAL);
        tokenToOperator.put(TokenType.GREATER_EQUAL, GREATER_EQUAL);
        tokenToOperator.put(TokenType.LESS, LESS);
        tokenToOperator.put(TokenType.GREATER, GREATER);
        tokenToOperator.put(TokenType.AND, AND);
        tokenToOperator.put(TokenType.OR, OR);
        tokenToOperator.put(TokenType.OR, OR);
    }
}
