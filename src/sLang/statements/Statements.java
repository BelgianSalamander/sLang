package sLang.statements;

import sLang.exceptions.SLangException;
import sLang.exceptions.UnsupportedOperatorException;
import sLang.expressions.Expression;
import sLang.interpreter.Environment;
import sLang.objects.SLangFunction;
import sLang.objects.SLangObject;
import sLang.types.Types;

import java.util.List;
import java.util.stream.Collectors;

public class Statements {
    public static class ExpressionStatement implements Statement{
        private final Statement expression;

        public ExpressionStatement(Statement expression) {
            this.expression = expression;
        }

        @Override
        public SLangObject run(Environment environment) throws SLangException {
            return expression.run(environment);
        }

        @Override
        public String toString() {
            return expression.toString();
        }
    }

    public static class VariableStatement implements Statement{
        private final String name;
        private final Statement initializer;

        public VariableStatement(String name, Statement initializer) {
            this.name = name;
            this.initializer = initializer;
        }

        @Override
        public SLangObject run(Environment environment) throws SLangException {
            environment.define(name, initializer.run(environment));
            return new SLangObject(Types.SLangNull, null);
        }

        @Override
        public String toString() {
            return "var " + name + " = " + initializer + ";";
        }
    }

    public static class AssignmentStatement implements Statement{
        private final String name;
        private final Statement value;

        public AssignmentStatement(String name, Statement value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public SLangObject run(Environment environment) throws SLangException {
            SLangObject value = this.value.run(environment);
            environment.set(name, value);
            return value;
        }

        @Override
        public String toString() {
            return name + " = " + value + ";";
        }
    }

    public static class Block implements Statement{
        private final List<Statement> statements;

        public Block(List<Statement> statements) {
            this.statements = statements;
        }

        @Override
        public SLangObject run(Environment environment) throws SLangException {
            Environment localEnvironment = new Environment(environment);
            for(Statement statement : statements){
                if(statement instanceof ReturnStatement){
                    return statement.run(localEnvironment);
                }
                statement.run(localEnvironment);
            }
            return null;
        }

        @Override
        public String toString() {
            String str = "{\n";
            for (Statement statement : statements){
                str += "\t" + statement + "\n";
            }
            return str + "}";
        }
    }

    public static class IfStatement implements Statement{
        final List<Statement> statements;
        final List<Statement> conditions;

        public IfStatement(List<Statement> statements, List<Statement> conditions) {
            this.statements = statements;
            this.conditions = conditions;
        }

        @Override
        public SLangObject run(Environment environment) throws SLangException {
            int i;
            for(i = 0; i < conditions.size(); i++){
                if(conditions.get(i).run(environment).toBool()){
                    Environment newEnvironment = new Environment(environment);
                    statements.get(i).run(newEnvironment);
                    return null;
                }
            }
            if(i < statements.size()){
                statements.get(i).run(new Environment(environment));
            }
            return null;
        }

        @Override
        public String toString() {
            String value = "if (" + conditions.get(0) + ")" + statements.get(0);
            int i;
            for(i = 1; i < conditions.size(); i++){
                value += "elif(" + conditions.get(i) + ")" + statements.get(i);
            }
            if(i < statements.size()){
                value += "else" + statements.get(i);
            }
            return value;
        }
    }

    public static class FunctionCall implements Statement{
        private final Statement whatToCall;
        private final List<Statement> arguments;

        public FunctionCall(Statement whatToCall, List<Statement> arguments) {
            this.whatToCall = whatToCall;
            this.arguments = arguments;
        }

        @Override
        public SLangObject run(Environment environment) throws SLangException {
            SLangObject callable =  whatToCall.run(environment);
            if(callable.getType().equals(Types.SLangFunction)){
                return ((SLangFunction) callable.getValue()).call(environment, arguments.stream().map((Statement s) -> {
                    try {
                        return s.run(environment);
                    } catch (SLangException exception) {
                        exception.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toList()));
            }
            throw new UnsupportedOperatorException(callable.getType() + " is not a function!");
        }

        @Override
        public String toString() {
            return whatToCall + "(" + String.join(",", arguments.stream().map((Statement s) -> s.toString()).collect(Collectors.toList())) + ")";
        }
    }

    public static class FunctionDefinition implements Statement{
        private final String name;
        private final Statement code;
        private final List<String> parameterNames;

        public FunctionDefinition(String name, Statement code, List<String> parameterNames) {
            this.name = name;
            this.code = code;
            this.parameterNames = parameterNames;
        }

        @Override
        public SLangObject run(Environment environment) throws SLangException {
            SLangFunction functionObject = new SLangFunction(parameterNames, code);
            SLangObject functionSLangObject = new SLangObject(Types.SLangFunction, functionObject);
            environment.define(name, functionSLangObject);
            return functionSLangObject;
        }

        @Override
        public String toString() {
            return name + " (" + String.join(", ", parameterNames) + ")" + code;
        }
    }

    public static class ReturnStatement implements Statement{
        private final Statement expression;

        public ReturnStatement(Statement expression) {
            this.expression = expression;
        }

        @Override
        public SLangObject run(Environment environment) throws SLangException {
            return expression.run(environment);
        }

        @Override
        public String toString() {
            return "return " + expression;
        }
    }
}
