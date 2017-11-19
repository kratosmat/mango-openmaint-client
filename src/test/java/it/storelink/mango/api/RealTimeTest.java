package it.storelink.mango.api;

import it.storelink.mango.ApiException;
import it.storelink.mango.model.RealTimeModel;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

/**
 * Created by kratoslink on 12/03/16.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RealTimeTest extends MangoBaseTest {

    @Test
    public void _01_test() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        List<RealTimeModel> realTimeModels = api.query();
        Assert.assertNotNull(realTimeModels);
        LOG.info(realTimeModels.toString());
    }

    @Test
    public void _02_test() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        RealTimeModel realTimeModel = api.get("DP_304268");
        Assert.assertNotNull(realTimeModel);
        LOG.info(realTimeModel.toString());
    }

    @Test
    public void _03_test() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        List<RealTimeModel> realTimeModels = api.getAll(100);
        Assert.assertNotNull(realTimeModels);
        Assert.assertTrue(realTimeModels.size()>0);
        LOG.info(realTimeModels.toString());
    }
}
