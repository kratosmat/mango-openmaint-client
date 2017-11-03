package it.storelink.openmaintmango;

import it.storelink.openmaintmango.config.ConfigSingleton;
import it.storelink.openmaintmango.config.XMLFileFilter;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AllineatoreMain {

    private static Logger logger = Logger.getLogger(AllineatoreMain.class);

    public static void main(String[] args) {
        logger.info("Start");
        String path = ConfigSingleton.getInstance().getSystemParam_INSTALL_PATH() + ConfigSingleton.getInstance().getSystemParam_SENOSRI_FOLDER();
        File folder = new File(path);
        List<AllineatoreTask> l_task = new ArrayList<AllineatoreTask>();
        File[] listFiles = null;
        if (folder.exists()) {
            if (folder != null && folder.listFiles().length != 0) {
                listFiles = folder.listFiles(new XMLFileFilter());
                Arrays.sort(listFiles);
                for (int i = 0; i < listFiles.length; i++) {
                    File currentFile = (File) listFiles[i];
                    AllineatoreTask task = new AllineatoreTask(currentFile);
                    l_task.add(task);
                }
            }
        }

        AllineatoreThreadMonitor allineatoreThreadMonitor = new AllineatoreThreadMonitor(Thread.currentThread(),l_task);
        allineatoreThreadMonitor.start();
        logger.info("End");
    }
}
