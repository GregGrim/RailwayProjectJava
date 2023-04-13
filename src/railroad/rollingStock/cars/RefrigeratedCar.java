package railroad.rollingStock.cars;

public class RefrigeratedCar extends BasicFreightCar{
    RefrigeratedCar(String name, String securityInfo, boolean electricalGridNeed) {
        super(name == null?"RefrigeratedCar":name, "electricity", true);
    }
}
