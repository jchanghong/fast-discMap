/*
 *
 *
 *    Created on  17-8-1 下午4:08 by jiang
 *    very fast key value store 简单，快速的键值储存。
 *    特别为小文件储存设计，比如图片文件。
 *    把小文件存数据库中不是理想的选择。存在文件系统中又有太多小文件难管理
 *
 */

package map.test;

import map.db.ObjectSeriaer;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: jiang
 * \* Date: 2017/8/1 0001
 * \* Time: 16:08
 * \
 */
public class TestBean {
    public int anInt=1;
    public static void main(String[] args) {
        TestBean testBean = new TestBean();
        testBean.anInt=3;
        byte[] bytes = ObjectSeriaer.getbytes(testBean);
        System.out.println(bytes.length);

        TestBean testBean1 = ObjectSeriaer.getObject(bytes);
        System.out.println(testBean1.anInt);

    }
}
