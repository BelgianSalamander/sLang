package sLang.parser;

import sLang.SLang;
import sLang.exceptions.ParsingException;
import sLang.expressions.Expression;
import sLang.expressions.Expressions;
import sLang.objects.SLangObject;
import sLang.statements.Statement;
import sLang.statements.Statements;
import sLang.statements.Statements;
import sLang.tokenizer.Token;
import sLang.tokenizer.TokenType;
import sLang.types.Types;

import javax.print.attribute.standard.MediaSize;

import static sLang.tokenizer.TokenType.*;
import static sLang.types.Types.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    public static SLangObject SLangGlobalNULL = new SLangObject(SLangNull, null);
    private final List<Token> tokens;
    private int index = 0;
    boolean hasError = false;

    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    public List<Statement> parse() {
        List<Statement> statements = new ArrayList<>();
        while (!isAtEnd()) {
            Token firstToken = tokens.get(index);
            try {
                Statement statement = statement();
                statements.add(statement);
                match(SEMICOLON);
            } catch (ParsingException e) {
                SLang.error(firstToken.getLine(), e);
                hasError = true;
                while (!match(SEMICOLON, RIGHT_BRACE)){
                    step();
                    if(isAtEnd()) break;
                }
            }
        }
        if(!hasError) {
            return statements;
        }else{
            return null;
        }
    }

    private boolean match(TokenType... tokenTypes){
        if(tokens.get(index).getType().equals(EOF)){
            return false;
        }
        TokenType currentType = tokens.get(index).getType();
        for(TokenType tokenType : tokenTypes){
            if(currentType.equals(tokenType)){
                index++;
                return true;
            }
        }
        return false;
    }

    private boolean isAtEnd(){
        return tokens.get(index).getType().equals(EOF);
    }

    private Token previous(){
        return tokens.get(index - 1);
    }

    private void step(){
        if(!isAtEnd()){
            index++;
        }
    }

    private Statement statement() throws ParsingException{
        return block();
    }


    private Statement expressionStatement() throws ParsingException {
        Statement expression = expression();
        //consume(SEMICOLON, "Expected a ';' after expression");
        return new Statements.ExpressionStatement(expression);
    }

    private Statement varDeclaration() throws ParsingException {
        String name = consume(NAME, "Expected a variable name").getValue();

        Statement initializer = new Expressions.Literal(new SLangObject(SLangNull, null));
        if(match(EQUAL)){
            initializer = expression();
        }
        consume(SEMICOLON, "Expected ';' after variable declaration");
        return new Statements.VariableStatement(name, initializer);
    }

    private Statement varAssignment() throws ParsingException{
        Token nextToken = tokens.get(index);

        if(nextToken.getType().equals(NAME)){
            index++;
            if(match(EQUAL)){
                Statement newValue = varAssignment();
                //consume(SEMICOLON, "Expected a ';' after an assignment");
                return new Statements.AssignmentStatement(nextToken.getValue(), newValue);
            }else{
                index--;
            }
        }

        return expressionStatement();
    }

    private Statement block() throws ParsingException{
        if(match(LEFT_BRACE)){
            List<Statement> statements = new ArrayList<>();
            while((!match(RIGHT_BRACE)) && (!isAtEnd())){
                Token firstToken = tokens.get(index);
                try {
                    Statement nextStatement = block();
                    statements.add(nextStatement);
                    match(SEMICOLON);
                }catch (ParsingException e){
                    hasError = true;
                    SLang.error(firstToken.getLine(), e);
                    while (!match(SEMICOLON, RIGHT_BRACE)){
                        step();
                        if(isAtEnd()) break;
                    }
                }
            }
            index--;
            consume(RIGHT_BRACE, "Expected a '}' to close block");
            return new Statements.Block(statements);
        }
        return keywordStatement();
    }

    private Statement keywordStatement() throws ParsingException{
        switch(tokens.get(index).getType()){
            case IF:
                step();
                return ifStatement();
            case FUNC:
                step();
                return functionDeclaration();
            case VAR:
                step();
                return varDeclaration();
            case RETURN:
                step();
                return returnStatement();
            default:
                return varAssignment();
        }
    }

    private Statement returnStatement() throws ParsingException{
        if(!match(SEMICOLON)){
            return new Statements.ReturnStatement(varAssignment());
        }
        return new Statements.ReturnStatement(new Expressions.Literal(SLangGlobalNULL));
    }

    private Statement ifStatement() throws ParsingException{
        List<Statement> statements = new ArrayList<>();
        List<Statement> conditions = new ArrayList<>();
        conditions.add(expression());
        statements.add(block());
        while(match(ELIF, ELSE)){
            Token prev = previous();
            if(prev.getType().equals(ELIF)){
                conditions.add(expression());
                statements.add(block());
            }else{
                statements.add(block());
                break;
            }
        }
        return new Statements.IfStatement(statements, conditions);
    }

    private Statement functionDeclaration() throws ParsingException{
        String name = consume(NAME, "Expected a function name").getValue();
        consume(LEFT_PARENTHESE, "Expected a '(' after function declaration");
        List<String> parameterNames = new ArrayList<>();
        while (!match(RIGHT_PARENTHESE)){
            parameterNames.add(consume(NAME, "Expected an argument name").getValue());
            match(COMMA);
        }
        return new Statements.FunctionDefinition(name, block(), parameterNames);
    }

    public Statement expression() throws ParsingException {
        return logicalOr();
    }

    public Statement logicalOr() throws ParsingException{
        Statement expression = logicalAnd();

        while(match(OR)){
            Statement right = logicalAnd();
            expression = new Expressions.Binary(expression, SLangOperator.OR, right);
        }

        return expression;
    }

    public Statement logicalAnd() throws ParsingException{
        Statement expression = equality();

        while(match(AND)){
            Statement right = equality();
            expression = new Expressions.Binary(expression, SLangOperator.AND, right);
        }

        return expression;
    }

    private Statement equality() throws ParsingException {
        Statement expression = comparison();

        while (match(DOUBLE_EQUAL, EXCLAMATION_MARK_EQUAL)){
            TokenType operator = previous().getType();
            Statement right = comparison();
            expression = new Expressions.Binary(expression, SLangOperator.fromTokenType(operator), right);
        }

        return expression;
    }

    private Statement comparison() throws ParsingException {
        Statement expression = term();

        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)){
            TokenType operator = previous().getType();
            Statement right = term();
            expression = new Expressions.Binary(expression, SLangOperator.fromTokenType(operator), right);
        }

        return expression;
    }

    private Statement term() throws ParsingException {
        Statement expression = factor();

        while (match(MINUS, PLUS)){
            TokenType operator = previous().getType();
            Statement right = factor();
            expression = new Expressions.Binary(expression, SLangOperator.fromTokenType(operator), right);
        }

        return expression;
    }

    private Statement factor() throws ParsingException {
        Statement expression = getAttribute();

        while (match(STAR, SLASH)){
            TokenType operator = previous().getType();
            Statement right = getAttribute();
            expression = new Expressions.Binary(expression, SLangOperator.fromTokenType(operator), right);
        }

        return expression;
    }

    private Statement getAttribute() throws ParsingException{
        Statement expression = unary();

        while (match(DOT)){
            Statement right = new Expressions.Dot(expression, consume(NAME, "Expected an attribute name").getValue());
        }

        return expression;
    }

    private Statement unary() throws ParsingException {
        if(match(EXCLAMATION_MARK, MINUS)){
            TokenType operator = previous().getType();
            Statement right = unary();
            SLangOperator sLangOperator = SLangOperator.fromTokenType(operator);
            if(operator.equals(MINUS)){
                sLangOperator = SLangOperator.NEGATE;
            }
            return new Expressions.Unary(sLangOperator, right);
        }

        return primary();
    }

    private Statement primary() throws ParsingException {
        if (match(FALSE)) {
            return new Expressions.Literal(new SLangObject(SLangBoolean, false));
        }
        if (match(TRUE)) {
            return new Expressions.Literal(new SLangObject(SLangBoolean, true));
        }
        if (match(NULL)) {
            return new Expressions.Literal(new SLangObject(SLangNull, null));
        }

        Token currentToken = tokens.get(index);
        TokenType type = currentToken.getType();

        Statement mayBeFunctionCall = null;

        switch (type){
            case INTEGER:
                index++;
                return new Expressions.Literal(new SLangObject(SLangInteger, Long.valueOf(currentToken.getValue())));
            case FLOAT:
                index++;
                return new Expressions.Literal(new SLangObject(SLangFloat, Double.valueOf(currentToken.getValue())));
            case STRING:
                index++;
                return new Expressions.Literal(new SLangObject(SLangString, currentToken.getValue()));
            case NAME:
                index++;
                mayBeFunctionCall = new Expressions.Variable(currentToken.getValue());
                break;
            default:
                if(match(LEFT_PARENTHESE)){
                    Statement statement = varAssignment();
                    consume(RIGHT_PARENTHESE, "Expected a ')' after expression");
                    mayBeFunctionCall = new Expressions.Grouping(statement);
                    //return new Expressions.Grouping(statement);
                }
        }

        if(mayBeFunctionCall != null){
            while (match(LEFT_PARENTHESE)){
                List<Statement> parameters = new ArrayList<>();
                while(!match(RIGHT_PARENTHESE)){
                    parameters.add(varAssignment());
                    match(COMMA);
                }
                mayBeFunctionCall =  new Statements.FunctionCall(mayBeFunctionCall, parameters);
            }
            return mayBeFunctionCall;
        }

        throw new ParsingException("Couldn't understand token : " + currentToken);
    }

    private Token consume(TokenType type, String message) throws ParsingException {
        if(tokens.get(index).getType().equals(type)){
            index++;
            return previous();
        }

        throw new ParsingException(message);
    }
}
