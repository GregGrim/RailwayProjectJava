package railroad.rollingStock.cars;

import railroad.DebugMsg;

public class BaggageMailCar extends Car{
    private String shipper;
    public BaggageMailCar(String name) {
        super(name == null?"BaggageMailCar":name, "-", false);
        shipper="FedEx";
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

    @Override
    public void getSummary() {
        System.out.println(getName());
        System.out.println("carWeight= "+carWeight);
        System.out.println("loadWeight= "+loadWeight+"\nshipper:"+shipper);
        System.out.println("securityInfo: "+getSecurityInfo());
        System.out.println("electricalGreedNeed: "+(isElectricalGridNeed()?"Yes":"No"));
    }
}
