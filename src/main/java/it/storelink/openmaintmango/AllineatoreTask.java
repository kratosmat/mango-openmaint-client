package it.storelink.openmaintmango;


import com.sun.jersey.api.client.Client;
import it.storelink.openmaintmango.config.ConfigSingleton;
import it.storelink.openmaintmango.xmlconfig.SensoreType;
import it.storelink.openmaintmango.xmlconfig.SensoriType;
import it.storelink.openmaintmango.xmlconfig.ObjectFactory;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import java.io.File;
import java.util.List;

public class AllineatoreTask implements Runnable {
    File sensorFile = null;
    AllineatoreProcessor allineatoreProcessor;
    public AllineatoreTask(File f) {
        sensorFile =  f;
        try {
            JAXBContext jc = JAXBContext.newInstance(SensoriType.class);
            JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
            SensoriType sensoriType = ((JAXBElement<SensoriType>) jaxbContext.createUnmarshaller().unmarshal(sensorFile)).getValue();
            List<SensoreType> lista = sensoriType.getSensore();
            allineatoreProcessor = new AllineatoreProcessor(lista);

        } catch (Exception e) {
            isRunning = false;
            e.printStackTrace();
        } finally {
        }
    }
    private static Logger logger = Logger.getLogger(AllineatoreTask.class);
    public boolean isRunning() {
        return isRunning;
    }
    private boolean isRunning = false;

    public void run() {
        isRunning = true;
        int reapet = ConfigSingleton.getInstance().getSystemParam_SCENARIO_REPEAT();

        int count = 1;
        while (true && isRunning) {
            //do work
            logger.info("isRunning: " + isRunning);
            if (reapet > -1 && count > reapet) {
                kill();
                break;
            } else {

                try {

                    long ts = System.currentTimeMillis();
                    allineatoreProcessor.process();
                    logger.info("Processo di allineamento termitao in "+(System.currentTimeMillis()-ts) + " ms");

                } catch (Exception e) {
                    isRunning = false;
                    e.printStackTrace();
                } finally {
                }
            }
            count++;
            try {
                Thread.sleep(ConfigSingleton.getInstance().getSystemParam_SCENARIO_SLEEP());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void kill() {
        isRunning = false;
    }


}