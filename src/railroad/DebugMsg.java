package railroad;

/**
 * Class created for splitting outputs by debug mod for convenient program debugging
 */
public class DebugMsg {
    private static int debugLevel = 0;
    public static void msg(Object txt, int level) {
        if(debugLevel>=level) {
            System.out.println(txt);
        }
    }
    public static void msg(Object txt) {
        msg(txt, 1);
    }

    public static void errMsg(Object txt, int level) {
        if (debugLevel >= level) {
            System.err.println(txt);
        }
    }
    public static void errMsg(Object txt) {
            errMsg(txt,1);
    }

    public static void setDebugLevel(int x) {
        debugLevel = x;
    }
}
