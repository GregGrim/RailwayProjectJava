package railroad.rollingStock.cars;

public class LiquidMaterialsCar extends BasicFreightCar implements LiquidMaterials{
    enum Type {
        WATER, OIL, GASOLINE
    }
    private final Type liqType;
    private double volume;
    private final int capacity;
    private final String securityInfo;
    public LiquidMaterialsCar(String name) {
        super(name == null?"LiquidMaterialsCar":name);
        securityInfo="liquids";
        double x = Math.random();
        capacity=1000;
        if(x<0.33) {
            liqType=Type.WATER;
            calcVolume();
        }
        else if(x>0.66) {
            liqType=Type.OIL;
            calcVolume();
        }
        else {
            liqType=Type.GASOLINE;
            calcVolume();
        }
    }
    @Override
    public int capLeft() {
        return capacity-loadWeight;
    }

    @Override
    public void calcVolume() {
        if(liqType==Type.WATER) {
            volume = loadWeight;
        }
        else if(liqType==Type.OIL) {
            volume = loadWeight/0.8;
        }
        else {
            volume=loadWeight/0.75;
        }
    }
    @Override
    public void loadCargo(double cargo) {
        if(capacity>cargo+loadWeight) super.loadCargo(cargo);
        else System.out.println("No space left for cargo");
        calcVolume();
    }
    @Override
    public void unloadCargo(double cargo) {
        super.unloadCargo(cargo);
        calcVolume();
    }
    @Override
    public void getSummary() {
        System.out.println(getName());
        System.out.println("carWeight= "+carWeight);
        System.out.println("loadWeight= "+loadWeight+"\ncapacity= "+capacity);
        System.out.println("securityInfo: "+securityInfo);
        System.out.println("electricalGreedNeed: "+(isElectricalGridNeed()?"Yes":"No"));
        System.out.println("liquid Type: "+liqType+ "\nvolume= "+volume);
    }
}
