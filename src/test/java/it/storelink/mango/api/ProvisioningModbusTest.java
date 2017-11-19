package it.storelink.mango.api;

import it.storelink.mango.ApiException;
import it.storelink.mango.model.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProvisioningModbusTest extends MangoBaseTest {

    private static Logger LOG = LoggerFactory.getLogger(ProvisioningModbusTest.class);

    /*
    private static MangoRestApi api;
    private static Boolean initialized = false;
    private static String user = "admin";
    private static String password = "$t0rlinK";
    private static String baseHost = "79.60.49.49";
    private static String basePort = "7778";
    */

    /*
    private static String user = "admin";
    private static String password = "admin";
    private static String baseHost = "localhost";
    private static String basePort = "8080";
    */

    /*
    private static String baseUrl = "http://" + baseHost + ":" + basePort + "/rest";
    private static String wsUrl = "ws://" +  baseHost + ":" + basePort + "/rest/v2/websocket/point-value";

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
    */

    @Test
    public void _01_testSaveDataSource() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        try {

            DataSourceModbusIpModel model = new DataSourceModbusIpModel();
            model.setEnabled(false);
            model.setName("modbus_from_api");
            model.setXid("DS_998877");
            model.setTransportType("TCP");
            model.setEncapsulated(true);
            model.setHost("192.168.0.68");
            model.setPort(502);
            model.setTimeout(500);
            model.setMaxReadBitCount(2000);
            model.setMaxReadRegisterCount(125);
            model.setMaxWriteRegisterCount(120);
            DataSourceModel dataSourceModel = api.saveDataSource(model);
            Assert.assertNotNull(dataSourceModel);
            LOG.info(dataSourceModel.toString());
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void _02_testGetDataSource() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataSourceModel dataSourceModel = api.getDataSource("DS_998877");
        Assert.assertNotNull(dataSourceModel);
        LOG.info(dataSourceModel.toString());
    }

    @Test
    public void _03_testGetAllDataPoints() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        List<DataPointModel> listDataPoints = api.getAllDataPoints(100);
        Assert.assertNotNull(listDataPoints);
        LOG.info(listDataPoints.toString());
        Assert.assertEquals(listDataPoints.size(), 26);
    }

    private DataPointModel createDataPoint() {
        DataPointModel dp = new DataPointModel();

        dp.setDataSourceId(2);
        dp.setDataSourceXid("DS_998877");
        dp.setDeviceName("modbus_from_api");
        dp.setDataSourceName("modbus_from_api");

        //dp.setTemplateXid("Binary_Default");
        dp.setEnabled(false);
        dp.setUseIntegralUnit(false);
        dp.setUseRenderedUnit(false);
        dp.setIntegralUnit("s");
        dp.setModelType("DATA_POINT");
        dp.setId(10);
        dp.setName("modbus_from_api");
        dp.setXid("DS_998878");

        PointLocatorModbusModel pv = new PointLocatorModbusModel();
        pv.setDataType(PointLocatorModel.DataTypeEnum.BINARY);
        pv.setSettable(false);
        pv.setRelinquishable(false);
        pv.setSlaveId(10);
        pv.setRange(PointLocatorModbusModel.RANGE_CODES.getCode(4));
        pv.setModbusDataType(PointLocatorModbusModel.MODBUS_DATA_TYPE_CODES.getCode(8));
        pv.setOffset(1);
        pv.setBit(0);
        pv.setRegisterCount(0);
        pv.setAdditive(0.0);
        pv.setMultiplier(1.0);
        dp.setPointLocator(pv);

        return dp;
    }

    @Test
    public void _04_testSaveDataPoints() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        List<DataPointModel> dataPointModels = new ArrayList<DataPointModel>();

        dataPointModels.add(createDataPoint());

        List<DataPointModel> dataPointModelsSaved = api.saveDataPoints(dataPointModels);
        Assert.assertNotNull(dataPointModelsSaved);
    }

    @Test
    public void _05_testGetDataPoint() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataPointModel dataPointModel = api.getDataPoint("DS_998878");
        Assert.assertNotNull(dataPointModel);
        LOG.info(dataPointModel.toString());
    }

    @Test
    public void _06_testUpdateDataSource() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataSourceModbusIpModel model = new DataSourceModbusIpModel();
        model.setEnabled(true);
        model.setName("modbus_from_api");
        model.setXid("DS_998877");
        model.setTransportType("TCP");
        model.setEncapsulated(true);
        model.setHost("192.168.0.68");
        model.setPort(502);
        model.setTimeout(500);
        model.setMaxReadBitCount(2000);
        model.setMaxReadRegisterCount(125);
        model.setMaxWriteRegisterCount(120);
        DataSourceModel dataSourceModel = api.updateDataSource("DS_998877", model);
        Assert.assertNotNull(dataSourceModel);
        LOG.info(dataSourceModel.toString());
    }

    @Test
    public void _07_testUpdateDataPoint() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);

        DataPointModel model = createDataPoint();
        model.setEnabled(true);

        DataPointModel model1 = api.updateDataPoint("DS_998878", model);
        Assert.assertNotNull(model1);
        LOG.info(model1.toString());
    }

    @Test
    public void _08_testGetPointValues() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        DateTime from = formatter.parseDateTime("09/01/2016 16:00:00");
        DateTime to = formatter.parseDateTime("31/12/2016 16:00:00");

        List<PointValueModel> pointValueModels = api.getPointValues("DS_998878", false, false, from.toDate(), to.toDate(), "NONE", "", 0);
        Assert.assertNotNull(pointValueModels);
        Assert.assertTrue(pointValueModels.size()>0);
    }

    @Test
    public void _09_testDeleteDataPoints() throws ApiException, InterruptedException {
        if (!initialized) return;
        Thread.sleep(1000);
        try {
            DataPointModel dataPointModelDeleted = api.deleteDataPoint("DS_998878");
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
    public void _10_testGetDataPointAfterDelete() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataPointModel dataPointModel = null;
        try {
            dataPointModel = api.getDataPoint("DS_998878");
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        Assert.assertNull(dataPointModel);
    }

    @Test
    public void _11_testDeleteDataSource() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataSourceModel dataSourceModel = api.deleteDataSource("DS_998877");
        Assert.assertNotNull(dataSourceModel);
        LOG.info(dataSourceModel.toString());
    }

    @Test
    public void _12_testGetDataSourceAfterDelete() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataSourceModel dataSourceModel = null;
        try {
            dataSourceModel = api.getDataSource("DS_998877");
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        Assert.assertNull(dataSourceModel);
    }

    /*
    @AfterClass
    public static void close() throws ApiException {
        if(!initialized) return;
        if(api!=null) api.logout();
    }
    */

}
