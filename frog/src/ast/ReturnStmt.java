package frog.ast;

import frog.interpreter.Environment;

/**
 * Оператор return в Frog: return = value["1000"]
 * Выводит значение и делает задержку (из комментария или значения).
 */
public class ReturnStmt extends Statement {
    private final String value;      // значение (например, "1000")
    private final int delayMs;      // задержка в миллисекундах

    public ReturnStmt(String value, int delayMs) {
        this.value = value;
        this.delayMs = delayMs;
    }

    @Override
    public void execute(Environment env) {
        System.out.println("Returning: " + value);
        if (delayMs > 0) {
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                // Восстанавливаем флаг прерывания, чтобы не потерять его
                Thread.currentThread().interrupt();
            }
        }
    }
}
