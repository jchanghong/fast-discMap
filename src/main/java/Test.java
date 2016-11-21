import map.MMap;
import map.MyMapImpl;
import map.test.TestObject;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by 长宏 on 2016/11/21 0021.
 */
public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("ABCDEa123abc".hashCode());  // 165374702
        System.out.println("ABCDFB123abc".hashCode());// 165374702
        for (int i = 0; i < 10; i++) {
            testmymap();
        }
        for (int i = 0; i < 10; i++) {
//            testMapDB();
        }

    }

    private static void testmymap() {
        long s = System.currentTimeMillis();
        TestObject object = new TestObject("金香1");
        MMap mMap = new MyMapImpl("changhong.txt");
//        mMap.put("hello1", object);
        mMap.put("ABCDEa123abc", "a123");
        mMap.put("ABCDFB123abc", "B123");

        System.out.println("values is:" + mMap.get("ABCDEa123abc") + "    我的 time is:" + (System.currentTimeMillis() - s));
        System.out.println("values is:" + mMap.get("ABCDFB123abc") + "    我的 time is:" + (System.currentTimeMillis() - s));
    }

    private static void testMapDB() {
        long s = System.currentTimeMillis();
        DB db = DBMaker.fileDB("file.db").make();
        ConcurrentMap map = db.hashMap("map").createOrOpen();
//        map.put("something", "here");
        System.out.println("values is:" + map.get("something") + "      MapDB time is:" + (System.currentTimeMillis() - s));
        db.close();

    }

}
