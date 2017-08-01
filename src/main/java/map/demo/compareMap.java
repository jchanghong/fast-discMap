/*
 *
 *
 *    Created on  16-12-21 下午9:49 by jiang
 *    very fast key value store 简单，快速的键值储存。
 *    特别为小文件储存设计，比如图片文件。
 *    把小文件存数据库中不是理想的选择。存在文件系统中又有太多小文件难管理
 *
 */

package map.demo;

import map.htreemap.HTreeMap;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jiang on 2016/12/19 0019.
 * 测试不同集合的查找速度
 */
public class compareMap {
    /**
     * The My.
     */
    static Map<String, Object> my = new HTreeMap();
    /**
     * The Map.
     */
    static Map<String, Object> map = new HashMap<>();
    /**
     * The Maptree.
     */
    static Map<String, Object> maptree = new TreeMap<>();
    /**
     * The Mapconcur.
     */
    static Map<String, Object> mapconcur = new ConcurrentHashMap<>();
    /**
     * The List.
     */
    static List<String> list = new ArrayList<>();
    /**
     * The Sum.
     */
    static int sum = 300000;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        setUp();
        long st = System.nanoTime();
        testlist();
        System.out.println((System.nanoTime() - st) / 1000 + "  list");

        st = System.nanoTime();
        testconc();
        System.out.println((System.nanoTime() - st) / 1000 + "  conmap");

        st = System.nanoTime();
        testhashmap();
        System.out.println((System.nanoTime() - st) / 1000 + "  hashmp");

        st = System.nanoTime();
        testmy();
        System.out.println((System.nanoTime() - st) / 1000 + "  tesmy");


        st = System.nanoTime();
        testtreemap();
        System.out.println((System.nanoTime() - st) / 1000 + "  testtreemap");

    }

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    static public void setUp() throws Exception {
        for (int i = 0; i < sum; i++) {
            my.put(i + "asdf", i + "");
            map.put(i + "asdf", i + "");
            maptree.put(i + "asdf", i + "");
            mapconcur.put(i + "asdf", i + "");
            list.add(i + "asdf");
        }
        list.sort(null);

    }

    /**
     * Testlist.
     *
     * @throws Exception the exception
     */
    static public void testlist() throws Exception {
        for (int i = 0; i < sum; i++) {
            Collections.binarySearch(list, i + "asdf");
//            assertEquals(Collections.binarySearch(list,i+"asdf")>=0,true);
        }
    }

    /**
     * Testmy.
     *
     * @throws Exception the exception
     */
    static public void testmy() throws Exception {
        for (int i = 0; i < sum; i++) {
            my.get(i + "asdf");
//            assertEquals(i + "", my.get(i + "asdf"));
        }


    }

    /**
     * Testhashmap.
     */
    static public void testhashmap() {
        for (int i = 0; i < sum; i++) {
            map.get(i + "asdf");
//            assertEquals(i + "", map.get(i + "asdf"));
        }
    }


    /**
     * Testconc.
     *
     * @throws Exception the exception
     */
    static public void testconc() throws Exception {
        for (int i = 0; i < sum; i++) {
            mapconcur.get(i + "asdf");
//            assertEquals(i + "", mapconcur.get(i + "asdf"));
        }

    }

    /**
     * Testtreemap.
     *
     * @throws Exception the exception
     */
    static public void testtreemap() throws Exception {
        for (int i = 0; i < sum; i++) {
            maptree.get(i + "asdf");
//            assertEquals(i + "", maptree.get(i + "asdf"));
        }

    }
}
