package it.storelink.openmaintmango;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.storelink.mango.ApiException;
import it.storelink.openmaint.OpenMaintAPI;
import it.storelink.openmaintmango.config.ConfigSingleton;
import it.storelink.openmaintmango.openmaint.client.sessions.Output;
import it.storelink.openmaintmango.xmlconfig.FieldType;
import it.storelink.openmaintmango.xmlconfig.ObjectFactory;
import it.storelink.openmaintmango.xmlconfig.SensoreType;
import it.storelink.mango.api.DefaultApiImpl;
import it.storelink.mango.api.MangoRestApi;
import it.storelink.mango.model.PointValueModel;
import it.storelink.openmaintmango.xmlconfig.SensoriType;
import org.apache.log4j.Logger;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class AllineatoreProcessor {
    private static Logger logger = Logger.getLogger(AllineatoreProcessor.class);

    private static MangoRestApi mangoApi;
    private static Boolean mangoInitialized = false;
    private static OpenMaintAPI openMaintAPI;
    private static Boolean openMaintInitialized = false;
    List<SensoreType> listaSensori = null;

    public AllineatoreProcessor( List<SensoreType> l) {

        listaSensori =  l;

        try {
            mangoApi = new DefaultApiImpl();
            mangoApi.setBasePath(ConfigSingleton.getInstance().getSystemParam_MANGO_BASE_URL());
            mangoApi.setBaseWSPath(ConfigSingleton.getInstance().getSystemParam_MANGO_BASE_WSURL());
            mangoApi.setDebugging(true);
            mangoApi.login(ConfigSingleton.getInstance().getSystemParam_MANGO_USR(),ConfigSingleton.getInstance().getSystemParam_MANGO_PWD() , true);
            mangoInitialized = true;

            openMaintAPI = new OpenMaintAPI(ConfigSingleton.getInstance().getSystemParam_OPENMAINT_BASE_URL());
            Output response = openMaintAPI.login("admin", "pIPP0");

            openMaintInitialized = true;
            logger.debug(response.toString());

        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    public void process() throws Exception{
        logger.info("process started");
        for (Iterator<SensoreType> iterator = listaSensori.iterator(); iterator.hasNext(); ) {
            SensoreType sensor =  iterator.next();
            String mangoSensorID = sensor.getMango().getXid();
            List<PointValueModel> pointValueModels = mangoApi.getLatestPointValues(mangoSensorID, 1, true, false, false);
            Object mangoVal = null;
            for (Iterator<PointValueModel> pointValueModelIterator = pointValueModels.iterator(); pointValueModelIterator.hasNext(); ) {
                PointValueModel next =  pointValueModelIterator.next();
                mangoVal =next.getValue();
            }
            logger.info("Get value : " + mangoVal);
            String opType= sensor.getOpemaint().getOperationType();

            if ("INSERT".equalsIgnoreCase(opType)){
                processInsert(openMaintAPI, sensor, mangoVal.toString());
            } else   if ("UPDATE".equalsIgnoreCase(opType)){
                processUpdate(openMaintAPI,  sensor , mangoVal.toString() );
            }
        }
    }

    public static void main(String[] args) throws ApiException {
        try {
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
                    processInsert(openMaintAPI, sensor, mangoVal);
                } else   if ("UPDATE".equalsIgnoreCase(opType)){
                    processUpdate(openMaintAPI, sensor , attributeValue);
                }
            }


        } catch (Exception e) {

            e.printStackTrace();
        } finally {
        }
    }

    private static void processInsert(OpenMaintAPI openMaintAPI, SensoreType sensor, String mangoVal) throws JsonProcessingException {
        String attName= sensor.getOpemaint().getAttributeName();
        String attType= sensor.getOpemaint().getAttributeType();
        String relPath = sensor.getOpemaint().getRelativePath();
        String className = relPath.substring(relPath.indexOf("/classes/") + "/classes/".length()) ;
        className = className.substring(0,className.indexOf("/") );
        logger.info(className);
        Map<String,Object> mapFields = new HashMap<>();
        if (    attType.equalsIgnoreCase("STRING") ||
                attType.equalsIgnoreCase("TIME") ||
                attType.equalsIgnoreCase("TIMESTAMP") ||
                attType.equalsIgnoreCase("TEXT")  ){
            mapFields.put(attName,mangoVal);
        }else if ( attType.equalsIgnoreCase("REFERENCE") ) {
            //TO_DO
        } else {
            Integer intValue = new Integer(mangoVal);
            mapFields.put(attName,intValue);
        }
        List content = sensor.getOpemaint().getContent();
        for (Iterator iterator1 = content.iterator(); iterator1.hasNext(); ) {
            Object next =  iterator1.next();
            if (next instanceof JAXBElement) {
                JAXBElement<FieldType> fieldType = (JAXBElement) next;
                FieldType field = fieldType.getValue();
                String fName = field.getName();
                String fValue = field.getValue();
                String fType = field.getType();
                if (    fType.equalsIgnoreCase("STRING") ||
                        fType.equalsIgnoreCase("TIME") ||
                        fType.equalsIgnoreCase("TIMESTAMP") ||
                        fType.equalsIgnoreCase("TEXT")  ){
                    mapFields.put(fName,fValue);
                }else if ( fType.equalsIgnoreCase("REFERENCE") ) {
                    //TO_DO
                } else {
                    Integer intValue = new Integer(fValue);
                    mapFields.put(fName,intValue);
                }
            }
        }
        String jsoninsert = new ObjectMapper().writeValueAsString(mapFields);
        logger.info(jsoninsert);
        openMaintAPI.insertObj(className,jsoninsert);
    }

    private static void processUpdate(OpenMaintAPI openMaintAPI,  SensoreType sensor,String attributeValue) throws JsonProcessingException {
        Map<String,Object> map = new HashMap<>();
        String objId= sensor.getOpemaint().getRelativePath();
        String attributeName=sensor.getOpemaint().getAttributeName();
        String attributeType=sensor.getOpemaint().getAttributeType();

        if ( attributeType.equalsIgnoreCase("STRING") || attributeType.equalsIgnoreCase("TIME") || attributeType.equalsIgnoreCase("TIMESTAMP") || attributeType.equalsIgnoreCase("TEXT")  ){
            map.put(attributeName,attributeValue);
        } else if ( attributeType.equalsIgnoreCase("REFERENCE") ) {
            //TO_DO
        } else {
            Integer attributeIntValue = new Integer(attributeValue);
            map.put(attributeName,attributeIntValue);
        }
        String json = new ObjectMapper().writeValueAsString(map);
        logger.info(json);
        openMaintAPI.updateObj(objId, json);
    }

    private static URI getOpenMaintBaseURI() {
        return UriBuilder.fromUri(ConfigSingleton.getInstance().getSystemParam_OPENMAINT_BASE_URL()).build();
    }
}
