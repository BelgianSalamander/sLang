package sLang.objects;

import sLang.exceptions.InvalidArgumentCountException;
import sLang.exceptions.SLangException;
import sLang.exceptions.UnsupportedOperatorException;
import sLang.interpreter.Environment;
import sLang.statements.Statement;

import java.util.List;

public class SLangFunction {
    private final List<String> parameterNames;
    private final Statement code;

    public SLangFunction(List<String> parameterNames, Statement code) {
        this.parameterNames = parameterNames;
        this.code = code;
    }

    public SLangObject call(Environment environment, List<SLangObject> parameters) throws SLangException {
        if(parameters.size() != parameterNames.size()){
            throw new InvalidArgumentCountException(parameters.size() + " arguments were passed but the function expected " + parameterNames.size());
        }
        Environment functionalEnvironment = new Environment(environment);

        for(int i = 0; i < parameters.size(); i++){
            functionalEnvironment.define(parameterNames.get(i), parameters.get(i));
        }

        return code.run(functionalEnvironment);
    }

    @Override
    public String toString() {
        return "<sLang Function [ parameters = " + parameterNames + " code = " + code + " >";
    }
}
