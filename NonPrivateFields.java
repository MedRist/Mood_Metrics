package parsers.mood;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

import java.util.List;
import java.util.stream.Collectors;

public class NonPrivateFields extends VoidVisitorAdapter<List<String>> {
    @Override
    public void visit(ClassOrInterfaceDeclaration classe, List<String>collector) {

        ResolvedReferenceTypeDeclaration resolvedClass = classe.resolve();
        collector.addAll(resolvedClass.getAllFields().stream()
                .filter(f -> f.accessSpecifier() != AccessSpecifier.PRIVATE)
                .map(p->p.getName())
                .collect(Collectors.toList()))
                ;

    }
}
