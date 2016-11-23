import map.MMap;
import map.MyDiscMap;
import map.MyDiscMap2;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.IOException;
import java.util.Map;
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
            testmymap2();
        }
        for (int i = 0; i < 10; i++) {
            testMapDB();
        }

    }

    private static void testmymap() {
        long s = System.currentTimeMillis();
//        TestObject object = new TestObject("金香1");
        MMap mMap = new MyDiscMap("file1.map");
//        mMap.put("ABCDEa123abc", "a123");
//        mMap.put("ABCDFB123abc", "B123");
        for (int i = 0; i < 20; i++) {
            mMap.put("hello" + i, "hello" + i);
        }
        for (int i = 0; i < 20; i++) {
            mMap.get("hello" + i);
        }
        System.out.println("values is:" + mMap.get("hello10") + "    我的 time is:" + (System.currentTimeMillis() - s));
//        System.out.println("values is:" + mMap.get("ABCDFB123abc") + "    我的 time is:" + (System.currentTimeMillis() - s));
    }
    private static void testmymap2() {
        long s = System.currentTimeMillis();
//        TestObject object = new TestObject("金香1");
        Map<String, Object> mMap = new MyDiscMap2("file.map");
//        mMap.put("ABCDEa123abc", "a123");
//        mMap.put("ABCDFB123abc", "B123");
        for (int i = 0; i < 20; i++) {
            mMap.put("hello" + i, "hello" + i);
        }
        for (int i = 0; i < 20; i++) {
            mMap.get("hello" + i);
        }
        System.out.println("values is:" + mMap.get("hello10") + "    我的2 time is:" + (System.currentTimeMillis() - s));
//        System.out.println("values is:" + mMap.get("ABCDFB123abc") + "    我的 time is:" + (System.currentTimeMillis() - s));
    }

    private static void testMapDB() {
        long s = System.currentTimeMillis();
        DB db = DBMaker.fileDB("file.db").make();
        ConcurrentMap map = db.hashMap("map1").createOrOpen();
        for (int i = 0; i < 20; i++) {
            map.put("hello" + i, "hello" + i);
        }
        for (int i = 0; i < 20; i++) {
            map.get("hello" + i);
        }
        System.out.println("values is:" + map.get("hello10") + "      MapDB time is:" + (System.currentTimeMillis() - s));
        db.close();

    }

}
