package sLang.expressions;

import sLang.exceptions.SLangException;
import sLang.exceptions.UnsupportedOperatorException;
import sLang.interpreter.Environment;
import sLang.objects.SLangFunction;
import sLang.objects.SLangObject;
import sLang.parser.SLangOperator;
import sLang.statements.Statement;

import java.util.Arrays;

public class Expressions {
    static public class Binary implements Statement{
        final Statement left, right;
        final SLangOperator operator;

        public Binary(Statement left, SLangOperator operator, Statement right) {
            this.left = left;
            this.right = right;
            this.operator = operator;
        }

        @Override
        public SLangObject run(Environment environment) throws SLangException {
            SLangObject evaluatedLeft = left.run(environment);
            SLangObject evaluatedRight = right.run(environment);
            SLangObject shouldBeAFunction = evaluatedLeft.getAttribute("operator" + operator);
            if(!(shouldBeAFunction.getValue() instanceof SLangFunction)){
                throw new UnsupportedOperatorException(shouldBeAFunction + " is not a function!");
            }
            return ((SLangFunction) shouldBeAFunction.getValue()).call(environment, Arrays.asList(evaluatedLeft, evaluatedRight));
        }

        @Override
        public String toString() {
            return "<sLang Binary Statement [ left = " + left + ", operator = " + operator + ", right = " + right + " ] >";
        }
    }

    static public class Unary implements Statement{
        final Statement right;
        final SLangOperator operator;

        public Unary(SLangOperator operator, Statement right) {
            this.right = right;
            this.operator = operator;
        }

        @Override
        public SLangObject run(Environment environment) throws SLangException {
            SLangObject evaluatedRight = right.run(environment);
            SLangObject shouldBeAFunction = evaluatedRight.getAttribute("operator" + operator);
            if(!(shouldBeAFunction.getValue() instanceof SLangFunction)){
                throw new UnsupportedOperatorException(shouldBeAFunction + " is not a function!");
            }
            return ((SLangFunction) shouldBeAFunction.getValue()).call(environment, Arrays.asList(evaluatedRight));
        }

        @Override
        public String toString() {
            return "<sLang Unary Statement [ operator = " + operator + ", right = " + right + " ] >";
        }
    }

    static public class Literal implements Statement{
        final SLangObject value;

        public Literal(SLangObject value){
            this.value = value;
        }

        @Override
        public SLangObject run(Environment environment) {
            return value;
        }

        public String toString() {
            return value.toString();
        }
    }

    static public class Grouping implements Statement{
        final Statement value;

        public Grouping(Statement expression){
            value = expression;
        }

        @Override
        public SLangObject run(Environment environment) throws SLangException {
            return value.run(environment);
        }

        public String toString() {
            return "( " + value + " )";
        }
    }

    static public class Variable implements Statement{
        final String name;

        public Variable(String name) {
            this.name = name;
        }

        @Override
        public SLangObject run(Environment environment) throws SLangException {
            return environment.get(name);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    static public class Dot implements Statement{
        final Statement left;
        final String right;

        public Dot(Statement left, String right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public SLangObject run(Environment environment) throws SLangException {
            return left.run(environment).getAttribute(right);
        }
    }
}
