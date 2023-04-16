package railroad.rollingStock.cars;

public class LiquidToxicMaterialsCar extends ToxicMaterialsCar implements LiquidMaterials{
    private enum Type {
        ETHER, MERCURY, ACID
    }
    private double volume;
    private final int capacity;
    private final Type liqType;
    private final String chemicalName;
    private final String securityInfo;
    public LiquidToxicMaterialsCar(String name) {
        super(name == null?"LiquidToxicMaterialsCar":name);
        securityInfo=super.getSecurityInfo()+", liquids";
        capacity=1000;
        double x = Math.random();
        if(x<0.33) {
            liqType= Type.ACID;
            chemicalName="Sulfuric Acid";
            calcVolume();
        }
        else if(x>0.66) {
            liqType= Type.MERCURY;
            chemicalName="Mercury";
            calcVolume();
        }
        else {
            liqType= Type.ETHER;
            chemicalName="diethyl ether";
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
        return capacity-loadWeight;
    }

    @Override
    public void getSummary() {
        System.out.println(getName());
        System.out.println("carWeight= "+carWeight);
        System.out.println("loadWeight= "+loadWeight+"\nshipper: "+super.getShipper());
        System.out.println("securityInfo: "+securityInfo);
        System.out.println("electricalGreedNeed: "+(isElectricalGridNeed()?"Yes":"No"));
        System.out.println("volume= "+volume+"\nliquid Type: "+liqType+ "\nchemicalName= "+chemicalName);

    }
}
