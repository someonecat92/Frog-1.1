package frog.ast;

import java.util.ArrayList;
import java.util.List;

public class ClassDef extends Node {
    public String name;
    public List<FieldAssignment> fields = new ArrayList<>();
    public List<Statement> statements = new ArrayList<>(); // вместо одного returnStmt
    public ReturnStmt returnStmt;
}
