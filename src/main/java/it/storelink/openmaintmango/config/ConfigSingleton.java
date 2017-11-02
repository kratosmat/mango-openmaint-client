package it.storelink.openmaintmango.config;

import java.io.InputStream;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConfigSingleton {
    private static Logger logger = LoggerFactory.getLogger(ConfigSingleton.class);
    private static ConfigSingleton sm_instance;

    private Properties map = new Properties();


    public static final String SCENARIO_REPEAT = "SCENARIO_REPEAT";
    public static final String INSTALL_PATH = "INSTALL_PATH";
    public static final String SENSORI_FOLDER = "SENSORI_FOLDER";
    public static final String OPENMAINT_BASE_URL = "OPENMAINT_BASE_URL";
    public static final String MANGO_BASE_URL = "MANGO_BASE_URL";
    public static final String MANGO_BASE_WSURL = "MANGO_BASE_WSURL";
    public static final String OPENMAINT_USR = "OPENMAINT_USR";
    public static final String OPENMAINT_PWD = "OPENMAINT_PWD";

    public static final String MANGO_USR = "MANGO_USR";
    public static final String MANGO_PWD = "MANGO_PWD";


    /**
     * Create and fill a new instance of the object by reading from props files.
     */
    private ConfigSingleton() {
        ClassLoader cl = getClass().getClassLoader();
        String fileName = "config.properties";

        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        if (is == null)
            is = ClassLoader.getSystemResourceAsStream(fileName);
        try {
            if (is == null) {
                cl = getTCL();
                is = cl.getResourceAsStream(fileName);
            }
            map.load(is);
            is.close();
        } catch (IllegalAccessException e) {
            logger.error("ERROR in loading configuration: " + e.getMessage(), e);
            sm_instance = null;
        } catch (InvocationTargetException e) {
            logger.error("ERROR in loading configuration: " + e.getMessage(), e);
            sm_instance = null;
        } catch (Throwable e) {
            logger.error("ERROR in loading configuration: " + e.getMessage(), e);
            sm_instance = null;
        }

    }

    /**
     * <pre>
     * This method returns the ClassLoader by reflection method invokation
     * </pre>
     *
     * @return ClassLoader - null if exception raise
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private static ClassLoader getTCL() throws IllegalAccessException, InvocationTargetException {
        // Are we running on a JDK 1.2 or later system?
        Method method = null;
        try {
            method = Thread.class.getMethod("getContextClassLoader", null);
        } catch (NoSuchMethodException e) {
            // We are running on JDK 1.1
            return null;
        }
        return (ClassLoader) method.invoke(Thread.currentThread(), null);
    }

    /**
     * <pre>
     * Gets the ConfigSingleton instance using singleton pattern
     * </pre>
     *
     * @return ConfigSigleton
     */
    public static ConfigSingleton getInstance() {
        if (sm_instance == null)
            sm_instance = new ConfigSingleton();
        return sm_instance;
    }

    /**
     * @param query String - key
     * @return String the value associated to the key
     */
    public Object getParam(String query) {
        return map.get(query);
    }

    public void setMap(Properties map) {
        this.map = map;
    }

    /**
     * Getter
     *
     * @return Properties - the mapped properties
     */
    public Properties getMap() {
        return map;
    }

    public String getSystemParam(String input) {
        String parametro = (String) map.get(input);
        return parametro;
    }


    public int getSystemParam_SCENARIO_REPEAT() {
        int toret = -1;
        String parametro = (String) map.get(ConfigSingleton.SCENARIO_REPEAT);
        if (parametro != null) {
            toret = (new Integer(parametro.trim())).intValue();
        }
        return toret;
    }

    public String getSystemParam_INSTALL_PATH() {
        String toret = "";
        String parametro = (String) map.get(ConfigSingleton.INSTALL_PATH);
        if (parametro != null) {
            toret = parametro.trim();
        }
        return toret;
    }

    public String getSystemParam_SENOSRI_FOLDER() {
        String toret = "";
        String parametro = (String) map.get(ConfigSingleton.SENSORI_FOLDER);
        if (parametro != null) {
            toret = parametro.trim();
        }
        return toret;
    }

    public String getSystemParam_OPENMAINT_BASE_URL() {
        String toret = "";
        String parametro = (String) map.get(ConfigSingleton.OPENMAINT_BASE_URL);
        if (parametro != null) {
            toret = parametro.trim();
        }
        return toret;
    }
    public String getSystemParam_MANGO_BASE_URL() {
        String toret = "";
        String parametro = (String) map.get(ConfigSingleton.MANGO_BASE_URL);
        if (parametro != null) {
            toret = parametro.trim();
        }
        return toret;
    }
    public String getSystemParam_MANGO_USR() {
        String toret = "";
        String parametro = (String) map.get(ConfigSingleton.MANGO_USR);
        if (parametro != null) {
            toret = parametro.trim();
        }
        return toret;
    }
    public String getSystemParam_MANGO_PWD() {
        String toret = "";
        String parametro = (String) map.get(ConfigSingleton.MANGO_PWD);
        if (parametro != null) {
            toret = parametro.trim();
        }
        return toret;
    }

    public String getSystemParam_OPENMAINT_USR() {
        String toret = "";
        String parametro = (String) map.get(ConfigSingleton.OPENMAINT_USR);
        if (parametro != null) {
            toret = parametro.trim();
        }
        return toret;
    }
    public String getSystemParam_OPENMAINT_PWD() {
        String toret = "";
        String parametro = (String) map.get(ConfigSingleton.OPENMAINT_PWD);
        if (parametro != null) {
            toret = parametro.trim();
        }
        return toret;
    }
    public String getSystemParam_MANGO_BASE_WSURL() {
        String toret = "";
        String parametro = (String) map.get(ConfigSingleton.MANGO_BASE_WSURL);
        if (parametro != null) {
            toret = parametro.trim();
        }
        return toret;
    }




    public static void main(String[] args) {


        String a_config = ConfigSingleton.getInstance().getSystemParam_INSTALL_PATH();
        logger.info("getSystemParam_INSTALL_PATH() :" + a_config);

        a_config = ConfigSingleton.getInstance().getSystemParam_SENOSRI_FOLDER();
        logger.info("getSystemParam_SENOSRI_FOLDER() :" + a_config);


        a_config = ConfigSingleton.getInstance().getSystemParam_OPENMAINT_BASE_URL();
        logger.info("getSystemParam_OPENMAINT_BASE_URL() :" + a_config);

        a_config = ConfigSingleton.getInstance().getSystemParam_MANGO_BASE_URL();
        logger.info("getSystemParam_MANGO_BASE_URL() :" + a_config);

        a_config = ConfigSingleton.getInstance().getSystemParam_MANGO_BASE_WSURL();
        logger.info("getSystemParam_MANGO_BASE_WSURL() :" + a_config);

        a_config = ConfigSingleton.getInstance().getSystemParam_MANGO_USR();
        logger.info("getSystemParam_MANGO_USR :" + a_config);

        a_config = ConfigSingleton.getInstance().getSystemParam_MANGO_PWD();
        logger.info("getSystemParam_MANGO_PWD() :" + a_config);

        a_config = ConfigSingleton.getInstance().getSystemParam_OPENMAINT_USR();
        logger.info("getSystemParam_OPENMAINT_USR() :" + a_config);

        a_config = ConfigSingleton.getInstance().getSystemParam_OPENMAINT_PWD();
        logger.info("getSystemParam_OPENMAINT_PWD() :" + a_config);

        int b_config = ConfigSingleton.getInstance().getSystemParam_SCENARIO_REPEAT();
        logger.info("getSystemParam_SCENARIO_REPEAT() :" + b_config);


    }

}