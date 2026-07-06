package frog.ast;

import frog.interpreter.Environment;

public class BinaryExpr extends Expr {
    public enum Op { ADD, SUB, MUL, DIV }

    private final Expr left;
    private final Op op;
    private final Expr right;

    public BinaryExpr(Expr left, Op op, Expr right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public Object evaluate(Environment env) {
        Object l = left.evaluate(env);
        Object r = right.evaluate(env);

        if (l instanceof Number && r instanceof Number) {
            double a = ((Number) l).doubleValue();
            double b = ((Number) r).doubleValue();
            switch (op) {
                case ADD: return a + b;
                case SUB: return a - b;
                case MUL: return a * b;
                case DIV: return b != 0 ? a / b : Double.NaN;
            }
        }
        throw new RuntimeException("Неподдерживаемая операция над типами: " + l.getClass() + " и " + r.getClass());
    }
}
