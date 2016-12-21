/*
 *
 *
 *    Created on  16-12-21 下午9:49 by jiang
 *    very fast key value store 简单，快速的键值储存。
 *    特别为小文件储存设计，比如图片文件。
 *    把小文件存数据库中不是理想的选择。存在文件系统中又有太多小文件难管理
 *
 */

package map.htree;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by jiang on 2016/12/19 0019.
 */
@SuppressWarnings("Duplicates")
public class HtreeTestTest {

    @Test
    public void test3() {
        MHtree htree = new MHtree();
        htree.put("AaAa", "111");
        htree.put("BBBB", "222");
        htree.put("AaBB", "333");
        htree.put("BBAa", "444");
        assertEquals("111", htree.get("AaAa"));
        assertEquals("222", htree.get("BBBB"));
        assertEquals("333", htree.get("AaBB"));
        assertEquals("444", htree.get("BBAa"));
        htree.remove("BBAa");
        assertEquals(null, htree.get("BBAa"));
    }

    @Test
    public void test1() {

        int sum = 10000;
        MHtree htree = new MHtree();
        for (int i = 0; i < sum; i++) {
            htree.put(i + "asd", i + "");
        }
        for (int i = 0; i < sum; i++) {
            assertEquals("" + i, htree.get(i + "asd"));
        }
        for (int i = 0; i < sum; i++) {
            htree.remove(i + "asd");
            assertEquals(null, htree.get(i + "asd"));
        }
        System.out.println(htree.size());
    }

    @Test
    public void test12() {
        System.out.println("A:" + ((int) 'A'));
        System.out.println("B:" + ((int) 'B'));
        System.out.println("a:" + ((int) 'a'));

        System.out.println(hash("Aa".hashCode()));
        System.out.println(hash("BB".hashCode()));
        System.out.println(hash("Aa".hashCode()));
        System.out.println(hash("BB".hashCode()));


        System.out.println(hash("AaAa".hashCode()));
        System.out.println(hash("BBBB".hashCode()));
        System.out.println(hash("AaBB".hashCode()));
        System.out.println(hash("BBAa".hashCode()));
    }

    private String hash(int i) {
        return i + "";
    }
}