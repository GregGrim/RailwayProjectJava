package railroad.rollingStock.cars;

public class LiquidToxicMaterialsCar extends ToxicMaterialsCar implements LiquidMaterials{
    private enum Type {
        ETHER, MERCURY, ACID
    }
    private double volume;
    private int capacity;
    private Type liqType;
    private String chemicalName;
    LiquidToxicMaterialsCar(String name, String securityInfo, boolean electricalGridNeed) {
        super(name == null?"LiquidToxicMaterialsCar":name, "liquids"+securityInfo, electricalGridNeed);
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
        System.out.println("carWeight= "+getCarWeight());
        System.out.println("loadWeight= "+getLoadWeight()+"shipper:"+super.getShipper());
        System.out.println("securityInfo: "+getSecurityInfo());
        System.out.println("electricalGreedNeed"+(isElectricalGridNeed()?"Yes":"No"));
        System.out.println("material Type: "+super.getMatType()+ "chemicalName= "+chemicalName);

    }
}
