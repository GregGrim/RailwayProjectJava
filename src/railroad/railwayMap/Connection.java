package railroad.railwayMap;

import railroad.DebugMsg;
import railroad.rollingStock.Locomotive;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Basic railroad world class characterized by two stations
 */
public class Connection {
    private final Station stationA;
    private final Station stationB;
    private final int distance;
    private final Queue<Locomotive> queue = new ConcurrentLinkedQueue<>();
    public Connection(Station stationA, Station stationB) {
        this.distance=1000+(int)(Math.random()*1000);
        this.stationA=stationA;
        this.stationB=stationB;
    }
    public Queue<Locomotive> getQueue() {
        return queue;
    }

    /**
     *  setting to queue loco if it's not already there
     * @param locomotive locomotive
     */
    public void setToQueue(Locomotive locomotive) {
        if(!queue.contains(locomotive)) {
            DebugMsg.msg("set to queue "+locomotive);
            queue.offer(locomotive);
        }
    }

    /**
     * removes head of queue
     * @param locomotive loco to be removed from queue
     * @throws RuntimeException if queue is empty
     */
    public void removeFromQueue(Locomotive locomotive) throws RuntimeException{
        if(locomotive.isRunning()) {
            if (!locomotive.equals(queue.poll())) {
                throw new RuntimeException("error! " + locomotive.isRunning());
            }
        }
    }

    /**
     * checks if loco is first in queue(running on connection)
     * @param locomotive loco to be checked
     * @return boolean
     */
    public boolean onTheWay(Locomotive locomotive) {
        return locomotive.equals(queue.peek());
    }
    @Override
    public String toString() {
        return "Connection {"+ stationA.getName() +" - "+ stationB.getName() +
                "}";
    }

    /**
     * finding second end of connection
     * @param station first end of connection
     * @return station
     */
    public Station connectTo (Station station) {
        if(station.equals(stationA)) return stationB;
        if(station.equals(stationB)) return stationA;
        return null;
    }

    /**
     * checks if stations are connected
     * @param station to be checked
     * @return boolean
     */
    public boolean isConnected (Station station) {
        return connectTo(station)!=null;
    }

    /**
     * overriden method equals that comparing connections by 2 stations
     * @param connection connection to be compared
     * @return boolean
     */
    @Override
    public boolean equals(Object connection) {
        if (connection == null) {
            return false;
        }
        if(connection instanceof Connection c) {
            return  this.stationA.equals(c.stationA)&&this.stationB.equals(c.stationB)||
                    this.stationA.equals(c.stationB)&&this.stationB.equals(c.stationA);
        } else return false;
    }
    public int getDistance() {
        return distance;
    }
}
