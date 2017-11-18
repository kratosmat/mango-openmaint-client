package it.storelink.mango.client;

import it.storelink.mango.ApiException;
import it.storelink.mango.model.PointValueModel;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by kratoslink on 05/08/16.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlasticAtlantaZigBeeTest extends MangoBaseTest {

    protected static Logger LOG = LoggerFactory.getLogger(PlasticAtlantaZigBeeTest.class);

    @Test
    public void testPutFalseRele1() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        PointValueModel pointValueModel = new PointValueModel();
        pointValueModel.setDataType(PointValueModel.DataTypeEnum.BINARY);
        pointValueModel.setTimestamp(System.currentTimeMillis());
        pointValueModel.setValue(false);
        pointValueModel.setAnnotation("from API");
        PointValueModel pointValue = api.putPointValue("DP_001726", pointValueModel, false);
        Assert.assertNotNull(pointValue);
        LOG.info(pointValue.toString());
    }

    @Test
    public void testPutTrueRele1() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        PointValueModel pointValueModel = new PointValueModel();
        pointValueModel.setDataType(PointValueModel.DataTypeEnum.BINARY);
        pointValueModel.setTimestamp(System.currentTimeMillis());
        pointValueModel.setValue(true);
        pointValueModel.setAnnotation("from API");
        PointValueModel pointValue = api.putPointValue("DP_001726", pointValueModel, false);
        Assert.assertNotNull(pointValue);
        LOG.info(pointValue.toString());
    }

    @Test
    public void testLatest() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        PointValueModel pointValueModel = new PointValueModel();
        pointValueModel.setDataType(PointValueModel.DataTypeEnum.BINARY);
        pointValueModel.setTimestamp(System.currentTimeMillis());
        pointValueModel.setValue(true);
        pointValueModel.setAnnotation("from API");
        List<PointValueModel> pointValues = api.getLatestPointValues("DP_010515", 1, false, false, false);
        Assert.assertNotNull(pointValues);
        LOG.info(Integer.valueOf(pointValues.size()).toString());
    }

}
