package sLang.tokenizer;

import sLang.exceptions.SLangException;

public class SkipWhitespace implements Tokenizer{
    public SkipWhitespace(){}

    @Override
    public Token tokenize(String input, int index){
        int consumedChars = 0;
        String value = "";

        while(index < input.length()){
            if(Character.isWhitespace(input.charAt(index))) {
                value += input.charAt(index);
                consumedChars++;
                index++;
            }else{
                break;
            }
        }

        return consumedChars == 0 ? null : new Token(consumedChars, TokenType.WHITESPACE, value);
    }
}
