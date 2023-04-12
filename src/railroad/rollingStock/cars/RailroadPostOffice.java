package railroad.rollingStock.cars;

public class RailroadPostOffice extends Car{
    public RailroadPostOffice(String name, double carWeight) {
        super(name == null?"RailroadPostOffice":name, "electricity", true);
    }
}
