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
    <t>  t read(int id);
}
