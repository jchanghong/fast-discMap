package map.db;

/**
 * Created by jiang on 2016/12/19 0019.
 */
public class DiscIO implements MdiscIO {
    MStorage storage;

    public DiscIO(MStorage storage) {
        this.storage = storage;
    }

    /**
     * Write int.
     *
     * @param o the o
     * @return the int 物理页面号！！！
     */
    @Override
    public int write(Object o) {
        return 0;
    }

    @Override
    public int update(Object o, int recid) {
        return 0;
    }

    @Override
    public Object read(int id) {
        return null;
    }
}
