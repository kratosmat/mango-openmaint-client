package it.storelink.mango.client;

import it.storelink.mango.ApiException;
import it.storelink.mango.api.DefaultApiImpl;
import it.storelink.mango.model.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProvisioningVirtualTest extends MangoBaseTest {

    @Test
    public void _00_testGetAllDataSources() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        List<DataSourceModel> allDataSources = api.getAllDataSources();
        Assert.assertNotNull(allDataSources);
        LOG.info(allDataSources.toString());
    }

    @Test
    public void _01_testSaveDataSourceVirtual() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        try {

            DataSourceVirtualModel model = new DataSourceVirtualModel();
            model.setEnabled(false);
            model.setName("from_api");
            model.setXid("DS_465466");
            TimePeriodModel pollPeriod = new TimePeriodModel();
            pollPeriod.setPeriods(1);
            pollPeriod.setType("SECONDS");
            model.setPollPeriod(pollPeriod);
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
        DataSourceModel dataSourceModel = api.getDataSource("DS_465466");
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
        dp.setDataSourceXid("DS_465466");
        dp.setDeviceName("virtual1");
        dp.setDataSourceName("virtual1");

        dp.setTemplateXid("Binary_Default");
        dp.setEnabled(false);
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
    public void _04_testGetDataPoint() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataPointModel dataPointModel = api.getDataPoint("DP_921288");
        Assert.assertNotNull(dataPointModel);
        LOG.info(dataPointModel.toString());
    }

    @Test
    public void _05_testUpdateDataSource() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataSourceVirtualModel model = new DataSourceVirtualModel();
        model.setEnabled(true);
        model.setName("from_api");
        model.setXid("DS_465466");
        TimePeriodModel pollPeriod = new TimePeriodModel();
        pollPeriod.setPeriods(1);
        pollPeriod.setType("SECONDS");
        model.setPollPeriod(pollPeriod);
        DataSourceModel dataSourceModel = api.updateDataSource("DS_465466", model);
        Assert.assertNotNull(dataSourceModel);
        LOG.info(dataSourceModel.toString());
    }

    @Test
    public void _06_testUpdateDataPoint() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataPointModel model = new DataPointModel();
        model.setEnabled(true);
        model.setXid("DP_921288");
        model.setName("device_from_api");

        PointLocatorVirtualModel pv = new PointLocatorVirtualModel();
        pv.setDataType(PointLocatorModel.DataTypeEnum.BINARY);
        pv.setModelType("PL.VIRTUAL");
        pv.setSettable(false);
        pv.setRelinquishable(false);
        pv.setStartValue(false);
        model.setPointLocator(pv);

        DataPointModel model1 = api.updateDataPoint("DP_921288", model);
        Assert.assertNotNull(model1);
        LOG.info(model1.toString());
    }

    @Test
    public void _07_testGetPointValues() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(10000);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        DateTime from = formatter.parseDateTime("09/01/2016 16:00:00");
        DateTime to = formatter.parseDateTime("31/12/2016 16:00:00");

        List<PointValueModel> pointValueModels = api.getPointValues("DP_921288", false, false, from.toDate(), to.toDate(), "NONE", "", 0);
        Assert.assertNotNull(pointValueModels);
        Assert.assertTrue(pointValueModels.size()>0);
    }

    @Test
    public void _08_testDeleteDataPoints() throws ApiException, InterruptedException {
        if (!initialized) return;
        Thread.sleep(1000);
        try {
            DataPointModel dataPointModelDeleted = api.deleteDataPoint("DP_921288");
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
            dataPointModel = api.getDataPoint("DP_921288");
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
        DataSourceModel dataSourceModel = api.deleteDataSource("DS_465466");
        Assert.assertNotNull(dataSourceModel);
        LOG.info(dataSourceModel.toString());
    }

    @Test
    public void _11_testGetDataSourceAfterDelete() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataSourceModel dataSourceModel = null;
        try {
            dataSourceModel = api.getDataSource("DS_465466");
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        Assert.assertNull(dataSourceModel);
    }

}
