package parsers.mood;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.security.SecureRandom;

public class PushDownMethod {
    private CompilationUnit cu;
    private ClassOrInterfaceDeclaration classe;

    public PushDownMethod(CompilationUnit compilationUnit) {
        cu = compilationUnit;
        classe= cu.findAll(ClassOrInterfaceDeclaration.class).get(0);
    }

    public void refactor(MethodDeclaration methodDeclaration)
    {
        String methodeName = methodDeclaration.getNameAsString();
        classe.addMethod(methodeName, Modifier.PUBLIC,Modifier.STATIC)
                .setBody(methodDeclaration.getBody().get());
    }

    public CompilationUnit getCu() {
        return cu;
    }
}
