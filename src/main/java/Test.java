import map.MMap;
import map.MMapIpml;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.util.concurrent.ConcurrentMap;

/**
 * Created by 长宏 on 2016/11/21 0021.
 */
public class Test {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            testmymap();
        }
        for (int i = 0; i < 10; i++) {
//            testMapDB();
        }

    }

    private static void testmymap() {
        long s = System.currentTimeMillis();
        MMap mMap = new MMapIpml("changhong.txt");
        mMap.put("something", "here");
        System.out.println("values is:" + mMap.get("something") + "    我的 time is:" + (System.currentTimeMillis() - s));
    }

    private static void testMapDB() {
        long s = System.currentTimeMillis();
        DB db = DBMaker.fileDB("file.db").make();
        ConcurrentMap map = db.hashMap("map").createOrOpen();
        map.put("something", "here");
        System.out.println("values is:" + map.get("something") + "      MapDB time is:" + (System.currentTimeMillis() - s));
        db.close();

    }

}
