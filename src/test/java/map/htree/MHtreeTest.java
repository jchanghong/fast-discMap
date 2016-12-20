package map.htree;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jiang on 2016/12/19 0019.
 */
@SuppressWarnings("Duplicates")
public class MHtreeTest {
    MHtree mHtree = new MHtree();
    @Before
    public void setUp() throws Exception {
        mHtree = new MHtree();
    }

    @Test
    public void size() throws Exception {
        mHtree.put("11", "11");
        assertEquals(1, mHtree.size());
        mHtree.put("dddd", "ddd");
        assertEquals(2, mHtree.size());
    }

    @Test
    public void isEmpty() throws Exception {

        assertEquals(true, mHtree.isEmpty());
        mHtree.put("ddd", "dd");
        assertEquals(false, mHtree.isEmpty());
    }

    @Test
    public void containsKey() throws Exception {

        assertEquals(false, mHtree.containsKey("h"));
        mHtree.put("h", "");
        assertEquals(true, mHtree.containsKey("h"));
    }

    @Test
    public void containsValue() throws Exception {
        assertEquals(false, mHtree.containsValue("h"));
        mHtree.put("h", "h");
        assertEquals(true, mHtree.containsValue("h"));

    }

    @Test
    public void get() throws Exception {

    }

    @Test
    public void put() throws Exception {

    }

    @Test
    public void remove() throws Exception {

    }

    @Test
    public void putAll() throws Exception {

    }

    @Test
    public void clear() throws Exception {

    }

    @Test
    public void keySet() throws Exception {

    }

    @Test
    public void values() throws Exception {

    }

    @Test
    public void entrySet() throws Exception {

    }

    @Test
    public void toString1() throws Exception {

    }

    @Test
    public void test3() {
        MHtree htree = new MHtree();
        htree.put("AaAa", "111");
        htree.put("BBBB", "222");
        htree.put("AaBB", "333");
        htree.put("BBAa", "444");
        assertEquals("111",htree.get("AaAa"));
        assertEquals("222",htree.get("BBBB"));
        assertEquals("333",htree.get("AaBB"));
        assertEquals("444",htree.get("BBAa"));
        htree.remove("BBAa");
        assertEquals(null,htree.get("BBAa"));
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
        System.out.println("A:" + ((int)'A'));
        System.out.println("B:" + ((int)'B'));
        System.out.println("a:" + ((int)'a'));

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
        return i+"";
    }

}