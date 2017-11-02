package it.storelink.openmaintmango;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import it.storelink.mango.api.DefaultApiImpl;
import it.storelink.mango.api.MangoRestApi;
import it.storelink.mango.model.PointValueModel;
import it.storelink.openmaintmango.config.ConfigSingleton;
import it.storelink.openmaintmango.openmaint.client.building.Output;
import it.storelink.openmaintmango.xmlconfig.SensoreType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Iterator;
import java.util.List;


public class AllineatoreProcessor {

    private static Logger logger = LoggerFactory.getLogger(AllineatoreProcessor.class);

    private static MangoRestApi api;
    private static Boolean initialized = false;

    //private Client client = Client.create();
    //private WebResource webTarget = client.resource(getOpenMaintBaseURI());


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

    /*
    public static void fra2(String[] args) {
        ClientConfig clientConfig = new ClientConfig();

        Client client = ClientBuilder.newClient(clientConfig);

        WebTarget webTarget = client.target(getOpenMaintBaseURI());
        WebTarget sessionsWebTarget = webTarget.path("/sessions/");
        User user = new User("admin", "pIPP0");

        Response response = sessionsWebTarget.request().post(Entity.entity(user, MediaType.APPLICATION_JSON));
        it.storelink.openmaintmango.openmaint.client.sessions.Output sessions = response.readEntity(it.storelink.openmaintmango.openmaint.client.sessions.Output.class);
        System.out.println(sessions.data._id);

        WebTarget buildingWebTarget = webTarget.path("/classes/Building/attributes/");

        Invocation.Builder invocationBuilder =
                buildingWebTarget.request(MediaType.APPLICATION_JSON);
        invocationBuilder.header("CMDBuild-Authorization", sessions.data._id);
        Response responseGet = invocationBuilder.get();

        System.out.println(responseGet);
        System.out.println(responseGet.getStatus());
        System.out.println(responseGet.getStatusInfo());
        Output building = responseGet.readEntity(Output.class);
        System.out.println(building.meta.total);
    }
    */

    private static URI getOpenMaintBaseURI() {
        return UriBuilder.fromUri(ConfigSingleton.getInstance().getSystemParam_OPENMAINT_BASE_URL()).build();
    }
}
