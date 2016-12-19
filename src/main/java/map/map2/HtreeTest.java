package map.map2;

/**
 * Created by jiang on 2016/12/19 0019.
 */
public class HtreeTest {
    public static void main(String[] args) {
        MHtree htree = new MHtree();
        htree.put("hellp", "ddddd");
        System.out.println(htree.get("hellp"));
        htree.remove("hellp");
        System.out.println(htree.get("hellp"));
    }
}
