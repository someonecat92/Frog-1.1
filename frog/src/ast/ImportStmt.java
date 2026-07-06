package frog.ast;

import frog.interpreter.Environment;

public class ImportStmt extends Statement {
    private final String path;

    public ImportStmt(String path) {
        this.path = path;
    }

    @Override
    public void execute(Environment env) {
        // Здесь будет логика загрузки и парсинга файла path
        System.out.println("Импорт: " + path);
        // TODO: вызвать Parser.parseFile(path) и добавить классы в Environment
    }
}
