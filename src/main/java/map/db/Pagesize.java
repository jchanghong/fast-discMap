package map.db;

/**
 * Created by jiang on 2016/12/19 0019.
 * 伙伴算法内存分配
 */
public interface Pagesize {
    int M1 = 1024*4;
    int M2 = M1 *2;
    int M3 = M2 *2;
    int M4 = M3 *2;
    int M5 = M4 *2;
    int M6 = M5 *2;
    int M7 = M6 *2;
    int MAXPAGENUMBER = 1048576;
    short pagehead_tree = 1;
    short pagehead_node = 2;
}
