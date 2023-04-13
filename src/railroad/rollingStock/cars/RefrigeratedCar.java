package railroad.rollingStock.cars;

import railroad.DebugMsg;

public class RefrigeratedCar extends BasicFreightCar{
    private boolean electricalGridNeed ;
    private String securityInfo;
    public RefrigeratedCar(String name) {
        super(name == null?"RefrigeratedCar":name);
        electricalGridNeed=true;
        securityInfo="electricity";
    }

    @Override
    public void loadCargo(double cargo) {
        super.loadCargo(cargo);
    }

    @Override
    public void unloadCargo(double cargo) {
        super.unloadCargo(cargo);
    }

    @Override
    public void getSummary() {
        System.out.println(getName());
        System.out.println("carWeight= "+carWeight);
        System.out.println("loadWeight= "+loadWeight+"\nshipper: "+super.getShipper());
        System.out.println("securityInfo: "+securityInfo);
        System.out.println("electricalGreedNeed: "+(isElectricalGridNeed()?"Yes":"No"));
    }
}
