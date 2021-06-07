package sLang.exceptions;

public class InvalidArgumentCountException extends SLangException{
    public InvalidArgumentCountException(String message) {
        super(message);
    }

    public InvalidArgumentCountException(){
        super();
    }
}
