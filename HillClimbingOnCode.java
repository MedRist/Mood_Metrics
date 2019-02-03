package parsers.mood;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.javaparser.ast.CompilationUnit;

import parsers.Solution;

public class HillClimbingOnCode {
    private Solution next;
    private Solution best;
    private int iter; //max number of iterations
    public HillClimbingOnCode(int iter,String fileName){
        this.iter = iter;
        try {
            best = new CodeSolution(fileName); // init our entry point, since we deal with code, we dont have to randomly generate
        } catch (IOException e) {
            e.printStackTrace();
        }
        // initial solution.
    }
    public void findSolution(){
        int i = 0;
        do{
            next = best.tweak();
            if(next.value()<best.value())
                best = next;
        }while(i<iter);
    }
    /**
     * implementation of the Solution Interface to fit our problem
     *
     */
    private class CodeSolution implements Solution{
        QMoodMetrics m;
        CompilationUnit cu;
        public CodeSolution(String fileName) throws IOException {
            try {
                m = new QMoodMetrics(fileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        /**
         * randomly tweak the code to attempt improving the results
         */
        @Override
        public Solution tweak() {
// TODO implement tweak method on code here
// return new solution
            //size of the list of CUs
            List<String> nameOfClasses = QMoodMetrics.nameOfClasses.apply(m.getCus());
            // we will use the  Push Down Method
            // Push Down Method moves a method from some class to
            //those subclasses that require it. This refactoring is intended to simplify the design by reducing the size of
           // class interfaces
            PushDownMethod pushDownMethod =  new PushDownMethod(m.getCus().get(0));
            return null;
        }

        @Override
        public double value() {
            return m.understandability();
        }
    }
}