package it.storelink.openmaintmango;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.storelink.mango.api.DefaultApiImpl;
import it.storelink.mango.api.MangoRestApi;
import it.storelink.mango.model.PointValueModel;
import it.storelink.openmaint.OpenMaintAPI;
import it.storelink.openmaintmango.config.ConfigSingleton;
import it.storelink.openmaintmango.openmaint.client.sessions.Output;
import it.storelink.openmaintmango.xmlconfig.FieldType;
import it.storelink.openmaintmango.xmlconfig.SensoreType;
import org.apache.log4j.Logger;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBElement;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;


public class AllineatoreProcessor {

    private static Logger logger = Logger.getLogger(AllineatoreProcessor.class);

    private static MangoRestApi mangoApi;
    private static Boolean mangoInitialized = false;
    private static OpenMaintAPI openMaintAPI;
    private static Boolean openMaintInitialized = false;
    List<SensoreType> listaSensori = null;

    private static Map cache = new HashMap();

    public AllineatoreProcessor( List<SensoreType> l) {

        listaSensori =  l;

        try {
            mangoApi = new DefaultApiImpl();
            mangoApi.setBasePath(ConfigSingleton.getInstance().getSystemParam_MANGO_BASE_URL());
            mangoApi.setBaseWSPath(ConfigSingleton.getInstance().getSystemParam_MANGO_BASE_WSURL());
            mangoApi.setDebugging(true);
            mangoApi.login(ConfigSingleton.getInstance().getSystemParam_MANGO_USR(),ConfigSingleton.getInstance().getSystemParam_MANGO_PWD());
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
            if (! isInCache(sensor,  mangoVal.toString()) ){
                if ("INSERT".equalsIgnoreCase(opType)){
                    processInsert(openMaintAPI, sensor, mangoVal.toString());
                } else   if ("UPDATE".equalsIgnoreCase(opType)){
                    processUpdate(openMaintAPI,  sensor , mangoVal.toString() );
                }
                putInCache(sensor, mangoVal.toString());
            }
        }
    }

    private void putInCache(SensoreType sensor, String val) {
        String sXid = sensor.getMango().getXid();
        String opType= sensor.getOpemaint().getOperationType();
        String opRelPath= sensor.getOpemaint().getRelativePath();
        String keyCache = sXid +"_"+ opType +"_"+   opRelPath;
        cache.put(keyCache , val);
    }

    private boolean isInCache(SensoreType sensor,String val) {
        String sXid = sensor.getMango().getXid();
        String opType= sensor.getOpemaint().getOperationType();
        String opRelPath= sensor.getOpemaint().getRelativePath();
        String keyCache = sXid +"_"+ opType +"_"+   opRelPath;
        String sXidVal = (String)cache.get( keyCache );
        if ( sXidVal != null && sXidVal.equalsIgnoreCase(val)  ) {
            logger.info("sensor " +  keyCache + " is in cache ");
            return true;
        } else{
            logger.info("sensor " +  keyCache + " is not in cache. Val in cache :  " +sXidVal +" . Val : "+val);
            return false;
        }

    }


    public static void processInsert(OpenMaintAPI openMaintAPI, SensoreType sensor, String mangoVal) throws JsonProcessingException {
        String attName= sensor.getOpemaint().getAttributeName();
        String attType= sensor.getOpemaint().getAttributeType();
        String relPath = sensor.getOpemaint().getRelativePath();
        String className = relPath.substring(relPath.indexOf("/classes/") + "/classes/".length()) ;
        className = className.substring(0,className.indexOf("/") );
        logger.info(className);
        Map<String,Object> mapFields = new HashMap<>();
        convertType(mapFields, attName, attType, mangoVal);
        List content = sensor.getOpemaint().getContent();
        for (Iterator iterator1 = content.iterator(); iterator1.hasNext(); ) {
            Object next =  iterator1.next();
            if (next instanceof JAXBElement) {
                JAXBElement<FieldType> fieldType = (JAXBElement) next;
                FieldType field = fieldType.getValue();
                String fName = field.getName();
                String fType = field.getType();
                String fValue = field.getValue();

                convertType(mapFields, fName, fType, fValue);
            }
        }
        String jsoninsert = new ObjectMapper().writeValueAsString(mapFields);
        logger.info(jsoninsert);
        openMaintAPI.insertObj(className,jsoninsert);
    }

    private static void convertType(Map<String, Object> mapFields, String fName, String fType, String fValue) {

        if (    fType.toUpperCase().startsWith("STRING") ||
                fType.toUpperCase().startsWith("TIME") ||
                fType.toUpperCase().startsWith("TIMESTAMP") ||
                fType.toUpperCase().startsWith("TEXT")  ||
                fType.toUpperCase().startsWith("CHAR")  ||
                fType.toUpperCase().startsWith("DATE") ||
                fType.toUpperCase().startsWith("INET")  ){

           if(fValue.toUpperCase().startsWith(fType) &&  ( "TIMESTAMP".toUpperCase().startsWith(fValue) || "DATE".toUpperCase().startsWith(fValue) ) ) {
               String formattedTime = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).format(new Date());
               mapFields.put(fName,formattedTime);
           }else {
               mapFields.put(fName,fValue);
           }
        }else if ( fType.toUpperCase().startsWith("REFERENCE") ) {
            //TO_DO
        } else if ( fType.toUpperCase().startsWith("BOOLEAN") ) {
          //  Boolean intValue = new Boolean(fValue);
            mapFields.put(fName,fValue);
        }else if ( fType.toUpperCase().startsWith("DECIMAL") ) {
            int i1= fType.toUpperCase().indexOf("(");
            int i2= fType.toUpperCase().indexOf(",");
            int i3= fType.toUpperCase().indexOf(")");
            String sPrecision = fType.substring(i1+1,i2);
            String sScale = fType.substring(i2+1,i3);
            int precision = Integer.parseInt(sPrecision);
            int scale = Integer.parseInt(sScale);
            BigDecimal intValue = new BigDecimal(fValue);
            BigDecimal bValue =  intValue.round(new MathContext(precision, RoundingMode.HALF_UP)).setScale(scale, RoundingMode.HALF_UP);
            mapFields.put(fName,bValue);
        }else if ( fType.toUpperCase().startsWith("DOUBLE") ) {
            Double intValue = new Double(fValue);
            mapFields.put(fName,intValue);
        } else  {
            // FOREIGNKEY, INT, LOOKUP
            Integer intValue = new Integer(fValue);
            mapFields.put(fName,intValue);
        }
    }

    public static void processUpdate(OpenMaintAPI openMaintAPI,  SensoreType sensor,String attributeValue) throws JsonProcessingException {
        Map<String,Object> map = new HashMap<>();
        String objId= sensor.getOpemaint().getRelativePath();
        String attributeName=sensor.getOpemaint().getAttributeName();
        String attributeType=sensor.getOpemaint().getAttributeType();
        convertType(map, attributeName, attributeType, attributeValue);
        String json = new ObjectMapper().writeValueAsString(map);
        logger.info(json);
        openMaintAPI.updateObj(objId, json);
    }

    private static URI getOpenMaintBaseURI() {
        return UriBuilder.fromUri(ConfigSingleton.getInstance().getSystemParam_OPENMAINT_BASE_URL()).build();
    }
}
