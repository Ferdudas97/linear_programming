import net.objecthunter.exp4j.Expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class FunctionSolver {
    private ArrayList<Expression> limitations;
    private ArrayList<String> variables;
    private Random generator = new Random();
    private Expression optimizationFunction;
    private String optimization;
    private String integerOrDouble;
    private HashMap<String, Double> variablesAndDoubleValues;
    private Integer numberOfPoints = 10000;
    private Double optimalValue=Double.MIN_VALUE;
    private Double previousOptimalValue=Double.MIN_VALUE;

    public FunctionSolver(ArrayList<Expression> limitations, Expression optimizationFunction, ArrayList<String> variables,
                          String optimization, String integerOrDouble) {
        this.limitations = limitations;
        this.optimizationFunction = optimizationFunction;
        this.variables = variables;
        this.optimization = optimization;
        this.integerOrDouble = integerOrDouble;
    }

    private Boolean solve( HashMap<String, Double> variables) {

        Double sum = 0.0;
        for (int i = 0; i < limitations.size(); i++) {
            sum += limitations.get(i).setVariables(variables).evaluate();
        }
        if (sum.equals(limitations.size())) return true;
        return false;

    }


    private void monteCarlo(Double[] previousPoint, Double radius) {
        Double[] newPoint = new Double[previousPoint.length];
        HashMap<String, Double> localPoint = new HashMap<String, Double>();
        Double point;


        for (int j = 0; j < numberOfPoints; j++) {
            for (String var:variables) {

                point = generator.nextDouble() / radius;
                localPoint.put(var,point);
            }
            if (solve(localPoint)) {
                isMaxOrMin(localPoint);
                newPoint=(Double[]) localPoint.values().toArray();
            }

        }
        if (Math.abs(optimalValue-previousOptimalValue)<1 || radius<1)
        monteCarlo(newPoint,radius/2);



    }

    private void isMaxOrMin(HashMap<String, Double> variables){
        Double value=optimizationFunction.setVariables(variables).evaluate();
        if (optimization.equals("min") && value < optimalValue) {
            previousOptimalValue=optimalValue;
            optimalValue=value;
            this.variablesAndDoubleValues=variables;

        }
        if (optimization.equals("max") && value>optimalValue){
            previousOptimalValue=optimalValue;
            optimalValue=value;
            this.variablesAndDoubleValues=variables;
        }
    }


}
