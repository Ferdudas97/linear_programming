

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.operator.Operator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FunctionParser {
    private ArrayList<Expression> limitations=new ArrayList<Expression>();
    private Operator equalOrGreaterThan;
    private Operator equalOrLessThan;
    private Operator greaterThan;
    private Operator lessThan;
    private Operator equal;
    public Set<String> variabes= new HashSet<String>();
    private  Expression optimizationFunction;
    private String optimization;


    {
        equal= new Operator("=", 2, true, Operator.PRECEDENCE_ADDITION - 1) {

            @Override
            public double apply(double[] values) {
                if (values[0] == values[1]) {
                    return 1d;
                } else {
                    return 0d;
                }

            }
        };
        equalOrGreaterThan = new Operator(">=", 2, true, Operator.PRECEDENCE_ADDITION - 1) {

            @Override
            public double apply(double[] values) {
                if (values[0] >= values[1]) {
                    return 1d;
                } else {
                    return 0d;
                }

            }
        };
        equalOrLessThan=new Operator("<=", 2, true, Operator.PRECEDENCE_ADDITION - 1) {

            @Override
            public double apply(double[] values) {
                if (values[0] <= values[1]) {
                    return 1d;
                } else {
                    return 0d;
                }

            }
        };
        lessThan=new Operator("<", 2, true, Operator.PRECEDENCE_ADDITION - 1) {

            @Override
            public double apply(double[] values) {
                if (values[0] < values[1]) {
                    return 1d;
                } else {
                    return 0d;
                }

            }
        };
        greaterThan=new Operator(">", 2, true, Operator.PRECEDENCE_ADDITION - 1) {

        @Override
        public double apply(double[] values) {
            if (values[0] > values[1]) {
                return 1d;
            } else {
                return 0d;
            }

        }
    };
    }


    public void makeFunctions(){
        int howmany;
        Scanner scanner= new Scanner(System.in);
        String answer;
        System.out.println("Podaj ilość zmiennych");
        howmany=scanner.nextInt();
        for (int i = 0; i <howmany ; i++) {
            System.out.println("podaj zmienna");
            answer=scanner.next();
            variabes.add(answer);
        }
        System.out.println("Podaj ilosć ograniczen");
        howmany=scanner.nextInt();
        for (int i = 0; i <howmany ; i++) {
            System.out.println("Podaj funkcje nr "+ (i+1));
             answer=scanner.next();
            limitations.add(makeFunction(answer));
            saveFunctionsToFile("functions",answer);

        }
        System.out.println("Podaj funckje optymalizująca");
        answer=scanner.next();
        optimizationFunction=makeFunction(answer);
        System.out.println("maks/min");
        optimization=scanner.next();


    }

    private void saveFunctionsToFile(String path,String function){
        try {
            FileWriter file = new FileWriter(path);
            file.write(function+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Expression makeFunction(String exp){
        ExpressionBuilder builder= new ExpressionBuilder(exp);
        builder.variables(variabes);
        if (exp.contains(">=")) builder.operator(equalOrGreaterThan);
        if (exp.contains("<=")) builder.operator(equalOrLessThan);
        if (exp.contains(">")) builder.operator(greaterThan);
        if (exp.contains("<")) builder.operator(lessThan);
        return builder.build();

    }

    public ArrayList<Expression> getFunctions() {
        return limitations;
    }
    static public void main(String [] arg){
       try{System.exit(0);}
       finally {
           System.out.println("Dasd");
       }

    }
    public FunctionSolver getFunctionSolver(){

        return new FunctionSolver(limitations,optimizationFunction,variabes,optimization);
    }
}
