/*
 *
 *
 *    Created on  17-8-1 上午10:32 by jiang
 *    very fast key value store 简单，快速的键值储存。
 *    特别为小文件储存设计，比如图片文件。
 *    把小文件存数据库中不是理想的选择。存在文件系统中又有太多小文件难管理
 *
 */

package map.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: jiang
 * \* Date: 2017/8/1 0001
 * \* Time: 10:32
 * \
 */
public class Primenumber {

    private static final Logger logger = LoggerFactory.getLogger(Primenumber.class.getName());
    public static void main(String[] args) {
        List<Integer> primes = getPrimes(1000);
        System.out.println("start--------");
        logger.debug(primes.size()+"");
        primes.stream().filter(a -> a > 990 && a < 1100).limit(2).forEach(a -> System.out.println(a));
    }
    public static List<Integer> getPrimes(int numbers) {
        List<Integer> result = new ArrayList<>(numbers);
        if (numbers <=0) {
            return result;
        }
        result.add(2);
        if (result.size() == numbers) {
            return result;
        }
        for (int i=3;i<Integer.MAX_VALUE;i++) {
            int j=2;
            for (;j<i/2+1;j++) {
                if (i % j == 0) {
                    break;
                }
            }
            if (j >= i / 2 + 1) {
                result.add(i);
                if (result.size() == numbers) {
                    return result;
                }
            }
        }
        return result;
    }
}
