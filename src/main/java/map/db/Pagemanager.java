/*
 *
 *
 *    Created on  16-12-21 下午9:49 by jiang
 *    very fast key value store 简单，快速的键值储存。
 *    特别为小文件储存设计，比如图片文件。
 *    把小文件存数据库中不是理想的选择。存在文件系统中又有太多小文件难管理
 *
 */

package map.db;

import java.io.IOException;

/**
 * Created by jiang on 2016/12/19 0019.
 * 页面分配
 */
public class Pagemanager {
    /**
     * The Storage.
     */
    MStorage storage;

    /**
     * Instantiates a new Pagemanager.
     *
     * @param storage the storage
     */
    public Pagemanager(MStorage storage) {
        this.storage = storage;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {
        Pagemanager pagemanager = new Pagemanager(MStorage.getInstance("d"));
        int[] getfreepanages = pagemanager.getfreepanages(4089);
        System.out.println(getfreepanages[0]);
        System.out.println(getfreepanages.length);


    }

    /**
     * Getfreepanages int [ ].
     *
     * @param recsize the recsize记录大小
     * @return the int [ ] 从0开始的pageindex
     */
//0开始
    public int[] getfreepanages(int recsize) {
        int sum = (recsize - 1) / Pagesize.page_size_for_content + 1;
        int[] ints = new int[sum];
        int index = 0;
        for (int i = 512; i < Pagesize.max_page_number; i++) {
            if (!MStorage.bitArray.get(i)) {
                ints[index++] = i;
                MStorage.bitArray.set(i, true);
                if (index >= sum) {
                    break;
                }
            }
        }
        return ints;
    }

}
