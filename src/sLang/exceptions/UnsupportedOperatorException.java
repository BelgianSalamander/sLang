package sLang.exceptions;

public class UnsupportedOperatorException extends SLangException{
    public UnsupportedOperatorException(String errorMessage){
        super(errorMessage);
    }

    public UnsupportedOperatorException(){
        super();
    }
}
