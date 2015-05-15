class Robot implements Comparable<Robot>{

    private int name;
    private int index;
    private double x;
    private double y;
    private double time;
    private boolean awake;
    private int neighbors;


    Robot(int index,double xCoord,double yCoord){
        name = index;
        this.index = name;
        x = xCoord;
        y = yCoord;
        time = 0;
        awake = false;
    }

    public int getIndex(){
        return index;
    }

    public void setIndex(int newIndex){
        index = newIndex;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public void setPosition(double newX, double newY){
        x = newX;
        y = newY;
    }

    public double getTime(){
        return time;
    }

    public void setTime(double newTime){
        time = newTime;
    }

    public boolean isAwake(){
        return awake;
    }

    public boolean wakeUp(){
        if(awake){
            return false;
        }
        awake = true;
        return true;
    }

    public int getNeighbors(){
        return neighbors;
    }

    public void addNeighbor(){
        neighbors++;
    }

    public int compareTo(Robot other){
        return Double.compare(this.time,other.time);
    }

    public boolean equals(Robot other){
        return (this.name == other.name);
    }
}
