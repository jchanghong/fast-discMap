/*
 *
 *
 *    Created on  17-8-1 下午4:17 by jiang
 *    very fast key value store 简单，快速的键值储存。
 *    特别为小文件储存设计，比如图片文件。
 *    把小文件存数据库中不是理想的选择。存在文件系统中又有太多小文件难管理
 *
 */

/*
 *
 *
 *    Created on  16-12-21 下午9:49 by jiang
 *    very fast key value store 简单，快速的键值储存。
 *    特别为小文件储存设计，比如图片文件。
 *    把小文件存数据库中不是理想的选择。存在文件系统中又有太多小文件难管理
 *
 */

package map.htreemap;

import map.util.Primenumber;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jiang on 2016/12/19 0019.
 */
public class HashCodes {
    /**
     * The constant codes.
     */
    public static int[] codes;
    static {
       codes= Primenumber.getPrimes(1000).stream().filter(a -> a > 10 && a < 100).mapToInt(Integer::intValue).toArray();
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

        BigInteger integer = BigInteger.ONE;
        for (int a :
                codes) {
            System.out.println(a);
            integer = integer.multiply(BigInteger.valueOf(a));
        }
        System.out.println(integer.toString());
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Long.MAX_VALUE);

    }
}
