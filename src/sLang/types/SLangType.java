package sLang.types;

import sLang.interpreter.Environment;
import sLang.objects.SLangFunction;
import sLang.objects.SLangJavaFunction;
import sLang.objects.SLangObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SLangType {
    private String name;

    public String getName() {
        return name;
    }

    private Map<String, SLangObject> attributes;

    public SLangType(String name){
        attributes = new HashMap<>();
        addMethod("operatorEQUAL", new SLangJavaFunction((List<SLangObject> parameters, Environment environment) -> {
            SLangObject self = parameters.get(0);
            SLangObject other = parameters.get(1);

            return new SLangObject(Types.SLangBoolean, Objects.equals(self.getValue(), other.getValue()));
        }));
        this.name = name;
    }

    public SLangObject getAttribute(String name){
        return attributes.get(name);
    }

    public void addMethod(String name, SLangFunction function){
        attributes.put(name, new SLangObject(Types.SLangFunction, function));
    }
}
