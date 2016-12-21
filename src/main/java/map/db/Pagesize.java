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
 * 伙伴算法内存分配
 */
public interface Pagesize {
    /**
     * The constant Max_file_size.
     */
    long Max_file_size = 1L << 32;
    /**
     * The constant page_size.
     */
    int page_size = 1024 * 4;
    /**
     * The constant max_page_number.
     */
    int max_page_number = (int) (Max_file_size / page_size);
    /**
     * The constant headsize_in_byte.
     */
    int headsize_in_byte = max_page_number / 8;//需要多少个字节存bit map
    /**
     * The constant pagehead_tree.
     */
    short pagehead_tree = 1;
    /**
     * The constant pagehead_node.
     */
    short pagehead_node = 2;
    /**
     * The constant pagehead_other.
     */
    short pagehead_other = 2;
}
