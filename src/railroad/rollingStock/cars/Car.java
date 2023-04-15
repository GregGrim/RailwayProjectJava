package railroad.rollingStock.cars;


import railroad.rollingStock.Trainset;

public abstract class Car{
private static int counter= 1;
private String name;
protected int carWeight;
protected int loadWeight;
private String securityInfo;
private Trainset trainset;

private final boolean electricalGridNeed ;

Car (String name, String securityInfo, boolean electricalGridNeed) {
    this.name=name+"-"+(counter++);
    this.carWeight= (int) (1000+(Math.random()*2000));
    this.loadWeight= (int) (150+(Math.random()*500));
    this.securityInfo=securityInfo;
    this.electricalGridNeed=electricalGridNeed;
    this.trainset=null;
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

    public int getCarWeight() {
        return carWeight;
    }

    public Trainset getTrainset() {
        return trainset;
    }

    public void setTrainset(Trainset trainset) {
        this.trainset = trainset;
    }

    public String getSecurityInfo() {
        return securityInfo;
    }

    @Override
    public String toString() {
        return  name;
    }

    public abstract void getSummary() ;
}
