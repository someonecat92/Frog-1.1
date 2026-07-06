package frog.ast;

import frog.interpreter.Environment;

public class StringLiteral extends Expr {
    private final String value;

    public StringLiteral(String value) {
        this.value = value;
    }

    @Override
    public Object evaluate(Environment env) {
        return value;
    }
}
