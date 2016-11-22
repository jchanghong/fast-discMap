package map;

import java.util.Set;

/**
 * Created by 长宏 on 2016/11/21 0021.
 */
public interface MMap {
    /**
     * 假为错误或者更改以前的值*/
    boolean put(String ket, Object v);
    Object get(String key);
    Set<String> keySet();

    int size();
}
