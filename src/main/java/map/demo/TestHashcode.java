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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 长宏 on 2016/11/23 0023.
 */
public class TestHashcode {
    /**
     * The Hash number.
     */
    static Map<Integer, Integer> hash_number = new HashMap<>();
    /**
     * The Chars.
     */
    static char[] chars = {'a', 'z', 'c', 'd', 'e', 'f', 'g', 'h', 'q', 'e', 'r', 't', 'u', 'i', 'k', 'm'};
    /**
     * The Num.
     */
    static int num = chars.length;

    /**
     * Gets .
     *
     * @param key the key
     * @return the
     */
    static int getcode(String key) {
        int hashindex = (Math.abs(key.hashCode())) % 997 + 3;
        if (hash_number.containsKey(hashindex)) {
            hash_number.put(hashindex, hash_number.get(hashindex) + 1);
        } else {
            hash_number.put(hashindex, 1);
        }
        return hashindex;
    }

    /**
     * Randstring string.
     *
     * @return the string
     */
    static String randstring() {
        int c = (int) (Math.random() * 20);
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < c; i++) {
            b.append(randchar());
        }
        return b.toString();
    }

    /**
     * Randchar char.
     *
     * @return the char
     */
    static char randchar() {
        int c = (int) (Math.random() * num);
        return chars[c];
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
//            System.out.println(getcode(randstring()));
            getcode(randstring());
        }
        System.out.println(hash_number.keySet().size());//不突然的数量
        //noinspection OptionalGetWithoutIsPresent
        System.out.println(hash_number.values().stream().mapToInt(a -> a).max().getAsInt());//最长的数

    }
}
