package map;
import static org.junit.Assert.*;
import javafx.scene.media.VideoTrack;
import map.db.DB;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mapdb.DBMaker;

import java.util.Map;

/**
 * Created by jiang on 2016/12/21 0021.
 * 测试表示平均查找性能是mapdb的7倍左右
 */
public class dbcomare {
    static DB db = DB.getInstance("d");
    static org.mapdb.DB db2;
   static Map<String, Object> map1;
   static Map<String, Object> mapmy;

    @BeforeClass

    public static void setUp() throws Exception {
        db2 = DBMaker.fileDB("d2").make();
        map1 = (Map<String, Object>) db2.treeMap("dd114").createOrOpen();
        mapmy = db.getmap("db114");
    }

    @Test
    public void compare_mapdb() throws Exception {
        for (int i = 0; i < 5000; i++) {
            assertEquals(i, map1.get("dddd" + i));
        }
    }

    @Test
    public void compare_my() throws Exception {
        for (int i = 0; i < 5000; i++) {
            assertEquals(i, mapmy.get("fff" + i));
        }
    }

    @Test
    public void insert() throws Exception {
        for (int i = 0; i < 5000; i++) {
            mapmy.put("fff" + i, i);
//            map1.put("dddd" + i, i);
        }
//        db2.close();
    }

    @Test
    public void remove_db() throws Exception {
        for (int i = 0; i < 5000; i++) {
            map1.remove("dddd" + i);
        }
        db2.close();
    }

    @Test
    public void remove_my() throws Exception {
        for (int i = 0; i < 5000; i++) {
            mapmy.remove("dddd" + i);
        }
    }
}
