package parsers.mood;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class NameFieldsVisitor extends VoidVisitorAdapter<List<String>> {
    @Override
    public void visit(ClassOrInterfaceDeclaration classe, List<String>collector) {

        collector.addAll(classe.getFields().stream()
                .map(p->p.getVariables().get(0).getName().asString())
                .collect(Collectors.toList()));

    }}

