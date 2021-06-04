package sLang.tokenizer;

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternTokenizer implements Tokenizer{
    Set<Character> validChars;
    TokenType type;

    public PatternTokenizer(TokenType type, String chars) {
        validChars = new TreeSet<>();
        for(char character : chars.toCharArray()){
            validChars.add(character);
        }
        this.type = type;
    }

    @Override
    public Token tokenize(String input, int index) {
        Character current = input.charAt(index);
        int consumedChars = 0;
        String value = "";
        while(index < input.length() && validChars.contains(current)){
            value += current;
            consumedChars++;
            index++;
            current = input.charAt(index);
        }
        return value.length() == 0 ? null : new Token(consumedChars, type, value);
    }
}
