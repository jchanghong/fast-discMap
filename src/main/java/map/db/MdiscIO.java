package map.db;

/**
 * Created by jiang on 2016/12/19 0019.
 */
public interface MdiscIO {

    short pagemagic = 111;//每个页面开始的2个字节

    /**
     * Write int.
     *
     * @param o the o
     * @return the int 记录逻辑id
     */
    int write(Object o);

    int update(Object o, int recid);

    Object read(int id);
}
