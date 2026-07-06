package frog.ast;

/**
 * Присваивание поля в классе Frog: key = ["value"]
 */
public class FieldAssignment extends Node {
    public String key;
    public String value; // уже без кавычек, как обработал Lexer
}
