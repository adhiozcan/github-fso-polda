package id.net.iconpln.fso.polda.jobscheduler.job;

/**
 * Created by Ozcan on 16/02/2017.
 */

public interface ServiceApi {

    void start();

    void stop();

    void performJob();

    boolean isRunning();

    Runnable task();
}
