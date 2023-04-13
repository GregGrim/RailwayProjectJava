package railroad.rollingStock.cars;

public class PassengerCar extends Car {
    private int numberOfSeats;
    private int numberOfPeople;
    private String transporter;
    public PassengerCar(String name) {
        super(name == null?"PassengerCar":name,
                "passengers, electricity",
                true);
        numberOfSeats=70;
        numberOfPeople=50-(int)(Math.random()*20);
        transporter="UkrZaliznytsia";
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public boolean hasFreeSeat() {
        if(numberOfSeats - numberOfPeople > 0){
            System.out.println(getName()+"has at least 1 free seat");
            return true;
        } else {
            System.out.println(getName()+"has no free seats");
            return false;
        }
    }
    public void occupySeats(int num) {
        if(numberOfSeats - numberOfPeople - num >= 0){
            System.out.println(num+" more seat(s) occupied in "+getName());
        } else {
            System.out.println(getName()+"has not enough free seats, only"+(numberOfSeats - numberOfPeople)+"left");
        }
    }
    public void getSummary() {
        System.out.println(getName());
        System.out.println("carWeight= "+getCarWeight());
        System.out.println("numberOfPeople= "+getNumberOfPeople()+"transporter:"+transporter);
        System.out.println("numberOfSeats= "+getNumberOfSeats());
        System.out.println("securityInfo: "+getSecurityInfo());
        System.out.println("electricalGreedNeed"+(isElectricalGridNeed()?"Yes":"No"));
    }
}
