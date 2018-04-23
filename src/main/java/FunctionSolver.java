import net.objecthunter.exp4j.Expression;

import java.awt.geom.Point2D;
import java.util.*;

public class FunctionSolver {
    private ArrayList<Expression> limitations;
    private Set<String> variables;
    private Random generator = new Random();
    private Expression optimizationFunction;
    private String optimization="max";
    private String integerOrDouble;
    private HashMap<String, Double> variablesAndDoubleValues=new HashMap<String, Double>();
    private Integer numberOfPoints = 1000000;
    private Double optimalValue=Double.MIN_VALUE;
    private Double previousOptimalValue=Double.MIN_VALUE;
    private Double radius=Math.sqrt(Double.MAX_VALUE);



    public FunctionSolver(ArrayList<Expression> limitations, Expression optimizationFunction, Set<String> variables,
                          String optimization, String integerOrDouble) {
        this.limitations = limitations;
        this.optimizationFunction = optimizationFunction;
        this.variables = variables;

        this.integerOrDouble = integerOrDouble;
    }

    private Boolean solve( HashMap<String, Double> variables) {

        Double sum = 0.0;
        for (int i = 0; i < limitations.size(); i++) {
            sum += limitations.get(i).setVariables(variables).evaluate();
        }
        return sum==limitations.size();

    }
    public void solve(){
        Double[] startingPoint=new Double[variables.size()];
        Arrays.fill(startingPoint,0.0);
        monteCarlo(startingPoint,radius);
    }

    private void monteCarlo(Double[] previousPoint, Double radius) {
        Double[] newPoint=new Double[previousPoint.length];
        HashMap<String, Double> localPoint = new HashMap<String, Double>();
        Double point;
        int iterator=0;
        double randomed;
        if (radius<1) radius=0.99;
        previousOptimalValue=optimalValue;
        for (int j = 0; j < numberOfPoints; j++) {
            iterator=0;

            for (String var:variables) {
                randomed=generator.nextInt()%radius;
                if (randomed%2==0) point = previousPoint[iterator] + randomed;
                else point = previousPoint[iterator] - randomed;
                localPoint.put(var,point);
                iterator++;
            }
            if (solve(localPoint)) {
                /*for (Map.Entry<String,Double> en:localPoint.entrySet()) {
                    System.out.println(en.getKey()+":  "+en.getValue());
                }*/
               if(isMaxOrMin((HashMap<String,Double>)localPoint.clone())) newPoint=(Double[])localPoint.values().toArray(newPoint);
            }
         localPoint.clear();
        }
        System.out.println(optimalValue);
        System.out.println("Radius "+radius);
        if (Arrays.equals(newPoint,new Double[newPoint.length])) newPoint=previousPoint;
        if (optimalValue==Double.MIN_VALUE ) monteCarlo(newPoint,Math.sqrt(radius));
        if (Math.abs(optimalValue-previousOptimalValue)>=0.0000001) monteCarlo(newPoint,radius/2);


    }

    private boolean isMaxOrMin(HashMap<String, Double> variables){
        Double value=optimizationFunction.setVariables(variables).evaluate();
        if (optimization.equals("min") && value < optimalValue) {
            optimalValue=value;
            this.variablesAndDoubleValues=variables;
            System.out.println("tat");
            return true;

        }
     //   System.out.println(value);
        if (optimization.equals("max") && value>optimalValue){
            optimalValue=value;

            this.variablesAndDoubleValues=variables;
            return true;
        }
        return false;
    }
    public void print(){

        for (Map.Entry<String,Double> en:variablesAndDoubleValues.entrySet()) {
            System.out.println(en.getKey()+":  "+en.getValue());
        }
    }


}
