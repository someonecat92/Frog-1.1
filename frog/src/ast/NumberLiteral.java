package frog.ast;
import frog.interpreter.Environment;
public class NumberLiteral extends Expr {
    private final int value;
    public NumberLiteral(int value) { this.value = value; }
    @Override public Object evaluate(Environment env) { return value; }
}
