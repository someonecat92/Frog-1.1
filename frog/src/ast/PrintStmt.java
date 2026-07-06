package frog.ast;

import frog.interpreter.Environment;

import java.util.List;

public class PrintStmt extends Statement {
    private final List<Expr> args;

    public PrintStmt(List<Expr> args) {
        this.args = args;
    }

    @Override
    public void execute(Environment env) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.size(); i++) {
            Object val = args.get(i).evaluate(env);
            sb.append(val);
            if (i < args.size() - 1) sb.append(" ");
        }
        System.out.println(sb.toString());
    }
}
