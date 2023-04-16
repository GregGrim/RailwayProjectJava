package railroad.rollingStock.cars;

public class ToxicMaterialsCar extends HeavyFreightCar{
    private enum Type {
        POISON, RADIOACTIVE, DANGER_FOR_ENVIRONMENT
    }
    private final String chemicalName;
    private final Type matType;
    private final String securityInfo;

    public ToxicMaterialsCar(String name) {
        super(name == null?"ToxicMaterialsCar":name);
        securityInfo=super.getSecurityInfo()+", toxic";
        double x = Math.random();
        if(x<0.33) {
            matType= Type.RADIOACTIVE;
            chemicalName="Uranium ore";
        }
        else if(x>0.66) {
            matType= Type.POISON;
            chemicalName="Potassium cyanide";
        }
        else {
            matType= Type.DANGER_FOR_ENVIRONMENT;
            chemicalName="Lead compounds";
        }
    }
    @Override
    public void getSummary() {
        System.out.println(getName());
        System.out.println("carWeight= "+carWeight);
        System.out.println("loadWeight= "+loadWeight+"\nshipper: "+super.getShipper());
        System.out.println("securityInfo: "+securityInfo);
        System.out.println("electricalGreedNeed: "+(isElectricalGridNeed()?"Yes":"No"));
        System.out.println("material Type: "+matType+ "\nchemicalName= "+chemicalName);
    }
}
