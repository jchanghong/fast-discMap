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
 * 一个页面pagesize-2-4-4
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
        if (pages.length == 1) {
            if (DB.debug) {

                System.out.println("写页面地址：" + pages[0] + "   大小：" + bytes.length);
            }
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
            return writemorepage(bytes, pages, o);
        }
        return 0;
    }

    private int writemorepage(byte[] bytes, int[] pages, Object o) {
        int sum = bytes.length;
        int byteindex = 0;
        for (int i = 0; i < pages.length; i++) {
            try {
                ByteBuffer buffer = storage.read(pages[i]);
                if (o instanceof DHtree)
                    buffer.putShort(Pagesize.pagehead_tree);
                else if (o instanceof DHtreeNode) {
                    buffer.putShort(Pagesize.pagehead_node);
                } else {
                    buffer.putShort(Pagesize.pagehead_other);
                }
                buffer.putInt(bytes.length);
                if (i < pages.length - 1) {
                    buffer.put(bytes, byteindex, Pagesize.page_size_for_content);
                    byteindex += Pagesize.page_size_for_content;
                    buffer.position(Pagesize.page_size - 4);
                    buffer.putInt(pages[i + 1]);
                    if (DB.debug) {

                        System.out.println("写页面地址：" + pages[i] + "   大小：" + Pagesize.page_size_for_content);
                    }
                    sum -= Pagesize.page_size_for_content;
                } else {
                    if (sum > 0) {
                        if (DB.debug) {

                            System.out.println("写页面地址：" + pages[i] + "   大小：" + sum);
                        }
                        buffer.put(bytes, byteindex, sum);
                        buffer.position(Pagesize.page_size - 4);
                        buffer.putInt(0);
                        return pages[0];
                    } else {
                        return pages[0];
                    }
                }
                storage.write(pages[i], buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ObjectMap.putorupdate(o, pages[0]);
        return pages[0];
    }

    @Override
    public int update(Object o, int recid) {
        if (!ObjectMap.map.containsKey(recid)) {
            return -1;
        }
        byte[] bytes = ObjectSeriaer.getbytes(o);

        if (bytes.length <= Pagesize.page_size_for_content) {
            if (DB.debug) {

                System.out.println("更新页面地址：" + recid + "   大小：" + bytes.length);
            }
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
        } else {
            updatemoredata(bytes, recid, o);
            return recid;
        }
    }

    private void updatemoredata(byte[] bytes, int recid, Object o) {
        int sum = bytes.length;
        int pageid = recid;
        int byteindex = 0;
        while (sum > 0 && byteindex < bytes.length) {
            try {
                ByteBuffer buffer = storage.read(pageid);
                buffer.putShort(Pagesize.pagehead_node);
                buffer.putInt(bytes.length);
                if (sum >= Pagesize.page_size_for_content) {
                    buffer.put(bytes, byteindex, Pagesize.page_size_for_content);
                    byteindex += Pagesize.page_size_for_content;
                    sum -= Pagesize.page_size_for_content;
                    buffer.position(Pagesize.page_size - 4);
                    pageid = buffer.getInt();
                    if (pageid <= 0) {
                        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
                        byteBuffer.position(byteindex);
                        int[] ps = pagemanager.getfreepanages(sum);
                        buffer.position(Pagesize.page_size - 4);
                        buffer.putInt(ps[0]);
                        byte[] news = new byte[sum];
                        byteBuffer.get(news);
                        writemorepage(news, ps, o);
                        return;
                    }
                } else {
                    if (sum > 0) {
                        buffer.put(bytes, byteindex, sum);
                        buffer.position(Pagesize.page_size - 4);
                        buffer.putInt(0);
                        sum -= sum;
                    } else {
                        return;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public <T> T read(int id) {
        try {
            ByteBuffer buffer = storage.read(id);
            short type = buffer.getShort();
            int size = buffer.getInt();
            byte[] buff = new byte[size];
            if (size <= Pagesize.page_size_for_content) {
                if (DB.debug) {
                    System.out.println("得到页面地址：" + id + "   大小：" + size);

                }
                buffer.get(buff);

            } else {
                buffer.get(buff, 0, Pagesize.page_size_for_content);
                if (DB.debug) {

                    System.out.println("得到页面地址：" + id + "   大小：" + Pagesize.page_size_for_content);
                }
                buffer.position(Pagesize.page_size - 4);
                int nextpage = buffer.getInt();
                readmoredata(nextpage, size - Pagesize.page_size_for_content, buff);
            }
            T object = ObjectSeriaer.getObject(buff);
            ObjectMap.putorupdate(object, id);
            return object;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //下个页面和剩下的大小,
    private void readmoredata(int pagestartid, int size, byte[] buff) {
        int nextpage = pagestartid;
        int remain = size;
        int bytebuffindex = Pagesize.page_size_for_content;
        while (remain > 0 && nextpage > 0 && bytebuffindex < buff.length) {
            try {
                ByteBuffer buffer = storage.read(nextpage);
                short type = buffer.getShort();
                int s = buffer.getInt();
                if (remain > Pagesize.page_size_for_content) {
                    if (DB.debug) {

                        System.out.println("得到页面地址：" + nextpage + "   大小：" + Pagesize.page_size_for_content);
                    }
                    buffer.get(buff, bytebuffindex, Pagesize.page_size_for_content);
                    remain -= Pagesize.page_size_for_content;
                    bytebuffindex += Pagesize.page_size_for_content;
                    buffer.position(Pagesize.page_size - 4);
                    nextpage = buffer.getInt();
                } else {
                    buffer.get(buff, bytebuffindex, remain);
                    if (DB.debug) {

                        System.out.println("得到页面地址：" + nextpage + "   大小：" + remain);
                    }
                    return;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
