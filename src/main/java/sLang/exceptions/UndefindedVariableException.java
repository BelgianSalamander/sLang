package sLang.exceptions;

import sLang.objects.SLangObject;

public class UndefindedVariableException extends SLangException {
    public UndefindedVariableException(String errorMessage){
        super(errorMessage);
    }

    public UndefindedVariableException(){
        super();
    }
}
