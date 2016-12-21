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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by jiang on 2016/12/21 0021.
 */
public class DHtreeTest {
    static DB db;
    DHtree dHtree;

    @Before
    public void setUp() throws Exception {
        db = DB.getInstance("d");
        dHtree = (DHtree) db.createorGetmap("mymap");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void get() throws Exception {

        System.out.println(dHtree.get("111"));
        System.out.println(dHtree.get("222"));

    }

    @Test
    public void put() throws Exception {
        dHtree.put("111", "111");
        dHtree.put("222", "222");
    }

}