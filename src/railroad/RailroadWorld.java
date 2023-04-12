package railroad;

import railroad.exceptions.CannotAttachException;
import railroad.railwayMap.Connection;
import railroad.railwayMap.Station;
import railroad.rollingStock.Locomotive;
import railroad.rollingStock.Trainset;
import railroad.rollingStock.cars.Car;
import railroad.rollingStock.cars.PassengerRailroadCar;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Project that simulates railroad movement along routes with multithreading(trainsets)
 * and includes some basic methods for cars
 * @author Hryhorii Hrymailo
 * @version 1.0
 * @since 20.03.2023
 *
 */
public class RailroadWorld {

    private Map<String, Car> cars = new HashMap();
    private Map<String,Station> stations = new HashMap<>();
    private Set<Connection> connections = new HashSet<>();
    private Map<String, Locomotive> locomotives = new HashMap<>();
    private Map<Locomotive, Trainset> trains = new HashMap<>();

    public List<Trainset> getSortedTrainsets (Map<Locomotive, Trainset> trains) {
        List<Trainset> trainsets = new ArrayList<>();
        for(Trainset t : trains.values()) {
            trainsets.add(t);
        }
//        trainsets.stream().sorted(()).collect(Collectors.toList());
        // TODO sorting trainsets by route distance

        return trainsets;
    }

    public static void main(String[] args) {
        RailroadWorld world = new RailroadWorld();
        world.runProcess();
    }
    private void runProcess () {
        menu(new Scanner(System.in));
    }
    /** Generates objects for Railroad World from file
     * @param fileName file currently being read
     */
    private void genFromFile(String fileName) {
        try {
            FileInputStream file = new FileInputStream(fileName);
            menu(new Scanner(file));
            file.close();
        }catch (FileNotFoundException e) {
            System.out.println("file not found");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection(Station station1, Station station2) {
        for (Connection c:
             connections) {
            if(c.equals(new Connection(station1,station2,0))) return c;
        }
        return null;
    }

    /** Recursive function that takes two Station object
     finds the route between them based on connections
     * @param r passed route
     * @param s station to which path from source station already found
     * @param f destination Station
     *@return the first found solution(not shortest) or null
     */
    private List<Station> findPathTo (List<Station> r, Station s, Station f) {
        List<Station> route = new ArrayList<>(r);
        route.add(s);
        /*
        generates set of connections from given station
        */
        Set<Connection> filteredConnections = connections.stream().filter(p -> p.isConnected(s))
                .collect(Collectors.toCollection(LinkedHashSet::new));

//        System.out.println(filteredConnections);
        for (Connection c: filteredConnections) {
            Station dStation = c.connectTo(s);
            if(dStation.equals(f)) {
                route.add(dStation);
                return route;
            } else if (!route.contains(dStation)) { // going back in route and searching for another path
                List<Station> route2 = findPathTo(route,dStation, f);
                if(route2!=null) return route2;
            }
        }
        return null;
    }

    /**
     * Wrapper function for finding path between Stations
     * @param startStation source station
     * @param finishStation destination Station
     * @return route or null if it doesn't exist
     */
    public List<Station> computeRoute(Station startStation, Station finishStation)  {
        return findPathTo(new ArrayList<>(), startStation,finishStation);
    }

    /**
     * main input method that implements commands from text interface
     * <ol>
     *      <li> <font color="red">quit </font> -> to shut down the program
     *      <li> <font color="red">load fileName.txt </font>-> to load data from file
     *      <li> <font color="red">test-route StationName StationName </font>-> to execute computeRoute process
     *      <li> <font color="red">add Object(constructor) </font>-> to add object manually
     * </ol>
     * @param scan input Scanner
     */
    private void menu (Scanner scan) {
        String command;
        do  {
            System.out.print(">");
            command = scan.next();
            switch (command) {
                case "quit" :
                    System.out.println("exiting");
                    break;
                case "load" :
                    genFromFile(scan.next());
                    break;
                case "sample" :
                    // TODO examples of classes and their methods

                    break;
                case "test-route" :
                    System.out.println(computeRoute(stations.get("Station-5"),stations.get("Station-4")));
                    break;
                case "add":
                    switch (scan.next()) {
                        case "car" :
                            Car newCar = null;
                            switch (scan.next()) {
                                case "passenger":
                                    newCar = new PassengerRailroadCar("ukrzaliznytsia");
                                    break;
                            }
                            if(newCar!=null){
                                cars.put(newCar.getName(), newCar);
                                System.out.println("added: "+newCar);
                            }
                            break;
                        case "station":
                            Station newStation = new Station();
                            stations.put(newStation.getName(), newStation);
                            System.out.println("added: "+newStation);
                            break;
                        case "connection":
                            if(stations.size()<2) {
                                System.out.println("add at least one more station");
                            } else {
                                Connection newConnection = new Connection(
                                        stations.get(scan.next()),
                                        stations.get(scan.next()),
                                        100+(int)(Math.random()*100)
                                );
                                connections.add(newConnection);
                                System.out.println("added: "+newConnection);
                            }
                            break;
                        case "locomotive" : {
                            Locomotive locomotive = new Locomotive(stations.get(scan.next()), this);
                            locomotives.put(locomotive.getName(), locomotive);
                            //TODO exception if station not found
                            break;
                        }
                        case  "trainset" : {
                            Trainset trainset = new Trainset(locomotives.get(scan.next()));
                            trains.put(trainset.getLocomotive(), trainset);
                            break;
                        }
                        case "car-to-trainset" : {
                            try {
                                Trainset train = trains.get(locomotives.get(scan.next()));
                                train.attachCar(cars.get(scan.next()));
                            } catch (CannotAttachException e) {
                                System.err.println(e.getMessage());
                            }

                            break;
                        }
                    }
                    break;
                case "run-train" : {
                    Trainset train = trains.get(locomotives.get(scan.next()));
                    train.startTrain(stations.get(scan.next()));
                    break;
                }
                case "log"  : {
                    new Logger(this, "AppState.txt");
                }



            }
        } while(!"quit".equals(command));

    }
}