package railroad;

import railroad.rollingStock.Trainset;

import java.io.*;

import static java.lang.Thread.sleep;

/**
 * Class that implements reporting basic info about world each 5sec in AppState.txt
 */
public class Logger implements Runnable{
    private final RailroadWorld world;
    private boolean isLogging;
    private final String fileName;
    public Logger(RailroadWorld world,String fileName) {
        this.world = world;
        this.fileName=fileName;
        this.isLogging=true;
        new Thread(this).start();

    }

    @Override
    public void run() {
        while (isLogging) {
            try {
                sleep(1000);
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
                sleep(4000);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
