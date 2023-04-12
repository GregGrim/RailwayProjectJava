package railroad.rollingStock;

import railroad.exceptions.RailroadHazard;
import railroad.railwayMap.Connection;
import railroad.railwayMap.Station;
import railroad.RailroadWorld;

import java.util.List;

public class Locomotive {
    private enum Status {
        MOVING, ARRIVED, STARTING, SPAWNED
    }
    private Status status;
    private String name;
    private final Station homeStation;
    private Station sourceStation;

    private Station endStation;
    private Station destinationStation;

    private List<Station> route;

    private static int counter= 1;

    private RailroadWorld world;

    private boolean running;
    private double speed;
    private double distance;
    private Connection currentConnection;

    private int timeOnStation;

    public Locomotive (Station _homeStation,RailroadWorld _world) {
        world=_world;
        name="locomotive-"+(counter++);
        homeStation=_homeStation;
        sourceStation = homeStation;
        status = Status.SPAWNED;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getName() {
        return name;
    }

    public void setSourceStation(Station sourceStation) {
        this.sourceStation = sourceStation;
    }

    public void setEndStation(Station endStation) {
        this.endStation = endStation;
    }

    public void setDestinationStation(Station destinationStation) {

        this.destinationStation = destinationStation;
        route = world.computeRoute(sourceStation, destinationStation);
        System.out.println("computed route for "+this+" is "+route);
    }

    public List<Station> getRoute() {
        return route;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean onTheWay() {
        return currentConnection.onTheWay(this);
    }
    public void tryToStart () {
        if(route!=null) {
            if(onTheWay()) {
                distance = 0;
                speed = 50;
            } else System.out.println(currentConnection+" is occupied, "+this+" cannot move");
        }
    }
    public void setToQueue() {
        if(route!=null) {
            currentConnection  = world.getConnection(route.get(0), route.get(1));
            currentConnection.setToQueue(this);
        }
    }
    public void start() {
        setToQueue();
        tryToStart();
    }
    public void stop() {
        speed=0;
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public void move() {
        switch (status) { // 1. locomotive just spawned at home station
            case SPAWNED -> {
                System.out.println(this +" "+ status + " at " + route.get(0));
                start();
                if(onTheWay()) {
                    status = Status.MOVING;
                } else status = Status.STARTING;
            }

            case ARRIVED -> { // 2. locomotive just arrived to station
                System.out.println(this +" "+ status + " to " + route.get(1));
                currentConnection.removeFromQueue(this);
                speed = 0;
                timeOnStation = 0;
                route.remove(0);
                status = Status.STARTING;
            }

            case STARTING -> { // 3. locomotive is trying to start to the other station
                timeOnStation++;
                if (route.size() > 1) {
                    System.out.println(this+" "+status+" from "+route.get(0)+" to "+
                            route.get(1)+" Queue: "+currentConnection.getQueue());
                    if(timeOnStation>=2) start();
                } else {
                    System.out.println(this+" "+status+" from "+route.get(0)+" in opposite route");
                    if(timeOnStation > 30) {
                        sourceStation=destinationStation;
                        route.clear();
                        if (!destinationStation.equals(homeStation)) {
                            setDestinationStation(homeStation);
                            route = world.computeRoute(endStation, homeStation);
                        } else {
                            setDestinationStation(endStation);
                            route = world.computeRoute(homeStation, endStation);
                        }
                        start();
                    }
                }
                if(onTheWay()) {
                    status = Status.MOVING;
                }
            }

            case MOVING -> { // 1. locomotive is moving from station to station
                System.out.println(this+" "+status+" from "+route.get(0)+" to "+route.get(1)+" "+distance+" / "+currentConnection.getDistance());

                if (distance < currentConnection.getDistance()) {
                    distance += speed;
                    if (Math.random() > 0.5)
                        this.speed *= 1.03;
                    else this.speed /= 1.03;
                    if (speed > 200) throw new RailroadHazard(this);
                } else status = Status.ARRIVED;
            }

        }
    }
}


