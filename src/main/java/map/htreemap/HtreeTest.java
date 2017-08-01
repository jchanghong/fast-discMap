/*
 *
 *
 *    Created on  17-8-1 下午4:17 by jiang
 *    very fast key value store 简单，快速的键值储存。
 *    特别为小文件储存设计，比如图片文件。
 *    把小文件存数据库中不是理想的选择。存在文件系统中又有太多小文件难管理
 *
 */

/*
 *
 *
 *    Created on  16-12-21 下午9:49 by jiang
 *    very fast key value store 简单，快速的键值储存。
 *    特别为小文件储存设计，比如图片文件。
 *    把小文件存数据库中不是理想的选择。存在文件系统中又有太多小文件难管理
 *
 */

package map.htreemap;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * Created by jiang on 2016/12/19 0019.
 */
public class HtreeTest {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    @Test
    public  void mains() {
        Map<String, Object> map = new HTreeMap();
        for (int i=0;i<50000;i++) {
            map.put(i + "", i);
        }
        for (int i=0;i<5000;i++) {
            Assert.assertEquals(map.get(i + ""), i);
        }
        Assert.assertEquals(map.size(), 50000);
        map.remove("9");
        Assert.assertEquals(map.size(), 49999);
        Assert.assertEquals(map.get("9"), null);
        HTreeMap hTreeMap = (HTreeMap) map;
        System.out.println(hTreeMap.high());

    }

    @Test
    public void testprint() throws Exception {
        HTreeMap map = new HTreeMap();
        for (int i=0;i<20;i++) {
            map.put(i + "", i);
        }
        map.printTree();

    }
}
