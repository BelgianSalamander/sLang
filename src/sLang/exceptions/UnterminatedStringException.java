package sLang.exceptions;

public class UnterminatedStringException extends SLangException {
    public UnterminatedStringException(String errorMessage){
        super(errorMessage);
    }

    public UnterminatedStringException(){
        super();
    }
}
