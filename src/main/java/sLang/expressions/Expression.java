package sLang.expressions;

import sLang.exceptions.SLangException;
import sLang.interpreter.Environment;
import sLang.objects.SLangObject;

public interface Expression {
    public SLangObject evaluate(Environment environment) throws SLangException;
}
