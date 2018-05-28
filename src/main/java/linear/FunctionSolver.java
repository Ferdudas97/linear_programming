package linear;

import net.objecthunter.exp4j.Expression;

import java.util.*;

public class FunctionSolver<T> {
    private ArrayList<Expression> limitations;
    private Set<String> variables;
    private Random generator = new Random();
    private Expression optimizationFunction;
    private String optimization="max";
    private HashMap<String, Double> variablesAndValues =new HashMap<String, Double>();
    private Integer numberOfPoints = 1000000;
    private Double optimalValue=Double.MIN_VALUE;
    private Double previousOptimalValue=Double.MIN_VALUE;
    private Double radius=100000000.0;
    private Double error=0.000001;



    public FunctionSolver(ArrayList<Expression> limitations, Expression optimizationFunction, Set<String> variables,
                          String optimization) {
        this.limitations = limitations;
        this.optimizationFunction = optimizationFunction;
        this.variables = variables;
        this.optimization=optimization;
        if (optimization.equals("max")){
            optimalValue=Double.MIN_VALUE;
            previousOptimalValue=Double.MIN_VALUE;
        }
        else {
            optimalValue=Double.MAX_VALUE;
            previousOptimalValue=Double.MAX_VALUE;
        }

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
        if (radius<50) radius=9.99;
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
    //    if (optimalValue==Double.MIN_VALUE || optimalValue==Double.MAX_VALUE ) monteCarlo(newPoint,Math.sqrt(radius));
        if ((Math.abs(optimalValue-previousOptimalValue)==0 && radius>9.99) ||Math.abs(optimalValue-previousOptimalValue)>=error) monteCarlo(newPoint,radius/2);


    }

    private boolean isMaxOrMin(HashMap<String, Double> variables){
        Double value=optimizationFunction.setVariables(variables).evaluate();
        if (optimization.equals("min") && value < optimalValue) {
            optimalValue=value;
            this.variablesAndValues =variables;
            return true;

        }
     //   System.out.println(value);
        if (optimization.equals("max") && value>optimalValue){
            optimalValue=value;

            this.variablesAndValues =variables;
            return true;
        }
        return false;
    }
    public void print(){

        for (Map.Entry<String,Double> en: variablesAndValues.entrySet()) {
            System.out.println(en.getKey()+":  "+en.getValue());
        }
    }

    public void setError(Double error) {
        this.error = error;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public void setNumberOfPoints(Integer numberOfPoints) {
        this.numberOfPoints = numberOfPoints;
    }

    public Double getOptimalValue() {
        return optimalValue;
    }

    public HashMap<String, Double> getVariablesAndValues() {
        return variablesAndValues;
    }
}
