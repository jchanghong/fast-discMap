/*
 *
 *
 *    Created on  16-12-22 下午12:01 by jiang
 *    very fast key value store 简单，快速的键值储存。
 *    特别为小文件储存设计，比如图片文件。
 *    把小文件存数据库中不是理想的选择。存在文件系统中又有太多小文件难管理
 *
 */

package map.db;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by 长宏 on 2016/12/22 0022.
 * 测试大数据下
 */
public class testbigdata {
    DB db = DB.getInstance("d");
    Map<String, Object> map = db.createorGetmap("bigdata");

    @BeforeClass
  static   public void setUp() throws Exception {

        DB.debug = false;

    }

    @Test
    public void test2() throws Exception {
            person person = (testbigdata.person) map.get("dddd" + 2);
        System.out.println(person.id);
        System.out.println(person.ints.length);

    }

    @Test
    public void testbigdata() throws Exception {
        for (int i = 0; i < 50; i++) {
            person person = (testbigdata.person) map.get("dddd" + i);
            Assert.assertEquals(person.id, i);
        }
    }
    static class person implements Serializable {
        public int id;
        String name;
        int[] ints = new int[50000];
    }
    @Test
    public void insert() throws Exception {
        person person = new person();
        for (int i = 0; i < 50; i++) {
            person.id = i;
            map.put("dddd" + i, person);
        }
    }
}
