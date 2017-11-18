package it.storelink.mango.client;

import it.storelink.mango.ApiException;
import it.storelink.mango.model.DataSourceBacnetIpModel;
import it.storelink.mango.model.DataSourceModel;
import it.storelink.mango.model.DataSourceVirtualModel;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

@FixMethodOrder(MethodSorters.DEFAULT)
public class DataSourceTest extends MangoBaseTest {

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
        DataSourceModel dataSourceModel = api.getDataSource("DS_601314");
        Assert.assertNotNull(dataSourceModel);
        LOG.info(dataSourceModel.toString());
    }

    @Test
    public void testSaveDataSourceVirtual() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataSourceVirtualModel model = new DataSourceVirtualModel();
        model.setModelType("VIRTUAL");
        model.setEnabled(false);
        model.setName("from_api");
        model.setXid("DS_465466");
        DataSourceModel dataSourceModel = api.saveDataSource(model);
        Assert.assertNotNull(dataSourceModel);
        LOG.info(dataSourceModel.toString());
    }

    @Test
    public void testSaveDataSourceBacnet() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataSourceBacnetIpModel model = new DataSourceBacnetIpModel();
        model.setModelType("BACnetIP");
        model.setEnabled(false);
        model.setName("from_api");
        model.setXid("DS_465467");
        model.setCovSubscriptionTimeoutMinutes(60);
        //model.setLocalDeviceConfig("");
        DataSourceModel dataSourceModel = api.saveDataSource(model);
        Assert.assertNotNull(dataSourceModel);
        LOG.info(dataSourceModel.toString());
    }

    @Test
    public void testUpdateDataSourceBacnet() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataSourceBacnetIpModel model = new DataSourceBacnetIpModel();
        model.setModelType("BACnetIP");
        model.setEnabled(true);
        model.setName("from_api");
        model.setXid("DS_465467");
        model.setCovSubscriptionTimeoutMinutes(120);
        //model.setLocalDeviceConfig("");
        DataSourceModel dataSourceModel = api.updateDataSource("DS_465467", model);
        Assert.assertNotNull(dataSourceModel);
        LOG.info(dataSourceModel.toString());
    }

    @Test
    public void testDeleteDataSourceBacnet() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataSourceModel dataSourceModel = api.deleteDataSource("DS_465466");
        Assert.assertNotNull(dataSourceModel);
        LOG.info(dataSourceModel.toString());
    }

}
