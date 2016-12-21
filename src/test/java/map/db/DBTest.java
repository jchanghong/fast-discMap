/*
 *
 *
 *    Created on  16-12-21 下午9:49 by jiang
 *    very fast key value store 简单，快速的键值储存。
 *    特别为小文件储存设计，比如图片文件。
 *    把小文件存数据库中不是理想的选择。存在文件系统中又有太多小文件难管理
 *
 */

package map.db;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by jiang on 2016/12/21 0021.
 */
public class DBTest {
    DB db = DB.getInstance("d");

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void getmap() throws Exception {

    }

    @Test
    public void createmap() throws Exception {
        Map<String, Object> map = db.createorGetmap("db166");
        map.put("11", "11");
        map.put("22", "22");
        assertEquals("11", map.get("11"));
        map.remove("11");
        assertEquals(null, map.get("11"));
        map.put("11", "11");
        assertEquals("11", map.get("11"));
        assertEquals("22", map.get("22"));
        System.out.println(map.size());

    }

}