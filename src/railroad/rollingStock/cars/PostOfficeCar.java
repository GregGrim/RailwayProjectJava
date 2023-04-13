package railroad.rollingStock.cars;

public class RailroadPostOffice extends Car{
    public RailroadPostOffice(String name, double carWeight) {
        super(name == null?"RailroadPostOffice":name, "electricity", true);
    }
    public String getSummary() {
        //TODO out all info
        return null;
    }
}
