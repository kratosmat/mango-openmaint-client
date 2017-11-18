package it.storelink.mango.api;

import it.storelink.mango.ApiException;
import it.storelink.mango.model.DataSourceModel;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.List;

/**
 * Created by kratoslink on 12/03/16.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class MangoBaseTest {

    protected static MangoRestApi api;
    protected static Boolean initialized = false;
    protected static Logger LOG = Logger.getLogger(MangoBaseTest.class);

    protected static String user = "admin";
    protected static String password = "@dmin123";
    protected static String baseHost = "storelink.ns0.it";
    protected static String basePort = "7779";


    /*
    protected static String user = "admin";
    protected static String password = "admin";
    protected static String baseHost = "localhost";
    protected static String basePort = "8080";
    */

    protected static String baseUrl = "http://" + baseHost + ":" + basePort + "/rest";
    protected static String wsUrl = "ws://" +  baseHost + ":" + basePort + "/rest/v1/websocket/point-value";

    @BeforeClass
    public static void init() {
        try {
            api = new DefaultApiImpl();
            api.setBasePath(baseUrl);
            api.setBaseWSPath(wsUrl);
            api.setDebugging(true);
            api.login(user, password);
            initialized = true;
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Test
    @Ignore
    public void _00_testGetAllDataSources() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        List<DataSourceModel> allDataSources = api.getAllDataSources();
        Assert.assertNotNull(allDataSources);
        //LOG.info(allDataSources.toString());
    }

    @AfterClass
    public static void close() throws ApiException {
        if(!initialized) return;
        if(api!=null) api.logout(user);
    }
}
