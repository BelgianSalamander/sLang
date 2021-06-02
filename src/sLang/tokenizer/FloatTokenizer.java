package sLang.tokenizer;

import sLang.exceptions.SLangException;

public class FloatTokenizer implements Tokenizer{
    private boolean isDigit(char character){
        return character >= '0' && character <= '9';
    }

    @Override
    public Token tokenize(String input, int index) throws SLangException {
        String value = "";
        boolean hadDecimalPoint = false;
        if(!isDigit(input.charAt(index))){
            return null;
        }
        int consumedChars = 0;
        while (index < input.length()){
            char character = input.charAt(index);
            if(isDigit(character)){
                value  += character;
            }else if(character == '.'){
                if(hadDecimalPoint){
                    return null;
                }
                value += '.';
                hadDecimalPoint = true;
            }else{
                break;
            }
            index++;
            consumedChars++;
        }

        if(hadDecimalPoint){
            return new Token(consumedChars, TokenType.FLOAT, value);
        }
        return null;
    }
}
