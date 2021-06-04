package sLang.exceptions;

public class ParsingException extends SLangException{
    public ParsingException(String errorMessage){
        super(errorMessage);
    }

    public ParsingException(){
        super();
    }
}
