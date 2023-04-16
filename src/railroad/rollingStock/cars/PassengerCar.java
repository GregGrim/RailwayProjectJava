package railroad.rollingStock.cars;

public class PassengerCar extends Car {
    private int numberOfSeats;
    private int numberOfPeople;
    private final String transporter;
    public PassengerCar(String name) {
        super(name == null?"PassengerCar":name,
                "passengers, electricity",
                true);
        numberOfSeats=70;
        numberOfPeople=50-(int)(Math.random()*20);
        transporter="UkrZaliznytsia";
    }
    public void hasFreeSeat() {
        if(numberOfSeats - numberOfPeople > 0){
            System.out.println(getName()+" has at least 1 free seat");
        } else {
            System.out.println(getName()+" has no free seats");
        }
    }
    public void occupySeats(int num) {
        if(numberOfSeats - numberOfPeople - num >= 0){
            numberOfPeople+=num;
            System.out.println(num+" more seat(s) occupied in "+getName());
        } else {
            System.out.println(getName()+" has not enough free seats, only "+(numberOfSeats - numberOfPeople)+" left");
        }
    }
    public void addMoreSeats(int num) {
        numberOfSeats+=num;
    }
    public void getSummary() {
        System.out.println(getName());
        System.out.println("carWeight= "+carWeight);
        System.out.println("numberOfPeople= "+numberOfPeople+"\ntransporter: "+transporter);
        System.out.println("numberOfSeats= "+numberOfSeats);
        System.out.println("securityInfo: "+getSecurityInfo());
        System.out.println("electricalGreedNeed: "+(isElectricalGridNeed()?"Yes":"No"));
    }
}
