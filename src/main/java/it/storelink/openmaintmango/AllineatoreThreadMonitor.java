package it.storelink.openmaintmango;

import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;

public class AllineatoreThreadMonitor implements AllineatoreThreadMonitorInterface {
    private Thread m_thrd = null;
    private List<AllineatoreTask> l_task = null;
    private static Logger logger = Logger.getLogger(AllineatoreThreadMonitor.class);

    public AllineatoreThreadMonitor(Thread thrd, List<AllineatoreTask> l ) {
        m_thrd = thrd;
        l_task = l;
    }

    public String getName() {
        return "JMX Controlled App";
    }

    public void start() {
        for (Iterator<AllineatoreTask> iterator = l_task.iterator(); iterator.hasNext(); ) {
            AllineatoreTask m_task =  iterator.next();
            if (m_task == null || !m_task.isRunning()) {
                Thread thread = new Thread(m_task);
                thread.start();
            }
        }
    }

    public void kill() {
        logger.info("remote kill called");
        m_thrd.interrupt();
    }

    public void stop() {

        logger.info("remote stop called");
        for (Iterator<AllineatoreTask> iterator = l_task.iterator(); iterator.hasNext(); ) {
            AllineatoreTask m_task =  iterator.next();
            m_task.kill();
        }
    }

    public boolean isRunning() {
        return Thread.currentThread().isAlive();
    }

    public boolean isStarted() {
        boolean toRet=true;
        for (Iterator<AllineatoreTask> iterator = l_task.iterator(); iterator.hasNext(); ) {
            AllineatoreTask m_task =  iterator.next();
            if ( m_task==null || !m_task.isRunning()) toRet=false;
        }
       return toRet;
    }


}