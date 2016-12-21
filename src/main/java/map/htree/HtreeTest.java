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
