package frog.parser;

public enum TokenType {
    // Ключевые слова языка Frog
    CLASS,
    RETURN,
    PRINTLN,
    IMPORT,
    CALC,
    VALUE,

    // Литералы
    STRING,
    NUMBER,

    // Операторы
    PLUS,
    MINUS,
    STAR,
    SLASH,

    // Разделители
    LEFT_BRACE,   // {
    RIGHT_BRACE,  // }
    LEFT_BRACKET, // [
    RIGHT_BRACKET,// ]
    EQUAL,        // =
    COMMA,        // ,

    EOF
}
