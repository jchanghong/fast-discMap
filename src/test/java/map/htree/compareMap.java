package map.htree;

import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jiang on 2016/12/19 0019.
 * 测试不同集合的查找速度
 */
public class compareMap {
  static   Map<String, Object> my = new MHtree();
  static   Map<String, Object> map = new HashMap<>();
  static   Map<String, Object> maptree = new TreeMap<>();
  static   Map<String, Object> mapconcur = new ConcurrentHashMap<>();
  static   List<String> list = new ArrayList<>();
  static   int sum = 10000;

    public static void main(String[] args) throws Exception {
        long st = System.nanoTime();
        for (int i = 0; i < sum; i++) {
            Collections.binarySearch(list, i + "asdf");
        }
        System.out.println(System.nanoTime()-st);

    }

    @Before
    public void setUp() throws Exception {
        for (int i = 0; i < sum; i++) {
            my.put(i + "asdf", i + "");
            map.put(i + "asdf", i + "");
            maptree.put(i + "asdf", i + "");
            mapconcur.put(i + "asdf", i + "");
            list.add(i + "asdf");
        }
        list.sort(null);

    }

    @Test
    public void testlist() throws Exception {
        for (int i = 0; i < sum; i++) {
            Collections.binarySearch(list, i + "asdf");
//            assertEquals(Collections.binarySearch(list,i+"asdf")>=0,true);
        }
    }

    @Test
    public void testmy() throws Exception {
        for (int i = 0; i < sum; i++) {
            my.get(i + "asdf");
//            assertEquals(i + "", my.get(i + "asdf"));
        }


    }

    @Test
    public void testhashmap() {
        for (int i = 0; i < sum; i++) {
            map.get(i + "asdf");
//            assertEquals(i + "", map.get(i + "asdf"));
        }
    }


    @Test

    public void testconc() throws Exception {
        for (int i = 0; i < sum; i++) {
            mapconcur.get(i + "asdf");
//            assertEquals(i + "", mapconcur.get(i + "asdf"));
        }

    }

    @Test
    public void testtreemap() throws Exception {
        for (int i = 0; i < sum; i++) {
            maptree.get(i + "asdf");
//            assertEquals(i + "", maptree.get(i + "asdf"));
        }

    }
}
