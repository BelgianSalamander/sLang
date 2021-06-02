package sLang.interpreter;

import sLang.exceptions.SLangException;
import sLang.statements.Statement;

import java.util.List;

public class Interpreter {
    private Environment environment = new Environment();

    public void interpret(List<Statement> statements){
        try {
            for (Statement statement : statements) {
                System.out.println(statement.run(environment));
            }
        }catch (SLangException e){
            e.printStackTrace();
        }
    }
}
