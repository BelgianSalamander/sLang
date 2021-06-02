package sLang.statements;

import sLang.exceptions.SLangException;
import sLang.interpreter.Environment;
import sLang.objects.SLangObject;

public interface Statement {
    public SLangObject run(Environment environment) throws SLangException;
}
