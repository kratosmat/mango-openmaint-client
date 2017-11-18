package it.storelink.mango.client;

import it.storelink.mango.ApiException;
import it.storelink.mango.model.*;
import it.storelink.mango.utils.BacnetObjectTypes;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.fail;

/**
 * Created by kratoslink on 05/08/16.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DiscoveryBacnetTest extends MangoBaseTest {

    protected static Logger LOG = LoggerFactory.getLogger(DiscoveryBacnetTest.class);

    private static LocalDeviceConfig localDeviceConfig;


    /**
     * Create a bacnet datasource
     * @throws ApiException
     * @throws InterruptedException
     */
    @Test
    public void _01_testSaveDataSource() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        try {

            List<LocalDeviceConfig> localDeviceConfigs = api.bacnetLocalDeviceConfigs();
            if(localDeviceConfigs!=null && localDeviceConfigs.size()>0) {
                localDeviceConfig = localDeviceConfigs.get(0);

                DataSourceBacnetIpModel model = new DataSourceBacnetIpModel();
                model.setModelType("BACnetIP");
                model.setEnabled(true);
                model.setName("bacnet_from_api");
                model.setXid("DS_465469");
                model.setCovSubscriptionTimeoutMinutes(1);
                model.setLocalDeviceConfig(localDeviceConfig.getId());
                DataSourceModel dataSourceModel = api.saveDataSource(model);
                Assert.assertNotNull(dataSourceModel);
                LOG.info(dataSourceModel.toString());
            }
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * Start discovery process and create data points
     * @throws ApiException
     * @throws InterruptedException
     */
    @Test
    public void _02_testDiscovery() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        try {
            //get the data source created before
            DataSourceModel dataSourceModel = api.getDataSource("DS_465469");

            List<Object> devices = null;

            //send bacnet whois
            api.bacnetSendWhoIs(localDeviceConfig.getId());
            boolean finished = false;
            Thread.sleep(1000);
            while (!finished) {
                // send bacnet whois update while it finds devices
                Map<String, Object> map = api.bacnetWhoIsUpdate();
                if (map.containsKey("devices")) {
                    finished = true;
                    devices = (List<Object>) map.get("devices");
                    Thread.sleep(5000);
                }
            }

            if(devices!=null && devices.size()>0) {
                for (Object object : (ArrayList) devices) {
                    LinkedHashMap device = (LinkedHashMap)object;
                    LOG.info("device: " + device.toString());
                    BacnetDiscoveryObjectRequestBean bean = new BacnetDiscoveryObjectRequestBean();
                    bean.setMac((device).get("mac").toString());
                    bean.setNetworkNumber(Integer.valueOf((device).get("networkNumber").toString()));
                    bean.setRemoteDeviceId(Integer.valueOf((device).get("instanceNumber").toString()));

                    //send bacnet object discovery
                    ProcessResult processResult = api.bacnetSendDeviceObjectListRequest(bean);

                    finished = false;
                    while (!finished) {
                        Thread.sleep(5000);

                        //send bacnet discovery update while the process finished
                        processResult = api.bacnetGetObjectListUpdate();

                        LOG.info(processResult.toString());
                        LinkedHashMap data = (LinkedHashMap)processResult.getData();
                        if(data.containsKey("done")) {
                            finished = (Boolean)((LinkedHashMap) processResult.getData()).get("done");
                            if(finished) {
                                //ArrayList deviceObjects = (ArrayList)data.get("deviceObjects");
                                //object beans
                                ArrayList beans = (ArrayList)data.get("beans");
                                String deviceName = data.get("deviceName").toString();
                                Integer deviceId = (Integer)data.get("deviceId");
                                String deviceMac = data.get("deviceMac").toString();
                                Integer deviceNetwork = (Integer) data.get("deviceNetwork");
                                List<DataPointModel> dataPointModels = new ArrayList<DataPointModel>();
                                int i = 0;
                                for (Object object1 : beans) {
                                    LinkedHashMap bean1 = (LinkedHashMap)object1;
                                    LOG.info(bean.toString());
                                    Integer objectTypeId = (Integer)bean1.get("objectTypeId");
                                    if(objectTypeId==8) continue;
                                    dataPointModels.add(createDataPoint(
                                            deviceId,
                                            dataSourceModel.getXid(),
                                            deviceName,
                                            "bacnet_from_api1",
                                            deviceMac,
                                            deviceNetwork,
                                            bean1,
                                            "DS_46546" + i));
                                    i++;

                                }
                                List<DataPointModel> dataPointModelsSaved = api.saveDataPoints(dataPointModels);
                            }
                        }
                    }
                }
            }

        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    @Test
    public void _03_testDeleteDataSource() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataSourceModel dataSourceModel = api.deleteDataSource("DS_465469");
        Assert.assertNotNull(dataSourceModel);
        LOG.info(dataSourceModel.toString());
    }

    /**
     * Create a Data Point from object bean retrieved from discovery process
     * @param dataSourceId
     * @param dataSourceXid
     * @param dataSourceName
     * @param deviceName
     * @param mac
     * @param networkNumber
     * @param deviceObject
     * @param dataPointXid
     * @return
     */
    private DataPointModel createDataPoint(
            Integer dataSourceId,
            String dataSourceXid,
            String dataSourceName,
            String deviceName,
            String mac,
            Integer networkNumber,
            LinkedHashMap deviceObject,
            String dataPointXid) {

        DataPointModel dp = new DataPointModel();

        dp.setDataSourceId(dataSourceId);
        dp.setDataSourceXid(dataSourceXid);
        dp.setDataSourceName(dataSourceName);
        dp.setDeviceName(deviceName);

        dp.setEnabled(true);
        dp.setUseIntegralUnit(false);
        dp.setUseRenderedUnit(false);
        dp.setIntegralUnit("s");
        dp.setModelType("DATA_POINT");
        dp.setName(deviceObject.get("objectName").toString());
        dp.setXid(dataPointXid);

        PointLocatorBacnetModel pl = new PointLocatorBacnetModel();
        pl.setNetworkNumber(networkNumber);
        pl.setMac(mac);
        pl.setRemoteDeviceInstanceNumber(Integer.valueOf(deviceObject.get("instanceNumber").toString()));
        pl.setObjectTypeId(BacnetObjectTypes.convertTo((Integer)deviceObject.get("objectTypeId")));
        pl.setObjectInstanceNumber(Integer.valueOf(deviceObject.get("instanceNumber").toString()));
        pl.setDataType(PointLocatorModel.DataTypeEnum.convertTo((Integer)deviceObject.get("dataTypeId")));
        pl.setUseCovSubscription((Boolean) deviceObject.get("cov"));
        pl.setSettable(false);

        dp.setPointLocator(pl);
        return dp;
    }

    /*
    private boolean settableDefault(int typeId) {
        return typeId == 0 || typeId == 1 || typeId == 2;
    }
    */

}
