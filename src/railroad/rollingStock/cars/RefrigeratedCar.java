package railroad.rollingStock.cars;

public class RefrigeratedCar extends BasicFreightCar{
    private final boolean electricalGridNeed;
    private final String securityInfo;
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
    public boolean isElectricalGridNeed() {
        return electricalGridNeed;
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
