package frog.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Корневой узел AST: программа состоит из списка классов.
 */
public class Program extends Node {
    public final List<ClassDef> classes = new ArrayList<>();
}
