package railroad;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static java.lang.Thread.sleep;

public class Logger implements Runnable{
    private RailroadWorld world;
    private OutputStream ostream;
    public Logger(RailroadWorld world,String fileName) {
        this.world = world;
        try {
            ostream = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            // TODO work with file

            sleep(5000);
        } catch (InterruptedException e) {
            try {
                ostream.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);

        }
    }
}
