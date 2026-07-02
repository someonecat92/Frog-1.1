package frog.parser;

import frog.ast.*;
import frog.parser.Token;
import frog.parser.Lexer;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int position = 0;

    public Parser(String source) {
        Lexer lexer = new Lexer(source);
        this.tokens = lexer.tokenize();
    }

    public Program parse() {
        List<ClassDef> classes = new ArrayList<>();
        while (!isAtEnd()) {
            if (match(TokenType.CLASS)) {
                classes.add(parseClass());
            } else {
                advance(); // Пропускаем токены до следующего ключевого слова
            }
        }
        return new Program(classes);
    }

    private ClassDef parseClass() {
        Token nameToken = expect(TokenType.IDENTIFIER, "Ожидается имя класса после 'class'");
        String className = nameToken.lexeme;

        expect(TokenType.LEFT_BRACE, "Ожидается '{' после имени класса");

        ClassDef classDef = new ClassDef();
        classDef.name = className;

        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            if (match(TokenType.RETURN)) {
                classDef.returnStmt = parseReturnStatement();
            } else if (match(TokenType.PRINTLN)) {
                classDef.statements.add(parsePrintlnStatement());
            } else if (match(TokenType.IMPORT)) {
                classDef.statements.add(parseImportStatement());
            } else if (match(TokenType.CALC)) {
                classDef.statements.add(parseCalcStatement());
            } else {
                // Это поле: key = ["value"]
                parseFieldAssignment(classDef);
            }
        }

        expect(TokenType.RIGHT_BRACE, "Ожидается '}' для завершения класса");
        return classDef;
    }

    private void parseFieldAssignment(ClassDef classDef) {
        Token keyToken = expect(TokenType.IDENTIFIER, "Ожидается имя поля");
        String key = keyToken.lexeme;

        expect(TokenType.EQUAL, "Ожидается '=' после имени поля");

        Token valueToken = expect(TokenType.STRING, "Ожидается строковый литерал в формате [\"...\"]");
        String value = valueToken.literal.toString();

        FieldAssignment assignment = new FieldAssignment();
        assignment.key = key;
        assignment.value = value;
        classDef.fields.add(assignment);
    }

    private ReturnStmt parseReturnStatement() {
        expect(TokenType.EQUAL, "Ожидается '=' после return");
        expect(TokenType.VALUE, "Ожидается ключевое слово 'value'");
        
        Token delayToken = expect(TokenType.STRING, "Ожидается значение задержки в квадратных скобках");
        String delayStr = delayToken.literal.toString();
        
        int delay = 0;
        try {
            delay = Integer.parseInt(delayStr);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Некорректное значение задержки: " + delayStr);
        }

        ReturnStmt stmt = new ReturnStmt();
        stmt.delayMs = delay;
        return stmt;
    }

    private PrintStmt parsePrintlnStatement() {
        expect(TokenType.EQUAL, "Ожидается '=' после println");
        expect(TokenType.LEFT_BRACKET, "Ожидается '[' для списка аргументов");

        List<Expr> args = new ArrayList<>();
        if (!check(TokenType.RIGHT_BRACKET)) {
            args.add(parseExpression());
            while (match(TokenType.COMMA)) {
                args.add(parseExpression());
            }
        }

        expect(TokenType.RIGHT_BRACKET, "Ожидается ']' для завершения списка аргументов");

        return new PrintStmt(args);
    }

    private ImportStmt parseImportStatement() {
        expect(TokenType.EQUAL, "Ожидается '=' после import");
        expect(TokenType.LEFT_BRACKET, "Ожидается '[' для пути импорта");
        
        Token pathToken = expect(TokenType.STRING, "Ожидается путь к файлу в формате [\"...\"]");
        String path = pathToken.literal.toString();
        
        expect(TokenType.RIGHT_BRACKET, "Ожидается ']' для завершения пути импорта");

        return new ImportStmt(path);
    }

    private CalcStmt parseCalcStatement() {
        expect(TokenType.EQUAL, "Ожидается '=' после calc");
        expect(TokenType.LEFT_BRACKET, "Ожидается '[' для аргументов calc");

        Token nameToken = expect(TokenType.IDENTIFIER, "Ожидается имя переменной для результата");
        String name = nameToken.lexeme;

        expect(TokenType.COMMA, "Ожидается запятая между именем и выражением");

        Expr expr = parseExpression();

        expect(TokenType.RIGHT_BRACKET, "Ожидается ']' для завершения calc");

        return new CalcStmt(name, expr);
    }

    // --- Парсинг выражений (рекурсивный спуск) ---

    private Expr parseExpression() {
        return parseAddition();
    }

    private Expr parseAddition() {
        Expr left = parseMultiplication();

        while (match(TokenType.PLUS) || match(TokenType.MINUS)) {
            TokenType op = previous().type;
            Expr right = parseMultiplication();
            
            BinaryExpr.Op bop = (op == TokenType.PLUS) ? BinaryExpr.Op.ADD : BinaryExpr.Op.SUB;
            left = new BinaryExpr(left, bop, right);
        }

        return left;
    }

    private Expr parseMultiplication() {
        Expr left = parsePrimary();

        while (match(TokenType.STAR) || match(TokenType.SLASH)) {
            TokenType op = previous().type;
            Expr right = parsePrimary();
            
            BinaryExpr.Op bop = (op == TokenType.STAR) ? BinaryExpr.Op.MUL : BinaryExpr.Op.DIV;
            left = new BinaryExpr(left, bop, right);
        }

        return left;
    }

    private Expr parsePrimary() {
        if (match(TokenType.NUMBER)) {
            return new NumberLiteral((Integer) previous().literal);
        }
        if (match(TokenType.STRING)) {
            return new StringLiteral((String) previous().literal);
        }
        if (match(TokenType.LEFT_BRACE)) {
            Expr expr = parseExpression();
            expect(TokenType.RIGHT_BRACE, "Ожидается ')'");
            return expr;
        }

        throw new RuntimeException("Неожиданный токен: " + peek());
    }

    // --- Вспомогательные методы ---

    private boolean match(TokenType type) {
        if (check(type)) {
            advance();
            return true;
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return tokens.get(position).type == type;
    }

    private Token advance() {
        if (!isAtEnd()) position++;
        return previous();
    }

    private Token previous() {
        return tokens.get(position - 1);
    }

    private boolean isAtEnd() {
        return check(TokenType.EOF);
    }

    private Token expect(TokenType type, String message) {
        if (check(type)) return advance();
        throw new RuntimeException(message + " (найдено: " + peek().type + ")");
    }

    private Token peek() {
        return tokens.get(position);
    }
}
