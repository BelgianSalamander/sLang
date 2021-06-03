package sLang.types;

import sLang.exceptions.UnsupportedOperatorException;
import sLang.interpreter.Environment;
import sLang.objects.SLangJavaFunction;
import sLang.objects.SLangObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Types {
    public static Map<String, SLangType> types;

    public static SLangType SLangInteger, SLangBoolean, SLangFloat, SLangString, SLangNull, SLangFunction;

    private static SLangType createType(String name){
        SLangType type = new SLangType(name);
        types.put(name, type);
        return type;
    }

    static{
        types = new HashMap<>();

        SLangInteger = createType("int");
        SLangBoolean = createType("boolean");
        SLangFloat = createType("float");
        SLangString = createType("str");
        SLangNull = createType("null");
        SLangFunction = createType("func");

        /*SLangFunction.addMethod("sLangCall", new SLangJavaFunction((List<SLangObject> parameters, Environment environment) ->{
            SLangObject self = parameters.get(0);
            return ((sLang.objects.SLangFunction) self.getValue()).call(environment, parameters.subList(1, parameters.size() - 1));
        }));*/

        //Integer
        SLangInteger.addMethod("operatorADD", new SLangJavaFunction((List<SLangObject> parameters, Environment environment) ->
                {
                    SLangObject self = parameters.get(0);
                    SLangObject other = parameters.get(1);

                    if(other.getType().equals(SLangInteger)){
                        return new SLangObject(SLangInteger, (long) self.getValue() + (long) other.getValue());
                    }else if(other.getType().equals(SLangFloat)){
                        return new SLangObject(SLangFloat, (long) self.getValue() + (double) other.getValue());
                    }
                    throw new UnsupportedOperatorException("Operator + not supported for types 'int' and '" +other.getType().getName() + "'");
                }
                ));

        SLangInteger.addMethod("operatorMULTIPLY", new SLangJavaFunction((List<SLangObject> parameters, Environment environment) ->
        {
            SLangObject self = parameters.get(0);
            SLangObject other = parameters.get(1);

            if(other.getType().equals(SLangInteger)){
                return new SLangObject(SLangInteger, (long) self.getValue() * (long) other.getValue());
            }else if(other.getType().equals(SLangFloat)){
                return new SLangObject(SLangFloat, (long) self.getValue() * (double) other.getValue());
            }
            throw new UnsupportedOperatorException("Operator * not supported for types 'int' and '" +other.getType().getName() + "'");
        }
        ));

        /*SLangInteger.addMethod("operatorDIVIDE", (ParameterList parameters) ->
        {
            SLangObject self = parameters.get(0);
            SLangObject other = parameters.get(1);

            if(other.getType().equals(SLangInteger)){
                return new SLangObject(SLangFloat, (long) self.getValue() / (double) (long) other.getValue());
            }else if(other.getType().equals(SLangFloat)){
                return new SLangObject(SLangFloat, (long) self.getValue() / (double) other.getValue());
            }
            throw new UnsupportedOperatorException("Operator / not supported for types 'int' and '" +other.getType().getName() + "'");
        });

        //Float
        SLangFloat.addMethod("operatorLESS_EQUAL", (ParameterList parameters) -> {
            SLangObject self = parameters.get(0);
            SLangObject other = parameters.get(1);

            if(other.getType().equals(SLangInteger)){
                return new SLangObject(SLangBoolean, (double) self.getValue() <= (long) other.getValue());
            }else if(other.getType().equals(SLangFloat)){
                return new SLangObject(SLangBoolean, (double) self.getValue() <= (double) other.getValue());
            }

            throw new UnsupportedOperatorException("Operator <= not supported for types 'float' and '" + other.getType().getName() + "'");
        });

        SLangFloat.addMethod("operatorGREATER", (ParameterList parameters) -> {
            SLangObject self = parameters.get(0);
            SLangObject other = parameters.get(1);

            if(other.getType().equals(SLangInteger)){
                return new SLangObject(SLangBoolean, (double) self.getValue() > (long) other.getValue());
            }else if(other.getType().equals(SLangFloat)){
                return new SLangObject(SLangBoolean, (double) self.getValue() > (double) other.getValue());
            }

            throw new UnsupportedOperatorException("Operator <= not supported for types 'float' and '" + other.getType().getName() + "'");
        });

        SLangBoolean.addMethod("operatorAND", (ParameterList parameters) -> {
            SLangObject self = parameters.get(0);
            SLangObject other = parameters.get(1);

            return new SLangObject(SLangBoolean , (boolean) self.getValue() && (boolean) other.getValue());
        });

        SLangBoolean.addMethod("operatorOR", (ParameterList parameters) -> {
            SLangObject self = parameters.get(0);
            SLangObject other = parameters.get(1);

            return new SLangObject(SLangBoolean , (boolean) self.getValue() || (boolean) other.getValue());
        });*/
    }

}
