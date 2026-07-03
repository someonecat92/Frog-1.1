package frog.interpreter;

import frog.ast.*;

public class Interpreter {
    private final Environment env = new Environment();

    public void run(Program program) {
        for (ClassDef cls : program.classes) {
            // Сначала выполняем операторы (println, import, calc)
            for (Statement stmt : cls.statements) {
                stmt.execute(env);
            }
            // Затем выполняем return (если есть)
            if (cls.returnStmt != null) {
                cls.returnStmt.execute(env);
            }
        }
    }
}
