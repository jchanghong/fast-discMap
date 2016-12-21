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

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by jiang on 2016/12/20 0020.
 */
@SuppressWarnings("Duplicates")
public class Objectseiabletest {

    static String test;
    static DHtreeNode test1;

    @BeforeClass
    static public void setUp() throws Exception {
        ObjectSeriaer objectSeriaer = new ObjectSeriaer();
//      System.out.println(objectSeriaer);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            builder.append("i" + "ddddddd");
        }
        test = builder.toString();
        test1 = new DHtreeNode(1, "1", "1");
//        test1.childs = new DHtreeNode[1000];
    }

    @Test
    public void testjava() throws Exception {
        for (int i = 0; i < 5000; i++) {
            byte[] getbytes = ObjectSeriaer.getbytes(test1);
            System.out.println(getbytes.length);
//            ObjectSeriaer.geto(getbytes);
        }
    }

    @Test
    public void testkyo() throws Exception {
        DHtreeNode f = null;
        for (int i = 0; i < 50000; i++) {
            byte[] getbytes = ObjectSeriaer.getbytes(test1);
//            System.out.println(getbytes.length);
            f = ObjectSeriaer.getObject(getbytes);
            assertEquals(f.values, test1.values);
        }

    }
}
