package frog;

import frog.ast.Program;
import frog.interpreter.Interpreter;
import frog.parser.Lexer;
import frog.parser.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Точка входа интерпретатора языка Frog.
 * Читает program.frog, строит AST и выполняет программу.
 */
public class Main {
    public static void main(String[] args) {
        String filePath = "example.frog";

        try {
            // Читаем исходный код Frog из файла
            String source = Files.readString(Paths.get(filePath));

            // Лексический анализ: превращаем текст в токены
            Lexer lexer = new Lexer(source);
            var tokens = lexer.tokenize();

            // Синтаксический анализ: строим AST из токенов
            Parser parser = new Parser(tokens);
            Program program = parser.parse();

            // Выполняем AST
            Interpreter interpreter = new Interpreter();
            interpreter.run(program);

        } catch (IOException e) {
            System.err.println("Ошибка чтения файла '" + filePath + "': " + e.getMessage());
            e.printStackTrace();
        } catch (RuntimeException e) {
            System.err.println("Ошибка при разборе или выполнении программы: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

