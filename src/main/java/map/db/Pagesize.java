package map.db;

/**
 * Created by jiang on 2016/12/19 0019.
 * 伙伴算法内存分配
 */
public interface Pagesize {
    long Max_file_size = 1l << 32;
    int page_size = 1024*4;
    int M2 = page_size *2;
    int M3 = M2 *2;
    int M4 = M3 *2;
    int M5 = M4 *2;
    int M6 = M5 *2;
    int M7 = M6 *2;
    int max_page_number = (int) (Max_file_size/ page_size);
    int headsize = max_page_number / 8;//需要多少个字节存bit map
    short pagehead_tree = 1;
    short pagehead_node = 2;
}
