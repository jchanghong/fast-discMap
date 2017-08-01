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

/**
 * Created by 长宏 on 2016/11/23 0023.
 */
public interface Log {
    /**
     * Log.
     *
     * @param s the s
     */
    default void log(Object s) {
        System.out.println(s.toString());
    }
}
