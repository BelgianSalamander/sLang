package sLang.tokenizer;

import sLang.exceptions.CouldntTokenizeException;
import sLang.exceptions.SLangException;

import java.util.ArrayList;
import java.util.List;

public class Tokenizers {
    static public List<Tokenizer> tokenizers;

    static public List<Token> tokenize(String input) throws SLangException {
        List<Token> tokens = new ArrayList<>();
        int index = 0;
        int line = 1;
        while (index < input.length()){
            boolean tokenized = true;
            for(Tokenizer tokenizer : tokenizers){
                Token token = tokenizer.tokenize(input, index);
                if(token != null){
                    index += token.getLength();
                    token.setLine(line);
                    tokenized = true;
                    if(!(token.getType().equals(TokenType.WHITESPACE) || token.getType().equals(TokenType.COMMENT))) {
                        tokens.add(token);
                    }else if(token.getType().equals(TokenType.WHITESPACE)){
                        for(char character : token.getValue().toCharArray()){
                            if(character == '\n') line++;
                        }
                    }
                    break;
                }
            }
            if(!tokenized){
                throw new CouldntTokenizeException();
            }
        }

        tokens.add(new Token(0, TokenType.EOF, "", line));
        return tokens;
    }

    static{
        tokenizers = new ArrayList();
        tokenizers.add(new SkipWhitespace());
        tokenizers.add(new SingleLineCommentTokenizer());
        tokenizers.add(new ExactWordTokenizer(TokenType.DOUBLE_EQUAL, "=="));
        tokenizers.add(new ExactWordTokenizer(TokenType.GREATER_EQUAL, ">="));
        tokenizers.add(new ExactWordTokenizer(TokenType.LESS_EQUAL, "<="));
        tokenizers.add(new ExactWordTokenizer(TokenType.EXCLAMATION_MARK_EQUAL, "!="));
        tokenizers.add(new ExactWordTokenizer(TokenType.EXCLAMATION_MARK, "!"));
        tokenizers.add(new SingleCharacterTokenizer(TokenType.LEFT_PARENTHESE, '('));
        tokenizers.add(new SingleCharacterTokenizer(TokenType.RIGHT_PARENTHESE, ')'));
        tokenizers.add(new SingleCharacterTokenizer(TokenType.LEFT_BRACE, '{'));
        tokenizers.add(new SingleCharacterTokenizer(TokenType.RIGHT_BRACE, '}'));
        tokenizers.add(new SingleCharacterTokenizer(TokenType.SEMICOLON, ';'));
        tokenizers.add(new SingleCharacterTokenizer(TokenType.COMMA, ','));
        tokenizers.add(new SingleCharacterTokenizer(TokenType.MINUS, '-'));
        tokenizers.add(new SingleCharacterTokenizer(TokenType.PLUS, '+'));
        tokenizers.add(new SingleCharacterTokenizer(TokenType.STAR, '*'));
        tokenizers.add(new SingleCharacterTokenizer(TokenType.SLASH, '/'));
        tokenizers.add(new SingleCharacterTokenizer(TokenType.EQUAL, '='));
        tokenizers.add(new SingleCharacterTokenizer(TokenType.GREATER, '>'));
        tokenizers.add(new SingleCharacterTokenizer(TokenType.LESS, '<'));
        tokenizers.add(new SingleCharacterTokenizer(TokenType.DOT, '.'));

        //Keywords
        tokenizers.add(new ExactWordTokenizer(TokenType.AND, "and", true));
        tokenizers.add(new ExactWordTokenizer(TokenType.OR, "or", true));
        tokenizers.add(new ExactWordTokenizer(TokenType.CLASS, "class", true));
        tokenizers.add(new ExactWordTokenizer(TokenType.FUNC, "func", true));
        tokenizers.add(new ExactWordTokenizer(TokenType.FOR, "for", true));
        tokenizers.add(new ExactWordTokenizer(TokenType.WHILE, "while", true));
        tokenizers.add(new ExactWordTokenizer(TokenType.IF, "if", true));
        tokenizers.add(new ExactWordTokenizer(TokenType.ELSE, "else", true));
        tokenizers.add(new ExactWordTokenizer(TokenType.NULL, "null", true));
        tokenizers.add(new ExactWordTokenizer(TokenType.TRUE, "true", true));
        tokenizers.add(new ExactWordTokenizer(TokenType.FALSE, "false", true));
        tokenizers.add(new ExactWordTokenizer(TokenType.RETURN, "return", true));
        tokenizers.add(new ExactWordTokenizer(TokenType.THIS, "this", true));
        tokenizers.add(new ExactWordTokenizer(TokenType.VAR, "var", true));
        tokenizers.add(new ExactWordTokenizer(TokenType.ELIF, "elif", true));

        tokenizers.add(new FloatTokenizer());
        tokenizers.add(new PatternTokenizer(TokenType.INTEGER, "0123456789"));
        tokenizers.add(new PatternTokenizer(TokenType.NAME, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_"));
        tokenizers.add(new StringTokenizer());
    }
}
