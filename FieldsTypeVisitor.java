package parsers.mood;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import javafx.util.Pair;

import java.util.List;
import java.util.stream.Collectors;

public class FieldsTypeVisitor extends VoidVisitorAdapter<List<String>> {
    @Override
    public void visit(ClassOrInterfaceDeclaration classe, List<String>collection) {

        collection.addAll(classe.getFields()
                .stream()
                .map(p->p.getVariables().get(0).getType().asString())
                .collect(Collectors.toList()));
    }}

