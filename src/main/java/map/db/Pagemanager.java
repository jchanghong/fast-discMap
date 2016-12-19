package map.db;

import sun.security.util.BitArray;

import java.util.*;

/**
 * Created by jiang on 2016/12/19 0019.
 */
public class Pagemanager {
  static   Map<Integer, List<Integer>> map = new HashMap<>();

    public static void main(String[] args) {
        for (int i = 0; i < 7; i++) {
            ArrayList linkedList = new ArrayList();
            for (int j = 0; j < 139; j++) {
                linkedList.add(i + "" + j);
            }
            map.put(i, linkedList);
        }
        System.out.println(ObjectSeriaer.getbytes(map).length);
         long x= (1024 * 1024 * 1024 *4l)>>>12;
        System.out.println(x);
    }

}
