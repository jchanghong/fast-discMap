package test;

import map.MyDiscMap2;
import map.MyDiscMap3;
import map.TestObject;
import org.junit.Assert;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by jiang on 2016/11/23 0023.
 */
public class Test {
    static Map<String, Object> map;
    static MyDiscMap3 map3;
  static   DB db = DBMaker.fileDB("file.db").make();
  static   ConcurrentMap mapdb = db.hashMap("map1").createOrOpen();

    @org.junit.BeforeClass
    public static void init() {
        map = new MyDiscMap2("file.map");
        map3 = new MyDiscMap3("file3.map");

    }

    @org.junit.Test
    public void testMymap2() {
        TestObject ob = new TestObject();
        for (int i = 0; i < 2000; i++) {
            ob.id = i;
            ob.name = "changhong" + i;
            map.put("hello" + i, ob);
            TestObject o = (TestObject) map.get("hello" + i);
            Assert.assertTrue(o.name.equals("changhong" + i));
            Assert.assertTrue(o.id == i);

        }
    }
    @org.junit.Test
    public void testmyMap3() {
        for (int i = 0; i < 2000; i++) {
            TestObject ob = new TestObject();
            ob.id = i;
            ob.name = "changhong" + i;
            map3.put("hello" + i, ob);
            TestObject o = (TestObject) map3.get("hello" + i);
            Assert.assertTrue(o.name.equals("changhong" + i));
            Assert.assertTrue(o.id == i);
        }
        map3.close();
    }
    @org.junit.Test
    public void testMapDB() {
        TestObject ob = new TestObject();
        for (int i = 0; i < 2000; i++) {
            ob.id = i;
            ob.name = "changhong" + i;
            mapdb.put("hello" + i, ob);
            TestObject o = (TestObject) mapdb.get("hello" + i);
            Assert.assertTrue(o.name.equals("changhong" + i));
            Assert.assertTrue(o.id == i);

        }
        db.close();
    }

}
