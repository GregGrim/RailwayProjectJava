package railroad.rollingStock.cars;

public class ExplosiveMaterialsCar extends HeavyFreightCar{
    private int capacity;
    ExplosiveMaterialsCar(String name, String securityInfo, boolean electricalGridNeed) {
        super(name == null?"ExplosiveMaterialsCar":name, "explosive"+securityInfo, electricalGridNeed);
        capacity=loadWeight+(int)(Math.random()*500);
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
        super.getSummary();
        System.out.println("Is at risk: "+isAtRiskOfExploding());
    }

}
