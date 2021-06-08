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

        createIntegerOperators();
        createFloatOperators();
    }

    private static void createIntegerOperators(){
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

        SLangInteger.addMethod("operatorSUBTRACT", new SLangJavaFunction((List<SLangObject> parameters, Environment environment) ->
        {
            SLangObject self = parameters.get(0);
            SLangObject other = parameters.get(1);

            if(other.getType().equals(SLangInteger)){
                return new SLangObject(SLangInteger, (long) self.getValue() - (long) other.getValue());
            }else if(other.getType().equals(SLangFloat)){
                return new SLangObject(SLangFloat, (long) self.getValue() - (double) other.getValue());
            }
            throw new UnsupportedOperatorException("Operator - not supported for types 'int' and '" +other.getType().getName() + "'");
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

        SLangInteger.addMethod("operatorDIVIDE", new SLangJavaFunction((List<SLangObject> parameters, Environment environment) ->
        {
            SLangObject self = parameters.get(0);
            SLangObject other = parameters.get(1);

            if(other.getType().equals(SLangInteger)){
                return new SLangObject(SLangFloat, (long) self.getValue() / ((double) (long) other.getValue()));
            }else if(other.getType().equals(SLangFloat)){
                return new SLangObject(SLangFloat, (long) self.getValue() / (double) other.getValue());
            }
            throw new UnsupportedOperatorException("Operator / not supported for types 'int' and '" +other.getType().getName() + "'");
        }
        ));

        SLangInteger.addMethod("operatorNOT", new SLangJavaFunction((List<SLangObject> parameters, Environment environment) ->
        {
            SLangObject self = parameters.get(0);

            return new SLangObject(SLangBoolean, ! ((boolean) self.getValue()));
        }
        ));

        SLangInteger.addMethod("operatorNEGATE", new SLangJavaFunction((List<SLangObject> parameters, Environment environment) ->
        {
            SLangObject self = parameters.get(0);

            return new SLangObject(SLangInteger, -(long)self.getValue());
        }
        ));
    }

    private static void createFloatOperators(){
        SLangFloat.addMethod("operatorADD", new SLangJavaFunction((List<SLangObject> parameters, Environment environment) ->
        {
            SLangObject self = parameters.get(0);
            SLangObject other = parameters.get(1);

            if(other.getType().equals(SLangInteger)){
                return new SLangObject(SLangFloat, (double) self.getValue() + (long) other.getValue());
            }else if(other.getType().equals(SLangFloat)){
                return new SLangObject(SLangFloat, (double) self.getValue() + (double) other.getValue());
            }
            throw new UnsupportedOperatorException("Operator + not supported for types 'float' and '" +other.getType().getName() + "'");
        }
        ));

        SLangFloat.addMethod("operatorSUBTRACT", new SLangJavaFunction((List<SLangObject> parameters, Environment environment) ->
        {
            SLangObject self = parameters.get(0);
            SLangObject other = parameters.get(1);

            if(other.getType().equals(SLangInteger)){
                return new SLangObject(SLangFloat, (double) self.getValue() - (long) other.getValue());
            }else if(other.getType().equals(SLangFloat)){
                return new SLangObject(SLangFloat, (double) self.getValue() - (double) other.getValue());
            }
            throw new UnsupportedOperatorException("Operator - not supported for types 'float' and '" +other.getType().getName() + "'");
        }
        ));

        SLangFloat.addMethod("operatorMULTIPLY", new SLangJavaFunction((List<SLangObject> parameters, Environment environment) ->
        {
            SLangObject self = parameters.get(0);
            SLangObject other = parameters.get(1);

            if(other.getType().equals(SLangInteger)){
                return new SLangObject(SLangFloat, (double) self.getValue() * (long) other.getValue());
            }else if(other.getType().equals(SLangFloat)){
                return new SLangObject(SLangFloat, (double) self.getValue() * (double) other.getValue());
            }
            throw new UnsupportedOperatorException("Operator * not supported for types 'float' and '" +other.getType().getName() + "'");
        }
        ));

        SLangFloat.addMethod("operatorDIVIDE", new SLangJavaFunction((List<SLangObject> parameters, Environment environment) ->
        {
            SLangObject self = parameters.get(0);
            SLangObject other = parameters.get(1);

            if(other.getType().equals(SLangInteger)){
                return new SLangObject(SLangFloat, (double) self.getValue() / ((double) (long) other.getValue()));
            }else if(other.getType().equals(SLangFloat)){
                return new SLangObject(SLangFloat, (double) self.getValue() / (double) other.getValue());
            }
            throw new UnsupportedOperatorException("Operator / not supported for types 'float' and '" +other.getType().getName() + "'");
        }
        ));

        SLangFloat.addMethod("operatorNOT", new SLangJavaFunction((List<SLangObject> parameters, Environment environment) ->
        {
            SLangObject self = parameters.get(0);

            return new SLangObject(SLangBoolean, ! ((boolean) ((long) self.getValue() != 0)));
        }
        ));

        SLangFloat.addMethod("operatorNEGATE", new SLangJavaFunction((List<SLangObject> parameters, Environment environment) ->
        {
            SLangObject self = parameters.get(0);

            return new SLangObject(SLangInteger, -(double)self.getValue());
        }
        ));
    }
}
