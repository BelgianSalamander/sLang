package sLang.interpreter;

import sLang.BuiltInFunctions;
import sLang.SLang;
import sLang.exceptions.SLangException;
import sLang.statements.Statement;

import java.util.List;
import java.util.stream.Collectors;

public class Interpreter {
    private Environment environment = new Environment();

    public void interpret(List<Statement> statements){
        BuiltInFunctions.addBuiltinsToEnv(environment);

        System.out.println("Parsed. Running interpreter!");
        //System.out.println(String.join("\n", statements.stream().map((Statement s) -> s.toString()).collect(Collectors.toList())));
        System.out.println("Starting program!");
        System.out.println("#####sLang Start#####");
        try {
            for (Statement statement : statements) {
                statement.run(environment);
            }
        }catch (SLangException e){
            SLang.error(-1, e);
        }
    }
}
