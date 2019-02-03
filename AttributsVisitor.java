package parsers.mood;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;
import java.util.stream.Collectors;

public class AttributsVisitor extends VoidVisitorAdapter<List<String>> {
@Override
public void visit(ClassOrInterfaceDeclaration classe, List<String>collector) {

        collector.addAll(classe.getFields().stream()
        .map(p->p.getModifiers().iterator().next().asString())
        .collect(Collectors.toList()));

        }
}
