package railroad.rollingStock.cars;

import railroad.DebugMsg;

public class GaseousMaterialsCar extends BasicFreightCar{
    enum Type {
        OXYGEN, NITROGEN, ARGON
    }
    private Type gasType;
    private double volume; // volume under atmospheric pressure

    private int capacity;
    private String securityInfo;

    public GaseousMaterialsCar(String name) {
        super(name == null?"GaseousMaterialsCar":name);
        securityInfo="gases";
        capacity=2500;
        double x = Math.random();
        if(x<0.33) {
            gasType= Type.OXYGEN;
            calcVolume();
        }
        else if(x>0.66) {
            gasType= Type.NITROGEN;
            calcVolume();
        }
        else {
            gasType= Type.ARGON;
            calcVolume();
        }
    }
    public void calcVolume() {
        if(gasType== Type.OXYGEN) {
            volume = loadWeight*1000/1.43;
        }
        else if(gasType== Type.NITROGEN) {
            volume = loadWeight*1000/1.16;
        }
        else {
            volume=loadWeight*1000/1.78;
        }
    }
    @Override
    public void loadCargo(double cargo) {
        if(capacity>cargo+loadWeight) super.loadCargo(cargo);
        super.loadCargo(cargo);
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
        System.out.println("loadWeight= "+loadWeight+"\ncapacity="+capacity);
        System.out.println("securityInfo: "+securityInfo);
        System.out.println("electricalGreedNeed: "+(isElectricalGridNeed()?"Yes":"No"));
        System.out.println("gas Type: "+gasType+ "\nvolume= "+volume);
    }
}
