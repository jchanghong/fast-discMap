package map.htree;

/**
 * Created by jiang on 2016/12/19 0019.
 */
public class HtreeTest {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        MHtree htree = new MHtree();
        htree.put("hellp", "ddddd");
        System.out.println(htree.get("hellp"));
        htree.remove("hellp");
        System.out.println(htree.get("hellp"));
    }
}
