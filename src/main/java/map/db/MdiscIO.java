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

/**
 * Created by jiang on 2016/12/19 0019.
 */
public interface MdiscIO {


    /**
     * Write int.
     *
     * @param o the o
     * @return the int 物理页面号！！！
     */
    int write(Object o);

    /**
     * Update int.
     *
     * @param o     the o
     * @param recid the recid
     * @return the int
     */
    int update(Object o, int recid);

    /**
     * Read t.
     *
     * @param <t> the type parameter
     * @param id  the id
     * @return the t
     */
    <t> t read(int id);
}
