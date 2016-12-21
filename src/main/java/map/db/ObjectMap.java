package map.db;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiang on 2016/12/20 0020.
 * 内存对象和磁盘index的map,用来更新磁盘对象
 */
public class ObjectMap {
    /**
     * The constant map.
     */
    public static Map<Integer, Object> map = new HashMap<>();

    /**
     * Clear.
     */
    public static void clear() {
        map.clear();
    }

    /**
     * Gets .
     *
     * @param o the o
     * @return the
     */
    public static int getindex(Object o) {
        for (Map.Entry<Integer, Object> entry :
                map.entrySet()) {
            if (entry.getValue()==o) {
                return entry.getKey();
            }
        }
        return -1;
    }

    /**
     * Putorupdate.
     *
     * @param o         the o
     * @param pageindex the pageindex
     */
    public static void putorupdate(Object o, int pageindex) {
        map.put(pageindex, o);
    }

    /**
     * Gets .
     *
     * @param pageindex the pageindex
     * @return the
     */
    public static Object getobject(int pageindex) {
        return map.get(pageindex);
    }
}
