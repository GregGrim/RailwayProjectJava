package railroad;

import java.io.*;

import static java.lang.Thread.sleep;

public class Logger implements Runnable{
    private RailroadWorld world;
    private RandomAccessFile fil;
    public Logger(RailroadWorld world,String fileName) {
        this.world = world;
        try {
            fil = new RandomAccessFile(fileName, "rw");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                // TODO work with file
                fil.seek(0);
                fil.writeBytes("something" + Math.random());
//                bw.newLine();

                sleep(500);
            } catch (InterruptedException e) {
                try {
                    fil.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                throw new RuntimeException(e);

            } catch (IOException e) {

                throw new RuntimeException(e);
            }
        }
    }
}
