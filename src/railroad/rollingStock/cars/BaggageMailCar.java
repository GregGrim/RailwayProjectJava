package railroad.rollingStock.cars;

public class BaggageMailCar extends Car{
    public BaggageMailCar(String name) {
        super(name == null?"BaggageMailCar":name, "-", false);
    }
    
    public void loadCargo (double cargo) {
        loadWeight += cargo;
    }

}
