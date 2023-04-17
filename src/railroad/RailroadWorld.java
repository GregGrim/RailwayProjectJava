package railroad;

import railroad.exceptions.CannotAttachException;
import railroad.railwayMap.Connection;
import railroad.railwayMap.Station;
import railroad.rollingStock.Locomotive;
import railroad.rollingStock.Trainset;
import railroad.rollingStock.cars.*;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Semaphore;
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
    private final Map<String, Car> cars = new HashMap<>();
    private final Map<String,Station> stations = new HashMap<>();
    private final Set<Connection> connections = new HashSet<>();
    private final Map<String, Locomotive> locomotives = new HashMap<>();
    private final Map<Locomotive, Trainset> trains = new HashMap<>();
    private final Semaphore semaphoreDelete = new Semaphore(1);
    private final Semaphore semaphoreCompute = new Semaphore(1000);
    public Map<Locomotive, Trainset> getTrains() {
        return trains;
    }

    /**
     * sorting trainsets by the length of route
     * @return sorted list of trainsets
     */

    public List<Trainset> getSortedTrainsets () {
        List<Trainset> trainsets = new ArrayList<>(trains.values());
        trainsets = trainsets.stream().sorted((Trainset t1, Trainset t2)->
                 t2.getLocomotive().getRouteDistance()-t1.getLocomotive().getRouteDistance()).toList();
        return trainsets;
    }

    /**
     * function checks if class is child of ...
     * @param parentCls parent object
     * @param object child object
     * @return boolean
     * @param <Y> parent class
     * @param <T> child class
     */
    public <Y, T> boolean isChildOf(Class<Y> parentCls, T object){
        return parentCls.isAssignableFrom(object.getClass());
    }

    /**
     * generic function that deletes object from world even if threads are running
     * @param object object to be deleted
     * @param <T> any basic class of railroad world
     */
    public synchronized <T> void deleteObject(T object) {
            if (isChildOf(Car.class, object)){
                Car car = (Car)object;
                String carName = car.getName();
                // if car is in trainset removes it from trainset and removes car
                if(car.getTrainset()!=null) {
                    List<Car> remainedCars = car.getTrainset().getCars();
                    remainedCars.removeIf(c-> carName.equals(c.getName()));
                    car.getTrainset().setCars(remainedCars);
                }
                cars.remove(carName);
            } else if (isChildOf(Locomotive.class, object)) {
                Locomotive locomotive = (Locomotive)object;
                String locomotiveName = locomotive.getName();
                //stops locomotive threads
                locomotive.stop();
                locomotive.getCurrentConnection().getQueue().remove(locomotive);
                //removes all cars from train and deletes loco and train
                Collection<Car> cc = List.copyOf(locomotive.getTrainset().getCars());
                cc.forEach(this::deleteObject);
                trains.remove(locomotive);
                locomotives.remove(locomotiveName);
            } else if (isChildOf(Connection.class, object)) {
                    Connection connection = (Connection) object;
                    // removing connection from available
                    connections.remove(connection);
                    // getting loco in connection
                    Locomotive runningLoco = connection.getQueue().peek();
                    // clearing queue
                    connection.getQueue().clear();
                    if (runningLoco != null) {
                        deleteObject(runningLoco);
                    } else {
                        System.out.println("No locomotive found in this connection");
                    }
                    // after arriving to the next station each locomotive will recalculate next route without this station
                    locomotives.values().stream()
                            .filter(locomotive -> locomotive.hasConnectionInRoute(connection))
                            .forEach(locomotive -> locomotive.setShouldRecalc(true));

            } else if (isChildOf(Station.class, object)) {
                Station station = (Station) object;
                //removing locos from this station and locos that have this station as home||destination
                Collection<Locomotive> ll = List.copyOf(locomotives.values());
                Collection<Connection> connectionsCopy = List.copyOf(connections);
                for (Locomotive l: ll) {
                    if (l.getRoute().get(0).equals(station)
                            || l.getHomeStation().equals(station) ||
                            l.getDestinationStation().equals(station)) {
                        deleteObject(l);
                    } else System.out.println(l.getName() + " is not related to this station");
                }
                //finding all connections from this station and deleting them
                connectionsCopy.stream().filter(p -> p.isConnected(station)).forEach(this::deleteObject);
                stations.remove(station.getName());
            }
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
            if(c.equals(new Connection(station1,station2))) return c;
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
    public List<Station> computeRoute(Station startStation, Station finishStation) {
        List<Station> list = null;
        try {
            while (semaphoreDelete.availablePermits() == 0) {
                Thread.sleep(10);
            }
            semaphoreCompute.acquire();
            list = findPathTo(new ArrayList<>(), startStation, finishStation);
            semaphoreCompute.release();
        } catch (InterruptedException e) {
                e.printStackTrace();
        }
        return list;
    }

    /**
     * main input method that implements commands from text interface
     * <ol>
     *      <li> <font color="red">quit </font> -> to shut down the program
     *      <li> <font color="red">debug </font> -> to set debug mode
     *      <li> <font color="red">report </font> -> to get specific info
     *      <li> <font color="red">load fileName.txt </font>-> to load data from file
     *      <li> <font color="red">test-route StationName StationName </font>-> to execute computeRoute process
     *      <li> <font color="red">add Object(constructor) </font>-> to add object
     *      <li> <font color="red">remove Object(constructor) </font>-> to remove object
     *      <li> <font color="red">run-train </font>-> to start threads of train(train movement simulation)
     *      <li> <font color="red">log </font> -> to start thread logger that rewriting general info in AppState.txt each 5sec
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
                    System.out.println("exiting...");
                    break;
                case "load" :
                    genFromFile(scan.next());
                    break;
                case "debug" :
                    DebugMsg.setDebugLevel(Integer.parseInt(scan.next()));
                    break;
                case "sample" : {
                    // simply shows functionalities that are not called in simulation
                    Station station = new Station();
                    Locomotive loco = new Locomotive(station, this);
                    Trainset trainset = new Trainset(loco);

                    RestaurantCar restaurantCar = new RestaurantCar(null);
                    System.out.println(restaurantCar.getName()+" has been created");
                    restaurantCar.getSummary();
                    restaurantCar.addSeatingCapacity(15);
                    System.out.println();

                    BaggageMailCar baggageMailCar = new BaggageMailCar(null);
                    System.out.println(baggageMailCar.getName()+" has been created");
                    baggageMailCar.getSummary();
                    baggageMailCar.unloadCargo(baggageMailCar.getLoadWeight()+100);
                    baggageMailCar.loadCargo(1000);
                    System.out.println();


                    BasicFreightCar basicFreightCar = new BasicFreightCar(null);
                    System.out.println(basicFreightCar.getName()+" has been created");
                    basicFreightCar.getSummary();
                    basicFreightCar.unloadCargo(basicFreightCar.getLoadWeight()+100);
                    basicFreightCar.loadCargo(1000);
                    System.out.println();

                    ExplosiveMaterialsCar explosiveMaterialsCar = new ExplosiveMaterialsCar(null);
                    System.out.println(explosiveMaterialsCar.getName()+" has been created");
                    explosiveMaterialsCar.getSummary();
                    explosiveMaterialsCar.unloadCargo(explosiveMaterialsCar.getLoadWeight()+100);
                    explosiveMaterialsCar.loadCargo(1000);
                    System.out.println(explosiveMaterialsCar.getName()+" is at risk of exploding?:"
                            +explosiveMaterialsCar.isAtRiskOfExploding());
                    System.out.println();

                    GaseousMaterialsCar gaseousMaterialsCar = new GaseousMaterialsCar(null);
                    System.out.println(gaseousMaterialsCar.getName()+" has been created");
                    gaseousMaterialsCar.getSummary();
                    gaseousMaterialsCar.unloadCargo(explosiveMaterialsCar.getLoadWeight()+100);
                    gaseousMaterialsCar.loadCargo(1000);
                    System.out.println();

                    HeavyFreightCar heavyFreightCar = new HeavyFreightCar(null);
                    System.out.println(heavyFreightCar.getName()+" has been created");
                    heavyFreightCar.getSummary();
                    heavyFreightCar.unloadCargo(heavyFreightCar.getLoadWeight()+100);
                    heavyFreightCar.loadCargo(1000);
                    System.out.println();

                    LiquidMaterialsCar liquidMaterialsCar = new LiquidMaterialsCar(null);
                    System.out.println(liquidMaterialsCar.getName()+" has been created");
                    liquidMaterialsCar.getSummary();
                    liquidMaterialsCar.unloadCargo(liquidMaterialsCar.getLoadWeight()+100);
                    liquidMaterialsCar.loadCargo(1000);
                    System.out.println("Capacity left: "+liquidMaterialsCar.capLeft());
                    System.out.println();

                    LiquidToxicMaterialsCar liquidToxicMaterialsCar = new LiquidToxicMaterialsCar(null);
                    System.out.println(liquidToxicMaterialsCar.getName()+" has been created");
                    liquidToxicMaterialsCar.getSummary();
                    liquidToxicMaterialsCar.unloadCargo(liquidToxicMaterialsCar.getLoadWeight()+100);
                    liquidToxicMaterialsCar.loadCargo(1000);
                    System.out.println("Capacity left: "+liquidToxicMaterialsCar.capLeft());
                    System.out.println();

                    PassengerCar passengerCar = new PassengerCar(null);
                    System.out.println(passengerCar.getName()+" has been created");
                    passengerCar.getSummary();
                    passengerCar.occupySeats(100);
                    passengerCar.occupySeats(5);
                    passengerCar.addMoreSeats(10);
                    passengerCar.hasFreeSeat();
                    System.out.println();

                    PostOfficeCar postOfficeCar = new PostOfficeCar(null);
                    System.out.println(postOfficeCar.getName()+" has been created");
                    postOfficeCar.getSummary();
                    postOfficeCar.mailsLeft();
                    postOfficeCar.sendMail("John Nelson");
                    System.out.println();

                    RefrigeratedCar refrigeratedCar = new RefrigeratedCar(null);
                    System.out.println(refrigeratedCar.getName()+" has been created");
                    refrigeratedCar.getSummary();
                    refrigeratedCar.unloadCargo(refrigeratedCar.getLoadWeight()+100);
                    refrigeratedCar.loadCargo(1000);
                    System.out.println();

                    ToxicMaterialsCar toxicMaterialsCar = new ToxicMaterialsCar(null);
                    System.out.println(toxicMaterialsCar.getName()+" has been created");
                    toxicMaterialsCar.getSummary();
                    toxicMaterialsCar.unloadCargo(toxicMaterialsCar.getLoadWeight()+100);
                    toxicMaterialsCar.loadCargo(1000);
                    System.out.println();


                    trainset.detachCar(); // should show that there is no cars in train
                    try {
                        trainset.attachCar(restaurantCar);
                        trainset.attachCar(explosiveMaterialsCar);
                        trainset.attachCar(passengerCar);
                        trainset.attachCar(postOfficeCar);
                        trainset.attachCar(liquidMaterialsCar);
                    } catch (CannotAttachException e) {
                        System.err.println(e.getMessage());
                    }
                    System.out.println("Cars: "+ trainset.getCars());
                    break;
                }
                case "report" :
                    Locomotive loc = locomotives.get(scan.next());
                    String option = scan.next();
                    if(loc==null) {
                        System.out.println("Train not found");
                    } else switch (option) {
                        case "basic" -> System.out.println(loc.getTrainset());
                        case "cars" -> {
                            System.out.println("Cars:");
                        loc.getTrainset().getSummaryOfCars();
                        }
                        case "conDistancePassed" ->
                                System.out.println(Math.min(loc.getDistance() * 100 /
                                        loc.getCurrentConnection().getDistance(),100)+"%");
                        case "routeDistancePassed" ->
                                System.out.println(Math.min(loc.getRouteDistancePassed() * 100 /
                                        loc.getRouteDistance(),100)+"%");
                    }
                    break;
                case "test-route" :
                    System.out.println(computeRoute(stations.get("Station-5"),stations.get("Station-4")));
                    break;
                case "add":
                    switch (scan.next()) {
                        case "car" :
                            Car newCar = switch (scan.next()) {
                                case "passenger" -> new PassengerCar(null);
                                case "baggageMail" -> new BaggageMailCar(null);
                                case "basicFreight" -> new BasicFreightCar(null);
                                case "expMat" -> new ExplosiveMaterialsCar(null);
                                case "gasMat" -> new GaseousMaterialsCar(null);
                                case "heavyFreight" -> new HeavyFreightCar(null);
                                case "liqMat" -> new LiquidMaterialsCar(null);
                                case "liqToxMat" -> new LiquidToxicMaterialsCar(null);
                                case "postOffice" -> new PostOfficeCar(null);
                                case "refrigerated" -> new RefrigeratedCar(null);
                                case "restaurant" -> new RestaurantCar(null);
                                case "toxMat" -> new ToxicMaterialsCar(null);
                                default -> null;
                            };
                            if(newCar!=null){
                                cars.put(newCar.getName(), newCar);
                                System.out.println("added: "+newCar);
                            } else System.err.println("No such car");
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
                                        stations.get(scan.next())
                                );
                                connections.add(newConnection);
                                System.out.println("added: "+newConnection);
                            }
                            break;
                        case "locomotive" : {
                            String inputStationName = scan.next();
                            Station station = stations.get(inputStationName);
                            if(station==null) {
                                System.err.println(inputStationName + " does not exist!");
                            } else {
                                Locomotive locomotive = new Locomotive(station, this);
                                locomotives.put(locomotive.getName(), locomotive);
                                System.out.println(locomotive.getName() + " added to: " + locomotive.getHomeStation());
                                Trainset trainset = new Trainset(locomotive);
                                trains.put(trainset.getLocomotive(), trainset);
                                System.out.println("train set was created for locomotive: " + locomotive.getName());
                            }
                            break;
                        }
                        case "car-to-trainset" : {
                            try {
                                Trainset train = trains.get(locomotives.get(scan.next()));
                                Car car = cars.get(scan.next());
                                train.attachCar(car);
                            } catch (CannotAttachException e) {
                                System.err.println(e.getMessage());
                            }

                            break;
                        }
                    }
                    break;
                case "remove" : {
                    try {
                        while (semaphoreCompute.availablePermits()  < 1000) {
                            Thread.sleep(10);
                        }
                        semaphoreDelete.acquire();
                        switch (scan.next()) {
                            case "car-from-trainset" -> {
                                Trainset train = trains.get(locomotives.get(scan.next()));
                                train.detachCar();
                            }
                            case "car" -> {
                                String carName = scan.next();
                                Car car = cars.get(carName);
                                if (car != null) {
                                    deleteObject(car);
                                    System.out.println(car.getName() + " deleted");
                                } else System.err.println("No such car in Railroad World");
                            }
                            case "locomotive" -> {
                                String locomotiveName = scan.next();
                                Locomotive locomotive = locomotives.get(locomotiveName);
                                if (locomotive != null) {
                                    deleteObject(locomotive);
                                    System.out.println(locomotive.getName() + " deleted");
                                } else System.err.println("No such locomotive in Railroad World");
                            }
                            case "connection" -> {
                                Station stationA = stations.get(scan.next());
                                Station stationB = stations.get(scan.next());
                                Connection connection = new Connection(stationA, stationB);
                                List<Connection> matchingConnections = connections.stream()
                                        .filter(c -> c.equals(connection)).toList();
                                if (matchingConnections.size() > 0) {
                                    Connection matchingConnection = matchingConnections.get(0);
                                    deleteObject(matchingConnection);
                                } else System.err.println(connection + "does not exist!");
                            }
                            case "station" -> {
                                Station station = stations.get(scan.next());
                                if (station != null) {
                                    deleteObject(station);
                                    System.out.println(station.getName() + " deleted");
                                } else System.err.println("No such station in Railroad World");
                            }
                        }
                        semaphoreDelete.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "run-train" : {
                    Locomotive train = locomotives.get(scan.next());
                    train.startTrain(stations.get(scan.next()));
                    break;
                }
                case "log"  : {
                    new Logger(this, "AppState.txt");
                    break;
                }
            }
        } while(!"quit".equals(command));
    }
}
