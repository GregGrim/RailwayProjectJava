package railroad.rollingStock.cars;

public class LiquidToxicMaterialsCar extends ToxicMaterialsCar implements LiquidMaterials{
    private enum Type {
        ETHER, MERCURY, ACID
    }
    private double volume;
    private int capacity;
    private Type liqType;
    LiquidToxicMaterialsCar(String name, String securityInfo, boolean electricalGridNeed) {
        super(name == null?"LiquidToxicMaterialsCar":name, "liquids"+securityInfo, electricalGridNeed);
        capacity=1000;
        double x = Math.random();
        if(x<0.33) {
            liqType= Type.ACID;
            calcVolume();
        }
        else if(x>0.66) {
            liqType= Type.MERCURY;
            calcVolume();
        }
        else {
            liqType= Type.ETHER;
            calcVolume();
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
    public void calcVolume() {
        if(liqType== Type.ACID) {
            volume = loadWeight/2.0;
        }
        else if(liqType== Type.ETHER) {
            volume = loadWeight/0.8;
        }
        else {
            volume=loadWeight/13.5;
        }
    }
    @Override
    public int capLeft() {
        return 0;
    }
}
