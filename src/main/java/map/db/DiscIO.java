package map.db;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by jiang on 2016/12/19 0019.
 * 和内存指针差不多
 * new 后得到地址,这里地址是page index
 * 每个页面开始分别是type，记录大小，数据，页面后2个字节用来连接每个页面
 */
@SuppressWarnings("ControlFlowStatementWithoutBraces")
public class DiscIO implements MdiscIO {
    private static DiscIO instn = null;

    public MStorage getStorage() {
        return storage;
    }

    public Pagemanager getPagemanager() {
        return pagemanager;
    }

    public static DiscIO getInstance(String filename) {
        if (instn == null) {
            instn = new DiscIO(filename);
        }
        return instn;
    }

    private DiscIO(String filename) {
        this(MStorage.getInstance(filename));
        this.filename = filename;
    }
    String filename;

    MStorage storage;
    Pagemanager pagemanager;

    private DiscIO(MStorage storage) {
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
        System.out.println("页面地址：" + pages[0] + "   大小：" + bytes.length);
        if (pages.length == 1) {
            try {
                ByteBuffer buffer = storage.read(pages[0]);
                if (o instanceof DHtree)
                    buffer.putShort(Pagesize.pagehead_tree);
                else {
                    buffer.putShort(Pagesize.pagehead_node);
                }
                buffer.putInt(bytes.length);
                buffer.put(bytes);
                storage.write(pages[0], buffer);
                return pages[0];
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //
        }
        return 0;
    }

    @Override
    public int update(Object o, int recid) {
        byte[] bytes = ObjectSeriaer.getbytes(o);
        int[] pages = pagemanager.getfreepanages(bytes.length);
        System.out.println("页面地址：" + pages[0] + "   大小：" + bytes.length);
        if (pages.length == 1) {
            try {
                ByteBuffer buffer = storage.read(pages[0]);
                if (o instanceof DHtree)
                    buffer.putShort(Pagesize.pagehead_tree);
                else {
                    buffer.putShort(Pagesize.pagehead_node);
                }
                buffer.putInt(bytes.length);
                buffer.put(bytes);
                storage.write(pages[0], buffer);
                return pages[0];
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //
        }
        return recid;
    }

    @Override
    public <T> T read(int id) {
        try {
            ByteBuffer buffer = storage.read(id);
            short type = buffer.getShort();
            int size = buffer.getInt();
            byte[] buff = new byte[buffer.remaining()];
            buffer.get(buff);
            return ObjectSeriaer.getObject(buff);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        DiscIO discIO = DiscIO.getInstance("d");
        DHtreeNode node = new DHtreeNode(1, "d", "dd66666666666666666666666666666666666666666");
        int i = discIO.write(node);
        System.out.println(i);
        node = discIO.read(i);
        System.out.println(node.values);
        node.values = "Faaaa";
        discIO.update(node, i);
        node = discIO.read(i);
        System.out.println(node.values);
    }
}
