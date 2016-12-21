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

import org.junit.Test;

/**
 * Created by jiang on 2016/12/20 0020.
 */
public class PagemanagerTest {
    @Test
    public void testnumber() throws Exception {
        System.out.println(Pagesize.Max_file_size);
        System.out.println(Pagesize.max_page_number);
        double d = Pagesize.max_page_number / 8;
        System.out.println(Pagesize.headsize_in_byte);

    }

    @Test
    public void getfreepanages() throws Exception {

    }

}