package railroad.rollingStock.cars;

public class ToxicMaterialsCar extends HeavyFreightCar{
    private enum Type {
        POISON, RADIOACTIVE, DANGER_FOR_ENVIRONMENT
    }
    private String chemicalName;
    private Type matType;

    ToxicMaterialsCar(String name, String securityInfo, boolean electricalGridNeed) {
        super(name == null?"ToxicMaterialsCar":name, "toxic"+securityInfo, electricalGridNeed);
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

    public Type getMatType() {
        return matType;
    }

    @Override
    public void getSummary() {
        super.getSummary();
        System.out.println("material Type: "+matType+ "chemicalName= "+chemicalName);
    }
}
