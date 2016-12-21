package map.htree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jiang on 2016/12/19 0019.
 */
public class MHashCodes {
    /**
     * The constant codes.
     */
    public static int[] codes = new int[]{997, 991, 983, 977, 971, 967, 953, 947, 941, 937};

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        int sum = 1000;
        List<Integer> integers = new ArrayList<>();
        boolean isgood;
        for (int i = 0; i < sum; i++) {
            isgood = true;
            for (int j = 2; j <=  i/2; j++) {
                if (i % j == 0) {
                    isgood = false;
                    break;
                }
            }
            if (isgood == true) {
                integers.add(i);
            }
        }
        integers.sort(Comparator.reverseOrder());
        integers.stream().limit(10).forEach(System.out::println);
    }
}
