package map.db;

/**
 * Created by jiang on 2016/12/19 0019.
 * 伙伴算法内存分配
 */
public interface Pagesize {
    long Max_file_size = 1l << 32;
    int page_size = 1024*4;
    int max_page_number = (int) (Max_file_size/ page_size);
    int headsize_in_byte = max_page_number / 8;//需要多少个字节存bit map
    short pagehead_tree = 1;
    short pagehead_node = 2;
    short pagehead_root = 2;
}
