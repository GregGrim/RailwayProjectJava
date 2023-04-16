package railroad.rollingStock.cars;


public class BasicFreightCar extends Car{
    private final String shipper;

    public BasicFreightCar(String name) {
        super(name == null?"BasicFreightCar":name, "-", false);
        shipper = "Rail GmbH";
    }
    public void loadCargo (double cargo) {
        loadWeight += cargo;
        System.out.println(cargo+"kg loaded to "+getName());
    }
    public void unloadCargo (double cargo) {
        if(loadWeight-cargo>0) {
            loadWeight -= cargo;
            System.out.println(cargo+"kg loaded to "+getName());
        }
        else System.out.println("No more cargo left on "+getName());
    }

    public String getShipper() {
        return shipper;
    }

    @Override
    public void getSummary() {
        System.out.println(getName());
        System.out.println("carWeight= "+carWeight);
        System.out.println("loadWeight= "+loadWeight+"\nshipper:"+shipper);
        System.out.println("securityInfo: "+getSecurityInfo());
        System.out.println("electricalGreedNeed: "+(isElectricalGridNeed()?"Yes":"No"));
    }
}
