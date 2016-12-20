package map.htree;

/**
 * Created by jiang on 2016/12/19 0019.
 */
public class Compare2 {
    public static void main(String[] args) {
        MHtree my = new MHtree();
        int sum=10000;
        for (int i = 0; i < sum; i++) {
            my.put(i + "asdf", i + "");
        }
        long l = System.nanoTime();
        my.get("500asdf");
        System.out.println(System.nanoTime()-l);


        l = System.nanoTime();
        for (int i = 0; i < sum; i++) {
            MHtreeNode node = MHtree.nodes.get(i);
            if (node.hasV && node.key.equals("5000asdf")) {
                break;
            }
        }

        System.out.println(System.nanoTime()-l);


    }
}
