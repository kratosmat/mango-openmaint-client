package it.storelink.mango.api;

import it.storelink.mango.ApiException;
import it.storelink.mango.model.DataPointModel;
import it.storelink.mango.model.DataSourceModel;
import it.storelink.mango.model.PointValueModel;
import it.storelink.mango.model.StatisticsStream;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

@FixMethodOrder(MethodSorters.DEFAULT)
public class MainTest extends MangoBaseTest {

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
        DataSourceModel dataSourceModel = api.getDataSource("internal_mango_monitoring_ds");
        Assert.assertNotNull(dataSourceModel);
        LOG.info(dataSourceModel.toString());
    }

    @Test
    public void testGetLatestPointValues() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        List<PointValueModel> pointValueModels = api.getLatestPointValues("Demo 01-amps", 100, true, false, false);
        Assert.assertNotNull(pointValueModels);
        LOG.info(pointValueModels.toString());
    }

    @Test
    @Ignore
    public void testGetLatestPointValues2() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        List<PointValueModel> pointValueModels = api.getLatestPointValues("virtual_dp1", 100, true, false, false);
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
        PointValueModel pointValue = api.putPointValue("Demo 01-amps", pointValueModel, false);
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

        List<PointValueModel> pointValueModels = api.getPointValues("Demo 01-amps", false, false, from.toDate(), to.toDate(), "NONE", "", 0);
        Assert.assertNotNull(pointValueModels);
    }

    @Test
    public void testCount() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        DateTime from = formatter.parseDateTime("09/01/2016 16:00:00");
        DateTime to = formatter.parseDateTime("21/02/2016 16:00:00");

        Integer count = api.count("Demo 01-amps", from.toDate(), to.toDate(), "NONE", "", 0);
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

        List<PointValueModel> pointValueModels = api.firstAndLastPointValues("Demo 01-amps", false, false, from.toDate(), to.toDate());
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

        StatisticsStream statisticsStream = api.getPointStatistics("Demo 01-amps", false, false, from.toDate(), to.toDate());
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
        Assert.assertEquals(listDataPoints.size(), 18);

    }

    @Test
    public void testGetDataPoint() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataPointModel dataPointModel = api.getDataPoint("Demo 01-amps");
        Assert.assertNotNull(dataPointModel);
        LOG.info(dataPointModel.toString());
    }

    @Test
    @Ignore
    public void testGetDataPoint2() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataPointModel dataPointModel = api.getDataPoint("DP_495821");
        Assert.assertNotNull(dataPointModel);
        LOG.info(dataPointModel.toString());
    }



}
