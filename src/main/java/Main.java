public class Main {
    public static void main(String []arg){
        FunctionParser functionParser=new FunctionParser();
        functionParser.makeFunctions();
        FunctionSolver functionSolver=functionParser.getFunctionSolver();
        functionSolver.solve();
        functionSolver.print();
    }
}
