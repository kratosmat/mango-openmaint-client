package it.storelink.mango.client;

import it.storelink.mango.ApiException;
import it.storelink.mango.api.DefaultApiImpl;
import it.storelink.mango.api.MangoRestApi;
import it.storelink.mango.model.*;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import it.storelink.mango.api.ws.DefaultConsumer;

@FixMethodOrder(MethodSorters.DEFAULT)
public class WebSocketTest {

    private static MangoRestApi api;
    private static Logger LOG = LoggerFactory.getLogger(WebSocketTest.class);
    private static Boolean initialized = false;

    /*
    private static String user = "mario";
    private static String password = "ro$$i";
    private static String baseHost = "79.60.49.49";
    private static String basePort = "7778";
    */

    private static String user = "admin";
    private static String password = "admin";
    private static String baseHost = "192.168.178.30";
    private static String basePort = "8080";

    private static String baseUrl = "http://" + baseHost + ":" + basePort + "/rest";
    private static String wsUrl = "ws://" +  baseHost + ":" + basePort + "/rest/v1/websocket/point-value";

    @BeforeClass
    public static void init() {
        try {
            api = new DefaultApiImpl();
            api.setBasePath(baseUrl);
            api.setBaseWSPath(wsUrl);
            api.setDebugging(true);
            api.login(user, password, true);
            initialized = true;
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private PointValueRegistrationModel createPointValueRegistrationModel(String xid) {
        PointValueRegistrationModel registrationModel = new PointValueRegistrationModel();
        registrationModel.setDataPointXid(xid);
        List<PointValueEventType> types = new ArrayList<PointValueEventType>();
        types.add(PointValueEventType.CHANGE);
        //types.add(PointValueEventType.UPDATE);
        types.add(PointValueEventType.INITIALIZE);
        types.add(PointValueEventType.SET);
        types.add(PointValueEventType.BACKDATE);
        types.add(PointValueEventType.REGISTERED);
        types.add(PointValueEventType.TERMINATE);
        registrationModel.setEventTypes(types);

        return registrationModel;
    }



    @Test
    public void testStartWS() throws ApiException, InterruptedException {
        Thread.sleep(1000);
        if(!initialized) {
            Assert.fail();
            return;
        }
        try {
            CountDownLatch downLatch = new CountDownLatch(1);

            //Start manager
            api.startWSManager();


            PointValueRegistrationModel registrationModel1 = createPointValueRegistrationModel("DP_519977");
            //create an user queue
            BlockingQueue<ResponsePointValueEventModel> queue1 = new LinkedBlockingDeque<ResponsePointValueEventModel>();
            //register a datapoint to write on user queue
            api.registerDataPoint(registrationModel1, queue1);
            DefaultConsumer c1 = new DefaultConsumer(queue1);
            Thread consumer1 = new Thread(c1);
            consumer1.start();

            downLatch.await(10, TimeUnit.SECONDS);

            //register a second datapoint
            PointValueRegistrationModel registrationModel2 = createPointValueRegistrationModel("DP_512982");
            BlockingQueue<ResponsePointValueEventModel> queue2 = new LinkedBlockingDeque<ResponsePointValueEventModel>();
            api.registerDataPoint(registrationModel2, queue2);
            DefaultConsumer c2 = new DefaultConsumer(queue2);
            Thread consumer2 = new Thread(c2);
            consumer2.start();

            downLatch.await(10, TimeUnit.SECONDS);

            //unregister the first data point
            api.unRegisterDataPoint("DP_519977");
            if(consumer1!=null) c1.setRunning(false);

            downLatch.await(10, TimeUnit.SECONDS);

            //register the first one again, but on the same queue of second one
            //in this way we can register multiple datapoints to write on a single queue
            api.registerDataPoint(registrationModel1, queue2);

            downLatch.await(10, TimeUnit.SECONDS);

            //shutdown the server, if there are running registration they'll be unregistered
            api.shutdownWSManager();

            downLatch.await(10, TimeUnit.SECONDS);

            if(consumer2!=null) c2.setRunning(false);
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @AfterClass
    public static void close() throws ApiException {
        if(!initialized) return;
        if(api!=null) api.logout();
    }

}
