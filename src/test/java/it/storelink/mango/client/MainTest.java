package it.storelink.mango.client;


import it.storelink.mango.ApiException;
import it.storelink.mango.api.ApiClient;
import it.storelink.mango.api.DefaultApiImpl;
import it.storelink.mango.api.MangoRestApi;
import it.storelink.mango.model.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * User: kratos
 * Date: 10/01/16
 * Time: 17.49
 */

public class MainTest {

    private static MangoRestApi api;
    private static Boolean initialized = false;
    private static Logger LOG = LoggerFactory.getLogger(MainTest.class);

    private static String user = "mario";
    private static String password = "ro$$i";
    private static String baseHost = "79.60.49.49";
    private static String basePort = "7778";
    /*
        private static String user = "admin";
        private static String password = "admin";
        private static String baseHost = "localhost";
        private static String basePort = "8080";
    */
    private static String baseUrl = "http://" + baseHost + ":" + basePort + "/rest/";
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

    @Test
    public void testGetAllDataSources() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        List<DataSourceModel> allDataSources = api.getAllDataSources();
        Assert.assertNotNull(allDataSources);
        LOG.info(allDataSources.toString());
    }

    @Test
    public void testGetDataSource() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataSourceModel dataSourceModel = api.getDataSource("DS_720786");
        Assert.assertNotNull(dataSourceModel);
        LOG.info(dataSourceModel.toString());
    }

    @Test
    public void testGetLatestPointValues() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        List<PointValueModel> pointValueModels = api.getLatestPointValues("DP_495821", 100, true, false, false);
        Assert.assertNotNull(pointValueModels);
        LOG.info(pointValueModels.toString());
    }

    @Test
    public void testGetLatestPointValues2() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        List<PointValueModel> pointValueModels = api.getLatestPointValues("DP_495821", 100, true, false, false);
        Assert.assertNotNull(pointValueModels);
        LOG.info(pointValueModels.toString());
    }

    @Test
    public void testPutPointValue() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        PointValueModel pointValueModel = new PointValueModel();
        pointValueModel.setDataType(PointValueModel.DataTypeEnum.NUMERIC);
        //pointValueModel.setTimestamp(System.currentTimeMillis());
        pointValueModel.setValue(37.10);
        //pointValueModel.setAnnotation("from API");
        PointValueModel pointValue = api.putPointValue("DP_889620", pointValueModel, false);
        Assert.assertNotNull(pointValue);
        LOG.info(pointValue.toString());
    }

    @Test
    public void testGetPointValues() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        DateTime from = formatter.parseDateTime("09/01/2016 16:00:00");
        DateTime to = formatter.parseDateTime("21/02/2016 16:00:00");

        List<PointValueModel> pointValueModels = api.getPointValues("DP_729855", false, false, from.toDate(), to.toDate(), "NONE", "", 0);
        Assert.assertNotNull(pointValueModels);
    }

    @Test
    public void testCount() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        DateTime from = formatter.parseDateTime("09/01/2016 16:00:00");
        DateTime to = formatter.parseDateTime("21/02/2016 16:00:00");

        Integer count = api.count("DP_729855", from.toDate(), to.toDate(), "NONE", "", 0);
        Assert.assertNotNull(count);
        LOG.info(count.toString());
    }

    @Test
    public void test1FirstAndLastPointValues() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        DateTime from = formatter.parseDateTime("09/01/2016 16:00:00");
        DateTime to = formatter.parseDateTime("22/02/2016 16:00:00");

        List<PointValueModel> pointValueModels = api.firstAndLastPointValues("DP_729855", false, false, from.toDate(), to.toDate());
        Assert.assertNotNull(pointValueModels);
        LOG.info(pointValueModels.toString());
    }

    @Test
    public void testGetPointStatistics() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        DateTime from = formatter.parseDateTime("09/01/2016 16:00:00");
        DateTime to = formatter.parseDateTime("22/02/2016 16:00:00");

        StatisticsStream statisticsStream = api.getPointStatistics("DP_729855", false, false, from.toDate(), to.toDate());
        Assert.assertNotNull(statisticsStream);
        LOG.info(statisticsStream.toString());
    }

    @Test
    public void testGetAllDataPoints() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        List<DataPointModel> listDataPoints = api.getAllDataPoints(100);
        Assert.assertNotNull(listDataPoints);
        LOG.info(listDataPoints.toString());
        Assert.assertEquals(listDataPoints.size(), 26);

    }

    @Test
    public void testGetDataPoint() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataPointModel dataPointModel = api.getDataPoint("DP_099283");
        Assert.assertNotNull(dataPointModel);
        LOG.info(dataPointModel.toString());
    }

    @Test
    public void testGetDataPoint2() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataPointModel dataPointModel = api.getDataPoint("DP_495821");
        Assert.assertNotNull(dataPointModel);
        LOG.info(dataPointModel.toString());
    }

    @AfterClass
    public static void close() throws ApiException {
        if(!initialized) return;
        if(api!=null) api.logout();
    }

}
