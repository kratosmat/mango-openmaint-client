package it.storelink.mango.api;

import it.storelink.mango.ApiException;
import it.storelink.mango.model.DataSourceModbusIpModel;
import it.storelink.mango.model.DataSourceModel;
import it.storelink.mango.model.ModbusIpRequestBean;
import it.storelink.mango.model.ModbusSerialRequestBean;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.fail;

/**
 * Created by kratoslink on 05/08/16.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DiscoveryModbusTest extends MangoBaseTest {

    protected static Logger LOG = LoggerFactory.getLogger(DiscoveryModbusTest.class);

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
            DataSourceModel dataSource = api.getDataSource("DS_998877");

            //send zigbee whois
            ModbusIpRequestBean requestBean = new ModbusIpRequestBean();
            requestBean.setHost("192.168.0.68");
            requestBean.setPort(502);
            requestBean.setTimeout(500);
            api.modbusIpScan(requestBean);

            boolean finished = false;
            while (!finished) {
                Thread.sleep(2000);
                // send bacnet whois update while it finds devices
                Map<String, Object> map = api.modbusScanUpdate();
                List<Object> freshDevices = (List<Object>) map.get("devices");
                if (map.containsKey("finished")) {
                    finished = (Boolean)map.get("finished");
                }
            }
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

    /**
     * Start discovery process and create data points
     * @throws ApiException
     * @throws InterruptedException
     */
    @Test
    public void _03_testDiscoverySerial() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        try {
            //get the data source created before
            DataSourceModel dataSource = api.getDataSource("DS_998877");

            //send zigbee whois
            ModbusSerialRequestBean requestBean = new ModbusSerialRequestBean();
            requestBean.setCommPortId("/dev/ttyUSB0");
            requestBean.setBaudRate(9600);
            requestBean.setTimeout(500);
            api.modbusSerialScan(requestBean);

            boolean finished = false;
            while (!finished) {
                Thread.sleep(2000);
                // send bacnet whois update while it finds devices
                Map<String, Object> map = api.modbusScanUpdate();
                List<Object> freshDevices = (List<Object>) map.get("devices");
                if (map.containsKey("finished")) {
                    finished = (Boolean)map.get("finished");
                }
            }
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
            fail(e.getMessage());
        }
    }

}
