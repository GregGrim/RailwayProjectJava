package railroad.rollingStock.cars;


public abstract class Car{
private static int counter= 1;
private String name;
protected double carWeight;
protected double loadWeight;
private String securityInfo;

private final boolean electricalGridNeed ;

Car (String name, String securityInfo, boolean electricalGridNeed) {
    this.name=name+"-"+(counter++);
    this.carWeight=10+(Math.random()*100);
    this.loadWeight=20+(Math.random()*500);
    this.securityInfo=securityInfo;
    this.electricalGridNeed=electricalGridNeed;
}

    public String getName() {
        return name;
    }

    public boolean isElectricalGridNeed() {
        return electricalGridNeed;
    }

    public double getLoadWeight() {
        return loadWeight;
    }

    @Override
    public String toString() {
        return  name+" ";
    }


}
