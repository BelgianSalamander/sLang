package sLang.parser;

import sLang.exceptions.ParsingException;
import sLang.expressions.Expression;
import sLang.expressions.Expressions;
import sLang.objects.SLangObject;
import sLang.statements.Statement;
import sLang.statements.Statements;
import sLang.tokenizer.Token;
import sLang.tokenizer.TokenType;

import javax.print.attribute.standard.MediaSize;

import static sLang.tokenizer.TokenType.*;
import static sLang.types.Types.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int index = 0;

    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    public List<Statement> parse(){
        List<Statement> statements = new ArrayList<>();
        try {
            while (!isAtEnd()) {
                Statement statement = statement();
                statements.add(statement);
                System.out.println(statement);
            }
        }catch (ParsingException e){
            e.printStackTrace();
        }

        return statements;
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
        Expression expression = expression();
        if(match(LEFT_PARENTHESE)){
            while (!tokens.get(index).getType().equals(RIGHT_PARENTHESE))
        }
        consume(SEMICOLON, "Expected a ';' after expression");
        return new Statements.ExpressionStatement(expression);
    }

    private Statement varDeclaration() throws ParsingException {
        String name = consume(NAME, "Expected a variable name").getValue();

        Expression initializer = new Expressions.Literal(new SLangObject(SLangNull, null));
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
                Expression newValue = expression();
                consume(SEMICOLON, "Expected a ';' after an assignment");
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
                Statement nextStatement = block();
                statements.add(nextStatement);
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
            default:
                return expressionStatement();
        }
    }

    private Statement ifStatement() throws ParsingException{
        List<Statement> statements = new ArrayList<>();
        List<Expression> conditions = new ArrayList<>();
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
        consume(LEFT_PARENTHESE, "Expected a '(' after function declaration");
        String name = consume(NAME, "Expected a function name").getValue();
        List<String> parameterNames = new ArrayList<>();
        while (!match(RIGHT_PARENTHESE)){
            parameterNames.add(consume(NAME, "Expected an argument name").getValue());
            match(COMMA);
        }
        return new Statements.FunctionDefinition(name, block(), parameterNames);
    }

    public Expression expression() throws ParsingException {
        return logicalOr();
    }

    public Expression logicalOr() throws ParsingException{
        Expression expression = logicalAnd();

        while(match(OR)){
            Expression right = logicalAnd();
            expression = new Expressions.Binary(expression, SLangOperator.OR, right);
        }

        return expression;
    }

    public Expression logicalAnd() throws ParsingException{
        Expression expression = equality();

        while(match(AND)){
            Expression right = equality();
            expression = new Expressions.Binary(expression, SLangOperator.AND, right);
        }

        return expression;
    }

    private Expression equality() throws ParsingException {
        Expression expression = comparison();

        while (match(DOUBLE_EQUAL, EXCLAMATION_MARK_EQUAL)){
            TokenType operator = previous().getType();
            Expression right = comparison();
            expression = new Expressions.Binary(expression, SLangOperator.fromTokenType(operator), right);
        }

        return expression;
    }

    private Expression comparison() throws ParsingException {
        Expression expression = term();

        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)){
            TokenType operator = previous().getType();
            Expression right = term();
            expression = new Expressions.Binary(expression, SLangOperator.fromTokenType(operator), right);
        }

        return expression;
    }

    private Expression term() throws ParsingException {
        Expression expression = factor();

        while (match(MINUS, PLUS)){
            TokenType operator = previous().getType();
            Expression right = factor();
            expression = new Expressions.Binary(expression, SLangOperator.fromTokenType(operator), right);
        }

        return expression;
    }

    private Expression factor() throws ParsingException {
        Expression expression = unary();

        while (match(STAR, SLASH)){
            TokenType operator = previous().getType();
            Expression right = unary();
            expression = new Expressions.Binary(expression, SLangOperator.fromTokenType(operator), right);
        }

        return expression;
    }

    private Expression unary() throws ParsingException {
        if(match(EXCLAMATION_MARK, MINUS)){
            TokenType operator = previous().getType();
            Expression right = unary();
            SLangOperator sLangOperator = SLangOperator.fromTokenType(operator);
            if(operator.equals(MINUS)){
                sLangOperator = SLangOperator.NEGATE;
            }
            return new Expressions.Unary(sLangOperator, right);
        }

        return primary();
    }

    private Expression primary() throws ParsingException {
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
                return new Expressions.Variable(currentToken.getValue());
        }

        if(match(LEFT_PARENTHESE)){
            Statement statement = varAssignment();
            consume(RIGHT_PARENTHESE, "Expected a ')' after expression");
            return new Expressions.Grouping(statement);
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
