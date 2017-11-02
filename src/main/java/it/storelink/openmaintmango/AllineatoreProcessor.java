package it.storelink.openmaintmango;
import it.storelink.mango.api.DefaultApiImpl;
import it.storelink.mango.api.MangoRestApi;
import it.storelink.mango.model.PointValueModel;
import it.storelink.openmaintmango.config.ConfigSingleton;
import it.storelink.openmaintmango.openmaint.client.building.Output;
import it.storelink.openmaintmango.xmlconfig.SensoreType;
import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Iterator;
import java.util.List;


public class AllineatoreProcessor {
    private static Logger logger = LoggerFactory.getLogger(AllineatoreProcessor.class);

    private static MangoRestApi api;
    private static Boolean initialized = false;

    private ClientConfig clientConfig = new ClientConfig();
    private Client client = ClientBuilder.newClient(clientConfig);
    private WebTarget webTarget = client.target(getOpenMaintBaseURI());


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

    public static void fra2(String[] args) {
        ClientConfig clientConfig = new ClientConfig();
        // clientConfig.register(MyClientResponseFilter.class);
        // clientConfig.register(new AnotherClientFilter());

        Client client = ClientBuilder.newClient(clientConfig);
        //client.register(ThirdClientFilter.class);

        WebTarget webTarget = client.target(getOpenMaintBaseURI());
        // webTarget.register(FilterForExampleCom.class);
        WebTarget sessionsWebTarget = webTarget.path("/sessions/");
        //  WebTarget helloworldWebTarget = resourceWebTarget;
        //   WebTarget helloworldWebTarget = resourceWebTarget.path("helloworld");
        //   WebTarget helloworldWebTargetWithQueryParam =
        //           helloworldWebTarget.queryParam("greeting", "Hi World!");

        //  Invocation.Builder invocationBuilder =
        //   helloworldWebTarget.request(MediaType.APPLICATION_JSON);
        //   invocationBuilder.header("Content-Type", "application/json");

        // Response response = invocationBuilder.get();

        /*String response = webTarget.path("sessions").
                request().
                accept(MediaType.APPLICATION_JSON_TYPE).
                get(Response.class)
                .toString();*/
        User user = new User("admin", "pIPP0");

        Response response = sessionsWebTarget.request().post(Entity.entity(user, MediaType.APPLICATION_JSON));

        //System.out.println(response);
        //System.out.println(response.getStatus());
        // System.out.println(response.getStatusInfo());
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

    private static URI getOpenMaintBaseURI() {
        return UriBuilder.fromUri(ConfigSingleton.getInstance().getSystemParam_OPENMAINT_BASE_URL()).build();
    }
}
