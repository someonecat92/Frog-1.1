package frog.ast;

import frog.interpreter.Environment;

public abstract class Expr extends Node {
    public abstract Object evaluate(Environment env);
}
