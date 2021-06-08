package sLang;

import sLang.exceptions.SLangException;
import sLang.interpreter.Environment;
import sLang.interpreter.Interpreter;
import sLang.parser.Parser;
import sLang.statements.Statement;
import sLang.tokenizer.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class SLang {
    static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        if(args.length >= 1){
            runFile(args[0]);
        }else{
            prompt();
        }
    }

    private static void prompt(){
        System.out.println("Welcome to sLang!");
        System.out.println("To run a script use slang [path]");
    }

    private static void runFile(String path) throws IOException {
        System.out.println(path);
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
    }

    private static void run(String program){
        try {
            Interpreter interpreter = new Interpreter();
            List<Token> tokens = Tokenizers.tokenize(program);
            System.out.println(tokens);
            Parser parser = new Parser(tokens);
            List<Statement> statements = parser.parse();
            interpreter.interpret(statements);
        }catch (SLangException e){
            error(0, e);
        }
    }

    public static void error(int line, SLangException exception){
        String message = exception.getMessage();
        report(line, "", message, exception.getClass().getTypeName());
    }

    private static void report(int line, String s, String message, String errorType) {
        System.out.println(errorType + " : " + message + " at line " + line);
    }
}
