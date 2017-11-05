package it.storelink.openmaintmango;

import it.storelink.mango.ApiException;
import it.storelink.openmaint.OpenMaintAPI;
import it.storelink.openmaintmango.config.ConfigSingleton;
import it.storelink.openmaintmango.openmaint.client.sessions.Output;
import it.storelink.openmaintmango.xmlconfig.ObjectFactory;
import it.storelink.openmaintmango.xmlconfig.SensoreType;
import it.storelink.openmaintmango.xmlconfig.SensoriType;
import org.apache.log4j.Logger;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;


public class AllineatoreTest {
    private static Logger logger = Logger.getLogger(AllineatoreTest.class);


    public static void main(String[] args) throws ApiException {
        try {

            Map cache = new HashMap();
            cache.put("A","A");
            cache.put("A","B");
            Object o = cache.get("C");

            String formattedTime = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).format(new Date());
            logger.info(formattedTime);
        OpenMaintAPI openMaintAPI = new OpenMaintAPI(ConfigSingleton.getInstance().getSystemParam_OPENMAINT_BASE_URL());
        Output response = openMaintAPI.login("admin", "pIPP0");
        logger.debug(response.toString());

        String objId = "/classes/Building/cards/261257";


        String classId =  "/classes/Building";
       // it.storelink.openmaintmango.openmaint.client.classattributes.Output class_attributes = openMaintAPI.attributes(classId);
       // logger.debug(response.toString());

        //UPDATE
        String attributeName="status";
        String attributeType="STRING";
        String attributeValue="D";

        //INSERT

            File sensorFile = new File("C:\\iot\\git\\mango-openmaint-client\\sensors\\sensors_1.xml");
            JAXBContext jc = JAXBContext.newInstance(SensoriType.class);
            JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
            SensoriType sensoriType = ((JAXBElement<SensoriType>) jaxbContext.createUnmarshaller().unmarshal(sensorFile)).getValue();
            List<SensoreType> lista = sensoriType.getSensore();
            for (Iterator<SensoreType> iterator = lista.iterator(); iterator.hasNext(); ) {
                SensoreType sensor =  iterator.next();

                String opType= sensor.getOpemaint().getOperationType();
                String mangoVal = "7.9";

                if ("INSERT".equalsIgnoreCase(opType)){
                    AllineatoreProcessor.processInsert(openMaintAPI, sensor, mangoVal);
                } else   if ("UPDATE".equalsIgnoreCase(opType)){
                    AllineatoreProcessor.processUpdate(openMaintAPI, sensor , attributeValue);
                }
            }


        } catch (Exception e) {

            e.printStackTrace();
        } finally {
        }
    }



    private static URI getOpenMaintBaseURI() {
        return UriBuilder.fromUri(ConfigSingleton.getInstance().getSystemParam_OPENMAINT_BASE_URL()).build();
    }
}
