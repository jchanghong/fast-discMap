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

/**
 * Created by jiang on 2016/12/19 0019.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Compare2 {
    public static void main(String[] args) {
        MHtree my = new MHtree();
        int sum = 10000;
        for (int i = 0; i < sum; i++) {
            my.put(i + "asdf", i + "");
        }
        long l = System.nanoTime();
        my.get("500asdf");
        System.out.println(System.nanoTime() - l);


        l = System.nanoTime();
        for (int i = 0; i < sum; i++) {
            MHtreeNode node = MHtree.nodes.get(i);
            if (node.hasV && node.key.equals("5000asdf")) {
                break;
            }
        }

        System.out.println(System.nanoTime() - l);


    }
}
