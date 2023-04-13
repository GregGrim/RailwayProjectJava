package railroad.rollingStock.cars;

import railroad.DebugMsg;

public class ExplosiveMaterialsCar extends HeavyFreightCar{
    private int capacity;
    private String securityInfo;
    public ExplosiveMaterialsCar(String name) {
        super(name == null?"ExplosiveMaterialsCar":name);
        capacity=loadWeight+(int)(Math.random()*500);
        securityInfo=super.getSecurityInfo()+", explosive";
    }
    public boolean isAtRiskOfExploding() {
        final double EXPLOSIVE_RISK_THRESHOLD = 90.0;
        double loadPercentage = loadWeight / capacity * 100.0;
        return loadPercentage >= EXPLOSIVE_RISK_THRESHOLD;
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
        System.out.println("loadWeight= "+loadWeight+"\ncapacity="+capacity);
        System.out.println("securityInfo: "+securityInfo);
        System.out.println("electricalGreedNeed: "+(isElectricalGridNeed()?"Yes":"No"));
        System.out.println("Is at risk: "+isAtRiskOfExploding());
    }

}
