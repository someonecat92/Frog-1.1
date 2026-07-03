package frog.parser;

public enum TokenType {
    // Ключевые слова
    CLASS, RETURN, PRINTLN, IMPORT, CALC, VALUE,

    // Литералы
    STRING, NUMBER,

    // Операторы
    PLUS, MINUS, STAR, SLASH,

    // Разделители
    LEFT_BRACE, RIGHT_BRACE,
    LEFT_BRACKET, RIGHT_BRACKET,
    EQUAL, COMMA,

    EOF
}

package frog.parser;

public class Token {
    public final TokenType type;
    public final String lexeme;
    public final Object literal;
    public final int line;

    public Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    @Override
    public String toString() {
        return type + " '" + lexeme + "'";
    }
}
