package railroad;

import railroad.rollingStock.Trainset;

import java.io.*;

import static java.lang.Thread.sleep;

public class Logger implements Runnable{
    private RailroadWorld world;
    private FileOutputStream fil;

    private String fileName;
    public Logger(RailroadWorld world,String fileName) {
        this.world = world;
        this.fileName=fileName;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                FileOutputStream fil = new FileOutputStream(fileName,false);
                OutputStreamWriter osw = new OutputStreamWriter(fil);
                for (Trainset trainset : world.getSortedTrainsets()) {
                    osw.write(trainset+"\n");
                    osw.write("Cars: "+trainset.getSortedCars()+"\n");
                    osw.write("On the route: "+trainset.getLocomotive().getRoute()+
                            "\nOn "+ trainset.getLocomotive().getCurrentConnection()+"\n");
                    osw.write(Math.min(trainset.getLocomotive().getDistance() * 100 /
                            trainset.getLocomotive().getCurrentConnection().getDistance(),100)
                            +"% of connection passed\n");
                    osw.write(Math.min(trainset.getLocomotive().getRouteDistancePassed() * 100 /
                            trainset.getLocomotive().getRouteDistance(),100)+
                            "% of route passed\n");
                    osw.write("Status: "+trainset.getLocomotive().getStatus()+"\n\n");
                }
                osw.close();
                fil.close();
                sleep(5000);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
