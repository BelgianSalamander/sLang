package sLang.tokenizer;

import sLang.exceptions.SLangException;

public class SingleLineCommentTokenizer implements Tokenizer{
    public SingleLineCommentTokenizer(){};

    @Override
    public Token tokenize(String input, int index){
        if(input.charAt(index) == '/'){
            index += 1;
            if(index < input.length()){
                if(input.charAt(index) == '/'){
                    int consumedChars = 2;
                    index++;
                    while (index < input.length()){
                        char character = input.charAt(index);
                        if(character == '\n'){
                            break;
                        }
                        consumedChars++;
                        index++;
                    }
                    return new Token(consumedChars, TokenType.COMMENT, "");
                }
            }
        }
        return null;
    }
}
