package sLang.exceptions;

public class CouldntTokenizeException extends SLangException{
    public CouldntTokenizeException(){
        super("Could not find a valid tokenizer");
    }

    public CouldntTokenizeException(String message){
        super(message);
    }
}
