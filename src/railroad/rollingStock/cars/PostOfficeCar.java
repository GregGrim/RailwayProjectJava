package railroad.rollingStock.cars;

public class PostOfficeCar extends Car{
    private String postCompany;
    private int mailsToSend;
    public PostOfficeCar(String name) {
        super(name == null?"PostOfficeCar":name, "electricity", true);
        postCompany="NovaPost";
        mailsToSend= (int) (10+Math.random()*20);
    }

    public void sendMail(String receiverName) {
        System.out.println("Mail to "+receiverName+"sent");
        mailsToSend--;
    }
    public void mailsLeft() {
        System.out.println(mailsToSend+"left to be sent");
    }
    public void getSummary() {
        System.out.println(getName());
        System.out.println("carWeight= "+getCarWeight());
        System.out.println("loadWeight= "+getLoadWeight()+"postCompany:"+postCompany);
        System.out.println("mailsToSend: "+mailsToSend);
        System.out.println("securityInfo: "+getSecurityInfo());
        System.out.println("electricalGreedNeed"+(isElectricalGridNeed()?"Yes":"No"));
    }
}
