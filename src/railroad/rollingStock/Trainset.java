package railroad.rollingStock;

import railroad.exceptions.CannotAttachException;

import railroad.railwayMap.Station;
import railroad.rollingStock.cars.Car;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Trainset Object that is based on
 * @see Locomotive
 *
 */
public class Trainset implements Runnable{
    private List<Car> cars ;
    private int maxCars;
    private double maxLoad;
    private double loaded = 0;
    private int maxElecNeededCars;
    private int elecConnectedCars = 0;

    private Locomotive locomotive;
    public Trainset (Locomotive _locomotive) {
        locomotive=_locomotive;
        cars = new ArrayList<>();
        maxCars = 3+(int)(Math.random()*10);
        maxLoad = 30+(Math.random()*1000);
        maxElecNeededCars = maxCars-2;
    }

    public Locomotive getLocomotive() {
        return locomotive;
    }

    @Override
    public String toString() {
        return locomotive+" : "+cars;
    }

    /**
     * Method implements attaching new Car to given traiset
     * @param car Car to attach
     * @throws CannotAttachException appears if any of trainset restrictions has been violated
     */
    public void attachCar(Car car) throws CannotAttachException {
        if(maxCars==cars.size()) {
            throw new CannotAttachException("Max number of cars reached");
        } else if (maxLoad<(loaded+car.getLoadWeight())) {
            throw new CannotAttachException("Max load weight of trainset exceeded");
        } else if (car.isElectricalGridNeed()&&(maxElecNeededCars == elecConnectedCars)) {
            throw new CannotAttachException("Max number of electricity needed cars reached");
        } else {
            cars.add(car);
            loaded+=car.getLoadWeight();
            if (car.isElectricalGridNeed()) elecConnectedCars++;
            System.out.println(car+" has been attached to "+ this);
        }
    }

    /**
     * Method implements detaching last Car from given trainset if it has any
     */
    public void detachCar() {
        if(cars.size()>0) {
            System.out.println(cars.get(cars.size() - 1) + "has been detached");
            cars.remove(cars.size() - 1);
        } else System.out.println("no cars left in trainset");
    }

    /**
     * Method implements sorting cars in trainset for further demonstrating in AppState.txt
     * @param cars cars of given trainset
     * @return sorted by loadWeight list of cars
     */
    public List<Car> getSortedCars(List<Car> cars) {
        return cars.stream().sorted((Car car1, Car car2) -> (int) (car1.getLoadWeight() - car2.getLoadWeight())).toList();

    }



    public void setMaxCars(int maxCars) {
        this.maxCars = maxCars;
    }

    public void setMaxElecNeededCars(int maxElecNeededCars) {
        this.maxElecNeededCars = maxElecNeededCars;
    }

    public void setMaxLoad(double maxLoad) {
        this.maxLoad = maxLoad;
    }


    @Override
    public void run() {
        while(locomotive.isRunning()) {
            locomotive.move();
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                locomotive.stop();
            }
        }
    }
    public void startTrain(Station destinationStation) {
        locomotive.setEndStation(destinationStation);
        locomotive.setDestinationStation(destinationStation);
        locomotive.setRunning(true);
        new Thread(this).start();
    }
}
