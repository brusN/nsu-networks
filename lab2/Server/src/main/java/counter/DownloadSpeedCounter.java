package counter;

import constraints.Constraints;

import java.util.Timer;
import java.util.TimerTask;

public class DownloadSpeedCounter {
    private Timer timer;
    private final String timerName;
    private final long period; // in ms
    private long startTime;
    private long lastInstanceSpeedTimeUpdate;
    private long totalReceivedBytes; // total counted bytes for whole counting time
    private long lastReceivedBytes; // for the last period
    private double instanceSpeed; // bytes per second
    private double averageSpeed; // bytes per second

    private synchronized void printStats() {
        System.out.println("[" + timerName + "] AVG: " + averageSpeed + " | INSTANCE: " + instanceSpeed + " MB/s");
    }

    public synchronized void start() {
        startTime = System.currentTimeMillis();
        lastInstanceSpeedTimeUpdate = startTime;
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                totalReceivedBytes += lastReceivedBytes;
                averageSpeed = (double) (totalReceivedBytes * Constraints.SECOND) / ((System.currentTimeMillis() - startTime) * Constraints.MB);
                lastReceivedBytes = 0;
                printStats();
            }
        };
        timer.schedule(timerTask, period, period);
    }

    public synchronized void stop() {
        timer.cancel();
        totalReceivedBytes += lastReceivedBytes;
        long now = System.currentTimeMillis();
        averageSpeed = (double) ((totalReceivedBytes) * Constraints.SECOND) / ((now - startTime) * Constraints.MB);
        if (now - startTime < period) {
            instanceSpeed = averageSpeed;
        }
        printStats();
    }

    public synchronized long getReceivedBytes() {
        return totalReceivedBytes;
    }

    public synchronized double getInstanceSpeed() {
        return instanceSpeed;
    }

    public synchronized double getAverageSpeed() {
        return averageSpeed;
    }

    public synchronized long getUpdatePeriod() {
        return period;
    }

    public DownloadSpeedCounter(String timerName, long updatePeriod) {
        this.timerName = timerName;
        period = updatePeriod;
        totalReceivedBytes = 0;
        instanceSpeed = 0;
        averageSpeed = 0;
    }

    public synchronized void updateReceivedBytes(long newReceivedBytes) {
        lastReceivedBytes += newReceivedBytes;
        long now = System.currentTimeMillis();
        if (now - lastInstanceSpeedTimeUpdate > 1000) {
            instanceSpeed = (double) (lastReceivedBytes * Constraints.SECOND) / ((now - lastInstanceSpeedTimeUpdate) * Constraints.MB);
            lastInstanceSpeedTimeUpdate = now;
        }
    }
}
