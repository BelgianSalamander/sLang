package sLang;

import sLang.interpreter.Environment;
import sLang.objects.SLangFunction;
import sLang.objects.SLangJavaFunction;
import sLang.objects.SLangObject;
import sLang.types.Types;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BuiltInFunctions {
    static public List<SLangObject> functions;
    static public List<String> names;

    static public void addBuiltinsToEnv(Environment environment){
        for(int i = 0; i < functions.size(); i++){
            environment.define(names.get(i), functions.get(i));
        }
    }

    static {
        functions = new ArrayList<>();
        names = new ArrayList<>();

        names.add("print");

        functions.add(new SLangObject(Types.SLangFunction, new SLangJavaFunction((List<SLangObject> parameters, Environment environment) -> {
            System.out.println(String.join(" ", parameters.stream().map( (SLangObject s) -> {return s.toString();} ).collect(Collectors.toList())));
            return new SLangObject(Types.SLangNull, null);
        })));
    }
}
