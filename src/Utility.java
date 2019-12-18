import java.util.concurrent.TimeUnit;

public class Utility {
    public static void waitMilliseconds(long timeToSleep){
        TimeUnit time = TimeUnit.MICROSECONDS ;
        try {
            time.sleep(timeToSleep);
        }
        catch (InterruptedException e) {
        }
    }
}

