package map.db;

import map.htree.MHtree;
import map.htree.MHtreeNode;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by jiang on 2016/12/19 0019.
 */
@SuppressWarnings("ControlFlowStatementWithoutBraces")
public class DiscIO implements MdiscIO {
    MStorage storage;
    Pagemanager pagemanager;
    public DiscIO(MStorage storage) {
        this.storage = storage;
        pagemanager = new Pagemanager(storage);
    }

    /**
     * Write int.
     *
     * @param o the o
     * @return the int 物理页面号！！！
     */
    @Override
    public int write(Object o) {

        byte[] bytes = ObjectSeriaer.getbytes(o);
        int[] pages = pagemanager.getfreepanages(bytes.length);
        if (pages.length == 1) {
            try {
                ByteBuffer buffer = storage.read(pages[0]);
                if (o instanceof MHtree)
                    buffer.putShort(Pagesize.pagehead_tree);
                else {
                    buffer.putShort(Pagesize.pagehead_node);
                }
                buffer.put(bytes);
                return pages[0];
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            //
        }
        return 0;
    }

    @Override
    public int update(Object o, int recid) {
        return 0;
    }

    @Override
    public Object read(int id) {
        try {
            ByteBuffer buffer = storage.read(id);
            short type = buffer.getShort();
            byte[] buff = new byte[buffer.remaining()];
            buffer.get(buff);
            MHtreeNode mHtreeNode = null;
            if (type == Pagesize.pagehead_node) {
                mHtreeNode = ObjectSeriaer.geto(buff);
                return mHtreeNode;
            }
            return ObjectSeriaer.geto(buff);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        DiscIO discIO = new DiscIO(new MStorage("d"));
        MHtreeNode node = new MHtreeNode(1, "d", "dd");
        int i = discIO.write(node);
        System.out.println(i);
        node = (MHtreeNode) discIO.read(i);
        System.out.println(node.values);
    }
}
