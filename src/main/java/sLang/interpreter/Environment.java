package sLang.interpreter;

import sLang.exceptions.UndefindedVariableException;
import sLang.objects.SLangObject;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Map<String, SLangObject> values = new HashMap<>();
    final private Environment previousScope;

    public Environment() {
        this(null);
    }

    public Environment(Environment previousScope) {
        this.previousScope = previousScope;
    }

    public void define(String name, SLangObject value){
        values.put(name, value);
    }

    public void set(String name, SLangObject value) throws UndefindedVariableException {
        if(has(name)){
            values.put(name, value);
            return;
        }
        if(previousScope != null) {
            previousScope.set(name, value);
            return;
        }
        throw new UndefindedVariableException("Assignment before declaration");
    }

    public SLangObject get(String name) throws UndefindedVariableException {
        SLangObject value = values.get(name);
        if(value == null){
            if(previousScope != null){
                return previousScope.get(name);
            }
            throw new UndefindedVariableException();
        }
        return value;
    }

    public boolean has(String name){
        return values.containsKey(name);
    }
}
