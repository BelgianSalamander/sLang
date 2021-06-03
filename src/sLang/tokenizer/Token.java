package sLang.tokenizer;

public class Token {
    private int length, line;
    private TokenType type;

    public int getLength() {
        return length;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public int getLine(){
        return line;
    }

    public void setLine(int line){
        this.line = line;
    }

    private String value;

    public Token(int length, TokenType type, String value) {
        this.length = length;
        this.type = type;
        this.value = value;
    }

    public Token(int length, TokenType type, String value, int line) {
        this.length = length;
        this.type = type;
        this.value = value;
        this.line = line;
    }

    @Override
    public String toString() {
        return "sLang Token [ length = " + length + ", type = " + type + ", value = '" + value + "' line = " + line + " ]";
    }
}
