package linear;

import linear.FunctionParser;
import linear.FunctionSolver;

public class Main {
    public static void main(String []arg){
        FunctionParser functionParser=new FunctionParser();
        functionParser.makeFunctions();
        FunctionSolver functionSolver=functionParser.getFunctionSolver();
        functionSolver.setError(0.0000001);
        functionSolver.solve();
        functionSolver.print();
    }
}
