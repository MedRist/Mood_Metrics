package parsers.mood;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import javafx.util.Pair;

import java.util.List;
import java.util.stream.Collectors;

public class ModifiersVisitor extends VoidVisitorAdapter<List<String>> {
    @Override
    public void visit(ClassOrInterfaceDeclaration classe, List<String>collector) {

             collector.addAll(classe.getMethods().stream()
                         .map(p->p.getModifiers().iterator().next().asString())
                         .collect(Collectors.toList()));

    }
}
