package map.db;

import java.util.Map;

/**
 * Created by jiang on 2016/12/21 0021.
 * 这个项目怎么用？？？
 * 就这么简单
 */
public class demoAPP {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        DB db = DB.getInstance("d");
        Map<String, Object> map = db.createorGetmap("db166");
        map.put("11", "11");
        map.put("22", "22");
        map.remove("11");
        map.put("11", "11");
        System.out.println(map.get("11"));
    }
}
