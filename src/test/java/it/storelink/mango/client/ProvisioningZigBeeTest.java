package it.storelink.mango.client;

import it.storelink.mango.ApiException;
import it.storelink.mango.model.DataPointModel;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProvisioningZigBeeTest extends MangoBaseTest {


    private static Logger LOG = LoggerFactory.getLogger(ProvisioningZigBeeTest.class);

    @Test
    public void _00_testGetDataPoints() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        List<DataPointModel> dataPointModels = api.getDataPointsForDataSource("DS_375681");
        Assert.assertNotNull(dataPointModels);
        LOG.info(dataPointModels.toString());
    }


    /*
    @Test
    public void _01_testSaveDataSourceVirtual() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        try {
            DataSourceBacnetIpModel model = new DataSourceBacnetIpModel();
            model.setModelType("BACnetIP");
            model.setEnabled(false);
            model.setName("bacnet_from_api");
            model.setXid("DS_465469");
            model.setCovSubscriptionTimeoutMinutes(1);
            DataSourceModel dataSourceModel = api.saveDataSource(model);
            Assert.assertNotNull(dataSourceModel);
            LOG.info(dataSourceModel.toString());
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Test
    public void _02_testGetDataSource() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataSourceModel dataSourceModel = api.getDataSource("DS_465469");
        Assert.assertNotNull(dataSourceModel);
        LOG.info(dataSourceModel.toString());
    }

    @Test
    public void _03_testSaveDataPointsVirtual() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        List<DataPointModel> dataPointModels = new ArrayList<DataPointModel>();
        DataPointModel dp = new DataPointModel();

        dp.setDataSourceId(2);
        dp.setDataSourceXid("DS_465469");
        dp.setDeviceName("bacnet_from_api1");
        dp.setDataSourceName("bacnet_from_api1");

        dp.setTemplateXid("Binary_Default");
        dp.setEnabled(false);
        dp.setUseIntegralUnit(false);
        dp.setUseRenderedUnit(false);
        dp.setIntegralUnit("s");
        dp.setTemplateXid("Binary_Default");
        dp.setModelType("DATA_POINT");
        dp.setId(10);
        dp.setName("bacnet_from_api");
        dp.setXid("DS_465467");

        PointLocatorBacnetModel pl = new PointLocatorBacnetModel();
        pl.setDataType(PointLocatorModel.DataTypeEnum.BINARY);
        //pl.setModelType("PL.VIRTUAL");
        pl.setSettable(false);
        pl.setRelinquishable(true);
        pl.setAddress("192.168.178.30:47808");
        pl.setIoLine(0);
        pl.setUseCovSubscription(true);
        pl.setObjectInstanceNumber(202);
        pl.setNodeIdentifier("BINARY_INPUT");
        dp.setPointLocator(pl);

        dataPointModels.add(dp);
        List<DataPointModel> dataPointModelsSaved = api.saveDataPoints(dataPointModels);
        Assert.assertNotNull(dataPointModelsSaved);
    }

    @Test
    public void _04_testGetDataPoint() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataPointModel dataPointModel = api.getDataPoint("DP_624444");
        Assert.assertNotNull(dataPointModel);
        LOG.info(dataPointModel.toString());
    }

    @Test
    public void _05_testUpdateDataSource() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataSourceBacnetIpModel model = new DataSourceBacnetIpModel();
        model.setEnabled(true);
        model.setXid("DS_465469");
        model.setName("bacnet_from_api");
        DataSourceModel dataSourceModel = api.updateDataSource("DS_465469", model);
        Assert.assertNotNull(dataSourceModel);
        LOG.info(dataSourceModel.toString());
    }

    @Test
    public void _06_testUpdateDataPoint() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataPointModel model = new DataPointModel();
        model.setEnabled(true);
        model.setXid("DS_465467");
        model.setName("bacnet_from_api");

        PointLocatorBacnetModel pl = new PointLocatorBacnetModel();
        pl.setDataType(PointLocatorModel.DataTypeEnum.BINARY);
        pl.setSettable(false);
        pl.setRelinquishable(true);
        pl.setAddress("192.168.178.30:47808");
        pl.setIoLine(0);
        pl.setUseCovSubscription(true);
        pl.setObjectInstanceNumber(202);
        pl.setNodeIdentifier("BINARY_INPUT");
        model.setPointLocator(pl);

        DataPointModel model1 = api.updateDataPoint("DS_465467", model);
        Assert.assertNotNull(model1);
        LOG.info(model1.toString());
    }

    @Test
    public void _07_testGetPointValues() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(20000);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        DateTime from = formatter.parseDateTime("09/01/2016 16:00:00");
        DateTime to = formatter.parseDateTime("31/12/2016 16:00:00");

        List<PointValueModel> pointValueModels = api.getPointValues("DS_465467", false, false, from.toDate(), to.toDate(), "NONE", "", 0);
        Assert.assertNotNull(pointValueModels);
        Assert.assertTrue(pointValueModels.size()>0);
    }

    @Test
    public void _08_testDeleteDataPoints() throws ApiException, InterruptedException {
        if (!initialized) return;
        Thread.sleep(1000);
        try {
            DataPointModel dataPointModelDeleted = api.deleteDataPoint("DS_465467");
            Assert.assertNotNull(dataPointModelDeleted);
        }
        catch (ApiException e) {
            for(String error : ((DefaultApiImpl)api).getMessagges().get("errors")) {
                LOG.error(error);
            }
            fail(e.getMessage());
        }
    }

    @Test
    public void _09_testGetDataPointAfterDelete() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataPointModel dataPointModel = null;
        try {
            dataPointModel = api.getDataPoint("DS_465467");
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        Assert.assertNull(dataPointModel);
    }

    @Test
    public void _10_testDeleteDataSource() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataSourceModel dataSourceModel = api.deleteDataSource("DS_465469");
        Assert.assertNotNull(dataSourceModel);
        LOG.info(dataSourceModel.toString());
    }

    @Test
    public void _11_testGetDataSourceAfterDelete() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataSourceModel dataSourceModel = null;
        try {
            dataSourceModel = api.getDataSource("DS_465469");
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        Assert.assertNull(dataSourceModel);
    }
    */

}
