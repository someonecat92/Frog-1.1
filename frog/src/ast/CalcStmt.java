package frog.ast;

import frog.interpreter.Environment;

public class CalcStmt extends Statement {
    private final String name;
    private final Expr expr;

    public CalcStmt(String name, Expr expr) {
        this.name = name;
        this.expr = expr;
    }

    @Override
    public void execute(Environment env) {
        Object result = expr.evaluate(env);
        env.setVariable(name, result);
        System.out.println("Вычислено " + name + " = " + result);
    }
}
