package it.storelink.mango.api;

import it.storelink.mango.ApiException;
import it.storelink.mango.model.DataPointModel;
import it.storelink.mango.model.PointLocatorModel;
import it.storelink.mango.model.PointLocatorVirtualModel;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.DEFAULT)
public class DataPointTest extends MangoBaseTest {


    @Test
    public void testGetAllDataPoints() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        List<DataPointModel> listDataPoints = api.getAllDataPoints(100);
        Assert.assertNotNull(listDataPoints);
        LOG.info(listDataPoints.toString());
        Assert.assertEquals(listDataPoints.size(), 6);
    }

    @Test
    public void testGetDataPoint() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataPointModel dataPointModel = api.getDataPoint("DP_512982");
        Assert.assertNotNull(dataPointModel);
        LOG.info(dataPointModel.toString());
    }

    @Test
    public void testGetDataPoint2() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataPointModel dataPointModel = api.getDataPoint("DP_624444");
        Assert.assertNotNull(dataPointModel);
        LOG.info(dataPointModel.toString());
    }

    @Test
    public void testSaveDataPointsVirtual() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        List<DataPointModel> dataPointModels = new ArrayList<DataPointModel>();
        DataPointModel dp = new DataPointModel();

        dp.setDataSourceId(2);
        dp.setDataSourceXid("DS_601314");
        dp.setDeviceName("virtual1");
        dp.setDataSourceName("virtual1");

        dp.setTemplateXid("Binary_Default");
        dp.setEnabled(true);
        dp.setUseIntegralUnit(false);
        dp.setUseRenderedUnit(false);
        dp.setIntegralUnit("s");
        dp.setTemplateXid("Binary_Default");
        dp.setModelType("DATA_POINT");
        dp.setId(10);
        dp.setName("device_from_api");
        dp.setXid("DP_921288");

        PointLocatorVirtualModel pv = new PointLocatorVirtualModel();
        pv.setDataType(PointLocatorModel.DataTypeEnum.BINARY);
        pv.setModelType("PL.VIRTUAL");
        pv.setSettable(false);
        pv.setRelinquishable(false);
        pv.setStartValue(false);
        dp.setPointLocator(pv);

        dataPointModels.add(dp);
        List<DataPointModel> dataPointModelsSaved = api.saveDataPoints(dataPointModels);
        Assert.assertNotNull(dataPointModelsSaved);
    }

    @Test
    public void testDeleteDataPoints() throws ApiException, InterruptedException {
        if (!initialized) return;
        Thread.sleep(1000);
        try {
            DataPointModel dataPointModelDeleted = api.deleteDataPoint("DP_107558");
            Assert.assertNotNull(dataPointModelDeleted);
        }
        catch (ApiException e) {
            LOG.error(e.getMessage());
            fail(e.getMessage());
        }
    }

}
