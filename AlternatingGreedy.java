import java.util.Scanner;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.lang.Math;

class AlternatingGreedy{

    public static double distance(Robot one, Robot two){
        double x = Math.pow(one.getX() - two.getX(),2);
        double y = Math.pow(one.getY() - two.getY(),2);
        double d = Math.sqrt(x+y);
        return d;
    }


    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
    
        int numRobots = sc.nextInt();

        Robot[] robots = new Robot[numRobots];

        for(int i=0; i<numRobots; i++){
            double x = sc.nextDouble();
            double y = sc.nextDouble();
            robots[i] = new Robot(i,x,y);
        }

        ArrayList<TreeMap<Double,Robot>> adjList = 
            new ArrayList<TreeMap<Double,Robot>>(numRobots);

        int numDist = 0;
        for(int i=0; i<numRobots; i++){
            adjList.add(new TreeMap<Double,Robot>());
            for(int j=0; j<numRobots; j++){
                if(i!=j){
                    Double dist = new Double(distance(robots[i],robots[j]));
                    adjList.get(i).put(dist,robots[j]);
                }
            }
        }


        TreeSet<Robot> awakePath = new TreeSet<Robot>();
        robots[0].wakeUp();
        awakePath.add(robots[0]);
        int numAwake = 1;
        double lastTime = -1;

        while(numAwake < numRobots-1){
            Robot current = awakePath.pollFirst();
            Robot nextRobot;
            double dist;
            if(current.getTime() != lastTime){
                System.out.println("first");
                dist = adjList.get(current.getIndex()).firstKey();
                nextRobot = adjList.get(current.getIndex()).pollFirstEntry().getValue();
                while( nextRobot.isAwake() ){
                    dist = adjList.get(current.getIndex()).firstKey();
                    nextRobot = adjList.get(current.getIndex()).pollFirstEntry().getValue();
                }
                lastTime = current.getTime() + dist;
            } else {
                System.out.println("last");
                dist = adjList.get(current.getIndex()).lastKey();
                nextRobot = adjList.get(current.getIndex()).pollLastEntry().getValue();
                while( nextRobot.isAwake() ){
                    dist = adjList.get(current.getIndex()).lastKey();
                    nextRobot = adjList.get(current.getIndex()).pollLastEntry().getValue();
                }   
                lastTime = 0;
            }
            nextRobot.wakeUp();
            double time = current.getTime();
            current.setIndex(nextRobot.getIndex());
            current.setPosition(nextRobot.getX(),nextRobot.getY());
            current.setTime(time + dist);
            nextRobot.setTime(time + dist);
            awakePath.add(current);
            awakePath.add(nextRobot);
            numAwake ++;

        }

        System.out.println(awakePath.first().getTime());
    
    }
}
