package it.storelink.mango.client;

import it.storelink.mango.ApiException;
import it.storelink.mango.api.ws.DefaultConsumer;
import it.storelink.mango.model.PointValueEventType;
import it.storelink.mango.model.PointValueRegistrationModel;
import it.storelink.mango.model.ResponsePointValueEventModel;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

@FixMethodOrder(MethodSorters.DEFAULT)
public class WebSocketAccentureTest extends  MangoBaseTest {

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

            PointValueRegistrationModel registrationModel1 = createPointValueRegistrationModel("DP_368107");
            BlockingQueue<ResponsePointValueEventModel> queue1 = new LinkedBlockingDeque<ResponsePointValueEventModel>();
            api.registerDataPoint(registrationModel1, queue1);
            DefaultConsumer c1 = new DefaultConsumer(queue1);
            Thread consumer1 = new Thread(c1);
            consumer1.start();

            api.startWSManager();

            downLatch.await(60, TimeUnit.SECONDS);

            api.shutdownWSManager();

            if(consumer1!=null) c1.setRunning(false);
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

}
