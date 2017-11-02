package it.storelink.mango.client;

import it.storelink.mango.ApiException;
import it.storelink.mango.model.*;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.junit.Assert.fail;

/**
 * Created by kratoslink on 05/08/16.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DiscoveryZigBeeTest extends MangoBaseTest {

    protected static Logger LOG = LoggerFactory.getLogger(DiscoveryZigBeeTest.class);

    /**
     * Create a zigbee datasource
     * @throws ApiException
     * @throws InterruptedException
     */
    @Test
    public void _01_testSaveDataSource() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        try {
            DataSourceZigBeeModel model = new DataSourceZigBeeModel();
            model.setEnabled(true);
            model.setName("zigbee_from_api");
            model.setXid("DS_465499");
            model.setCovSubscriptionTimeoutMinutes(1);
            model.setPort("/dev/ttyUSB1");
            model.setBaudRate(9600);
            model.setVendor("xbee");

            DataSourceModel dataSourceModel = api.saveDataSource(model);
            Assert.assertNotNull(dataSourceModel);
            LOG.info(dataSourceModel.toString());
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
            DataSourceModel dataSource = api.getDataSource("DS_465499");

            SerialPort serialPort = new SerialPort();
            serialPort.setPort("/dev/ttyUSB1");
            serialPort.setVendor("xbee");
            serialPort.setBaudrate(9600);

            List<Object> devices = new LinkedList<Object>();

            //send zigbee whois
            api.zigbeeSendWhoIs(serialPort);

            boolean finished = false;
            while (!finished) {
                Thread.sleep(2000);
                // send zigbee whois update while it finds devices
                Map<String, Object> map = api.zigbeeWhoIsUpdate();
                List<Object> freshDevices = (List<Object>) map.get("devices");
                devices.addAll(freshDevices);
                if (map.containsKey("finished")) {
                    finished = (Boolean)map.get("finished");
                }
            }

            if(devices!=null && devices.size()>0) {
                for (Object object : (List) devices) {
                    LinkedHashMap device = (LinkedHashMap)object;
                    LOG.info("device: " + device.toString());

                    //send zigbee object discovery
                    ProcessResult processResult = api.zigbeeSendDeviceObjectListRequest(device.get("nodeIdentifier").toString());

                    finished = false;
                    while (!finished) {
                        Thread.sleep(2000);

                        //send zigbee discovery update while the process finished
                        processResult = api.zigbeeGetObjectListUpdate();

                        LOG.info(processResult.toString());

                        LinkedHashMap data = (LinkedHashMap)processResult.getData();
                        if(data.containsKey("done")) {
                            finished = (Boolean)((LinkedHashMap) processResult.getData()).get("done");
                            if(finished) {
                                ArrayList deviceObjects = (ArrayList)data.get("deviceObjects");
                                //object beans
                                String address = data.get("address").toString();
                                String nodeId = data.get("nodeId").toString();
                                List<DataPointModel> dataPointModels = new ArrayList<DataPointModel>();
                                for (Object deviceObject : deviceObjects) {
                                    LinkedHashMap deviceMap = (LinkedHashMap)deviceObject;
                                    LOG.info(deviceMap.toString());
                                    dataPointModels.add(createDataPoint(
                                            dataSource.getXid(),
                                            nodeId,
                                            "zigbee_from_api1",
                                            address,
                                            nodeId,
                                            deviceMap));
                                }
                                List<DataPointModel> dataPointModelsSaved = api.saveDataPoints(dataPointModels);
                                LOG.info(dataPointModelsSaved.toString());
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
    @Ignore
    public void _03_testDeleteDataSource() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        DataSourceModel dataSourceModel = api.deleteDataSource("DS_465469");
        Assert.assertNotNull(dataSourceModel);
        LOG.info(dataSourceModel.toString());
    }

    /**
     * Create a Data Point from object bean retrieved from discovery process
     * @param dataSourceXid
     * @param dataSourceName
     * @param deviceName
     * @param address
     * @param nodeIdentifier
     * @param deviceObject
     * @return
     */
    private DataPointModel createDataPoint(
            String dataSourceXid,
            String dataSourceName,
            String deviceName,
            String address,
            String nodeIdentifier,
            LinkedHashMap deviceObject) {

        DataPointModel dp = new DataPointModel();

        dp.setDataSourceXid(dataSourceXid);
        dp.setDataSourceName(dataSourceName);
        dp.setDeviceName(deviceName);

        dp.setEnabled(true);
        dp.setUseIntegralUnit(false);
        dp.setUseRenderedUnit(false);
        dp.setIntegralUnit("s");
        dp.setModelType("DATA_POINT");

        if(deviceObject.get("typeId").equals(3)) {
            dp.setName(address + "@MESSAGE");
        }
        else {
            dp.setName(address + "@" + deviceObject.get("ioLine"));
        }
        dp.setId(-1);

        PointLocatorZigBeeModel pl = new PointLocatorZigBeeModel();
        pl.setAddress(address);
        pl.setNodeIdentifier(nodeIdentifier);
        pl.setIoLine((Integer) deviceObject.get("ioLineId"));
        pl.setIoMode((Integer) deviceObject.get("ioModeId"));
        pl.setType((Integer) deviceObject.get("typeId"));

        pl.setDataType(PointLocatorModel.DataTypeEnum.convertTo((Integer)deviceObject.get("dataTypeId")));
        pl.setUseCovSubscription((Boolean) deviceObject.get("cov"));
        pl.setSettable(false);

        dp.setPointLocator(pl);
        return dp;
    }

}
