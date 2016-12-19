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

    int update(Object o, int recid);

    Object read(int id);
}
