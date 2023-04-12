package railroad.railwayMap;

import railroad.rollingStock.Locomotive;
import railroad.rollingStock.Trainset;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Connection {
    private Station stationA;
    private Station stationB;
    private int distance;

    private Queue<Locomotive> queue = new ConcurrentLinkedQueue<>();

    public Connection(Station stationA, Station stationB, int distance) {
        this.distance=distance;
        this.stationA=stationA;
        this.stationB=stationB;
    }

    public Queue<Locomotive> getQueue() {
        return queue;
    }

    public void setToQueue(Locomotive locomotive) {

        if(!queue.contains(locomotive)) {
            System.out.println("set to queue "+locomotive);
            queue.offer(locomotive);
        }
    }
    public void removeFromQueue(Locomotive locomotive) throws RuntimeException{
        if(!locomotive.equals(queue.poll())) {
            throw new RuntimeException("error!");
        }
    }
    public boolean onTheWay(Locomotive locomotive) {
        return locomotive.equals(queue.peek());
    }
    @Override
    public String toString() {
        return "Connection{"+ stationA.getName() +" - "+ stationB.getName() +
                "} queue: "+queue;
    }

    public Station connectTo (Station station) {
        if(station.equals(stationA)) return stationB;
        if(station.equals(stationB)) return stationA;
        return null;
    }

    public boolean isConnected (Station station) {
        return connectTo(station)!=null;
    }

    @Override
    public boolean equals(Object connection) {
        if (connection == null) {
            return false;
        }
        if(connection instanceof Connection) {
            Connection c = (Connection) connection;
            return  this.stationA.equals(c.stationA)&&this.stationB.equals(c.stationB)||
                    this.stationA.equals(c.stationB)&&this.stationB.equals(c.stationA);
        } else return false;
    }

    public int getDistance() {
        return distance;
    }
}
