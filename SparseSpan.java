import java.util.Scanner;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.lang.Math;

class SparseSpan{

    //TODO: Prove that this works

    public static void main(String[] args){
        Scanner sc = new  Scanner(System.in);

        int numRobots = sc.nextInt();

        Robot[] robots = new Robot[numRobots];

        for(int i=0; i<numRobots; i++){
            double x = sc.nextDouble();
            double y = sc.nextDouble();
            Robot r = new Robot(i,x,y);
            robots[i] = r;
        }

        TreeSet<Edge>  edges = new TreeSet<Edge>();

        for(int i=0; i<numRobots; i++){
            for(int j=0; j<numRobots; j++){
                if(i!=j){
                    Edge e = new Edge(robots[i],robots[j]);
                    edges.add(e);
                }
            }
        }

        
        double time = 0;
        robots[0].addNeighbor();


        while(edges.size()>0){
            Edge minEdge = edges.pollFirst();
            if(minEdge.getRobot1().getNeighbors() < 3 &&
                    minEdge.getRobot2().getNeighbors() < 3){
                

            }
        }


    }


}

class Edge implements Comparable<Edge>{

    private Robot robot1;
    private Robot robot2;
    private double weight;

    Edge(Robot r1, Robot r2){
        robot1 = r1;
        robot2 = r2;
        weight = Math.sqrt(Math.pow(r1.getX()-r2.getX(),2) + 
                Math.pow(r1.getY() - r2.getY(),2));
    }

    
    public Robot getRobot1(){
        return robot1;
    }

    public Robot getRobot2(){
        return robot2;
    }

    public double getWeight(){
        return weight;
    }

    public int compareTo(Edge other){
        return Double.compare(this.weight,other.weight);
    }
    
}
