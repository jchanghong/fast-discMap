package map;

import java.util.Map;

/**
 * Created by 长宏 on 2016/11/23 0023.
 */
public class Testmap implements Log {
    Testmap() {
        Map map = new MyDiscMap2("file.map");
//        for (int i = 0; i < 200; i++) {
//            map.put("hello" + i, "hello" + i);
//        }
//        map.remove("hello198");
//        for (int i = 0; i < 200; i++) {
//            Object o = map.get("hello" + i);
//            if (o == null) {
//                break;
//            }
//            log(o.toString());
//        }
//        log(map.keySet().size());

    }

    public static void main(String[] args) {
        new Testmap();
    }
}
