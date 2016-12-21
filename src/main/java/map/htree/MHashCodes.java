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
            for (int j = 2; j <= i / 2; j++) {
                if (i % j == 0) {
                    isgood = false;
                    break;
                }
            }
            if (isgood) {
                integers.add(i);
            }
        }
        integers.sort(Comparator.reverseOrder());
        integers.stream().limit(10).forEach(System.out::println);
    }
}
