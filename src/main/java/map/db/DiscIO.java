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
    /**
     * The Filename.
     */
    String filename;
    /**
     * The Storage.
     */
    MStorage storage;
    /**
     * The Pagemanager.
     */
    Pagemanager pagemanager;

    private DiscIO(String filename) {
        this(MStorage.getInstance(filename));
        this.filename = filename;
    }

    private DiscIO(MStorage storage) {
        this.storage = storage;
        pagemanager = new Pagemanager(storage);
    }

    /**
     * Gets instance.
     *
     * @param filename the filename
     * @return the instance
     */
    public static DiscIO getInstance(String filename) {
        if (instn == null) {
            instn = new DiscIO(filename);
        }
        return instn;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException the io exception
     */
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

    /**
     * Gets storage.
     *
     * @return the storage
     */
    public MStorage getStorage() {
        return storage;
    }

    /**
     * Gets pagemanager.
     *
     * @return the pagemanager
     */
    public Pagemanager getPagemanager() {
        return pagemanager;
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
        System.out.println("写页面地址：" + pages[0] + "   大小：" + bytes.length);
        if (pages.length == 1) {
            try {
                ByteBuffer buffer = storage.read(pages[0]);
                if (o instanceof DHtree)
                    buffer.putShort(Pagesize.pagehead_tree);
                else if (o instanceof DHtreeNode) {
                    buffer.putShort(Pagesize.pagehead_node);
                } else {
                    buffer.putShort(Pagesize.pagehead_other);
                }
                buffer.putInt(bytes.length);
                buffer.put(bytes);
                storage.write(pages[0], buffer);
                ObjectMap.putorupdate(o, pages[0]);
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
        if (!ObjectMap.map.containsKey(recid)) {
            return recid;
        }
        byte[] bytes = ObjectSeriaer.getbytes(o);
//        System.out.println("更新页面地址：" + recid + "   大小：" + bytes.length);
        try {
            ByteBuffer buffer = storage.read(recid);
            if (o instanceof DHtree)
                buffer.putShort(Pagesize.pagehead_tree);
            else if (o instanceof DHtreeNode) {
                buffer.putShort(Pagesize.pagehead_node);
            } else {
                buffer.putShort(Pagesize.pagehead_other);
            }
            buffer.putInt(bytes.length);
            buffer.put(bytes);
            storage.write(recid, buffer);
            ObjectMap.putorupdate(o, recid);
            return recid;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public <T> T read(int id) {
        try {
            ByteBuffer buffer = storage.read(id);
            short type = buffer.getShort();
            int size = buffer.getInt();
//            System.out.println("得到页面地址：" + id + "   大小：" + size);
            byte[] buff = new byte[size];
            buffer.get(buff);
            T object = ObjectSeriaer.getObject(buff);
            ObjectMap.putorupdate(object, id);
            return object;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
