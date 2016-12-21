package map.db;

import com.sun.org.apache.bcel.internal.generic.RET;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiang on 2016/12/20 0020.
 * 内存对象和磁盘index的map,用来更新磁盘对象
 */
public class ObjectMap {
    private static Map<Object, Integer> map = new HashMap<>();
    public static void clear() {
        map.clear();
    }

    public static int getindex(Object o) {
        Integer integer = map.get(o);
        if (integer == null) {
            return -1;
        }
        else {
            return integer;
        }
    }
    public static void putorupdate(Object o, int pageindex) {
        map.put(o, pageindex);
    }
    public static Object getobject(int pageindex) {
        for (Map.Entry<Object,Integer> entry:
        map.entrySet()) {
            if (entry.getValue()==pageindex) {
                return entry.getKey();
            }
        }
        return null;
    }
}
