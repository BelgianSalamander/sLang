package sLang.tokenizer;

import sLang.exceptions.SLangException;
import sLang.exceptions.UnterminatedStringException;

import java.util.HashMap;
import java.util.Map;

public class StringTokenizer implements Tokenizer{
    public StringTokenizer(){};

    static private Map<Character, Character> escapeCharacters;

    @Override
    public Token tokenize(String input, int index) throws UnterminatedStringException {
        char starter = input.charAt(index);
        if(starter == '\'' || starter == '"'){
            String value = "";
            boolean escaping = false;
            int consumedChars = 1;
            index++;
            while (index < input.length()){
                char character = input.charAt(index);
                //System.out.println(character + " " + starter + " " + (character == starter) + " " + escaping);
                if(escaping){
                    value += escapeCharacters.get(character);
                    escaping = false;
                    consumedChars++;
                    index++;
                }else if(character == starter){
                    //System.out.println("Returning");
                    return new Token(consumedChars + 1, TokenType.STRING, value);
                }else if(character == '\\'){
                    escaping = true;
                    index++;
                    consumedChars++;
                }else if(character == '\n'){
                    break;
                }else{
                    value += character;
                    index++;
                    consumedChars++;
                }
            }

            throw new UnterminatedStringException("String was not terminated");
        }
        return null;
    }

    static {
        escapeCharacters = new HashMap<>();
        escapeCharacters.put('"', '"');
        escapeCharacters.put('\'', '\'');
        escapeCharacters.put('n', '\n');
        escapeCharacters.put('t', '\t');
        escapeCharacters.put('\\', '\\');
        escapeCharacters.put('r', '\r');
        escapeCharacters.put('b', '\b');
    }
}
