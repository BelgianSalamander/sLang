package sLang.objects;

import sLang.exceptions.SLangException;
import sLang.interpreter.Environment;
import sLang.statements.Statement;

import java.util.List;

public class SLangJavaFunction extends SLangFunction{
    private final SLangJavaFunctionalInterface function;

    @FunctionalInterface
    public static interface SLangJavaFunctionalInterface{
        public SLangObject call(List<SLangObject> parameters, Environment environment) throws SLangException;
    }

    public SLangJavaFunction(SLangJavaFunctionalInterface function) {
        super(null, null);
        this.function = function;
    }

    @Override
    public SLangObject call(Environment environment, List<SLangObject> parameters) throws SLangException {
        return function.call(parameters, environment);
    }

    @Override
    public String toString() {
        return "<Java Function>";
    }
}
