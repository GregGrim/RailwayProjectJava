package railroad.rollingStock.cars;

public class PassengerRailroadCar extends Car {
    private double carWeight;
    private double loadWeight = 0;

    public PassengerRailroadCar(String name) {
        super(name == null?"PassengerRailroadCar":name,
                "passengers, electricity",
                true);
    }

}
