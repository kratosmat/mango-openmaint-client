package it.storelink.openmaintmango;


public interface AllineatoreThreadMonitorInterface {
    String getName();
    void start();
    void stop();
    void kill();
    boolean isRunning();
    boolean isStarted();
}
