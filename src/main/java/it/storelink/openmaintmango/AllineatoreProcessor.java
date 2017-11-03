package it.storelink.openmaintmango;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import it.storelink.mango.ApiException;
import it.storelink.mango.api.utils.Pair;
import it.storelink.mango.api.utils.StringUtil;
import it.storelink.openmaint.OpenMaintAPI;
import it.storelink.openmaintmango.config.ConfigSingleton;
import it.storelink.openmaintmango.openmaint.client.sessions.Output;
import it.storelink.openmaintmango.xmlconfig.SensoreType;
import it.storelink.mango.api.DefaultApiImpl;
import it.storelink.mango.api.MangoRestApi;
import it.storelink.mango.model.PointValueModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class AllineatoreProcessor {
    private static Logger logger = LoggerFactory.getLogger(AllineatoreProcessor.class);

    private static MangoRestApi api;
    private static Boolean initialized = false;

    List<SensoreType> listaSensori = null;
    public AllineatoreProcessor( List<SensoreType> l) {
        listaSensori =  l;

        try {
            api = new DefaultApiImpl();
            api.setBasePath(ConfigSingleton.getInstance().getSystemParam_MANGO_BASE_URL());
            api.setBaseWSPath(ConfigSingleton.getInstance().getSystemParam_MANGO_BASE_WSURL());
            api.setDebugging(true);
            api.login(ConfigSingleton.getInstance().getSystemParam_MANGO_USR(),ConfigSingleton.getInstance().getSystemParam_MANGO_PWD() , true);
            initialized = true;
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    public void process() throws Exception{

        for (Iterator<SensoreType> iterator = listaSensori.iterator(); iterator.hasNext(); ) {
            SensoreType sensor =  iterator.next();
            String mangoSensorID = sensor.getMango().getRelativePath();
            List<PointValueModel> pointValueModels = api.getLatestPointValues(mangoSensorID, 1, true, false, false);
            String val = pointValueModels.toString();
        }
    }

    public static void main(String[] args) throws ApiException {

        OpenMaintAPI openMaintAPI = new OpenMaintAPI(ConfigSingleton.getInstance().getSystemParam_OPENMAINT_BASE_URL());
        Output response = openMaintAPI.login("admin", "pIPP0");
        logger.debug(response.toString());

        it.storelink.openmaintmango.openmaint.client.building.Output response2 = openMaintAPI.attributes(response.data._id);
        logger.debug(response.toString());
    }

    private static URI getOpenMaintBaseURI() {
        return UriBuilder.fromUri(ConfigSingleton.getInstance().getSystemParam_OPENMAINT_BASE_URL()).build();
    }
}
