package frog.ast;

import frog.interpreter.Environment;

public abstract class Statement extends Node {
    public abstract void execute(Environment env);
}
