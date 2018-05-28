package linear;

public class Main {
    public static void main(String []arg){
        FunctionParser functionParser=new FunctionParser();
        functionParser.makeFunctionsFromJSON("function2.json");
        FunctionSolver functionSolver=functionParser.getFunctionSolver();
        functionSolver.setError(0.0000001);
        functionSolver.solve();
        functionSolver.print();
    }
}
