package it.storelink.openmaintmango.config;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Calendar;

/**
 * A class that implements the Java FileFilter interface.
 */
public class XMLFileFilter implements FileFilter {

    private Logger logger = Logger.getLogger(XMLFileFilter.class);

    private final String[] okFileExtensions = new String[]{"XML"};
    private String t = "";
    private static long timeInLong = 0;

    public XMLFileFilter(String timestamp) {
        t = timestamp;
    }
    public XMLFileFilter() {

    }
    public boolean accept(File file) {
        long timeInLongIntoTheLoop = 0;
        for (String extension : okFileExtensions) {
            if (file.getName().toUpperCase().endsWith(extension) && file.getName().toUpperCase().indexOf(t) > -1) {
                long prevSize = 0;
                long currentSize = 0;
                timeInLong = Calendar.getInstance().getTimeInMillis();
                while (true) {
                    timeInLongIntoTheLoop = Calendar.getInstance().getTimeInMillis();
                    //if((timeInLongIntoTheLoop-timeInLong>5*60*1000) && currentSize==0){
                    if ((timeInLongIntoTheLoop - timeInLong > 2000) && currentSize == 0) {
                        //il file da cercare dopo 5 minuti ha ancora come dimensione 0 byte quindi esco!
                        return false;
                    }
                    File currentFile = new File(file.getAbsolutePath());
                    currentSize = currentFile.length();
                    if (prevSize != 0 && prevSize == currentSize) {
                        break;
                    }
                    prevSize = currentSize;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
                return true;
            }
        }
        return false;
    }


}
