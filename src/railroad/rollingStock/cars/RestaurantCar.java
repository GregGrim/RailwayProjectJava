package railroad.rollingStock.cars;

public class RailroadRestaurantCar extends Car{
    private int seatingCapacity;
    private boolean isSmokingAllowed;
    private boolean hasBar;

    public RailroadRestaurantCar (String name) {
        super(name == null?"RailroadRestaurantCar":name, "passengers, electricity", true);
        this.seatingCapacity= (int) (10+Math.random()*20);
        this.isSmokingAllowed= Math.random() > 0.5;
        this.hasBar= Math.random() > 0.5;
    }

    public void addSeatingCapacity(int additionalCapacity) {
        seatingCapacity += additionalCapacity;
        System.out.println("Seating capacity in " + getName() + " increased to " + seatingCapacity);
    }

    @Override
    public void getSummary() {
        String smokingAllowed = isSmokingAllowed ? "Yes" : "No";
        String hasBarText = hasBar ? "Yes" : "No";
        System.out.println("Car: " + getName());
        System.out.println("carWeight= "+getCarWeight());
        System.out.println("loadWeight= "+getLoadWeight());
        System.out.println("Smoking Allowed: " + smokingAllowed);
        System.out.println("Has Bar: " + hasBarText);
        System.out.println("Seating Capacity= " + seatingCapacity);
    }
}
