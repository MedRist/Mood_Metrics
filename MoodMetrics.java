package parsers.mood;

import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;
import javafx.util.Pair;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;



public class MoodMetrics {
    private File file;
    private List<CompilationUnit> cus;


    public MoodMetrics(String path) throws IOException {
        File dir = new File(
                path);
        CombinedTypeSolver typeSolver = new CombinedTypeSolver(
                new ReflectionTypeSolver(),
                new JavaParserTypeSolver(dir));
        ParserConfiguration parserConfiguration =
                new ParserConfiguration()
                        .setSymbolResolver(new JavaSymbolSolver(typeSolver));

        SourceRoot sourceRoot = new
                SourceRoot(dir.toPath());
        sourceRoot.setParserConfiguration(parserConfiguration);
        List<ParseResult<CompilationUnit>> parseResults =
                sourceRoot.tryToParse("");


        // For computing the metrics, we need to have an access to all the classes.
        // @variable allCus = All computation Units of the packages, and foreach class we create an AST.
        this.cus = parseResults.stream()
                .filter(ParseResult::isSuccessful)
                .map(r -> r.getResult().get())
                .collect(Collectors.toList());
    }

    static final BiFunction<List<CompilationUnit>,String,CompilationUnit> findCompilationUnit = (compilationUnits, ss) -> {
        CompilationUnit cu =compilationUnits.stream()
                .filter(p->p.findAll(ClassOrInterfaceDeclaration.class)
                        .get(0).getNameAsString().equals(ss))
                .findAny()
                .orElse(null);
        return cu;
    };

    public double MHF(List<CompilationUnit> l){

     int countPrivates=   l.stream().mapToInt(p->MHF_Bis.apply(p).getValue()).sum();
        int countAllMethods=   l.stream().mapToInt(p->MHF_Bis.apply(p).getKey()).sum();
        return countPrivates/countAllMethods;
    }

    static final Function<CompilationUnit ,Pair<Integer,Integer>> MHF_Bis = new Function<CompilationUnit, Pair<Integer, Integer>>() {
        @Override
        public Pair<Integer, Integer> apply(CompilationUnit compilationUnit) {
            ModifiersVisitor m =  new ModifiersVisitor();
            List<String> list =  new LinkedList<>();
            m.visit(compilationUnit.findAll(ClassOrInterfaceDeclaration.class).get(0),list);
            Integer countPrivate = list.stream().filter(p->p.equalsIgnoreCase("private")).collect(Collectors.toList()).size();
            Integer countAll = list.size();
            return new Pair<Integer,Integer>(countPrivate,countAll);
        }
    };

    public double AHF(List<CompilationUnit> l){

        int countPrivates=   l.stream().mapToInt(p->MHF_Bis.apply(p).getValue()).sum();
        int countAllMethods=   l.stream().mapToInt(p->MHF_Bis.apply(p).getKey()).sum();
        return (double)countPrivates/countAllMethods;
    }

    static final Function<CompilationUnit ,Pair<Integer,Integer>> AHF_Bis = new Function<CompilationUnit, Pair<Integer, Integer>>() {
        @Override
        public Pair<Integer, Integer> apply(CompilationUnit compilationUnit) {
            AttributsVisitor m =  new AttributsVisitor();
            List<String> list =  new LinkedList<>();
            m.visit(compilationUnit.findAll(ClassOrInterfaceDeclaration.class).get(0),list);
            Integer countPrivate = list.stream().filter(p->p.equalsIgnoreCase("private")).collect(Collectors.toList()).size();
            Integer countAll = list.size();
            return new Pair<Integer,Integer>(countPrivate,countAll);
        }
    };


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static double AIF(List<CompilationUnit> l){

        int inhretedFildsAllClasses = l.stream()
                .mapToInt(p->inheretedFields.apply(l,p.findAll(ClassOrInterfaceDeclaration.class).get(0).getNameAsString()))
                .sum();
        List<String> fields = new LinkedList<>();
        for (CompilationUnit c : l )
        {
            NameFieldsVisitor fieldsNameVisitor = new NameFieldsVisitor();
            fieldsNameVisitor.visit(c.findAll(ClassOrInterfaceDeclaration.class).get(0),fields);

        }
        int allFieldsOfProjetc = fields.size();
        System.out.println(inhretedFildsAllClasses);
        System.out.println(allFieldsOfProjetc);
        return (double) inhretedFildsAllClasses/allFieldsOfProjetc;
    }

    public static  BiFunction<List<CompilationUnit>,String,Integer> inheretedFields = (compilationUnits, s) -> {
        CompilationUnit c = findCompilationUnit.apply(compilationUnits,s);
        ClassOrInterfaceDeclaration declaration = c.findAll(ClassOrInterfaceDeclaration.class).get(0);
        //////////////////////////////////////////////
        NonPrivateFields visitor = new NonPrivateFields();
        List<String> list = new LinkedList<>();
        visitor.visit(declaration,list);
        ///////////////////////////////////////////////
        NodeList<ClassOrInterfaceType> nodes =  c.getClassByName(s)
                .get()
                .getExtendedTypes();// Get the extended Types of the class
        if (nodes.isEmpty()) return 0;
        CompilationUnit motherCom = findCompilationUnit.apply(compilationUnits,nodes.get(0).asString());
        NameFieldsVisitor fieldsNameVisitor = new NameFieldsVisitor();
        List<String> motherClassFields = new LinkedList<>();
        fieldsNameVisitor.visit(motherCom.findAll(ClassOrInterfaceDeclaration.class).get(0),motherClassFields);
        ////////////////////////////////////////////////

        Integer inhFieds = list.stream()
                 .filter(p->motherClassFields.contains(p))
                 .collect(Collectors.toList())
                 .size();
        return inhFieds ;

    };

   ////////////////////////////////////////////////MIF//////////////////////////////////////////////////////

    public static double MIF(List<CompilationUnit> l){

        int   countAllMethods      =     l.stream().mapToInt(p->MIF_bis.apply(l,p.findAll(ClassOrInterfaceDeclaration.class).get(0).getNameAsString()).getValue()).sum();
        int  countInheritedMethods =     l.stream().mapToInt(p->MIF_bis.apply(l,p.findAll(ClassOrInterfaceDeclaration.class).get(0).getNameAsString()).getKey()).sum();
        return (double)countInheritedMethods/countAllMethods;
    }

    public static  BiFunction<List<CompilationUnit>,String,Pair<Integer,Integer>> MIF_bis = (compilationUnits, s) -> {
        CompilationUnit c = findCompilationUnit.apply(compilationUnits,s);
        ClassOrInterfaceDeclaration classe = c.findAll(ClassOrInterfaceDeclaration.class).get(0);
        //////////////////////////////////////////////
        MethodsSignatureVisitor md =new MethodsSignatureVisitor();
        Map<String,String> methods_Mother_CLass = new HashMap<>();

        ////////////////////Mother  CLass//////////////////////////////

        NodeList<ClassOrInterfaceType> nodes =  c.getClassByName(s)
                .get()
                .getExtendedTypes();// Get the extended Types of the class
        int inherted_methods=0;
        if (!nodes.isEmpty()){
            CompilationUnit motherCom = findCompilationUnit.apply(compilationUnits,nodes.get(0).asString());
            ClassOrInterfaceDeclaration mother_classe = motherCom.findAll(ClassOrInterfaceDeclaration.class).get(0);
            md.visit(mother_classe,methods_Mother_CLass);
            inherted_methods = methods_Mother_CLass.size();
        };


         int total_number_Of_methods  = classe.getMethods().size();
        return new Pair<Integer,Integer>(inherted_methods,total_number_Of_methods) ;

    };
}
