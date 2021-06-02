package sLang.tokenizer;

import sLang.exceptions.SLangException;

public class ExactWordTokenizer implements Tokenizer{
    private TokenType type;
    private String lookingFor;
    int length;
    private boolean allowInName = false;

    public ExactWordTokenizer(TokenType type, String lookingFor, boolean allowInName) {
        this.type = type;
        this.lookingFor = lookingFor;
        length = lookingFor.length();
        this.allowInName = allowInName;
    }

    public ExactWordTokenizer(TokenType type, String lookingFor) {
        this.type = type;
        this.lookingFor = lookingFor;
        length = lookingFor.length();
    }

    private boolean isCharacterAllowedInName(char character){
        return ('0' <= character && character <= '9') || ('A' <= character && character <= 'Z') || ('a' <= character && character <= 'z');
    }

    @Override
    public Token tokenize(String input, int index) {
        if(index + lookingFor.length() <= input.length()) {
            String substring =  input.substring(index, index + length);
            if(substring.equals(lookingFor)){
                if(allowInName){
                    if(index + length < input.length()){
                        if(isCharacterAllowedInName(input.charAt(index + length))){
                            System.out.println("Found a " + lookingFor + " exact match but it was followed by a " + input.charAt(index + 1));
                            return null;
                        }
                    }
                }
                return new Token(length, type, lookingFor);
            }
        }
        return null;
    }
}
