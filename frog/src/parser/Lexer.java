package frog.parser;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private int start = 0;
    private int current = 0;
    private int line = 1;

    public Lexer(String source) {
        this.source = source;
    }

    public List<Token> tokenize() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    private void scanToken() {
        char c = advance();

        if (c == '{') addToken(TokenType.LEFT_BRACE);
        else if (c == '}') addToken(TokenType.RIGHT_BRACE);
        else if (c == '[') addToken(TokenType.LEFT_BRACKET);
        else if (c == ']') addToken(TokenType.RIGHT_BRACKET);
        else if (c == '=') addToken(TokenType.EQUAL);
        else if (c == ',') addToken(TokenType.COMMA);
        else if (c == '+') addToken(TokenType.PLUS);
        else if (c == '-') addToken(TokenType.MINUS);
        else if (c == '*') addToken(TokenType.STAR);
        else if (c == '/') addToken(TokenType.SLASH);
        else if (c == '"') string();
        else if (Character.isDigit(c)) number();
        else if (Character.isLetter(c)) identifierOrKeyword();
        else if (Character.isWhitespace(c)) {
            // Пропускаем пробелы, табуляции, переносы строк
            if (c == '\n') line++;
        } else {
            // Игнорируем неизвестные символы или выбрасываем ошибку
        }
    }

    private void string() {
        // Ожидаем формат ["Текст"]
        // Пропускаем открывающую кавычку
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }

        if (isAtEnd()) {
            throw new RuntimeException("Незакрытая строка.");
        }

        // Пропускаем закрывающую кавычку
        advance();

        // Извлекаем содержимое между [" и "]
        String value = source.substring(start + 1, current - 1);
        addToken(TokenType.STRING, value);
    }

    private void number() {
        while (peek() != 0 && Character.isDigit(peek())) {
            advance();
        }
        String numberStr = source.substring(start, current);
        addToken(TokenType.NUMBER, Integer.parseInt(numberStr));
    }

    private void identifierOrKeyword() {
        while (peek() != 0 && (Character.isLetterOrDigit(peek()) || peek() == '_')) {
            advance();
        }

        String text = source.substring(start, current);

        // Проверка ключевых слов
        TokenType type = switch (text) {
            case "class" -> TokenType.CLASS;
            case "return" -> TokenType.RETURN;
            case "println" -> TokenType.PRINTLN;
            case "import" -> TokenType.IMPORT;
            case "calc" -> TokenType.CALC;
            case "value" -> TokenType.VALUE;
            default -> TokenType.IDENTIFIER;
        };

        // Если это не ключевое слово, то это идентификатор (например, имя поля)
        if (type == TokenType.IDENTIFIER) {
            addToken(type, text);
        } else {
            addToken(type);
        }
    }

    private char peek() {
        if (current >= source.length()) return '\0';
        return source.charAt(current);
    }

    private char advance() {
        char c = source.charAt(current++);
        if (c == '\n') line++;
        return c;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        tokens.add(new Token(type, source.substring(start, current), literal, line));
    }
}

    private void string() {
        // Ожидаем, что строка начинается с ["
        if (source.charAt(start) != '[' || (current < source.length() && source.charAt(current) != '"')) {
             // Если формат не совпадает, можно выбросить ошибку или обработать как ошибку
             throw new RuntimeException("Ожидался формат строки [\"...\"] на позиции " + start);
        }
        
        // Пропускаем '["'
        advance(); // пропускаем '['
        advance(); // пропускаем '"'

        // Читаем содержимое до '"]'
        while (!isAtEnd()) {
            if (peek() == '"' && (current + 1 < source.length()) && source.charAt(current + 1) == ']') {
                break;
            }
            if (peek() == '\n') line++;
            advance();
        }

        if (isAtEnd()) {
            throw new RuntimeException("Незакрытая строка.");
        }

        // Пропускаем '"]'
        advance(); // пропускаем '"'
        advance(); // пропускаем ']'

        String value = source.substring(start + 2, current - 2);
        addToken(TokenType.STRING, value);
    }
