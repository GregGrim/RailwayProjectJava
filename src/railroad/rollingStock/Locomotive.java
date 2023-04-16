package railroad.rollingStock;

import railroad.DebugMsg;
import railroad.exceptions.RailroadHazard;
import railroad.railwayMap.Connection;
import railroad.railwayMap.Station;
import railroad.RailroadWorld;
import java.util.List;
import static java.lang.Thread.sleep;

/**
 * Basic railroad world class that implements main part of train movement simulation
 */
public class Locomotive {
    private enum Status { // all possible statuses of train in world
        MOVING, ARRIVED, STARTING, SPAWNED, PREPARING_BACK
    }
    private Status status;
    private final String name;
    private final Station homeStation;
    private Station sourceStation;
    private Station endStation;
    private Station destinationStation;
    private List<Station> route;
    private int routeDistance;
    private int routeDistancePassed;
    private static int counter= 1;
    private final RailroadWorld world;
    private boolean running;
    private double speed;
    private int distance;
    private Connection currentConnection;
    private boolean shouldRecalc;
    public Locomotive (Station _homeStation,RailroadWorld _world) {
        world=_world;
        name="locomotive-"+(counter++);
        homeStation=_homeStation;
        sourceStation = homeStation;
        routeDistance=0;
        routeDistancePassed=0;
        status = Status.SPAWNED;
    }
    @Override
    public String toString() {
        return getName();
    }
    public String getName() {
        return name;
    }
    public Station getDestinationStation() {
        return destinationStation;
    }
    public int getRouteDistancePassed() {
        return routeDistancePassed;
    }
    public int getRouteDistance() {
        return routeDistance;
    }

    /**
     * calculate route distance for this loco
     * @return route distance for this loco
     */
    public int calculateRouteDistance () {
        int dist = 0;
        for (int i = 0; i < route.size()-1; i++) {
            dist+=world.getConnection(route.get(i), route.get(i+1)).getDistance();
        }
        return dist;
    }

    /**
     * checks if particular connection is in route
     * @param connection connection to check
     * @return boolean
     */
    public boolean hasConnectionInRoute(Connection connection){
        for (int i = 0; i < route.size()-1; i++) {
           Connection con = new Connection(route.get(i), route.get(i+1));
           if (connection.equals(con)){
               return true;
           }
        }
        return false;
    }
    public Station getHomeStation() {
        return homeStation;
    }
    public Connection getCurrentConnection() {
        return currentConnection;
    }
    public int getDistance() {
        return distance;
    }
    public Trainset getTrainset() {
        return world.getTrains().get(this);
    }
    public Status getStatus() {
        return status;
    }
    public void setShouldRecalc(boolean shouldRecalc) {
        this.shouldRecalc = shouldRecalc;
    }
    public void setEndStation(Station endStation) {
        this.endStation = endStation;
    }
    public void setDestinationStation(Station destinationStation) {
        this.destinationStation = destinationStation;
        route = world.computeRoute(sourceStation, destinationStation);
        DebugMsg.msg("computed route for "+this+" is "+route);
    }
    public List<Station> getRoute() {
        return route;
    }
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * checks if it`s time for loco to start from station(1st in queue)
     * @return boolean
     */
    public boolean onTheWay() {
        return currentConnection.onTheWay(this);
    }

    /**
     * trying to start(checks queue and route)
     */
    public void tryToStart () {
        if(route!=null) {
            if(onTheWay()) {
                distance = 0;
                speed = 180;
            }// else System.out.println(currentConnection+" is occupied, "+this+" cannot move");
        }
    }

    /**
     * setting loco to queue of current connection  (if it's not done yet)
     */
    public void setToQueue() {
        if(route!=null) {
            currentConnection  = world.getConnection(route.get(0), route.get(1));
            currentConnection.setToQueue(this);
        }
    }

    /**
     * function to start train movement from station
     */
    public void start() {
        setToQueue();
        tryToStart();
    }

    /**
     * function to stop train movement and kill threads
     */
    public void stop() {
        if(route!=null) currentConnection.getQueue().remove(this);
        speed=0;
        running = false;
    }
    public boolean isRunning() {
        return running;
    }

    /**
     * start general train movement by creating threads(each thread is calling functions depending on train status)
     * @param destinationStation direction
     */
    public void startTrain(Station destinationStation) {
        setEndStation(destinationStation);
        setDestinationStation(destinationStation);
        setRunning(true);

        new Thread(() -> { // run thread to implement spawned logic
            while(isRunning()) {
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (status == Status.SPAWNED) {
                    mSpawned();
                }
            }
        }).start();

        new Thread(() -> { // run thread to implement arrived logic
            while(isRunning()) {
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (status == Status.ARRIVED) {
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    mArrived();
                }
            }
        }).start();

        new Thread(() -> { // run thread to implement starting logic
            while(isRunning()) {
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (status == Status.STARTING) {
                    mStarting();
                }
            }
        }).start();

        new Thread(() -> { // run thread to implement preparing back journey logic
            while(isRunning()) {
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (status == Status.PREPARING_BACK) {
                    try {
                        sleep(30000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    mPreparingBack();
                }
            }
        }).start();

        new Thread(() -> { // run thread to implement moving logic
            while(isRunning()) {
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (status == Status.MOVING) {
                    try {
                        sleep(1000);
                        mMoving();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (RailroadHazard e) {
                        DebugMsg.errMsg(e.getMessage());
                        speed-=20;
                    }
                }
            }
        }).start();
    }
    /**
     * function for locomotive that just spawned at home station (status: SPAWNED)
     * starts or trying to start its movement depending on queue
     * calculating distance
     * delegates train to thread with status STARTING
     */
    public synchronized void mSpawned() {
         DebugMsg.msg(this +" "+ status + " at " + route.get(0));
         start();
         routeDistance=calculateRouteDistance();
         status = Status.STARTING;
    }

    /**
     * function for locomotive that just arrived to station
     * removes it from passed connection queue
     * sets speed zero
     * removes previous station from route
     * depending on remained route delegates train to tread with status STARTING or with status PREPARING_BACK
     */
    public synchronized void mArrived() {
        DebugMsg.msg(this +" "+ status + " to " + route.get(1));
        currentConnection.removeFromQueue(this);
        speed = 0;
        route.remove(0);
        if (route.size() > 1) {
            status = Status.STARTING;
        } else {
            status= Status.PREPARING_BACK;
            DebugMsg.msg(this+" "+status+" from "+route.get(0)+" in opposite direction");
        }
    }

    /**
     * function for locomotive that is trying to start to the other station on the route
     * if connection from route was deleted -> recalculates route
     * starts or trying to start its movement depending on queue
     * if starts -> delegates train to thread with status MOVING
     */
    public synchronized void mStarting() {
        if(shouldRecalc) {
            route = world.computeRoute(route.get(0), destinationStation);
            DebugMsg.msg("computed route for "+this+" is "+route);
            shouldRecalc=false;
        }
        start();
        if(onTheWay()) {
            DebugMsg.msg(this+" "+status+" from "+route.get(0)+" to "+
                    route.get(1)+" Queue: "+currentConnection.getQueue());
            status = Status.MOVING;
        }
    }

    /**
     * function for locomotive that finished its route and preparing for back journey
     * recalculate route in opposite direction
     * delegates train to thread with status STARTING
     */
    public synchronized void mPreparingBack() {
        sourceStation = destinationStation;
        routeDistancePassed=0;
        route.clear();
        if (!destinationStation.equals(homeStation)) {
            setDestinationStation(homeStation);
            route = world.computeRoute(endStation, homeStation);
        } else {
            setDestinationStation(endStation);
            route = world.computeRoute(homeStation, endStation);
        }
        routeDistance=calculateRouteDistance();
        status=Status.STARTING;
    }

    /**
     * function for locomotive that is moving from station to station
     * if connection distance passed delegates train to thread with status ARRIVED
     * else keep moving
     * @throws RailroadHazard if speed reaches 200km/h
     */
    public synchronized void mMoving() throws RailroadHazard{
        DebugMsg.msg(this+" "+status+" from "+route.get(0)+
                " to "+route.get(1)+" "+
                Math.min(distance, currentConnection.getDistance())+"/"+currentConnection.getDistance());
        if (distance < currentConnection.getDistance()) {
            routeDistancePassed+=(distance+speed<currentConnection.getDistance())?
                    speed:currentConnection.getDistance()-distance;
            distance += speed;
            if (Math.random() > 0.5)
                this.speed *= 1.03;
            else this.speed /= 1.03;
            if (speed > 200) throw new RailroadHazard(this);
        } else status = Status.ARRIVED;
    }
}


