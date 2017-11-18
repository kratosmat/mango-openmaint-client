package it.storelink.mango.client;

import it.storelink.mango.ApiException;
import it.storelink.mango.model.PointFolderHierarchyModel;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

/**
 * Created by kratoslink on 12/03/16.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PointHierarchyTest extends MangoBaseTest {

    @Test
    public void _01_test() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        PointFolderHierarchyModel pointFolderHierarchyModel = api.getPointHierarchy();
        Assert.assertNotNull(pointFolderHierarchyModel);
        LOG.info(pointFolderHierarchyModel.toString());
    }

    @Test
    public void _02_test() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        PointFolderHierarchyModel pointFolderHierarchyModel = api.getFolderById(0);
        Assert.assertNotNull(pointFolderHierarchyModel);
        LOG.info(pointFolderHierarchyModel.toString());
    }

    @Test
    public void _03_test() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        PointFolderHierarchyModel pointFolderHierarchyModel = api.getFolderByName("Root");
        Assert.assertNotNull(pointFolderHierarchyModel);
        LOG.info(pointFolderHierarchyModel.toString());
    }

    @Test
    public void _04_test() throws ApiException, InterruptedException {
        if(!initialized) return;
        Thread.sleep(1000);
        List<String> strings = api.getPath("DP_304268");
        Assert.assertNotNull(strings);
        Assert.assertTrue(strings.size()>0);
        LOG.info(strings.toString());
    }
}
