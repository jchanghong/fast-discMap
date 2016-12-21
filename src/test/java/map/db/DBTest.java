package map.db;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by jiang on 2016/12/21 0021.
 */
public class DBTest {
    DB db = DB.getInstance("d");
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void getmap() throws Exception {

    }

    @Test
    public void createmap() throws Exception {
        System.out.println(Pagesize.max_page_number);
        Map<String, Object> map = db.createorGetmap("db166");
        map.put("11", "11");
        map.put("22", "22");
        assertEquals("11", map.get("11"));
        map.remove("11");
        assertEquals(null, map.get("11"));
        assertEquals("22", map.get("22"));
        System.out.println(map.size());

    }

}