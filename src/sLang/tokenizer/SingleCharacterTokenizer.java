package sLang.tokenizer;

public class SingleCharacterTokenizer implements Tokenizer{
    TokenType type;
    char value;

    public SingleCharacterTokenizer(TokenType type, char value){
        this.type = type;
        this.value = value;
    }

    @Override
    public Token tokenize(String input, int index) {
        return value == input.charAt(index) ? new Token(1, type, Character.toString(value)) : null;
    }
}
