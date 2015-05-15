/** File: AlternatingGreedy.java
 * Author: Isaac Banner
 *         Rachel Silva
 *
 * Given an input of coordinates, preceded by the number of coordinates,
 * program will print the 'time' to 'wake' all robots at coordinates,
 * starting with the first listed coordinate, by choosing the closest
 * coordinate for the first robot leaving a coordinate, and the farthest
 * coordinate for the second robot leaving the same coordinate.
 *
 * Running time: O(n^2)
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.PriorityQueue;
import java.lang.Math;
import java.io.*;

class AlternatingGreedy{

    public static double distance(Robot one, Robot two){
        double x = Math.pow(one.getX() - two.getX(),2);
        double y = Math.pow(one.getY() - two.getY(),2);
        return Math.sqrt(x+y);
    }


    public static void main(String[] args){

        // Import set of points from file
        if(args.length != 1){
            System.out.println("Program requires exactly one argument.");
            System.exit(2);
        }

        Scanner sc = null;
        try{
            sc = new Scanner(new File(args[0]));
        } catch (FileNotFoundException e){
            System.out.println(e.getMessage());
            System.exit(2);
        }
    
        int numRobots = sc.nextInt();

        Robot[] robots = new Robot[numRobots];

        for(int i=0; i<numRobots; i++){
            double x = sc.nextDouble();
            double y = sc.nextDouble();
            robots[i] = new Robot(i,x,y);
        }

        ArrayList<TreeMap<Double,Robot>> adjList = 
            new ArrayList<>(numRobots);

        for(int i=0; i<numRobots; i++){
            adjList.add(new TreeMap<>());
            for(int j=0; j<numRobots; j++){
                if(i!=j){

                    Double dist = distance(robots[i],robots[j])+(robots[j].getIndex()*(0.0000001));
                    adjList.get(i).put(dist, robots[j]);
                }
            }
        }

        long startTime = System.currentTimeMillis();


        PriorityQueue<Robot> awakePath = new PriorityQueue<>();
        robots[0].wakeUp();
        awakePath.add(robots[0]);
        int numAwake = 1;

        while(numAwake < numRobots){
            Robot current = awakePath.poll();
            Robot nextRobot;
            double dist;
            if(current.getCount() == 0){
                dist = adjList.get(current.getIndex()).firstKey();
                nextRobot = adjList.get(current.getIndex()).pollFirstEntry().getValue();
                while( nextRobot.isAwake() ){
                    dist = adjList.get(current.getIndex()).firstKey();
                    nextRobot = adjList.get(current.getIndex()).pollFirstEntry().getValue();
                }
            } else {
                dist = adjList.get(current.getIndex()).lastKey();
                nextRobot = adjList.get(current.getIndex()).pollLastEntry().getValue();
                while( nextRobot.isAwake() ){
                    dist = adjList.get(current.getIndex()).lastKey();
                    nextRobot = adjList.get(current.getIndex()).pollLastEntry().getValue();
                }   
            }
            nextRobot.wakeUp();
            double time = current.getTime();
            System.out.println(current.getIndex()+ " " + nextRobot.getIndex());
            current.setIndex(nextRobot.getIndex());
            current.setPosition(nextRobot.getX(), nextRobot.getY());
            current.setTime(time + dist);
            current.incCount();
            nextRobot.setTime(time + dist);
            awakePath.add(current);
            awakePath.add(nextRobot);
            numAwake ++;

        }

        Robot lastRobot = awakePath.poll();
        while(awakePath.size() > 1) {
            lastRobot = awakePath.poll();
        }
        System.out.println(lastRobot.getTime());
        long endTime = System.currentTimeMillis();
        System.out.println("Runtime for AltGreed : " + (endTime - startTime) + " milliseconds");
    }
}
