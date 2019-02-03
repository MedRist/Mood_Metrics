package parsers.mood;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MethodsSignatureVisitor extends VoidVisitorAdapter<Map<String,String>> {
    @Override
    public void visit(ClassOrInterfaceDeclaration classe, Map<String,String>collector) {

       classe.getMethods().stream()
               .filter(p->{
                   return !p.getModifiers().iterator().next().asString().equalsIgnoreCase("private")
                           && !p.asMethodDeclaration().isAnnotationPresent("Override");
               })
                .forEach(e->collector.put(e.getNameAsString(),e.getModifiers().iterator().next().asString()));



    }
}
