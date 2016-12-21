package map.db;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiang on 2016/12/20 0020.
 * 内存对象和磁盘index的map,用来更新磁盘对象
 */
public class ObjectMap {
    private static Map<Integer, Object> map = new HashMap<>();

    public static void clear() {
        map.clear();
    }

    public static int getindex(Object o) {
        for (Map.Entry<Integer, Object> entry :
                map.entrySet()) {
            if (entry.getValue().equals(o)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public static void putorupdate(Object o, int pageindex) {
        map.put(pageindex, o);
    }

    public static Object getobject(int pageindex) {
        return map.get(pageindex);
    }
}
