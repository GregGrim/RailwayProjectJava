package railroad.rollingStock.cars;

public class RestaurantCar extends Car{
    private int seatingCapacity;
    private final boolean isSmokingAllowed;
    private final boolean hasBar;

    public RestaurantCar(String name) {
        super(name == null?"RestaurantCar":name, "passengers, electricity", true);
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
        System.out.println(getName());
        System.out.println("carWeight= "+carWeight);
        System.out.println("loadWeight= "+loadWeight);
        System.out.println("securityInfo: "+getSecurityInfo());
        System.out.println("electricalGreedNeed: "+(isElectricalGridNeed()?"Yes":"No"));
        System.out.println("Smoking Allowed: " + smokingAllowed);
        System.out.println("Has Bar: " + hasBarText);
        System.out.println("Seating Capacity= " + seatingCapacity);
    }
}
